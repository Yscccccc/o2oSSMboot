package com.ysc.o2o.web.shopadmin;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ysc.o2o.dto.EchartSeries;
import com.ysc.o2o.dto.EchartXAxis;
import com.ysc.o2o.dto.ShopAuthMapExecution;
import com.ysc.o2o.dto.UserProductMapExecution;
import com.ysc.o2o.dto.WechatInfo;
import com.ysc.o2o.entity.PersonInfo;
import com.ysc.o2o.entity.Product;
import com.ysc.o2o.entity.ProductSellDaily;
import com.ysc.o2o.entity.Shop;
import com.ysc.o2o.entity.ShopAuthMap;
import com.ysc.o2o.entity.UserProductMap;
import com.ysc.o2o.entity.WechatAuth;
import com.ysc.o2o.enums.UserProductMapStateEnum;
import com.ysc.o2o.service.ProductSellDailyService;
import com.ysc.o2o.service.ProductService;
import com.ysc.o2o.service.ShopAuthMapService;
import com.ysc.o2o.service.UserProductMapService;
import com.ysc.o2o.service.WechatAuthService;
import com.ysc.o2o.util.HttpServletRequestUtil;
import com.ysc.o2o.util.wechat.WechatUserUtil;
import com.ysc.o2o.util.wechat.message.pojo.UserAccessToken;

@Controller
@RequestMapping("/shopadmin")
public class UserProductManagementController {
	
	@Autowired
	private UserProductMapService userProductMapService;
	
	@Autowired
	private ProductSellDailyService productSellDailyService;
	
	@Autowired
	private WechatAuthService wechatAuthSevice;
	
	@Autowired
	private ShopAuthMapService shopAuthMapService;
	
	@Autowired
	private ProductService productService;
	
	@RequestMapping(value = "/listuserproductmapsbyshop", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listUserProductMapsByShop(
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		
		//获取当前的店铺信息
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		//空值检验，确保shopId不为空
		if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
			//添加查询条件
			UserProductMap userProductMapCondition = new UserProductMap();
			userProductMapCondition.setShop(currentShop);
			String productName = HttpServletRequestUtil.getString(request, "productName");
			if (productName != null) {
				//若前端想按照商品名模糊查询，则传入productName
				Product product = new Product();
				product.setProductName(productName);
				userProductMapCondition.setProduct(product);
			}
			//根据传入的查询条件获取该店铺的商品销售情况
			UserProductMapExecution ue = userProductMapService.listUserProductMap(userProductMapCondition, pageIndex, pageSize);
			modelMap.put("userProductMapList", ue.getUserProductMapList());
			modelMap.put("count", ue.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/listproductselldailyinfobyshop", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listProductSellDailyInfoByShop(
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//获取当前店铺的信息
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		//空值检验，主要确保shopId不为空
		if ((currentShop != null) && (currentShop.getShopId() != null)) {
			//添加查询条件
			ProductSellDaily productSellDailyCondition = new ProductSellDaily();
			productSellDailyCondition.setShop(currentShop);
			Calendar calendar = Calendar.getInstance();
			//获取昨天的日期
			calendar.add(Calendar.DATE, -1);
			Date endTime = calendar.getTime();
			//获取七天前的日期
			calendar.add(Calendar.DATE, -6);
			Date beginTime = calendar.getTime();
			//根据传入的查询条件获取该店铺的商品销售情况
			List<ProductSellDaily> productSellDailyList = productSellDailyService.listProductSellDaily(productSellDailyCondition, beginTime, endTime);
			//指定日期格式
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			//商品名列表，保证唯一性
			HashSet<String> legendData = new HashSet<String>();
			//x轴数据
			TreeSet<String> xData = new TreeSet<String>();
			//定义series
			List<EchartSeries> series = new ArrayList<EchartSeries>();
			//日销量列表
			List<Integer> totalList = new ArrayList<Integer>();
			//当前商品名，默认为空
			String currentProductName = "";
			for (int i = 0; i < productSellDailyList.size(); i++) {
				ProductSellDaily productSellDaily = productSellDailyList.get(i);
				//自动去重
				legendData.add(productSellDaily.getProduct().getProductName());
				xData.add(sdf.format(productSellDaily.getCreateTime()));
				if (!currentProductName.equals(productSellDaily.getProduct().getProductName()) && !currentProductName.isEmpty()) {
					//如果currentProductName不等于获取的商品名，或者已遍历到列表的末尾，且currentProductName
					//则是遍历到下一个商品的日销量信息了，将前一轮遍历的信息放入戴series中，
					//包括了商品名以及商品对应的统计日期以及当日的销量
					EchartSeries es = new EchartSeries();
					es.setName(currentProductName);
					es.setData(totalList.subList(0, totalList.size()));
					series.add(es);
					//重置totalList
					totalList = new ArrayList<Integer>();
					//变换下currentProductId为当前的productId
					currentProductName = productSellDaily.getProduct().getProductName();
					//继续添加新的值
					totalList.add(productSellDaily.getTotal());
				} else {
					//如果还是当前的productId则继续添加新值
					totalList.add(productSellDaily.getTotal());
					currentProductName = productSellDaily.getProduct().getProductName();
				}
				//队列末，需要将最后一个商品销量信息也添加上
				if (i == productSellDailyList.size() - 1) {
					EchartSeries es = new EchartSeries();
					es.setName(currentProductName);
					es.setData(totalList.subList(0, totalList.size()));
					series.add(es);
				}
			}
			modelMap.put("series", series);
			modelMap.put("legendData", legendData);
			//拼接出xAxis
			List<EchartXAxis> xAxis= new ArrayList<EchartXAxis>();
			EchartXAxis exa = new EchartXAxis();
			exa.setData(xData);
			xAxis.add(exa);
			modelMap.put("xAxis", xAxis);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/adduserproductmap", method = RequestMethod.GET)
	@ResponseBody
	private String addUserProductMap(HttpServletRequest request) throws UnsupportedEncodingException {
		//获取微信授权信息
		WechatAuth auth = getOperatorInfo(request);
		if (auth != null) {
			PersonInfo operator = auth.getPersonInfo();
			request.getSession().setAttribute("user", operator);
			
			String qrCodeinfo = URLDecoder.decode(HttpServletRequestUtil.getString(request, "state"), "UTF-8");
			ObjectMapper mapper = new ObjectMapper();
			WechatInfo wechatInfo = null;
			try {
				wechatInfo = mapper.readValue(qrCodeinfo.replace("aaa", "\""), WechatInfo.class);
			} catch (Exception e) {
				return "shop/operationfail";
			}
			//校验二维码是否已过期
			if (!checkQRCodeInfo(wechatInfo)) {
				return "shop/operationfail";
			}
			//获取添加消费记录所需要的参数并组成userproductmap实例
			Long productId = wechatInfo.getProductId();
			Long customerId = wechatInfo.getCustomerId();
			UserProductMap userProductMap = compactUserProductMap4Add(customerId,
					productId, auth.getPersonInfo());
			if (userProductMap != null && customerId != -1) {
				try {
					if (!checkShopAuth(operator.getUserId(), userProductMap)) {
						return "shop/operationfail";
					}
					//添加消费记录
					UserProductMapExecution se = userProductMapService
							.addUserProductMap(userProductMap);
					if (se.getState() == UserProductMapStateEnum.SUCCESS.getState()) {
						return "shop/operationsuccess";
					}
				} catch (RuntimeException e) {
					return "shop/operationfail";
				}
			}
		}
		return "shop/operationfail";
		
	}
	
	/**
	 * 检查扫码人员是否有操作权限
	 * @param userId
	 * @param userProductMap
	 * @return
	 */
	private boolean checkShopAuth(long userId, UserProductMap userProductMap) {
		//获取该店铺的所有授权信息
		ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService
				.listShopAuthMapByShopId(userProductMap.getShop().getShopId(), 1, 1000);
		for (ShopAuthMap shopAuthMap : shopAuthMapExecution
				.getShopAuthMapList()) {
			//看看是否给过该人员进行授权
			if (shopAuthMap.getEmployee().getUserId() == userId) {
				return true;
			}
		}
		return false;
	}
	private boolean checkQRCodeInfo(WechatInfo wechatInfo) {
		if (wechatInfo != null && wechatInfo.getProductId() != null
				&& wechatInfo.getCustomerId() != null
				&& wechatInfo.getCreateTime() != null) {
			long nowTime = System.currentTimeMillis();
			if ((nowTime - wechatInfo.getCreateTime()) <= 5000) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	/**
	 * 根据code获取userAccessToken,进而通过token里的openId获取微信用户信息
	 * @param request
	 * @return
	 */
	private WechatAuth getOperatorInfo(HttpServletRequest request) {
		String code = request.getParameter("code");
		WechatAuth auth = null;
		if (null != code) {
			UserAccessToken token;
			try {
				token = WechatUserUtil.getUserAccessToken(code);
				String openId = token.getOpenId();
				request.getSession().setAttribute("openId", openId);
				auth = wechatAuthSevice.getWechatAuthByOpenId(openId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return auth;
	}
	
	private UserProductMap compactUserProductMap4Add(Long customerId,
			Long productId, PersonInfo operator) {
		UserProductMap userProductMap = null;
		if (customerId != null && productId != null) {
			userProductMap = new UserProductMap();
			PersonInfo customer = new PersonInfo();
			customer.setUserId(customerId);
			Product product = productService.getProductById(productId);
			userProductMap.setProduct(product);
			userProductMap.setShop(product.getShop());
			userProductMap.setUser(customer);
			userProductMap.setPoint(product.getPoint());
			userProductMap.setCreateTime(new Date());
		}
		return userProductMap;
	}
}

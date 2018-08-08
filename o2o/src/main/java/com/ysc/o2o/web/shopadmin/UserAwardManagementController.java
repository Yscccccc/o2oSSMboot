package com.ysc.o2o.web.shopadmin;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ysc.o2o.dto.ShopAuthMapExecution;
import com.ysc.o2o.dto.UserAwardMapExecution;
import com.ysc.o2o.dto.WechatInfo;
import com.ysc.o2o.entity.Award;
import com.ysc.o2o.entity.PersonInfo;
import com.ysc.o2o.entity.Shop;
import com.ysc.o2o.entity.ShopAuthMap;
import com.ysc.o2o.entity.UserAwardMap;
import com.ysc.o2o.entity.WechatAuth;
import com.ysc.o2o.enums.UserAwardMapStateEnum;
import com.ysc.o2o.service.PersonInfoService;
import com.ysc.o2o.service.ShopAuthMapService;
import com.ysc.o2o.service.UserAwardMapService;
import com.ysc.o2o.service.WechatAuthService;
import com.ysc.o2o.util.HttpServletRequestUtil;
import com.ysc.o2o.util.wechat.WechatUserUtil;
import com.ysc.o2o.util.wechat.message.pojo.UserAccessToken;

@Controller
@RequestMapping("/shopadmin")
public class UserAwardManagementController {
	
	@Autowired
	private UserAwardMapService userAwardMapService;
	
	@Autowired
	private WechatAuthService wechatAuthSevice;
	
	@Autowired
	private PersonInfoService personInfoService;
	
	@Autowired
	private ShopAuthMapService shopAuthMapService;
	
	@RequestMapping(value = "/listuserawardmapsbyshop", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listUserAwardMapsByShop(
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//从session中获取店铺信息
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		//获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		//空值判断
		if ((pageSize > -1) && (pageIndex) > -1 && (currentShop != null) && (currentShop.getShopId() != null)) {
			UserAwardMap  userAwardMap = new UserAwardMap();
			//从请求中获取奖品名
			String awardName =HttpServletRequestUtil.getString(request, "awardName");
			if (awardName != null) {
				//如果需要按照奖品名称搜索，则添加搜索条件
				Award award = new Award();
				award.setAwardName(awardName);
				userAwardMap.setAward(award);
			}
			//分页返回结果
			UserAwardMapExecution ue = userAwardMapService.listUserAwardMap(userAwardMap, pageIndex, pageSize);
			modelMap.put("userAwardMapList", ue.getUserAwardMapList());
			modelMap.put("count", ue.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or ShopID");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/exchangeaward", method = RequestMethod.POST)
	@ResponseBody
	private String exchangeAward(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		//负责扫描二维码的店员信息
		WechatAuth auth = getOperatorInfo(request);
		if (auth != null) {
			//通过userId获取店员信息
			PersonInfo operator = personInfoService.getPersonInfoById(auth.getPersonInfo().getUserId());
			//设置上用户的session
			request.getSession().setAttribute("user", operator);
			//解析微信回传过来的自定义参数state,由于之前进行了编码
			String qrCodeinfo = URLDecoder.decode(HttpServletRequestUtil.getString(request, "state"), "UTF-8");
			ObjectMapper mapper = new ObjectMapper();
			WechatInfo wechatInfo = null;
			try {
				wechatInfo = mapper.readValue(qrCodeinfo.replace("aaa", "\""), WechatInfo.class);
			} catch (Exception e) {
				return "shop/operationfail";
			}
			if (!checkQRCodeInfo(wechatInfo)) {
				return "shop/operationfail";
			}
			//获取用户奖品映射主键
			Long userAwardId = wechatInfo.getUserAwardId();
			//获取顾客Id
			Long customerId = wechatInfo.getCustomerId();
			
			UserAwardMap userAwardMap = compactUserAwardMap4Exchange(customerId,
					userAwardId, operator);
			if (userAwardMap != null) {
				try {
					//检查该员工是否具有扫码权限
					if (!checkShopAuth(operator.getUserId(), userAwardMap)) {
						return "shop/operationfail";
					}
					//修改奖品领取状态
					UserAwardMapExecution se = userAwardMapService
							.modifyUserAwardMap(userAwardMap);
					if (se.getState() == UserAwardMapStateEnum.SUCCESS.getState()) {
						return "shop/operationsuccess";
					} 
				} catch (RuntimeException e) {
					return "shop/operationfail";
				}
			}
		}
		return "shop/operationfail";
	}
	
	private UserAwardMap compactUserAwardMap4Exchange(Long customerId,
			Long userAwardId, PersonInfo operator) {
		UserAwardMap userAwardMap = null;
		if (customerId != null && userAwardId != null) {
			userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
			userAwardMap.setUsedStatus(0);
			PersonInfo customer = new PersonInfo();
			customer.setUserId(customerId);
			userAwardMap.setUser(customer);
		}
		return userAwardMap;
	}

	/**
	 * 检查扫码人员是否有操作权限
	 * @param userId
	 * @param userProductMap
	 * @return
	 */
	private boolean checkShopAuth(long userId, UserAwardMap userAwardMap) {
		//获取该店铺的所有授权信息
		ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService
				.listShopAuthMapByShopId(userAwardMap.getShop().getShopId(), 1, 1000);
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
}

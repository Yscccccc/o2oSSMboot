package com.ysc.o2o.web.frontend;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.ysc.o2o.dto.UserAwardMapExecution;
import com.ysc.o2o.entity.Award;
import com.ysc.o2o.entity.PersonInfo;
import com.ysc.o2o.entity.Shop;
import com.ysc.o2o.entity.UserAwardMap;
import com.ysc.o2o.enums.UserAwardMapStateEnum;
import com.ysc.o2o.service.AwardService;
import com.ysc.o2o.service.UserAwardMapService;
import com.ysc.o2o.util.CodeUtil;
import com.ysc.o2o.util.HttpServletRequestUtil;
import com.ysc.o2o.util.ShortNetAddressUtil;
import com.ysc.o2o.util.WechatProperties;

@Controller
@RequestMapping("frontend")
public class MyAwardController {
	@Autowired
	private UserAwardMapService userAwardMapService;
	@Autowired
	private AwardService awardService;
	
	@RequestMapping(value = "/adduserawardmap", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addUserAwardMap(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		PersonInfo user = (PersonInfo) request.getSession()
				.getAttribute("user");
		Long awardId = HttpServletRequestUtil.getLong(request, "awardId");
		UserAwardMap userAwardMap = compactUserAwardMap4Add(user, awardId);
		if (userAwardMap != null) {
			try {
				UserAwardMapExecution se = userAwardMapService
						.addUserAwardMap(userAwardMap);
				if (se.getState() == UserAwardMapStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请选择领取的奖品");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/listuserawardmapsbycustomer", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listUserAwardMapsByCustomer(
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		//Long userId = 1L;
		//从session中获取用户信息
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		if ((pageIndex > -1) && (pageSize > -1) && (user != null) && (user.getUserId() != null)) {
			UserAwardMap userAwardMapCondition = new UserAwardMap();
			userAwardMapCondition.setUser(user);
			long shopId = HttpServletRequestUtil.getLong(request, "shopId");
			if (shopId > -1) {
				//若店铺Id为非空，则将其添加进查询条件，即查询该用户在某个店铺的兑换记录
				Shop shop = new Shop();
				shop.setShopId(shopId);
				userAwardMapCondition.setShop(shop);
			}
			String awardName = HttpServletRequestUtil.getString(request,
					"awardName");
			if (awardName != null) {
				Award award = new Award();
				award.setAwardName(awardName);
				userAwardMapCondition.setAward(award);
			}
			UserAwardMapExecution ue = userAwardMapService.listUserAwardMap(
					userAwardMapCondition, pageIndex, pageSize);
			modelMap.put("userAwardMapList", ue.getUserAwardMapList());
			modelMap.put("count", ue.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or userId");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/getawardbyuserawardid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getAwardByUserAwardId(
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//获取前端传递过来的userAwardId
		long userAwardId = HttpServletRequestUtil.getLong(request, "userAwardId");
		//空值判断
		if (userAwardId > -1) {
			//根据Id获取顾客的映射信息，进而获取奖品Id
			UserAwardMap userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
			//根据奖品Id获取奖品信息
			Award award = awardService.getAwardById(userAwardMap.getAward().getAwardId());
			//将奖品信息和领取状态返回给前端
			modelMap.put("award", award);
			modelMap.put("usedStatus", userAwardMap.getUsedStatus());
			modelMap.put("userAwardMap", userAwardMap);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty awardId");
		}
		return modelMap;
	}
	
	//spring boot
//	//微信获取用户信息的api前缀
//	private static String urlPrefix;
//	//微信获取用户信息的api中间部分
//	private static String urlMiddle;
//	//微信获取用户信息的api后缀
//	private static String urlSuffix;
//	//微信回传给的响应添加授权信息的url
//	private static String authUrl;
//	
//	@Value("${wechat.prefix}")
//	public void setUrlPerfix(String urlPrefix) {
//		ShopAuthManagementController.urlPrefix = urlPrefix;
//	}
//	@Value("${wechat.middle}")
//	public void setUrlMiddle(String urlMiddle) {
//		ShopAuthManagementController.urlMiddle = urlMiddle;
//	}
//	@Value("${wechat.suffix}")
//	public void setUrlSuffix(String urlSuffix) {
//		ShopAuthManagementController.urlSuffix = urlSuffix;
//	}
//	@Value("${wechat.auth.ur}")
//	public void setAuthUrl(String authUrl) {
//		ShopAuthManagementController.authUrl = authUrl;
//	}
	
	//微信获取用户信息的api前缀
	private static String urlPrefix = WechatProperties.urlPrefix;
	//微信获取用户信息的api中间部分
	private static String urlMiddle = WechatProperties.urlMiddle;
	//微信获取用户信息的api后缀
	private static String urlSuffix = WechatProperties.urlSuffix;
	//微信回传给的响应添加授权信息的url
	private static String exchangeUrl = WechatProperties.exchangeUrl;
	
	@RequestMapping(value = "/generateqrcode4award", method = RequestMethod.GET)
	@ResponseBody
	private void generateQRCode4Product(HttpServletRequest request,
			HttpServletResponse response) {
		long userAwardId = HttpServletRequestUtil.getLong(request, "userAwardId");
		//根据Id获取顾客奖品映射实体类对象
		UserAwardMap userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
		//从session中获取顾客的信息
		PersonInfo user = (PersonInfo) request.getSession()
				.getAttribute("user");
		if (userAwardMap !=  null && user != null && user.getUserId() != null
				&& userAwardMap.getUser().getUserId() == user.getUserId()) {
			//获取当前时间戳，以保证二维码的时间有效性，精确到毫秒
			long timeStamp = System.currentTimeMillis();
			//将店铺id和timestamp传入content，赋值到state中，这样微信获取到这些信息后会回传到授权信息的添加方法
			//加上aaa是为了一会的在添加信息的方法里替换这些信息使用
			String content = "{aaauserAwardIdaaa:" + userAwardId + ",aaacustomerId" + user.getUserId() + ",aaacreateTimeaaa:" + timeStamp + "}";
			try {
				//将content的信息先进行base64编码以避免特殊字符造成的干扰，之后拼接目标的url
				String longUrl = urlPrefix + exchangeUrl + urlMiddle + URLEncoder.encode(content, "UTF-8") + urlSuffix;
				//将目标URL转换成短的URL
				String shortUrl = ShortNetAddressUtil.getShotURL(longUrl);
				//调用二维码生成工具类方法，传入短的URL，生成二维码
				BitMatrix qRcodeImg = CodeUtil.generateQRCodeStream(shortUrl, response);
				//将二维码以图片流的形式输出到前端
				MatrixToImageWriter.writeToStream(qRcodeImg, "png", response.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private UserAwardMap compactUserAwardMap4Add(PersonInfo user, Long awardId) {
		UserAwardMap userAwardMap = null;
		if (user != null && user.getUserId() != null && awardId != -1) {
			userAwardMap = new UserAwardMap();
			Award award = awardService.getAwardById(awardId);
			userAwardMap.setUser(user);
			userAwardMap.setAward(award);
			Shop shop = new Shop();
			shop.setShopId(award.getShopId());
			userAwardMap.setShop(shop);
			userAwardMap.setOperator(user);
			userAwardMap.setPoint(award.getPoint());
			userAwardMap.setCreateTime(new Date());
			userAwardMap.setUsedStatus(1);
		}
		return userAwardMap;
	}
}

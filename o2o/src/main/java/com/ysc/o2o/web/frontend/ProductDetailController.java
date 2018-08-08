package com.ysc.o2o.web.frontend;

import java.io.IOException;
import java.net.URLEncoder;
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
import com.ysc.o2o.entity.PersonInfo;
import com.ysc.o2o.entity.Product;
import com.ysc.o2o.service.ProductService;
import com.ysc.o2o.util.CodeUtil;
import com.ysc.o2o.util.HttpServletRequestUtil;
import com.ysc.o2o.util.ShortNetAddressUtil;
import com.ysc.o2o.util.WechatProperties;

@Controller
@RequestMapping("/frontend")
public class ProductDetailController {
	
	@Autowired
	private ProductService productService;

	@RequestMapping(value = "/listproductdetailpageinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listProductDetailPageInfo(
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long productId = HttpServletRequestUtil.getLong(request, "productId");
		Product product = null;
		if (productId != -1) {
			product = productService.getProductById(productId);
			//新增
			PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
			if (user == null) {
				modelMap.put("needQRCode", false);
			} else {
				modelMap.put("needQRCode", true);
			}
			
			modelMap.put("product", product);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty productId");
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
	private static String productMapUrl = WechatProperties.productMapUrl;
	
	@RequestMapping(value = "/generateqrcode4product", method = RequestMethod.GET)
	@ResponseBody
	private void generateQRCode4Product(HttpServletRequest request,
			HttpServletResponse response) {
		long productId = HttpServletRequestUtil.getLong(request, "productId");
		PersonInfo user = (PersonInfo) request.getSession()
				.getAttribute("user");
		if (productId != -1 && user != null && user.getUserId() != null) {
			//获取当前时间戳，以保证二维码的时间有效性，精确到毫秒
			long timeStamp = System.currentTimeMillis();
			//将店铺id和timestamp传入content，赋值到state中，这样微信获取到这些信息后会回传到授权信息的添加方法
			//加上aaa是为了一会的在添加信息的方法里替换这些信息使用
			String content = "{aaaprodutIdaaa:" + productId + ",aaacustomerId" + user.getUserId() + ",aaacreateTimeaaa:" + timeStamp + "}";
			try {
				//将content的信息先进行base64编码以避免特殊字符造成的干扰，之后拼接目标的url
				String longUrl = urlPrefix + productMapUrl + urlMiddle + URLEncoder.encode(content, "UTF-8") + urlSuffix;
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
}

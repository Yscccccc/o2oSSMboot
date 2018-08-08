package com.ysc.o2o.util;

import java.io.IOException;
import java.util.Properties;

public class WechatProperties {
	//微信获取用户信息的api前缀
	public static String urlPrefix ;
	//微信获取用户信息的api中间部分
	public static String urlMiddle;
	//微信获取用户信息的api后缀
	public static String urlSuffix;
	//微信回传给的响应添加授权信息的url
	public static String authUrl;
	
	public static String productMapUrl;
	
	public static String exchangeUrl;
	
	static {
		Properties pro = new Properties();
		try {
			pro.load(WechatProperties.class.getClassLoader().getResourceAsStream(
					"wechat.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		urlPrefix = pro.getProperty("wechat.prefix");
		urlMiddle = pro.getProperty("wechat.middle");
		urlSuffix = pro.getProperty("wechat.suffix");
		authUrl = pro.getProperty("wechat.auth.url");
		productMapUrl = pro.getProperty("wechat.productmap.url");
		exchangeUrl = pro.getProperty("wechat.exchange.url");
	}
}

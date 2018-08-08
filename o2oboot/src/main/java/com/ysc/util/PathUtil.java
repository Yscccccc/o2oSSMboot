package com.ysc.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PathUtil {
	private static String seperator = System.getProperty("file.separator");
	
	private static String winPath;
	
	@Value("${win.base.path}")
	public void setWinPath(String winPath) {
		PathUtil.winPath = winPath;
	}
	
	private static String linuxPath;
	
	@Value("${linux.base.path}")
	public void setLinuxPath(String linuxPath) {
		PathUtil.linuxPath = linuxPath;
	}
	
	private static String shopPath;
	
	@Value("${shop.relevant.path}")
	public void setShopPath(String shopPath) {
		PathUtil.shopPath = shopPath;
	}


	public static String getImgBasePath() {
		String os = System.getProperty("os.name");
		String basePath = "";
		if (os.toLowerCase().startsWith("win")) {
			basePath = winPath;
		} else {
			basePath = linuxPath;
		}
		basePath = basePath.replace("/", seperator);
		return basePath;
	}
	
	public static String getShopImagePath(long shopId) {
		String imagePath = shopPath + shopId + seperator;
		return imagePath.replace("/", seperator);
	}
}

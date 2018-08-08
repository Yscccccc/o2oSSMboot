package com.ysc.o2o.util;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	
	//需要加密的字段数组
	private String[] encyptPropNames = { "jdbc.username", "jdbc.password" };
	
	/**
	 * 对关键的属性进行转换
	 */
	@Override
	protected String convertProperty(String propertyName, String propertyValue) {
		if (isEncryptProp(propertyName)) {
			String decryptValue = DESUtil.getDecryptString(propertyValue);
			return decryptValue;
		} else {
			return propertyValue;
		}
	}
	
	/**
	 * 该属性是否已加密
	 * @param propertyName
	 * @return
	 */
	private boolean isEncryptProp(String propertyName) {
		for (String encryptpropertyName : encyptPropNames) {
			if (encryptpropertyName.equals(propertyName)) {
				return true;
			}
		}
		return false;
	}
	
}

package com.ysc.util.wechat.message.pojo;

/**
 * 微信通用接口凭证
 * @author chenshuying
 *
 */
public class AccessToken {
	//获取到的凭证
	private String token;
	//凭证有效时间，单位：秒
	private int expiresIn;
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public int getExiresIn() {
		return expiresIn;
	}
	
	public void setExpiresIn(int expiesIn) {
		this.expiresIn = expiesIn;
	}
}

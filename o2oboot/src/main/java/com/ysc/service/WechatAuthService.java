package com.ysc.service;

import com.ysc.dto.WechatAuthExecution;
import com.ysc.entity.WechatAuth;
import com.ysc.exceptions.WechatAuthOperationException;

public interface WechatAuthService {
	
	/**
	 * 通过openId查找平台对应的微信帐号
	 * @param openId
	 * @return
	 */
	WechatAuth getWechatAuthByOpenId(String openId);
	
	/**
	 * 
	 * @param wechatAuth
	 * @return
	 * @throws WechatAuthOperationException
	 */
	WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthOperationException;
}

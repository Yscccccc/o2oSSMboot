package com.ysc.o2o.service;

import com.ysc.o2o.dto.WechatAuthExecution;
import com.ysc.o2o.entity.WechatAuth;
import com.ysc.o2o.exceptions.WechatAuthOperationException;

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

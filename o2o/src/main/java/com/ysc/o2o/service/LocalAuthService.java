package com.ysc.o2o.service;

import com.ysc.o2o.dto.LocalAuthExecution;
import com.ysc.o2o.entity.LocalAuth;
import com.ysc.o2o.exceptions.LocalAuthOperationException;

public interface LocalAuthService {
	
	/**
	 * 通过账号和密码获取平台账号信息
	 * @param userName
	 * @param password
	 * @return
	 */
	LocalAuth getLocalAuthByUsernameAndPwd(String userName, String password);
	
	
	/**
	 * 通过userId获取平台账号信息
	 * @param userId
	 * @return
	 */
	LocalAuth getLocalAuthByUserId(long userId);
	
	/**
	 * 绑定微信生成平台专属的账号
	 * @param localAuth
	 * @return
	 * @throws LocalAuthOperationException
	 */
	LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException;
	
	/**
	 * 修改平台账号的登录密码
	 * @param userId
	 * @param username
	 * @param password
	 * @param newPassword
	 * @return
	 * @throws LocalAuthOperationException
	 */
	LocalAuthExecution modifyLocalAuth(Long userId, String userName, String password, String newPassword) throws LocalAuthOperationException;
}

package com.ysc.o2o.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ysc.o2o.BaseTest;
import com.ysc.o2o.dto.LocalAuthExecution;
import com.ysc.o2o.entity.LocalAuth;
import com.ysc.o2o.entity.PersonInfo;
import com.ysc.o2o.enums.LocalAuthStateEnum;

public class LocalAuthServiceTest extends BaseTest{
	
	@Autowired
	private LocalAuthService localAuthService;

	@Test
	public void testBindLocalAuth() {
		//新增一条平台账号
		LocalAuth localAuth = new LocalAuth();
		PersonInfo personInfo = new PersonInfo();
		String username = "testusername";
		String password = "testpassword";
		//给平台账号设置上用户信息
		//给用户设置上用户信息，表明是某个用户创建的账号
		personInfo.setUserId(1L);
		//给平台账号设置用户信息，标明是与那个用户绑定
		localAuth.setPersonInfo(personInfo);
		//设置帐号
		localAuth.setUsername(username);
		//设置密码
		localAuth.setPassword(password);
		//绑定帐号
		LocalAuthExecution lae = localAuthService.bindLocalAuth(localAuth);
		assertEquals(LocalAuthStateEnum.SUCCESS.getState(), lae.getState());
		//通过userId找到新增的localAuth
		localAuth = localAuthService.getLocalAuthByUserId(personInfo.getUserId());
		//打印用户名和帐号密码看看跟预期是否相符
		System.out.println("用户昵称： " + localAuth.getPersonInfo().getName());
		System.out.println("平台账号密码： " + localAuth.getPassword());
		
		
	}

	@Test
	public void testModifyLocalAuth() {
		//设置帐号信息
		long userId = 1;
		String username = "testusername";
		String password = "testpassword";
		String newPassword = "testnewpassword";
		//修改该帐号对应的密码
		LocalAuthExecution lae = localAuthService.modifyLocalAuth(userId, username, password, newPassword);
		assertEquals(LocalAuthStateEnum.SUCCESS.getState(), lae.getState());
		//通过帐号密码找到修改后的localAuth
		LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPwd(username, newPassword);
		//打印用户名字看看跟预期是否相符
		System.out.println(localAuth.getPersonInfo().getName());
	}

}

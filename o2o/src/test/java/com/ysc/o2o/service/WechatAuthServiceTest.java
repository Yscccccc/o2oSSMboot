package com.ysc.o2o.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ysc.o2o.BaseTest;
import com.ysc.o2o.dto.WechatAuthExecution;
import com.ysc.o2o.entity.PersonInfo;
import com.ysc.o2o.entity.WechatAuth;
import com.ysc.o2o.enums.WechatAuthStateEnum;

public class WechatAuthServiceTest extends BaseTest{
	
	@Autowired
	private WechatAuthService wechatAuthService;

	@Test
	public void testRegister() {
		//新增一条微信帐号
		WechatAuth wechatAuth = new WechatAuth();
		PersonInfo personInfo = new PersonInfo();
		String openId = "dasdasczx";
		//给微信帐号设置上用户信息，但不设置上用户Id
		//希望创建微信帐号的时候自动创建用户信息
		personInfo.setCreateTime(new Date());
		personInfo.setName("测试一哈");
		personInfo.setUserType(1);
		wechatAuth.setPersonInfo(personInfo);
		wechatAuth.setOpenId(openId);
		wechatAuth.setCreateTime(new Date());
		WechatAuthExecution wae = wechatAuthService.register(wechatAuth);
		assertEquals(WechatAuthStateEnum.SUCCESS.getState(), wae.getState());
		//通过openId找到新增的wechatAuth
		wechatAuth = wechatAuthService.getWechatAuthByOpenId(openId);
		//打印用户名字
		System.out.println(wechatAuth.getPersonInfo().getName());
	}

}

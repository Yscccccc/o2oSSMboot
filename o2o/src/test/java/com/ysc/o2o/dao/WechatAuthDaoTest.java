package com.ysc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ysc.o2o.BaseTest;
import com.ysc.o2o.entity.PersonInfo;
import com.ysc.o2o.entity.WechatAuth;

public class WechatAuthDaoTest extends BaseTest{
	
	@Autowired
	private WechatAuthDao wechatAuthDao;
	@Test
	public void testQueryWechatInfoByOpenId() {
		WechatAuth wechatAuth = wechatAuthDao.queryWechatInfoByOpenId("adsdad");
		assertEquals("测试", wechatAuth.getPersonInfo().getName());
	}

	@Test
	public void testIntsertWechatAuth() {
		//新增一条微信帐号
		WechatAuth wechatAuth = new WechatAuth();
		PersonInfo personInfo = new PersonInfo();
		personInfo.setUserId(1L);
		//给微信帐号绑定上用户信息
		wechatAuth.setPersonInfo(personInfo);
		//随意设置上openID
		wechatAuth.setOpenId("adsdad");
		wechatAuth.setCreateTime(new Date());
		int effectedNum = wechatAuthDao.insertWechatAuth(wechatAuth);
		assertEquals(1, effectedNum);
	}

}

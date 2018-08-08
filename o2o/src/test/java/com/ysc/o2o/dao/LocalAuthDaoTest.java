package com.ysc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ysc.o2o.BaseTest;
import com.ysc.o2o.entity.LocalAuth;
import com.ysc.o2o.entity.PersonInfo;

public class LocalAuthDaoTest extends BaseTest{
	
	@Autowired
	private LocalAuthDao localAuthDao;
	
	private static final String username = "testusername";
	private static final String password = "testpassword";
	
	@Test
	public void testQuerylocalByUserNameAndPwd() {
		//按照账号和密码查询用户信息
		LocalAuth localAuth = localAuthDao.querylocalByUserNameAndPwd(username, password);
		assertEquals("测试", localAuth.getPersonInfo().getName());
	}

	@Test
	public void testQuerylocalByUserId() {
		//按照用户Id查询平台账号，进而获取用户信息
		LocalAuth localAuth = localAuthDao.querylocalByUserId(1L);
		assertEquals("测试", localAuth.getPersonInfo().getName());
	}

	@Test
	public void testInsertLocalAuth() {
		//新增一台平台账号信息
		LocalAuth localAuth = new LocalAuth();
		PersonInfo personInfo = new PersonInfo();
		personInfo.setUserId(1L);
		//给平台账号绑定上用户信息
		localAuth.setPersonInfo(personInfo);
		//设置上用户名和密码
		localAuth.setUsername(username);
		localAuth.setPassword(password);
		localAuth.setCreateTime(new Date());
		int effectedNum = localAuthDao.insertLocalAuth(localAuth);
		assertEquals(1, effectedNum);
	}

	@Test
	public void testUpdateLocalAuth() {
		//依据用户ID，平台账号，以及旧密码修改平台账号密码
		Date now = new Date();
		int effectedNum = localAuthDao.updateLocalAuth(1L, username, password, password+"new", now);
		assertEquals(1, effectedNum);
		//查询出该条平台账号的最新信息
		LocalAuth localAuth = localAuthDao.querylocalByUserId(1L);
		//输出新密码
		System.out.println(localAuth.getPassword());
	}

}

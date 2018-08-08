package com.ysc.o2o.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ysc.o2o.BaseTest;
import com.ysc.o2o.entity.PersonInfo;
import com.ysc.o2o.entity.Shop;
import com.ysc.o2o.entity.UserShopMap;

public class UserShopMapDaoTest extends BaseTest{
	@Autowired
	private UserShopMapDao userShopMapDao;
	@Test
	public void testQueryUserShopMapList() {
		UserShopMap userShopMap = new UserShopMap();
		//查全部
		List<UserShopMap> userShopMapList = userShopMapDao.queryUserShopMapList(userShopMap, 0, 3);
		assertEquals(2, userShopMapList.size());
		int count = userShopMapDao.queryUserShopMapCount(userShopMap);
		assertEquals(2, count);
		//按店铺去查询
		Shop shop = new Shop();
		shop.setShopId(1L);
		userShopMap.setShop(shop);
		userShopMapList = userShopMapDao.queryUserShopMapList(userShopMap, 0, 3);
		assertEquals(2, userShopMapList.size());
		count = userShopMapDao.queryUserShopMapCount(userShopMap);
		assertEquals(2, count);
		//按用户Id和店铺查询
		userShopMap = userShopMapDao.queryUserShopMap(1, 1);
		assertEquals("测试", userShopMap.getUser().getName());
	}

	@Test
	public void testInsertUserShopMap() {
		//创建用户店铺积分统计信息1
		UserShopMap userShopMap = new UserShopMap();
		PersonInfo customer = new PersonInfo();
		customer.setUserId(1L);
		userShopMap.setUser(customer);
		Shop shop = new Shop();
		shop.setShopId(1L);
		userShopMap.setShop(shop);
		userShopMap.setCreateTime(new Date());
		userShopMap.setPoint(1);
		int effectedNum = userShopMapDao.insertUserShopMap(userShopMap);
		assertEquals(1, effectedNum);
		//创建用户店铺积分统计信息1
		UserShopMap userShopMap2 = new UserShopMap();
		PersonInfo customer2 = new PersonInfo();
		customer2.setUserId(2L);
		userShopMap2.setUser(customer2);
		Shop shop2 = new Shop();
		shop2.setShopId(1L);
		userShopMap2.setShop(shop2);
		userShopMap2.setCreateTime(new Date());
		userShopMap2.setPoint(1);
		int effectedNum2 = userShopMapDao.insertUserShopMap(userShopMap2);
		assertEquals(1, effectedNum2);
	}

	@Test
	public void testUpdateUserShopMapPoint() {
		UserShopMap userShopMap = new UserShopMap();
		userShopMap = userShopMapDao.queryUserShopMap(1, 1);
		assertTrue("积分不一致", 1 == userShopMap.getPoint());
		userShopMap.setPoint(2);
		int effectedNum = userShopMapDao.updateUserShopMapPoint(userShopMap);
		assertEquals(1, effectedNum);
	}

}

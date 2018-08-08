package com.ysc.o2o.dao;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ysc.o2o.BaseTest;
import com.ysc.o2o.entity.Award;
import com.ysc.o2o.entity.PersonInfo;
import com.ysc.o2o.entity.Shop;
import com.ysc.o2o.entity.UserAwardMap;

public class UserAwardMapDaoTest extends BaseTest{
	
	@Autowired
	private UserAwardMapDao userAwardMapDao;
	@Test
	public void testQueryUserAwardMapList() {
		UserAwardMap userAwardMap = new UserAwardMap();
		//测试queryUserAwardMapList
		List<UserAwardMap> userAwardMapList = userAwardMapDao.queryUserAwardMapList(userAwardMap, 0, 4);
		assertEquals(2, userAwardMapList.size());
		int count = userAwardMapDao.quertUserAwardMapCount(userAwardMap);
		assertEquals(2, count);
		PersonInfo customer = new PersonInfo();
		//按用户名模糊查询
		customer.setName("测试");
		userAwardMap.setUser(customer);
		userAwardMapList = userAwardMapDao.queryUserAwardMapList(userAwardMap, 0, 4);
		assertEquals(2, userAwardMapList.size());
		count = userAwardMapDao.quertUserAwardMapCount(userAwardMap);
		assertEquals(2, count);
		//测试queryUserAwardMapByID,
		userAwardMap = userAwardMapDao.queryUserAwardMapById(userAwardMapList.get(0).getUserAwardId());
		assertEquals("测试一", userAwardMap.getAward().getAwardName());
	}


	@Test
	public void testInsertUserAwardMap() {
		//创建用户奖品映射信息1
		UserAwardMap userAwardMap = new UserAwardMap();
		PersonInfo customer = new PersonInfo();
		customer.setUserId(1L);
		userAwardMap.setUser(customer);
		userAwardMap.setOperator(customer);
		Award award = new Award();
		award.setAwardId(3L);
		userAwardMap.setAward(award);
		Shop shop = new Shop();
		shop.setShopId(1L);
		userAwardMap.setShop(shop);
		userAwardMap.setCreateTime(new Date());
		userAwardMap.setUsedStatus(1);
		userAwardMap.setPoint(1);
		int effectedNum = userAwardMapDao.insertUserAwardMap(userAwardMap);
		assertEquals(1, effectedNum);
		//创建用户奖品映射信息2
		UserAwardMap userAwardMap2 = new UserAwardMap();
		PersonInfo customer2 = new PersonInfo();
		customer2.setUserId(1L);
		userAwardMap2.setUser(customer2);
		userAwardMap2.setOperator(customer2);
		Award award2 = new Award();
		award2.setAwardId(3L);
		userAwardMap2.setAward(award2);
		Shop shop2 = new Shop();
		shop2.setShopId(1L);
		userAwardMap2.setShop(shop2);
		userAwardMap2.setCreateTime(new Date());
		userAwardMap2.setUsedStatus(0);
		userAwardMap2.setPoint(2);
		int effectedNum2 = userAwardMapDao.insertUserAwardMap(userAwardMap2);
		assertEquals(1, effectedNum2);
	}

	@Test
	public void testUpdateUserAwardMap() {
		UserAwardMap userAwardMap = new UserAwardMap();
		PersonInfo customer = new PersonInfo();
		//按用户名模糊查询
		customer.setName("测试");
		userAwardMap.setUser(customer);
		List<UserAwardMap> userAwardMapList = userAwardMapDao.queryUserAwardMapList(userAwardMap, 0, 3);
		assertTrue("积分不一致", 0 == userAwardMapList.get(0).getUsedStatus());
		userAwardMapList.get(0).setUsedStatus(1);
		int effectedNum = userAwardMapDao.updateUserAwardMap(userAwardMapList.get(0));
		assertEquals(1, effectedNum);
	}

}

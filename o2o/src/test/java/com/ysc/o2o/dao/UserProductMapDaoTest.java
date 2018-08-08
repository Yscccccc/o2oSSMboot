package com.ysc.o2o.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ysc.o2o.BaseTest;
import com.ysc.o2o.entity.PersonInfo;
import com.ysc.o2o.entity.Product;
import com.ysc.o2o.entity.Shop;
import com.ysc.o2o.entity.UserProductMap;

public class UserProductMapDaoTest extends BaseTest{
	
	@Autowired
	private UserProductMapDao userProductMapDao;
	@Test
	public void testQueryUserProductMapList() {
		UserProductMap userProductMap = new UserProductMap();
		PersonInfo customer = new PersonInfo();
		//按顾客名字模糊查询
		customer.setName("测试");
		userProductMap.setUser(customer);
		List<UserProductMap> userProductMapList = userProductMapDao.queryUserProductMapList(userProductMap, 0, 3);
		assertEquals(2, userProductMapList.size());
		int count = userProductMapDao.queryUserProductMapCount(userProductMap);
		assertEquals(2, count);
//		叠加店铺去查询
		Shop shop = new Shop();
		shop.setShopId(1L);
		userProductMap.setShop(shop);
		userProductMapList = userProductMapDao.queryUserProductMapList(userProductMap, 0, 3);
		assertEquals(2, userProductMapList.size());
		count = userProductMapDao.queryUserProductMapCount(userProductMap);
		assertEquals(2, count);
	}

	@Test
	public void testInsertUserProductMap() {
		//创建用户商品映射信息1
		UserProductMap userProductMap = new UserProductMap();
		PersonInfo customer = new PersonInfo();
		customer.setUserId(2L);
		userProductMap.setUser(customer);
		userProductMap.setOperator(customer);
		Product product = new Product();
		product.setProductId(3L);
		userProductMap.setProduct(product);
		Shop shop = new Shop();
		shop.setShopId(1L);
		userProductMap.setShop(shop);
		userProductMap.setCreateTime(new Date());
		int effectedNum = userProductMapDao.insertUserProductMap(userProductMap);
		assertEquals(1, effectedNum);
//		UserProductMap userProductMap2 = new UserProductMap();
//		PersonInfo customer2 = new PersonInfo();
//		customer2.setUserId(1L);
//		userProductMap2.setUser(customer2);
//		userProductMap2.setOperator(customer2);
//		Product product2 = new Product();
//		product2.setProductId(4L);
//		userProductMap2.setProduct(product2);
//		Shop shop2 = new Shop();
//		shop2.setShopId(1L);
//		userProductMap2.setShop(shop2);
//		userProductMap2.setCreateTime(new Date());
//		int effectedNum2 = userProductMapDao.insertUserProductMap(userProductMap2);
//		assertEquals(1, effectedNum2);
	}

}

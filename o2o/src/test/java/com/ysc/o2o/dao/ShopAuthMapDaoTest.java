package com.ysc.o2o.dao;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ysc.o2o.BaseTest;
import com.ysc.o2o.entity.PersonInfo;
import com.ysc.o2o.entity.Shop;
import com.ysc.o2o.entity.ShopAuthMap;

public class ShopAuthMapDaoTest extends BaseTest{
	@Autowired
	private ShopAuthMapDao shopAuthMapDao;
	@Test
	public void testQueryShopAuthMapListByShopId() {
		//测试queryShopAuthMapListByShopId
		List<ShopAuthMap> shopAuthMapList = shopAuthMapDao.queryShopAuthMapListByShopId(1, 0, 2);
		assertEquals(1, shopAuthMapList.size());
		//测试queryShopAuthMapById
		ShopAuthMap shopAuthMap = shopAuthMapDao.queryShopAuthMapById(shopAuthMapList.get(0).getShopAuthId());
		assertEquals("CEO", shopAuthMap.getTitle());
		System.out.println("employee's name is: " + shopAuthMap.getEmployee().getName());
		System.out.println(shopAuthMap.getShop().getShopName());
		//测试queryShopAuthCountByShopId
		int count = shopAuthMapDao.queryShopAuthCountByShopId(1);
		assertEquals(1, count);
	}

	@Test
	public void testInsertShopAuthMap() {
		//创建店铺授权信息1
		ShopAuthMap shopAuthMap1 = new ShopAuthMap();
		PersonInfo employee = new PersonInfo();
		employee.setUserId(1L);
		shopAuthMap1.setEmployee(employee);
		Shop shop = new Shop();
		shop.setShopId(1L);
		shopAuthMap1.setShop(shop);
		shopAuthMap1.setTitle("CEO");
		shopAuthMap1.setTitleFlag(1);
		shopAuthMap1.setCreateTime(new Date());
		shopAuthMap1.setLastEditTime(new Date());
		shopAuthMap1.setEnableStatus(1);
		int effectedNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap1);
		assertEquals(1, effectedNum);
		
		//创建店铺授权信息2
		ShopAuthMap shopAuthMap2 = new ShopAuthMap();
		PersonInfo employee2 = new PersonInfo();
		employee2.setUserId(1L);
		shopAuthMap2.setEmployee(employee2);
		Shop shop2 = new Shop();
		shop2.setShopId(2L);
		shopAuthMap2.setShop(shop2);
		shopAuthMap2.setTitle("打工的");
		shopAuthMap2.setTitleFlag(2);
		shopAuthMap2.setCreateTime(new Date());
		shopAuthMap2.setLastEditTime(new Date());
		shopAuthMap2.setEnableStatus(0);
		int effectedNum2 = shopAuthMapDao.insertShopAuthMap(shopAuthMap2);
		assertEquals(1, effectedNum2);
	}

	@Test
	public void testUpdateShopAuthMap() {
		List<ShopAuthMap> shopAuthMapList = shopAuthMapDao.queryShopAuthMapListByShopId(1, 0, 2);
		shopAuthMapList.get(0).setTitle("CCO");
		shopAuthMapList.get(0).setTitleFlag(2);
		int effectedNum = shopAuthMapDao.updateShopAuthMap(shopAuthMapList.get(0));
		assertEquals(1, effectedNum);
	}

	@Test
	public void testDeleteShopAuthMap() {
		List<ShopAuthMap> shopAuthMapList1 = shopAuthMapDao.queryShopAuthMapListByShopId(1, 0, 2);
		List<ShopAuthMap> shopAuthMapList2 = shopAuthMapDao.queryShopAuthMapListByShopId(2, 0, 2);
		int effectedNum = shopAuthMapDao.deleteShopAuthMap(shopAuthMapList1.get(0).getShopAuthId());
		assertEquals(1, effectedNum);
		effectedNum = shopAuthMapDao.deleteShopAuthMap(shopAuthMapList2.get(0).getShopAuthId());
		assertEquals(1, effectedNum);
	}

}

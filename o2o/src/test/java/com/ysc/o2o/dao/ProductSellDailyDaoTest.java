package com.ysc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ysc.o2o.BaseTest;
import com.ysc.o2o.entity.ProductSellDaily;
import com.ysc.o2o.entity.Shop;

public class ProductSellDailyDaoTest extends BaseTest{
	@Autowired
	private ProductSellDailyDao productSellDailyDao;
	@Test
	public void testQueryProductSellDailyList() {
		ProductSellDaily productSellDaily = new ProductSellDaily();
		//叠加店铺去查询
		Calendar calendar = Calendar.getInstance();
		//获取昨天的日期
		calendar.add(Calendar.DATE, -1);
		Date endTime = calendar.getTime();
		//获取七天前的日期
		calendar.add(Calendar.DATE, -6);
		Date beginTime = calendar.getTime();
		Shop shop = new Shop();
		shop.setShopId(1L);
		productSellDaily.setShop(shop);
		List<ProductSellDaily> productSellDailieList = productSellDailyDao.queryProductSellDailyList(productSellDaily, beginTime, endTime);
		for (ProductSellDaily psd : productSellDailieList) {
			System.out.println(psd.getTotal());
		}
	}

	@Test
	public void testInsertProductSellDaily() {
		//创建商品日销量统计
		int effectedNum = productSellDailyDao.insertProductSellDaily();
		assertEquals(2, effectedNum);
	}
	
	@Test
	public void testInsertDefaultProductSellDaily() {
		//创建商品日销量统计
		int effectedNum = productSellDailyDao.insertDefaultProductSellDaily();
		assertEquals(4, effectedNum);
	}
}

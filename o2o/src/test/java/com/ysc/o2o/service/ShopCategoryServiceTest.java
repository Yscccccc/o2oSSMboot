package com.ysc.o2o.service;


import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ysc.o2o.BaseTest;
import com.ysc.o2o.entity.ShopCategory;

public class ShopCategoryServiceTest extends BaseTest{
	
	@Autowired
	private ShopCategoryService shopCategoryService; 
	@Test
	public void testGetShopCategoryList() {
		
//		List<ShopCategory> shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
//		assertEquals(2, shopCategoryList.size());
//		ShopCategory testCategory = new ShopCategory();
//		ShopCategory parentCategory = new ShopCategory();
//		parentCategory.setShopCategoryId(1L);
//		testCategory.setParent(parentCategory);
//		shopCategoryList = shopCategoryService.getShopCategoryList(testCategory);
//		assertEquals(2, shopCategoryList.size());
//		System.out.println(shopCategoryList.get(0).getShopCategoryName());
		
		List<ShopCategory> shopCategoryList = shopCategoryService.getShopCategoryList(null);
		System.out.println(shopCategoryList.size());
	}

}

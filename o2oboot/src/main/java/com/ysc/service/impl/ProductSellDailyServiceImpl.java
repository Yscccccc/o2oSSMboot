package com.ysc.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ysc.dao.ProductSellDailyDao;
import com.ysc.service.impl.ProductSellDailyServiceImpl;
import com.ysc.service.ProductSellDailyService;

@Service
public class ProductSellDailyServiceImpl implements ProductSellDailyService{
	private static final Logger log = LoggerFactory.getLogger(ProductSellDailyServiceImpl.class);
	@Autowired
	private ProductSellDailyDao productSellDailyDao;
	
	@Override
	public void dailyCalculate() {
		log.info("Quartz Running!");
		productSellDailyDao.insertProductSellDaily();		
	}
}

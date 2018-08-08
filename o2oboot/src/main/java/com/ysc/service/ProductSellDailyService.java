package com.ysc.service;

public interface ProductSellDailyService {
	/**
	 * 每日定时对所有店铺的商品销量进行统计
	 */
	void dailyCalculate();
}

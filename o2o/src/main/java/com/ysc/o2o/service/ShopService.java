package com.ysc.o2o.service;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.ysc.o2o.dto.ShopExecution;
import com.ysc.o2o.entity.Shop;
import com.ysc.o2o.exceptions.ShopOperationException;

public interface ShopService {
	/**
	 * 根据shopCondition分页返回相应的店铺列表
	 * @param shopCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);
	/**
	 * 通过店铺Id获取店铺信息
	 * @param shopId
	 * @return
	 */
	Shop getByShopId(long shopId);
	
	/**
	 * 注册店铺信息，包括图片的处理
	 * @param shop
	 * @param shopImg
	 * @return
	 * @throws ShopOperationException
	 */
	ShopExecution addShop(Shop shop,CommonsMultipartFile shopImg) throws ShopOperationException;
	
	/**
	 * 更新店铺信息，包括图片的处理
	 * @param shop
	 * @param shopImg
	 * @return
	 * @throws ShopOperationException
	 */
	ShopExecution modifyShop(Shop shop,CommonsMultipartFile shopImg) throws ShopOperationException;
	
}

package com.ysc.service;

import java.util.List;

import com.ysc.dto.ProductCategoryExecution;
import com.ysc.entity.ProductCategory;
import com.ysc.exceptions.ProductCategoryOperationException;

public interface ProductCategoryService {
	
	/**
	 * 查询指定某个店铺下的所有商品类别信息
	 * @param shopId
	 * @return
	 */
	List<ProductCategory> getProductCategoryList(long shopId);
	
	/**
	 * 
	 * @param productCategoryList
	 * @return
	 */
	ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
				throws ProductCategoryOperationException;
	/**
	 * 将此类别下的商品里的类别id置为空，在删除掉该商品类别
	 * @param productCategoryId
	 * @param shopId
	 * @return
	 * @throws ProductCategoryOperationException
	 */
	ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
				throws ProductCategoryOperationException;
}

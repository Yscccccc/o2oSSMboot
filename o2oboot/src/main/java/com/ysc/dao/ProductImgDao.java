package com.ysc.dao;

import java.util.List;

import com.ysc.entity.ProductImg;

public interface ProductImgDao {
	
	List<ProductImg> queryProductImgList(long productId);
	
	/**
	 * 批量添加商品详情图片
	 * @param productImgList
	 * @return
	 */
	int batchInsertProductImg(List<ProductImg> productImgList);
	
	int deleteProductImgByProductId(long productId);
}

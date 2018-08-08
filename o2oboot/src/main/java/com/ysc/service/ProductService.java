package com.ysc.service;

import java.util.List;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.ysc.dto.ProductExecution;
import com.ysc.entity.Product;
import com.ysc.exceptions.ProductOperationException;

public interface ProductService {
	/**
	 * 查询商品列表并分页，可输入的条件有：商品名（模糊），商品状态，店铺id,商品类别
	 * @param productCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	
	ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize);
	
	/**
	 * 通过商品Id查询唯一的商品信息
	 * @param productId
	 * @return
	 */
	Product getProductById(long productId);
	
	/**
	 * 添加商品信息以及图片处理
	 * @param product
	 * @param thumbnail
	 * @param productImgs
	 * @return
	 * @throws ProductOperationException
	 */
	ProductExecution addProduct(Product product, CommonsMultipartFile thumbnail, List<CommonsMultipartFile> productImgs) throws ProductOperationException;
	
	/**
	 * 修改商品信息以及图片处理
	 * @param product
	 * @param thumbnail
	 * @param prouctImgs
	 * @return
	 * @throws ProductOperationException
	 */
	ProductExecution modifyProduct(Product product, CommonsMultipartFile thumbnail, List<CommonsMultipartFile> productImgs) throws ProductOperationException;
}

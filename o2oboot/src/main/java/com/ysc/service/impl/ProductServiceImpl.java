package com.ysc.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.ysc.dao.ProductDao;
import com.ysc.dao.ProductImgDao;
import com.ysc.dto.ProductExecution;
import com.ysc.entity.Product;
import com.ysc.entity.ProductImg;
import com.ysc.enums.ProductStateEnum;
import com.ysc.exceptions.ProductOperationException;
import com.ysc.service.ProductService;
import com.ysc.util.ImageUtil;
import com.ysc.util.PageCalculator;
import com.ysc.util.PathUtil;


@Service
public class ProductServiceImpl implements ProductService{
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired	
	private ProductImgDao productImgDao;
	
	//1.处理缩略图，获取缩略图相对路径并赋值给product
	//2.往tb_product写入商品信息，获取productId
	//3.结合productId批量处理商品详情图
	//4.将商品详情图列表批量插入tb_product_img中
	@Override
	@Transactional
	public ProductExecution addProduct(Product product, CommonsMultipartFile thumbnail,
			List<CommonsMultipartFile> productImgs) throws ProductOperationException {
		//空值判断
		if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
			//给商品设置上默认属性
			product.setCreateTime(new Date());
			product.setLastEditTime(new Date());
			//默认为上架的状态
			product.setEnableStaus(1);
			//若商品缩略图不为空则添加
			if (thumbnail != null) {
				addThumbnail(product, thumbnail);
			}
			
			try {
				//创建商品信息
				int effectedNum = productDao.insertProduct(product);
				if (effectedNum <= 0) {
					throw new ProductOperationException("创建商品失败");
				}
			} catch (Exception e) {
				throw new ProductOperationException("创建商品失败：" + e.toString());
			}
			
			//若商品详情图不为空则添加
			if (productImgs != null && productImgs.size() > 0) {
				addProductImgs(product, productImgs);
			}
			return new ProductExecution(ProductStateEnum.SUCCESS, product);
		} else {
			//传参为空则返回空值错误信息
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}

	@Override
	public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
		// TODO Auto-generated method stub
		//页码转换成数据库的行码，并调用dao层取回指定页码的商品列表
		int rowIndex = PageCalculator.calculaterRowIndex(pageIndex, pageSize);
		List<Product> productList = productDao.queryProductList(productCondition, rowIndex, pageSize);
		//基于同样的查询条件返回该查询条件下的商品总数
		int count = productDao.queryProductCount(productCondition);
		ProductExecution pe =  new ProductExecution();
		pe.setProductList(productList);
		pe.setCount(count);
		return pe;
	}

	@Override
	public Product getProductById(long productId) {
		return productDao.queryProductByProductId(productId);
	}

	@Override
	@Transactional
	//1.若缩略图参数有值，则处理缩略图
	//若原先存在缩略图则先删除在添加新图，之后获取缩略图相对路径并赋值给product
	//2.若商品详情图列表参数有值，对商品原先的商品详情图记录全部清除
	//3.将tb_product_img下面的该商品原先的商品详情图记录全部清除
	//4.更新tb_product_img以及tb_product的信息
	public ProductExecution modifyProduct(Product product, CommonsMultipartFile thumbnail,
			List<CommonsMultipartFile> productImgs) throws ProductOperationException {
		//空值判断
		if (product !=null && product.getShop() != null && product.getShop().getShopId() != null) {
			//给商品设置上默认属性
			product.setLastEditTime(new Date());
			//若商品缩略图不为空且原有缩略图不为空则删除原有缩略图并添加
			if (thumbnail != null) {
				//先获取一遍原有信息，因为原来的信息里有原图片的地址
				Product tempProduct = productDao.queryProductByProductId(product.getProductId());
				if (tempProduct.getImgAddr() != null) {
					ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
				}
				addThumbnail(product, thumbnail);
			}
			//如果有新存入的商品详情图，则将原先的删除，并添加新的图片
			if (productImgs != null && productImgs.size() > 0) {
				deleteProductImgs(product.getProductId());
				addProductImgs(product, productImgs);
			}
			try {
				//更新商品信息
				int effectedNum = productDao.updateProduct(product);
				if (effectedNum <= 0) {
					throw new ProductOperationException("更新商品信息失败");
				}
				return new ProductExecution(ProductStateEnum.SUCCESS, product);
			} catch (Exception e) {
				throw new ProductOperationException("更新商品信息失败：" + e.getMessage());
			}
		} else {
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}
	
	/**
	 * 添加商品详情图
	 * @param product
	 * @param productImgs
	 */
	private void addProductImgs(Product product, List<CommonsMultipartFile> productImgs) {
		//获取图片存储路径，这里直接存放到相应店铺的文件夹底下
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		List<String> imgAddrList = ImageUtil.generateNormalImgs(productImgs, dest);
		//遍历图片一次就去处理，并添加到productImg实体类里
		if (imgAddrList != null && imgAddrList.size() > 0) {
			List<ProductImg> productImgList = new ArrayList<ProductImg>();
			for (String imgAddr : imgAddrList) {
				ProductImg productImg = new ProductImg();
				productImg.setImgAddr(imgAddr);
				productImg.setProductId(product.getProductId());
				productImg.setCreateTime(new Date());
				productImgList.add(productImg);
			}
			try {
				int effectedNum = productImgDao.batchInsertProductImg(productImgList);
				if (effectedNum <= 0) {
					throw new ProductOperationException("创建商品详情图片失败");
				}
			} catch (Exception e) {
				throw new ProductOperationException("创建商品详情图片失败： " + e.toString());
			}
		}
		
	}
	/**
	 * 添加缩略图
	 * @param product
	 * @param thumbnail
	 */
	private void addThumbnail(Product product, CommonsMultipartFile thumbnail) {
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		product.setImgAddr(thumbnailAddr);
	}
	/**
	 * 删除某个商品下的所有详情图
	 * @param productId
	 */
	private void deleteProductImgs(long productId) {
		//根据productId获取原来的图片
		List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
		//删除原来的图片
		for (ProductImg productImg : productImgList) {
			ImageUtil.deleteFileOrPath(productImg.getImgAddr());
		}
		//删除数据库里所有图片的信息
		productImgDao.deleteProductImgByProductId(productId);
	}
}

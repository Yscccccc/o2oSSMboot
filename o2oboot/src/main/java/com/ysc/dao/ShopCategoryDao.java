package com.ysc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ysc.entity.ShopCategory;

public interface ShopCategoryDao {
	List<ShopCategory> queryShopCategory(@Param("shopCategoryCondition") ShopCategory shopCategoryCondition);
}

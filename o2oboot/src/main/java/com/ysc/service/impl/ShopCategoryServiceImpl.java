package com.ysc.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ysc.cache.JedisUtil;
import com.ysc.dao.ShopCategoryDao;
import com.ysc.entity.ShopCategory;
import com.ysc.exceptions.ShopCategoryOperationException;
import com.ysc.service.ShopCategoryService;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService{
	
	@Autowired
	private ShopCategoryDao shopCategoryDao;
	
	@Autowired
	private JedisUtil.Keys jedisKeys;
	
	@Autowired
	private JedisUtil.Strings jedisStrings;
	
	private static Logger logger = LoggerFactory.getLogger(ShopCategoryServiceImpl.class);
	
	@Override
	@Transactional
	public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) {
		//定义redis的key前缀
		String key = SCLISTKEY;
		//定义接收对象
		List<ShopCategory> shopCategoryList = null;
		//定义jackson的数据转换操作类
		ObjectMapper mapper = new ObjectMapper();
		//拼接出redis的key
		if (shopCategoryCondition == null) {
			//若查询条件为空，则列出所有首页大类，即parentId为空的店铺类别
			key = key + "_allfirstlevel";
		} else if (shopCategoryCondition != null && shopCategoryCondition.getParent() != null && shopCategoryCondition.getParent().getShopCategoryId() != null) {
			//若parentId为非空，则列出该parentId下的所有子类别
			key = key + "_parent" + shopCategoryCondition.getParent().getShopCategoryId();
		} else if (shopCategoryCondition != null) {
			//列出所有子类别，不管属于哪个类，都列出来
			key = key + "allsecondlevel";
		}
		//判断key是否存在
		if (!jedisKeys.exists(key)) {
			//若不存在，则从数据库中取出相应数据
			shopCategoryList = shopCategoryDao.queryShopCategory(shopCategoryCondition);
			String jsonString;
			try {
				//将相关的实体类集合转换成string，存入redis里面对应的key中
				jsonString = mapper.writeValueAsString(shopCategoryList);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			}
			jedisStrings.set(key, jsonString);
		} else {
			//若存在，则直接从redis中取出相应的数据
			String jsonString = jedisStrings.get(key);
			//指定要将string转换成的集合类型
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, ShopCategory.class);
			try {
				//将相关的key对应的value里的string转换成对象的实体集合
				shopCategoryList = mapper.readValue(jsonString, javaType);
			} catch (JsonParseException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			} catch (JsonMappingException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			}
		}
		return shopCategoryList;
	}
	
}
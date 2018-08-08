package com.ysc.o2o.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ysc.o2o.dao.UserShopMapDao;
import com.ysc.o2o.dto.UserShopMapExecution;
import com.ysc.o2o.entity.UserShopMap;
import com.ysc.o2o.service.UserShopMapService;
import com.ysc.o2o.util.PageCalculator;

@Service
public class UserShopMapServiceImpl implements UserShopMapService{
	
	@Autowired
	private UserShopMapDao userShopMaoDao;
	
	@Override
	public UserShopMapExecution listUserShopMap(UserShopMap userShopMapCondition, int pageIndex, int pageSize) {
		//空值判断
		if (userShopMapCondition != null && pageIndex != -1 && pageSize != -1) {
			//页转行
			int beginIndex = PageCalculator.calculaterRowIndex(pageIndex, pageSize);
			//根据传入的查询条件分页返回用户积分列表信息
			List<UserShopMap> userShopMapList = userShopMaoDao.queryUserShopMapList(userShopMapCondition, beginIndex, pageSize);
			//返回总数
			int count = userShopMaoDao.queryUserShopMapCount(userShopMapCondition);
			UserShopMapExecution ue = new UserShopMapExecution();
			ue.setUserShopMapList(userShopMapList);
			ue.setCount(count);
			return ue;
		} else {
			return null;
		}		
	}

	@Override
	public UserShopMap getUserShopMap(long userId, long shopId) {
		// TODO Auto-generated method stub
		return userShopMaoDao.queryUserShopMap(userId, shopId);
	}
	
}

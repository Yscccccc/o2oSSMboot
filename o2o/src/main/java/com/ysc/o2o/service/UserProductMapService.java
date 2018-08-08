package com.ysc.o2o.service;

import com.ysc.o2o.dto.UserProductMapExecution;
import com.ysc.o2o.entity.UserProductMap;

public interface UserProductMapService {
	/**
	 * 通过传入的查询条件分页列出用户消费信息列表
	 * @param userProductCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	UserProductMapExecution listUserProductMap(UserProductMap userProductCondition, Integer pageIndex,
			Integer pageSize);
	
	/**
	 * 添加消费记录
	 * @param userProductMap
	 * @return
	 */
	UserProductMapExecution addUserProductMap(UserProductMap userProductMap);
}

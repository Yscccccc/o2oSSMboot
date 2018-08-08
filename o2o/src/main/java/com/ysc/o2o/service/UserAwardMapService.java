package com.ysc.o2o.service;

import com.ysc.o2o.dto.UserAwardMapExecution;
import com.ysc.o2o.entity.UserAwardMap;

public interface UserAwardMapService {
	/**
	 * 根据传入的查询条件分页获取映射列表的总数
	 * @param userAwardMapCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	UserAwardMapExecution listUserAwardMap(UserAwardMap userAwardMapCondition, Integer pageIndex, Integer pageSize);
	
	/**
	 * 根据传入的ID获取映射信息
	 * @param userAwardMapId
	 * @return
	 */
	UserAwardMap getUserAwardMapById(long userAwardMapId);
	
	/**
	 * 领取奖品，添加映射信息
	 * @param userAwardMap
	 * @return
	 */
	UserAwardMapExecution addUserAwardMap(UserAwardMap userAwardMap);
	
	/**
	 * 修改映射信息，这里主要修改奖品的领取状态
	 * @param userAwardMap
	 * @return
	 */
	UserAwardMapExecution modifyUserAwardMap(UserAwardMap userAwardMap);
}

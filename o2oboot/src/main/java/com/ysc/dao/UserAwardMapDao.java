package com.ysc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ysc.entity.UserAwardMap;

public interface UserAwardMapDao {
	
	/**
	 * 根据传入进来的查询条件分页返回用户兑换奖品记录的列表信息
	 * @param userAwardMap
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	List<UserAwardMap> queryUserAwardMapList(@Param("userAwardCondition") UserAwardMap userAwardMap,
			@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);
	
	/**
	 * 配合queryUserAwardMapList返回相同查询条件下的兑换奖品记录数
	 * @param userAwardCondition
	 * @return
	 */
	int quertUserAwardMapCount(@Param("userAwardCondition") UserAwardMap userAwardCondition);
	
	
	/**
	 * 通过UserAwardId返回某条奖品兑换信息
	 * @param userAwardId
	 * @return
	 */
	UserAwardMap queryUserAwardMapById(long userAwardId);
	
	/**
	 * 添加一条奖品兑换记录
	 * @param userAwardMap
	 * @return
	 */
	int insertUserAwardMap(UserAwardMap userAwardMap);
	
	
	/**
	 * 更新奖品兑换信息，主要更新奖品领取状态
	 * @param award
	 * @return
	 */
	int updateUserAwardMap(UserAwardMap userAwardMap);
	
}

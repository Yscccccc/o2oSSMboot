package com.ysc.service;

import com.ysc.entity.PersonInfo;

public interface PersonInfoService {
	
	/**
	 * 根据用户Id获取personInfo信息
	 * @param userId
	 * @return
	 */
	PersonInfo getPersonInfoById(Long userId);
}

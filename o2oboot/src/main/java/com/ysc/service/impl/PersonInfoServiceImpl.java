package com.ysc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ysc.dao.PersonInfoDao;
import com.ysc.entity.PersonInfo;
import com.ysc.service.PersonInfoService;

@Service
public class PersonInfoServiceImpl implements PersonInfoService{
	
	@Autowired
	private PersonInfoDao personInfoDao;

	@Override
	public PersonInfo getPersonInfoById(Long userId) {
		
		return personInfoDao.queryPersonInfoById(userId);
	}
	
}

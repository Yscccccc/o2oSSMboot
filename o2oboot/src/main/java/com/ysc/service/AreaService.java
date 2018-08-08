package com.ysc.service;

import java.util.List;

import com.ysc.entity.Area;

public interface AreaService {
	
	public static final String AREALISTKEY = "arealist";
	
	List<Area> getAreaList();
}

package com.ysc.dao;

import java.util.List;

import com.ysc.entity.Area;

public interface AreaDao {
	/**
	 * 列出区域列表
	 * @return areaList
	 */
	List<Area> queryArea();
}

package com.ysc.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ysc.entity.Area;
import com.ysc.service.AreaService;
import com.ysc.service.CacheService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AreaServiceTest {

	@Autowired
	private AreaService areaService;
	
	@Autowired
	private CacheService cacheService;
	@Test
	public void testGetAreaList() {
		List<Area> areaList = areaService.getAreaList();
		assertEquals("南苑",areaList.get(0).getAreaName());
		cacheService.removeFromCache(areaService.AREALISTKEY);
		areaList = areaService.getAreaList();
	}

}

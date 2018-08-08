package com.ysc.o2o.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ysc.o2o.BaseTest;
import com.ysc.o2o.entity.HeadLine;

public class HeadLineServiceTest extends BaseTest{
	
	@Autowired
	private HeadLineService headLineService;
	@Test
	public void testGetHeadLineList() {
		
		List<HeadLine> headLineList = headLineService.getHeadLineList(new HeadLine());
		assertEquals(1, headLineList.size());
	}

}

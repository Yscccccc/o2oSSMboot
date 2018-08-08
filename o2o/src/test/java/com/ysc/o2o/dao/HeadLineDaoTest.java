package com.ysc.o2o.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ysc.o2o.BaseTest;
import com.ysc.o2o.entity.HeadLine;

public class HeadLineDaoTest extends BaseTest{
	
	@Autowired
	private HeadLineDao headLineDao;
	
	@Test
	public void testQueryHeadLine() {
		List<HeadLine> headLineList = headLineDao.queryHeadLine(new HeadLine());
		assertEquals(1, headLineList.size());
	}

	@Test
	public void testQueryHeadLineById() {
		fail("Not yet implemented");
	}

	@Test
	public void testQueryHeadLineByIds() {
		fail("Not yet implemented");
	}

	@Test
	public void testInsertHeadLine() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateHeadLine() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteHeadLine() {
		fail("Not yet implemented");
	}

	@Test
	public void testBatchDeleteHeadLine() {
		fail("Not yet implemented");
	}

}

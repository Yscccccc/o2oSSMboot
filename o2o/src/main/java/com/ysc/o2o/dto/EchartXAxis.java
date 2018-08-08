package com.ysc.o2o.dto;

import java.util.TreeSet;

/**
 * 迎合echart里的xAxis
 * @author chenshuying
 *
 */
public class EchartXAxis {
	
	private String type = "category";
	//为了去重
	private TreeSet<String> data;
	
	public TreeSet<String> getData() {
		return data;
	}
	public void setData(TreeSet<String> data) {
		this.data = data;
	}
	public String getType() {
		return type;
	}

}

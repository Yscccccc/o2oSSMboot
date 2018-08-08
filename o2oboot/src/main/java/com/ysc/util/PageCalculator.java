package com.ysc.util;

public class PageCalculator {
	public static int calculaterRowIndex(int pageIndex, int pageSize) {
		return (pageIndex > 0) ? (pageIndex - 1) * pageSize : 0;
	}
}

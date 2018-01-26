package com.commons.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 分页类
 * 
 * @author wenwen
 * @param <T>
 * 
 * 2015年8月12日 下午11:39:39
 */
public class Page<T> {
	private long totalNum; // 总记录数

	private long lastPage; // 最后一页页数

	private long firstPage; // 第一页

	private int pageNow; // 当前页数

	private int pageSize; // 一页包含多少条记录

	private long pageCount; // 总页数

	private String orderAttr; //排序字段

	private String orderType; //正序 or 倒序

	private List<T> content = Collections.emptyList(); // 当前页中存放的记录

	private List<Object> continuePage = Collections.emptyList(); //连续显示的页数

	public Page() {

	}

	public Page(long totalNum) {
		this.totalNum = totalNum;
	}

	public Page(int pageNow, int pageSize) {
		this.pageNow = pageNow;
		this.pageSize = pageSize;
	}
	
	public Page(long totalNum, int pageSize, int pageNow) {
		this.totalNum = totalNum;
		this.pageSize = pageSize;
		this.pageCount = totalNum / pageSize;
		// 总页数的判断修改
		long s = totalNum % pageSize;
		long c = totalNum / pageSize;
		if (this.pageCount == 0) {
			this.pageCount = 1;
		} else if (s == 0) {
			this.pageCount = c;
		} else {
			this.pageCount++;
		}
		this.pageNow = pageNow > this.pageCount ? (int)this.pageCount : pageNow;
		this.lastPage = this.pageCount;
	}

	public Page(long totalNum, int pageSize, int pageNow, String orderAttr, String orderType) {
		this(totalNum, pageSize, pageNow);
		this.continuePage = this.getContinuePage(this.pageNow, (int)this.pageCount);
		this.orderAttr = orderAttr;
		this.orderType = orderType;
	}

	public long getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(long totalNum) {
		this.totalNum = totalNum;
	}

	public long getLastPage() {
		return lastPage;
	}

	public void setLastPage(long lastPage) {
		this.lastPage = lastPage;
	}

	public long getFirstPage() {
		return firstPage;
	}

	public void setFirstPage(long firstPage) {
		this.firstPage = firstPage;
	}

	public int getPageNow() {
		return pageNow;
	}

	public void setPageNow(int pageNow) {
		this.pageNow = pageNow;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getPageCount() {
		return pageCount;
	}

	public void setPageCount(long pageCount) {
		this.pageCount = pageCount;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	public List<Object> getContinuePage() {
		return continuePage;
	}

	public void setContinuePage(List<Object> continuePage) {
		this.continuePage = continuePage;
	}

	public String getOrderAttr() {
		return orderAttr;
	}

	public void setOrderAttr(String orderAttr) {
		this.orderAttr = orderAttr;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	private List<Object> getContinuePage(int pageNow, int pageCount) {
		List<Object> list = new ArrayList<Object>();
		Integer flag = 0;
		int continuePage = 10;
		if(continuePage > pageCount) {
			continuePage = pageCount;
		}
		if (pageNow > pageCount){
			pageNow = pageCount;
		}
		if (pageNow > (continuePage/2 + 1) && pageNow <= (pageCount - continuePage/2)) {
			flag = pageNow - (continuePage/2 + 1);
		}else if (pageNow > (pageCount - continuePage/2)){
			flag = pageNow -(continuePage - (pageCount - pageNow));
		}
		for (int i = 0; i < continuePage; i++) {
			flag += 1;
			list.add(flag);
		}

		return list;
	}

}

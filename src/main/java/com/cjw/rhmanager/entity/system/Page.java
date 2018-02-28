package com.cjw.rhmanager.entity.system;

import com.cjw.rhmanager.utils.ParamData;


public class Page {
	//当前页
	private int pageCur;
	//每页显示记录数
	private int pageSize;
	//总记录数
	private int totalNum;
	//总页数
	private int pageMax;
	//当前记录起始索引，组装sql时使用
	private int indexCur;
	//查询条件
	private ParamData pd = new ParamData();
	
	public int getPageCur() {
		return pageCur;
	}
	public void setPageCur(int pageCur) {
		this.pageCur = pageCur;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public int getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	public int getPageMax() {
		if(totalNum%pageSize==0)
			pageMax = totalNum/pageSize;
		else
			pageMax = totalNum/pageSize+1;
		return pageMax;
	}
	public void setPageMax(int pageMax) {
		this.pageMax = pageMax;
	}
	public int getIndexCur() {
		indexCur = (getPageCur()-1)*getPageSize();
		if(indexCur<0)
			indexCur = 0;
		return indexCur;
	}
	public void setIndexCur(int indexCur) {
		this.indexCur = indexCur;
	}
	public ParamData getPd() {
		return pd;
	}
	public void setPd(ParamData pd) {
		this.pd = pd;
	}
	public Page(){
		
	}
	public Page(int pageCur, int pageSize, ParamData pd) {
		super();
		this.pageCur = pageCur;
		this.pageSize = pageSize;
		this.pd = pd;
	}
}
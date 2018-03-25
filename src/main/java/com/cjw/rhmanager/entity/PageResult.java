package com.cjw.rhmanager.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/25.
 */
public class PageResult<T> {
    //成功标志
    private boolean success;
    //返回说明
    private String message;
    //每页显示记录数
    private int pageSize;
    //总页数
    private int pageMax;
    //总记录数
    private int totalNum;
    //当前页
    private int pageCur;
    //数据集合
    private List<T> pageList;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getPageMax() {
        if(pageSize==0){
            return pageMax;
        }
        if(totalNum%pageSize==0)
            pageMax = totalNum/pageSize;
        else
            pageMax = totalNum/pageSize+1;
        return pageMax;
    }

    public void setPageMax(int pageMax) {
        this.pageMax = pageMax;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getPageCur() {
        if(pageCur<=0)
            pageCur = 1;
        if(pageCur>getPageMax())
            pageCur = getPageMax();
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

    public List<T> getPageList() {
        return pageList;
    }

    public void setPageList(List<T> pageList) {
        this.pageList = pageList;
    }

    public PageResult(int pageCur, int pageSize, List<T> reportList) {
        super();
        this.pageCur = pageCur;
        this.pageSize = pageSize;
        this.pageList = reportList;
    }
    public PageResult(int pageCur, int pageSize, int totalNum, List<T> reportList) {
        super();
        this.success = true;
        this.pageCur = pageCur;
        this.pageSize = pageSize;
        this.totalNum = totalNum;
        this.pageList = reportList;
    }
    public PageResult(boolean success, String message) {
        super();
        this.success = success;
        this.message = message;
    }
}

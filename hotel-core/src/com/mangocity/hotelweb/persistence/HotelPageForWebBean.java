package com.mangocity.hotelweb.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class HotelPageForWebBean implements Serializable {

    // 多少条记录
    private int pageSize;

    // 当前页
    private int pageIndex;

    // 总页数
    private int totalIndex;

    // 汇率
    private String rateStr;

    // 查询的结果
    private List<QueryHotelForWebResult> list = new ArrayList<QueryHotelForWebResult>();

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalIndex() {
        return totalIndex;
    }

    public void setTotalIndex(int totalIndex) {
        this.totalIndex = totalIndex;
    }

    public List<QueryHotelForWebResult> getList() {
        return list;
    }

    public void setList(List<QueryHotelForWebResult> list) {
        this.list = list;
    }

    public String getRateStr() {
        return rateStr;
    }

    public void setRateStr(String rateStr) {
        this.rateStr = rateStr;
    }
}

package com.mangocity.webnew.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class HotelPageForSalesBean implements Serializable {
	
	private static final long serialVersionUID = -8633845382623533121L;

    // 每页多少条记录
    private int pageSize;

    // 当前页
    private int pageIndex;

    // 总页数
    private int totalIndex;

    // 汇率
    private String rateStr;
    
    /**
     * 总共记录数
     */
    private int totalSize;

    // 查询的结果
    private List<QueryHotelForSalesResult> list = new ArrayList<QueryHotelForSalesResult>();

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

    public List<QueryHotelForSalesResult> getList() {
		return list;
	}

	public void setList(List<QueryHotelForSalesResult> list) {
		this.list = list;
	}

	public String getRateStr() {
        return rateStr;
    }

    public void setRateStr(String rateStr) {
        this.rateStr = rateStr;
    }

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}
}

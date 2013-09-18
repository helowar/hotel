package com.mangocity.webnew.persistence;

import java.util.List;

/**
 * add by shengwei.zuo 
 */
public class HtlCalendarRowsBean {

    //行数
    private int rowNumber;

    // 批次释放id
    private List lstColsPrice;

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public List getLstColsPrice() {
		return lstColsPrice;
	}

	public void setLstColsPrice(List lstColsPrice) {
		this.lstColsPrice = lstColsPrice;
	}

   
}

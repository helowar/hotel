package com.mangocity.hweb.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class AdditionalServeItem implements Serializable {
    // 有效日期字符串
    private String validDate;

    // 金额
    private double amount;

    // 类型(暂时用于加床价)
    private String addType;

    // 早餐类型
    private String breakfastType;
    
    //网站改版，订单详情页面用,针对酒店给出一个时间段的早餐价格
    private Date beginDate;
    
    private Date endDate;

    public String getAddType() {
        return addType;
    }

    public void setAddType(String addType) {
        this.addType = addType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getBreakfastType() {
        return breakfastType;
    }

    public void setBreakfastType(String breakfastType) {
        this.breakfastType = breakfastType;
    }

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}

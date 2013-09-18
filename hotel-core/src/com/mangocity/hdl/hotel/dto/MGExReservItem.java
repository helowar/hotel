package com.mangocity.hdl.hotel.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MGExReservItem", propOrder = { 
		"result",
		"dayIndex", 
		"ableSaleDate", 
		"basePrice",
		"salePrice", 
		"netPrice", 
		"change", 
		"firstDayPrice",
		"rmbBaseprice",
		"rmbSalePrice"
})
public class MGExReservItem {
	
	protected Result result;
	
    protected int dayIndex; // 日期序号

    protected String ableSaleDate;

    protected float basePrice;

    protected float salePrice;

    /**
     * 净房价
     */
    protected float netPrice;

    protected int change;

    protected float firstDayPrice;
    
    //暂时只有中旅用到以下属性begin
    protected float rmbBaseprice;// 人民币底价

    protected float rmbSalePrice;// 人民币销售价
    //暂时只有中旅用到以下属性end
    /**
     * Gets the value of the dayIndex property.
     * 
     */
    public int getDayIndex() {
        return dayIndex;
    }

    /**
     * Sets the value of the dayIndex property.
     * 
     */
    public void setDayIndex(int value) {
        this.dayIndex = value;
    }

    /**
     * Gets the value of the basePrice property.
     * 
     */
    public float getBasePrice() {
        return basePrice;
    }

    /**
     * Sets the value of the basePrice property.
     * 
     */
    public void setBasePrice(float value) {
        this.basePrice = value;
    }

    /**
     * Gets the value of the salePrice property.
     * 
     */
    public float getSalePrice() {
        return salePrice;
    }

    /**
     * Sets the value of the salePrice property.
     * 
     */
    public void setSalePrice(float value) {
        this.salePrice = value;
    }

    /**
     * Gets the value of the change property.
     * 
     */
    public int getChange() {
        return change;
    }

    /**
     * Sets the value of the change property.
     * 
     */
    public void setChange(int value) {
        this.change = value;
    }

    /**
     * Gets the value of the firstDayPrice property.
     * 
     */
    public float getFirstDayPrice() {
        return firstDayPrice;
    }

    /**
     * Sets the value of the firstDayPrice property.
     * 
     */
    public void setFirstDayPrice(float value) {
        this.firstDayPrice = value;
    }

    public String getAbleSaleDate() {
        return ableSaleDate;
    }

    public void setAbleSaleDate(String ableSaleDate) {
        this.ableSaleDate = ableSaleDate;
    }

    public float getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(float netPrice) {
        this.netPrice = netPrice;
    }

	public float getRmbBaseprice() {
		return rmbBaseprice;
	}

	public void setRmbBaseprice(float rmbBaseprice) {
		this.rmbBaseprice = rmbBaseprice;
	}

	public float getRmbSalePrice() {
		return rmbSalePrice;
	}

	public void setRmbSalePrice(float rmbSalePrice) {
		this.rmbSalePrice = rmbSalePrice;
	}

	public Result getResult() {
		if(null == result){
    		result = new Result();
    	}
        return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

}

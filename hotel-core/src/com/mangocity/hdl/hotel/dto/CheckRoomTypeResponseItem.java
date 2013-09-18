package com.mangocity.hdl.hotel.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CheckRoomTypeResponseItem", propOrder = { 
		"result",
		"qty",
		"date",
		"dayIndex",
		"sCutoff",
		"maxQty",
		"sDupNameFlg",
		"nMinNite"
})
public class CheckRoomTypeResponseItem {
	
	private Result result;
	
    private int qty; // 房间数量

    private String date; // 当天日期 yyyy-MM-dd

    private int dayIndex; // 日期序号

    private String sCutoff; // 此房间有效时间

    /**
     * 最大可订数
     * 
     * @author chenkeming Mar 17, 2009 6:34:33 PM
     */
    private int maxQty;

    private String sDupNameFlg;// 允许重复标识 Y:允许重复

    private int nMinNite;

	public Result getResult() {
		if(null == result){
    		result = new Result();
    	}
        return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getDayIndex() {
		return dayIndex;
	}

	public void setDayIndex(int dayIndex) {
		this.dayIndex = dayIndex;
	}

	public String getSCutoff() {
		return sCutoff;
	}

	public void setSCutoff(String cutoff) {
		sCutoff = cutoff;
	}

	public int getMaxQty() {
		return maxQty;
	}

	public void setMaxQty(int maxQty) {
		this.maxQty = maxQty;
	}

	public String getSDupNameFlg() {
		return sDupNameFlg;
	}

	public void setSDupNameFlg(String dupNameFlg) {
		sDupNameFlg = dupNameFlg;
	}

	public int getNMinNite() {
		return nMinNite;
	}

	public void setNMinNite(int minNite) {
		nMinNite = minNite;
	}
    
}

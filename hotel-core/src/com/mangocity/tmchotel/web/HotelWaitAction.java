package com.mangocity.tmchotel.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.order.web.GenericCCAction;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.hweb.persistence.HotelPageForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebBean;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.hotel.constant.FromChannelType;
import com.mangocity.tmchotel.service.HotelQueryService;

/**
 * 查询等待页面Action
 * @author lixiangyang
 *
 */
public class HotelWaitAction extends GenericCCAction{
	
	private static final long serialVersionUID = -8331487657049289041L;
	
	private String cityCode;

	private String bizCode;
	
	// 处理seo传来的商业区
	private String bizZone;
	
	private String bizFlag;
	
	private String priceFlag;
	
	private String bizName;

	private String chkHotelStar;
	
	private String formalStarLevels;

	private String mapSearchOrNot;

	private String priceStrValue;
	
	 /**
     * 最低价
     */
    private String minPrice;

    /**
     * 最高价
     */
    private String maxPrice;

	/**
     * 城市ID
     */
    private String cityId;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 酒店ID
     */
    private long hotelId;

    /**
     * 酒店名称
     */
    private String hotelName;

    /**
     * 行政区代码
     */
    private String district;

    /**
     * 入住日期
     */
    private String inDate;

    /**
     * 离店日期
     */
    private String outDate;

    /**
     * 价格范围
     */
    private String priceStr;

    /**
     * 酒店星级
     */
    private String hotelStar;

    /**
     * 酒店星级 -- 挂牌星级
     */
    private String formalStarLevel;

    /**
     * 酒店星级 -- 未评星级(准星级)
     */
    private String informalStarLevel;

    /**
     * 特殊要求，目前网站查询有:宽带3，最近开业/装修4， 中间用","分割
     */
    private String specialRequest;

    /**
     * 酒店主题
     */
    private String theme;

    /**
     * 酒店排列顺序方式:<br>
     * 芒果推荐1 <br>
     * 价格2 <br>
     * 星级3 <br>
     * 缺省是按芒果推荐方式
     */
    private int qrymethod = 1;

    /**
     * 房型排列顺序<br>
     * 1: 严格按价格排序 0: 正常排序
     */
    private int priceOrder = 0;
  /**
     * 酒店类型 hotel2.9.2 add by chenjiajie 2009-07-23 商务酒店:01
     * ;商务酒店:02;酒店式公寓:03;酒店式公寓:04;经济型酒店:05;主题酒店:06
     */
    private String hotelType;
    
    private int toPageIndex;
    
    /**
     * 品牌ID
     * add by haibo.li 网站改版
     */
    private String parentId;
    
    private String cityComparisonId;

	public String getCityComparisonId() {
		return cityComparisonId;
	}


	public void setCityComparisonId(String cityComparisonId) {
		this.cityComparisonId = cityComparisonId;
	}


	public int getToPageIndex() {
		return toPageIndex;
	}


	public void setToPageIndex(int toPageIndex) {
		this.toPageIndex = toPageIndex;
	}


	public String execute(){
		
		return SUCCESS;
	}
	

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public int getQrymethod() {
		return qrymethod;
	}

	public void setQrymethod(int qrymethod) {
		this.qrymethod = qrymethod;
	}

	public String getPriceStrValue() {
		return priceStrValue;
	}

	public void setPriceStrValue(String priceStrValue) {
		this.priceStrValue = priceStrValue;
	}

	public String getMapSearchOrNot() {
		return mapSearchOrNot;
	}

	public void setMapSearchOrNot(String mapSearchOrNot) {
		this.mapSearchOrNot = mapSearchOrNot;
	}

	public String getBizName() {
		return bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getInDate() {
		return inDate;
	}

	public void setInDate(String inDate) {
		this.inDate = inDate;
	}

	public String getOutDate() {
		return outDate;
	}

	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}

	public String getBizCode() {
		return bizCode;
	}

	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}

	public String getBizZone() {
		return bizZone;
	}

	public void setBizZone(String bizZone) {
		this.bizZone = bizZone;
	}

	public String getBizFlag() {
		return bizFlag;
	}

	public void setBizFlag(String bizFlag) {
		this.bizFlag = bizFlag;
	}

	public String getPriceFlag() {
		return priceFlag;
	}

	public void setPriceFlag(String priceFlag) {
		this.priceFlag = priceFlag;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public String getFormalStarLevel() {
		return formalStarLevel;
	}

	public void setFormalStarLevel(String formalStarLevel) {
		this.formalStarLevel = formalStarLevel;
	}

	public String getInformalStarLevel() {
		return informalStarLevel;
	}

	public void setInformalStarLevel(String informalStarLevel) {
		this.informalStarLevel = informalStarLevel;
	}

	public String getPriceStr() {
		return priceStr;
	}

	public void setPriceStr(String priceStr) {
		this.priceStr = priceStr;
	}

	public String getChkHotelStar() {
		return chkHotelStar;
	}

	public void setChkHotelStar(String chkHotelStar) {
		this.chkHotelStar = chkHotelStar;
	}

	public String getFormalStarLevels() {
		return formalStarLevels;
	}

	public void setFormalStarLevels(String formalStarLevels) {
		this.formalStarLevels = formalStarLevels;
	}

	public String getSpecialRequest() {
		return specialRequest;
	}

	public void setSpecialRequest(String specialRequest) {
		this.specialRequest = specialRequest;
	}

	public long getHotelId() {
		return hotelId;
	}

	public void setHotelId(long hotelId) {
		this.hotelId = hotelId;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getHotelStar() {
		return hotelStar;
	}

	public void setHotelStar(String hotelStar) {
		this.hotelStar = hotelStar;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public int getPriceOrder() {
		return priceOrder;
	}

	public void setPriceOrder(int priceOrder) {
		this.priceOrder = priceOrder;
	}

	public String getHotelType() {
		return hotelType;
	}

	public void setHotelType(String hotelType) {
		this.hotelType = hotelType;
	}


	public String getMinPrice() {
		return minPrice;
	}


	public void setMinPrice(String minPrice) {
		this.minPrice = minPrice;
	}


	public String getMaxPrice() {
		return maxPrice;
	}


	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}


	public String getParentId() {
		return parentId;
	}


	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

}

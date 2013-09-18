package com.mangocity.client.hotel.gwt.queryCondition;

import com.mangocity.hotel.search.vo.SerializableVO;



/**
 * 与QueryHotelCondition一样，多了个序列化和构造函数，date型改为string
 * @aim 不改变原来的结构
 * @author houdiandian
 *
 */
public class GWTQueryCondition implements SerializableVO {

	public GWTQueryCondition(){}
	
	/**
	 * 酒店ID
	 */
	protected String hotelId;
	/**
	 * 城市代码（三字码）
	 */
	protected String cityCode;
	
	/**
	 * 城市名称(给seo用)
	 */
	protected String cityName;

	/**
	 * 入住日期
	 */
	protected String inDate;

	/**
	 * 离店日期
	 */
	protected String outDate;

	/**
	 * 酒店名称
	 */
	protected String hotelName;
	
	/**
	 * 最低价格
	 */
	protected String minPrice;

	/**
	 * 最高价格
	 */
	protected String maxPrice;

	/**
	 * 酒店星级
	 */
	protected String starLeval;

	/**
	 * 特殊要求
	 */
	protected String specialRequestString;

	/**
	 * 酒店类型
	 */
	protected String hotelType;

	/**
	 * 行政区代码
	 */
	protected String district;

	/**
	 * 商业区代码
	 */
	protected String bizZone;

	/**
	 * 查询渠道
	 */
	protected String fromChannel;
	
	/**
	 * 酒店集团,对应界面的酒店品牌
	 */
	protected String hotelGroup;
	
	/**
	 * 优惠立减
	 */
		
	/**
	 * 支付方式
	 */
	protected String payMethod;
	/**
	 * 1推荐,2,价格,3,星级，默认是推荐
	 */
	protected int sorttype = 1;
	 /**
	  * 1代表升序,2代表降序,默认是推荐从高到底,价格从低到高,星级从高到底
	  */
	protected int sortUpOrDown = 1;
	/**
     * 多少条记录/页，默认15个酒店/页
     */
	protected int pageSize = 15;
    
    /**
     * 页码
     */
	protected int pageNo = 1;
	
	// 地理位置信息的id
	protected String geoId;
	
	// 地理位置信息的名称
	protected String geoName;
	
		//促销类型，醒狮计划所增加
	protected int promoteType;
	
	/**
	 * 酒店来源渠道，用于返现控制 add by hushunqiang
	 */
	protected String projectCode;

	public int getPromoteType() {
		return promoteType;
	}

	public void setPromoteType(int promoteType) {
		this.promoteType = promoteType;
	}

	/**
	 * 用于标记第一次是否查到酒店，第一次找不到，只显示酒店推荐的前5个酒店。
	 */
	private boolean fagReQuery;
	public String getCityCode() {
		return cityCode;
	}
	
	

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}


	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
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

	public String getStarLeval() {
		return starLeval;
	}

	public void setStarLeval(String starLeval) {
		this.starLeval = starLeval;
	}



	public String getHotelType() {
		return hotelType;
	}

	public void setHotelType(String hotelType) {
		this.hotelType = hotelType;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getBizZone() {
		return bizZone;
	}

	public void setBizZone(String bizZone) {
		this.bizZone = bizZone;
	}

	public String getFromChannel() {
		return fromChannel;
	}

	public void setFromChannel(String fromChannel) {
		this.fromChannel = fromChannel;
	}

	public String getSpecialRequestString() {
		return specialRequestString;
	}

	public void setSpecialRequestString(String specialRequestString) {
		this.specialRequestString = specialRequestString;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public int getSorttype() {
		return sorttype;
	}

	public void setSorttype(int sorttype) {
		this.sorttype = sorttype;
	}

	public int getSortUpOrDown() {
		return sortUpOrDown;
	}

	public void setSortUpOrDown(int sortUpOrDown) {
		this.sortUpOrDown = sortUpOrDown;
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

	public String getGeoId() {
		return geoId;
	}

	public void setGeoId(String geoId) {
		this.geoId = geoId;
	}

	public String getGeoName() {
		return geoName;
	}

	public void setGeoName(String geoName) {
		this.geoName = geoName;
	}

	public String getHotelGroup() {
		return hotelGroup;
	}

	public void setHotelGroup(String hotelGroup) {
		this.hotelGroup = hotelGroup;
	}

	public String getHotelId() {
		return hotelId;
	}

	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public boolean isFagReQuery() {
		return fagReQuery;
	}



	public void setFagReQuery(boolean fagReQuery) {
		this.fagReQuery = fagReQuery;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
}

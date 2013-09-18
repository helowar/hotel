package com.mangocity.hweb.persistence;

import java.io.Serializable;
import java.util.Date;


public class HotelBookingInfoForHkSale implements Serializable {
	
	//售价
	private double salePrice;
	
	//酒店id
	private Long hotelId;
	
	//最早可预订日期
	private Date firstBookableDate;
	
	//最早可预订时间
	private String firstBookableTime;
	
	//最晚可预订日期
	private Date latestBookableDate;
	
	//最晚可预订时间
	private String latestBokableTime;
	
    //是否需要担保
	private String needAssure;
	
	//必住最晚日期
	private Date mustLastDate;
	
	//必住最早日期
	private Date mustFirstDate;
	
	//连住日期
	private int continueDay;
	
	//必住日期
	private String mustInDate;
	
	//是否有预订条款
	private int hasReserv;
	
	//最少限住几晚
	private int minRestrictNights;
	
	//最多限住几晚
	private int maxRestrictNights;
	
	//必住日期的关系 or  或者 and
	private String continueDatesRelation;
	
	//开关房标志
	private String closeFlag;
	
	//支付方式
	private String payMethod;
	
	//是否在外网显示预付
	private String webPrepayShow;
	
	//房态
	private String roomState;
	
	//酒店中文名称
	private String  hotelChnName;
	
	//商业区
	private String  bizZone;
	
	private String zone;
	
	//酒店星级
	private String hotelStar;
	
	//城市三字码
	private String cityCode;
	
	//价格类型Id
	private  String childRoomTypeId;
	
	private String isHkRoomType;
	
	//中旅酒店映射编码是否有效，普通酒店为null、
	private  String mappingIsActive;
	
	//合同币种
	private  String currency;
	
	//推荐级别
	private  String tuiJian;
	
	//配额数
	private String sumquota;
	
	
	public double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

	public Long getHotelId() {
		return hotelId;
	}

	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}

	public Date getLatestBookableDate() {
		return latestBookableDate;
	}

	public void setLatestBookableDate(Date latestBookableDate) {
		this.latestBookableDate = latestBookableDate;
	}

	public String getLatestBokableTime() {
		return latestBokableTime;
	}

	public void setLatestBokableTime(String latestBokableTime) {
		if(null == latestBokableTime){
			this.latestBokableTime = "";
		}else{
			this.latestBokableTime = latestBokableTime;
		}
		
	}

	public String getNeedAssure() {
		return needAssure;
	}

	public void setNeedAssure(String needAssure) {
		this.needAssure = needAssure;
	}

	public Date getMustLastDate() {
		return mustLastDate;
	}

	public void setMustLastDate(Date mustLastDate) {
		this.mustLastDate = mustLastDate;
	}

	public Date getMustFirstDate() {
		return mustFirstDate;
	}

	public void setMustFirstDate(Date mustFirstDate) {
		this.mustFirstDate = mustFirstDate;
	}

	public int getContinueDay() {
		return continueDay;
	}

	public void setContinueDay(int continueDay) {
		this.continueDay = continueDay;
	}

	public String getMustInDate() {
		return mustInDate;
	}

	public void setMustInDate(String mustInDate) {
		if(null == mustInDate){
			this.mustInDate = "";
		}else{
			this.mustInDate = mustInDate;
		}
	}

	public int getHasReserv() {
		return hasReserv;
	}

	public void setHasReserv(int hasReserv) {
		this.hasReserv = hasReserv;
	}

	public int getMinRestrictNights() {
		return minRestrictNights;
	}

	public void setMinRestrictNights(int minRestrictNights) {
		this.minRestrictNights = minRestrictNights;
	}

	public int getMaxRestrictNights() {
		return maxRestrictNights;
	}

	public void setMaxRestrictNights(int maxRestrictNights) {
		this.maxRestrictNights = maxRestrictNights;
	}

	public String getContinueDatesRelation() {
		return continueDatesRelation;
	}

	public void setContinueDatesRelation(String continueDatesRelation) {
		this.continueDatesRelation = continueDatesRelation;
	}

	public String getCloseFlag() {
		return closeFlag;
	}

	public void setCloseFlag(String closeFlag) {
		if(closeFlag == null){
			this.closeFlag = "";
		}else{
			this.closeFlag = closeFlag;
		}
	}

	public String getRoomState() {
		return roomState;
	}

	public void setRoomState(String roomState) {
		this.roomState = roomState;
	}

	public String getSumquota() {
		return sumquota;
	}

	public void setSumquota(String sumquota) {
		this.sumquota = sumquota;
	}

	public String getHotelChnName() {
		return hotelChnName;
	}

	public void setHotelChnName(String hotelChnName) {
		this.hotelChnName = hotelChnName;
	}

	public String getBizZone() {
		return bizZone;
	}

	public void setBizZone(String bizZone) {
		if(null == bizZone){
			this.bizZone = "";
		}else{
			this.bizZone = bizZone;
		}
		
	}

	public String getHotelStar() {
		return hotelStar;
	}

	public void setHotelStar(String hotelStar) {
		if(null == hotelStar){
			this.hotelStar = "";
		}else{
			this.hotelStar = hotelStar;
		}
		
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public Date getFirstBookableDate() {
		return firstBookableDate;
	}

	public void setFirstBookableDate(Date firstBookableDate) {
		this.firstBookableDate = firstBookableDate;
	}

	public String getFirstBookableTime() {
		return firstBookableTime;
	}

	public void setFirstBookableTime(String firstBookableTime) {
		if(null == firstBookableTime){
			this.firstBookableTime = "";
		}else{
			this.firstBookableTime = firstBookableTime;
		}
		
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		if(null == zone){
			this.zone = "";
		}else{
			this.zone = zone;
		}
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getWebPrepayShow() {
		return webPrepayShow;
	}

	public void setWebPrepayShow(String webPrepayShow) {
		this.webPrepayShow = webPrepayShow;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getTuiJian() {
		return tuiJian;
	}

	public void setTuiJian(String tuiJian) {
		this.tuiJian = tuiJian;
	}

	public String getChildRoomTypeId() {
		return childRoomTypeId;
	}

	public void setChildRoomTypeId(String childRoomTypeId) {
		this.childRoomTypeId = childRoomTypeId;
	}

	public String getMappingIsActive() {
		return mappingIsActive;
	}

	public void setMappingIsActive(String mappingIsActive) {
		this.mappingIsActive = mappingIsActive;
	}

	public String getIsHkRoomType() {
		return isHkRoomType;
	}

	public void setIsHkRoomType(String isHkRoomType) {
		this.isHkRoomType = isHkRoomType;
	}
	
}

package com.mangocity.hweb.persistence;

import java.io.Serializable;
import java.util.List;

/**
 */
public class HotelInfoForNewWeb implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//酒店电话
    private String telephone;
    
    //酒店星级类型
    private String starType;
    
    //酒店标志图片名称
    private String logoPictureName;
    
    //酒店可用信用卡
    private String creditCard;
    
    //酒店所在商业区中文名
    private String districtName;
    
    //酒店总房间数
    private String roomCount;
    
    //组装开业年份、装修年份字符串
    private String praciceAndFitmentStr;
    
    //酒店餐饮休闲设施
    private String mealFixtrue;
    
    //酒店客房设施
    private String roomFixtrue;
    
    //酒店免费服务
    private String freeService;
    
    //酒店残疾人设施
    private String handicappedFixtrue;
    
    //用来存放cookie中存放的酒店名称和酒店Id，存放的是数组
    private List hotelNameAndIdStr;
    
    //酒店所在城市的行政区
    private List businessList;
    
    //酒店所在城市的商业区
    private List districtList;
    
    //酒店详情页面附加信息
    private String serviceStr;
    
    //酒店详细信息
    private String hotelIntroduce;
    
    //电子地图用begin
    private String longitude;
    
    private String latitude;
    
    private String freeServiceRemark;
    
    private String roomFixtrueRemark;
    //电子地图用end
    
    private String chnAddress;
    
    private String cityName;
    
    //酒店网址 TMC-v2.0 add by shengwei.zuo  2010-3-11
    private String website;
    
      /**
     * 酒店网站 诺曼底加入酒店网站的显示
     * add by haibo.li
     */
    private String hotelNet;

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getChnAddress() {
		return chnAddress;
	}

	public void setChnAddress(String chnAddress) {
		this.chnAddress = chnAddress;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getStarType() {
		return starType;
	}

	public void setStarType(String starType) {
		this.starType = starType;
	}

	public String getLogoPictureName() {
		return logoPictureName;
	}

	public void setLogoPictureName(String logoPictureName) {
		this.logoPictureName = logoPictureName;
	}

	public String getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getRoomCount() {
		return roomCount;
	}

	public void setRoomCount(String roomCount) {
		this.roomCount = roomCount;
	}

	public String getPraciceAndFitmentStr() {
		return praciceAndFitmentStr;
	}

	public void setPraciceAndFitmentStr(String praciceAndFitmentStr) {
		this.praciceAndFitmentStr = praciceAndFitmentStr;
	}

	public String getMealFixtrue() {
		return mealFixtrue;
	}

	public void setMealFixtrue(String mealFixtrue) {
		this.mealFixtrue = mealFixtrue;
	}

	public String getRoomFixtrue() {
		return roomFixtrue;
	}

	public void setRoomFixtrue(String roomFixtrue) {
		this.roomFixtrue = roomFixtrue;
	}

	public String getFreeService() {
		return freeService;
	}

	public void setFreeService(String freeService) {
		this.freeService = freeService;
	}

	public String getHandicappedFixtrue() {
		return handicappedFixtrue;
	}

	public void setHandicappedFixtrue(String handicappedFixtrue) {
		this.handicappedFixtrue = handicappedFixtrue;
	}

	public List getHotelNameAndIdStr() {
		return hotelNameAndIdStr;
	}

	public void setHotelNameAndIdStr(List hotelNameAndIdStr) {
		this.hotelNameAndIdStr = hotelNameAndIdStr;
	}

	public List getBusinessList() {
		return businessList;
	}

	public void setBusinessList(List businessList) {
		this.businessList = businessList;
	}

	public List getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List districtList) {
		this.districtList = districtList;
	}

	public String getServiceStr() {
		return serviceStr;
	}

	public void setServiceStr(String serviceStr) {
		this.serviceStr = serviceStr;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getFreeServiceRemark() {
		return freeServiceRemark;
	}

	public void setFreeServiceRemark(String freeServiceRemark) {
		this.freeServiceRemark = freeServiceRemark;
	}

	public String getRoomFixtrueRemark() {
		return roomFixtrueRemark;
	}

	public void setRoomFixtrueRemark(String roomFixtrueRemark) {
		this.roomFixtrueRemark = roomFixtrueRemark;
	}

	public String getHotelIntroduce() {
		return hotelIntroduce;
	}

	public void setHotelIntroduce(String hotelIntroduce) {
		this.hotelIntroduce = hotelIntroduce;
	}
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}
	public String getHotelNet() {
		return hotelNet;
	}

	public void setHotelNet(String hotelNet) {
		this.hotelNet = hotelNet;
	}
	
	
	

}

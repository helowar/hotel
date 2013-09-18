package com.mangocity.webnew.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 酒店网站v2.2查询结果<br>
 * 酒店类<br>
 * 
 * 注: 该类除了多了房型列表成员，实际上和存储过程返回的结果类是一致的，在页面展现中 使用该类来展现酒店相关的信息，该类中的房型相关信息，在页面展现中没有用到
 * 
 * @author chenkeming
 * 
 */
public class QueryHotelForSalesResult implements Serializable {
	
	private static final long serialVersionUID = -5670439186537957813L;

    /**
     * 酒店id
     */
    private long hotelId;

    /**
     * 酒店中文名称
     */
    private String hotelChnName;

    /**
     * 酒店英文名称
     */
    private String hotelEngName;

    /**
     * 酒店星级
     */
    private String hotelStar;

    /**
     * 酒店类型
     */
    // private String hotelType;
    /**
     * 酒店中文地址
     */
     private String chnAddress;
    /**
     * 酒店英文地址
     */
     private String engAddress;
    /**
     * 酒店介绍
     */
    private String hotelIntroduce;

   
    /**
     * 商业区
     */
    private String bizZone;

    /**
     * 城区
     */
    private String district;

    /**
     * 酒店推荐级别
     */
    private String commendType;

    /**
     * 酒店所在城市
     */
    private String city;

    /**
     * 酒店自动生成简介信息
     */
    private String autoIntroduce;
   
    /**
     * 酒店星级数目
     */
    private int starNum;

    /**
     * 酒店星级类型 1代表实心mango 2代表准星级酒店空心mango
     */
    private int starBody;

    /**
     * 房型列表，是RoomType类的集合
     */
    private List roomTypes = new ArrayList();

    /**
     * 地图标志
     */
    private long gisid;

	public long getHotelId() {
		return hotelId;
	}

	public void setHotelId(long hotelId) {
		this.hotelId = hotelId;
	}

	public String getHotelChnName() {
		return hotelChnName;
	}

	public void setHotelChnName(String hotelChnName) {
		this.hotelChnName = hotelChnName;
	}

	public String getHotelEngName() {
		return hotelEngName;
	}

	public void setHotelEngName(String hotelEngName) {
		this.hotelEngName = hotelEngName;
	}

	public String getHotelStar() {
		return hotelStar;
	}

	public void setHotelStar(String hotelStar) {
		this.hotelStar = hotelStar;
	}

	public String getChnAddress() {
		return chnAddress;
	}

	public void setChnAddress(String chnAddress) {
		this.chnAddress = chnAddress;
	}

	public String getEngAddress() {
		return engAddress;
	}

	public void setEngAddress(String engAddress) {
		this.engAddress = engAddress;
	}

	public String getHotelIntroduce() {
		return hotelIntroduce;
	}

	public void setHotelIntroduce(String hotelIntroduce) {
		this.hotelIntroduce = hotelIntroduce;
	}

	public String getBizZone() {
		return bizZone;
	}

	public void setBizZone(String bizZone) {
		this.bizZone = bizZone;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getCommendType() {
		return commendType;
	}

	public void setCommendType(String commendType) {
		this.commendType = commendType;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAutoIntroduce() {
		return autoIntroduce;
	}

	public void setAutoIntroduce(String autoIntroduce) {
		this.autoIntroduce = autoIntroduce;
	}

	public int getStarNum() {
		return starNum;
	}

	public void setStarNum(int starNum) {
		this.starNum = starNum;
	}

	public int getStarBody() {
		return starBody;
	}

	public void setStarBody(int starBody) {
		this.starBody = starBody;
	}

	public List getRoomTypes() {
		return roomTypes;
	}

	public void setRoomTypes(List roomTypes) {
		this.roomTypes = roomTypes;
	}

	public long getGisid() {
		return gisid;
	}

	public void setGisid(long gisid) {
		this.gisid = gisid;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
  
	
}

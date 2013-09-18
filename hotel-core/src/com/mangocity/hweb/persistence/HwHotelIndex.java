package com.mangocity.hweb.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 * 
 * 酒店网站显示数据的临时数据表
 * 
 * @author chenkeming
 */
public class HwHotelIndex implements Entity {

    private static final long serialVersionUID = -557794850181439976L;

    /**
     * 主键 <pk>
     */
    private Long ID;

    /**
     * 酒店ID
     */
    private String hotelId;

    /**
     * 省
     */
    private String state;

    /**
     * 城市
     */
    private String city;

    /**
     * 酒店中文名
     */
    private String chnName;

    /**
     * 酒店星级
     */
    private String hotelStar;

    /**
     * 推荐级别
     */
    private String commendType;

    /**
     * 合同币种
     */
    private String currency;

    /**
     * 最低价
     */
    private double lowestPrice;

    /**
     * 日期
     */
    private Date createDate;

    /**
     * 点击量
     */
    private long clickNum;

    /**
     * 订单量
     */
    private long orderNum;

    /**
     * 不同的用途标识
     */
    private String forDif;
    
    /**
     * 最低返现金额
     */
    private double lowestFavPrice;

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public String getChnName() {
        return chnName;
    }

    public void setChnName(String chnName) {
        this.chnName = chnName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getClickNum() {
        return clickNum;
    }

    public void setClickNum(long clickNum) {
        this.clickNum = clickNum;
    }

    public String getCommendType() {
        return commendType;
    }

    public void setCommendType(String commendType) {
        this.commendType = commendType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getForDif() {
        return forDif;
    }

    public void setForDif(String forDif) {
        this.forDif = forDif;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelStar() {
        return hotelStar;
    }

    public void setHotelStar(String hotelStar) {
        this.hotelStar = hotelStar;
    }

    public double getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(double lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public long getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(long orderNum) {
        this.orderNum = orderNum;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

	public double getLowestFavPrice() {
		return lowestFavPrice;
	}

	public void setLowestFavPrice(double lowestFavPrice) {
		this.lowestFavPrice = lowestFavPrice;
	}

    
}

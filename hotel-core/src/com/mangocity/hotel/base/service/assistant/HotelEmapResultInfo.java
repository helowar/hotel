package com.mangocity.hotel.base.service.assistant;

/**
 * 查询电子地图查询结果页面
 * 
 * @author guojun
 */
public class HotelEmapResultInfo {

    private long hotelId;

    /**
     * 最低价格
     */
    private Double lowestPrice;

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
     * 酒店中文地址
     */
    private String chnAddress;

    /**
     * 酒店简介
     */
    private String hotelIntroduce;

    /**
     * 酒店中文简介
     */
    private String hotelChnIntroduce;

    /**
     * 酒店推荐级别
     */
    private String hotel_comm_type;

    /**
     * 酒店所属城市
     */
    private String city;

    /**
     * 酒店所在经度
     */
    private Double longitude;

    /**
     * 酒店所在纬度
     */
    private Double latitude;

    /***
     * 这部分用来计算与中心点相差的距离
     */
    private Double distance;

    /**
     * MGIS GISID
     */
    private Long gisid;

    /**
     * 推荐级别
     */
    private Long commendType;

    public String getChnAddress() {
        return chnAddress;
    }

    public void setChnAddress(String chnAddress) {
        this.chnAddress = chnAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getHotel_comm_type() {
        return hotel_comm_type;
    }

    public void setHotel_comm_type(String hotel_comm_type) {
        this.hotel_comm_type = hotel_comm_type;
    }

    public String getHotelChnIntroduce() {
        return hotelChnIntroduce;
    }

    public void setHotelChnIntroduce(String hotelChnIntroduce) {
        this.hotelChnIntroduce = hotelChnIntroduce;
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

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelIntroduce() {
        return hotelIntroduce;
    }

    public void setHotelIntroduce(String hotelIntroduce) {
        this.hotelIntroduce = hotelIntroduce;
    }

    public String getHotelStar() {
        return hotelStar;
    }

    public void setHotelStar(String hotelStar) {
        this.hotelStar = hotelStar;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(Double lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public Long getCommendType() {
        return commendType;
    }

    public void setCommendType(Long commendType) {
        this.commendType = commendType;
    }

    public Long getGisid() {
        return gisid;
    }

    public void setGisid(Long gisid) {
        this.gisid = gisid;
    }

}

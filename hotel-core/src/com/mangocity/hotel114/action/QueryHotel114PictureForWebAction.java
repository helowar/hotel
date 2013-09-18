package com.mangocity.hotel114.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlPictureInfo;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.hotel114.manage.Hotel114ManageWeb;
import com.mangocity.hotelweb.persistence.HotelInfoForWeb;

/**
 */
public class QueryHotel114PictureForWebAction extends PersistenceAction {

    private static final long serialVersionUID = 4266026961373725401L;

    /*
     * 酒店ID
     */
    private long hotelId;

    /*
     * 入住日期
     */
    private Date checkindate;

    /*
     * 离店日期
     */
    private Date checkoutdate;

    /*
     * 价格范围
     */
    private String priceScope;

    /*
     * 查询的返回结果
     */
    private List hotelPictureLis = new ArrayList();

    /*
     * 酒店图片信息
     */
    private HtlPictureInfo htlPictureInfo;

    /*
     * 酒店中文名
     */
    private String hotelChineseName;

    /*
     * 酒店信息
     */
    private HtlHotel htlHotel;

    /*
     * 酒店详细介绍
     */
    private String chnHotelIntroduce;

    /*
     * 酒店中文名
     */
    private String chnName;

    /*
     * 酒店星级
     */
    private String hotelStar;

    /*
     * 酒店推荐级别
     */
    private String commendType;

    /*
     * 城市ID
     */
    private String cityId;

    private HotelManage hotelManage;

    private Hotel114ManageWeb hotel114ManageWeb;

    private HotelInfoForWeb hotelstarinfo = new HotelInfoForWeb();

    /**
     * 查询图片信息
     * 
     * @return
     */
    public String query() {
        htlHotel = hotelManage.findHotel(hotelId);
        if (null != htlHotel) {
            hotelChineseName = htlHotel.getChnName();
            chnHotelIntroduce = htlHotel.getChnHotelIntroduce();
            HtlPictureInfo htlPictureInfo = new HtlPictureInfo();
            if (null != htlPictureInfo) {
                htlPictureInfo.setHotelId(hotelId);
                hotelPictureLis = hotelManage.queryHotelPicList(htlPictureInfo);
            }
        }
        hotelstarinfo = hotel114ManageWeb.queryHotelInfoForWeb(hotelId);
        return "view";

    }

    public Date getCheckindate() {
        return checkindate;
    }

    public void setCheckindate(Date checkindate) {
        this.checkindate = checkindate;
    }

    public Date getCheckoutdate() {
        return checkoutdate;
    }

    public void setCheckoutdate(Date checkoutdate) {
        this.checkoutdate = checkoutdate;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public String getPriceScope() {
        return priceScope;
    }

    public void setPriceScope(String priceScope) {
        this.priceScope = priceScope;
    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

    public List getHotelPictureLis() {
        return hotelPictureLis;
    }

    public void setHotelPictureLis(List hotelPictureLis) {
        this.hotelPictureLis = hotelPictureLis;
    }

    public HtlPictureInfo getHtlPictureInfo() {
        return htlPictureInfo;
    }

    public void setHtlPictureInfo(HtlPictureInfo htlPictureInfo) {
        this.htlPictureInfo = htlPictureInfo;
    }

    public String getHotelChineseName() {
        return hotelChineseName;
    }

    public void setHotelChineseName(String hotelChineseName) {
        this.hotelChineseName = hotelChineseName;
    }

    public HtlHotel getHtlHotel() {
        return htlHotel;
    }

    public void setHtlHotel(HtlHotel htlHotel) {
        this.htlHotel = htlHotel;
    }

    public String getChnHotelIntroduce() {
        return chnHotelIntroduce;
    }

    public void setChnHotelIntroduce(String chnHotelIntroduce) {
        this.chnHotelIntroduce = chnHotelIntroduce;
    }

    public String getChnName() {
        return chnName;
    }

    public void setChnName(String chnName) {
        this.chnName = chnName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCommendType() {
        return commendType;
    }

    public void setCommendType(String commendType) {
        this.commendType = commendType;
    }

    public String getHotelStar() {
        return hotelStar;
    }

    public void setHotelStar(String hotelStar) {
        this.hotelStar = hotelStar;
    }

    public Hotel114ManageWeb getHotel114ManageWeb() {
        return hotel114ManageWeb;
    }

    public void setHotel114ManageWeb(Hotel114ManageWeb hotel114ManageWeb) {
        this.hotel114ManageWeb = hotel114ManageWeb;
    }

    public HotelInfoForWeb getHotelstarinfo() {
        return hotelstarinfo;
    }

    public void setHotelstarinfo(HotelInfoForWeb hotelstarinfo) {
        this.hotelstarinfo = hotelstarinfo;
    }

}

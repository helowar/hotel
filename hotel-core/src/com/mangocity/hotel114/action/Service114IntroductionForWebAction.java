package com.mangocity.hotel114.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel114.manage.Hotel114ManageWeb;
import com.mangocity.hotelweb.persistence.HotelInfoForWeb;
import com.mangocity.hotelweb.persistence.QueryHotelFactorageResult;
import com.mangocity.hotelweb.persistence.QueryHotelForWebServiceIntroduction;

/**
 */
public class Service114IntroductionForWebAction extends PersistenceAction {

    private static final long serialVersionUID = 3891655202855337817L;

    private long hotelId;

    // 酒店星级
    private String hotelStar;

    // 酒店名称
    private String chnName;

    // 酒店推荐级别
    private String commendType;

    // 入住日期
    private String inDate;

    // 退房日期
    private String outDate;

    // 价格范围
    private String priceStr;

    // 城市
    private String cityId;

    private String cityMapCode;

    private String cityName;

    private long memberId;

    // 为渠道增加begin
    private String telephone; // 联系电话

    private String email;// 电邮

    private String fax; // 传真

    private String logo; // 渠道logo

    private String title; // 页头title

    private String bgColor; // 背景颜色

    private String color; // 行颜色

    // 为渠道增加end

    private Hotel114ManageWeb hotel114ManageWeb;

    private QueryHotelForWebServiceIntroduction serviceIntroduction = 
        new QueryHotelForWebServiceIntroduction();

    private HotelInfoForWeb hotelinfo = new HotelInfoForWeb();

    private List pictureInfos = new ArrayList();

    protected static final String QUERY = "query";

    public String query() {

        QueryHotelFactorageResult facrotage = (QueryHotelFactorageResult) 
        getFromSession("facrotage");
        telephone = facrotage.getTelephone();
        email = facrotage.getEmail();
        fax = facrotage.getFax();
        logo = facrotage.getLogo();
        title = facrotage.getTitle();
        bgColor = facrotage.getBgColor();
        color = facrotage.getColor();

        MemberDTO member1 = (MemberDTO) getFromSession("member");
        if (null != member1) {
            memberId = member1.getId();
        }
        // memberName = member1.getName();
        // memberCD = member1.getMembercd();
        return QUERY;
    }

    public String view() {
        QueryHotelFactorageResult facrotage = (QueryHotelFactorageResult)
        getFromSession("facrotage");
        telephone = facrotage.getTelephone();
        email = facrotage.getEmail();
        fax = facrotage.getFax();
        logo = facrotage.getLogo();
        title = facrotage.getTitle();
        bgColor = facrotage.getBgColor();
        color = facrotage.getColor();

        MemberDTO member1 = (MemberDTO) getFromSession("member");
        if (null != member1) {
            memberId = member1.getId();
        }
        // memberName = member1.getName();
        // memberCD = member1.getMembercd();
        try {
            cityName = new String(cityName.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(),e);
        }
        serviceIntroduction = hotel114ManageWeb.queryServiceIntroductionForWeb(hotelId);
        hotelinfo = hotel114ManageWeb.queryHotelInfoForWeb(hotelId);
        chnName = hotelinfo.getHotelName();
        if (null != serviceIntroduction) {
            pictureInfos = serviceIntroduction.getPictureInfos();
        }
        cityMapCode = InitServlet.platObj.get(cityId);
        return VIEW;
    }

    public Hotel114ManageWeb getHotel114ManageWeb() {
        return hotel114ManageWeb;
    }

    public void setHotel114ManageWeb(Hotel114ManageWeb hotel114ManageWeb) {
        this.hotel114ManageWeb = hotel114ManageWeb;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public List getPictureInfos() {
        return pictureInfos;
    }

    public void setPictureInfos(List pictureInfos) {
        this.pictureInfos = pictureInfos;
    }

    public QueryHotelForWebServiceIntroduction getServiceIntroduction() {
        return serviceIntroduction;
    }

    public void setServiceIntroduction(QueryHotelForWebServiceIntroduction serviceIntroduction) {
        this.serviceIntroduction = serviceIntroduction;
    }

    public static String getQUERY() {
        return QUERY;
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

    public String getPriceStr() {
        return priceStr;
    }

    public void setPriceStr(String priceStr) {
        this.priceStr = priceStr;
    }

    public HotelInfoForWeb getHotelinfo() {
        return hotelinfo;
    }

    public void setHotelinfo(HotelInfoForWeb hotelinfo) {
        this.hotelinfo = hotelinfo;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityMapCode() {
        return cityMapCode;
    }

    public void setCityMapCode(String cityMapCode) {
        this.cityMapCode = cityMapCode;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}

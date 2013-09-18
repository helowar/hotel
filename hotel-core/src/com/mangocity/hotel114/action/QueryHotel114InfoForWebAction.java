package com.mangocity.hotel114.action;

import java.sql.SQLException;

import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel114.manage.Hotel114ManageWeb;
import com.mangocity.hotelweb.persistence.HotelInfoForWeb;
import com.mangocity.hotelweb.persistence.HotelInfoForWebBean;
import com.mangocity.hotelweb.persistence.QueryHotelFactorageResult;
import com.mangocity.hotelweb.persistence.QueryHotelForWebBean;
import com.mangocity.util.DateUtil;

/**
 */
public class QueryHotel114InfoForWebAction extends PersistenceAction {

    private static final long serialVersionUID = -2382309285210807081L;

    private long hotelId;

    private String inDate;

    private String outDate;

    private String chnName;

    private String hotelStar;

    private String commendType;

    private String cityId;

    private String cityMapCode;

    private String cityName;

    private HotelInfoForWebBean hotelInfoForWebBean;

    // 查询条件
    private QueryHotelForWebBean queryHotelForWebBean = new QueryHotelForWebBean();

    private Hotel114ManageWeb hotel114ManageWeb;

    private String priceStr;

    private long memberId;

    private HotelInfoForWeb hotelinfo = new HotelInfoForWeb();

    // 为渠道增加begin
    private String telephone; // 联系电话

    private String email;// 电邮

    private String fax; // 传真

    private String logo; // 渠道logo

    private String title; // 页头title

    private String bgColor; // 背景颜色

    private String color; // 行颜色

    // 为渠道增加end

    public String query() throws SQLException {
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
        queryHotelForWebBean.setHotelId(hotelId);
        queryHotelForWebBean.setInDate(DateUtil.getDate(inDate));
        queryHotelForWebBean.setOutDate(DateUtil.getDate(outDate));
        queryHotelForWebBean.setHotelStar(hotelStar);
        queryHotelForWebBean.setCityId(cityId);
        String[] priceStrs = null;
        if (null != priceStr && 0 < priceStr.length()) {
            priceStrs = priceStr.split("-");
            queryHotelForWebBean.setMinPrice(priceStrs[0]);
            queryHotelForWebBean.setMaxPrice(priceStrs[1]);
        }
        hotelInfoForWebBean = hotel114ManageWeb.queryHotelInfoBeanForWeb(queryHotelForWebBean);
        chnName = hotelInfoForWebBean.getChnName();
        putSession("hotelName", chnName);
        hotelinfo = hotel114ManageWeb.queryHotelInfoForWeb(hotelId);
        queryHotelForWebBean.setPriceStr(priceStr);
        cityMapCode = InitServlet.platObj.get(cityId);

        return "success";
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public HotelInfoForWebBean getHotelInfoForWebBean() {
        return hotelInfoForWebBean;
    }

    public void setHotelInfoForWebBean(HotelInfoForWebBean hotelInfoForWebBean) {
        this.hotelInfoForWebBean = hotelInfoForWebBean;
    }

    public Hotel114ManageWeb getHotel114ManageWeb() {
        return hotel114ManageWeb;
    }

    public void setHotel114ManageWeb(Hotel114ManageWeb hotel114ManageWeb) {
        this.hotel114ManageWeb = hotel114ManageWeb;
    }

    public String getPriceStr() {
        return priceStr;
    }

    public void setPriceStr(String priceStr) {
        this.priceStr = priceStr;
    }

    public String getChnName() {
        return chnName;
    }

    public void setChnName(String chnName) {
        this.chnName = chnName;
    }

    public String getHotelStar() {
        return hotelStar;
    }

    public void setHotelStar(String hotelStar) {
        this.hotelStar = hotelStar;
    }

    public String getCommendType() {
        return commendType;
    }

    public void setCommendType(String commendType) {
        this.commendType = commendType;
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

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
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

    public QueryHotelForWebBean getQueryHotelForWebBean() {
        return queryHotelForWebBean;
    }

    public void setQueryHotelForWebBean(QueryHotelForWebBean queryHotelForWebBean) {
        this.queryHotelForWebBean = queryHotelForWebBean;
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

package com.mangocity.hotel114.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlUsersComment;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.hotel114.manage.Hotel114ManageWeb;
import com.mangocity.hotel114.util.Hotel114InfoForWebUtil;
import com.mangocity.hotelweb.persistence.HotelInfoForWeb;
import com.mangocity.hotelweb.persistence.QueryHotelFactorageResult;

/**
 */
public class Hotel114CommentWebAction extends PersistenceAction {

    private static final long serialVersionUID = -2382422543250807081L;

    private String forward;

    // 酒店id
    private long hotelId;

    // 酒店星级
    private String hotelStar;

    // 酒店名称
    private String hotelName;

    // 酒店推荐级别
    private String commendType;

    // 房间总评分
    private String roomPoint;

    // 环境总评分
    private String environmentPoint;

    // 服务总评分
    private String servicePoint;

    // 星级总评分
    private String starPoint;

    // 入住日期
    private String inDate;

    // 退房日期
    private String outDate;

    // 价格范围
    private String priceStr;

    // 城市id
    private String cityId;

    private HotelManage hotelManage;

    private int startIndex;

    private int maxResults = 10;

    private int totalNum = 0;

    private int pageIndex;

    private int totalIndex;

    private HtlUsersComment htlUsersComment;

    private List<HtlUsersComment> userCommentLis = new ArrayList<HtlUsersComment>();

    // 为渠道增加begin
    private String telephone; // 联系电话

    private String email;// 电邮

    private String fax; // 传真

    private String logo; // 渠道logo

    private String title; // 页头title

    private String bgColor; // 背景颜色

    private String color; // 行颜色

    // 为渠道增加end

    private int starNum;

    private int starBody;

    private HotelInfoForWeb hotelstarinfo = new HotelInfoForWeb();

    private Hotel114ManageWeb hotel114ManageWeb;

    public String forward() {
        QueryHotelFactorageResult facrotage = (QueryHotelFactorageResult)
        getFromSession("facrotage");
        telephone = facrotage.getTelephone();
        email = facrotage.getEmail();
        fax = facrotage.getFax();
        logo = facrotage.getLogo();
        title = facrotage.getTitle();
        bgColor = facrotage.getBgColor();
        color = facrotage.getColor();

        hotelstarinfo = hotel114ManageWeb.queryHotelInfoForWeb(hotelId);
        hotelName = hotelstarinfo.getHotelName();
        Hotel114InfoForWebUtil util = new Hotel114InfoForWebUtil(hotel114ManageWeb);
        hotelstarinfo = util.queryHotelInfo(hotelId);
        starNum = hotelstarinfo.getStarNum();
        starBody = hotelstarinfo.getStarBody();
        // 根据酒店ID查询评论表
        if (0 == totalIndex) {
            totalNum = hotelManage.queryHtlUsersCommentTotal(hotelId);
            totalIndex = 1 < totalIndex / 10 ? totalIndex / 10 : 1;
        }
        if (0 < startIndex) {
            pageIndex = startIndex;
        } else {
            startIndex = 0;
            pageIndex = 1;
        }
        userCommentLis = hotelManage.queryHtlUsersComment(hotelId, startIndex, maxResults);
        if (!userCommentLis.isEmpty()) {
            Double rPoint = 0.0;
            Double ePoint = 0.0;
            Double sPoint = 0.0;
            Double tPoint = 0.0;
            for (int i = 0; i < userCommentLis.size(); i++) {
                HtlUsersComment htlUsersComment = userCommentLis.get(i);
                rPoint += htlUsersComment.getRoomPoint();
                ePoint += htlUsersComment.getEnvironmentPoint();
                sPoint += htlUsersComment.getServicePoint();
                tPoint += htlUsersComment.getStarPoint();
            }
            roomPoint = String.valueOf(rPoint / userCommentLis.size()) + "分";
            environmentPoint = String.valueOf(ePoint / userCommentLis.size()) + "分";
            servicePoint = String.valueOf(sPoint / userCommentLis.size()) + "分";
            starPoint = String.valueOf(tPoint / userCommentLis.size()) + "分";
        }

        return "query";
    }

    public String addComment() {
        QueryHotelFactorageResult facrotage = (QueryHotelFactorageResult)
        getFromSession("facrotage");
        telephone = facrotage.getTelephone();
        email = facrotage.getEmail();
        fax = facrotage.getFax();
        logo = facrotage.getLogo();
        title = facrotage.getTitle();
        bgColor = facrotage.getBgColor();
        color = facrotage.getColor();

        htlUsersComment
            .setGeneralPoint((htlUsersComment.getRoomPoint()
                + htlUsersComment.getEnvironmentPoint() +
                htlUsersComment.getServicePoint() + htlUsersComment
                .getStarPoint()) / 4);
        htlUsersComment.setCreateTime(new Date());
        htlUsersComment.setHotelID(hotelId);
        htlUsersComment.setCommentStatus("1");
        hotelManage.addHtlUsersComment(htlUsersComment);
        totalIndex = 0;
        startIndex = 0;
        return "view";
    }

    public String getForward() {
        return forward;
    }

    public void setForward(String forward) {
        this.forward = forward;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelStar() {
        return hotelStar;
    }

    public void setHotelStar(String hotelStar) {
        this.hotelStar = hotelStar;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getCommendType() {
        return commendType;
    }

    public void setCommendType(String commendType) {
        this.commendType = commendType;
    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

    public List<HtlUsersComment> getUserCommentLis() {
        return userCommentLis;
    }

    public void setUserCommentLis(List<HtlUsersComment> userCommentLis) {
        this.userCommentLis = userCommentLis;
    }

    public String getRoomPoint() {
        return roomPoint;
    }

    public void setRoomPoint(String roomPoint) {
        this.roomPoint = roomPoint;
    }

    public String getEnvironmentPoint() {
        return environmentPoint;
    }

    public void setEnvironmentPoint(String environmentPoint) {
        this.environmentPoint = environmentPoint;
    }

    public String getServicePoint() {
        return servicePoint;
    }

    public void setServicePoint(String servicePoint) {
        this.servicePoint = servicePoint;
    }

    public String getStarPoint() {
        return starPoint;
    }

    public void setStarPoint(String starPoint) {
        this.starPoint = starPoint;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public HtlUsersComment getHtlUsersComment() {
        return htlUsersComment;
    }

    public void setHtlUsersComment(HtlUsersComment htlUsersComment) {
        this.htlUsersComment = htlUsersComment;
    }

    public int getTotalIndex() {
        return totalIndex;
    }

    public void setTotalIndex(int totalIndex) {
        this.totalIndex = totalIndex;
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

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public HotelInfoForWeb getHotelstarinfo() {
        return hotelstarinfo;
    }

    public void setHotelstarinfo(HotelInfoForWeb hotelstarinfo) {
        this.hotelstarinfo = hotelstarinfo;
    }

    public int getStarBody() {
        return starBody;
    }

    public void setStarBody(int starBody) {
        this.starBody = starBody;
    }

    public int getStarNum() {
        return starNum;
    }

    public void setStarNum(int starNum) {
        this.starNum = starNum;
    }

    public Hotel114ManageWeb getHotel114ManageWeb() {
        return hotel114ManageWeb;
    }

    public void setHotel114ManageWeb(Hotel114ManageWeb hotel114ManageWeb) {
        this.hotel114ManageWeb = hotel114ManageWeb;
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

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
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

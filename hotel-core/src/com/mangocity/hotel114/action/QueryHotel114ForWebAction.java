package com.mangocity.hotel114.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.web.GenericCCAction;
import com.mangocity.hotel114.manage.Hotel114ManageWeb;
import com.mangocity.hotelweb.persistence.HotelInfoForWeb;
import com.mangocity.hotelweb.persistence.HotelPageForWebBean;
import com.mangocity.hotelweb.persistence.QueryHotelFactorageResult;
import com.mangocity.hotelweb.persistence.QueryHotelForWebBean;
import com.mangocity.hotelweb.persistence.QueryHotelForWebResult;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 */
public class QueryHotel114ForWebAction extends GenericCCAction {

    private static final long serialVersionUID = -2382309240480807081L;

    private QueryHotelForWebBean queryHotelForWebBean;

    private String forward;

    private Hotel114ManageWeb hotel114ManageWeb;

    private List<QueryHotelForWebResult> webHotelResultLis = 
        new ArrayList<QueryHotelForWebResult>();

    private HotelPageForWebBean hotelPageForWebBean;

    private HotelInfoForWeb resultHotelInfo;

    private long hotelId;

    private int lisNum = 0;

    private String cityName;

    private Double maxPrice;

    private Double minPrice;

    private int nightNum;

    private long memberId;
    private String memberCd;

    // 要跳到的页数
    private int toPageIndex;

    // 为渠道增加begin
    private String telephone = "40066-40066"; // 联系电话

    private String email = "cs@mangocity.com";// 电邮

    private String fax = "33399610"; // 传真

    private String logo = "hotel/images/success.jpg"; // 渠道logo

    private String title = "芒果网"; // 页头title

    private String bgColor = "#E5F1F5"; // 背景颜色

    private String color = "#D2EBF4"; // 行颜色

    // 为渠道增加end

    public String allForward() {
        try {
            MemberDTO member = memberInterfaceService.getMemberByCode(memberCd);
            putSession("member", member);
            log.info("QueryHotel114ForWebAction allForward memberInterfaceService.getMemberById("
                + memberId + ") success,memberName=" + member.getName() + ",memberCD="
                + member.getMembercd());
            QueryHotelFactorageResult factorage = hotel114ManageWeb
                .queryHotelFactorageForWeb(memberId);
            if (null != factorage) {
                if (null == factorage.getTelephone() || 0 == factorage.getTelephone().length()) {
                    factorage.setTelephone("40066-40066");
                }
                telephone = factorage.getTelephone();
                if (null == factorage.getEmail() || 0 == factorage.getEmail().length()) {
                    factorage.setEmail("cs@mangocity.com");
                }
                email = factorage.getEmail();
                if (null == factorage.getFax() || 0 == factorage.getFax().length()) {
                    factorage.setFax("33399610");
                }
                fax = factorage.getFax();
                if (null == factorage.getLogo() || 0 == factorage.getLogo().length()) {
                    factorage.setLogo("");
                }
                logo = factorage.getLogo();
                if (null == factorage.getTitle() || 0 == factorage.getTitle().length()) {
                    factorage.setTitle("芒果网");
                }
                title = factorage.getTitle();
                bgColor = factorage.getBgColor();
                color = factorage.getColor();
            } else {
                factorage.setTelephone(telephone);
                factorage.setEmail(email);
                factorage.setFax(fax);
                factorage.setLogo(logo);

                factorage.setTitle(title);
                factorage.setBgColor(bgColor);
                factorage.setColor(color);
            }
            putSession("facrotage", factorage);
            log
                .info("QueryHotel114ForWebAction allForward " +
                        "hotel114ManageWeb.queryHotelFactorageForWeb("
                    + memberId
                    + ") success,telephone="
                    + factorage.getTelephone()
                    + ",email="
                    + factorage.getEmail()
                    + ",fax="
                    + factorage.getFax()
                    + ",logo="
                    + factorage.getLogo()
                    + ",title="
                    + factorage.getTitle()
                    + ",bgcolor="
                    + factorage.getBgColor() + ",color=" + factorage.getColor());
        } catch (Exception e) {
            QueryHotelFactorageResult factorage = new QueryHotelFactorageResult();
            factorage.setTelephone("40066-40066");
            factorage.setEmail("cs@mangocity.com");
            factorage.setFax("33399610");
            factorage.setLogo("hotel/images/success.jpg");
            factorage.setTitle("芒果网");
            factorage.setBgColor("#E5F1F5");
            factorage.setColor("#D2EBF4");
            putSession("facrotage", factorage);
            log.error("QueryHotel114ForWebAction allForward memberInterfaceService.getMemberById("
                + memberId + ") excption, ex=" + e);
        }
        return forward;
    }

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

        Map params = super.getParams();
        if (null == queryHotelForWebBean) {
            queryHotelForWebBean = new QueryHotelForWebBean();
        }
        MyBeanUtil.copyProperties(queryHotelForWebBean, params);
        if (0 != toPageIndex) {
            queryHotelForWebBean.setPageIndex(toPageIndex);
        }
        /*
         * String priceStr[] = null; if (queryHotelForWebBean !=null &&
         * queryHotelForWebBean.getPriceStr() != null && queryHotelForWebBean.getPriceStr().length()
         * > 0) { priceStr = queryHotelForWebBean.getPriceStr().split("-"); minPrice =
         * Double.valueOf(priceStr[0]); maxPrice = Double.valueOf(priceStr[1]);
         * queryHotelForWebBean.setMinPrice(priceStr[0]);
         * queryHotelForWebBean.setMaxPrice(priceStr[1]); }
         */
        if (null != queryHotelForWebBean) {
            if (!(null == queryHotelForWebBean.getMinPrice() 
                && null == queryHotelForWebBean.getMaxPrice())) {
                if (StringUtil.isValidStr(queryHotelForWebBean.getMinPrice())) {
                    minPrice = Double.valueOf(queryHotelForWebBean.getMinPrice());
                } else {
                    queryHotelForWebBean.setMinPrice("0");
                }
                if (StringUtil.isValidStr(queryHotelForWebBean.getMaxPrice())) {
                    maxPrice = Double.valueOf(queryHotelForWebBean.getMaxPrice());
                } else {
                    queryHotelForWebBean.setMaxPrice("9999");
                }
            } else {
                queryHotelForWebBean.setMinPrice("0");
                queryHotelForWebBean.setMaxPrice("9999");
            }
        }
        if (null != queryHotelForWebBean.getHotelStar()
            && 2 > queryHotelForWebBean.getHotelStar().length()) {
            queryHotelForWebBean.setHotelStar("19,29,39,49,59,64,66,69,79");
        }
        if (null != hotel114ManageWeb) {
            hotelPageForWebBean = hotel114ManageWeb.queryHotelsForWeb(queryHotelForWebBean);
            webHotelResultLis = hotelPageForWebBean.getList();
            lisNum = webHotelResultLis.size();

            nightNum = DateUtil.getDay(queryHotelForWebBean.getInDate(), queryHotelForWebBean
                .getOutDate());
        }
        forward = "qryresult";
        ActionContext.getContext().getSession().remove("hotelName");
        return forward;
    }

    public String group() {
        QueryHotelFactorageResult facrotage = (QueryHotelFactorageResult)
        getFromSession("facrotage");
        telephone = facrotage.getTelephone();
        email = facrotage.getEmail();
        fax = facrotage.getFax();
        logo = facrotage.getLogo();
        title = facrotage.getTitle();
        bgColor = facrotage.getBgColor();
        color = facrotage.getColor();
        return "group114";
    }

    public QueryHotelForWebBean getQueryHotelForWebBean() {
        return queryHotelForWebBean;
    }

    public void setQueryHotelForWebBean(QueryHotelForWebBean queryHotelForWebBean) {
        this.queryHotelForWebBean = queryHotelForWebBean;
    }

    public String getForward() {
        return forward;
    }

    public void setForward(String forward) {
        this.forward = forward;
    }

    public Hotel114ManageWeb getHotel114ManageWeb() {
        return hotel114ManageWeb;
    }

    public void setHotel114ManageWeb(Hotel114ManageWeb hotel114ManageWeb) {
        this.hotel114ManageWeb = hotel114ManageWeb;
    }

    public List<QueryHotelForWebResult> getWebHotelResultLis() {
        return webHotelResultLis;
    }

    public void setWebHotelResultLis(List<QueryHotelForWebResult> webHotelResultLis) {
        this.webHotelResultLis = webHotelResultLis;
    }

    public int getLisNum() {
        return lisNum;
    }

    public void setLisNum(int lisNum) {
        this.lisNum = lisNum;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public int getNightNum() {
        return nightNum;
    }

    public void setNightNum(int nightNum) {
        this.nightNum = nightNum;
    }

    public HotelPageForWebBean getHotelPageForWebBean() {
        return hotelPageForWebBean;
    }

    public void setHotelPageForWebBean(HotelPageForWebBean hotelPageForWebBean) {
        this.hotelPageForWebBean = hotelPageForWebBean;
    }

    public int getToPageIndex() {
        return toPageIndex;
    }

    public void setToPageIndex(int toPageIndex) {
        this.toPageIndex = toPageIndex;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public HotelInfoForWeb getResultHotelInfo() {
        return resultHotelInfo;
    }

    public void setResultHotelInfo(HotelInfoForWeb resultHotelInfo) {
        this.resultHotelInfo = resultHotelInfo;
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

	public String getMemberCd() {
		return memberCd;
	}

	public void setMemberCd(String memberCd) {
		this.memberCd = memberCd;
	}

}

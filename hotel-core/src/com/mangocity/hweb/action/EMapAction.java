package com.mangocity.hweb.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mangocity.framework.exception.ServiceException;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hotel.order.web.GenericCCAction;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.hweb.persistence.HotelPageForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebResult;
import com.mangocity.mgis.app.common.Constants;
import com.mangocity.mgis.app.service.baseinfo.GisService;
import com.mangocity.mgis.app.util.StringCodeUtils;
import com.mangocity.mgis.domain.entity.GisBaseInfo;
import com.mangocity.mgis.domain.valueobject.GisInfo;
import com.mangocity.mgis.domain.valueobject.GisResult;
import com.mangocity.mgis.domain.valueobject.LatLng;
import com.mangocity.mgis.domain.valueobject.MapsInfo;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.hotel.constant.FaxEmailModel;
import com.mangoctiy.communicateservice.CommunicaterService;
import com.mangoctiy.communicateservice.domain.Mail;

/**
 * @author yangshaojun
 */
public class EMapAction extends GenericCCAction {
    /**
     * Automatically generated variable: SMALL_MAP_SIGHT_DATATYPE
     */
    private static final int SMALL_MAP_SIGHT_DATATYPE = 23;

    /**
     * Automatically generated variable: SMALL_MAP_UNDERGROUND_DATATYPE
     */
    private static final int SMALL_MAP_UNDERGROUND_DATATYPE = 24;

    /**
     * Automatically generated variable: SMALL_MAP_hINGE_DATATYPE
     */
    private static final int SMALL_MAP_HINGE_DATATYPE = 21;

    /**
     * Automatically generated variable: HOTEL_DESC_INTERCEPT_LENGTH
     */
    private static final int HOTEL_DESC_INTERCEPT_LENGTH = 40;

    /**
     * Automatically generated variable: HOTEL_DESC_LENGTH
     */
    private static final int HOTEL_DESC_SHOW_LENGTH = 45;

    /**
     * Automatically generated variable: MAX_NIGHT_NUM
     */
    private static final int MAX_NIGHT_NUM = 28;

    private static final long serialVersionUID = -8479345840073521527L;

    // private static final String BIZ_AREA_LIST = "bizAreaList";

    private List<GisBaseInfo> gisBaseInfoList = new ArrayList<GisBaseInfo>();

    private List<QueryHotelForWebResult> webHotelResultLis = 
                                                new ArrayList<QueryHotelForWebResult>();

    private List<GisInfo> gisList;// 餐饮

    private List<GisInfo> gisHingeList;// 交通枢纽

    private List<GisInfo> gisFunList;// 休闲娱乐

    private List<GisInfo> gisUndergroundList;// 地铁

    private List<GisInfo> gisSightList;// 景点

    private Map gisMaps = new HashMap();

    private String hotelid;

    private String address;

    private double distance;

    private String city;

    private String city_shortened;

    private String bizZoneCode;

    private double neLat;

    private double neLng;

    private double swLat;

    private double swLng;

    private double seLat;

    private double seLng;

    private double nwLat;

    private double nwLng;

    private String actionUrl;

    private String toMail;

    private String fromName;

    private String messageMail;

    private GisService gisService;

    /**
     * message接口
     */
    private CommunicaterService communicaterService;

    private HotelManageWeb hotelManageWeb;

    private HotelManage hotelManage;

    /**
     * 要跳到的页数
     */
    private int toPageIndex;

    private HtlHotel htlHotel;

    private String forward;

    private int nightNum;

    /**
     * 查询条件实体
     */
    private QueryHotelForWebBean queryHotelForWebBean;

    /**
     * 酒店查询结果实体
     */
    private HotelPageForWebBean hotelPageForWebBean;

    public HotelPageForWebBean getHotelPageForWebBean() {
        return hotelPageForWebBean;
    }

    public void setHotelPageForWebBean(HotelPageForWebBean hotelPageForWebBean) {
        this.hotelPageForWebBean = hotelPageForWebBean;
    }

    public HtlHotel getHtlHotel() {
        return htlHotel;
    }

    public void setHtlHotel(HtlHotel htlHotel) {
        this.htlHotel = htlHotel;
    }

    /**
     * 单个酒店地址
     * 
     * @return
     */
    @SuppressWarnings("deprecation")
    public String gisIndex() {
        if (StringUtil.isValidStr(hotelid)) {
            htlHotel = hotelManageWeb.findHotel(Long.valueOf(hotelid));
            /**
             * 如果查询结果为空为空,返回错误信息,
             */
            /*
             * if(htlHotel==null){ return "rootException"; }else{ try { List mapsInfoList = new
             * ArrayList(); MapsInfo masInfo = new MapsInfo();
             * masInfo.setGisId(htlHotel.getGisid()); masInfo.setName(htlHotel.getChnName()); //
             * masInfo.setDescription(htlHotel.getChnName()+"<br>"+htlHotel.getChnAddress());
             * masInfo.setDescription(""); String path = request.getRealPath("/");
             * masInfo.setImageUrl(path+"/images/emap/mango.gif"); masInfo.setImageInfo("1");
             * mapsInfoList.add(masInfo); gisMaps = gisService.kmlGenerator(mapsInfoList, 0,
             * 0,path); gisBaseInfoList = (List<GisBaseInfo>) gisMaps
             * .get(gisService.GIS_BASEINFO_RESULT); } catch (Exception e) {
             * log.info("电子地图读取酒店数据异常!"+e); } city_shortened = htlHotel.getCity(); city =
             * InitServlet.cityObj.get(htlHotel.getCity()); request.setAttribute("htlHotel",
             * htlHotel); }
             */
            return "smallMapContent";
        } else {
            return "rootException";
        }
    }

    /**
     * 地图框选
     * 
     * @return
     */
    public String dragIndex() {
        try {

            Map params = super.getParams();
            List<GisInfo> list = gisService.calculateZoomArea(new LatLng(neLng, neLat), new LatLng(
                swLng, swLat), new LatLng(seLng, seLat), new LatLng(nwLng, nwLat), 0);
            String path = request.getRealPath("/");
            List mapsInfoList = new ArrayList();
            String gisLst = "";
            String hotelIdLst = "";
            String subStr = "";
            for (Iterator<GisInfo> i = list.iterator(); i.hasNext();) {
                GisInfo gisInfo = i.next();
                gisLst += gisInfo.getGisId() + ",";
            }
            if (null == queryHotelForWebBean) {
                queryHotelForWebBean = new QueryHotelForWebBean();
                MyBeanUtil.copyProperties(queryHotelForWebBean, params);// 页面参数copy到查询实体中
            }

            Date inDate = queryHotelForWebBean.getInDate();
            Date outDate = queryHotelForWebBean.getOutDate();
            // 城市ID,入住和离店日期不能为空
            if (null == inDate || null == outDate
                || !StringUtil.isValidStr(queryHotelForWebBean.getCityId())) {
                return forward;
            }
            // 查询不能超过28天
            nightNum = DateUtil.getDay(inDate, outDate);
            if (0 >= nightNum || MAX_NIGHT_NUM < nightNum) {
                return forward;
            }
            // 如果不传城市中文名则根据城市代码设置
            String tmpCityName = queryHotelForWebBean.getCityName();
            if (!StringUtil.isValidStr(tmpCityName)) {
                queryHotelForWebBean.setCityName(InitServlet.cityObj.get(queryHotelForWebBean
                    .getCityId()));
            }

            // 处理和讯过来的连接, 酒店中文名要从ISO-8859-1转成GBK编码
            String hn = (String) params.get("hx");
            if (null != hn && hn.equals("1")) {
                try {
                    queryHotelForWebBean.setHotelName(new String(queryHotelForWebBean
                        .getHotelName().getBytes("ISO-8859-1"), "GBK"));
                } catch (Exception ex) {
                    log.error("change hotel name to GBK error!" + ex);
                }
            }

            if (0 != toPageIndex) {
                queryHotelForWebBean.setPageIndex(toPageIndex);
            }

            String[] priceStr = null;
            if (StringUtil.isValidStr(queryHotelForWebBean.getPriceStr())) {
                priceStr = queryHotelForWebBean.getPriceStr().split("-");
                queryHotelForWebBean.setMinPrice(priceStr[0]);
                if (1 < priceStr.length) {
                    queryHotelForWebBean.setMaxPrice(priceStr[1]);
                }
            } else {
                String minStr = queryHotelForWebBean.getMinPrice();
                String maxStr = queryHotelForWebBean.getMaxPrice();
                if (StringUtil.isValidStr(minStr) || StringUtil.isValidStr(maxStr)) {
                    queryHotelForWebBean.setPriceStr((null != minStr ? minStr : "") + "-"
                        + (null != maxStr ? maxStr : ""));
                }
            }

            // 两种星级参数组装
            String hotelStar1 = queryHotelForWebBean.getFormalStarLevel();
            String hotelStar2 = queryHotelForWebBean.getInformalStarLevel();
            queryHotelForWebBean.setHotelStar((StringUtil.isValidStr(hotelStar1) ? hotelStar1 + ","
                : "")
                + (StringUtil.isValidStr(hotelStar2) ? hotelStar2 : ""));

            /* 获取酒店id */
            if (!gisLst.equals("") && null != gisLst) {
                hotelIdLst = hotelManageWeb.getHotelIdLst(gisLst);
                queryHotelForWebBean.setHotelIdLst(hotelIdLst);
            }

            /* 封装酒店id */
            hotelPageForWebBean = hotelManageWeb.queryHotelsForWeb(queryHotelForWebBean);
            webHotelResultLis = hotelPageForWebBean.getList();
            for (int i = 0; i < webHotelResultLis.size(); i++) {
                QueryHotelForWebResult queryhotelResult = webHotelResultLis.get(i);
                MapsInfo mapsInfo = new MapsInfo();
                mapsInfo.setGisId(queryhotelResult.getGisid());
                mapsInfo.setName(queryhotelResult.getHotelChnName());
                mapsInfo.setImageUrl(path + "/images/emap/mango.gif");
                mapsInfo.setImageInfo(String.valueOf(i + 1));
                mapsInfoList.add(mapsInfo);
                subStr = queryhotelResult.getAutoIntroduce();
                if (!"".equals(subStr) && null != subStr) {
                    if (HOTEL_DESC_SHOW_LENGTH < subStr.length()) {
                        subStr = subStr.substring(0, HOTEL_DESC_INTERCEPT_LENGTH);
                        subStr += "....";
                    }
                    queryhotelResult.setAutoIntroduce(subStr);
                }
            }
            gisMaps = gisService.kmlGenerator(mapsInfoList, 0, 0, path);
        } catch (Exception e) {
            log.info("电子地图拖动数据异常!" + e);
        }
        return "emappingDrag";
    }

    /**
     * 地图点选结果
     * 
     * @return
     */
    public String searchIndex() {
        try {
            String gisid = "";
            String hotelid = "";
            String subStr = "";
            Map params = super.getParams();

            LatLng latLng = null;
            if (0 < nwLng && 0 < nwLat) {
                latLng = new LatLng(nwLng, nwLat);
            }

            GisResult result = gisService.searchMaps(StringCodeUtils.decodeUTF8(address), distance,
                StringCodeUtils.decodeUTF8(city), latLng, 0, bizZoneCode);
            if (Constants.GIS_BASE_INFO_STATUS_CODE_NORMAL.equals(result.getStatusCode())) {

                List<GisInfo> list = result.getResultList();
                List mapsInfoList = new ArrayList();
                String path = request.getRealPath("/");
                for (Iterator<GisInfo> i = list.iterator(); i.hasNext();) {
                    GisInfo gisInfo = i.next();
                    gisid += gisInfo.getGisId() + ",";
                }
                if (null == queryHotelForWebBean) {
                    queryHotelForWebBean = new QueryHotelForWebBean();
                    MyBeanUtil.copyProperties(queryHotelForWebBean, params);// 页面参数copy到查询实体中
                }
                if (0 != toPageIndex) {
                    queryHotelForWebBean.setPageIndex(toPageIndex);
                }
                if (!gisid.equals("") && null != gisid) {
                    hotelid = hotelManageWeb.getHotelIdLst(gisid);
                    queryHotelForWebBean.setHotelIdLst(hotelid);
                }
                hotelPageForWebBean = hotelManageWeb.queryHotelsForWeb(queryHotelForWebBean);
                webHotelResultLis = hotelPageForWebBean.getList();
                for (int i = 0; i < webHotelResultLis.size(); i++) {
                    QueryHotelForWebResult queryhotelResult = webHotelResultLis.get(i);
                    MapsInfo mapsInfo = new MapsInfo();
                    mapsInfo.setGisId(queryhotelResult.getGisid());
                    mapsInfo.setName(queryhotelResult.getHotelChnName());
                    mapsInfo.setImageUrl(path + "/images/emap/mango.gif");
                    mapsInfo.setImageInfo(String.valueOf(i + 1));
                    mapsInfoList.add(mapsInfo);
                    subStr = queryhotelResult.getAutoIntroduce();
                    if (!("".equals(subStr)) && null != subStr) {
                        if (HOTEL_DESC_SHOW_LENGTH < subStr.length()) {
                            subStr = subStr.substring(0, HOTEL_DESC_INTERCEPT_LENGTH);
                            subStr += "....";
                        }
                        queryhotelResult.setAutoIntroduce(subStr);
                    }
                }
                gisMaps = gisService.kmlGenerator(mapsInfoList, 0, 0, path);

            }
        } catch (Exception e) {
            log.info("地图搜索结果数据异常!" + e);
        }
        return "emappingDrag";
    }

    public String sendMail() {
        StringBuffer xml = new StringBuffer();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xml.append("<hotelEMap>");
        xml.append("<mailContext>");
        xml.append(messageMail);
        xml.append("</mailContext>");
        xml.append("</hotelEMap>");
        Mail mail = new Mail();
        mail.setApplicationName("hotel");
        mail.setTo(new String[] { toMail });
        mail.setSubject("芒果网电子地图");
        mail.setFrom("cs@mangocity.com");
        mail.setTemplateFileName(FaxEmailModel.HOTEL_EMAP_SEND_MAIL);
        log.info("FaxEmailModel.HOTEL_EMAP_SEND_MAIL=" + FaxEmailModel.HOTEL_EMAP_SEND_MAIL);
        mail.setXml(xml.toString());
        log.info("messageMail=" + messageMail);

        try {
            communicaterService.sendEmail(mail);
        } catch (Exception e) {
            log.error("EMapAction.sendMail send failed: " + e);
        }
        return "emapping";
    }

    public String printMap() {
        return "pringMap";
    }

    public String smallIndex() {
        try {
            LatLng latLng = new LatLng();
            latLng.setLatitude(nwLat);
            latLng.setLongitude(nwLng);
            log.info("SMALL MAP city=" + city);
            gisList = gisService.getSmallMapInfo(latLng, StringCodeUtils.decodeUTF8(city), 2, 6);
            log.info("SMALL MAP gisList:" + gisList);
            gisHingeList = gisService.getSmallMapInfo(latLng, StringCodeUtils.decodeUTF8(city),
                SMALL_MAP_HINGE_DATATYPE, 6);
            log.info("SMALL MAP gisHingeList:" + gisHingeList);
            gisFunList = gisService.getSmallMapInfo(latLng, StringCodeUtils.decodeUTF8(city), 4, 6);
            log.info("SMALL MAP gisFunList:" + gisFunList);
            gisUndergroundList = gisService.getSmallMapInfo(latLng, StringCodeUtils
                .decodeUTF8(city), SMALL_MAP_UNDERGROUND_DATATYPE, 6);
            log.info("SMALL MAP gisUndergroundList:" + gisUndergroundList);
            gisSightList = gisService.getSmallMapInfo(latLng, StringCodeUtils.decodeUTF8(city),
                SMALL_MAP_SIGHT_DATATYPE, 6);
            log.info("SMALL MAP gisSightList:" + gisSightList);
            // gisList=gisService.calculateCenterPoint(latLng, 1.0, StringCodeUtils
            // .decodeUTF8(city), 2, 2);
        } catch (ServiceException e) {
        	log.error(e.getMessage(),e);
        }
        return "smallIndex";
    }

    public List<GisInfo> getGisFunList() {
        return gisFunList;
    }

    public void setGisFunList(List<GisInfo> gisFunList) {
        this.gisFunList = gisFunList;
    }

    public List<GisBaseInfo> getGisBaseInfoList() {
        return gisBaseInfoList;
    }

    public void setGisBaseInfoList(List<GisBaseInfo> gisBaseInfoList) {
        this.gisBaseInfoList = gisBaseInfoList;
    }

    public List<GisInfo> getGisList() {
        return gisList;
    }

    public void setGisList(List<GisInfo> gisList) {
        this.gisList = gisList;
    }

    public Map getGisMaps() {
        return gisMaps;
    }

    public void setGisMaps(Map gisMaps) {
        this.gisMaps = gisMaps;
    }

    public GisService getGisService() {
        return gisService;
    }

    public void setGisService(GisService gisService) {
        this.gisService = gisService;
    }

    public double getNeLat() {
        return neLat;
    }

    public void setNeLat(double neLat) {
        this.neLat = neLat;
    }

    public double getNeLng() {
        return neLng;
    }

    public void setNeLng(double neLng) {
        this.neLng = neLng;
    }

    public double getNwLat() {
        return nwLat;
    }

    public void setNwLat(double nwLat) {
        this.nwLat = nwLat;
    }

    public double getNwLng() {
        return nwLng;
    }

    public void setNwLng(double nwLng) {
        this.nwLng = nwLng;
    }

    public double getSeLat() {
        return seLat;
    }

    public void setSeLat(double seLat) {
        this.seLat = seLat;
    }

    public double getSeLng() {
        return seLng;
    }

    public void setSeLng(double seLng) {
        this.seLng = seLng;
    }

    public double getSwLat() {
        return swLat;
    }

    public void setSwLat(double swLat) {
        this.swLat = swLat;
    }

    public double getSwLng() {
        return swLng;
    }

    public void setSwLng(double swLng) {
        this.swLng = swLng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBizZoneCode() {
        return bizZoneCode;
    }

    public void setBizZoneCode(String bizZoneCode) {
        this.bizZoneCode = bizZoneCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getHotelid() {
        return hotelid;
    }

    public void setHotelid(String hotelid) {
        this.hotelid = hotelid;
    }

    public HotelManageWeb getHotelManageWeb() {
        return hotelManageWeb;
    }

    public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
        this.hotelManageWeb = hotelManageWeb;
    }

    public String getCity_shortened() {
        return city_shortened;
    }

    public void setCity_shortened(String city_shortened) {
        this.city_shortened = city_shortened;
    }

    public List<QueryHotelForWebResult> getWebHotelResultLis() {
        return webHotelResultLis;
    }

    public void setWebHotelResultLis(List<QueryHotelForWebResult> webHotelResultLis) {
        this.webHotelResultLis = webHotelResultLis;
    }

    public List<GisInfo> getGisHingeList() {
        return gisHingeList;
    }

    public void setGisHingeList(List<GisInfo> gisHingeList) {
        this.gisHingeList = gisHingeList;
    }

    public QueryHotelForWebBean getQueryHotelForWebBean() {
        return queryHotelForWebBean;
    }

    public void setQueryHotelForWebBean(QueryHotelForWebBean queryHotelForWebBean) {
        this.queryHotelForWebBean = queryHotelForWebBean;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public CommunicaterService getCommunicaterService() {
        return communicaterService;
    }

    public void setCommunicaterService(CommunicaterService communicaterService) {
        this.communicaterService = communicaterService;
    }

    public String getMessageMail() {
        return messageMail;
    }

    public void setMessageMail(String messageMail) {
        this.messageMail = messageMail;
    }

    public String getToMail() {
        return toMail;
    }

    public void setToMail(String toMail) {
        this.toMail = toMail;
    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public int getToPageIndex() {
        return toPageIndex;
    }

    public void setToPageIndex(int toPageIndex) {
        this.toPageIndex = toPageIndex;
    }

    public List<GisInfo> getGisUndergroundList() {
        return gisUndergroundList;
    }

    public void setGisUndergroundList(List<GisInfo> gisUndergroundList) {
        this.gisUndergroundList = gisUndergroundList;
    }

    public String getForward() {
        return forward;
    }

    public void setForward(String forward) {
        this.forward = forward;
    }

    public List<GisInfo> getGisSightList() {
        return gisSightList;
    }

    public void setGisSightList(List<GisInfo> gisSightList) {
        this.gisSightList = gisSightList;
    }

    public int getNightNum() {
        return nightNum;
    }

    public void setNightNum(int nightNum) {
        this.nightNum = nightNum;
    }

}

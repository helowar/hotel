package com.mangocity.hotel.base.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mangocity.hotel.base.persistence.HtlGeographicalposition;
import com.mangocity.hotel.base.service.IGeograpPositionService;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.DateUtil;

/**
 */
public class GeograpPositionAction extends PersistenceAction {

    private IGeograpPositionService geograpPositionService;

    private Long ID;

    // 地点
    private String name;

    // 详细地点
    private String address;

    // 省份名称
    private String provinceName;

    // 城市名称
    private String cityName;

    // 电子地图
    private Long gisId;

    // 经度
    private Long longitude;

    // 纬度
    private Long latitude;

    // 类型ID
    private Long gptypeId;

    // 操作人名称
    private String operationer;

    // 操作人ID
    private String operationerId;

    // 操作日期
    private Date operationDate;

    private HtlGeographicalposition htlGeographicalposition;

    private String cityCode;

    private List listAddressType;

    @Override
    protected Class getEntityClass() {

        return HtlGeographicalposition.class;

    }

    // 新增加一个地点
    public String createGeograpPosition() {

        HtlGeographicalposition htlGeographicalposition = new HtlGeographicalposition();

        /*
         * 添加人的工号，ID，姓名和添加时间;
         */
        if (null != super.getOnlineRoleUser()) {

            htlGeographicalposition.setOperationer(super.getOnlineRoleUser().getName());
            htlGeographicalposition.setOperationerId(super.getOnlineRoleUser().getLoginName());
            htlGeographicalposition.setOperationDate(DateUtil.getSystemDate());

        } else {// 如果拿不到登陆用户信息,则提示重新登陆
            return super.forwardError("获取登陆用户信息失效,请重新登陆");
        }

        htlGeographicalposition.setProvinceName(provinceName);
        htlGeographicalposition.setCityName(cityName);
        htlGeographicalposition.setName(name);
        htlGeographicalposition.setAddress(cityName + name);
        htlGeographicalposition.setCityCode(cityCode);
        htlGeographicalposition.setGptypeId(gptypeId);

        geograpPositionService.createGeograpPosition(htlGeographicalposition);

        return "success";
    }

    // 显示地点信息
    public String viewGeograpPositionInfo() {

        super.setEntityID(ID);
        super.setEntity(super.populateEntity());
        this.setHtlGeographicalposition((HtlGeographicalposition) this.getEntity());

        htlGeographicalposition = geograpPositionService.queryGeopositionById(ID);

        return "viewGP";

    }

    // 修改地点信息
    public String updateGeograpPosition() {

        super.setEntity(super.populateEntity());
        this.setHtlGeographicalposition((HtlGeographicalposition) this.getEntity());

        HtlGeographicalposition geographicalposition = new HtlGeographicalposition();

        /*
         * 添加人的工号，ID，姓名和添加时间;
         */
        if (null != super.getOnlineRoleUser()) {

            geographicalposition.setOperationer(super.getOnlineRoleUser().getName());
            geographicalposition.setOperationerId(super.getOnlineRoleUser().getLoginName());
            geographicalposition.setOperationDate(DateUtil.getSystemDate());

        } else {// 如果拿不到登陆用户信息,则提示重新登陆
            return super.forwardError("获取登陆用户信息失效,请重新登陆");
        }

        geographicalposition.setID(ID);
        geographicalposition.setName(name);
        geographicalposition.setProvinceName(provinceName);
        geographicalposition.setCityName(cityName);
        geographicalposition.setAddress(cityName + name);
        geographicalposition.setGptypeId(gptypeId);
        geographicalposition.setCityCode(cityCode);

        geograpPositionService.updateGeoposition(geographicalposition);

        return "success";
    }

    // 删除地点信息
    public String deleteGeograpPosition() {

        geograpPositionService.deleteGeoposition(ID);

        return "success";
    }

    /**
     * 根据城市名称获得对应的地点信息 ajax调用
     * 
     * @param name
     * @return add by shengwei.zuo 2009-08-11
     */

    public String queryGeograPByName() {

        String gpName = "";

        String regEx = "[A-Z]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(cityCode);
        boolean isABC = m.find();

        if (isABC) {
            // 根据城市三字码得到城市名称
            Map<String, String> cityCodeAndName = new HashMap<String, String>();
            cityCodeAndName = InitServlet.cityObj;
            gpName = cityCodeAndName.get(cityCode);

        } else {

            gpName = cityCode;
        }

        listAddressType = new ArrayList();
        if (null != gpName && !gpName.equals("")) {
            listAddressType = geograpPositionService.queryGeopositionByName(gpName);
        }
        return "divShow";

    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Long getGisId() {
        return gisId;
    }

    public void setGisId(Long gisId) {
        this.gisId = gisId;
    }

    public Long getLongitude() {
        return longitude;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }

    public Long getLatitude() {
        return latitude;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public Long getGptypeId() {
        return gptypeId;
    }

    public void setGptypeId(Long gptypeId) {
        this.gptypeId = gptypeId;
    }

    public String allFroward() {

        return "addGeograpPosition";
    }

    public IGeograpPositionService getGeograpPositionService() {
        return geograpPositionService;
    }

    public void setGeograpPositionService(IGeograpPositionService geograpPositionService) {
        this.geograpPositionService = geograpPositionService;
    }

    public String getOperationerId() {
        return operationerId;
    }

    public void setOperationerId(String operationerId) {
        this.operationerId = operationerId;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public HtlGeographicalposition getHtlGeographicalposition() {
        return htlGeographicalposition;
    }

    public void setHtlGeographicalposition(HtlGeographicalposition htlGeographicalposition) {
        this.htlGeographicalposition = htlGeographicalposition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOperationer() {
        return operationer;
    }

    public void setOperationer(String operationer) {
        this.operationer = operationer;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public List getListAddressType() {
        return listAddressType;
    }

    public void setListAddressType(List listAddressType) {
        this.listAddressType = listAddressType;
    }

}

package com.mangocity.hotel.order.service.impl;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OracleTypes;

import com.mangocity.framework.exception.ServiceException;
import com.mangocity.hotel.base.service.assistant.CalculateDistance;
import com.mangocity.hotel.base.service.assistant.HotelEmapResultInfo;
import com.mangocity.hotel.base.service.assistant.HotelQueryCondition;
import com.mangocity.hotel.order.service.IHotelEmapService;
import com.mangocity.mgis.app.service.baseinfo.GisService;
import com.mangocity.mgis.domain.entity.GisBaseInfo;
import com.mangocity.mgis.domain.valueobject.LatLng;
import com.mangocity.util.DateUtil;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.log.MyLog;

/**
 */
public class HotelEmapServiceImpl extends DAOHibernateImpl implements IHotelEmapService {

    private static final long serialVersionUID = 1L;
    private static final MyLog log = MyLog.getLogger(HotelEmapServiceImpl.class);

    private GisService gisService;

    public void setGisService(GisService gisService) {
        this.gisService = gisService;
    }



    /****
     * 查询酒店的修改实现
     * 
     * @param condition查询条件
     * @throws SQLException 
     * 
    
     */
    public List queryHotels(HotelQueryCondition condition, Double longitude, Double latitude,
        int dis, LatLng leftTopLatLng, LatLng leftBottomLatLng, LatLng rightTopLatLng,
        LatLng rightBottomLatLng, boolean isAllFlag,
        List<HotelEmapResultInfo> allHotelList) throws SQLException {
        DateUtil.getDay(condition.getBeginDate(), condition.getEndDate());

        List<HotelEmapResultInfo> list = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        try {
            cstmt = setQueryHotelParas(condition);
            cstmt.execute();
            cstmt.getObject(30);
            rs = (ResultSet) cstmt.getObject(31);
            list = getSearchResult(rs);
            rs = null;
            cstmt.close();
        } catch (SQLException e) {
            log.error("Query hotel info error! the cause is:" + e);
            cstmt.close();
        }finally{
            if(null!=rs){
                rs.close();
            }
            if(null!=cstmt){
                cstmt.close();
            }
        }

        List<HotelEmapResultInfo> mapList = null;

        /**
         * 返回给页面的List数据
         */
        List<HotelEmapResultInfo> returnMapList = null;

        /**
         * 框选后页面的List数据
         */
        List<HotelEmapResultInfo> dragMapList = null;

        // 经度,纬度不存在
        if (null == longitude || null == latitude) {
            // 调用gisid接口获取
            try {
                List gisList = gisService.getQueryList4CityAndType("", condition.getCityName(), -1);
                if ((null != gisList) && (0 < gisList.size())) {
                    GisBaseInfo baseInfo = (GisBaseInfo) gisList.get(0);
                    longitude = baseInfo.getLongitude();
                    latitude = baseInfo.getLatitude();
                }
            } catch (ServiceException e) {
                log.error("Get gisService getQueryList4CityAndType by "
                    + condition.getCityName() + " error: " + e);
            }
        }

        /**
         * 如果查询出来的酒店不为空
         */
        if (null != list && 0 < list.size()) {

            /**
             * 如果距离小于等于0
             */
            if (0 >= dis) {
                /**
                 * 设置总页数
                 */

                /**
                 * 如果isAllFlag为true显示所有酒店
                 */
                if (isAllFlag) {
                    for (HotelEmapResultInfo mapResultInfo : list) {
                        allHotelList.add(mapResultInfo);
                    }
                }

                /**
                 * 将框选酒店计算在内
                 */
                if (null != leftTopLatLng) {
                    dragMapList = new ArrayList<HotelEmapResultInfo>();
                    for (HotelEmapResultInfo mapResultInfo : list) {
                        if (0 < mapResultInfo.getLongitude() - 1) {
                            LatLng hotelLatLng = new LatLng();
                            hotelLatLng.setLongitude(mapResultInfo.getLongitude());
                            hotelLatLng.setLatitude(mapResultInfo.getLatitude());
                            if (getPointResult(hotelLatLng, leftTopLatLng, leftBottomLatLng,
                                rightTopLatLng, rightBottomLatLng)) {
                                dragMapList.add(mapResultInfo);
                            }
                        }
                    }
                    list = dragMapList;
                }

                int hotelCount = list.size();
                if (0 < condition.getPageSize()) {
                    if (0 < hotelCount % condition.getPageSize()) {
                        condition.setTotalPage(hotelCount / condition.getPageSize() + 1);
                    } else {
                        condition.setTotalPage(hotelCount / condition.getPageSize());
                    }
                }

                mapList = new ArrayList<HotelEmapResultInfo>();

                /**
                 * 如果经度,纬度为空, 搜索gooogle的API,读取相应的经度和纬度数据
                 */
                if (null == longitude || null == latitude) {
                    /**
                     * 如果不存在经度和纬度,直接返回相应的数据.
                     */

                    if (hotelCount < condition.getPageSize() * condition.getPageNo()) {
                        for (int i = condition.getPageSize() * 
                            (condition.getPageNo() - 1); i < hotelCount; i++) {
                            HotelEmapResultInfo mapResultInfo = list.get(i);
                            mapList.add(mapResultInfo);
                        }
                    } else {
                        /**
                         * 查询出来的酒店个数大于　condition.getPageSize() * condition.getPageNo()
                         */
                        for (int i = condition.getPageSize() * 
                            (condition.getPageNo() - 1); i < condition
                            .getPageSize()
                            * condition.getPageNo(); i++) {
                            HotelEmapResultInfo mapResultInfo = list.get(i);
                            // 酒店的精度与纬度
                            mapList.add(mapResultInfo);
                        }
                    }
                    return mapList;
                } else {
                    /**
                     * 依据精度来判断该关键字区域是否存在，
                     */
                    LatLng keyWordLatLng = new LatLng();
                    keyWordLatLng.setLongitude(longitude);
                    keyWordLatLng.setLatitude(latitude);
                    /**
                     * 只取范围内的酒店
                     */
                    if (hotelCount >= condition.getPageSize() * (condition.getPageNo() - 1)) {
                        /**
                         * 查询出来的酒店个数小于　condition.getPageSize() * condition.getPageNo()
                         */
                        if (hotelCount < condition.getPageSize() * condition.getPageNo()) {
                            for (int i = condition.getPageSize() * 
                                (condition.getPageNo() - 1); i < hotelCount; i++) {
                                HotelEmapResultInfo mapResultInfo = list.get(i);
                                // 酒店的精度与纬度
                                if ((0 < mapResultInfo.getLongitude() - 2)
                                    && (0 < mapResultInfo.getLatitude() - 2)) {
                                    LatLng hotelLatLng = new LatLng();
                                    hotelLatLng.setLongitude(mapResultInfo.getLongitude());
                                    hotelLatLng.setLatitude(mapResultInfo.getLatitude());
                                    Double distance = CalculateDistance.getDistance(keyWordLatLng,
                                        hotelLatLng);
                                    mapResultInfo.setDistance(Math.abs(distance));
                                }
                                mapList.add(mapResultInfo);
                            }
                        } else {
                            /**
                             * 查询出来的酒店个数大于　condition.getPageSize() * condition.getPageNo()
                             */
                            for (int i = condition.getPageSize() * 
                                (condition.getPageNo() - 1); i < condition
                                .getPageSize()
                                * condition.getPageNo(); i++) {
                                HotelEmapResultInfo mapResultInfo = list.get(i);
                                // 酒店的精度与纬度
                                if ((0 < mapResultInfo.getLongitude() - 2)
                                    && (0 < mapResultInfo.getLatitude() - 2)) {
                                    LatLng hotelLatLng = new LatLng();
                                    hotelLatLng.setLongitude(mapResultInfo.getLongitude());
                                    hotelLatLng.setLatitude(mapResultInfo.getLatitude());
                                    Double distance = CalculateDistance.getDistance(keyWordLatLng,
                                        hotelLatLng);
                                    mapResultInfo.setDistance(Math.abs(distance));
                                }
                                mapList.add(mapResultInfo);
                            }
                        }
                    }
                }
            } else {
                /**
                 * 如果距离大于0
                 */

                /**
                 * 如果isAllFlag为true显示所有酒店
                 */
                if (isAllFlag) {
                    for (HotelEmapResultInfo mapResultInfo : list) {
                        allHotelList.add(mapResultInfo);
                    }
                }

                /**
                 * 将框选酒店计算在内
                 */
                if (null != leftTopLatLng) {
                    dragMapList = new ArrayList<HotelEmapResultInfo>();
                    for (HotelEmapResultInfo mapResultInfo : list) {
                        if (0 < mapResultInfo.getLongitude() - 1) {
                            LatLng hotelLatLng = new LatLng();
                            hotelLatLng.setLongitude(mapResultInfo.getLongitude());
                            hotelLatLng.setLatitude(mapResultInfo.getLatitude());
                            if (getPointResult(hotelLatLng, leftTopLatLng, leftBottomLatLng,
                                rightTopLatLng, rightBottomLatLng)) {
                                dragMapList.add(mapResultInfo);
                            }
                        }
                    }
                    list = dragMapList;
                }

                mapList = new ArrayList<HotelEmapResultInfo>();

                LatLng keyWordLatLng = new LatLng();
                keyWordLatLng.setLongitude(longitude);
                keyWordLatLng.setLatitude(latitude);

                for (HotelEmapResultInfo hotelMapResultInfo : list) {
                    // 酒店的精度与纬度
                    if ((0 < hotelMapResultInfo.getLongitude() - 1)
                        && (0 < hotelMapResultInfo.getLatitude() - 1)) {
                        LatLng hotelLatLng = new LatLng();
                        hotelLatLng.setLongitude(hotelMapResultInfo.getLongitude());
                        hotelLatLng.setLatitude(hotelMapResultInfo.getLatitude());
                        Double distance = CalculateDistance.getDistance(keyWordLatLng, hotelLatLng);
                        hotelMapResultInfo.setDistance(Math.abs(distance));
                        if (distance < dis) {
                            mapList.add(hotelMapResultInfo);
                        }
                    }
                }

                /**
                 * 设置总页数
                 */
                int hotelCount = mapList.size();
                if (0 < condition.getPageSize()) {
                    if (0 < hotelCount % condition.getPageSize()) {
                        condition.setTotalPage(hotelCount / condition.getPageSize() + 1);
                    } else {
                        condition.setTotalPage(hotelCount / condition.getPageSize());
                    }
                }

                returnMapList = new ArrayList();
                /***
                 * 只取范围内的酒店
                 */
                if (hotelCount >= condition.getPageSize() * (condition.getPageNo() - 1)) {
                    /***
                     * 查询出来的酒店个数小于　condition.getPageSize() * condition.getPageNo()
                     */
                    if (hotelCount < condition.getPageSize() * condition.getPageNo()) {
                        for (int i = condition.getPageSize() * 
                            (condition.getPageNo() - 1); i < hotelCount; i++) {
                            HotelEmapResultInfo mapResultInfo = mapList.get(i);
                            // 酒店的精度与纬度
                            returnMapList.add(mapResultInfo);
                        }
                    } else {
                        /***
                         * 查询出来的酒店个数大于　condition.getPageSize() * condition.getPageNo()
                         */
                        for (int i = condition.getPageSize() * 
                            (condition.getPageNo() - 1); i < condition
                            .getPageSize()
                            * condition.getPageNo(); i++) {
                            HotelEmapResultInfo mapResultInfo = mapList.get(i);
                            // 酒店的精度与纬度

                            returnMapList.add(mapResultInfo);
                        }
                    }
                }
                return returnMapList;
            }

        }

        return mapList;
    }



    /**
     * 设置查询存储过程参数
     * 
     * @param condition
     * @return
     * @throws SQLException
     */
    @SuppressWarnings("unused")
    private CallableStatement setQueryHotelParas(HotelQueryCondition condition)
    throws SQLException {
        CallableStatement cstmt = null;
        try {
            String procedureName = "{call PKGHOTELQUERY_3_0.getHotelEmapList(?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            // cstmt = (CallableStatement)conn.prepareCall(procedureName);
            cstmt = super.getCurrentSession().connection().prepareCall(
                procedureName);
            cstmt.setString(1, condition.getSaleChannel()); // p_salechannel
            // VARCHAR2 -- 销售渠道
            // 114 ,b2b
            cstmt.setString(2, condition.getCityId()); // p_cityid VARCHAR2,
            // --城市id
            cstmt.setString(3, condition.getCityName()); // p_cityname
            // VARCHAR2, --城市名称
            cstmt.setString(4, condition.getDistrict()); // p_district
            // VARCHAR2, --城区
            cstmt.setString(5, condition.getBizZone()); // p_bizzone VARCHAR2,
            // --商业区
            cstmt.setString(6, condition.getScale()); // p_scale VARCHAR2,
            // --搜寻范围
            cstmt.setString(7, condition.getChnAddress()); // p_chnaddress
            // VARCHAR2,
            // --酒店中文地址
            cstmt.setString(8, condition.getStar()); // p_hotelstar
            // VARCHAR2, --酒店星级
            cstmt.setString(9, condition.getStar()); // p_star VARCHAR2,
            // --处理酒店星级 CheckBox
            cstmt.setString(10, DateUtil.dateToString(condition.getBeginDate())); // p_begindate
            // VARCHAR2
            // --开始日期(YYYY-MM-DD)
            cstmt.setString(11, DateUtil.dateToString(condition.getEndDate()));// p_enddate
            // VARCHAR2
            // --结束日期(YYYY-MM-DD)
            cstmt.setInt(12, condition.getDays()); // p_days NUMERIC, --入住天数

            // VARCHAR2,
            // --酒店中文名称
            if (null != condition.getIsEngHotelName() && null != condition.getHotelChnName()) {
                cstmt.setString(13, null);
                cstmt.setString(14, condition.getHotelChnName().trim()); // p_hotelengname
                // VARCHAR2,
                // --酒店英文名称
            } else {
                cstmt.setString(13, condition.getHotelChnName()); // p_hotelchnname
                cstmt.setString(14, null); // p_hotelengname
            }
            cstmt.setString(15, condition.getMinPrice()); // p_minprice
            // NUMERIC, --浠锋牸涓嬮檺
            cstmt.setString(16, condition.getMaxPrice()); // p_maxprice
            // NUMERIC, --价格上限
            cstmt.setString(17, condition.getHotelCode()); // p_hotelcode
            // VARCHAR2, --酒店代码
            cstmt.setLong(18, 0);// Long.valueOf(condition.getHotelId())); // p_hotelid
            // VARCHAR2, --酒店id
            cstmt.setString(19, condition.getMemberType()); // p_membertype
            // VARCHAR2, --会员类型
            cstmt.setLong(20, condition.getMemberId()); // p_memberid NUMERIC,
            // --会员id
            cstmt.setString(21, condition.getHotelType()); // p_hoteltype
            // VARCHAR2, --酒店类别
            cstmt.setString(22, condition.getRoomType()); // p_roomtype
            // VARCHAR2, --房型
            cstmt.setString(23, condition.getSpecialRequest()); // p_specialrequest
            // VARCHAR2,
            // --地理名胜范围
            cstmt.setInt(24, 800); // p_pagesize NUMERIC,
            // cstmt.setInt(26, condition.getPageSize()); // p_pagesize NUMERIC,
            // --每页记录数, 默认为10
            cstmt.setInt(25, condition.getPageNo()); // p_pageno NUMERIC,
            // --当前页, 默认为1
            cstmt.setInt(26, condition.getSortWay()); // p_sortway NUMERIC,
            // --排序方式 1:芒果网推荐 2:价格
            // 3:酒店星级
            // v2.8 设置查询的渠道来源 add by chenkeming
            cstmt.setString(27, condition.getFromChannel());
            cstmt.registerOutParameter(28, OracleTypes.NUMERIC); // return_pagecount
            // OUT
            // NUMERIC,
            cstmt.registerOutParameter(29, OracleTypes.NUMERIC); // return_recordcount
            // out
            // numeric,
            cstmt.registerOutParameter(30, OracleTypes.VARCHAR); // return_sql
            // OUT
            // VARCHAR2
            cstmt.registerOutParameter(31, OracleTypes.CURSOR); // return_list
            // out
            // outHotelList
        } catch (SQLException sqle) {
            log.error("Get the database connection error,error=" + sqle);
            throw sqle;
        }
        return cstmt;
    }

    /**
     * @param rs
     * @return
     * @throws SQLException
     */
    private List<HotelEmapResultInfo> getSearchResult(ResultSet rs) throws SQLException {
        List<HotelEmapResultInfo> list = new ArrayList<HotelEmapResultInfo>();
        HotelEmapResultInfo mapInfo = null;
        try {
            while (rs.next()) {
                mapInfo = new HotelEmapResultInfo();
                mapInfo.setHotelId(rs.getLong("hotel_id"));
                mapInfo.setHotelChnName(rs.getString("chn_name"));
                mapInfo.setHotelEngName(rs.getString("eng_name"));
                mapInfo.setHotelStar(rs.getString("hotel_star"));
                mapInfo.setChnAddress(rs.getString("chn_address"));
                mapInfo.setLowestPrice(rs.getDouble("LOWERPRICE"));
                mapInfo.setCommendType(rs.getLong("COMMENDTYPE"));
                mapInfo.setLongitude(rs.getDouble("longitude"));
                mapInfo.setLatitude(rs.getDouble("latitude"));
                mapInfo.setGisid(rs.getLong("gisid"));
                list.add(mapInfo);
            }
        } catch (SQLException e) {
            log.error("Get the database data error,error=" + e);
            throw e;
        }
        return list;
    }

    public boolean getPointResult(LatLng middleLatLng, LatLng leftTopLatLng,
        LatLng leftBottomLatLng, LatLng rightTopLatLng, LatLng rightBottomLatLng) {
        boolean longitudePoint;
        boolean latitudePoint;
        Double midLongitude = middleLatLng.getLongitude();
        Double midLatitude = middleLatLng.getLatitude();
        if (midLongitude >= leftTopLatLng.getLongitude()
            && midLongitude <= rightTopLatLng.getLongitude()) {
            longitudePoint = true;
        } else {
            longitudePoint = false;
        }

        if (midLatitude >= leftBottomLatLng.getLatitude()
            && midLatitude <= leftTopLatLng.getLatitude()) {
            latitudePoint = true;
        } else {
            latitudePoint = false;
        }

        // 如果两者为真就为真,否则为假
        return longitudePoint & latitudePoint;

    }
}

package com.mangocity.hotel.base.dao.impl;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OracleTypes;

import com.mangocity.hotel.base.dao.IHotelEmapDao;
import com.mangocity.hotel.base.service.assistant.HotelEmapResultInfo;
import com.mangocity.hotel.base.service.assistant.HotelQueryCondition;
import com.mangocity.util.DateUtil;
import com.mangocity.util.dao.GenericDAOHibernateImpl;
import com.mangocity.util.log.MyLog;

public class HotelEmapDaoImpl extends GenericDAOHibernateImpl implements IHotelEmapDao{
	 private static final long serialVersionUID = 1L;
	 private static final MyLog log = MyLog.getLogger(HotelEmapDaoImpl.class);
	 
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

	public List<HotelEmapResultInfo> queryHotelsByCondition(HotelQueryCondition condition) throws SQLException {
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
	        return list;
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

}

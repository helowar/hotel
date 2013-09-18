package com.mangocity.hotel.base.dao.impl;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleTypes;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mangocity.hotel.base.dao.HotelQueryDao;
import com.mangocity.hotel.base.persistence.HtlAssure;
import com.mangocity.hotel.base.persistence.HtlFavourableclause;
import com.mangocity.hotel.base.persistence.HtlPreconcertItem;
import com.mangocity.hotel.base.service.assistant.HotelQueryCondition;
import com.mangocity.hotel.base.service.assistant.HotelResultInfo;
import com.mangocity.hotel.base.service.assistant.URLClient;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hweb.persistence.QueryHotelForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebResult;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.dao.DAO;
import com.mangocity.util.dao.DAOHibernateReadOnly;
import com.mangocity.util.log.MyLog;

/**
 * 酒店查询Dao接口实现
 * @author chenjiajie
 *
 */
@Transactional
public class HotelQueryDaoImpl implements HotelQueryDao {

    private static final long serialVersionUID = -2814469486747180296L;

    private static final MyLog log = MyLog.getLogger(HotelQueryDaoImpl.class);
    
    /**
     * 读取只读数据库的DAO类 
     */
    private DAOHibernateReadOnly readOnlyBaseDao;
    
    /**
     * 在线数据库的DAO类
     */
    private DAO entityManager;
    
    /**
     * 地图查询酒店 
     */
    private URLClient urlClient;
    
    /**
     * CC查询数据库，并把数据库返回的游标缓存到List中
     * @param condition
     * @return Map 包括记录数和返回数据
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly=true,propagation=Propagation.REQUIRED)
    public Map queryHotelResultListForCC(HotelQueryCondition condition) throws SQLException{
        Map returnMap = new HashMap();
        List<HotelResultInfo> resultList = null;
        Integer totalSize = new Integer(0);
        CallableStatement cstmt = null;
        ResultSet rs = null;
        try {
            cstmt = setQueryHotelParas(condition);
            cstmt.execute();

            Object dd = cstmt.getObject(34);
            BigDecimal ssb = (BigDecimal) dd;
            long ssbss = ssb.longValue();
            totalSize = Long.valueOf(ssbss).intValue();
            rs = (ResultSet) cstmt.getObject(37);
            resultList = getSearchResultForCC(rs, condition);
            rs = null;
            cstmt.close();
        } catch (SQLException e) {
            log.error("Query hotel info error! the cause is:" + e);
            cstmt.close();
        } finally {
            if(null != rs) {
                rs.close();
            }
            if(null != cstmt) {
                cstmt.close();
            }
        }
        returnMap.put(KEY_RESULT_LIST, resultList);
        returnMap.put(KEY_TOTAL_SIZE, totalSize);
        return returnMap;
    }
    
    
    /**
     * 设置查询存储过程参数
     * 
     * @param condition
     * @return
     * @throws SQLException
     */
    private CallableStatement setQueryHotelParas(HotelQueryCondition condition)
    throws SQLException {
        CallableStatement cstmt = null;
        try {
            String procedureName = "{call pkghotelquery_3_0.getHotelList(?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            
            Date nowDate = DateUtil.getDate(new Date());
            //checkindate 减 当前日期
           // int diffDayBeginMinusNow = DateUtil.getDay(nowDate, condition.getBeginDate());
            //checkoutdate 减 当前日期
            //int duffDayEndMinusNow = DateUtil.getDay(nowDate, condition.getEndDate());
            
            cstmt = entityManager.getCurrentSession().connection().prepareCall(procedureName);
            
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
            
            // LONG, --酒店Id
            if(StringUtil.isValidStr(condition.getHotelId())) {
                cstmt.setLong(18, Long.valueOf(condition.getHotelId()));
            } else {
                cstmt.setLong(18, 0);    
            }
            
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

            // 地图接口返回的城市id串(以","分割)
            if (StringUtil.isValidStr(condition.getNotes())
                && StringUtil.isValidStr(condition.getScale())
                && StringUtil.isValidStr(condition.getCityId())) {
                String cityIds = urlClient.getMapHotel(InitServlet.platObj.get(condition
                    .getCityId()), condition.getNotes(), Double.parseDouble(condition.getScale()));
                cstmt.setString(24, cityIds); // p_notes VARCHAR2,
            } else {
                cstmt.setString(24, null); // p_notes VARCHAR2,
            }

            // --地理名胜
            cstmt.setString(25, condition.getNotesScope()); // p_notesscope
            // VARCHAR2,
            // --地理名胜范围
            cstmt.setInt(26, condition.getPageSize()); // p_pagesize NUMERIC,
            // --每页记录数, 默认为10
            cstmt.setInt(27, condition.getPageNo()); // p_pageno NUMERIC,
            // --当前页, 默认为1
            cstmt.setInt(28, condition.getSortWay()); // p_sortway NUMERIC,
            // --排序方式 1:芒果网推荐 2:价格
            // 3:酒店星级

            // v2.8 设置查询的渠道来源 add by chenkeming
            cstmt.setString(29, condition.getFromChannel());

            // v2.9.3.1 优惠立减查询标志 add by chenjiajie 2009-10-15
            cstmt.setInt(30, condition.getHasBenefit());

            // RMS3008 增加"合作方"查询条件 add by chenjiajie 2010-01-07
            cstmt.setString(31, condition.getCooperator());
            
            //查询性能优化 是否忽略count总数,1:忽略;0:不忽略,默认是0;页面总数通过异步获取 add by chenjiajie 2010-01-19
            cstmt.setInt(32, condition.getIgnorePageCount());
            
            //查询性能优化 是否忽略查询酒店List,1:忽略;0:不忽略,默认是0;页面总数通过异步获取 add by chenjiajie 2010-01-19
            cstmt.setInt(33, condition.getIgnoreQueryList());
            
            
            cstmt.registerOutParameter(34, OracleTypes.NUMERIC); // return_pagecount
            // OUT
            // NUMERIC,
            cstmt.registerOutParameter(35, OracleTypes.NUMERIC); // return_recordcount
            // out
            // numeric,
            cstmt.registerOutParameter(36, OracleTypes.VARCHAR); // return_sql
            // OUT
            // VARCHAR2
            cstmt.registerOutParameter(37, OracleTypes.CURSOR); // return_list
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
     * @param condition
     * @return
     * @throws SQLException
     */
    private List<HotelResultInfo> getSearchResultForCC(ResultSet rs,
			HotelQueryCondition condition) throws SQLException {
		List<HotelResultInfo> list = new ArrayList<HotelResultInfo>();
        HotelResultInfo info = null;
        try {
            while (rs.next()) {
                info = new HotelResultInfo();
                info.setLowestPrice(rs.getFloat("LowestPrice"));
                info.setHotelId(rs.getLong("HotelId"));
                info.setHotelChnName(rs.getString("HotelChnName"));
                info.setHotelEngName(rs.getString("HotelEngName"));
                info.setHotelStar(rs.getString("HotelStar"));
                info.setHotelStar1(rs.getString("HotelStar1"));
                info.setHotelType(rs.getString("HotelType"));
                info.setChnAddress(rs.getString("ChnAddress"));
                info.setHotelIntroduce(rs.getString("HotelIntroduce"));
                info.setHotelChnIntroduce(rs.getString("HotelChnIntroduce"));
                info.setHotel_comm_type(rs.getString("Hotel_comm_type"));
                info.setAlertMessage(rs.getString("ClueInfo"));
                info.setBizZone(rs.getString("BIZZONE"));
                info.setCity(rs.getString("City"));
                /** 增加酒店所属省份和城区的查询结果 hotel2.9.2 add by chenjiajie 2009-08-18 begin **/
                info.setState(rs.getString("HOTELSTATE"));
                info.setZone(rs.getString("HOTELZONE"));
                /** 增加酒店所属省份和城区的查询结果 hotel2.9.2 add by chenjiajie 2009-08-18 end **/
                String str400 = rs.getString("IsContract");
                info.setFlag400(StringUtil.isValidStr(str400) && str400.equals("1") ? true : false);

                // --价格类型
                info.setChildRoomTypeId(rs.getString("ChildRoomTypeId"));
                info.setComm_level(rs.getInt("Comm_level"));
                
                //hotel 2.9.3 房间数 add by shengwei.zuo 2009-09-09
                info.setRoom_qty(rs.getInt("Room_qty"));
                
                info.setRoom_type_id(rs.getLong("Room_type_id"));
                info.setRoom_name(rs.getString("Room_name"));
                info.setSalesPrice(rs.getDouble("SalesPrice"));
                info.setPrice_type(rs.getString("Price_type"));
                info.setClose_flag(rs.getString("close_flag"));
                info.setAcceptCustom(rs.getString("ACCEPT_CUSTOM"));
                info.setReason(dearCloseRoomResaon(rs.getString("Reason")));

                // base price BASEPRICE
                info.setBasePrice(rs.getDouble("baseprice"));

                // --配额类型及支付方式
                info.setQuotaType(rs.getString("Quota_Type"));
                info.setPayMethod(rs.getString("Pay_Method"));

                // --房间配额 价格
                info.setAvail_qty(rs.getInt("Avail_qty"));

                info.setAble_sale_date(new java.util.Date(rs.getDate("Able_sale_date").getTime()));
                info.setCurrency(rs.getString("Currency"));
                info.setInc_breakfast_type(rs.getString("Inc_breakfast_type"));
                info.setInc_breakfast_number(rs.getString("Inc_breakfast_number"));
                info.setRoom_state(rs.getString("Room_state"));
                info.setQuota_batch_id(rs.getLong("Quota_batch_id"));
                info.setQuota_pattern(rs.getString("Quota_pattern"));
                info.setSalesroom_price(rs.getFloat("Salesroom_price"));
                info.setHotel_currency(rs.getString("Hotel_currency"));
                info.setHotel_balanceMethod(rs.getString("Hotel_balanceMethod"));
                // 直联标志 add by wuyun 2008-11-26 15:30
                // info.setCooperateChannel("1");
                info.setCooperateChannel(rs.getString("cooperateChannel"));

                // add by chenkeming@2009-02-06
                int nNeedAssure = rs.getInt("NEED_ASSURE");
                info.setNeedAssure(1 == nNeedAssure ? true : false);
                info.setPayToPrepay(rs.getInt("PAY_TO_PREPAY"));
                
                // 交行全卡商旅等渠道无需芒果网优惠信息 modify by chenkeming
                if(!"1".equals(condition.getForCooperate())) {
                    // 优惠立减标志 1:有,0:无 add by chenjiajie 2009-10-15
                    info.setHasBenefit(rs.getInt("HASBENEFIT"));	
                    
                    // 取芒果网优惠信息
                    info.setHasPreSale(rs.getInt("hasPreSale"));
                    if (1 == info.getHasPreSale()) {
                        info.setPreSaleName(rs.getString("preSaleName"));
                        info.setPreSaleContent(rs.getString("preSaleContent"));
                        /*
                         * if(StringUtil.isValidStr(tempStr)) {
                         * info.setPreSaleContent(tempStr.replaceAll("\n", "<br>")); }
                         */
                        info.setPreSaleBeginEnd(rs.getString("preSaleBeginEnd"));
                        info.setPreSaleURL(rs.getString("preSaleURL"));
                    }
                    
                    info.setHasCashReturn(rs.getInt("hasCashReturn"));
                } else {
                	info.setHasBenefit(0);
                	info.setHasPreSale(0);
                	info.setHasCashReturn(0);
                }
                
                // add by zhineng.zhuang 2009-02-16
                if (null != rs.getDate("LATEST_BOOKABLE_DATE")) {
                    info.setLatestBookableDate(new java.util.Date(rs
                        .getDate("LATEST_BOOKABLE_DATE").getTime()));
                }
                info.setLatestBokableTime(rs.getString("LATEST_BOKABLE_TIME"));
                
                
                /**hotel 2.9.3  
                 * 新增时间段的预订规则 add by shengwei.zuo 2009-09-06 begin  
                 */
                
                if (null != rs.getDate("FIRST_BOOKABLE_DATE")) {
                    info.setFirstBookableDate(new java.util.Date(rs
                        .getDate("FIRST_BOOKABLE_DATE").getTime()));
                }
                info.setFirstBookableTime(rs.getString("FIRST_BOOKABLE_TIME"));

                /**hotel 2.9.3  
                 * 新增时间段的预订规则 add by shengwei.zuo 2009-09-06 end  
                 */
                
                
                if (null != rs.getDate("MUST_LAST_DATE")) {
                    info
                        .setMustLastDate(new java.util.Date(rs.getDate(
                            "MUST_LAST_DATE").getTime()));
                }
                if (null != rs.getDate("MUST_FIRST_DATE")) {
                    info.setMustFirstDate(new java.util.Date(rs.getDate("MUST_FIRST_DATE")
                        .getTime()));
                }
                info.setContinueDay(rs.getLong("CONTINUE_DAY"));
                // v2.9.2 增加限住天数 2009-07-28 lixiaoyong begin
                info.setMinRestrictNights(rs.getLong("MIN_RESTRICT_NIGHTS"));
                info.setMaxRestrictNights(rs.getLong("MAX_RESTRICT_NIGHTS"));
                // v2.9.2 增加限住天数 2009-07-28 lixiaoyong end
                info.setMustInDate(rs.getString("MUST_IN_DATE"));
                // addby chenjuesu 增加必住日期关系
                info.setMustInDatesRelation(rs.getString("CONTINUE_DATES_RELATION"));
                int nHasReserv = rs.getInt("HAS_RESERV");
                info.setHasReserv(1 == nHasReserv ? true : false);

                String flagHK = rs.getString("flagCtsHK");
                info.setFlagCtsHK("1".equals(flagHK) ? true : false);
                // info.setFlagCtsHK(true);

                // v2.8 设置某支付方式的渠道 add by chenkemign@2009-03-20
                info.setRoomChannel(rs.getInt("roomChannel"));
                // info.setRoomChannel(ChannelType.CHANNEL_CTS);

                // v2.8.1 设置是否停止销售 add by chenkeming@2009-05-12
                String flagStopSell = rs.getString("flagStopSell");
                info.setFlagStopSell("1".equals(flagStopSell) ? true : false);
                
                info.setStopSellReason(rs.getInt("stopSellReason"));
                info.setStopSellNote(rs.getString("stopSellNote"));
                

                // 取子房型优惠信息
                info.setHasPromo(rs.getInt("hasPromo"));
                if (1 == info.getHasPromo()) {
                    String tempStr = rs.getString("promoContent");
                    if (StringUtil.isValidStr(tempStr)) {
                        info.setPromoContent(tempStr.replaceAll("\n", "<br>"));
                    }
                    info.setPromoBeginEnd(rs.getString("promoBeginEnd"));
                }

                //佣金计算公式
                info.setFormulaId(rs.getString("formulaid"));
                info.setCommission(rs.getDouble("COMMISSION"));
                info.setCommissionrate(rs.getDouble("COMMISSION_RATE"));                

                list.add(info);
            }
        } catch (SQLException e) {
            log.error("Get the database data error,error=" + e);
            throw e;
        }
        return list;
    }
    
    /**
     * 过滤关房原因组合 add by zhineng.zhuang 2008-10-31
     * 
     * @param allReason
     *            关房原因：5，策略性关房CC可订；6，策略性关房CC不可订
     * @return
     */
    public String dearCloseRoomResaon(String allReason) {
        String dearedReason = "";
        if (!("".equals(allReason)) && null != allReason) {
            if (0 <= allReason.indexOf("5")) {
                dearedReason = allReason.replace("5,", "");
            } else {
                dearedReason = allReason;
            }
            if (0 <= allReason.indexOf("6")) {
                dearedReason = "6,";
                return dearedReason;
            }
        }
        return dearedReason;
    }
    
    /**
     * 查询传入的所有价格类型的连住优惠信息
     * @param allPriceTypeList
     * @return 
     */
    @SuppressWarnings("unchecked")
    public Map<String,List<HtlFavourableclause>> queryAllFavourableList(String allPriceTypeList){
        //缓存参数传入的所有价格类型连住优惠信息
        Map<String,List<HtlFavourableclause>> favourableMap = new HashMap<String,List<HtlFavourableclause>>();
        if(StringUtil.isValidStr(allPriceTypeList)){
            String hsql = "from HtlFavourableclause where priceTypeId in ("+allPriceTypeList+") and endDate >= trunc(sysdate)";
            List<HtlFavourableclause> htlFavourableclauseList  = entityManager.doquery(hsql, false);
            
            if(null != htlFavourableclauseList && !htlFavourableclauseList.isEmpty()){
                //创建所有的key
                for (Iterator it = htlFavourableclauseList.iterator(); it.hasNext();) {
                    HtlFavourableclause htlFavourableclause = (HtlFavourableclause) it.next();
                    //生成连住优惠信息Map的key 
                    String mapKey = generateFavourableListKey(htlFavourableclause.getHotelId(), htlFavourableclause.getPriceTypeId());
                    favourableMap.put(mapKey, new ArrayList<HtlFavourableclause>());
                }
                //对每个key的值进行赋值
                for (Iterator it = htlFavourableclauseList.iterator(); it.hasNext();) {
                    HtlFavourableclause htlFavourableclause = (HtlFavourableclause) it.next();
                    //生成连住优惠信息Map的key 
                    String mapKey = generateFavourableListKey(htlFavourableclause.getHotelId(), htlFavourableclause.getPriceTypeId());
                    //缓存某个酒店价格类型连住优惠信息
                    List<HtlFavourableclause> favourableclauseListCached = favourableMap.get(mapKey);
                    favourableclauseListCached.add(htlFavourableclause);
                    favourableMap.put(mapKey, favourableclauseListCached);
                }
            }
        }
        return favourableMap;
    }
    
	/**
	 * 据酒店和价格计划查询某时间优惠信息
	 * 
	 * @param hotelId
	 * @param roomTypeId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HtlFavourableclause> queryFavourableByHotelIdAndPriceTypeId(Long hotelId,
			String priceTypeId, Date checkInDate, Date checkOutDate) {
		String hsql = "from HtlFavourableclause where hotelId=? and priceTypeId=? and beginDate < ? and endDate>=?";
		List<HtlFavourableclause> favourabletList = entityManager
				.doquery(hsql, new Object[] { hotelId, new Long(priceTypeId), checkOutDate,
						checkInDate },false);
		if (favourabletList != null) {
			for (HtlFavourableclause htlFavourableclause : favourabletList) {
				Hibernate.initialize(htlFavourableclause.getLstPackagerate());
			}
		}

		return favourabletList;
	}
    
    /**
     * 生成连住优惠信息Map的key
     * @param hotelId
     * @param priceTypeId
     * @return
     */
    public String generateFavourableListKey(Long hotelId,Long priceTypeId){
        String mapKey = hotelId + "_" + priceTypeId;
        return mapKey;
    }
    
    /**
     * 查询传入的所有价格类型在某一天的担保信息
     * @param hotelIdList
     * @param allPriceTypeList
     * @param beginDate check-in date
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String,List<List<HtlAssure>>> queryAllAssureListByBeginDate(String hotelIdList, String allPriceTypeList, Date beginDate){
      //缓存参数传入的所有价格类型连住优惠信息
        Map<String,List<List<HtlAssure>>> htlPreconcertItemMap = new HashMap<String,List<List<HtlAssure>>>();
        if(StringUtil.isValidStr(allPriceTypeList)){
            String hsql = "from HtlPreconcertItem where hotelId in (" + hotelIdList + ") and priceTypeId in ("+allPriceTypeList+") and validDate = ? and Active = 1";
            List<HtlPreconcertItem> htlPreconcertItemList = entityManager.doquery(hsql, beginDate, false);
            if(null != htlPreconcertItemList && !htlPreconcertItemList.isEmpty()){
                for (Iterator it = htlPreconcertItemList.iterator(); it
                        .hasNext();) {
                    HtlPreconcertItem htlPreconcertItem = (HtlPreconcertItem) it.next();
                    //生成担保信息Map的key
                    String mapKey = generateAssureListKey(htlPreconcertItem.getHotelId(), htlPreconcertItem.getPriceTypeId(),beginDate);
                    htlPreconcertItemMap.put(mapKey, new ArrayList<List<HtlAssure>>());
                }
                
                for (Iterator it = htlPreconcertItemList.iterator(); it
                        .hasNext();) {
                    HtlPreconcertItem htlPreconcertItem = (HtlPreconcertItem) it
                            .next();
                    // 生成担保信息Map的key
                    String mapKey = generateAssureListKey(htlPreconcertItem.getHotelId(), 
                                                        htlPreconcertItem.getPriceTypeId(),
                                                        beginDate);
                    List<List<HtlAssure>> assureListInMap = htlPreconcertItemMap.get(mapKey);
                    assureListInMap.add(htlPreconcertItem.getHtlAssureList());
                    htlPreconcertItemMap.put(mapKey, assureListInMap);
                }
            }
        }
        return htlPreconcertItemMap;
    }
    
    /**
     * 生成担保信息Map的key
     * @param hotelId
     * @param priceTypeId
     * @return
     */
    public String generateAssureListKey(Long hotelId,Long priceTypeId,Date beginDate){
        String mapKey = hotelId + "_" + priceTypeId + "_" + DateUtil.dateToString(beginDate);
        return mapKey;
    }
    
    /** getter and setter **/
    public DAOHibernateReadOnly getReadOnlyBaseDao() {
        return readOnlyBaseDao;
    }

    public void setReadOnlyBaseDao(DAOHibernateReadOnly readOnlyBaseDao) {
        this.readOnlyBaseDao = readOnlyBaseDao;
    }

    public DAO getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(DAO entityManager) {
        this.entityManager = entityManager;
    }

    public URLClient getUrlClient() {
        return urlClient;
    }

    public void setUrlClient(URLClient urlClient) {
        this.urlClient = urlClient;
    }
}

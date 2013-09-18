package com.mangocity.hweb.dao.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleTypes;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mangocity.hotel.exception.DAException;
import com.mangocity.hweb.dao.HWebHotelDAO;
import com.mangocity.hweb.persistence.QueryHotelForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebResult;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.dao.GenericDAOHibernateImpl;
import com.mangocity.util.log.MyLog;
import com.mangocity.webnew.persistence.HtlChannelClickLog;

public class HWebHotelDAOImpl extends GenericDAOHibernateImpl implements HWebHotelDAO {
	
	private static final MyLog log = MyLog.getLogger(HWebHotelDAOImpl.class);
	
	public static final String KEY_RESULT_LIST = "keyResultList";

    public static final String KEY_TOTAL_SIZE = "keyTotalSize";
    
    private static final String PROCEDURE_QUERY_HOTEL_PRICE_DETAIL = 
    	"{call PKGHOTELQUERY_3_0.getHWEBPrice(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
    
    private static final String PROCEDURE_QUERY_HOTEL = "{call PKGHOTELQUERY_3_0.getHWEBList(" +
    		"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
    
    private static final String PROCEDURE_QUERY_THEME = "{call PKGHOTELQUERY_3_0.getHWEBThemeList(" +
    		"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

    @SuppressWarnings(value={"unchecked", "deprecation"})
    @Transactional(readOnly=true,propagation=Propagation.REQUIRED)
    public List<QueryHotelForWebResult> queryPriceDetailForWeb(Long hotelID, Date beginDate, Date endDate, 
            Date beginDateExt, Date endDateExt, Long roomTypeID, Long childRoomTypeID, String payMethod) {
    	Map inParamIdxAndValue = getInParamIdxAndValueMap4PriceQuery(hotelID, beginDate, endDate, beginDateExt, 
				endDateExt, roomTypeID, childRoomTypeID, payMethod);    	
    	Map<Integer, Integer> outParamIdxAndType = getOutParamIdxAndTypeMap4PriceQuery();    	
        Map<Integer, ?> resultMap = super.execProcedure(PROCEDURE_QUERY_HOTEL_PRICE_DETAIL, inParamIdxAndValue, 
        		outParamIdxAndType);
        
        ResultSet rs = (ResultSet) resultMap.get(Integer.valueOf(12));
        if(rs == null){
        	return Collections.EMPTY_LIST;
        }
            
        return getResultListFromResultSet4PriceQuery(rs);
    }
    
    /**
     * 得到净价
     */
    public List queryAdvicePriceForWeb(Long hotelID, Date beginDate,
            Date endDate, Date beginDateExt, Date endDateExt, Long roomTypeID, Long childRoomTypeID,
            String payMethod){
    	String sql="select p.able_sale_date,p.advice_price from hwtemp_htl_price p " +
    			"where p.HOTEL_ID = ? " +
    			"and p.room_type_id = ? " +
    			"and p.CHILD_ROOM_TYPE_ID = ? " +
    			"and p.able_sale_date >=to_date( ? ,'yyyy-MM-dd') " +
    			"and p.able_sale_date <to_date(nvl( ? ,'2999-12-30'),'yyyy-MM-dd') " +
    			"and p.pay_method = ?";
    	return super.queryByNativeSQL(sql, 
    			new Object[]{hotelID,roomTypeID,childRoomTypeID,
    			             DateUtil.dateToString(beginDateExt),
    			             DateUtil.dateToString(endDateExt),
    			             payMethod});
    }

	private Map<Integer, Integer> getOutParamIdxAndTypeMap4PriceQuery() {
		//输出参数的索引和数据类型键值对
    	Map<Integer, Integer> outParamIdxAndType = new HashMap<Integer, Integer>(4);
    	outParamIdxAndType.put(Integer.valueOf(9), Integer.valueOf(java.sql.Types.NUMERIC));
    	outParamIdxAndType.put(Integer.valueOf(10), Integer.valueOf(java.sql.Types.NUMERIC));
    	outParamIdxAndType.put(Integer.valueOf(11), Integer.valueOf(java.sql.Types.VARCHAR));
    	outParamIdxAndType.put(Integer.valueOf(12), Integer.valueOf(OracleTypes.CURSOR));
		return outParamIdxAndType;
	}

	@SuppressWarnings("unchecked")
	private Map getInParamIdxAndValueMap4PriceQuery(Long hotelID, Date beginDate, Date endDate, Date beginDateExt, 
			Date endDateExt, Long roomTypeID,Long childRoomTypeID, String payMethod) {
		//输入参数的索引和参数值键值对
    	Map inParamIdxAndValue = new HashMap(8);
    	inParamIdxAndValue.put(Integer.valueOf(1), hotelID);
    	inParamIdxAndValue.put(Integer.valueOf(2), DateUtil.dateToString(beginDate));
    	inParamIdxAndValue.put(Integer.valueOf(3), DateUtil.dateToString(endDate));
    	inParamIdxAndValue.put(Integer.valueOf(4), DateUtil.dateToString(beginDateExt));
    	inParamIdxAndValue.put(Integer.valueOf(5), DateUtil.dateToString(endDateExt));    	
    	inParamIdxAndValue.put(Integer.valueOf(6), roomTypeID);
    	inParamIdxAndValue.put(Integer.valueOf(7), childRoomTypeID);
    	inParamIdxAndValue.put(Integer.valueOf(8), payMethod);
		return inParamIdxAndValue;
	}

	private List<QueryHotelForWebResult> getResultListFromResultSet4PriceQuery(ResultSet rs) {
		// 组装返回结果
        List<QueryHotelForWebResult> results = new ArrayList<QueryHotelForWebResult>();
        try{
            while (rs.next()) {
                QueryHotelForWebResult info = new QueryHotelForWebResult();

                info.setSalesPrice(rs.getDouble("SalesPrice"));
                info.setClose_flag(rs.getString("Close_flag"));//开关房标志
                info.setAvailQty(rs.getInt("Avail_qty"));//配额数量
                info.setAble_sale_date(rs.getString("Able_sale_date"));//房间间夜日期
                info.setCurrency(rs.getString("Currency"));//酒店币种
                info.setBreakfastType(rs.getInt("Inc_breakfast_type"));//早餐类型
                info.setBreakfastNum(rs.getInt("Inc_breakfast_number"));//早餐数量
                info.setRoom_state(rs.getString("Room_state"));//房态
                info.setRoomNet(rs.getString("roomNet"));//房型宽带信息
                // 子房型能否预订信息
                info.setCanBook(rs.getInt("canBook"));
                if (0 == info.getCanBook()) {
                    info.setCantBookReason(rs.getString("cantBookReason"));//如果根据预订条款该房型不能预订，则保存原因信息
                }
                // 网站订单页面重新查询时需要重新确定是否面转预
                info.setPayToPrepay(rs.getInt("pay_to_prepay"));
                
                // add by linpeng.fang 返现需要佣金和佣金率计算净价
                info.setCommission(rs.getDouble("COMMISSION"));
                info.setCommissionrate(rs.getDouble("COMMISSION_RATE"));
                info.setFormulaId(rs.getString("FORMULAID"));
                // add by longkangfu
                info.setBasePrice(rs.getDouble("baseprice"));
                results.add(info);
            }
        } catch(SQLException sqle) {
        	throw new DAException(DataAccessError.FETCH_RESULTSET_FAILED, sqle);
        }
		return results;
	}
    
    /**
     * 查询数据库，并把数据库返回的游标缓存到List中
     * @param queryBean
     * @return Map 包括记录数和返回数据
     */
    @SuppressWarnings(value={"unchecked", "deprecation"})
    @Transactional(readOnly=true,propagation=Propagation.REQUIRED)
    public Map queryHotelResultListForWeb(QueryHotelForWebBean queryBean) {
    	Map inParamIdxAndValue = getInParamIdxAndValueMap4HotelQuery(queryBean);        
    	Map<Integer, Integer> outParamIdxAndType = getOutParamIdxAndValueMap4HotelQuery();    	
        Map<Integer, ?> resultMap = super.execProcedure(PROCEDURE_QUERY_HOTEL, inParamIdxAndValue, outParamIdxAndType);
        
        ResultSet rs = (ResultSet) resultMap.get(Integer.valueOf(26));
        Map returnMap = new HashMap(2);
        if(rs == null) {
        	returnMap.put(KEY_RESULT_LIST, Collections.EMPTY_LIST);
            returnMap.put(KEY_TOTAL_SIZE, Integer.valueOf(0));
            
            return returnMap;
        }
        
        List<QueryHotelForWebResult> results = null;
        Integer totalSize = null; // 查询结果后返回总记录数
        try {
        	totalSize = Integer.valueOf(((BigDecimal) resultMap.get(Integer.valueOf(24))).toString());
            results = getResultListFromResultSet4HotelQuery(rs, queryBean);
        } catch (DAException e) {
            log.error("Query hotel info error! the cause is:" + e,e);
        } 
        returnMap.put(KEY_RESULT_LIST, results);
        returnMap.put(KEY_TOTAL_SIZE, totalSize);
        return returnMap;
    }

	private Map<Integer, Integer> getOutParamIdxAndValueMap4HotelQuery() {
		//输出参数的索引和数据类型键值对
    	Map<Integer, Integer> outParamIdxAndType = new HashMap<Integer, Integer>(4);
    	outParamIdxAndType.put(Integer.valueOf(23), Integer.valueOf(java.sql.Types.NUMERIC));
    	outParamIdxAndType.put(Integer.valueOf(24), Integer.valueOf(java.sql.Types.NUMERIC));
    	outParamIdxAndType.put(Integer.valueOf(25), Integer.valueOf(java.sql.Types.VARCHAR));
    	outParamIdxAndType.put(Integer.valueOf(26), Integer.valueOf(OracleTypes.CURSOR));
		return outParamIdxAndType;
	}

	@SuppressWarnings("unchecked")
	private Map getInParamIdxAndValueMap4HotelQuery(
			QueryHotelForWebBean queryBean) {
		//输入参数的索引和参数值键值对
    	Map inParamIdxAndValue = new HashMap(22);
    	inParamIdxAndValue.put(Integer.valueOf(1), queryBean.getCityId());
    	inParamIdxAndValue.put(Integer.valueOf(2), queryBean.getCityName());
    	inParamIdxAndValue.put(Integer.valueOf(3), queryBean.getDistrict());// 城区
    	inParamIdxAndValue.put(Integer.valueOf(4), queryBean.getBizZone());// 商业区
    	inParamIdxAndValue.put(Integer.valueOf(5), queryBean.getHotelId());    	
    	inParamIdxAndValue.put(Integer.valueOf(6), queryBean.getHotelName());
    	
    	String star = queryBean.getHotelStar();
        if (null != star && 0 < star.length()
            && star.substring(star.length() - 1, star.length()).equals(",")) {
        	queryBean.setHotelStar(star.substring(0, star.length() - 1));
        }
    	inParamIdxAndValue.put(Integer.valueOf(7), queryBean.getHotelStar());
    	inParamIdxAndValue.put(Integer.valueOf(8), DateUtil.dateToString(queryBean.getInDate()));// 开始日期
    	inParamIdxAndValue.put(Integer.valueOf(9), DateUtil.dateToString(queryBean.getOutDate()));
    	inParamIdxAndValue.put(Integer.valueOf(10), queryBean.getMinPrice());
    	inParamIdxAndValue.put(Integer.valueOf(11), queryBean.getMaxPrice());
    	inParamIdxAndValue.put(Integer.valueOf(12), queryBean.getSpecialRequest());// 特殊要求
    	inParamIdxAndValue.put(Integer.valueOf(13), queryBean.getPageSize()); // 每页记录数, 默认为15
    	inParamIdxAndValue.put(Integer.valueOf(14), queryBean.getPageIndex());// 当前页, 默认为1
    	inParamIdxAndValue.put(Integer.valueOf(15), queryBean.getQrymethod());// 排序方式 1:芒果网推荐 2:价格
    	inParamIdxAndValue.put(Integer.valueOf(16), queryBean.getPriceOrder());// 房型排序方式
    	inParamIdxAndValue.put(Integer.valueOf(17), queryBean.getFromChannel());// 渠道来源
    	inParamIdxAndValue.put(Integer.valueOf(18), queryBean.getHotelType());
    	inParamIdxAndValue.put(Integer.valueOf(19), queryBean.getHotelIdLst());
    	inParamIdxAndValue.put(Integer.valueOf(20), queryBean.getQueryType());
    	inParamIdxAndValue.put(Integer.valueOf(21), queryBean.getIgnorePageCount());
    	inParamIdxAndValue.put(Integer.valueOf(22), queryBean.getIgnoreQueryList());// 1:忽略;0:不忽略,默认是0
		return inParamIdxAndValue;
	}
    
    /**
     * 获取存储过程返回ResultSet
     * 
     * @param rs
     * @param queryBean
     * @return
     */
    private List<QueryHotelForWebResult> getResultListFromResultSet4HotelQuery(
			ResultSet rs, QueryHotelForWebBean queryBean) {
		List<QueryHotelForWebResult> list = new ArrayList<QueryHotelForWebResult>();
        QueryHotelForWebResult info = null;
        try {
            String tempStr = "";
            while (rs.next()) {
                info = new QueryHotelForWebResult();
                info.setHotelId(rs.getLong("HotelId"));
                info.setHotelChnName(rs.getString("HotelChnName"));
                info.setHotelEngName(rs.getString("HotelEngName"));
                info.setHotelStar(rs.getString("HotelStar"));

                // ADD BY WUYUN 香港中科酒店标志、直连酒店标志 、中科酒店房型标志2009-03-19
                info.setFlagCtsHK(rs.getBoolean("flagCtsHK"));
                info.setCooperateChannel(rs.getString("cooperateChannel"));
                info.setRoomChannel(rs.getInt("roomChannel"));
                // 因为有可能该值为空，需要捕获异常
                int roomNumCts = 0;
                try {
                    roomNumCts = rs.getInt("roomNumCts");
                } catch (Exception e) {
                    roomNumCts = 1;
                }
                info.setRoomNumCts(0 == roomNumCts ? 1 : roomNumCts);
                info.setAutoIntroduce(rs.getString("HotelIntroduce"));
                info.setCommendType(rs.getString("Hotel_comm_type"));
                info.setBizZone(rs.getString("BIZZONE"));
                info.setDistrict(rs.getString("district"));
                info.setCity(rs.getString("City"));
                // --价格类型
                info.setChildRoomTypeId(rs.getString("ChildRoomTypeId"));
                info.setRoom_type_id(rs.getLong("Room_type_id"));
                info.setRoomName(rs.getString("Room_name"));
                info.setSalesPrice(rs.getDouble("SalesPrice"));
                info.setPriceType(rs.getString("Price_type"));
                info.setClose_flag(rs.getString("Close_flag"));
                info.setRoom_qty(rs.getInt("Room_qty"));                
                info.setReason(rs.getString("Reason"));

                // --配额类型及支付方式
                info.setQuotaType(rs.getString("Quota_Type"));
                info.setPayMethod(rs.getString("Pay_Method"));

                // --房间配额 价格
                info.setAvailQty(rs.getInt("Avail_qty"));
                info.setAble_sale_date(rs.getString("Able_sale_date"));
                info.setCurrency(rs.getString("Currency"));
                info.setBreakfastType(rs.getInt("Inc_breakfast_type"));
                info.setBreakfastNum(rs.getInt("Inc_breakfast_number"));
                info.setRoom_state(rs.getString("Room_state"));
                info.setSalesRoomPrice(rs.getDouble("Salesroom_price"));
                info.setOutPictureName(rs.getString("OutPictureName"));
                info.setHallPictureName(rs.getString("HallPictureName"));
                info.setRoomPictureName(rs.getString("RoomPictureName"));
                info.setSandNum(rs.getInt("NumSand"));
                info.setHotelLogo(rs.getString("hotelLogo"));

                // 交行全卡商旅等渠道查询,无需这些优惠 modify by chenkeming
                if(!queryBean.isForCooperate()) {
                    // 取芒果网优惠信息
                    info.setHasPreSale(rs.getInt("hasPreSale"));
                    if (1 == info.getHasPreSale()) {
                        info.setPreSaleName(rs.getString("preSaleName"));
                        tempStr = rs.getString("preSaleContent");
                        info.setPreSaleContent(tempStr);
                        /*
                         * if(StringUtil.isValidStr(tempStr)) {
                         * info.setPreSaleContent(tempStr.replaceAll("\n", "<br>")); }
                         */
                        info.setPreSaleBeginEnd(rs.getString("preSaleBeginEnd"));
                        info.setPreSaleURL(rs.getString("preSaleURL"));
                    }	
                } else {
                	info.setHasPreSale(0);
                }

                // 主要设施图标
                info.setForPlane(rs.getInt("FORPLANE"));
                if (1 == rs.getInt("FREEFORPLANE")) {
                    info.setFreeForPlane(true);
                } else {
                    info.setFreeForPlane(false);
                }
                info.setForFreeStop(rs.getInt("FORFREESTOP"));
                info.setForFreePool(rs.getInt("FORFREEPOOL"));
                info.setForFreeGym(rs.getInt("FORFREEGYM"));
                info.setForNetBand(rs.getInt("FORNETBAND"));

                // 开业/装修年份
                String tempYear = rs.getString("praciceYear");
                if (StringUtil.isValidStr(tempYear)) {
                    int tempIndex = tempYear.indexOf('-');
                    if (-1 != tempIndex) {
                        info.setPraciceYear(Integer.parseInt(tempYear.substring(0, tempIndex)));
                    } else {
                        tempIndex = tempYear.indexOf('.');
                        if (-1 != tempIndex) {
                            info.setPraciceYear(Integer.parseInt(tempYear.substring(0, tempIndex)));
                        } else {
                            info.setPraciceYear(Integer.parseInt(tempYear));
                        }
                    }
                }
                info.setFitmentYear(rs.getString("fitmentYear"));

                // 交通信息
                info.setTrafficInfo(rs.getString("trafficInfo"));

                // 获取房型面积和所在楼层信息
                info.setAcreage(rs.getString("acreage"));
                info.setRoomFloor(rs.getString("roomFloor"));

                // 房型宽带信息
                info.setRoomNet(rs.getString("roomNet"));

                // 子房型能否预订信息
                info.setCanBook(rs.getInt("canBook"));
                if (0 == info.getCanBook()) {
                    info.setCantBookReason(rs.getString("cantBookReason"));
                }

                // 取子房型优惠信息
                info.setHasPromo(rs.getInt("hasPromo"));
                if (1 == info.getHasPromo()) {
                    tempStr = rs.getString("promoContent");
                    if (StringUtil.isValidStr(tempStr)) {
                        info.setPromoContent(tempStr.replaceAll("\n", "<br>"));
                    }
                    info.setPromoBeginEnd(rs.getString("promoBeginEnd"));
                }
                // 综合评分
                info.setGeneralPoint(rs.getDouble("generalPoint"));

                int nNeedAssure = rs.getInt("NEED_ASSURE");
                info.setNeedAssure(1 == nNeedAssure ? true : false);
                info.setPayToPrepay(rs.getInt("PAY_TO_PREPAY"));
                if (null != rs.getDate("LATEST_BOOKABLE_DATE")) {
                    info.setLatestBookableDate(new java.util.Date(rs.getDate("LATEST_BOOKABLE_DATE").getTime()));
                }
                info.setLatestBokableTime(rs.getString("LATEST_BOKABLE_TIME"));
                
                if (null != rs.getDate("FIRST_BOOKABLE_DATE")) {
                    info.setFirstBookableDate(new java.util.Date(rs.getDate("FIRST_BOOKABLE_DATE").getTime()));
                }
                info.setFirstBookableTime(rs.getString("FIRST_BOOKABLE_TIME"));                
                
                if (null != rs.getDate("MUST_LAST_DATE")) {
                    info.setMustLastDate(new java.util.Date(rs.getDate("MUST_LAST_DATE").getTime()));
                }
                if (null != rs.getDate("MUST_FIRST_DATE")) {
                    info.setMustFirstDate(new java.util.Date(rs.getDate("MUST_FIRST_DATE").getTime()));
                }
                info.setContinueDay(rs.getLong("CONTINUE_DAY"));
                info.setMustInDate(rs.getString("MUST_IN_DATE"));
                // addby chenjuesu 增加必住日期关系
                info.setMustInDatesRelation(rs.getString("CONTINUE_DATES_RELATION"));
                info.setMaxPersons(rs.getInt("maxPersons"));
                info.setAddBedQty(rs.getInt("addBedQty"));
                info.setRoomEquipment(rs.getString("roomEquipment"));
                info.setRoomOtherEquipment(rs.getString("roomOtherEquipment"));
                info.setMinRestrictNights(rs.getLong("MIN_RESTRICT_NIGHTS"));
                info.setMaxRestrictNights(rs.getLong("MAX_RESTRICT_NIGHTS"));
                info.setGisid(rs.getLong("gisid"));
                
                // 交行全卡商旅等渠道查询,无需这些优惠 modify by chenkeming
                if(!queryBean.isForCooperate()) {
                    // 优惠立减标志 1:有,0:无 add by chenjiajie 2009-11-13
                    info.setHasBenefit(rs.getInt("HASBENEFIT"));                	
                } else {
                	info.setHasBenefit(0);
                }
                
                //用户评分  网站改版
                if(rs.getString("rate")==null){
                    info.setRate("0.0");
                }else{
                     info.setRate(rs.getString("rate"));
                }
                //佣金计算公式
                info.setFormulaId(rs.getString("formulaid"));
                info.setCommission(rs.getDouble("COMMISSION"));
                info.setCommissionrate(rs.getDouble("COMMISSION_RATE"));
                
                // 交行全卡商旅等渠道查询,无需这些优惠 modify by chenkeming
                if(!queryBean.isForCooperate()) {
                	info.setHasCashReturn(rs.getInt("hasCashReturn"));	
                } else {
                	info.setHasCashReturn(0);
                }
                
                list.add(info);
            }
        } catch (SQLException sqle) {
            throw new DAException(DataAccessError.FETCH_RESULTSET_FAILED, sqle);
        }
        return list;
    }
    
    /**
     * 查询主题酒店
     * @param queryBean
     * @return Map 包括记录数和返回数据
     */
    @SuppressWarnings(value={"unchecked", "deprecation"})
    @Transactional(readOnly=true,propagation=Propagation.REQUIRED)
    public Map queryThemeHotelsForWeb(QueryHotelForWebBean queryBean){
    	Map inParamIdxAndValue = getInParamIdxAndValueMap4ThemeQuery(queryBean);        
    	Map<Integer, Integer> outParamIdxAndType = getOutParamIdxAndValueMap4ThemeQuery();    	
        Map<Integer, ?> resultMap = super.execProcedure(PROCEDURE_QUERY_THEME, inParamIdxAndValue, outParamIdxAndType);
        
        ResultSet rs = (ResultSet) resultMap.get(Integer.valueOf(11));
        Map returnMap = new HashMap(2);
        if(rs == null) {
        	returnMap.put(KEY_RESULT_LIST, Collections.EMPTY_LIST);
            returnMap.put(KEY_TOTAL_SIZE, Integer.valueOf(0));
            
            return returnMap;
        }
        
        List<QueryHotelForWebResult> results = null;
        Integer totalSize = null; // 查询结果后返回总记录数
        try {
        	totalSize = Integer.valueOf(((BigDecimal) resultMap.get(Integer.valueOf(9))).toString());
            results = getResultListFromResultSet4ThemeQuery(rs);
        } catch (DAException e) {
            log.error("Query theme hotel error! the cause is:" + e, e);
        } 
        returnMap.put(KEY_RESULT_LIST, results);
        returnMap.put(KEY_TOTAL_SIZE, totalSize);
        
        return resultMap;
    }
    
    private Map<Integer, Integer> getOutParamIdxAndValueMap4ThemeQuery() {
		//输出参数的索引和数据类型键值对
    	Map<Integer, Integer> outParamIdxAndType = new HashMap<Integer, Integer>(4);
    	outParamIdxAndType.put(Integer.valueOf(8), Integer.valueOf(java.sql.Types.NUMERIC));
    	outParamIdxAndType.put(Integer.valueOf(9), Integer.valueOf(java.sql.Types.NUMERIC));
    	outParamIdxAndType.put(Integer.valueOf(10), Integer.valueOf(java.sql.Types.VARCHAR));
    	outParamIdxAndType.put(Integer.valueOf(11), Integer.valueOf(OracleTypes.CURSOR));
		return outParamIdxAndType;
	}
    
    @SuppressWarnings("unchecked")
	private Map getInParamIdxAndValueMap4ThemeQuery(QueryHotelForWebBean queryBean) {
		//输入参数的索引和参数值键值对
    	Map inParamIdxAndValue = new HashMap(7);
    	inParamIdxAndValue.put(Integer.valueOf(1), queryBean.getCityId());
    	inParamIdxAndValue.put(Integer.valueOf(2), DateUtil.dateToString(queryBean.getInDate()));
    	inParamIdxAndValue.put(Integer.valueOf(3), DateUtil.dateToString(queryBean.getOutDate()));
    	inParamIdxAndValue.put(Integer.valueOf(4), queryBean.getTheme());// 主题
    	inParamIdxAndValue.put(Integer.valueOf(5), queryBean.getPageSize());    	
    	inParamIdxAndValue.put(Integer.valueOf(6), queryBean.getPageIndex());
    	inParamIdxAndValue.put(Integer.valueOf(7), queryBean.getQrymethod());// 排序方式 1:芒果网推荐 2:价格
 
		return inParamIdxAndValue;
	}
    
    /**
     * 获取主题酒店查询存储过程返回ResultSet
     * 
     * @param rs
     * @return
     */
    private List<QueryHotelForWebResult> getResultListFromResultSet4ThemeQuery(ResultSet rs) {
        List<QueryHotelForWebResult> themeHotelList = new ArrayList<QueryHotelForWebResult>();
        QueryHotelForWebResult info = null;
        try {
            String tempStr = "";
            while (rs.next()) {
                info = new QueryHotelForWebResult();
                info.setHotelId(rs.getLong("HotelId"));
                info.setHotelChnName(rs.getString("HotelChnName"));
                info.setHotelEngName(rs.getString("HotelEngName"));
                info.setHotelStar(rs.getString("HotelStar"));

                // 取自动生成简介
                info.setAutoIntroduce(rs.getString("HotelIntroduce"));
                info.setCommendType(rs.getString("Hotel_comm_type"));
                info.setBizZone(rs.getString("BIZZONE"));
                info.setDistrict(rs.getString("district"));
                info.setCity(rs.getString("City"));
                info.setSalesPrice(rs.getDouble("SalesPrice"));
                info.setCurrency(rs.getString("Currency"));

                info.setOutPictureName(rs.getString("OutPictureName"));
                info.setHallPictureName(rs.getString("HallPictureName"));
                info.setRoomPictureName(rs.getString("RoomPictureName"));
                info.setSandNum(rs.getInt("NumSand"));
                info.setHotelLogo(rs.getString("hotelLogo"));

                // 取芒果网优惠信息
                info.setHasPreSale(rs.getInt("hasPreSale"));
                if (1 == info.getHasPreSale()) {
                    info.setPreSaleName(rs.getString("preSaleName"));
                    tempStr = rs.getString("preSaleContent");
                    info.setPreSaleContent(tempStr);
                    info.setPreSaleBeginEnd(rs.getString("preSaleBeginEnd"));
                    info.setPreSaleURL(rs.getString("preSaleURL"));
                }

                // 主要设施图标
                info.setForPlane(rs.getInt("FORPLANE"));
                if (1 == rs.getInt("freeForPlane")) {
                    info.setFreeForPlane(true);
                } else {
                    info.setFreeForPlane(false);
                }
                info.setForFreeStop(rs.getInt("FORFREESTOP"));
                info.setForFreePool(rs.getInt("FORFREEPOOL"));
                info.setForFreeGym(rs.getInt("FORFREEGYM"));
                info.setForNetBand(rs.getInt("FORNETBAND"));

                // 开业/装修年份
                String tempYear = rs.getString("praciceYear");
                if (StringUtil.isValidStr(tempYear)) {
                    int tempIndex = tempYear.indexOf('-');
                    if (-1 != tempIndex) {
                        info.setPraciceYear(Integer.parseInt(tempYear.substring(0, tempIndex)));
                    } else {
                        tempIndex = tempYear.indexOf('.');
                        if (-1 != tempIndex) {
                            info.setPraciceYear(Integer.parseInt(tempYear.substring(0, tempIndex)));
                        } else {
                            info.setPraciceYear(Integer.parseInt(tempYear));
                        }
                    }
                }
                info.setFitmentYear(rs.getString("fitmentYear"));                
                info.setTrafficInfo(rs.getString("trafficInfo"));// 交通信息                
                info.setGeneralPoint(rs.getDouble("generalPoint"));// 综合评分

                themeHotelList.add(info);
            }
        } catch (SQLException sqle) {
        	throw new DAException(DataAccessError.FETCH_RESULTSET_FAILED, sqle);
        }
        
        return themeHotelList;
    }
    
    public void saveChannelLog(HtlChannelClickLog channelClickLog){
    	super.save(channelClickLog);
    }
    
    public void updateChannelLog(long channelLogId){
    	HtlChannelClickLog clickLog = super.get(HtlChannelClickLog.class, channelLogId);
		clickLog.setClick("1");
		super.update(clickLog);
    }

}

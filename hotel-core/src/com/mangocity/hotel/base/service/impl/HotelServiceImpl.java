/**
 * 
 */
package com.mangocity.hotel.base.service.impl;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleTypes;

import com.mangocity.hotel.base.constant.ClaueType;
import com.mangocity.hotel.base.dao.HotelQueryDao;
import com.mangocity.hotel.base.dao.IQueryAnyDao;
import com.mangocity.hotel.base.dao.RoomControlDao;
import com.mangocity.hotel.base.manage.ClauseManage;
import com.mangocity.hotel.base.manage.ContractManage;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.manage.HotelManageGroup;
import com.mangocity.hotel.base.manage.HtlLimitFavourableManage;
import com.mangocity.hotel.base.manage.assistant.HotelAddressInfo;
import com.mangocity.hotel.base.persistence.HtlAlerttypeInfo;
import com.mangocity.hotel.base.persistence.HtlAssure;
import com.mangocity.hotel.base.persistence.HtlAssureItemEveryday;
import com.mangocity.hotel.base.persistence.HtlBookCaulClause;
import com.mangocity.hotel.base.persistence.HtlBookSetup;
import com.mangocity.hotel.base.persistence.HtlCreditAssure;
import com.mangocity.hotel.base.persistence.HtlEveningsRent;
import com.mangocity.hotel.base.persistence.HtlExhibit;
import com.mangocity.hotel.base.persistence.HtlFavouraParameter;
import com.mangocity.hotel.base.persistence.HtlFavourableDecrease;
import com.mangocity.hotel.base.persistence.HtlFavourableReturn;
import com.mangocity.hotel.base.persistence.HtlFavourableclause;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlModifRecord;
import com.mangocity.hotel.base.persistence.HtlPreconcertItem;
import com.mangocity.hotel.base.persistence.HtlPrepayEveryday;
import com.mangocity.hotel.base.persistence.HtlPrepayItemEveryday;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.persistence.HtlPriceType;
import com.mangocity.hotel.base.persistence.HtlReservCont;
import com.mangocity.hotel.base.persistence.HtlReservation;
import com.mangocity.hotel.base.resource.ResourceManager;
import com.mangocity.hotel.base.service.IBenefitService;
import com.mangocity.hotel.base.service.IHotelFavourableReturnService;
import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.hotel.base.service.SupplierInfoService;
import com.mangocity.hotel.base.service.assistant.BookRoomCondition;
import com.mangocity.hotel.base.service.assistant.HotelInfo;
import com.mangocity.hotel.base.service.assistant.HotelPriceSearchParam;
import com.mangocity.hotel.base.service.assistant.HotelQueryCondition;
import com.mangocity.hotel.base.service.assistant.HotelResultInfo;
import com.mangocity.hotel.base.service.assistant.QueryCreditAssureForCCBean;
import com.mangocity.hotel.base.service.assistant.RoomInfo;
import com.mangocity.hotel.base.service.assistant.RoomType;
import com.mangocity.hotel.base.service.assistant.SaleItem;
import com.mangocity.hotel.base.service.assistant.URLClient;
import com.mangocity.hotel.base.service.assistant.UserComment;
import com.mangocity.hotel.base.util.HotelStrUtil;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.bean.DateComponent;
import com.mangocity.util.bean.MustDate;
import com.mangocity.util.collections.FormatMap;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.dao.DAOIbatisImpl;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.util.log.MyLog;

/**
 * @author
 * 
 */
public class HotelServiceImpl extends DAOHibernateImpl implements IHotelService {
	private static final MyLog log = MyLog.getLogger(HotelServiceImpl.class);
    private IQueryAnyDao queryAnyDao;

    private HotelManage hotelManage;

    private HotelManageGroup hotelManageGroup;

    private DAOIbatisImpl queryDao;

    private RoomControlDao roomControlDao;

    private ResourceManager resourceManager;

    private ContractManage contractManage;

    private URLClient urlClient;

    /**
     * 预订条款相关管理接口 hotel 2.9.2 add by chenjiajie 2009-08-16
     */
    private ClauseManage clauseManage;
    
    /**
     * 优惠立减服务接口 add by chenjiajie 2009-10-15
     */
    private IBenefitService benefitService;
    
    /**
     * 现金返还
     */
    private IHotelFavourableReturnService returnService;
    
    /**
     * 331限量返现
     */
    private HtlLimitFavourableManage limitFavourableManage;
    
    /**
     * 酒店查询Dao接口 
     */
    private HotelQueryDao hotelQueryDao;
    
    /**
     * 记录本次查询结果所有的酒店id
     */
    private static final String KEY_ALL_HOTELLIST = "allHotelList";
    
    /**
     * 记录本次查询结果所有的价格类型id
     */
    private static final String KEY_ALL_PRICETYPELIST = "allPriceTypeList";
    
    /**
     * 记录本次查询结果有优惠立减的价格类型id
     */
    private static final String KEY_BENEFIT_PRICETYPELIST = "benefitPriceTypeList";
    
    /**
     * 记录本次查询结果有现金返还的价格类型id
     */
    private static final String KEY_CASHRETURN_PRICETYPELIST = "cashReturnPriceTypeList";
    
    /**
     * 多供应商service
     */
    private SupplierInfoService supplierInfoService;

    /**
     * 查询酒店的修改实现
     * @throws SQLException 
     */
    public List<HotelInfo> queryHotels(HotelQueryCondition condition) throws SQLException {

        int difdays = DateUtil.getDay(condition.getBeginDate(), condition.getEndDate());

        List<HotelInfo> resultList = new ArrayList<HotelInfo>();
        List<HotelResultInfo> list = null;
        boolean falg = true;
        Integer pageCount = new Integer(0);
        CallableStatement cstmt = null;
        ResultSet rs = null;
        Map<String, String> rateExchangeMap = CurrencyBean.rateMap;
        //TODO:delete debug
        long pkgBeginTime = System.currentTimeMillis();
        
        /* CC查询数据库，并把数据库返回的游标缓存到List中 begin add by chenjiajie 2010-07-05 */
        try {
            Map resultMap = hotelQueryDao.queryHotelResultListForCC(condition);
            pageCount = (Integer) resultMap.get(HotelQueryDao.KEY_TOTAL_SIZE);
            list = (List<HotelResultInfo>) resultMap.get(HotelQueryDao.KEY_RESULT_LIST);
        } catch (SQLException e) {
            log.error("Query Hotel exception,cause:" + e.getMessage(), e);
            throw e;
        }
        /* CC查询数据库，并把数据库返回的游标缓存到List中 end add by chenjiajie 2010-07-05 */
        
        //TODO:delete debug 
        long pkgEndTime = System.currentTimeMillis();
        log.info("debug 001:PKG use time:" + (pkgEndTime - pkgBeginTime) + "ms");
        if (null != list) {
            // 设置总页数
            condition.setTotalPage(pageCount);
            List<HotelResultInfo> tempResultList = null;
            String rateStr = "no";
            String CurrencySave = "";
            long hotelId = 0;
            long roomTypeId = 0;
            String childRoomTypeId = "";
            long tempHotelId = 0;
            if (0 != list.size() && null != list.get(0)) {
                tempHotelId = list.get(0).getHotelId();
            }
            boolean readFlag = false;

            // 每天房态和房间数量数据
            String[] roomStates = new String[difdays];
            int[] qtys = new int[difdays];
            
            /* 循环嵌套查询效率优化，把需要查询的内容一次查出 begin add by chenjiajie 2010-07-08 */
            Map paramsMap = getAllParamsForDB(list);
            String allPriceIds = (String) paramsMap.get(KEY_ALL_PRICETYPELIST);
            //缓存参数传入的所有价格类型连住优惠信息 add by chenjiajie 2010-05-06 
            Map<String,List<HtlFavourableclause>> favourableMap = hotelQueryDao.queryAllFavourableList(allPriceIds);
            String benefitPriceIds = (String) paramsMap.get(KEY_BENEFIT_PRICETYPELIST);
            //缓存参数传入的所有价格类型立减优惠信息 add by chenjiajie 2010-05-06 
            Map<String,List<HtlFavourableDecrease>> decreaseMap = Collections.emptyMap();
            //如果没有立减优惠的价格类型，则不查询数据库缓存到map中  add by chenjiajie 2010-05-06 
            if(StringUtil.isValidStr(benefitPriceIds)){
                decreaseMap = benefitService.queryBatchBenefitByDate(benefitPriceIds, condition.getBeginDate(), condition.getEndDate());
            }
            /* 循环嵌套查询效率优化，把需要查询的内容一次查出 end add by chenjiajie 2010-07-08 */
            
            //现金返还
            String cashReturnPriceIds = (String) paramsMap.get(KEY_CASHRETURN_PRICETYPELIST);
            Map<String,List<HtlFavourableReturn>> cashReturnMap = Collections.emptyMap(); 
            if(StringUtil.isValidStr(cashReturnPriceIds)){
            	cashReturnMap = returnService.queryFavourableReturnForPriceTypeIds(cashReturnPriceIds, condition.getBeginDate(), condition.getEndDate());
            }

            for (HotelResultInfo info : list) {
                if (info.getHotelId() == tempHotelId && readFlag) // 跳过同一酒店的记录
                {
                    tempHotelId = info.getHotelId();
                    continue;
                }
                readFlag = true;
                tempHotelId = info.getHotelId();
                if (null != tempResultList) // 清除前一个酒店数据
                {
                    tempResultList.clear();
                    tempResultList = null;
                }
                tempResultList = pickupUniqueHotel(info.getHotelId(), list); // 得到当前酒店ID对应的酒店数据
                // HtlBookCaulClause htlBookCaulClause =
                // quetyBookClause(info.getHotelId());//得到当前酒店ID对应的担保金额计算规则的实体

                /** 按酒店id和时间段查询计算规则 hotel2.9.2 add by chenjiajie 2009-08-16 begin **/
                List<HtlBookCaulClause> htlBookCaulClauseList = clauseManage
                    .searchBookCaulByDateRange(info.getHotelId(), condition.getBeginDate(),
                        condition.getEndDate());
                // 取出计算规则中最严格的计算规则，如果没有计算规则，默认累加判定
                String clauseRule = clauseManage.drawoutHtlBookCaulClause(htlBookCaulClauseList);
                /** 按酒店id和时间段查询计算规则 hotel2.9.2 add by chenjiajie 2009-08-16 end **/

                hotelId = info.getHotelId();
                roomTypeId = info.getRoom_type_id();
                List hotelInfoLis = getHotelInfoList(hotelId, roomTypeId, tempResultList);
                HotelInfo hotelInfo = new HotelInfo();
                hotelInfo.setFlag400(info.isFlag400());
                hotelInfo.setFlagCtsHK(info.isFlagCtsHK());
                hotelInfo.setFlagStopSell(info.isFlagStopSell());
                hotelInfo.setStopSellReason(info.getStopSellReason());
                hotelInfo.setStopSellNote(info.getStopSellNote());
                hotelInfo.setCommendType(info.getHotel_comm_type());
                String lowsePrice = String.valueOf(info.getLowestPrice());
                hotelInfo.setLowestPrice(lowsePrice);
                List<RoomType> roomTypeLis = new ArrayList<RoomType>();
                int roomTypesNum = hotelInfoLis.size();
                hotelInfo.setRoomTypesSize(roomTypesNum);
                for (int k = 0; k < roomTypesNum; k++) {
                    RoomType roomType = (RoomType) hotelInfoLis.get(k);
                    // 根据酒店ID，价格类型ID（子房型ID），时间，查询出SaleItem集合
                    List<SaleItem> saleItems = new ArrayList<SaleItem>();
                    roomTypeId = Long.parseLong(roomType.getRoomTypeId());
                    childRoomTypeId = roomType.getChildRoomTypeId();
                                        
                    /** 计算价格类型的优惠立减金额 V2.9.3.1 add by chenjiajie 2009-10-15 begin **/
                    //当该酒店存在优惠立减标志的时候再进行计算
                    if(info.getHasBenefit() > 0 && !condition.isForTmc()){ // tmc会员不支持立减优惠 modify by chenkeming@2009-11-03
                    	//计算某价格类型在入住时间段的优惠总金额 
                    	int benefitAmount = benefitService.calculateBenefitAmount(childRoomTypeId, 
												                    			condition.getBeginDate(), 
												                    			condition.getEndDate(),
												                    			Integer.parseInt(condition.getHotelCount()),
												                    			info.getCurrency(),
												                    			decreaseMap);
                    	roomType.setBenefitAmount(benefitAmount);
                    }
                    //没有优惠立减标志，默认优惠0元
                    else{
                    	roomType.setBenefitAmount(0);
                    }
                    /** 计算价格类型的优惠立减金额 V2.9.3.1 add by chenjiajie 2009-10-15 end **/
                    
                    List saleItemLis = getSaleItemList(hotelId, roomTypeId, childRoomTypeId,
                        tempResultList);
                    boolean bSetUd = false;
                    for (int z = 0; z < saleItemLis.size(); z++) {
                        SaleItem saleItem = (SaleItem) saleItemLis.get(z);
                        // 查询RoomInfo
                        String quotaType = saleItem.getQuotaType();
                        String payMethod = saleItem.getPayMethod();
                        List<RoomInfo> roomInfo = new ArrayList<RoomInfo>();
                        List roomInfoLis = getRoomInfoList(hotelId, childRoomTypeId, quotaType,
                            payMethod, tempResultList);

                        // add by zhineng.zhuang 2009-02-10 酒店2.6 RMS2388 预订按钮显示
                        String bookButton = new String();

                        // add by shengwei.zuo 增加面转预标志 2009-04-01
                        int payTorepayShow = saleItem.getPayToPrepay();

                        // 预订不符合的信息提示 add by zhineng.zhuang 2009-02-16
                        StringBuffer bookHintNoMeet = new StringBuffer();
                        // 最小配额数
                        int quotaLeastNum = 0;
                        boolean bHasReserv = false;

                        boolean isMustPayToRepay = false;

                        if (!roomInfoLis.isEmpty() && 0 < roomInfoLis.size()) {
                        	//用于标示天数是否足够连住N晚送M晚
                            int f = 0;
                            for (int x = 0; x < difdays; x++) {
                                Date dt = DateUtil.getDate(condition.getBeginDate(), x);
                                int m = 0;
                                
                                for (int y =0; y <  roomInfoLis.size(); y++) {
                                	RoomInfo roomInfos = (RoomInfo) roomInfoLis.get(y);
                                	
                                	//连住优惠对价格进行修改
                                	if(null !=roomInfos && dt.equals(roomInfos.getFellowDate())
                                			&& !condition.isForTmc()){ // tmc会员不支持连住优惠 modify by chenkeming@2009-11-03
                                		f = changePriceForFavourable(roomInfoLis,roomInfos,hotelId,childRoomTypeId,y,f,favourableMap);
                                	}
                                    if (null != roomInfos) {

                                        // v2.6 add by chenkeming@2009-03-05 是否有预订条款 begin
                                        if (roomInfos.isHasReserv()) {
                                            bHasReserv = true;
                                        }
                                        // v2.6 add by chenkeming@2009-03-05 是否有预订条款 end

                                        // add by shengwei.zuo 2009-04-07 begin
                                        // 酒店查询页面的预订按钮显示逻辑；只要其中一种付款方式为“必须面转预”,按钮应该显示"预付",不管是否是担保单
                                        if (payMethod.equals("pay")) {
                                            if (0 == y) {
                                                bookButton = "pay";
                                            }
                                            if (1 == roomInfos.getPayToPrepay()) {
                                                bookButton = "pre_pay";
                                                isMustPayToRepay = true;
                                            } else if (roomInfos.isNeedAssure()) {
                                                bookButton = "vouch";
                                            }
                                        }
                                        // add by shengwei.zuo 2009-04-07 end
                                        // 酒店查询页面的预订按钮显示逻辑；只要其中一种付款方式为“必须面转预”,按钮应该显示"预付",不管是否是担保单

                                        // 预订不符合条件提示 2.6 add by zhineng.zhuang 2009-02-16 begin
                                        // 系统日期应该在最晚预订时间的前面，如果系统日期不比其小，提示
                                        bookHintNoMeet = new StringBuffer();
                                        String latestTime = roomInfos.getLatestBokableTime();
                                        String[] sTime = null;
                                        if (null != latestTime && !latestTime.equals("")) {
                                            if (StringUtil.isValidStr(latestTime)
                                                && 0 <= latestTime.indexOf(":")) {
                                                sTime = latestTime.split(":");
                                            }
                                        } else {// 如果预订时间点为空，则赋初始值
                                            latestTime = "23:59";
                                            sTime = latestTime.split(":");
                                        }

                                        if (null != roomInfos.getLatestBookableDate()) {
                                        	
                                        	//如果最早预订时间不为空
                                            if(null!=roomInfos.getFirstBookableDate()){
                                            	
                                                //add by shengwei.zuo hotel2.9.3  最早预订时间 2009-09-06
                                                String firstTime = roomInfos.getFirstBookableTime();
                                                String[] fTime = null;
                                                if (null != firstTime && !firstTime.equals("")) {
                                                    if (StringUtil.isValidStr(firstTime)
                                                        && 0 <= firstTime.indexOf(":")) {
                                                    	fTime = firstTime.split(":");
                                                    }
                                                } else {// 如果预订时间点为空，则赋初始值
                                                	firstTime = "23:59";
                                                    fTime = firstTime.split(":");
                                                }
                                            	
                                                //如果当前系统时间不在预订的时间段内，都不能进行预订 add by shengwei.zuo 2009-09-06 hotel 2.9.3
                                            	if(!DateUtil.isBetweenDateTime(roomInfos.getFirstBookableDate(),(fTime[0]+fTime[1]),
                                            			roomInfos.getLatestBookableDate(),(sTime[0] + sTime[1]))){
                                            			
    	                                        		bookHintNoMeet.append("预订此房型，必须在");
    		                                            bookHintNoMeet
    		                                                    .append(DateUtil.dateToString(roomInfos
    		                                                        .getFirstBookableDate()));
    		                                            bookHintNoMeet.append(" ");
    		                                            bookHintNoMeet.append(null == roomInfos
    		                                                    .getFirstBookableTime() ? "" : roomInfos
    		                                                    .getFirstBookableTime());
    		                                            bookHintNoMeet.append(" 至 ");
    		                                            bookHintNoMeet
                                                        .append(DateUtil.dateToString(roomInfos
                                                            .getLatestBookableDate()));
    		                                            bookHintNoMeet.append(" ");
    		                                            bookHintNoMeet.append(null == roomInfos
    		                                                    .getLatestBokableTime() ? "" : roomInfos
    		                                                    .getLatestBokableTime());
    		                                            bookHintNoMeet.append("之间预订。");
    		                                          
                                            	}
                                            	
                                            }else{
                                        	
                                            		if (!DateUtil.compareDateTimeToSys(roomInfos
    	                                                .getLatestBookableDate(), (sTime[0] + sTime[1]))) {
    		                                             bookHintNoMeet.append("预订此房型，必须在");
    		                                             bookHintNoMeet
    		                                                    .append(DateUtil.dateToString(roomInfos
    		                                                        .getLatestBookableDate()));
    		                                             bookHintNoMeet.append(" ");
    		                                             bookHintNoMeet.append(null == roomInfos
    		                                                    .getLatestBokableTime() ? "" : roomInfos
    		                                                    .getLatestBokableTime());
    		                                             bookHintNoMeet.append("之前预订。");
    	                                            }
                                            }
                                       }
                                       
                                      //hotel 2.9.3 预订条款时间段的预订规则 add by shengwei.zuo 20090-09-06 end
                                        
                                        
                                        // 系统连住日期应该满足
                                        if (difdays < roomInfos.getContinueDay()) {
                                            bookHintNoMeet.append("在此期间预订此房型，必须连住");
                                            bookHintNoMeet.append(roomInfos.getContinueDay());
                                            bookHintNoMeet.append("晚。");
                                        }
                                        // 系统限住晚数应该满足
                                        /*
                                         * if(roomInfos.getMinRestrictNights()!
                                         * =0&&difdays<roomInfos. getMinRestrictNights()){
                                         * if("".equals(bookHintNoMeet.toString())){
                                         * bookHintNoMeet.append("在此期间预订此房型，"); }else{
                                         * bookHintNoMeet.append("并且"); }
                                         * bookHintNoMeet.append("入住间夜不能少于"
                                         * ).append(roomInfos.getMinRestrictNights()).append("晚。");
                                         * }
                                         */
                                        if (0 != roomInfos.getMaxRestrictNights()
                                            && roomInfos.getMaxRestrictNights() != difdays) {
                                            if ("".equals(bookHintNoMeet.toString())) {
                                                bookHintNoMeet.append("入住此房型，仅限连住 ").append(
                                                    roomInfos.getMaxRestrictNights()).append(
                                                    "晚方可接受预订");
                                            } else {
                                                bookHintNoMeet.append("并且仅限连住 ").append(
                                                    roomInfos.getMaxRestrictNights()).append(
                                                    "晚方可接受预订");
                                            }
                                        }
                                        // 必住日期 增加关系选择,并且 或者 ,如果为空,则默认为 或者 . add by juesuchen
                                        if (null != roomInfos.getMustFirstDate()
                                            && null != roomInfos.getMustLastDate()
                                            && null != condition.getBeginDate()
                                            && null != condition.getEndDate()) {
                                            // 对必住日期进行逻辑判断的方法
                                            checkMustInDate(roomInfos, bookHintNoMeet, condition);
                                        }
                                        // 预订不符合条件提示 2.6 add by zhineng.zhuang 2009-02-16 end

                                        // 设置预订提示只显示第一天的。add by shengwei.zuo 2009-05-10 bengin;
                                        if (0 == x && 0 == y) {

                                            saleItem.setBookHintNotMeet(bookHintNoMeet.toString());
                                            // 判断同一酒店同一价格类型的第一天是否有担保条款
                                            falg = getCluserBeginDate(hotelId, Long
                                                .valueOf(childRoomTypeId),
     condition.getBeginDate(), condition.getHotelCount());
                                        }
                                        // 设置预订提示只显示第一天的。add by shengwei.zuo 2009-05-10 end;

                                        if (dt.equals(roomInfos.getFellowDate())) {
                                            roomType.setRoomPrice(BigDecimal.valueOf(
                                                roomInfos.getSalesRoomPrice()).setScale(2,
                                                BigDecimal.ROUND_HALF_UP).doubleValue());
                                            // 判断币种读取汇率
                                            if (!CurrencyBean.RMB.equals(roomInfos.getCurrency())) {
                                                double ratedou = 0;
                                                String currencyCompare = roomInfos.getCurrency();
                                                if (null != currencyCompare
                                                    && !currencyCompare.equals(CurrencySave)) {
                                                    if (null != rateExchangeMap) {
                                                        rateStr = rateExchangeMap
                                                            .get(currencyCompare);
                                                    } else {
                                                        rateStr = "1";
                                                        log.error("get rateExchange "
                                                            + "ERROR:rateExchangeMap is null ");
                                                    }
                                                    if (null == rateStr || rateStr.equals("")) {
                                                        log.error("get rateExchange "
                                                            + "ERROR:NO such currency in DB");
                                                        rateStr = "1";
                                                    }
                                                }
                                                CurrencySave = currencyCompare;
                                                ratedou = Double.valueOf(rateStr.trim())
                                                    .doubleValue();
                                                double ratedoures = BigDecimal.valueOf(
                                                    roomInfos.getSalePrice()).multiply(
                                                    BigDecimal.valueOf(ratedou)).doubleValue();
                                                roomInfos.setRmbSalePrice(BigDecimal.valueOf(
                                                    ratedoures).setScale(2,
                                                    BigDecimal.ROUND_HALF_UP).doubleValue());
                                                roomInfos.setRmbCurrency(CurrencyBean.RMB);
                                            }
                                            if ("G".equals(roomInfos.getCloseFlag())) {
                                                roomType.setCloseFlag(roomInfos.getCloseFlag());
                                                roomType.setReason(roomInfos.getReason());
                                            }

                                            int tmpQty = roomInfos.getQuotaAmount();
                                            // 生产BUG：配额为负数 add by shengwei.zuo 2009-07-28
                                            if (0 > tmpQty) {
                                                tmpQty = 0;
                                                roomInfos.setQuotaAmount(0);
                                            }
                                            // 判断配额数量是否不为0，填写标志
                                            if (1 > tmpQty) {
                                                saleItem.setQuotaBool(true);
                                            }

                                            // modify by chenkeming@2008.12.11 v2.4.1 需求变更(关于呼出配额提醒)
                                            boolean bNotMore = false; // 是否为不可超
                                            // 判断床型的房态，在所有时间段内如果床型都有房态设为不可超那么设置床型房态标志
                                            String roomStatStr = roomInfos.getRoomStatus();
                                            if (StringUtil.isValidStr(roomStatStr)) {
                                                String[] roomOne = roomStatStr.split("/");
                                                for (String kk : roomOne) {
                                                    String[] roomKK = kk.split(":");
                                                    if ("1".equals(roomKK[0])
                                                        && "3".equals(roomKK[1])) {
                                                        saleItem.setBedStateOneBool(true);
                                                    } else if ("2".equals(roomKK[0])
                                                        && "3".equals(roomKK[1])) {
                                                        saleItem.setBedStateTwoBool(true);
                                                    } else if ("3".equals(roomKK[0])
                                                        && "3".equals(roomKK[1])) {
                                                        saleItem.setBedStateThrBool(true);
                                                    }
                                                    if ("3".equals(roomKK[1])) {
                                                        bNotMore = true;
                                                    }
                                                }

                                                roomStates[x] = roomInfos.getRoomStatus();
                                                qtys[x] = tmpQty;

                                                roomInfo.add(roomInfos);
                                                m++;

                                                // 呼出配额的提醒，只考虑房态为不可超的情况
                                                if (0 == x) {
                                                    if (bNotMore) {
                                                        quotaLeastNum = tmpQty;
                                                    } else {
                                                        quotaLeastNum = 9999;
                                                    }
                                                } else {
                                                    if (bNotMore) {
                                                        if (quotaLeastNum > tmpQty) {
                                                            quotaLeastNum = tmpQty;
                                                        }
                                                    }
                                                }

                                                break;
                                            }
                                        }
                                    }
                                }
                                if (0 == m) {
                                    roomStates[x] = "";
                                    roomInfo.add(new RoomInfo());
                                }
                            }

                            // 要处理所有床型均为满房和房态不可超且配额为0的情况(114和mango用户)
                            if (!bSetUd) {
                                roomType.setYud(HotelStrUtil.handleRoomStates(roomStates, qtys));
                                bSetUd = true;
                            }
                        }

                        /*
                         * if (roomInfoLis.size()<difdays){ for(int dd =0;
                         * dd<difdays-roomInfoLis.size();dd++){ RoomInfo roomInfos = new RoomInfo();
                         * roomInfo.add(roomInfos); } }
                         */

                        // v2.6 add by chenkeming@2009-03-05 是否有预订条款
                        saleItem.setHasReserv(bHasReserv);

                        // add by shengwei.zuo 2009-04-08 当有“必须面转预”时，按钮就显示“面转预”；
                        if (!("pre_pay".equals(bookButton)) && isMustPayToRepay) {

                            bookButton = "pre_pay";

                        }

                        // 只要酒店的计算担保规则为check in day而且第一天的担保为空，则页面按钮显示为 面付
                        if (null != clauseRule && clauseRule.equals("1") && !falg) {
                            bookButton = "pay";
                        }

                        // add by zhineng.zhuang 2009-02-10 酒店2.6 RMS2388 预订按钮显示
                        saleItem.setBookButton(bookButton);

                        // add by zhineng.zhuang 2009-02-16 hotel2.6
                        // saleItem.setBookHintNotMeet(bookHintNoMeet.toString());

                        // 增加面转预标志 add by shengwei.zuo 2009-04-01
                        saleItem.setPayToPrepay(payTorepayShow);

                        saleItem.setQuotaLeastNum(quotaLeastNum);
                        saleItem.setRoomItems(roomInfo);
                        saleItems.add(saleItem);
                    }
                    roomType.setSaleItems(saleItems);
                    
                    //现金账户
                    if (info.getHasCashReturn() > 0 && !condition.isForTmc()) {
    					calculateCashReturnAmount(tempResultList, roomType,cashReturnMap,condition.getBeginDate(),condition.getEndDate());	
    				}
                    
                    roomTypeLis.add(roomType);
                }
                hotelInfo = setHotelInfo(hotelInfo, info, roomTypeLis, rateStr);
                
                resultList.add(hotelInfo);
            }
        }

        //TODO:delete debug
        log.info("debug 002:Programs use time:" + (System.currentTimeMillis() - pkgEndTime) + "ms");
        return resultList;
    }
    
    /**
     * 计算房型的返现金额
     * @param result
     * @param childRoomTypeId
     * @param cashReturnMap
     * @return
     */
    private void calculateCashReturnAmount(List<HotelResultInfo> result,RoomType roomType,Map<String,List<HtlFavourableReturn>> cashReturnMap, Date checkInDate, Date checkOutDate){
    	for(SaleItem item : roomType.getSaleItems()){
            String pm = item.getPayMethod();
            if(pm.equals(PayMethod.PAY)){
            	int cashAmount = 0;
            	for(Object obj : item.getRoomItems()){
        			RoomInfo room = (RoomInfo)obj;
        			BigDecimal salePrice = new BigDecimal(room.getSalePrice());
        			if(salePrice.intValue() >= 99999){
        				continue;
        			}
        			
        			for(HotelResultInfo hotel :result){
        				BigDecimal price = returnService.calculateRoomTypePrice(hotel.getFormulaId(), new BigDecimal(hotel.getCommission()), new BigDecimal(hotel.getCommissionrate()), salePrice);
        				if(hotel.getChildRoomTypeId().equals(roomType.getChildRoomTypeId())&&hotel.getPayMethod().equals(pm)&&hotel.getAble_sale_date().equals(room.getFellowDate())){
	        				
	        					cashAmount += returnService.calculateCashReturnAmount(roomType.getChildRoomTypeId(),checkInDate,checkOutDate, room.getFellowDate(), 1, price, pm, hotel.getCurrency(), cashReturnMap);
	        				
        				}
        			}
        		}
                roomType.setPayCashReturnAmount(cashAmount);
            }
            else{
            	int cashAmount = 0;
            	for(Object obj : item.getRoomItems()){
        			RoomInfo room = (RoomInfo)obj;
        			BigDecimal salePrice = new BigDecimal(room.getSalePrice());
        			if(salePrice.intValue() >= 99999){
        				continue;
        			}
        			
        			for(HotelResultInfo hotel :result){
        				
        				BigDecimal price = returnService.calculateRoomTypePrice(hotel.getFormulaId(), new BigDecimal(hotel.getCommission()), new BigDecimal(hotel.getCommissionrate()), salePrice);
        				if(hotel.getChildRoomTypeId().equals(roomType.getChildRoomTypeId())&&hotel.getPayMethod().equals(pm)&&hotel.getAble_sale_date().equals(room.getFellowDate())){
	        				
	        					cashAmount += returnService.calculateCashReturnAmount(roomType.getChildRoomTypeId(),checkInDate,checkOutDate, room.getFellowDate(), 1, price, pm, hotel.getCurrency(), cashReturnMap);
	        			    
	        			}
        			}
        		}
            	roomType.setPrePayCashReturnAmount(cashAmount);
            }        
    	} 	
    }

    /**
     * 对必住日期进行判断的方法 add by chenjuesu
     * 
     * @param roomInfos
     * @param bookHintNoMeet
     * @param condition
     */
    private void checkMustInDate(RoomInfo roomInfos, StringBuffer bookHintNoMeet,
        HotelQueryCondition condition) {
        // TODO Auto-generated method stub
        String mustDatesRelation = roomInfos.getMustInDatesRelation();// 取得必住日期关系
        List<MustDate> mustInDates = new ArrayList<MustDate>();
        int type = MustDate.getMustIndatesAndType(mustInDates, roomInfos.getMustInDate());
        boolean isCanLive = false;
        StringBuffer noMeet = new StringBuffer();
        if (!StringUtil.isValidStr(mustDatesRelation) || mustDatesRelation.equals("or")) {// 里边为 或者
            // 逻辑判断
            // 得到必住日期集合
            if (type == MustDate.DATE_TYPE) {// 必住日期逻辑
                for (MustDate date : mustInDates) {
                    // //如果入住日期包括任意一个必住日期即可入住
                    if (DateUtil.isBetween(date.getContinueDate(), condition.getBeginDate(),
                        condition.getEndDate())) {
                        isCanLive = true;
                        break;
                    }
                    noMeet.append(DateUtil.dateToString(date.getContinueDate())).append(",");
                }
                // //如果不能入住,则显示提示信息
                if (!isCanLive) {
                    bookHintNoMeet.append("在此期间预订此房型，入住日期必须至少包含");
                    noMeet.deleteCharAt(noMeet.length() - 1);
                    bookHintNoMeet.append(noMeet.toString());
                    bookHintNoMeet.append("中任意一天");
                }
            } else if (type == MustDate.WEEK_TYPE) {// 必住星期逻辑
                for (MustDate date : mustInDates) {
                    if (DateUtil.isBetween(condition.getBeginDate(), date.getContinueDate(), date
                        .getContinueEndDate())) {
                        String[] checkInWeeks = date.getWeeks().split(",");
                        Date[] checkInDates = DateUtil.getDateWithWeek(condition.getBeginDate(),
                            condition.getEndDate(), checkInWeeks);
                        if (0 < checkInDates.length) {// 说明入住日期内已经至少包含有一个必住星期
                            isCanLive = true;
                        } else {
                            noMeet.append("从")
                                .append(DateUtil.dateToString(date.getContinueDate())).append("至")
                                .append(DateUtil.dateToString(date.getContinueEndDate())).append(
                                    "期间的星期").append(date.getWeeks());
                        }
                        break;
                    }
                }
                // 如果不能入住,则显示提示信息
                if (!isCanLive) {
                    bookHintNoMeet.append("在此期间预订此房型，入住日期必须至少包含");
                    bookHintNoMeet.append(noMeet.toString());
                    bookHintNoMeet.append("中任意一天");
                }
            }
        } else {// 里边为 并且 逻辑判断
            if (type == MustDate.DATE_TYPE) {// 必住日期逻辑
                if ((!DateUtil.isBetween(roomInfos.getMustFirstDate(), condition.getBeginDate(),
                    condition.getEndDate()))
                    || (!DateUtil.isBetween(roomInfos.getMustLastDate(), condition.getBeginDate(),
                        condition.getEndDate()))) {
                    bookHintNoMeet.append("在此期间预订此房型，入住日期必须包含");
                    for (MustDate date : mustInDates)
                        noMeet.append(DateUtil.dateToString(date.getContinueDate())).append(",");
                    noMeet.deleteCharAt(noMeet.length() - 1);
                    bookHintNoMeet.append(noMeet.toString());
                }
            } else if (type == MustDate.WEEK_TYPE) {// 必住星期逻辑
                for (MustDate date : mustInDates) {
                    if (DateUtil.isBetween(condition.getBeginDate(), date.getContinueDate(), date
                        .getContinueEndDate())) {
                        String[] checkInWeeks = date.getWeeks().split(",");
                        Date[] checkInDates = DateUtil.getDateWithWeek(condition.getBeginDate(),
                            condition.getEndDate(), checkInWeeks);
                        if (checkInDates.length >= checkInWeeks.length) {// 说明入住日期内已经至少包含有一个整体的必住星期
                            isCanLive = true;
                        } else {
                            noMeet.append("从")
                                .append(DateUtil.dateToString(date.getContinueDate())).append("至")
                                .append(DateUtil.dateToString(date.getContinueEndDate())).append(
                                    "期间的逢星期").append(date.getWeeks());
                        }
                        break;
                    }
                }
                // 如果不能入住,则显示提示信息
                if (!isCanLive) {
                    bookHintNoMeet.append("在此期间预订此房型，入住日期必须包含");
                    bookHintNoMeet.append(noMeet.toString());
                }
            }
        }
    }

    /**
     * 挑出同一酒店数据
     * 
     * @param hotelId
     * @param list
     * @return
     */
    private List<HotelResultInfo> pickupUniqueHotel(long hotelId, List<HotelResultInfo> list) {
        List<HotelResultInfo> resultList = new ArrayList<HotelResultInfo>();
        for (HotelResultInfo info : list) {
            if (info.getHotelId() == hotelId) {
                resultList.add(info);
            }
        }
        return resultList;
    }

    // /**
    // * 查询出对应酒店的担保金额计算规则表,该对象和酒店是一对一关系 add haibo.li
    // */
    // private HtlBookCaulClause quetyBookClause(long hotelId) {
    //
    // String hsql = "from HtlBookCaulClause where hotelId = ?";
    // HtlBookCaulClause htlBookCaulClause = (HtlBookCaulClause) super.find(hsql, hotelId);
    // return htlBookCaulClause;
    // }

    /**
     * 设置房型信息
     * 
     * @param hotelId
     * @param roomTypeId
     * @param hotelResultInfo
     * @return
     */
    private List<RoomType> getHotelInfoList(long hotelId, long roomTypeId,
        List<HotelResultInfo> hotelResultInfo) {
        List<RoomType> roomTypeList = new ArrayList<RoomType>();
        RoomType roomType = null;
        String tmpChildRoomTypeId = "";
        String tmpRoomTypeId = "";
        for (HotelResultInfo info : hotelResultInfo) {
            if (!tmpChildRoomTypeId.equals("")
                && tmpChildRoomTypeId.equals(info.getChildRoomTypeId())
                && tmpRoomTypeId.equals(String.valueOf(info.getRoom_type_id()))) {
                continue;
            }
            if (hotelId == info.getHotelId()) {
                roomType = new RoomType();
                roomType.setChildRoomTypeId(info.getChildRoomTypeId());
                roomType.setChildRoomTypeName(info.getPrice_type());
                roomType.setRoomTypeName(info.getRoom_name());
                roomType.setRoomTypeId(String.valueOf(info.getRoom_type_id()));
                roomType.setRecommend("" + info.getComm_level());
                
                //hotel 2.9.3 房间数 add by shengwei.zuo 2009-09-09
                roomType.setCanRoomNumber("" + info.getRoom_qty());

                // v2.8 设置房型的渠道 add by chenkeming@2009-03-20
                roomType.setRoomChannel(info.getRoomChannel());

                roomType.setHasPromo(info.getHasPromo());
                if (1 == roomType.getHasPromo()) {
                    roomType.setPromoContent(info.getPromoContent());
                    roomType.setPromoBeginEnd(info.getPromoBeginEnd());
                }

                roomTypeList.add(roomType);
                tmpChildRoomTypeId = info.getChildRoomTypeId();
                tmpRoomTypeId = roomType.getRoomTypeId();
            }
        }
        List<RoomType> roomTypes = new ArrayList<RoomType>();
        int i = 0;
        for (; i < roomTypeList.size(); i++) {
            RoomType temp = roomTypeList.get(i);
            if (0 != Integer.parseInt(temp.getRecommend())) {
                break;
            }
            roomTypes.add(temp);
        }
        if (0 != i) {
            roomTypeList.removeAll(roomTypes);
            roomTypeList.addAll(roomTypes);
        }
        return roomTypeList;
    }

    /**
     * 得到配额和支付方式
     * 
     * @param hotelId
     * @param roomTypeId
     * @param childRoomTypeId
     * @param list
     * @return
     */
    private List<SaleItem> getSaleItemList(long hotelId, long roomTypeId, String childRoomTypeId,
        List<HotelResultInfo> list) {
        List<SaleItem> saleItems = new ArrayList<SaleItem>();
        SaleItem saleItem = null;
        String tmpQuotaType = "";
        String tmpPayMethod = "";
        for (HotelResultInfo info : list) {
            if (!tmpQuotaType.equals("") && tmpQuotaType.equals(info.getQuotaType())
                && tmpPayMethod.equals(info.getPayMethod())) {
                continue;
            }
            if (hotelId == info.getHotelId() && childRoomTypeId.equals(info.getChildRoomTypeId())
                && roomTypeId == info.getRoom_type_id()) {
                saleItem = new SaleItem();
                saleItem.setQuotaType(info.getQuotaType());
                saleItem.setPayMethod(info.getPayMethod());
                // add by shengwei.zuo 增加面转预标志
                saleItem.setPayToPrepay(info.getPayToPrepay());
                saleItems.add(saleItem);
                tmpQuotaType = info.getQuotaType();
                tmpPayMethod = info.getPayMethod();
            }
        }
        return saleItems;
    }

    /**
     * 获取价格信息
     * 
     * @param hotelId
     * @param childRoomTypeId
     * @param quotaType
     * @param payMethod
     * @param list
     * @return
     */
    private List<RoomInfo> getRoomInfoList(long hotelId, String childRoomTypeId, String quotaType,
        String payMethod, List<HotelResultInfo> list) {
        List<RoomInfo> roomInfos = new ArrayList<RoomInfo>();
        RoomInfo roomInfo = null;
        for (HotelResultInfo info : list) {
            if (hotelId == info.getHotelId() && childRoomTypeId.equals(info.getChildRoomTypeId())
                && payMethod.equals(info.getPayMethod()) && quotaType.equals(info.getQuotaType())) {
                roomInfo = new RoomInfo();
                roomInfo.setSalesRoomPrice(info.getSalesroom_price());
                roomInfo.setCurrency(info.getHotel_currency());
                roomInfo.setRmbCurrency(info.getHotel_currency());
                roomInfo.setRmbSalePrice(info.getSalesPrice());
                //roomInfo.setQuotaAmount(info.getAvail_qty()/info.getRoom_qty());
                roomInfo.setQuotaAmount(info.getAvail_qty());
                info.getRoom_qty();
                //info.get
                roomInfo.setSalePrice(info.getSalesPrice());
                // base price
                roomInfo.setBasePrice(info.getBasePrice());
                roomInfo.setCloseFlag(info.getClose_flag());
                roomInfo.setReason(info.getReason());
                roomInfo.setRoomStatus(info.getRoom_state());
                roomInfo.setBreakfast(info.getInc_breakfast_type());
                roomInfo.setBreakNum(info.getInc_breakfast_number());
                roomInfo.setFellowDate(info.getAble_sale_date());

                // add by chenkeming@2009-02-06
                roomInfo.setNeedAssure(info.isNeedAssure());
                roomInfo.setPayToPrepay(info.getPayToPrepay());

                // add by zhineng.zhuang 2009-02-16
                roomInfo.setLatestBookableDate(info.getLatestBookableDate());
                roomInfo.setLatestBokableTime(info.getLatestBokableTime());
                
                /**hotel 2.9.3  
                 * 新增时间段的预订规则 add by shengwei.zuo 2009-09-06 begin  
                 */
                roomInfo.setFirstBookableDate(info.getFirstBookableDate());
                roomInfo.setFirstBookableTime(info.getFirstBookableTime());

                /**hotel 2.9.3  
                 * 新增时间段的预订规则 add by shengwei.zuo 2009-09-06 end  
                 */
                
                roomInfo.setMustLastDate(info.getMustLastDate());
                roomInfo.setMustFirstDate(info.getMustFirstDate());
                roomInfo.setContinueDay(info.getContinueDay());
                roomInfo.setMustInDate(info.getMustInDate());
                // addby chenjuesu 增加必住日期关系
                roomInfo.setMustInDatesRelation(info.getMustInDatesRelation());
                roomInfo.setHasReserv(info.isHasReserv());
                // v2.9.2 增加限住天数 2009-07-28 lixiaoyong begin
                roomInfo.setMinRestrictNights(info.getMinRestrictNights());
                roomInfo.setMaxRestrictNights(info.getMaxRestrictNights());
                // v2.9.2 增加限住天数 2009-07-28 lixiaoyong end

                roomInfos.add(roomInfo);
            }
        }
        return roomInfos;
    }
    
   
    
    /**
     * 设置返回结果信息
     * 
     * @param info
     * @param roomTypeLis
     * @param rateStr
     * @return
     */
    private HotelInfo setHotelInfo(HotelInfo hotelInfo, HotelResultInfo info,
        List<RoomType> roomTypeLis, String rateStr) {
        hotelInfo.setAcceptCustom(info.getAcceptCustom());
        hotelInfo.setRoomTypes(roomTypeLis);
        // 酒店id
        hotelInfo.setHotelId(info.getHotelId());
        // 酒店中文名称
        hotelInfo.setHotelChnName(info.getHotelChnName());
        // 酒店英文名称
        hotelInfo.setHotelEngName(info.getHotelEngName());
        // 酒店星级
        hotelInfo.setHotelStar(info.getHotelStar());
        String strHotelStar = resourceManager.getDescription("res_hotelStarToNum", info
            .getHotelStar());
        if (StringUtil.isValidStr(strHotelStar)) {
            hotelInfo.setHotelStar1(Float.valueOf(strHotelStar));
        } else {
            hotelInfo.setHotelStar1(1);
        }
        // 酒店类型
        hotelInfo.setHotelType(info.getHotelType());
        // 酒店中文地址
        // 拼装酒店地址 hotel2.9.2 add by chenjiajie 2009-08-18
        //refactor: to decouple
        HotelAddressInfo hotelAddressInfo = new HotelAddressInfo();
        hotelAddressInfo.setChnAddress(info.getChnAddress());
        hotelAddressInfo.setCity(info.getCity());
        hotelAddressInfo.setState(info.getState());
        hotelAddressInfo.setZone(info.getZone());
        hotelInfo.setChnAddress(hotelManage.joinHotelAddress(hotelAddressInfo));
        // /酒店介绍
        hotelInfo.setHotelIntroduce(info.getHotelIntroduce());
        // 酒店详细介绍
        hotelInfo.setHotelChnIntroduce(info.getHotelChnIntroduce());
        // 提示信息
        hotelInfo.setClueInfo(info.getAlertMessage());
        // 地区
        hotelInfo.setBizZone(info.getBizZone());
        // 城市
        hotelInfo.setCity(info.getCity());
        // 结算方式
        hotelInfo.setBalanceMethod(info.getHotel_balanceMethod());
        // 合同币种
        hotelInfo.setCurrency(info.getHotel_currency());
        // 汇率值
        hotelInfo.setRateValue(rateStr);
        // add by wuyun 酒店直联
        hotelInfo.setCooperateChannel(info.getCooperateChannel());

        hotelInfo.setFlagCtsHK(info.isFlagCtsHK());

        // 芒果促销信息
        hotelInfo.setHasPreSale(info.getHasPreSale());
        if (1 == hotelInfo.getHasPreSale()) {
            hotelInfo.setPreSaleName(info.getPreSaleName());
            hotelInfo.setPreSaleContent(info.getPreSaleContent());
            hotelInfo.setPreSaleBeginEnd(info.getPreSaleBeginEnd());
            hotelInfo.setPreSaleURL(info.getPreSaleURL());
        }

        return hotelInfo;
    }

    /**
     * 根据价格类型，酒店ID，开始时间查询是否有担保
     * 
     * @param bookRoomCond
     * @return
     */
    private boolean getCluserBeginDate(long hotelId, long priceTypeId, Date beginDate,
        String hotelConut) {
        String sql = "from HtlPreconcertItem where hotelId = ? and priceTypeId = ? "
            + "and validDate = ? and Active = 1";
        HtlPreconcertItem htlPreconcertItem = (HtlPreconcertItem) super.find(sql, new Object[] {
            hotelId, priceTypeId, beginDate });
        if (null != htlPreconcertItem) {
            List list = htlPreconcertItem.getHtlAssureList();
            if (null != list) {
                for (int i = 0; i < list.size(); i++) {
                    HtlAssure htlAssure = (HtlAssure) list.get(i);
                    if (null != htlAssure) {
                        if (null == htlAssure.getIsNotConditional()
                            && null == htlAssure.getLatestAssureTime()
                            && null == htlAssure.getAssureType()
                            && null == htlAssure.getOverRoomNumber()) {
                            return false;
                        } else if (null != htlAssure.getOverRoomNumber() && null != hotelConut) {
                            return false;
                        } else if (null != htlAssure.getLatestAssureTime()) {
                            return false;
                        }

                    }
                }
            }
        } else {
            return false;
        }
        return true;
    }

    public HotelInfo getHotelInfoById(long hotelId) {
        return null;
    }

    /**
     * 根据ID查找酒店信息
     * 
     * @param hotel_id
     * @return HtlHotel对象
     */
    public HtlHotel findHotel(long hotel_id) {
        return hotelManage.findHotel(hotel_id);
    }

    public String getHotelPresale(BookRoomCondition bc) {
        return "";
    }

    public String getRoomRemark(long roomTypeId) {
        return "";
    }
  
    public List getOrderQuota(BookRoomCondition con) {
        return null;
    }

    public void recycleQuota(Long hotelId, Long roomTypeId, List quotaItems) {
        // TODO Auto-generated method stub

    }

    public void registerQuota(Long hotelId, Long roomTypeId, List quotaItems) {
        // TODO Auto-generated method stub

    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

    public DAOIbatisImpl getQueryDao() {
        return queryDao;
    }

    public void setQueryDao(DAOIbatisImpl queryDao) {
        this.queryDao = queryDao;
    }

    /**
     * 更新用户评论
     * 
     * @param uc
     * @return 成功返回0 失败返回非0数
     */
    public int saveOrUpdateUsesComment(UserComment uc) {
    	log.info("saveOrUpdateUsesComment 没有实现此功能");
        return -1;
    }

    /**
     * 返回预订条款信息
     */
    public List<HtlCreditAssure> queryCreditAssureForCC(QueryCreditAssureForCCBean queryBean) {
        // HtlCreditAssure htlCreditAssure = new HtlCreditAssure();
        List<HtlCreditAssure> queryResult = hotelManage.qryCreditAssureForCC(
            queryBean.getHotelId(), queryBean.getBeginDate(), queryBean.getEndDate(), queryBean
                .getRoomType());
        /*
         * if(queryResult!=null && queryResult.size()>1){ int assureType = 0; for(int
         * i=0;i<queryResult.size();i++){ HtlCreditAssure item =
         * (HtlCreditAssure)queryResult.get(i); String assureTypeStr = item.getAssureType(); if
         * (assureType < Integer.valueOf(assureTypeStr).intValue()){ htlCreditAssure = item;
         * assureType = Integer.valueOf(assureTypeStr).intValue(); } } }else if (queryResult!=null
         * && queryResult.size()==1){ htlCreditAssure = (HtlCreditAssure)queryResult.get(0); }
         */
        return queryResult;
    }

    /**
     * CC查询当前发送酒店信息
     * 
     * @param
     * @return String
     */
    public List getHotelBookSetup(HtlHotel hotel, String ctctType) {

        Date now = new Date();
        now = DateUtil.getDate(now);
        Calendar calStart = Calendar.getInstance();
        int dayw = calStart.get(Calendar.DAY_OF_WEEK) - 1;
        if (0 == dayw) {
            dayw = 7;
        }
        String day = "%" + dayw + "%";
        // if (ctctType.equals("01")) {
        // //selsrc = " select bookemail ";
        // } else {
        // //selsrc = " select bookfax ";
        // }
        String hsql = " from HtlBookSetup " + " where htlHotel = ? and bookctctType = ? "
            + " and bookBeginDate <= ? " + " and bookEndDate >= ? " + " and weeks like ? ";
        Object[] obj = new Object[] { hotel, ctctType, now, now, day };
        List hbList = super.query(hsql, obj);
        return hbList;
    }
    
    //增加供应商后加的方法，由于页面需要根据bookctctType来做判断，故增加了新的方法，以不改变原有逻辑
    public String getHotelSendType(long hotelId,Long childRoomTypeId){
    	List<HtlBookSetup> htlBookSetupList =  Collections.emptyList();
    	if(null != childRoomTypeId){
    		HtlPriceType htlprice = hotelManage.findHtlPriceType(childRoomTypeId);
    		htlBookSetupList = supplierInfoService.queryHtlSupplierFax(htlprice);
    	}
    	
    	String bookctctType = "";
    	if(!htlBookSetupList.isEmpty()){
    		bookctctType = "02";
    	}else{
    		bookctctType = getHotelSendType(String.valueOf(hotelId));
    	}
    	return bookctctType;
    }

    /**
     * 查询默认联系方式
     * 
     * @param
     * @return String
     */
    public String getHotelSendType(String hotelid) {
        HtlBookSetup htlBookSetup = new HtlBookSetup();
        Date now = new Date();
        Date nowDate = DateUtil.getDate(now);
        String strNowTime = DateUtil.formatTimeToString(now);
        String[] strTime = strNowTime.split(":");
        String intTime = strTime[0] + strTime[1];
        int nowTime = Integer.parseInt(intTime);
        String ctcttype = "";
        String hsql = " from HtlBookSetup " + " where htlHotel.ID = ? "
            + " and bookBeginDate  <= ? " + " and bookEndDate >= ? ";

        Object[] obj = new Object[] { Long.parseLong(hotelid), nowDate, nowDate };
        List hbList = super.query(hsql, obj);
        Iterator it = hbList.iterator();
        while (it.hasNext()) {
            htlBookSetup = (HtlBookSetup) it.next();

            if (null != htlBookSetup.getBookBeginTime() && null != htlBookSetup.getBookEndTime()) {
                int bookBeginTime = 0;
                int bookEndTime = 0;

                if (-1 != htlBookSetup.getBookBeginTime().indexOf(":")) {
                    bookBeginTime = Integer.parseInt(htlBookSetup.getBookBeginTime().replace(":",
                        ""));
                } else {
                    // 修复订单0907273003045无法保存 2009-07-27
                    bookBeginTime = Integer.parseInt(htlBookSetup.getBookBeginTime().trim());
                }
                if (-1 != htlBookSetup.getBookEndTime().indexOf(":")) {
                    bookEndTime = Integer.parseInt(htlBookSetup.getBookEndTime().replace(":", ""));
                } else {
                    // 修复订单0907273003045无法保存 2009-07-27
                    bookEndTime = Integer.parseInt(htlBookSetup.getBookEndTime().trim());
                }

                if (bookBeginTime < bookEndTime) {
                    if (nowTime > bookBeginTime && nowTime < bookEndTime) {
                        ctcttype = htlBookSetup.getBookctctType();
                    }

                } else {
                    if ((nowTime > bookBeginTime && 2400 > nowTime)
                        || (0000 < nowTime && nowTime < bookEndTime)) {
                        ctcttype = htlBookSetup.getBookctctType();
                    }

                }
            }
        }
        return ctcttype;

    }

    /**
     * CC查询当前发送酒店Email
     * 
     * @param
     * @return String
     */
    public String getHotelMail(long hotelId) {

        HtlHotel hotel = hotelManage.findHotel(hotelId);
        String bookctctType = "01";
        String hotelEmail = "";
        List hbList = getHotelBookSetup(hotel, bookctctType);
        // 当前系统时间 (HH:mm)
        Date now = new Date();
        String strNowTime = DateUtil.formatTimeToString(now);
        String[] strTime = strNowTime.split(":");
        String intTime = strTime[0] + strTime[1];
        int nowTime = Integer.parseInt(intTime);
        if (0 < hbList.size()) {
            for (int j = 0; j < hbList.size(); j++) {
                HtlBookSetup hbBookSetup = (HtlBookSetup) hbList.get(j);
                if (null != hbBookSetup.getBookBeginTime() 
                    && null != hbBookSetup.getBookEndTime()) {
                    int bookBeginTime = 0;
                    int bookEndTime = 0;
                    if (-1 != hbBookSetup.getBookBeginTime().indexOf(":")) {
                        bookBeginTime = Integer.parseInt(hbBookSetup.getBookBeginTime().replace(
                            ":", ""));
                    } else {
                        bookBeginTime = Integer.parseInt(hbBookSetup.getBookBeginTime());
                    }
                    if (-1 != hbBookSetup.getBookEndTime().indexOf(":")) {
                        bookEndTime = Integer.parseInt(hbBookSetup.getBookEndTime()
                            .replace(":", ""));
                    } else {
                        bookEndTime = Integer.parseInt(hbBookSetup.getBookEndTime());
                    }
                    if (bookBeginTime > bookEndTime) {
                        if (bookBeginTime >= nowTime && bookEndTime <= nowTime) {
                            hotelEmail = hbBookSetup.getBookemail();
                        }
                    } else {
                        if (bookEndTime >= nowTime && bookBeginTime <= nowTime) {
                            hotelEmail = hbBookSetup.getBookemail();
                        }
                    }
                } else {
                    hotelEmail = hbBookSetup.getBookemail();
                }
            }
        } else {
            List htlBookSetup = hotel.getHtlBookSetup();
            for (int i = 0; i < htlBookSetup.size(); i++) {
                if (((HtlBookSetup) htlBookSetup.get(i)).getBookctctType().equals(bookctctType)) {
                    hotelEmail = ((HtlBookSetup) htlBookSetup.get(i)).getBookemail();
                }
            }
        }
        return hotelEmail;
    }

    /**
     * 取出预定传真列表
     * 
     * @param
     * @return List
     */
    public List getHtlBookSetupList(long hotelId, String bookctctType) {
        HtlHotel hotel = hotelManage.findHotel(hotelId);
        List htlBookSetup = hotel.getHtlBookSetup();
        List<HtlBookSetup> htlBookSetupList = new ArrayList<HtlBookSetup>();
        for (int i = 0; i < htlBookSetup.size(); i++) {
            if (((HtlBookSetup) htlBookSetup.get(i)).getBookctctType().equals(bookctctType)) {
                htlBookSetupList.add((HtlBookSetup) htlBookSetup.get(i));
            }
        }
        return htlBookSetupList;
    }

    /**
     * CC查询当前发送酒店传真的号码
     * 
     * @param
     * @return String
     */
    public String getHotelFaxNo(long hotelId) {
    	String bookctctType = "02";
    	HtlHotel hotel = hotelManage.findHotel(hotelId);
        List<HtlBookSetup> hbList = getHotelBookSetup(hotel, bookctctType);
        return getHotelFaxNo(hbList,hotel,bookctctType);
    }
    
    /**
     * 查询预订条款，返回一个字符串 
     */
    public String qryCreditAssure(long hotelId, Date beginDate, String roomType) {

        String queryResult = hotelManage.qryCreditAssure(hotelId, beginDate, roomType);

        return queryResult;
    }
    
    /**
     * v2.6 订单修改基本信息查询酒店
     * 
     * @author chenkeming Feb 5, 2009 4:38:03 PM
     * @param condition
     * @return
     * @throws SQLException 
     */
    public HotelInfo queryBaseInfoHotel(HotelQueryCondition condition) throws SQLException {

        int difdays = DateUtil.getDay(condition.getBeginDate(), condition.getEndDate());

        HotelInfo hotelInfo = null;
        List<HotelResultInfo> list = null;
        int pageCount = 0;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        Map<String, String> rateExchangeMap = CurrencyBean.rateMap;
        try {
            cstmt = setQueryParasBaseInfo(condition);
            cstmt.execute();

            Object dd = cstmt.getObject(30);
            if (null != dd) {
                BigDecimal ssb = (BigDecimal) dd;
                long ssbss = ssb.longValue();
                pageCount = Long.valueOf(ssbss).intValue();
            }
            rs = (ResultSet) cstmt.getObject(33);
            list = getBaseInfoSearchResult(rs);
            rs = null;
            cstmt.close();
        } catch (SQLException e) {
            log.error("Query hotel info error! the cause is:" + e);
            cstmt.close();
        }finally{
            if(null != rs){
                rs.close();
            }
            if(null!=cstmt){
                cstmt.close();
            }
        }

        if (null != list && !list.isEmpty() && 0 != pageCount) {
            // 设置总页数
            condition.setTotalPage(pageCount);
            String rateStr = "no";
            String CurrencySave = "";
            long hotelId = 0;
            long roomTypeId = 0;
            String childRoomTypeId = "";

            // 每天房态和房间数量数据
            String[] roomStates = new String[difdays];
            int[] qtys = new int[difdays];
            hotelInfo = new HotelInfo();
            HotelResultInfo info = list.get(0);
            hotelId = info.getHotelId();
            roomTypeId = info.getRoom_type_id();
            List hotelInfoLis = getBaseInfoHotelInfoList(hotelId, roomTypeId, list);
            hotelInfo.setFlag400(info.isFlag400());
            hotelInfo.setCommendType(info.getHotel_comm_type());
            String lowsePrice = String.valueOf(info.getLowestPrice());
            hotelInfo.setLowestPrice(lowsePrice);
            List<RoomType> roomTypeLis = new ArrayList<RoomType>();
            int roomTypesNum = hotelInfoLis.size();
            hotelInfo.setRoomTypesSize(roomTypesNum);
            for (int k = 0; k < roomTypesNum; k++) {
                RoomType roomType = (RoomType) hotelInfoLis.get(k);
                // 根据酒店ID，价格类型ID（子房型ID），时间，查询出SaleItem集合
                List<SaleItem> saleItems = new ArrayList<SaleItem>();
                roomTypeId = Long.parseLong(roomType.getRoomTypeId());
                childRoomTypeId = roomType.getChildRoomTypeId();
                List saleItemLis = getBaseInfoSaleItemList(hotelId, roomTypeId, childRoomTypeId,
                    list);
                boolean bSetUd = false;
                for (int z = 0; z < saleItemLis.size(); z++) {
                    SaleItem saleItem = (SaleItem) saleItemLis.get(z);
                    // 查询RoomInfo
                    String quotaType = saleItem.getQuotaType();
                    String payMethod = saleItem.getPayMethod();
                    List<RoomInfo> roomInfo = new ArrayList<RoomInfo>();
                    List roomInfoLis = getBaseInfoRoomInfoList(hotelId, childRoomTypeId, quotaType,
                        payMethod, list);

                    // 酒店2.6 预订按钮显示
                    // String bookButtonShow = saleItem.getPayMethod();

                    // add by zhineng.zhuang 2009-02-10 酒店2.6 RMS2388 预订按钮显示
                    String bookButton = new String();

                    // add by shengwei.zuo hotelV2.6 增加面转预标志 2009-04-01
                    saleItem.getPayToPrepay();

                    // 是否是必须面转预？add by shengwei.zuo hotel v2.6 2009-04-24
                    boolean isMustPayToRepay = false;

                    // v2.6 预订不符合的信息提示
                    StringBuffer bookHintNoMeet = new StringBuffer();

                    // 最小配额数
                    int quotaLeastNum = 0;

                    // v2.6 是否有预订条款 add by chenkeming@2009-03-05
                    boolean bHasReserv = false;
                    if (!roomInfoLis.isEmpty() && 0 < roomInfoLis.size()) {
                        for (int x = 0; x < difdays; x++) {
                            Date dt = DateUtil.getDate(condition.getBeginDate(), x);
                            int m = 0;
                            for (int y = 0; y < roomInfoLis.size(); y++) {
                                RoomInfo roomInfos = (RoomInfo) roomInfoLis.get(y);

                                // v2.6 add by chenkeming@2009-03-05 是否有预订条款 begin
                                if (roomInfos.isHasReserv()) {
                                    bHasReserv = true;
                                }
                                // v2.6 add by chenkeming@2009-03-05 是否有预订条款 end

                                // v2.6 预订按钮显示和预订不符合条件提示 begin
                                if (payMethod.equals("pay")) {

                                    // add by shengwei.zuo hotel v2.6 判断预订显示按钮；
                                    bookButton = "pay";

                                    if (1 == roomInfos.getPayToPrepay()) {
                                        bookButton = "pre_pay";
                                        isMustPayToRepay = true;

                                    } else if (roomInfos.isNeedAssure()) {
                                        bookButton = "vouch";

                                    }

                                    /*
                                     * if (roomInfos.isNeedAssure()) { bookButtonShow = "vouch"; }
                                     * if (roomInfos.getPayToPrepay() == 1) { bookButtonShow =
                                     * "pre_pay"; }
                                     */

                                    // 系统日期应该在最晚预订时间的前面，如果系统日期不比其小，提示
//                                    if (StringUtil.isValidStr(roomInfos.getLatestBokableTime())
//                                        && null != roomInfos.getLatestBookableDate()
//                                        && !DateUtil.compareDateTimeToSys(roomInfos
//                                            .getLatestBookableDate(), roomInfos
//                                            .getLatestBokableTime())) {
//                                        bookHintNoMeet.append("预订此房型，必须在");
//                                        bookHintNoMeet.append(DateUtil.dateToString(roomInfos
//                                            .getLatestBookableDate()));
//                                        bookHintNoMeet.append(" ");
//                                        bookHintNoMeet.append(roomInfos.getLatestBokableTime());
//                                        bookHintNoMeet.append("之前预订。");
//                                    }
                                    
                                    //hotel 2.9.3 预订条款时间段的预订规则 add by shengwei.zuo 20090-09-06 begin
                                    bookHintNoMeet = new StringBuffer();
                                    String latestTime = roomInfos.getLatestBokableTime();
                                    String[] sTime = null;
                                    if (null != latestTime && !latestTime.equals("")) {
                                        if (StringUtil.isValidStr(latestTime)
                                            && 0 <= latestTime.indexOf(":")) {
                                            sTime = latestTime.split(":");
                                        }
                                    } else {// 如果预订时间点为空，则赋初始值
                                        latestTime = "23:59";
                                        sTime = latestTime.split(":");
                                    }

                                    if (null != roomInfos.getLatestBookableDate()) {
                                    	
                                    	//如果最早预订时间不为空
                                        if(null!=roomInfos.getFirstBookableDate()){
                                        	
                                            //add by shengwei.zuo hotel2.9.3  最早预订时间 2009-09-06
                                            String firstTime = roomInfos.getFirstBookableTime();
                                            String[] fTime = null;
                                            if (null != firstTime && !firstTime.equals("")) {
                                                if (StringUtil.isValidStr(firstTime)
                                                    && 0 <= firstTime.indexOf(":")) {
                                                	fTime = firstTime.split(":");
                                                }
                                            } else {// 如果预订时间点为空，则赋初始值
                                            	firstTime = "23:59";
                                                fTime = firstTime.split(":");
                                            }
                                        	
                                            //如果当前系统时间不在预订的时间段内，都不能进行预订 add by shengwei.zuo 2009-09-06 hotel 2.9.3
                                        	if(!DateUtil.isBetweenDateTime(roomInfos.getFirstBookableDate(),(fTime[0]+fTime[1]),
                                        			roomInfos.getLatestBookableDate(),(sTime[0] + sTime[1]))){
                                        			
	                                        		bookHintNoMeet.append("预订此房型，必须在");
		                                            bookHintNoMeet
		                                                    .append(DateUtil.dateToString(roomInfos
		                                                        .getFirstBookableDate()));
		                                            bookHintNoMeet.append(" ");
		                                            bookHintNoMeet.append(null == roomInfos
		                                                    .getFirstBookableTime() ? "" : roomInfos
		                                                    .getFirstBookableTime());
		                                            bookHintNoMeet.append(" 至 ");
		                                            bookHintNoMeet
                                                    .append(DateUtil.dateToString(roomInfos
                                                        .getLatestBookableDate()));
		                                            bookHintNoMeet.append(" ");
		                                            bookHintNoMeet.append(null == roomInfos
		                                                    .getLatestBokableTime() ? "" : roomInfos
		                                                    .getLatestBokableTime());
		                                            bookHintNoMeet.append("之间预订。");
		                                          
                                        	}
                                        	
                                        }else{
                                    	
                                        		if (!DateUtil.compareDateTimeToSys(roomInfos
	                                                .getLatestBookableDate(), (sTime[0] + sTime[1]))) {
		                                             bookHintNoMeet.append("预订此房型，必须在");
		                                             bookHintNoMeet
		                                                    .append(DateUtil.dateToString(roomInfos
		                                                        .getLatestBookableDate()));
		                                             bookHintNoMeet.append(" ");
		                                             bookHintNoMeet.append(null == roomInfos
		                                                    .getLatestBokableTime() ? "" : roomInfos
		                                                    .getLatestBokableTime());
		                                             bookHintNoMeet.append("之前预订。");
	                                            }
                                        }
                                   }
                                   
                                  //hotel 2.9.3 预订条款时间段的预订规则 add by shengwei.zuo 20090-09-06 end
                                    
                                    // 系统连住日期应该满足
                                    if (difdays < roomInfos.getContinueDay()) {
                                        bookHintNoMeet.append("在此期间预订此房型，必须连住");
                                        bookHintNoMeet.append(roomInfos.getContinueDay());
                                        bookHintNoMeet.append("晚。");
                                    }
                                    // 必住日期
                                    if ((null != roomInfos.getMustFirstDate()
                                        && null != condition.getBeginDate()
                                        && null != condition.getEndDate() && (!DateUtil.isBetween(
                                        roomInfos.getMustFirstDate(), condition.getBeginDate(),
                                        condition.getEndDate())))
                                        || (null != roomInfos.getMustLastDate()
                                            && null != condition.getBeginDate()
                                            && null != condition.getEndDate() && (!DateUtil
                                            .isBetween(roomInfos.getMustLastDate(), condition
                                                .getBeginDate(), condition.getEndDate())))) {
                                        bookHintNoMeet.append("在此期间预订此房型，入住日期必须包含");
                                        bookHintNoMeet.append(roomInfos.getMustInDate());
                                    }
                                }
                                // v2.6 预订按钮显示和预订不符合条件提示 end

                                if (dt.equals(roomInfos.getFellowDate())) {
                                    roomType.setRoomPrice(BigDecimal.valueOf(
                                        roomInfos.getSalesRoomPrice()).setScale(2,
                                        BigDecimal.ROUND_HALF_UP).doubleValue());
                                    // 判断币种读取汇率
                                    if (!CurrencyBean.RMB.equals(roomInfos.getCurrency())) {
                                        double ratedou = 0;
                                        String currencyCompare = roomInfos.getCurrency();
                                        if (null != currencyCompare
                                            && !currencyCompare.equals(CurrencySave)) {
                                            if (null != rateExchangeMap) {
                                                rateStr = rateExchangeMap.get(currencyCompare);
                                            } else {
                                                rateStr = "1";
                                                log.error("get rateExchange"
                                                    + " ERROR:rateExchangeMap is null ");
                                            }
                                            if (null == rateStr || rateStr.equals("")) {
                                                log.error("get rateExchange "
                                                    + "ERROR:NO such currency in DB");
                                                rateStr = "1";
                                            }
                                        }
                                        CurrencySave = currencyCompare;
                                        ratedou = Double.valueOf(rateStr.trim()).doubleValue();
                                        double ratedoures = BigDecimal.valueOf(
                                            roomInfos.getSalePrice()).multiply(
                                            BigDecimal.valueOf(ratedou)).doubleValue();
                                        roomInfos.setRmbSalePrice(BigDecimal.valueOf(ratedoures)
                                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                        roomInfos.setRmbCurrency(CurrencyBean.RMB);
                                    }
                                    if ("G".equals(roomInfos.getCloseFlag())) {
                                        roomType.setCloseFlag(roomInfos.getCloseFlag());
                                        roomType.setReason(roomInfos.getReason());
                                    }

                                    int tmpQty = roomInfos.getQuotaAmount();
                                    // 生产BUG：配额为负数 add by shengwei.zuo 2009-07-28
                                    if (0 > tmpQty) {
                                        tmpQty = 0;
                                        roomInfos.setQuotaAmount(0);

                                    }
                                    // 判断配额数量是否不为0，填写标志
                                    if (1 > tmpQty) {
                                        saleItem.setQuotaBool(true);
                                    }

                                    // modify by chenkeming@2008.12.11 v2.4.1
                                    // 需求变更(关于呼出配额提醒)
                                    boolean bNotMore = false; // 是否为不可超
                                    // 判断床型的房态，在所有时间段内如果床型都有房态设为不可超那么设置床型房态标志
                                    String roomStatStr = roomInfos.getRoomStatus();
                                    if (StringUtil.isValidStr(roomStatStr)) {
                                        String[] roomOne = roomStatStr.split("/");
                                        for (String kk : roomOne) {
                                            String[] roomKK = kk.split(":");
                                            if ("1".equals(roomKK[0]) && "3".equals(roomKK[1])) {
                                                saleItem.setBedStateOneBool(true);
                                            } else if ("2".equals(roomKK[0])
                                                && "3".equals(roomKK[1])) {
                                                saleItem.setBedStateTwoBool(true);
                                            } else if ("3".equals(roomKK[0])
                                                && "3".equals(roomKK[1])) {
                                                saleItem.setBedStateThrBool(true);
                                            }
                                            if ("3".equals(roomKK[1])) {
                                                bNotMore = true;
                                            }
                                        }

                                        roomStates[x] = roomInfos.getRoomStatus();
                                        qtys[x] = tmpQty;

                                        roomInfo.add(roomInfos);
                                        m++;

                                        // 呼出配额的提醒，只考虑房态为不可超的情况
                                        if (0 == x) {
                                            if (bNotMore) {
                                                quotaLeastNum = tmpQty;
                                            } else {
                                                quotaLeastNum = 9999;
                                            }
                                        } else {
                                            if (bNotMore) {
                                                if (quotaLeastNum > tmpQty) {
                                                    quotaLeastNum = tmpQty;
                                                }
                                            }
                                        }

                                        break;
                                    }
                                }
                            }
                            if (0 == m) {
                                roomStates[x] = "";
                                roomInfo.add(new RoomInfo());
                            }
                        }

                        // 要处理所有床型均为满房和房态不可超且配额为0的情况(114和mango用户)
                        if (!bSetUd) {
                            roomType.setYud(HotelStrUtil.handleRoomStates(roomStates, qtys));
                            bSetUd = true;
                        }
                    }

                    // v2.6 是否有预订条款 add by chenkeming@2009-03-05
                    saleItem.setHasReserv(bHasReserv);

                    // add by shengwei.zuo hotel v2.6 2009-04-08 当有“必须面转预”时，按钮就显示“面转预”；
                    if (!("pre_pay".equals(bookButton)) && isMustPayToRepay) {

                        bookButton = "pre_pay";

                    }

                    // add by zhineng.zhuang 2009-02-10 酒店2.6 RMS2388 预订按钮显示
                    saleItem.setBookButton(bookButton);

                    // 酒店2.6 预订条款提示
                    saleItem.setBookHintNotMeet(bookHintNoMeet.toString());
                    saleItem.setQuotaLeastNum(quotaLeastNum);
                    saleItem.setRoomItems(roomInfo);
                    saleItems.add(saleItem);
                }
                roomType.setSaleItems(saleItems);
                roomTypeLis.add(roomType);
            }
            hotelInfo = setBaseInfoHotelInfo(hotelInfo, info, roomTypeLis, rateStr);
        }
        return hotelInfo;
    }

    /**
     * 订单修改信息查询酒店,设置查询存储过程参数
     * 
     * @author chenkeming Feb 5, 2009 5:08:39 PM
     * @param condition
     * @return
     * @throws SQLException
     */
    private CallableStatement setQueryParasBaseInfo(HotelQueryCondition condition)
        throws SQLException {
        CallableStatement cstmt = null;
        try {
            String procedureName = "{call PKGHOTELQUERY_3_0.getHotelList(?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            cstmt = super.getCurrentSession().connection().prepareCall(procedureName);
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
            cstmt.setString(16, condition.getMaxPrice()); // p_maxprice

            // NUMERIC, --价格上限
            cstmt.setString(17, condition.getHotelCode()); // p_hotelcode
            // VARCHAR2, --酒店代码
            cstmt.setLong(18, Long.valueOf(condition.getHotelId()));
            // Long.valueOf(condition.getHotelId()));
            // // p_hotelid
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

            cstmt.setString(24, null); // p_notes VARCHAR2,

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
            // 新加入渠道.add by haibo.li V2.6 2009-5-21
            cstmt.setString(29, condition.getFromChannel());

            cstmt.registerOutParameter(30, OracleTypes.NUMERIC); // return_pagecount
            // OUT
            // NUMERIC,
            cstmt.registerOutParameter(31, OracleTypes.NUMERIC); // return_recordcount
            // out
            // numeric,
            cstmt.registerOutParameter(32, OracleTypes.VARCHAR); // return_sql
            // OUT
            // VARCHAR2
            cstmt.registerOutParameter(33, OracleTypes.CURSOR); // return_list
            // out
            // outHotelList
        } catch (SQLException sqle) {
            log.error("Get the database connection error,error=" + sqle);
            throw sqle;
        }
        return cstmt;
    }

    /**
     * v2.6 订单修改基本信息重新查询酒店,设置房型信息
     * 
     * @author chenkeming Feb 5, 2009 5:34:17 PM
     * @param hotelId
     * @param roomTypeId
     * @param hotelResultInfo
     * @return
     */
    private List<RoomType> getBaseInfoHotelInfoList(long hotelId, long roomTypeId,
        List<HotelResultInfo> hotelResultInfo) {
        List<RoomType> roomTypeList = new ArrayList<RoomType>();
        RoomType roomType = null;
        String tmpChildRoomTypeId = "";
        String tmpRoomTypeId = "";
        for (HotelResultInfo info : hotelResultInfo) {
            if (!tmpChildRoomTypeId.equals("")
                && tmpChildRoomTypeId.equals(info.getChildRoomTypeId())
                && tmpRoomTypeId.equals(String.valueOf(info.getRoom_type_id()))) {
                continue;
            }
            if (hotelId == info.getHotelId()) {
                roomType = new RoomType();
                roomType.setChildRoomTypeId(info.getChildRoomTypeId());
                roomType.setChildRoomTypeName(info.getPrice_type());
                roomType.setRoomTypeName(info.getRoom_name());
                roomType.setRoomTypeId(String.valueOf(info.getRoom_type_id()));
                roomType.setRecommend("" + info.getComm_level());
                
                //hotel 2.9.3 房间数 add by shengwei.zuo 2009-09-09
                roomType.setCanRoomNumber("" + info.getRoom_qty());
                
                roomTypeList.add(roomType);
                tmpChildRoomTypeId = info.getChildRoomTypeId();
                tmpRoomTypeId = roomType.getRoomTypeId();
            }
        }
        List<RoomType> roomTypes = new ArrayList<RoomType>();
        int i = 0;
        for (; i < roomTypeList.size(); i++) {
            RoomType temp = roomTypeList.get(i);
            if (0 != Integer.parseInt(temp.getRecommend())) {
                break;
            }
            roomTypes.add(temp);
        }
        if (0 != i) {
            roomTypeList.removeAll(roomTypes);
            roomTypeList.addAll(roomTypes);
        }
        return roomTypeList;
    }

    /**
     * 订单修改基本信息，重新查询酒店，得到配额和支付方式
     * 
     * @author chenkeming Feb 5, 2009 5:38:44 PM
     * @param hotelId
     * @param roomTypeId
     * @param childRoomTypeId
     * @param list
     * @return
     */
    private List<SaleItem> getBaseInfoSaleItemList(long hotelId, long roomTypeId,
        String childRoomTypeId, List<HotelResultInfo> list) {
        List<SaleItem> saleItems = new ArrayList<SaleItem>();
        SaleItem saleItem = null;
        String tmpQuotaType = "";
        String tmpPayMethod = "";
        for (HotelResultInfo info : list) {
            if (!tmpQuotaType.equals("") && tmpQuotaType.equals(info.getQuotaType())
                && tmpPayMethod.equals(info.getPayMethod())) {
                continue;
            }
            if (hotelId == info.getHotelId() && childRoomTypeId.equals(info.getChildRoomTypeId())
                && roomTypeId == info.getRoom_type_id()) {
                saleItem = new SaleItem();
                saleItem.setQuotaType(info.getQuotaType());
                saleItem.setPayMethod(info.getPayMethod());
                saleItems.add(saleItem);
                tmpQuotaType = info.getQuotaType();
                tmpPayMethod = info.getPayMethod();
            }
        }
        return saleItems;
    }

    /**
     * v2.6 订单修改基本信息，重新查询酒店，获取价格信息
     * 
     * @author chenkeming Feb 5, 2009 5:40:12 PM
     * @param hotelId
     * @param childRoomTypeId
     * @param quotaType
     * @param payMethod
     * @param list
     * @return
     */
    private List<RoomInfo> getBaseInfoRoomInfoList(long hotelId, String childRoomTypeId,
        String quotaType, String payMethod, List<HotelResultInfo> list) {
        List<RoomInfo> roomInfos = new ArrayList<RoomInfo>();
        RoomInfo roomInfo = null;
        for (HotelResultInfo info : list) {
            if (hotelId == info.getHotelId() && childRoomTypeId.equals(info.getChildRoomTypeId())
                && payMethod.equals(info.getPayMethod()) && quotaType.equals(info.getQuotaType())) {
                roomInfo = new RoomInfo();
                roomInfo.setSalesRoomPrice(info.getSalesroom_price());
                roomInfo.setCurrency(info.getHotel_currency());
                roomInfo.setRmbCurrency(info.getHotel_currency());
                roomInfo.setRmbSalePrice(info.getSalesPrice());
                roomInfo.setQuotaAmount(info.getAvail_qty());
                info.getRoom_qty();
                roomInfo.setSalePrice(info.getSalesPrice());
                // base price
                roomInfo.setBasePrice(info.getBasePrice());
                roomInfo.setCloseFlag(info.getClose_flag());
                roomInfo.setReason(info.getReason());
                roomInfo.setRoomStatus(info.getRoom_state());
                roomInfo.setBreakfast(info.getInc_breakfast_type());
                roomInfo.setBreakNum(info.getInc_breakfast_number());
                roomInfo.setFellowDate(info.getAble_sale_date());

                // v2.6 增加预订条款提示
                roomInfo.setLatestBookableDate(info.getLatestBookableDate());
                roomInfo.setLatestBokableTime(info.getLatestBokableTime());
                
                /**hotel 2.9.3  
                 * 新增时间段的预订规则 add by shengwei.zuo 2009-09-06 begin  
                 */
                roomInfo.setFirstBookableDate(info.getFirstBookableDate());
                roomInfo.setFirstBookableTime(info.getFirstBookableTime());

                /**hotel 2.9.3  
                 * 新增时间段的预订规则 add by shengwei.zuo 2009-09-06 end  
                 */
                
                roomInfo.setMustLastDate(info.getMustLastDate());
                roomInfo.setMustFirstDate(info.getMustFirstDate());
                roomInfo.setContinueDay(info.getContinueDay());
                roomInfo.setMustInDate(info.getMustInDate());

                roomInfo.setHasReserv(info.isHasReserv());

                // v2.9.2 增加预订条款提示 lixiaoyong 2009-07-28 begin
                roomInfo.setMaxRestrictNights(info.getMaxRestrictNights());
                roomInfo.setMinRestrictNights(info.getMinRestrictNights());
                // v2.9.2 增加预订条款提示 lixiaoyong 2009-07-28 end

                roomInfos.add(roomInfo);
            }
        }
        return roomInfos;
    }

    /**
     * v2.6 订单修改基本信息，重新查询酒店，设置返回结果信息
     * 
     * @author chenkeming Feb 5, 2009 5:57:44 PM
     * @param hotelInfo
     * @param info
     * @param roomTypeLis
     * @param rateStr
     * @return
     */
    private HotelInfo setBaseInfoHotelInfo(HotelInfo hotelInfo, HotelResultInfo info,
        List<RoomType> roomTypeLis, String rateStr) {
        hotelInfo.setAcceptCustom(info.getAcceptCustom());
        hotelInfo.setRoomTypes(roomTypeLis);
        // 酒店id
        hotelInfo.setHotelId(info.getHotelId());
        // 酒店中文名称
        hotelInfo.setHotelChnName(info.getHotelChnName());
        // 酒店英文名称
        hotelInfo.setHotelEngName(info.getHotelEngName());
        // 酒店星级
        hotelInfo.setHotelStar(info.getHotelStar());
        String strHotelStar = resourceManager.getDescription("res_hotelStarToNum", info
            .getHotelStar());
        if (StringUtil.isValidStr(strHotelStar)) {
            hotelInfo.setHotelStar1(Float.valueOf(strHotelStar));
        } else {
            hotelInfo.setHotelStar1(1);
        }
        // 酒店类型
        hotelInfo.setHotelType(info.getHotelType());
        // 酒店中文地址
        hotelInfo.setChnAddress(info.getChnAddress());
        // /酒店介绍
        hotelInfo.setHotelIntroduce(info.getHotelIntroduce());
        // 酒店详细介绍
        hotelInfo.setHotelChnIntroduce(info.getHotelChnIntroduce());
        // 提示信息
        hotelInfo.setClueInfo(info.getAlertMessage());
        // 地区
        hotelInfo.setBizZone(info.getBizZone());
        // 城市
        hotelInfo.setCity(info.getCity());
        // 结算方式
        hotelInfo.setBalanceMethod(info.getHotel_balanceMethod());
        // 合同币种
        hotelInfo.setCurrency(info.getHotel_currency());
        // 汇率值
        hotelInfo.setRateValue(rateStr);
        // add by wuyun 酒店直联
        hotelInfo.setCooperateChannel(info.getCooperateChannel());
        return hotelInfo;
    }

    /**
     * v2.6 订单修改基本信息，重新查询酒店，获取存储过程查询结果
     * 
     * @author chenkeming Feb 5, 2009 7:12:14 PM
     * @param rs
     * @return
     * @throws SQLException
     */
    private List<HotelResultInfo> getBaseInfoSearchResult(ResultSet rs) throws SQLException {
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

                // 获取预订条款提示
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
                        .setMustLastDate(new java.util.Date(
                            rs.getDate("MUST_LAST_DATE").getTime()));
                }
                if (null != rs.getDate("MUST_FIRST_DATE")) {
                    info.setMustFirstDate(new java.util.Date(rs.getDate("MUST_FIRST_DATE")
                        .getTime()));
                }
                info.setContinueDay(rs.getLong("CONTINUE_DAY"));
                info.setMustInDate(rs.getString("MUST_IN_DATE"));

                int nHasReserv = rs.getInt("HAS_RESERV");
                info.setHasReserv(1 == nHasReserv ? true : false);

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

    public ContractManage getContractManage() {
        return contractManage;
    }

    public void setContractManage(ContractManage contractManage) {
        this.contractManage = contractManage;
    }

    public HotelManageGroup getHotelManageGroup() {
        return hotelManageGroup;
    }

    public void setHotelManageGroup(HotelManageGroup hotelManageGroup) {
        this.hotelManageGroup = hotelManageGroup;
    }

    /**
     * @author lixiaoyong
     * @param queryHotelPriceList
     *            查询的ID
     * @param subParam
     *            传入参数的map
     * @return 某个酒店的某个房型的价格详情列表
     */
    public List queryHotelPriceList(String queryHotelPriceList, Map subParam) {
        List retList = null;
        try {
            retList = queryDao.queryForList(queryHotelPriceList, subParam);
        } catch (Exception e) {
        	log.error(e.getMessage(),e);
        }
        return retList;
    }

    /**
     * cc修改订单基本信息，前后多查询价格详情
     * 
     * @author OrOrder order
     * @param reserv
     * @param params
     */
    public List getHotelPriceList(HotelPriceSearchParam hotelPriceSearchParam) {
        List hotePricesList = new ArrayList();
        Map subParam = new FormatMap();
        Date tempDate = new Date();
        subParam.put("hotelId", hotelPriceSearchParam.getHotelId());
        subParam.put("roomTypeId", hotelPriceSearchParam.getRoomTypeId());
        // 生产bug 面转预的时候应该查询面付价格详情 modify by chenjiajie 2009-10-25
        String payMethod = "";
        if(PayMethod.PAY.equals(hotelPriceSearchParam.getPayMethod())
        		|| hotelPriceSearchParam.isPayToPrepay()){
        	payMethod = PayMethod.PAY;
        }else{
        	payMethod = PayMethod.PRE_PAY;
        }
        subParam.put("payMethod", payMethod);
        List<String> dateStrList = DateUtil.getDateStrList(DateUtil.getDate(hotelPriceSearchParam.getCheckinDate(),
            -2), DateUtil.getDate(hotelPriceSearchParam.getCheckoutDate(), 2), false);
        for (int i = 0; 4 > i; i++) {
            Map hotelPriceMap = new HashMap();
            if (2 > i) {
                tempDate = DateUtil.getDate(hotelPriceSearchParam.getCheckinDate(), i - 2);
            } else {
                tempDate = DateUtil.getDate(hotelPriceSearchParam.getCheckoutDate(), i - 2);
            }
            subParam.put("ableSaleDate", DateUtil.dateToString(tempDate));
            List hotelInfo = queryHotelPriceList("queryHotelPriceList", subParam);
            Iterator iterator = hotelInfo.iterator();
            while (iterator.hasNext()) {
                Map priceMap = (HashMap) iterator.next();
                if (2 > i) {
                    hotelPriceMap.put("dateStr", dateStrList.get(i));
                } else {
                    hotelPriceMap.put("dateStr", dateStrList.get(i
                        + DateUtil.getDay(hotelPriceSearchParam.getCheckinDate(), hotelPriceSearchParam.getCheckoutDate())));
                }
                hotelPriceMap.put("roomSatuDaStr",  this.getAbleBedTypes((String) priceMap
                    .get("ROOM_STATE")));
                hotelPriceMap.put("priceDetail", ((BigDecimal) priceMap.get("SALE_PRICE"))
                    .toString());
                hotelPriceMap.put("breakfaseType", this.getBreakType((String) priceMap
                    .get("BREAKFAST_TYPE"), (String) priceMap.get("INC_BREAKFAST")));
                hotelPriceMap.put("availQty", ((BigDecimal) priceMap.get("AVAIL_QTY")).toString());
            }
            hotePricesList.add(hotelPriceMap);
        }
        return hotePricesList;
    }

    /**
	 * @author lixiaoyong
	 * @param sRoomStatus  从数据库中查出的标识
	 * @return  		   将标识转化为的字符串
	 */
    public static String getAbleBedTypes(String sRoomStatus) {
        StringBuffer buffer = new StringBuffer("");
        if (StringUtil.isValidStr(sRoomStatus)&&null != sRoomStatus) {
            String[] roomArray0 = sRoomStatus.trim().split("/");
            for (int n = 0; n < roomArray0.length; n++) {
                String bedType = roomArray0[n].substring(0, roomArray0[n]
                        .indexOf(":"));
                String roomStatu = roomArray0[n].substring(roomArray0[n]
                        .indexOf(":") + 1);
                    if (bedType.equals("1")) {
                    buffer.append("大:");}
                    if (bedType.equals("2")) {
                    buffer.append("双:");}
                    if (bedType.equals("3")) {
                    buffer.append("单:");}
                    if (roomStatu.equals("0")) {
                    buffer.append("freesale,");}
                    if (roomStatu.equals("1")) {
                    buffer.append("良,");}
                    if (roomStatu.equals("2")) {
                    buffer.append("紧张,");}
                    if (roomStatu.equals("3")) {
                    buffer.append("不可超,");}
                    if (roomStatu.equals("4")) {
                    buffer.append("满房,");}
                }
            }
        return buffer.toString();        
    }
   
    /**
	 * @author lixiaoyong 
	 * @param   breakType 从数据库中查出的标识
	 * @return  		  将标识转化为的字符串
	 */
     private String getBreakType(String breakType,String breakNumber){
        String str = "";
        if(null != breakType && !breakType.equals("")){
            if(breakType.equals("1")){
                str = "中:";
            }else if(breakType.equals("2")){
                str ="西:";
            }else if(breakType.equals("3")){
                str ="自助:";
            }
        }
        if(null != breakNumber && !"".equals(breakNumber)){
            if(breakNumber.equals("0")){
                str+="不含";
            }else {
                str+=breakNumber;
            }
        }
        return str;
        
//		
//		
//		
//		
//		
//		if(breakType.endsWith("1")){
//			return "中 : 1";
//		}else if(breakType.equals("2")){
//			return "西 : 2";
//		}else if(breakType.equals("3")){
//			return "自助 : 1";
//		}else{
//			return " ";
//		}
    }
    /**
     * 酒店预定条款操作日志明细记录方法 add by haibo.li 2009-5-2 参数(1,新的修改过后的对象，以前数据库中没有修改过的对象) 该方法用于比较
     * 预定条款按日期修改，2个对象中修改了那些属性，都于记录下来，便于查看
     */

    public void putRecord(HtlPreconcertItem newPreconcertItem, HtlPreconcertItem oldPreconcertItem,
        long id) {
        List newhtlReservationList = newPreconcertItem.getHtlReservationList();
        // 根据新操作的预定对象取得预订的list
        List newhtlAssureList = newPreconcertItem.getHtlAssureList();
        // 根据新操作的预定对象取得担保的list
        List newhtlPrepayEverydayList = newPreconcertItem.getHtlPrepayEverydayList();
        // 根据新操作的预定对象取得预付的list
        List oldhtlReservationList = oldPreconcertItem.getHtlReservationList();
        // 根据数据库中没有更新的预定对象取的预订list
        List oldhtlAssureList = oldPreconcertItem.getHtlAssureList();
        // 根据数据库中没有更新的预定对象取的担保list
        List oldPrepayEverydayList = oldPreconcertItem.getHtlPrepayEverydayList();
        // 根据数据库中没有更新的预定对象取的预付list

        // 定义2个类型的StringBuffer，用于记录预订操作明细
        StringBuffer reservationNewRecord = new StringBuffer();
        StringBuffer reservationOldRecord = new StringBuffer();

        // 定义4个类型的StringBuffer，用于记录预付操作明细
        StringBuffer prepayNewRecord = new StringBuffer();
        StringBuffer prepayOldRecord = new StringBuffer();
        StringBuffer prepayTemplateNew = new StringBuffer();
        StringBuffer prepayTemplateOld = new StringBuffer();

        // 定义4个类型的StringBuffer，用于记录担保操作明细
        StringBuffer assureNewRecord = new StringBuffer();
        StringBuffer assureOldRecord = new StringBuffer();
        StringBuffer assureTemplateNew = new StringBuffer();
        StringBuffer assureTemplateOld = new StringBuffer();

        /**
         * 预订
         */

        if (0 == oldhtlReservationList.size() && 0 < newhtlReservationList.size()) {
            // 旧的预订没有数据，新操作的有数据，则为增加了新数据，记录下显示起来
            HtlReservation newHtlReservation = (HtlReservation) newhtlReservationList.get(0);
            // 校验不为空.add by shengwei.zuo 2009-05-31 hotel2.6 begin
            if (null != newHtlReservation) {
                if (null != newHtlReservation.getAheadDay()
                    || (null != newHtlReservation.getAheadTime() && !"".equals(newHtlReservation
                        .getAheadTime()))
                    || null != newHtlReservation.getMustAheadDate()
                    || (null != newHtlReservation.getMustAheadTime() && !""
                        .equals(newHtlReservation.getMustAheadTime()))
                    || null != newHtlReservation.getContinueNights()) {
                    reservationNewRecord.append("新增加了预订条款,内容为--");
                }
                if (null != newHtlReservation.getAheadDay()) {
                    reservationNewRecord.append("提前天数:<font color='red'>"
                        + (null == newHtlReservation.getAheadDay() ? "无" : newHtlReservation
                            .getAheadDay()) + "</font>;");
                }
                if (null != newHtlReservation.getAheadTime()
                    && !"".equals(newHtlReservation.getAheadTime())) {
                    reservationNewRecord.append("提前时间点:<font color='red'>"
                        + (newHtlReservation.getAheadTime().equals("") ? "无" : newHtlReservation
                            .getAheadTime()) + "</font>;");
                }
                if (null != newHtlReservation.getMustAheadDate()) {
                    reservationNewRecord.append("必须提前日期:<font color='red'>"
                        + (null == newHtlReservation.getMustAheadDate() ? "无" : DateUtil
                            .dateToString(newHtlReservation.getMustAheadDate())) + "</font>;");
                }
                if (null != newHtlReservation.getMustAheadTime()
                    && !"".equals(newHtlReservation.getMustAheadTime())) {
                    reservationNewRecord.append("必须提前时间点:<font color='red'>"
                        + (newHtlReservation.getMustAheadTime().equals("") ? "无"
                            : newHtlReservation.getMustAheadTime()) + "</font>;");
                }
                if (null != newHtlReservation.getContinueNights()) {
                    reservationNewRecord.append("连住晚数:<font color='red'>"
                        + (null == newHtlReservation.getContinueNights() ? "无" : newHtlReservation
                            .getContinueNights()) + "</font>;");
                }

                List newhtlReservacontList = newHtlReservation.getHtlReservacontList();
                // 预订对象下的子对象，必住日期对象
                if (null != newhtlReservacontList && 0 < newhtlReservacontList.size()) {
                    reservationNewRecord.append("<br/> 新增加了必住日期：");
                    for (int i = 0; i < newhtlReservacontList.size(); i++) {
                        HtlReservCont htlReservCont = (HtlReservCont) newhtlReservacontList.get(i);
                        if (null != htlReservCont && null != htlReservCont.getContinueDate()) {
                            reservationNewRecord.append("<font color='red'>"
                                + DateUtil.dateToString(htlReservCont.getContinueDate())
                                + "</font>;");
                        }

                    }
                }
            }
            // 校验不为空.add by shengwei.zuo 2009-05-31 hotel2.6 end
        }
        if (0 < oldhtlReservationList.size() && 0 == newhtlReservationList.size()) {
            // 如果之前的记录有，而新修改的记录预订记录没有，则被清空了，记录一句话即可
            reservationNewRecord.append("把之前的预订条款内容删除了");
        }
        if (0 < oldhtlReservationList.size() && 0 < newhtlReservationList.size()) {
            // 记录都不为空，则根据旧记录修改了，都要记录下来
            HtlReservation newHtlReservation = (HtlReservation) newhtlReservationList.get(0);
            HtlReservation oldHtlReservation = (HtlReservation) oldhtlReservationList.get(0);

            // 校验之前的预订条款不为空.add by shengwei.zuo 2009-05-31 hotel2.6 begin
            if (null != oldHtlReservation) {
                if (null != oldHtlReservation.getAheadDay()) {
                    reservationOldRecord.append("之前的提前天数:<font color='red'>"
                        + (null == oldHtlReservation.getAheadDay() ? "无" : oldHtlReservation
                            .getAheadDay()) + "</font>;");
                }
                if (null != oldHtlReservation.getAheadTime()
                    && !oldHtlReservation.getAheadTime().equals("")) {
                    reservationOldRecord.append("之前的提前时间点:<font color='red'>"
                        + (oldHtlReservation.getAheadTime().equals("") ? "无" : oldHtlReservation
                            .getAheadTime()) + "</font>;");
                }
                if (null != oldHtlReservation.getMustAheadDate()) {
                    reservationOldRecord.append("之前的必须提前日期:<font color='red'>"
                        + (null == oldHtlReservation.getMustAheadDate() ? "无" : DateUtil
                            .dateToString(oldHtlReservation.getMustAheadDate())) + "</font>;");
                }
                if (null != oldHtlReservation.getMustAheadTime()
                    && !oldHtlReservation.getMustAheadTime().equals("")) {
                    reservationOldRecord.append("之前的必须提前时间点:<font color='red'>"
                        + (oldHtlReservation.getMustAheadTime().equals("") ? "无"
                            : oldHtlReservation.getMustAheadTime()) + "</font>;");
                }
                if (null != oldHtlReservation.getContinueNights()) {
                    reservationOldRecord.append("之前的连住晚数:<font color='red'>"
                        + (null == oldHtlReservation.getContinueNights() ? "无" : oldHtlReservation
                            .getContinueNights()) + "</font>;");
                }
            }
            // 校验之前的预订条款不为空.add by shengwei.zuo 2009-05-31 hotel2.6 end

            // 校验新的预订条款不为空.add by shengwei.zuo 2009-05-31 hotel2.6 begin
            if (null != newHtlReservation) {
                if (null != newHtlReservation.getAheadDay()) {
                    reservationNewRecord.append("新的提前天数:<font color='red'>"
                        + (null == newHtlReservation.getAheadDay() ? "无" : newHtlReservation
                            .getAheadDay()) + "</font>;");
                }
                if (null != newHtlReservation.getAheadTime()
                    && !newHtlReservation.getAheadTime().equals("")) {
                    reservationNewRecord.append("新的提前时间点:<font color='red'>"
                        + (newHtlReservation.getAheadTime().equals("") ? "无" : newHtlReservation
                            .getAheadTime()) + "</font>;");
                }
                if (null != newHtlReservation.getMustAheadDate()) {
                    reservationNewRecord.append("新的必须提前日期:<font color='red'>"
                        + (null == newHtlReservation.getMustAheadDate() ? "无" : DateUtil
                            .dateToString(newHtlReservation.getMustAheadDate())) + "</font>;");
                }
                if (null != newHtlReservation.getMustAheadTime()
                    && !newHtlReservation.getMustAheadTime().equals("")) {
                    reservationNewRecord.append("新的必须提前时间点:<font color='red'>"
                        + (newHtlReservation.getMustAheadTime().equals("") ? "无"
                            : newHtlReservation.getMustAheadTime()) + "</font>;");
                }
                if (null != newHtlReservation.getContinueNights()) {
                    reservationNewRecord.append("新的连住晚数:<font color='red'>"
                        + (null == newHtlReservation.getContinueNights() ? "无" : newHtlReservation
                            .getContinueNights()) + "</font>;");
                }
            }
            // 校验新的预订条款不为空.add by shengwei.zuo 2009-05-31 hotel2.6 end

            // 在子对象中又出现了3种情况,1为旧的必住日期没有，新的有，则新增。2为旧的有，新的没有，则删除了。3旧的记录有，新的也有，则修改了都要记录

            if (0 == newHtlReservation.getHtlReservacontList().size()
                && 0 < oldHtlReservation.getHtlReservacontList().size()) {

                reservationNewRecord.append("把之前的必住日期删除了<br>");

            } else if (0 < newHtlReservation.getHtlReservacontList().size()
                && 0 == oldHtlReservation.getHtlReservacontList().size()) {
                reservationNewRecord.append("新增加了必住日期,内容为--");
                for (int i = 0; i < newHtlReservation.getHtlReservacontList().size(); i++) {
                    HtlReservCont htlReservacont = (HtlReservCont) newHtlReservation
                        .getHtlReservacontList().get(i);
                    reservationNewRecord.append("<font color='red'>"
                        + DateUtil.dateToString(htlReservacont.getContinueDate()) + "</font><br>");
                }
                reservationNewRecord.append("<br>");
            } else if (0 < newHtlReservation.getHtlReservacontList().size()
                && 0 < oldHtlReservation.getHtlReservacontList().size()) {
                reservationOldRecord.append("之前的必住日期为:");
                for (int i = 0; i < oldHtlReservation.getHtlReservacontList().size(); i++) {
                    HtlReservCont htlReservacont = (HtlReservCont) oldHtlReservation
                        .getHtlReservacontList().get(i);
                    reservationOldRecord.append("<font color='red'>"
                        + DateUtil.dateToString(htlReservacont.getContinueDate()) + "</font><br>");

                }
                reservationNewRecord.append("<br>");
                reservationNewRecord.append("修改后的必住日期为:");
                for (int j = 0; j < newHtlReservation.getHtlReservacontList().size(); j++) {
                    HtlReservCont htlReservacont = (HtlReservCont) newHtlReservation
                        .getHtlReservacontList().get(j);
                    reservationNewRecord.append("<font color='red'>"
                        + DateUtil.dateToString(htlReservacont.getContinueDate()) + "</font><br>");
                }
                reservationNewRecord.append("<br>");
            }
        }
        /**
         * 担保
         */

        if (0 == oldhtlAssureList.size() && 0 < newhtlAssureList.size()) {
            // 旧的记录没有，新的记录有，则是新增加的记录，得记录下来
            HtlAssure ha = (HtlAssure) newhtlAssureList.get(0);
            // 担保条款和预定总表是一对一关系，则取第一个对象
            if (null != ha) {

                assureNewRecord.append("新增加了担保条款,内容为--");
                if (null != ha.getIsNotConditional()) {
                    assureNewRecord.append("是否无条件担保:<font color='red'>"
                        + (ha.getIsNotConditional().equals("1") ? "是" : "否") + "</font>;");
                }
                if (null != ha.getLatestAssureTime() && !ha.getLatestAssureTime().equals("")) {
                    assureNewRecord.append("最晚担保时间:<font color='red'>"
                        + (ha.getLatestAssureTime().equals("") ? "无" : ha.getLatestAssureTime())
                        + "</font>;");
                }
                if (null != ha.getOverRoomNumber()) {
                    assureNewRecord.append("超房数量:<font color='red'>"
                        + (null == ha.getOverRoomNumber() ? "无" : ha.getOverRoomNumber())
                        + "</font>;");
                }
                if (null != ha.getAssureType()) {
                    assureNewRecord.append("担保类型:<font color='red'> "
                        + (ha.getAssureType().equals("") ? "无" : (this.returnSorce(ha
                            .getAssureType()))) + "</font>;");
                }
                if (null != ha.getAssureLetter()) {
                    assureNewRecord
                        .append("是否需要担保函:<font color='red'>"
                            + ("1".equals(ha.getAssureLetter())
                                || ha.getAssureLetter().equals("") ? "是"
                                : "否") + "</font>;");
                }
                assureNewRecord.append("<br>");
                // 校验担保条款不为空.add by shengwei.zuo 2009-05-31 hotel2.6 begin
                if (!ha.getHtlAssureItemEverydayList().isEmpty()) {
                    for (int i = 0; i < ha.getHtlAssureItemEverydayList().size(); i++) {
                        HtlAssureItemEveryday hi = (HtlAssureItemEveryday) ha
                            .getHtlAssureItemEverydayList().get(i);
                        if (null != hi) {
                            if (null != hi.getType()) {
                                if (ClaueType.ASSURE_TYPE_ONE.equals(hi.getType())) {
                                    assureTemplateNew.append("担保取消修改条款:");
                                    assureTemplateNew.append("凡<font color='red'>"
                                        + this.returnSorceType(hi.getScope()) + "</font>");
                                    assureTemplateNew.append("均需扣取<font color='red'>"
                                        + this.returnDeductType(hi.getDeductType()) + "</font>");
                                    if ((null != hi.getDeductType() && !hi.getDeductType().equals(
                                        ""))
                                        && null != hi.getPercentage()) {
                                        assureTemplateNew.append("<font color='red'>"
                                            + (hi.getPercentage().equals("") ? "无" : hi
                                                .getPercentage()) + "</font>");
                                    }
                                    assureTemplateNew.append("<br>");
                                } else if (ClaueType.ASSURE_TYPE_TWO.equals(hi.getType())) {
                                    assureTemplateNew.append("担保取消修改条款:在");
                                    assureTemplateNew.append("<font color='red'>"
                                        + hi.getFirstDateOrDays() + "</font>" + "日期");
                                    assureTemplateNew.append("<font color='red'>"
                                        + hi.getFirstTime() + "</font> 时间至");
                                    assureTemplateNew.append("<font color='red'>"
                                        + hi.getSecondDateOrDays() + "</font> 日期");
                                    assureTemplateNew.append("<font color='red'>"
                                        + hi.getSecondTime() + "</font> 时间");
                                    assureTemplateNew.append("<font color='red'>"
                                        + this.returnSorceType(hi.getScope()) + "</font>");
                                    assureTemplateNew.append("需扣取<font color='red'>"
                                        + this.returnDeductType(hi.getDeductType()) + "</font>");
                                    if ((null != hi.getDeductType() && !hi.getDeductType().equals(
                                        ""))
                                        && null != hi.getPercentage()) {
                                        assureTemplateNew.append("<font color='red'>"
                                            + (hi.getPercentage().equals("") ? "无" : hi
                                                .getPercentage()) + "</font>");
                                    }

                                    assureTemplateNew.append("<br>");

                                } else if (ClaueType.ASSURE_TYPE_THREE.equals(hi.getType())) {
                                    assureTemplateNew.append("担保取消修改条款:在");
                                    assureTemplateNew.append("<font color='red'>"
                                        + hi.getFirstDateOrDays() + "</font> 日期");
                                    assureTemplateNew.append("<font color='red'>"
                                        + hi.getFirstTime() + "</font> 时间至");
                                    assureTemplateNew.append("入住当日<font color='red'>"
                                        + hi.getSecondTime() + "</font>时间");
                                    assureTemplateNew.append("<font color='red'>"
                                        + this.returnSorceType(hi.getScope()) + "</font>");
                                    assureTemplateNew.append("需扣取<font color='red'>"
                                        + this.returnDeductType(hi.getDeductType()) + "</font>");
                                    if ((null != hi.getDeductType() && !hi.getDeductType().equals(
                                        ""))
                                        && null != hi.getPercentage()) {
                                        assureTemplateNew.append("<font color='red'>"
                                            + (hi.getPercentage().equals("") ? "无" : hi
                                                .getPercentage()) + "</font>");
                                    }
                                    assureTemplateNew.append("<br>");
                                } else if (ClaueType.ASSURE_TYPE_FORV.equals(hi.getType())) {
                                    assureTemplateNew.append("担保取消修改条款:在");
                                    assureTemplateNew.append("入住日期前<font color='red'>"
                                        + hi.getFirstDateOrDays() + "</font> 天");
                                    assureTemplateNew.append("<font color='red'>"
                                        + hi.getFirstTime() + "</font> 时间至");
                                    assureTemplateNew.append("<font color='red'>"
                                        + hi.getSecondDateOrDays() + "</font> 天");
                                    assureTemplateNew.append("<font color='red'>"
                                        + hi.getSecondTime() + "</font> 时间");
                                    assureTemplateNew.append("<font color='red'>"
                                        + this.returnSorceType(hi.getScope()) + "</font> ");
                                    assureTemplateNew.append("需要扣取<font color='red'>"
                                        + this.returnDeductType(hi.getDeductType()) + "</font>");
                                    if ((null != hi.getDeductType() && !hi.getDeductType().equals(
                                        ""))
                                        && null != hi.getPercentage()) {
                                        assureTemplateNew.append("<font color='red'>"
                                            + (hi.getPercentage().equals("") ? "无" : hi
                                                .getPercentage()) + "</font>");
                                    }
                                    assureTemplateNew.append("<br>");
                                } else if (ClaueType.ASSURE_TYPE_FIVE.equals(hi.getType())) {
                                    assureTemplateNew.append("担保取消修改条款:");
                                    assureTemplateNew.append("当天<font color='red'>"
                                        + hi.getFirstTime() + "</font>");
                                    assureTemplateNew.append("<font color='red'>"
                                        + this.returnAfter(hi.getBeforeOrAfter()) + "</font>");
                                    assureTemplateNew.append("<font color='red'>"
                                        + this.returnSorceType(hi.getScope()) + "</font>");
                                    assureTemplateNew.append("需要扣取<font color='red'>"
                                        + this.returnDeductType(hi.getDeductType()) + "</font>");
                                    if ((null != hi.getDeductType() && !hi.getDeductType().equals(
                                        ""))
                                        && null != hi.getPercentage()) {
                                        assureTemplateNew.append("<font color='red'>"
                                            + (hi.getPercentage().equals("") ? "无" : hi
                                                .getPercentage()) + "</font>");
                                    }
                                    assureTemplateNew.append("<br>");
                                }
                            }
                        }
                    }

                }

            }
        }
        if (0 < oldhtlAssureList.size() && 0 == newhtlAssureList.size()) {
            // 旧的条款中有数据,而新的没有,就是删除了旧的,所以直接显示
            assureTemplateNew.append("删除了担保条款内容");
        }
        if (0 < oldhtlAssureList.size() && 0 < newhtlAssureList.size()) {
            // 如果新旧记录都有，则都记录出来
            HtlAssure newha = (HtlAssure) newhtlAssureList.get(0);
            HtlAssure oldha = (HtlAssure) oldhtlAssureList.get(0);

            // 做不为空的校验，否则会报空指针.add by shengwei.zuo hotel 2.6 2009-05-27
            if (null != oldha) {
                assureOldRecord.append("之前的担保条款为--");
                if (null != oldha.getIsNotConditional()) {
                    assureOldRecord.append("是否无条件担保:<font color='red'>"
                        + (oldha.getIsNotConditional().equals("1") ? "是" : "否") + "</font>;");
                }
                if (null != oldha.getLatestAssureTime() 
                    && !oldha.getLatestAssureTime().equals("")) {
                    assureOldRecord.append("最晚担保时间:<font color='red'>"
                        + (oldha.getLatestAssureTime().equals("") ? "无" : oldha
                            .getLatestAssureTime()) + "</font>;");
                }
                if (null != oldha.getOverRoomNumber()) {
                    assureOldRecord.append("超房数量:<font color='red'>"
                        + (null == oldha.getOverRoomNumber() ? "无" : oldha.getOverRoomNumber())
                        + "</font>;");
                }
                if (null != oldha.getAssureType()) {
                    assureOldRecord.append("担保类型:<font color='red'> "
                        + (oldha.getAssureType().equals("") ? "无" : (this.returnSorce(oldha
                            .getAssureType()))) + "</font>;");
                }

                if (null != oldha.getAssureLetter()) {
                    assureOldRecord.append("是否需要担保函:<font color='red'>"
                        + ("1".equals(oldha.getAssureLetter())
                            || oldha.getAssureLetter().equals("1") ? "是" : "否") + "</font>;");
                } else {
                    assureOldRecord.append("是否需要担保函:<font color='red'>否</font>");
                }

                assureOldRecord.append("<br>");

            }

            // 做不为空的校验，否则会报空指针.add by shengwei.zuo hotel 2.6 2009-05-27
            if (null != newha) {
                assureNewRecord.append("修改后的担保条款为--");
                if (null != newha.getIsNotConditional()) {
                    assureNewRecord.append("是否无条件担保:<font color='red'>"
                        + (newha.getIsNotConditional().equals("1") ? "是" : "否") + "</font>;");
                }
                if (null != newha.getLatestAssureTime() 
                    && !newha.getLatestAssureTime().equals("")) {
                    assureNewRecord.append("最晚担保时间:<font color='red'>"
                        + ((newha.getLatestAssureTime().equals("")) ? "无" : newha
                            .getLatestAssureTime()) + "</font>;");
                }
                if (null != newha.getOverRoomNumber()) {

                    assureNewRecord.append("超房数量:<font color='red'>"
                        + (null == newha.getOverRoomNumber() ? "无" : newha.getOverRoomNumber())
                        + "</font>;");
                }
                if (null != newha.getAssureType()) {
                    assureNewRecord.append("担保类型:<font color='red'> "
                        + (newha.getAssureType().equals("") ? "无" : (this.returnSorce(newha
                            .getAssureType()))) + "</font>;");
                }
                log.info("修改后的担保条款: " + newha.getAssureLetter());
                if (null != newha.getAssureLetter()) {
                    assureNewRecord.append("是否需要担保函:<font color='red'>"
                        + ("1".equals(newha.getAssureLetter())
                            || newha.getAssureLetter().equals("1") ? "是" : "否") + "</font>;");
                }
                assureNewRecord.append("<br>");

            }

            // 做不为空的校验，否则会报空指针.add by shengwei.zuo hotel 2.6 2009-05-27
            if (null != oldha && !oldha.getHtlAssureItemEverydayList().isEmpty()) {

                for (int i = 0; i < oldha.getHtlAssureItemEverydayList().size(); i++) {
                    HtlAssureItemEveryday hi = (HtlAssureItemEveryday) oldha
                        .getHtlAssureItemEverydayList().get(i);
                    if (null != hi.getType()) {

                        if (ClaueType.ASSURE_TYPE_ONE.equals(hi.getType())
                            || hi.getType().equals(ClaueType.ASSURE_TYPE_ONE)) {
                            assureTemplateOld.append("之前的担保取消修改条款:");
                            assureTemplateOld.append("凡<font color='red'>"
                                + this.returnSorceType(hi.getScope()) + "</font>");
                            assureTemplateOld.append("均需扣取<font color='red'>"
                                + this.returnDeductType(hi.getDeductType()) + "</font>");
                            if ((null != hi.getDeductType() && !hi.getDeductType().equals(""))
                                && null != hi.getPercentage()) {
                                assureTemplateOld.append("<font color='red'>"
                                    + (hi.getPercentage().equals("") ? "无" : hi.getPercentage())
                                    + "</font>");
                            }
                            assureTemplateOld.append("<br>");
                        }
                        if (ClaueType.ASSURE_TYPE_TWO.equals(hi.getType())
                            || hi.getType().equals(ClaueType.ASSURE_TYPE_TWO)) {
                            assureTemplateOld.append("之前的担保取消修改条款:在");
                            assureTemplateOld.append("<font color='red'>" + hi.getFirstDateOrDays()
                                + "</font>" + "日期");
                            assureTemplateOld.append("<font color='red'>" + hi.getFirstTime()
                                + "</font> 时间至");
                            assureTemplateOld.append("<font color='red'>"
                                + hi.getSecondDateOrDays() + "</font> 日期");
                            assureTemplateOld.append("<font color='red'>" + hi.getSecondTime()
                                + "</font> 时间");
                            assureTemplateOld.append("<font color='red'>"
                                + this.returnSorceType(hi.getScope()) + "</font>");
                            assureTemplateOld.append("需扣取<font color='red'>"
                                + this.returnDeductType(hi.getDeductType()) + "</font>");
                            if ((null != hi.getDeductType() && !hi.getDeductType().equals(""))
                                && null != hi.getPercentage()) {
                                assureTemplateOld.append("<font color='red'>"
                                    + (hi.getPercentage().equals("") ? "无" : hi.getPercentage())
                                    + "</font>");
                            }

                            assureTemplateOld.append("<br>");
                        }
                        if (ClaueType.ASSURE_TYPE_THREE.equals(hi.getType())
                            || hi.getType().equals(ClaueType.ASSURE_TYPE_THREE)) {
                            assureTemplateOld.append("之前的担保取消修改条款:在");
                            assureTemplateOld.append("<font color='red'>" + hi.getFirstDateOrDays()
                                + "</font> 日期");
                            assureTemplateOld.append("<font color='red'>" + hi.getFirstTime()
                                + "</font> 时间至");
                            assureTemplateOld.append("入住当日<font color='red'>" + hi.getSecondTime()
                                + "</font>时间");
                            assureTemplateOld.append("<font color='red'>"
                                + this.returnSorceType(hi.getScope()) + "</font>");
                            assureTemplateOld.append("需扣取<font color='red'>"
                                + this.returnDeductType(hi.getDeductType()) + "</font>");
                            if ((null != hi.getDeductType() && !hi.getDeductType().equals(""))
                                && null != hi.getPercentage()) {
                                assureTemplateOld.append("<font color='red'>"
                                    + (hi.getPercentage().equals("") ? "无" : hi.getPercentage())
                                    + "</font>");
                            }
                            assureTemplateOld.append("<br>");
                        }
                        if (ClaueType.ASSURE_TYPE_FORV.equals(hi.getType())
                            || hi.getType().equals(ClaueType.ASSURE_TYPE_FORV)) {
                            assureTemplateOld.append("之前的担保取消修改条款:在");
                            assureTemplateOld.append("入住日期前<font color='red'>"
                                + hi.getFirstDateOrDays() + "</font> 天");
                            assureTemplateOld.append("<font color='red'>" + hi.getFirstTime()
                                + "</font> 时间至");
                            assureTemplateOld.append("<font color='red'>"
                                + hi.getSecondDateOrDays() + "</font> 天");
                            assureTemplateOld.append("<font color='red'>" + hi.getSecondTime()
                                + "</font> 时间");
                            assureTemplateOld.append("<font color='red'>"
                                + this.returnSorceType(hi.getScope()) + "</font> ");
                            assureTemplateOld.append("需要扣取<font color='red'>"
                                + this.returnDeductType(hi.getDeductType()) + "</font>");
                            if ((null != hi.getDeductType() && !hi.getDeductType().equals(""))
                                && null != hi.getPercentage()) {
                                assureTemplateOld.append("<font color='red'>"
                                    + (hi.getPercentage().equals("") ? "无" : hi.getPercentage())
                                    + "</font>");
                            }
                            assureTemplateOld.append("<br>");

                        }
                        if (ClaueType.ASSURE_TYPE_FIVE.equals(hi.getType())
                            || hi.getType().equals(ClaueType.ASSURE_TYPE_FIVE)) {
                            assureTemplateOld.append("之前的担保取消修改条款:");
                            assureTemplateOld.append("当天<font color='red'>" + hi.getFirstTime()
                                + "</font>");
                            assureTemplateOld.append("<font color='red'>"
                                + this.returnAfter(hi.getBeforeOrAfter()) + "</font>");
                            assureTemplateOld.append("<font color='red'>"
                                + this.returnSorceType(hi.getScope()) + "</font>");
                            assureTemplateOld.append("需要扣取<font color='red'>"
                                + this.returnDeductType(hi.getDeductType()) + "</font>");
                            if ((null != hi.getDeductType() && !hi.getDeductType().equals(""))
                                && null != hi.getPercentage()) {
                                assureTemplateOld.append("<font color='red'>"
                                    + (hi.getPercentage().equals("") ? "无" : hi.getPercentage())
                                    + "</font>");
                            }
                            assureTemplateOld.append("<br>");
                        }
                    }
                }
            }

            // 做不为空的校验，否则会报空指针.add by shengwei.zuo hotel 2.6 2009-05-27
            if (null != newha && !newha.getHtlAssureItemEverydayList().isEmpty()) {
                for (int i = 0; i < newha.getHtlAssureItemEverydayList().size(); i++) {
                    HtlAssureItemEveryday hi = (HtlAssureItemEveryday) newha
                        .getHtlAssureItemEverydayList().get(i);
                    if (null != hi.getType()) {

                        if (ClaueType.ASSURE_TYPE_ONE.equals(hi.getType())
                            || hi.getType().equals(ClaueType.ASSURE_TYPE_ONE)) {
                            assureTemplateNew.append("新的担保取消修改条款:");
                            assureTemplateNew.append("凡<font color='red'>"
                                + this.returnSorceType(hi.getScope()) + "</font>");
                            assureTemplateNew.append("均需扣取<font color='red'>"
                                + this.returnDeductType(hi.getDeductType()) + "</font>");
                            if ((null != hi.getDeductType() && !hi.getDeductType().equals(""))
                                && null != hi.getPercentage()) {
                                assureTemplateNew.append("<font color='red'>"
                                    + (hi.getPercentage().equals("") ? "无" : hi.getPercentage())
                                    + "</font>");
                            }
                            assureTemplateNew.append("<br>");
                        }
                        if (ClaueType.ASSURE_TYPE_TWO.equals(hi.getType())
                            || hi.getType().equals(ClaueType.ASSURE_TYPE_TWO)) {
                            assureTemplateNew.append("新的担保取消修改条款:在");
                            assureTemplateNew.append("<font color='red'>" + hi.getFirstDateOrDays()
                                + "</font>" + "日期");
                            assureTemplateNew.append("<font color='red'>" + hi.getFirstTime()
                                + "</font> 时间至");
                            assureTemplateNew.append("<font color='red'>"
                                + hi.getSecondDateOrDays() + "</font> 日期");
                            assureTemplateNew.append("<font color='red'>" + hi.getSecondTime()
                                + "</font> 时间");
                            assureTemplateNew.append("<font color='red'>"
                                + this.returnSorceType(hi.getScope()) + "</font>");
                            assureTemplateNew.append("需扣取<font color='red'>"
                                + this.returnDeductType(hi.getDeductType()) + "</font>");
                            if ((null != hi.getDeductType() && !hi.getDeductType().equals(""))
                                && null != hi.getPercentage()) {
                                assureTemplateNew.append("<font color='red'>"
                                    + (hi.getPercentage().equals("") ? "无" : hi.getPercentage())
                                    + "</font>");
                            }

                            assureTemplateNew.append("<br>");
                        }
                        if (ClaueType.ASSURE_TYPE_THREE.equals(hi.getType())
                            || hi.getType().equals(ClaueType.ASSURE_TYPE_THREE)) {
                            assureTemplateNew.append("新的担保取消修改条款:在");
                            assureTemplateNew.append("<font color='red'>" + hi.getFirstDateOrDays()
                                + "</font> 日期");
                            assureTemplateNew.append("<font color='red'>" + hi.getFirstTime()
                                + "</font> 时间至");
                            assureTemplateNew.append("入住当日<font color='red'>" + hi.getSecondTime()
                                + "</font>时间");
                            assureTemplateNew.append("<font color='red'>"
                                + this.returnSorceType(hi.getScope()) + "</font>");
                            assureTemplateNew.append("需扣取<font color='red'>"
                                + this.returnDeductType(hi.getDeductType()) + "</font>");
                            if ((null != hi.getDeductType() && !hi.getDeductType().equals(""))
                                && null != hi.getPercentage()) {
                                assureTemplateNew.append("<font color='red'>"
                                    + (hi.getPercentage().equals("") ? "无" : hi.getPercentage())
                                    + "</font>");
                            }
                            assureTemplateNew.append("<br>");
                        }
                        if (ClaueType.ASSURE_TYPE_FORV.equals(hi.getType())
                            || hi.getType().equals(ClaueType.ASSURE_TYPE_FORV)) {
                            assureTemplateNew.append("新的担保取消修改条款:在");
                            assureTemplateNew.append("入住日期前<font color='red'>"
                                + hi.getFirstDateOrDays() + "</font> 天");
                            assureTemplateNew.append("<font color='red'>" + hi.getFirstTime()
                                + "</font> 时间至");
                            assureTemplateNew.append("<font color='red'>"
                                + hi.getSecondDateOrDays() + "</font> 天");
                            assureTemplateNew.append("<font color='red'>" + hi.getSecondTime()
                                + "</font> 时间");
                            assureTemplateNew.append("<font color='red'>"
                                + this.returnSorceType(hi.getScope()) + "</font> ");
                            assureTemplateNew.append("需要扣取<font color='red'>"
                                + this.returnDeductType(hi.getDeductType()) + "</font>");
                            if ((null != hi.getDeductType() && !hi.getDeductType().equals(""))
                                && null != hi.getPercentage()) {
                                assureTemplateNew.append("<font color='red'>"
                                    + (hi.getPercentage().equals("") ? "无" : hi.getPercentage())
                                    + "</font>");
                            }
                            assureTemplateNew.append("<br>");

                        }
                        if (ClaueType.ASSURE_TYPE_FIVE.equals(hi.getType())
                            || hi.getType().equals(ClaueType.ASSURE_TYPE_FIVE)) {
                            assureTemplateNew.append("新的担保取消修改条款:");
                            assureTemplateNew.append("当天<font color='red'>" + hi.getFirstTime()
                                + "</font>");
                            assureTemplateNew.append("<font color='red'>"
                                + this.returnAfter(hi.getBeforeOrAfter()) + "</font>");
                            assureTemplateNew.append("<font color='red'>"
                                + this.returnSorceType(hi.getScope()) + "</font>");
                            assureTemplateNew.append("需要扣取<font color='red'>"
                                + this.returnDeductType(hi.getDeductType()) + "</font>");
                            if ((null != hi.getDeductType() && !hi.getDeductType().equals(""))
                                && null != hi.getPercentage()) {
                                assureTemplateNew.append("<font color='red'>"
                                    + (hi.getPercentage().equals("") ? "无" : hi.getPercentage())
                                    + "</font>");
                            }
                            assureTemplateNew.append("<br>");
                        }
                    }
                }

            }

        }

        /**
         * 预付
         */
        if (0 < newhtlPrepayEverydayList.size() && 0 == oldPrepayEverydayList.size()) {
            HtlPrepayEveryday htlPrepayEveryday = (HtlPrepayEveryday) newhtlPrepayEverydayList
                .get(0);

            prepayNewRecord.append("新增加了预付条款,内容为--");
            prepayNewRecord.append("结算方法:<font color='red'>"
                + this.returnPrepay(htlPrepayEveryday.getBalanceType()) + "</font>;");
            prepayNewRecord.append("支付类型:<font color='red'>"
                + this.returnPaymentType(htlPrepayEveryday.getPaymentType()) + "</font>;");
            prepayNewRecord.append("预付金额类型:<font color='red'>"
                + this.returnDeductType(htlPrepayEveryday.getAmountType()) + "</font>;");
            if ((null != htlPrepayEveryday.getAmountType() && !(htlPrepayEveryday.getAmountType())
                .equals(""))
                && null != htlPrepayEveryday.getPrepayDeductType()) {
                prepayNewRecord.append("<font color='red'>"
                    + (htlPrepayEveryday.getPrepayDeductType().equals("") ? "无" : htlPrepayEveryday
                        .getPrepayDeductType()) + "</font> ;");
            }
            if (null != htlPrepayEveryday.getTimeLimitType()) {
                if (htlPrepayEveryday.getTimeLimitType().equals("0")
                    || "0".equals(htlPrepayEveryday.getTimeLimitType())) {
                    prepayNewRecord.append("付款时限日期:<font color='red'>"
                        + DateUtil.dateToString(htlPrepayEveryday.getLimitDate()) + "</font>;");
                    prepayNewRecord.append("付款时限日期时间点:<font color='red'>"
                        + (null == htlPrepayEveryday.getLimitTime()
                            || htlPrepayEveryday.getLimitTime().equals("") ? "无"
                            : htlPrepayEveryday.getLimitTime()) + "</font>; ");
                } else if (htlPrepayEveryday.getTimeLimitType().equals("1")
                    || "1".equals(htlPrepayEveryday.getTimeLimitType())) {
                    prepayNewRecord.append("付款时限提前天数:<font color='red'>"
                        + (null == htlPrepayEveryday.getLimitAheadDays() ? "无" : htlPrepayEveryday
                            .getLimitAheadDays()) + "</font>;");
                    prepayNewRecord.append("付款时限提前天数时间点:<font color='red'>"
                        + (null == htlPrepayEveryday.getLimitAheadDaysTime()
                            || htlPrepayEveryday.getLimitAheadDaysTime().equals("") ? "无"
                            : htlPrepayEveryday.getLimitAheadDaysTime()) + "</font> ;");
                } else if (htlPrepayEveryday.getTimeLimitType().equals("2")
                    || "2".equals(htlPrepayEveryday.getTimeLimitType())) {
                    prepayNewRecord.append("付款时限确认后天数:<font color='red'>"
                        + (null == htlPrepayEveryday.getDaysAfterConfirm() ? "无"
                            : htlPrepayEveryday.getDaysAfterConfirm()) + "</font>;");
                    prepayNewRecord.append("付款时限确认后时间点:<font color='red'>"
                        + (null == htlPrepayEveryday.getTimeAfterConfirm()
                            || htlPrepayEveryday.getTimeAfterConfirm().equals("") ? "无"
                            : htlPrepayEveryday.getTimeAfterConfirm()) + "</font>;");
                }
            }
            prepayNewRecord.append("<br>");

            if (0 < htlPrepayEveryday.getHtlPrepayItemEverydayList().size()) {
                for (int i = 0; i < htlPrepayEveryday.getHtlPrepayItemEverydayList().size(); i++) {
                    HtlPrepayItemEveryday he = (HtlPrepayItemEveryday) htlPrepayEveryday
                        .getHtlPrepayItemEverydayList().get(i);
                    if (null != he.getType()) {
                        if (ClaueType.PREPAY_TYPE_ONE.equals(he.getType())
                            || he.getType().equals(ClaueType.PREPAY_TYPE_ONE)) {
                            prepayTemplateNew.append("预付取消修改条款:");
                            prepayTemplateNew.append("凡<font color='red'>"
                                + this.returnSorceType(he.getScope()) + "</font>");
                            prepayTemplateNew.append("均需扣取<font color='red'>"
                                + this.returnDeductType(he.getDeductType()) + "</font>");
                            if ((null != he.getDeductType() && !he.getDeductType().equals(""))
                                && null != he.getPercentage()) {
                                prepayTemplateNew.append("<font color='red'>"
                                    + (he.getPercentage().equals("") ? "无" : he.getPercentage())
                                    + "</font>");
                            }
                            prepayTemplateNew.append("<br>");
                        }
                        if (ClaueType.PREPAY_TYPE_TWO.equals(he.getType())
                            || he.getType().equals(ClaueType.PREPAY_TYPE_TWO)) {
                            prepayTemplateNew.append("预付取消修改条款:在");
                            prepayTemplateNew.append("<font color='red'>" + he.getFirstDateOrDays()
                                + "</font>" + "日期");
                            prepayTemplateNew.append("<font color='red'>" + he.getFirstTime()
                                + "</font> 时间至");
                            prepayTemplateNew.append("<font color='red'>"
                                + he.getSecondDateOrDays() + "</font> 日期");
                            prepayTemplateNew.append("<font color='red'>" + he.getSecondTime()
                                + "</font> 时间");
                            prepayTemplateNew.append("<font color='red'>"
                                + this.returnSorceType(he.getScope()) + "</font>");
                            prepayTemplateNew.append("需扣取<font color='red'>"
                                + this.returnDeductType(he.getDeductType()) + "</font>");
                            if ((null != he.getDeductType() && !he.getDeductType().equals(""))
                                && null != he.getPercentage()) {
                                prepayTemplateNew.append("<font color='red'>"
                                    + (he.getPercentage().equals("") ? "无" : he.getPercentage())
                                    + "</font>");
                            }

                            prepayTemplateNew.append("<br>");
                        }
                        if (ClaueType.PREPAY_TYPE_THREE.equals(he.getType())
                            || he.getType().equals(ClaueType.PREPAY_TYPE_THREE)) {
                            prepayTemplateNew.append("预付取消修改条款:在");
                            prepayTemplateNew.append("<font color='red'>" + he.getFirstDateOrDays()
                                + "</font> 日期");
                            prepayTemplateNew.append("<font color='red'>" + he.getFirstTime()
                                + "</font> 时间至");
                            prepayTemplateNew.append("入住当日<font color='red'>" + he.getSecondTime()
                                + "</font>时间");
                            prepayTemplateNew.append("<font color='red'>"
                                + this.returnSorceType(he.getScope()) + "</font>");
                            prepayTemplateNew.append("需扣取<font color='red'>"
                                + this.returnDeductType(he.getDeductType()) + "</font>");
                            if ((null != he.getDeductType() && !he.getDeductType().equals(""))
                                && null != he.getPercentage()) {
                                prepayTemplateNew.append("<font color='red'>"
                                    + (he.getPercentage().equals("") ? "无" : he.getPercentage())
                                    + "</font>");
                            }
                            prepayTemplateNew.append("<br>");
                        }
                        if (ClaueType.PREPAY_TYPE_FORV.equals(he.getType())
                            || he.getType().equals(ClaueType.PREPAY_TYPE_FORV)) {
                            prepayTemplateNew.append("预付取消修改条款:在");
                            prepayTemplateNew.append("入住日期前<font color='red'>"
                                + he.getFirstDateOrDays() + "</font> 天");
                            prepayTemplateNew.append("<font color='red'>" + he.getFirstTime()
                                + "</font> 时间至");
                            prepayTemplateNew.append("<font color='red'>"
                                + he.getSecondDateOrDays() + "</font> 天");
                            prepayTemplateNew.append("<font color='red'>" + he.getSecondTime()
                                + "</font> 时间");
                            prepayTemplateNew.append("<font color='red'>"
                                + this.returnSorceType(he.getScope()) + "</font> ");
                            prepayTemplateNew.append("需要扣取<font color='red'>"
                                + this.returnDeductType(he.getDeductType()) + "</font>");
                            if ((null != he.getDeductType() && !he.getDeductType().equals(""))
                                && null != he.getPercentage()) {
                                prepayTemplateNew.append("<font color='red'>"
                                    + (he.getPercentage().equals("") ? "无" : he.getPercentage())
                                    + "</font>");
                            }
                            prepayTemplateNew.append("<br>");

                        }
                        if (ClaueType.PREPAY_TYPE_FIVE.equals(he.getType())
                            || he.getType().equals(ClaueType.PREPAY_TYPE_FIVE)) {
                            prepayTemplateNew.append("预付取消修改条款:");
                            prepayTemplateNew.append("当天<font color='red'>" + he.getFirstTime()
                                + "</font>");
                            prepayTemplateNew.append("<font color='red'>"
                                + this.returnAfter(he.getBeforeOrAfter()) + "</font>");
                            prepayTemplateNew.append("<font color='red'>"
                                + this.returnSorceType(he.getScope()) + "</font>");
                            prepayTemplateNew.append("需要扣取<font color='red'>"
                                + this.returnDeductType(he.getDeductType()) + "</font>");
                            if ((null != he.getDeductType() && !he.getDeductType().equals(""))
                                && null != he.getPercentage()) {
                                prepayTemplateNew.append("<font color='red'>"
                                    + (he.getPercentage().equals("") ? "无" : he.getPercentage())
                                    + "</font>");
                            }
                            prepayTemplateNew.append("<br>");
                        }
                    }
                }

            }
        }

        if (0 == newhtlPrepayEverydayList.size() && 0 < oldPrepayEverydayList.size()) {
            prepayNewRecord.append("把之前的预付条款删除了");
        }
        if (0 < newhtlPrepayEverydayList.size() && 0 < oldPrepayEverydayList.size()) {
            HtlPrepayEveryday newhtlPrepay = (HtlPrepayEveryday) newhtlPrepayEverydayList.get(0);
            HtlPrepayEveryday oldhtlPrepay = (HtlPrepayEveryday) oldPrepayEverydayList.get(0);

            prepayOldRecord.append("之前的预付条款,内容为--");
            prepayOldRecord.append("结算方法:<font color='red'>"
                + this.returnPrepay(oldhtlPrepay.getBalanceType()) + "</font>;");
            prepayOldRecord.append("支付类型:<font color='red'>"
                + this.returnPaymentType(oldhtlPrepay.getPaymentType()) + "</font>;");
            prepayOldRecord.append("预付金额类型:<font color='red'>"
                + this.returnDeductType(oldhtlPrepay.getAmountType()) + "</font>;");
            if ((null != oldhtlPrepay.getAmountType() && !oldhtlPrepay.getAmountType().equals(""))
                && null != oldhtlPrepay.getPrepayDeductType()) {
                prepayOldRecord.append("<font color='red'>"
                    + (oldhtlPrepay.getPrepayDeductType().equals("") ? "无" : oldhtlPrepay
                        .getPrepayDeductType()) + "</font>; ");
            }
            if (null != oldhtlPrepay.getTimeLimitType()) {
                if (oldhtlPrepay.getTimeLimitType().equals("0")
                    || "0".equals(oldhtlPrepay.getTimeLimitType())) {
                    prepayOldRecord.append("付款时限日期:<font color='red'>"
                        + DateUtil.dateToString(oldhtlPrepay.getLimitDate()) + "</font>; ");
                    prepayOldRecord.append("付款时限日期时间点:<font color='red'>"
                        + (null == oldhtlPrepay.getLimitTime()
                            || oldhtlPrepay.getLimitTime().equals("") ? "无" : oldhtlPrepay
                            .getLimitTime()) + "</font>; ");
                } else if (oldhtlPrepay.getTimeLimitType().equals("1")
                    || "1".equals(oldhtlPrepay.getTimeLimitType())) {
                    prepayOldRecord.append("付款时限提前天数:<font color='red'>"
                        + (null == oldhtlPrepay.getLimitAheadDays() ? "无" : oldhtlPrepay
                            .getLimitAheadDays()) + "</font>; ");
                    prepayOldRecord.append("付款时限提前天数时间点:<font color='red'>"
                        + (null == oldhtlPrepay.getLimitAheadDaysTime()
                            || oldhtlPrepay.getLimitAheadDaysTime().equals("") ? "无" : oldhtlPrepay
                            .getLimitAheadDaysTime()) + "</font>; ");
                } else if (oldhtlPrepay.getTimeLimitType().equals("2")
                    || "2".equals(oldhtlPrepay.getTimeLimitType())) {
                    prepayOldRecord.append("付款时限确认后天数:<font color='red'>"
                        + (null == oldhtlPrepay.getDaysAfterConfirm() ? "无" : oldhtlPrepay
                            .getDaysAfterConfirm()) + "</font>;");
                    prepayOldRecord.append("付款时限确认后时间点:<font color='red'>"
                        + (null == oldhtlPrepay.getTimeAfterConfirm()
                            || oldhtlPrepay.getTimeAfterConfirm().equals("") ? "无" : oldhtlPrepay
                            .getTimeAfterConfirm()) + "</font>;");
                }
            }
            prepayOldRecord.append("<br>");

            prepayNewRecord.append("修改后的预付条款,内容为--");
            prepayNewRecord.append("结算方法:<font color='red'>"
                + this.returnPrepay(newhtlPrepay.getBalanceType()) + "</font>;");
            prepayNewRecord.append("支付类型:<font color='red'>"
                + this.returnPaymentType(newhtlPrepay.getPaymentType()) + "</font>;");
            prepayNewRecord.append("预付金额类型:<font color='red'>"
                + this.returnDeductType(newhtlPrepay.getAmountType()) + "</font>;");
            if ((null != newhtlPrepay.getAmountType() && !newhtlPrepay.getAmountType().equals(""))
                && null != newhtlPrepay.getPrepayDeductType()) {
                prepayNewRecord.append("<font color='red'>"
                    + (newhtlPrepay.getPrepayDeductType().equals("") ? "无" : newhtlPrepay
                        .getPrepayDeductType()) + "</font> ;");
            }
            if (null != newhtlPrepay.getTimeLimitType()) {
                if (newhtlPrepay.getTimeLimitType().equals("0")
                    || "0".equals(newhtlPrepay.getTimeLimitType())) {
                    prepayNewRecord.append("付款时限日期:<font color='red'>"
                        + DateUtil.dateToString(newhtlPrepay.getLimitDate()) + "</font>;");
                    prepayNewRecord.append("付款时限日期时间点:<font color='red'>"
                        + (null == newhtlPrepay.getLimitTime()
                            || newhtlPrepay.getLimitTime().equals("") ? "无" : newhtlPrepay
                            .getLimitTime()) + "</font>; ");
                } else if (newhtlPrepay.getTimeLimitType().equals("1")
                    || "1".equals(newhtlPrepay.getTimeLimitType())) {
                    prepayNewRecord.append("付款时限提前天数:<font color='red'>"
                        + (null == newhtlPrepay.getLimitAheadDays() ? "无" : newhtlPrepay
                            .getLimitAheadDays()) + "</font>;");
                    prepayNewRecord.append("付款时限提前天数时间点:<font color='red'>"
                        + (null == newhtlPrepay.getLimitAheadDaysTime()
                            || newhtlPrepay.getLimitAheadDaysTime().equals("") ? "无" : newhtlPrepay
                            .getLimitAheadDaysTime()) + "</font>; ");
                } else if (newhtlPrepay.getTimeLimitType().equals("2")
                    || "2".equals(newhtlPrepay.getTimeLimitType())) {
                    prepayNewRecord.append("付款时限确认后天数:<font color='red'>"
                        + (null == newhtlPrepay.getDaysAfterConfirm() ? "无" : newhtlPrepay
                            .getDaysAfterConfirm()) + "</font>;");
                    prepayNewRecord.append("付款时限确认后时间点:<font color='red'>"
                        + (null == newhtlPrepay.getTimeAfterConfirm()
                            || newhtlPrepay.getTimeAfterConfirm().equals("") ? "无" : newhtlPrepay
                            .getTimeAfterConfirm()) + "</font>;");
                }
            }
            prepayNewRecord.append("<br>");

            // 只有预付取消修改条款有值的情况下设置
            if (0 < newhtlPrepay.getHtlPrepayItemEverydayList().size()) {
                for (int i = 0; i < newhtlPrepay.getHtlPrepayItemEverydayList().size(); i++) {
                    HtlPrepayItemEveryday hp = (HtlPrepayItemEveryday) newhtlPrepay
                        .getHtlPrepayItemEverydayList().get(i);
                    if (null != hp.getType()) {
                        if (ClaueType.PREPAY_TYPE_ONE.equals(hp.getType())
                            || hp.getType().equals(ClaueType.PREPAY_TYPE_ONE)) {
                            prepayTemplateNew.append("新的预付取消修改条款:");
                            prepayTemplateNew.append("凡<font color='red'>"
                                + this.returnSorceType(hp.getScope()) + "</font>");
                            prepayTemplateNew.append("均需扣取<font color='red'>"
                                + this.returnDeductType(hp.getDeductType()) + "</font>");
                            if ((null != hp.getDeductType() && !hp.getDeductType().equals(""))
                                && null != hp.getPercentage()) {
                                prepayTemplateNew.append("<font color='red'>"
                                    + (hp.getPercentage().equals("") ? "无" : hp.getPercentage())
                                    + "</font>");
                            }
                            prepayTemplateNew.append("<br>");
                        }
                        if (ClaueType.PREPAY_TYPE_TWO.equals(hp.getType())
                            || hp.getType().equals(ClaueType.PREPAY_TYPE_TWO)) {
                            prepayTemplateNew.append("新的预付取消修改条款:在");
                            prepayTemplateNew.append("<font color='red'>" + hp.getFirstDateOrDays()
                                + "</font>" + "日期");
                            prepayTemplateNew.append("<font color='red'>" + hp.getFirstTime()
                                + "</font> 时间至");
                            prepayTemplateNew.append("<font color='red'>"
                                + hp.getSecondDateOrDays() + "</font> 日期");
                            prepayTemplateNew.append("<font color='red'>" + hp.getSecondTime()
                                + "</font> 时间");
                            prepayTemplateNew.append("<font color='red'>"
                                + this.returnSorceType(hp.getScope()) + "</font>");
                            prepayTemplateNew.append("需扣取<font color='red'>"
                                + this.returnDeductType(hp.getDeductType()) + "</font>");
                            if ((null != hp.getDeductType() && !hp.getDeductType().equals(""))
                                && null != hp.getPercentage()) {
                                prepayTemplateNew.append("<font color='red'>"
                                    + (hp.getPercentage().equals("") ? "无" : hp.getPercentage())
                                    + "</font>");
                            }

                            prepayTemplateNew.append("<br>");
                        }
                        if (ClaueType.PREPAY_TYPE_THREE.equals(hp.getType())
                            || hp.getType().equals(ClaueType.PREPAY_TYPE_THREE)) {
                            prepayTemplateNew.append("新的预付取消修改条款:在");
                            prepayTemplateNew.append("<font color='red'>" + hp.getFirstDateOrDays()
                                + "</font> 日期");
                            prepayTemplateNew.append("<font color='red'>" + hp.getFirstTime()
                                + "</font> 时间至");
                            prepayTemplateNew.append("入住当日<font color='red'>" + hp.getSecondTime()
                                + "</font>时间");
                            prepayTemplateNew.append("<font color='red'>"
                                + this.returnSorceType(hp.getScope()) + "</font>");
                            prepayTemplateNew.append("需扣取<font color='red'>"
                                + this.returnDeductType(hp.getDeductType()) + "</font>");
                            if ((null != hp.getDeductType() && !hp.getDeductType().equals(""))
                                && null != hp.getPercentage()) {
                                prepayTemplateNew.append("<font color='red'>"
                                    + (hp.getPercentage().equals("") ? "无" : hp.getPercentage())
                                    + "</font>");
                            }
                            prepayTemplateNew.append("<br>");
                        }
                        if (ClaueType.PREPAY_TYPE_FORV.equals(hp.getType())
                            || hp.getType().equals(ClaueType.PREPAY_TYPE_FORV)) {
                            prepayTemplateNew.append("新的预付取消修改条款:在");
                            prepayTemplateNew.append("入住日期前<font color='red'>"
                                + hp.getFirstDateOrDays() + "</font> 天");
                            prepayTemplateNew.append("<font color='red'>" + hp.getFirstTime()
                                + "</font> 时间至");
                            prepayTemplateNew.append("<font color='red'>"
                                + hp.getSecondDateOrDays() + "</font> 天");
                            prepayTemplateNew.append("<font color='red'>" + hp.getSecondTime()
                                + "</font> 时间");
                            prepayTemplateNew.append("<font color='red'>"
                                + this.returnSorceType(hp.getScope()) + "</font> ");
                            prepayTemplateNew.append("需要扣取<font color='red'>"
                                + this.returnDeductType(hp.getDeductType()) + "</font>");
                            if ((null != hp.getDeductType() && !hp.getDeductType().equals(""))
                                && null != hp.getPercentage()) {
                                prepayTemplateNew.append("<font color='red'>"
                                    + (hp.getPercentage().equals("") ? "无" : hp.getPercentage())
                                    + "</font>");
                            }
                            prepayTemplateNew.append("<br>");

                        }
                        if (ClaueType.PREPAY_TYPE_FIVE.equals(hp.getType())
                            || hp.getType().equals(ClaueType.PREPAY_TYPE_FIVE)) {
                            prepayTemplateNew.append("新的预付取消修改条款:");
                            prepayTemplateNew.append("当天<font color='red'>" + hp.getFirstTime()
                                + "</font>");
                            prepayTemplateNew.append("<font color='red'>"
                                + this.returnAfter(hp.getBeforeOrAfter()) + "</font>");
                            prepayTemplateNew.append("<font color='red'>"
                                + this.returnSorceType(hp.getScope()) + "</font>");
                            prepayTemplateNew.append("需要扣取<font color='red'>"
                                + this.returnDeductType(hp.getDeductType()) + "</font>");
                            if ((null != hp.getDeductType() && !hp.getDeductType().equals(""))
                                && null != hp.getPercentage()) {
                                prepayTemplateNew.append("<font color='red'>"
                                    + (hp.getPercentage().equals("") ? "无" : hp.getPercentage())
                                    + "</font>");
                            }
                            prepayTemplateNew.append("<br>");
                        }
                    }
                }
            }

            if (0 < oldhtlPrepay.getHtlPrepayItemEverydayList().size()) {
                for (int i = 0; i < oldhtlPrepay.getHtlPrepayItemEverydayList().size(); i++) {
                    HtlPrepayItemEveryday hp = (HtlPrepayItemEveryday) oldhtlPrepay
                        .getHtlPrepayItemEverydayList().get(i);
                    if (null != hp.getType()) {
                        if (ClaueType.PREPAY_TYPE_ONE.equals(hp.getType())
                            || hp.getType().equals(ClaueType.PREPAY_TYPE_ONE)) {
                            prepayTemplateOld.append("之前的预付取消修改条款:");
                            prepayTemplateOld.append("凡<font color='red'>"
                                + this.returnSorceType(hp.getScope()) + "</font>");
                            prepayTemplateOld.append("均需扣取<font color='red'>"
                                + this.returnDeductType(hp.getDeductType()) + "</font>");
                            if ((null != hp.getDeductType() && !hp.getDeductType().equals(""))
                                && null != hp.getPercentage()) {
                                prepayTemplateOld.append("<font color='red'>"
                                    + (hp.getPercentage().equals("") ? "无" : hp.getPercentage())
                                    + "</font>");
                            }
                            prepayTemplateOld.append("<br>");
                        }
                        if (ClaueType.PREPAY_TYPE_TWO.equals(hp.getType())
                            || hp.getType().equals(ClaueType.PREPAY_TYPE_TWO)) {
                            prepayTemplateOld.append("之前的预付取消修改条款:在");
                            prepayTemplateOld.append("<font color='red'>" + hp.getFirstDateOrDays()
                                + "</font>" + "日期");
                            prepayTemplateOld.append("<font color='red'>" + hp.getFirstTime()
                                + "</font> 时间至");
                            prepayTemplateOld.append("<font color='red'>"
                                + hp.getSecondDateOrDays() + "</font> 日期");
                            prepayTemplateOld.append("<font color='red'>" + hp.getSecondTime()
                                + "</font> 时间");
                            prepayTemplateOld.append("<font color='red'>"
                                + this.returnSorceType(hp.getScope()) + "</font>");
                            prepayTemplateOld.append("需扣取<font color='red'>"
                                + this.returnDeductType(hp.getDeductType()) + "</font>");
                            if ((null != hp.getDeductType() && !hp.getDeductType().equals(""))
                                && null != hp.getPercentage()) {
                                prepayTemplateOld.append("<font color='red'>"
                                    + (hp.getPercentage().equals("") ? "无" : hp.getPercentage())
                                    + "</font>");
                            }

                            prepayTemplateOld.append("<br>");
                        }
                        if (ClaueType.PREPAY_TYPE_THREE.equals(hp.getType())
                            || hp.getType().equals(ClaueType.PREPAY_TYPE_THREE)) {
                            prepayTemplateOld.append("之前的预付取消修改条款:在");
                            prepayTemplateOld.append("<font color='red'>" + hp.getFirstDateOrDays()
                                + "</font> 日期");
                            prepayTemplateOld.append("<font color='red'>" + hp.getFirstTime()
                                + "</font> 时间至");
                            prepayTemplateOld.append("入住当日<font color='red'>" + hp.getSecondTime()
                                + "</font>时间");
                            prepayTemplateOld.append("<font color='red'>"
                                + this.returnSorceType(hp.getScope()) + "</font>");
                            prepayTemplateOld.append("需扣取<font color='red'>"
                                + this.returnDeductType(hp.getDeductType()) + "</font>");
                            if ((null != hp.getDeductType() && !hp.getDeductType().equals(""))
                                && null != hp.getPercentage()) {
                                prepayTemplateOld.append("<font color='red'>"
                                    + (hp.getPercentage().equals("") ? "无" : hp.getPercentage())
                                    + "</font>");
                            }
                            prepayTemplateOld.append("<br>");
                        }
                        if (ClaueType.PREPAY_TYPE_FORV.equals(hp.getType())
                            || hp.getType().equals(ClaueType.PREPAY_TYPE_FORV)) {
                            prepayTemplateOld.append("之前的预付取消修改条款:在");
                            prepayTemplateOld.append("入住日期前<font color='red'>"
                                + hp.getFirstDateOrDays() + "</font> 天");
                            prepayTemplateOld.append("<font color='red'>" + hp.getFirstTime()
                                + "</font> 时间至");
                            prepayTemplateOld.append("<font color='red'>"
                                + hp.getSecondDateOrDays() + "</font> 天");
                            prepayTemplateOld.append("<font color='red'>" + hp.getSecondTime()
                                + "</font> 时间");
                            prepayTemplateOld.append("<font color='red'>"
                                + this.returnSorceType(hp.getScope()) + "</font> ");
                            prepayTemplateOld.append("需要扣取<font color='red'>"
                                + this.returnDeductType(hp.getDeductType()) + "</font>");
                            if ((null != hp.getDeductType() && !hp.getDeductType().equals(""))
                                && null != hp.getPercentage()) {
                                prepayTemplateOld.append("<font color='red'>"
                                    + (hp.getPercentage().equals("") ? "无" : hp.getPercentage())
                                    + "</font>");
                            }
                            prepayTemplateOld.append("<br>");

                        }
                        if (ClaueType.PREPAY_TYPE_FIVE.equals(hp.getType())
                            || hp.getType().equals(ClaueType.PREPAY_TYPE_FIVE)) {
                            prepayTemplateOld.append("之前的预付取消修改条款:");
                            prepayTemplateOld.append("当天<font color='red'>" + hp.getFirstTime()
                                + "</font>");
                            System.out
                                .println("********之前的预付取消修改条款:是否是之后？" + hp.getBeforeOrAfter());
                            prepayTemplateOld.append("<font color='red'>"
                                + this.returnAfter(hp.getBeforeOrAfter()) + "</font>");
                            prepayTemplateOld.append("<font color='red'>"
                                + this.returnSorceType(hp.getScope()) + "</font>");
                            prepayTemplateOld.append("需要扣取<font color='red'>"
                                + this.returnDeductType(hp.getDeductType()) + "</font>");
                            if ((null != hp.getDeductType() && !hp.getDeductType().equals(""))
                                && null != hp.getPercentage()) {
                                prepayTemplateOld.append("<font color='red'>"
                                    + (hp.getPercentage().equals("") ? "无" : hp.getPercentage())
                                    + "</font>");
                            }
                            prepayTemplateOld.append("<br>");
                        }
                    }
                }
            }

        }

        HtlModifRecord htlModifRecord = new HtlModifRecord();// 新创建操作明细对象
        htlModifRecord.setHotelId(newPreconcertItem.getHotelId());// 设置酒店ID
        htlModifRecord.setPricetypeId(newPreconcertItem.getPriceTypeId());// 设置价格类型ID
        htlModifRecord.setRoomDate(DateUtil.getDate(newPreconcertItem.getValidDt()));// 设置当天时间
        htlModifRecord.setRecordId(id);// 设置操作日志表id

        // 设置担保的明细
        htlModifRecord.setAssureNewRecord(assureNewRecord.toString());
        htlModifRecord.setAssureOldRecord(assureOldRecord.toString());
        htlModifRecord.setAssureTemplateNew(assureTemplateNew.toString());
        htlModifRecord.setAssureTemplateOld(assureTemplateOld.toString());

        // 设置预付的明细
        htlModifRecord.setPrepayNewRecord(prepayNewRecord.toString());
        htlModifRecord.setPrepayOldRecord(prepayOldRecord.toString());
        htlModifRecord.setPrepayTemplateNew(prepayTemplateNew.toString());
        htlModifRecord.setPrepayTemplateOld(prepayTemplateOld.toString());

        htlModifRecord.setModifId(newPreconcertItem.getModifyById());// 设置修改人ID
        htlModifRecord.setModifDate(newPreconcertItem.getModifyTime());// 设置修改时间

        // 设置预订的明细
        htlModifRecord.setReservationNewRecord(reservationNewRecord.toString());
        htlModifRecord.setReservationOldRecord(reservationOldRecord.toString());
        super.save(htlModifRecord);
    }

    // /**
    // * 判断String返回null的，
    // */
    // public String returnNull(String isNull){
    // return isNull==null ? "无" : isNull;
    // }
    //	
    // /**
    // * 判断Long类型返回null
    // */
    // public Long returnLong(Long isNull){
    // return isNull==null ? "无" : isNull;
    // }
    //	

    /**
     * 返回担保类型的
     * 
     * @param Sorce
     * @return
     */
    public String returnSorce(String Sorce) {
        String SorceStr = "";
        if (null != Sorce && !Sorce.equals("")) {
            if ("2".equals(Sorce) || Sorce.equals("2")) {
                SorceStr = "首日担保";
            } else if ("4".equals(Sorce) || Sorce.equals("4")) {
                SorceStr = "全额担保";
            }
        } else {
            return "无";
        }
        return SorceStr;
    }

    /**
     * 返回扣款金额类型的
     * 
     * @param DeductType
     * @return
     */
    public String returnDeductType(String DeductType) {
        String DeductTypeStr = "";
        if (null != DeductType && !DeductType.equals("")) {
            if ("1".equals(DeductType) || DeductType.equals("1")) {
                DeductTypeStr = "首日金额";
            } else if ("2".equals(DeductType) || DeductType.equals("2")) {
                DeductTypeStr = "全额金额";
            } else if ("4".equals(DeductType) || DeductType.equals("4")) {
                DeductTypeStr = "首日金额百分比";
            } else if ("6".equals(DeductType) || DeductType.equals("6")) {
                DeductTypeStr = "全额金额百分比";
            }
        } else {
            return "无";
        }
        return DeductTypeStr;
    }

    /**
     * 返回修改取消范围的
     * 
     * @param Sorce
     * @return
     */
    public String returnSorceType(String Sorce) {
        String SorceStr = "";
        if (null != Sorce && !Sorce.equals("")) {
            if ("1".equals(Sorce) || Sorce.equals("1")) {
                SorceStr = "取消";
            } else if ("2".equals(Sorce) || Sorce.equals("2")) {
                SorceStr = "修改";
            } else if ("3".equals(Sorce) || Sorce.equals("3")) {
                SorceStr = "取消或修改";
            }
        } else {
            SorceStr = "无";
        }
        return SorceStr;
    }

    /**
     * 返回支付方式的
     * 
     * @param Prepay
     * @return
     */
    public String returnPrepay(String Prepay) {
        String PrepayStr = "";
        if (null != Prepay && !Prepay.equals("")) {
            if ("02".equals(Prepay) || Prepay.equals("02")) {
                PrepayStr = "COA客人到之前";
            } else if ("04".equals(Prepay) || Prepay.equals("04")) {
                PrepayStr = "SEND BILL 月结房费";
            }
        } else {
            PrepayStr = "无";
        }
        return PrepayStr;
    }

    /**
     * 返回结算方式的
     * 
     * @param PaymentType
     * @return
     */
    public String returnPaymentType(String PaymentType) {
        String PaymentTypeStr = "";
        if (null != PaymentType && !PaymentType.equals("")) {
            if ("1".equals(PaymentType) || PaymentType.equals("1")) {
                PaymentTypeStr = "底价支付酒店";
            } else if ("2".equals(PaymentType) || PaymentType.equals("2")) {
                PaymentTypeStr = "售价支付酒店";
            }
        } else {
            PaymentTypeStr = "无";
        }
        return PaymentTypeStr;
    }

    /**
     * 返回之后
     * 
     * @param PaymentType
     * @return
     */
    public String returnAfter(String afterType) {

        String AfterStr = "";
        if (null != afterType && !afterType.equals("")) {
            if ("2".equals(afterType) || afterType.equals("2")) {
                AfterStr = "之后";
            }
        } else {
            AfterStr = "无";
        }
        return AfterStr;

    }

   

    /**
     * 查询指定城市某日期内的会展信息 add by juesu.chen
     * 
     * @param city
     *            城市
     * @param exhibitStartDate
     *            会展开始日期
     * @param exhibitEndDate
     *            会展结束日期
     * @return
     */
    public List<HtlExhibit> queryExhibit(String city, Date exhibitStartDate, Date exhibitEndDate) {
        // 组装查询参数
        Map<String, String> params = new HashMap<String, String>();
        params.put("city", city);
        params.put("exhibitStartDate", DateUtil.dateToString(exhibitStartDate));
        params.put("exhibitEndDate", DateUtil.dateToString(exhibitEndDate));
        return queryDao.queryForList("queryExhibit", params);
    }

   

   

    public ClauseManage getClauseManage() {
        return clauseManage;
    }

    public void setClauseManage(ClauseManage clauseManage) {
        this.clauseManage = clauseManage;
    }

    /**
     * add by shengwei.zuo hotel 2.9.2 根据酒店ID,价格类型和起始日期查询出提示信息
     * modify by alfred.
     */
    public String queryAlertInfoStr(long hotelid, String priceTypeId, Date begin, Date end,
        String ccOrweb) {

        StringBuilder alertinfo = new StringBuilder();
        
        List<DateComponent> dateLst = new ArrayList<DateComponent>();
        
        if(begin != null && end != null) {
        	
            List<Date> lstDate = DateUtil.getDates(begin, end);
        	
        	//查询出所有涵盖预订日期的提示信息
        	List<HtlAlerttypeInfo> alertInfoList = getAinfoByIdDate(hotelid, priceTypeId, begin, end, ccOrweb);
        	
        	if(alertInfoList != null) {
        		
        		for(int i=0;i<alertInfoList.size();i++) {
        			
        			HtlAlerttypeInfo erverDayAlertInfo = alertInfoList.get(i);
        			
        			String everyWweek = erverDayAlertInfo.getWeek();
        			
        			if(everyWweek != null && !"".equals(everyWweek)) {
        				
        				everyWweek = everyWweek.substring(0, everyWweek.length() - 1);
        				
        				// 拆分星期
                        String[] weekArray = everyWweek.split(",");
                        
                        boolean isWeekDate = false;
                        
                        //判断该条提示信息中的星期是否包含入住日期的一天，就添加到提示信息中去
                        for(int j=0;j<lstDate.size();j++) {
                        	
                        	Date tmpDate = (Date)lstDate.get(j);
                        	
                        	if(DateUtil.isMatchWeek(tmpDate, weekArray)) {
                        		
                        		alertinfo.append(DateUtil.dateToString(erverDayAlertInfo.getBeginDate()) + "至"
                                        + DateUtil.dateToString(erverDayAlertInfo.getEndDate()) + ":" + erverDayAlertInfo.getAlerttypeInfo());
                        		
                        		alertinfo.append("<br>");
                        		
                        		break;
                        	}
                        }
        			}
        		}
        	}
        }

        return alertinfo.toString();
    }

    /**
     * add by shengwei.zuo 2009-08-17 hotel 2.9.2 根据酒店ID，子房型ID，和日期，得到对应的提示信息对象
     * modify by alfred
     * 
     */
    public List<HtlAlerttypeInfo> getAinfoByIdDate(long hotelid, String priceTypeId, Date begin, Date end,
            String ccORweb) {
        	
        	String hql = "from HtlAlerttypeInfo a where a.hotelId= ? and a.priceTypeId = ? and not exists ( from HtlAlerttypeInfo b where (b.beginDate >= ? or b.endDate < ?) and a.id = b.id ) and a.localFlag <> ? ";

            Object[] obj = new Object[] { hotelid, priceTypeId, end, begin, ccORweb };
            
            List<HtlAlerttypeInfo> list = super.query(hql, obj);
            
            return list;

        }
    
    
    /**
     * add by zhijie.gu 2009-09-15 hotel2.9.3 改变在优惠条款内的价格
     */
	private int changePriceForFavourable(List roomInfoLis,RoomInfo roomInfos,long hotelId,
	        String childRoomTypeId,int y, int f,
	        Map<String,List<HtlFavourableclause>> favourableMap){
			
    		//连住优惠过程量
    		RoomInfo roomInfosFav = new RoomInfo();
//	    	List<HtlFavourableclause> htlFavourableclauseList = this.getHtlFavourableClause(hotelId,Long.parseLong(childRoomTypeId));
    		//原来多次访问数据库的方式，改为在Map中获取需要的信息
	    	List<HtlFavourableclause> htlFavourableclauseList =  favourableMap.get(hotelQueryDao.generateFavourableListKey(hotelId, Long.valueOf(childRoomTypeId)));
	        //连住优惠对价格处理
	    	List lis =new ArrayList();
	    	if(null !=htlFavourableclauseList){
	        		for(int i= 0 ;i<htlFavourableclauseList.size();i++){
	        			HtlFavourableclause htlFavourableclause=htlFavourableclauseList.get(i);
	        			
	        			if("2".equals(htlFavourableclause.getFavourableType())){
	        				if(!roomInfos.getFellowDate().before(htlFavourableclause.getBeginDate()) 
	        						&& !roomInfos.getFellowDate().after(htlFavourableclause.getEndDate())){
	        					List<HtlFavouraParameter> lstPackagerate = htlFavourableclause.getLstPackagerate();
	        					if(null != lstPackagerate){
	        						for(int j = 0; j < lstPackagerate.size();j++ ){
	        						
	        							BigDecimal b = new BigDecimal(""+lstPackagerate.get(j).getDiscount()*roomInfos.getSalePrice()/10);    
	        	    					//逢一进十
	        	    					if(1 == lstPackagerate.get(j).getDecimalPointType()){
	        	    						roomInfos.setSalePrice(Math.ceil(b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()));
		            					}
	        	    					//四舍五入取一位小数
	        	    					if(2 == lstPackagerate.get(j).getDecimalPointType()){
	        	            				roomInfos.setSalePrice(b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue()) ;
	        	    					}
	        	    					//直接取整
	        	    					if(3 == lstPackagerate.get(j).getDecimalPointType()){
	        	    						roomInfos.setSalePrice(Math.floor(lstPackagerate.get(j).getDiscount()*roomInfos.getSalePrice()/10));
	        	    					}
	        	    					//四舍五入取整
	        	    					if(4 == lstPackagerate.get(j).getDecimalPointType()){
	        	    						roomInfos.setSalePrice(b.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue()) ;
	        	    					}
	        						
	        						}
	        						
	        					}
	        					
	        				}
	        				
	        			}
	        			if("3".equals(htlFavourableclause.getFavourableType())){
	        				if(!roomInfos.getFellowDate().before(htlFavourableclause.getBeginDate()) 
	        						&& !roomInfos.getFellowDate().after(htlFavourableclause.getEndDate())){
	        					List<HtlFavouraParameter> lstPackagerate = htlFavourableclause.getLstPackagerate();
	        	    			if(null != lstPackagerate){
	        	    				for(int j = 0; j < lstPackagerate.size();j++ ){
	        	        				if(f == lstPackagerate.get(j).getPackagerateNight()-1){
	        	        					for (int a = y-(int)(lstPackagerate.get(j).getPackagerateNight()-1); a <= y; a++ ){
	        	        						if(a == y-(int)(lstPackagerate.get(j).getPackagerateNight()-1)){
	        	        							roomInfosFav =(RoomInfo)roomInfoLis.get(a);
	        	        							roomInfosFav.setSalePrice(lstPackagerate.get(j).getPackagerateSaleprice());
	        	        						}else{
	        	        							roomInfosFav =(RoomInfo)roomInfoLis.get(a);
	        	        							roomInfosFav.setSalePrice(99999.0);
	        	        						}
	        	        					}
	        	        					f=0;
	        	        				}else{
	        	        					f++;
	        	        				}
	        	    				}	
	        	    			}
	        					
	        				}
	        				
	        			}
	        			if("1".equals(htlFavourableclause.getFavourableType())){
	        				if(!roomInfos.getFellowDate().before(htlFavourableclause.getBeginDate()) 
	        	    					&& !roomInfos.getFellowDate().after(htlFavourableclause.getEndDate())){
	        					List<HtlFavouraParameter> lstPackagerate = htlFavourableclause.getLstPackagerate();
	        	    			if(null != lstPackagerate){
	        	    				for(int j = 0; j < lstPackagerate.size();j++ ){
	        	    					int ConNumD = (int)(lstPackagerate.get(j).getContinueNight()+0);
	        	    					int doNumD = (int)(lstPackagerate.get(j).getDonateNight()+0);
	        	    					List<HtlEveningsRent> lstEveningsRent= lstPackagerate.get(j).getLstEveningsRent();
	        	    					int NumDay = ConNumD+doNumD;
	        	        				if(f == NumDay-1){
	        	        					for(int aa =0;aa<lstEveningsRent.size();aa++){
	        	        						int night =(int)(lstEveningsRent.get(aa).getNight()+0);
	        	        						roomInfosFav =(RoomInfo)roomInfoLis.get(y-(NumDay-1)+(night-1));
	        	        						if(0 == (int)(lstEveningsRent.get(aa).getSalePrice()+0)){
	        	        							roomInfosFav.setSalePrice(99999.0);
	    	        							}else{
	    	        								roomInfosFav.setSalePrice(lstEveningsRent.get(aa).getSalePrice());
	    	        							}
	        	        					}
	        	        					f=0;
	        	        					if(2 == lstPackagerate.get(j).getCirculateType()){
	        	    							f = 9999;
	        	    						}
	        	            			}else{
	        	            					f++;
	        	            			}
	        	        			}	
	        	        		}
	        					
	        				}
	        				
	        			}
	        			
	        		}
	        		
	        	}
	        	
			return f;
			
	}
	
	
	//获取价格表信息
	public HtlPrice qryHtlPriceForCC(long childRoomId, Date date, 
	        String payMethod, String quotaType) {
	        String hsql = "FROM HtlPrice WHERE able_sale_date = ? "
	            + "AND child_room_type_id =? AND pay_method =? ";
	        Object[] obj = new Object[] { date, childRoomId, payMethod};
	        return (HtlPrice) super.find(hsql, obj);
	}
	
	/**
     * 封装所有需要再一次返回数据库的参数
     * add by chenjiajie 2010-05-06
     * @param results 调用pkg返回的List
     * @return
     */
    private Map getAllParamsForDB(List<HotelResultInfo> results){
        Map paramsMap = new HashMap();
        StringBuilder allHotelList = new StringBuilder(); //记录本次查询结果所有的酒店id,用,分隔
        StringBuilder allPriceTypeList = new StringBuilder(); //记录本次查询结果所有的价格类型id,用,分隔
        StringBuilder benefitPriceTypeList = new StringBuilder(); //记录本次查询结果有优惠立减的价格类型id,用,分隔
        StringBuilder cashReturnPriceTypeList = new StringBuilder(); //记录本次查询结果有优惠立减的价格类型id,用,分隔
        for (Iterator it = results.iterator(); it.hasNext();) {
            HotelResultInfo queryHotelResult = (HotelResultInfo) it.next();
            
            /* 封装本次查询结果所有的酒店ID begin */
            String hotelId = String.valueOf(queryHotelResult.getHotelId());
            if(StringUtil.isValidStr(hotelId)
                    && allHotelList.indexOf(hotelId) < 0){
                allHotelList.append(hotelId);
                if(it.hasNext()){
                    allHotelList.append(",");
                }
            }
            /* 封装本次查询结果所有的酒店ID end */
            
            String priceTypeId = queryHotelResult.getChildRoomTypeId();
            /* 封装本次查询结果所有的价格类型ID begin */
            //priceTypeId有效 && priceTypeId不在字符串中
            if(StringUtil.isValidStr(priceTypeId)
                    && allPriceTypeList.indexOf(priceTypeId) < 0){
                allPriceTypeList.append(queryHotelResult.getChildRoomTypeId());
                if(it.hasNext()){
                    allPriceTypeList.append(",");
                }
            }
            /* 封装本次查询结果所有的价格类型ID end */
            
            /* 封装本次查询结果有优惠立减的价格类型ID begin */
            boolean hasBenefit = queryHotelResult.getHasBenefit() == 1 ? true : false;
            //有立减标志 && priceTypeId有效 && priceTypeId不在字符串中
            if(hasBenefit && StringUtil.isValidStr(priceTypeId)
                    && benefitPriceTypeList.indexOf(priceTypeId) < 0){
                benefitPriceTypeList.append(queryHotelResult.getChildRoomTypeId());
                if(it.hasNext()){
                    benefitPriceTypeList.append(",");
                }
            }
            /* 封装本次查询结果有优惠立减的价格类型ID end */
            
            /* 封装本次查询结果有现金返还的价格类型ID */
			boolean hasCashReturn = queryHotelResult.getHasCashReturn()==1 ?true:false;
			if(hasCashReturn&&StringUtil.isValidStr(priceTypeId)&&cashReturnPriceTypeList.indexOf(priceTypeId)<0){
				cashReturnPriceTypeList.append(queryHotelResult.getChildRoomTypeId());
				if(it.hasNext()){
					cashReturnPriceTypeList.append(",");
				}
			}
        }
        String allHotelListStr = allHotelList.toString();
        String allPriceTypeListStr = allPriceTypeList.toString();
        String benefitPriceTypeListStr = benefitPriceTypeList.toString();
        //删除最后一个逗号','
        allHotelListStr = StringUtil.deleteLastChar(allHotelListStr, ',');
        allPriceTypeListStr = StringUtil.deleteLastChar(allPriceTypeListStr, ',');
        benefitPriceTypeListStr = StringUtil.deleteLastChar(benefitPriceTypeListStr, ',');
        String hasCashReturnPriceTypeListStr = cashReturnPriceTypeList.toString();
        hasCashReturnPriceTypeListStr = StringUtil.deleteLastChar(hasCashReturnPriceTypeListStr, ',');
        paramsMap.put(KEY_ALL_HOTELLIST,allHotelListStr);
        paramsMap.put(KEY_ALL_PRICETYPELIST, allPriceTypeListStr);
        paramsMap.put(KEY_BENEFIT_PRICETYPELIST, benefitPriceTypeListStr);
        paramsMap.put(KEY_CASHRETURN_PRICETYPELIST, hasCashReturnPriceTypeListStr);
        return paramsMap;
    }
	
    public String getHotelFaxNo(long hotelId,long childRoomTypeId){
    	HtlPriceType htlprice = hotelManage.findHtlPriceType(childRoomTypeId);
    	List<HtlBookSetup> htlBookSetupList= supplierInfoService.queryHtlSupplierFax(htlprice);
    	
    	String bookctctType = "02";
    	HtlHotel hotel = hotelManage.findHotel(hotelId);
    	if(!htlBookSetupList.isEmpty()){
    		return getHotelFaxNo(htlBookSetupList,hotel,bookctctType);
    	}else{
            List<HtlBookSetup> hbList = getHotelBookSetup(hotel, bookctctType);
            return getHotelFaxNo(hbList,hotel,bookctctType);
    	}
    }
    
    public String getHotelFaxNo(List<HtlBookSetup> hbList,HtlHotel hotel,String bookctctType){
    	String hotelFax ="";
        String strNowTime = DateUtil.formatTimeToString(DateUtil.getSystemDate());
        String[] strTime = strNowTime.split(":");
        String intTime = strTime[0] + strTime[1];
        int nowTime = Integer.parseInt(intTime);
        if(null !=hbList && !hbList.isEmpty()){
            for (HtlBookSetup hbBookSetup : hbList) {
                if (null != hbBookSetup.getBookBeginTime() 
                    && null != hbBookSetup.getBookEndTime()) {
                    int bookBeginTime = 0;
                    int bookEndTime = 0;
                    if (-1 != hbBookSetup.getBookBeginTime().indexOf(":")) {
                        bookBeginTime = Integer.parseInt(hbBookSetup.getBookBeginTime().replace(
                            ":", ""));
                    } else {
                        bookBeginTime = Integer.parseInt(hbBookSetup.getBookBeginTime());
                    }
                    if (-1 != hbBookSetup.getBookEndTime().indexOf(":")) {
                        bookEndTime = Integer.parseInt(hbBookSetup.getBookEndTime()
                            .replace(":", ""));
                    } else {
                        bookEndTime = Integer.parseInt(hbBookSetup.getBookEndTime());
                    }
                    if (bookBeginTime > bookEndTime) {
                        if (bookBeginTime >= nowTime && bookEndTime >= nowTime) {
                            hotelFax = hbBookSetup.getBookfax();
                        } else if (bookBeginTime <= nowTime && bookEndTime <= nowTime) {
                            hotelFax = hbBookSetup.getBookfax();
                        }
                    } else if (bookBeginTime < bookEndTime) {
                        if (bookEndTime >= nowTime && bookBeginTime <= nowTime) {
                            hotelFax = hbBookSetup.getBookfax();
                        }
                    }
                } else {
                    hotelFax = hbBookSetup.getBookfax();
                }
            }
        } else {
            List<HtlBookSetup> htlBookSetupList = hotel.getHtlBookSetup();
            for (HtlBookSetup htlBookSetup : htlBookSetupList) {
                if (bookctctType.equals(htlBookSetup.getBookctctType())) {
                    hotelFax = htlBookSetup.getBookfax();
                }
            }
        }
        return hotelFax;
    }
    
    public List<HtlBookSetup> getSupplierFax(long hotelId,long childRoomTypeId){
    	HtlPriceType htlprice = hotelManage.findHtlPriceType(childRoomTypeId);
    	return supplierInfoService.queryHtlSupplierFax(htlprice);
    }
	public IBenefitService getBenefitService() {
		return benefitService;
	}

	public void setBenefitService(IBenefitService benefitService) {
		this.benefitService = benefitService;
	}

    public IQueryAnyDao getQueryAnyDao() {
        return queryAnyDao;
    }

    public void setQueryAnyDao(IQueryAnyDao queryAnyDao) {
        this.queryAnyDao = queryAnyDao;
    }

    public URLClient getUrlClient() {
        return urlClient;
    }

    public void setUrlClient(URLClient urlClient) {
        this.urlClient = urlClient;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public RoomControlDao getRoomControlDao() {
        return roomControlDao;
    }

    public void setRoomControlDao(RoomControlDao roomControlDao) {
        this.roomControlDao = roomControlDao;
    }

    public HotelQueryDao getHotelQueryDao() {
        return hotelQueryDao;
    }

    public void setHotelQueryDao(HotelQueryDao hotelQueryDao) {
        this.hotelQueryDao = hotelQueryDao;
    }
    
    public void setReturnService(IHotelFavourableReturnService returnService) {
		this.returnService = returnService;
	}
	
	public HtlLimitFavourableManage getLimitFavourableManage() {
		return limitFavourableManage;
	}

	public void setLimitFavourableManage(
			HtlLimitFavourableManage limitFavourableManage) {
		this.limitFavourableManage = limitFavourableManage;
	}

	public void setSupplierInfoService(SupplierInfoService supplierInfoService) {
		this.supplierInfoService = supplierInfoService;
	} 
    
}

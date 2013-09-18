package com.mangocity.hweb.manage.impl;

import hk.com.cts.ctcp.hotel.constant.ResultConstant;
import hk.com.cts.ctcp.hotel.constant.TxnStatusType;
import hk.com.cts.ctcp.hotel.service.HKService;
import hk.com.cts.ctcp.hotel.webservice.enquiryservice.TxnStatusData;
import hk.com.cts.ctcp.hotel.webservice.response.HKRoomAmtResponse;
import hk.com.cts.ctcp.hotel.webservice.saleservice.BasicData;
import hk.com.cts.ctcp.hotel.webservice.saleservice.BeginData;
import hk.com.cts.ctcp.hotel.webservice.saleservice.CustInfo;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mangocity.hdl.constant.ChannelType;
import com.mangocity.hk.constant.HkConstant;
import com.mangocity.hotel.base.manage.ContractManage;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.manage.HtlLimitFavourableManage;
import com.mangocity.hotel.base.manage.assistant.HotelAddressInfo;
import com.mangocity.hotel.base.persistence.HtlAddBedPrice;
import com.mangocity.hotel.base.persistence.HtlB2bComminfo;
import com.mangocity.hotel.base.persistence.HtlBreakfast;
import com.mangocity.hotel.base.persistence.HtlChargeBreakfast;
import com.mangocity.hotel.base.persistence.HtlChildWelcomePrice;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlEveningsRent;
import com.mangocity.hotel.base.persistence.HtlFavouraParameter;
import com.mangocity.hotel.base.persistence.HtlFavourableDecrease;
import com.mangocity.hotel.base.persistence.HtlFavourableReturn;
import com.mangocity.hotel.base.persistence.HtlFavourableclause;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlPresale;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.persistence.HtlWelcomePrice;
import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.hotel.base.persistence.OrReservation;
import com.mangocity.hotel.base.resource.ResourceManager;
import com.mangocity.hotel.base.service.CommissionService;
import com.mangocity.hotel.base.service.IBenefitService;
import com.mangocity.hotel.base.service.IHotelFavourableReturnService;
import com.mangocity.hotel.base.service.assistant.AssureInforAssistant;
import com.mangocity.hotel.base.service.assistant.ReservationAssist;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.hotel.order.persistence.OrChannelNo;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderItem;
import com.mangocity.hotel.order.persistence.OrPriceDetail;
import com.mangocity.hotel.order.service.IOrderEditService;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.hotelweb.persistence.HotelOrderFromBean;
import com.mangocity.hweb.dao.HWebHotelDAO;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.hweb.persistence.AdditionalServeItem;
import com.mangocity.hweb.persistence.HotelAdditionalServe;
import com.mangocity.hweb.persistence.HotelInfoForWeb;
import com.mangocity.hweb.persistence.HotelInfoForWebBean;
import com.mangocity.hweb.persistence.HotelPageForWebBean;
import com.mangocity.hweb.persistence.HwClickAmount;
import com.mangocity.hweb.persistence.HwHotelIndex;
import com.mangocity.hweb.persistence.OftenDeliveryAddress;
import com.mangocity.hweb.persistence.QueryHotelForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebResult;
import com.mangocity.hweb.persistence.QueryHotelForWebRoomType;
import com.mangocity.hweb.persistence.QueryHotelForWebSaleItems;
import com.mangocity.hweb.persistence.QueryHotelForWebServiceIntroduction;
import com.mangocity.hweb.persistence.QueryPictureForWebServiceIntroduction;
import com.mangocity.mgis.app.service.baseinfo.GisService;
import com.mangocity.mgis.app.util.CalculateDistance;
import com.mangocity.mgis.domain.valueobject.GisInfo;
import com.mangocity.mgis.domain.valueobject.LatLng;
import com.mangocity.mgis.domain.valueobject.MapsInfo;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.bean.MustDate;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.dao.DAOIbatisImpl;
import com.mangocity.util.hotel.constant.FromChannelType;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.util.log.MyLog;
import com.mangocity.webnew.persistence.HtlChannelClickLog;

/**
 */
public class HotelManageWebImpl extends DAOHibernateImpl implements HotelManageWeb {
	private static final MyLog log = MyLog.getLogger(HotelManageWebImpl.class);
    private static final long serialVersionUID = 4641531585796050793L;
    
    public static final String KEY_RESULT_LIST = "keyResultList";

    public static final String KEY_TOTAL_SIZE = "keyTotalSize";

    private DAOIbatisImpl queryDao;

    private ContractManage contractManage;

    private HotelManage hotelManage;

    private HKService hkService;
    
    private CommissionService commissionService;

    /**
     * v2.9.2 add by guzhijie 2009-08-09 允许取消修改的时间是否在今天之前
     */
    private int isBeforeCanModTime;

    /**
     * 主要用于查询预订条款 add by chenjiajie 2009-05-25
     */
    private IOrderService orderService;
    
    /**
     * 用于订单基本信息修改相关操作 add by chenjiajie 2009-05-25
     */
    private IOrderEditService orderEditService;

    private GisService gisService;
    
    /**
     * 优惠立减服务接口 add by chenjiajie 2009-10-15
     */
    private IBenefitService benefitService;
    
    /**
     * 现金返还服务接口 add by linpeng.fang 2009-09-29
     */
    private IHotelFavourableReturnService returnService;
    
    /**
     * 限量返现促销服务接口 add by xiaojun.xiong 2011-3-4
     */
    private HtlLimitFavourableManage limitFavourableManage;
    
    /**
     * 资源接口
     */
    private ResourceManager resourceManager;

    //TODO:delete debug
    private int totalForCount = 0;
    private int totalStayDiscountCount = 0;
    private int totalBenefitCount = 0;
	
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
    
    private HWebHotelDAO hwebHotelDao;

	/**
     * 查询单家酒店的房型
     * 
     * @param queryBean
     *            查询条件(酒店ID，入店时间，离店时间)
     * @return 查询结果（queryHotelForWebResult）
     */
    public QueryHotelForWebResult queryHotelInfoBeanForWebs(QueryHotelForWebBean queryBean) {
        QueryHotelForWebResult queryHotelForWebResult;

        // 设置查询渠道来源
        queryBean.setFromChannel(FromChannelType.WEB);
        HotelPageForWebBean hotelPageForWebBean = this.queryHotelsForWeb(queryBean);
        // 根据酒店ID查询，因此只会返回一家酒店 add by CMB
        if (null != hotelPageForWebBean.getList() && 0 < hotelPageForWebBean.getList().size()) {
            queryHotelForWebResult = hotelPageForWebBean.getList().get(0);
        } else {
        	queryHotelForWebResult = new QueryHotelForWebResult();
        }
        return queryHotelForWebResult;
    }

    /**
     * 网站v2.2查询酒店价格明细
     * 
     * @param hotelID
     *            酒店ID
     * @param beginDate
     *            实际开始日期
     * @param endDate
     *            实际结束日期
     * @param beginDateExt
     *            参考开始日期
     * @param endDateExt
     *            参考结束日期
     * @param roomTypeID
     *            房型
     * @param childRoomTypeID
     *            子房型
     * @param payMethod
     *            支付方式
     * @return 查询结果
     */
    public List<QueryHotelForWebSaleItems> queryPriceDetailForWeb(Long hotelID, Date beginDate,
        Date endDate, Date beginDateExt, Date endDateExt, Long roomTypeID, Long childRoomTypeID,
        String payMethod) {
        
        //预订条款查询某一个酒店价格明细 
        List<QueryHotelForWebResult> results = hwebHotelDao.queryPriceDetailForWeb(hotelID, beginDate, endDate, 
                beginDateExt, endDateExt, roomTypeID, childRoomTypeID, payMethod);
        
        List<QueryHotelForWebSaleItems> saleResults = new ArrayList<QueryHotelForWebSaleItems>();
        
        if (null != results) {
            for (int i = 0; i < results.size(); i++) {
                QueryHotelForWebResult webItem = results.get(i);
                QueryHotelForWebSaleItems saleItem = new QueryHotelForWebSaleItems();
                saleItem.setFellowDate(DateUtil.getDate(webItem.getAble_sale_date()));
                saleItem.setAvailQty(webItem.getAvailQty());
                saleItem.setRoomState(webItem.getRoom_state());
                saleItem.setBreakfastNum(webItem.getBreakfastNum());
                saleItem.setBreakfastType(Long.valueOf(webItem.getBreakfastType()).toString());
                saleItem.setRoomEquipment(webItem.getRoomNet());
                saleItem.setClose_flag(webItem.getClose_flag());
                saleItem.setCanBook(webItem.getCanBook());
                saleItem.setSalePrice(webItem.getSalesPrice());
                saleItem.setBasePrice(webItem.getBasePrice());
                //add by shizhongwen 2009-10-3 加入TMC价格
                saleItem.setTmcPirce(webItem.getSalesPrice());
                
                // add by wuyun 2009-06-05 网站订单页面重新查询时需要重新确定是否面转预
                saleItem.setPayToPrepay(webItem.getPayToPrepay());
                // add by linpeng.fang 返现需要佣金和佣金率计算净价
                saleItem.setCommission(webItem.getCommission());             
                saleItem.setCommissionrate(webItem.getCommissionrate());          
                saleItem.setFormulaId(webItem.getFormulaId());
                saleResults.add(saleItem);
            }
            queryAdvicePriceForWeb(saleResults, hotelID, beginDate, endDate, 
                    beginDateExt, endDateExt, roomTypeID, childRoomTypeID, payMethod);
        }
        return saleResults;
    }
    
    private void queryAdvicePriceForWeb(List<QueryHotelForWebSaleItems> saleResults,Long hotelID, Date beginDate,
            Date endDate, Date beginDateExt, Date endDateExt, Long roomTypeID, Long childRoomTypeID,
            String payMethod){
    	List objList=hwebHotelDao.queryAdvicePriceForWeb(hotelID, beginDate, endDate, beginDateExt, endDateExt, roomTypeID, childRoomTypeID, payMethod);
    	if(objList != null && objList.size()!=0){
    		for(int i=0;i<objList.size();i++){
    			Object[] obj=(Object[])objList.get(i);
    			for(QueryHotelForWebSaleItems item:saleResults){
    				if(obj[0]!=null && obj[0].toString()!=""){
    					if(DateUtil.compare(DateUtil.getDate(obj[0].toString()),item.getFellowDate())==0){
    						item.setAdvicePrice(Double.valueOf(obj[1].toString()));
    					}
    				}
    			}
    		}
    	}
    }

    /**
     * hotel2.6 预订不符合条件提示 @author zhuangzhineng @2009-02-26
     * 
     * @param queryHotelForWebSaleItems
     * @param queryBean
     * @param bookHintNoMeet
     * @param buttonShowEnable
     * @return
     *  chenkeming@2009-06-24
     */
    public String addBookRemark(QueryHotelForWebSaleItems queryHotelForWebSaleItems,
        QueryHotelForWebBean queryBean, int difdays, StringBuffer bookHintNoMeet,
        String buttonShowEnable) {

        // add by chenkeming@2009-06-24 用于判断处理3种情况中出现多种的组合时，语句适当调整
        boolean bFirst = true;

        // 预订不符合条件提示 2.6 add by zhineng.zhuang 2009-02-26 begin
        // 系统日期应该在最晚预订时间的前面，如果系统日期不比其小，提示
        boolean enableShow = true;
        
        //hotel 2.9.3 预订条款新增预订时间段的规则。add by shengwei.zuo 2009-09-06 begin
        String latestTime = queryHotelForWebSaleItems.getLatestBokableTime();
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
        
        if (null != queryHotelForWebSaleItems.getLatestBookableDate()) {
        	
         	//如果最早预订时间不为空
            if(null!=queryHotelForWebSaleItems.getFirstBookableDate()){
            	
                //add by shengwei.zuo hotel2.9.3  最早预订时间 2009-09-06
                String firstTime = queryHotelForWebSaleItems.getFirstBookableTime();
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
            	if(!DateUtil.isBetweenDateTime(queryHotelForWebSaleItems.getFirstBookableDate(),(fTime[0]+fTime[1]),
            			queryHotelForWebSaleItems.getLatestBookableDate(),(sTime[0] + sTime[1]))){
            			
                		bookHintNoMeet.append("预订此房型，必须在");
                        bookHintNoMeet
                                .append(DateUtil.dateToString(queryHotelForWebSaleItems
                                    .getFirstBookableDate()));
                        bookHintNoMeet.append(" ");
                        bookHintNoMeet.append(null == queryHotelForWebSaleItems
                                .getFirstBookableTime() ? "" : queryHotelForWebSaleItems
                                .getFirstBookableTime());
                        bookHintNoMeet.append(" 至 ");
                        bookHintNoMeet
                        .append(DateUtil.dateToString(queryHotelForWebSaleItems
                            .getLatestBookableDate()));
                        bookHintNoMeet.append(" ");
                        bookHintNoMeet.append(null == queryHotelForWebSaleItems
                                .getLatestBokableTime() ? "" : queryHotelForWebSaleItems
                                .getLatestBokableTime());
                        bookHintNoMeet.append("之间预订。");
                        enableShow = false;
                      
            	}
            	
            }else{
        	
            		if (!DateUtil.compareDateTimeToSys(queryHotelForWebSaleItems
                        .getLatestBookableDate(), (sTime[0] + sTime[1]))) {
                         bookHintNoMeet.append("预订此房型，必须在");
                         bookHintNoMeet
                                .append(DateUtil.dateToString(queryHotelForWebSaleItems
                                    .getLatestBookableDate()));
                         bookHintNoMeet.append(" ");
                         bookHintNoMeet.append(null == queryHotelForWebSaleItems
                                .getLatestBokableTime() ? "" : queryHotelForWebSaleItems
                                .getLatestBokableTime());
                         bookHintNoMeet.append("之前预订。");
                         
                         enableShow = false;
                    }
            }
        	
            //hotel 2.9.3 预订条款新增预订时间段的规则。add by shengwei.zuo 2009-09-06 end
            
//            bookHintNoMeet.append("入住此房型需在");
//            bookHintNoMeet.append(DateUtil.dateToString(queryHotelForWebSaleItems
//                .getLatestBookableDate()));
//            if (StringUtil.isValidStr(queryHotelForWebSaleItems.getLatestBokableTime())) {
//                if (!DateUtil.compareDateTimeToSys(queryHotelForWebSaleItems
//                    .getLatestBookableDate(), queryHotelForWebSaleItems.getLatestBokableTime())) {
//                    bookHintNoMeet.append(" ");
//                    bookHintNoMeet.append(queryHotelForWebSaleItems.getLatestBokableTime());
//                    enableShow = false;
//                }
//            } else {
//                if (0 > DateUtil.compare(DateUtil.getDate(DateUtil.getSystemDate()),
//                             queryHotelForWebSaleItems.getLatestBookableDate())) {
//                    enableShow = false;
//                }
//            }
//            bookHintNoMeet.append("前预订。");
        	
        	
        	
            // modified by wuyun 之所以要清空，是因为预付的情况下，有可能满足预定条件，因此这段提示应该去掉，以免和后面的提示拼接在一起
            if (enableShow) {
                bookHintNoMeet.delete(0, bookHintNoMeet.length());
            } else {
                bFirst = false;
            }
        }
        // 系统连住日期应该满足
        if (difdays < queryHotelForWebSaleItems.getContinueDay()) {
            if (bFirst) {
                bookHintNoMeet.append("入住此房型，需连住");
            } else {
                bookHintNoMeet.append("并需连住");
            }
            bookHintNoMeet.append(queryHotelForWebSaleItems.getContinueDay());
            // modified by lixiaoyong v2.6 2009-05-03 晚后面不要句号,修复bug811
            bookHintNoMeet.append("晚以上（含");
            bookHintNoMeet.append(queryHotelForWebSaleItems.getContinueDay());
            if (bFirst) {
                bookHintNoMeet.append("晚）方可接受预订。");
            } else {
                bookHintNoMeet.append("晚）。");
            }
            enableShow = false;
            bFirst = false;
        }
        if (0 != queryHotelForWebSaleItems.getMaxRestrictNights()
            && (queryHotelForWebSaleItems.getMaxRestrictNights() != difdays)) {
            if ("".endsWith(bookHintNoMeet.toString())) {
                bookHintNoMeet.append("入住此房型，仅限连住").append(
                    queryHotelForWebSaleItems.getMaxRestrictNights()).append("晚方可接受预订");
            } else {
                bookHintNoMeet.append("并且仅限连住 ").append(
                    queryHotelForWebSaleItems.getMaxRestrictNights()).append("晚方可接受预订");
            }
            enableShow = false;
            bFirst = false;
        }/*
          * if(queryHotelForWebSaleItems.getMinRestrictNights()!=0
          * &&queryHotelForWebSaleItems.getMinRestrictNights
          * ()>difdays){ if("".endsWith(bookHintNoMeet.toString())){
          * bookHintNoMeet.append("入住此房型，最少预订 "
          * ).append(queryHotelForWebSaleItems.getMinRestrictNights()).append(" 间夜"); }else{
          * bookHintNoMeet
          * .append("，并且最少预订 ").append(queryHotelForWebSaleItems.getMinRestrictNights()
          * ).append(" 间夜"); } enableShow = false; bFirst = false; }
          */
        // 必住日期 增加关系选择,并且 或者 ,如果为空,则默认为 或者 . add by juesuchen
        if (null != queryHotelForWebSaleItems.getMustFirstDate()
            && null != queryHotelForWebSaleItems.getMustLastDate() && null != queryBean.getInDate()
            && null != queryBean.getOutDate()) {
            // 对必住日期进行逻辑判断的方法
            boolean isCanLive = checkMustInDate(queryHotelForWebSaleItems, bookHintNoMeet,
                queryBean, bFirst);
            if (!isCanLive) {
                enableShow = false;
                bFirst = false;
            }
        }
        // 预订不符合条件提示 2.6 add by zhineng.zhuang 2009-02-26 end

        if (enableShow) {
            buttonShowEnable = "1";
        } else {
            buttonShowEnable = "0";
        }

        return buttonShowEnable;
    }

    /**
     * 对必住日期进行逻辑判断的方法 addby chenjuesu
     * 
     * @param queryHotelForWebSaleItems
     * @param bookHintNoMeet
     * @param queryBean
     */
    private boolean checkMustInDate(QueryHotelForWebSaleItems queryHotelForWebSaleItems,
        StringBuffer bookHintNoMeet, QueryHotelForWebBean queryBean, boolean bFirst) {
        // TODO Auto-generated method stub
        String mustDatesRelation = queryHotelForWebSaleItems.getMustInDatesRelation();// 取得必住日期关系
        List<MustDate> mustInDates = new ArrayList<MustDate>();
        int type = MustDate.getMustIndatesAndType(mustInDates, queryHotelForWebSaleItems
            .getMustInDate());
        boolean isCanLive = false;
        StringBuffer noMeet = new StringBuffer();
        if (!StringUtil.isValidStr(mustDatesRelation) || mustDatesRelation.equals("or")) {// 里边为 或者
                                                                                          // 逻辑判断
            // 得到必住日期集合
            if (type == MustDate.DATE_TYPE) {// 必住日期逻辑
                for (MustDate date : mustInDates) {
                    // //如果入住日期包括任意一个必住日期即可入住
                    if (DateUtil.isBetween(date.getContinueDate(), queryBean.getInDate(), queryBean
                        .getOutDate())) {
                        isCanLive = true;
                        break;
                    }
                    noMeet.append(DateUtil.dateToString(date.getContinueDate())).append(",");
                }
                if (!isCanLive) {
                    if (bFirst) {
                        bookHintNoMeet.append("入住此房型，住店日期需至少包含");
                    } else {
                        bookHintNoMeet.append("并且住店日期需至少包含");
                    }
                    noMeet.deleteCharAt(noMeet.length() - 1);
                    bookHintNoMeet.append(noMeet.toString());
                    if (bFirst) {
                        bookHintNoMeet.append("中任意一天方可接受预订。");
                    } else {
                        bookHintNoMeet.append("中任意一天。");
                    }
                }
            } else if (type == MustDate.WEEK_TYPE) {// 必住星期逻辑
                for (MustDate date : mustInDates) {
                    if (DateUtil.isBetween(queryBean.getInDate(), date.getContinueDate(), date
                        .getContinueEndDate())) {
                        String[] checkInWeeks = date.getWeeks().split(",");
                        Date[] checkInDates = DateUtil.getDateWithWeek(queryBean.getInDate(),
                            queryBean.getOutDate(), checkInWeeks);
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
                    if (bFirst) {
                        bookHintNoMeet.append("入住此房型，住店日期需至少包含");
                    } else {
                        bookHintNoMeet.append("并且住店日期需至少包含");
                    }
                    bookHintNoMeet.append(noMeet.toString());
                    if (bFirst) {
                        bookHintNoMeet.append("中任意一天方可接受预订。");
                    } else {
                        bookHintNoMeet.append("中任意一天。");
                    }
                }
            }
        } else {// 里边为 并且 逻辑判断
            if (type == MustDate.DATE_TYPE) {// 必住日期逻辑
                if (!DateUtil.isBetween(queryHotelForWebSaleItems.getMustFirstDate(), queryBean
                    .getInDate(), queryBean.getOutDate())
                    || !DateUtil.isBetween(queryHotelForWebSaleItems.getMustLastDate(), queryBean
                        .getInDate(), queryBean.getOutDate())) {
                    // 不能入住
                    if (bFirst) {
                        bookHintNoMeet.append("入住此房型，住店日期需包含");
                    } else {
                        bookHintNoMeet.append("并且住店日期需包含");
                    }
                    for (MustDate date : mustInDates)
                        noMeet.append(DateUtil.dateToString(date.getContinueDate())).append(",");
                    noMeet.deleteCharAt(noMeet.length() - 1);
                    bookHintNoMeet.append(noMeet.toString());
                    if (bFirst) {
                        bookHintNoMeet.append("方可接受预订。");
                    } else {
                        bookHintNoMeet.append("。");
                    }
                } else
                    isCanLive = true;// 可以入住
            } else if (type == MustDate.WEEK_TYPE) {// 必住星期逻辑
                for (MustDate date : mustInDates) {
                    if (DateUtil.isBetween(queryBean.getInDate(), date.getContinueDate(), date
                        .getContinueEndDate())) {
                        String[] checkInWeeks = date.getWeeks().split(",");
                        Date[] checkInDates = DateUtil.getDateWithWeek(queryBean.getInDate(),
                            queryBean.getOutDate(), checkInWeeks);
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
                    if (bFirst) {
                        bookHintNoMeet.append("入住此房型，住店日期需包含");
                    } else {
                        bookHintNoMeet.append("并且住店日期需包含");
                    }
                    bookHintNoMeet.append(noMeet.toString());
                    if (bFirst) {
                        bookHintNoMeet.append("方可接受预订。");
                    } else {
                        bookHintNoMeet.append("。");
                    }
                }
            }
        }
        return isCanLive;
    }

    /**
     * 网站v2.2查询酒店的实现
     * 
     * @param queryBean
     *            查询条件
     * @return 查询结果
     */
    public HotelPageForWebBean queryHotelsForWeb(QueryHotelForWebBean queryBean) {
        double minPrice = 0.0;
        HotelPageForWebBean hotelPageForWebBean = new HotelPageForWebBean();
        List<QueryHotelForWebResult> list = new ArrayList<QueryHotelForWebResult>();
        //芒果给代理的佣金率
        Double mangoRate = 0.0;
        int pageNo = 1;
        // 当指定跳到哪一页时
        if (0 != queryBean.getPageIndex()) {
            pageNo = queryBean.getPageIndex();
        }

        // 计算周天数
        List weekNum = getDateStrList(queryBean.getInDate(), queryBean.getOutDate());
        List<String> DateNum = getDateStrLst(queryBean.getInDate(), queryBean.getOutDate());

        // 计算周需要显示的行数
        int difdays = DateUtil.getDay(queryBean.getInDate(), queryBean.getOutDate());
        int weekTotal = (difdays - 1) / 7 + 1;

        // 计算显示的页面显示的宽度
        int colCount = (1 < weekTotal ? 8 : difdays) + 6;
        // int avgWidth = (int)100/colCount;
        // String avgWidthStr = " width = " + avgWidth + "%";

        // 每页显示数
        int pageSize = queryBean.getPageSize();

        List<QueryHotelForWebResult> results = null;
        Integer totalSize = new Integer(0); // 查询结果后返回总记录数

        //TODO:delete debug
        long queryBeginTime = System.currentTimeMillis();
        //查询数据库，并把数据库返回的游标缓存到List中 
        Map resultMap = hwebHotelDao.queryHotelResultListForWeb(queryBean);
        //TODO:delete debug
        long queryEendTime = System.currentTimeMillis();
        log.info("==========+++++++++debug 001:本次调用只读数据库PKG查询共用" + (queryEendTime - queryBeginTime) + "毫秒+++++++++==========");
        
        //从查询结果Map中取数据
        totalSize = (Integer) resultMap.get(KEY_TOTAL_SIZE);
        results = (List<QueryHotelForWebResult>) resultMap.get(KEY_RESULT_LIST);
        

        if (null != results) {
        	
	        long beginTime = System.currentTimeMillis();
            // 计算总页数
            int totalIndex = 0;
            if (0 != totalSize % pageSize) {
                totalIndex = totalSize / pageSize + 1;
            } else {
                totalIndex = totalSize / pageSize;
            }
            hotelPageForWebBean.setTotalIndex(totalIndex);
            hotelPageForWebBean.setPageSize(pageSize);
            hotelPageForWebBean.setPageIndex(pageNo);

            List<QueryHotelForWebRoomType> roomTypeLis = null;
            List<QueryHotelForWebSaleItems> saleItemsLis = null;
            List<QueryHotelForWebRoomType> roomTypes = null;
            List<QueryHotelForWebSaleItems> saleLis = null;
            List<QueryHotelForWebSaleItems> faceLis = null;
            List<QueryHotelForWebSaleItems> faceItemsLis = null;
            QueryHotelForWebResult queryHotelForWebResult = null;
            Date dt = null;
            int priceNum = 0;

            int safk = 0;
            int fafk = 0;

            // 每天房态和房间数量数据
            String[] roomStates = new String[difdays];
            int[] qtys = new int[difdays];

            List<QueryHotelForWebResult> tempResultList = null;
            String rateStr = "";
            long hotelId = 0;
            long roomTypeId = 0;
            long tempHotelId = 0;
            if (0 != results.size() && null != results.get(0)) {
                tempHotelId = results.get(0).getHotelId();
            }
            boolean readFlag = false;
            int curIndex = 0;
            Map ma = hotelManage.getExchangeRateMap();
            //TODO:delete debug 每次开始先清空，预防缓存记录
            totalForCount = 0;
            totalStayDiscountCount = 0;
            totalBenefitCount = 0;
            
            //TODO:delete debug 
            long beginTimeOfHotel = System.currentTimeMillis();
            
            //封装所有需要再一次返回数据库的参数 add by chenjiajie 2010-05-06 
            Map paramsMap = getAllParamsForDB(results);
            String allPriceIds = (String) paramsMap.get(KEY_ALL_PRICETYPELIST);
            //缓存参数传入的所有价格类型连住优惠信息 add by chenjiajie 2010-05-06 
            Map<String,List<HtlFavourableclause>> favourableMap = queryAllFavourableList(allPriceIds);                        
            String benefitPriceIds = null;
            //缓存参数传入的所有价格类型立减优惠信息 add by chenjiajie 2010-05-06 
            Map<String,List<HtlFavourableDecrease>> decreaseMap = Collections.emptyMap();
            
            //现金返还
            String cashReturnPriceIds = null;
            Map<String,List<HtlFavourableReturn>> cashReturnMap = Collections.emptyMap(); 
            
            // 如果是交行全卡商旅查询，则不用查询芒果的优惠信息 modify by chenkeming
            if(!queryBean.isForCooperate()) {
            	
				benefitPriceIds = (String) paramsMap
						.get(KEY_BENEFIT_PRICETYPELIST);
				decreaseMap = new HashMap<String, List<HtlFavourableDecrease>>();

				// 如果没有立减优惠的价格类型，则不查询数据库缓存到map中 add by chenjiajie 2010-05-06
				if (StringUtil.isValidStr(benefitPriceIds)) {
					decreaseMap = benefitService.queryBatchBenefitByDate(
							benefitPriceIds, queryBean.getInDate(), queryBean
									.getOutDate());
				}

				// 现金返还
				cashReturnPriceIds = (String) paramsMap
						.get(KEY_CASHRETURN_PRICETYPELIST);
				cashReturnMap = new HashMap<String, List<HtlFavourableReturn>>();
				if (StringUtil.isValidStr(cashReturnPriceIds)) {
					cashReturnMap = returnService
							.queryFavourableReturnForPriceTypeIds(
									cashReturnPriceIds, queryBean.getInDate(),
									queryBean.getOutDate());
				}
            }
            
            //B2B的逻辑，散客不需要执行
            if(StringUtil.isValidStr(queryBean.getB2bCd())){
                //只有B2B才需要查询
                mangoRate = setMangoRateForB2B(mangoRate);
            }
            
            for (QueryHotelForWebResult info : results) {
                if (info.getHotelId() == tempHotelId && readFlag) // 跳过同一酒店的记录
                {
                    curIndex++;
                    continue;
                }
                readFlag = true;
                tempHotelId = info.getHotelId();
                if (null != tempResultList) // 清除前一个酒店数据
                {
                    tempResultList.clear();
                    tempResultList = null;
                }

                // 得到当前酒店ID对应的酒店数据, 前提是results里的记录是按hotelId分组的
                hotelId = info.getHotelId();
                tempResultList = new ArrayList<QueryHotelForWebResult>();
                for (int i = curIndex; i < results.size(); i++) {
                    QueryHotelForWebResult tempInfo = results.get(i);
                    tempInfo.setMangorate(mangoRate);
                    if (tempInfo.getHotelId() == hotelId) {
                        tempResultList.add(tempInfo);
                    } else {
                        break;
                    }
                }

                // 每个酒店信息
                queryHotelForWebResult = new QueryHotelForWebResult();
                queryHotelForWebResult.setHotelId(hotelId);
                queryHotelForWebResult.setCommendType(info.getCommendType());
                // 地图标志
                queryHotelForWebResult.setGisid(info.getGisid());

                // ADD BY WUYUN 增加香港中科酒店标志、直连酒店标志 2009-03-19
                queryHotelForWebResult.setFlagCtsHK(info.isFlagCtsHK());
                
                queryHotelForWebResult.setCooperateChannel(info.getCooperateChannel());
                
                // 交行全卡商旅等合作渠道则不需要芒果的优惠 modify by chenkeming
                if(!queryBean.isForCooperate()) {
                    //立减优惠标志 add by chenjiajie 2009-11-13
                    queryHotelForWebResult.setHasBenefit(info.getHasBenefit());                	
                }

                //代理系统芒果网应得的佣金率 add by haibo.li
                queryHotelForWebResult.setMangorate(mangoRate);
                
                
                // 交行全卡商旅等合作渠道没有这些优惠 modify by chenkeming
                if(!queryBean.isForCooperate()) {
    				//现金返还 1：有，0：无 add by linpeng.fang 2010-09-29
    				queryHotelForWebResult
    						.setHasCashReturn(info.getHasCashReturn());
    			
                }
                
                // 每个酒店下的房型信息
                roomTypeLis = getRoomTypeInfoList(hotelId, tempResultList, queryHotelForWebResult, queryBean,decreaseMap);

                roomTypes = new ArrayList<QueryHotelForWebRoomType>();
                queryHotelForWebResult.setPriceId(priceNum);
                long tmpRoomTypeId = 0;
                int tmpUd = -1;
                boolean bSetUd = false;
                //TODO:delete debug 
//                long beginTimeOfRoomType = System.currentTimeMillis();
                for (QueryHotelForWebRoomType queryHotelForWebRoomType : roomTypeLis) {

                    // 酒店结果中每个房型有几种付款方式
                    int fk = 0;
                    double price = 0;
                    //用来存放连住优惠（一个循环后）总价格变化后
                    double priceTotel = 0;
                    roomTypeId = Long.parseLong(queryHotelForWebRoomType.getRoomTypeId());
                    if (tmpRoomTypeId != roomTypeId) {
                        bSetUd = false;
                    }

                    // 当为预付时，相关的记录。add by shengwei.zuo 2009-03-30 Begin

                    // 预付方式价格明细查询 -----Begin
                        	saleItemsLis = getSaleItemList("pre_pay", hotelId, roomTypeId,
                             queryHotelForWebRoomType, tempResultList); 	
                        	
                    /**
                     * ADD BY WUYUN 2009-04-01 对于香港中科酒店，订单每天都有房间数的限制，这里需得到预订日期范围内所每单最多可订房间数
                     */
                    int minRoomNumCts = 9999;
                    for (QueryHotelForWebSaleItems queryHotelForWebSaleItems : saleItemsLis) {
                        int roomNumCts = queryHotelForWebSaleItems.getRoomNumCts();
                        if (roomNumCts < minRoomNumCts) {
                            minRoomNumCts = roomNumCts;
                        }
                    }

                    // 给预付list补空，方便页面显示
                    saleLis = new ArrayList<QueryHotelForWebSaleItems>();
                    safk = 0;
                    // 按钮是否灰掉 add by zhineng.zhuang 2009-02-26
                    String buttonShowEnable = "1";
                    String buttonShowEnableForPrepay = "1";
                    // 预订不符合的信息提示 add by zhineng.zhuang 2009-02-26
                    StringBuffer bookHintNoMeet = new StringBuffer();
                    StringBuffer bookHintNotMeetForPrepay = new StringBuffer();
                    
                    //TODO:delete debug 
//                    long beginTimeOfSaleItemsLis = System.currentTimeMillis();
                    if (0 < saleItemsLis.size()) {
                        safk = 1;
                        //用于标示天数是否足够连住包价
                        int f = 0;
//                        long beginTimePrepay1 = System.currentTimeMillis();
                        for (int k = 0; k < difdays; k++) {
                        	//TODO:delete debug
                        	totalForCount++;
                            dt = DateUtil.getDate(queryBean.getInDate(), k);
                            roomStates[k] = null;
                            if (queryBean.getOutDate().after(dt)) {
                                int m = 0;
                                boolean bFound = false;
//                                long beginTimePrepay0 = System.currentTimeMillis();
                                for (int pp = 0; pp < saleItemsLis.size(); pp++) {
                                	
                                    QueryHotelForWebSaleItems queryHotelForWebSaleItems =  saleItemsLis.get(pp);
                                    if (dt.equals(queryHotelForWebSaleItems.getFellowDate())) {
                                    	
                                    	queryHotelForWebSaleItems.setPriceId(++priceNum);
                                        saleLis.add(queryHotelForWebSaleItems);
                                        //连住优惠对价格处理
                                        List li = null;
                                        // 如果是tmc会员查酒店，则不用连住优惠 modify by chenkeming@2009-11-03
//                                        long tPreBegin1 = System.currentTimeMillis();
                                        if(!queryBean.isForTmc()) {
                                        	//TODO:delete debug
                                        	totalStayDiscountCount++;
                                            li = changePriceForFavourable(info.getHotelStar(),
                                            		queryHotelForWebResult.getMangorate(),priceTotel,saleItemsLis,
                                            		queryHotelForWebSaleItems,hotelId,queryHotelForWebRoomType.getChildRoomTypeId(),
                                            		pp,f,favourableMap);
                                        }
//                                        long tPreEnd1 = System.currentTimeMillis();
//                                        logger.info("tPre1:" + (tPreEnd1 - tPreBegin1) + "ms");

//                                        long tPreBegin3 = System.currentTimeMillis();
                                    	if(null != li ){
                                         	priceTotel = (Double)li.get(0);
                                             f = (Integer)li.get(1);
                                             price = priceTotel;
                                         }
                                    	price += queryHotelForWebSaleItems.getSalePrice();
                                        if(99999.0 == queryHotelForWebSaleItems.getSalePrice()){
                                        	price = price-queryHotelForWebSaleItems.getSalePrice();
                                        	
                                        }
                                        priceTotel = price;

                                        if (0.0001 < queryHotelForWebSaleItems.getSalePrice()) {
                                            m++;
                                        }
                                        // 得到最低价格 add by haibo.li 电子地图二期
                                        if (0 == Double.compare(minPrice,0.0)) {
                                            minPrice = queryHotelForWebSaleItems.getSalePrice();
                                        } else if (minPrice > queryHotelForWebSaleItems
                                            .getSalePrice()) {
                                            minPrice = queryHotelForWebSaleItems.getSalePrice();
                                        }

                                        roomStates[k] = queryHotelForWebSaleItems.getRoomStatus();
                                        qtys[k] = queryHotelForWebSaleItems.getAvailQty();
                                        
                                        // 中旅房型配额为0则不可订 add by chenkeming@2009-12-09
                                        if(qtys[k] == 0 && queryHotelForWebRoomType.getRoomChannel() 
                                        		== ChannelType.CHANNEL_CTS) {
                                        	tmpUd = 4;
                                        	bSetUd = true;
                                        }
                                        
                                        bFound = true;
//                                        long tPreEnd3 = System.currentTimeMillis();
//                                        logger.info("tPre3:" + (tPreEnd3 - tPreBegin3) + "ms");
                                    }
//                                    long tPreBegin4 = System.currentTimeMillis();
                                    if (null != queryHotelForWebSaleItems.getClose_flag()
                                        && queryHotelForWebSaleItems.getClose_flag().equals("G")) {
                                        queryHotelForWebRoomType.setClose_flag("G");
                                    }
//                                    long tPreEnd4 = System.currentTimeMillis();
//                                    logger.info("tPre4:" + (tPreEnd4 - tPreBegin4) + "ms");
                                    
                                    // hotel2.6 加不符合预订条款的提示 @author zhuangzhineng @2009-02-26
//                                    long tPreBegin2 = System.currentTimeMillis();
                                    if (0 == k && 0 == pp) {
                                        buttonShowEnableForPrepay = addBookRemark(
                                            queryHotelForWebSaleItems, queryBean, difdays,
                                            bookHintNotMeetForPrepay, buttonShowEnableForPrepay);
                                        queryHotelForWebRoomType
                                            .setBookHintNotMeetForPrepay(bookHintNotMeetForPrepay
                                                .toString());
                                        queryHotelForWebRoomType
                                           .setBookButtonenAbleForPrepay(buttonShowEnableForPrepay);
                                    }
//                                    long tPreEnd2 = System.currentTimeMillis();
//                                    logger.info("tPre2:" + (tPreEnd2 - tPreBegin2) + "ms");

                                }

//                                long endTimePrepay0 = System.currentTimeMillis();
//                                logger.info("TimePrepay0:"+ (endTimePrepay0 - beginTimePrepay0) + "ms");
                                if (0 == m) {
                                    if (!bFound) {
                                        saleLis.add(new QueryHotelForWebSaleItems());
                                    }
                                    queryHotelForWebRoomType.setNoOrder(true);
                                }
                            }
                        }

//                        long endTimePrepay1 = System.currentTimeMillis();
//                        logger.info("TimePrepay1:"+(endTimePrepay1 - beginTimePrepay1) + "ms/saleItemsLis.size()" + saleItemsLis.size());
                        if (!bSetUd) {
                            tmpUd = handleRoomStates(roomStates, qtys);
                            bSetUd = true;
                        }
                        queryHotelForWebRoomType.setYud(tmpUd);
                    }
                    
                    //TODO:delete debug 
//                    long endTimeOfSaleItemsLis = System.currentTimeMillis();
//                    log.info("==========+++++++++debug 007:酒店:" + ",本次遍历预付价格共用" + (endTimeOfSaleItemsLis - beginTimeOfSaleItemsLis) + "毫秒");
                    
                    //求均价
                    double avlPrice=0.00;
                    if (0.001 < price) {
                    	//生产bug修改均价，查询日期内价格不为零的天数 add by zhijie.gu 2010-01-13
                        int salePriceIsnotZeroNum = getSalePriceIsZeroNum(saleItemsLis);
                    	if(price == 999999 || price == 999999.0 || price == 888888 || price > 99999){
                    		price = 0.0;
                    	}
                    	if(0 < salePriceIsnotZeroNum){
                    		avlPrice = price/salePriceIsnotZeroNum;
                    	}
                    }
                    queryHotelForWebRoomType.setAvlPrice(avlPrice);
                    // 该房型价格类型的预付价格
                    queryHotelForWebRoomType.setPrepayPrice(price);
                    price = 0;
                    priceTotel = 0;

                    // 当为预付时，相关的记录。add by shengwei.zuo 2009-03-30 End;

                    // 当为面付时，相关的记录。add by shengwei.zuo 2009-03-30 Begin

                    // 面付方式价格明细查询
                    faceItemsLis = getSaleItemList("pay", hotelId, roomTypeId,
                            queryHotelForWebRoomType, tempResultList);
      
                    // 给面付list补空，方便页面显示
                    faceLis = new ArrayList<QueryHotelForWebSaleItems>();
                    fafk = 0;
                    
                    //TODO:delete debug 
//                    long beginTimeOfFaceItemsLis = System.currentTimeMillis();
                    if (0 < faceItemsLis.size()) {
                        fafk = 2;
                        int f = 0;
//                        long beginTimePay1 = System.currentTimeMillis();
                        for (int k = 0; k < difdays; k++) {
                            dt = DateUtil.getDate(queryBean.getInDate(), k);
                            roomStates[k] = null;
                            //用于标示天数是否足够连住包价
                            
                            if (queryBean.getOutDate().after(dt)) {
                                int m = 0;
                                boolean bFound = false;

//                                long beginTimePay0 = System.currentTimeMillis();
                                for (int z = 0; z < faceItemsLis.size(); z++) {
                                	QueryHotelForWebSaleItems queryHotelForWebSaleItems = 
                                                                    faceItemsLis.get(z);
                                   
                                    if (dt.equals(queryHotelForWebSaleItems.getFellowDate())) {
                                        List li = null;
                                        // 如果是tmc会员查酒店，则不用连住优惠 modify by chenkeming@2009-11-03
//                                        long tPayBegin1 = System.currentTimeMillis();
                                        if(!queryBean.isForTmc()) {
                                        	
                                            li = changePriceForFavourable(info.getHotelStar(),queryHotelForWebResult.getMangorate(),
                                            priceTotel,faceItemsLis,queryHotelForWebSaleItems,
                                            hotelId,queryHotelForWebRoomType.getChildRoomTypeId(),
                                            z,f,favourableMap);  
                                            
                                        }           
//                                        long tPayEnd1 = System.currentTimeMillis();    
//                                        logger.info("tPay1:" + (tPayEnd1 - tPayBegin1) +"ms");
//                                        long tPayBegin3 = System.currentTimeMillis();
                                    	if(null != li ){
                                         	priceTotel = (Double)li.get(0);
                                             f = (Integer)li.get(1);
                                             price = priceTotel;
                                         }
                                    	queryHotelForWebSaleItems.setPriceId(++priceNum);
                                        faceLis.add(queryHotelForWebSaleItems);
                                        price += queryHotelForWebSaleItems.getSalePrice();
                                        if(99999.0 == queryHotelForWebSaleItems.getSalePrice()){
                                        	price = price-queryHotelForWebSaleItems.getSalePrice();
                                        	
                                        }
                                        priceTotel = price;
                                        if (0.0001 < queryHotelForWebSaleItems.getSalePrice()) {
                                            m++;
                                        }

                                        roomStates[k] = queryHotelForWebSaleItems.getRoomStatus();
                                        qtys[k] = queryHotelForWebSaleItems.getAvailQty();
                                        bFound = true;
//                                        long tPayEnd3 = System.currentTimeMillis();    
//                                        logger.info("tPay3:" + (tPayEnd3 - tPayBegin3) +"ms");
                                    }

//                                    long tPayBegin4 = System.currentTimeMillis();    
                                    // 得到最低价格 add by haibo.li 电子地图二期
                                    if (0 == Double.compare(minPrice,0.0)) {
                                        minPrice = queryHotelForWebSaleItems.getSalePrice();
                                    } else if (minPrice > queryHotelForWebSaleItems
                                                                    .getSalePrice()) {
                                        minPrice = queryHotelForWebSaleItems.getSalePrice();
                                    }
                                    if (null != queryHotelForWebSaleItems.getClose_flag()
                                        && queryHotelForWebSaleItems.getClose_flag().equals("G")) {
                                        queryHotelForWebRoomType.setClose_flag("G");
                                    }
//                                    long tPayEnd4 = System.currentTimeMillis();    
//                                    logger.info("tPay4:" + (tPayEnd4 - tPayBegin4) +"ms");
                                    // if (30073243 == hotelId) {
                                        // log.info("stop");
                                    // }
                                    // hotel2.6 加不符合预订条款的提示 @author zhuangzhineng @2009-02-26
//                                    long tPayBegin2 = System.currentTimeMillis();
                                    if (0 == k && 0 == z) {

                                        // add by shengwei.zuo 解决预订时显示多条重复的提示信息问题--Begin

                                        bookHintNoMeet = new StringBuffer();

                                        buttonShowEnable = addBookRemark(queryHotelForWebSaleItems,
                                            queryBean, difdays, bookHintNoMeet, buttonShowEnable);

                                        // add by shengwei.zuo ---End

                                    }
//                                    long tPayEnd2 = System.currentTimeMillis();
//                                    logger.info("tPay2:" + (tPayEnd2 - tPayBegin2) +"ms");

                                    // add by shengwei.zuo 设置面转预标记
                                    if (1 == queryHotelForWebSaleItems.getPayToPrepay()) {
                                        queryHotelForWebRoomType
                                            .setPayToPrepay(queryHotelForWebSaleItems
                                                .getPayToPrepay());
                                    }

                                }

//                                long endTimePay0 = System.currentTimeMillis();
//                                logger.info("TimePay0:"+(endTimePay0 - beginTimePay0) + "ms");
                                if (0 == m) {
                                    if (!bFound) {
                                        faceLis.add(new QueryHotelForWebSaleItems());
                                    }
                                    queryHotelForWebRoomType.setNoOrder(true);
                                }
                            }
                        }

//                        long endTimePay1 = System.currentTimeMillis();
//                        logger.info("TimePay1:" + (endTimePay1 - beginTimePay1) + "ms/faceItemsLis.size()" + faceItemsLis.size());
                        if (!bSetUd) {
                            tmpUd = handleRoomStates(roomStates, qtys);
                            bSetUd = true;
                        }
                        queryHotelForWebRoomType.setMf(tmpUd);

                    }
                    
                    //TODO:delete debug 
//                    long endTimeOfFaceItemsLis = System.currentTimeMillis();
//                    log.info("==========+++++++++debug 008:酒店:" + ",本次遍历面付价格共用" + (endTimeOfFaceItemsLis - beginTimeOfFaceItemsLis) + "毫秒");

                    // 当为面付时，相关的记录。add by shengwei.zuo 2009-03-30 End

                    // hotel2.6 加不符合预订条款的提示
                    queryHotelForWebRoomType.setBookHintNotMeet(bookHintNoMeet.toString());
                    // 判断"预订"按钮是否灰掉
                    queryHotelForWebRoomType.setBookButtonenAble(buttonShowEnable);
                    
                    //求均价
                    double payAvlPrice=0.00;
                    if (0.001 < price ) {
                    	//生产bug修改均价，查询日期内价格不为零的天数 add by zhijie.gu 2010-01-13
                    	int faceSalePriceIsnotZeroNum = getSalePriceIsZeroNum(faceItemsLis);
                    	if(0 < faceSalePriceIsnotZeroNum){
                    		payAvlPrice = price/faceSalePriceIsnotZeroNum;
                    	}
                    	if(price == 999999 || price == 888888 || price == 99999.0 ||  price==0.0){
                        	payAvlPrice = 0.00;
                        }
                    }
                    queryHotelForWebRoomType.setPayAvlPrice(payAvlPrice);
                    // 该房型价格类型的面付价格
                    queryHotelForWebRoomType.setPayPrice(price);
                    price = 0;
                    priceTotel = 0;
                    if (7 < difdays) {
                        for (int k = 0; k < 7 - difdays % 7; k++) {
                            saleLis.add(new QueryHotelForWebSaleItems());
                            faceLis.add(new QueryHotelForWebSaleItems());
                        }
                    }
                    fk = fafk + safk;
                    queryHotelForWebRoomType.setFk(fk);
                    // queryHotelForWebRoomType.setColCount(colCount);
                    queryHotelForWebRoomType.setWeekTotal(weekTotal);
                    // queryHotelForWebRoomType.setWeekNum(weekNum);
                    // queryHotelForWebRoomType.setRowNum(weekTotal * 2);
                    queryHotelForWebRoomType.setCurrency(info.getCurrency());
                    //设置首日价begin
                    if(saleLis.size()>0){
                		QueryHotelForWebSaleItems hotelForWebSaleItems = (QueryHotelForWebSaleItems)saleLis.get(0);
                		if("G".equals(hotelForWebSaleItems.getClose_flag()) && 1 == hotelForWebSaleItems.getCloseShowPrice()){
                			queryHotelForWebRoomType.setOneDayPrice(888888);
                		}else{
                			queryHotelForWebRoomType.setOneDayPrice(hotelForWebSaleItems.getSalePrice());
                			int prePayCashReturnAmount = calculateCashReturnAmount(queryHotelForWebRoomType.getChildRoomTypeId(),saleLis, queryBean, info, "pre_Pay", cashReturnMap);            			
                			queryHotelForWebRoomType.setCashReturnAmount(prePayCashReturnAmount);
                		}
                		
                		
                	}
                	if(faceLis.size()>0){
                		QueryHotelForWebSaleItems hotelForWebSaleItems = (QueryHotelForWebSaleItems)faceLis.get(0);
                		if("G".equals(hotelForWebSaleItems.getClose_flag()) && 1 == hotelForWebSaleItems.getCloseShowPrice()){
                			queryHotelForWebRoomType.setPayOneDayPrice(888888);
                			
                		}else{
                			
                			queryHotelForWebRoomType.setPayOneDayPrice(hotelForWebSaleItems.getSalePrice());
                			int payCashReturnAmount = calculateCashReturnAmount(queryHotelForWebRoomType.getChildRoomTypeId(),faceLis, queryBean, info, "pay", cashReturnMap);            			
                			queryHotelForWebRoomType.setPayCashReturnAmount(payCashReturnAmount);
                		}
                		
                	}
                	 //设置首日价end
                    if (3 == fk) {
                    	queryHotelForWebRoomType.setSaleItems(saleLis);
                        queryHotelForWebRoomType.setFaceItems(faceLis);
                    } else if (2 == fk) {
                        queryHotelForWebRoomType.setItemsList(faceLis);
                        queryHotelForWebRoomType.setItemsPrice(queryHotelForWebRoomType
                            .getPayPrice());
                        queryHotelForWebRoomType.setPayMethod("pay");
                        // queryHotelForWebRoomType.setPayStr("面付");
                    } else if (1 == fk) {
                        queryHotelForWebRoomType.setItemsList(saleLis);
                        queryHotelForWebRoomType.setItemsPrice(queryHotelForWebRoomType
                            .getPrepayPrice());
                        queryHotelForWebRoomType.setPayMethod("pre_pay");
                        // queryHotelForWebRoomType.setPayStr("预付");
                    }
                    // ADD BY WUYUN 2009-04-01
                    queryHotelForWebRoomType.setMinRoomNumCts(minRoomNumCts);
                    roomTypes.add(queryHotelForWebRoomType);
                    tmpRoomTypeId = roomTypeId;
                    
                    
                    //判断最终预定按钮的显示..add by haibo.li 网站改版
                    if(queryHotelForWebRoomType.getClose_flag()=="G" ||
                    (queryHotelForWebRoomType.getFk() == 3 && queryHotelForWebRoomType.getYud() == 4) ||	
                    (queryHotelForWebRoomType.getFk() == 2 && queryHotelForWebRoomType.getMf() == 4)  ||
                    (queryHotelForWebRoomType.getFk() == 1 && queryHotelForWebRoomType.getYud() == 4) || 
                    queryHotelForWebRoomType.isNoOrder()||( queryHotelForWebRoomType.getItemsPrice()>999998 )){
                    	queryHotelForWebRoomType.setFalseButton(false);	
                    }
                    //add by haibo.li 判断预订条款封装 按钮显示 
                    if(queryHotelForWebRoomType.getBookButtonenAble()!=null && !queryHotelForWebRoomType.getBookButtonenAble().equals("")){
                    	if(queryHotelForWebRoomType.getBookButtonenAble().equals("0")){
                    		queryHotelForWebRoomType.setFalseButton(false);	
                    	}
                    }
                    if(queryHotelForWebRoomType.getBookButtonenAbleForPrepay()!=null && !queryHotelForWebRoomType.getBookButtonenAbleForPrepay().equals("")){
                    	if(queryHotelForWebRoomType.getBookButtonenAbleForPrepay().equals("0")){
                    		queryHotelForWebRoomType.setFalseButton(false);	
                    	}
                    }
                  }

                //TODO:delete debug 
//                long endTimeOfRoomType = System.currentTimeMillis();
//                log.info("==========+++++++++debug 006:酒店:" + ",本次遍历房型共用" + (endTimeOfRoomType - beginTimeOfRoomType) + "毫秒");
                
                queryHotelForWebResult.setFx(roomTypes.size());
                queryHotelForWebResult.setMinPrice(minPrice);
                minPrice = 0.0;
                queryHotelForWebResult.setHotelStar(info.getHotelStar());
                queryHotelForWebResult.setColCount(colCount);
                queryHotelForWebResult.setWeekTotal(weekTotal);
                queryHotelForWebResult.setWeekNum(weekNum);
                queryHotelForWebResult.setDateNum(DateNum);
                queryHotelForWebResult.setRoomTypes(roomTypes);
                //判断房型是否就只有3个,或者小于3，如果只有3个 那全部房型字段不显示
                if(roomTypes.size()<=3){
                	queryHotelForWebResult.setRk("3");
                	queryHotelForWebResult.setJk(roomTypes.size());	
                }else{
                	queryHotelForWebResult.setRk("2");
                	queryHotelForWebResult.setJk(3);
                }
                queryHotelForWebResult.setHotelChnName(info.getHotelChnName());
                queryHotelForWebResult.setHotelEngName(info.getHotelEngName());
                queryHotelForWebResult.setAutoIntroduce(info.getAutoIntroduce());
                queryHotelForWebResult.setPraciceYear(info.getPraciceYear());
                queryHotelForWebResult.setFitmentYear(info.getFitmentYear());
                //根据商区区code获得值 add by haibo.li 网站改版
                String BizValue = InitServlet.businessSozeObj.get(info.getBizZone()); 
                queryHotelForWebResult.setBizValue(BizValue);
                queryHotelForWebResult.setBizZone(info.getBizZone());
                queryHotelForWebResult.setDistrict(info.getDistrict());

                // 主要设施图标
                queryHotelForWebResult.setForPlane(info.getForPlane());
                queryHotelForWebResult.setFreeForPlane(info.isFreeForPlane());
                queryHotelForWebResult.setForFreeStop(info.getForFreeStop());
                queryHotelForWebResult.setForFreePool(info.getForFreePool());
                queryHotelForWebResult.setForFreeGym(info.getForFreeGym());
                queryHotelForWebResult.setForNetBand(info.getForNetBand());
                queryHotelForWebResult.setRate(info.getRate());
                // 判断是否只查询一晚 add by haibo.li 电子地图二期
                if (1 < difdays) {
                    queryHotelForWebResult.setContinueLong(true);
                } else {
                    queryHotelForWebResult.setContinueLong(false);
                }

                // 酒店交通信息
                queryHotelForWebResult.setTrafficInfo(info.getTrafficInfo());
//                log.info("rs.getString(trafficInfo)========="+info.getTrafficInfo());
                // 芒果促销信息
                queryHotelForWebResult.setHasPreSale(info.getHasPreSale());
                if (1 == queryHotelForWebResult.getHasPreSale()) {
                    queryHotelForWebResult.setPreSaleName(info.getPreSaleName());
                    queryHotelForWebResult.setPreSaleContent(info.getPreSaleContent());
                    queryHotelForWebResult.setPreSaleBeginEnd(info.getPreSaleBeginEnd());
                    queryHotelForWebResult.setPreSaleURL(info.getPreSaleURL());
                }

                // 得到酒店图片
                queryHotelForWebResult.setOutPictureName(info.getOutPictureName());
                queryHotelForWebResult.setHallPictureName(info.getHallPictureName());
                queryHotelForWebResult.setRoomPictureName(info.getRoomPictureName());
                queryHotelForWebResult.setHotelLogo(info.getHotelLogo());
                //获得最终币种
                if(info.getCurrency().equals("HKD")){
                	queryHotelForWebResult.setCurrencyValue("HK$");
                }else if(info.getCurrency().equals("RMB")){
                	//不适用JSON直接输出特殊字符
                	if(0 == queryBean.getJsonTag()){
                		queryHotelForWebResult.setCurrencyValue("&yen;");
                	}else{
                		queryHotelForWebResult.setCurrencyValue("RMB");
                	}
                }else if(info.getCurrency().equals("MOP")){
                	queryHotelForWebResult.setCurrencyValue("MOP$");
                } 
                // 获取3D图片数量
                queryHotelForWebResult.setSandNum(info.getSandNum());

                // 评论分数
                queryHotelForWebResult.setGeneralPoint(info.getGeneralPoint());

                if (CurrencyBean.RMB.equals(info.getCurrency())) {
                    queryHotelForWebResult.setRateStr("1");
                } else {
                    if (null == ma) {
                        rateStr = "1";
                    } else {
                        rateStr = (String) ma.get(info.getCurrency());
                        if (null == rateStr) {
                            rateStr = "1";
                        }
                    }
                    queryHotelForWebResult.setRateStr(rateStr);
                }
                queryHotelForWebResult.setCurrency(info.getCurrency());
                queryHotelForWebResult.setEndpriceId(priceNum);
                //add by shizhongwen 2009-10-17 海尔VIP会员 面转预 
                if(queryBean.isHaierVipMember()){
                    queryHotelForWebResult.setPayToPrepay(1);
                }
                priceNum++;
                list.add(queryHotelForWebResult);

                curIndex++;
            }
            //TODO:delete debug
//            long endTimeOfHotel = System.currentTimeMillis();
//            log.info("==========+++++++++debug 005:本次遍历酒店:" + ",共用" + (endTimeOfHotel - beginTimeOfHotel) + "毫秒");
            roomTypeLis = null;
            saleItemsLis = null;
            roomTypes = null;
            saleLis = null;
            faceLis = null;
            faceItemsLis = null;
            queryHotelForWebResult = null;
            dt = null;
            tempResultList = null;

	        //TODO:delete debug
	        long endTime = System.currentTimeMillis();
	        log.info("==========+++++++++debug 002:本次PKG调用后程序中查询共用" 
	        		+ (endTime - beginTime) + "毫秒，" 
	        		+ "总共循环了:" + totalForCount + "次;" 
	        		+ "连住优惠执行:" + totalStayDiscountCount + "次;"
	        		+ "立减优惠执行:" + totalBenefitCount + "次;"
	        		+ "+++++++++==========");
        }
        // 强行设置网站币种汇率，为港币汇率（目前只考虑港币情况）
        hotelPageForWebBean.setRateStr(getCurrenyRate(CurrencyBean.HKD));

        
        //对list进行操作 add by yong.zeng 2010.3.21
        //设置佣金价和佣金率
        double strRate = 0;
        
        hotelPageForWebBean.setList(list);
        results = null;
        list = null;
        return hotelPageForWebBean;
    }
    
    /**
     * 计算房型的返现金额
     * @param saleItems
     * @param queryBean
     * @param info
     * @param payMethod
     * @param cashReturnMap
     * @return
     */
    private int calculateCashReturnAmount(String childRoomTypeId,List<QueryHotelForWebSaleItems> saleItems,QueryHotelForWebBean queryBean,QueryHotelForWebResult info,String payMethod,Map<String,List<HtlFavourableReturn>> cashReturnMap){
    	int cashReturnAmount = 0;
    	
		if(info.getHasCashReturn() > 0){
				for(QueryHotelForWebSaleItems item : saleItems){	
					BigDecimal price = new BigDecimal(item.getSalePrice());
					if(price.intValue() >= 99999 || "1".equals(item.getCloseShowPrice()) || null == item.getFellowDate()) continue;
				    BigDecimal cPrice = returnService.calculateRoomTypePrice(item.getFormulaId(), new BigDecimal(item.getCommission()), new BigDecimal(item.getCommissionRate()), price);
					
					    cashReturnAmount += returnService.calculateCashReturnAmount(childRoomTypeId, queryBean.getInDate(), queryBean.getOutDate(),item.getFellowDate(), 1, cPrice, payMethod, info.getCurrency(), cashReturnMap);
					
				}
			}
		
		return cashReturnAmount;
    }

    /**
     * 查询获取芒果代理佣金率
     * @param mangoRate
     * @return
     */
    private Double setMangoRateForB2B(Double mangoRate) {
        //取得芒果网得到的代理佣金率 代理系统2.9.3 add by haibo.li 2010-1-15
        HtlB2bComminfo htlB2bComminfo = orderService.queryB2bComminfo();
        if(htlB2bComminfo!=null){
            mangoRate = htlB2bComminfo.getRemainComission();
        }
        return mangoRate;
    }

    /**
     * 得到房型信息
     * 
     * @param hotelId
     * @param resultInfo
     * @param hotelResult
     * @param queryBean
     * @return
     */
    private List<QueryHotelForWebRoomType> getRoomTypeInfoList(long hotelId,
        List<QueryHotelForWebResult> resultInfo, QueryHotelForWebResult hotelResult,
        QueryHotelForWebBean queryBean,Map<String,List<HtlFavourableDecrease>> decreaseMap) {
        List<QueryHotelForWebRoomType> roomTypeList = new ArrayList<QueryHotelForWebRoomType>();
        QueryHotelForWebRoomType roomType = null;
        String tmpChildRoomTypeId = "";
        String tmpRoomTypeId = "";
        String tmpPayMethod = "";
        String infoRoomType = "";
        int totalRoomTypes = 0;
        for (QueryHotelForWebResult info : resultInfo) {
            infoRoomType = String.valueOf(info.getRoom_type_id());
            // 避过相同房型和子房型的数据
            if (tmpRoomTypeId.equals(infoRoomType)) {
                if (tmpChildRoomTypeId.equals(info.getChildRoomTypeId())) {
                    if (!tmpPayMethod.equals(info.getPayMethod()) && null != roomType) {
                        if (tmpPayMethod.equals("pay")) {
                            roomType.setBreakfastNum1(roomType.getBreakfastNum());
                            roomType.setBreakfastNum(info.getBreakfastNum());
                        } else {
                            roomType.setBreakfastNum1(info.getBreakfastNum());
                        }
                        tmpPayMethod = info.getPayMethod();
                    }
                    continue;
                }
            }

            // 忽略关房(变价原因)的房型信息
            String closeFlag = info.getClose_flag();
            // String reason = info.getReason();
            if (hotelId == info.getHotelId()) {
                roomType = new QueryHotelForWebRoomType();
                roomType.setCurrency(info.getCurrency());
                tmpPayMethod = info.getPayMethod();
                roomType.setPayMethod(tmpPayMethod);
                roomType.setChildRoomTypeId(info.getChildRoomTypeId());
                roomType.setChildRoomTypeName(info.getPriceType());
                roomType.setRoomTypeName(info.getRoomName());
                roomType.setRoomTypeId(String.valueOf(info.getRoom_type_id()));
                roomType.setRoomPrice(info.getSalesRoomPrice());
                roomType.setQuotaType(info.getQuotaType());
                roomType.setBreakfastType(info.getBreakfastType());
                roomType.setBreakfastNum(info.getBreakfastNum());
                
                //hotel 2.9.3 房间数 add by shengwei.zuo 2009-09-09
                roomType.setCanRoomNumber(""+info.getRoom_qty());
                
                roomType.setAcreage(info.getAcreage());
                roomType.setRoomFloor(info.getRoomFloor());
                roomType.setBedType(info.getRoom_state(), info.getAvailQty());
                
                //设置房型对应的床型列表，以逗号分隔;add by shengwei.zuo  2009-10-26
                roomType.setBedTypeStr(info.getRoom_state());
                
                roomType.setNet(info.getRoomNet());
                roomType.setCanBook(info.getCanBook());
                // 是否香港中科酒店的房型 ADD BY WUYUN 2009-03-20
                roomType.setRoomChannel(info.getRoomChannel());
                if (0 == roomType.getCanBook()) {
                    roomType.setCantBookReason(info.getCantBookReason());
                }
                roomType.setHasPromo(info.getHasPromo());
                if (1 == roomType.getHasPromo()) {
                    roomType.setPromoContent(info.getPromoContent());
                    roomType.setPromoBeginEnd(info.getPromoBeginEnd());
                }
                roomType.setClose_flag(closeFlag);

                /** hotel2.9.2 RMS2105新增房型浮动框显示内容 add by chenjiajie 2009-07-22 begin **/
                roomType.setMaxPersons(info.getMaxPersons());
                roomType.setAddBedQty(info.getAddBedQty());
                roomType.setRoomEquipment(info.getRoomEquipment());
                roomType.setRoomOtherEquipment(info.getRoomOtherEquipment());
                /** hotel2.9.2 RMS2105新增房型浮动框显示内容 add by chenjiajie 2009-07-22 begin **/

                // 交行全卡商旅等合作渠道没有这些优惠 modify by chenkeming
                if(!queryBean.isForCooperate()) {
                    /** 计算价格类型的优惠立减金额 V2.9.3.1 add by chenjiajie 2009-11-13 begin **/
                    //当该酒店存在优惠立减标志的时候再进行计算
                    if(info.getHasBenefit() > 0){

                    	//TODO:delete debug
                    	totalBenefitCount++;
                    	//计算某价格类型在入住时间段的优惠1间的总金额
                    	int benefitAmount = benefitService.calculateBenefitAmount(info.getChildRoomTypeId(), 
    												                			queryBean.getInDate(), 
    												                			queryBean.getOutDate(),
    												                			1,
    												                			info.getCurrency(),
    												                			decreaseMap);

                    	roomType.setBenefitAmount(benefitAmount);
                    }
                    //没有优惠立减标志，默认优惠0元
                    else{
                    	roomType.setBenefitAmount(0);
                    }
                    /** 计算价格类型的优惠立减金额 V2.9.3.1 add by chenjiajie 2009-11-13 end **/	
                } else {
                	roomType.setBenefitAmount(0);
                }
                
                //当该酒店有现金返还，计算返还金额 add by linpeng.fang 2010-09-29
//				if (info.getHasCashReturn() > 0) {
//
//					double price = returnService.calculateRoomTypePrice(info.getFormulaId(), info.getCommission(), info.getCommissionrate(), info.getSalesPrice());
//					int cashReturnAmount = returnService.calculateCashReturnAmount(info.getChildRoomTypeId(), queryBean.getInDate(), queryBean.getOutDate(),DateUtil.getDate(info.getAble_sale_date()), 1, price, info.getPayMethod(), info.getCurrency(), cashReturnMap);					
//					roomType.setCashReturnAmount(cashReturnAmount);
//				} else {
//					roomType.setCashReturnAmount(0);
//				}
				
                roomTypeList.add(roomType);
                tmpChildRoomTypeId = info.getChildRoomTypeId();
                if (!tmpRoomTypeId.equals(infoRoomType)) {
                    totalRoomTypes++;
                }
                tmpRoomTypeId = roomType.getRoomTypeId();
            }
        }
        // 房型超过3个页面需要显示房型全显示下拉按钮
        if (3 < totalRoomTypes) {
            hotelResult.setFx(totalRoomTypes);
        }
        return roomTypeList;
    }

    /**
     * 得到房态和价格
     * 
     * @param hotelId
     * @param roomTypeId
     * @param childRoomTypeId
     * @param list
     * @return
     */
    private List<QueryHotelForWebSaleItems> getSaleItemList(String payMethod, long hotelId,
        long roomTypeId, QueryHotelForWebRoomType queryHotelForWebRoomType,
        List<QueryHotelForWebResult> list) {
    	long saleItemBegin = System.currentTimeMillis();
        List<QueryHotelForWebSaleItems> saleItems = new ArrayList<QueryHotelForWebSaleItems>();
        double avlPrice = 0.00;
        // QueryHotelForWebSaleItems saleItem = null;
        int i = 0;
        for (QueryHotelForWebResult info : list) {
            if (hotelId == info.getHotelId() && info.getPayMethod().equals(payMethod)
                && queryHotelForWebRoomType.getChildRoomTypeId().equals(info.getChildRoomTypeId())
                && roomTypeId == info.getRoom_type_id()) {
                QueryHotelForWebSaleItems saleItem = new QueryHotelForWebSaleItems();
                saleItem.setRoomState(info.getRoom_state());
                saleItem.setFellowDate(DateUtil.toDateByFormat(info.getAble_sale_date(),
                    "yyyy-MM-dd"));
                saleItem.setSalePrice(info.getSalesPrice());
                saleItem.setClose_flag(info.getClose_flag());
                saleItem.setAvailQty(info.getAvailQty());
                saleItem.setCloseShowPriceInfo(info.getReason());

                // add by zhineng.zhuang 2009-02-26 begin
                saleItem.setNeedAssure(info.isNeedAssure());
                saleItem.setPayToPrepay(info.getPayToPrepay());
                saleItem.setLatestBookableDate(info.getLatestBookableDate());
                saleItem.setLatestBokableTime(info.getLatestBokableTime());
                
                /**hotel 2.9.3  
                 * 新增时间段的预订规则 add by shengwei.zuo 2009-09-06 begin  
                 */
                saleItem.setFirstBookableDate(info.getFirstBookableDate());
                saleItem.setFirstBookableTime(info.getFirstBookableTime());
                /**hotel 2.9.3  
                 * 新增时间段的预订规则 add by shengwei.zuo 2009-09-06 end  
                 */
                
                saleItem.setMustLastDate(info.getMustLastDate());
                saleItem.setMustFirstDate(info.getMustFirstDate());
                saleItem.setContinueDay(info.getContinueDay());
                saleItem.setMustInDate(info.getMustInDate());
                // addby chenjuesu 增加必住日期关系
                saleItem.setMustInDatesRelation(info.getMustInDatesRelation());
                // add by zhineng.zhuang 2009-02-26 end
                saleItem.setRoomNumCts(info.getRoomNumCts());

                // add by lixiaoyong 2009-08-05 begin v2.9.2
                saleItem.setMaxRestrictNights(info.getMaxRestrictNights());
                saleItem.setMinRestrictNights(info.getMinRestrictNights());
                // add by lixiaoyong 2009-08-05 end v2.9.2
                saleItem.setFormulaId(info.getFormulaId());
                saleItem.setCommission(info.getCommission());
                saleItem.setCommissionRate(info.getCommissionrate());
                if (0 == i) {
                    if (payMethod.equals("pay")) {
                        queryHotelForWebRoomType.setPayOneDayPrice(info.getSalesPrice());
                    } else {
                        queryHotelForWebRoomType.setOneDayPrice(info.getSalesPrice());
                    }
                }
                //设置佣金价和佣金率
                double strRate = 0;
                if(info.getSalesPrice() == 0){
                	saleItem.setAgentComissionRate(0.0);
                	saleItem.setAgentComissionPrice(0.0);
                }else{
                	if(info.getFormulaId()==null||info.getFormulaId().equals("")
                			||info.getFormulaId().equals("0")){
                    	strRate = info.getCommission()/info.getSalesPrice() - info.getMangorate();
                    	if(String.valueOf(strRate).length()>3){
                    		saleItem.setAgentComissionRate(Double.valueOf(String.valueOf(strRate).substring(0,4)));
                    		
                    	}else{
                    		saleItem.setAgentComissionRate(info.getCommission()/info.getSalesPrice() - info.getMangorate());
                    	}
                    	saleItem.setAgentComissionPrice(info.getSalesPrice());
                    }else{
                    	strRate = info.getCommissionrate() - info.getMangorate();
                    	if(strRate<0){
                    		saleItem.setAgentComissionRate(0.0);
                    		saleItem.setAgentComissionPrice(0.0);
                    	}else{
                    		if(String.valueOf(strRate).length()>3){
                        		saleItem.setAgentComissionRate(Double.valueOf(String.valueOf(strRate).substring(0,4)));
                        	}else{
                        		saleItem.setAgentComissionRate(info.getCommissionrate() - info.getMangorate());
                        	}
                        	saleItem.setAgentComissionPrice(info.getCommission()/info.getCommissionrate());
                    	}
                    }
                }
                //B2B 2期修改，5星酒店固定显示返佣率9%，其他星显示返佣率12% add by zhijie.gu 2010-3-16 begin 
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                if(0 == Double.compare(saleItem.getSalePrice(), 0.0) ||
                		0 == Double.compare(Double.parseDouble("999999"), saleItem.getSalePrice()) || 
                		"G".equals(info.getClose_flag()) || strRate<0){
                	saleItem.setAgentReadComissionRate(0.0);
                	saleItem.setAgentReadComissionPrice(0.0);
                	saleItem.setAgentReadComission(0.0);
                }else{
                	  if("5".equals(info.getHotelStar()) || "19".equals(info.getHotelStar()) || "4.5".equals(info.getHotelStar()) || "29".equals(info.getHotelStar())){
                      	//代理佣金默认政策修改 add by zhijie.gu 2010-4-27
                		  //if(saleItem.getAgentComissionRate()>=0.09){
                      		saleItem.setAgentReadComissionRate(0.09);
                      		saleItem.setAgentReadComissionPrice(saleItem.getAgentComissionPrice());
                      		saleItem.setAgentReadComission(saleItem.getAgentComissionPrice()*0.09);
                         // }else{
//                      		saleItem.setAgentReadComissionRate(0.09);
//                      		double comission = saleItem.getAgentComissionPrice()* strRate;
//                      		log.info("============strRate+hotelId"+strRate+"  "+hotelId);
//                      		log.info("============comission"+comission);
//                      		double ComissionPrice = Math.round(comission/0.09*100);
//                      		saleItem.setAgentReadComissionPrice(Double.parseDouble(""+(ComissionPrice/100)));
//                      		saleItem.setAgentReadComission(BigDecimal.valueOf(comission).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//                      }
                      	
                      }else{
                    	//代理佣金默认政策修改 add by zhijie.gu 2010-4-27
//                      	if(saleItem.getAgentComissionRate()>=0.12){
                      			saleItem.setAgentReadComissionRate(0.12);
                      			saleItem.setAgentReadComissionPrice(saleItem.getAgentComissionPrice());
                      			saleItem.setAgentReadComission(saleItem.getAgentComissionPrice()*0.12);
//                      	}else{
//                      		saleItem.setAgentReadComissionRate(0.12);
//                      		double comission = saleItem.getAgentComissionPrice()* strRate;
//                      		double ComissionPrice = Math.round(comission/0.12*100);
//                      		saleItem.setAgentReadComissionPrice(Double.parseDouble(""+(ComissionPrice/100)));
//                      		saleItem.setAgentReadComission(BigDecimal.valueOf(comission).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//                      	}
                      }
                }
              
                
                //B2B 2期修改，5星酒店固定显示返佣率9%，其他星显示返佣率12% add by zhijie.gu 2010-3-16 end 
                avlPrice += info.getSalesPrice();
                saleItems.add(saleItem);
                i++;
            }

        }
        if (0.001 < avlPrice) {
            avlPrice /= i;
        }
        if (payMethod.equals("pay")) {
            queryHotelForWebRoomType.setPayAvlPrice(avlPrice);
        } else {
            queryHotelForWebRoomType.setAvlPrice(avlPrice);
        }

    	long saleItemEnd = System.currentTimeMillis();
//    	logger.info("saleItemTime:" + (saleItemEnd - saleItemBegin) + "ms");
        return saleItems;
    }

    /**
     * 根据每天的房态和房间数量数据，判断能否预订
     * 
     * @param roomStates
     * @param qtys
     * @return
     */
    private int handleRoomStates(String[] roomStates, int[] qtys) {
        int result = -1;
        int[] bedStatus = { -1, -1, -1 };
        boolean[] bedUse = { false, false, false };
        for (int j = 0; j < roomStates.length; j++) {
            String roomState = roomStates[j];
            if (!StringUtil.isValidStr(roomState)) {
                continue;
            }
            // 解析房态1:0/2:-1/3:4
            String[] roomArray = roomState.split("/");
            for (int i = 0; i < roomArray.length; i++) {
                String[] testStr2 = roomArray[i].split(":");
                int bedIndex = Integer.parseInt(testStr2[0]) - 1;
                if (testStr2[1].equals("4") || (testStr2[1].equals("3") && 0 >= qtys[j])) {
                    if (4 > bedStatus[bedIndex]) {
                        bedStatus[bedIndex] = 4;
                    }
                }
                bedUse[bedIndex] = true;
            }
        }
        if ((bedUse[0] && -1 == bedStatus[0]) || (bedUse[1] && -1 == bedStatus[1])
            || (bedUse[2] && -1 == bedStatus[2])
            || (false == bedUse[0] && false == bedUse[1] && false == bedUse[2])) {
            return result;
        }
        return 4;
    }

    /**
     * 获取汇率
     * 
     * @param curreny
     * @return
     */
    private String getCurrenyRate(String curreny) {
        String rate = "1";
        Map ma = hotelManage.getExchangeRateMap();
        if (null == ma) {
            return rate;
        }
        rate = String.valueOf(ma.get(curreny));
        return rate;
    }

    /**
     * 对于日期进行格式描述
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    @SuppressWarnings("deprecation")
    protected List<String> getDateStrList(Date startDate, Date endDate) {

        int difdays = DateUtil.getDay(startDate, endDate);

        List<String> dateStrList = new ArrayList<String>();

        difdays = 7 <= difdays ? 7 : difdays;
        for (int i = 0; i < difdays; i++) {
            Date reservationDate = DateUtil.getDate(startDate, i);
            int week = reservationDate.getDay();
            String dateStr = "";
            switch (week) {
            case 1:
                dateStr = "周一";
                break;
            case 2:
                dateStr = "周二";
                break;
            case 3:
                dateStr = "周三";
                break;
            case 4:
                dateStr = "周四";
                break;
            case 5:
                dateStr = "周五";
                break;
            case 6:
                dateStr = "周六";
                break;
            case 0:
                dateStr = "周日";
                break;

            }

            dateStrList.add(dateStr);
        }

        return dateStrList;
    }

    /**
     * 对日期进行封装
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    private List<String> getDateStrLst(Date startDate, Date endDate) {
        List dateStrList = new ArrayList();
        dateStrList = DateUtil.getDates(startDate, endDate);
        int difdays = dateStrList.size() - 1;
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < difdays; i++) {
            String str = DateUtil.dateToString((Date)dateStrList.get(i));
            java.util.Date a = DateUtil.getDate(str);
            String b = DateUtil.formatDateToMD(a);
            list.add(b);
        }
        return list;
    }

    public List<QueryHotelForWebSaleItems> queryPriceForWeb(long hotelId, long roomTypeId,
        long childRoomTypeId, Date beginDate, Date endDate, double minPrice, double maxPrice,
        String payMethod, String quotaType) {
        // ---对参考价格的查询需要参考入住日期是否为当天、次天或其他
        Date tempBeginDate = new Date();
        Date tempEndDate = new Date();
        Calendar calendar = Calendar.getInstance();
        if (0 == DateUtil.getDayOverToday(beginDate)) {
            calendar.setTime(endDate);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 4);// 让结束日期加4
            tempEndDate = calendar.getTime();
            tempBeginDate = beginDate;
        } else if (1 == DateUtil.getDayOverToday(beginDate)) {
            calendar.setTime(endDate);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 3);// 让结束日期加3
            tempEndDate = calendar.getTime();
            calendar.setTime(beginDate);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);// 让起始日期减1
            tempBeginDate = calendar.getTime();
        } else {
            calendar.setTime(endDate);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 2);// 让结束日期加2
            tempEndDate = calendar.getTime();
            calendar.setTime(beginDate);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 2);// 让起始日期减2
            tempBeginDate = calendar.getTime();
        }
        List resultLis = queryPriceDetailForWeb(hotelId, beginDate, endDate, tempBeginDate,
            tempEndDate, roomTypeId, childRoomTypeId, payMethod);
        return removeSomePrice(resultLis, beginDate, endDate);

    }

    public QueryHotelForWebServiceIntroduction queryServiceIntroductionForWeb(long hotelId) {
        QueryHotelForWebServiceIntroduction sIn = new QueryHotelForWebServiceIntroduction();
        Map<String, Object> params = new HashMap<String, Object>();
        sIn = (QueryHotelForWebServiceIntroduction) queryDao.queryForObject(
            "qryHWebWebServiceIntroduction", hotelId);
        List pictureInfos = new ArrayList();
        params.put("hotelId", hotelId);
        pictureInfos = queryDao.queryForList("qryHWebPictureWebServiceIntroduction", params);
        if (null != pictureInfos && null != sIn) {
            sIn.setPictureInfos(pictureInfos);
        }
        return sIn;
    }

    public HotelInfoForWeb queryHotelInfoForWeb(long hotelId) {
        HotelInfoForWeb hotelInfo = new HotelInfoForWeb();
        Map<String, Object> params = new HashMap<String, Object>();
        List pictureInfos = new ArrayList();
        params.put("hotelId", hotelId);
        // 得到酒店基本信息
        HotelInfoForWebBean bean = (HotelInfoForWebBean) queryDao.queryForObject(
            "qryHWebHotelInfoForWeb", params);
        // 得到酒店图片
        pictureInfos = queryDao.queryForList("qryHWebPictureWebServiceIntroduction", params);
        // 组装
        hotelInfo.setHotelId(hotelId);
        if (null != bean) {
            hotelInfo.setHotelName(bean.getChnName());
            hotelInfo.setCommendType(bean.getCommendType());
            hotelInfo.setStarType(bean.getHotelStar());
            hotelInfo.setHotelStar(bean.getHotelStar());
            // 组合酒店地址 hotel2.9.2 modify by jiajie.chen 2009-07-22
            //refactor: to decouple 
            HotelAddressInfo hotelAddressInfo = new HotelAddressInfo();
            hotelAddressInfo.setChnAddress(bean.getChnAddress());
            hotelAddressInfo.setCity(bean.getCity());
            hotelAddressInfo.setState(bean.getState());
            hotelAddressInfo.setZone(bean.getZone());
            
            hotelInfo.setChnAddress(hotelManage.joinHotelAddress(hotelAddressInfo));
            hotelInfo.setHotelIntroduce(bean.getHotelIntroduce());
            hotelInfo.setLogoPictureName(bean.getPictureName());
            hotelInfo.setTelephone(bean.getTelephone());
            hotelInfo.setCheckInTime(bean.getCheckInTime());
            hotelInfo.setCheckOutTime(bean.getCheckOutTime());
            hotelInfo.setCreditCard(bean.getCreditCard());
            hotelInfo.setRoomCount(bean.getLayerCount());
            hotelInfo.setRoomFixtrue(bean.getRoomFixtrue());
            hotelInfo.setFreeService(bean.getFreeService());
            hotelInfo.setHandicappedFixtrue(bean.getHandicappedFixtrue());
            hotelInfo.setMealFixtrue(bean.getMealFixtrue());
            hotelInfo.setCity(bean.getCity());
            hotelInfo.setLongitude(bean.getLongitude());
            hotelInfo.setLatitude(bean.getLatitude());
            hotelInfo.setRoomFixtrueRemark(bean.getRoomFixtrueRemark());
            hotelInfo.setFreeServiceRemark(bean.getFreeServiceRemark());
            
        }
        // 图片
        for (int i = 0; i < pictureInfos.size(); i++) {
            QueryPictureForWebServiceIntroduction picture = 
                                    (QueryPictureForWebServiceIntroduction) pictureInfos
                .get(i);
            if (picture.getPictureType().equals("0")) {
                hotelInfo.setOutPictureName(picture.getPictureName());
            } else if (picture.getPictureType().equals("1")) {
                hotelInfo.setHallPictureName(picture.getPictureName());
            } else {
                hotelInfo.setRoomPictureName(picture.getPictureName());
            }
        }
        return hotelInfo;
    }

    /**
     * 重新找回酒店信息
     * 
     * @param hotel_id
     *            酒店id;
     * @return 酒店一个实体
     */
    public HtlHotel findHotel(long hotel_id) {
        HtlHotel htlHotel = (HtlHotel) super.find(HtlHotel.class, hotel_id);
        if (null != htlHotel) {
            return htlHotel;
        }
        return null;
    }

    public boolean isPurviewPublish(long memberId, long hotelId) {
        // 会员在一年内对该酒店的评论少于入住该酒店次数*2，并入住过该酒店
        return hotelManage.countCommentMember4Hotel(memberId, hotelId) < hotelManage
               .countOrderMember4Hotel(memberId, hotelId) * 2;
    }

    public List<HtlPresale> queryPresalesForWeb(long hotelId, Date date) {
        List<HtlPresale> results = new ArrayList<HtlPresale>();
        results = super.queryByNamedQuery("lstPresaleHWEB", 
                new Object[] { Long.valueOf(hotelId), date,"1" });
        return results;
    }

    public HotelAdditionalServe queryWebAdditionalServeInfo(long hotelId, long roomTypeId,
        Date beginDate, Date endDate, String payMethod) {
        HtlContract contract = contractManage.checkContractDateNew(hotelId, beginDate);
        return queryHotelAdditionalServe(contract.getID(), roomTypeId, beginDate, endDate,
            payMethod);
    }

    // -------酒店附加服务--------------------
    @SuppressWarnings("unchecked")
	public HotelAdditionalServe queryHotelAdditionalServe(long contractId, long roomTypeId,
        Date beginDate, Date endDate, String payMethod) {
    	String dateFormatePattern="MM-dd";
        HotelAdditionalServe hotelAdditionalServe = new HotelAdditionalServe();
        // ----加床价服务
        List<HtlAddBedPrice> bedList = new ArrayList<HtlAddBedPrice>();
        bedList = super.queryByNamedQuery("queryAddBedPriceForWeb", new Object[] { contractId,
            Long.valueOf(roomTypeId).toString(), beginDate, endDate ,payMethod});
        for (int i = 0; i < bedList.size(); i++) {
            HtlAddBedPrice price = bedList.get(i);
            AdditionalServeItem item = new AdditionalServeItem();
            if (null != price.getAddPrice() && price.getAddPrice() > 0.0) {
                item.setAmount(price.getAddPrice());
            
           
            if (null != price.getAddPriceType())
                item.setAddType(price.getAddPriceType().toString());
            if (null != price.getBeginDate()
                && (price.getBeginDate().before(beginDate) 
                        || 0 == beginDate.compareTo(price.getBeginDate()))) {
                if (0 == beginDate.compareTo(price.getEndDate())) {
                    item.setValidDate(DateUtil.formatDateToMD(price.getEndDate(),dateFormatePattern));
                } else if (price.getEndDate().before(endDate)
                    || 0 == endDate.compareTo(price.getEndDate())) {
                    item.setValidDate(DateUtil.formatDateToMD(beginDate,dateFormatePattern) + "至"
                        + DateUtil.formatDateToMD(price.getEndDate(),dateFormatePattern));
                } else {
                    item.setValidDate(DateUtil.formatDateToMD(beginDate,dateFormatePattern) + "至"
                        + DateUtil.formatDateToMD(endDate,dateFormatePattern));
                }
            } else if (null != price.getBeginDate() && price.getBeginDate().before(endDate)) {
                if (price.getEndDate().before(endDate)
                    || 0 == price.getEndDate().compareTo(endDate)) {
                    if (0 == price.getBeginDate().compareTo(price.getEndDate())) {
                        item.setValidDate(DateUtil.formatDateToMD(price.getBeginDate(),dateFormatePattern));
                    } else {
                        item.setValidDate(DateUtil.formatDateToMD(price.getBeginDate(),dateFormatePattern) + "至"
                            + DateUtil.formatDateToMD(price.getEndDate(),dateFormatePattern));
                    }
                } else {
                    item.setValidDate(DateUtil.formatDateToMD(price.getBeginDate(),dateFormatePattern) + "至"
                        + DateUtil.formatDateToMD(endDate,dateFormatePattern));
                }
            } else {
                if (null != price.getBeginDate())
                    item.setValidDate(DateUtil.formatDateToMD(price.getBeginDate(),dateFormatePattern));
            }
            
            hotelAdditionalServe.getBedServes().add(item);
            }
        }
        // ----获取各种早餐服务信息
        List<HtlChargeBreakfast> breakfastList = new ArrayList<HtlChargeBreakfast>();
        breakfastList = super.queryByNamedQuery("queryBreakfastForWeb", 
                                    new Object[] { contractId,payMethod, beginDate, endDate });
        List alist = new ArrayList(); // 中早
        List blist = new ArrayList(); // 西早
        List clist = new ArrayList(); // 自助早
        Set aset = new HashSet(); // 中早
        Set bset = new HashSet(); // 西早
        Set cset = new HashSet(); // 自助早
        for (int i = 0; i < breakfastList.size(); i++) {
            HtlChargeBreakfast breakfast = breakfastList.get(i);
            for (int j = 0; j < breakfast.getBreakfastFees().size(); j++) {
                HtlBreakfast fast = (HtlBreakfast) breakfast.getBreakfastFees().get(j);
                String breakfastType = fast.getType();
                if (null != breakfastType) {
                    if (0 < fast.getBasePrice()) {
                        AdditionalServeItem item = new AdditionalServeItem();
                        item.setAmount(fast.getBasePrice());
                        item.setBreakfastType(fast.getType());
                        if (null != breakfast.getBeginDate()
                            && (breakfast.getBeginDate().before(beginDate) 
                                    || 0 == beginDate.compareTo(breakfast.getBeginDate()))) {
                            if (0 == beginDate.compareTo(breakfast.getEndDate())) {
                                item.setValidDate(DateUtil.formatDateToMD(breakfast.getEndDate(),dateFormatePattern));
                            } else if (breakfast.getEndDate().before(endDate)
                                || 0 == endDate.compareTo(breakfast.getEndDate())) {
                                item.setValidDate(DateUtil.formatDateToMD(beginDate,dateFormatePattern) + "至"
                                    + DateUtil.formatDateToMD(breakfast.getEndDate(),dateFormatePattern));
                            } else {
                                item.setValidDate(DateUtil.formatDateToMD(beginDate,dateFormatePattern) + "至"
                                    + DateUtil.formatDateToMD(endDate,dateFormatePattern));
                            }
                        } else if (null != breakfast.getBeginDate()
                            && breakfast.getBeginDate().before(endDate)) {
                            if (breakfast.getEndDate().before(endDate)
                                || 0 == endDate.compareTo(breakfast.getEndDate())) {
                                if (0 == breakfast.getBeginDate()
                                        .compareTo(breakfast.getEndDate())) {
                                    item.setValidDate(DateUtil.formatDateToMD(breakfast
                                        .getBeginDate(),dateFormatePattern));
                                } else {
                                    item.setValidDate(DateUtil.formatDateToMD(breakfast
                                        .getBeginDate(),dateFormatePattern)
                                        + "至" + DateUtil.formatDateToMD(breakfast.getEndDate(),dateFormatePattern));
                                }
                            } else {
                                if (null != breakfast.getBeginDate())
                                    item.setValidDate(DateUtil.formatDateToMD(breakfast
                                        .getBeginDate(),dateFormatePattern)
                                        + "至" + DateUtil.formatDateToMD(endDate,dateFormatePattern));
                            }
                        } else {
                            item.setValidDate(DateUtil.formatDateToMD(breakfast.getBeginDate(),dateFormatePattern));
                        }

                        if (breakfastType.equals("chineseFood")) {
                            alist.add(item);
                            aset.add(Double.valueOf(item.getAmount()));
                        } else if (breakfastType.equals("westernFood")) {
                            blist.add(item);
                            bset.add(Double.valueOf(item.getAmount()));
                        } else if (breakfastType.equals("buffetDinner")) {
                            clist.add(item);
                            cset.add(Double.valueOf(item.getAmount()));
                        }
                    }
                }
            }
        }
   /*     List lastlist = new ArrayList();
        for (Iterator it = aset.iterator(); it.hasNext();) {
            double b = ((Double) it.next()).doubleValue();
            StringBuffer sb = new StringBuffer();
            for (int ii = 0; ii < alist.size(); ii++) {
                AdditionalServeItem a = (AdditionalServeItem) alist.get(ii);
                if (0 == Double.compare(a.getAmount(),b)) {
                    sb.append(a.getValidDate() + "、");
                }
            }
            String s = sb.substring(0, sb.lastIndexOf("、"));
            AdditionalServeItem ad = new AdditionalServeItem();
            ad.setAmount(b);
            ad.setValidDate(s);
            lastlist.add(ad);
        }*/
        hotelAdditionalServe.setChineseServes(alist);
     /*   lastlist = new ArrayList();
        for (Iterator it = bset.iterator(); it.hasNext();) {
            double b = ((Double) it.next()).doubleValue();
            StringBuffer sb = new StringBuffer();
            for (int ii = 0; ii < blist.size(); ii++) {
                AdditionalServeItem a = (AdditionalServeItem) blist.get(ii);
                if (0 == Double.compare(a.getAmount(),b)) {
                    sb.append(a.getValidDate() + "、");
                }
            }
            String s = sb.substring(0, sb.lastIndexOf("、"));
            AdditionalServeItem ad = new AdditionalServeItem();
            ad.setAmount(b);
            ad.setValidDate(s);
            lastlist.add(ad);
        }*/
        hotelAdditionalServe.setWesternServes(blist);
    /*    lastlist = new ArrayList();
        for (Iterator it = cset.iterator(); it.hasNext();) {
            double b = ((Double) it.next()).doubleValue();
            StringBuffer sb = new StringBuffer();
            for (int ii = 0; ii < clist.size(); ii++) {
                AdditionalServeItem a = (AdditionalServeItem) clist.get(ii);
                if (0 == Double.compare(a.getAmount(),b)) {
                    sb.append(a.getValidDate() + "、");
                }
            }
            String s = sb.substring(0, sb.lastIndexOf("、"));
            AdditionalServeItem ad = new AdditionalServeItem();
            ad.setAmount(b);
            ad.setValidDate(s);
            lastlist.add(ad);
        }*/
        hotelAdditionalServe.setBuffetServes(clist);
        return hotelAdditionalServe;
    }

    /**
     * 系统判断额外显示参考价格日期出现某一天或全部无价或满房或不可超且配额为0时， 系统判断日期能按顺序排列的则额外显示参考价格，如不能按顺序排列（中间有中断的情况），不显示中断后的参考价格。
     * 
     * @param somelist
     * @return
     */
    public List removeSomePrice(List sourceList, Date checkinDate, Date checkoutDate) {
        int beginFlag = -1, endFlag = -1;
        if (1 > sourceList.size()) {
            return sourceList;
        }
        for (int k = 0; k < sourceList.size(); k++) {
            QueryHotelForWebSaleItems item = (QueryHotelForWebSaleItems) sourceList.get(k);
            String closeFlag = item.getClose_flag();
            if (item.getFellowDate().before(checkinDate)) {
                // 无价或关房
                if (0.001 > item.getSalePrice() || StringUtil.isValidStr(closeFlag)
                    && closeFlag.equals("G")) {
                    beginFlag = k;
                    continue;
                }
                // 房态:满房flag,不可超overFlag
                String roomstate = item.getRoomStatus();
                if (!StringUtil.isValidStr(roomstate)) {
                    continue;
                }
                String[] roomArray = roomstate.trim().split("/");
                int flag = 0, overFlag = 0;
                for (int n = 0; n < roomArray.length; n++) {
                    String[] temp = roomArray[n].split(":");
                    if (1 < temp.length) {
                        if (temp[1].equals("4")) {
                            flag++;
                        } else if (temp[1].equals("3")) {
                            overFlag++;
                        }
                    }
                }
                if (flag == roomArray.length
                    || (overFlag == roomArray.length 
                            && 0 == item.getAvailQty())) {// 如果满房,或者不可超并且可用配额数为0
                    beginFlag = k;
                    continue;
                }
            } else if (!item.getFellowDate().before(checkoutDate)) {
                // 无价或关房
                if (0.001 > item.getSalePrice() || StringUtil.isValidStr(closeFlag)
                    && closeFlag.equals("G")) {
                    endFlag = k;
                    break;
                }
                String roomstate = item.getRoomStatus();
                if (null != roomstate) {
                    String[] roomArray = roomstate.trim().split("/");
                    int flag = 0, overFlag = 0;
                    for (int n = 0; n < roomArray.length; n++) {
                        String[] temp = roomArray[n].split(":");
                        if (1 < temp.length) {
                            if (temp[1].equals("4")) {// 如果满房
                                flag++;
                            } else if (temp[1].equals("3")) {// 如果不可超并且可用配额数为0
                                overFlag++;
                            }
                        }
                    }// 如果满房，或者不可超并且可用配额数为0，则不用考虑后面的记录了
                    if (flag == roomArray.length
                        || (overFlag == roomArray.length && 0 == item.getAvailQty())) {
                        endFlag = k;
                        break;
                    }
                }
            }
        }
        List result = new ArrayList();
        int i = -1 == beginFlag ? 0 : beginFlag + 1;
        int j = -1 == endFlag ? sourceList.size() : endFlag;
        for (; i < j; i++) {
            QueryHotelForWebSaleItems it = (QueryHotelForWebSaleItems) sourceList.get(i);
            result.add(it);
        }
        return result;
    }

    public List<OftenDeliveryAddress> queryOftenDeliveryAddress(Long memberID) {
        List<OftenDeliveryAddress> oftenAddressList = new ArrayList<OftenDeliveryAddress>();
        oftenAddressList = (List<OftenDeliveryAddress>) queryDao.queryForList(
            "queryOftenDeliveryAddress", memberID);
        return oftenAddressList;
    }

    /**
     * 根据给定主题编号查询有该主题的城市
     * 
     * @param theme
     * @return
     */
    public List<Object[]> queryThemeCity(String theme) {
        // String hsql = "select distinct h.city from HtlHotel h,HtlPrice p where h.theme like '%" +
        // theme + "%'";
        // List list = super.doquery(hsql, false);
        String sql = "select distinct h.city from V_HOTEL_ABLE_THEME h" + " where h.theme like '%"
            + theme + "%' ";
        List list = super.getSession().createSQLQuery(sql).list();
        if (null == list || 0 == list.size()) {
            return null;
        }
        List<Object[]> results = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            String city = (String) list.get(i);
            Object[] res = new Object[2];
            res[0] = city;
            res[1] = InitServlet.cityObj.get(city);
            results.add(res);
        }
        return results;
    }

    /**
     * v2.4查询主题酒店
     * 
     * @param queryBean
     *            查询条件
     * @return 查询结果
     */
    public HotelPageForWebBean queryThemeHotelsForWeb(QueryHotelForWebBean queryBean) {
        HotelPageForWebBean hotelPageForWebBean = new HotelPageForWebBean();
        List<QueryHotelForWebResult> list = new ArrayList<QueryHotelForWebResult>();

        int pageNo = 1;
        // 当指定跳到哪一页时
        if (0 != queryBean.getPageIndex()) {
            pageNo = queryBean.getPageIndex();
        }

        // 每页显示数
        int pageSize = queryBean.getPageSize();

        List<QueryHotelForWebResult> results = null;
        Integer totalSize = new Integer(0); // 查询结果后返回总记录数
        //查询主题酒店 
        Map resultMap = hwebHotelDao.queryThemeHotelsForWeb(queryBean);
        
        //从查询结果Map中取数据
        totalSize = (Integer) resultMap.get(KEY_TOTAL_SIZE);
        results = (List<QueryHotelForWebResult>) resultMap.get(KEY_RESULT_LIST);

        if (null != results) {
            // 计算总页数
            int totalIndex = 0;
            if (0 != totalSize % pageSize) {
                totalIndex = totalSize / pageSize + 1;
            } else {
                totalIndex = totalSize / pageSize;
            }
            hotelPageForWebBean.setTotalIndex(totalIndex);
            hotelPageForWebBean.setPageSize(pageSize);
            hotelPageForWebBean.setPageIndex(pageNo);

            QueryHotelForWebResult queryHotelForWebResult = null;
            long tempHotelId = 0;
            if (0 != results.size() && null != results.get(0)) {
                tempHotelId = results.get(0).getHotelId();
            }
            boolean readFlag = false;
            for (QueryHotelForWebResult info : results) {
                if (info.getHotelId() == tempHotelId && readFlag) // 跳过同一酒店的记录
                {
                    continue;
                }
                readFlag = true;
                tempHotelId = info.getHotelId();

                // 每个酒店信息
                queryHotelForWebResult = new QueryHotelForWebResult();
                queryHotelForWebResult.setHotelId(tempHotelId);
                queryHotelForWebResult.setCommendType(info.getCommendType());

                queryHotelForWebResult.setHotelStar(info.getHotelStar());
                queryHotelForWebResult.setRoomTypes(null);
                queryHotelForWebResult.setHotelChnName(info.getHotelChnName());
                queryHotelForWebResult.setHotelEngName(info.getHotelEngName());
                queryHotelForWebResult.setAutoIntroduce(info.getAutoIntroduce());
                queryHotelForWebResult.setPraciceYear(info.getPraciceYear());
                queryHotelForWebResult.setFitmentYear(info.getFitmentYear());
                queryHotelForWebResult.setBizZone(info.getBizZone());
                queryHotelForWebResult.setDistrict(info.getDistrict());

                // 主要设施图标
                queryHotelForWebResult.setForPlane(info.getForPlane());
                queryHotelForWebResult.setFreeForPlane(info.isFreeForPlane());
                queryHotelForWebResult.setForFreeStop(info.getForFreeStop());
                queryHotelForWebResult.setForFreePool(info.getForFreePool());
                queryHotelForWebResult.setForFreeGym(info.getForFreeGym());
                queryHotelForWebResult.setForNetBand(info.getForNetBand());

                // 酒店交通信息
                queryHotelForWebResult.setTrafficInfo(info.getTrafficInfo());

                // 芒果促销信息
                queryHotelForWebResult.setHasPreSale(info.getHasPreSale());
                if (1 == queryHotelForWebResult.getHasPreSale()) {
                    queryHotelForWebResult.setPreSaleName(info.getPreSaleName());
                    queryHotelForWebResult.setPreSaleContent(info.getPreSaleContent());
                    queryHotelForWebResult.setPreSaleBeginEnd(info.getPreSaleBeginEnd());
                    queryHotelForWebResult.setPreSaleURL(info.getPreSaleURL());
                }

                // 得到酒店图片
                queryHotelForWebResult.setOutPictureName(info.getOutPictureName());
                queryHotelForWebResult.setHallPictureName(info.getHallPictureName());
                queryHotelForWebResult.setRoomPictureName(info.getRoomPictureName());
                queryHotelForWebResult.setHotelLogo(info.getHotelLogo());

                // 获取3D图片数量
                queryHotelForWebResult.setSandNum(info.getSandNum());

                // 评论分数
                queryHotelForWebResult.setGeneralPoint(info.getGeneralPoint());

                /*
                 * if (CurrencyBean.RMB.equals(info.getCurrency())) {
                 * queryHotelForWebResult.setRateStr("1"); } else { if (ma == null) { rateStr = "1";
                 * } else { rateStr = (String)ma.get(info.getCurrency()); if(rateStr == null) {
                 * rateStr = "1"; } } queryHotelForWebResult.setRateStr(rateStr); }
                 * queryHotelForWebResult.setCurrency(info.getCurrency());
                 */
                queryHotelForWebResult.setSalesPrice(info.getSalesPrice());
                list.add(queryHotelForWebResult);
            }

            queryHotelForWebResult = null;
        }
        // 强行设置网站币种汇率，为港币汇率（目前只考虑港币情况）
        hotelPageForWebBean.setRateStr(getCurrenyRate(CurrencyBean.HKD));

        hotelPageForWebBean.setList(list);
        results = null;
        list = null;
        return hotelPageForWebBean;
    }
    
    
    public List queryAllHotleActive() {
    	String sql="select hotel_id,chn_name,sale_price,scale from("+
    			"select hotel_id,chn_name,sale_price,scale from("+
    			" select hh.hotel_id,hh.chn_name,"+
    			" hlp.sale_price ,hft.return_scale,hlp.formulaid,hlp.commission,hlp.commission_rate,(case when (hft.return_scale <1) then"+
    			" (case when(hlp.formulaid = '0' or hlp.formulaid='pricef')  then trunc(hlp.sale_price*hft.return_scale)" +
    			" when(hlp.commission>0 and hlp.commission_rate>0) then trunc(hlp.commission/hlp.commission_rate*hft.return_scale)"+
		         " else 0 end) else  hft.return_scale end) scale "+
		         " from htl_hotel hh,htl_favourable_return hft,htl_lowest_pric hlp "+
		         " where hh.hotel_id=hlp.hotel_id and hh.hotel_id=hft.hotel_id"+
		         " and hft.pricetype_id=hlp.child_room_type_id"+
		         " and hlp.able_sale_date=trunc(sysdate)+1"+
		         " and hft.begindate<=trunc(sysdate)+1"+
                 " and hft.enddate>=trunc(sysdate)+1"+
		         " and hlp.sale_price > 0"+
		         " and hlp.sale_price < 999999.0"+
		         " )"+
		         " order by hotel_id"+
		         " )group by hotel_id,chn_name,sale_price,scale";
    	
    	log.info(sql);
		return super.doquerySQL(sql, false);
	}
    /************ getter and setter begin *********************/

    public DAOIbatisImpl getQueryDao() {
        return queryDao;
    }

    public void setQueryDao(DAOIbatisImpl queryDao) {
        this.queryDao = queryDao;
    }

    public ContractManage getContractManage() {
        return contractManage;
    }

    public void setContractManage(ContractManage contractManage) {
        this.contractManage = contractManage;
    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

    public HKService getHkService() {
        return hkService;
    }

    public void setHkService(HKService hkService) {
        this.hkService = hkService;
    }

    // --------------香港中科-------------------//
    public List<QueryHotelForWebSaleItems> queryPriceForWebHK(long hotelId, long roomTypeId,
        long childRoomTypeId, Date beginDate, Date endDate) {
        List<HKRoomAmtResponse> hkResponseList = hkService.enqHKAmtByNation(hotelId, beginDate,
            endDate, roomTypeId, Long.toString(childRoomTypeId));
        // 需要对返回对象进行重新组装以便适合现有网站代码的处理
        List<QueryHotelForWebSaleItems> itemList = new ArrayList<QueryHotelForWebSaleItems>();
        for (HKRoomAmtResponse response : hkResponseList) {
            QueryHotelForWebSaleItems item = new QueryHotelForWebSaleItems();
            item.setFellowDate(DateUtil.toDateByFormat(response.getDate(), "yyyy-MM-dd"));
            item.setPriceId(Long.parseLong(response.getChildRoomTypeId()));
            item.setWeekDay(response.getDayIndex());
            // //销售价
            item.setSalePrice(response.getListAmt());
            // //低价
            item.setBasePrice(response.getBaseAmt());
            // 测试网上银行交易
            // item.setSalePrice(1d);
            // 低价
            // item.setBasePrice(1d);
            itemList.add(item);
        }
        // 添加离店日期的一个对象，方便显示
        /*
         * QueryHotelForWebSaleItems it = new QueryHotelForWebSaleItems();
         * it.setFellowDate(endDate); itemList.add(it);
         */
        return itemList;
    }

    /**
     * 网站查配额
     * @param hotelId
     * @param roomTypeId
     * @param beginDate
     * @param endDate
     * @return
     */
    public List<Date> queryQtyForWebHK(long hotelId, long roomTypeId,
			Date beginDate, Date endDate) {
		return hkService.enqRoomQtyForWeb(hotelId, beginDate, DateUtil.getDate(endDate, -1),
				roomTypeId);
	}
    
    /**
     * 获取可能的预订日期字符串
     * @param hotelId
     * @param roomTypeId
     * @param childRoomTypeId
     * @param checkIn
     * @param checkOut
     * @return
     */
    public String getCanBookDatesForWebHK(Long hotelId, Long roomTypeId,
            Long childRoomTypeId, Date checkIn, Date checkOut) {
    	StringBuffer sb = new StringBuffer("");
    	Date today = DateUtil.getDate(new Date());
    	Date preDate = DateUtil.getDate(checkIn, -5);
    	if(preDate.before(today)) {
    		preDate = today;
    	}
    	Date aftDate = DateUtil.getDate(checkOut, 5);
    	int nTotal = DateUtil.getDay(preDate, aftDate);
    	boolean bChk[] = new boolean[nTotal];    	
    	String sql = " select distinct q.able_sale_date from hwtemp_htl_quota_new q where hotel_id = ? "
				+ " and roomtype_id = ? and "
				+ " (able_sale_date >= ? and able_sale_date < ? or "
				+ " able_sale_date >= ? and able_sale_date < ?) "
				+ " and quota_share_type = 2 and common_quota_able_num > 0 "
				+ " and not exists (select c.htl_quota_detail_id from htl_quota_cutoff_day_new c where quota_id = q.htl_quota_new_id "
				+ " and c.outofdate < sysdate) order by q.able_sale_date ";    	
    	List<Date> liRes = super.doquerySQL(sql, new Object[]{hotelId, roomTypeId, preDate, checkIn, 
    			checkOut, aftDate}, false);
    	for(int i=0; i<nTotal; i++) {
    		bChk[i] = false;
    	}
    	if(null != liRes) {
        	for(Date date : liRes) {
        		int i = DateUtil.getDay(preDate, date);
        		if(0 <= i) {
        			bChk[i] = true;
        		}
        	}	
    	}
    	sql = " select distinct p.able_sale_date from hwtemp_htl_price p where hotel_id = ? "
				+ " and room_type_id = ? and child_room_type_id = ? "
				+ " and (able_sale_date >= ? and able_sale_date < ? or "
				+ " able_sale_date >= ? and able_sale_date < ?) and sale_price > 0 "
				+ " and sale_price < 99999 and pay_method = 'pre_pay' order by able_sale_date ";
		liRes = super.doquerySQL(sql, new Object[] { hotelId, roomTypeId,
				childRoomTypeId, preDate, checkIn, checkOut, preDate }, false);
    	if(null != liRes) {
        	for(Date date : liRes) {
        		int i = DateUtil.getDay(preDate, date);
        		if(0 <= i) {
        			bChk[i] = true;
        		}
        	}
    	}
    	boolean bFirst = true;
    	for(int i=0; i<nTotal; i++) {
    		if(bChk[i]) {
    			Date date = DateUtil.getDate(preDate, i);
    			if(!bFirst) {
    				sb.append(",");    				
    			}
    			sb.append(DateUtil.toStringByFormat(date, "MM-dd"));
    			bFirst = false;
    		}
    	}
    	return sb.toString();
    }
            
    
    /**
     * ADD BY WUYUN 2009-03-25 获取香港中科酒店的配额 获取失败时需要释放之前已经获取成功的配额
     */
    public int checkQuotaForWebHK(OrOrder order) {
        List<OrChannelNo> orderList = order.getChannelList();
        // 标志，0:默认成功，1:接口异常，2:确实无配额
        int result = 0;
        boolean bSuc = true;
        int size = orderList.size();
        for (int i = 0; i < size; i++) {
            OrChannelNo orderChannel = orderList.get(i);
            // 调中科接口，产生交易号
            BeginData beginData = hkService.saleBegin();
            // 如果交易出错，设置订单状态为“取消”
            if (0 > beginData.getNRet()) {
                String error = beginData.getSMessage();
                // 记录取消订单原因
                order.setCancelMessage(error);
                result = 1;
                bSuc = false;
            } else {// 交易成功
                // 中科交易号
                String sTxnNo = beginData.getSTxnNo();
                orderChannel.setOrderChannel(sTxnNo);
                BasicData ret = new BasicData();
                try {
                    // 扣配额
                    ret = hkService.holdRoom(sTxnNo, order.getHotelId(), order.getCheckinDate(),
                        order.getCheckoutDate(), order.getRoomTypeId(), order.getChildRoomTypeId(),
                        orderChannel.getQuantity());
                    // 如果没有成功
                    if (ret.getNRet() < ResultConstant.RESULT_SUCCESS) {
                        logger.error("中旅订单hold配额失败, 芒果订单编号:" + order.getOrderCD() + ", 中旅订单号:"
                            + orderChannel.getOrderChannel() + ", WebService错误代码: " + ret.getNRet()
                            + ", 错误信息:" + ret.getSMessage());
                        bSuc = false;
                        result = 2;
                        log.info("HotelManageWebImpl==============checkQuotaForWebHK=====hold Quota failed=");
                        // 记录扣配额失败原因
                        order.setCancelMessage(ret.getSMessage());
                    }
                } catch (Exception e) {
                    bSuc = false;
                    result = 1;
                    log.error("中旅订单hold配额出异常!");
                    log.error(e.getMessage(),e);
                }
            }
            // 如果接口出错或扣配额不成功，则需要回滚之前已经成功获取配额的订单
            if (!bSuc&&2==result) {
                for (int j = 0; j < i; j++) {
                    OrChannelNo rollChannel = orderList.get(j);
                    try {
                        BasicData retRoll = hkService.saleRollback(rollChannel.getOrderChannel());
                        if (retRoll.getNRet() < ResultConstant.RESULT_SUCCESS) {
                            log.error("释放中旅订单配额失败, 芒果订单编号:" + order.getOrderCD() + ", 中旅订单号:"
                                + rollChannel.getOrderChannel() + ", WebService错误代码: "
                                + retRoll.getNRet() + ", 错误信息:" + retRoll.getSMessage());
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                // 需要取消该订单，并记录原因
                order.setOrderState(OrderState.CANCEL);
                break;
            }
        }
        return result;
    }

    /**
     * ADD BY WUYUN 2009-03-25
     */
    public int saleAddCustInfo(OrOrder order) {
        List<OrChannelNo> liC = order.getChannelList();
        // 网站没有特殊要求
        String remark = "";
        // 标志，0:默认成功，1:接口异常，2:不成功
        int result = 0;
        boolean bSuc = true;
        for (OrChannelNo orderChannel : liC) {
            String sTxnNo = orderChannel.getOrderChannel();
            String[] names = orderChannel.getFellows().split("#");
            List<CustInfo> liCust = new ArrayList<CustInfo>();
            for (int i = 0; i < names.length; i++) {
            	log.info("HotelManageWebImpl==============orderChannel.getFellows.name=====snames[i] :"+names[i]);
                CustInfo custom = new CustInfo();
                custom.setSName(names[i]);
                // 没有入住人的电话信息，但必须设置成空字符串，如果为NULL接口将调不通
                custom.setSPhone("");
                liCust.add(custom);
            }
            BasicData ret = new BasicData();
            try {
                ret = hkService.saleAddCustInfo(sTxnNo, liCust, remark);
                if (ret.getNRet() < ResultConstant.RESULT_SUCCESS) {
                    log.error("中旅订单添加入住人信息失败, 芒果订单编号:" + order.getOrderCD() + ", 中旅订单号:"
                        + orderChannel.getOrderChannel() + ", WebService错误代码: " + ret.getNRet()
                        + ", 错误信息:" + ret.getSMessage());
                    bSuc = false;
                    log.info("HotelManageWebImpl==============saleAddCustInfo=====sai fellow failed=");
                    result = 2;
                }
            } catch (Exception e) {
                bSuc = false;
                result = 1;
                log.error("中旅订单添加入住人信息出异常!");
                log.error(e.getMessage(),e);
            }
        }

        // 如果中旅订单添加入住人信息失败
//        if (!bSuc) {
//            // 回滚释放之前hold的配额
//            for (OrChannelNo rollChannel : liC) {
//                try {
//                    BasicData retRoll = hkService.saleRollback(rollChannel.getOrderChannel());
//                    if (retRoll.getNRet() < ResultConstant.RESULT_SUCCESS) {
//                        log.error("释放中旅订单配额失败, 芒果订单编号:" + order.getOrderCD() + ", 中旅订单号:"
//                            + rollChannel.getOrderChannel() + ", WebService错误代码: "
//                            + retRoll.getNRet() + ", 错误信息:" + retRoll.getSMessage());
//                    }
//                } catch (Exception e2) {
//                    e2.printStackTrace();
//                }
//            }
//            // 记录操作日志
//            // 需要取消该订单，并记录原因
//            order.setOrderState(OrderState.CANCEL);
//            order.setCancelMessage(HkConstant.ADD_CUSTOMER_FIAL);
//        }
        return result;
    }

    /**
     * ADD BY WUYUN 2009-03-26
     */
    public boolean rollbackForWebHK(OrOrder order) {
        List<OrChannelNo> orChannelList = order.getChannelList();
        boolean success = true;
        for (OrChannelNo item : orChannelList) {
            try {
                BasicData basicData = hkService.saleRollback(item.getOrderChannel());
                if (basicData.getNRet() < ResultConstant.RESULT_SUCCESS) {
                    success = false;
                    item.setStatus(basicData.getNRet());
                    log.error("释放中旅订单配额失败, 芒果订单编号:" + order.getOrderCD() + ", 中旅订单号:"
                        + item.getOrderChannel() + ", WebService错误代码: " + basicData.getNRet()
                        + ", 错误信息:" + basicData.getSMessage());
                }
            } catch (Exception e) {
            	log.error(e.getMessage(),e);
            }
        }
        return success;
    }

    /**
     * ADD BY WUYUN 2009-03-26 确认交易时首先需要查询香港中科方的交易状态，有可能对方的配额已经释放
     */
    public boolean saleCommitFowWebHK(OrOrder order) {

        List<OrChannelNo> orChannelList = order.getChannelList();

        boolean success = true;
        boolean confirmSuccess = true;
        if (null != orChannelList && 0 < orChannelList.size()) {
            for (OrChannelNo item : orChannelList) {
                try {
                    // 调中科接口查交易状态
                    TxnStatusData status = hkService.enqTxnStatus(item.getOrderChannel());
                    int nRet = status.getNRet();
                    item.setStatus(nRet);
                    // 交易成功
                    if (nRet >= ResultConstant.RESULT_SUCCESS) {
                        // 已经回滚
                        if (nRet == TxnStatusType.Rollbacked) {
                            success = false;
                        }
                        continue;
                    } else {// 失败
                        // 记录操作日志
                        OrHandleLog handleLog = new OrHandleLog();
                        handleLog.setModifiedTime(new Date());
                        handleLog.setContent(DateUtil.datetimeToString(new Date()) + " 交易号"
                            + item.getOrderChannel() + HkConstant.SALE_FIAL);
                        handleLog.setModifierName("网站");
                        handleLog.setOrder(order);
                        order.getLogList().add(handleLog);
                        success = false;
                    }
                } catch (Exception e) {
                	log.error(e.getMessage(),e);
                }
            }
            // 已经支付了，且配额没有被释放，则确认交易
            if (success) {
                for (OrChannelNo channelItem : orChannelList) {
                    // 如果已经确认，则不需再确认了
                    if (channelItem.getStatus() == TxnStatusType.Commited) {
                        continue;
                    }                    
                    
                    boolean bSuc = true;
                    BasicData basicData = null;
                    try {
                        basicData = hkService.saleCommit(channelItem.getOrderChannel(),
                                channelItem.getQuantity());                    	
                    } catch (Exception e1) { // 如果提交出异常
                    	basicData.setNRet(ResultConstant.RESULT_FAIL);
                        e1.printStackTrace();
                        log.error("提交中旅订单失败，芒果订单编号:" + order.getOrderCD() + ", 中旅订单号:"
                                    + channelItem.getOrderChannel()+e1);
                        bSuc = false;
                    }
                    
                    // 确认是否成功                    
                    if (bSuc && null != basicData && basicData.getNRet() < ResultConstant.RESULT_SUCCESS) {
                    	bSuc = false;
                    }
                    
                    if(bSuc) {
                        // 查询中旅订单状态
                        try {
                        	Thread.sleep(100); // 先等0.1秒再查询
                            TxnStatusData eRet = hkService
    								.enqTxnStatus(channelItem.getOrderChannel());
    						int nRet = eRet.getNRet();    						
    						if (TxnStatusType.Rollbacked == nRet
    								|| TxnStatusType.Commited != nRet) {
    							bSuc = false;
    						} else if (nRet < ResultConstant.RESULT_SUCCESS) {
    							bSuc = false;
    						}
                        	log.info("提交中旅订单后查询中旅订单状态, 芒果订单编号:" + order.getOrderCD() + ", 中旅订单号:"
                                    + channelItem.getOrderChannel() + ",状态:" + nRet);
                        } catch (Exception e) {
                        	log.error("提交中旅订单后查询中旅订单状态失败, 芒果订单编号:" + order.getOrderCD() + ", 中旅订单号:"
                                    + channelItem.getOrderChannel());
                        	log.error(e.getMessage(),e);
                            bSuc = false;                        
                        }
                    }
                    
                    if(bSuc) {
                        channelItem.setStatus(TxnStatusType.Commited);   
                        channelItem.setCommitTime(new Date()); // 设置提交成功时间
                        continue;
                    } else {// 不成功
                        // 记录操作日志
                        OrHandleLog handleLog = new OrHandleLog();
                        handleLog.setModifiedTime(new Date());
                        handleLog.setContent(DateUtil.datetimeToString(new Date()) + " 交易号"
                            + channelItem.getOrderChannel() + HkConstant.CONFIRM_SALE_FIAL);
                        handleLog.setModifierName("网站");
                        handleLog.setOrder(order);
                        order.getLogList().add(handleLog);
                        confirmSuccess = false;
                    }
                }
            }
        } else {
            success = false;
            confirmSuccess = false;
        }
        return success && confirmSuccess;
    }

    /**
     * hotel2.6 根据订单，取出该订单的预订条款，担保条款，取消修改条款等 并组合成字符串
     * 
     * @param orOrder
     * @param bookhintSpanValue
     *            组合担保条款的字符串
     * @param cancelModifyItem
     *            组合修改取消条款的字符串
     * @param telephoneStr
     *            电话号码（组合字符串用），如40066 40066 或 80099 90999 add by chenjiajie 2009-05-25
     */
    public void getReservationHintForWeb(OrOrder orOrder, StringBuffer bookhintSpanValue,
        StringBuffer cancelModifyItem, String telephoneStr) {
        OrReservation reservation = null;
        ReservationAssist reservationAssist = null;
        if (null != orOrder) {
            List<OrPriceDetail> priceList = orOrder.getPriceList();
            if (priceList.isEmpty()) {
                // 填充每天的价格
                List<OrOrderItem> orderItemList = orOrder.getOrderItems();
                int size = orderItemList.size();
                for (int p = 0; p < size; p++) {
                    OrOrderItem orderItem = orderItemList.get(p);
                    OrPriceDetail orPriceDetail = new OrPriceDetail();
                    orPriceDetail.setNight(orderItem.getNight());
                    orPriceDetail.setDayIndex(p);
                    orPriceDetail.setSalePrice(orderItem.getSalePrice());
                    orPriceDetail.setOrder(orOrder);
                    orOrder.getPriceList().add(orPriceDetail);
                }
            }
            if(ChannelType.CHANNEL_ELONG != orOrder.getChannel()){
            	try {
                    reservationAssist = orderService.loadBookClauseForWeb(orOrder);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }  
            // orderEditService.getReservationInfo(orOrder, true);
            reservation = orOrder.getReservation();
        }

        // 担保信息封装
        if (null != reservation) {
            // 信息封装
            if (null == bookhintSpanValue) {
                bookhintSpanValue = new StringBuffer();
            }
            // 支付方式
            String payMentType = orOrder.getPayMethod();

            /** 信用卡担保条款 begin **/
            // 如果是面付
            if (PayMethod.PAY.equals(payMentType)) {
                if (reservation.isUnCondition()) {
                    bookhintSpanValue.append("该时段内入住该房型，需按酒店要求提供信用卡担保。");
                } else if (reservation.isOverTimeAssure() && reservation.isRoomsAssure()) {
                    bookhintSpanValue.append("入住时间超过酒店规定最晚时间 " + reservation.getLateSuretyTime()
                        + " 或预订该房型超过 " + reservation.getRooms() + " 间房以上，需按酒店要求提供信用卡担保。 ");
                } else if (reservation.isOverTimeAssure()) {
                    bookhintSpanValue.append("入住时间超过酒店规定最晚时间 " + reservation.getLateSuretyTime()
                        + " ，需按酒店要求提供信用卡担保。");
                } else if (reservation.isRoomsAssure()) {
                    bookhintSpanValue.append("预订该房型超过 " + reservation.getRooms()
                        + " 间房以上，需按酒店要求提供信用卡担保。 ");
                }
            }
            /** 信用卡担保条款 end **/
            // 担保提示信息   9为艺龙担保，--add by wangjian
            /** 修改，取消条款 begin **/
           if(ChannelType.CHANNEL_ELONG == orOrder.getChannel()){
        	   List liRes = new ArrayList();
               if (null != reservation) {
                   liRes = orderEditService.getModifyCancelStr2(orOrder, reservation.getID(),
                       orOrder.getSumRmb());
                   if (null != liRes && !liRes.isEmpty()) {
                	   if (null == cancelModifyItem) {
                           cancelModifyItem = new StringBuffer();
                       }
                  	 cancelModifyItem.append(liRes.get(0).toString());
                   }
               }
            }else if (null != reservationAssist) {
                if (null == cancelModifyItem) {
                    cancelModifyItem = new StringBuffer();
                }
                // 取消修改显示类型不为空，并且是面付
                if (StringUtil.isValidStr(reservationAssist.getCancmodiType())
                    && PayMethod.PAY.equals(payMentType)) {
                    // 取消修改显示类型
                    String cancmodiType = reservationAssist.getCancmodiType();
                    // 1、不接受免费取消
                    if ("1".equals(cancmodiType)) {
                        cancelModifyItem.append("该房型一旦预订并确认成功将不接受免费取消，如需取消将按酒店规定比例扣取您的担保金额。");
                    }
                    // 2、不接受免费修改
                    else if ("2".equals(cancmodiType)) {
                        cancelModifyItem.append("该房型一旦预订并确认成功将不接受免费修改，如需修改将按酒店规定比例扣取您的担保金额。");
                    }
                    // 3、不接受免费取消或修
                    else if ("3".equals(cancmodiType)) {
                        cancelModifyItem.append("该房型一旦预订并确认成功将不接受免费取消或修改，如需取消或修改将按酒店规定比例扣取您的担保金额。");
                    }
                    // 4、请您务必于某日期前提出修改取消
                    else {
                        Date beforeDate = reservationAssist.getEarliestNoPayDate();
                        String beforeTime = reservationAssist.getEarliestNoPayTime();
                        Date earlyDate = DateUtil.stringToDatetime(DateUtil
                            .dateToString(beforeDate)
                            + " " + beforeTime);
                        isBeforeCanModTime = Float.valueOf(((float) (earlyDate.getTime() - Calendar
                            .getInstance().getTime().getTime()) / 86400000 + 1)).intValue();
                        if (0 < isBeforeCanModTime) {
                            cancelModifyItem.append("需取消或修改本次预订，请您务必于 ");
                            cancelModifyItem.append(DateUtil.dateToString(beforeDate));
                            cancelModifyItem.append(" " + beforeTime);
                            cancelModifyItem.append("前致电 " + telephoneStr
                                + " 提出变更，否则将按酒店规定比例扣取您的担保金额。");
                        } else {
                            cancelModifyItem
                                .append("该房型一旦预订并确认成功将不接受免费取消或修改，如需取消或修改将按酒店规定比例扣取您的担保金额。");
                        }
                    }
                }
                // 预付时，不管有没有取消修改条款都显示一句话
                if (!PayMethod.PAY.equals(payMentType)) {
                    /*
                     * //取消修改显示类型 String cancmodiType = reservationAssist.getCancmodiType();
                     * //1、不接受免费取消 if("1".equals(cancmodiType)){
                     * cancelModifyItem.append("该房型一旦预订并确认成功将不接受免费取消，如需修改将按酒店规定比例扣取您的预付金额。"); }
                     * //2、不接受免费修改 else if("2".equals(cancmodiType)){
                     * cancelModifyItem.append("该房型一旦预订并确认成功将不接受免费修改，如需取消将按酒店规定比例扣取您的预付金额。"); }
                     * //3、不接受免费取消或修 else if("3".equals(cancmodiType)){
                     * cancelModifyItem.append("该房型一旦预订并确认成功将不接受免费取消或修改，如需取消或修改将按酒店规定比例扣取您的预付金额。");
                     * } //4、请您务必于某日期前提出修改取消 else{ Date beforeDate =
                     * reservationAssist.getEarliestNoPayDate(); String beforeTime =
                     * reservationAssist.getEarliestNoPayTime();
                     * cancelModifyItem.append("需取消或修改本次预订，请您务必于");
                     * cancelModifyItem.append(DateUtil.dateToString(beforeDate));
                     * cancelModifyItem.append(beforeTime); cancelModifyItem.append("前致电" +
                     * telephoneStr + "提出变更，否则将按酒店规定比例扣取您的预付金额。"); }
                     */
                    cancelModifyItem.append("该房型一旦预订确认成功将不接受免费取消或修改，如需取消或修改将按酒店规定扣取您的全部预付金额。");
                }
            }
            /** 修改，取消条款 end **/
        }
    }

    /**
     * 电子地图二期开发 add by haibo.li
     */
    public Map queryMapskmlGenerator(List webHotelResultLis, int sourceType, int dataType,
        String path) {
        try {

            List<MapsInfo> mapInfoLst = new ArrayList<MapsInfo>();
            for (int i = 0; i < webHotelResultLis.size(); i++) {
                QueryHotelForWebResult queryhotelResult = (QueryHotelForWebResult) webHotelResultLis
                    .get(i);
               if((null!=queryhotelResult)&&(queryhotelResult.getGisid()>0)){
	                MapsInfo maps = new MapsInfo();
	                maps.setGisId(queryhotelResult.getGisid());
	                maps.setName(queryhotelResult.getHotelChnName());
	                // maps.setDescription(this.getDescription(queryhotelResult));
	                maps.setImageUrl(path + "/images/emap/mango.gif");
	                maps.setImageInfo(String.valueOf(i + 1));
	                mapInfoLst.add(maps);
               }
            }
            // 调用jar内方法返回地图map
            Map map = gisService.kmlGenerator(mapInfoLst, sourceType, dataType, path);
            return map;
        } catch (Exception e) {
            log.error("电子地图读取酒店数据异常!" + e);
            return null;
        }
    }

    /**
     * 取得酒店id集合的方法 add by haibo.li 电子地图二期
     */
    public String getHotelIdLst(String gisidLst) {
        gisidLst = gisidLst.substring(0, gisidLst.length() - 1);
        String hotelIdLst = "";
        List Hotellist = hotelManage.queryHtlHotelList(gisidLst);
        for (int i = 0; i < Hotellist.size(); i++) {
            HtlHotel htl = (HtlHotel) Hotellist.get(i);
            hotelIdLst += htl.getID() + ",";
        }
        if(null!=hotelIdLst&&!"".equals(hotelIdLst)){
        	hotelIdLst = hotelIdLst.substring(0, hotelIdLst.length() - 1);
        }
        return hotelIdLst;
    }

    public OrParam getHalfhoutTimeLimit() {
        String hsql = "from OrParam a where a.name = 'HALFHOUR_TIME_LIMIT' ";
        OrParam param = (OrParam) find(hsql);
        return param;
    }

    public OrParam getIpsServiceFee() {
        String hsql = "from OrParam a where a.name = 'IPS_SERVICE_FEE' ";
        OrParam param = (OrParam) find(hsql);
        return param;
    }

    public IOrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(IOrderService orderService) {
        this.orderService = orderService;
    }

    public IOrderEditService getOrderEditService() {
        return orderEditService;
    }

    public void setOrderEditService(IOrderEditService orderEditService) {
        this.orderEditService = orderEditService;
    }

    public GisService getGisService() {
        return gisService;
    }

    public void setGisService(GisService gisService) {
        this.gisService = gisService;
    }

	public double calculateSuretyAmount(
			List<AssureInforAssistant> assureDetailLit,
			HotelOrderFromBean hotelOrderFromBean) {
		double assureAmout = 0.0;
        AssureInforAssistant assureInforAssistantFrist = assureDetailLit
            .get(0);
        //担保规则 check in day
        if ("1".equals(assureInforAssistantFrist.getAssureRule())) {
        	//是无条件担保
            if (assureInforAssistantFrist.isUnconditionAssure()) {
                assureAmout = assureInforAssistantFrist.getUnconditionAssureAmount();
            } 
            //是超时担保 
            else if (assureInforAssistantFrist.isOverTimeAssure()
                && 0 == DateUtil.getDay(hotelOrderFromBean.getCheckinDate(),
                    assureInforAssistantFrist.getAssureDate())
                && satisfiyCodition("1", assureInforAssistantFrist.getOverTimeStr(),
                    hotelOrderFromBean, assureInforAssistantFrist.getAssureDate())) {
                assureAmout = assureInforAssistantFrist.getOverTimeAssureAmount();
            } 
            //是超房担保 
            else if (assureInforAssistantFrist.isOverRoomsAssure()
                && 0 == DateUtil.getDay(hotelOrderFromBean.getCheckinDate(),
                    assureInforAssistantFrist.getAssureDate())
                && satisfiyCodition("2", assureInforAssistantFrist.getOverRoomsNum(),
                    hotelOrderFromBean, assureInforAssistantFrist.getAssureDate())) {
                assureAmout = assureInforAssistantFrist.getOverRoomsAssureAmount();
            }
            //是超间夜担保
            else if(assureInforAssistantFrist.isOverNightsAssure()
		            &&DateUtil.getDay(hotelOrderFromBean.getCheckinDate(),assureInforAssistantFrist.getAssureDate())==0
					&&satisfiyCodition("3",assureInforAssistantFrist.getOverNightsNum(),hotelOrderFromBean,assureInforAssistantFrist.getAssureDate())){
            	assureAmout = assureInforAssistantFrist.getOverNightsAssureAmount();
            }
        } 
        //担保规则 全额
        else if ("2".equals(assureInforAssistantFrist.getAssureRule())) {
            for (Iterator<AssureInforAssistant> assureDetailIterator = assureDetailLit.iterator(); assureDetailIterator.hasNext();) {
                AssureInforAssistant assureInforAssistant = assureDetailIterator.next();
                //担保类型 2:首日
                if ("2".equals(assureInforAssistant.getAssureType())
                    && 0 == Double.compare(0.0, assureAmout)) {
                	//是无条件担保
                    if (assureInforAssistant.isUnconditionAssure()) {
                        assureAmout = assureInforAssistant.getFristDayAssureAmount();
                    } 
                    //是超时担保 
                    else if (assureInforAssistant.isOverTimeAssure()
                        && 0 == DateUtil.getDay(hotelOrderFromBean.getCheckinDate(),
                            assureInforAssistant.getAssureDate())
                        && satisfiyCodition("1", assureInforAssistant.getOverTimeStr(),
                            hotelOrderFromBean, assureInforAssistant.getAssureDate())) {
                        assureAmout = assureInforAssistant.getFristDayAssureAmount();
                    } 
                    //是超房担保 
                    else if (assureInforAssistant.isOverRoomsAssure()
                        && satisfiyCodition("2", assureInforAssistant.getOverRoomsNum(),
                            hotelOrderFromBean, assureInforAssistant.getAssureDate())) {
                        assureAmout = assureInforAssistant.getFristDayAssureAmount();
                    }
                    //是超间夜担保
                    else if(assureInforAssistant.isOverNightsAssure()
							&&satisfiyCodition("3",assureInforAssistant.getOverNightsNum(),hotelOrderFromBean,assureInforAssistant.getAssureDate())){
						assureAmout = assureInforAssistant.getFristDayAssureAmount();
                    }
                } 
                //担保类型 4:全额
                else if ("4".equals(assureInforAssistant.getAssureType())) {
                	//是无条件担保
                    if (assureInforAssistant.isUnconditionAssure()) {
                        assureAmout = hotelOrderFromBean.getPriceNum();
                    } 
                    //是超时担保 
                    else if (assureInforAssistant.isOverTimeAssure()
                        && 0 == DateUtil.getDay(hotelOrderFromBean.getCheckinDate(),
                            assureInforAssistant.getAssureDate())
                        && satisfiyCodition("1", assureInforAssistant.getOverTimeStr(),
                            hotelOrderFromBean, assureInforAssistant.getAssureDate())) {
                        assureAmout = hotelOrderFromBean.getPriceNum();
                    } 
                    //是超房担保 
                    else if (assureInforAssistant.isOverRoomsAssure()
                        && satisfiyCodition("2", assureInforAssistant.getOverRoomsNum(),
                            hotelOrderFromBean, assureInforAssistant.getAssureDate())) {
                        assureAmout = hotelOrderFromBean.getPriceNum();
                    }
                    //是超间夜担保
                    else if(assureInforAssistant.isOverNightsAssure()
							&&satisfiyCodition("3",assureInforAssistant.getOverNightsNum(),hotelOrderFromBean,assureInforAssistant.getAssureDate())){
						assureAmout = hotelOrderFromBean.getPriceNum();
                    }
                }
            }

        } 
        //担保规则 累加
        else if ("3".equals(assureInforAssistantFrist.getAssureRule())) {
            for (Iterator<AssureInforAssistant> assureDetailIterator = assureDetailLit.iterator(); assureDetailIterator.hasNext();) {
                AssureInforAssistant assureInforAssistant = assureDetailIterator .next();
                //担保类型 2:首日
                if ("2".equals(assureInforAssistant.getAssureType())
                    && 0 == Double.compare(0.0, assureAmout)) {
                    if (assureInforAssistant.isUnconditionAssure()) {
                        assureAmout = assureInforAssistant.getFristDayAssureAmount();
                    } else if (assureInforAssistant.isOverTimeAssure()
                        && 0 == DateUtil.getDay(hotelOrderFromBean.getCheckinDate(),
                            assureInforAssistant.getAssureDate())
                        && satisfiyCodition("1", assureInforAssistant.getOverTimeStr(),
                            hotelOrderFromBean, assureInforAssistant.getAssureDate())) {
                        assureAmout = assureInforAssistant.getFristDayAssureAmount();
                    } else if (assureInforAssistant.isOverRoomsAssure()
                        && satisfiyCodition("2", assureInforAssistant.getOverRoomsNum(),
                            hotelOrderFromBean, assureInforAssistant.getAssureDate())) {
                        assureAmout = assureInforAssistant.getFristDayAssureAmount();
                    }else if(assureInforAssistant.isOverNightsAssure()
							&&satisfiyCodition("3",assureInforAssistant.getOverNightsNum(),hotelOrderFromBean,assureInforAssistant.getAssureDate())){
						assureAmout = assureInforAssistant.getFristDayAssureAmount();
                    }
                } 
                //担保类型 4:全额
                else if ("4".equals(assureInforAssistant.getAssureType())) {
                    if (assureInforAssistant.isUnconditionAssure()) {
                        assureAmout += assureInforAssistant.getUnconditionAssureAmount();
                    } else if (assureInforAssistant.isOverTimeAssure()
                        && 0 == DateUtil.getDay(hotelOrderFromBean.getCheckinDate(),
                            assureInforAssistant.getAssureDate())
                        && satisfiyCodition("1", assureInforAssistant.getOverTimeStr(),
                            hotelOrderFromBean, assureInforAssistant.getAssureDate())) {
                        assureAmout += assureInforAssistant.getOverTimeAssureAmount();
                    } else if (assureInforAssistant.isOverRoomsAssure()
                        && satisfiyCodition("2", assureInforAssistant.getOverRoomsNum(),
                            hotelOrderFromBean, assureInforAssistant.getAssureDate())) {
                        assureAmout += assureInforAssistant.getOverRoomsAssureAmount();
                    }else if(assureInforAssistant.isOverNightsAssure()
							&&satisfiyCodition("3",assureInforAssistant.getOverNightsNum(),hotelOrderFromBean,assureInforAssistant.getAssureDate())){
						assureAmout += assureInforAssistant.getOverNightsAssureAmount();
                    }
                }
            }
        }
        return assureAmout;
	}
	private boolean satisfiyCodition(String conditionType, Object obj,
	        HotelOrderFromBean hotelOrderFromBean, Date assuredate) {
	        boolean ret = false;
	        if ("1".equals(conditionType)) {
	            long assureTime = StringUtil.getStrTolong((((String) obj).replace(":", "")));
	            long orderTime = StringUtil.getStrTolong(hotelOrderFromBean.getLatestArrivalTime()
	                .replace(":", ""));
	            if (orderTime > assureTime) {
	                ret = true;
	            } else {
	                ret = false;
	            }
	        } else if ("2".equals(conditionType)) {
	            long assureRooms = StringUtil.getStrTolong((obj.toString()));
	            long orderRooms = StringUtil.getStrTolong(hotelOrderFromBean.getRoomQuantity());
	            if (orderRooms > assureRooms) {
	                ret = true;
	            } else {
	                ret = false;
	            }
	        }else if("3".equals(conditionType)){
				long assureNights = StringUtil.getStrTolong((obj.toString()));
				long orderNights = DateUtil.getDay(hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate())*StringUtil.getStrTolong(hotelOrderFromBean.getRoomQuantity());
				if(orderNights>assureNights){
					ret = true;
				}else{
					ret = false;
				}
			}
	        return ret;
	    }

	public List convertToAssureInforAssistant(String assureDetailString) {
		List assureDetailList = new ArrayList();
        String[] assureDetailArray = assureDetailString.split("&");
        for (int i = 0; i < assureDetailArray.length; i++) {
            AssureInforAssistant assureInforAssistant = new AssureInforAssistant();
            String[] assureDetail = (assureDetailArray[i]).split("#");
            for (int j = 0; j < assureDetail.length; j++) {
                switch (j) {
                case 0:
                    if (null != assureDetail[j])
                        assureInforAssistant.setAssureDate(DateUtil.getDate(assureDetail[j]));
                    break;
                case 1:
                    if (null != assureDetail[j])
                        assureInforAssistant.setAssureRule(assureDetail[j]);
                    break;
                case 2:
                    if (null != assureDetail[j])
                        assureInforAssistant.setAssureType(assureDetail[j]);
                    break;
                case 3:
                    if (null != assureDetail[j])
                        assureInforAssistant.setUnconditionAssure(StringUtil
                            .getBooleanValue(assureDetail[j]));
                    break;
                case 4:
                    if (null != assureDetail[j])
                        assureInforAssistant.setUnconditionAssureAmount(StringUtil
                            .getStrTodouble(assureDetail[j]));
                    break;
               case 5:
                    if (null != assureDetail[j])
                        assureInforAssistant.setOverRoomsAssure(StringUtil
                            .getBooleanValue(assureDetail[j]));
                    break;
                case 6:
                    if (null != assureDetail[j])
                        assureInforAssistant.setOverRoomsNum(Integer.parseInt(assureDetail[j]));
                    break;
                case 7:
                    if (null != assureDetail[j])
                        assureInforAssistant.setOverRoomsAssureAmount(StringUtil
                            .getStrTodouble(assureDetail[j]));
                    break;
                 case 8:
                    if (null != assureDetail[j])
                        assureInforAssistant.setOverNightsAssure(StringUtil
                            .getBooleanValue(assureDetail[j]));
                    break;
                case 9:
                    if (null != assureDetail[j])
                        assureInforAssistant.setOverNightsNum(Integer.parseInt(assureDetail[j]));
                    break;
                case 10:
                    if (null != assureDetail[j])
                        assureInforAssistant.setOverNightsAssureAmount(StringUtil
                            .getStrTodouble(assureDetail[j]));
                    break;
				case 11:
					if(assureDetail[j]!=null)
					assureInforAssistant.setOverTimeAssure(StringUtil.getBooleanValue(assureDetail[j]));
					break;
				case 12:
					if(assureDetail[j]!=null)
					assureInforAssistant.setOverTimeStr(assureDetail[j]);
					break;
				case 13:
					if(assureDetail[j]!=null)
					assureInforAssistant.setOverTimeAssureAmount(StringUtil.getStrTodouble(assureDetail[j]));
					break;
				case 14:
					if(assureDetail[j]!=null)
					assureInforAssistant.setFristDayAssureAmount(StringUtil.getStrTodouble(assureDetail[j]));
                    break;
                default:
                    break;
                }
            }
            assureDetailList.add(assureInforAssistant);
        }
        return assureDetailList;
	}
    
	/**
	 * add by zhijie.gu 2009-09-15 hotel2.9.3 改变在优惠条款内的价格同时对应改变总价
	 * @param hotelStar
	 * @param mangoRate
	 * @param totelPrice
	 * @param faceItemsLis
	 * @param queryHotelForWebSaleItems
	 * @param hotelId
	 * @param childRoomTypeId
	 * @param y 第几天
	 * @param f 标示天数是否足够连住包价
	 * @param favourableMap
	 * @return
	 */	
	public List changePriceForFavourable(String hotelStar,double mangoRate,double totelPrice,List faceItemsLis,
			QueryHotelForWebSaleItems queryHotelForWebSaleItems,long hotelId,String childRoomTypeId,int y, int f,
			Map<String,List<HtlFavourableclause>> favourableMap){
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		//连住优惠过程量
		QueryHotelForWebSaleItems queryHotelForWebSaleItem = new QueryHotelForWebSaleItems();
		
		/* 改变从数据库取的方式，从之前缓存出来的Map中取 begin */
		String mapKey = hotelId + "_" + childRoomTypeId;
		List<HtlFavourableclause> htlFavourableclauseList = favourableMap.get(mapKey);
		/* 改变从数据库取的方式，从之前缓存出来的Map中取 end */
		
        //连住优惠对价格处理
    	List lis =new ArrayList();
    	if(null !=htlFavourableclauseList){
        	for(int i= 0 ;i<htlFavourableclauseList.size();i++){
        		HtlFavourableclause htlFavourableclause=htlFavourableclauseList.get(i);
        		
        		if("2".equals(htlFavourableclause.getFavourableType())){
        			if(!queryHotelForWebSaleItems.getFellowDate().before(htlFavourableclause.getBeginDate()) 
        					&& !queryHotelForWebSaleItems.getFellowDate().after(htlFavourableclause.getEndDate())){
        				List<HtlFavouraParameter> lstPackagerate = htlFavourableclause.getLstPackagerate();
        				if(null != lstPackagerate){
        					queryHotelForWebSaleItems.setFavourableFlag(true);
        					for(int j = 0; j < lstPackagerate.size();j++ ){
        						double oldSalePrice = queryHotelForWebSaleItems.getSalePrice();
        						BigDecimal b = new BigDecimal(""+lstPackagerate.get(j).getDiscount()*queryHotelForWebSaleItems.getSalePrice()/10);    
            					//逢一进十
            					if(1 == lstPackagerate.get(j).getDecimalPointType()){
            						queryHotelForWebSaleItems.setSalePrice(Math.ceil(b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()));
            					}
            					//四舍五入保留1位小数
            					if(2 == lstPackagerate.get(j).getDecimalPointType()){
            						queryHotelForWebSaleItems.setSalePrice(b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue()) ;
            					}
            					//直接取整
            					if(3 == lstPackagerate.get(j).getDecimalPointType()){
            						queryHotelForWebSaleItems.setSalePrice(Math.floor(lstPackagerate.get(j).getDiscount()*queryHotelForWebSaleItems.getSalePrice()/10));
            					}
            					//四舍五入取整
            					if(4 == lstPackagerate.get(j).getDecimalPointType()){
            						queryHotelForWebSaleItems.setSalePrice(b.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue()) ;
            					}
            					
    							double newSalePrice = queryHotelForWebSaleItems.getSalePrice();
    							int k = 0;
    							if("0".equals(queryHotelForWebSaleItems.getFormulaId())){
    								k=0;
    							}else if("priceA".equals(queryHotelForWebSaleItems.getFormulaId())){
    								k=1;
    							}else if("priceB".equals(queryHotelForWebSaleItems.getFormulaId())){
    								k=2;
    							}else if("priceC".equals(queryHotelForWebSaleItems.getFormulaId())){
    								k=3;
    							}else if("priceD".equals(queryHotelForWebSaleItems.getFormulaId())){
    								k=4;
    							}else if("priceE".equals(queryHotelForWebSaleItems.getFormulaId())){
    								k=5;
    							}else if("priceF".equals(queryHotelForWebSaleItems.getFormulaId())){
    								k=6;
    							}else if("priceG".equals(queryHotelForWebSaleItems.getFormulaId())){
    								k=7;
    							}else if("priceH".equals(queryHotelForWebSaleItems.getFormulaId())){
    								k=8;
    							}else if("priceI".equals(queryHotelForWebSaleItems.getFormulaId())){
    								k=9;
    							}else if("priceJ".equals(queryHotelForWebSaleItems.getFormulaId())){
    								k=10;
    							}else if("priceK".equals(queryHotelForWebSaleItems.getFormulaId())){
    								k=11;
    							}
    							switch(k){
    							case 0:
    								queryHotelForWebSaleItems.setCommission(queryHotelForWebSaleItems.getCommission());
    								break;
    							case 1:
    								queryHotelForWebSaleItems.setCommission(BigDecimal.valueOf(
    										(newSalePrice/(oldSalePrice/(queryHotelForWebSaleItems.getCommission()/
    												queryHotelForWebSaleItems.getCommissionRate()+
    												(queryHotelForWebSaleItems.getBreakfastPrice()*queryHotelForWebSaleItems.getBreakfastNum())))-
    												queryHotelForWebSaleItems.getBreakfastPrice()*queryHotelForWebSaleItems.getBreakfastNum())*
    												queryHotelForWebSaleItems.getCommissionRate()).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
    								break;
    							case 2:
    								queryHotelForWebSaleItems.setCommission(BigDecimal.valueOf((newSalePrice-
    										queryHotelForWebSaleItems.getBreakfastPrice()*queryHotelForWebSaleItems.getBreakfastNum())/
    										((oldSalePrice-queryHotelForWebSaleItems.getBreakfastPrice()*queryHotelForWebSaleItems.getBreakfastNum())/
    												(queryHotelForWebSaleItems.getCommission()/queryHotelForWebSaleItems.getCommissionRate()))*queryHotelForWebSaleItems.getCommissionRate()).
    												setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
    								break;
    							case 3:
    								queryHotelForWebSaleItems.setCommission(BigDecimal.valueOf(newSalePrice*queryHotelForWebSaleItems.getCommissionRate()).
    										setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
    								break;
    							case 4:
    								queryHotelForWebSaleItems.setCommission(BigDecimal.valueOf((newSalePrice/(oldSalePrice/(queryHotelForWebSaleItems.getCommission()/
    										queryHotelForWebSaleItems.getCommissionRate())))*queryHotelForWebSaleItems.getCommissionRate()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
    								break;
    							case 5:
    								queryHotelForWebSaleItems.setCommission(BigDecimal.valueOf((newSalePrice-queryHotelForWebSaleItems.getBreakfastPrice()*
    										queryHotelForWebSaleItems.getBreakfastNum())*queryHotelForWebSaleItems.getCommissionRate()).
    										setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
    								
    								break;
    							case 6:
    								queryHotelForWebSaleItems.setCommission(queryHotelForWebSaleItems.getCommission());
    								break;
    							case 7:
    								queryHotelForWebSaleItems.setCommission(BigDecimal.valueOf((newSalePrice-queryHotelForWebSaleItems.getBreakfastPrice()*
    										queryHotelForWebSaleItems.getBreakfastNum())*queryHotelForWebSaleItems.getCommissionRate()).
    										setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
    								break;
    							case 8:
    								queryHotelForWebSaleItems.setCommission(BigDecimal.valueOf((newSalePrice-queryHotelForWebSaleItems.getBreakfastPrice()*
    										queryHotelForWebSaleItems.getBreakfastNum())*queryHotelForWebSaleItems.getCommissionRate()).
    										setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
    								break;
    							case 9:
    								queryHotelForWebSaleItems.setCommission(BigDecimal.valueOf(newSalePrice*queryHotelForWebSaleItems.getCommissionRate()).
    										setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
    								break;
    							case 10:
    								queryHotelForWebSaleItems.setCommission(BigDecimal.valueOf(newSalePrice*queryHotelForWebSaleItems.getCommissionRate()).
    										setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
    								break;
    							case 11:
    								queryHotelForWebSaleItems.setCommission(BigDecimal.valueOf((newSalePrice-queryHotelForWebSaleItems.getBreakfastPrice()*
    										queryHotelForWebSaleItems.getBreakfastNum())*queryHotelForWebSaleItems.getCommissionRate()).
    										setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
    								break;
    							
    							}
    							queryHotelForWebSaleItems.setBasePrice(newSalePrice-queryHotelForWebSaleItems.getCommission());
    							//佣金价
    							queryHotelForWebSaleItems.setAgentComissionPrice(queryHotelForWebSaleItems.getSalePrice());
    							//代理佣金率保留两位小数
    							double agentComissionRate= queryHotelForWebSaleItems.getCommission()/queryHotelForWebSaleItems.getSalePrice()-mangoRate;
    							if(String.valueOf(agentComissionRate).length()>3){
    								queryHotelForWebSaleItems.setAgentComissionRate(Double.valueOf(String.valueOf(agentComissionRate).substring(0,4)));
    							}else{
    								queryHotelForWebSaleItems.setAgentComissionRate(agentComissionRate);
    							}
    							//B2B 2期修改，5星酒店固定显示返佣率9%，其他星显示返佣率12% add by zhijie.gu 2010-3-16 begin 
    							if("5".equals(hotelStar) || "19".equals(hotelStar) || "4.5".equals(hotelStar) || "29".equals(hotelStar)){
    								//代理佣金默认政策修改 add by zhijie.gu 2010-4-27
//    			                	if(queryHotelForWebSaleItems.getAgentComissionRate()>=0.09){
    			                		queryHotelForWebSaleItems.setAgentReadComissionRate(0.09);
    			                		queryHotelForWebSaleItems.setAgentReadComissionPrice(queryHotelForWebSaleItems.getAgentComissionPrice());
    			                		queryHotelForWebSaleItems.setAgentReadComission(queryHotelForWebSaleItems.getAgentComissionPrice()*0.09);
//    			                	}else{
//    			                		queryHotelForWebSaleItems.setAgentReadComissionRate(0.09);
//    			                		double comission = queryHotelForWebSaleItems.getAgentComissionPrice()* agentComissionRate;
//    			                		queryHotelForWebSaleItems.setAgentReadComissionPrice(Double.parseDouble(""+(Math.round(comission/0.09))));
//    			                		queryHotelForWebSaleItems.setAgentReadComission(BigDecimal.valueOf(comission).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//    			                	}
    			                	
    			                }else{
    			                	//代理佣金默认政策修改 add by zhijie.gu 2010-4-27
    			                	//if(queryHotelForWebSaleItems.getAgentComissionRate()>=0.12){
    			                		queryHotelForWebSaleItems.setAgentReadComissionRate(0.12);
    			                		queryHotelForWebSaleItems.setAgentReadComissionPrice(queryHotelForWebSaleItems.getAgentComissionPrice());
    			                		queryHotelForWebSaleItems.setAgentReadComission(queryHotelForWebSaleItems.getAgentComissionPrice()*0.12);
//    			                	}else{
//    			                		queryHotelForWebSaleItems.setAgentReadComissionRate(0.12);
//    			                		double comission = queryHotelForWebSaleItems.getAgentComissionPrice()* agentComissionRate;
//    			                		queryHotelForWebSaleItems.setAgentReadComissionPrice(Double.parseDouble(""+(Math.round(comission/0.12))));
//    			                		queryHotelForWebSaleItems.setAgentReadComission(BigDecimal.valueOf(comission).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//    			                	}
    			                }
    							
        					}
        					
        				}
        				
        			}
        			
        			
        		}
        		if("3".equals(htlFavourableclause.getFavourableType())){
        			if(!queryHotelForWebSaleItems.getFellowDate().before(htlFavourableclause.getBeginDate()) 
        					&& !queryHotelForWebSaleItems.getFellowDate().after(htlFavourableclause.getEndDate())){
        				List<HtlFavouraParameter> lstPackagerate = htlFavourableclause.getLstPackagerate();
            			if(null != lstPackagerate){
            				for(int j = 0; j < lstPackagerate.size();j++ ){
                				if(f == lstPackagerate.get(j).getPackagerateNight()-1){
                					for (int a = y-(int)(lstPackagerate.get(j).getPackagerateNight()-1); a <= y; a++ ){
                						if(a == y-(int)(lstPackagerate.get(j).getPackagerateNight()-1)){
                							queryHotelForWebSaleItem =(QueryHotelForWebSaleItems)faceItemsLis.get(a);
                							totelPrice = totelPrice - (queryHotelForWebSaleItem.getSalePrice() - lstPackagerate.get(j).getPackagerateSaleprice());
                							queryHotelForWebSaleItem.setSalePrice(lstPackagerate.get(j).getPackagerateSaleprice());
                							queryHotelForWebSaleItem.setCommission(lstPackagerate.get(j).getPackagerateCommission());
                							queryHotelForWebSaleItem.setBasePrice(BigDecimal.valueOf(lstPackagerate.get(j).getPackagerateSaleprice()-
                									lstPackagerate.get(j).getPackagerateCommission()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                							queryHotelForWebSaleItem.setAgentComissionPrice(queryHotelForWebSaleItem.getSalePrice());
                							//代理佣金率保留两位小数
                							double agentComissionRate= queryHotelForWebSaleItem.getCommission()/queryHotelForWebSaleItem.getSalePrice()-mangoRate;
                							if(String.valueOf(agentComissionRate).length()>3){
                								queryHotelForWebSaleItem.setAgentComissionRate(Double.valueOf(String.valueOf(agentComissionRate).substring(0,4)));
                							}else{
                								queryHotelForWebSaleItem.setAgentComissionRate(agentComissionRate);
                							}
                							//B2B 2期修改，5星酒店固定显示返佣率9%，其他星显示返佣率12% add by zhijie.gu 2010-3-16 begin 
                							if("5".equals(hotelStar) || "19".equals(hotelStar) || "4.5".equals(hotelStar) || "29".equals(hotelStar)){
                								//代理佣金默认政策修改 add by zhijie.gu 2010-4-27
                								//if(queryHotelForWebSaleItem.getAgentComissionRate()>=0.09){
                			                		queryHotelForWebSaleItem.setAgentReadComissionRate(0.09);
                			                		queryHotelForWebSaleItem.setAgentReadComissionPrice(queryHotelForWebSaleItem.getSalePrice());
                			                		queryHotelForWebSaleItem.setAgentReadComission(queryHotelForWebSaleItem.getAgentComissionPrice()*0.09);
//                			                	}else{
//                			                		queryHotelForWebSaleItem.setAgentReadComissionRate(0.09);
//                			                		double comission = queryHotelForWebSaleItem.getAgentComissionPrice()* agentComissionRate;
//                			                		queryHotelForWebSaleItem.setAgentReadComissionPrice(Double.parseDouble(""+(Math.round(comission/0.09))));
//                			                		queryHotelForWebSaleItem.setAgentReadComission(BigDecimal.valueOf(comission).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//                			                	}
                			                }else{
                			                	//代理佣金默认政策修改 add by zhijie.gu 2010-4-27
                			                	//if(queryHotelForWebSaleItem.getAgentComissionRate()>=0.12){
                			                		queryHotelForWebSaleItem.setAgentReadComissionRate(0.12);
                			                		queryHotelForWebSaleItem.setAgentReadComissionPrice(queryHotelForWebSaleItem.getSalePrice());
                			                		queryHotelForWebSaleItem.setAgentReadComission(queryHotelForWebSaleItem.getAgentComissionPrice()*0.12);
//                			                	}else{
//                			                		queryHotelForWebSaleItem.setAgentReadComissionRate(0.12);
//                			                		double comission = queryHotelForWebSaleItem.getAgentComissionPrice()* agentComissionRate;
//                			                		queryHotelForWebSaleItem.setAgentReadComissionPrice(Double.parseDouble(""+(Math.round(comission/0.12))));
//                			                		queryHotelForWebSaleItem.setAgentReadComission(BigDecimal.valueOf(comission).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//                			                	}
                			                }
                							
                							
                						}else{
                							queryHotelForWebSaleItem =(QueryHotelForWebSaleItems)faceItemsLis.get(a);
                							//如果等于y，在调用这个方法的位置再减去多加的价格。add by zhijie.gu 2010-01-13
                							if(a!=y){
                								totelPrice = totelPrice-queryHotelForWebSaleItem.getSalePrice();
                							}
                							queryHotelForWebSaleItem.setSalePrice(99999.0);
                							queryHotelForWebSaleItem.setCommission(0.0);
                							queryHotelForWebSaleItem.setBasePrice(0.0);
                							queryHotelForWebSaleItem.setAgentComissionPrice(0.0);
                							queryHotelForWebSaleItem.setAgentComissionRate(0.0);
                							queryHotelForWebSaleItem.setAgentReadComissionPrice(0.0);
                							queryHotelForWebSaleItem.setAgentReadComissionRate(0.0);
                							queryHotelForWebSaleItem.setAgentReadComission(0.0);
										}
                						queryHotelForWebSaleItems.setFavourableFlag(true);
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
        			if(!queryHotelForWebSaleItems.getFellowDate().before(htlFavourableclause.getBeginDate()) 
            					&& !queryHotelForWebSaleItems.getFellowDate().after(htlFavourableclause.getEndDate())){
        				List<HtlFavouraParameter> lstPackagerate = htlFavourableclause.getLstPackagerate();
            			if(null != lstPackagerate){
            				for(int j = 0; j < lstPackagerate.size();j++ ){
            					int ConNumD = (int)(lstPackagerate.get(j).getContinueNight()+0);
            					int doNumD = (int)(lstPackagerate.get(j).getDonateNight()+0);
            					List<HtlEveningsRent> lstEveningsRent= lstPackagerate.get(j).getLstEveningsRent();
            					int numDay = ConNumD+doNumD;
            					int zeroNum = 0;
                				if(f == numDay-1){
                					for(int aa =0;aa<lstEveningsRent.size();aa++){
    	        						int night =(int)(lstEveningsRent.get(aa).getNight()+0);
    	        						queryHotelForWebSaleItem =(QueryHotelForWebSaleItems)faceItemsLis.get(y-(numDay-1)+(night-1));
    	        						queryHotelForWebSaleItem.setFavourableFlag(true);
    	        						if(0 == (int)(lstEveningsRent.get(aa).getSalePrice()+0)){
    	        							if(numDay == night){
    	        								queryHotelForWebSaleItem.setSalePrice(99999.0);
    	        							}else{
    	        								totelPrice = totelPrice - queryHotelForWebSaleItem.getSalePrice();
    	        								queryHotelForWebSaleItem.setSalePrice(99999.0);
    	        							}
    	        							queryHotelForWebSaleItem.setAgentComissionRate(0.0);
    	        							queryHotelForWebSaleItem.setBasePrice(0.0);
    	        							queryHotelForWebSaleItem.setAgentComissionPrice(0.0);
    	        							queryHotelForWebSaleItem.setAgentReadComissionPrice(0.0);
    	        							queryHotelForWebSaleItem.setAgentReadComissionRate(0.0);
    	        							queryHotelForWebSaleItem.setAgentReadComission(0.0);
	        							}else{
	        								if(numDay == night){
	        									queryHotelForWebSaleItem.setSalePrice(lstEveningsRent.get(aa).getSalePrice());
    	        							}else{
    	        								totelPrice = totelPrice - (queryHotelForWebSaleItem.getSalePrice() - lstEveningsRent.get(aa).getSalePrice());
    	        								queryHotelForWebSaleItem.setSalePrice(lstEveningsRent.get(aa).getSalePrice());
    	        							}
	        								queryHotelForWebSaleItem.setBasePrice(BigDecimal.valueOf(lstEveningsRent.get(aa).getSalePrice()-lstEveningsRent.get(aa).getCommission()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	        								queryHotelForWebSaleItem.setCommission(lstEveningsRent.get(aa).getCommission());
	        								queryHotelForWebSaleItem.setAgentComissionPrice(queryHotelForWebSaleItem.getSalePrice());
	        								//代理佣金率保留两位小数
                							double agentComissionRate= queryHotelForWebSaleItem.getCommission()/queryHotelForWebSaleItem.getSalePrice()-mangoRate;
                							if(String.valueOf(agentComissionRate).length()>3){
                								queryHotelForWebSaleItem.setAgentComissionRate(Double.valueOf(String.valueOf(agentComissionRate).substring(0,4)));
                							}else{
                								queryHotelForWebSaleItem.setAgentComissionRate(agentComissionRate);
                							}
                							//B2B 2期修改，5星酒店固定显示返佣率9%，其他星显示返佣率12% add by zhijie.gu 2010-3-16 begin 
                							if("5".equals(hotelStar) || "19".equals(hotelStar) || "4.5".equals(hotelStar) || "29".equals(hotelStar)){
                								//代理佣金默认政策修改 add by zhijie.gu 2010-4-27
                								//if(queryHotelForWebSaleItem.getAgentComissionRate()>=0.09){
                			                		queryHotelForWebSaleItem.setAgentReadComissionRate(0.09);
                			                		queryHotelForWebSaleItem.setAgentReadComissionPrice(queryHotelForWebSaleItem.getAgentComissionPrice());
                			                		queryHotelForWebSaleItem.setAgentReadComission(queryHotelForWebSaleItem.getAgentComissionPrice()*0.09);
//                			                	}else{
//                			                		queryHotelForWebSaleItem.setAgentReadComissionRate(0.09);
//                			                		double comission = queryHotelForWebSaleItem.getAgentComissionPrice()* agentComissionRate;
//                			                		queryHotelForWebSaleItem.setAgentReadComissionPrice(Double.parseDouble(""+(Math.round(comission/0.09))));
//                			                		queryHotelForWebSaleItem.setAgentReadComission(BigDecimal.valueOf(comission).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//                			                	}
                			                	
                			                }else{
                			                	//代理佣金默认政策修改 add by zhijie.gu 2010-4-27
                			                	//if(queryHotelForWebSaleItem.getAgentComissionRate()>=0.12){
                			                		queryHotelForWebSaleItem.setAgentReadComissionRate(0.12);
                			                		queryHotelForWebSaleItem.setAgentReadComissionPrice(queryHotelForWebSaleItem.getAgentComissionPrice());
                			                		queryHotelForWebSaleItem.setAgentReadComission(queryHotelForWebSaleItem.getAgentComissionPrice()*0.12);
//                			                	}else{
//                			                		queryHotelForWebSaleItem.setAgentReadComissionRate(0.12);
//                			                		double comission = queryHotelForWebSaleItem.getAgentComissionPrice()*agentComissionRate;
//                			                		queryHotelForWebSaleItem.setAgentReadComissionPrice(Double.parseDouble(""+(Math.round(comission/0.12))));
//                			                		queryHotelForWebSaleItem.setAgentReadComission(BigDecimal.valueOf(comission).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//                			                	}
                			                }
                							
                							
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
    	lis.add(0,totelPrice);
    	lis.add(1,f);
		return lis;
		
	}
	 public List<HtlFavourableclause> queryFavourableResult(long hotelId,long childRoomTypeId) {
	        String hsql = "from HtlFavourableclause where hotelId = ?"+" and priceTypeId = ?";
	        List<HtlFavourableclause> htlFavourableclause  = super.query(hsql, new Object[]{hotelId,childRoomTypeId});
	        return htlFavourableclause;
	    }
	 
	    /**
	     * add by zhijie.gu 2009-09-15 hotel2.9.3 改变在优惠条款内的价格，
	     * 改变QueryHotelForWebSaleItems的价格，连住N送M时价格为99999.0，用来在页面上判断显示'-'的条件
	     */
	 
	 public int changePriceForFavourableTwo(List faceItemsLis,QueryHotelForWebSaleItems queryHotelForWebSaleItems,long hotelId,long childRoomTypeId,int y, int f){
			
			//连住优惠过程量
			QueryHotelForWebSaleItems queryHotelForWebSaleItem = new QueryHotelForWebSaleItems();
	    	List<HtlFavourableclause> htlFavourableclauseList = queryFavourableResult(hotelId,childRoomTypeId);
	        //连住优惠对价格处理
	        if(null !=htlFavourableclauseList){
	        	for(int i= 0 ;i<htlFavourableclauseList.size();i++){
	        		HtlFavourableclause htlFavourableclause=htlFavourableclauseList.get(i);
	        		
	        		if("2".equals(htlFavourableclause.getFavourableType())){
	        			if(!queryHotelForWebSaleItems.getFellowDate().before(htlFavourableclause.getBeginDate()) 
	        					&& !queryHotelForWebSaleItems.getFellowDate().after(htlFavourableclause.getEndDate())){
	        				List<HtlFavouraParameter> lstPackagerate = htlFavourableclause.getLstPackagerate();
	        				if(null != lstPackagerate){
	        					for(int j = 0; j < lstPackagerate.size();j++ ){
	        						double oldSalePrice = queryHotelForWebSaleItems.getSalePrice();
	        					
	        						BigDecimal b = new BigDecimal(""+lstPackagerate.get(j).getDiscount()*queryHotelForWebSaleItems.getSalePrice()/10);    
	            					//逢一进十
	            					if(1 == lstPackagerate.get(j).getDecimalPointType()){
	            						queryHotelForWebSaleItems.setSalePrice(Math.ceil(b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()));
	            						}
	            					//四舍五入
	            					if(2 == lstPackagerate.get(j).getDecimalPointType()){
	            						queryHotelForWebSaleItems.setSalePrice(b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue()) ;
	            					}
	            					//直接取整
	            					if(3 == lstPackagerate.get(j).getDecimalPointType()){
	            						queryHotelForWebSaleItems.setSalePrice(Math.floor(lstPackagerate.get(j).getDiscount()*queryHotelForWebSaleItems.getSalePrice()/10));
	            					}
	            					//四舍五入取整
	            					if(4 == lstPackagerate.get(j).getDecimalPointType()){
	            						queryHotelForWebSaleItems.setSalePrice(b.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue()) ;
	            					}
	            					
	            				    //根据连住优惠更改了售价 的标记 add by shengwei.zuo 2009-11-13
	            					queryHotelForWebSaleItems.setChgSalePri(true);
	            					
	            					
	            					double newSalePrice = queryHotelForWebSaleItems.getSalePrice();
	    							int k = 0;
	    							String formulaId=queryHotelForWebSaleItems.getFormulaId();
	    							if("0".equals(formulaId)){
	    								k=0;
	    							}else if("priceA".equals(formulaId)){
	    								k=1;
	    							}else if("priceB".equals(formulaId)){
	    								k=2;
	    							}else if("priceC".equals(formulaId)){
	    								k=3;
	    							}else if("priceD".equals(formulaId)){
	    								k=4;
	    							}else if("priceE".equals(formulaId)){
	    								k=5;
	    							}else if("priceF".equals(formulaId)){
	    								k=6;
	    							}else if("priceG".equals(formulaId)){
	    								k=7;
	    							}else if("priceH".equals(formulaId)){
	    								k=8;
	    							}else if("priceI".equals(formulaId)){
	    								k=9;
	    							}else if("priceJ".equals(formulaId)){
	    								k=10;
	    							}else if("priceK".equals(formulaId)){
	    								k=11;
	    							}
	    							switch(k){
	    							case 0:
	    								queryHotelForWebSaleItems.setCommission(queryHotelForWebSaleItems.getCommission());
	    								queryHotelForWebSaleItems.setBasePrice(newSalePrice-queryHotelForWebSaleItems.getCommission());
	    								break;
	    							case 1:
	    								queryHotelForWebSaleItems.setCommission(BigDecimal.valueOf(
	    										(newSalePrice/(oldSalePrice/(queryHotelForWebSaleItems.getCommission()/
	    												queryHotelForWebSaleItems.getCommissionRate()+
	    												(queryHotelForWebSaleItems.getBreakfastPrice()*queryHotelForWebSaleItems.getBreakfastNum())))-
	    												queryHotelForWebSaleItems.getBreakfastPrice()*queryHotelForWebSaleItems.getBreakfastNum())*
	    												queryHotelForWebSaleItems.getCommissionRate()).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
	    								queryHotelForWebSaleItems.setBasePrice(newSalePrice-queryHotelForWebSaleItems.getCommission());
	    								break;
	    							case 2:
	    								queryHotelForWebSaleItems.setCommission(BigDecimal.valueOf((newSalePrice-
	    										queryHotelForWebSaleItems.getBreakfastPrice()*queryHotelForWebSaleItems.getBreakfastNum())/
	    										((oldSalePrice-queryHotelForWebSaleItems.getBreakfastPrice()*queryHotelForWebSaleItems.getBreakfastNum())/
	    												(queryHotelForWebSaleItems.getCommission()/queryHotelForWebSaleItems.getCommissionRate()))*queryHotelForWebSaleItems.getCommissionRate()).
	    												setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
	    								queryHotelForWebSaleItems.setBasePrice(newSalePrice-queryHotelForWebSaleItems.getCommission());
	    								break;
	    							case 3:
	    								queryHotelForWebSaleItems.setCommission(BigDecimal.valueOf(newSalePrice*queryHotelForWebSaleItems.getCommissionRate()).
	    										setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	    								queryHotelForWebSaleItems.setBasePrice(newSalePrice-queryHotelForWebSaleItems.getCommission());
	    								break;
	    							case 4:
	    								queryHotelForWebSaleItems.setCommission(BigDecimal.valueOf((newSalePrice/(oldSalePrice/(queryHotelForWebSaleItems.getCommission()/
	    										queryHotelForWebSaleItems.getCommissionRate())))*queryHotelForWebSaleItems.getCommissionRate()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	    								queryHotelForWebSaleItems.setBasePrice(newSalePrice-queryHotelForWebSaleItems.getCommission());
	    								break;
	    							case 5:
	    								queryHotelForWebSaleItems.setCommission(BigDecimal.valueOf((newSalePrice-queryHotelForWebSaleItems.getBreakfastPrice()*
	    										queryHotelForWebSaleItems.getBreakfastNum())*queryHotelForWebSaleItems.getCommissionRate()).
	    										setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	    								
	    								queryHotelForWebSaleItems.setBasePrice(newSalePrice-queryHotelForWebSaleItems.getCommission());
	    								break;
	    							case 6:
	    								queryHotelForWebSaleItems.setCommission(queryHotelForWebSaleItems.getCommission());
	    								queryHotelForWebSaleItems.setBasePrice(newSalePrice-queryHotelForWebSaleItems.getCommission());
	    								break;
	    							case 7:
	    								queryHotelForWebSaleItems.setCommission(BigDecimal.valueOf((newSalePrice-queryHotelForWebSaleItems.getBreakfastPrice()*
	    										queryHotelForWebSaleItems.getBreakfastNum())*queryHotelForWebSaleItems.getCommissionRate()).
	    										setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	    								queryHotelForWebSaleItems.setBasePrice(newSalePrice-queryHotelForWebSaleItems.getCommission());
	    								break;
	    							case 8:
	    								queryHotelForWebSaleItems.setCommission(BigDecimal.valueOf((newSalePrice-queryHotelForWebSaleItems.getBreakfastPrice()*
	    										queryHotelForWebSaleItems.getBreakfastNum())/(queryHotelForWebSaleItems.getBreakfastPrice()*
	    										queryHotelForWebSaleItems.getBreakfastNum()/(queryHotelForWebSaleItems.getAdvicePrice()-queryHotelForWebSaleItems.getCommission()/queryHotelForWebSaleItems.getCommissionRate()))*
	    	    								queryHotelForWebSaleItems.getCommissionRate()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	    								queryHotelForWebSaleItems.setBasePrice(newSalePrice-queryHotelForWebSaleItems.getCommission());
	    								break;
	    							case 9:
	    								queryHotelForWebSaleItems.setCommission(BigDecimal.valueOf(newSalePrice*queryHotelForWebSaleItems.getCommissionRate()).
	    										setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	    								queryHotelForWebSaleItems.setBasePrice(newSalePrice-queryHotelForWebSaleItems.getCommission());
	    								break;
	    							case 10:
	    								queryHotelForWebSaleItems.setCommission(BigDecimal.valueOf(newSalePrice*queryHotelForWebSaleItems.getCommissionRate()).
	    										setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	    								queryHotelForWebSaleItems.setBasePrice(newSalePrice-queryHotelForWebSaleItems.getCommission());
	    								break;
	    							case 11:
	    								queryHotelForWebSaleItems.setCommission(BigDecimal.valueOf((newSalePrice-queryHotelForWebSaleItems.getBreakfastPrice()*
	    										queryHotelForWebSaleItems.getBreakfastNum())*queryHotelForWebSaleItems.getCommissionRate()).
	    										setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
	    								queryHotelForWebSaleItems.setBasePrice(newSalePrice-queryHotelForWebSaleItems.getCommission());
	    								break;
	    							}
	        					}
	        				}
	        			}
	        			
	        		}
	        		if("3".equals(htlFavourableclause.getFavourableType())){
	        			if(!queryHotelForWebSaleItems.getFellowDate().before(htlFavourableclause.getBeginDate()) 
	        					&& !queryHotelForWebSaleItems.getFellowDate().after(htlFavourableclause.getEndDate())){
	        				List<HtlFavouraParameter> lstPackagerate = htlFavourableclause.getLstPackagerate();
	            			if(null != lstPackagerate){
	            				for(int j = 0; j < lstPackagerate.size();j++ ){
	                				if(f == lstPackagerate.get(j).getPackagerateNight()-1){
	                					for (int a = y-(int)(lstPackagerate.get(j).getPackagerateNight()-1); a <= y; a++ ){
	                						if(a == y-(int)(lstPackagerate.get(j).getPackagerateNight()-1)){
	                							queryHotelForWebSaleItem =(QueryHotelForWebSaleItems)faceItemsLis.get(a);
	                							queryHotelForWebSaleItem.setSalePrice(lstPackagerate.get(j).getPackagerateSaleprice());
	                							//-----------longkangfu-------------2012-6-18
	                							queryHotelForWebSaleItem.setCommission(lstPackagerate.get(j).getPackagerateCommission());
	                						}else{
	                							queryHotelForWebSaleItem =(QueryHotelForWebSaleItems)faceItemsLis.get(a);
	                							queryHotelForWebSaleItem.setSalePrice(99999.0);
	                							//-----------longkangfu-------------2012-6-18
	                							queryHotelForWebSaleItem.setCommission(0);
	                						}
	                						
	                						 //根据连住优惠更改了售价 的标记 add by shengwei.zuo 2009-11-13
	                						queryHotelForWebSaleItem.setChgSalePri(true);
	                						
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
	        			if(!queryHotelForWebSaleItems.getFellowDate().before(htlFavourableclause.getBeginDate()) 
	            					&& !queryHotelForWebSaleItems.getFellowDate().after(htlFavourableclause.getEndDate())){
	        				List<HtlFavouraParameter> lstPackagerate = htlFavourableclause.getLstPackagerate();
	            			if(null != lstPackagerate){
	            				for(int j = 0; j < lstPackagerate.size();j++ ){
	            					int ConNumD = (int)(lstPackagerate.get(j).getContinueNight()+0);
	            					int doNumD = (int)(lstPackagerate.get(j).getDonateNight()+0);
	            					List<HtlEveningsRent> lstEveningsRent= lstPackagerate.get(j).getLstEveningsRent();
	            					int numDay = ConNumD+doNumD;
	                				if(f == numDay-1){
	                					for(int aa =0;aa<lstEveningsRent.size();aa++){
	    	        						int night =(int)(lstEveningsRent.get(aa).getNight()+0);
	    	        						queryHotelForWebSaleItem =(QueryHotelForWebSaleItems)faceItemsLis.get(y-(numDay-1)+(night-1));
	    	        						if(0 == (int)(lstEveningsRent.get(aa).getSalePrice()+0)){
	    	        							queryHotelForWebSaleItem.setSalePrice(99999.0);
	    	        							//-----------longkangfu-------------2012-6-18
	                							queryHotelForWebSaleItem.setCommission(0);
		        							}else{
		        								queryHotelForWebSaleItem.setSalePrice(lstEveningsRent.get(aa).getSalePrice());
		        								//-----------longkangfu-------------2012-6-18
	                							queryHotelForWebSaleItem.setCommission(lstEveningsRent.get(aa).getCommission());
		        							}
	    	        						
	    	        						//根据连住优惠更改了售价 的标记 add by shengwei.zuo 2009-11-13
	                						queryHotelForWebSaleItem.setChgSalePri(true);
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
	 
	 /**
	 * add by zhijie.gu 2009-09-15 hotel2.9.3 改变在优惠条款内的价格，
	 * 改变OrPriceDetail的价格，当连住N送M时，M价格为0
	 */
	 public int changePriceForFavourableThree(List faceItemsLis,QueryHotelForWebSaleItems queryHotelForWebSaleItems,long hotelId,long childRoomTypeId,int y, int f){
		//连住优惠过程量
			QueryHotelForWebSaleItems queryHotelForWebSaleItem = new QueryHotelForWebSaleItems();
	    	List<HtlFavourableclause> htlFavourableclauseList = queryFavourableResult(hotelId,childRoomTypeId);
	        //连住优惠对价格处理
	        if(null !=htlFavourableclauseList){
	        	for(int i= 0 ;i<htlFavourableclauseList.size();i++){
	        		HtlFavourableclause htlFavourableclause=htlFavourableclauseList.get(i);
	        		
	        		if("2".equals(htlFavourableclause.getFavourableType())){
	        			if(!queryHotelForWebSaleItems.getFellowDate().before(htlFavourableclause.getBeginDate()) 
	        					&& !queryHotelForWebSaleItems.getFellowDate().after(htlFavourableclause.getEndDate())){
	        				List<HtlFavouraParameter> lstPackagerate = htlFavourableclause.getLstPackagerate();
	        				if(null != lstPackagerate){
	        					for(int j = 0; j < lstPackagerate.size();j++ ){
	        					
	        						BigDecimal b = new BigDecimal(""+lstPackagerate.get(j).getDiscount()*queryHotelForWebSaleItems.getSalePrice()/10);    
	            					//逢一进十
	            					if(1 == lstPackagerate.get(j).getDecimalPointType()){
	            						queryHotelForWebSaleItems.setSalePrice(Math.ceil(b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()));
	            					}
	            					//四舍五入
	            					if(2 == lstPackagerate.get(j).getDecimalPointType()){
	            						queryHotelForWebSaleItems.setSalePrice(b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue()) ;
	            					}
	            					//直接取整
	            					if(3 == lstPackagerate.get(j).getDecimalPointType()){
	            						queryHotelForWebSaleItems.setSalePrice(Math.floor(lstPackagerate.get(j).getDiscount()*queryHotelForWebSaleItems.getSalePrice()/10));
	            					}
	            					//四舍五入取整
	            					if(4 == lstPackagerate.get(j).getDecimalPointType()){
	            						queryHotelForWebSaleItems.setSalePrice(b.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue()) ;
	            					}
	        					
	        					}
	        					
	        				}
	        				
	        			}
	        			
	        		}
	        		if("3".equals(htlFavourableclause.getFavourableType())){
	        			if(!queryHotelForWebSaleItems.getFellowDate().before(htlFavourableclause.getBeginDate()) 
	        					&& !queryHotelForWebSaleItems.getFellowDate().after(htlFavourableclause.getEndDate())){
	        				List<HtlFavouraParameter> lstPackagerate = htlFavourableclause.getLstPackagerate();
	            			if(null != lstPackagerate){
	            				for(int j = 0; j < lstPackagerate.size();j++ ){
	                				if(f == lstPackagerate.get(j).getPackagerateNight()-1){
	                					for (int a = y-(int)(lstPackagerate.get(j).getPackagerateNight()-1); a <= y; a++ ){
	                						if(a == y-(int)(lstPackagerate.get(j).getPackagerateNight()-1)){
	                							queryHotelForWebSaleItem =(QueryHotelForWebSaleItems)faceItemsLis.get(a);
	                							queryHotelForWebSaleItem.setSalePrice(lstPackagerate.get(j).getPackagerateSaleprice());
	                						}else{
	                							queryHotelForWebSaleItem =(QueryHotelForWebSaleItems)faceItemsLis.get(a);
	                							queryHotelForWebSaleItem.setSalePrice(0.0);
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
	        			if(!queryHotelForWebSaleItems.getFellowDate().before(htlFavourableclause.getBeginDate()) 
	            					&& !queryHotelForWebSaleItems.getFellowDate().after(htlFavourableclause.getEndDate())){
	        				List<HtlFavouraParameter> lstPackagerate = htlFavourableclause.getLstPackagerate();
	            			if(null != lstPackagerate){
	            				for(int j = 0; j < lstPackagerate.size();j++ ){
	            					int ConNumD = (int)(lstPackagerate.get(j).getContinueNight()+0);
	            					int doNumD = (int)(lstPackagerate.get(j).getDonateNight()+0);
	            					List<HtlEveningsRent> lstEveningsRent= lstPackagerate.get(j).getLstEveningsRent();
	            					int numDay = ConNumD+doNumD;
	                				if(f == numDay-1){
	                					for(int aa =0;aa<lstEveningsRent.size();aa++){
	    	        						int night =(int)(lstEveningsRent.get(aa).getNight()+0);
	    	        						queryHotelForWebSaleItem =(QueryHotelForWebSaleItems)faceItemsLis.get(y-(numDay-1)+(night-1));
	    	        						if(0 == (int)(lstEveningsRent.get(aa).getSalePrice()+0)){
	    	        							queryHotelForWebSaleItem.setSalePrice(0.0);
		        							}else{
		        								queryHotelForWebSaleItem.setSalePrice(lstEveningsRent.get(aa).getSalePrice());
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
	/**
	* add by zhijie.gu 2009-09-15 hotel2.9.3 改变在优惠条款内的佣金，
	*/
	 public int changeBasePriceCommission(HtlPrice htlPrice,List orPriceDetailList,OrPriceDetail orPriceDetailItems,long hotelId,long childRoomTypeId,int y, int f){
			List<HtlFavourableclause> htlFavourableclauseList = queryFavourableResult(hotelId,childRoomTypeId);
			//连住优惠过程量
			OrPriceDetail orPriceDetailItemFav = new OrPriceDetail();
	    	//优惠条款改变佣金。优惠条款类型2为打折优惠，这个类型不改变佣金，所以不对2处理
	        if(null !=htlFavourableclauseList){
	        	for(int i= 0 ;i<htlFavourableclauseList.size();i++){
	        		HtlFavourableclause htlFavourableclause=htlFavourableclauseList.get(i);
	        		if("2".equals(htlFavourableclause.getFavourableType())){
	    				if(!orPriceDetailItems.getNight().before(htlFavourableclause.getBeginDate()) 
	    						&& !orPriceDetailItems.getNight().after(htlFavourableclause.getEndDate())){
	    					List<HtlFavouraParameter> lstPackagerate = htlFavourableclause.getLstPackagerate();
	    					if(null != lstPackagerate){
	    						
	    						orPriceDetailItems.setIsFavourable(true);
	    						
	    						for(int j = 0; j < lstPackagerate.size();j++ ){
	    							
	    							//应对变价，需重新计算销售价
	    							BigDecimal b = new BigDecimal(""+lstPackagerate.get(j).getDiscount()*htlPrice.getSalePrice()/10);    
	            					//逢一进十
	            					if(1 == lstPackagerate.get(j).getDecimalPointType()){
	            						orPriceDetailItems.setSalePrice(Math.ceil(b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()));
	            					}
	            					//四舍五入
	            					if(2 == lstPackagerate.get(j).getDecimalPointType()){
	            						orPriceDetailItems.setSalePrice(b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue()) ;
	            					}
	            					//直接取整
	            					if(3 == lstPackagerate.get(j).getDecimalPointType()){
	            						orPriceDetailItems.setSalePrice(Math.floor(lstPackagerate.get(j).getDiscount()*htlPrice.getSalePrice()/10));
	            					}
	            					//四舍五入取整
	            					if(4 == lstPackagerate.get(j).getDecimalPointType()){
	            						orPriceDetailItems.setSalePrice(b.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue()) ;
	            					}
	    							
	    							
	    							
	    							double oldSalePrice = htlPrice.getSalePrice();
	    							double newSalePrice = orPriceDetailItems.getSalePrice();
	    							int k = 0;
	    							if("0".equals(htlPrice.getFormulaId())){
	    								k=0;
	    							}else if("priceA".equals(htlPrice.getFormulaId())){
	    								k=1;
	    							}else if("priceB".equals(htlPrice.getFormulaId())){
	    								k=2;
	    							}else if("priceC".equals(htlPrice.getFormulaId())){
	    								k=3;
	    							}else if("priceD".equals(htlPrice.getFormulaId())){
	    								k=4;
	    							}else if("priceE".equals(htlPrice.getFormulaId())){
	    								k=5;
	    							}else if("priceF".equals(htlPrice.getFormulaId())){
	    								k=6;
	    							}else if("priceG".equals(htlPrice.getFormulaId())){
	    								k=7;
	    							}else if("priceH".equals(htlPrice.getFormulaId())){
	    								k=8;
	    							}else if("priceI".equals(htlPrice.getFormulaId())){
	    								k=9;
	    							}else if("priceJ".equals(htlPrice.getFormulaId())){
	    								k=10;
	    							}else if("priceK".equals(htlPrice.getFormulaId())){
	    								k=11;
	    							}
	    							switch(k){
	    							case 0:
	    								orPriceDetailItems.setCommission(htlPrice.getCommission());
	    								orPriceDetailItems.setBasePrice(newSalePrice-orPriceDetailItems.getCommission());
	    								break;
	    							case 1:
	    								orPriceDetailItems.setCommission(BigDecimal.valueOf(
	    										(newSalePrice/(oldSalePrice/(htlPrice.getCommission()/
	    												htlPrice.getCommissionRate()+
	    												(htlPrice.getIncBreakfastPrice()*Double.parseDouble(htlPrice.getIncBreakfastNumber()))))-
	    												htlPrice.getIncBreakfastPrice()*Double.parseDouble(htlPrice.getIncBreakfastNumber()))*
	    												htlPrice.getCommissionRate()).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
	    								orPriceDetailItems.setBasePrice(newSalePrice-orPriceDetailItems.getCommission());
	    								break;
	    							case 2:
	    								orPriceDetailItems.setCommission(BigDecimal.valueOf((newSalePrice-
	    										htlPrice.getIncBreakfastPrice()*Double.parseDouble(htlPrice.getIncBreakfastNumber()))/
	    										((oldSalePrice-htlPrice.getIncBreakfastPrice()*Double.parseDouble(htlPrice.getIncBreakfastNumber()))/
	    												(htlPrice.getCommission()/htlPrice.getCommissionRate()))*htlPrice.getCommissionRate()).
	    												setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
	    								orPriceDetailItems.setBasePrice(newSalePrice-orPriceDetailItems.getCommission());
	    								break;
	    							case 3:
	    								orPriceDetailItems.setCommission(BigDecimal.valueOf(newSalePrice*htlPrice.getCommissionRate()).
	    										setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	    								orPriceDetailItems.setBasePrice(newSalePrice-orPriceDetailItems.getCommission());
	    								break;
	    							case 4:
	    								orPriceDetailItems.setCommission(BigDecimal.valueOf((newSalePrice/(oldSalePrice/(htlPrice.getCommission()/
	    										htlPrice.getCommissionRate())))*htlPrice.getCommissionRate()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	    								orPriceDetailItems.setBasePrice(newSalePrice-orPriceDetailItems.getCommission());
	    								break;
	    							case 5:
	    								orPriceDetailItems.setCommission(BigDecimal.valueOf((newSalePrice-htlPrice.getIncBreakfastPrice()*
	    										Double.parseDouble(htlPrice.getIncBreakfastNumber()))*htlPrice.getCommissionRate()).
	    										setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	    								
	    								orPriceDetailItems.setBasePrice(newSalePrice-orPriceDetailItems.getCommission());
	    								break;
	    							case 6:
	    								orPriceDetailItems.setCommission(htlPrice.getCommission());
	    								orPriceDetailItems.setBasePrice(newSalePrice-orPriceDetailItems.getCommission());
	    								break;
	    							case 7:
	    								orPriceDetailItems.setCommission(BigDecimal.valueOf((newSalePrice-htlPrice.getIncBreakfastPrice()*
	    										Double.parseDouble(htlPrice.getIncBreakfastNumber()))*htlPrice.getCommissionRate()).
	    										setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	    								orPriceDetailItems.setBasePrice(newSalePrice-orPriceDetailItems.getCommission());
	    								break;
	    							case 8:
	    								orPriceDetailItems.setCommission(BigDecimal.valueOf((newSalePrice-htlPrice.getIncBreakfastPrice()*
	    										Double.parseDouble(htlPrice.getIncBreakfastNumber()))/(htlPrice.getIncBreakfastPrice()*
	    	    										Double.parseDouble(htlPrice.getIncBreakfastNumber())/(htlPrice.getAdvicePrice()-htlPrice.getCommission()/htlPrice.getCommissionRate()))*
	    										htlPrice.getCommissionRate()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	    								orPriceDetailItems.setBasePrice(newSalePrice-orPriceDetailItems.getCommission());
	    								break;
	    							case 9:
	    								orPriceDetailItems.setCommission(BigDecimal.valueOf(newSalePrice*htlPrice.getCommissionRate()).
	    										setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	    								orPriceDetailItems.setBasePrice(newSalePrice-orPriceDetailItems.getCommission());
	    								break;
	    							case 10:
	    								orPriceDetailItems.setCommission(BigDecimal.valueOf(newSalePrice*htlPrice.getCommissionRate()).
	    										setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	    								orPriceDetailItems.setBasePrice(newSalePrice-orPriceDetailItems.getCommission());
	    								break;
	    							case 11:
	    								orPriceDetailItems.setCommission(BigDecimal.valueOf((newSalePrice-htlPrice.getIncBreakfastPrice()*
	    										Double.parseDouble(htlPrice.getIncBreakfastNumber()))*htlPrice.getCommissionRate()).
	    										setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
	    								orPriceDetailItems.setBasePrice(newSalePrice-orPriceDetailItems.getCommission());
	    								break;
	    							
	    							}
	    							
	    						
	    						}
	    						
	    					}
	    					
	    				}
	    				
	    			}
	        		if("3".equals(htlFavourableclause.getFavourableType())){
	        			if(!orPriceDetailItems.getNight().before(htlFavourableclause.getBeginDate()) 
	        					&& !orPriceDetailItems.getNight().after(htlFavourableclause.getEndDate())){
	        				List<HtlFavouraParameter> lstPackagerate = htlFavourableclause.getLstPackagerate();
	            			if(null != lstPackagerate){
	            				for(int j = 0; j < lstPackagerate.size();j++ ){
	                				if(f == lstPackagerate.get(j).getPackagerateNight()-1){
	                					for (int a = y-(int)(lstPackagerate.get(j).getPackagerateNight()-1); a <= y; a++ ){
	                						orPriceDetailItemFav =(OrPriceDetail)orPriceDetailList.get(a);
	                						if(a==y-(int)(lstPackagerate.get(j).getPackagerateNight()-1)){
	                							orPriceDetailItemFav.setCommission(lstPackagerate.get(j).getPackagerateCommission());
	                							orPriceDetailItemFav.setBasePrice(BigDecimal.valueOf(lstPackagerate.get(j).getPackagerateSaleprice()-
	                									lstPackagerate.get(j).getPackagerateCommission()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	                							orPriceDetailItemFav.setSalePrice(lstPackagerate.get(j).getPackagerateSaleprice());
	                						}else{
	                							orPriceDetailItemFav.setCommission(0.0);
	                							orPriceDetailItemFav.setBasePrice(0.0);
	                							orPriceDetailItemFav.setSalePrice(0.0);
	                						}
	                						orPriceDetailItemFav.setIsFavourable(true);
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
	        			if(!orPriceDetailItems.getNight().before(htlFavourableclause.getBeginDate()) 
	        					&& !orPriceDetailItems.getNight().after(htlFavourableclause.getEndDate())){
	        				List<HtlFavouraParameter> lstPackagerate = htlFavourableclause.getLstPackagerate();
	            			if(null != lstPackagerate){
	            				for(int j = 0; j < lstPackagerate.size();j++ ){
	            					int ConNumD = (int)(lstPackagerate.get(j).getContinueNight()+0);
	            					int doNumD = (int)(lstPackagerate.get(j).getDonateNight()+0);
	            					List<HtlEveningsRent> lstEveningsRent= lstPackagerate.get(j).getLstEveningsRent();
	            					int numDay = ConNumD+doNumD;
	                				if(f == numDay-1){
	                					for(int aa =0;aa<lstEveningsRent.size();aa++){
	    	        						int night =(int)(lstEveningsRent.get(aa).getNight()+0);
	    	        						orPriceDetailItemFav =(OrPriceDetail)orPriceDetailList.get(y-(numDay-1)+(night-1));
	    	        						orPriceDetailItemFav.setCommission(lstEveningsRent.get(aa).getCommission());
	    	        						orPriceDetailItemFav.setBasePrice(BigDecimal.valueOf(lstEveningsRent.get(aa).getSalePrice()-lstEveningsRent.get(aa).getCommission()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	    	        						orPriceDetailItemFav.setIsFavourable(true);
	    	        						if(0 == (int)(lstEveningsRent.get(aa).getSalePrice()+0)){
	    	        							orPriceDetailItemFav.setSalePrice(0.0);
		        							}else{
		        								orPriceDetailItemFav.setSalePrice(lstEveningsRent.get(aa).getSalePrice());
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
	        Object[] obj = new Object[] { date, childRoomId, payMethod };
	        return (HtlPrice) super.find(hsql, obj);
	    }
	
	/**
	 * add by shaojun.yang 2009-11-09 hotel2.10 查找酒店附近的周边酒店 
	 */
	public List<GisInfo> calculateMapNearHotel(LatLng latLng, Long hotelId, String city, int count,Class classes) {
		
		String sql="SELECT gisid,chn_name,longitude,latitude,chn_address,hotel_id,POWER(distance,0.5)FROM " +
		"(SELECT DISTINCT hotel_id,gisid,chn_name,longitude,latitude,chn_address,(ABS((?-longitude)*(?-longitude))+ABS((?-latitude)*(?-latitude)))as distance" +
		" from htl_hotel where longitude>0 and latitude>0 and longitude is not null and latitude is not null and gisid>0 and hotel_id!=? and active=1 and HOTEL_SYSTEM_SIGN='01' order by distance asc)" +
		" WHERE ROWNUM <= 5";
			//"select distinct gisid,chn_name,longitude,latitude,chn_address,hotel_id,power((ABS(?-longitude)*ABS(?-longitude)+ABS(?-latitude)*ABS(?-latitude)),0.5) as distance from htl_hotel where longitude>0 and latitude>0 and longitude is not null and latitude is not null and gisid>0 and hotel_id!=? and active=1 and HOTEL_SYSTEM_SIGN='01' order by distance asc";
		Object[] values={latLng.getLatitude(),latLng.getLatitude(),latLng.getLongitude(),latLng.getLongitude(),hotelId};
		 List list = super.doquerySQL(sql, values,0,count, false,null);
		 List<GisInfo> gisList=new ArrayList<GisInfo>();
		 if(null!=list && list.size()>0){
			 for(Iterator i = list.iterator();i.hasNext();){
				 Object[] objects=(Object[]) i.next();
				 if(null!=objects){
	                 GisInfo info=new GisInfo();
	                 info.setGisId(Long.valueOf(objects[0].toString()));
	                 info.setName((String)objects[1]);
	                 LatLng newLatLng=new LatLng(Double.valueOf(objects[2].toString()),Double.valueOf(objects[3].toString()));
	                 info.setLatLng(newLatLng);
	                 info.setAddress((String)objects[4]);
	                 info.setDataType(Integer.valueOf(objects[5].toString()));
	                 info.setDistance(CalculateDistance.getDistance(latLng, newLatLng));
	                 gisList.add(info);
				 }
			 }
		 }
		 return gisList;
	}
	
	 /**
     * 根据酒店id获取酒店名称。、
     * 
     * @param hotelId
     * @return
     */
    public String queryHotelName(long hotelId) {
        // String hsql = "select distinct h.city from HtlHotel h,HtlPrice p where h.theme like '%" +
        // theme + "%'";
        // List list = super.doquery(hsql, false);
        String sql = "select  h.chnName from HtlHotel h" + " where h.ID =?";
        Object[] obj = new Object[]{hotelId};
        
        String hotelName = super.getSession().createSQLQuery(sql).getQueryString();
        return (String) super.find(sql, obj);
       
       
    }
	
	/**
     * Normand 获得order中的OrpriceDetail 封装到 List<QueryHotelForWebSaleItems>中  
     * add by shizhongwen
     * 时间:Sep 9, 2009  5:21:36 PM
     * @param order
     * @return
     */
    public List<QueryHotelForWebSaleItems> getSaleItemPriceList(List<QueryHotelForWebSaleItems> priceLis,OrOrder order){
        if(null==order){
            return null;
        }
        
        List<OrPriceDetail> pricedetailList = order.getPriceList();
        for(QueryHotelForWebSaleItems websaleitem:priceLis){
            if(null!=websaleitem){
                for(OrPriceDetail pricedetail:pricedetailList){
                    //日期相等
                    if(DateUtil.getDay(websaleitem.getFellowDate(),pricedetail.getNight())==0){
                        //如果是面付或者是面转预则用散客价
                        if(PayMethod.PAY.equals(order.getPayMethod()) 
                            || PayMethod.CONVERSION.equals(order.getPayMethod())){
                            websaleitem.setSalePrice(pricedetail.getSalePrice());
                        }
                        //如果是预付则用商旅价
                        if(PayMethod.PRE_PAY.equals(order.getPayMethod())){
                            websaleitem.setSalePrice(pricedetail.getTmcPrice());
                        }
                    }
                }
            }
        }
        
        return priceLis;
    }
    
    /**
     * 根据酒店所在城市Id获取所在城市的商业区。、
     * 
     * @param cityName
     * @return
     */
    public List queryBusinessForHotelInfo(String cityName){
    	String sql="select cc.name,cc.title from cmd.t_cdm_basedata cc where cc.treepath ="+
    			"(select c.treepath from cmd.t_cdm_basedata c where c.levels = 5 and c.name = '"+cityName+"') || "+"'/"+
    			cityName+"/business'";
    	
    	List businessLis=super.doquerySQL(sql, false);
    	return businessLis;
    	
    }
    
    /**
     * 根据品牌ID 和城市ID查询出对应的酒店
     * add by haibo.li 网站改版 2009-12-7
     */
    public List<HtlHotel> queryParentHotelInfo(String cityCode,String parentId){
    	String hsql = "from HtlHotel where city =? and parentHotelGroup =?";
    	List <HtlHotel>parentList  = super.doquery(hsql, new Object[]{cityCode,parentId}, false);
    	//String sql = "select * from htl_hotel where city= '"+cityCode+"' and parentHotelGroup = '"+parentId+"'";
    	//= super.doquerySQL(sql, false);
    	return parentList;
    }
    
    
    /**
	  * 根据品牌ID 和城市ID查询出对应的品牌名称
	  * @param cityName  add by shengwei.zuo 2010-3-9
	  * @return
	  */
	public String queryParentNaHtlInfo(String cityCode,String parentId) {
		String parentNa= "";
		String sql = "select distinct  g.hotel_group_id,g.group_name  from htl_hotel h,htl_hotel_group g  where h.city = ? \n"+
				     " and  h.parent_hotel_group = g.hotel_group_id and h.parent_hotel_group = ? ";
		List list = super.doquerySQL(sql, new Object[]{cityCode,parentId}, false);
		for(Iterator i = list.iterator();i.hasNext();){
			Object[]  parentNaLst=(Object[])i.next();
			parentNa=(String) parentNaLst[1];
		}
		return parentNa;
	}
    
    /**
	  * 根据酒店所在城市Id获取所在城市的行政区与商业区
	  * @param cityName
	  * @return
	  */
	public Map queryBusinessForCityId(String cityName) {
		Map map=new HashMap();
		String sql = "select id,title,treepath, name from cmd.t_cdm_basedata where parent_id in (select id from cmd.t_cdm_basedata where parent_id in(select id from cmd.t_cdm_basedata where title=? and levels=5))";
		List list = super.doquerySQL(sql, new Object[]{cityName}, false);
		List districtList=new ArrayList();
		List businessList=new ArrayList();
		for(Iterator i = list.iterator();i.hasNext();){
			Object[] objects=(Object[]) i.next();
			if(null!=objects&&objects.length>3){
				String treePath=(String) objects[2];
				if(treePath.contains("business")){
					businessList.add(objects);
				}
				if(treePath.contains("district")){
					districtList.add(objects);
				}
			}
		}
		map.put("business", businessList);
		map.put("district", districtList);
		return map;
	}
	
    /**
	  * 根据酒店所在城市Id和行政区或商业区的编号，查询行政区或商业区的中文名
	  * @param cityName  add by shengwei.zuo 2010-3-8
	  * @return
	  */
	public String querydisBusNaForCityNa(String cityName,String disBusCode) {
		String disBusName= "";
		String sql = "select distinct id,title,name from cmd.t_cdm_basedata where parent_id in \n"+
		 		     "(select id from cmd.t_cdm_basedata where parent_id in(select id from cmd.t_cdm_basedata where title=? and levels=5)) and name=? ";
		List list = super.doquerySQL(sql, new Object[]{cityName,disBusCode}, false);
		for(Iterator i = list.iterator();i.hasNext();){
			Object[] disbusNaLst=(Object[]) i.next();
		   disBusName=(String) disbusNaLst[1];
		}
		log.info("==================================disBusName  : "+disBusName);
		return disBusName;
	}
    
	// 酒店详情页面查询附加服务（早餐和接送信息）
	public String queryHotelAdditionalServeForHotelInfo(long hotelId,
			Date beginDate, Date endDate) {
		List serviceInfoLis = new ArrayList();
		StringBuffer strBuffer = new StringBuffer();
		String breakfastWelcomelServiceStr="";
		// ----获取各种早餐服务信息
		HtlContract contract = contractManage.checkContractDateNew(hotelId,
				beginDate);
		if(null != contract){
			
			List<HtlChargeBreakfast> breakfastList = new ArrayList<HtlChargeBreakfast>();
			List<HtlWelcomePrice> welcomePriceList = new ArrayList<HtlWelcomePrice>();
			breakfastList = super.queryByNamedQuery("queryBreakfastsCheckoutDate",
					new Object[] {contract.getID(), beginDate });
			// 组装早餐信息字符串
			
			if (breakfastList.size() > 0) {
				strBuffer.append("&nbsp;&nbsp;&nbsp;&nbsp;");
				strBuffer.append("早餐服务：");	
				for (int i = 0; i < breakfastList.size(); i++) {
					
					HtlChargeBreakfast htlChargeBreakfast = breakfastList.get(i);
					//得到面预付转为对应的中文 add by diandian.hou 2010-11-9
					String payMothodStrOfCH = "";
					// 如果结束时间在时间段前，则结束
					if(endDate.before(htlChargeBreakfast.getBeginDate())){
						break;
					}
					if(PayMethod.PAY.equals(htlChargeBreakfast.getPayMethod())){
						payMothodStrOfCH = "面付";
					}
					if(PayMethod.PRE_PAY.equals(htlChargeBreakfast.getPayMethod())){
						payMothodStrOfCH = "预付";
					}
					if(i>0){
						strBuffer.append("<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
					}
					//只显示一个面付（预付）
					if(strBuffer.indexOf(payMothodStrOfCH) >= 0){
						strBuffer.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
					}else {
					    strBuffer.append(payMothodStrOfCH+"：");
					}		  
					boolean beginDateIsBetween = DateUtil.between(beginDate, htlChargeBreakfast.getBeginDate(), htlChargeBreakfast.getEndDate());
					boolean endDateIsBetween = DateUtil.between(endDate, htlChargeBreakfast.getBeginDate(), htlChargeBreakfast.getEndDate());	
					// 开始，结束都在时间段内，不显示时间段,如果不在的话，再判断
					if(beginDateIsBetween == false || endDateIsBetween == false){
						Date beginDateShow = beginDate.after(htlChargeBreakfast.getBeginDate()) ? beginDate : htlChargeBreakfast.getBeginDate();
						Date endDateShow = endDate.before(htlChargeBreakfast.getEndDate()) ? endDate : htlChargeBreakfast.getEndDate();
						strBuffer.append(DateUtil.dateToString(beginDateShow) + "至"
								+ DateUtil.dateToString(endDateShow) + "，");
					}
					for (int j = 0; j < htlChargeBreakfast.getBreakfastFees()
							.size(); j++) {
						HtlBreakfast fast = (HtlBreakfast) htlChargeBreakfast
								.getBreakfastFees().get(j);
						if (null != fast.getType() && 0 < fast.getBasePrice()) {
							 if ("chineseFood".equals(fast.getType())) {
								 strBuffer.append("中早");
		                        } 
							 else if ("westernFood".equals(fast.getType())) {
		                        	strBuffer.append("西早");
		                        } 
							 else if ("buffetDinner".equals(fast.getType())) {
		                        	strBuffer.append("自助早");
		                        }
							 strBuffer.append(contract.getCurrency()+
										 Double.valueOf(Math.rint(fast.getBasePrice())).intValue() + "；");
							
						}
						
	
					}
	
				}
	
			}
			welcomePriceList = super.queryByNamedQuery(
					"queryWelcomePricesCheckoutDate", new Object[] {
							contract.getID(), beginDate });
			// 组装接送信息字符串
			
			if (welcomePriceList.size() > 0) {
				strBuffer.append("<br />&nbsp;&nbsp;&nbsp;&nbsp;");
				strBuffer.append("接送服务：");
				for (int n = 0; n < welcomePriceList.size(); n++) {
					if(n>0 ){
						strBuffer.append("<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
					}
					HtlWelcomePrice htlWelcomePrice = welcomePriceList.get(n);
					
					for (int m = 0; m < htlWelcomePrice.getWelcomeFees().size(); m++) {
						
						HtlChildWelcomePrice htlChildWelcomePrice = (HtlChildWelcomePrice) htlWelcomePrice
								.getWelcomeFees().get(m);
						if(m>0){
							strBuffer.append("<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
							
						}
						
						
						strBuffer.append(DateUtil.dateToString(htlWelcomePrice.getBeginDate()) + "至"
								+ DateUtil.dateToString(htlWelcomePrice.getEndDate()) + "，");
						if("1".equals(htlChildWelcomePrice.getWelcomeStype())){
							strBuffer.append("单程，");
						}else if("2".equals(htlChildWelcomePrice.getWelcomeStype())){
							strBuffer.append("双程，");
							
						}
						strBuffer.append("地点："
								+ htlChildWelcomePrice.getWelcomePlace()
								+ "，费用："+contract.getCurrency()
								+ Double.valueOf(Math.rint(htlChildWelcomePrice.getWelcomePrice())).intValue()
								+"；");
					}
				}
	
			}
		}
		breakfastWelcomelServiceStr = strBuffer.toString();
		
		
		return breakfastWelcomelServiceStr;

	}
	
	/**
     * 查询页数，页码，总记录数
     * @param queryBean
     * @return
     */
    public HotelPageForWebBean queryHotelPagesForWeb(QueryHotelForWebBean queryBean){
    	double minPrice = 0.0;
        HotelPageForWebBean hotelPageForWebBean = new HotelPageForWebBean();
        List<QueryHotelForWebResult> list = new ArrayList<QueryHotelForWebResult>();

		//需要使用查询总数
        queryBean.setIgnorePageCount(0);
		//忽略查询酒店列表
        queryBean.setIgnoreQueryList(1);
		
        int pageNo = 1;
        // 当指定跳到哪一页时
        if (0 != queryBean.getPageIndex()) {
            pageNo = queryBean.getPageIndex();
        }

        // 每页显示数
        int pageSize = queryBean.getPageSize();

        List<QueryHotelForWebResult> results = null;
        Integer totalSize = new Integer(0); // 查询结果后返回总记录数

        //TODO:delete debug
        long beginTime = System.currentTimeMillis();
        Map resultMap = hwebHotelDao.queryHotelResultListForWeb(queryBean);
        //TODO:delete debug
        long endTime = System.currentTimeMillis();
        log.info("==========+++++++++debug 004:"+queryBean.getCityId()+"异步获得分页数据：" + (endTime - beginTime) + "毫秒+++++++++==========");
        
        totalSize = (Integer) resultMap.get(KEY_TOTAL_SIZE);
        
        // 计算总页数
        int totalIndex = 0;
        if (0 != totalSize % pageSize) {
            totalIndex = totalSize / pageSize + 1;
        } else {
            totalIndex = totalSize / pageSize;
        }
        hotelPageForWebBean.setTotalIndex(totalIndex);
        hotelPageForWebBean.setPageSize(pageSize);
        hotelPageForWebBean.setPageIndex(pageNo);
        hotelPageForWebBean.setTotalSize(totalSize);
    	return hotelPageForWebBean;
    }
    
    /**
     * 生产bug求均价修改，计算一个价格类型查询日期内价格不为零的天数；
     * @param salePriceLis
     * @return
     */
    private int getSalePriceIsZeroNum(List salePriceLis){
    	int salePriceIsZeroNum = 0;
    	for (int pp = 0; pp < salePriceLis.size(); pp++) {
            QueryHotelForWebSaleItems queryHotelForWebSaleItems = 
            	(QueryHotelForWebSaleItems)salePriceLis.get(pp);
	        if(0.001 < queryHotelForWebSaleItems.getSalePrice()){
	        	salePriceIsZeroNum++;
	        }   
        
    	}
    	return salePriceIsZeroNum;
    }
	
    /**
     * 查询传入的所有价格类型的连住优惠信息
     * add by chenjiajie 2010-05-06
     * @param allPriceTypeList
     * @return favourableMap
     */
    private Map<String,List<HtlFavourableclause>> queryAllFavourableList(String allPriceTypeList){
    	//缓存参数传入的所有价格类型连住优惠信息
    	Map<String,List<HtlFavourableclause>> favourableMap = new HashMap<String,List<HtlFavourableclause>>();
    	if(StringUtil.isValidStr(allPriceTypeList)){
    		long beginDate1 = System.currentTimeMillis();
    		String hsql = "from HtlFavourableclause where priceTypeId in ("+allPriceTypeList+") and endDate >= trunc(sysdate)";
//    		log.info("---Favourableclause hql:"+hsql);
	        List<HtlFavourableclause> htlFavourableclauseList  = super.query(hsql);
	        long endDate1 = System.currentTimeMillis();
//	        log.info("---Favourableclause db time:" + (endDate1 - beginDate1) +"ms") ;
	        
	        long beginDate2 = System.currentTimeMillis();
	        if(null != htlFavourableclauseList && !htlFavourableclauseList.isEmpty()){
	        	//创建所有的key
	        	for (Iterator it = htlFavourableclauseList.iterator(); it.hasNext();) {
					HtlFavourableclause htlFavourableclause = (HtlFavourableclause) it.next();
					//格式: 酒店id_价格类型id
					String mapKey = htlFavourableclause.getHotelId() + "_" + htlFavourableclause.getPriceTypeId();
					favourableMap.put(mapKey, new ArrayList<HtlFavourableclause>());
				}
	        	//对每个key的值进行赋值
	        	for (Iterator it = htlFavourableclauseList.iterator(); it.hasNext();) {
					HtlFavourableclause htlFavourableclause = (HtlFavourableclause) it.next();
					//格式: 酒店id_价格类型id
					String mapKey = htlFavourableclause.getHotelId() + "_" + htlFavourableclause.getPriceTypeId();
		        	//缓存某个酒店价格类型连住优惠信息
					List<HtlFavourableclause> favourableclauseListCached = favourableMap.get(mapKey);
					favourableclauseListCached.add(htlFavourableclause);
					favourableMap.put(mapKey, favourableclauseListCached);
				}
	        }
	        long endDate2 = System.currentTimeMillis();
//	        log.info("---Favourableclause Map time:" + (endDate2 - beginDate2) +"ms") ;
    	}
    	return favourableMap;
    }
    
    /**
     * 封装所有需要再一次返回数据库的参数
     * add by chenjiajie 2010-05-06
     * @param results 调用pkg返回的List
     * @return
     */
    private Map getAllParamsForDB(List<QueryHotelForWebResult> results){
    	Map paramsMap = new HashMap();
    	StringBuffer allPriceTypeList = new StringBuffer(); //记录本次查询结果所有的价格类型id,用,分隔
    	StringBuffer benefitPriceTypeList = new StringBuffer(); //记录本次查询结果有优惠立减的价格类型id,用,分隔
    	StringBuffer cashReturnPriceTypeList = new StringBuffer(); //记录本次查询结果有现金返还的价格类型id,用,分隔
    	for (Iterator it = results.iterator(); it.hasNext();) {
			QueryHotelForWebResult queryHotelForWebResult = (QueryHotelForWebResult) it.next();
			String priceTypeId = queryHotelForWebResult.getChildRoomTypeId();
			/* 封装本次查询结果所有的价格类型ID begin */
			//priceTypeId有效 && priceTypeId不在字符串中
			if(StringUtil.isValidStr(priceTypeId)
					&& allPriceTypeList.indexOf(priceTypeId) < 0){
				allPriceTypeList.append(queryHotelForWebResult.getChildRoomTypeId());
				if(it.hasNext()){
					allPriceTypeList.append(",");
				}
			}
			/* 封装本次查询结果所有的价格类型ID end */
			
			/* 封装本次查询结果有优惠立减的价格类型ID begin */
			boolean hasBenefit = queryHotelForWebResult.getHasBenefit() == 1 ? true : false;
			//有立减标志 && priceTypeId有效 && priceTypeId不在字符串中
			if(hasBenefit && StringUtil.isValidStr(priceTypeId)
					&& benefitPriceTypeList.indexOf(priceTypeId) < 0){
				benefitPriceTypeList.append(queryHotelForWebResult.getChildRoomTypeId());
				if(it.hasNext()){
					benefitPriceTypeList.append(",");
				}
			}
			/* 封装本次查询结果有优惠立减的价格类型ID end */
			
			/* 封装本次查询结果有现金返还的价格类型ID */
			boolean hasCashReturn = queryHotelForWebResult.getHasCashReturn()==1 ?true:false;
			if(hasCashReturn&&StringUtil.isValidStr(priceTypeId)&&cashReturnPriceTypeList.indexOf(priceTypeId)<0){
				cashReturnPriceTypeList.append(queryHotelForWebResult.getChildRoomTypeId());
				if(it.hasNext()){
					cashReturnPriceTypeList.append(",");
				}
			}
		}
    	String allPriceTypeListStr = allPriceTypeList.toString();
    	String benefitPriceTypeListStr = benefitPriceTypeList.toString();
    	String hasCashReturnPriceTypeListStr = cashReturnPriceTypeList.toString();
    	//删除最后一个逗号','
    	allPriceTypeListStr = StringUtil.deleteLastChar(allPriceTypeListStr, ',');
    	benefitPriceTypeListStr = StringUtil.deleteLastChar(benefitPriceTypeListStr, ',');
    	hasCashReturnPriceTypeListStr = StringUtil.deleteLastChar(hasCashReturnPriceTypeListStr, ',');
    	paramsMap.put(KEY_ALL_PRICETYPELIST, allPriceTypeListStr);
    	paramsMap.put(KEY_BENEFIT_PRICETYPELIST, benefitPriceTypeListStr);
    	paramsMap.put(KEY_CASHRETURN_PRICETYPELIST, hasCashReturnPriceTypeListStr);
    	return paramsMap;
    }
    
    @Transactional(propagation=Propagation.REQUIRED)
    public long addClickAmount(HwClickAmount hwClickAmount) {
        try {
            super.save(hwClickAmount);
        } catch (Exception e) {
            log.error("新增点击量出错", e);
        }
        return 0;
    }
    
    public Map queryHwHotelIndexItems(String mark) {
    	Map result = new HashMap();
		if (mark.equals("public")) {
			List lowestPriceList = super.queryByNamedQuery("queryLowastPriceItems", new Object[] { "1" });
			List hotelCommendList = super.queryByNamedQuery("queryHotelCommendItems", new Object[] { "3" });
			List orderNumList = super.queryByNamedQuery("queryOrderNumItems", new Object[] { "2" });

			result.put("lowestPrice", null == lowestPriceList ? new ArrayList() : lowestPriceList);
			result.put("hotelCommend", null == hotelCommendList ? new ArrayList() : hotelCommendList);
			result.put("orderNum", null == orderNumList ? new ArrayList() : orderNumList);

		} else if (mark.equals("popular")) {
			List clickNumList = super.queryByNamedQuery("queryClickNumItems", new Object[] { "4" });
			result.put("clickNum", null == clickNumList ? new ArrayList() : clickNumList);
		} else if (mark.equals("htlCommend")) { //添加单独查询特推酒店
			List hotelCommendList = super.queryByNamedQuery("queryHotelCommendItems", new Object[] { "3" });
			result.put("htlCommend", null == hotelCommendList ? new ArrayList() : hotelCommendList);
		}
		return result;
    }
    
    @Transactional(propagation=Propagation.REQUIRED)
    public void addChannelLog(HtlChannelClickLog clickLog) {
		hwebHotelDao.saveChannelLog(clickLog);
	}
	
	public String getIpAddr(HttpServletRequest request) {      
		String ip = request.getHeader("clientip");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;      
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public void updateChannelLog(long logId) {
		hwebHotelDao.updateChannelLog(logId);
	} 
	
	
	public String getProjectCodeForWeb(HttpServletRequest request){
		String projectCode = "";//网络合作渠道号
		
		//第一步从url后缀中取projectcode
		projectCode = request.getParameter("projectcode"); 
		if(StringUtil.isValidStr(projectCode)) return projectCode;
		
		//url后缀中没有再从 cookie中取值
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals("projectcode")) {
					projectCode = cookies[i].getValue();
					break;
				}
			}
		}
		return projectCode;
   }
	
	/**
     * 查询网站订单统计数据
     * @return
     */
    public Long queryNetOrderStatistik(){
    	String queryID = "queryNetOrderStatistik";
		Object[] params = new Object[] {};
		List<Long> netOrders = super.queryByNamedQuery(queryID, params);
		if (netOrders.size() == 1) {
			return netOrders.get(0);
		} else {
			log.error("HwManageImpl :查询订单数不可能为0");
			return new Long(0);
		}
    }
    
    
    /**
     * 查询房型
     */
    public HtlRoomtype queryHtlRoomTypeForWeb(long hotelId, long roomTypeId) {
        HtlRoomtype htlRoomtype = new HtlRoomtype();
        List results = super.queryByNamedQuery("queryRoomTypeForWeb", new Object[] { hotelId,
            roomTypeId });
        if (null != results && results.size()>0) {
            htlRoomtype = (HtlRoomtype) results.get(0);
        }
        return htlRoomtype;
    }
    
    
    /**
	  * 查询芒果网站首页特推酒店所需数据
	  * @return List
	  */
    public List<HwHotelIndex> queryTTHtlData(){
		List<HwHotelIndex> list= new ArrayList<HwHotelIndex>();
		Session session=null;
    	String parentNa= "";
    	StringBuffer sql = new StringBuffer();
    	sql.append("select hotelid,");
    	sql.append("citycode,");
    	sql.append("hotelname,");
    	sql.append("hotel_star,");
    	sql.append("lowestpricecurrency,");
    	sql.append("lowestprice,");
    	sql.append("returncash ");
    	sql.append("from (select hl.hotelid,");
    	sql.append("hl.citycode,");
    	sql.append("hl.hotelname,");
    	sql.append("hh.hotel_star,");
    	sql.append("hl.lowestpricecurrency,");
    	sql.append("hl.lowestprice,");
    	sql.append("hl.returncash,");
    	sql.append("row_number() over(partition by hl.hotelid, hl.citycode order by createtime desc) rn ");
    	sql.append("from htl_hotel hh, htl_lowest_price hl ");
    	sql.append("where hh.hotel_id = hl.hotelid and hl.abledate=trunc(sysdate)+1) ");
    	sql.append("where rn = 1 ");
        try{
            session =super.getSessionFactory().openSession();
        	Query query =session.createSQLQuery(sql.toString());
        	System.out.println(sql.toString());
        	List<Object []> objects = query.list();
        	if(objects!=null && objects.size()>0){
	        	for(Object [] obj : objects){
	        		HwHotelIndex hwHotelIndex = new HwHotelIndex();
	        		hwHotelIndex.setHotelId(obj!=null && obj[0]!=null ? String.valueOf(obj[0]) : "");
	        		hwHotelIndex.setCity(obj!=null && obj[1]!=null ? String.valueOf(obj[1]) : "");
	        		hwHotelIndex.setChnName(obj!=null && obj[2]!=null ? String.valueOf(obj[2]) : "");
	        		hwHotelIndex.setHotelStar(obj!=null && obj[3]!=null ? String.valueOf(obj[3]) : "");
	        		hwHotelIndex.setCurrency(obj!=null && obj[4]!=null ? String.valueOf(obj[4]) : "");
	        		hwHotelIndex.setLowestPrice(obj!=null && obj[5]!=null ? Double.parseDouble(String.valueOf(obj[5])) : 0);
	        		hwHotelIndex.setLowestFavPrice(obj!=null && obj[6]!=null && !String.valueOf(obj[6]).equals("X")? Double.parseDouble(String.valueOf(obj[6])) : 0);
	        		list.add(hwHotelIndex);
	        	}
        	}
        }catch(Exception e){
        	log.error("特推酒店查询酒店价格数据错误", e);
        }finally{
        	session.flush();
        	session.clear();
        	session.close();
        }
		return list;
    }
    
    
    
    
    /** ********** getter and setter end ***************** */
	public IBenefitService getBenefitService() {
		return benefitService;
	}

	public void setBenefitService(IBenefitService benefitService) {
		this.benefitService = benefitService;
	}

	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	public void setCommissionService(CommissionService commissionService) {
		this.commissionService = commissionService;
	}
	
	public void setHwebHotelDao(HWebHotelDAO hwebHotelDao) {
		this.hwebHotelDao = hwebHotelDao;
	}
	
	public IHotelFavourableReturnService getReturnService() {
		return returnService;
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
}

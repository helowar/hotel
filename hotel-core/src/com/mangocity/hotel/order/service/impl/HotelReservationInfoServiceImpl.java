package com.mangocity.hotel.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.mangocity.hotel.base.constant.HotelCalcuAssuAmoType;
import com.mangocity.hotel.base.manage.ClauseManage;
import com.mangocity.hotel.base.manage.ContractManage;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlAssure;
import com.mangocity.hotel.base.persistence.HtlAssureItemEveryday;
import com.mangocity.hotel.base.persistence.HtlBookCaulClause;
import com.mangocity.hotel.base.persistence.HtlEveningsRent;
import com.mangocity.hotel.base.persistence.HtlFavouraParameter;
import com.mangocity.hotel.base.persistence.HtlFavourableclause;
import com.mangocity.hotel.base.persistence.HtlPreconcertItem;
import com.mangocity.hotel.base.persistence.HtlPrepayEveryday;
import com.mangocity.hotel.base.persistence.HtlPrepayItemEveryday;
import com.mangocity.hotel.base.persistence.HtlPresale;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.persistence.HtlReservCont;
import com.mangocity.hotel.base.persistence.HtlReservation;
import com.mangocity.hotel.base.persistence.HtlSalesPromo;
import com.mangocity.hotel.base.persistence.OrAssureItemEvery;
import com.mangocity.hotel.base.persistence.OrGuaranteeItem;
import com.mangocity.hotel.base.persistence.OrReservation;
import com.mangocity.hotel.base.service.assistant.AssureInforAssistant;
import com.mangocity.hotel.base.service.assistant.BookRoomCondition;
import com.mangocity.hotel.base.service.assistant.QueryCreditAssureForCCBean;
import com.mangocity.hotel.base.service.assistant.ReservationAssist;
import com.mangocity.hotel.order.constant.GuaranteeType;
import com.mangocity.hotel.order.constant.HotelBalanceMethod;
import com.mangocity.hotel.order.constant.HotelModifyCancel;
import com.mangocity.hotel.order.constant.HotelPayLimitType;
import com.mangocity.hotel.order.constant.HotelVouchCondiction;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrPreSale;
import com.mangocity.hotel.order.persistence.OrPriceDetail;
import com.mangocity.hotel.order.service.IHotelReservationInfoService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;

public class HotelReservationInfoServiceImpl implements IHotelReservationInfoService {
	
	
	 /**
     * 预订条款相关管理接口 hotel 2.9.2 add by chenjiajie 2009-08-16
     */
    private ClauseManage clauseManage;    
    
    /**
     * 酒店本部Manager
     */
    private HotelManage hotelManage;
    
    /**
     * 酒店合同manager,用于获取酒店促销和优惠信息
     */
    private ContractManage contractManage;    
    
    /**
     * 从网站查询预订条款
     */
    private final static int FROM_WEB = 1;
    
    /**
     * 从3G手机方式查询预订条款
     */
    private final static int FROM_3G = 2;

	/**
     * hotel2.6 网站：add by zhineng.zhuang 2009-02-11 酒店网站2.6获取预订条款
     */
    public ReservationAssist getReservationInfoForWeb(
        BookRoomCondition bookRoomCond, OrOrder order) {
    	return getReservationInfo(bookRoomCond, order, FROM_WEB);
    }
    

    /**
     * 
     * 网站和3G获取预订条款信息方法
     * 
     * @param bookRoomCond
     * @param order
     * @param from FROM_WEB:网站, FROM_3G:3g
     * @return
     */
    private ReservationAssist getReservationInfo(
        BookRoomCondition bookRoomCond, OrOrder order, int from) {
        ReservationAssist reservationAssist = new ReservationAssist();
        QueryCreditAssureForCCBean queryBean = new QueryCreditAssureForCCBean();
        queryBean.setBeginDate(bookRoomCond.getCheckinDate());
        queryBean.setEndDate(bookRoomCond.getCheckoutDate());
        queryBean.setHotelId(bookRoomCond.getHotelId().longValue());
        queryBean.setRoomType(bookRoomCond.getChildRoomTypeId().toString());
        List<HtlPreconcertItem> reservationList = hotelManage
				.queryReservationForCC(queryBean.getHotelId(), queryBean
						.getBeginDate(), queryBean.getEndDate(), queryBean
						.getRoomType());
        Date beginDate = bookRoomCond.getCheckinDate();

        OrReservation orReservation = null;
        orReservation = order.getReservation();
        if (null == orReservation) {
            orReservation = new OrReservation();
        }

        // 是否需要担保标志位
        Boolean isNeedVouch = false;
        // 最终的担保条件 1超时担保 2超房数担保 3无条件担保 (判断最终当天是否需要担保)
        int vouchCondiction = 0;
        // 结算方法
        String banlanceType = "";
        // 预付日期
        Date prePayDate = bookRoomCond.getCheckinDate();
        // 是否需要担保函
        Boolean isNeedLetter = false;
        // 最早的担保取消修改不扣款日期
        Date earliestNoCosDate = bookRoomCond.getCheckinDate();
        // 最早的担保取消修改不扣款时间点
        StringBuffer earliestNoCosTime = new StringBuffer();
        // 取消修改显示类型
        StringBuffer cancmodiType = new StringBuffer();
        // 对每天的价格详情加预订条款信息
        List<OrPriceDetail> orPriceDetailList = order.getPriceList();

        /** 按酒店id和时间段查询计算规则 hotel2.9.2 add by chenjiajie 2009-08-16 begin **/
        List<HtlBookCaulClause> htlBookCaulClauseList = clauseManage.searchBookCaulByDateRange(
            order.getHotelId(), order.getCheckinDate(), order.getCheckoutDate());
        /** 按酒店id和时间段查询计算规则 hotel2.9.2 add by chenjiajie 2009-08-16 end **/

        // 判断该酒店下是否设置了预定条款计算规则。
        String clauseRule = "";
        if (null == htlBookCaulClauseList || htlBookCaulClauseList.isEmpty()) {
            clauseRule = "3";
        } else {
            // 取出计算规则中最严格的计算规则，如果没有计算规则，默认累加判定 add by chenjiajie 2009-08-16
            clauseRule = clauseManage.drawoutHtlBookCaulClause(htlBookCaulClauseList);
        }
        // add by lixiaoyong v2.6 2009-05-11 最晚免费取消修改日期和时间bug begin
        String lastModifyDate = "";
        String lastModifyTime = "";
        // add by lixiaoyong v2.6 2009-05-11 最晚免费取消修改日期和时间bug end

        boolean cancelModifyExist = false;        
        for(HtlPreconcertItem htlPreconcertItem : reservationList) {	
            Date validDate = htlPreconcertItem.getValidDate();
            int dayIndex = DateUtil.getDay(beginDate, validDate);

            // 每天的预订条款详情提前日期
            StringBuffer beforDateDay = new StringBuffer();
            // 每天的预订条款详情提前天数
            StringBuffer beforeDateNumDay = new StringBuffer();
            // 每天的预订条款详情的连住天数
            StringBuffer continueInDay = new StringBuffer();
            // 每天的预订条款详情中必住日期
            StringBuffer mustContainDate = new StringBuffer();
            // 预订条款详情中的每天最终的担保条件（面付）
            StringBuffer assureCondiDay = new StringBuffer();
            // 预订条款详情中的每天的担保类型（面付）
            String assureDay = "";
            // 预订条款详情中的每天的结算方法（预付）
            String balanceDay = "";
            // 预订条款详情中的每天的提前日期(预付)
            StringBuffer aheadDateDay = new StringBuffer();
            
            OrPriceDetail orPriceDetail = null;
            
            if(FROM_WEB == from) { // 网站查询需获取每天的预订条款详情
                // 设置每天的预订条款详情
                orPriceDetail = orPriceDetailList.get(dayIndex);
                orPriceDetail.setHasReserv(true);
                // 每天的预订条款详情
                List<HtlReservation> htlReservationList = htlPreconcertItem.getHtlReservationList();            
                for(HtlReservation htlReservation : htlReservationList) {	
                    // 必须提前日期和必须提前日期时间点不为空，加入“必须在YYYY-MM-DD HH24:MI 之前预订” hotel2.6 modify by chenjiajie
                    // 2009-03-27
                    if (null != htlReservation.getMustAheadDate()
                        && StringUtil.isValidStr(htlReservation.getMustAheadTime())) {
                        beforDateDay.append("必须在");
                        beforDateDay.append(DateUtil.dateToString(htlReservation.getMustAheadDate()));
                        beforDateDay.append(" ");
                        beforDateDay.append(htlReservation.getMustAheadTime());
                        beforDateDay.append("之前预订");
                    }
                    // 提前天数和提前时间点不为空，加入“提前X天，并且在预订当天的HH24:MI之前预订” hotel2.6 modify by chenjiajie
                    // 2009-03-27
                    if (null != htlReservation.getAheadDay()
                        && 0 <= htlReservation.getAheadDay().longValue()
                        && StringUtil.isValidStr(htlReservation.getAheadTime())) {
                        beforeDateNumDay.append("提前");
                        beforeDateNumDay.append(htlReservation.getAheadDay());
                        beforeDateNumDay.append("天,并且在预订当天的");
                        beforeDateNumDay.append(htlReservation.getAheadTime());
                        beforeDateNumDay.append("之前预订");
                    }
                    // 连住晚数不为空，假如“入住期间必需连住X晚” hotel2.6 modify by chenjiajie 2009-03-27
                    if (null != htlReservation.getContinueNights()
                        && 0 <= htlReservation.getContinueNights().longValue()) {
                        continueInDay.append("入住期间必需连住");
                        continueInDay.append(htlReservation.getContinueNights());
                        continueInDay.append("晚");
                    }
                    orPriceDetail.setBeforeTime(beforDateDay.toString());
                    orPriceDetail.setBeforeDayNum(beforeDateNumDay.toString());
                    orPriceDetail.setContinueDay(continueInDay.toString());
                    List<HtlReservCont> htlReservContList = new ArrayList<HtlReservCont>();
                    htlReservContList = htlReservation.getHtlReservacontList();
                    // add by wuyun 2009-06-15 对连住日期进行排序
                    Collections.sort(htlReservContList, reservContComparator);
                    for(HtlReservCont htlReservCont : htlReservContList) {	
                        mustContainDate.append(DateUtil.dateToString(htlReservCont.getContinueDate()));
                        if (1 < htlReservContList.size()) {
                            mustContainDate.append(",");
                        }
                    }
                    // 每天的预订条款详情中必住日期
                    orPriceDetail.setMustDate(mustContainDate.toString());
                }	
            }

            // 当为面付时
            if (!order.isPrepayOrder() && !order.isPayToPrepay()) {                
                List<HtlAssure> htlAssureList = htlPreconcertItem.getHtlAssureList();
                for(HtlAssure htlAssure : htlAssureList) {	
                    if (null == htlAssure.getOverNightsNumber()
                        && null == htlAssure.getOverRoomNumber()
                        && (null == htlAssure.getIsNotConditional())
                        && !StringUtil.isValidStr(htlAssure.getLatestAssureTime())) {
                        break;
                    }
                    if (null != htlAssure) {
                        // 如果是第一天
                        if (0 == DateUtil.compare(beginDate, htlPreconcertItem.getValidDate())) {
                            // 担保类型中首日担保只判断第一天
                            if (StringUtil.isValidStr(htlAssure.getAssureType())
                                && (String.valueOf(GuaranteeType.FIRST_DAY)).equals(htlAssure
                                    .getAssureType())) {
                                // 担保类型首日担保
                                assureDay = "首日担保";
                            }

                            // 担保条件超时担保只判断第一天
                            if (StringUtil.isValidStr(htlAssure.getLatestAssureTime())) {
                                // 担保条件超时担保
                                assureCondiDay.append("超时担保(");
                                assureCondiDay.append(htlAssure.getLatestAssureTime());
                                assureCondiDay.append(")");
                                // 当没有设过担保条件时，设置总的担保条件
                                // modified by lixiaoyong 2009-05-13 网站超时超房不能同时显示bug begin
                                // if(vouchCondiction==0){
                                orReservation.setLateSuretyTime(htlAssure.getLatestAssureTime());
                                orReservation.setOverTimeAssure(true);
                                vouchCondiction = HotelVouchCondiction.OVERTIMEVOUCH;
                                // }
                                // modified by lixiaoyong 2009-05-13 网站超时超房不能同时显示bug end
                            }
                        }

                        // 担保修改取消条款也只考虑第一天，但是如果第一天没有，则需要取第二天的，依次类推
                        // 如果是check-in-day规则,则只判断首日 modify by chenkeming@2009-06-24
                        if (!cancelModifyExist && (!"1".equals(clauseRule) || 0 == dayIndex)) {
                            List<HtlAssureItemEveryday> htlAssureItemEverydayList = htlAssure
									.getHtlAssureItemEverydayList();
                            if (null != htlAssureItemEverydayList
                                && 0 < htlAssureItemEverydayList.size()) {
                                cancelModifyExist = true;
                                // add by lixiaoyong v2.6 2009-05-11 最晚免费取消修改日期和时间bug begin
                                String lastModifyDateTime = "";
                                lastModifyDateTime = this.getLastModifyDateTime(order.getCheckinDate(),
                                    htlAssureItemEverydayList);
                                lastModifyDate = lastModifyDateTime.substring(0, lastModifyDateTime
                                    .indexOf("+"));
                                lastModifyTime = lastModifyDateTime.substring(lastModifyDateTime
                                    .indexOf("+") + 1, lastModifyDateTime.length());
                                // add by lixiaoyong v2.6 2009-05-11 最晚免费取消修改日期和时间bug end
                                boolean bForNew = orReservation.getAssureList().isEmpty();
                                for (HtlAssureItemEveryday htlAssureItemEveryday : htlAssureItemEverydayList) {
									OrAssureItemEvery orAssureItemEvery = new OrAssureItemEvery();
									orAssureItemEvery
											.setNight(htlPreconcertItem
													.getValidDate());
									MyBeanUtil.copyProperties(
											orAssureItemEvery,
											htlAssureItemEveryday);
									orAssureItemEvery.setID(null);
									if (null != orAssureItemEvery) {
										earliestNoCosDate = cancAndModiShow(
												orAssureItemEvery,
												cancmodiType,
												earliestNoCosTime,
												earliestNoCosDate, beginDate);
										if (bForNew) {
											orAssureItemEvery
													.setReserv(orReservation);
											orReservation.getAssureList().add(
													orAssureItemEvery);
										}
									}
								}
                            }
                        }
                        // 判断最终的担保条件 超房数担
                        if (null != htlAssure.getOverRoomNumber()
                            && 0 != htlAssure.getOverRoomNumber()) {
                            // 担保条件超房数担保
                            assureCondiDay.append("超房数担保(");
                            assureCondiDay.append(htlAssure.getOverRoomNumber());
                            assureCondiDay.append(")");
                            // 当没有设过担保条件时，设置总的担保条件
                            // modified by lixiaoyong 2009-05-13 网站超时超房不能同时显示bug begin
                            if (clauseRule.equals("1")) {
                                if (0 == DateUtil.compare(beginDate, htlPreconcertItem
                                    .getValidDate())) {
                                    orReservation
                                        .setRooms(htlAssure.getOverRoomNumber().intValue());
                                    orReservation.setRoomsAssure(true);
                                    vouchCondiction = HotelVouchCondiction.OVERROOMNUMVOUCH;	                        
                                }
                            } else {
                                orReservation.setRooms(htlAssure.getOverRoomNumber().intValue());
                                orReservation.setRoomsAssure(true);
                                vouchCondiction = HotelVouchCondiction.OVERROOMNUMVOUCH;
                            }
                            // modified by lixiaoyong 2009-05-13 网站超时超房不能同时显示bug end
                        }
                        // 判断最终的担保条件 超间夜担
                        if (null != htlAssure.getOverNightsNumber()
                            && 0 != htlAssure.getOverNightsNumber()) {
                            // 担保条件超间夜担保
                            assureCondiDay.append("超间夜担保(");
                            assureCondiDay.append(htlAssure.getOverNightsNumber());
                            assureCondiDay.append(")");
                            orReservation.setNights(htlAssure.getOverNightsNumber().intValue());
                            orReservation.setNightsAssure(true);
                            vouchCondiction = HotelVouchCondiction.OVERNIGHTNUMVOUCH;
                            // modified by lixiaoyong 2009-05-13 网站超时超房不能同时显示bug end
                        }

                        // 判断无条件担保
                        if (("1").equals(htlAssure.getIsNotConditional())) {
                            // 担保条件无条件担保
                            assureCondiDay.append("无条件担保");
                            // 设置总的担保条件，无条件担保优先级最高
                            if (!isNeedVouch && 3 > vouchCondiction) {
                                // 设置整体需要担保与否，担保条件当无条件担保，勾才勾上
                                if (clauseRule.equals("1")) {
                                    if (0 == DateUtil.compare(beginDate, htlPreconcertItem
                                        .getValidDate())) {
                                        isNeedVouch = true;
                                        // 当无条件担保，把超时担保与超房担保去掉，变成要担保
                                        orReservation.setUnCondition(true);
                                        vouchCondiction = HotelVouchCondiction.UNCONDICTIONVOUCH;
                                    }
                                } else {
                                    isNeedVouch = true;
                                    // 当无条件担保，把超时担保与超房担保去掉，变成要担保
                                    orReservation.setUnCondition(true);
                                    vouchCondiction = HotelVouchCondiction.UNCONDICTIONVOUCH;
                                }

                            }
                        }
                        // 设置需要担保与否，不管超时还是超房还是无条件
                        if (0 < vouchCondiction) {
                            orReservation.setNeedCredit(true);
                        }

                        // 担保类型 全额担保
                        if (StringUtil.isValidStr(htlAssure.getAssureType())
                            && (String.valueOf(GuaranteeType.ALL_DAY)).equals(htlAssure
                                .getAssureType())) {
                            // 担保类型全额担保
                            assureDay = "全额担保";
                        }

                        // 当天需要担保时，添加到每天担保详情里
                        if (StringUtil.isValidStr(assureCondiDay.toString())) {
                            OrGuaranteeItem orGuaranteeItem = new OrGuaranteeItem();
                            orGuaranteeItem.setNight(htlPreconcertItem.getValidDate());
                            orGuaranteeItem.setAssureType(htlAssure.getAssureType());
                            orGuaranteeItem.setAssureLetter(("1").equals(htlAssure
                                .getAssureLetter()) ? "是" : "否");
                            orGuaranteeItem.setAssureCondiction(assureCondiDay.toString());
                            orGuaranteeItem.setReserv(orReservation);
                            orReservation.getGuarantees().add(orGuaranteeItem);
                        }

                        // 只要有一天需要担保函，就需要
                        if (StringUtil.isValidStr(htlAssure.getAssureLetter())
                            && ("1").equals(htlAssure.getAssureLetter()) && !isNeedLetter) {
                            isNeedLetter = true;
                            orReservation.setAssureLetter(isNeedLetter);
                        }
                        
                        if(FROM_WEB == from) { // 从网站查询
                            // 预订条款详情中的每天最终的担保条件
                            orPriceDetail.setAssureCond(assureCondiDay.toString());
                            // 预订条款详情中的每天的担保类型
                            orPriceDetail.setAssureType(assureDay);	
                        }
                    }
                }
            }
            // 预付时
            else {
                List<HtlPrepayEveryday> htlPrepayEverydayList = htlPreconcertItem
						.getHtlPrepayEverydayList();
                for(HtlPrepayEveryday htlPrepayEveryday : htlPrepayEverydayList) {	
                    if (null != htlPrepayEveryday) {

                        // 预付修改取消条款也只考虑第一天，但是如果第一天没有，则需要取第二天的，依次类推
                        // 如果是check-in-day规则,则指判断首日 modify by chenkeming@2009-06-24
                        if (!cancelModifyExist && (!"1".equals(clauseRule) || 0 == dayIndex)) {
                            List<HtlPrepayItemEveryday> htlPrepayItemEverydayList = htlPrepayEveryday
									.getHtlPrepayItemEverydayList();
                            if (null != htlPrepayItemEverydayList
                                && 0 < htlPrepayItemEverydayList.size()) {
                                cancelModifyExist = true;
                                // add by lixiaoyong v2.6 2009-05-11 最晚免费取消修改日期和时间bug begin
                                String lastModifyDateTime = "";
                                lastModifyDateTime = this.getLastModifyDateTimeForPre(order.getCheckinDate(),
                                    htlPrepayItemEverydayList);
                                lastModifyDate = lastModifyDateTime.substring(0, lastModifyDateTime
                                    .indexOf("+"));
                                lastModifyTime = lastModifyDateTime.substring(lastModifyDateTime
                                    .indexOf("+") + 1, lastModifyDateTime.length());
                                // add by lixiaoyong v2.6 2009-05-31 最晚免费取消修改日期和时间bug end
                                for (HtlPrepayItemEveryday htlPrepayItemEveryday : htlPrepayItemEverydayList) {
									OrAssureItemEvery orAssureItemEvery = new OrAssureItemEvery();
									MyBeanUtil.copyProperties(
											orAssureItemEvery,
											htlPrepayItemEveryday);
									orAssureItemEvery.setID(null);
									orAssureItemEvery
											.setNight(htlPreconcertItem
													.getValidDate());
									earliestNoCosDate = cancAndModiShow(
											orAssureItemEvery, cancmodiType,
											earliestNoCosTime,
											earliestNoCosDate, beginDate);
									if (null != orAssureItemEvery) {
										orAssureItemEvery
												.setReserv(orReservation);
										orReservation.getAssureList().add(
												orAssureItemEvery);
									}
								}
                            }
                        }

                        // 结算方法
                        if (StringUtil.isValidStr(htlPrepayEveryday.getBalanceType())
                            && !StringUtil.isValidStr(banlanceType)) {
                            banlanceType = htlPrepayEveryday.getBalanceType();
                            // 一个酒店统一都是同一个结算方法
                            orReservation.setBalanceMode(banlanceType);
                        }

                        // 当一单一结时 预付日期还要再提前2天
                        if ((HotelBalanceMethod.COA).equals(htlPrepayEveryday.getBalanceType())) {
                            balanceDay = "客人到之前";
                            // 预付付款时限判断，当为提前日期
                            if ((HotelPayLimitType.BEFOREDATE).equals(htlPrepayEveryday
                                .getTimeLimitType())) {
                                aheadDateDay.append("在");
                                aheadDateDay.append(DateUtil.dateToString(DateUtil.getDate(
                                    htlPrepayEveryday.getLimitDate(), -2)));
                                aheadDateDay.append("之前");
                                // 新取的提前日期-2比之前存的最提前日期还要早，更新这个总的最提前日期
                                if (0 < DateUtil.compare(DateUtil.getDate(htlPrepayEveryday
                                    .getLimitDate(), -2), prePayDate)) {
                                    prePayDate = DateUtil.getDate(htlPrepayEveryday.getLimitDate(),
                                        -2);
                                }
                                // 当为提前天数
                            } else if ((HotelPayLimitType.BEFOREDATENUM).equals(htlPrepayEveryday
                                .getTimeLimitType())) {
                                aheadDateDay.append("在");
                                aheadDateDay.append(DateUtil.dateToString(DateUtil.getDate(
                                    beginDate, Long.valueOf(
                                        -htlPrepayEveryday.getLimitAheadDays() - 2).intValue())));
                                aheadDateDay.append("之前");
                                // 新取的经过计算后的提前日期-2比之前存的最提前日期还要早，更新这个总的最提前日期
                                if (0 < DateUtil.compare(DateUtil.getDate(beginDate,
                                    (int) -htlPrepayEveryday.getLimitAheadDays() - 
                                    2), prePayDate)) {
                                    prePayDate = DateUtil.getDate(beginDate,
                                        (int) -htlPrepayEveryday.getLimitAheadDays() - 2);
                                }
                            }
                        }
                        // 当为月结时
                        else if ((HotelBalanceMethod.SEND_BILL).equals(htlPrepayEveryday
                            .getBalanceType())) {
                            balanceDay = "月结房费";
                            // 预付付款时限判断，当为提前日期
                            if ((HotelPayLimitType.BEFOREDATE).equals(htlPrepayEveryday
                                .getTimeLimitType())) {
                                // 预订条款详情中的每天的提前日期
                                aheadDateDay.append("在");
                                aheadDateDay.append(DateUtil.dateToString(htlPrepayEveryday
                                    .getLimitDate()));
                                aheadDateDay.append("之前");
                                // 新取的提前日期比之前存的最提前日期还要早，更新这个最提前日期
                                if (0 < DateUtil.compare(htlPrepayEveryday.getLimitDate(),
                                    prePayDate)) {
                                    prePayDate = htlPrepayEveryday.getLimitDate();
                                }
                                // 当为提前天数
                            } else if ((HotelPayLimitType.BEFOREDATENUM).equals(htlPrepayEveryday
                                .getTimeLimitType())) {
                                // 预订条款详情中的每天的提前日期
                                aheadDateDay.append("在");
                                aheadDateDay.append(DateUtil.dateToString(DateUtil.getDate(
                                    beginDate, Long.valueOf(-htlPrepayEveryday.getLimitAheadDays())
                                        .intValue())));
                                aheadDateDay.append("之前");
                                // 新取的经过计算后的提前日期比之前存的最提前日期还要早，更新这个最提前日期
                                if (0 < DateUtil.compare(DateUtil.getDate(beginDate,
                                    (int) -htlPrepayEveryday.getLimitAheadDays()), prePayDate)) {
                                    prePayDate = DateUtil.getDate(beginDate,
                                        (int) -htlPrepayEveryday.getLimitAheadDays());
                                }
                            }
                        }
                        
                        if("web".equals(from)) { // 从网站查询
                            // 预订条款详情中的每天的结算方法
                            orPriceDetail.setBalanceMode(balanceDay);
                            // 预订条款详情中的每天的提前日期
                            orPriceDetail.setPrepayTime(aheadDateDay.toString());	
                        }

                        // 首日时设置 add by chenkeming@2009-06-23
                        if (0 == dayIndex) {
                            orReservation.setPrepayLimitType(htlPrepayEveryday.getTimeLimitType());
                        }
                    }
                }
            }
            // 当为第一天时，取预订条款显示
            if (0 == dayIndex) {
                StringBuffer clauseStrBu = new StringBuffer();
                // hotel2.6 modify by chenjiajie 2009-03-27
                if (StringUtil.isValidStr(beforDateDay.toString())) {
                    clauseStrBu.append(beforDateDay);
                }
                // hotel2.6 modify by chenjiajie 2009-03-27
                if (StringUtil.isValidStr(beforeDateNumDay.toString())) {
                    clauseStrBu.append(",");
                    clauseStrBu.append(beforeDateNumDay);
                }
                // hotel2.6 modify by chenjiajie 2009-03-27
                if (StringUtil.isValidStr(continueInDay.toString())) {
                    clauseStrBu.append(",");
                    clauseStrBu.append(continueInDay);
                }
                // hotel2.6 modify by chenjiajie 2009-03-27
                if (StringUtil.isValidStr(mustContainDate.toString())) {
                    clauseStrBu.append(",");
                    clauseStrBu.append("必住日期为:");
                    clauseStrBu.append(mustContainDate);
                }
                // hotel2.6 当第一位是,则去除 modify by chenjiajie 2009-03-27
                if (null != clauseStrBu && 0 < clauseStrBu.length()) {
                    if (0 == clauseStrBu.indexOf(",")) {
                        String clauseStr = clauseStrBu.substring(1, clauseStrBu.length());
                        orReservation.setClauseStr(clauseStr);
                    } else {
                        orReservation.setClauseStr(clauseStrBu.toString());
                    }
                }
            }
        }
        // 当有设了预付付款时限的时候，设置订单最终总的预付时限日期
        if (0 < DateUtil.compare(prePayDate, beginDate)) {
            orReservation.setAdvancePayTime(prePayDate);
        }
        // 取消修改显示类型
        if (StringUtil.isValidStr(cancmodiType.toString())) {
            reservationAssist.setCancmodiType(cancmodiType.toString());
        }
        // 如果取消修改显示类型是带时间日期的，设日期时间
        if (HotelModifyCancel.MODIORCANWITHDATE.equals(cancmodiType.toString())
            && StringUtil.isValidStr(earliestNoCosTime.toString())) {
            // add by lixiaoyong v2.6 2009-05-11 最晚免费取消修改日期和时间bug begin
            if ("".endsWith(lastModifyDate) || "".equals(lastModifyTime)) {
                reservationAssist.setEarliestNoPayDate(earliestNoCosDate);
                reservationAssist.setEarliestNoPayTime(earliestNoCosTime.toString());
            } else {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(DateUtil.getDate(lastModifyDate));
                reservationAssist.setEarliestNoPayDate(calendar.getTime());
                reservationAssist.setEarliestNoPayTime(lastModifyTime);
            }
            // add by lixiaoyong v2.6 2009-05-11 最晚免费取消修改日期和时间bug
        }
        // 最终的担保条件，用于判断页面是否担保
        if (0 < vouchCondiction) {
            // add by lixiaoyong v2.6 2009-05-11 最晚免费取消修改日期和时间bug begin
            if ("".endsWith(lastModifyDate) || "".equals(lastModifyTime)) {
                reservationAssist.setEarliestNoPayDate(earliestNoCosDate);
                reservationAssist.setEarliestNoPayTime(earliestNoCosTime.toString());
            } else {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(DateUtil.getDate(lastModifyDate));
                reservationAssist.setEarliestNoPayDate(calendar.getTime());
                reservationAssist.setEarliestNoPayTime(lastModifyTime);
            }
            // add by lixiaoyong v2.6 2009-05-11 最晚免费取消修改日期和时间bug
            reservationAssist.setAssureConditions(String.valueOf(vouchCondiction));
        }
        // 最终是否需要担保，用于判断页面是否担保，页面最开始是只有无条件担保才要担保
        reservationAssist.setNeedCredit(isNeedVouch);
        // 设置最终的担保类型
        // reservationAssist.setSuretyType(assureType);
        order.setReservation(orReservation);
        reservationAssist.setAssureInforList(this.getAssureDetailList(reservationList, order,
            clauseRule));
        return reservationAssist;

    }
    
    
    /**
     * add by ting.li
     * 获取简单的担保条款
     */   
    public OrReservation getAssureReservation(BookRoomCondition bookRoomCond){
    	OrReservation orReservation =new OrReservation();
    	Date beginDate = bookRoomCond.getCheckinDate();
    	Date endDate=bookRoomCond.getCheckoutDate();
    	if(beginDate==null || endDate==null){
    		return orReservation;
    	}
    	List<HtlPreconcertItem> reservationList = hotelManage.queryReservationForCC(bookRoomCond.getHotelId(), beginDate, endDate, bookRoomCond.getChildRoomTypeId().toString());
		

		// 是否需要担保标志位
		Boolean isNeedVouch = false;
		// 最终的担保条件 1超时担保 2超房数担保 3无条件担保 (判断最终当天是否需要担保)
		int vouchCondiction = 0;

		
		//String clauseRule = "";
		// 是否需要担保函
		Boolean isNeedLetter = false;
		
		 /** 按酒店id和时间段查询计算规则 hotel2.9.2 add by chenjiajie 2009-08-16 begin **/
        List<HtlBookCaulClause> htlBookCaulClauseList = clauseManage.searchBookCaulByDateRange(
        		bookRoomCond.getHotelId(), bookRoomCond.getCheckinDate(),bookRoomCond.getCheckoutDate());
        /** 按酒店id和时间段查询计算规则 hotel2.9.2 add by chenjiajie 2009-08-16 end **/

        // 判断该酒店下是否设置了预定条款计算规则。
        String clauseRule = "";
        if (null == htlBookCaulClauseList || htlBookCaulClauseList.isEmpty()) {
            clauseRule = "3";
        } else {
            // 取出计算规则中最严格的计算规则，如果没有计算规则，默认累加判定 add by chenjiajie 2009-08-16
            clauseRule = clauseManage.drawoutHtlBookCaulClause(htlBookCaulClauseList);
        }
		
		
		for (HtlPreconcertItem htlPreconcertItem : reservationList) {

			List<HtlAssure> htlAssureList = htlPreconcertItem.getHtlAssureList();
		
			for (HtlAssure htlAssure : htlAssureList) {
				if (null == htlAssure.getOverNightsNumber() && null == htlAssure.getOverRoomNumber() && (null == htlAssure.getIsNotConditional())
						&& !StringUtil.isValidStr(htlAssure.getLatestAssureTime())) {
					break;
				}
				if (null != htlAssure) {
					// 如果是第一天
					if (0 == DateUtil.compare(beginDate, htlPreconcertItem.getValidDate())) {

						// 担保条件超时担保只判断第一天
						if (StringUtil.isValidStr(htlAssure.getLatestAssureTime())) {
							orReservation.setLateSuretyTime(htlAssure.getLatestAssureTime());
							orReservation.setOverTimeAssure(true);
							vouchCondiction = HotelVouchCondiction.OVERTIMEVOUCH;
						
						}
					}

					// 判断最终的担保条件 超房数担
					if (null != htlAssure.getOverRoomNumber() && 0 != htlAssure.getOverRoomNumber()) {

						// 当没有设过担保条件时，设置总的担保条件
						// modified by lixiaoyong 2009-05-13 网站超时超房不能同时显示bug
						// begin
						if (clauseRule.equals("1")) {
							if (0 == DateUtil.compare(beginDate, htlPreconcertItem.getValidDate())) {
								orReservation.setRooms(htlAssure.getOverRoomNumber().intValue());
								orReservation.setRoomsAssure(true);
								vouchCondiction = HotelVouchCondiction.OVERROOMNUMVOUCH;
							}
						} else {
							orReservation.setRooms(htlAssure.getOverRoomNumber().intValue());
							orReservation.setRoomsAssure(true);
							vouchCondiction = HotelVouchCondiction.OVERROOMNUMVOUCH;
						}
						// modified by lixiaoyong 2009-05-13 网站超时超房不能同时显示bug end
					}
					// 判断最终的担保条件 超间夜担
					if (null != htlAssure.getOverNightsNumber() && 0 != htlAssure.getOverNightsNumber()) {

						orReservation.setNights(htlAssure.getOverNightsNumber().intValue());
						orReservation.setNightsAssure(true);
						vouchCondiction = HotelVouchCondiction.OVERNIGHTNUMVOUCH;
						// modified by lixiaoyong 2009-05-13 网站超时超房不能同时显示bug end
					}

					// 判断无条件担保
					if (("1").equals(htlAssure.getIsNotConditional())) {
						// 担保条件无条件担保

						// 设置总的担保条件，无条件担保优先级最高
						if (!isNeedVouch && 3 > vouchCondiction) {
							// 设置整体需要担保与否，担保条件当无条件担保，勾才勾上
							if (clauseRule.equals("1")) {
								if (0 == DateUtil.compare(beginDate, htlPreconcertItem.getValidDate())) {
									isNeedVouch = true;
									// 当无条件担保，把超时担保与超房担保去掉，变成要担保
									orReservation.setUnCondition(true);
									vouchCondiction = HotelVouchCondiction.UNCONDICTIONVOUCH;
								}
							} else {
								isNeedVouch = true;
								// 当无条件担保，把超时担保与超房担保去掉，变成要担保
								orReservation.setUnCondition(true);
								vouchCondiction = HotelVouchCondiction.UNCONDICTIONVOUCH;
							}

						}
					}
					// 设置需要担保与否，不管超时还是超房还是无条件
					if (0 < vouchCondiction) {
						orReservation.setNeedCredit(true);
					}
					// 只要有一天需要担保函，就需要
					if (StringUtil.isValidStr(htlAssure.getAssureLetter()) && ("1").equals(htlAssure.getAssureLetter()) && !isNeedLetter) {
						isNeedLetter = true;
						orReservation.setAssureLetter(isNeedLetter);
					}
				}
			}
		}
		return orReservation;
		
	}
    
    
    
    private static Comparator<HtlReservCont> reservContComparator = new Comparator<HtlReservCont>() {
    	public int compare(HtlReservCont reservCont1, HtlReservCont reservCont2)
		{
			return reservCont1.getContinueDate().before(reservCont2.getContinueDate())?0 : 1;
		}
    };
    
    /**
	 * 
	 * 3g查询获取预订条款信息
	 * 
	 * @param bookRoomCond
	 * @param order
	 * @return
	 */
    public ReservationAssist getReservationInfoFor3G(
			BookRoomCondition bookRoomCond, OrOrder order) {
		return getReservationInfo(bookRoomCond, order, FROM_3G);
	}
  

    private List<AssureInforAssistant> getAssureDetailList(
			List<HtlPreconcertItem> reservationList, OrOrder order, String type) {
        double fristDayPrice = 0.0;
        List<AssureInforAssistant> assureInforList = new ArrayList<AssureInforAssistant>();
        for(HtlPreconcertItem htlPreconcertItem : reservationList) {	
            List htlAssureList = htlPreconcertItem.getHtlAssureList();
            for (Iterator htlAssureIterator = htlAssureList.iterator();
            htlAssureIterator.hasNext();) {
                HtlAssure htlAssure = (HtlAssure) htlAssureIterator.next();
                if (null == htlAssure.getOverNightsNumber()
                    && null == htlAssure.getOverRoomNumber()
                    && (null == htlAssure.getIsNotConditional())
                    && !StringUtil.isValidStr(htlAssure.getLatestAssureTime())) {
                    break;
                }
                List<OrPriceDetail> orPriceDetailList = order.getPriceList();
                for(OrPriceDetail orPriceDetail : orPriceDetailList) {	
                    if (0 == DateUtil.compare(orPriceDetail.getNight(), order.getCheckinDate())) {
                        fristDayPrice = orPriceDetail.getSalePrice();
                    }
                    if (0 == DateUtil.compare(htlPreconcertItem.getValidDate(), orPriceDetail
                        .getNight())) {
                        if (String.valueOf(HotelCalcuAssuAmoType.CHECKIN).equals(type)) {
                            if ((0 == DateUtil.compare(htlPreconcertItem.getValidDate(), order
                                .getCheckinDate()))) {
                                AssureInforAssistant assureInforAssistant = 
                                    new AssureInforAssistant();
                                assureInforAssistant
                                    .setAssureDate(htlPreconcertItem.getValidDate());
                                assureInforAssistant.setAssureRule(type);
                                assureInforAssistant.setAssureType(htlAssure.getAssureType());
                                if (StringUtil.isValidStr(htlAssure.getLatestAssureTime())) {
                                    assureInforAssistant.setOverTimeAssure(true);
                                    assureInforAssistant.setOverTimeStr(htlAssure
                                        .getLatestAssureTime());
                                    assureInforAssistant.setOverTimeAssureAmount(orPriceDetail
                                        .getSalePrice());
                                }
                                if (null != htlAssure.getOverRoomNumber()
                                    && 0 < htlAssure.getOverRoomNumber().intValue()) {
                                    assureInforAssistant.setOverRoomsAssure(true);
                                    assureInforAssistant.setOverRoomsNum(htlAssure
                                        .getOverRoomNumber().intValue());
                                    assureInforAssistant.setOverRoomsAssureAmount(orPriceDetail
                                        .getSalePrice());
                                }
                                if (null != htlAssure.getOverNightsNumber()
                                    && 0 < htlAssure.getOverNightsNumber().intValue()) {
                                    assureInforAssistant.setOverNightsAssure(true);
                                    assureInforAssistant.setOverNightsNum(htlAssure
                                        .getOverNightsNumber().intValue());
                                    assureInforAssistant.setOverNightsAssureAmount(orPriceDetail
                                        .getSalePrice());
                                }
                                if (!StringUtil.isValidStr(htlAssure.getLatestAssureTime())
                                    && null == htlAssure.getOverRoomNumber()
                                    && null == htlAssure.getOverNightsNumber()) {
                                    assureInforAssistant.setUnconditionAssure(true);
                                    assureInforAssistant.setUnconditionAssureAmount(orPriceDetail
                                        .getSalePrice());
                                }
                                assureInforAssistant.setFristDayAssureAmount(fristDayPrice);
                                assureInforList.add(assureInforAssistant);
                                break;
                            } else {
                                break;
                            }
                        }
                        AssureInforAssistant assureInforAssistant = new AssureInforAssistant();
                        assureInforAssistant.setAssureDate(htlPreconcertItem.getValidDate());
                        assureInforAssistant.setAssureRule(type);
                        assureInforAssistant.setAssureType(htlAssure.getAssureType());
                        if (StringUtil.isValidStr(htlAssure.getLatestAssureTime())) {
                            assureInforAssistant.setOverTimeAssure(true);
                            assureInforAssistant.setOverTimeStr(htlAssure.getLatestAssureTime());
                            assureInforAssistant.setOverTimeAssureAmount(orPriceDetail
                                .getSalePrice());
                        }
                        if (null != htlAssure.getOverRoomNumber()
                            && 0 < htlAssure.getOverRoomNumber().intValue()) {
                            assureInforAssistant.setOverRoomsAssure(true);
                            assureInforAssistant.setOverRoomsNum(htlAssure.getOverRoomNumber()
                                .intValue());
                            assureInforAssistant.setOverRoomsAssureAmount(orPriceDetail
                                .getSalePrice());
                        }
                        if (null != htlAssure.getOverNightsNumber()
                            && 0 < htlAssure.getOverNightsNumber().intValue()) {
                            assureInforAssistant.setOverNightsAssure(true);
                            assureInforAssistant.setOverNightsNum(htlAssure.getOverNightsNumber()
                                .intValue());
                            assureInforAssistant.setOverNightsAssureAmount(orPriceDetail
                                .getSalePrice());
                        }
                        if (!StringUtil.isValidStr(htlAssure.getLatestAssureTime())
                            && null == htlAssure.getOverRoomNumber()
                            && null == htlAssure.getOverNightsNumber()) {
                            assureInforAssistant.setUnconditionAssure(true);
                            assureInforAssistant.setUnconditionAssureAmount(orPriceDetail
                                .getSalePrice());
                        }
                        assureInforAssistant.setFristDayAssureAmount(fristDayPrice);
                        assureInforList.add(assureInforAssistant);

                    }
                }
                if (String.valueOf(HotelCalcuAssuAmoType.CHECKIN).equals(type)) {
                    break;
                }
            }
        }
        return assureInforList;
    }       
    
    /**
     * @author lixiaoyong
     * @param order
     * @param htlAssureItemEverydayList
     * @return 最晚日期+"+"+最晚时间字符串
     */
    private String getLastModifyDateTime(Date checkinDate,
			List<HtlAssureItemEveryday> htlAssureItemEverydayList) {
        String lastModifyDate = "";
        String lastModifyTime = "";
        String originLastModifyDate = "";
        String originLastModifyTime = "";
        boolean dateFlag = true;
        Date tempDate = null;
        for(HtlAssureItemEveryday htlAssureItemEveryday : htlAssureItemEverydayList) {	
            OrAssureItemEvery orAssureItemEvery = new OrAssureItemEvery();
            MyBeanUtil.copyProperties(orAssureItemEvery, htlAssureItemEveryday);
            orAssureItemEvery.setID(null);
            if ("234".contains(orAssureItemEvery.getType())) {
                Date date = null;
                if ("4".equals(orAssureItemEvery.getType())) {
                    date = DateUtil.getDate(checkinDate, -StringUtil.getIntValue(
                        orAssureItemEvery.getFirstDateOrDays(), 0));
                } else {
                    date = DateUtil.getDate(orAssureItemEvery.getFirstDateOrDays());
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int tempMonth = calendar.get(Calendar.MONTH) + 1;
                lastModifyDate = calendar.get(Calendar.YEAR)
                    + "-"
                    + ((9 < tempMonth) ? tempMonth : ("0" + tempMonth))
                    + "-"
                    + ((9 < calendar.get(Calendar.DAY_OF_MONTH)) ? calendar
                        .get(Calendar.DAY_OF_MONTH) : ("0" + calendar.get(Calendar.DAY_OF_MONTH)));
                lastModifyTime = orAssureItemEvery.getFirstTime();
                // 只执行首次
                if (dateFlag) {
                    originLastModifyDate = lastModifyDate;
                    originLastModifyTime = lastModifyTime;
                    dateFlag = false;
                    continue;
                }
                if (0 > lastModifyDate.compareTo(originLastModifyDate)) {
                    originLastModifyDate = lastModifyDate;
                    originLastModifyTime = lastModifyTime;
                } else if (0 == lastModifyDate.compareTo(originLastModifyDate)) {
                    if (0 > lastModifyTime.compareTo(originLastModifyTime)) {
                        originLastModifyDate = lastModifyDate;
                        originLastModifyTime = lastModifyTime;
                    }
                }
            } else if ("5".equals(orAssureItemEvery.getType()) && "".equals(originLastModifyDate)) {
                // 只执行一次
                if (dateFlag) {
                    lastModifyTime = orAssureItemEvery.getFirstTime();
                    originLastModifyTime = lastModifyTime;
                    dateFlag = false;
                    continue;
                }
                lastModifyTime = orAssureItemEvery.getFirstTime();
                originLastModifyTime = lastModifyTime;
                if (0 > lastModifyTime.compareTo(originLastModifyTime)) {
                    originLastModifyDate = lastModifyDate;
                    originLastModifyTime = lastModifyTime;
                }
            }
        }
        if (StringUtil.isValidStr(originLastModifyDate)) {
            tempDate = DateUtil.getDate(originLastModifyDate);
        } else {
            tempDate = DateUtil.getDate(checkinDate);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.getDate(tempDate, -1));
        originLastModifyDate = calendar.get(Calendar.YEAR) + "-"
            + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        if (!StringUtil.isValidStr(originLastModifyTime)) {
            originLastModifyTime = "00:00";
        }
        return originLastModifyDate + "+" + originLastModifyTime;
    }

    /**
     * @author lixiaoyong
     * @param order
     * @param htlPrepayEverydayList
     * @return 最晚日期+"+"+最晚时间字符串
     */
    private String getLastModifyDateTimeForPre(Date checkinDate,
			List<HtlPrepayItemEveryday> htlPrepayEverydayList) {
        String lastModifyDate = "";
        String lastModifyTime = "";
        String originLastModifyDate = "";
        String originLastModifyTime = "";
        boolean dateFlag = true;        
        for (HtlPrepayItemEveryday htlPrepayItemEveryday : htlPrepayEverydayList) {	
            OrAssureItemEvery orAssureItemEvery = new OrAssureItemEvery();
            MyBeanUtil.copyProperties(orAssureItemEvery, htlPrepayItemEveryday);
            orAssureItemEvery.setID(null);
            if ("234".contains(orAssureItemEvery.getType())) {
                Date date = null;
                if ("4".equals(orAssureItemEvery.getType())) {
                    date = DateUtil.getDate(checkinDate, -StringUtil.getIntValue(
                        orAssureItemEvery.getFirstDateOrDays(), 0));
                } else {
                    date = DateUtil.getDate(orAssureItemEvery.getFirstDateOrDays());
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int tempMonth = calendar.get(Calendar.MONTH) + 1;
                lastModifyDate = calendar.get(Calendar.YEAR)
                    + "-"
                    + ((9 < tempMonth) ? tempMonth : ("0" + tempMonth))
                    + "-"
                    + ((9 < calendar.get(Calendar.DAY_OF_MONTH)) ? calendar
                        .get(Calendar.DAY_OF_MONTH) : ("0" + calendar.get(Calendar.DAY_OF_MONTH)));
                lastModifyTime = orAssureItemEvery.getFirstTime();
                // 只执行首次
                if (dateFlag) {
                    originLastModifyDate = lastModifyDate;
                    originLastModifyTime = lastModifyTime;
                    dateFlag = false;
                    continue;
                }
                if (0 > lastModifyDate.compareTo(originLastModifyDate)) {
                    originLastModifyDate = lastModifyDate;
                    originLastModifyTime = lastModifyTime;
                } else if (0 == lastModifyDate.compareTo(originLastModifyDate)) {
                    if (0 > lastModifyTime.compareTo(originLastModifyTime)) {
                        originLastModifyDate = lastModifyDate;
                        originLastModifyTime = lastModifyTime;
                    }
                }
            } else if ("5".equals(orAssureItemEvery.getType()) && "".equals(originLastModifyDate)) {
                // 只执行一次
                if (dateFlag) {
                    lastModifyTime = orAssureItemEvery.getFirstTime();
                    originLastModifyTime = lastModifyTime;
                    dateFlag = false;
                    continue;
                }
                lastModifyTime = orAssureItemEvery.getFirstTime();
                originLastModifyTime = lastModifyTime;
                if (0 > lastModifyTime.compareTo(originLastModifyTime)) {
                    originLastModifyDate = lastModifyDate;
                    originLastModifyTime = lastModifyTime;
                }
            }
        }
        if ((null == originLastModifyTime) && (null != originLastModifyDate)) {
            originLastModifyTime = "00:00";
        }
        return originLastModifyDate + "+" + originLastModifyTime;
    }

    /**
     * hotel2.6 add by zhineng.zhuang 2009-02-28 网站预订取消条款加显示类型
     * 
     * @param orAssureItemEvery
     * @param cancmodiType
     * @param earliestNoCosTime
     * @param earliestNoCosDate
     * @param beginDate
     */
    private Date cancAndModiShow(OrAssureItemEvery orAssureItemEvery, StringBuffer cancmodiType,
        StringBuffer earliestNoCosTime, Date earliestNoCosDate, Date beginDate) {
        if (StringUtil.isValidStr(orAssureItemEvery.getType())
            && StringUtil.isValidStr(orAssureItemEvery.getScope())) {
            // 如果凡取消修改都要扣cancmodiType分三种取消、修改、取消/修改
            if (HotelModifyCancel.ALLNEEDCOST.equals(orAssureItemEvery.getType())) {
                cancmodiType.append(orAssureItemEvery.getScope());
            }
            // 如果是其它有带时间的情况，取日期时间显示
            else {
                cancmodiType.append(HotelModifyCancel.MODIORCANWITHDATE);
                Date dateTemp = beginDate;
                // 当第一个为日期时
                if (HotelModifyCancel.DATETODATE.equals(orAssureItemEvery.getType())
                    || HotelModifyCancel.DATETOCHECDATE.equals(orAssureItemEvery.getType())) {
                    dateTemp = DateUtil.stringToDate(orAssureItemEvery.getFirstDateOrDays());
                }
                // 当第一个值为天数时
                if (HotelModifyCancel.DATENUMTONUM.equals(orAssureItemEvery.getType())) {
                    dateTemp = DateUtil.getDate(beginDate, -Integer.parseInt(orAssureItemEvery
                        .getFirstDateOrDays()));
                }
                // 当提前日期比之前大，或者直接是第5种取消修改规则时
                if (0 < DateUtil.compare(dateTemp, earliestNoCosDate)
                    || HotelModifyCancel.CHECKINDATETIME.equals(orAssureItemEvery.getType())) {
                    earliestNoCosDate = dateTemp;
                    earliestNoCosTime.append(orAssureItemEvery.getFirstTime());
                }
            }
        }
        return earliestNoCosDate;
    }
    
    /**
     * 取得芒果网和酒店的促销信息
     * 
     * @author lixiaoyong
     * @param OrOrder
     *            order
     */
    public List<OrPreSale> queryRoomPresale(OrOrder order) {        
        List<OrPreSale> orderPromos = new ArrayList<OrPreSale>();
        List<HtlSalesPromo> salePromos = contractManage.lstPreOrderSalePromosRoom(new Object[] {
				order.getHotelId(), "%" + order.getChildRoomTypeId() + "%",
				order.getCheckinDate(), order.getCheckoutDate(),
				order.getCheckinDate(), order.getCheckoutDate(),
				order.getCheckinDate(), order.getCheckoutDate() }); 
        for(HtlSalesPromo salespromo : salePromos) {	
            OrPreSale orderPromo = new OrPreSale();
            orderPromo.setID(salespromo.getID());
            // 1为酒店促销 2为芒果网促销
            orderPromo.setType(1);
            orderPromo.setContent(salespromo.getSalePromoCont());
            orderPromo.setBeginEnd(DateUtil.dateToString(salespromo.getBeginDate()) + "至"
                + DateUtil.dateToString(salespromo.getEndDate()));
            orderPromo.setNameStr("酒店促销");
            orderPromo.setWebShow(salespromo.getWebShow());
            orderPromos.add(orderPromo);
        }
        List<HtlPresale> hotelPromos = contractManage.lstPreOrderPresale(new Object[] {
				order.getHotelId(), order.getCheckinDate(),
				order.getCheckoutDate(), order.getCheckinDate(),
				order.getCheckoutDate(), order.getCheckinDate(),
				order.getCheckoutDate() });
        for(HtlPresale presale : hotelPromos) {	
            OrPreSale orderPromo = new OrPreSale();
            orderPromo.setID(presale.getID());
            orderPromo.setType(2);
            orderPromo.setContent(presale.getPresaleContent());
            orderPromo.setBeginEnd(DateUtil.dateToString(presale.getBeginDate()) + "至"
                + DateUtil.dateToString(presale.getEndDate()));
            orderPromo.setUrl(presale.getUrl());
            orderPromo.setNameStr(presale.getPresaleName());
            orderPromo.setWebShow(presale.getWebShow());
            orderPromos.add(orderPromo);
        }
        return orderPromos;
    }
        
	/**
	* add by zhijie.gu 2009-09-15 hotel2.9.3 改变在优惠条款内的佣金和底价，
	*/
	public int changeBasePriceCommission(HtlPrice htlPrice,List orPriceDetailList,OrPriceDetail orPriceDetailItems,long hotelId,long childRoomTypeId,int y, int f){
		List<HtlFavourableclause> htlFavourableclauseList = contractManage
				.queryFavourableclauseByHotelAndPriceType(hotelId,
						childRoomTypeId);		
		
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
	
	
    public void setClauseManage(ClauseManage clauseManage) {
        this.clauseManage = clauseManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

	public void setContractManage(ContractManage contractManage) {
		this.contractManage = contractManage;
	}
}

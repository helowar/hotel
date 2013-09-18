package com.mangocity.hotel.order.persistence.assistant;


import hk.com.cts.ctcp.hotel.constant.TxnStatusType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.constant.HotelCalcuAssuAmoType;
import com.mangocity.hotel.base.persistence.OrAssureItemEvery;
import com.mangocity.hotel.base.persistence.OrGuaranteeItem;
import com.mangocity.hotel.base.persistence.OrReservation;
import com.mangocity.hotel.order.constant.ConfirmType;
import com.mangocity.hotel.order.constant.GuaranteeState;
import com.mangocity.hotel.order.constant.MemberConfirmSmsStutas;
import com.mangocity.hotel.order.constant.MoneyTargetType;
import com.mangocity.hotel.order.constant.MoneyType;
import com.mangocity.hotel.order.constant.OrderItemAuditState;
import com.mangocity.hotel.order.constant.OrderItemType;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.hotel.order.constant.PayDirectionType;
import com.mangocity.hotel.order.constant.PrepayType;
import com.mangocity.hotel.order.persistence.HAssureItemEvery;
import com.mangocity.hotel.order.persistence.HCreditCardTemp;
import com.mangocity.hotel.order.persistence.HFellowInfo;
import com.mangocity.hotel.order.persistence.HFulfillment;
import com.mangocity.hotel.order.persistence.HGuaranteeItem;
import com.mangocity.hotel.order.persistence.HOrder;
import com.mangocity.hotel.order.persistence.HOrderItem;
import com.mangocity.hotel.order.persistence.HPayment;
import com.mangocity.hotel.order.persistence.HPreSale;
import com.mangocity.hotel.order.persistence.HPriceDetail;
import com.mangocity.hotel.order.persistence.HRefund;
import com.mangocity.hotel.order.persistence.HRemark;
import com.mangocity.hotel.order.persistence.HReservation;
import com.mangocity.hotel.order.persistence.HTaxCharge;
import com.mangocity.hotel.order.persistence.OrChannelNo;
import com.mangocity.hotel.order.persistence.OrCreditCard;
import com.mangocity.hotel.order.persistence.OrCreditCardTemp;
import com.mangocity.hotel.order.persistence.OrFellowInfo;
import com.mangocity.hotel.order.persistence.OrFulfillment;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrMemberConfirm;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderFax;
import com.mangocity.hotel.order.persistence.OrOrderItem;
import com.mangocity.hotel.order.persistence.OrOrderMoney;
import com.mangocity.hotel.order.persistence.OrPayment;
import com.mangocity.hotel.order.persistence.OrPreSale;
import com.mangocity.hotel.order.persistence.OrPriceDetail;
import com.mangocity.hotel.order.persistence.OrRefund;
import com.mangocity.hotel.order.persistence.OrRemark;
import com.mangocity.hotel.order.persistence.OrTaxCharge;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.hotel.constant.QuotaType;

/**
 * OrOrder辅助类 
 * @author chenkeming
 *
 */
public class OrderUtil implements Serializable {
    
    /**
	 * 香港和澳门的三字编码
	 * modify by chenkeming@2008-02-10 城市编码写死,因为初始化有可能没取到该数据
	 */
    public static String HK_CODE = "HKG"; 
    public static String MA_CODE = "MAC";    
    
    /**
	 * 如果包含现金和POS刷卡方式，则需要配送	
	 * @param payments
	 */
    public static boolean isNeedFulfill(List payments) {        
        for(int i=0; i<payments.size(); i++) {
            OrPayment payment = (OrPayment)payments.get(i);
            int payType = payment.getPayType();
            if (PrepayType.Cash == payType || PrepayType.POS == payType) {
                return true;    
            }
        }
        return false;
    }
    
    /**
	 * 考虑预付付款退款类型 决定是否在页面显示信用卡信息
	 * @param paymentList
	 * @param refundList
	 * @return
	 */
    public static boolean isNeedCreditCard(List paymentList, List refundList) {        
        for(int i=0; i<paymentList.size(); i++) {
            OrPayment payment = (OrPayment)paymentList.get(i);
            if(payment.isNeedCreditCard()) {
                return true;
            }
        }
        for(int i=0; i<refundList.size(); i++) {
            OrRefund refund = (OrRefund)refundList.get(i);
            if(refund.isNeedCreditCard()) {
                return true;
            }
        }
        return false;
    }
    
    /**
	 * 检查order是否已经付款成功
	 * @param order
	 * @return
	 */
    public static boolean checkHasPrepayed(OrOrder order) {
        List paymentList = order.getPaymentList();
      //考虑paymentList为空的情况(不可能为null) add by diandian.hou 2010-11-21
        if(paymentList.isEmpty()){
        	return false;
        }
        // paymentList 有多条记录，只要有一条没成功则是false
        int paymentSize = paymentList.size();
        if(paymentSize == 1){
        	OrPayment payment = (OrPayment)paymentList.get(0);
        	if(payment.isPaySucceed()) {
                return true;
            }else{
            	return false;
            }
        }
        // 如果有 0 17(代金券)和 1 14两条数据也是成功的 add by diandian.hou
        if (paymentSize > 1) {
			for (int i = 0; i < paymentList.size(); i++) {
				OrPayment payment = (OrPayment) paymentList.get(i);
				// hotel2.9.3 代金券的支付状态不参与计算订单是否付款成功 modify by chenjiajie
				// 2009-09-15
				if (!payment.isPaySucceed() && PrepayType.Coupon != payment.getPayType() ) {
					return false;
				}
			}
        }
        return true;
    }
    
    /**
	 * 更新order的中台状态
	 * @param order
	 */
    public static boolean updateStayInMid(OrOrder order) {    
        boolean bStayInMid = order.isStayInMid();        
        
        if(!order.isCancel()) { // 如果是非取消单，直接处理
            // 订单留在中台条件：订单状态是提交中台后面的状态；并且已发酒店传真，酒店已书面确认，
            // 已确认客人，已收酒店回传这几个条件至少有一个不满足。
            boolean bJudge = order.getOrderState() >= OrderState.SUBMIT_TO_MID &&
                    !(order.isSendedHotelFax() && order.isHotelConfirmFax() && 
                            order.isCustomerConfirm() &&    order.isHotelConfirmFaxReturn());
            if(bStayInMid != bJudge) {               
                order.setIsStayInMid(!bStayInMid);
                if(order.isStayInMid()) {
                    order.setToMidTime(new Date());
                }
            } 
        } else { // 如果是取消单		
            if(!order.isCreditAssured() && !order.isPrepayOrder()) {
                if(bStayInMid) {
                    order.setIsStayInMid(false);
                }
            } else {// 如果是取消单，并且是担保单或预付单，则
                if(order.isHotelConfirmFaxReturn()) { // 如果已收回传,则出中台
                    if(bStayInMid) {
                        order.setIsStayInMid(false);
                    }
                } else { // 如果不勾选“已收回传” 					
                    // 1． 如果未发预订传真时，即订单要在前台。
                    // 2． 如果已发预订传真时，则订单要在中台。
                    List list = new ArrayList();
                    list = order.getFaxList();
                    boolean bSend = false;
                    for (Iterator i = list.iterator(); i.hasNext();) {
                        OrOrderFax orOrderFax = (OrOrderFax) i.next();
                        if (orOrderFax.isSendSucceed()
                                && 3 == orOrderFax.getType()) {
                            bSend = true;
                            break;
                        }
                    }
                    if(!bSend) {
                        if(bStayInMid) {
                            order.setIsStayInMid(false);
                        }
                    } else {
                        if(!bStayInMid) {
                            order.setIsStayInMid(true);
                            order.setToMidTime(new Date());
                        }
                    }
                }
            }
        }
        order.setModifiedTime(new Date());
        return order.isStayInMid();
    }
    
    /**
	 * 检查order是否已经退款成功
	 * @return
	 */
    public static boolean checkHasRefund(OrOrder order) {
        List refundList = order.getRefundList();
        for(int i=0; i<refundList.size(); i++) {
            OrRefund refund = (OrRefund)refundList.get(i);
            if(!refund.isRefundSucceed()) {
                return false;
            }
        }
        return true;
    }
    
    /**
	 * 是否包含给定的预付方式
	 * @param type - 预付方式 
	 * @param paymentList
	 * @see PrepayType
	 * @return
	 */
    public static boolean includePrepay(int type, List paymentList) {
        for(int i=0; i<paymentList.size(); i++) {
            OrPayment payment = (OrPayment)paymentList.get(i);
            if(payment.getPayType() == type) {
                return true;
            }
        }
        return false;
    }
    
    /**
	 * 是否包含给定的退款方式
	 * @param type - 退款方式 
	 * @param refundList
	 * @see PrepayType
	 * @return
	 */
    public static boolean includeRefund(int type, List refundList) {
        for(int i=0; i<refundList.size(); i++) {
            OrRefund refund = (OrRefund)refundList.get(i);
            if(refund.getRefundType() == type) {
                return true;
            }
        }
        return false;
    }
    
    /**
	 * 转化星级
	 * @param star
	 * @return
	 */
    public static String getMostStar(double star) {
        
        String mostStar = "";
        
        if (2 > star) {
            mostStar = "无星级";
        } else if (0 == Double.compare(2.0,star)) {
            mostStar = "二星";
        } else if (0 == Double.compare(3.0,star)) {
            mostStar = "三星";
        } else if (0 == Double.compare(3.5,star)) {
            mostStar = "四星";
        } else if (0 == Double.compare(4.0,star)) {
            mostStar = "四星";
        } else if (0 == Double.compare(4.5,star)) {
            mostStar = "五星";
        } else if (0 == Double.compare(5.0,star)) {
            mostStar = "五星";
        } else {
            mostStar = "无星级";
        }
        
        return mostStar;
    }
    
    /**
	 * 转化价格范围
	 * @param price
	 * @return
	 */
    public static String getPriceLevel(double price) {
        
        String mostPriceLevel = "";
        
        if (1 == Double.valueOf(price).intValue()) {
            mostPriceLevel = "0-250";
        } else if (2 == Double.valueOf(price).intValue()) {
            mostPriceLevel = "250-400";
        } else if (3 == Double.valueOf(price).intValue()) {
            mostPriceLevel = "400-600";
        } else if (4 == Double.valueOf(price).intValue()) {
            mostPriceLevel = "600-800";
        } else if (5 == Double.valueOf(price).intValue()) {
            mostPriceLevel = ">800";
        }
        
        return mostPriceLevel;
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
	 * 根据房态字符串获取可预订的床型，供下单页面使用
	 * @param hRoomStatus
	 * @return
	 */
    public static String getAbleBedTypes(String[] hRoomStatus) {
        boolean boolDa = false;
        boolean boolShuang = false;
        boolean boolDan = false;
        String tempStatus = null;
        for (int m = 0; m < hRoomStatus.length; m++) {
            String obj1 = hRoomStatus[m];
            if (null != obj1) {
                if (null == tempStatus) {
                    tempStatus = obj1;
                }
                String[] roomArray0 = obj1.trim().split(",");
                for (int n = 0; n < roomArray0.length; n++) {
                    String bedType = roomArray0[n].substring(0, roomArray0[n]
                            .indexOf(":"));
                    String roomStatu = roomArray0[n].substring(roomArray0[n]
                            .indexOf(":") + 1);
                    if (roomStatu.equals("满")) {
                        if (bedType.trim().equals("大")) {
                            boolDa = true;
                        } else if (bedType.equals("双")) {
                            boolShuang = true;
                        } else if (bedType.equals("单")) {
                            boolDan = true;
                        }
                    }
                }
            }
        }
        String[] roomArray1 = tempStatus.trim().split(",");
        StringBuffer buffer = new StringBuffer("");
        for (int k = 0; k < roomArray1.length; k++) {
            String bedType = roomArray1[k].substring(0, roomArray1[k]
                    .indexOf(":"));
            if (false == boolDa) {
                if (bedType.equals("大")) {
                    buffer.append("大,");
                    continue;
                }

            }
            if (false == boolShuang) {
                if (bedType.equals("双")) {
                    buffer.append("双,");
                    continue;
                }
            }
            if (false == boolDan) {
                if (bedType.equals("单")) {
                    buffer.append("单");
                    continue;
                }
            }
        }
        return buffer.toString();        
    }
    
    /**
	 * @author lixiaoyong
	 * @param sRoomStatus  从数据库中查出的标识
	 * @return  		   将标识转化为的字符串
	 */
    public static String getAbleBedTypes3G(String sRoomStatus,boolean toDb) {
        StringBuffer buffer = new StringBuffer("");
        if (StringUtil.isValidStr(sRoomStatus)&&null != sRoomStatus) {
        	if(toDb){
                if (sRoomStatus.equals("大床"))buffer.append("1");
                if (sRoomStatus.equals("双床"))buffer.append("2");
                if (sRoomStatus.equals("单床"))buffer.append("3");
        	}else{
                if (sRoomStatus.equals("1"))buffer.append("大床");
                if (sRoomStatus.equals("2"))buffer.append("双床");
                if (sRoomStatus.equals("3"))buffer.append("单床");
        	}
        }
        return buffer.toString();        
    }
    /**
	 * @author lixiaoyong
	 * @param sRoomStatus  从数据库中查出的标识
	 * @return  		   将标识转化为的字符串
	 */
    public static int getAbleBedType(String bedTypeName) {
    	int bedType = 0;
    	if(StringUtil.isValidStr(bedTypeName)){
    		if(bedTypeName.contains("大")){
    			bedType = 1;
    		} 
    		if(bedTypeName.contains("双")){
    			bedType = 2;
    		}
    		if(bedTypeName.contains("单")){
    			bedType = 3;
    		}
    	}
    	return bedType;
    }
  
    /**
	 * 系统根据即时确认条件判断
	 */
    public static boolean popDialogBox(OrOrder order, Map params) {    
        // 判断是否是芒果网订单，是则可以做即时确认		
//		if (order.isMango()) {
            // 是否手工订单 add by chenjiajie 2008-11-18
            if(order.isManualOrder()){
                return false;
            }
            
            // 直连酒店非传统渠道只要下单成功即可确认
            if(!order.isOriChannel()) {
                return true;        
            }
            
            // 可以即时确认条件
            String firstSubmit = (String)params.get("firstSubmit");
            // 判断是否是首次提交
            if ("1".equals(firstSubmit)) {
                for (OrOrderItem ooi : order.getOrderItems()) {
                    String quotaPattern = ooi.getQuotaPattern();
                    String roomState = ooi.getRoomState();
                    String roomStaHigh = null;
                    if(roomState!=null){
	                    roomStaHigh = strRoomStatue(roomState);
	                    if("0".equals(roomStaHigh)) { // freesale的房态均满足条件
	                        continue;
	                    }
                    }else{
                    	roomStaHigh = "0";
                    }
                    // 判断是否是进店C-I/在店S-I
                    if ("C-I".equals(quotaPattern)) {
                        // 首晚的房态为"F" 或 首晚的房态为良好、紧张、不可以超+房间数少于或等于配额数;
                        boolean firstNight = ooi.isFirstNight();
                        if (firstNight) {
                            // 配额类型QuotaType!=4
                            if (!QuotaType.CALLQUOTA.equals(ooi.getQuotaType())) {
                                // 房态!=4
                                if ("4".equals(roomStaHigh)) {
                                    return false;                                    
                                }
                            } else {
                                return false;
                            }
                        }
                        // 在店S-I
                        // 所有天数的房态为“F”或 .房态为良好、紧张、不可超+房间数少于或等于配额数;
                    } else if ("S-I".equals(quotaPattern)) {
                        // //配额类型QuotaType!=4
                        if (!QuotaType.CALLQUOTA.equals(ooi.getQuotaType())&&
                                1 == ooi.getQuantity()) {
                            if ("4".equals(roomStaHigh)) {
                                return false;                                
                            }
                        } else {
                            return false;
                        }

                    }
                }
            }

//		}
        return true;
    }
    
    /**
	 * 从下单页面（参数）获取价格详细信息
	 * @author chenkeming Feb 11, 2009 11:24:03 AM
	 * @param order
	 * @param params
	 * @param bForCCNew 是否CC新下单
	 */
    public static void fillPriceDetail(OrOrder order, Map params, boolean bForCCNew) {
        Date tempDate = order.getCheckinDate();
        int difdays = DateUtil.getDay(tempDate, order.getCheckoutDate());
        List<String> dateStrList = DateUtil.getDateStrList(tempDate, order.getCheckoutDate(), 
                false);
        if(!order.getPriceList().isEmpty()) {
            order.getPriceList().clear();
        }
        for (int i = 0; i < difdays; i++) {
            if (0 < i) {
                tempDate = DateUtil.getDate(tempDate, 1);
            }
            OrPriceDetail priceDetail = new OrPriceDetail();
            priceDetail.setDayIndex(i);
            priceDetail.setNight(tempDate);
            priceDetail.setBreakfastStr((String) params.get("hBreakfasts" + i));            
            priceDetail.setRoomState((String) params.get("hRoomStatuss" + i));
            String tmpStr = (String) params.get("hSalePrices" + i);
            priceDetail.setSalePrice(null != tmpStr ? Double.valueOf(tmpStr) : 0L);
            tmpStr = (String) params.get("hBasePrice" + i);
            priceDetail.setBasePrice(null != tmpStr ? Double.valueOf(tmpStr) : 0L);
            tmpStr = (String) params.get("hMarketPrice" + i);
            priceDetail.setMarketPrice(null != tmpStr ? Double.valueOf(tmpStr) : 0L);
            tmpStr = (String) params.get("hQuantity" + i);
            priceDetail.setQuantity(null != tmpStr ? Integer.parseInt(tmpStr) : 0);
            priceDetail.setDateStr(dateStrList.get(i));
            if(bForCCNew) {
                priceDetail.setHasReserv("true".equals(params.get("hasReserv" + i)) ? true : false);
                priceDetail.setBeforeTime((String)params.get("beforeTime" + i));
                priceDetail.setBeforeDayNum((String)params.get("beforeDayNum" + i));
                priceDetail.setContinueDay((String)params.get("continueDay" + i));                
                priceDetail.setMustDate((String)params.get("mustDate" + i));                
                priceDetail.setAssureCond((String)params.get("assureCond" + i));
                priceDetail.setAssureType((String)params.get("assureType" + i));
                priceDetail.setBalanceMode((String)params.get("balanceMode" + i));                
                priceDetail.setPrepayTime((String)params.get("prepayTime" + i));                
            }                
            priceDetail.setOrder(order);
            order.getPriceList().add(priceDetail);
        }
    }    
    
    /**
	 * CC新下单时获取担保明细
	 * @author chenkeming Mar 1, 2009 10:59:17 PM
	 * @param reserv
	 * @param params
	 */
    public static void fillGuaranteeItem(OrReservation reserv, Map params) {        
        int i = 0;
        do {
            OrGuaranteeItem item = new OrGuaranteeItem();
            String strNight = (String)params.get("night" + i);
            if(null == strNight) {
                return;
            }
            item.setNight(DateUtil.getDate(strNight));
            item.setNotConditional((String)params.get("notConditional" + i));
            item.setLatestAssureTime((String)params.get("latestAssureTime" + i));
            item.setOverRoomNumber(Integer.parseInt((String) params.get("overRoomNumber" + i)));
			item.setOverNightsNumber(new Integer((String) params.get("overNightsNumber" + i)));
            item.setAssureType((String)params.get("assureTypeG" + i));
            item.setAssureLetter((String)params.get("assureLetter" + i));
            item.setAssureCondiction((String)params.get("assureCondiction" + i));
            item.setReserv(reserv);
            reserv.getGuarantees().add(item);
            i ++;
        } while(true);
    }
    
    /**
	 * 检查订单是否已进行首日日审
	 * @author chenkeming Feb 11, 2009 2:06:08 PM
	 * @param order
	 * @return
	 */
    public static boolean checkAuditFirstNight(OrOrder order) {
        for (OrOrderItem orderItem : order.getOrderItems()) {
            if(0 < orderItem.getDayIndex()) {
                return false;
            }
            if(orderItem.getAuditState() > OrderItemAuditState.NOT_WORK) {
                return true;
            }
        }
        return false;
    }
    
    /**
	 * 修改订单时复制订单
	 * @author chenkeming Feb 11, 2009 9:40:41 AM
	 * @param orderH
	 * @param order
	 */
    public static void copyOrder(HOrder orderH, OrOrder order) {
        MyBeanUtil.copyProperties(orderH, order);
        orderH.setOrderId(order.getID());
        orderH.setHisHotelConfirm(false);
        orderH.setHisValid(true);        
        orderH.setHisHotelConfirm(order.isHotelConfirm());
        
        // 复制订单明细
        for(OrOrderItem item : order.getOrderItems()) {
            HOrderItem itemH = new HOrderItem();
            MyBeanUtil.copyProperties(itemH, item);
            itemH.setOrderH(orderH);
            orderH.getOrderItemsH().add(itemH);
        }
        
        // 操作日志不用copy
        /*for(OrHandleLog item : order.getLogList()) {
			HHandleLog itemH = new HHandleLog();
			MyBeanUtil.copyProperties(itemH, item);
			itemH.setOrderH(orderH);
			orderH.getLogListH().add(itemH);
		}*/    
        order.getLogList().size();
        
        for(OrFellowInfo item : order.getFellowList()) {
            HFellowInfo itemH = new HFellowInfo();
            MyBeanUtil.copyProperties(itemH, item);
            itemH.setOrderH(orderH);
            orderH.getFellowListH().add(itemH);
        }    
        
        // 酒店确认记录不用copy
        /*for(OrOrderFax item : order.getFaxList()) {
			HOrderFax itemH = new HOrderFax();
			MyBeanUtil.copyProperties(itemH, item);
			itemH.setOrderH(orderH);
			orderH.getFaxListH().add(itemH);
			for(OrFaxLog log : item.getLogList()) {
				HFaxLog logH = new HFaxLog();
				MyBeanUtil.copyProperties(logH, log);
				logH.setOrderFaxH(itemH);
				itemH.getLogListH().add(logH);
			}
		}*/
        for(OrOrderFax item :order.getFaxList()) {
            item.getLogList().size();
        }
        
        // 客人确认记录不用copy
        /*for(OrMemberConfirm item : order.getMemberConfirmList()) {
			HMemberConfirm itemH = new HMemberConfirm();
			MyBeanUtil.copyProperties(itemH, item);
			itemH.setOrderH(orderH);
			orderH.getMemberConfirmListH().add(itemH);
		}*/
        order.getMemberConfirmList().size();
        
        // 价格明细
        for(OrPriceDetail item : order.getPriceList()) {
            HPriceDetail itemH = new HPriceDetail();
            MyBeanUtil.copyProperties(itemH, item);
            itemH.setOrderH(orderH);
            orderH.getPriceListH().add(itemH);
        }
        
        // 备注信息
        OrRemark remark = order.getRemark();
        if(null != remark) {
            HRemark remarkH = new HRemark();
            MyBeanUtil.copyProperties(remarkH, remark);
            orderH.setRemarkH(remarkH);
        }
        
        // 预订规则
        OrReservation reserv = order.getReservation();
        if(null != reserv) {
            HReservation reservH = new HReservation();
            MyBeanUtil.copyProperties(reservH, reserv);
            orderH.setReservationH(reservH);
            // 有可能担保或者预付单时才考虑copy取消修改规则
            if(reserv.isCanAssure() || order.isPrepayOrder()) {
                for(OrAssureItemEvery item : reserv.getAssureList()) {
                    HAssureItemEvery itemH = new HAssureItemEvery();
                    MyBeanUtil.copyProperties(itemH, item);
                    itemH.setReservH(reservH);
                    reservH.getAssureListH().add(itemH);
                }
            }
            // 有可能担保时才考虑copy担保明细			
            if(reserv.isCanAssure()) {
                for(OrGuaranteeItem item : reserv.getGuarantees()) {
                    HGuaranteeItem itemH = new HGuaranteeItem();
                    MyBeanUtil.copyProperties(itemH, item);
                    itemH.setReservH(reservH);
                    reservH.getGuaranteesH().add(itemH);
                }                
            }
        }        
        
        // 促销信息
        if(reserv.isHasPresale()) {
            for(OrPreSale item : order.getPreSales()) {
                HPreSale itemH = new HPreSale();
                MyBeanUtil.copyProperties(itemH, item);
                itemH.setOrderH(orderH);
                orderH.getPreSalesH().add(itemH);
            }
        }
        
        // 房费另缴税
        if(reserv.isHasTaxCharge()) {
            for(OrTaxCharge item : order.getTaxCharges()) {
                HTaxCharge itemH = new HTaxCharge();
                MyBeanUtil.copyProperties(itemH, item);
                itemH.setOrderH(orderH);
                orderH.getTaxChargesH().add(itemH);
            }
        }
        
        if(!order.isMango() && order.isNeedCreditCard()) {
            for(OrCreditCardTemp item : order.getCardTempList()) {
                HCreditCardTemp itemH = new HCreditCardTemp();
                MyBeanUtil.copyProperties(itemH, item);
                itemH.setOrderH(orderH);
                orderH.getCardTempListH().add(itemH);
            }        
        }
        
        if(order.isPrepayOrder()) {
            // 复制预付支付信息
            for(OrPayment item : order.getPaymentList()) {
                HPayment itemH = new HPayment();
                MyBeanUtil.copyProperties(itemH, item);
                itemH.setOrderH(orderH);
                orderH.getPaymentListH().add(itemH);
            }
            // 复制退款信息
            if(order.isHasPrepayed()) {
                for(OrRefund item : order.getRefundList()) {
                    HRefund itemH = new HRefund();
                    MyBeanUtil.copyProperties(itemH, item);
                    itemH.setOrderH(orderH);
                    orderH.getRefundListH().add(itemH);
                }            
            }
        }
        
        if(order.isNeedFulfill()) {
            OrFulfillment fulfill = order.getFulfill();
            if(null != fulfill) {
                HFulfillment fulfillH = new HFulfillment();
                MyBeanUtil.copyProperties(fulfillH, fulfill);
                orderH.setFulfillH(fulfillH);
            }
        }    
        
        order.getMoneyList().size();
    }
    
    /**
	 * 拆分入住人到OrOrderItem v2.4.2 2008-12-30
	 * @param order
	 * @return 每天各个房间的入住人姓名数组
	 * @author chenjiajie
	 */
    public static String[] fillFellowNamesToOrderItem(OrOrder order) {
        int roomQuantity = order.getRoomQuantity();
        String[] fellowNamesArr = new String[roomQuantity]; // 用于封装入住人姓名到OrOrderItem里的数组
        List<OrFellowInfo> fellowList = order.getFellowList();
        int fellowSize = fellowList.size(); // 入住人数量
        /**
		 * 入住人数量=房间数量
		 * 如A,B,C三个入住人，预订了三间房在同一张订单：
		 * 第一间房：A
		 * 第二间房：B
		 * 第三间房：C
		 */
        if(fellowSize == roomQuantity){  
            for (int i = 0; i < roomQuantity; i++) {
                OrFellowInfo orFellowInfo = fellowList.get(i);
                fellowNamesArr[i] = orFellowInfo.getFellowName();
            }
        }
        /**
		 * 入住人数量>房间数量
		 * 如A,B,C,D,E五个入住人，预订了三间房在同一张订单：
		 * 第一间房：A D
		 * 第二间房：B E
		 * 第三间房：C
		 */
        else if(fellowSize > roomQuantity){ 
            for (int i = 0; i < fellowSize; i++) {
                int fellowIndex = i - (i / roomQuantity) - (roomQuantity - 1)
                * (i/roomQuantity); // 房间列表的下标
                OrFellowInfo orFellowInfo = fellowList.get(i);
                if(i > fellowIndex){ //第一次增加入住人的时候前面不需要有空格
                    fellowNamesArr[fellowIndex] += " " + orFellowInfo.getFellowName();
                }else{
                    fellowNamesArr[fellowIndex] = orFellowInfo.getFellowName();
                }
            }
        }
        /**
		 * 入住人数量<房间数量
		 * 如A,B,C三个入住人，预订了五间房在同一张订单
		 * 第一间房：A
		 * 第二间房：B
		 * 第三间房：C
		 * 第四间房：A代订 
		 * 第五间房：B代订 
		 */
        else {
            for (int i = 0; i < roomQuantity; i++) {
                int fellowIndex = i - (i / fellowSize) - (fellowSize - 1) 
                * (i/fellowSize); // 入住人列表的下标
                OrFellowInfo orFellowInfo = fellowList.get(fellowIndex);
                if(i > fellowIndex){ //第一组入住人填充完后，重复填充的时候在入住人姓名后加"代订"
                    if(0 < orFellowInfo.getFellowName().indexOf("代订")){
                        fellowNamesArr[i] = orFellowInfo.getFellowName();
                    }else{
                        fellowNamesArr[i] = orFellowInfo.getFellowName() + "代订";
                    }
                }else{
                    fellowNamesArr[i] = orFellowInfo.getFellowName();
                }
            }
        }
        return fellowNamesArr;
    }
    
    /**
	 * 修改OrderItem的入住人信息  v2.4.2 2008-12-31
	 * @param order
	 * @return
	 */
    public static boolean modifyOrderItem(OrOrder order){
        List<OrOrderItem> orderItemLst = order.getOrderItems();
        String[] fellowNamesArr = fillFellowNamesToOrderItem(order);    
        int nSize = orderItemLst.size();
        for (int i = 0; i < nSize; i++) {
            OrOrderItem orderItem = orderItemLst.get(i);
            orderItem.setFellowName(fellowNamesArr[orderItem.getRoomIndex()]);
        }
        return true;
    }
    
    /**
	 * 恢复历史订单时复制订单,其中修改日志,酒店确认记录,客人确认记录,备注信息不用从历史单恢复
	 * @author chenkeming Feb 11, 2009 9:40:41 AM
	 * @param orderH
	 * @param order
	 */
    public static void copyOrderResume(OrOrder order, HOrder orderH) {
        MyBeanUtil.copyProperties(order, orderH);
        
        // 恢复订单明细
        order.getOrderItems().clear();
        for(HOrderItem itemH : orderH.getOrderItemsH()) {
            OrOrderItem item = new OrOrderItem();
            MyBeanUtil.copyProperties(item, itemH);
            item.setOrder(order);
            order.getOrderItems().add(item);
        }        
        
        order.getFellowList().clear();
        for(HFellowInfo itemH : orderH.getFellowListH()) {
            OrFellowInfo item = new OrFellowInfo();
            MyBeanUtil.copyProperties(item, itemH);
            item.setOrder(order);
            order.getFellowList().add(item);
        }    
        
        /*order.getFaxList().clear();
		for(HOrderFax itemH : orderH.getFaxListH()) {
			OrOrderFax item = new OrOrderFax();
			MyBeanUtil.copyProperties(item, itemH);
			item.setOrder(order);
			order.getFaxList().add(item);
			for(HFaxLog faxItemH : itemH.getLogList()) {
				OrFaxLog faxItem = new OrFaxLog();
				MyBeanUtil.copyProperties(faxItem, faxItemH);
				item.getLogList().add(faxItem);
			}
		}*/
        for(OrOrderFax item :order.getFaxList()) {
            item.getLogList().size();
        }
        
        /*order.getMemberConfirmList().clear();
		for(HMemberConfirm itemH : orderH.getMemberConfirmListH()) {
			OrMemberConfirm item = new OrMemberConfirm();
			MyBeanUtil.copyProperties(item, itemH);
			item.setOrder(order);
			order.getMemberConfirmList().add(item);
		}*/
        order.getMemberConfirmList().size();
        
        order.getPriceList().clear();
        for(HPriceDetail itemH : orderH.getPriceListH()) {
            OrPriceDetail item = new OrPriceDetail();
            MyBeanUtil.copyProperties(item, itemH);
            item.setOrder(order);
            order.getPriceList().add(item);
        }
        
        /*HRemark remarkH = orderH.getRemarkH();
		if(remarkH != null) {
			MyBeanUtil.copyProperties(order.getRemark(), remarkH);
		}*/
        order.getRemark().getRoomRemark();
        
        // 预订规则
        HReservation reservH = orderH.getReservationH();
        if(null != reservH) {
            OrReservation reserv = order.getReservation();
            MyBeanUtil.copyProperties(reserv, reservH);
            // 有可能担保或者预付单时才考虑copy取消修改规则
            if(reservH.isCanAssure() || orderH.isPrepayOrder()) {
                reserv.getAssureList().clear();
                for(HAssureItemEvery itemH : reservH.getAssureListH()) {
                    OrAssureItemEvery item = new OrAssureItemEvery();
                    MyBeanUtil.copyProperties(item, itemH);
                    item.setReserv(reserv);
                    reserv.getAssureList().add(item);
                }
            }
            // 有可能担保时才考虑copy担保明细			
            if(reserv.isCanAssure()) {
                reserv.getGuarantees().clear();
                for(HGuaranteeItem itemH : reservH.getGuaranteesH()) {
                    OrGuaranteeItem item = new OrGuaranteeItem();
                    MyBeanUtil.copyProperties(item, itemH);
                    item.setReserv(reserv);
                    reserv.getGuarantees().add(item);
                }                
            }
        }
        
        // 促销信息
        order.getPreSales().clear();
        if(reservH.isHasPresale()) {
            for(HPreSale itemH : orderH.getPreSalesH()) {
                OrPreSale item = new OrPreSale();
                MyBeanUtil.copyProperties(item, itemH);
                item.setOrder(order);
                order.getPreSales().add(item);
            }
        }
        
        // 房费另缴税
        order.getTaxCharges().clear();
        if(reservH.isHasTaxCharge()) {
            for(HTaxCharge itemH : orderH.getTaxChargesH()) {
                OrTaxCharge item = new OrTaxCharge();
                MyBeanUtil.copyProperties(item, itemH);
                item.setOrder(order);
                order.getTaxCharges().add(item);
            }
        }
        
        order.getPaymentList().clear();
        order.getRefundList().clear();
        if(order.isPrepayOrder()) {
            // 复制预付支付信息
            for(HPayment itemH : orderH.getPaymentListH()) {
                OrPayment item = new OrPayment();
                MyBeanUtil.copyProperties(item, itemH);
                item.setOrder(order);
                order.getPaymentList().add(item);
            }
            // 复制退款信息
            if(order.isHasPrepayed()) {
                for(HRefund itemH : orderH.getRefundListH()) {
                    OrRefund item = new OrRefund();
                    MyBeanUtil.copyProperties(item, itemH);
                    item.setOrder(order);
                    order.getRefundList().add(item);
                }            
            }
        }
        
        if(!order.isMango() && order.isNeedCreditCard()) {
            for(HCreditCardTemp itemH : orderH.getCardTempListH()) {
                OrCreditCardTemp item = new OrCreditCardTemp();
                MyBeanUtil.copyProperties(item, itemH);
                item.setOrder(order);
                order.getCardTempList().add(item);
            }
        }    
        
        if(order.isNeedFulfill()) {
            HFulfillment fulfillH = orderH.getFulfillH();
            if(null != fulfillH) {
                OrFulfillment fulfill = new OrFulfillment();
                MyBeanUtil.copyProperties(fulfill, fulfillH);
                order.setFulfill(fulfill);
            }
        }
        
        order.getLogList().size();
        order.getMoneyList().size();
        
    }
    
    /**
	 * 四舍五入 精确两位小数
	 * @param vouchAmountIn
	 * @return
	 */
    public static double roundPrice(double vouchAmountIn){
        return BigDecimal.valueOf(vouchAmountIn).setScale(
                2,
                BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    /**
	 * 是否部分支付成功 
	 * @param refundList
	 * @see PrepayType
	 * @return
	 */
    public static boolean partlyPay(List paymentList) {
        for(int i=0; i<paymentList.size(); i++) {
            OrPayment payment = (OrPayment)paymentList.get(i);
            if(payment.isPaySucceed()) {
                return true;
            }
        }
        return false;
    }
    
    /**
	 * 更新订单的已担保金额
	 * @author chenkeming Feb 25, 2009 3:24:03 PM
	 * @param order
	 * @param suretyState
	 * @param roleUser
	 */
    /*public static OrHandleLog updateSurtyPrice(OrOrder order, int suretyState, UserWrapper roleUser) {		
		if(suretyState == GuaranteeState.SUCCESS) {
			OrReservation reservation = order.getReservation();
			double suretyPrice = order.getSuretyPrice();
			if(reservation != null) {
				
				StringBuffer strCmp = new StringBuffer();
				strCmp.append("订单的已担保金额由:"
						+ exeSuretyPrice
						+ "改为:"
						+ suretyPrice);
				strCmp.append("<br>");
				OrHandleLog handleLog = new OrHandleLog();
				handleLog.setModifierName(roleUser.getName());
				handleLog.setModifierRole(roleUser.getLoginName());
				handleLog.setBeforeState(order.getOrderState());
				handleLog.setAfterState(order.getOrderState());
				handleLog.setContent(strCmp.toString());
				handleLog.setModifiedTime(new Date());
				handleLog.setHisNo(order.getHisNo());
				handleLog.setOrder(order);
				return handleLog;
			}
		}
		return null;
	}*/
    
    /**
	 * 更新修改发生金额
	 * @author chenkeming Feb 25, 2009 3:24:03 PM
	 * @param modifyPrice
	 * @param order
	 * @param roleUser
	 */
    /*public static void updateModifyPrice(double modifyPrice, OrOrder order, UserWrapper roleUser) {
		OrReservation reserv = order.getReservation();
		if(modifyPrice > 0 && modifyPrice != reserv.getModifyPrice()) {
			reserv.setModifyPrice(modifyPrice);
			StringBuffer strCmp = new StringBuffer();
			strCmp.append("本次订单修改产生的金额为:" + modifyPrice);
			strCmp.append("<br>");
			OrHandleLog handleLog = new OrHandleLog();
			handleLog.setModifierName(roleUser.getName());
			handleLog.setModifierRole(roleUser.getLoginName());
			handleLog.setBeforeState(order.getOrderState());
			handleLog.setAfterState(order.getOrderState());
			handleLog.setContent(strCmp.toString());
			handleLog.setModifiedTime(new Date());
			handleLog.setHisNo(order.getHisNo());
			handleLog.setOrder(order);
			order.getLogList().add(handleLog);		
		}		
	}*/
    
    /**
	 * 修改单提交,生成基本信息修改的日志
	 * @author chenkeming Feb 26, 2009 7:44:26 PM
	 * @param order
	 * @param orderH
	 * @param roleUser
	 * @return
	 */
    public static OrHandleLog logModifyBaseInfo(OrOrder order, HOrder orderH, 
            UserWrapper roleUser) {
        StringBuffer strCmp = new StringBuffer();
        strCmp.append("订单修改基本信息(生成修改单序号:" + order.getHisNo() + "):<br>");
        String oldStr;
        String newStr;
        Date oldDate;
        Date newDate;
        oldDate = orderH.getCheckinDate();
        newDate = order.getCheckinDate();
        if(!newDate.equals(oldDate)) {
            strCmp.append("入住日期:<font color=blue>" + DateUtil.dateToString(oldDate) + 
                    "</font>-><font color=red>" + DateUtil.dateToString(newDate) + "</font><br>");
        }
        oldDate = orderH.getCheckoutDate();
        newDate = order.getCheckoutDate();
        if(!newDate.equals(oldDate)) {
            strCmp.append("离店日期:<font color=blue>" + DateUtil.dateToString(oldDate) + 
                    "</font>-><font color=red>" + DateUtil.dateToString(newDate) + "</font><br>");
        }
        if(orderH.getRoomQuantity() != order.getRoomQuantity()) {
            strCmp.append("房间数量:<font color=blue>" + orderH.getRoomQuantity() + 
                    "</font>-><font color=red>" + order.getRoomQuantity() + "</font><br>");
        }
        oldStr = orderH.getRoomTypeName();
        newStr = order.getRoomTypeName();        
        if(!oldStr.equals(newStr)) {
            strCmp.append("房型:<font color=blue>" + oldStr + 
                    "</font>-><font color=red>" + newStr + "</font><br>");
        }
        oldStr = orderH.getChildRoomTypeName();
        newStr = order.getChildRoomTypeName();
        if(!oldStr.equals(newStr)) {
            strCmp.append("子房型:<font color=blue>" + oldStr + 
                    "</font>-><font color=red>" + newStr + "</font><br>");
        }
        if(!order.getPayMethod().equals(orderH.getPayMethod())) {
            strCmp.append("支付方式:<font color=blue>");
            if(orderH.isPrepayOrder()) {
                strCmp.append("预付</font>-><font color=red>面付</font><br>");
            } else {
                strCmp.append("面付</font>-><font color=red>预付</font><br>");
            }
        }
        OrHandleLog handleLog = new OrHandleLog();
        handleLog.setModifierName(roleUser.getName());
        handleLog.setModifierRole(roleUser.getLoginName());
        handleLog.setBeforeState(orderH.getOrderState());
        handleLog.setAfterState(order.getOrderState());
        handleLog.setContent(strCmp.toString());
        handleLog.setModifiedTime(new Date());
        handleLog.setHisNo(orderH.getHisNo());
        handleLog.setOrder(order);
        order.getLogList().add(handleLog);    
        return handleLog;
    }    
    
    /**
	 * 增加修改金额记录
	 * @author chenkeming Mar 6, 2009 10:55:43 AM
	 * @param order
	 * @param modifyPrice
	 * @param roleUser
	 */
    public static void addModifyMoney(OrOrder order, double modifyPrice, UserWrapper roleUser) {
        OrOrderMoney orderMoney = new OrOrderMoney();
        orderMoney.setCreateTime(new Date());
        orderMoney.setHisNo(order.getHisNo());
        orderMoney.setMoneyType(MoneyType.MODIFY);
        orderMoney.setCreator(roleUser.getLoginName());
        orderMoney.setDirection(PayDirectionType.IN);
        orderMoney.setMoney(modifyPrice);        
        orderMoney.setSuccess(false);
        orderMoney.setTarget(MoneyTargetType.CUSTOMER);
        orderMoney.setValid(true);
        orderMoney.setReason("要收客人的修改费用");    
        
        orderMoney.setOrder(order);
        order.getMoneyList().add(orderMoney);
        
    }
    
    /**
	 * 增加取消金额记录
	 * @author chenkeming Mar 6, 2009 10:55:43 AM
	 * @param order
	 * @param cancelPrice
	 * @param roleUser
	 */
    public static void addCancelMoney(OrOrder order, double cancelPrice, UserWrapper roleUser) {
        OrOrderMoney orderMoney = new OrOrderMoney();
        orderMoney.setCreateTime(new Date());
        orderMoney.setHisNo(order.getHisNo());
        orderMoney.setMoneyType(MoneyType.CANCEL);
        orderMoney.setCreator(roleUser.getLoginName());
        orderMoney.setDirection(PayDirectionType.IN);
        orderMoney.setMoney(cancelPrice);        
        orderMoney.setSuccess(false);
        orderMoney.setTarget(MoneyTargetType.CUSTOMER);
        orderMoney.setValid(true);
        orderMoney.setReason("要收客人的取消费用");    
        
        orderMoney.setOrder(order);
        order.getMoneyList().add(orderMoney);
        
    }
    
    /*function setCardIds(ids,types,nos) {
		document.getElementById("creditCardIds").value=ids;
		document.getElementById("creditCardTypes").value=types;
		document.getElementById("creditCardNos").value=nos;
	}*/    
    
    /**
	 * 预授权创建成功则插入卡信息
	 * @author chenkeming Mar 10, 2009 7:03:35 PM
	 * @param order
	 * @param params
	 */
    public static void addPreAuthCard(OrOrder order, Map params) {
			String[] sTypes = null;
			String[] sNos = null ;
			if(params.get("creditCardTypes")!=null && !params.get("creditCardTypes").equals("") && params.get("creditCardNos")!=null && !params.get("creditCardNos").equals("")){
        String types = params.get("creditCardTypes").toString();
        String nos = params.get("creditCardNos").toString();
				sTypes = types.split("&");
				sNos = nos.split("&");
			}
			String ids = params.get("creditCardIds").toString();
        String[] sIds = ids.split("&");
        for(int i=0; i<sIds.length; i++) {
            OrCreditCard card = new OrCreditCard();
            card.setCardId(Long.valueOf(sIds[i]));
				if(sTypes!=null){
            card.setCardType(sTypes[i]);
				}
				if(sNos!=null){
            card.setCardNo(sNos[i]);
				}
            card.setOrder(order);
            order.getCreditCardList().add(card);
        }
    }
    
    /**
	 * 预付单增加预付给酒店的金额记录
	 * @author chenkeming Mar 6, 2009 10:55:43 AM
	 * @param order
	 * @param roleUser
	 */
    public static void addPrepayToHotel(OrOrder order, UserWrapper roleUser) {
        OrReservation reserv = order.getReservation();
        OrOrderMoney orderMoney = new OrOrderMoney();
        orderMoney.setCreateTime(new Date());
        orderMoney.setHisNo(order.getHisNo());
        orderMoney.setMoneyType(MoneyType.PREPAY);
        orderMoney.setCreator(roleUser.getLoginName());
        orderMoney.setDirection(PayDirectionType.OUT);
        orderMoney.setMoney(reserv.getPayToHotelAdv());        
        orderMoney.setSuccess(false);
        orderMoney.setTarget(MoneyTargetType.HOTEL);
        orderMoney.setValid(true);
        orderMoney.setReason("要预付给酒店的订金");    
        
        orderMoney.setOrder(order);
        order.getMoneyList().add(orderMoney);
        
    }
    
    
    /**
	 * 拆单
	 * @author chenkeming Mar 17, 2009 5:24:01 PM
	 * @param order
	 * @param maxNo
	 */
    public static void splitOrderForChannel(OrOrder order, int[] maxNo) {
        int minMax = 9999;
        for(int i=0; i<maxNo.length; i++) {
            if(maxNo[i] < minMax) {
                if(1 == maxNo[i]) {
                    minMax = 1;
                    break;
                }
                minMax = maxNo[i];
            }
        }
        
        int quantity = order.getRoomQuantity();
        int nDeduct;
        do {
            nDeduct = quantity > minMax ? minMax : quantity;
            OrChannelNo channelNo = new OrChannelNo();
            channelNo.setQuantity(nDeduct);
            channelNo.setOrder(order);
            order.getChannelList().add(channelNo);
            quantity -= nDeduct;
        } while(0 < quantity);
    }
    
    /**
	 * 拆单
	 * @author chenkeming Mar 17, 2009 5:24:01 PM
	 * @param order
	 * @param maxNo
	 */
    public static void splitOrderForChannel(OrOrder order, int minMax) {
        
        int quantity = order.getRoomQuantity();
        int nDeduct;
        do {
            nDeduct = quantity > minMax ? minMax : quantity;
            OrChannelNo channelNo = new OrChannelNo();
            channelNo.setQuantity(nDeduct);
            channelNo.setStatus(TxnStatusType.Default);
            channelNo.setOrder(order);
            order.getChannelList().add(channelNo);
            quantity -= nDeduct;
        } while(0 < quantity);
    }
    
    /**
	 * 填充中旅订单的入住人信息
	 * @author chenkeming Mar 18, 2009 2:11:27 PM
	 * @param order
	 */
    public static void fillCtsFellows(OrOrder order) {
        List<OrChannelNo> liC = order.getChannelList();
        List<OrFellowInfo> liF = order.getFellowList();
        int nSizeF = liF.size();
        if(0 >= nSizeF) {
            return;
        }
        int nSizeC = liC.size();
        if(0 >= nSizeC) {
            return;
        }
        int totalRoom = order.getRoomQuantity();
        int indexC = 0;
        int index = 0;
        do {
            OrChannelNo orderChannel = liC.get(indexC);
            int quantity = orderChannel.getQuantity();
            StringBuffer names = new StringBuffer();
            for(int i=0; i<quantity; i++) {
                int nIndex = index + i;
                OrFellowInfo fellow = liF.get(nIndex);
                if(0 < i) {
                    names.append("#");
                }
                if(nIndex + totalRoom >= nSizeF) {                
                    names.append(fellow.getFellowGender() + "*" 
                            + fellow.getFellowName());
                } else {
                    OrFellowInfo fellow1 = liF.get(nIndex + totalRoom);
                    names.append(fellow.getFellowGender() + "*" 
                            + fellow.getFellowName() + "," + fellow1.getFellowGender() 
                            + "*" + fellow1.getFellowName());
                }
            }
            orderChannel.setFellows(names.toString());
            
            index += quantity;
            indexC ++;
        } while(indexC < nSizeC);
    }
    
    /**
     * 中旅订单支付成功,自动发短信后生成客人确认记录
     * @author chenkeming Mar 27, 2009 12:56:19 PM
     * @param order
     * @param phoneNo
     * @param smsType
     * @param smsText
     * @param res
     */
    public static void genMemberConfirm(OrOrder order, String phoneNo, int smsType, 
            String smsText, Long res){
        OrMemberConfirm memberConfirm = new OrMemberConfirm();
        memberConfirm.setChannel(ConfirmType.SMS);
        memberConfirm.setModelType(order.getType());// 114还是芒果
        memberConfirm.setType(smsType);
        memberConfirm.setSendTarget(phoneNo);

        memberConfirm.setSendMan("系统");
        memberConfirm.setSendTime(new Date());
        memberConfirm.setSendSucceed(true);
        
        memberConfirm.setSendManId("SYSTEM"); //发送人的工号
        memberConfirm.setContent(smsText); //发送给客人的内容
        memberConfirm.setUnicallRetId(res); //UnitCall返回的流水号
        memberConfirm.setSendStatus(MemberConfirmSmsStutas.SENDING); //发送状态，初始化为1
        
        memberConfirm.setOrder(order);
        order.getMemberConfirmList().add(memberConfirm);
    }
    
    /**
     * 判断中旅芒果单的中旅单是否提交成功
     * @author chenkeming Apr 17, 2009 12:29:01 PM
     * @param order
     * @return
     */
    public static boolean isCtsOK(OrOrder order) {
        for(OrChannelNo orderChannel : order.getChannelList()) {
            if(orderChannel.getStatus() != TxnStatusType.Commited) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 网站生成订单明细
     * ADD BY WUYUN 2009-04-21
     */
    public static void generateOrderItems(OrOrder order, String hkPrices, String hkBasePrices){
        
        //天数
        int nDays = DateUtil.getDay(order.getCheckinDate(), order.getCheckoutDate());
        //房间数
        int nRooms = order.getRoomQuantity();
        //每天的销售价
        String[] prices = hkPrices.substring(0,hkPrices.lastIndexOf("#")).split("#");
        //每天的底价
        String[] basePrices = hkBasePrices.substring(0,hkBasePrices.lastIndexOf("#")).split("#");
        //循环
        for (int i = 0; i < nDays; i++) {
            for(int j = 0; j < nRooms; j++){
                OrOrderItem orderItem = new OrOrderItem();
                orderItem.setRoomIndex(j);
                orderItem.setDayIndex(i);
                //是否首晚
                orderItem.setFirstNight(0 == i ? true : false);
                orderItem.setLastNight(i == nDays - 1 ? true : false);
                //日期
                orderItem.setNight(DateUtil.getDate(order.getCheckinDate(), i));
                //配额数量，香港中科始终为1
                orderItem.setQuantity(1);
                //配额类型:普通配额
                orderItem.setQuotaType("1");
                //销售价
                orderItem.setSalePrice(Double.parseDouble(prices[i]));
                //底价
                orderItem.setBasePrice(Double.parseDouble(basePrices[i]));
                //市场价
                orderItem.setMarketPrice(0);
                //房态:均为良好
                orderItem.setRoomState("1:1/2:1/3:1");
                //酒店
                orderItem.setHotelId(order.getHotelId());
                //配额模式
                orderItem.setQuotaPattern("C-I");
                //是否已确认
                orderItem.setConfirm(true);
                orderItem.setOrderItemsType(OrderItemType.NORMAL);
                orderItem.setOrder(order);
                order.getOrderItems().add(orderItem);
            }            
        }
    }
    
    /**
     * v2.8 判断是否有可能担保(需要考虑判定规则)
     * @author chenkeming Jun 15, 2009 5:45:36 PM
     * @param order
     * @return
     */
    public static boolean isCanAssure(OrOrder order) {
        OrReservation reserv = order.getReservation();
        if(null == reserv) {
            return false;
        }
        if((""+HotelCalcuAssuAmoType.CHECKIN).equals(reserv.getClauseRule())) {
            List<OrGuaranteeItem> li = reserv.getGuarantees(); 
            if(li.isEmpty()) {
                return false;
            }
            if(0 == order.getCheckinDate().compareTo(li.get(0).getNight())) {
                return true;
            }
        } else {
            return reserv.isUnCondition() || reserv.isOverTimeAssure() 
                    || reserv.isRoomsAssure()||reserv.isNightsAssure();
        }
        return false;
    }
    
    /**
     * 是否显示最晚保留时间 面付:担保成功 不显示 return false;预付:支付成功 不显示 return false;其他return true
     * @param order
     * @return
     */
    public static boolean isShowArrivalTime(OrOrder order){
        if (order.isHasPrepayed() || GuaranteeState.SUCCESS == order.getSuretyState()){
            return false;
        }else{
            return true;
        }
    }
    
    /**
     * 网站根据房态字符串获取房间的最高房态
     * that method copy from WebStrUtil
     * @param roomState
     * @return
     */
    private static String strRoomStatue(String roomState) {
        String testStr = new String(roomState);
        String[] str = testStr.split("/");
        int t = 10;
        for (int i = 0; i < str.length; i++) {
            String[] testStr2 = str[i].split(":");
            for (int j = 1; j < testStr2.length; j += 2) {
                try {
                    int status = Integer.parseInt(testStr2[j]);
                    if (t > status) {
                        t = status;
                    }
                } catch (Exception ex) {
                    return "0";
                }
            }
        }
        return String.valueOf(t);
    }
}

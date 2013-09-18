package com.mangocity.hotel.order.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.hotel.order.constant.PrepayType;
import com.mangocity.hotel.order.dao.impl.OrOrderDao;
import com.mangocity.hotel.order.persistence.OrCouponRecords;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrPayment;
import com.mangocity.hotel.order.persistence.assistant.OrderUtil;
import com.mangocity.hotel.order.service.IVoucherInterfaceService;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.log.MyLog;
import com.mangocity.vch.app.service.VchService;
import com.mangocity.vch.app.service.exception.VCHException;
import com.mangocity.vch.domain.entity.Voucher;

/**
 * 代金券管理接口实现 hotel2.9.3
 * @author chenjiajie
 *
 */
public class VoucherInterfaceService implements IVoucherInterfaceService {
	
	private static final MyLog log = MyLog.getLogger(VoucherInterfaceService.class);
    /**
     * 代金券接口
     * hotel2.9.3 add by chenjiajie 2009-09-02
     */
    private VchService vchService;
    
    /**
     * 订单管理接口
     * hotel2.9.3 add by chenjiajie 2009-09-02
     */
    private OrOrderDao orOrderDao;
    
    /**
     * 对外酒店本部服务提供接口
     */
    private IHotelService hotelService;

    /**
     * 调用代金券接口预订确认接口 hotel2.9.3 add by chenjiajie 2009-09-02
     * @param orderData
     * @param voucherCodeList
     * @param order
     * @param roleUser
     * @return
     * @throws VCHException
     */
	public int callVchServiceOrder(Map<String, String> orderData,
			List<String> voucherCodeList, OrOrder order
    		,UserWrapper roleUser) throws VCHException {
		//调用代金券接口扣代金券
		int vchResult = vchService.order(orderData, voucherCodeList);
		//调用接口成功
		if(0 < vchResult){
			//更新代金券明细的支付状态
			List<OrCouponRecords> couponRecords = order.getCouponRecords();
			for (OrCouponRecords orCouponRecords : couponRecords) {
				orCouponRecords.setPaysucceed(true);
				orCouponRecords.setOrder(order);
			}
			order.setCouponRecords(couponRecords);
			//更新代金券的支付方式和订单代金券使用日志
			List<OrPayment> paymentList = order.getPaymentList();
			for (OrPayment orPayment : paymentList) {
				if(orPayment.getPayType() == PrepayType.Coupon){
					orPayment.setPaySucceed(true);
					orPayment.setConfirmTime(new Date());
					//更新日志
                    OrHandleLog handleLog = new OrHandleLog();
                    handleLog.setOrder(order);
                    handleLog.setModifiedTime(new Date());
                    if(null != roleUser){
    					orPayment.setConfirmer(roleUser.getName());
                        handleLog.setModifierName(roleUser.getName());
                        handleLog.setModifierRole(roleUser.getLoginName());
                    }else{
    					orPayment.setConfirmer("HWEB");
                        handleLog.setModifierName("HWEB");
                        handleLog.setModifierRole("HWEB");
                    }
                    handleLog.setContent("使用代金券成功，代金券金额共"+orPayment.getMoney()+"元");
                    order.setPaymentList(paymentList);
                    order.getLogList().add(handleLog);
                    if (OrderUtil.checkHasPrepayed(order)) {
                        order.setHasPrepayed(true);
                        order.setOrderState(OrderState.HAS_PAID);
                        OrderUtil.updateStayInMid(order);
                    }
                    break;
				}
			}
			orOrderDao.updateOrder(order);
		}
		return vchResult;
	}

	 /**
     * 调用代金券接口取消接口 hotel2.9.3 add by chenjiajie 2009-09-02
     * @param order
     * @param roleUser
     */
    public void callVchServiceCancelOrder(OrOrder order, UserWrapper roleUser) throws VCHException{
//    	//把代金券的支付记录设为未支付    	
//    	List<OrPayment> paymentList = order.getPaymentList();
//    	for (OrPayment orPayment : paymentList) {
//    		if(PrepayType.Coupon == orPayment.getPayType()){
//    			orPayment.setPaySucceed(false);
//    			orPayment.setOrder(order);
//    		}
//		}
//    	order.setPaymentList(paymentList);

    	vchService.cancelOrder(order.getOrderCD(), roleUser.getName());
    	
        OrHandleLog handleLog = new OrHandleLog();
        handleLog.setModifierName(roleUser.getName());
        handleLog.setModifierRole(roleUser.getLoginName());
        handleLog.setContent("进行了取消代金券操作");
        handleLog.setModifiedTime(new Date());
        handleLog.setOrder(order);
        order.getLogList().add(handleLog);
    }
	
    
    /**
     * 调用代金券接口试预订接口 hotel2.9.3 add by chenjiajie 2009-09-02
     * @param orderData
     * @param voucherCodeList
     * @return
     * @throws VCHException
     */
    public int callVchServicePreOrder(OrOrder order,UserWrapper roleUser,MemberDTO member,String channel) throws VCHException{
    	//生成调用代金券接口的参数
    	Map<String, String> orderData = this.genVoucherParams(order,roleUser,member,channel);
    	//用于检验代金券使用状态的列表
    	List<String> voucherCodeListForQuery = new ArrayList<String>(order.getCouponRecords().size());
    	for (OrCouponRecords couponRecords : order.getCouponRecords()) {
    	    voucherCodeListForQuery.add(couponRecords.getCouponCode());
		} 
    	List<Voucher> voucherList = vchService.getVouchersByCodes(voucherCodeListForQuery);
        //记录用于锁定的代金券列表
        List<String> voucherCodeList = new ArrayList<String>(order.getCouponRecords().size());
    	for (Iterator it = voucherList.iterator(); it.hasNext();) {
            Voucher voucher = (Voucher) it.next();
            //当代金券未锁定，未使用的时候才进行锁定操作
            if(null != voucher && 2 > voucher.getStatus()){
                voucherCodeList.add(voucher.getCode());
            }
        }
		//调用代金券接口占住代金券
		int vchResult = vchService.preOrder(orderData, voucherCodeList);
		if(vchResult > 0){
		    StringBuffer voucherCodeStrList = new StringBuffer();
		    for (Iterator it = voucherCodeList.iterator(); it.hasNext();) {
	            String voucherCode = (String) it.next();
	            voucherCodeStrList.append(voucherCode);
	            if(it.hasNext()){
	                voucherCodeStrList.append(",");
	            }
	        }
	        OrHandleLog handleLog = new OrHandleLog();
	        handleLog.setModifierName("system");
	        handleLog.setModifierRole("system");
	        handleLog.setContent("锁定代金券:"+voucherCodeStrList.toString()+"成功");
	        handleLog.setModifiedTime(new Date());
	        handleLog.setOrder(order);
	        order.getLogList().add(handleLog);
	        orOrderDao.saveOrUpdate(order);
		}
    	return vchResult;
    }
    
    /**
     * 调用代金券接口失败的时候回滚曹错 hotel2.9.3 add by chenjiajie 2009-09-11
     * @param order
     * @param reason
     */
    public void rollBackVchOrderState(OrOrder order,String reason){
    	//把代金券的支付记录设为支付    	
    	List<OrPayment> paymentList = order.getPaymentList();
    	for (OrPayment orPayment : paymentList) {
    		if(PrepayType.Coupon == orPayment.getPayType()){
    			orPayment.setPaySucceed(false);
    		}
		}
    	
    	List<OrCouponRecords> orCouponRecordsList = order.getCouponRecords();
    	Date now = new Date();
    	for (OrCouponRecords orCouponRecords : orCouponRecordsList) {
    		orCouponRecords.setPaysucceed(false);
    		orCouponRecords.setOperateTime(now);
		}
    	
        OrHandleLog handleLog = new OrHandleLog();
        handleLog.setModifierName("system");
        handleLog.setModifierRole("system");
        handleLog.setContent(reason);
        handleLog.setModifiedTime(new Date());
        handleLog.setOrder(order);
        order.getLogList().add(handleLog);
        orOrderDao.saveOrUpdate(order);
    }
    
    /**
     * 调用代金券接口预订确认接口 hotel2.9.3 add by chenjiajie 2009-09-02
     * @param order
     * @param userName
     * @return
     * @throws VCHException
     */
    public void callVchServiceOrder(OrOrder order,String userName) throws VCHException{
    	//调用代金券接口扣代金券
		vchService.order(order.getOrderCD(), userName);
		//调用接口成功
		//更新代金券明细的支付状态
		List<OrCouponRecords> couponRecords = order.getCouponRecords();
		for (OrCouponRecords orCouponRecords : couponRecords) {
			orCouponRecords.setPaysucceed(true);
			orCouponRecords.setOrder(order);
		}
		order.setCouponRecords(couponRecords);
		//更新代金券的支付方式和订单代金券使用日志
		List<OrPayment> paymentList = order.getPaymentList();
		for (OrPayment orPayment : paymentList) {
			if(orPayment.getPayType() == PrepayType.Coupon){
				orPayment.setPaySucceed(true);
				orPayment.setConfirmTime(new Date());
				//更新日志
                OrHandleLog handleLog = new OrHandleLog();
                handleLog.setOrder(order);
                handleLog.setModifiedTime(new Date());
                if(null != userName){
					orPayment.setConfirmer(userName);
                    handleLog.setModifierName(userName);
                    handleLog.setModifierRole(userName);
                }else{
					orPayment.setConfirmer("HWEB");
                    handleLog.setModifierName("HWEB");
                    handleLog.setModifierRole("HWEB");
                }
                handleLog.setContent("使用代金券成功，代金券金额共"+orPayment.getMoney()+"元");
                order.setPaymentList(paymentList);
                order.getLogList().add(handleLog);
                if (OrderUtil.checkHasPrepayed(order)) {
                    order.setHasPrepayed(true);
                    order.setOrderState(OrderState.HAS_PAID);
                    OrderUtil.updateStayInMid(order);
                }
                break;
			}
		}
		orOrderDao.updateOrder(order);		
    }


    /**
	 * 确认使用代金券的方法
	 * @param order
	 * @param roleUser
	 * @param member
	 */
	public void confirmVoucherState(OrOrder order, UserWrapper roleUser, MemberDTO member){
		if(null != order){
			//必须是预付，或者面付转预付
			if(order.isPrepayOrder() || order.isPayToPrepay()){
				//如果订单非取消单，使用了代金券，订单的状态是已支付状态
				if(!order.isCancel() 
						&& order.isIncludeCouponPrepay() 
						&& OrderState.HAS_PAID == order.getOrderState()){
					List<OrPayment> payMentList = order.getPaymentList();
					//存放代金券是否支付成功
					boolean isPaySucceed = false;
					double couponMoney = 0.0D;
					for (OrPayment orPayment : payMentList) {
						if(PrepayType.Coupon == orPayment.getPayType() && orPayment.isPaySucceed()){
						    couponMoney = orPayment.getMoney();
							isPaySucceed = true;
							break;
						}
					}
					//只有未支付成功的时候才进行下一部操作
					if(!isPaySucceed){
						try {
							//调用代金券接口预订确认接口 
							this.callVchServiceOrder(order, order.getModifierName());
//				            OrHandleLog handleLog = new OrHandleLog();
//				            handleLog.setModifierName("system");
//				            handleLog.setModifierRole("system");
//				            handleLog.setContent("使用代金券成功，代金券金额共"+couponMoney+"元");
//				            handleLog.setModifiedTime(new Date());
//				            handleLog.setOrder(order);
//				            order.getLogList().add(handleLog);
//				            orOrderDao.saveOrUpdate(order);
						} catch (VCHException e) {
							//调用代金券接口失败的时候回滚
							this.rollBackVchOrderState(order, e.getMessage());
							log.error(e.getMessage(),e);
						}						
					}
				}
			}
		}
	}
	
	/**
	 * 生成调用代金券接口的参数 CC参数封装
     * hotel 2.9.3 add by chenjiajie 2009-09-02
	 * @param order
	 * @param roleUser
	 * @param member
	 * @param channel
	 * @return
	 */
	public Map<String, String> genVoucherParams(OrOrder order,UserWrapper roleUser,MemberDTO member,String channel) {
		Map<String, String> orderData = new HashMap<String, String>();
		orderData.put(VchService.KEY_ORDER_NO, order.getOrderCD());//订单编号
		orderData.put(VchService.KEY_ORDER_AMOUNT, String.valueOf((int)Math.ceil(order.getSumRmb())));//订单金额
		//间夜数
		int roomNight = order.getRoomQuantity() * DateUtil.getDay(order.getCheckinDate(), order.getCheckoutDate());
		orderData.put(VchService.KEY_ORDER_COUNT, String.valueOf(roomNight));//订单间夜
		orderData.put(VchService.KEY_CONSUMEDATE, DateUtil.dateToString(order.getCheckinDate()));//入住日期
		orderData.put(VchService.KEY_PRODUCT_TYPE, VchService.PRODUCT_TYPE_HOTEL);//业务类型(酒店)
		//使用平台：网站或者呼叫中心
		String usedPlatForm = "";
		if(CHANNEL_CC.equals(channel)){
			usedPlatForm = VchService.USED_BY_CC;
		}else if(CHANNEL_HKWEB.equals(channel)){
			usedPlatForm =CHANNEL_HKWEB;
		}
		else{
			usedPlatForm = VchService.USED_BY_WEB;
		}
		orderData.put(VchService.KEY_USED_PLATFORM, usedPlatForm);
		String beShipedStr = order.isNeedFulfill() ? "Y" : "N";
		orderData.put(VchService.KEY_HOTEL_BE_SHIPED, beShipedStr);//是否配送
		String userName = null != roleUser && StringUtil.isValidStr(roleUser.getName()) ? roleUser.getName() : "system";
		orderData.put(VchService.KEY_ORDER_OPTERATOR, userName);//订单操作员
		orderData.put(VchService.KEY_ORDER_DATE, DateUtil.formatDateToYMDHMS1(new Date()));//订单时间
		orderData.put(VchService.KEY_HOTEL_HOTEL, String.valueOf(order.getHotelId()));//酒店--酒店编码
		HtlHotel htlHotel = hotelService.findHotel(order.getHotelId());
		orderData.put(VchService.KEY_HOTEL_CITY, htlHotel.getCity());//酒店--城市编码
		String hotelStar = htlHotel.getHotelStar(); //酒店星级
		String hotelStarForVoucher = "";
		//5星级
		if("19".equals(hotelStar) || "29".equals(hotelStar)){
			hotelStarForVoucher = VchService.STAR_5;
		}
		//4星级
		else if("39".equals(hotelStar) || "49".equals(hotelStar)){
			hotelStarForVoucher = VchService.STAR_4;
		}
		//3星级
		else if("59".equals(hotelStar) || "64".equals(hotelStar)){
			hotelStarForVoucher = VchService.STAR_3;
		}
		//3星级以下
		else {
			hotelStarForVoucher = VchService.STAR_BELOW_3;
		}
		orderData.put(VchService.KEY_HOTEL_STAR_LEVEL, hotelStarForVoucher);
		orderData.put(VchService.KEY_HOTEL_COUNTRY, htlHotel.getCountry());
		orderData.put(VchService.KEY_HOTEL_PROVINCE, htlHotel.getState());
		orderData.put(VchService.KEY_MEMBER_NO, member.getMembercd());//会员编号
		orderData.put(VchService.KEY_AGENT, member.getAgentid());//渠道号
		String memberCardLevel = "";
		String memberCardType = "";
		//如果是职员卡，则转化为金卡
		if(StringUtil.isValidStr(member.getType()) && member.getType().equals("4")){
			memberCardLevel = "2";
			memberCardType = VchService.CARD_TYPE_STAFF;
		}
		//其他情况按照原来的卡级别赋值
		else{
			memberCardLevel = member.getType();
			//TMC卡
			if(StringUtil.isValidStr(member.getTravelbusiness()) && member.getTravelbusiness().equals("Y")){
				memberCardType = VchService.CARD_TYPE_TMC;
			}
			//个人卡
			else{
				memberCardType = VchService.CARD_TYPE_PERSONAL;
			}
		}
		orderData.put(VchService.KEY_MEMBER_CARD_LEVEL, memberCardLevel);//会员卡级别
		orderData.put(VchService.KEY_MEMBER_CARD_TYPE, memberCardType);//会员卡类别
		return orderData;
	}
    
	public VchService getVchService() {
		return vchService;
	}

	public void setVchService(VchService vchService) {
		this.vchService = vchService;
	}

	public OrOrderDao getOrOrderDao() {
		return orOrderDao;
	}

	public void setOrOrderDao(OrOrderDao orOrderDao) {
		this.orOrderDao = orOrderDao;
	}

	public void setHotelService(IHotelService hotelService) {
		this.hotelService = hotelService;
	}

}

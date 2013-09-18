package com.mangocity.webnew.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hagtb2b.service.IHtlB2bService;
import com.mangocity.hdl.constant.ChannelType;
import com.mangocity.hotel.base.manage.HtlLimitFavourableManage;
import com.mangocity.hotel.base.persistence.B2BAgentCommUtils;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.resource.ResourceManager;
import com.mangocity.hotel.base.service.CommissionService;
import com.mangocity.hotel.base.service.IHotelFavourableReturnService;
import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.hotel.base.service.IQuotaControlService;
import com.mangocity.hotel.base.service.assistant.QuotaQuery;
import com.mangocity.hotel.base.service.assistant.QuotaReturn;
import com.mangocity.hotel.base.web.TranslateUtil;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.ext.member.dto.PointDTO;
import com.mangocity.hotel.ext.member.exception.MemberException;
import com.mangocity.hotel.ext.member.service.PointsDelegate;
import com.mangocity.hotel.order.constant.ConfirmType;
import com.mangocity.hotel.order.constant.MemberConfirmSmsStutas;
import com.mangocity.hotel.order.constant.MemberConfirmType;
import com.mangocity.hotel.order.constant.ModelType;
import com.mangocity.hotel.order.constant.OrderItemType;
import com.mangocity.hotel.order.constant.OrderSource;
import com.mangocity.hotel.order.constant.OrderType;
import com.mangocity.hotel.order.constant.PrepayType;
import com.mangocity.hotel.order.persistence.FITCashItem;
import com.mangocity.hotel.order.persistence.FITOrderCash;
import com.mangocity.hotel.order.persistence.OrCouponRecords;
import com.mangocity.hotel.order.persistence.OrFellowInfo;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrMemberConfirm;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderItem;
import com.mangocity.hotel.order.persistence.OrPayment;
import com.mangocity.hotel.order.persistence.OrPriceDetail;
import com.mangocity.hotel.order.persistence.assistant.OrderAssist;
import com.mangocity.hotel.order.service.IOrderBenefitService;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.hotel.order.service.IVoucherInterfaceService;
import com.mangocity.hotel.order.service.assistant.MsgAssist;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.hweb.persistence.QueryHotelForWebSaleItems;
import com.mangocity.proxy.member.service.MemberInterfaceService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.hotel.constant.BaseConstant;
import com.mangocity.util.hotel.constant.FaxEmailModel;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.util.hotel.constant.QuotaType;
import com.mangocity.util.log.MyLog;
import com.mangocity.vch.app.service.exception.VCHException;
import com.mangocity.webnew.persistence.HotelOrderFromBean;
import com.mangocity.webnew.service.IHotelOrderCompleteService;
import com.mangoctiy.communicateservice.CommunicaterService;
import com.mangoctiy.communicateservice.domain.Mail;
import com.mangoctiy.communicateservice.domain.Sms;

/** 
 * 新版网站处理订单完成Service实现
 * 主要功能是生成订单，订单提交中台，扣取会员信用卡费用，积分，锁定代金
 * @author chenjiajie
 *
 */
public class HotelOrderCompleteService implements IHotelOrderCompleteService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4083152570399610755L;
    
	
	private static final MyLog log = MyLog.getLogger(HotelOrderCompleteService.class);

    /**
     * 网站业务接口
     */
    private HotelManageWeb hotelManageWeb;
    
    /**
     * Order辅助类
     */
    private OrderAssist orderAssist;
    
    /**
     * 会员接口
     */
    private MemberInterfaceService memberInterfaceService;

    /**
     * 积分接口 改用会员Jar的接口 modify by chenjiajie 2009-05-12
     */
    private PointsDelegate pointsDelegate;

    /**
     * IOrderService
     */
    private IOrderService orderService;
    
    private IHtlB2bService htlB2bService;
    
    /**
     * IHotelService
     */
    private IHotelService hotelService;

    /**
     * message接口
     */
    private CommunicaterService communicaterService;

    /**
     * 传真邮件辅助类
     */
    private MsgAssist msgAssist;
    
    /**
     * 代金券管理接口
     */
    private IVoucherInterfaceService voucherInterfaceService;
    
    /**
     * 优惠立减服务接口 add by chenjiajie 2009-10-15
     */
    private IOrderBenefitService orderbenefitService;
    
    /**
     * 会员转换辅助
     */
    protected TranslateUtil translateUtil;
    
    
    /**
     * 资源接口
     */
    private ResourceManager resourceManager;// parasoft-suppress SERIAL.NSFSC "暂不修改"
    
    /**
     * 配额接口
     */
	private IQuotaControlService quotaControl;
	
	/**
	 * 现金返还接口 add by linpeng.fang 2010-10-12
	 */ 
    private IHotelFavourableReturnService returnService;

	private CommissionService commissionService;
    
    /**
     * 限量返现促销活动service add by xiaojun.xiong 2011-3-4
     */
    private HtlLimitFavourableManage limitFavourableManage;
    /**
     * 客人使用信用卡支付或担保，和订单绑定 信用卡id在信用卡填写页面财务返回
     */
    public void bindOrderCreditCard(OrOrder order,String cardID){
    	order.setCreditCardIdsSelect(cardID);
    }
       
    
    /**
     * 使用全额积分支付或全代金券时，订单订单扣配额后，如果没有特殊要求就要发“即时确认”信息
     * @param member
     * @param order
     */
    public void sendImediateMessage(MemberDTO member,OrOrder order) {
        if (null != member && member.isMango()) {
            HtlHotel hotel = hotelService.findHotel(order.getHotelId().longValue());
            OrMemberConfirm memberConfirm = new OrMemberConfirm();
            if (3 == order.getConfirmType()) {// 发送短信确认
                String hotelLink = "(" + hotel.getChnAddress() + " " + hotel.getTelephone() + ")";
                String hotelInfoStr = order.getHotelName();
                Date choutDate = new Date(order.getCheckoutDate().getTime() + 24 * 60 * 60 * 1000);
                String dateStr = "";
                long checkInLong = order.getCheckinDate().getTime() + 24 * 60 * 60 * 1000;
                long checkOutLong = choutDate.getTime();
                if (checkOutLong > checkInLong) {
                    dateStr = DateUtil.toStringByFormat(order.getCheckinDate(), "MM") + "月"
                        + DateUtil.toStringByFormat(order.getCheckinDate(), "dd") + "-"
                        + DateUtil.toStringByFormat(order.getCheckoutDate(), "dd") + "日";
                } else {
                    dateStr = DateUtil.toStringByFormat(order.getCheckinDate(), "MM月dd日");
                }
                String roomType = (order.getRoomQuantity() + "").trim() + "间"
                    + order.getRoomTypeName() + "(" + order.getChildRoomTypeName() + ")";
                String confirmString = dateStr + hotelInfoStr + hotelLink + roomType
                    + "已确认。4006640066";
                Sms sms = new Sms();
                sms.setApplicationName("hotel");
                sms.setTo(new String[] { order.getMobile() });
                sms.setMessage(confirmString);
                Long sendRes = 0L;
                try {
                	sendRes = communicaterService.sendSms(sms);
				} catch (Exception e) {
					log.error("ErrCode:135150 HotelOrderCompleteService.sendImediateMessage() call sendSms() Error",e);
				}
                memberConfirm.setChannel(ConfirmType.SMS);
                memberConfirm.setModelType(order.getType());// 114还是芒果
                memberConfirm.setType(MemberConfirmType.CONFIRM);
                memberConfirm.setSendTarget(order.getMobile());
                memberConfirm.setSendTime(new Date());
                memberConfirm.setSendSucceed(true);
                memberConfirm.setHisNo(order.getHisNo());
                /** v2.7.1 RMS 2416订单页面增加短信发送状态显示及实时更新功能 chenjiajie 2009-02-17 begin * */
                memberConfirm.setSendManId("HWEB"); // 发送人的工号
                memberConfirm.setUnicallRetId(sendRes); // UnitCall返回的流水号
                memberConfirm.setContent(confirmString); // 发送内容
                memberConfirm.setSendStatus(MemberConfirmSmsStutas.SENDING); // 发送状态，初始化为1
                /** v2.7.1 RMS 2416订单页面增加短信发送状态显示及实时更新功能 chenjiajie 2009-02-17 end * */

                memberConfirm.setOrder(order);
                order.getMemberConfirmList().add(memberConfirm);
                // 已发送客户确认
                order.setSendedMemberFax(true);
                OrHandleLog handleLog = new OrHandleLog();
                handleLog.setContent("订单改为:<font color='red'>已发送客户确认<font>");
                handleLog.setModifiedTime(new Date());
                handleLog.setHisNo(order.getHisNo());
                handleLog.setOrder(order);
                order.getLogList().add(handleLog);
                orderService.saveOrUpdate(order);
            } else if (2 == order.getConfirmType()) {// 发送电子邮件确认
                Mail mail = new Mail();
                mail.setApplicationName("hotel");
                mail.setTo(new String[] { order.getEmail() });
                String subject = "";
                String templateNo = "";
                String xmlString = "";

                subject = "芒果网酒店预定成功";
                templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MEMO_MANGOBOOK_HK;

                if (null != member)
                    xmlString = msgAssist.genOrderMailByGuestMangoConfirm(order, hotel, member, "0");
                mail.setFrom("cs@mangocity.com");
                mail.setSubject(subject);
                mail.setTemplateFileName(templateNo);
                mail.setXml(xmlString);
                Long ret = null;
                try {
                    ret = communicaterService.sendEmail(mail);
                } catch (RuntimeException e) {
                    log.info("send customer email error:" + e);
                }
                memberConfirm.setChannel(ConfirmType.EMAIL);
                memberConfirm.setType(MemberConfirmType.CONFIRM);
                memberConfirm.setModelType(ModelType.MODEL_MANGO);
                memberConfirm.setSendTarget(order.getEmail());
                memberConfirm.setSendTime(new Date());
                memberConfirm.setSendSucceed(true);
                memberConfirm.setHisNo(order.getHisNo());
                /** v2.7.1 RMS 2416订单页面增加短信发送状态显示及实时更新功能 chenjiajie 2009-02-17 begin * */
                memberConfirm.setSendManId("HWEB"); // 发送人的工号
                memberConfirm.setUnicallRetId(ret); // UnitCall返回的流水号
                memberConfirm.setContent(xmlString); // 发送内容
                memberConfirm.setSendStatus(MemberConfirmSmsStutas.SENDING); // 发送状态，初始化为0
                /** v2.7.1 RMS 2416订单页面增加短信发送状态显示及实时更新功能 chenjiajie 2009-02-17 end * */

                memberConfirm.setOrder(order);
                order.getMemberConfirmList().add(memberConfirm);
                // 已发送客户确认
                order.setSendedMemberFax(true);
                OrHandleLog handleLog = new OrHandleLog();
                handleLog.setContent("订单改为:<font color='red'>已发送客户确认<font>");
                handleLog.setModifiedTime(new Date());
                handleLog.setHisNo(order.getHisNo());
                handleLog.setOrder(order);
                order.getLogList().add(handleLog);
                orderService.saveOrUpdate(order);
            }
        }
    }
    
    public void finalSaveOrder(){
    	
    }
    
	/**
	 * 获取订单配额, 如果获取成功则同时填充orderItems
	 * 
	 * @param order
	 * @return true:获取配额成功, false:没有全部获取到配额
	 */
    public int[] fillOrderItems(OrOrder order,HotelOrderFromBean hotelOrderFromBean) {

        int[] deduct = new int[2];
        // 扣配额
        List<QuotaReturn> kk = new ArrayList<QuotaReturn>();
        QuotaQuery quotaQuery = new QuotaQuery();
        // 添加扣配额参数
        quotaQuery.setBeginDate(hotelOrderFromBean.getCheckinDate());
        quotaQuery.setEndDate(hotelOrderFromBean.getCheckoutDate());
        quotaQuery.setChildRoomId(hotelOrderFromBean.getChildRoomTypeId());
        quotaQuery.setHotelId(hotelOrderFromBean.getHotelId());
        // 默认为cc 1
        quotaQuery.setMemberType(1);
        quotaQuery.setPayMethod(hotelOrderFromBean.getPayMethod());
        quotaQuery.setQuotaNum(Integer.parseInt(hotelOrderFromBean.getRoomQuantity()));

        quotaQuery.setQuotaType(hotelOrderFromBean.getQuotaType());
        quotaQuery.setRoomTypeId(hotelOrderFromBean.getRoomTypeId());
        //设置床型ID
        quotaQuery.setBedID(order!=null?order.getBedType():0);
        quotaQuery.setChannel(Long.valueOf(order.getChannel()));
        //if (log.isInfoEnabled())
            log.info("HotelOrderCompleteService saveOrder, fillOrderItems:"
                + "in parameter quotaControl.deductQuota "
                + "QuotaQueryPo(beginDate=" + hotelOrderFromBean.getCheckinDate() + ",endDate="
                + hotelOrderFromBean.getCheckoutDate() + ",childRoomId="
                + hotelOrderFromBean.getChildRoomTypeId() + ",hotelId="
                + hotelOrderFromBean.getHotelId() + ",memberType=1,payMethod="
                + hotelOrderFromBean.getPayMethod() + ",quotaNum="
                + hotelOrderFromBean.getRoomQuantity() + ",quotaType="
                + hotelOrderFromBean.getQuotaType() + ",roomTypeId="
                + hotelOrderFromBean.getRoomTypeId() + ")");
        try {
        	//kk = hotelWebRmiClient.getQuotaServ().deductQuota(quotaQuery);
        	kk = quotaControl.deductQuota(quotaQuery);
        } catch (Exception ex) {
            log.error("HotelOrderCompleteService saveOrder, fillOrderItems:"
                + "quotaControl.deductQuota(quotaQueryPo) result: ex=" + ex
                + ",kk=" + kk.size());
        }
        log.info("HotelOrderCompleteService saveOrder, fillOrderItems:"
            + "quotaControl.deductQuota(quotaQueryPo) result:kk=" + kk.size());
        if (0 >= kk.size()) {
            //if (log.isTraceEnabled())
                log.error("HotelOrderCompleteService saveOrder, fillOrderItems:"
                    + "quotaControl.deductQuota QuotaQueryPo result: "
                    + kk.size());
            deduct[0] = -1;
            return deduct;
        } else {
            boolean isReturn = false;
            /**
            for (QuotaReturn quotaReturn : kk) {
                if (null != quotaReturn.getQuotaType()
                    && (quotaReturn.getQuotaType().equals("3") || quotaReturn.getQuotaType().equals("4"))) {
                	List<QuotaReturn> returnQuotaList = new ArrayList<QuotaReturn>();
                	returnQuotaList.add(quotaReturn);
                    quotaControl.getQuotaServ().returnQuota(returnQuotaList);
                	
                    isReturn = true;
                    deduct[1] = -1;
                    break;
                }
            }
            **/
            if (isReturn) // 临时配额3或呼出配额4返回失败
            {
               // if (log.isTraceEnabled())
                    log.error("HotelOrderCompleteService saveOrder, fillOrderItems:"
                        + "quotaControl.getQuotaServ() QuotaQueryPo "
                        + "result: is temporary quota or callout quota.");
                deduct[0] = -1;
                return deduct;
            }
            deduct[1] = -2; // 已扣配额成功
           // if (log.isInfoEnabled())
                log.info("HotelOrderCompleteService saveOrder, fillOrderItems:"
                    + "quotaForCCService.deductQuota QuotaQueryPo result(List quotaQueryPo.size()="
                    + kk.size());
        }

        // 填充orderItems,这里quotas依次按日期，房间号排序
        boolean bQuotaOk = true;
        Date tonight = null;
        int roomIndex = -1;
        int dayIndex = -1;
        int nDays = DateUtil.getDay(order.getCheckinDate(), order.getCheckoutDate());
        boolean quotaCanReturn = true;
        boolean bSetCanReturn = false;
        String[] sQuotaTypes = new String[order.getRoomQuantity()];
        String[] fellowNamesArr = fillFellowNamesToOrderItem(order); // 每天各个房间的入住人姓名数组 v2.4.2
       
        String b2bCD = order.getAgentCode();
        int policyScope = orderService.getPolicyScope(b2bCD);//享受佣金政策，1,2,3分别代表第一套，第二套，两套都享受
        float hstar = order.getHotelStar();
        if(hstar > 10){
        	String strHotelStar = resourceManager.getDescription("res_hotelStarToNum",Math.round(hotelOrderFromBean.getHotelStar()));
        	hstar = Float.parseFloat(strHotelStar);
        }
        int intStar = Math.round(hstar);
        
        List<HtlPrice> priceList = new ArrayList<HtlPrice>();
        // chenjiajie 2008-12-30
        for (int i = 0; i < kk.size(); i++) {
            QuotaReturn quotaReturn = kk.get(i);
            OrOrderItem orderItem = new OrOrderItem();

            Date curDate = quotaReturn.getQuotaDate();
            if (null == tonight || 0 < DateUtil.compare(tonight, curDate)) {
                // 如果是下一天
                tonight = curDate;
                roomIndex = 0;
                dayIndex++;
            } else {
                roomIndex++;
            }

            orderItem.setRoomIndex(roomIndex);
            orderItem.setDayIndex(dayIndex);
            orderItem.setFirstNight(0 == dayIndex ? true : false);
            orderItem.setLastNight(dayIndex >= nDays - 1 ? true : false);
            orderItem.setNight(curDate);
            orderItem.setFellowName(fellowNamesArr[roomIndex]); // 把拆分后的入住人姓名封装到orderItem中 V2.4.2
            // chenjiajie 2008-12-30

            String returnQuotaType = quotaReturn.getQuotaType();
            String returnQuotaPattern = quotaReturn.getQuotaPattern();
            if (returnQuotaPattern.equals("C-I")) { // 进店模式
                if (0 == dayIndex) { // 首天
                    sQuotaTypes[roomIndex] = returnQuotaType;
                }
                if (null == sQuotaTypes[roomIndex]
                    || sQuotaTypes[roomIndex].equals(QuotaType.CALLQUOTA)) {
                    orderItem.setConfirm(false);
                    bQuotaOk = false;
                } else {
                    orderItem.setConfirm(true);
                }
                if(QuotaType.CALLQUOTA.equals(returnQuotaType)){
               	 	orderItem.setConfirm(false);
               	 	bQuotaOk = false;
               }
            } else {
                // 呼出配额一律为获取不成功
                if (returnQuotaType.equals(QuotaType.CALLQUOTA)) {
                    orderItem.setConfirm(false);
                    bQuotaOk = false;
                } else {
                    int nSign = quotaReturn.getSign();
                    if (0 != nSign) {
                        bQuotaOk = false;
                    }
                    orderItem.setConfirm(0 == nSign ? true : false);
                }
            }
            
            
            //获取每天的早餐信息 add by shengwei.zuo 2010-3-1
            HtlPrice  priceEntity = new HtlPrice();
            priceEntity = hotelManageWeb.qryHtlPriceForCC(hotelOrderFromBean.getChildRoomTypeId(), curDate, hotelOrderFromBean.getPayMethod(),
    				returnQuotaType);
            if(priceEntity!=null){
            	priceList.add(priceEntity);
            	
            	 log.info("====================priceEntity  is  not null================");
            	 if(priceEntity.getIncBreakfastType()!=null && !"".equals(priceEntity.getIncBreakfastType())){
            		 orderItem.setBreakfast(Integer.parseInt(priceEntity.getIncBreakfastType()));
            	 }else{
            		 orderItem.setBreakfast(0);
            	 }
            	 log.info("==================== orderItem.getBreakfast()================"+orderItem.getBreakfast());
            	 if(priceEntity.getIncBreakfastNumber()!=null && !"".equals(priceEntity.getIncBreakfastNumber())){
            		 orderItem.setBreakfastNum(Integer.parseInt(priceEntity.getIncBreakfastNumber()));
            	 }else{
            		 orderItem.setBreakfastNum(0);
            	 }
            	 log.info("==================== orderItem.getBreakfastNum()================"+orderItem.getBreakfastNum());
            }else{
            	 orderItem.setBreakfast(0);
                 orderItem.setBreakfastNum(0);
            }
            
            orderItem.setQuantity(quotaReturn.getQuotaNum());
            orderItem.setQuotaType(returnQuotaType);
            orderItem.setBasePrice(quotaReturn.getBasePrice());
            orderItem.setSalePrice(quotaReturn.getSalePrice());
            orderItem.setMarketPrice(quotaReturn.getSalesroomPrice());
            orderItem.setRoomState(quotaReturn.getRoomState());
            orderItem.setHotelId(hotelOrderFromBean.getHotelId());
            
            orderItem.setQuotaPattern(quotaReturn.getQuotaPattern());
            orderItem.setQuotaholder("CC");
            orderItem.setQuotaType(quotaReturn.getQuotaType());
            orderItem.setQuotashare(quotaReturn.getQuotaShare());
            orderItem.setOrderItemsType(OrderItemType.NORMAL);
            orderItem.setOrder(order);
            
            //当为 代理订单时 ，才会去算代理佣金.则得把代理佣金填充到orderItem里面 add by shengwei.zuo 2010-1-13
            if(order.getType() == OrderType.TYPE_B2BAGENT){
            	
                B2BAgentCommUtils B2BAgentCommUtilsInfo  =   commissionService.getB2BCommInfo(ChannelType.CHANNEL_ELONG==order.getChannel(),order.getPaymentCurrency(),curDate, order.getHotelId(),order.getRoomTypeId(),order.getChildRoomTypeId(),
	        			 order.getPayMethod(),String.valueOf(intStar),b2bCD);
                if(B2BAgentCommUtilsInfo!=null){
                	
                	log.info("HotelOrderCompleteService  fillOrderItems B2BAgentCommUtilsInfo : "+
                              " agentComission  "+ B2BAgentCommUtilsInfo.getAgentComission() +","+
                              " agentComissionPrice" + B2BAgentCommUtilsInfo.getAgentComissionPrice()+","+
                              " agentComissionRate" + B2BAgentCommUtilsInfo.getAgentComissionRate() + ","+
                              " comissionType"+ B2BAgentCommUtilsInfo.getComissionType() + ","+
                              " comissionTypeValue" + B2BAgentCommUtilsInfo.getComissionTypeValue()+","+
                              " CommTax" + B2BAgentCommUtilsInfo.getCommTax() );
                	
                	orderItem.setAgentComission(B2BAgentCommUtilsInfo.getAgentComission());
                    orderItem.setAgentComissionPrice(B2BAgentCommUtilsInfo.getAgentComissionPrice());
                    orderItem.setAgentComissionRate(B2BAgentCommUtilsInfo.getAgentComissionRate());
                    orderItem.setComissionType(B2BAgentCommUtilsInfo.getComissionType());
                    orderItem.setComissionTypeValue(B2BAgentCommUtilsInfo.getComissionTypeValue());
                    orderItem.setCommTax(B2BAgentCommUtilsInfo.getCommTax());
                    orderItem.setAgentReadComission(B2BAgentCommUtilsInfo.getAgentReadComission());
                    orderItem.setAgentReadComissionPrice(B2BAgentCommUtilsInfo.getAgentReadComissionPrice());
                    orderItem.setAgentReadComissionRate(B2BAgentCommUtilsInfo.getAgentReadComissionRate());
                }                
            }

            order.getOrderItems().add(orderItem);
           // if (log.isInfoEnabled()) {
                log.info("AddHotelOrderForWebAction fillOrderItems orderItem:quantity="
                    + orderItem.getQuantity() + ",quotaType" + orderItem.getQuotaType()
                    + ",basePrice" + orderItem.getBasePrice() + ",salePrice"
                    + orderItem.getSalePrice() + ",roomState" + orderItem.getRoomState()
                    + ",hotelId" + orderItem.getHotelId() + ",breakfast" + orderItem.getBreakfast()
                    + ",breakfastNum" + orderItem.getBreakfastNum() + ",quotaPattern"
                    + orderItem.getQuotaPattern());
           // }
            // 系统内配额如果不可退，设置不可退标志
            if (!bSetCanReturn && !quotaReturn.isTakebackQuota()
                && (null != returnQuotaType && !returnQuotaType.equals(QuotaType.CALLQUOTA))) {
                quotaCanReturn = true;
                bSetCanReturn = true;
            }
        }
        
        //连住优惠相应改变OrOrderItem的售价和佣金 add by guzhijie 2009-09-17 begin
    	List<OrPriceDetail>  orPriceDetailList = order.getPriceList();
    	List<OrOrderItem> orderItems1List = order.getOrderItems();
    	int dateNum = DateUtil.getDay(order.getCheckinDate(), order.getCheckoutDate());
        if(!orderItems1List.isEmpty()){
	        for(int k=0;k < dateNum;k++){
	        	Date day = DateUtil.getDate(order.getCheckinDate(), k);
	        	
	        	log.info("ChildRoomId: "+quotaQuery.getChildRoomId()+"---day :"+day+"-----payMethod :"+hotelOrderFromBean.getPayMethod()+"----QuotaType : "+hotelOrderFromBean.getQuotaType());
	        	
	        	HtlPrice htlPrice = hotelManageWeb.qryHtlPriceForCC(quotaQuery
	    				.getChildRoomId(), day, hotelOrderFromBean.getPayMethod(),
	    				hotelOrderFromBean.getQuotaType());
	        	if(htlPrice==null){
	        		log.info("取到的 htlPrice 对象为空 ");
	        	}
	        	for(int x=0;x<orPriceDetailList.size();x++){
	        		OrPriceDetail orPriceDetailItems = orPriceDetailList.get(x);
	        		log.info("orPriceDetailItems.getNight() : "+orPriceDetailItems.getNight());
	        		log.info("htlPrice.getAbleSaleDate() : "+htlPrice.getAbleSaleDate());
	        		log.info("htlPrice.getCommission() : "+htlPrice.getCommission());
	        		log.info("htlPrice.getBasePrice() : "+htlPrice.getBasePrice());
	            	if(orPriceDetailItems.getNight().equals(htlPrice.getAbleSaleDate())){
	            		if(String.valueOf(htlPrice.getCommission())!=null && !"".equals(String.valueOf(htlPrice.getCommission()))){
	            			orPriceDetailItems.setCommission(htlPrice.getCommission());
	            		}
	            		if(String.valueOf(htlPrice.getBasePrice())!=null && !"".equals(String.valueOf(htlPrice.getBasePrice()))){
	            			orPriceDetailItems.setBasePrice(htlPrice.getBasePrice());
	            		}
	            		if(String.valueOf(htlPrice.getSalePrice())!=null && !"".equals(String.valueOf(htlPrice.getSalePrice()))){
	            			orPriceDetailItems.setSalePrice(htlPrice.getSalePrice());
	            		}
	            	}
	        	}
	        }
        }
       
        log.info("========================orderItems1List  for  end =====================");
        
        if(!orPriceDetailList.isEmpty() && !orderItems1List.isEmpty()){
        	//连住优惠增加佣金值到OrPriceDetail，方便下面赋值到OrderItems
        	int f=0;
        	
        	for(int i =0;i<orPriceDetailList.size();i++){
        		Date day = DateUtil.getDate(order.getCheckinDate(), i);
	        	HtlPrice htlPrice = hotelManageWeb.qryHtlPriceForCC(quotaQuery
	    				.getChildRoomId(), day, hotelOrderFromBean.getPayMethod(),
	    				hotelOrderFromBean.getQuotaType());
        		OrPriceDetail orPriceDetailItem = orPriceDetailList.get(i);
        		f=hotelManageWeb.changeBasePriceCommission(htlPrice, orPriceDetailList, orPriceDetailItem, 
        				hotelOrderFromBean.getHotelId(), hotelOrderFromBean.getChildRoomTypeId(), i, f);
        	}
        	//是否有连住优惠
        	boolean isFavourable = false;
        	for(int x= 0 ;x<orPriceDetailList.size();x++){
        		OrPriceDetail orPriceDetailItems = orPriceDetailList.get(x);
        		isFavourable = orPriceDetailItems.getIsFavourable();
        		for(int y=0;y<orderItems1List.size();y++){
        			OrOrderItem orderItems1 = orderItems1List.get(y);
        			if(orPriceDetailItems.getNight().getTime() == orderItems1.getNight().getTime()){
        				orderItems1List.get(y).setSalePrice(orPriceDetailItems.getSalePrice());
        				orderItems1List.get(y).setCommission(orPriceDetailItems.getCommission());
        				orderItems1List.get(y).setBasePrice(orPriceDetailItems.getBasePrice());
        				
        				//当为 代理订单时 ，才会去算代理佣金.则得把代理佣金填充到orderItem里面 add by shengwei.zuo 2010-1-13
        	            if(order.getType() == OrderType.TYPE_B2BAGENT){
            				//有连住优惠 add by shengwei.zuo 2010-1-15
            				if(isFavourable){
            					//代理佣金辅助类
            					B2BAgentCommUtils agentCommUtilsInfo  =   commissionService.getB2BCommInfo(orPriceDetailItems.getSalePrice(), orPriceDetailItems.getCommission(),order.getPaymentCurrency(), order.getHotelId(),order.getRoomTypeId(),order.getChildRoomTypeId(),
            		        			 order.getPayMethod(),String.valueOf(intStar),b2bCD);
            					if(agentCommUtilsInfo!=null){
            						
            						log.info("HotelOrderCompleteService  fillOrderItems agentCommUtilsInfo  : "+
            								"isFavourable :"+isFavourable+","+
            	                            " agentComission  "+ agentCommUtilsInfo.getAgentComission() +","+
            	                            " agentComissionPrice" + agentCommUtilsInfo.getAgentComissionPrice()+","+
            	                            " agentComissionRate" + agentCommUtilsInfo.getAgentComissionRate() + ","+
            	                            " comissionType"+ agentCommUtilsInfo.getComissionType() + ","+
            	                            " comissionTypeValue" + agentCommUtilsInfo.getComissionTypeValue()+","+
            	                            " CommTax" + agentCommUtilsInfo.getCommTax() );
            						
            						orderItems1List.get(y).setAgentComission(agentCommUtilsInfo.getAgentComission());
                					orderItems1List.get(y).setAgentComissionPrice(agentCommUtilsInfo.getAgentComissionPrice());
                					orderItems1List.get(y).setAgentComissionRate(agentCommUtilsInfo.getAgentComissionRate());
                					orderItems1List.get(y).setComissionType(agentCommUtilsInfo.getComissionType());
                					orderItems1List.get(y).setCommTax(agentCommUtilsInfo.getCommTax());
                					orderItems1List.get(y).setAgentReadComission(agentCommUtilsInfo.getAgentReadComission());
                					orderItems1List.get(y).setAgentReadComissionPrice(agentCommUtilsInfo.getAgentReadComissionPrice());
                					orderItems1List.get(y).setAgentReadComissionRate(agentCommUtilsInfo.getAgentReadComissionRate());
            					}
            				}
        	            	
        	            }

        			}
        		}
        	}
    		
    	}
        //连住优惠相应改变OrOrderItem的售价和佣金 add by guzhijie 2009-09-17 end
        
      //现金返现（2.9.5） add by linpeng.fang 2010-10-13 
        BigDecimal totalCashReturnAmount = new BigDecimal(0);
        BigDecimal cashReturnAmount = new BigDecimal(0);
        
        // 交行全卡商旅等渠道无需芒果网优惠信息 modify by chenkeming
        if(!order.isCooperateOrder()) {
    		if (null!=hotelOrderFromBean.getReturnAmount()&&hotelOrderFromBean.getReturnAmount() > 0) {
    			int payMethod = PayMethod.PAY.equals(order.getPayMethod()) ? 1 : 2;
    			/**面转预类型按照面付的返现规则来计算返现 add by xiaojun.xiong  begin*/
    			if(order.isPayToPrepay()){
    				payMethod=1;
    			}
    			/**面转预类型按照面付的返现规则来计算返现 add by xiaojun.xiong  end*/
    			for (OrOrderItem item : order.getOrderItems()) {
    				// 现金返现（2.9.5） add by linpeng.fang 2010-10-13
    				cashReturnAmount = new BigDecimal(0);//每次循环必须要清空为0，add by diandian.hou 2011-06-27
    				for (HtlPrice priceEntity : priceList) {
    					int diff = DateUtil.getDay(priceEntity.getAbleSaleDate(),
    							item.getNight());
    					if (diff == 0 && item.getSalePrice() > 0) {
    						BigDecimal price = returnService.calculateRoomTypePrice(
    								priceEntity.getFormulaId(), new BigDecimal(item
    										.getCommission()), new BigDecimal(priceEntity
    										.getCommissionRate()), new BigDecimal(item
    										.getSalePrice()));
    						
    						//计算限量返现
    						int cashLimitReturnAmount=limitFavourableManage.calculateCashLimitReturnAmount(order.getHotelId(), order.getChildRoomTypeId(), 
    								item.getNight(), order.getPaymentCurrency(), new BigDecimal(item.getSalePrice()), item.getCommission());
    						
    						//如果没有限量返现，再计算普通返现，如果有，则不计算普通返现
    						if(cashLimitReturnAmount==-1){
    							cashReturnAmount = new BigDecimal(returnService
        								.calculateCashReturnAmount(order
        										.getChildRoomTypeId(), item.getNight(),
        										payMethod, priceEntity.getCurrency(),
        										1,  price));
    						}else{
    							cashReturnAmount = new BigDecimal(cashLimitReturnAmount);
    						}
    						break;
    					}
    				}
    				cashReturnAmount = cashReturnAmount.multiply(new BigDecimal(hotelOrderFromBean.getCashbackratevalue()));
    				item.setCashReturnAmount(cashReturnAmount.intValue());
    				totalCashReturnAmount=totalCashReturnAmount.add(cashReturnAmount);
    			}
    		}
        }

		order.setCashBackTotal(totalCashReturnAmount.intValue());
        
        log.info("========================orPriceDetailList  for  end =====================");
        
        for(int g=0;g<orderItems1List.size();g++){
        	OrOrderItem orderItemObj = new OrOrderItem();
        	orderItemObj = orderItems1List.get(g);
        	log.info("fillOrderItems orderItem ============SalePrice() : "+orderItemObj.getSalePrice()+"====getBasePrice() :"+orderItemObj.getBasePrice());
        }
        
        /** V2.9.3.1 必须放在最后计算，如果是优惠立减订单，使用之前已经查询出来的立减规则计算明细的立减金额 add by chenjiajie 2009-10-19 begin **/
        if(0 < order.getFavourableFlag()){
        	//生成订单明细的时候，计算订单明细的立减金额
        	orderbenefitService.calculateOrderItemBenefit(order);
        }
        /** V2.9.3.1 必须放在最后计算，如果是优惠立减订单，使用之前已经查询出来的立减规则计算明细的立减金额 add by chenjiajie 2009-10-19 end **/
        
        order.setQuotaCanReturn(quotaCanReturn);
        order.setQuotaOk(bQuotaOk);
        if(!bQuotaOk){
        	deduct[0] = -1;
        }else{
        	deduct[0] = 0;
        }
        return deduct;
    }
    
    /**
     * 保存返现相关信息
     * @param order
     * @param  orderCd
     * @param hotelOrderFromBean
     * @param member
     */
    public void fillCashInformation(OrOrder order, String orderCd,
			HotelOrderFromBean hotelOrderFromBean,MemberDTO memberDTO) {
		if (hotelOrderFromBean.getReturnAmount()>0) {
			if (null != memberDTO && memberDTO.isFitFlag()
					&& Double.valueOf(hotelOrderFromBean.getReturnAmount()) > 0) {
				FITOrderCash fitCash = new FITOrderCash();
				fitCash.setMemberCd(memberDTO.getMembercd());
				fitCash.setOrderCd(orderCd);
				fitCash.setReturnCash(order.getCashBackTotal());

				Map<String, Double> cashMap = returnService.fillCashItem(order.getChildRoomTypeId(), order.isPayToPrepay()?"pay":order.getPayMethod(), order.getCheckinDate(), order.getCheckoutDate());

				List<OrOrderItem> orderItems = order.getOrderItems();
				List<FITCashItem> cashItems = new ArrayList<FITCashItem>();
				for (OrOrderItem orderItem : orderItems) {
					FITCashItem cashItem = new FITCashItem();
					cashItem.setOrderCd(orderCd);
					cashItem.setReturnCash(orderItem.getCashReturnAmount());
					cashItem.setReturnDate(orderItem.getNight());
					
						cashItem.setReturnScale(cashMap.get(DateUtil.formatDateToSQLString(orderItem.getNight())) == null ? 0: cashMap.get(DateUtil.formatDateToSQLString(orderItem.getNight())));
					
					cashItems.add(cashItem);
				}
				returnService.saveCashInformation(fitCash, cashItems);
			}
		}
	}

    /**
     * 拆分入住人到OrOrderItem v2.4.2 2008-12-30
     * 
     * @param order
     * @return 每天各个房间的入住人姓名数组
     * @author chenjiajie
     */
    public String[] fillFellowNamesToOrderItem(OrOrder order) {
        int roomQuantity = order.getRoomQuantity();
        String[] fellowNamesArr = new String[roomQuantity]; // 用于封装入住人姓名到OrOrderItem里的数组
        List<OrFellowInfo> fellowList = order.getFellowList();
        int fellowSize = fellowList.size(); // 入住人数量
        /**
         * 入住人数量=房间数量 如A,B,C三个入住人，预订了三间房在同一张订单： 第一间房：A 第二间房：B 第三间房：C
         */
        if (fellowSize == roomQuantity) {
            for (int i = 0; i < roomQuantity; i++) {
                OrFellowInfo orFellowInfo = fellowList.get(i);
                fellowNamesArr[i] = orFellowInfo.getFellowName();
            }
        }
        /**
         * 入住人数量>房间数量 如A,B,C,D,E五个入住人，预订了三间房在同一张订单： 第一间房：A D 第二间房：B E 第三间房：C
         */
        else if (fellowSize > roomQuantity) {
            for (int i = 0; i < fellowSize; i++) {
                // 房间列表的下标
                int fellowIndex = i - (i / roomQuantity) - (roomQuantity - 1) * (i / roomQuantity);
                OrFellowInfo orFellowInfo = fellowList.get(i);
                if (i > fellowIndex) { // 第一次增加入住人的时候前面不需要有空格
                    fellowNamesArr[fellowIndex] += " " + orFellowInfo.getFellowName();
                } else {
                    fellowNamesArr[fellowIndex] = orFellowInfo.getFellowName();
                }
            }
        }
        /**
         * 入住人数量<房间数量 如A,B,C三个入住人，预订了五间房在同一张订单 第一间房：A 第二间房：B 第三间房：C 第四间房：A代订 第五间房：B代订
         */
        else {
            for (int i = 0; i < roomQuantity; i++) {
                // 入住人列表的下标
                int fellowIndex = i - (i / fellowSize) - (fellowSize - 1) * (i / fellowSize);
                OrFellowInfo orFellowInfo = fellowList.get(fellowIndex);
                if (i > fellowIndex) { // 第一组入住人填充完后，重复填充的时候在入住人姓名后加"代订"
                    if (0 < orFellowInfo.getFellowName().indexOf("代订")) {
                        fellowNamesArr[i] = orFellowInfo.getFellowName();
                    } else {
                        fellowNamesArr[i] = orFellowInfo.getFellowName() + "代订";
                    }
                } else {
                    fellowNamesArr[i] = orFellowInfo.getFellowName();
                }
            }
        }
        return fellowNamesArr;
    }
    
    /**
     * 调用会员积分接口
     * @param orderNew
     * @param acturalAmount 使用的积分所对应的人民币数量
     * @param particialPay  是否使用积分
     * @throws MemberException 
     * @throws Exception
     */
    public boolean deductUlmPoint(OrOrder orderNew, double acturalAmount, boolean particialPay) throws MemberException,Exception {
    	boolean isSuccessed = false;
        OrPayment payment = new OrPayment();
        double points = 0.0d;
        if (particialPay) {
            points = acturalAmount * 100;
        } else {
            points = Double.valueOf(orderNew.getSumRmb()).doubleValue() * 100;
        }
        points = BigDecimal.valueOf(points).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
        PointDTO ptt = null;
        //if (log.isInfoEnabled())
            log.info("HotelOrderCompleteService saveOrder, deductUlmPoint:"
                + "pointsDelegate.changePonitsByMemberCd(membercd=" + orderNew.getMemberCd()
                + ",username=" + BaseConstant.USERNAME + ",tranType=1,changeValue=" + points
                + ",orderCD=" + orderNew.getOrderCD() + ").");
        boolean bSuc = true;
        PointDTO pointWrapper = null;
        try {
        	pointWrapper = pointsDelegate.getPonitsByMemberCd(orderNew.getMemberCd(), BaseConstant.USERNAME);
        }catch(MemberException e) {
        	throw new Exception("查找积分失败!");
        }
        
        //检查会员是否够积分扣，防止通过工具篡改积分
        if(null != pointWrapper
        		&& StringUtil.isValidStr(pointWrapper.getBalanceValue())){
    		double diffValue = Double.parseDouble(pointWrapper.getBalanceValue()) - points;
    		if(diffValue >= 0){
    			isSuccessed = true;
				//调用会员接口扣积分
                if (orderNew.getSource() != null
						&& orderNew.getSource().equals(
								OrderSource.FROM_WEB)) {
                	ptt = pointsDelegate.changePonitsByMemberCd(
							orderNew.getMemberCd(), BaseConstant.USERNAME,
		    	            "1", points, orderNew.getOrderCD(),
							BaseConstant.TRANSCHANNEL_NET,
							BaseConstant.TRANSCHANNELSN_NET);
				} else if (orderNew.getSource() != null
						&& orderNew.getSource().equals(
								OrderSource.FAN_TI_NET)) {
					ptt = pointsDelegate.changePonitsByMemberCd(
							orderNew.getMemberCd(), BaseConstant.USERNAME,
		    	            "1", points, orderNew.getOrderCD(),
							BaseConstant.TRANSCHANNEL_NET,
							BaseConstant.TRANSCHANNELSN_BIG);
				} else {
					ptt = pointsDelegate.changePonitsByMemberCd(
							orderNew.getMemberCd(), BaseConstant.USERNAME,
		    	            "1", points, orderNew.getOrderCD(),
							BaseConstant.TRANSCHANNEL_CC,
							BaseConstant.TRANSCHANNELSN_CC);
				}
    			
    	        
    	        if (null != ptt && ptt.getRc().equals("0")) {
    	           // if (log.isInfoEnabled())
    	                log.info("HotelOrderCompleteService saveOrder, deductUlmPoint:"
    	                    + "pointsDelegate.changePonitsByMemberCd result Points(balanceValue="
    	                    + ptt.getBalanceValue() + ",excRate=" + ptt.getExcRate() + ",rc=" + ptt.getRc()
    	                    + ",message=" + ptt.getMessage() + ").");
    	            // double extPt = Double.parseDouble(member.getPoint().getBalanceValue());
    	            // extPt -= points;
    	            // member.getPoint().setBalanceValue("" + extPt);
    	        } else {
    	            bSuc = false;
    	            log.error("HotelOrderCompleteService saveOrder, deductUlmPoint:"
    	                + "pointsDelegate.changePonitsByMemberCd result Points is null  ---"
    	                + ptt.getMessage());
    	        }
    	        Date now = new Date();
    	        payment.setPayType(PrepayType.Points);
    	        // modify by chenkeming@2008.01.08 修改生产td98:无法正常扣减积分.
    	        payment.setPaySucceed(bSuc);
    	        payment.setConfirmer("HWEB");
    	        payment.setConfirmTime(now);
    	        payment.setMoney(particialPay ? acturalAmount : orderNew.getSumRmb());
    	        payment.setCreator("HWEB");
    	        payment.setOperator("HWEB");
    	        payment.setCreateTime(now);
    	        payment.setOperateTime(now);
    	        payment.setOrder(orderNew);
    	        orderNew.getPaymentList().add(payment);
    	        //添加日志 add by diandian.hou 2011-11-23
    	        OrHandleLog handleLog = new OrHandleLog();
    	        handleLog.setOrder(orderNew);
    	        handleLog.setModifiedTime(new Date());
                handleLog.setModifierName("HWEB");
                handleLog.setModifierRole("HWEB");
                String flag_cutPointSuccess=bSuc==true?"成功":"失败";
                handleLog.setContent("网站扣积分"+flag_cutPointSuccess);
                orderNew.getLogList().add(handleLog);
    	        
        	}
        }
        //if (log.isInfoEnabled())
            log.info("HotelOrderCompleteService saveOrder, deductUlmPoint:"
                + "orderService.updateOrder(orderCD=" + orderNew.getOrderCD() + ") begin...");
        orderService.updateOrder(orderNew);
      //  if (log.isInfoEnabled())
            log.info("HotelOrderCompleteService saveOrder, deductUlmPoint:"
                + "orderService.updateOrder(orderCD=" + orderNew.getOrderCD() + ") success.");
        return isSuccessed;
    }

    /**
     * 调用代金券接口，扣减代金券 hotel2.9.3 add by chenjiajie 2009-09-04
     * @param params
     * @param order
     * @param member
     */
    public void deductUsedCoupon(Map params, OrOrder order,MemberDTO member){
		//使用代金券的数量
		String couponStr = (String) params.get("couponNum");
		List<OrCouponRecords> orCouponRecordsList = order.getCouponRecords();
		if(StringUtil.isValidStr(couponStr)
				&& null != orCouponRecordsList
				&& !orCouponRecordsList.isEmpty()){
			double couponAmount = 0;
			for (OrCouponRecords orCouponRecords : orCouponRecordsList) {
				couponAmount += orCouponRecords.getCouponValue();
			}
			//保存代金券支付方式信息
			Date currentDate = new Date();
			OrPayment payment = new OrPayment();
			payment.setPayType(PrepayType.Coupon);
		    payment.setPaySucceed(false);
		    payment.setConfirmer("HWEB");
		    payment.setConfirmTime(currentDate);
		    payment.setMoney(couponAmount);
		    payment.setCreator("HWEB");
		    payment.setOperator("HWEB");
		    payment.setCreateTime(currentDate);
		    payment.setOperateTime(currentDate);
		    payment.setOrder(order);
		    order.getPaymentList().add(payment);
		}
		//调用代金券接口试预订接口 占住代金券
		try {
			// 来自繁体网站 add by diandian.hou 
			int vchResult =0;
			if(OrderSource.FAN_TI_NET.equals((order.getSource()))){
				vchResult = voucherInterfaceService.callVchServicePreOrder(order,null,member,IVoucherInterfaceService.CHANNEL_HKWEB);
			}else
			 vchResult = voucherInterfaceService.callVchServicePreOrder(order,null,member,IVoucherInterfaceService.CHANNEL_WEB);

	        OrHandleLog handleLog = new OrHandleLog();
	        handleLog.setOrder(order);
	        handleLog.setModifiedTime(new Date());
            handleLog.setModifierName("HWEB");
            handleLog.setModifierRole("HWEB");
			//调用接口内部返回出错
			if(vchResult == 0){
				log.error(" vch001 代金券支付失败!原因：接口内部错误。OrderCD: "+order.getOrderCD());
		        handleLog.setContent("代金券支付失败!原因：接口内部错误。");
			}else{
		        handleLog.setContent("网站代金券锁定成功!");
			}
	        order.getLogList().add(handleLog); 
			orderService.saveOrUpdate(order);    
		} catch (VCHException e) {
			voucherInterfaceService.rollBackVchOrderState(order, e.getMessage());
			log.error(" vch002 代金券支付失败!原因："+e.getMessage()+" OrderCD: "+order.getOrderCD());
			log.info(" vch002 代金券支付失败!原因："+e.getMessage()+" OrderCD: "+order.getOrderCD());
		}
    }

    
    //添加安全扣除代价券    add by diandian.hou 2011-11-03  
    public void deductUsedCouponAndConfirm(Map params, OrOrder order,MemberDTO member){
		//使用代金券的数量
		String couponStr = (String) params.get("couponNum");
		List<OrCouponRecords> orCouponRecordsList = order.getCouponRecords();
		if(StringUtil.isValidStr(couponStr)
				&& null != orCouponRecordsList
				&& !orCouponRecordsList.isEmpty()){
			double couponAmount = 0;
			for (OrCouponRecords orCouponRecords : orCouponRecordsList) {
				couponAmount += orCouponRecords.getCouponValue();
			}
			//保存代金券支付方式信息
			Date currentDate = new Date();
			OrPayment payment = new OrPayment();
			payment.setPayType(PrepayType.Coupon);
		    payment.setPaySucceed(false);
		    payment.setConfirmer("HWEB");
		    payment.setConfirmTime(currentDate);
		    payment.setMoney(couponAmount);
		    payment.setCreator("HWEB");
		    payment.setOperator("HWEB");
		    payment.setCreateTime(currentDate);
		    payment.setOperateTime(currentDate);
		    payment.setOrder(order);
		    order.getPaymentList().add(payment);
		    String errMsg = "";
		//调用代金券接口试预订接口 占住代金券
		    try {
                //调用代金券接口试预订确认接口
                int vchResult = voucherInterfaceService.callVchServicePreOrder(order, null, member, IVoucherInterfaceService.CHANNEL_WEB);   
                //调用接口内部返回出错
                if(vchResult == 0){
                    errMsg += " 代金券锁定失败!原因：接口内部错误。";
                    log.info(" vch005 代金券锁定失败!原因：接口内部错误。OrderCD: "+order.getOrderCD());
                }else{
                    //确认使用代金券的方法 hotel2.9.3 add by chenjiajie 2009-09-15
                    voucherInterfaceService.confirmVoucherState(order, null, member);
                    orderService.saveOrUpdate(order);
                }
            } catch (Exception e) {
                voucherInterfaceService.rollBackVchOrderState(order, e.getMessage());
                errMsg += " 代金券支付失败!原因："+e.getMessage();
                log.error(" vch006 代金券支付失败!原因："+e.getMessage()+" OrderCD: "+order.getOrderCD(),e);
            }
		}
    }  
	  /**
     * 同步配额明细中的总价格到订单表，防止变价，导致总金额和配额明细中的金额不相等 add by shengwei.zuo 2010-6-4
     * @param order
     * @return
     */
    public void synchroSumPriceToOrder(OrOrder order, Boolean isMinPriceOrder){
    	
    	boolean favFlag = false;
    	//有优惠立减
    	if(order.getFavourableFlag()==1){
    		favFlag = true;
    	}
    	
    	//实际每天的价格的价格总和
    	double sumItemPrice = 0d;
    	
    	//每天预付立减的价格总和
    	float sumFavPrice = 0f; 
    	
    	List<OrOrderItem> orderIteLst = order.getOrderItems();
    	if(isMinPriceOrder){
    		//调用增幅价格计算接口
    		List<QueryHotelForWebSaleItems> priceLis =hotelManageWeb.queryPriceForWeb(order.getHotelId(), order.getRoomTypeId(), order.getChildRoomTypeId(), order.getCheckinDate(), order.getCheckoutDate(),0.0,0.0, order.getPayMethod(), order.getQuotaTypeOld());
    		List<QueryHotelForWebSaleItems>	bigsaPriLis = htlB2bService.modifyIncreaePrice(priceLis, order.getHotelId(), order.getChildRoomTypeId(), order.getCheckinDate(), order.getCheckoutDate());
    		orderIteLst = this.updateOrderItemPriceToIncreacePrice(orderIteLst, bigsaPriLis);
    	}
    	
    	if(orderIteLst!=null && !orderIteLst.isEmpty()){
    		int osize = orderIteLst.size();
    		for(int i =0;i<osize;i++){
    			OrOrderItem  OrIte = new OrOrderItem();
    			OrIte = orderIteLst.get(i);
    			
    			BigDecimal  bigSumItePrice  =  new  BigDecimal(Double.toString(sumItemPrice));  
    			//每天的销售价
    			BigDecimal  bigsaPri  =  new  BigDecimal(Double.toString(OrIte.getSalePrice()));   
    			sumItemPrice  = bigSumItePrice.add(bigsaPri).doubleValue();
    			
    			if(favFlag){//有预付立减
    				BigDecimal  bigSumFavPrice  =  new  BigDecimal(Float.toString(sumFavPrice));  
    				//每天的预付立减的价格
    				BigDecimal   bigfavPri  =  new   BigDecimal(Float.toString(OrIte.getFavourableAmount()));   
    				sumFavPrice = bigSumFavPrice.add(bigfavPri).floatValue();
    			}
    		}
    		
    		BigDecimal  sumIte =  new  BigDecimal(Double.toString(sumItemPrice));  
        	
        	BigDecimal  sumFav  =  new  BigDecimal(Float.toString(sumFavPrice));  
        	
        	//订单总金额
        	double sum =  sumIte.add(sumFav).doubleValue();
        	
        	order.setSum(sum);
        	
        	BigDecimal  sumBig =  new BigDecimal(Double.toString(sum));
        	
        	//汇率
        	BigDecimal  rateBig = new BigDecimal(Double.toString(order.getRateId()));
        	
        	double sumRmb = sumBig.multiply(rateBig).doubleValue();
        	
        	//结果四舍五入取两位小数
        	sumRmb = StringUtil.roundDouble(sumRmb, 2);
        	
        	if(favFlag){
        		
        		sumRmb = sumIte.multiply(rateBig).doubleValue();
        		
        		order.setFavourableAmount(sumFavPrice);
        		
        	}
        	
        	order.setSumRmb(sumRmb);
    		
    	}
    	
    }
    
    /**
     * 底价预付更新orderItems salePrice为增幅后价格
     * @param orderIteLst
     * @param bigsaPriLis
     * @return
     * add by xiaowei.wang
     */
    private List<OrOrderItem> updateOrderItemPriceToIncreacePrice(List<OrOrderItem> orderIteLst,List<QueryHotelForWebSaleItems>	bigsaPriLis){
    	//if(orderIteLst.isEmpty()||bigsaPriLis.isEmpty())return;
    	for(QueryHotelForWebSaleItems item:bigsaPriLis){
    		for(OrOrderItem orderitem:orderIteLst){
    			if(item.getFellowDate().equals(orderitem.getNight())){
    				orderitem.setSalePrice(item.getSalePrice());
    			}
    		}    		
    	}
    	return orderIteLst;
    }
    
    //如果变价得重新计算实收金额，用于完成页面的显示
    public double reCountActSum(HotelOrderFromBean hotelOrderFromBean,double sumRmbSl){
    	
    	//如果使用了积分
        if(hotelOrderFromBean.isUseUlmPoint()&& 
        hotelOrderFromBean.getUlmPoint()!=null && 
        !"".equals(hotelOrderFromBean.getUlmPoint())){
        	
        	int ulmMoney = 0;
        	int ulmPt =  Integer.parseInt(hotelOrderFromBean.getUlmPoint());
        	if(ulmPt>0){
        		ulmMoney = ulmPt/100;
        	}
        	
        	if(ulmMoney >0)
        	{
        		BigDecimal  sumsumRmbSl =  new  BigDecimal(Double.toString(sumRmbSl));  
        		BigDecimal  sumulmMoney =  new  BigDecimal(Integer.toString(ulmMoney));  
        		sumRmbSl = sumsumRmbSl.subtract(sumulmMoney).doubleValue(); 
        	}
        	
        }
        
        //如果使用了代金卷
        if(hotelOrderFromBean.isUsedCoupon() && !"0".equals(hotelOrderFromBean.getUlmCoupon())){
        	
        	BigDecimal  sumsumRmbSl =  new  BigDecimal(Double.toString(sumRmbSl));  
        	BigDecimal  sumCouponMoney =  new  BigDecimal(hotelOrderFromBean.getUlmCoupon());  
        	sumRmbSl = sumsumRmbSl.subtract(sumCouponMoney).doubleValue(); 
        	
        }
        
        return sumRmbSl;
    	
    }

	public HotelManageWeb getHotelManageWeb() {
		return hotelManageWeb;
	}

	public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
		this.hotelManageWeb = hotelManageWeb;
	}

	public OrderAssist getOrderAssist() {
		return orderAssist;
	}

	public void setOrderAssist(OrderAssist orderAssist) {
		this.orderAssist = orderAssist;
	}
	
    public MemberInterfaceService getMemberInterfaceService() {
		return memberInterfaceService;
	}

	public void setMemberInterfaceService(
			MemberInterfaceService memberInterfaceService) {
		this.memberInterfaceService = memberInterfaceService;
	}

	public IOrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(IOrderService orderService) {
		this.orderService = orderService;
	}

	public IHotelService getHotelService() {
		return hotelService;
	}

	public void setHotelService(IHotelService hotelService) {
		this.hotelService = hotelService;
	}

	public CommunicaterService getCommunicaterService() {
		return communicaterService;
	}

	public void setCommunicaterService(CommunicaterService communicaterService) {
		this.communicaterService = communicaterService;
	}

	public MsgAssist getMsgAssist() {
		return msgAssist;
	}

	public void setMsgAssist(MsgAssist msgAssist) {
		this.msgAssist = msgAssist;
	}

	public IVoucherInterfaceService getVoucherInterfaceService() {
		return voucherInterfaceService;
	}

	public void setVoucherInterfaceService(
			IVoucherInterfaceService voucherInterfaceService) {
		this.voucherInterfaceService = voucherInterfaceService;
	}


	public TranslateUtil getTranslateUtil() {
		return translateUtil;
	}

	public void setTranslateUtil(TranslateUtil translateUtil) {
		this.translateUtil = translateUtil;
	}

	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	public IQuotaControlService getQuotaControl() {
		return quotaControl;
	}

	public void setQuotaControl(IQuotaControlService quotaControl) {
		this.quotaControl = quotaControl;
	}

	public IOrderBenefitService getOrderbenefitService() {
		return orderbenefitService;
	}

	public void setOrderbenefitService(IOrderBenefitService orderbenefitService) {
		this.orderbenefitService = orderbenefitService;
	}
	
	public void setCommissionService(CommissionService commissionService) {
		this.commissionService = commissionService;
	}

	public IHotelFavourableReturnService getReturnService() {
		return returnService;
	}

	public void setReturnService(IHotelFavourableReturnService returnService) {
		this.returnService = returnService;
	}

	public IHtlB2bService getHtlB2bService() {
		return htlB2bService;
	}

	public void setHtlB2bService(IHtlB2bService htlB2bService) {
		this.htlB2bService = htlB2bService;
	}	
	
	public HtlLimitFavourableManage getLimitFavourableManage() {
		return limitFavourableManage;
	}

	public void setLimitFavourableManage(
			HtlLimitFavourableManage limitFavourableManage) {
		this.limitFavourableManage = limitFavourableManage;
	}


	public PointsDelegate getPointsDelegate() {
		return pointsDelegate;
	}


	public void setPointsDelegate(PointsDelegate pointsDelegate) {
		this.pointsDelegate = pointsDelegate;
	}
	
}

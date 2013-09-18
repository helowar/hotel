package com.mangocity.webnew.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mangocity.hdl.constant.ChannelType;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlArea;
import com.mangocity.hotel.base.persistence.HtlBookSetup;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlPriceType;
import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.hotel.base.service.SupplierInfoService;
import com.mangocity.hotel.base.service.SystemDataService;
import com.mangocity.hotel.base.service.assistant.HotelInfo;
import com.mangocity.hotel.base.web.TranslateUtil;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.constant.ConfirmType;
import com.mangocity.hotel.order.constant.HotelConfirmType;
import com.mangocity.hotel.order.constant.MemberConfirmSmsStutas;
import com.mangocity.hotel.order.constant.MemberConfirmType;
import com.mangocity.hotel.order.constant.ModelType;
import com.mangocity.hotel.order.constant.OrderSource;
import com.mangocity.hotel.order.dao.impl.OrOrderFaxDao;
import com.mangocity.hotel.order.manager.HotelFaxManager;
import com.mangocity.hotel.order.persistence.HtlOrderStsLog;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrMemberConfirm;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderFax;
import com.mangocity.hotel.order.persistence.OrOrderItem;
import com.mangocity.hotel.order.persistence.assistant.OrderUtil;
import com.mangocity.hotel.order.service.IMemberConfirmService;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.hotel.order.service.assistant.MemberAliasConstants;
import com.mangocity.hotel.order.service.assistant.MsgAssist;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.proxy.member.service.MemberInterfaceService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.hotel.constant.FaxEmailModel;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.util.log.MyLog;
import com.mangocity.webnew.dao.MangoSalesDao;
import com.mangocity.webnew.service.OrderImmedConfirmService;
import com.mangoctiy.communicateservice.CommunicaterService;
import com.mangoctiy.communicateservice.domain.Fax;
import com.mangoctiy.communicateservice.domain.Mail;
import com.mangoctiy.communicateservice.domain.Sms;

/**
 * 即时确认，
 * 给酒店传真，给用户发 邮件，短信操作
 * 
 * add by shengwei.zuo 2010-2-1
 * 
 * 
 */
public class OrderImmedConfirmServiceImpl extends DAOHibernateImpl implements OrderImmedConfirmService {
	
	private static final MyLog log = MyLog.getLogger(OrderImmedConfirmServiceImpl.class);
	
    private static final long serialVersionUID = -6480733904145267974L;

    /**
     * message接口
     */
    private CommunicaterService communicaterService;

    /**
     * 传真邮件辅助类
     */
    private MsgAssist msgAssist;

    private HotelFaxManager hotelFaxManager;

    /**
     * 酒店本部接口
     */
    private IHotelService hotelService;

    /*
     * 酒店信息实体类
     */
    private HotelInfo hotelInfo;

    /**
     * OrOrderFax Dao类
     */
    private OrOrderFaxDao orOrderFaxDao;

    private Long sumTo;

    private Long roomSumTo;

    private String confirmNo;

    private double orderSum;

    private Long hotelID;

//    private String faxNum = "";

    /*
     * 当前订单的状态
     */
//    private String orderType;

//    private String tohotelNotes = "";

//    private String faxType;

    private List orderItemGroupByList;

    private List hotelBookSetup;

    private int hotelBookSetupNumber;

    private String sendStatus = "";

   
    private Long memberConfirmId;

    private IMemberConfirmService memberConfirmService;

    /**
     * 发送SMS
     */
    private String confirmString;

    private String cancelString;

    private String pauseString;

    private String returnString;

    private String noContactString;

    private String isAnewSend;

    private String roomCrowdedString;

    private String changeRoomPriceString;

    private String guaranteeAnnounce;

    private String prepayAnnounce;

    private String checkoutAdvanced;

    private String partCheckin;

    private String noShow;
    
    //会员接口 add by shengwei.zuo  2010-1-30
    private  MemberInterfaceService    memberInterfaceService;
    
    /**
     * 会员转换辅助
     */
    protected TranslateUtil translateUtil;
    
    
    protected IOrderService orderService;
    
    private  MangoSalesDao  mangoSalesDao;
    
    /**
     * 酒店本部酒店基本信息管理接口
     */
    private HotelManage hotelManage;
    
    /**
     * 多供应商service
     */
    private SupplierInfoService supplierInfoService;
    
    //注入的service 
    private SystemDataService systemDataService;  // 取htlArea的数据 add by diandian.hou 
    
    /**
     * 给客人发送确认传真
     * @param faxNo
     * @param order
     * @param orderItemGroupByList
     * @param member
     * @param roleUser
     * @return
     */
    private Long sendCustomConfirmFax(String faxNo,OrOrder order,
        List orderItemGroupByList, MemberDTO memberDTO) {
        String sender = "0";
        // 无传真号
        if (null == faxNo || 0 == faxNo.length()) {
        	return new Long(0);
        }

        HtlHotel hotel = hotelManage.findHotel(order.getHotelId().longValue());
        String templateNo = "";
        String xmlString = "";
        int nFaxType = 1;

        if(!order.isCooperateOrder()) { // 非交行全卡商旅等渠道订单
            if (memberDTO.isMango()||"XJG".equals(order.getMemberState())) {
            	templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MEMO_MANGOBOOK;
            	xmlString = msgAssist.genOrderFaxByGuestMangoConfirm(order, hotel,orderItemGroupByList, memberDTO, sender);
            } else {//114
                if (MemberAliasConstants.LTT.equals(memberDTO.getAliasid())) {// 联通商旅 　　LTT
                    sender = "4";
                } else if (MemberAliasConstants.WTT.equals(memberDTO.getAliasid())) {// 　中国网通116114　WTT
                    sender = "3";
                } else if (MemberAliasConstants.WTBJ.equals(memberDTO.getAliasid())) {// 北京网通114电话导航　WTBJ
                    sender = "5";
                } else if (MemberAliasConstants.NHZY.equals(memberDTO.getAliasid())) {// 南航电话导航　NHZY
                    sender = "6";
                } else if (MemberAliasConstants.GDLT.equals(memberDTO.getAliasid())) {// 南航电话导航　NHZY
                    sender = "7";
                }else { // 114
                    sender = "2";
                }
                if (nFaxType == MemberConfirmType.CONFIRM) {
                    templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MEMO_MANGOBOOK;
                    xmlString = msgAssist.genOrderFaxByGuest114Confirm(order, hotel,
                        orderItemGroupByList, memberDTO, sender);
                }
            }	
        } else { // 交行全卡商旅等渠道订单
        	sender = "8";
            if (nFaxType == MemberConfirmType.CONFIRM) {
                templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MEMO_MANGOBOOK;
                xmlString = msgAssist.genOrderFaxByGuest114Confirm(order, hotel,
                    orderItemGroupByList, memberDTO, sender);
            } else if (nFaxType == MemberConfirmType.CANCEL) {
                templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MEMO_MANGOCANCEL;
                xmlString = msgAssist.genOrderFaxByGuest114Cancel(order, hotel,
                    orderItemGroupByList, memberDTO, sender);
            }
        }
        Fax fax = new Fax();
        fax.setXml(xmlString);
        fax.setApplicationName("hotel");
        fax.setTemplateFileName(templateNo);
        fax.setTo(new String[] { faxNo });
        fax.setFrom("system");
        Long ret = communicaterService.sendFax(fax); // 需要返回一个渠道实例号

        return ret;
    }
    
	//给用户发送即时确认信息-短信或者Email add by shengwei.zuo 2010-1-30
    // 添加一个 boolean参数 edit by diandian.hou
	public String sendImmedConfirmToCus(OrOrder order,boolean flagSencondConfirm, MemberDTO memberDTO) {
		
		String sendImMConfSate = "success";
		
		//判断是否为如家快捷上线的直联酒店
		//判断是否为如家快捷上线的直联酒店  二次确定 edit by diandian.hou
		//判断是否为艺龙酒店(艺龙酒店在下单时不发送客户短信) edit by yiwen.xu
		if( flagSencondConfirm || ChannelType.CHANNEL_ELONG == order.getChannel()){
			return sendImMConfSate;
		}
		
		log.info("=========NET OrderImmedConfirmServiceImpl.sendImmedConfirmToCuS=============orderCD:"+order.getOrderCD()+"=========confirmType : "+order.getConfirmType());
		
		 // 获取订单的member供预览页面用
		if(null==memberDTO){
			memberDTO = memberInterfaceService.getMemberByCode(order.getMemberCd());
		}
		
		if (ConfirmType.EMAIL==order.getConfirmType()) {// 电子邮件确认
			String stateus = sendEmailToCusProc(order, memberDTO);
			if("2".equals(stateus) || stateus =="2"){
				
				log.info("=========OrderImmedConfirmServiceImpl.sendImmedConfirmToCuS====Send Email   Failed=========orderCD:"+order.getOrderCD()+"=========");
				sendImMConfSate = "failed";
				
			}
        } else if (ConfirmType.FAX==order.getConfirmType()) {// 传真确认 add by luoguangming 2012年6月11日13:49:42
        	Long ret = this.sendCustomConfirmFax(order.getCustomerFax(), order, orderItemGroupByList, memberDTO);
        	if (null == ret || 0 >= ret) {
        		sendImMConfSate = "failed";
                log.info("OrderImmedConfirmServiceImpl.sendImmedConfirmToCuS====Send Fax   Failed===========orderCD:"+order.getOrderCD()+"=========");
            }else{
            	log.info("OrderImmedConfirmServiceImpl.sendImmedConfirmToCuS====Send Fax success: 渠道号："+ret);
            }
        	
        	// 已发送客户确认
            boolean bChange = false;
            if (!order.isCustomerConfirm()) {
                order.setCustomerConfirm(true);
                bChange = true;
            }
            if (bChange) {
                addMemberConfirmLog(order);
            }

            OrMemberConfirm memberConfirm = new OrMemberConfirm();
            memberConfirm.setChannel(ConfirmType.FAX);
            memberConfirm.setType(1);
            memberConfirm.setSendTarget(order.getCustomerFax());
            memberConfirm.setSendMan("system");
            memberConfirm.setSendTime(new Date());
            memberConfirm.setSendSucceed(true);
            if("XJG".equals(order.getMemberState())){
           	 memberConfirm.setModelType(1);// 114还是芒果
           }else{
           	memberConfirm.setModelType(order.getType());// 114还是芒果
           }

            /** v2.7.1 RMS 2416订单页面增加短信发送状态显示及实时更新功能 chenjiajie 2009-02-17 begin **/
            memberConfirm.setSendManId("system"); // 发送人的工号
            memberConfirm.setUnicallRetId(ret); // UnitCall返回的流水号
            memberConfirm.setSendStatus(sendImMConfSate == "failed" ? MemberConfirmSmsStutas.FAILED : MemberConfirmSmsStutas.SUCCESS); // 发送状态，初始化为1
            /** v2.7.1 RMS 2416订单页面增加短信发送状态显示及实时更新功能 chenjiajie 2009-02-17 end **/

            memberConfirm.setOrder(order);
            order.getMemberConfirmList().add(memberConfirm);

            // v2.8.1 增加操作人 add by chenkeming
            order.setModifier("system");
            order.setModifierName("system");
            this.update(order);
        } else if (ConfirmType.SMS==order.getConfirmType()) {// 短信确认
        	//手机号码
        	String phoneNo =  order.getMobile();
        	//准备发送短信给客户
        	String smsText = sendSMSToCus(order, memberDTO);
        	//确认默认为1；
        	int smsType = 1;
        	
        	try {
        		// 调用发送短信接口和创建订单客户确认信息发送表信息
                sendMessage(smsText, smsType, phoneNo,order);
        		log.info("===========OrderImmedConfirmServiceImpl.sendImmedConfirmToCus===========orderCD:"+order.getOrderCD()+"==========SMS && smsText :"+smsText+"========phoneNo :"+phoneNo);
        	}catch(Exception e){
        		sendImMConfSate = "failed";
        		log.info("=========OrderImmedConfirmServiceImpl.sendImmedConfirmToCuS==== SMS  Failed=========orderCD:"+order.getOrderCD()+"==========SMS && smsText :"+smsText+"========phoneNo :"+phoneNo);
        		log.error(e.getMessage(),e);
        	}
            
            //已发送客户确认
            if (!order.isSendedMemberFax()) {
                order.setSendedMemberFax(true);
            }
            
        	//设置客户确认标志
            boolean bChange = false;
            if (!order.isCustomerConfirm() && smsType == MemberConfirmType.CONFIRM) {
                order.setCustomerConfirm(true);
                bChange = true;
            }
            if (bChange) {
                addMemberConfirmLog(order);
            }
            
        } 
		
		log.info("=========NET OrderImmedConfirmServiceImpl.sendImmedConfirmToCuS==== SUCCESS OR  FAILED=========orderCD:"+order.getOrderCD()+"==========sendImMConfSate :"+sendImMConfSate);
		
		return sendImMConfSate;
		
	}
	
	private String sendFaxToHotel(OrOrder order,String faxNum,String faxType,MemberDTO memberDTO){
		String sendImMConfHotel = "failed";
        //如果取出来的传真号是email或为空，不发送
        if(!StringUtil.isValidStr(faxNum) || faxNum.indexOf('@') >= 0){
        	
        	 //写入错误日志 
        	 String errorLog = "传真号错误！发送即时确认传真给酒店失败！";
        	
        	 emailOrFaxErrorLog(order,errorLog);
        	
            return sendImMConfHotel = "failed";
        }
        
        //备注
        String tohotelNotes = "";
        if(order.getRemark()!= null && !"".equals(order.getRemark())){
        	tohotelNotes = order.getRemark().getHotelRemark();
        }
        
        //是否满足配额
        boolean quoOk = quotaOk(order);
        
        //是否支付成功
        boolean paySucc = order.isHasPrepayed();
        
        //如果是预付，或者面转预为true;
        if(PayMethod.PRE_PAY.equals(order.getPayMethod())||order.isPayToPrepay()){
        	
        	//满足配额
        	if(quoOk){
        		tohotelNotes = "芒果网担保入住；"+tohotelNotes;
        	}else{
        		
        		//不满足配额，信用卡担保或支付成功
        		if(order.getSuretyState()== 5 || paySucc){
        			
        			tohotelNotes = "芒果网担保入住；"+tohotelNotes;
        		}
        		
        	}
        	
        }else if(PayMethod.PAY.equals(order.getPayMethod())){
        	
        	//面付担保单
        	if(order.isCreditAssured() && order.getSuretyPrice()>0){
        		
        	    tohotelNotes = "芒果网担保入住；"+tohotelNotes;
        		
        	}
        	
        }
        
        log.info("=========OrderImmedConfirmServiceImpl.sendImmedConfirmToHotel====Start Send Fax ===========orderCD:"+order.getOrderCD());
        
        String satatuss  = hotelOrderFaxSend(order, faxNum, tohotelNotes, faxType, memberDTO);
        
        if("2".equals(satatuss) ||  satatuss =="2"){
			
			log.info("=========OrderImmedConfirmServiceImpl.sendImmedConfirmToHotel====Send Fax   Failed=========orderCD:"+order.getOrderCD()+"=========");
			sendImMConfHotel = "failed";
			
		}else{
		    log.info("=========OrderImmedConfirmServiceImpl.sendImmedConfirmToHotel====Send Fax   success=========orderCD:"+order.getOrderCD()+"=========");
		    sendImMConfHotel = "success";
		}
        return sendImMConfHotel;
	}
	
	//给酒店发送即时确认-传真  add by shengwei.zuo 2010-1-30
	public String sendImmedConfirmToHotel(OrOrder order, MemberDTO memberDTO) {
		
		//添加海南的不发酒店传真和电邮start add by diandian.hou 2011-12-30
		final String flag_noSendToHotel = "noSend";
		final Date startDate = DateUtil.stringToDateMain("2012-01-18", "yyyy-MM-dd");
		final Date endDate = DateUtil.stringToDateMain("2012-02-12", "yyyy-MM-dd");
		if(order!=null && order.getCity()!=null ){
			 HtlArea htlArea = systemDataService.queryAreaCode(order.getCity());
			 if(htlArea!=null && "HAN".equals(htlArea.getStateCode())){
				 Date checkOutDate = DateUtil.getDate(order.getCheckinDate(),-1);
				 if(DateUtil.compare(order.getCheckinDate(), endDate) >= 0 && DateUtil.compare(startDate, order.getCheckoutDate())>0 ) {
				      return flag_noSendToHotel;
				 }
			 }	
		}
		//添加海南的不发酒店传真和电邮end
		
		String faxType = "1";
		
		String sendImMConfHotel ="failed";
		
		/**
		 * 如有会员为空
		 */
		if(null==memberDTO){
			// 获取订单的member供预览页面用
			memberDTO = memberInterfaceService.getMemberByCode(order.getMemberCd());
		}
		
        // 如果没有发送或没有发送成功过，则不是新的预订单v2.6订单确认默认类型增加
        boolean succeedFlag = false;
        while (order.getFaxList().iterator().hasNext()) {
            OrOrderFax orderFax = order.getFaxList().iterator().next();
            if (orderFax.isSendSucceed()) {
                succeedFlag = true;
                break;
            }
        }
        
        //新下单
        String orderType = "newOrder";
        
        //如果有供应商，并能找到传真，则发送多供应商传真，否则走原来的实现
        HtlPriceType htlprice = hotelManage.findHtlPriceType(order.getChildRoomTypeId());
    	List<HtlBookSetup> htlBookSetupList= supplierInfoService.queryHtlSupplierFax(htlprice);

    	if(!htlBookSetupList.isEmpty()){
    		String faxNum = hotelService.getHotelFaxNo(order.getHotelId(), order.getChildRoomTypeId());
            log.info("=======orderCD:"+order.getOrderCD()+"=======supplier faxNum:"+faxNum);
            sendImMConfHotel = sendFaxToHotel(order,faxNum,faxType,memberDTO);
            if("failed".equals(sendImMConfHotel)){
            	return sendImMConfHotel = "failed" ;
            }
        }else{
            // 获取酒店默认联系方式,供保存成功页面使用
            String ctcttype = hotelService.getHotelSendType(order.getHotelId().toString());
            log.info("=======orderCD:"+order.getOrderCD()+"===============酒店默认确认方式:"+ctcttype);
            //因没有实现酒店发送email的功能，现在只有酒店默认发送传真的时候才自动发送
            if(StringUtil.isValidStr(ctcttype) && "02".equals(ctcttype)){
                
                //取出传真号码
                String faxNum = hotelService.getHotelFaxNo(order.getHotelId());
                log.info("=======orderCD:"+order.getOrderCD()+"=======faxNum:"+faxNum);
                
                sendImMConfHotel = sendFaxToHotel(order,faxNum,faxType,memberDTO);
                
                if("failed".equals(sendImMConfHotel)){
                	return sendImMConfHotel = "failed" ;
                }           
            }else if(StringUtil.isValidStr(ctcttype) && "01".equals(ctcttype)){//如果酒店的默认联系方式是Email，则发送Email add by shengwei.zuo 2010-3-9
            	  //email地址  
            	  String emailStr = hotelService.getHotelMail(order.getHotelId());
            	  //如果是空，或者不是合法的email,就提示错误
            	  if(!StringUtil.isValidStr(emailStr) || emailStr.indexOf("@")<0 ){
            		  
            		  //写入错误日志 
                 	 String errorLog = "Email错误！发送即时确认Email给酒店失败！";
                 	
                 	 emailOrFaxErrorLog(order,errorLog);
                 	 
            		 return sendImMConfHotel = "failed";
            		 
            	  }
            	  
                  //备注
                  String tohotelNotes = "";
                  if(order.getRemark()!= null && !"".equals(order.getRemark())){
                  	tohotelNotes = order.getRemark().getHotelRemark();
                  }
                  
                  //是否满足配额
                  boolean quoOk = quotaOk(order);
                  
                  //是否支付成功
                  boolean paySucc = order.isHasPrepayed();
                  
                  //如果是预付，或者面转预为true;
                  if(PayMethod.PRE_PAY.equals(order.getPayMethod())||order.isPayToPrepay()){
                  	
                  	//满足配额
                  	if(quoOk){
                  		tohotelNotes = "芒果网担保入住；"+tohotelNotes;
                  	}else{
                  		
                  		//不满足配额，信用卡担保或支付成功
                  		if(order.getSuretyState()== 5 || paySucc){
                  			
                  			tohotelNotes = "芒果网担保入住；"+tohotelNotes;
                  		}
                  		
                  	}
                  	
                  }else if(PayMethod.PAY.equals(order.getPayMethod())){
                  	
                  	//面付担保单
                  	if(order.isCreditAssured() && order.getSuretyPrice()>0){
                  		
                  	    tohotelNotes = "芒果网担保入住；"+tohotelNotes;
                  		
                  	}
                  	
                  }
                  
                  String satatuss  = hotelOrderEmailSend(order,emailStr,tohotelNotes,faxType, memberDTO);
                  
                  if("2".equals(satatuss) ||  satatuss =="2"){
          			log.info("=========OrderImmedConfirmServiceImpl.sendImmedConfirmToHotel====Send email   Failed=========orderCD:"+order.getOrderCD()+"=========");
          			sendImMConfHotel = "failed";
          		 }else{
          		    log.info("=========OrderImmedConfirmServiceImpl.sendImmedConfirmToHotel====Send email   success=========orderCD:"+order.getOrderCD()+"=========");
          		    sendImMConfHotel = "success";
          		 }
            	
            }else{//未取到酒店的即时确认方式，未发传真或Email add by shengwei.zuo 
            	
            	 //写入错误日志 
            	 String errorLog = "未获取到酒店的即时确认方式，未发传真或Email！";
            	
            	 emailOrFaxErrorLog(order,errorLog);
            	 
            	 return sendImMConfHotel = "failed";
            	 
            }
        }
		
		return sendImMConfHotel;
	}

	
    /**
     * 处理给酒店发送Email
     * add by shengwei.zuo  2010-3-9
     * @return
     */
    public String hotelOrderEmailSend(OrOrder order,String emailStr,String tohotelNotes,String faxType, MemberDTO memberDTO) {
    	
    	String sendStatusRes = "2";
    	
    	// 0 表示发送
        String isAnewSend = "0";
        
        hotelInfo = hotelService.getHotelInfoById(order.getHotelId());

        orderItemGroupByList = orderService.hQueryOrderItemByFaxGroup(order.getID());

        Map modifiedInfo = new HashMap();
        modifiedInfo.put("faxNum", emailStr);
        modifiedInfo.put("tohotelNotes", tohotelNotes);
        modifiedInfo.put("faxType", faxType);
        modifiedInfo.put("city", memberDTO.getMemberstate());
        Long ret = null;
        Long ID = orderService.getparmsId();
        String sender = "";

        if (memberDTO.isMango()) {
            sender = "1";
        } else {
            if (memberDTO.getAliasid().equals(MemberAliasConstants.LTT)) {// 联通商旅 LTT
                sender = "4";
            } else if (memberDTO.getAliasid().equals(MemberAliasConstants.WTT)) {// 中国网通116114
                // WTT
                sender = "3";
            } else if (memberDTO.getAliasid().equals(MemberAliasConstants.WTBJ)) {// 北京网通114电话导航
                // WTBJ
                sender = "5";
            } else if (memberDTO.getAliasid().equals(MemberAliasConstants.NHZY)) {// 南航 NHZY
                sender = "6";
            } else if(memberDTO.getAliasid().equals(MemberAliasConstants.GDLT)){
            	sender = "7";
            }else { // 114
                sender = "2";
            }
        }
        modifiedInfo.put("sender", sender);
        if (null != faxType) {
            Mail mail = new Mail();
            mail.setApplicationName("hotel");
            mail.setTo(new String[] { emailStr });
            String templateNo = "";
            String xmlString = "";
            String subject = "";
            HtlHotel hotel = hotelService.findHotel(order.getHotelId().longValue());
            int nFaxType = Integer.parseInt(faxType);
            if (nFaxType == HotelConfirmType.CONFIRM) {
                subject = "芒果网预定单";
                templateNo = FaxEmailModel.NOTIFY_EMAIL_HOTEL_ORDER_INFO;
                xmlString = msgAssist.genOrderFaxByHotelFaxConfirm(order, hotel, isAnewSend, ID,
                    orderItemGroupByList, modifiedInfo);

            } 
            
            mail.setSubject(subject);
            mail.setFrom("cs@mangocity.com");
            mail.setTemplateFileName(templateNo);
            mail.setXml(xmlString);
            mail.setUserLoginId("网站系统");
            try {
                ret = communicaterService.sendEmail(mail);
                if( null == ret || null == ret.toString() || "".equals(ret.toString()) || 0 >=ret.intValue()){
                	sendStatusRes = "2";
                	setLogSendHotelFaxOrEmail( order, emailStr, ID, false, ret, 2);
                	
                }else{
                	sendStatusRes = "1";
                	setLogSendHotelFaxOrEmail( order, emailStr, ID, true, ret, 2);
                	
                }
            } catch (RuntimeException e) {
                log.info(e.getMessage());
                sendStatusRes = "2";
                setLogSendHotelFaxOrEmail( order, emailStr, ID, false, ret, 2);
                // return super.forwardMsgBox("邮件发送失败！", "refreshSelf()");
            }
     
        }
        
        return  sendStatusRes ;
    
    }

	
	
    /**
     * 准备发送短信给客户
     * 
     * @return
     */
    public String sendSMSToCus(OrOrder order, MemberDTO memberDTO) {

        HtlHotel hotel = hotelService.findHotel(order.getHotelId().longValue());
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

        String roomType = (order.getRoomQuantity() + "").trim() + "间" + order.getRoomTypeName();

        //needArrivalTime是否显示保留时间点 add by chenjiajie 2010-02-02
        boolean needArrivalTime = OrderUtil.isShowArrivalTime(order);
        if (order.isMango()) {

            // 12月27－28日成都海悦酒店（四川省成都市春熙路东段步行街02881918888）2间标准间已确认,保留至18点
            confirmString = dateStr + hotelInfoStr + hotelLink + roomType + "已确认";
            if(needArrivalTime){
                confirmString += "，保留至" + order.getLatestArrivalTime() + "点";
            }

            // 您预订的12月27日成都海悦酒店2间标准间已取消，如有需要请再致电芒果网
            cancelString = "您预订的" + dateStr + order.getHotelName() + roomType + "已取消,如有需要请再致电芒果网";

            // 您预订的12月27－28日成都海悦酒店2间标准间正在处理中，如有问题请致电芒果网
            pauseString = "您预订的" + dateStr + order.getHotelName() + roomType + "正在处理中,我们将在";

            // 无法联系－满房
            // 满房通知：您预订的12月27日成都海悦酒店1间标准双人间已满,如需再预订请尽快致电芒果网
            roomCrowdedString = "满房通知:您预订的" + dateStr + order.getHotelName() + roomType
                + "已满,如需再预订请尽快致电芒果网";

            // 无法联系－变价
            // 价格调整通知：您预订的12月27日成都海悦酒店1间标准双人间价格有变,请尽快致电芒果网
            changeRoomPriceString = "价格调整通知:您预订的" + dateStr + order.getHotelName() + roomType
                + "价格有变,请尽快致电芒果网";

            // 无法联系－担保
            // 担保通知：您预订的12月27日成都海悦酒店1间标准双人间，房间紧张故需担保,请尽快致电芒果网
            guaranteeAnnounce = "担保通知:您预订的" + dateStr + order.getHotelName() + roomType
                + ",房间紧张故需担保,请尽快致电芒果网";

            // 无法联系－预付
            // 付款通知：您预订的12月27日成都海悦酒店1间标准双人间，酒店要求提前付款,请尽快致电芒果网
            prepayAnnounce = "付款通知:您预订的" + dateStr + order.getHotelName() + roomType
                + ",酒店要求提前付款,请尽快致电芒果网";

            // 日审－提前退房
            // modify by chenjiajie 2009-08-17
            // 您预订的08月11-12日（入住日期）杭州国际大厦（酒店名称）,酒店反馈您已提前退房,为确保积分准确,如不符请致电4008876698
            checkoutAdvanced = "您预订的" + dateStr + order.getHotelName()
                + ",酒店反馈您已提前退房,为确保积分准确,如不符请致电4008876698";

            // 日审－部分入住
            // modify by chenjiajie 2009-08-17
            // 您预订的08月11-12日（入住日期）杭州国际大厦（酒店名称）1间（房间数）经济特价房（房型）,酒店反馈您未全部入住,为确保积分准确,如不符请致电4008876698
            partCheckin = "您预订的" + dateStr + order.getHotelName() + roomType
                + ",酒店反馈您未全部入住,为确保积分准确,如不符请致电4008876698";

            // 日审－NO-SHOW
            // modify by chenjiajie 2009-08-17
            // 您预订的08月11-12日（入住日期）杭州国际大厦（酒店名称）1间（房间数）经济特价房（房型）,酒店反馈您未入住,为确保积分准确,如不符请致电4008876698
            noShow = "您预订的" + dateStr + order.getHotelName() + roomType
                + ",酒店反馈您未入住,为确保积分准确,如不符请致电4008876698";

            // <芒果网>您预订11月11-13日威尼斯皇冠假日酒店，据酒店反馈您于11月12日提前退房，积分相应减少，如不符请尽快联系4006640066

            // <芒果网>您预订的11月11日威尼斯皇冠假日酒店，对某些事项需与您确认，因无法联系到您，请您尽快致电4006640066。
        } else {
            if ("LTT".equals(memberDTO.getState())) {

                confirmString = dateStr + hotelInfoStr + hotelLink + roomType + "已确认";
                if(needArrivalTime){
                    confirmString += "，保留至" + order.getLatestArrivalTime() + "点";
                }

                cancelString = "您预订的" + dateStr + order.getHotelName() + roomType
                    + "已取消,如有需要请再致电联通商旅";

                pauseString = "您预订的" + dateStr + order.getHotelName() + roomType
                    + "正在处理中,我们将在";

                roomCrowdedString = "满房通知:您预订的" + dateStr + order.getHotelName() + roomType
                    + "已满,如需再预订请尽快致电联通商旅";

                changeRoomPriceString = "价格调整通知:您预订的" + dateStr + order.getHotelName() + roomType
                    + "价格有变,请尽快致电联通商旅";

                guaranteeAnnounce = "担保通知:您预订的" + dateStr + order.getHotelName() + roomType
                    + ",房间紧张故需担保,请尽快致电联通商旅";

                prepayAnnounce = "付款通知:您预订的" + dateStr + order.getHotelName() + roomType
                    + ",酒店要求提前付款,请尽快致电联通商旅";

                // 您预订的X月xx-xx日(入住日期）北京长峰假日酒店（酒店名称）,酒店反馈您已提前退房,如不符请致电4008876698
                // modify by chenjiajie 2009-08-17
                checkoutAdvanced = "您预订的" + dateStr + order.getHotelName()
                    + ",酒店反馈您已提前退房,如不符请致电4008876698";

                // 您预订的X月xx-xx日(入住日期）北京长峰假日酒店（酒店名称）1间（房间数）标准间（房型）,酒店反馈您未全部入住,如不符请致电4008876698
                // modify by chenjiajie 2009-08-17
                partCheckin = "您预订的" + dateStr + order.getHotelName() + roomType
                    + ",酒店反馈您未全部入住,如不符请致电4008876698";

                // 您预订的X月xx-xx日(入住日期）北京长峰假日酒店（酒店名称）1间（房间数）标准间（房型）,酒店反馈您未入住,如不符请致电4008876698
                // modify by chenjiajie 2009-08-17
                noShow = "您预订的" + dateStr + order.getHotelName() + roomType
                    + ",酒店反馈您未入住,如不符请致电4008876698";



            } else if ("WTT".equals(memberDTO.getState())) {

                confirmString = dateStr + hotelInfoStr + hotelLink + roomType + "已确认";
                if(needArrivalTime){
                    confirmString += "，保留至" + order.getLatestArrivalTime() + "点";
                }

                cancelString = "您预订的" + dateStr + order.getHotelName() + roomType
                    + "已取消,如有需要请再致电116114";

                pauseString = "您预订的" + dateStr + order.getHotelName() + roomType
                    + "正在处理中,我们将在";

                roomCrowdedString = "满房通知:您预订的" + dateStr + order.getHotelName() + roomType
                    + "已满,如需再预订请尽快致电116114";

                changeRoomPriceString = "价格调整通知:您预订的" + dateStr + order.getHotelName() + roomType
                    + "价格有变,请尽快致电116114";

                guaranteeAnnounce = "担保通知:您预订的" + dateStr + order.getHotelName() + roomType
                    + ",房间紧张故需担保,请尽快致电116114";

                prepayAnnounce = "付款通知:您预订的" + dateStr + order.getHotelName() + roomType
                    + ",酒店要求提前付款,请尽快致电116114";

                // 您预订的X月xx-xx日(入住日期）北京长峰假日酒店（酒店名称）,酒店反馈您已提前退房,如不符请致电4008876698
                // modify by chenjiajie 2009-08-17
                checkoutAdvanced = "您预订的" + dateStr + order.getHotelName()
                    + ",酒店反馈您已提前退房,如不符请致电4008876698";

                // 您预订的X月xx-xx日(入住日期）北京长峰假日酒店（酒店名称）1间（房间数）标准间（房型）,酒店反馈您未全部入住,如不符请致电4008876698
                // modify by chenjiajie 2009-08-17
                partCheckin = "您预订的" + dateStr + order.getHotelName() + roomType
                    + ",酒店反馈您未全部入住,如不符请致电4008876698";

                // 您预订的X月xx-xx日(入住日期）北京长峰假日酒店（酒店名称）1间（房间数）标准间（房型）,酒店反馈您未入住,如不符请致电4008876698
                // modify by chenjiajie 2009-08-17
                noShow = "您预订的" + dateStr + order.getHotelName() + roomType
                    + ",酒店反馈您未入住,如不符请致电4008876698";



            } else if ("WTBJ".equals(memberDTO.getState())) {// 北京网通

                confirmString = dateStr + hotelInfoStr + hotelLink + roomType + "已确认";
                if(needArrivalTime){
                    confirmString += "，保留至" + order.getLatestArrivalTime() + "点";
                }

                cancelString = "您预订的" + dateStr + order.getHotelName() + roomType
                    + "已取消,如有需要请再致电114";

                pauseString = "您预订的" + dateStr + order.getHotelName() + roomType
                    + "正在处理中,我们将在";

                roomCrowdedString = "满房通知:您预订的" + dateStr + order.getHotelName() + roomType
                    + "已满,如需再预订请尽快致电114";

                changeRoomPriceString = "价格调整通知:您预订的" + dateStr + order.getHotelName() + roomType
                    + "价格有变,请尽快致电114";

                guaranteeAnnounce = "担保通知:您预订的" + dateStr + order.getHotelName() + roomType
                    + ",房间紧张故需担保,请尽快致电114";

                prepayAnnounce = "付款通知:您预订的" + dateStr + order.getHotelName() + roomType
                    + ",酒店要求提前付款,请尽快致电114";

                // 您预订的X月xx-xx日(入住日期）北京长峰假日酒店（酒店名称）,酒店反馈您已提前退房,如不符请致电4008876698
                // modify by chenjiajie 2009-08-17
                checkoutAdvanced = "您预订的" + dateStr + order.getHotelName()
                    + ",酒店反馈您已提前退房,如不符请致电4008876698";

                // 您预订的X月xx-xx日(入住日期）北京长峰假日酒店（酒店名称）1间（房间数）标准间（房型）,酒店反馈您未全部入住,如不符请致电4008876698
                // modify by chenjiajie 2009-08-17
                partCheckin = "您预订的" + dateStr + order.getHotelName() + roomType
                    + ",酒店反馈您未全部入住,如不符请致电4008876698";

                // 您预订的X月xx-xx日(入住日期）北京长峰假日酒店（酒店名称）1间（房间数）标准间（房型）,酒店反馈您未入住,如不符请致电4008876698
                // modify by chenjiajie 2009-08-17
                noShow = "您预订的" + dateStr + order.getHotelName() + roomType
                    + ",酒店反馈您未入住,如不符请致电4008876698";



            } else if ("NHZY".equals(memberDTO.getState())) { // 南航

                confirmString = dateStr + hotelInfoStr + hotelLink + roomType + "已确认";
                if(needArrivalTime){
                    confirmString += "，保留至" + order.getLatestArrivalTime() + "点";
                }

                cancelString = "您预订的" + dateStr + order.getHotelName() + roomType
                    + "已取消,如有需要请再致电95539";

                pauseString = "您预订的" + dateStr + order.getHotelName() + roomType
                    + "正在处理中,我们将在";

                roomCrowdedString = "满房通知:您预订的" + dateStr + order.getHotelName() + roomType
                    + "已满,如需再预订请尽快致电95539";

                changeRoomPriceString = "价格调整通知:您预订的" + dateStr + order.getHotelName() + roomType
                    + "价格有变,请尽快致电95539";

                guaranteeAnnounce = "担保通知:您预订的" + dateStr + order.getHotelName() + roomType
                    + ",房间紧张故需担保,请尽快致电95539";

                prepayAnnounce = "付款通知:您预订的" + dateStr + order.getHotelName() + roomType
                    + ",酒店要求提前付款,请尽快致电95539";

                // 您预订的X月xx-xx日(入住日期）北京长峰假日酒店（酒店名称）,酒店反馈您已提前退房,如不符请致电4008876698
                // modify by chenjiajie 2009-08-17
                checkoutAdvanced = "您预订的" + dateStr + order.getHotelName()
                    + ",酒店反馈您已提前退房,如不符请致电4008876698";

                // 您预订的X月xx-xx日(入住日期）北京长峰假日酒店（酒店名称）1间（房间数）标准间（房型）,酒店反馈您未全部入住,如不符请致电4008876698
                // modify by chenjiajie 2009-08-17
                partCheckin = "您预订的" + dateStr + order.getHotelName() + roomType
                    + ",酒店反馈您未全部入住,如不符请致电4008876698";

                // 您预订的X月xx-xx日(入住日期）北京长峰假日酒店（酒店名称）1间（房间数）标准间（房型）,酒店反馈您未入住,如不符请致电4008876698
                // modify by chenjiajie 2009-08-17
                noShow = "您预订的" + dateStr + order.getHotelName() + roomType
                    + ",酒店反馈您未入住,如不符请致电4008876698";



            } else {

                confirmString = dateStr + hotelInfoStr + hotelLink + roomType + "已确认";
                if(needArrivalTime){
                    confirmString += "，保留至" + order.getLatestArrivalTime() + "点";
                }

                cancelString = "您预订的" + dateStr + order.getHotelName() + roomType
                    + "已取消,如有需要请再致电114";

                pauseString = "您预订的" + dateStr + order.getHotelName() + roomType
                    + "正在处理中,我们将在";

                roomCrowdedString = "满房通知:您预订的" + dateStr + order.getHotelName() + roomType
                    + "已满,如需再预订请尽快致电114";

                changeRoomPriceString = "价格调整通知:您预订的" + dateStr + order.getHotelName() + roomType
                    + "价格有变,请尽快致电114";

                guaranteeAnnounce = "担保通知:您预订的" + dateStr + order.getHotelName() + roomType
                    + ",房间紧张故需担保,请尽快致电114";

                prepayAnnounce = "付款通知:您预订的" + dateStr + order.getHotelName() + roomType
                    + ",酒店要求提前付款,请尽快致电114";

                // 您预订的X月xx-xx日(入住日期）北京长峰假日酒店（酒店名称）,酒店反馈您已提前退房,如不符请致电4008876698
                // modify by chenjiajie 2009-08-17
                checkoutAdvanced = "您预订的" + dateStr + order.getHotelName()
                    + ",酒店反馈您已提前退房,如不符请致电4008876698";

                // 您预订的X月xx-xx日(入住日期）北京长峰假日酒店（酒店名称）1间（房间数）标准间（房型）,酒店反馈您未全部入住,如不符请致电4008876698
                // modify by chenjiajie 2009-08-17
                partCheckin = "您预订的" + dateStr + order.getHotelName() + roomType
                    + ",酒店反馈您未全部入住,如不符请致电4008876698";

                // 您预订的X月xx-xx日(入住日期）北京长峰假日酒店（酒店名称）1间（房间数）标准间（房型）,酒店反馈您未入住,如不符请致电4008876698
                // modify by chenjiajie 2009-08-17
                noShow = "您预订的" + dateStr + order.getHotelName() + roomType
                    + ",酒店反馈您未入住,如不符请致电4008876698";

                // returnString="您预订的"+dateStr+order.getHotelName()+",据酒店反馈您于_月_日提前退房，如不符请尽快联系114";

            }
        }

        return confirmString;
    }
    
    /**
     * 调用发送短信接口和创建订单客户确认信息发送表信息
     */
    public void sendMessage(String smsText, int smsType, String message,OrOrder order) {
        // 调用发送短信接口
        Long res = sendMessageOrSMS(message, smsText);
        // 创建订单客户确认信息发送表信息
        OrMemberConfirm memberConfirm = getOrMemberConfirm(message, smsType, smsText, res,order);
        order.getMemberConfirmList().add(memberConfirm);
    }
    
    /**
     * 调用发送短信接口 修改了方法的返回值类型为Long modify by chenjiajie V2.7.1 2009-02-17
     */
    public Long sendMessageOrSMS(String phoneNo, String smsText) {
        Sms sms = new Sms();
        sms.setApplicationName("hotel");
        sms.setTo(new String[] { phoneNo });
        sms.setMessage(smsText);
        sms.setFrom("网站");
        return communicaterService.sendSms(sms);
    }
    
    /**
     * 创建订单客户确认信息发送表信息
     */
    public OrMemberConfirm getOrMemberConfirm(String phoneNo, 
        int smsType, String smsText, Long res,OrOrder order) {
        OrMemberConfirm memberConfirm = new OrMemberConfirm();
        memberConfirm.setChannel(ConfirmType.SMS);
        memberConfirm.setModelType(order.getType());// 114还是芒果
        memberConfirm.setType(smsType);
        memberConfirm.setSendTarget(phoneNo);

        memberConfirm.setSendMan("网站系统");
        memberConfirm.setSendManId("网站系统"); // 发送人的工号
        memberConfirm.setSendTime(new Date());
        memberConfirm.setSendSucceed(true);
        memberConfirm.setContent(smsText);

        memberConfirm.setSendStatus(MemberConfirmSmsStutas.SENDING);
        memberConfirm.setUnicallRetId(res);

        memberConfirm.setOrder(order);
        return memberConfirm;
    }
    
    /**
     * 发送客人确认日志
     * 
     */
    private void addMemberConfirmLog(OrOrder order) {
        StringBuffer strCmp = new StringBuffer();
        strCmp.append("订单改为:<font color='red'>" + "已发送客户确认" + "</font>");
        OrHandleLog handleLog = new OrHandleLog();
        handleLog.setModifierName("网站系统");
        handleLog.setModifierRole("网站系统");
        handleLog.setBeforeState(order.getOrderState());
        handleLog.setAfterState(order.getOrderState());
        handleLog.setContent(strCmp.toString());
        handleLog.setModifiedTime(new Date());
        handleLog.setHisNo(order.getHisNo());
        handleLog.setOrder(order);
        order.getLogList().add(handleLog);
        
        //更新order的中台状态
        OrderUtil.updateStayInMid(order);
        
    }
    
    
    /**
     * 处理给客人发送邮件
     * 
     * @return
     */
    public String sendEmailToCusProc(OrOrder order, MemberDTO memberDTO) {
    	
    	//默认为1
        int sendtype = 1;
        String toaddress = order.getEmail();
        //为0 表示 是芒果网 
        String sender = "0";

        Mail mail = new Mail();
        mail.setApplicationName("hotel");
        mail.setTo(new String[] { toaddress });

        String subject = "";
        String templateNo = "";
        String xmlString = "";
        String state = memberDTO.getState();
        HtlHotel hotel = hotelService.findHotel(order.getHotelId().longValue());
  
        if(order.isCooperateOrder()) { // 是否交行等渠道订单 - add by chenkeming       	        	
            if (sendtype == MemberConfirmType.CONFIRM) {
                subject = "酒店预订成功";

                templateNo = FaxEmailModel.JIAOHANG_ORDER_EMAIL_SEND_CONFIRM;
                xmlString = msgAssist.genOrderMailByGuest114Confirm(order, hotel, memberDTO, sender);
            } else {
                subject = "酒店预订已经成功取消";

                templateNo = FaxEmailModel.JIAOHANG_ORDER_EMAIL_SEND_CANCEL;
                xmlString = msgAssist.genOrderMailByGuest114Cancel(order, hotel, memberDTO, sender);
            }
        } else {
            if (order.isMango()) {
            	// add by diandian.hou 2010-8-26
            	if(OrderSource.FAN_TI_NET.equals(order.getSource())){//繁体网站
            		if (sendtype == MemberConfirmType.CONFIRM) {
            			subject = "芒果網酒店預定成功";

                        templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MANGOBOOK_HK_BIGFIVE;
                        xmlString = msgAssist.genOrderMailByGuestMangoConfirm(order, hotel, memberDTO,
                            sender);
                    } else {
                        subject = "芒果網酒店預定已經成功取消";

                        templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MANGOCANCEL_HK_BIGFIVE;
                        xmlString = msgAssist.genOrderMailByGuestMangoCancel(order, hotel, memberDTO,
                            sender);
                    }
            		
            	}
            	else if (OrderSource.FROM_HK.equals(order.getSource())) { // 香港
                    subject = "芒果網酒店預定";
                    if (sendtype == MemberConfirmType.CONFIRM) {
                        subject = "芒果網酒店預定成功";

                        templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MEMO_MANGOBOOK_HK;
                        xmlString = msgAssist.genOrderMailByGuestMangoConfirm(order, hotel, memberDTO,
                            sender);
                    } else {
                        subject = "芒果網酒店預定已經成功取消";

                        templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MEMO_MANGOCANCEL_HK;
                        xmlString = msgAssist.genOrderMailByGuestMangoCancel(order, hotel, memberDTO,
                            sender);
                    }
                    // mail.setFrom("cs@mangocity.com");
                } else {
                    if (sendtype == MemberConfirmType.CONFIRM) {
                        subject = "芒果网酒店预定成功";
                        templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MEMO_MANGOBOOK_HK;

                        xmlString = msgAssist.genOrderMailByGuestMangoConfirm(order, hotel, memberDTO,
                            sender);
                    } else {
                        subject = "芒果网酒店预定已经成功取消";
                        templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MEMO_MANGOCANCEL_HK;

                        xmlString = msgAssist.genOrderMailByGuestMangoCancel(order, hotel, memberDTO,
                            sender);
                    }
                    // mail.setFrom("cs@mangocity.com");
                }
            } else if ("LTT".equals(state)) {
                if (sendtype == MemberConfirmType.CONFIRM) {
                    subject = "联通商旅酒店预定成功";

                    templateNo = FaxEmailModel.UNICOM_ORDER_EMAIL_SEND_CONFIRM;
                    xmlString = msgAssist.genOrderMailByGuest114Confirm(order, hotel, memberDTO, sender);
                } else {
                    subject = "联通商旅酒店预定已经成功取消";

                    templateNo = FaxEmailModel.UNICOM_ORDER_EMAIL_SEND_CANCEL;
                    xmlString = msgAssist.genOrderMailByGuest114Cancel(order, hotel, memberDTO, sender);
                }
                // mail.setFrom("cs@mangocity.com");
            } else if ("WTT".equals(state)) {
                if (sendtype == MemberConfirmType.CONFIRM) {
                    subject = "网通商旅热线酒店预定成功";

                    templateNo = FaxEmailModel.NETCOM_ORDER_EMAIL_SEND_CONFIRM;
                    xmlString = msgAssist.genOrderMailByGuest114Confirm(order, hotel, memberDTO, sender);
                } else {
                    subject = "网通商旅热线酒店预定已经成功取消";

                    templateNo = FaxEmailModel.NETCOM_ORDER_EMAIL_SEND_CANCEL;
                    xmlString = msgAssist.genOrderMailByGuest114Cancel(order, hotel, memberDTO, sender);
                }
                // mail.setFrom("cs@mangocity.com");
            } else if ("WTBJ".equals(state)) {
                if (sendtype == MemberConfirmType.CONFIRM) {
                    subject = "北京网通114电话导航酒店预定成功";

                    templateNo = FaxEmailModel.BJNETCOM_ORDER_EMAIL_SEND_CONFIRM;
                    xmlString = msgAssist.genOrderMailByGuest114Confirm(order, hotel, memberDTO, sender);
                } else {
                    subject = "北京网通114电话导航酒店预定已经成功取消";

                    templateNo = FaxEmailModel.BJNETCOM_ORDER_EMAIL_SEND_CANCEL;
                    xmlString = msgAssist.genOrderMailByGuest114Cancel(order, hotel, memberDTO, sender);
                }
                // mail.setFrom("cs@mangocity.com");
            } else if ("NHZY".equals(state)) {
                if (sendtype == MemberConfirmType.CONFIRM) {
                    subject = "南航酒店预定成功";

                    templateNo = FaxEmailModel.NHZY_ORDER_EMAIL_SEND_CONFIRM;
                    xmlString = msgAssist.genOrderMailByGuest114Confirm(order, hotel, memberDTO, sender);
                } else {
                    subject = "南航酒店预定已经成功取消";

                    templateNo = FaxEmailModel.NHZY_ORDER_EMAIL_SEND_CANCEL;
                    xmlString = msgAssist.genOrderMailByGuest114Cancel(order, hotel, memberDTO, sender);
                }
                // mail.setFrom("cs@mangocity.com");
            } else {
                if (sendtype == MemberConfirmType.CONFIRM) {
                    subject = "114号码百事通酒店预定成功";

                    templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MEMO_114BOOK;
                    xmlString = msgAssist.genOrderMailByGuest114Confirm(order, hotel, memberDTO, sender);
                } else {
                    subject = "114号码百事通酒店预定已经成功取消";

                    templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MEMO_114CANCEL;
                    xmlString = msgAssist.genOrderMailByGuest114Cancel(order, hotel, memberDTO, sender);
                }
                // mail.setFrom("cs@mangocity.com");
            }        	
        }

        mail.setSubject(subject);
        mail.setTemplateFileName(templateNo);
        mail.setXml(xmlString);
        mail.setFrom("cs@mangocity.com");
        mail.setUserLoginId(memberDTO.getMembercd());
        Long ret = null;
        try {
            ret = communicaterService.sendEmail(mail);
        } catch (Exception e) {
            // log.info(e.getMessage());
        	log.error(e.getMessage(),e);
            sendStatus = "2";
            // return super.forwardMsgBox("邮件发送失败！", "refreshSelf()");
        }

        int sendedStatus = MemberConfirmSmsStutas.SENDING; // 发送状态
        // 改变了逻辑:V2.7.1 发送成功或失败都需要记录发送日志 chenjiajie 2009-02-17
        if (null == ret || 0 >= ret) {
            sendStatus = "2";
            sendedStatus = MemberConfirmSmsStutas.FAILED;
        } else {
            sendStatus = "1";
        }
        if (!order.isSendedMemberFax()) {
            order.setSendedMemberFax(true);
        }
        // 已发送客户确认
        boolean bChange = false;
        if (!order.isCustomerConfirm() && sendtype == MemberConfirmType.CONFIRM) {
            order.setCustomerConfirm(true);
            bChange = true;
        }
        if (bChange) {
            addMemberConfirmLog(order);
        }

        OrMemberConfirm memberConfirm = new OrMemberConfirm();
        memberConfirm.setChannel(ConfirmType.EMAIL);
        memberConfirm.setType(sendtype);
        memberConfirm.setModelType(order.isMango() ? ModelType.MODEL_MANGO : ModelType.MODEL_114);
        memberConfirm.setSendTarget(toaddress);
        memberConfirm.setSendMan("网站系统");
        memberConfirm.setSendTime(new Date());
        memberConfirm.setSendSucceed(true);

        /** v2.7.1 RMS 2416订单页面增加短信发送状态显示及实时更新功能 chenjiajie 2009-02-17 begin **/
        memberConfirm.setSendManId("网站系统"); // 发送人的工号
        memberConfirm.setUnicallRetId(ret); // UnitCall返回的流水号
        memberConfirm.setSendStatus(sendedStatus); // 发送状态，初始化为1
        /** v2.7.1 RMS 2416订单页面增加短信发送状态显示及实时更新功能 chenjiajie 2009-02-17 end **/

        memberConfirm.setOrder(order);
        order.getMemberConfirmList().add(memberConfirm);

        // 已发送客户确认
        order.setSendedMemberFax(true);
        
        // v2.8.1 增加操作人 add by chenkeming
        order.setModifier("网站系统");
        order.setModifierName("网站系统");

        //saveOrUpdateOrder(order);
        
        return sendStatus;

    }
    
    
    /**
     * 处理给酒店发送传真
     * 
     * @return
     */
    public String hotelOrderFaxSend(OrOrder order,String faxNum,String tohotelNotes,String faxType, MemberDTO memberDTO) {
    	String sendStatusRes = "2";
    	// 0 表示发送
        String isAnewSend = "0";

        hotelInfo = hotelService.getHotelInfoById(order.getHotelId());

        orderItemGroupByList = orderService.hQueryOrderItemByFaxGroup(order.getID());

        Map modifiedInfo = new HashMap();
        modifiedInfo.put("faxNum", faxNum);
        modifiedInfo.put("tohotelNotes", tohotelNotes);
        modifiedInfo.put("faxType", faxType);
        modifiedInfo.put("city", memberDTO.getMemberstate());

        String sender = "";

        if (memberDTO.isMango()) {
            sender = "1";
        } else {
            if (memberDTO.getMemberstate().toString().equals("LTT")) {// 联通商旅 LTT
                sender = "4";
            } else if (memberDTO.getMemberstate().toString().equals("WTT")) {// 中国网通116114
                // WTT
                sender = "3";
            } else if (memberDTO.getMemberstate().toString().equals("WTBJ")) {// 北京网通114电话导航
                // WTBJ
                sender = "5";
            } else if (memberDTO.getMemberstate().toString().equals("NHZY")) {// 南航专用 NHZY
                sender = "6";
            } else { // 114
                sender = "2";
            }
        }
        modifiedInfo.put("sender", sender);

        Long ret = null;
        Long ID = orderService.getparmsId();

        if (null != faxType) {
        	
            try {
            	
            	UserWrapper  roleUser = new UserWrapper();
            	roleUser.setLoginName(memberDTO.getMembercd());
            	roleUser.setName(memberDTO.getName());
                ret = hotelFaxManager.sendNotifyHotelOrderInfoFax(modifiedInfo, order, ID,
                    null, orderItemGroupByList, isAnewSend, roleUser);
                
                log.info("=======================hotelOrderFaxSend()=======================返回的渠道号实例ID ===ret :"+ret);

                if (null == ret || null == ret.toString() || "".equals(ret.toString()) || 0 >= ret.intValue()) {
                    sendStatusRes = "2";
                    setLogSendHotelFaxOrEmail( order, faxNum, ID,  false,ret,1);
                } else {
                    sendStatusRes = "1";
                    setLogSendHotelFaxOrEmail( order, faxNum, ID,  true,ret,1);
                }
                
            } catch (Exception e) {
            	log.error(e.getMessage(),e);
                // 发送失败处理
                sendStatusRes = "2";
                setLogSendHotelFaxOrEmail( order, faxNum, ID, false,ret,1);
            }

        }
        
        return sendStatusRes;
     
    }
    
    
    //是否满足配额 
    public boolean  quotaOk(OrOrder order){
    	
    	 boolean isConfirmTag = true;
     	
    	 List<OrOrderItem> itemList = new ArrayList<OrOrderItem>();
    	 itemList = order.getOrderItems();
    	 
    	 if(itemList == null ||itemList.isEmpty()){
    		 isConfirmTag =  false;
    	 }else{
    	     for(int i=0;i<itemList.size();i++){
    	    	OrOrderItem orderItem = new OrOrderItem();
				orderItem = itemList.get(i);
				String roomState = orderItem.getRoomState();
				String quotaType = orderItem.getQuotaType();
				if (roomState == null || "".equals(roomState)) {
					isConfirmTag = false;
					break;
				} else {
					String[] room = roomState.split("/");
					Map<String, String> roomStateMap = new HashMap<String, String>();
					if (room.length > 0) {
						for (String bed : room) {
							String[] state = bed.split(":");
							roomStateMap.put(state[0], state[1]);
						}
					}
					int bedType = order.getBedType();
					if (quotaType != null && !quotaType.equals("")) {
						 //如果配额类型为呼出配额且房态不为freeSale，则就不能进行即时确认 modify by zengzhouwu 2011-11-28
						if (Integer.parseInt(quotaType) == 4 && !"0".equals(roomStateMap.get(String.valueOf(bedType)))) {
							isConfirmTag = false;
							break;
						}
					} else {
						isConfirmTag = false;
						break;
					}
				}
    	     }
    	 }
    	 
    	 return isConfirmTag;
    	
    }
    
    //添加发送即时确认传真给酒店的日志  add by shengwei.zuo  2010-6-25
    public void setLogSendHotelFaxOrEmail(OrOrder order,String faxOrEmail,Long ID,boolean okISnot,Long ret,int confirmType){
    	log.info("---添加发送即时确认传真给酒店的日志start----okISnot:"+okISnot+"--ret:"+ret);
    	 // 增加修改日志
         StringBuffer strCmp = new StringBuffer();
    	 
         boolean bChange = false;
         
        	 if(okISnot){
        		 // 发送成功处理
                 if (!order.isSendedHotelFax()) {
                     order.setSendedHotelFax(okISnot);
                 }
                
                 if (order.isHotelConfirmFaxReturn()) {
                     order.setHotelConfirmFaxReturn(false);
                     //OrderUtil.updateStayInMid(order);
                     bChange = true;
                 }
                 
                 if(confirmType==ConfirmType.FAX){
                	 strCmp.append("发送酒店即时确认传真成功!<br>");
                 }else if(confirmType==ConfirmType.EMAIL){ 
                	 strCmp.append("发送酒店即时确认Emai成功！<br>");
                 }	 
        	 }else{
        		 // 发送失败处理
                 order.setSendedHotelFax(okISnot);
               
                 if (order.isHotelConfirmFaxReturn()) {
                     order.setHotelConfirmFaxReturn(false);
                     //OrderUtil.updateStayInMid(order);
                 }
                 if(confirmType==ConfirmType.FAX){
                	 strCmp.append("<font color=red>发送酒店即时确认传真失败！</font><br>");
                 }else if(confirmType==ConfirmType.EMAIL){ 
                	 strCmp.append("<font color=red>发送酒店即时确认Email失败！</font><br>");
                 }	 
        	 }
    	
         
         if (bChange) {
             strCmp.append("订单改为:<font color=red>未收酒店回传</font><br>");
         }
         OrHandleLog handleLog = new OrHandleLog();
         // handleLog.setModifier(new Long(roleUser.getId()));
         handleLog.setModifierName("网站系统");
         handleLog.setModifierRole("网站系统"); // 保存工号
         handleLog.setBeforeState(order.getOrderState());
         handleLog.setAfterState(order.getOrderState());
         handleLog.setContent(strCmp.toString());
         handleLog.setHisNo(order.getHisNo());
         handleLog.setModifiedTime(new Date());
         handleLog.setOrder(order);
         order.getLogList().add(handleLog);
         log.info("---发送即时确认传真给酒店的日志 handleLog---"+handleLog.getBeforeState()+"---"+handleLog.getAfterState()+"---"+handleLog.getContent()+"---"+handleLog.getHisNo()+"---"+handleLog.getModifiedTime());
         OrOrderFax orOrderFax = new OrOrderFax();
         orOrderFax.setBarCode(ID.toString());
         orOrderFax.setHotelId(order.getHotelId());
         orOrderFax.setChannel(confirmType);// 1为传真，2为电邮
         orOrderFax.setType(1);// 1为确认，2为修改，3为取消
         orOrderFax.setModelType(order.getType());// 114还是芒果
         // orOrderFax.setModelNo() 增加一个模版号。
         orOrderFax.setSendTarget(faxOrEmail);
         orOrderFax.setSendMan("网站系统");
         orOrderFax.setSendTime(new Date());
         orOrderFax.setSendSucceed(okISnot);
         orOrderFax.setUnicallRetId("" + ret);
         orOrderFax.setHisNo(order.getHisNo());
         orOrderFax.setOrder(order);
         order.getFaxList().add(orOrderFax);

         // v2.8.1 增加操作人 add by chenkeming
         order.setModifier("网站系统");
         order.setModifierName("网站系统");

         orderService.saveOrUpdate(order);
    	
         log.info("-----发送即时确认传真给酒店的日志orderService.saveOrUpdate success-----");
    }
    
    
    //取出的酒店传真或Email为空或者非法时，写入日志 add by shengwei.zuo 2010-6-25
    public void emailOrFaxErrorLog(OrOrder order,String errorLog){
    	
    	 StringBuffer  buffErrorLog = new  StringBuffer();
    	 buffErrorLog.append("<font color=red>"+errorLog+"</font><br>");
    	
	   	 OrHandleLog handleLog = new OrHandleLog();
	 	
		 handleLog.setModifierName("网站系统");
	     handleLog.setModifierRole("网站系统"); 
	     handleLog.setBeforeState(order.getOrderState());
	     handleLog.setAfterState(order.getOrderState());
	     handleLog.setContent(buffErrorLog.toString());
	     handleLog.setModifiedTime(new Date());
	     handleLog.setHisNo(order.getHisNo());
	     handleLog.setOrder(order);
	     order.getLogList().add(handleLog);
	
	     orderService.saveOrUpdate(order);
    	
    }
    
    
    //取出的酒店传真或Email为空或者非法时，写入日志 add by shengwei.zuo 2010-6-25
    public boolean validateRujiaHotel(Long hotelId){
    	List lstRujia = mangoSalesDao.getRuJiaHotel(hotelId);
    	int countrj = 0;
    	for(Iterator itRj = lstRujia.iterator();itRj.hasNext();){
    		Map itRjMap = (Map)itRj.next();
    		countrj = StringUtil.setStringToInt(itRjMap.get("HOTEL_COUNT").toString());
    	}
    	if(countrj>0){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    
    //预付单，通知结算组 add by shengwei.zuo 2010-3-9
    //public boolean 
    
    /** getter and setter begin */
    public String getCancelString() {
        return cancelString;
    }

    public void setCancelString(String cancelString) {
        this.cancelString = cancelString;
    }

    public String getConfirmString() {
        return confirmString;
    }

    public void setConfirmString(String confirmString) {
        this.confirmString = confirmString;
    }

    public String getNoContactString() {
        return noContactString;
    }

    public void setNoContactString(String noContactString) {
        this.noContactString = noContactString;
    }

    public String getPauseString() {
        return pauseString;
    }

    public void setPauseString(String pauseString) {
        this.pauseString = pauseString;
    }

    public String getReturnString() {
        return returnString;
    }

    public void setReturnString(String returnString) {
        this.returnString = returnString;
    }

    public CommunicaterService getCommunicaterService() {
        return communicaterService;
    }

    public void setCommunicaterService(CommunicaterService communicaterService) {
        this.communicaterService = communicaterService;
    }

//    public String getFaxNum() {
//        return faxNum;
//    }
//
//    public void setFaxNum(String faxNum) {
//        this.faxNum = faxNum;
//    }
//
//    public String getFaxType() {
//        return faxType;
//    }
//
//    public void setFaxType(String faxType) {
//        this.faxType = faxType;
//    }

    public HotelFaxManager getHotelFaxManager() {
        return hotelFaxManager;
    }

    public void setHotelFaxManager(HotelFaxManager hotelFaxManager) {
        this.hotelFaxManager = hotelFaxManager;
    }

    public HotelInfo getHotelInfo() {
        return hotelInfo;
    }

    public void setHotelInfo(HotelInfo hotelInfo) {
        this.hotelInfo = hotelInfo;
    }

    public List getOrderItemGroupByList() {
        return orderItemGroupByList;
    }

    public void setOrderItemGroupByList(List orderItemGroupByList) {
        this.orderItemGroupByList = orderItemGroupByList;
    }

    public OrOrderFaxDao getOrOrderFaxDao() {
        return orOrderFaxDao;
    }

    public void setOrOrderFaxDao(OrOrderFaxDao orOrderFaxDao) {
        this.orOrderFaxDao = orOrderFaxDao;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

//    public String getTohotelNotes() {
//        return tohotelNotes;
//    }
//
//    public void setTohotelNotes(String tohotelNotes) {
//        this.tohotelNotes = tohotelNotes;
//    }

    public IHotelService getHotelService() {
        return hotelService;
    }

    public void setHotelService(IHotelService hotelService) {
        this.hotelService = hotelService;
    }

    public MsgAssist getMsgAssist() {
        return msgAssist;
    }

    public void setMsgAssist(MsgAssist msgAssist) {
        this.msgAssist = msgAssist;
    }

    public Long getSumTo() {
        return sumTo;
    }

    public void setSumTo(Long sumTo) {
        this.sumTo = sumTo;
    }

    public Long getHotelID() {
        return hotelID;
    }

    public void setHotelID(Long hotelID) {
        this.hotelID = hotelID;
    }

    public List getHotelBookSetup() {
        return hotelBookSetup;
    }

    public void setHotelBookSetup(List hotelBookSetup) {
        this.hotelBookSetup = hotelBookSetup;
    }

    public String getIsAnewSend() {
        return isAnewSend;
    }

    public void setIsAnewSend(String isAnewSend) {
        this.isAnewSend = isAnewSend;
    }

    public Long getRoomSumTo() {
        return roomSumTo;
    }

    public void setRoomSumTo(Long roomSumTo) {
        this.roomSumTo = roomSumTo;
    }

    public String getConfirmNo() {
        return confirmNo;
    }

    public void setConfirmNo(String confirmNo) {
        this.confirmNo = confirmNo;
    }

    public double getOrderSum() {
        return orderSum;
    }

    public void setOrderSum(double orderSum) {
        this.orderSum = orderSum;
    }

    public String getRoomCrowdedString() {
        return roomCrowdedString;
    }

    public void setRoomCrowdedString(String roomCrowdedString) {
        this.roomCrowdedString = roomCrowdedString;
    }

    public String getChangeRoomPriceString() {
        return changeRoomPriceString;
    }

    public void setChangeRoomPriceString(String changeRoomPriceString) {
        this.changeRoomPriceString = changeRoomPriceString;
    }

    public String getPrepayAnnounce() {
        return prepayAnnounce;
    }

    public void setPrepayAnnounce(String prepayAnnounce) {
        this.prepayAnnounce = prepayAnnounce;
    }

    public String getGuaranteeAnnounce() {
        return guaranteeAnnounce;
    }

    public void setGuaranteeAnnounce(String guaranteeAnnounce) {
        this.guaranteeAnnounce = guaranteeAnnounce;
    }

    /** getter and setter end */

    public String getCheckoutAdvanced() {
        return checkoutAdvanced;
    }

    public void setCheckoutAdvanced(String checkoutAdvanced) {
        this.checkoutAdvanced = checkoutAdvanced;
    }

    public String getPartCheckin() {
        return partCheckin;
    }

    public void setPartCheckin(String partCheckin) {
        this.partCheckin = partCheckin;
    }

    public String getNoShow() {
        return noShow;
    }

    public void setNoShow(String noShow) {
        this.noShow = noShow;
    }

//    public String getOrderType() {
//        return orderType;
//    }
//
//    public void setOrderType(String orderType) {
//        this.orderType = orderType;
//    }

    public Long getMemberConfirmId() {
        return memberConfirmId;
    }

    public void setMemberConfirmId(Long memberConfirmId) {
        this.memberConfirmId = memberConfirmId;
    }

    public IMemberConfirmService getMemberConfirmService() {
        return memberConfirmService;
    }

    public void setMemberConfirmService(IMemberConfirmService memberConfirmService) {
        this.memberConfirmService = memberConfirmService;
    }

    public int getHotelBookSetupNumber() {
        return hotelBookSetupNumber;
    }

    public void setHotelBookSetupNumber(int hotelBookSetupNumber) {
        this.hotelBookSetupNumber = hotelBookSetupNumber;
    }

	public TranslateUtil getTranslateUtil() {
		return translateUtil;
	}

	public void setTranslateUtil(TranslateUtil translateUtil) {
		this.translateUtil = translateUtil;
	}

	public IOrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(IOrderService orderService) {
		this.orderService = orderService;
	}

	public MemberInterfaceService getMemberInterfaceService() {
		return memberInterfaceService;
	}

	public void setMemberInterfaceService(
			MemberInterfaceService memberInterfaceService) {
		this.memberInterfaceService = memberInterfaceService;
	}

	public MangoSalesDao getMangoSalesDao() {
		return mangoSalesDao;
	}

	public void setMangoSalesDao(MangoSalesDao mangoSalesDao) {
		this.mangoSalesDao = mangoSalesDao;
	}

	public HotelManage getHotelManage() {
		return hotelManage;
	}

	public void setHotelManage(HotelManage hotelManage) {
		this.hotelManage = hotelManage;
	}

	public SupplierInfoService getSupplierInfoService() {
		return supplierInfoService;
	}

	public void setSupplierInfoService(SupplierInfoService supplierInfoService) {
		this.supplierInfoService = supplierInfoService;
	}

	public void setSystemDataService(SystemDataService systemDataService) {
		this.systemDataService = systemDataService;
	}
}

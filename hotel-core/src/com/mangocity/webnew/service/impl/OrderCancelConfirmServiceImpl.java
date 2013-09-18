package com.mangocity.webnew.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.constant.ConfirmType;
import com.mangocity.hotel.order.constant.HotelConfirmType;
import com.mangocity.hotel.order.manager.HotelFaxManager;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderFax;
import com.mangocity.hotel.order.persistence.assistant.OrderUtil;
import com.mangocity.hotel.order.service.HtlOrderStsLogService;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.hotel.order.service.assistant.MsgAssist;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.StringUtil;
import com.mangocity.util.hotel.constant.FaxEmailModel;
import com.mangocity.util.log.MyLog;
import com.mangocity.webnew.service.OrderCancelConfirmService;
import com.mangoctiy.communicateservice.CommunicaterService;
import com.mangoctiy.communicateservice.domain.Mail;

public class OrderCancelConfirmServiceImpl implements OrderCancelConfirmService {
	protected static final MyLog log = MyLog.getLogger(OrderCancelConfirmServiceImpl.class);
	private MsgAssist msgAssist;
	private IOrderService orderService;
	private IHotelService hotelService;
	private CommunicaterService communicaterService;
	private HotelFaxManager hotelFaxManager;
	private HtlOrderStsLogService htlOrderStsLogService;
	/**
	 * 根据酒店确认方式发送传真或email
	 * @param order
	 * @param member
	 * @param roleUser
	 * @return
	 */
	public String orderCancelSend(OrOrder order,MemberDTO member,UserWrapper roleUser){
		
		String ctcttype = hotelService.getHotelSendType(order.getHotelId().toString());
		if(StringUtil.isValidStr(ctcttype) && "02".equals(ctcttype)){
			return this.hotelOrderFaxSend(order, member, roleUser);
		}else if(StringUtil.isValidStr(ctcttype) && "01".equals(ctcttype)){
			return this.hotelOrderEmailSend(order, member, roleUser);
		}
		return null;
	}
	/**
	 * 给酒店发送取消email
	 */
	public String hotelOrderEmailSend(OrOrder order,MemberDTO member,UserWrapper roleUser) {
		String faxNum = hotelService.getHotelFaxNo(order.getHotelId(), order.getChildRoomTypeId());
        Mail mail = new Mail();
        mail.setApplicationName("hotel");
        mail.setTo(new String[] { faxNum });
        //get modifiedInfo
        Map<String,String> modifiedInfo = new HashMap<String,String>(5);
        modifiedInfo.put("faxNum", faxNum);
        modifiedInfo.put("tohotelNotes", "");
        modifiedInfo.put("faxType",String.valueOf(HotelConfirmType.CANCEL));
        if(null!= order.getMemberState()&&"XJG".equals(order.getMemberState())){
        	modifiedInfo.put("city", "");
        }else{
        	modifiedInfo.put("city", member.getMemberstate().toString());
        }
        modifiedInfo.put("sender", getSender(member));
        //get xmlString
        Long ID = orderService.getparmsId();
        String xmlString = msgAssist.genOrderFaxByHotelFaxCancel(order, hotelService.findHotel(order.getHotelId().longValue()), "false", ID,
        		orderService.hQueryOrderItemByFaxGroup(order.getID()), modifiedInfo);
        mail.setSubject("芒果取消单");
        mail.setFrom("cs@mangocity.com");
        mail.setTemplateFileName(FaxEmailModel.NOTIFY_EMAIL_HOTEL_ORDER_CANCEL);
        mail.setXml(xmlString);
        mail.setUserLoginId(roleUser.getLoginName());
        
        long ret = 0L;
        String sendStatus ;
        try {
            ret = communicaterService.sendEmail(mail);
        } catch (RuntimeException e) {
        	log.error("给酒店发送取消email失败！"+e.getMessage());
            sendStatus = "2";
        }

        if (0 >= ret) {
            sendStatus = "2";
        } else {
            sendStatus = "1";
            OrOrderFax orOrderFax = new OrOrderFax();
            orOrderFax.setBarCode(ID.toString());
            orOrderFax.setHotelId(order.getHotelId());
            orOrderFax.setChannel(ConfirmType.EMAIL);// 1为传真，2为电邮，3为短信
            orOrderFax.setType(HotelConfirmType.CANCEL);// 3为取消
            if(null!= member.getState()&&"XJG".equals(member.getState().toString())){
              	 orOrderFax.setModelType(1);// 芒果
              }else{
              	orOrderFax.setModelType(order.getType());// 114还是芒果
             }
            orOrderFax.setSendTarget(faxNum);
            orOrderFax.setSendMan(roleUser.getName());
            orOrderFax.setSendTime(new Date());
            orOrderFax.setSendSucceed(true);
            orOrderFax.setUnicallRetId("" + ret);
            orOrderFax.setHisNo(order.getHisNo());
            orOrderFax.setOrder(order);

            order.getFaxList().add(orOrderFax);
            if (!order.isSendedHotelFax()) {
                order.setSendedHotelFax(true);
            }
            boolean bChange = false;
            if (order.isHotelConfirmFaxReturn()) {
                order.setHotelConfirmFaxReturn(false);
                OrderUtil.updateStayInMid(order);
                bChange = true;
            }
            
                         
            // 增加修改日志
            StringBuffer strCmp = new StringBuffer("发送酒店取消Email<br>");
            if (bChange) {
                strCmp.append("订单改为:<font color=red>未收酒店回传</font><br>");
            }
            OrHandleLog handleLog = new OrHandleLog();
            handleLog.setModifierName(member.getFamilyName()+member.getName());
            handleLog.setModifierRole(roleUser.getLoginName()); // 保存工号
            handleLog.setBeforeState(order.getOrderState());
            handleLog.setAfterState(order.getOrderState());
            handleLog.setContent(strCmp.toString());
            handleLog.setModifiedTime(new Date());
            handleLog.setHisNo(order.getHisNo());
            handleLog.setOrder(order);
            order.getLogList().add(handleLog);

            //增加操作人
            order.setModifier(roleUser.getLoginName());
            order.setModifierName(roleUser.getName());

            orderService.saveOrUpdate(order);
        }
        return sendStatus;//1成功，2失败
	}
	/**
	 * 给酒店发送取消传真
	 */
	public String hotelOrderFaxSend(OrOrder order,MemberDTO member,UserWrapper roleUser) {
		String faxNum = hotelService.getHotelFaxNo(order.getHotelId(), order.getChildRoomTypeId());
		List orderItemGroupByList = orderService.hQueryOrderItemByFaxGroup(order.getID());

        Map<String,String> modifiedInfo = new HashMap<String,String>(5);
        modifiedInfo.put("faxNum", faxNum);
        modifiedInfo.put("tohotelNotes", "");
        modifiedInfo.put("faxType", String.valueOf(HotelConfirmType.CANCEL));
        if(null!= order.getMemberState()&&"XJG".equals(order.getMemberState())){
        	modifiedInfo.put("city", "");
        }else{
        	modifiedInfo.put("city", member.getMemberstate().toString());
        }

        modifiedInfo.put("sender", getSender(member));

        Long ret = null;
        Long ID = orderService.getparmsId();
        String sendStatus;
        try {
            ret = hotelFaxManager.sendNotifyHotelOrderInfoFax(modifiedInfo, order, ID,
                "", orderItemGroupByList, "false", roleUser);
        } catch (Exception e) {
            // 发送失败处理
        	log.error("给酒店发送取消传真失败！"+e.getMessage());
            sendStatus = "2";
        }

        if (null == ret || 0 >= ret) {
            sendStatus = "2";
        } else {
            sendStatus = "1";
            OrOrderFax orOrderFax = new OrOrderFax();
            orOrderFax.setBarCode(ID.toString());
            orOrderFax.setHotelId(order.getHotelId());
            orOrderFax.setChannel(ConfirmType.FAX);
            orOrderFax.setType(HotelConfirmType.CANCEL);//3为取消
            if(null!= member.getState()&&"XJG".equals(member.getState().toString())){
           	 orOrderFax.setModelType(1);// 芒果
           }else{
           	orOrderFax.setModelType(order.getType());// 114还是芒果
           }
            orOrderFax.setSendTarget(faxNum);
            orOrderFax.setSendMan(roleUser.getName());
            orOrderFax.setSendTime(new Date());
            orOrderFax.setSendSucceed(true);
            orOrderFax.setUnicallRetId("" + ret);
            orOrderFax.setHisNo(order.getHisNo());
            orOrderFax.setOrder(order);
            order.getFaxList().add(orOrderFax);

            // 发送成功处理
            if (!order.isSendedHotelFax()) {
                order.setSendedHotelFax(true);
            }
            boolean bChange = false;
            if (order.isHotelConfirmFaxReturn()) {
                order.setHotelConfirmFaxReturn(false);
                OrderUtil.updateStayInMid(order);
                bChange = true;
            }
            

            // 增加修改日志
            StringBuffer strCmp = new StringBuffer("发送酒店取消传真<br>");
            if (bChange) {
                strCmp.append("订单改为:<font color=red>未收酒店回传</font><br>");
            }
            OrHandleLog handleLog = new OrHandleLog();
            handleLog.setModifierName(roleUser.getName());
            handleLog.setModifierRole(roleUser.getLoginName()); // 保存工号
            handleLog.setBeforeState(order.getOrderState());
            handleLog.setAfterState(order.getOrderState());
            handleLog.setContent(strCmp.toString());
            handleLog.setHisNo(order.getHisNo());
            handleLog.setModifiedTime(new Date());
            handleLog.setOrder(order);
            order.getLogList().add(handleLog);

            //操作人
            order.setModifier(roleUser.getLoginName());
            order.setModifierName(roleUser.getName());

            orderService.saveOrUpdate(order);
        }
        return sendStatus;//1成功，2失败
	}

	/**	增加中升集团，用新疆电信的114号代替，修改了新疆典型的MEMBERCD为660009097127
	 *  邮件渠道，传真渠道
	 *  add by xiaowei.wang 2012.02.14*/
	private String getSender(MemberDTO member){
		String sender = "";
		   if (member.isMango()||"XJG".equals(member.getMemberstate().toString())) {
	            sender = "1";
	        } else {
	            if (member.getMemberstate().toString().equals("LTT")) {// 联通商旅 LTT
	                sender = "4";
	            } else if (member.getMemberstate().toString().equals("WTT")) {// 中国网通116114
	                // WTT
	                sender = "3";
	            } else if (member.getMemberstate().toString().equals("WTBJ")) {// 北京网通114电话导航
	                // WTBJ
	                sender = "5";
	            } else if (member.getMemberstate().toString().equals("NHZY")) {// 南航 NHZY
	                sender = "6";
	            } else { // 114
	                sender = "2";
	            }
	        }
		return sender;
	}
	

	public MsgAssist getMsgAssist() {
		return msgAssist;
	}

	public void setMsgAssist(MsgAssist msgAssist) {
		this.msgAssist = msgAssist;
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

	public HotelFaxManager getHotelFaxManager() {
		return hotelFaxManager;
	}

	public void setHotelFaxManager(HotelFaxManager hotelFaxManager) {
		this.hotelFaxManager = hotelFaxManager;
	}

	public HtlOrderStsLogService getHtlOrderStsLogService() {
		return htlOrderStsLogService;
	}

	public void setHtlOrderStsLogService(HtlOrderStsLogService htlOrderStsLogService) {
		this.htlOrderStsLogService = htlOrderStsLogService;
	}
	
}

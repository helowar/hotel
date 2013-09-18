package com.mangocity.hotel.sendmessage.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.ext.member.service.MemberBaseInfoDelegate;
import com.mangocity.hotel.sendmessage.model.HotelOrder;
import com.mangocity.hotel.sendmessage.model.PromotionMessage;
import com.mangocity.hotel.sendmessage.model.PromotionTicket;
import com.mangocity.hotel.sendmessage.model.PromotionTicketHotel;
import com.mangocity.hotel.sendmessage.model.PromotionTicketType;
import com.mangocity.hotel.sendmessage.model.SendPromotionMsgRecord;
import com.mangocity.hotel.sendmessage.service.PromotionMessageService;
import com.mangocity.hotel.sendmessage.service.PromotionTicketHotelService;
import com.mangocity.hotel.sendmessage.service.PromotionTicketService;
import com.mangocity.hotel.sendmessage.service.PromotionTicketTypeService;
import com.mangocity.hotel.sendmessage.service.SendMsgSeqService;
import com.mangocity.hotel.sendmessage.service.SendPromotionMessageService;
import com.mangocity.hotel.sendmessage.service.SendPromotionMsgRecordService;
import com.mangocity.hotel.sendmessage.service.TargetOrderService;
import com.mangocity.tmccommon.service.MemberService;
import com.mangocity.util.DateUtil;
import com.mangocity.webnew.constant.FITAliasConstant;
import com.mangoctiy.communicateservice.CommunicaterService;
import com.mangoctiy.communicateservice.domain.Sms;

public class SendPromotionHkMacServiceImpl implements SendPromotionMessageService {

	private SendMsgSeqService sendMsgSeqService;
	private PromotionMessageService promotionMessageService;
	private TargetOrderService hotelOrderService;
	private PromotionTicketService promotionTicketService;
	private PromotionTicketTypeService promotionTicketTypeService;
	private PromotionTicketHotelService promotionTicketHotelService;
	private SendPromotionMsgRecordService promotionMsgRecordService;
	private CommunicaterService communicaterService;
	private MemberBaseInfoDelegate memberBaseInfoDelegate;
	
	private static Logger logger = Logger.getLogger(SendPromotionHkMacServiceImpl.class);
	
	public Long getJobSendPromotionSeqNextVal() {
				
		return sendMsgSeqService.getNextSeq("seq_htl_promotion_ticket_msg");
	}

	public void sendPromotionMessage() throws Exception {
		StringBuilder sendRecord = new StringBuilder();
		StringBuilder sendPhones = new StringBuilder();
		StringBuilder sendTicketCode = new StringBuilder();
		//PromotionMessage promotionMessage = promotionMessageService.getPromotionMessage();
		PromotionMessage promotionMessage=new PromotionMessage();
		promotionMessage.setBeginDate(DateUtil.stringToDateMain("2012-08-01", "yyyy-MM-dd"));
		promotionMessage.setEndDate(DateUtil.stringToDateMain("2012-09-30", "yyyy-MM-dd"));
		promotionMessage.setFuntionCode("20035");
		promotionMessage.setFuntionRemark("港澳夏日盛会");
		promotionMessage.setStatus(1);
		
		List<SendPromotionMsgRecord> promotionMsgRecordList = new ArrayList<SendPromotionMsgRecord>();
		List<Sms> smsList = new ArrayList<Sms>();
		List<PromotionTicket> usePromotionTicketList = new ArrayList<PromotionTicket>();
		
		if (promotionMessage != null) {//判断促销信息是否为空
			Date now=new Date();
			if (checkNeedSendMessage( now, promotionMessage)) {//判断活动是否已经过期或者发送状态是否已经关闭
				List<HotelOrder> hotelOrderList = hotelOrderService.queryTargetOrder();
				// 过滤掉不用发短信的酒店
				List<HotelOrder> newhotelOrderList = fiterHotelOrder(hotelOrderList,now);

				List<PromotionTicket> promotionTickets = promotionTicketService.queryPromotionTickets();
				List<PromotionTicketType> promotionTicketTypes = promotionTicketTypeService.queryPromotionTicketTypes();

				if (promotionTickets != null && promotionTickets.size() > 0) {//判断是否还有门票
					Map<Integer, List<PromotionTicket>> ticketsMap = transformTicketToMap(promotionTickets, promotionTicketTypes);
					Map<String, PromotionTicketHotel> promotionTicketHotelMap = promotionTicketHotelService.queryPromotionTicketHotelsToMap();

					for (HotelOrder order : newhotelOrderList) {
						PromotionTicketType promotionTicketType = getSendTicketType(order, promotionTicketTypes, promotionTicketHotelMap);
						if (promotionTicketType != null) {
							PromotionTicket sendPromotionTicket = getSendTicketsCode(ticketsMap.get(promotionTicketType.getTicketType()));
							if(sendPromotionTicket!=null){
							sendPromotionTicket.setHasUsed(true);
							usePromotionTicketList.add(sendPromotionTicket);

							promotionMsgRecordList.add(handleSendRecordValue(order, promotionMessage,sendPromotionTicket));

							String smsContent = getSendMessageContent(sendPromotionTicket.getTicketCode(), promotionTicketType.getTypeName());
							smsList.add(handleSmsValue(order.getMobile(), smsContent));

							sendPhones.append(order.getMobile() + ",");
							sendTicketCode.append(sendPromotionTicket.getTicketCode() + ",");
							}
						}
					}

					sendRecord.append("发送的号码：");
					sendRecord.append(sendPhones.toString());
					sendRecord.append("发送的换购票编码 :");
					sendRecord.append(sendTicketCode.toString());

					// 发送短息，必须在保存发送记录之前
					sendSms(smsList, promotionMsgRecordList);
					updatePromotionTicket(usePromotionTicketList);
					saveSendRecord(promotionMsgRecordList);
					
					//将所有不需要用的对象设为空
					ticketsMap=null;
					promotionTicketHotelMap=null;
					promotionTickets=null;
					promotionTicketTypes=null;
					promotionMsgRecordList=null;
					smsList=null;
					usePromotionTicketList =null;
				}

				else {
					sendRecord.append("没有换购的门票了。");
				}

			} else {
				sendRecord.append("活动已经过期或者发送状态已经关闭");
			}

		} else {
			sendRecord.append("获取促销信息xml文件失败");
		}

		logger.info(sendRecord.toString());

	}

	private void sendSms(List<Sms> smsList,List<SendPromotionMsgRecord> promotionMsgRecordList){
		if(smsList!=null && smsList.size()>0){
			for(int i=0;i<smsList.size();i++){
				Sms sms=smsList.get(i);
				
				Long unicallRetId=communicaterService.sendSms(sms);
				SendPromotionMsgRecord record=promotionMsgRecordList.get(i);
				record.setUnicallRetId(unicallRetId);
			}
		}
	}
	
	private void saveSendRecord(List<SendPromotionMsgRecord> promotionMsgRecordList){
		if (promotionMsgRecordList != null && promotionMsgRecordList.size() > 0) {
			promotionMsgRecordService.saveSendPromotionMsgRecords(promotionMsgRecordList);
		}
	}
	
	private void updatePromotionTicket(List<PromotionTicket> promotionTicketList){
		if(promotionTicketList!=null && promotionTicketList.size()>0){
		promotionTicketService.updateBatch(promotionTicketList);
		}
		
	}
	
	/**
	 * 过滤掉不用发送的酒店
	 * @param hotelOrderList
	 * @return
	 * @throws Exception 
	 */
	private List<HotelOrder> fiterHotelOrder(List<HotelOrder> hotelOrderList,Date now) throws Exception{
		List<HotelOrder> newHotelOrderList=new ArrayList<HotelOrder>();
		if(hotelOrderList!=null&&hotelOrderList.size()>=0){
			for(HotelOrder order:hotelOrderList){
				int days=DateUtil.getDay(order.getCheckInDate(), order.getCheckOutDate());
				if(days>=2){
					newHotelOrderList.add(order);
				}
			}
		}
		
		return fiterHasSendedHotelOrder(newHotelOrderList,now);
	}
	
	/**
	 * 过滤掉已经发送过的订单，查询两个小时前的订单，防止流失部分订单不发送，因为更改订单的状态会延后
	 * @throws Exception 
	 */
	private List<HotelOrder> fiterHasSendedHotelOrder(List<HotelOrder> hotelOrderList,Date now) throws Exception{
		List<SendPromotionMsgRecord> records=promotionMsgRecordService.querySendPromotionMsgRecordsByDate(getBeforeOneHour(now), now);
		List<HotelOrder> newHotelOrders=new ArrayList<HotelOrder>();
		if(hotelOrderList!=null &&hotelOrderList.size()>0){
			for(HotelOrder order:hotelOrderList){
				if(!checkHotelOrderIsInSendRecord(order,records)&& checkIsMangoMember(order)){
					newHotelOrders.add(order);
				}
			}
			
		}
		return newHotelOrders;
	}
	
	private boolean checkIsMangoMember(HotelOrder order) throws Exception{
		boolean fag=true;
		//0001397022网站公用会籍
		if(order.getMenberCd()!=null && !"0001397022".equals(order.getMenberCd())){
			MemberDTO member=memberBaseInfoDelegate.getMemberByMemberCd(order.getMenberCd());
			String aliasId = FITAliasConstant.fitAliasObj.get(member.getAliasid());
			fag=(aliasId==null?false:true);
		}	
		return fag;
	}
	
	private boolean checkHotelOrderIsInSendRecord(HotelOrder order,List<SendPromotionMsgRecord> records){
		boolean fag=false;
		if(records!=null&&records.size()>0){
			for(SendPromotionMsgRecord record:records){
				if(record.getOrderCd().equals(order.getOrdercd())){
					fag=true;
					break;
				}				
			}
		}
		return fag;
	}
	
	private Date getBeforeOneHour(Date now){
		Calendar c=Calendar.getInstance();
    	c.setTime(now);
    	c.add(Calendar.HOUR_OF_DAY, -2);
    	Date beforeOneHourDate=new Date(c.getTimeInMillis());
    	return beforeOneHourDate;
	}
	
	/**
	 * 获取换购券编码，随机产生
	 */
	private PromotionTicket getSendTicketsCode(List<PromotionTicket> tickets){
		if(tickets.size()>0){
		Random random=new Random();
		int index=random.nextInt(tickets.size());
		PromotionTicket ticket=tickets.get(index);
		tickets.remove(index);
		return ticket;
		}
		else{
			return null;
		}
	}
	
	/**
	 * 判断发送那种门票，目前有迪士尼乐园门票，海洋公园门票，杜莎夫人蜡像馆门票、昂坪360缆车票
	 */
	private PromotionTicketType getSendTicketType(HotelOrder order,List<PromotionTicketType> promotionTicketTypes,Map<String,PromotionTicketHotel> promotionTicketHotelMap){
		String key=null;
		PromotionTicketType sendType=null;
		for(PromotionTicketType type: promotionTicketTypes){
			key=order.getHotelId()+type.getTicketType()+"";
			if(promotionTicketHotelMap.get(key)!=null){
				sendType=type;
				break;
			}
		}
		return sendType;
	}
	
	
	/**
	 * 判断是否需要发送短信，如果活动过期则不发送短信了，活动时间：8月1日至9月30日
	 */
	private boolean checkNeedSendMessage(Date nowDate,PromotionMessage promotionMessage){

		if (promotionMessage.getStatus() == 0) {
			return false;
		} else {
			
			boolean isInSendDate = DateUtil.between(nowDate, promotionMessage.getBeginDate(), promotionMessage.getEndDate());
			if (isInSendDate) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	/**
	 * 将门票转换成Map，进行分类保存门票
	 * @param tickets
	 * @param types
	 * @return
	 */
	private Map<Integer,List<PromotionTicket>> transformTicketToMap(List<PromotionTicket> tickets,List<PromotionTicketType> types){
		Map<Integer,List<PromotionTicket>> ticketMap=new HashMap<Integer,List<PromotionTicket>>();
		if(types!=null){
			for(PromotionTicketType type:types){
				List<PromotionTicket> ticketsOfMap=new ArrayList<PromotionTicket>();
				int key=type.getTicketType();
				ticketMap.put(key, ticketsOfMap);
				
			}
		}
		if(tickets!=null){
			for(PromotionTicket ticket:tickets){
				for(Integer key:ticketMap.keySet()){
					if(ticket.getTicketType().getTicketType()==key){
						ticketMap.get(key).add(ticket);
						break;
					}
				}
			}
		}
		return ticketMap;
	}

	
	/**
	 * 拼接发送短信的内容，内容如：恭喜您获得门票换购券,编码XXXXXXXXXXX,凭此编码可致电4006640066换取XXX门票一张,兑换截止9月30日。
	 */
	private String getSendMessageContent(String ticketCode,String ticketName){
		StringBuilder content=new StringBuilder();
		content.append("恭喜您获得门票换购券,编码");
		content.append(ticketCode);
		content.append(",凭此编码可致电4006640066换取");
		content.append(ticketName);
		content.append(",兑换截止9月30日。");
		return content.toString();
	}

	private SendPromotionMsgRecord handleSendRecordValue(HotelOrder order,PromotionMessage promotionMessage,PromotionTicket sendPromotionTicket){
		
		SendPromotionMsgRecord sendPromotionMsgRecord=new SendPromotionMsgRecord();
		sendPromotionMsgRecord.setSendMobile(order.getMobile());
		sendPromotionMsgRecord.setFuntionRemark(promotionMessage.getFuntionRemark());
		sendPromotionMsgRecord.setOrderCd(order.getOrdercd());
		sendPromotionMsgRecord.setFuntionCode(promotionMessage.getFuntionCode());
		sendPromotionMsgRecord.setProductOrder("hotel");
		sendPromotionMsgRecord.setTicketCode(sendPromotionTicket.getTicketCode());
		sendPromotionMsgRecord.setCreateDate(new Date());
		return sendPromotionMsgRecord;
	}
	
	/**
	 * 设置短信的对象的值
	 * @param phoneNo
	 * @param messageText
	 * @return
	 */
	private Sms handleSmsValue(String phoneNo,String messageText){
		Sms sms=new Sms();
		sms.setApplicationName("hotel");
		sms.setTo(new String[]{phoneNo});
		sms.setMessage(messageText);
		sms.setFrom("网站");
		return sms;
	}
	public void setSendMsgSeqService(SendMsgSeqService sendMsgSeqService) {
		this.sendMsgSeqService = sendMsgSeqService;
	}

	public void setPromotionMessageService(PromotionMessageService promotionMessageService) {
		this.promotionMessageService = promotionMessageService;
	}

	public void setHotelOrderService(TargetOrderService hotelOrderService) {
		this.hotelOrderService = hotelOrderService;
	}

	public void setPromotionTicketService(PromotionTicketService promotionTicketService) {
		this.promotionTicketService = promotionTicketService;
	}

	public void setPromotionTicketTypeService(PromotionTicketTypeService promotionTicketTypeService) {
		this.promotionTicketTypeService = promotionTicketTypeService;
	}

	public void setPromotionTicketHotelService(PromotionTicketHotelService promotionTicketHotelService) {
		this.promotionTicketHotelService = promotionTicketHotelService;
	}

	public void setPromotionMsgRecordService(SendPromotionMsgRecordService promotionMsgRecordService) {
		this.promotionMsgRecordService = promotionMsgRecordService;
	}

	public void setCommunicaterService(CommunicaterService communicaterService) {
		this.communicaterService = communicaterService;
	}

	public MemberBaseInfoDelegate getMemberBaseInfoDelegate() {
		return memberBaseInfoDelegate;
	}

	public void setMemberBaseInfoDelegate(
			MemberBaseInfoDelegate memberBaseInfoDelegate) {
		this.memberBaseInfoDelegate = memberBaseInfoDelegate;
	}
	
}

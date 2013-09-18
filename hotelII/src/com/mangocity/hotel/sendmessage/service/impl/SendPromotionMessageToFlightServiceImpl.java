package com.mangocity.hotel.sendmessage.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.mangocity.hotel.sendmessage.model.PromotionMessage;
import com.mangocity.hotel.sendmessage.model.SendPromotionMsgRecord;
import com.mangocity.hotel.sendmessage.model.TargetOrder;
import com.mangocity.hotel.sendmessage.service.PromotionMessageService;
import com.mangocity.hotel.sendmessage.service.SendMsgSeqService;
import com.mangocity.hotel.sendmessage.service.SendPromotionMessageService;
import com.mangocity.hotel.sendmessage.service.SendPromotionMsgRecordService;
import com.mangocity.hotel.sendmessage.service.TargetOrderService;
import com.mangocity.util.DateUtil;
import com.mangoctiy.communicateservice.CommunicaterService;
import com.mangoctiy.communicateservice.domain.Sms;

public class SendPromotionMessageToFlightServiceImpl implements SendPromotionMessageService{

	private SendPromotionMsgRecordService promotionMsgRecordService;
	private PromotionMessageService promotionMessageService;
	private CommunicaterService communicaterService;
	private TargetOrderService flightOrderService;
	private SendMsgSeqService sendMsgSeqService ;
	private static Logger logger = Logger.getLogger(SendPromotionMessageToFlightServiceImpl.class);

	
	public void sendPromotionMessage() throws Exception{
	
		StringBuilder sendRecord = new StringBuilder();
		List<String> sendPhoneNos = new ArrayList<String>();
		PromotionMessage promotionMessage = promotionMessageService.getPromotionMessage();
		List<SendPromotionMsgRecord> promotionMsgRecordList = new ArrayList<SendPromotionMsgRecord>();
		if (promotionMessage != null) {
			Date nowDate = new Date();
			if (checkNeedSendMessage(nowDate, promotionMessage)) {
				List<TargetOrder> flightOrderList = getFlightOrderLastestFiveMinute();

				// 过滤掉当天已经发送的手机号码
				List<TargetOrder> sendListflightOrderList = fiterFlightOrder(flightOrderList);

				if (sendListflightOrderList != null && sendListflightOrderList.size() > 0) {
					sendRecord.append("发送短信的号码：");
					for (TargetOrder order : sendListflightOrderList) {
						sendPhoneNos.add(order.getMobile());
						sendRecord.append(order.getMobile());
						sendRecord.append(",");
						promotionMsgRecordList
								.add(handleSendPromotionMessageRecord(order.getOrdercd(), promotionMessage, null, order.getMobile()));
					}

					Long unicallRetId = sendMessage(sendPhoneNos, promotionMessage.getContent());
					sendRecord.append("\n");
					sendRecord.append("发送短信返回的id ：");
					sendRecord.append(unicallRetId.toString());
					sendRecord.append("\n");
					sendRecord.append("活动名称 ：   ");
					sendRecord.append(promotionMessage.getFuntionRemark());
					
					// 将发送的短信返回的id设置进记录里面。
					for (SendPromotionMsgRecord sendPromotionMsgRecord : promotionMsgRecordList) {
						sendPromotionMsgRecord.setUnicallRetId(unicallRetId);
					}
					saveSendRecord(promotionMsgRecordList);

				}
				else{
					sendRecord.append("没有发送短信的号码");
				}
			}
			else{
				sendRecord.append("活动已经过期或者不需要发送促销短信");
			}
			
		}
		else{
			sendRecord.append("没有获取到发送的促销信息");
		}
		logger.info(sendRecord.toString());
	}
	
	public Long getJobSendPromotionSeqNextVal() {
		
		return sendMsgSeqService.getNextSeq("seq_htl_job_sendpromotion");
	}
	
	/**
	 * 剔除重复的预订人，即当日预订联系人手机相同时，只发一次。
	 * @param flightOrderList
	 * @throws Exception 
	 */
	private List<TargetOrder> fiterFlightOrder(List<TargetOrder> flightOrderList) throws Exception{
		SimpleDateFormat sdfOne = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		SimpleDateFormat sdfTwo = new SimpleDateFormat("yyyy/MM/dd");
		String nowDate = sdfTwo.format(new Date());
		String starteDate = nowDate + " 00:00:00";
		String endDate = nowDate + " 23:59:59";
		List<SendPromotionMsgRecord> sendPromotionMsgRecords = promotionMsgRecordService.querySendPromotionMsgRecordsByDate(
				sdfOne.parse(starteDate), sdfOne.parse(endDate));

		if (sendPromotionMsgRecords == null || sendPromotionMsgRecords.size() == 0) {
			return flightOrderList;
		} else {
			ArrayList<TargetOrder> needSendMessageOrder = new ArrayList<TargetOrder>();
			for(int i=0;i<flightOrderList.size();i++){	
				
				boolean fagHadSend=checkMobileHasSend(flightOrderList.get(i), sendPromotionMsgRecords);
				boolean fagDoubleMobile=filterDoubleMobile(flightOrderList.get(i),flightOrderList,i);
				if (!fagHadSend && !fagDoubleMobile) {
					needSendMessageOrder.add(flightOrderList.get(i));
				}
			}
			return needSendMessageOrder;
		}
	}
	
	private boolean filterDoubleMobile(TargetOrder flightOrder,List<TargetOrder> flightOrderList,int index){
		String mobile=flightOrder.getMobile();
		boolean fagDouble=false;
		for(int i=0;i<flightOrderList.size();i++){
			if(mobile.equals(flightOrderList.get(i).getMobile()) && i<index){
				fagDouble=true;
				break;
			}
		}
		return fagDouble;
	}
	
	private boolean checkMobileHasSend(TargetOrder flightOrder,List<SendPromotionMsgRecord> sendPromotionMsgRecords){
		String mobile=flightOrder.getMobile();
		boolean fagHasSend=false;
		for(SendPromotionMsgRecord record:sendPromotionMsgRecords){
			if(mobile.equals(record.getSendMobile())){
				fagHasSend=true;
				break;
			}
		}
		return fagHasSend;
	}
	
	private SendPromotionMsgRecord handleSendPromotionMessageRecord(String orderCd,PromotionMessage promotionMessage,Long unicallRetId,String mobile){
		SendPromotionMsgRecord promotionMsgRecord=new SendPromotionMsgRecord();
		promotionMsgRecord.setOrderCd( orderCd);
		promotionMsgRecord.setFuntionCode(promotionMessage.getFuntionCode());
		promotionMsgRecord.setProductOrder("flight");
		promotionMsgRecord.setUnicallRetId(unicallRetId);
		promotionMsgRecord.setSendMobile(mobile);
		promotionMsgRecord.setFuntionRemark( promotionMessage.getFuntionRemark());
		promotionMsgRecord.setCreateDate(new Date());
		return promotionMsgRecord;
	}
	private List<TargetOrder> getFlightOrderLastestFiveMinute() throws Exception{

		return flightOrderService.queryTargetOrder();
	
	}
	
	private Long sendMessage(List<String> phoneNos, String messageText) {
		String[] phoneNosArray=new String[phoneNos.size()];

		phoneNos.toArray(phoneNosArray);
		Sms sms = new Sms();
		sms.setApplicationName("hotel");
		sms.setTo(phoneNosArray);
		sms.setMessage(messageText);
		sms.setFrom("网站");
		return communicaterService.sendSms(sms);
	}
	
	
	private void saveSendRecord(List<SendPromotionMsgRecord> promotionMsgRecordList){
        if (promotionMsgRecordList != null) {
			promotionMsgRecordService.saveSendPromotionMsgRecords(promotionMsgRecordList);
		}
	}

	
	/**
	 * 如果发送状态为0，则返回false，否则判断当前时间是否在发送日期内，如过在，则返回true
	 */
	private boolean checkNeedSendMessage(Date nowDate,PromotionMessage promotionMessage) {
	
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



	public void setPromotionMsgRecordService(SendPromotionMsgRecordService promotionMsgRecordService) {
		this.promotionMsgRecordService = promotionMsgRecordService;
	}


	public void setPromotionMessageService(PromotionMessageService promotionMessageService) {
		this.promotionMessageService = promotionMessageService;
	}

	public void setCommunicaterService(CommunicaterService communicaterService) {
		this.communicaterService = communicaterService;
	}


	public void setFlightOrderService(TargetOrderService flightOrderService) {
		this.flightOrderService = flightOrderService;
	}

	public void setSendMsgSeqService(SendMsgSeqService sendMsgSeqService) {
		this.sendMsgSeqService = sendMsgSeqService;
	}	
	
	
}

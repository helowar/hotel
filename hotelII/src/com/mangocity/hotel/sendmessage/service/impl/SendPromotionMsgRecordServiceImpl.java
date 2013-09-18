package com.mangocity.hotel.sendmessage.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.sendmessage.dao.SendPromotionMsgRecordDao;
import com.mangocity.hotel.sendmessage.model.SendPromotionMsgRecord;
import com.mangocity.hotel.sendmessage.service.SendPromotionMsgRecordService;

public class SendPromotionMsgRecordServiceImpl implements SendPromotionMsgRecordService {

	private SendPromotionMsgRecordDao sendPromotionMsgRecordDao;
	
	public void saveSendPromotionMsgRecord(SendPromotionMsgRecord promotionMsgRecord) {
		if(promotionMsgRecord!=null){
			sendPromotionMsgRecordDao.saveSendPromotionMsgRecord(promotionMsgRecord);
		}
	}
	

	public void saveSendPromotionMsgRecords(List<SendPromotionMsgRecord> promotionMsgRecords) {
		if(promotionMsgRecords!=null){
			sendPromotionMsgRecordDao.saveSendPromotionMsgRecords(promotionMsgRecords);
		}
		
	}
	

	public List<SendPromotionMsgRecord> querySendPromotionMsgRecordsByDate(Date startDate, Date endDate) {
		if(startDate==null ||endDate==null){
			startDate=new Date();
			endDate=new Date();
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
		return sendPromotionMsgRecordDao.querySendPromotionMsgRecordsByDate(sdf.format(startDate), sdf.format(endDate));
	}


	public void setSendPromotionMsgRecordDao(SendPromotionMsgRecordDao sendPromotionMsgRecordDao) {
		this.sendPromotionMsgRecordDao = sendPromotionMsgRecordDao;
	}


}

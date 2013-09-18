package com.mangocity.hotel.sendmessage.service;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.sendmessage.model.SendPromotionMsgRecord;

/**
 * 
 * @author liting
 *
 */
public interface SendPromotionMsgRecordService {
	/**
	 * 将发生促销信息记录到数据库中
	 * @param promotionMsgRecord
	 */
	public void saveSendPromotionMsgRecord(SendPromotionMsgRecord promotionMsgRecord);
	public void saveSendPromotionMsgRecords(List<SendPromotionMsgRecord> promotionMsgRecords);
	public List<SendPromotionMsgRecord> querySendPromotionMsgRecordsByDate(Date startDate,Date endDate);
}

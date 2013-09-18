package com.mangocity.hotel.sendmessage.dao;

import java.util.List;

import com.mangocity.hotel.sendmessage.model.SendPromotionMsgRecord;

/**
 * 用于促销短息记录表的增删改
 * @author liting
 *
 */
public interface SendPromotionMsgRecordDao {

	/**
	 * 将发生促销信息记录到数据库中
	 * @param promotionMsgRecord
	 */
	public void saveSendPromotionMsgRecord(SendPromotionMsgRecord promotionMsgRecord);
	
	/**
	 * 批量将促销信息记录到数据库中
	 * @param promotionMsgRecords
	 */
	public void saveSendPromotionMsgRecords(List<SendPromotionMsgRecord> promotionMsgRecords);
	
	/**
	 * 根据日期查询促销记录
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<SendPromotionMsgRecord> querySendPromotionMsgRecordsByDate(String startDate,String endDate);
}

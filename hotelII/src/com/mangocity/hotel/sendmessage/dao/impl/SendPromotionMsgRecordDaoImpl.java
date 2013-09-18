package com.mangocity.hotel.sendmessage.dao.impl;

import java.util.List;

import com.mangocity.hotel.sendmessage.dao.SendPromotionMsgRecordDao;
import com.mangocity.hotel.sendmessage.model.SendPromotionMsgRecord;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class SendPromotionMsgRecordDaoImpl extends GenericDAOHibernateImpl implements SendPromotionMsgRecordDao {

	public void saveSendPromotionMsgRecord(SendPromotionMsgRecord promotionMsgRecord) {
		super.saveOrUpdate(promotionMsgRecord);
	}

	public void saveSendPromotionMsgRecords(List<SendPromotionMsgRecord> promotionMsgRecords) {
		super.saveOrUpdateAll(promotionMsgRecords);
		
	}

	public List<SendPromotionMsgRecord> querySendPromotionMsgRecordsByDate(String startDate, String endDate) {
		StringBuilder sql=new StringBuilder();
		sql.append("select hpmr.record_id,hpmr.order_cd,hpmr.funtion_code,hpmr.funtion_remark,hpmr.product_order,hpmr.unicall_ret_id,");
		sql.append(	"hpmr.send_mobile,hpmr.ticket_code,hpmr.createdate from t_htl_promotion_msg_record hpmr ");
		sql.append(" where hpmr.createdate>=to_date(?,'yyyy/MM/dd hh24:mi:ss') and hpmr.createdate<to_date(?,'yyyy/MM/dd hh24:mi:ss')");
		
		return super.queryByNativeSQL(sql.toString(), new Object[]{startDate,endDate}, SendPromotionMsgRecord.class);
	}

}

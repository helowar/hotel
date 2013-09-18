package com.mangocity.hotel.orderrecord.dao.impl;


import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mangocity.hotel.orderrecord.dao.OrderRecordDao;
import com.mangocity.hotel.orderrecord.model.OrderRecord;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class OrderRecordDaoImpl extends GenericDAOHibernateImpl implements OrderRecordDao{
	private static final Logger log = Logger.getLogger(OrderRecordDaoImpl .class);
	public void addOrderRecord(OrderRecord orderRecord) {		
		Session mySession = null;
		Transaction tx=null;
		try {
			mySession = super.getSessionFactory().openSession();
			tx=mySession.beginTransaction();
			mySession.save(orderRecord);
			tx.commit();
		} catch (Exception e) {
			if(tx!=null){
				tx.rollback();
			}
			log.error(" add order record has a wrong ", e);
		} finally {
			if (mySession != null) {
				mySession.flush();
				mySession.clear();
				mySession.close();
			}
		}
		
	}
	
	public Long getActionIdSeq(){
		String sql = "	select  seq_or_order_action.nextval from dual ";
		String[] paraValues = null;
		BigDecimal bActionSeq=(BigDecimal)super.queryByNativeSQL(sql, null,null).get(0);
		
		return bActionSeq.longValue();
	}

	public Long queryActionIdByOrderCd(String orderCd) {
		String sql="select  distinct lh.action_id from l_htl_order_booking_flow lh where lh.ororder_cd=?";
		BigDecimal bActionId=(BigDecimal)super.queryByNativeSQL(sql, null,null).get(0);
		return bActionId.longValue();
	}

	public void updateOrderRecord(OrderRecord orderRecord) {
		Session mySession = null;
		Transaction tx=null;
		try {
			mySession = super.getSessionFactory().openSession();
			tx=mySession.beginTransaction();
			mySession.update(orderRecord);
			tx.commit();			
		} catch (Exception e) {
			if(tx!=null){
				tx.rollback();
			}
			log.error(" update order record has a wrong ", e);
		} finally {
			if (mySession != null) {
				mySession.flush();
				mySession.clear();
				mySession.close();
			}
		}
				
	}
}

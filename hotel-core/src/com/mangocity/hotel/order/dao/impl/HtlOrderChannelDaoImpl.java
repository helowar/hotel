package com.mangocity.hotel.order.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.mangocity.hotel.order.dao.HtlOrderChannelDao;
import com.mangocity.hotel.order.service.assistant.HtlOrderChannel;
import com.mangocity.util.dao.GenericDAOHibernateImpl;
import com.mangocity.util.log.MyLog;

public class HtlOrderChannelDaoImpl extends GenericDAOHibernateImpl implements HtlOrderChannelDao{
   
	private static final MyLog log = MyLog.getLogger(HtlOrderChannelDaoImpl.class);
	
	public void modifyHtlOrderChannel(Long orderId,String channel) {
		Session session = null;
        StringBuffer sql = new StringBuffer();
		try {
			session = super.getSessionFactory().openSession();
			sql.append("update t_htl_order_channel set channel='"+channel+"' where orderId="+orderId);
			session.createSQLQuery(sql.toString()).executeUpdate();
		}catch(Exception e){
			log.error("modifyHtlOrderChannel:orderId="+orderId, e);
		}finally{
			session.flush();
			session.clear();
			session.close();
		}
		
	}

	public HtlOrderChannel queryHtlOrderChannel(long orderId) {
		Session session = null;
        StringBuffer sql = new StringBuffer();
        HtlOrderChannel htlOrderChannel = null;
		try {
			session = super.getSessionFactory().openSession();
			sql.append("select channel,to_char(createTime,'yyyy-MM-dd hh24:mi:ss'),createName from t_htl_order_channel where orderId="+orderId);
			Query query = session.createSQLQuery(sql.toString());
			List<Object []> list = query.list();
			if(list!=null && list.size()>0){
				htlOrderChannel = new HtlOrderChannel();
				htlOrderChannel.setOrderId(orderId);
				htlOrderChannel.setChannel(list.get(0)[0]!=null ? list.get(0)[0].toString() : "");
				htlOrderChannel.setCreateTime(list.get(0)[1]!=null ? list.get(0)[1].toString() : "");
				htlOrderChannel.setCreateName(list.get(0)[2]!=null ? list.get(0)[2].toString() : "");
			}
			
		}catch(Exception e){
			log.error("queryHtlOrderChannel:orderId="+orderId, e);
		}finally{
			session.flush();
			session.clear();
			session.close();
		}
		return htlOrderChannel;
	}

	public void saveHtlOrderChannel(HtlOrderChannel htlOrderChannel) {
		Session session = null;
        StringBuffer sql = new StringBuffer();
		try {
			session = super.getSessionFactory().openSession();
			sql.append("insert into t_htl_order_channel values(");
			sql.append(htlOrderChannel.getOrderId()+",'");
			sql.append(htlOrderChannel.getChannel()+"',sysdate,'");
			sql.append(htlOrderChannel.getCreateName()+"')");
			session.createSQLQuery(sql.toString()).executeUpdate();
		}catch(Exception e){
			log.error("modifyHtlOrderChannel:orderId="+htlOrderChannel.getOrderId(), e);
		}finally{
			session.flush();
			session.clear();
			session.close();
		}
		
		
	}
  
	
}

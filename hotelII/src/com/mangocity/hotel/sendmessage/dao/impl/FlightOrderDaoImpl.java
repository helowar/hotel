package com.mangocity.hotel.sendmessage.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.mangocity.hotel.sendmessage.dao.QueryTargetOrderDao;
import com.mangocity.hotel.sendmessage.handler.AbstractTargetOrderHandler;
import com.mangocity.hotel.sendmessage.model.FlightOrder;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

/**
 * 查询机票订单
 * @author liting
 *
 */
public class FlightOrderDaoImpl extends GenericDAOHibernateImpl implements QueryTargetOrderDao {
	
	@SuppressWarnings("unchecked")
	public List<FlightOrder> queryTargetOrder()throws Exception {
		StringBuilder sql=new StringBuilder();
		sql.append(" select  o.ordercd,l.mobile from t_at_ticketorder o,t_at_linkmaninfo l ");
		sql.append(" where l.ticketorderid=o.id ");
		sql.append(" and o.ordersource = 'WEB'");
		sql.append(" and  o.status in('oComplete','oSubmitIssue','oIssued','oSubmitAuditing','oSubmitConfirm','oSubmitDelivery','oDelivered') ");
		sql.append(" and o.createtime>=sysdate-(5/24/60)");		
		Session session=null;
		try{
			session=super.getSessionFactory().openSession();
			Query query = session.createSQLQuery(sql.toString());
			List<Object[]> flightOrderQuery=query.list();
			
			if(flightOrderQuery==null||flightOrderQuery.size()==0){
				return new ArrayList<FlightOrder>();
			}
			else{
				return handleValue(flightOrderQuery);
			}
		}catch(Exception e){
			throw new Exception(e);
		}
		finally{
			if(session != null){
				session.flush();  
				session.clear();
				session.close();
			}	
		}
		
	}
	
	private List<FlightOrder> handleValue(List<Object[]> flightOrderObjects){
		AbstractTargetOrderHandler handler=new AbstractTargetOrderHandler(){
			
			@SuppressWarnings("unchecked")
			public FlightOrder handleTargetOrderValue(Object[] targetOrder) {
				
				FlightOrder order=new FlightOrder();
				order.setOrdercd((String)targetOrder[0]);
				order.setMobile((String)targetOrder[1]);
				return order;
			}};
			
		return handler.handleTemplete(flightOrderObjects);
		
	}

}

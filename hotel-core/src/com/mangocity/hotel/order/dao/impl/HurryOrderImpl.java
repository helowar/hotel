package com.mangocity.hotel.order.dao.impl;




import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.mangocity.hotel.order.dao.HurryOrderDAO;
import com.mangocity.util.dao.DAOHibernateImpl;

public class HurryOrderImpl extends DAOHibernateImpl implements HurryOrderDAO{

	public Integer modifyHurryOrderNum(Long orderId) {
		StringBuffer sql_1 = new StringBuffer();	
		StringBuffer sql_2 = new StringBuffer();
		
		sql_1.append("select hurryOrderTimes from or_order oo where oo.orderId="+orderId);
		Session session = null;
		Integer hurryOrderTimes = null;
		try {
			session = super.getSessionFactory().openSession();
			Query query = session.createSQLQuery(sql_1.toString());
			
			hurryOrderTimes = (query.uniqueResult()!=null ? Integer.parseInt(query.uniqueResult().toString())+1 : 1);
			
			sql_2.append("update ");
			sql_2.append("or_order oo set ");
			sql_2.append("hurryOrderTimes="+hurryOrderTimes+" ");
			sql_2.append("where oo.orderId="+orderId);
			
			session.createSQLQuery(sql_2.toString()).executeUpdate();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			session.flush();
			session.clear();
			session.close();
		}
	    
		
		return hurryOrderTimes;
	}

	public Integer queryHurryOrderNum(Long orderId) {
		Session session = null;
		Integer hurryOrderTimes = null;
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select hurryOrderTimes from or_order oo where oo.orderId="+orderId);
			session = super.getSessionFactory().openSession();
			Query query = session.createSQLQuery(sql.toString());
			
			hurryOrderTimes = (query.uniqueResult()!=null ? Integer.parseInt(query.uniqueResult().toString()) : 0);
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			session.flush();
			session.clear();
			session.close();
		}
		
		return hurryOrderTimes;
	}

	public void cleanHurryTimes(Long orderId) {
		// TODO Auto-generated method stub
		Session session = null;
		
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("update or_order oo set hurryOrderTimes=0 where oo.orderId="+orderId);
			session = super.getSessionFactory().openSession();
			Query query = session.createSQLQuery(sql.toString());
			query.executeUpdate();
			
			
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			session.flush();
			session.clear();
			session.close();
		}
		
	}
	
	
	
	

	
	
	
}

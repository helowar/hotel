package com.mangocity.hotel.order.dao.impl;




import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.mangocity.hotel.order.dao.OrChannelNoDAO;
import com.mangocity.hotel.order.persistence.OrChannelNo;
import com.mangocity.util.dao.DAOHibernateImpl;

public class OrChannelNoImpl extends DAOHibernateImpl implements OrChannelNoDAO{

    
	public void updateOrChannelNo(OrChannelNo orChannelNo) {
		// TODO Auto-generated method stub
		Session session = null;
		
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("update OR_ChannelNo oc set oc.status="+orChannelNo.getStatus()+" where oc.id="+orChannelNo.getID());
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

package com.mangocity.hotel.order.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.mangocity.hotel.base.persistence.TempOrder;
import com.mangocity.hotel.order.dao.TempOrderDao;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.log.MyLog;

public class TempOrderDaoImpl extends DAOHibernateImpl implements TempOrderDao {
	private static final MyLog log = MyLog.getLogger(TempOrderDaoImpl.class);
	
	public void insertTempOrder(TempOrder tempOrder) {
		if(null == tempOrder){
			return;
		}
		Session session = null;		
		StringBuffer sql=null;
		try {  
			session = super.getSessionFactory().openSession();
			sql = new StringBuffer();
		    sql.append("insert into temp_order (orderid, assigntime, grouptype, assignstate, relaxtime, clienttype)values (?,sysdate,?,?,?,?)");
		    Query query = session.createSQLQuery(sql.toString());
		    query.setLong(0, tempOrder.getOrderid());
		    query.setLong(1, tempOrder.getGrouptype());
		    query.setLong(2, tempOrder.getAssignstate());
		    query.setLong(3, tempOrder.getRelaxtime());
		    query.setInteger(4, tempOrder.getClienttype());
		    query.executeUpdate();
		} catch (Exception e) {
			log.error("插入temp_order记录orderId="+tempOrder.getOrderid()+"出错:", e);
		}finally{
			session.flush();
			session.clear();
			session.close();
		}
	}

	public void updateTempOrder(Long orderId, Long groupType) {
		Session session = null;		
		StringBuffer sql=null;
		try {  
			session = super.getSessionFactory().openSession();
			sql = new StringBuffer();
		    sql.append("update temp_order set assigntime=sysdate ,grouptype = ? where orderid = ?");
		    Query query = session.createSQLQuery(sql.toString());
		    query.setLong(0, groupType);
		    query.setLong(1, orderId);
		    query.executeUpdate();
		} catch (Exception e) {
			log.error("更新temp_order记录orderId="+orderId+"出错:", e);
		}finally{
			session.flush();
			session.clear();
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	public TempOrder queryTempOrder(Long orderId) {
		Session session = null;		
		StringBuffer sql=null;
		TempOrder tempOrer = null;
		try {  
			session = super.getSessionFactory().openSession();
			sql = new StringBuffer();
		    sql.append("select orderid orderid, assigntime assigntime, grouptype grouptype,assignstate assignstate, relaxtime relaxtime, clienttype clienttype ");
		    sql.append("from temp_order where orderid = ?");
		    Query query = session.createSQLQuery(sql.toString());
		    query.setLong(0, orderId);
		    List<Object[]> objList = query.list();
		    if(null != objList){
		    	tempOrer = new TempOrder();
		    	Object[] obj = objList.get(0);
		    	tempOrer.setOrderid(obj[0]!=null ? Long.parseLong(obj[0].toString()) : 0L);
				tempOrer.setGrouptype(obj[2]!=null ? Long.parseLong(obj[2].toString()) : 0L);
				tempOrer.setAssignstate(obj[3]!=null ? Long.parseLong(obj[3].toString()) : 0L);
				tempOrer.setRelaxtime(obj[4]!=null ? Long.parseLong(obj[4].toString()) : 0L);
				tempOrer.setClienttype(obj[5]!=null ? Integer.parseInt(obj[5].toString()) : 0);
		    }
		} catch (Exception e) {
			log.error("更新temp_order记录orderId="+orderId+"出错:", e);
		}finally{
			session.flush();
			session.clear();
			session.close();
		}

		return tempOrer;
	}

}

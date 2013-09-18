package com.mangocity.hotel.base.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.mangocity.hotel.base.dao.EpOrderDAO;
import com.mangocity.util.dao.GenericDAOHibernateImpl;
import com.mangocity.util.log.MyLog;

public class EpOrderDAOImpl extends GenericDAOHibernateImpl implements EpOrderDAO{

	private static final MyLog log = MyLog.getLogger(EpOrderDAOImpl.class);
	
	public List<String> queryEpHotelId() {
		Session session = null;		
		StringBuffer sql=null;
		List<String> list = new ArrayList<String>();
		try {
			session = super.getSessionFactory().openSession();
			
			sql = new StringBuffer();
			sql.append("select distinct a.hotel_id ");
			sql.append("from V_HTL_HOTEL a ");
			sql.append("where a.active = '1' ");
			sql.append("and a.HOTEL_SYSTEM_SIGN = '01' ");
			sql.append("and exists ");
			sql.append("(select 'x' from HTL_EBOOKING e ");
			sql.append("where e.WHETHEREBOOKING = 1 ");
			sql.append("and e.HOTEL_ID = a.HOTEL_ID ");
			sql.append(") ");
			//log.info("=========["+sql.toString()+"]==========");
			Query query = session.createSQLQuery(sql.toString());
			List<Object> tempList = query.list();
			for(Object obj : tempList){
				list.add(obj!=null ? String.valueOf(obj) : "");
			}
			//log.info("=========["+list+"]==========");
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}finally{
			session.flush();
			session.clear();
			session.close();
		}
								
		return list;
	}

	public void saveEpOrderData(List<String> hotelList) {
		
		Session session = null;		
		StringBuffer sql=null;
		try {
			session = super.getSessionFactory().openSession();
			//log.info("============saveEpOrderData=============");
			
			for(Object hotelId : hotelList){
				//log.info("============hotelId="+hotelId);
				sql = new StringBuffer();
				sql.append("insert into t_htl_ep_order ");
				sql.append("select SEQ_t_htl_ep_order.Nextval, ");
				sql.append("oo.orderid, ");
				sql.append("oo.hotelid, ");
				sql.append("oo.hotelname, ");
				sql.append("oo.roomtypeid, ");
				sql.append("oo.childroomtypeid, ");
				sql.append("oo.ordercd, ");
				sql.append("oo.fellownames, ");
				sql.append("oo.checkindate, ");
				sql.append("oo.checkoutdate, ");
				sql.append("oo.roomtypename, ");
				sql.append("oo.roomquantity, ");
				sql.append("oo.bedtype, ");
				sql.append("oo.arrivaltime, ");
				sql.append("oo.latestarrivaltime, ");
				sql.append("oo.sumrmb, ");
				sql.append("oo.paymethod, ");
				sql.append("oo.iscreditassured, ");
				sql.append("oo.specialrequest, ");
				sql.append("0, ");
				sql.append("0, ");
				sql.append("null, ");
				sql.append("null, ");
				sql.append("sysdate, ");
				sql.append("null, ");
				sql.append("null, ");
				sql.append("null, ");
				sql.append("null, ");
				sql.append("oo.paymentcurrency, ");
				sql.append("null, ");
				sql.append("null, ");
				sql.append("null, ");
				sql.append("null ");
				sql.append("from or_order oo ");
				sql.append("where oo.channel!='9' and oo.orderstate<14 ");
				sql.append("and oo.hotelid = ");
				sql.append(String.valueOf(hotelId)+" ");				
				//sql.append("and oo.sendedHotelFax = '0' ");
				sql.append("and oo.illusive = 0 ");						
				sql.append("and oo.orderCDHotel = oo.orderCd ");
				
				sql.append(" and not exists(select 1 from t_htl_order_channel t where t.orderid=oo.orderid and t.channel=9)");
				sql.append(" and ( (oo.paymethod = 'pay' and oo.iscreditassured = 0) or (oo.paymethod = 'pay' and oo.iscreditassured = 1 and oo.suretystate =4))");						
				sql.append("and oo.isstayinmid = '1' ");						
				sql.append("and not exists (select 'x' from t_htl_ep_order tr where tr.orderid=oo.orderid) ");	
				//sql.append("and oo.createdate >= (sysdate - 3) ");
				sql.append("and oo.createdate >= (sysdate - 5 / 24) ");
				//log.info(sql.toString());
				Query query = session.createSQLQuery(sql.toString());
				//System.out.println(sql.toString());
				int i =query.executeUpdate();
			}
			
		} catch (Exception e) {
			log.error("===========saveEpOrderData error!",e);
			throw new RuntimeException(e);
		}finally{
			session.flush();
			session.clear();
			session.close();
		}
	}
   
	
	
}

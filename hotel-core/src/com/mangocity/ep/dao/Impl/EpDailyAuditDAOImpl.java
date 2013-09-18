package com.mangocity.ep.dao.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mangocity.ep.dao.EpDailyAuditDAO;
import com.mangocity.ep.entity.AuditOrder;
import com.mangocity.ep.entity.AuditOrderItem;
import com.mangocity.ep.entity.OrderParam;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.log.MyLog;

public class EpDailyAuditDAOImpl extends DAOHibernateImpl implements EpDailyAuditDAO{
     
	private static final MyLog log = MyLog.getLogger(EpDailyAuditDAOImpl.class);
	
	
	
	public List<String> queryEpHotelId() {
		Session session = null;		
		StringBuffer sql=null;
		Transaction ts=null;
		List<String> list = new ArrayList<String>();
		try {
			session = super.getSessionFactory().openSession();
		    ts=session.beginTransaction(); 
			sql = new StringBuffer();
			sql.append("select a.hotel_id ");
			sql.append("from V_HTL_HOTEL a ");
			sql.append("where a.active = '1' ");
			sql.append("and a.HOTEL_SYSTEM_SIGN = '01' ");
			sql.append("and exists ");
			sql.append("(select 'x' from HTL_EBOOKING e ");
			sql.append("where e.WHETHEREBOOKING = 1 ");
			sql.append("and e.HOTEL_ID = a.HOTEL_ID ");
			sql.append(") for update skip locked ");
			//log.info("=========["+sql.toString()+"]==========");
			Query query = session.createSQLQuery(sql.toString());
			List<Object> tempList = query.list();
			if(tempList!=null && tempList.size()>0){
				for(Object obj : tempList){
					list.add(obj!=null ? String.valueOf(obj) : "");
				}
			}
			//log.info("=========["+list+"]==========");
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}finally{
			session.flush();
			session.clear();
			if(list.size()>0){
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {					
					e.printStackTrace();
				}
				ts.commit();
				session.close();
			}else{
				ts.commit();
				session.close();
			}
			
		}
								
		return list;
	}

	public void saveAuditOrder() {
		Session session = null;		
		StringBuffer sql=null;
		try {
			session = super.getSessionFactory().openSession();
			sql = new StringBuffer();
			sql.append("insert into t_htl_ep_dailyaudit ");
			sql.append("select SEQ_t_htl_ep_dailyaudit.Nextval,");
			sql.append("M.ORDERID,");
			sql.append("M.HOTELID,");
			sql.append("M.ORDERCD,M.CHECKINTIME,");
			sql.append("M.CHECKOUTTIME,");
			sql.append("trunc(SYSDATE),");
			sql.append("M.ROOMAMOUNT,");
			sql.append("M.ROOMID,");
			sql.append("M.ROOMNAME,");
			sql.append("to_number(M.AUDITTYPE)-1,");
			sql.append("0,");
			sql.append("0,");
			sql.append("null,");
			sql.append("null,");
			sql.append("null,");
			sql.append("null,");
			sql.append("sysdate, ");
			sql.append("null ");
			sql.append("from DA_DAILYAUDIT_ITEM M,");
			sql.append("(select distinct d.DAILYAUDIT_ID ");
			sql.append("from DA_DAILYAUDIT d, htl_el_auditpool h ");
			sql.append("where d.CHANNELID is not null ");
			sql.append("and d.dailyaudit_id = h.da_dailyauditid(+) ");
			sql.append("AND d.AUDITDATE = trunc(sysdate) - 1 ");
			sql.append("AND d.STATE = 0) T, ");
			sql.append("(select distinct a.hotel_id ");
			sql.append("from V_HTL_HOTEL a ");
			sql.append("where a.active = '1' ");
			sql.append("and a.HOTEL_SYSTEM_SIGN = '01' ");
			sql.append("and exists (select 'x' ");
			sql.append("from HTL_EBOOKING e ");
			sql.append("where e.WHETHEREBOOKING = 1 ");
			sql.append("and e.HOTEL_ID = a.HOTEL_ID)) V ");
			sql.append("where M.DAILYAUDITID = T.DAILYAUDIT_ID ");
			sql.append("AND M.HOTELID=V.HOTEL_ID ");
			log.info(sql.toString());
			Query query = session.createSQLQuery(sql.toString());
			query.executeUpdate();
			deleteRepeatAuditOrder();
		} catch (Exception e) {
			log.error(sql.toString());
			e.printStackTrace();
			log.error(e.getMessage());
		}finally{
			session.flush();
			session.clear();
			session.close();
		}
	}
	
	public void saveAuditOrderItem() {
		Session session = null;		
		StringBuffer sql=null;
		try {
			session = super.getSessionFactory().openSession();
			sql = new StringBuffer();		 
			sql.append("insert into t_htl_ep_audit_item ");
			sql.append("select SEQ_t_htl_ep_dailyaudit_item.Nextval,");
			sql.append("P.EP_DAILYAUDIT_ID,");    
			sql.append("S.checkinname,");
			sql.append("S.actualcheckinname,");
			sql.append("S.checkintime,");
			sql.append("S.checkouttime,");
			sql.append("S.roomid,");
			sql.append("S.roomname,");
			sql.append("S.roomnumber,");
			sql.append("S.affirmnumber,");
			sql.append("NULL,");
			sql.append("NULL,");
			sql.append("NULL,");
			sql.append("NULL,");
			sql.append("NULL,");
			sql.append("SYSDATE, ");
			sql.append("dailyaudit_item_subtable_id, ");
			sql.append("null ");
			sql.append("from DA_DAILYAUDIT_ITEM_SUBTABLE S,");
			sql.append("(select distinct D.DAILYAUDIT_ITEM_ID, T.EP_DAILYAUDIT_ID ");
			sql.append("from t_htl_ep_dailyaudit T, DA_DAILYAUDIT_ITEM D ");
			sql.append("where T.ORDERID = D.ORDERID ");
			sql.append("and D.DAILYAUDITID in(select dailyaudit_id from DA_DAILYAUDIT dt where dt.auditdate=trunc(sysdate)-1) ");
			sql.append("AND T.CHECKINTIME = D.CHECKINTIME) P ");
			sql.append("WHERE S.dailyaudit_item_id = P.DAILYAUDIT_ITEM_ID ");
			log.info(sql.toString());
			Query query = session.createSQLQuery(sql.toString());
			query.executeUpdate();
			deleteRepeatAuditOrderItem();
		} catch (Exception e) {
			log.error(sql.toString());
			e.printStackTrace();
			log.error(e.getMessage());
		}finally{
			session.flush();
			session.clear();
			session.close();
		}
	}
	
	
	public Map<String,String> queryHotelIdByDaliyAuditId(String ids) {
		StringBuffer sql=null;
		Session session = null;		
		Map<String,String> map= new HashMap<String,String>();
		try {
			session = super.getSessionFactory().openSession();
			sql = new StringBuffer();		 
			sql.append("select distinct hotelId,DAILYAUDITID from DA_DAILYAUDIT_ITEM where DAILYAUDITID in( ");
			sql.append(ids);
			sql.append(" )");
			log.info(sql.toString());
			Query query = session.createSQLQuery(sql.toString());
			List<Object[]> tempList = query.list();
			for(Object[] obj : tempList){
				map.put(obj[1]!=null ? String.valueOf(obj[1]) : "", obj[0]!=null ? String.valueOf(obj[0]) : "");
			}
		} catch (Exception e) {
			log.error(sql.toString());
			e.printStackTrace();
			log.error(e.getMessage());
		}finally{
			session.flush();
			session.clear();
			session.close();
		}
		return map;
	}
	
	public void deleteRepeatAuditOrder() {
		Session session = null;		
		StringBuffer sql=null;

		try {
			session = super.getSessionFactory().openSession();
			sql = new StringBuffer();
			sql.append("delete from t_htl_ep_dailyaudit th ");
			sql.append(" where th.ep_dailyaudit_id in (select ep_dailyaudit_id ");
			sql.append("from (select t.ep_dailyaudit_id,");
			sql.append("row_number() over(partition by ordercd,audittype order by t.createtime) rn ");
			sql.append("from t_htl_ep_dailyaudit t where createtime>trunc(sysdate)-50 ) p ");
			sql.append("where rn > 1)");
			Query query = session.createSQLQuery(sql.toString());
			query.executeUpdate();		
		} catch (Exception e) {
			log.error(sql.toString());
			e.printStackTrace();
			log.error(e.getMessage());
		}finally{
			session.flush();
			session.clear();
			session.close();
		}
	}
    
	public void deleteRepeatAuditOrderItem() {
		Session session = null;		
		StringBuffer sql=null;

		try {
			deleteInvalidEpAuditItem();
			session = super.getSessionFactory().openSession();
			sql = new StringBuffer();
			sql.append("delete from t_htl_ep_audit_item ");
			sql.append(" where ep_audit_item_id in (select ep_audit_item_id ");
			sql.append("from (select ep_audit_item_id,");
			sql.append("row_number() over(partition by old_dailyItem_id order by EP_DAILYAUDIT_ID desc,createtime) rn ");
			sql.append("from t_htl_ep_audit_item where createtime>trunc(sysdate)-50 ) ");
			sql.append("where rn > 1) ");
			Query query = session.createSQLQuery(sql.toString());
			query.executeUpdate();		
		} catch (Exception e) {
			log.error(sql.toString());
			e.printStackTrace();
			log.error(e.getMessage());
		}finally{
			session.flush();
			session.clear();
			session.close();
		}
		
	}
	
	
	public void deleteInvalidEpAuditItem(){
		Session session = null;		
		StringBuffer sql=null;

		try {
			session = super.getSessionFactory().openSession();
			sql = new StringBuffer();
			sql.append("delete from t_htl_ep_audit_item ti ");
			sql.append(" where not exists ");
			sql.append("(select 'X' ");
			sql.append("from t_htl_ep_dailyaudit tt ");
			sql.append("where tt.ep_dailyaudit_id = ti.ep_dailyaudit_id) ");
			Query query = session.createSQLQuery(sql.toString());
			query.executeUpdate();		
		} catch (Exception e) {
			log.error(sql.toString());
			e.printStackTrace();
			log.error(e.getMessage());
		}finally{
			session.flush();
			session.clear();
			session.close();
		}
	}
	
	public List<AuditOrderItem> queryAuditItemById(Long epDailyauditId) {
		Session session = null;		
		StringBuffer sql=null;
		List<AuditOrderItem> itemList = new ArrayList<AuditOrderItem>();
		try {  
			session = super.getSessionFactory().openSession();			
			sql = new StringBuffer();
			sql.append("select ep_audit_item_id,");
			sql.append("ep_dailyaudit_id,");
			sql.append("checkinname,");
			sql.append("actualcheckinname,");
			sql.append("checkintime,");
			sql.append("checkouttime,");
			sql.append("roomid,");
			sql.append("roomname,");
			sql.append("roomnumber,");
			sql.append("affirmnumber,");
			sql.append("auditresults,");
			sql.append("hotel_modifytime,");
			sql.append("hotel_modifyname,");
			sql.append("cc_modifytime,");
			sql.append("cc_modifyname,");
			sql.append("createtime, ");
			sql.append("attachauditdate ");
			sql.append("from t_htl_ep_audit_item t ");
			sql.append("where t.ep_dailyaudit_id =");
			sql.append(epDailyauditId);			
			Query query = session.createSQLQuery(sql.toString());
			log.info(sql.toString());
            List<Object[]> list = query.list();
            
            if(list!=null && list.size()>0){
            	for(Object[] obj : list){
            		AuditOrderItem item = new AuditOrderItem();
            		item.setEpAuditItemId(obj[0]!=null ? Long.parseLong(String.valueOf(obj[0])) : null);
            		item.setEpDailyauditId(obj[1]!=null ? Long.parseLong(String.valueOf(obj[1])) : null);
            		item.setCheckinname(obj[2]!=null ? String.valueOf(obj[2]) : "");
            		item.setActualcheckinname(obj[3]!=null ? String.valueOf(obj[3]) : "");
            		item.setCheckintime(obj[4]!=null ? String.valueOf(obj[4]) : "");
            		item.setCheckouttime(obj[5]!=null ? String.valueOf(obj[5]) : "");
            		item.setRoomid(obj[6]!=null ? String.valueOf(obj[6]) : "");
            		item.setRoomname(obj[7]!=null ? String.valueOf(obj[7]) : "");
            		item.setRoomnumber(obj[8]!=null ? String.valueOf(obj[8]) : "");
            		item.setAffirmnumber(obj[9]!=null ? String.valueOf(obj[9]) : "");
            		item.setAuditresults(obj[10]!=null ? String.valueOf(obj[10]) : "");
            		item.setHotelModifytime(obj[11]!=null ? String.valueOf(obj[11]) : "");
            		item.setHotelModifyname(obj[12]!=null ? String.valueOf(obj[12]) : "");
            		item.setCcModifytime(obj[13]!=null ? String.valueOf(obj[13]) : "");
            		item.setCcModifyname(obj[14]!=null ? String.valueOf(obj[14]) : "");
            		item.setCreatetime(obj[15]!=null ? String.valueOf(obj[15]) : "");
            		item.setAttachAuditDate(obj[16]!=null ? String.valueOf(obj[16]) : "");
            		itemList.add(item);
            	}
            }
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			session.flush();
			session.clear();
			session.close();
		}
		return itemList;
	}
	
	public List<AuditOrder> queryEpOrderAuditData(OrderParam param) {
		 Session session = null;		
			StringBuffer sql=null;
			List<AuditOrder> orderList = new ArrayList<AuditOrder>();
			try {  
				session = super.getSessionFactory().openSession();			
				sql = new StringBuffer();
	            sql.append("select ep_dailyaudit_id,orderid,hotelid,ordercd,checkintime,checkouttime,auditdate,roommount,");
	            sql.append("roomid,roomname,audittype,hotel_state,cc_state,hotel_modifytime,hotel_modifyname,");
	            sql.append("cc_modifytime,cc_modifyname,createtime,chn_name,remark ");
	            sql.append("from (select ep_dailyaudit_id,orderid,hotelid,ordercd,checkintime,");
	            sql.append("checkouttime,auditdate,roommount,roomid,roomname,audittype,");
	            sql.append("hotel_state,cc_state,hotel_modifytime,hotel_modifyname,cc_modifytime,");
	            sql.append("cc_modifyname,createtime,chn_name,remark,rownum rn ");
	            sql.append("from (select td.ep_dailyaudit_id,");
	            sql.append("td.orderid,");
	            sql.append("td.hotelid,");
	            sql.append("td.ordercd,");
	            sql.append("td.checkintime,");
	            sql.append("td.checkouttime,");
	            sql.append("td.auditdate,");
	            sql.append("td.roommount,");
	            sql.append("td.roomid,");
	            sql.append("td.roomname,");
	            sql.append("td.audittype,");
	            sql.append("td.hotel_state,");
	            sql.append("td.cc_state,");
	            sql.append("to_char(td.hotel_modifytime,'yyyy-MM-dd hh24:mi:ss') hotel_modifytime,");
	            sql.append("td.hotel_modifyname,");
	            sql.append("td.cc_modifytime,");
	            sql.append("td.cc_modifyname,");
	            sql.append("td.createtime, ");
	            sql.append("hh.chn_name, ");
	            sql.append("td.remark ");
	            sql.append("from t_htl_ep_dailyaudit td ,htl_hotel hh ");
	            sql.append("where td.audittype= '");
	            sql.append(param.getAuditType()+"' ");
	            if(param.getCcStatus()!=null && param.getCcStatus().length()>0){
		            sql.append("and td.cc_state= '");
		            sql.append(param.getCcStatus()+"' ");
	            }
	            if(param.getHotelStatus()!=null && param.getHotelStatus().equals("0")){
	            	sql.append("and td.hotel_modifytime is null ");
	            }
	            if(param.getHotelStatus()!=null && param.getHotelStatus().equals("1")){
	            	sql.append("and td.hotel_modifytime is not null ");
	            }
	            if(param.getOrderCd()!=null && param.getOrderCd().length()>0){
	            	sql.append(" and td.ordercd like '%");
		            sql.append(param.getOrderCd().trim()+"%' ");
	            }
	            if(param.getCheckOutDate()!=null && param.getCheckOutDate().length()>0){
	            	sql.append(" and td.checkouttime =to_date('");
	            	sql.append(param.getCheckOutDate().trim()+"','yyyy-MM-dd') ");
	            }
	            if(param.getHotelName()!=null && param.getHotelName().length()>0){
	   			 sql.append(" and hh.chn_name like '%"+param.getHotelName()+"%' ");	
	   			}
	            sql.append(" and td.hotelid= hh.hotel_id ))");	                     
	           // sql.append(" and trunc(td.createtime) = trunc(sysdate))) ");
	            sql.append("where rn>= ");			
				sql.append((param.getPageNo()-1)*param.getPageSize()+1+" ");	
				sql.append("and rn<= ");	
				sql.append(param.getPageNo()*param.getPageSize()+" ");	
				Query query = session.createSQLQuery(sql.toString());
	            List<Object[]> list = query.list();
	            if(list!=null && list.size()>0){
	            	for(Object[] obj : list){
	            		AuditOrder auditOrder = new AuditOrder();
	            		auditOrder.setEpDailyAuditId(obj[0]!=null ? Long.parseLong(String.valueOf(obj[0])) : null);
	            		auditOrder.setOrderid(obj[1]!=null ? Long.parseLong(String.valueOf(obj[1])) : null);
	            		auditOrder.setHotelid(obj[2]!=null ? Long.parseLong(String.valueOf(obj[2])) : null);
	            		auditOrder.setOrdercd(obj[3]!=null ? String.valueOf(obj[3]) : "");
	            		auditOrder.setCheckintime(obj[4]!=null ? String.valueOf(obj[4]) : "");
	            		auditOrder.setCheckouttime(obj[5]!=null ? String.valueOf(obj[5]) : "");
	            		auditOrder.setAuditdate(obj[6]!=null ? String.valueOf(obj[6]) : "");
	            		auditOrder.setRoommount(obj[7]!=null ? Integer.parseInt(String.valueOf(obj[7])) : null);
	            		auditOrder.setRoomid(obj[8]!=null ? String.valueOf(obj[8]) : "");
	            		auditOrder.setRoomname(obj[9]!=null ? String.valueOf(obj[9]) : "");
	            		auditOrder.setAudittype(obj[10]!=null ? String.valueOf(obj[10]) : "");
	            		auditOrder.setHotel_state(obj[11]!=null ? String.valueOf(obj[11]) : "");
	            		auditOrder.setCc_state(obj[12]!=null ? String.valueOf(obj[12]) : "");
	            		auditOrder.setHotelModifytime(obj[13]!=null ? String.valueOf(obj[13]) : "");
	            		auditOrder.setHotelModifyname(obj[14]!=null ? String.valueOf(obj[14]) : "");
	            		auditOrder.setCcModifytime(obj[15]!=null ? String.valueOf(obj[15]) : "");
	            		auditOrder.setCcModifyname(obj[16]!=null ? String.valueOf(obj[16]) : "");
	            		auditOrder.setCreatetime(obj[17]!=null ? String.valueOf(obj[17]) : "");
	            		auditOrder.setHotelName(obj[18]!=null ? String.valueOf(obj[18]) : "");
	            		auditOrder.setRemark(obj[19]!=null ? String.valueOf(obj[19]) : "");
	            		List<AuditOrderItem> itemList = queryAuditItemById(auditOrder.getEpDailyAuditId());
	            		auditOrder.setItemList(itemList);
	            		orderList.add(auditOrder);
	            	}
	            }	
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				session.flush();
				session.clear();
				session.close();
			}
		return orderList;
	}
	
	public Long queryOrderAuditSum(OrderParam param) {
		Session session = null;		
		StringBuffer sql=null;
		Long sum = 0l;
		Object object=null;
		try {
			session = super.getSessionFactory().openSession();
			
			sql = new StringBuffer();
			
			sql.append("select count(t.ep_dailyaudit_id) from t_htl_ep_dailyaudit t ,htl_hotel hh where 1=1 ");
			sql.append(" and t.hotelId= hh.hotel_id  ");
			if(param.getCcStatus()!=null && param.getCcStatus().length()>0){
	            sql.append("and t.cc_state= '");
	            sql.append(param.getCcStatus()+"' ");
            }
            if(param.getHotelStatus()!=null && param.getHotelStatus().length()>0){
            	sql.append("and t.hotel_state= '");
            	sql.append(param.getHotelStatus()+"' ");
            }
			if(param.getHotelName()!=null && param.getHotelName().length()>0){
			 sql.append(" and hh.chn_name like '%"+param.getHotelName()+"%' ");	
			}
			if(param.getCheckOutDate()!=null && param.getCheckOutDate().length()>0){
            	sql.append(" and t.checkouttime =to_date('");
            	sql.append(param.getCheckOutDate().trim()+"','yyyy-MM-dd') ");
            }
			sql.append(" and t.audittype= '");
	        sql.append(param.getAuditType()+"' ");
	        if(param.getOrderCd()!=null && param.getOrderCd().length()>0){
            	sql.append(" and t.ordercd like '%");
	            sql.append(param.getOrderCd().trim()+"%' ");
            }
			Query query = session.createSQLQuery(sql.toString());
		    sum = (object=query.uniqueResult())!=null ? Long.parseLong(object.toString()) : 0l;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			session.flush();
			session.clear();
			session.close();
		}
		return sum;
	}


	public void updateOrderCcStatus(UserWrapper user,String orderCd) {
		Session session = null;		
		StringBuffer sql=null;

		try {
			session = super.getSessionFactory().openSession();
			
			sql = new StringBuffer();
			sql.append("update t_htl_ep_dailyaudit set cc_state='1',cc_modifytime=sysdate,cc_modifyName= '");
			sql.append(user.getLoginName()+"' ");
			sql.append("where ordercd = '");
			sql.append(orderCd+"' ");
			Query query = session.createSQLQuery(sql.toString());
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			session.flush();
			session.clear();
			session.close();
		}
	}
	
	public String queryRemark(String orderCd, String auditType) {
	    Session session = null;		
		StringBuffer sql=null;
		Object obj=null;
		try {  
			session = super.getSessionFactory().openSession();			
			sql = new StringBuffer();
			sql.append("select remark from t_htl_ep_dailyaudit where ordercd='"+orderCd+"' ");
			sql.append("and audittype='"+auditType+"'");
			Query query = session.createSQLQuery(sql.toString());
		    obj = query.uniqueResult();				
		}catch (Exception e) {
			log.error(e);
		}finally{
			session.flush();
			session.clear();
			session.close();
		}
	return obj!=null && String.valueOf(obj).length()>0 ? String.valueOf(obj) : "";
    }
	
	
	public void updateRemark(String orderCd, String auditType, String remark) {
		
	    Session session = null;		
		StringBuffer sql=null;

		try {  
			session = super.getSessionFactory().openSession();			
			sql = new StringBuffer();
			sql.append("update  t_htl_ep_dailyaudit set remark = '"+remark+"' where ordercd='"+orderCd+"' ");
			sql.append("and audittype='"+auditType+"'");
			Query query = session.createSQLQuery(sql.toString());
			query.executeUpdate();
		}catch (Exception e) {
			log.error(e);
		}finally{
			session.flush();
			session.clear();
			session.close();
		}
   }
}

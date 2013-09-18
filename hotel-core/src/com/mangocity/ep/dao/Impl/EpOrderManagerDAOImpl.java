package com.mangocity.ep.dao.Impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.mangocity.ep.dao.EpOrderManagerDAO;
import com.mangocity.ep.entity.EpOrder;
import com.mangocity.ep.entity.RequestParam;
import com.mangocity.hotel.order.persistence.OrOrderFax;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.log.MyLog;

public class EpOrderManagerDAOImpl extends DAOHibernateImpl implements EpOrderManagerDAO{
	private static final MyLog log = MyLog.getLogger(EpOrderManagerDAOImpl.class);
	
	public List<EpOrder> queryEpOrder(RequestParam requestParam) {
         
		Session session = null;		
		StringBuffer sql=null;
		List<EpOrder> epOrderList = new ArrayList<EpOrder>();
		
		try {  
			session = super.getSessionFactory().openSession();
			sql = new StringBuffer();
			sql.append("select eporder_id,orderid,hotelid,hotelname,roomtypeid,childroomtypeid,ordercd,");
			sql.append("fellownames,checkindate,checkoutdate,roomtypename,roomquantity,");
			sql.append("bedtype,arrivaltime,latestarrivaltime,sumrmb,paymethod,iscreditassured,specialrequest,");
			sql.append("hotelstatus,ccstatus,introduce,remark,createTime,modifyTime,modifyName,");
			sql.append("ishotelconfirm,isccConfirm,paymentCurrency,ccModifyTime,ccModifyName from (");
			sql.append("select eporder_id,orderid,hotelid,hotelname,roomtypeid,childroomtypeid,ordercd,");
			sql.append("fellownames,checkindate,checkoutdate,roomtypename,roomquantity,");
			sql.append("bedtype,arrivaltime,latestarrivaltime,sumrmb,paymethod,iscreditassured,specialrequest,");
			sql.append("hotelstatus,ccstatus,introduce,remark,createTime,modifyTime,modifyName,");
			sql.append("ishotelconfirm,isccConfirm,paymentCurrency,ccModifyTime,ccModifyName,rownum rn from (");
			sql.append("select   eporder_id, ");
			sql.append("orderid, ");
			sql.append("hotelid, ");
			sql.append("hotelname, ");
			sql.append("roomtypeid, ");
			sql.append("childroomtypeid, ");
			sql.append("ordercd, ");
			sql.append("fellownames, ");
			sql.append("to_char(checkindate,'yyyy-MM-dd') checkindate, ");
			sql.append("to_char(checkoutdate,'yyyy-MM-dd') checkoutdate, ");
			sql.append("roomtypename, ");
			sql.append("roomquantity, ");
			sql.append("bedtype, ");
			sql.append("arrivaltime, ");
			sql.append("latestarrivaltime, ");
			sql.append("sumrmb, ");
			sql.append("paymethod, ");		
			sql.append("iscreditassured, ");
			sql.append("specialrequest, ");
			sql.append("hotelstatus, ");
			sql.append("ccstatus, ");
			sql.append("introduce, ");
			sql.append("remark, ");
			sql.append("to_char(createTime,'yyyy-MM-dd hh24:mi:ss') createTime, ");
			sql.append("to_char(modifyTime,'yyyy-MM-dd hh24:mi:ss') modifyTime, ");
			sql.append("modifyName, ");
			sql.append("ishotelconfirm, ");
			sql.append("isccConfirm, ");
			sql.append("paymentCurrency, ");
			sql.append("to_char(ccModifyTime,'yyyy-MM-dd hh24:mi:ss') ccModifyTime, ");
			sql.append("ccModifyName ");
			sql.append("from t_htl_ep_order ");
			sql.append("where 1=1 ");
			if(requestParam.getOrderCd()!=null && requestParam.getOrderCd().trim().length()>0){
				sql.append("and ordercd like '%");
				sql.append(requestParam.getOrderCd()+"%' ");
			}
			if(requestParam.getHotelName()!=null && requestParam.getHotelName().trim().length()>0){
				sql.append("and hotelname like '%");
				sql.append(requestParam.getHotelName()+"%' ");
			}
			if(requestParam.getHotelstatus()!=null && requestParam.getHotelstatus().trim().length()>0){
				sql.append("and hotelstatus = '");
				sql.append(requestParam.getHotelstatus()+"' ");
			}
			if(requestParam.getCcstatus()!=null && requestParam.getCcstatus().trim().length()>0){
			sql.append("and ccstatus = '");
			sql.append(requestParam.getCcstatus()+"' ");
			}
			sql.append(" order by createTime desc,modifyTime ");
			sql.append("))");
			sql.append("where rn>=");
			sql.append((requestParam.getPageNo()-1)*requestParam.getPerPageSize()+1+" ");
			sql.append("and rn<= ");	
			sql.append(requestParam.getPageNo()*requestParam.getPerPageSize()+" ");	
			System.out.println(sql.toString());
			Query query = session.createSQLQuery(sql.toString());
			List<Object[]> objList = query.list();
			
	      for(Object[] obj : objList){
	    	EpOrder epOrder = new EpOrder();
			epOrder.setEpOrder_id(obj[0]!=null ? Long.parseLong(obj[0].toString()) : 0l);
			epOrder.setOrderid(obj[1]!=null ? Long.parseLong(obj[1].toString()) : 0l);
			epOrder.setHotelid(obj[2]!=null ? Long.parseLong(obj[2].toString()) : 0l);
			epOrder.setHotelname(obj[3]!=null ? obj[3].toString() : "");
			epOrder.setRoomtypeid(obj[4]!=null ? Long.parseLong(obj[4].toString()) : 0l);
			epOrder.setChildroomtypeid(obj[5]!=null ? Long.parseLong(obj[5].toString()) : 0l);
			epOrder.setOrdercd(obj[6]!=null ? obj[6].toString() : "");
			epOrder.setFellownames(obj[7]!=null ? obj[7].toString() : "");
			epOrder.setCheckindate(obj[8]!=null ? obj[8].toString() : "");
			epOrder.setCheckoutdate(obj[9]!=null ? obj[9].toString() : "");
			epOrder.setRoomtypename(obj[10]!=null ? obj[10].toString() : "");
			epOrder.setRoomquantity(obj[11]!=null ? Integer.parseInt(obj[11].toString()) : 0);
			epOrder.setBedtype(obj[12]!=null ? obj[12].toString() : "");
			epOrder.setArrivaltime(obj[13]!=null ? obj[13].toString() : "");
			epOrder.setLatestarrivaltime(obj[14]!=null ? obj[14].toString() : "");
			epOrder.setSumrmb(obj[15]!=null ? Double.parseDouble(obj[15].toString()) : 0);
			epOrder.setPaymethod(obj[16]!=null ? obj[16].toString() : null);
			epOrder.setIscreditassured(obj[17]!=null ? Integer.parseInt(obj[17].toString()) : null);
			epOrder.setSpecialrequest(obj[18]!=null ? obj[18].toString() : "无");
			epOrder.setHotelstatus(obj[19]!=null ? Integer.parseInt(obj[19].toString()) : 0);
			epOrder.setCcstatus(obj[20]!=null ? Integer.parseInt(obj[20].toString()) : 0);
			epOrder.setIntroduce(obj[21]!=null ? obj[21].toString() : "");
			epOrder.setRemark(obj[22]!=null ? obj[22].toString() : "");
			epOrder.setCreateTime(obj[23]!=null ? obj[23].toString() : "");
			epOrder.setModifyTime(obj[24]!=null ? obj[24].toString() : "");
			epOrder.setModifyName(obj[25]!=null ? obj[25].toString() : "");
			epOrder.setIshotelconfirm(obj[26]!=null ? obj[26].toString() : "");
			epOrder.setIsccConfirm(obj[27]!=null ? obj[27].toString() : "");								
			epOrder.setPaymentCurrency(obj[28]!=null ? obj[28].toString() : "");	
			epOrder.setCcModifyTime(obj[29]!=null ? obj[29].toString() : null);	
			epOrder.setCcModifyName(obj[30]!=null ? obj[30].toString() : null);	
			
			epOrderList.add(epOrder);
	      }
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			session.flush();
			session.clear();
			session.close();
		}
		return epOrderList;
	}
	
	
	public void updateConfrimStatus(String orderCd,UserWrapper roleUser) {
		Session session = null;		
		StringBuffer sql=null;
		try {  
			session = super.getSessionFactory().openSession();
			sql = new StringBuffer();
		    sql.append("update t_htl_ep_order set ccstatus='1',isCCconfirm='1',ccmodifyTime=sysdate,ccmodifyName= '");
		    sql.append(roleUser.getLoginName()+"' ");
		    sql.append("where ordercd='");
		    sql.append(orderCd+"'");
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
	
	
	public Integer queryOrderSum(RequestParam requestParam) {
		Session session = null;		
		StringBuffer sql=null;
		Integer rows=0;
		try {  
			session = super.getSessionFactory().openSession();
			sql = new StringBuffer();
		    sql.append("select count(eporder_id) from t_htl_ep_order where 1=1 ");
		    if(requestParam.getOrderCd()!=null && requestParam.getOrderCd().trim().length()>0){
				sql.append("and ordercd like '%");
				sql.append(requestParam.getOrderCd()+"%' ");
			}
			if(requestParam.getHotelName()!=null && requestParam.getHotelName().trim().length()>0){
				sql.append("and hotelname like '%");
				sql.append(requestParam.getHotelName()+"%' ");
			}
			if(requestParam.getHotelstatus()!=null && requestParam.getHotelstatus().trim().length()>0){
				sql.append("and hotelstatus = '");
				sql.append(requestParam.getHotelstatus()+"' ");
			}
			if(requestParam.getCcstatus()!=null && requestParam.getCcstatus().trim().length()>0){
			sql.append("and ccstatus = '");
			sql.append(requestParam.getCcstatus()+"' ");
			}
		    Query query = session.createSQLQuery(sql.toString());
		    rows=Integer.parseInt(query.uniqueResult().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			session.flush();
			session.clear();
			session.close();
		}
		return rows;
	}
	
	
	public String validateEpOrderByHotelId(String hotelId) {
		Session session = null;		
		StringBuffer sql=null;
		String isEpOrder="0";
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
			sql.append("and a.hotel_id= ");
			sql.append(hotelId+"");
			
			Query query = session.createSQLQuery(sql.toString());
	        List<Object []> objList = query.list();
	        
	        if(objList!=null && objList.size()>=1){
	        	isEpOrder="1";
	        }
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			session.flush();
			session.clear();
			session.close();
		}
				
		return isEpOrder;
	}
	
	@SuppressWarnings("unchecked")
	public List<EpOrder> queryHotelConfirmedEPOrder(int minuteTime) {
		Session session = null;		
		StringBuffer sql=null;
		List<EpOrder> list = new ArrayList<EpOrder>();
		try {
			session = super.getSessionFactory().openSession();
			sql = new StringBuffer();
			sql.append("select heo.orderid,heo.ordercd,heo.ishotelconfirm,heo.confirmspecialreqtype,heo.hotelconfirmno,heo.remark,heo.introduce,heo.specialrequest");
			sql.append(" from t_htl_ep_order heo where heo.hotelstatus = 1 and heo.isccconfirm is null and heo.modifytime >= (sysdate - "+minuteTime+"/ 24)");
			Query query = session.createSQLQuery(sql.toString());
			List<Object[]> tempList = query.list();
			if(null != tempList && !tempList.isEmpty()){
				for(Object[] obj : tempList){
					EpOrder eo = new EpOrder();
					eo.setOrderid(obj[0]!=null ? Long.parseLong(obj[0].toString()) : 0l);
					eo.setOrdercd(obj[1]!=null ? obj[1].toString() : "");
					eo.setIshotelconfirm(obj[2]!=null ? obj[2].toString() : "");
					eo.setConfirmspecialReqType(obj[3]!=null ? Integer.parseInt(obj[3].toString()) : 0);
					eo.setHotelConfirmNo(obj[4]!=null ? obj[4].toString() : "");
					eo.setRemark(obj[5]!=null ? obj[5].toString() : "");
					eo.setIntroduce(obj[6]!=null ? obj[6].toString() : "");
					eo.setSpecialrequest(obj[7]!=null ? obj[7].toString() : "");
					list.add(eo);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}finally{
			session.flush();
			session.clear();
			session.close();
		}
								
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Long> queryEpOrderFaxId(Long orderId){
		Session session = null;		
		StringBuffer sql=null;
		List<Long> list = new ArrayList<Long>();
		try {
			session = super.getSessionFactory().openSession();
			sql = new StringBuffer();
			sql.append("select oof.id ");
			sql.append(" from or_orderfax oof ");
			sql.append(" where oof.orderid = "+orderId+" and oof.channel = 7");
			Query query = session.createSQLQuery(sql.toString());
			List<Object> tempList = query.list();
			if(null != tempList && !tempList.isEmpty()){
				for(Object obj : tempList){
					if(obj!=null){
						list.add(Long.parseLong(obj.toString()));
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}finally{
			session.flush();
			session.clear();
			session.close();
		}
								
		return list;
	}
	
	public void updateEpOrderFax(Long orderFaxId, int hotelReturn,
			int isConfirm, String hotelConfirmNo) {
		Session session = null;		
		StringBuffer sql=null;
		try {  
			session = super.getSessionFactory().openSession();
			sql = new StringBuffer();
		    sql.append("update or_orderfax f set f.hotelreturn = "+hotelReturn);
		    sql.append(", f.isconfirm = "+isConfirm);
		    if(null != hotelConfirmNo && !"".equals(hotelConfirmNo)){
		    	sql.append(", f.confirmno = "+hotelConfirmNo);
		    	sql.append(", f.validconfirm = 1");
		    }
		    sql.append(" where f.id = "+orderFaxId+"and f.channel = 7 ");
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
	
	@SuppressWarnings("unchecked")
	public long getOrParamSeqNextVal(String seqName) {
		Session session = null;		
		StringBuffer sql=null;
		long seq = 0;
		try {  
			session = super.getSessionFactory().openSession();
			sql = new StringBuffer();
		    sql.append("select " + seqName+ ".nextval from dual");
		    Query query = session.createSQLQuery(sql.toString());
		    List<Object> tempList = query.list();
		    if(null != tempList && !tempList.isEmpty()){
		    	seq = Long.valueOf(tempList.get(0).toString());
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			session.flush();
			session.clear();
			session.close();
		}
		return seq;
	}
}

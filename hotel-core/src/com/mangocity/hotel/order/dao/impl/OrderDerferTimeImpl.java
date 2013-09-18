package com.mangocity.hotel.order.dao.impl;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.mangocity.hotel.order.dao.OperOrderDerferTimeDAO;
import com.mangocity.hotel.order.persistence.DeferOrder;
import com.mangocity.hotel.order.persistence.DerferOrderParam;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.util.dao.DAOHibernateImpl;

public class OrderDerferTimeImpl extends DAOHibernateImpl implements OperOrderDerferTimeDAO{

	public void modifyOrderDerferTime(Integer deferTime, Long orderId) {
		StringBuffer sql = new StringBuffer();	
		sql.append("update ");
		sql.append("temp_order tm set ");
		sql.append("deferTime="+deferTime+",");
		sql.append("modifyTime=sysdate ");
		sql.append("where tm.orderId="+orderId);
        
		Session session=super.getSessionFactory().openSession();
	    session.createSQLQuery(sql.toString()).executeUpdate();
	    session.flush();
		session.clear();
		session.close();
	}
    
	public List<DeferOrder> queryDerferOrderData(DerferOrderParam param) {
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
		List <Object[]>list_1=null;
		List<DeferOrder>  list_2=new ArrayList<DeferOrder> ();
		StringBuffer sql = new StringBuffer();	
		Session session = null;
		try {
			sql.append("select ");
			sql.append("orderID,emergencyLevel,orderCD,hotelid,hotelName,orderState,roomTypename,roomquantity,CHECKINDATE,CHECKOUTDATE,hotelConfirm,CUSTOMERCONFIRM,");
			sql.append("MODIFIER,frontDate,midDate,isManualOrder,MODIFIERNAME,payToHotel,createdate,DIFFICULTYTIME,groupType,FLAGSTR,FLAGIMG,FLAGFRONT,FLAGTIMEOUT,");
			sql.append("READFLAG,SECONDCONFIRMDONE,sort4,toMidTime,payMethod,hurryOrderTimes,sort1,sort2,sort3,msg,modifierRole,deferTime,channel from (select ");
			sql.append("orderID,emergencyLevel,orderCD,hotelid,hotelName,orderState,roomTypename,roomquantity,CHECKINDATE,CHECKOUTDATE,hotelConfirm,CUSTOMERCONFIRM,");
			sql.append("MODIFIER,frontDate,midDate,isManualOrder,MODIFIERNAME,payToHotel,createdate,DIFFICULTYTIME,groupType,FLAGSTR,FLAGIMG,FLAGFRONT,FLAGTIMEOUT,");
			sql.append("READFLAG,SECONDCONFIRMDONE,sort4,toMidTime,payMethod,hurryOrderTimes,sort1,sort2,sort3,msg,modifierRole,deferTime,channel,ROWNUM dataRow from (select a.orderID,");
			sql.append("a.emergencyLevel,a.orderCD,a.hotelid,a.hotelName,a.orderState,a.roomTypename,a.roomquantity,a.CHECKINDATE,a.CHECKOUTDATE,a.hotelConfirm,");
			sql.append("a.CUSTOMERCONFIRM,a.MODIFIER,to_char(a.MODIFIEDFRONTTIME, 'yyyy-mm-dd hh24:mi:ss') as frontDate,");
			sql.append("to_char(a.MODIFIEDMIDTIME, 'yyyy-mm-dd hh24:mi:ss') as midDate,a.isManualOrder,a.MODIFIERNAME,a.payToHotel,a.createdate,");
			sql.append("round((sysdate - a.difficultytime) * 24 * 60) as DIFFICULTYTIME,temp.grouptype as groupType,to_char((case when a.emergencyLevel > 9 then ");
			sql.append("'一般' else '紧急' end)) as FLAGSTR,(case when a.emergencyLevel > 9 then 'green' else 'red' end) as FLAGIMG,(case ");
			sql.append("when a.modifiedTime = a.modifiedFrontTime then 1 else 0 end) as FLAGFRONT,(case when (a.customerConfirm = 0 and ");
			sql.append("(trunc(a.createDate) = a.CHECKINDATE and (a.createDate + 18 / (24 * 60)) < sysdate)) then 1 else (case when (a.customerConfirm = 0 and ");
			sql.append("(trunc(a.createDate) != a.CHECKINDATE and (a.createDate + 18 / (24 * 60)) < sysdate)) then 2 else 0 end) end) as FLAGTIMEOUT,((select count(f.id) ");
			sql.append("from or_faxlog f, or_orderfax c where f.type = 'HA' and f.state = 0 and (Length(f.barcode) < 12 and f.barcode = c.barcode and ");
			sql.append("c.orderid = a.orderid)) + (select count(f.id) from or_faxlog f where f.type = 'HA' and f.state = 0 ");
			sql.append("and (Length(f.barcode) > 12 and f.barcode = a.ordercd))) as READFLAG,(select count(m.ex_crsorder_item_id) from ex_crsorder_item m ");
			sql.append("where m.order_id = a.orderid) as SECONDCONFIRMDONE,(case when a.hotelConfirmFax = 1 then 1 else a.hotelConfirmTel end) as sort4,");
			sql.append("a.channel,(case when (sysdate - a.toMidTime) >= 0 then trunc((sysdate - a.toMidTime) * 1440, 0) else 0 end) as toMidTime,(case ");
			sql.append("when (a.paymethod = 'pay' and a.iscreditassured = 0) then '面付' else (case when a.paymethod = 'pre_pay' then '预付' else '担保' end) end) as payMethod,a.hurryOrderTimes,");
			sql.append("(select min(b.emergencyLevel) from or_order b where b.assignto like '%"+param.getAssignTo()+"%' and b.hotelid = a.hotelid) as sort1,null as sort2,null as sort3,");
			sql.append("mb.msg,a.modifierRole,to_number(temp.defertime - (round((sysdate - temp.modifytime) * 24 * 60))) as deferTime from or_order a,");
			sql.append("(select o.orderid, '1' as msg from or_order o, or_memberconfirm m where o.orderid = m.orderid AND o.assignTo like '%"+param.getAssignTo()+"%' and o.isStayInMid = 1 ");
			sql.append("and o.illusive = 0 and (m.sendstatus = 3 or (m.sendstatus = 4 and (sysdate - m.sendtime) * 24 * 60 >= 30)) and m.channel = 3 ");
			sql.append("and not exists (select m1.ID from or_memberconfirm m1 where m1.orderid = m.orderid and m.channel = 3 and m.sendstatus = 2) group by o.orderid) mb,");
			sql.append("TEMP_ORDER temp where a.isStayInMid = 1 and a.illusive = 0 and a.orderid = mb.orderid(+) and a.orderid = temp.orderid and temp.deferTime is not null ");
			if(param.getOrderCD().length()>0){
			sql.append("and a.orderCD like "+"'%"+param.getOrderCD()+"%' ");
			}
			if(param.getMemberName().length()>0){
			sql.append("and a.MEMBERNAME like '%"+param.getMemberName()+"%' ");
			}
			if(param.getHotelName().length()>0){
			sql.append("and a.HOTELNAME like "+"'%"+param.getHotelName()+"%' ");
			}
			if(param.getBussinessName().length()>0){
			sql.append("and a.modifierRole like "+"'%"+param.getBussinessName()+"%' ");
			}
			if(param.getVipLevel().length()>0){
			sql.append("and a.payState like "+"'%"+param.getVipLevel()+"%' ");
			}
			sql.append("AND a.orderState >= 3 AND a.assignTo like '%"+param.getAssignTo()+"%' order by deferTime asc )) where ");
			sql.append("dataRow>="+param.getStartIndex()+" ");
			sql.append("and dataRow<="+param.getEndIndex());
			
			System.out.println(sql.toString());
			
			session = super.getSessionFactory().openSession();
			Query query = session.createSQLQuery(sql.toString());
			list_1=query.list();
			for(Object[] obj:list_1){
				DeferOrder deferOrder=new DeferOrder();
				deferOrder.setOrderID(obj[0]==null ? null : Long.parseLong(obj[0].toString()));
				deferOrder.setEmergencyLevel(obj[1]==null ? null : Integer.parseInt(obj[1].toString()));
				deferOrder.setOrderCd(obj[2]==null ? null : obj[2].toString());
				deferOrder.setHotelid(obj[3]==null ? null : Long.parseLong(obj[3].toString()));
				deferOrder.setHotelName(obj[4]==null ? null : obj[4].toString());
				deferOrder.setOrderState(obj[5]==null ? null : Integer.parseInt(obj[5].toString()));
				deferOrder.setRoomTypename(obj[6]==null ? null : obj[6].toString());
				deferOrder.setRoomquantity(obj[7]==null ? null : Integer.parseInt(obj[7].toString()));
				deferOrder.setCHECKINDATE(obj[8]==null ? null : sdf.format((Date)obj[8]));
				deferOrder.setCHECKOUTDATE(obj[9]==null ? null : sdf.format((Date)obj[9]));
				deferOrder.setHotelConfirm(obj[10]==null ? null : Integer.parseInt(obj[10].toString()));
				deferOrder.setCUSTOMERCONFIRM(obj[11]==null ? null : Integer.parseInt(obj[11].toString()));
				deferOrder.setMODIFIER(obj[12]==null ? null : obj[12].toString());
				deferOrder.setFrontDate(obj[13]==null ? null : obj[13].toString());
				deferOrder.setMidDate(obj[14]==null ? null : obj[14].toString());
				deferOrder.setIsManualOrder(obj[15]==null ? null : Integer.parseInt(obj[15].toString()));
				deferOrder.setMODIFIERNAME(obj[16]==null ? null : obj[16].toString());
				deferOrder.setPayToHotel(obj[17]==null ? null : Integer.parseInt(obj[17].toString()));
				deferOrder.setCreatedate(obj[18]==null ? null : sdf.format((Date)obj[18]));
				deferOrder.setDIFFICULTYTIME(obj[19]==null ? null : Integer.parseInt(obj[19].toString()));
				deferOrder.setGroupType(obj[20]==null ? null : Integer.parseInt(obj[20].toString()));
				deferOrder.setFLAGSTR(obj[21]==null ? null : obj[21].toString());
				if(deferOrder.getFLAGSTR()!=null && deferOrder.getFLAGSTR().equals("一")){
					deferOrder.setFLAGSTR("一般");
				}
				if(deferOrder.getFLAGSTR()!=null && deferOrder.getFLAGSTR().equals("紧")){
					deferOrder.setFLAGSTR("紧急");
				}
				deferOrder.setFLAGIMG(obj[22]==null ? null : obj[22].toString());
				deferOrder.setFLAGFRONT(obj[23]==null ? null : Integer.parseInt(obj[23].toString()));
				deferOrder.setFLAGTIMEOUT(obj[24]==null ? null : Integer.parseInt(obj[24].toString()));
				deferOrder.setREADFLAG(obj[25]==null ? null : Integer.parseInt(obj[25].toString()));
				deferOrder.setSECONDCONFIRMDONE(obj[26]==null ? null : Integer.parseInt(obj[26].toString()));
				deferOrder.setSort4(obj[27]==null ? null : Integer.parseInt(obj[27].toString()));
				deferOrder.setToMidTime(obj[28]==null ? null : Long.parseLong(obj[28].toString()));
				deferOrder.setPayMethod(obj[29]==null ? null : obj[29].toString());
				if(deferOrder.getPayMethod()!=null && deferOrder.getPayMethod().equals("面")){
					deferOrder.setPayMethod("面付");
				}
				if(deferOrder.getPayMethod()!=null && deferOrder.getPayMethod().equals("预")){
					deferOrder.setPayMethod("预付");
				}
				if(deferOrder.getPayMethod()!=null && deferOrder.getPayMethod().equals("担")){
					deferOrder.setPayMethod("担保");
				}
				deferOrder.setHurryOrderTimes(obj[30]==null ? null : Integer.parseInt(obj[30].toString()));
				deferOrder.setSort1(obj[31]==null ? null : obj[31].toString());
				deferOrder.setSort2(obj[32]==null ? null : obj[32].toString());
				deferOrder.setSort3(obj[33]==null ? null : obj[33].toString());
				deferOrder.setMsg(obj[34]==null ? null : obj[34].toString());
				deferOrder.setModifierRole(obj[35]==null ? null : obj[35].toString());
				deferOrder.setDeferTime(obj[36]==null ? null : Integer.parseInt(obj[36].toString()));
				deferOrder.setChannel(obj[37]==null ? null : Integer.parseInt(obj[37].toString()));
				list_2.add(deferOrder);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(session != null){
				session.flush();  
				session.clear();
				session.close();
			}			
		}
		
		
		
		return list_2;
	}

	public Integer validateOrderSts(Long orderId) {
		
		StringBuffer sql = new StringBuffer();	
		sql.append("select count(oo.orderid) ");
		sql.append("from or_order oo, temp_order tm ");
		sql.append("where oo.orderid = tm.orderid ");
		sql.append("and oo.isstayinmid = 1 " );
		sql.append("and tm.assignstate = 1 ");
		sql.append("and tm.orderid="+orderId);

		Session session=super.getSessionFactory().openSession();
		Query query = session.createSQLQuery(sql.toString());
		Integer flag=Integer.parseInt(query.uniqueResult().toString());
		session.flush();
	    session.clear();
		session.close();
		return flag;
	}

	public void modifyDerferOrder(Long orderId) {
		StringBuffer sql = new StringBuffer();	
		sql.append("update temp_order tm ");
		sql.append("set tm.defertime = null, tm.modifytime = null ");
		sql.append("where tm.orderid ="+orderId);
		
		Session session=super.getSessionFactory().openSession();
	    session.createSQLQuery(sql.toString()).executeUpdate();
	    session.flush();
		session.clear();
		session.close();
	}

	public Long querySumRow(DerferOrderParam param) {
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
		List <Object[]>list_1=null;
		List<DeferOrder>  list_2=new ArrayList<DeferOrder> ();
		StringBuffer sql = new StringBuffer();	
		sql.append("select count(*)");
		sql.append(" from or_order a,");
		sql.append("(select o.orderid, '1' as msg from or_order o, or_memberconfirm m where o.orderid = m.orderid AND o.assignTo like '%%' and o.isStayInMid = 1 ");
		sql.append("and o.illusive = 0 and (m.sendstatus = 3 or (m.sendstatus = 4 and (sysdate - m.sendtime) * 24 * 60 >= 30)) and m.channel = 3 ");
		sql.append("and not exists (select m1.ID from or_memberconfirm m1 where m1.orderid = m.orderid and m.channel = 3 and m.sendstatus = 2) group by o.orderid) mb,");
		sql.append("TEMP_ORDER temp where a.isStayInMid = 1 and a.illusive = 0 and a.orderid = mb.orderid(+) and a.orderid = temp.orderid and temp.deferTime is not null ");
		if(param.getOrderCD().length()>0){
		sql.append("and a.orderCD like "+"'%"+param.getOrderCD()+"%' ");
		}
		if(param.getMemberName().length()>0){
		sql.append("and a.MEMBERNAME like '%"+param.getMemberName()+"%' ");
		}
		if(param.getHotelName().length()>0){
		sql.append("and a.HOTELNAME like "+"'%"+param.getHotelName()+"%' ");
		}
		if(param.getBussinessName().length()>0){
		sql.append("and a.modifierRole like "+"'%"+param.getBussinessName()+"%' ");
		}
		if(param.getVipLevel().length()>0){
		sql.append("and a.payState like "+"'%"+param.getVipLevel()+"%' ");
		}
		sql.append("AND a.orderState >= 3 AND a.assignTo like '%%' ");
		
		System.out.println(sql.toString());
		
		Session session=super.getSessionFactory().openSession();
		Query query = session.createSQLQuery(sql.toString());
		Long sumRow=Long.parseLong(query.uniqueResult().toString());
	    session.flush();
		session.clear();
		session.close();
		return sumRow;
	}

	public void saveDerferTimeLog(OrHandleLog handleLog) {		
		super.getHibernateTemplate().save(handleLog);
	}
   	
	
	
	
	
}

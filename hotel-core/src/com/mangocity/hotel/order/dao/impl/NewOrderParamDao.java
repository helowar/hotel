package com.mangocity.hotel.order.dao.impl;

import java.util.List;

import com.mangocity.hotel.order.dao.INewOrderParamDao;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class NewOrderParamDao extends GenericDAOHibernateImpl implements 
		INewOrderParamDao {

	public List<Object[]> searchNewOrderParams(String priceTypeID,
			String checkinDate, String checkoutDate) {
		System.out.println("=======searchNewOrderParams neibu======");
		System.out.println("caishu ----:"+priceTypeID+ checkinDate+checkoutDate);
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct(hpt.price_type_id), hh.hotel_id,hr.room_type_id,hpt.price_type,hp.pay_method,hp.quota_type,");
		sb.append("hr.room_qty,hh.chn_name,hr.room_name,hc.balance_method,hp.sale_price,hh.hotel_star,hh.city,hp.pay_to_prepay,");
		sb.append("hh.accept_custom,hh.alert_message,hp.currency,hh.iscontract,hh.purchase_channel");
		sb.append(" from htl_price hp,htl_price_type hpt,htl_roomtype hr,htl_hotel hh ,Htl_Contract hc");
		sb.append(" where hp.room_type_id=hpt.room_type_id and hpt.room_type_id=hr.room_type_id and hr.hotel_id=hh.hotel_id");
		sb.append("  and hc.hotel_id=hh.hotel_id");
		sb.append(" and hpt.price_type_id="+priceTypeID+"");
		sb.append(" and  hp.able_sale_date >= to_date('"+checkinDate+"','YYYY-MM-DD')");
		sb.append(" and hp.able_sale_date < to_date('"+checkoutDate+"','YYYY-MM-DD')");  
		List<Object[]> resultList = super.queryByNativeSQL(sb.toString(),null);
		System.out.println("==========="+resultList.size()+"========");
		return resultList;
	}

	public List<Object[]>  searchEverdayParams(String priceTypeID,
			String checkinDate, String checkoutDate,String payMethod) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		sb.append(" price.pay_method,price.child_room_type_id,price.hotel_id,price.able_sale_date,ot.bed_type,(case when ot.bed_type='1' and instr(room.room_state,'1:')>0 then");
		sb.append(" substr(room.room_state,instr(room.room_state,'1:')+2,1)");
		sb.append(" when ot.bed_type='2'  and instr(room.room_state,'2:')>0 then");
		sb.append(" substr(room.room_state,instr(room.room_state,'2:')+2,1)");
		sb.append(" when ot.bed_type='3'  and instr(room.room_state,'3:')>0 then");
		sb.append(" substr(room.room_state,instr(room.room_state,'3:')+2,1)");
		sb.append(" end) room_state ");
		sb.append("  from ");
		sb.append(" (select t.hotel_id, t.room_type_id, t.room_name, t.price_type_id,");
		sb.append(" substr(t.bed_type, instr(t.bed_type, ',', 1, c.lv) + 1, ");
		sb.append(" instr(t.bed_type, ',', 1, c.lv + 1) - (instr(t.bed_type, ',', 1, c.lv) + 1)) as bed_type ");
		sb.append(" from (select rp.hotel_id, rp.room_type_id, rp.room_name, hpt.price_type_id,");
		sb.append(" rp.quota_bed_share,',' || rp.bed_type || ',' as bed_type,");
		sb.append(" length(rp.bed_type || ',') - nvl(length(replace(rp.bed_type, ',')), 0) as cnt");
		sb.append(" from htl_roomtype rp, htl_price_type hpt where hpt.price_type_id = "+priceTypeID);
		sb.append(" and rp.room_type_id = hpt.room_type_id) t,(select level lv ");
		sb.append(" from dual connect by level <= 5) c where c.lv <= t.cnt) ot,");
		sb.append(" htl_price price,htl_room room where ");
		sb.append(" ot.room_type_id = room.room_type_id");
		sb.append(" and  price.able_sale_date >= to_date('"+checkinDate+"','YYYY-MM-DD')");
		sb.append(" and price.able_sale_date < to_date('"+checkoutDate+"','YYYY-MM-DD')");
		sb.append(" and room.room_id=price.room_id");
		sb.append(" and price.child_room_type_id="+priceTypeID);  
		sb.append(" and price.pay_method='"+payMethod+"'");
		List<Object[]> resultList = super.queryByNativeSQL(sb.toString(),null);
		return resultList;
	}

	public List<Object[]> searchQuotaParams(String priceTypeID,
			String checkinDate, String checkoutDate,String payMethod) {
		// TODO Auto-generated method stub
		System.out.println("=========searchQuotaParams()========="+priceTypeID+ checkinDate+checkoutDate+payMethod);
		StringBuilder sb = new StringBuilder();
		sb.append(" select  nvl(hqn.buy_quota_able_num,0),nvl(hqn.common_quota_able_num,0),nvl(hqn.casual_quota_able_num,0), ");
		sb.append(" hp.inc_breakfast_type,hp.inc_breakfast_price,hp.inc_breakfast_number ,ot.bed_id,hp.pay_method ,");
		sb.append(" hp.able_sale_date,hp.base_price,hp.salesroom_price,hp.sale_price,ot.quota_bed_share,ot.bed_type ,");
		sb.append(" nvl(hqn.buy_quota_outofdate_num,0), nvl(hqn.common_quota_outofdate_num,0), nvl(hqn.casual_quota_outofdate_num,0) ");
		sb.append(" from htl_quota_new hqn ,htl_price hp,");
		sb.append(" (select t.hotel_id, t.room_type_id, t.room_name, t.price_type_id,t.quota_bed_share,t.bed_type,");
		sb.append(" substr(t.bed_id, instr(t.bed_id, ',', 1, c.lv) + 1, ");
		sb.append(" instr(t.bed_id, ',', 1, c.lv + 1) - (instr(t.bed_id, ',', 1, c.lv) + 1)) as bed_id ");
		sb.append(" from (select rp.hotel_id, rp.room_type_id, rp.room_name, hpt.price_type_id,rp.bed_type,");
		sb.append(" rp.quota_bed_share,',' || rp.bed_type || ',' as bed_id,");
		sb.append(" length(rp.bed_type || ',') - nvl(length(replace(rp.bed_type, ',')), 0) as cnt");
		sb.append(" from htl_roomtype rp,htl_price_type hpt where hpt.price_type_id = "+priceTypeID);
		sb.append(" and rp.room_type_id = hpt.room_type_id) t,(select level lv ");
		sb.append(" from dual connect by level <= 5) c where c.lv <= t.cnt) ot");
		sb.append(" where ot.room_type_id = hp.room_type_id and hqn.roomtype_id(+)=hp.room_type_id and hqn.able_sale_date(+)=hp.able_sale_date");  
		sb.append(" and hp.able_sale_date >= to_date('"+checkinDate+"','YYYY-MM-DD') and hp.able_sale_date < to_date('"+checkoutDate+"','YYYY-MM-DD')"); 
		sb.append(" and hp.pay_method='"+payMethod+"' and  hp.child_room_type_id="+priceTypeID+" order by hp.able_sale_date,ot.bed_id");  	 
		List<Object[]> resultList = super.queryByNativeSQL(sb.toString(),null);
		System.out.println(resultList.size()+"========");
		return resultList;
	}
	
	
	public Boolean queryOrgByAgentCode(String agentCode) {
		Boolean isB2Bagent=false;
		if(agentCode != null) {
			StringBuilder sql =new StringBuilder(" select * from b2b_organizaioninfo info where info.agentcode='"+agentCode+"'");
			List<Object[]> objects= super.queryByNativeSQL(sql.toString(), null);
			
				if(objects.size() > 0) {
					isB2Bagent=true;
				}else {
					isB2Bagent=false;
				}
			}
			return isB2Bagent;

	}
}

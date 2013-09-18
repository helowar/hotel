package com.mangocity.hotel.dreamweb.search.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.mangocity.hotel.dreamweb.search.dao.HotelQueryAjaxDao;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class HotelQureyAjaxDaoImpl extends GenericDAOHibernateImpl implements HotelQueryAjaxDao{
	
	public JdbcTemplate jdbcTemplate;
	
	public List<String> autoHotelNameQuery(String hotelName, String cityCode){
		String sql = "select distinct(h.chn_name) from htl_hotel h,htl_contract hc where h.city = ? and " +
				" hc.hotel_id = h.hotel_id and h.active = 1 and (h.chn_name like '%"
				+hotelName+"%' or h.eng_name like '%"+hotelName+"%') order by h.chn_name desc";
		Object[] values = new Object[] { cityCode };
		List daoList = super.queryByNativeSQL(sql, 0, 11, values, null);
		List<String> returnList = new ArrayList<String>(); 
		for(int i = 0 ;i < daoList.size();i++){
			Object objArray = daoList.get(i);
			returnList.add(objArray.toString());
		}
		return returnList;
	}
	
	public List findMemberHotelCheckOut(String memberId) {
		//点评酒店显示改为：排除订单状态参数为“NOSHOW”和“已撤单”的。
		
		 String sql="select distinct hotelName,hotelid,memberId from (select oo.hotelName,oo.hotelid,oo.memberId from or_order oo where (oo.orderstate<>? or oo.orderstate<>?)"
				+" and oo.checkoutDate <=trunc(sysdate) and oo.checkoutDate>trunc(sysdate-180) and oo.memberid=?"
				+" order by oo.checkoutDate desc)";
		 return super.queryByNativeSQL(sql, 0, 6, new Object[]{OrderState.NOSHOW,OrderState.CANCEL,memberId}, null);
		 		//return super.query(sql, new Object[]{memberId},0,6,false);
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


}

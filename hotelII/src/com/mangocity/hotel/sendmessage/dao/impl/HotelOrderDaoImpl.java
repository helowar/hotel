package com.mangocity.hotel.sendmessage.dao.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.sendmessage.dao.QueryTargetOrderDao;
import com.mangocity.hotel.sendmessage.handler.AbstractTargetOrderHandler;
import com.mangocity.hotel.sendmessage.model.HotelOrder;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

/**
 * 查询酒店订单
 * @author liting
 *
 */
public class HotelOrderDaoImpl extends GenericDAOHibernateImpl implements QueryTargetOrderDao {

	/**
	 * 查询两个小时前的订单，防止确认订单的时间延后
	 */
	@SuppressWarnings("unchecked")
	public  List<HotelOrder> queryTargetOrder() throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select oo.ordercd,oo.mobile,oo.hotelid,oo.checkindate,oo.checkoutdate,oo.membercd ");
		sql.append(" from Or_Order oo where oo.paymethod = 'pre_pay' ");
		sql.append(" and oo.type = 1");
		sql.append(" and oo.source in ('NET', 'PHE', 'QUNAR', 'MTL')");
		sql.append(" and oo.orderstate = 8 and oo.customerconfirm=1");
		sql.append(" and (oo.membercd is null or oo.membercd <> '0000970198')");
		sql.append(" and oo.createdate >= sysdate - 2 / 24");
		List<Object[]> orderList=super.queryByNativeSQL(sql.toString(), null,null);
		
		return  handleValue(orderList);
	}

	private List<HotelOrder> handleValue(List<Object[]> orderList){
		AbstractTargetOrderHandler handler=new AbstractTargetOrderHandler(){

			@SuppressWarnings("unchecked")
			@Override
			public HotelOrder handleTargetOrderValue(Object[] targetOrder) {
				HotelOrder hotelOrder=new HotelOrder();
				hotelOrder.setOrdercd(targetOrder[0].toString());
				hotelOrder.setMobile(targetOrder[1].toString());
				
				hotelOrder.setHotelId(((BigDecimal)targetOrder[2]).longValue());
				hotelOrder.setCheckInDate((Date)targetOrder[3]);
				hotelOrder.setCheckOutDate((Date)targetOrder[4]);
				hotelOrder.setMenberCd(targetOrder[5].toString());
				return hotelOrder;
			}};
		return handler.handleTemplete(orderList);
	}

}

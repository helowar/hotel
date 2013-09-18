package com.mangocity.tmchotel.service.impl;

import java.util.List;

import com.mangocity.hotel.base.persistence.HtlPresale;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.persistence.HtlSalesPromo;
import com.mangocity.tmchotel.service.HotelDelayLoadService;
import com.mangocity.util.dao.DAOHibernateImpl;

/**
 * 处理延时加载信息的Service实现
 * @author chenjiajie
 *
 */
public class HotelDelayLoadServiceImpl extends DAOHibernateImpl implements HotelDelayLoadService {

	
	/**
	 * 按照大礼包ID查询
	 * @param presaleId
	 * @return
	 */
	public HtlPresale findHtlPresaleById(Long presaleId) {
		return (HtlPresale) super.find(HtlPresale.class, presaleId);
	}
	
	/**
	 * 按照房型ID查询
	 * @param roomTypeId
	 * @return
	 */
	public HtlRoomtype findHtlRoomtype(Long roomTypeId) {
		return (HtlRoomtype) super.find(HtlRoomtype.class, roomTypeId);
	}

	/**
	 * 按照价格类型ID查询酒店促销信息
	 * @param hotelId
	 * @param priceTypeId
	 * @return
	 */
	public List<HtlSalesPromo> findHtlSalesPromo(Long hotelId,String priceTypeId) {
		String hql = "from HtlSalesPromo p" +
				" where p.roomType like '%" + priceTypeId + ",%'" +
						" and p.contractID in " +
						"(select c.ID from HtlContract c where c.hotelId = ?" +
						" and c.beginDate <= trunc(sysdate)" +
						" and c.endDate >= trunc(sysdate))";
		List<HtlSalesPromo> htlSalesPromoList = super.doquery(hql, new Object[]{hotelId}, false);
		return htlSalesPromoList;
	}

}

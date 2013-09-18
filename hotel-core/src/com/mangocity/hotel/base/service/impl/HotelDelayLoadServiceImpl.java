package com.mangocity.hotel.base.service.impl;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.persistence.HtlPresale;
import com.mangocity.hotel.base.persistence.HtlSalesPromo;
import com.mangocity.hotel.base.service.HotelDelayLoadService;
import com.mangocity.util.dao.DAOHibernateImpl;

/**
 * 处理延时加载信息的Service实现
 * @author chenjiajie
 *
 */
public class HotelDelayLoadServiceImpl extends DAOHibernateImpl implements HotelDelayLoadService {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3654187907325292479L;

	/**
	 * 按照大礼包ID查询
	 * @param presaleId
	 * @return
	 */
	public HtlPresale findHtlPresaleById(Long presaleId) {
		return (HtlPresale) super.find(HtlPresale.class, presaleId);
	}

	/**
	 * 按照价格类型ID查询酒店促销信息
	 * @param hotelId
	 * @param priceTypeId
	 * @param beginDate 入住日期
	 * @return
	 */
	public List<HtlSalesPromo> findHtlSalesPromo(Long hotelId,String priceTypeId,Date beginDate) {
		String hql = "from HtlSalesPromo p" +
				" where p.roomType like '%" + priceTypeId + ",%'" +
						" and p.contractID in " +
						"(select c.ID from HtlContract c where c.hotelId = ?" +
						" and c.beginDate <= ?" +
						" and c.endDate >= ?)" +
						" and p.beginDate <= ?" +
						" and p.endDate >= ?";
		List<HtlSalesPromo> htlSalesPromoList = super.doquery(hql, new Object[]{hotelId,beginDate,beginDate,beginDate,beginDate}, false);
		return htlSalesPromoList;
	}

}

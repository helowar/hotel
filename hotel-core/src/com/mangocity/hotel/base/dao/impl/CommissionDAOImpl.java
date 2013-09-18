package com.mangocity.hotel.base.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.base.dao.CommissionDAO;
import com.mangocity.hotel.base.persistence.CommisionAdjust;
import com.mangocity.hotel.base.persistence.CommisionSet;
import com.mangocity.hotel.base.persistence.HtlB2bComminfo;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class CommissionDAOImpl extends GenericDAOHibernateImpl implements CommissionDAO {

	public List<HtlB2bComminfo> queryAllCommission() {
		String hql = " from HtlB2bComminfo b2bComminfo ";
		
		return super.query(hql, null);
	}

	@SuppressWarnings("unchecked")
	public List<CommisionAdjust> queryCommissionAdjust(CommisionAdjust commissionAdjust) {
		List paramList = new ArrayList();
		StringBuilder hql = new StringBuilder();		
		if(commissionAdjust.getB2BCd() != null && !"".equals(commissionAdjust.getB2BCd())) {
			hql.append(" and c.b2BCd = ? ");
			paramList.add(commissionAdjust.getB2BCd());
		}
		
		if(commissionAdjust.getHotelId() != null) {
			hql.append(" and c.hotelId = ? ");
			paramList.add(commissionAdjust.getHotelId());
		}
		
		if(commissionAdjust.getRoomTypeId() != null) {
			hql.append(" and c.roomTypeId = ? ");
			paramList.add(commissionAdjust.getRoomTypeId());
		}
		
		if(commissionAdjust.getChildRoomId() != null) {
			hql.append(" and c.childRoomId = ? ");
			paramList.add(commissionAdjust.getChildRoomId());
		}
		
		if(commissionAdjust.getPayType() != null && !"".equals(commissionAdjust.getPayType())) {
			hql.append(" and c.payType = ? ");
			paramList.add(commissionAdjust.getPayType());
		}
		
		if(commissionAdjust.getHotelStar() != null && !"".equals(commissionAdjust.getHotelStar())) {
			hql.append(" and c.hotelStar = ? ");
			paramList.add(commissionAdjust.getHotelStar());
		}
		
		if(commissionAdjust.getStartDate() != null) {
			hql.append(" and c.startDate = ? ");
			paramList.add(commissionAdjust.getStartDate());
		}
		
		if(commissionAdjust.getEndDate() != null) {
			hql.append(" and c.endDate = ? ");
			paramList.add(commissionAdjust.getEndDate());
		}
		StringBuilder hql_before = new StringBuilder();
		if(hql.length() > 0) {
			hql.delete(0, 4);//删除第一个and
			hql_before.append(" from CommisionAdjust c where ");
			hql_before.append(hql);
		}else{
			hql_before.append(" from CommisionAdjust ");
		}
		
		return super.query(hql_before.toString(), paramList.toArray());
	}

	public List<CommisionSet> queryCommissionSettingByHtlStar(String b2bCd, String hotelStar) {
		String hql = " from CommisionSet c where c.b2BCd = ? and c.hotelStar = ? ";
		
		return super.query(hql, new Object[]{b2bCd, hotelStar});
	}

}

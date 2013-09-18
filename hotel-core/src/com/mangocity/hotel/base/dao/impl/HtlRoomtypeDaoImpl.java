package com.mangocity.hotel.base.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.base.dao.HtlRoomtypeDao;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

/**
*@author chenhuizhong e-mail:huizong.chen@mangocity.com
*@version 1.0
*@since 1.0
*/
public class HtlRoomtypeDaoImpl extends GenericDAOHibernateImpl implements HtlRoomtypeDao {
	
	public HtlRoomtype qryHtlRoomTypeByRoomTypeId(long roomTypeId) {
		return super.get(HtlRoomtype.class, Long.valueOf(roomTypeId));
	}
	
    public List<HtlRoomtype> lstHotelRoomType(long hotelId) {        
        return this.queryByNamedQuery("lstHotelRoomType", new Object[] { Long.valueOf(hotelId) });
    }
    
	public List<HtlRoomtype> qryHtlRoomTypeByHtlId(long hotelId) {
		String hql=" from HtlRoomtype h where h.hotelID = ? ";
		return super.query(hql, new Object[]{Long.valueOf(hotelId)});
	}
	
	public HtlRoomtype qryHtlRoomTypeByIdAndHtlId(long hotelId, long roomTypeId) {
		String hql=" from HtlRoomtype r where r.hotelID = ? and r.ID = ? ";
		List<HtlRoomtype> roomTypeList = super.query(hql, new Object[]{Long.valueOf(hotelId), Long.valueOf(roomTypeId)});
		
		return roomTypeList.isEmpty()?null : roomTypeList.get(0);
	}
	
	public void updateHtlRoomType(HtlRoomtype htlRoomType) {
		super.update(htlRoomType);
	}
		
	public List<HtlRoomtype> getHtlRoomTypeListByHotelIds(String hotelIds) {
		StringBuilder hql= new StringBuilder();
		hql.append(" from HtlRoomtype r where r.hotelID in (");
		
		String[] hotelIdArray = hotelIds.split(",");
		List<Long> paramList = new ArrayList<Long>(hotelIdArray.length);
		for (String hotelId : hotelIdArray) {
        	hql.append("?, ");
            paramList.add(Long.valueOf(hotelId));
        }
		hql.setLength(hql.length() - 2);
        hql.append(")");
        hql.append(" order by r.hotelID ");
        
		return super.query(hql.toString(), paramList.toArray());
	}
}

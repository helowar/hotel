package com.mangocity.hotel.base.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mangocity.hotel.base.dao.HtlRoomtypeDao;
import com.mangocity.hotel.base.manage.assistant.BedTypeBean;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.service.HotelRoomTypeService;

public class HotelRoomTypeServiceImpl implements HotelRoomTypeService {
	
	private HtlRoomtypeDao roomtypeDao;

	public void setRoomtypeDao(HtlRoomtypeDao roomtypeDao) {
		this.roomtypeDao = roomtypeDao;
	}
	
	public void updateHtlRoomType(HtlRoomtype htlRoomtype) {
		roomtypeDao.updateHtlRoomType(htlRoomtype);
	}

	public HtlRoomtype getHtlRoomTypeByRoomTypeId(long roomTypeId) {
		return roomtypeDao.qryHtlRoomTypeByRoomTypeId(roomTypeId);
	}
	
	@SuppressWarnings("unchecked")
	public List<BedTypeBean> queryBedsByRoomId(Long roomTypeId) {		
		HtlRoomtype  roomType = this.getHtlRoomTypeByRoomTypeId(roomTypeId);		
		if(roomType == null || roomType.getBedType() == null || "".equals(roomType.getBedType())){			
			return Collections.EMPTY_LIST;		
		}
				
		String [] bedTypeArray = roomType.getBedType().split(",");//床型id组成的字符串，以","分隔。
		List<BedTypeBean> bedTypeList = new ArrayList<BedTypeBean>(bedTypeArray.length);			
		for(String bedTypeIdStr : bedTypeArray){				
			BedTypeBean bedBean = new  BedTypeBean();
			bedBean.setBedTypeId(Long.valueOf(bedTypeIdStr));//床型类型ID；
			bedBean.setRoomTypeId(roomTypeId);
			bedBean.setQuotaBedShare(roomType.getQuotaBedShare());//配额是否共享 				
			bedTypeList.add(bedBean);				
		}
	    
	    return bedTypeList;
	}
	
	public List<HtlRoomtype> getHtlRoomTypeListByHotelId(long hotelId) {
		return roomtypeDao.qryHtlRoomTypeByHtlId(hotelId); 
	}
	
	public HtlRoomtype getHtlRoomTypeByIdAndHtlId(long hotelId, long roomTypeId) {
		return roomtypeDao.qryHtlRoomTypeByIdAndHtlId(hotelId, roomTypeId); 
	}

	public List<HtlRoomtype> getHtlRoomTypeListByHotelIds(String hotelIds) {
		return roomtypeDao.getHtlRoomTypeListByHotelIds(hotelIds);
	}

}

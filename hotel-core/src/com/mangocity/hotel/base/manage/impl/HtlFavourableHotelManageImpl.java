package com.mangocity.hotel.base.manage.impl;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.base.dao.HtlFavourableHotelDao;
import com.mangocity.hotel.base.manage.HtlFavourableHotelManage;
import com.mangocity.hotel.base.persistence.HtlFavourableHotel;

public class HtlFavourableHotelManageImpl implements HtlFavourableHotelManage {
	private HtlFavourableHotelDao htlFavourableHotelDao;
	
	public void saveOrUpdateAll(List<HtlFavourableHotel> htlFavourableHotels){
		//如果该活动下已存在的酒店 需要先删除然后再作插入
		Long favId = htlFavourableHotels.get(0).getFavId();
		List<Long> hotelIds =new ArrayList<Long>();
		for(HtlFavourableHotel htlFavourableHotel:htlFavourableHotels){
			hotelIds.add(htlFavourableHotel.getHotelId());
		}
		if(hotelIds.size()!=0){
			htlFavourableHotelDao.batchDelete(hotelIds, favId);
		}
		htlFavourableHotelDao.saveOrUpdateAll(htlFavourableHotels);
	}

	public HtlFavourableHotelDao getHtlFavourableHotelDao() {
		return htlFavourableHotelDao;
	}

	public void setHtlFavourableHotelDao(HtlFavourableHotelDao htlFavourableHotelDao) {
		this.htlFavourableHotelDao = htlFavourableHotelDao;
	}

	public void delete(Long htlID, Long parID) {
		
		htlFavourableHotelDao.delete(htlID,parID);
	}

	public void batchDelete(List<Long> hotelIdList, Long parID) {
		htlFavourableHotelDao.batchDelete(hotelIdList,parID);
	}
	
}

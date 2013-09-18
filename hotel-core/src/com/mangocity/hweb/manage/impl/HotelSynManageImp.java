package com.mangocity.hweb.manage.impl;

import com.mangocity.hotel.base.dao.IHotelSynDao;
import com.mangocity.hweb.manage.HotelSynManage;

public class HotelSynManageImp implements HotelSynManage {
	
	private IHotelSynDao hotelSynDao;
	
	/**
	 * 根据ID同步酒店信息 
	 * @param hotelId
	 * @return
	 */

	public void hotelSynByHotelId(String hotelId) {
		hotelSynDao.hotelSynByHotelId(hotelId);
		
	}

	public IHotelSynDao getHotelSynDao() {
		return hotelSynDao;
	}

	public void setHotelSynDao(IHotelSynDao hotelSynDao) {
		this.hotelSynDao = hotelSynDao;
	}
	
	

}

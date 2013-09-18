package com.mangocity.hotel.base.manage.impl;

import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.dao.BatchRoomControlDao;
import com.mangocity.hotel.base.manage.BatchRoomControlManage;

public class BatchRoomControlManageImpl implements BatchRoomControlManage {
	 private BatchRoomControlDao batchRoomControlDao;


	public BatchRoomControlDao getBatchRoomControlDao() {
		return batchRoomControlDao;
	}


	public void setBatchRoomControlDao(BatchRoomControlDao batchRoomControlDao) {
		this.batchRoomControlDao = batchRoomControlDao;
	}

	public List<Map> getHotelRoomType(Long id) {
		return batchRoomControlDao.queryHotelRoomType(id);
	}	 
}

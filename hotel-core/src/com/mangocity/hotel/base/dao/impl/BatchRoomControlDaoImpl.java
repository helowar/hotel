package com.mangocity.hotel.base.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.mangocity.hotel.base.dao.BatchRoomControlDao;
import com.mangocity.util.dao.DAOIbatisImpl;

public class BatchRoomControlDaoImpl extends DAOIbatisImpl implements BatchRoomControlDao {

	private static final long serialVersionUID = 1L;

	
	@SuppressWarnings("unchecked")
	public List<Map> queryHotelRoomType(Long id) {
		SqlMapClientTemplate templet=super.getSqlMapClientTemplate();
		return templet.queryForList("selectBatchOpenCloseRoom",
	            id);
	}
}

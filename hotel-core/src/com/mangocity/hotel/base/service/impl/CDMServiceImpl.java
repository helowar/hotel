package com.mangocity.hotel.base.service.impl;

import java.sql.SQLException;

import com.mangocity.framework.base.model.Tree;
import com.mangocity.hotel.base.service.ICDMService;
import com.mangocity.util.dao.DAOIbatisImpl;
import com.mangocity.util.getcdm.GetCDMUtil;

public class CDMServiceImpl implements ICDMService{

	DAOIbatisImpl queryDao;
	public Tree getParamsByPath(String path) {
		try{
		 return GetCDMUtil.getParamsByPath(path,queryDao.getDataSource().getConnection());
		}catch(SQLException e){
			throw new RuntimeException("Can't not get CDM data..",e);
		}
	}
	public void setQueryDao(DAOIbatisImpl queryDao) {
		this.queryDao = queryDao;
	}

	
}

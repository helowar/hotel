package com.mangocity.hotel.base.service.impl;

import java.util.List;

import com.mangocity.framework.base.model.Tree;
import com.mangocity.hotel.base.dao.SystemDataDAO;
import com.mangocity.hotel.base.persistence.HtlArea;
import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.hotel.base.service.SystemDataService;

public class SystemDataServiceImpl implements SystemDataService {
	
	private SystemDataDAO systemDataDAO;

	public HtlArea queryAreaCode(String cityCode) {
		return systemDataDAO.queryAreaCode(cityCode);
	}
	
	public OrParam getSysParamByName(String paramName) {
		return systemDataDAO.querySysParamByName(paramName);
	}
	
	public void updateSysParamByName(OrParam sysParam) {
		systemDataDAO.updateSysParamByName(sysParam);
	}
	
	public List<String> queryID() {
		return systemDataDAO.queryHotelIDforUpd();
	}
	
	public Tree getParamsByPath(String path) {
        return systemDataDAO.qryCDMDataByPath(path);
    }
	
	public void setSystemDataDAO(SystemDataDAO systemDataDAO) {
		this.systemDataDAO = systemDataDAO;
	}
}

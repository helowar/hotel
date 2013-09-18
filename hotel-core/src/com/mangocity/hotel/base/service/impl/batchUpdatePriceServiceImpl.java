package com.mangocity.hotel.base.service.impl;

import java.util.List;

import com.mangocity.hotel.base.dao.BatchUpdatePriceDAO;
import com.mangocity.hotel.base.persistence.BatchUpdateParam;
import com.mangocity.hotel.base.persistence.HtlSupplierInfo;
import com.mangocity.hotel.base.service.BatchUpdatePriceService;
import com.mangocity.hotel.user.UserWrapper;


/**
 * 批量更新价格 
 * @author donglei
 *
 */
public class batchUpdatePriceServiceImpl implements BatchUpdatePriceService{

	private BatchUpdatePriceDAO batchUpdatePriceDAO;
	
	public List<HtlSupplierInfo> getSupplyId() {
		
		return batchUpdatePriceDAO.getSupplyId();
	}
    
	
	
	public List<String[]> getExportData(BatchUpdateParam bachUpdateParam) {
		// TODO Auto-generated method stub
		return batchUpdatePriceDAO.getExportData(bachUpdateParam);
	}

    

	public void batchUpdatePrice(List<String[]> importData,UserWrapper roleUser,BatchUpdateParam bachUpdateParam) {
		// TODO Auto-generated method stub
		batchUpdatePriceDAO.batchUpdatePrice(importData,roleUser,bachUpdateParam);
	}



	public BatchUpdatePriceDAO getBatchUpdatePriceDAO() {
		return batchUpdatePriceDAO;
	}

	public void setBatchUpdatePriceDAO(BatchUpdatePriceDAO batchUpdatePriceDAO) {
		this.batchUpdatePriceDAO = batchUpdatePriceDAO;
	};
    
	
}

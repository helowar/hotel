package com.mangocity.hotel.base.dao;

import java.util.List;


import com.mangocity.hotel.base.persistence.BatchUpdateParam;
import com.mangocity.hotel.base.persistence.HtlSupplierInfo;
import com.mangocity.hotel.user.UserWrapper;


/**
 * 批量更新价格 
 * @author donglei
 *
 */
public interface BatchUpdatePriceDAO {

	
	public List<HtlSupplierInfo> getSupplyId();
	
	public List<String []> getExportData(BatchUpdateParam bachUpdateParam);
    
	public void batchUpdatePrice(List<String []> importData,UserWrapper roleUser,BatchUpdateParam bachUpdateParam);
}

package com.mangocity.webnew.service;


import java.util.Date;
import java.util.List;

import com.mangocity.webnew.persistence.QueryHotelForSalesResult;


/**
 * 网站查询接口
 * @author chenjiajie
 *
 */
public interface MangoSalesService {
	
	/**
	 * 酒店查询接口
	 */
	public List<QueryHotelForSalesResult> queryHotelsForSales();
	
	
	public List getRoomType(Long hotelid,Long roomTypeID,Long priceTypeId);
	
	
	public List getSalePrice(Long hotelid, Long roomTypeID, Long priceTypeId,Date currDate);
	
}

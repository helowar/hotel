package com.mangocity.hotel.ejb.search;

import java.util.List;

import com.mangocity.hotel.search.dto.HotelQuotationQueryInfoDTO;
import com.mangocity.hotel.search.dto.HtlQueryDTO;
import com.mangocity.hotel.search.dto.HtlQuotationQueryDTO;
import com.mangocity.hotel.search.handler.AbstractHotelQueryHandler;

public interface HotelSearchEJB {

	/**
	 * 查询EJB
	 */
	public void searchHotelsForPage(HtlQueryDTO htlQueryDTO,AbstractHotelQueryHandler handler);
	
	/**
	 * 查询一个价格类型的报价
	 * @param quotationQueryDTO
	 * @return
	 */
	public List<HotelQuotationQueryInfoDTO> searchHotelQuotationQueryInfo(HtlQuotationQueryDTO quotationQueryDTO);
}

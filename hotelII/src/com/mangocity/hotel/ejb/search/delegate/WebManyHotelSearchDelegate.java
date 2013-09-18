package com.mangocity.hotel.ejb.search.delegate;

import java.util.Map;

import com.mangocity.hotel.dreamweb.comment.service.impl.DaoDaoCommentServiceImpl;
import com.mangocity.hotel.search.comm.AbstractConstructHotel;
import com.mangocity.hotel.search.delegate.ManyHotelSearchDelegate;
import com.mangocity.hotel.search.dto.HtlQueryDTO;
import com.mangocity.hotel.search.exception.HotelSearchDelegateException;
import com.mangocity.hotel.search.handler.AbstractHotelQueryHandler;
import com.mangocity.util.log.MyLog;

public class WebManyHotelSearchDelegate extends ManyHotelSearchDelegate {
	
	private static final MyLog log = MyLog.getLogger(WebManyHotelSearchDelegate.class);
    private AbstractConstructHotel constructHotelHandler;
	
	@Override
	public void searchHotelsForPage(HtlQueryDTO queryDTO,
			AbstractHotelQueryHandler handler, Map<String, Integer> requestMap) {
		try{			
			this.constructHotelHandler.setQueryDTO(queryDTO);
			this.constructHotelHandler.setHandler(handler);
			this.constructHotelHandler.setRequestMap(requestMap);

			
			long startDate1 = System.currentTimeMillis();
			constructHotelHandler.searchHotelIds();
			long startDate2 = System.currentTimeMillis();
			log.info("client --searchHotelIds-->"+(startDate2-startDate1));
			constructHotelHandler.sortAndFilterHotel();
			long startDate3 = System.currentTimeMillis();
			log.info("client --sortAndFilterHotel-->"+(startDate3-startDate2));
			constructHotelHandler.searchHotelBaseInfo();
			long startDate4 = System.currentTimeMillis();
			System.out.println("client --searchHotelBaseInfo-->"+(startDate4-startDate3));
			constructHotelHandler.combinationHotel();
			long startDate5 = System.currentTimeMillis();
			log.info("client --combinationHotel-->"+(startDate5-startDate4));
			constructHotelHandler.destroy();
			long startDate6 = System.currentTimeMillis();
			System.out.println("client --Total------->"+(startDate6-startDate1));
		}catch(Exception e){
			throw new HotelSearchDelegateException("searchHotelsForPage",e);
		}
	}

	public AbstractConstructHotel getConstructHotelHandler() {
		return constructHotelHandler;
	}

	public void setConstructHotelHandler(
			AbstractConstructHotel constructHotelHandler) {
		this.constructHotelHandler = constructHotelHandler;
	}
	
	

}

package com.mangocity.hotel.ejb.search.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.ejb.search.HotelSearchEJB;
import com.mangocity.hotel.search.delegate.BaseSearchDelegate;
import com.mangocity.hotel.search.delegate.ManyHotelSearchDelegate;
import com.mangocity.hotel.search.dto.HotelQuotationQueryInfoDTO;
import com.mangocity.hotel.search.dto.HtlQueryDTO;
import com.mangocity.hotel.search.dto.HtlQuotationQueryDTO;
import com.mangocity.hotel.search.handler.AbstractHotelQueryHandler;
import com.mangocity.hotel.search.util.DelegateUtil;

public abstract class HotelSearchEJBImpl implements HotelSearchEJB {

	
	private ManyHotelSearchDelegate manyHotelSearchDelegate;
	private BaseSearchDelegate baseSearchDelegate;
	public void searchHotelsForPage(HtlQueryDTO htlQueryDTO, AbstractHotelQueryHandler handler) {
		 manyHotelSearchDelegate = createManyHotelSearchDelegate();
	     manyHotelSearchDelegate.searchHotelsForPage(htlQueryDTO, handler,DelegateUtil.ccReqeustMap); 

	}
	
	/**
	 * 通过hotelId、priceTypeId查询一个价格类型，返回是一个价格类型
	 */
	public List<HotelQuotationQueryInfoDTO> searchHotelQuotationQueryInfo(HtlQuotationQueryDTO quotationQueryDTO){

		List<HotelQuotationQueryInfoDTO> quotationQueryInfos = baseSearchDelegate.queryHotelQuotation(quotationQueryDTO);
		List<HotelQuotationQueryInfoDTO> needQuotationQueryInfos=new ArrayList<HotelQuotationQueryInfoDTO>();
		if (quotationQueryInfos != null && quotationQueryInfos.size() > 0) {
			List<String> queryPriceTypeIds=quotationQueryDTO.getCommodityIDList();
			if(queryPriceTypeIds!=null && queryPriceTypeIds.size()>0){
				
				String querypriceTypeId=queryPriceTypeIds.get(0);
				String priceTypeId=null;
				for(HotelQuotationQueryInfoDTO quotationQueryInfo:quotationQueryInfos){
					priceTypeId=String.valueOf(quotationQueryInfo.getCommodityId());
					if(querypriceTypeId!=null && querypriceTypeId.equals(priceTypeId)){
						if(!filterCommonQuotationQueryInfo(quotationQueryInfo,needQuotationQueryInfos)){
							needQuotationQueryInfos.add(quotationQueryInfo);
						}
												
					}
					
				}
			
			}
			
		} 		
		return needQuotationQueryInfos;
		
	}
	
	/**
	 * 排除同一天不同床型的报价信息，只保留同一天的一条记录
	 * @param quotationQueryInfo
	 * @param needQuotationQueryInfos
	 * @return
	 */
	private boolean filterCommonQuotationQueryInfo(HotelQuotationQueryInfoDTO quotationQueryInfo,List<HotelQuotationQueryInfoDTO> needQuotationQueryInfos){
		boolean fag=false;
		if(needQuotationQueryInfos==null ||needQuotationQueryInfos.size()<=0){
			fag=false;
		}
		else{
			Date ableSallDate=quotationQueryInfo.getAbledate();
			String payMethod=quotationQueryInfo.getPaymethod();
			for(HotelQuotationQueryInfoDTO tempQuotationQueryInfo:needQuotationQueryInfos){
				boolean fagDateEquals=(ableSallDate!=null && ableSallDate.equals(tempQuotationQueryInfo.getAbledate()));
				boolean fagPayMethodEquals=(payMethod!=null && payMethod.equals(tempQuotationQueryInfo.getPaymethod()));
				if(fagDateEquals && fagPayMethodEquals ){
					fag=true;
					break;
				}
			}
		}
		return fag;
	}

	public abstract ManyHotelSearchDelegate createManyHotelSearchDelegate();

	public void setBaseSearchDelegate(BaseSearchDelegate baseSearchDelegate) {
		this.baseSearchDelegate = baseSearchDelegate;
	}
	

	
  
}

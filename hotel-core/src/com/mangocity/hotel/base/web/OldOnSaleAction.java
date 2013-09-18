package com.mangocity.hotel.base.web;

import java.util.Date;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlOnSale;
import com.mangocity.hotel.base.service.IOldOnSaleService;
import com.mangocity.hotel.base.web.webwork.GenericAction;
import com.mangocity.util.DateUtil;

public class OldOnSaleAction extends GenericAction {
	
	private IOldOnSaleService oldOnSaleService;   //上下架service
	
	/**
	 * 保存上下架信息
	 * @return
	 */
	public String saveOnSale(){
		Map params=getParams();
		
		HtlOnSale onSale=new HtlOnSale();
		onSale.setCommodityId(Long.valueOf((String)params.get("priceTypeId")));
		onSale.setSaleChannelId(Integer.valueOf((String)params.get("oldSaleChannel")));
		
		Date beginDate=DateUtil.getDate((String)params.get("beginDate"));
		Date endDate=DateUtil.getDate((String)params.get("endDate"));
		Date checkIn=DateUtil.getDate((String)params.get("checkIn"));
		Date checkOut=DateUtil.getDate((String)params.get("checkOut"));
		
		onSale.setSaleStartDate(beginDate);
		onSale.setSaleEndDate(endDate);
		onSale.setCheckStartDate(checkIn);
		onSale.setCheckEndDate(checkOut);
		onSale.setCreator(super.getOnlineRoleUser().getName());
		onSale.setCreateDate(new Date());
		
		if("on".equalsIgnoreCase((String)params.get("onsaleType"))){
			oldOnSaleService.addOldCommdityOnSale(onSale);
		}else if("off".equalsIgnoreCase((String)params.get("onsaleType"))){
			oldOnSaleService.addOldCommdityOffSale(onSale);
		}
		return SUCCESS;
	}

	public void setOldOnSaleService(IOldOnSaleService oldOnSaleService) {
		this.oldOnSaleService = oldOnSaleService;
	}
}

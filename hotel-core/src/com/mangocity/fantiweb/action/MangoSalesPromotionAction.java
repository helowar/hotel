package com.mangocity.fantiweb.action;


import java.util.List;


import com.mangocity.webnew.service.MangoSalesService;
import com.mangocity.webnew.util.MangoSalesUtil;

public class MangoSalesPromotionAction extends GenericWebAction{
	
	private static final long serialVersionUID = -8331487657049289041L;
	
	private List salesHotelResultLis;
	
	private MangoSalesService mangoSalesService;
    
    //跳转的页面
    private  static final String MANGO_SALES_PROMOTION = "mangoSalesPromotion";

	public String execute(){
		
		MangoSalesUtil.initSalesInfo();
		
		salesHotelResultLis =  mangoSalesService.queryHotelsForSales();

		return MANGO_SALES_PROMOTION ;
	}

	public MangoSalesService getMangoSalesService() {
		return mangoSalesService;
	}


	public void setMangoSalesService(MangoSalesService mangoSalesService) {
		this.mangoSalesService = mangoSalesService;
	}


	public List getSalesHotelResultLis() {
		return salesHotelResultLis;
	}


	public void setSalesHotelResultLis(List salesHotelResultLis) {
		this.salesHotelResultLis = salesHotelResultLis;
	}
	
}

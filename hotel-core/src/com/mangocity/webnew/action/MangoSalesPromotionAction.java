package com.mangocity.webnew.action;


import java.util.List;


import com.mangocity.webnew.service.MangoSalesService;
import com.mangocity.webnew.util.MangoSalesUtil;
import com.mangocity.webnew.util.action.GenericWebAction;

public class MangoSalesPromotionAction extends GenericWebAction{
	
	private static final long serialVersionUID = -8331487657049289041L;
	
	private List salesHotelResultLis;
	
	private MangoSalesService mangoSalesService;
    
    //跳转的页面
    private  static final String MANGO_SALES_PROMOTION = "mangoSalesPromotion";
    
    //跳转的ajax页面
    private  static final String MANGO_SALES_AJAX = "mangoSalesAjax";
    
    //酒店ID的字符串
    private String allHotelIdString;
    
    //酒店id，房型ID，子房型ID，停止售卖的日期
    private String allSaleHotelString;

	public String execute(){
		return MANGO_SALES_PROMOTION ;
	}
	
	public  String  getSaleHotelInfo(){
		if(null!=allHotelIdString && !"".equals(allHotelIdString) &&
				   null!=allSaleHotelString && !"".equals(allSaleHotelString)	
				){
					MangoSalesUtil.initMapInfo(allHotelIdString,allSaleHotelString);
				}
				
				MangoSalesUtil.initSalesInfo();
				
				salesHotelResultLis =  mangoSalesService.queryHotelsForSales();
		
		return  MANGO_SALES_AJAX;
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

	public String getAllHotelIdString() {
		return allHotelIdString;
	}

	public void setAllHotelIdString(String allHotelIdString) {
		this.allHotelIdString = allHotelIdString;
	}

	public String getAllSaleHotelString() {
		return allSaleHotelString;
	}

	public void setAllSaleHotelString(String allSaleHotelString) {
		this.allSaleHotelString = allSaleHotelString;
	}
	
}

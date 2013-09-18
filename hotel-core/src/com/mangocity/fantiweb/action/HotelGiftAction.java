package com.mangocity.fantiweb.action;

import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlPresale;
import com.mangocity.hotel.base.service.HotelDelayLoadService;
import com.mangocity.util.StringUtil;

/**
 * 酒店网站异步取大礼包Action
 * @author chenjiajie
 *
 */
public class HotelGiftAction extends GenericWebAction{
	
	/**
	 * 处理延时加载信息的Service 
	 */
	private HotelDelayLoadService hotelDelayLoadService;
	
	/**
	 * 返回页面的HtlPresale对象
	 */
	private HtlPresale htlPresale;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7501394652030404222L;

	public String execute(){
		Map params = super.getParams();
		String hotelIdStr = (String)params.get("hotelId");
		String preSaleId = (String)params.get("preSaleId");
		if(StringUtil.isValidStr(preSaleId)){
			htlPresale = hotelDelayLoadService.findHtlPresaleById(new Long(preSaleId));
		}
		return SUCCESS;
	}

	/** getter and setter **/
	public HotelDelayLoadService getHotelDelayLoadService() {
		return hotelDelayLoadService;
	}

	public void setHotelDelayLoadService(HotelDelayLoadService hotelDelayLoadService) {
		this.hotelDelayLoadService = hotelDelayLoadService;
	}

	public HtlPresale getHtlPresale() {
		return htlPresale;
	}

	public void setHtlPresale(HtlPresale htlPresale) {
		this.htlPresale = htlPresale;
	}
}

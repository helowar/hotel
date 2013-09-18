package com.mangocity.webnew.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlSalesPromo;
import com.mangocity.hotel.base.service.HotelDelayLoadService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.webnew.util.action.GenericWebAction;

/**
 * 酒店网站异步取酒店促销信息Action
 * @author chenjiajie
 *
 */
public class HtlSalesPromoAction extends GenericWebAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5001020624331745616L;

	/**
	 * 处理延时加载信息的Service 
	 */
	private HotelDelayLoadService hotelDelayLoadService;
	
	/**
	 * 页面用
	 */
	private List<HtlSalesPromo> htlSalesPromoList;
	
	public String execute(){
		Map params = super.getParams();
		String hotelIdStr = (String)params.get("hotelId");
		String childRoomTypeId = (String)params.get("childRoomTypeId");
		String beginDateStr = (String)params.get("beginDate");
		if(StringUtil.isValidStr(hotelIdStr) 
				&& StringUtil.isValidStr(childRoomTypeId)
				&& StringUtil.isValidStr(beginDateStr)){
			Date beginDate = DateUtil.getDate(beginDateStr);
			htlSalesPromoList = hotelDelayLoadService.findHtlSalesPromo(new Long(hotelIdStr), childRoomTypeId,beginDate);
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

	public List<HtlSalesPromo> getHtlSalesPromoList() {
		return htlSalesPromoList;
	}

	public void setHtlSalesPromoList(List<HtlSalesPromo> htlSalesPromoList) {
		this.htlSalesPromoList = htlSalesPromoList;
	}
}

package com.mangocity.webco.action;

import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.util.StringUtil;
import com.mangocity.webnew.util.action.GenericWebAction;

/**
 * 酒店网站异步取房型明细Action
 * @author chenjiajie
 *
 */
public class HotelRoomTypeDetailAction extends GenericWebAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7340083099990143342L;

	private HotelRoomTypeService hotelRoomTypeService;
	
	/**
	 * 页面用
	 */
	private HtlRoomtype htlRoomtype;
	
	public String execute(){
		Map params = super.getParams();
		String roomTypeId = (String)params.get("roomTypeId");
		if(StringUtil.isValidStr(roomTypeId)){
			htlRoomtype = hotelRoomTypeService.getHtlRoomTypeByRoomTypeId(Long.parseLong(roomTypeId));
		}
		return SUCCESS;
	}

	public HtlRoomtype getHtlRoomtype() {
		return htlRoomtype;
	}

	public void setHtlRoomtype(HtlRoomtype htlRoomtype) {
		this.htlRoomtype = htlRoomtype;
	}

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}
}

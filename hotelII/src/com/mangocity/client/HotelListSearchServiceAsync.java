package com.mangocity.client;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mangocity.client.hotel.gwt.queryCondition.GWTQueryCondition;
import com.mangocity.hotel.search.vo.HotelPageForWebBean;
import com.mangocity.hotel.search.vo.HotelTemplateVO;

public interface HotelListSearchServiceAsync {

	void searchHotelListInfo(GWTQueryCondition gwtQueryCondtion,
			AsyncCallback<HotelPageForWebBean> callback);
	
	void searchHotelListInfo(String hotelIdsStr,GWTQueryCondition gwtQueryCondition,
			AsyncCallback<HotelPageForWebBean> callback);
	
	void getHotelTemplate(GWTQueryCondition gwtQueryCondition,
			AsyncCallback<HotelTemplateVO> callback);
	
	
	void getHotelBizDistrictByCityName(String cityName, String bizDistrict,
			AsyncCallback<Map<String, String>> callback);

	void getHotelTransportHub(String cityName,
			AsyncCallback<Map<String, String>> callback);
	
	void sameObject(HotelPageForWebBean hotelpageForWebBean,
			AsyncCallback<HotelPageForWebBean> callback);

}

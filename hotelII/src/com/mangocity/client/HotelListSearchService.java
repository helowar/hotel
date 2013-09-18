package com.mangocity.client;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.mangocity.client.hotel.gwt.queryCondition.GWTQueryCondition;
import com.mangocity.hotel.search.vo.HotelPageForWebBean;
import com.mangocity.hotel.search.vo.HotelTemplateVO;
@RemoteServiceRelativePath("hotelList")
public interface HotelListSearchService extends RemoteService {
 	/**
	  * 根据查询条件查找酒店结果页bean.
	  *
	  * @param  gwtQueryCondtion    查询条件bean
	  * @return   HotelPageForWebBean 返回酒店结果页bean 
	  */
     public HotelPageForWebBean searchHotelListInfo(GWTQueryCondition gwtQueryCondtion);
     
     /**
      * 根据酒店id和条件（日期）查询酒店
      */
     public HotelPageForWebBean searchHotelListInfo(String hotelIdsStr,GWTQueryCondition gwtQueryCondition);//add by diandian.hou
     
     /**
      * 得到酒店片段
      */
     public HotelTemplateVO getHotelTemplate(GWTQueryCondition gwtQueryCondition);// add by diandian.hou
     
 	/**
	  * 根据城市查询商业区/行政区.
	  *
	  * @param  cityName    城市
	  * @param  bizDistrict 商业区/行政区,business为商业区，district为行政区
	  * @return   Map<String,String> 返回商业区/行政区键值对 
	  */
	Map<String,String> getHotelBizDistrictByCityName(String cityName,String bizDistrict);
	
	/**
	  * 根据城市查询交通枢纽.
	  *
	  * @param  cityName    城市
	  * @return  Map<String,String> 返回查询到的交通枢纽键值对 
	  */
	Map<String,String> getHotelTransportHub(String cityName);
	
	/**
	 * this method only is used to Asyc 返回本身对象
	 */
	public HotelPageForWebBean sameObject(HotelPageForWebBean hotelpageForWebBean);
}

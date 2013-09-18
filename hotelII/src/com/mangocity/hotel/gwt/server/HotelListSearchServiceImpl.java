package com.mangocity.hotel.gwt.server;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.context.WebApplicationContext;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mangocity.client.HotelListSearchService;
import com.mangocity.client.hotel.gwt.queryCondition.GWTQueryCondition;
import com.mangocity.hotel.base.persistence.HtlGeographicalposition;
import com.mangocity.hotel.base.service.GeographicalPositionService;
import com.mangocity.hotel.search.handler.impl.HotelQueryHandler;
import com.mangocity.hotel.search.model.QueryHotelCondition;
import com.mangocity.hotel.search.service.HotelSearchService;
import com.mangocity.hotel.search.template.HotelListShowVm;
import com.mangocity.hotel.search.vo.HotelPageForWebBean;
import com.mangocity.hotel.search.vo.HotelResultVO;
import com.mangocity.hotel.search.vo.HotelTemplateVO;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.log.MyLog;
import com.mangocity.util.web.WebUtil;

public class HotelListSearchServiceImpl extends RemoteServiceServlet implements HotelListSearchService {

	private GeographicalPositionService geographicalPositionService;
	private WebApplicationContext springContext;
	private static final MyLog log = MyLog.getLogger(HotelListSearchServiceImpl.class);
		
	public HotelPageForWebBean searchHotelListInfo(GWTQueryCondition gwtQueryCondition){
//			ApplicationContext context = new ClassPathXmlApplicationContext(
//				"classpath:spring/applicationContext.xml");
		try{
			 HotelSearchService hotelSearchService = (HotelSearchService)WebUtil.getBean(
	                 getServletContext(), "hotelSearchService");
			 QueryHotelCondition queryHotelCondition = new QueryHotelCondition();
			 
			 
			 MyBeanUtil.copyProperties(queryHotelCondition,gwtQueryCondition);
              
			 //支付包用 add by diandian.hou 2011-12-13
			 if (null == queryHotelCondition.getInDate() || null == queryHotelCondition.getOutDate()) {
				 String inDate = DateUtil.dateToString(DateUtil.getDate(DateUtil.getSystemDate(), 14));
				 String outDate = DateUtil.dateToString(DateUtil.getDate(DateUtil.getSystemDate(),15));
				 queryHotelCondition.setInDate( DateUtil.getDate(inDate));
				 queryHotelCondition.setOutDate(DateUtil.getDate(outDate));
		        }
			 //TODO test
			 //queryHotelCondition.setHotelId("30000640");
			 HotelQueryHandler handler = new HotelQueryHandler();
			 handler.setQueryHotelCondition(queryHotelCondition);
			 hotelSearchService.queryHotelsByHandler(queryHotelCondition, handler);
			 List<HotelResultVO> list = handler.getHotelResutlList();
			 int hotelCount = handler.getHotelCount();	
			 HotelPageForWebBean hotelPageForWebBean = new HotelPageForWebBean();
			 hotelPageForWebBean.setList(list);
			 //分页的
			 hotelPageForWebBean.setPageSize(gwtQueryCondition.getPageSize());
			 hotelPageForWebBean.setPageIndex(gwtQueryCondition.getPageNo());
		     hotelPageForWebBean.setTotalSize(hotelCount);
		     
		     return hotelPageForWebBean;
		}catch(Exception e){
			log.error("hotelListSearchService error：",e);
			return null;
		}
	}
	
	// add by diandian.hou
	public HotelPageForWebBean searchHotelListInfo(String hotelIdsStr,GWTQueryCondition gwtQueryCondition){
		try{
			if(StringUtil.isValidStr(hotelIdsStr)){
			 HotelSearchService hotelSearchService = (HotelSearchService)WebUtil.getBean(
	                 getServletContext(), "hotelSearchService");
			 QueryHotelCondition queryHotelCondition = new QueryHotelCondition();
			 MyBeanUtil.copyProperties(queryHotelCondition,gwtQueryCondition);
			 HotelQueryHandler handler = new HotelQueryHandler();
			 hotelSearchService.queryHotelsByHandler(hotelIdsStr,queryHotelCondition,handler);
			 List<HotelResultVO> list = handler.getHotelResutlList();
			 int hotelCount = handler.getHotelCount();	
			 HotelPageForWebBean hotelPageForWebBean = new HotelPageForWebBean();
			 hotelPageForWebBean.setHotelIdsStr(hotelIdsStr);
			 hotelPageForWebBean.setList(list);
			 //分页的
			 hotelPageForWebBean.setPageSize(gwtQueryCondition.getPageSize());
			 hotelPageForWebBean.setPageIndex(gwtQueryCondition.getPageNo());
		     hotelPageForWebBean.setTotalSize(hotelCount);
		     
		     return hotelPageForWebBean;
			} 
			else{
				return null;
			}
		}catch(Exception e){
			log.error("hotelListSearchService error：",e);
			return null;
		}
	}
	
	//this method only is used to asyc
	public HotelPageForWebBean sameObject(HotelPageForWebBean hotelpageForWebBean){
		return hotelpageForWebBean;
	}
	
	public HotelTemplateVO getHotelTemplate(GWTQueryCondition gwtQueryCondition){
		try{
		HotelSearchService hotelSearchService = (HotelSearchService)WebUtil.getBean(
                getServletContext(), "hotelSearchService");
		QueryHotelCondition queryHotelCondition = new QueryHotelCondition();
		MyBeanUtil.copyProperties(queryHotelCondition,gwtQueryCondition);
		HotelQueryHandler handler = new HotelQueryHandler();
		handler.setQueryHotelCondition(queryHotelCondition);
		hotelSearchService.queryOnlyHotelsByHandler(queryHotelCondition, handler);
		
		List<HotelResultVO> hotelVOList = handler.getHotelResutlList();
		if(gwtQueryCondition.isFagReQuery()){
			hotelVOList=getTopFiveHotel(hotelVOList);
		}
		String hotelIdsStr ="";
		for(int i = 0 ; i< hotelVOList.size();i++){
			String hotelId = String.valueOf(hotelVOList.get(i).getHotelId());
        	hotelIdsStr +=hotelId+",";
        }
        hotelIdsStr = StringUtil.deleteLastChar(hotelIdsStr, ',');

		HotelListShowVm vm = new HotelListShowVm();
    	String hotelListOut = "";
    	for(int i = 0 ; i< hotelVOList.size();i++){
    		String  hotelListOut1 =vm.getHotelListWithTemplate(hotelVOList.get(i));
    		hotelListOut += hotelListOut1;
    	}
    	HotelTemplateVO hotelTemplateVO = new HotelTemplateVO();
    	hotelTemplateVO.setHotelIdsStr(hotelIdsStr);
    	hotelTemplateVO.setHotelListOut(hotelListOut);
    	hotelTemplateVO.setHotelCount(handler.getHotelCount());
    	return hotelTemplateVO;
		}catch(Exception e){
			log.error("getHotelTemplate error：",e);
			return new HotelTemplateVO();
		}
	}
	/**
	 * 查不到酒店的时候，默认返回前5个酒店
	 * @param hotelVOList
	 * @return
	 */
	private List<HotelResultVO> getTopFiveHotel(List<HotelResultVO> hotelVOList){
		int size=hotelVOList.size();
		if(size<5){
			return hotelVOList;
		}
		else{
			List<HotelResultVO> subHotelVOList=hotelVOList.subList(0, 5);
			return subHotelVOList;
		}
		
	}
	@SuppressWarnings("unchecked")
	public Map<String,String> getHotelBizDistrictByCityName(String cityName,String bizDistrict) {
		Map map = getGeographicalPositionService().queryBusinessForCityName(cityName);
		List<Object[]> bizlist = (List) map.get(bizDistrict);
		Map<String,String> bizMap = new HashMap();
		for(Object[] bizObj : bizlist){
			bizMap.put(bizObj[3].toString(), bizObj[1].toString());
		}
		return bizMap;
	}

	
	@SuppressWarnings("unchecked")
	public Map<String,String> getHotelTransportHub(String cityName) {
		List<HtlGeographicalposition> thlist = getGeographicalPositionService().queryPositinList(cityName, 21L);
		System.out.println("hotelth:"+thlist.size()+cityName);
		Map<String,String> thubMap = new HashMap();
		for(HtlGeographicalposition htlgp : thlist){
			thubMap.put(htlgp.getID().toString(), htlgp.getName());
		}
		return thubMap;
	}
	
	private GeographicalPositionService getGeographicalPositionService(){
	/*	GeographicalPositionService geographicalPositionService = (GeographicalPositionService) new ClassPathXmlApplicationContext(
		"classpath:spring/applicationContext-hotelSearch.xml")
		.getBean("geographicalPositionService");;*/
		GeographicalPositionService geographicalPositionService = (GeographicalPositionService)WebUtil.getBean(
                 getServletContext(), "geographicalPositionService");
		return geographicalPositionService;
	}

	public void setGeographicalPositionService(
			GeographicalPositionService geographicalPositionService) {
		this.geographicalPositionService = geographicalPositionService;
	}
	
	
}

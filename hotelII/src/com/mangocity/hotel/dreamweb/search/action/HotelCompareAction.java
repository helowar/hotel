package com.mangocity.hotel.dreamweb.search.action;

import java.util.List;

import com.mangocity.hotel.search.handler.impl.HotelQueryHandler;
import com.mangocity.hotel.search.model.QueryHotelCondition;
import com.mangocity.hotel.search.service.HotelSearchService;
import com.mangocity.hotel.search.service.IHotelQueryHandler;
import com.mangocity.hotel.search.vo.HotelResultVO;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.hotel.constant.SaleChannel;
import com.mangocity.util.log.MyLog;
import com.opensymphony.xwork2.ActionSupport;

public class HotelCompareAction extends ActionSupport {
	//进入的条件
	private String comparedHotelIdsStr;
	private String compareInDate;
	private String compareOutDate;
	private String compareCityCode;
	
	//传出的结果
	private List<HotelResultVO> hotelVOList;
	
	// 注入
	private HotelSearchService hotelSearchService;
	private static final MyLog log = MyLog.getLogger(HotelCompareAction.class);
	
	public String execute(){
		comparedHotelIdsStr = StringUtil.deleteLastChar(comparedHotelIdsStr, ',');// 去掉最后一个,
		try{
			QueryHotelCondition queryHotelCondition = initQueryCondition();
			HotelQueryHandler handler = new HotelQueryHandler();
			hotelSearchService.queryHotelsByHandler(comparedHotelIdsStr, queryHotelCondition, handler);
			hotelVOList = handler.getHotelResutlList();
           }
		catch(Exception e){
			log.error("hotel-compare error:",e);
		}
		return SUCCESS;
	}
	
	
	//初始化查询条件
	private QueryHotelCondition initQueryCondition(){
		QueryHotelCondition queryHotelCondition = new QueryHotelCondition();
		queryHotelCondition.setCityCode(compareCityCode);
		queryHotelCondition.setInDate(DateUtil.stringToDateMain(compareInDate,"yyyy-MM-dd"));
		queryHotelCondition.setOutDate(DateUtil.stringToDateMain(compareOutDate,"yyyy-MM-dd"));
		queryHotelCondition.setFromChannel(SaleChannel.WEB);
		
		return queryHotelCondition;
	}

	public void setComparedHotelIdsStr(String comparedHotelIdsStr) {
		this.comparedHotelIdsStr = comparedHotelIdsStr;
	}

	public void setCompareInDate(String compareInDate) {
		this.compareInDate = compareInDate;
	}

	public void setCompareOutDate(String compareOutDate) {
		this.compareOutDate = compareOutDate;
	}

	public void setCompareCityCode(String compareCityCode) {
		this.compareCityCode = compareCityCode;
	}


	public void setHotelSearchService(HotelSearchService hotelSearchService) {
		this.hotelSearchService = hotelSearchService;
	}


	public List<HotelResultVO> getHotelVOList() {
		return hotelVOList;
	}


	public String getComparedHotelIdsStr() {
		return comparedHotelIdsStr;
	}


	public String getCompareInDate() {
		return compareInDate;
	}


	public String getCompareOutDate() {
		return compareOutDate;
	}


	public String getCompareCityCode() {
		return compareCityCode;
	}
	
}

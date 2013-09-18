package com.mangocity.hotel.dreamweb.search.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.mangocity.hotel.base.service.ChannelCashBackManagerService;
import com.mangocity.hotel.search.service.HotelMgisSearchService;
import com.mangocity.util.web.CookieUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class HotelEnterAction extends ActionSupport {
	private static final long serialVersionUID = -7557338701832957954L;
	private HotelMgisSearchService hotelMgisSearchService;
	private String cityCode="PEK";
	private Map<String,List> resultMap;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	public ChannelCashBackManagerService channelCashBackService;

	public String execute(){
	
		//1,主页所需展示基本信息获取
		Map resultMap=hotelMgisSearchService.getIndexMgisInfo(cityCode);
		
		//2,渠道号获取，并将渠道消息置于cookie中，add by hushunqiang 用于返现
		request = ServletActionContext.getRequest();
		response = ServletActionContext.getResponse();
		String projectcode = request.getParameter("projectcode");
		if(projectcode == null || "".equals(projectcode)){
			projectcode = CookieUtils.getCookieValue(request, "projectcode");
		}
		double cashbackrate = 1;
		if(projectcode !=null && !"".equals(projectcode) && !"null".equals(projectcode)){
			//3,渠道返现比例获取，用于百分点返现计算
			cashbackrate = channelCashBackService.getChannelCashBacktRate(projectcode);
		}
		resultMap.put("cashbackrate", cashbackrate);
		this.resultMap = resultMap;
	   return SUCCESS ;
	}
	public HotelMgisSearchService getHotelMgisSearchService() {
		return hotelMgisSearchService;
	}
	public void setHotelMgisSearchService(
			HotelMgisSearchService hotelMgisSearchService) {
		this.hotelMgisSearchService = hotelMgisSearchService;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	
	public Map<String, List> getResultMap() {
		return resultMap;
	}
	public void setResultMap(Map<String, List> resultMap) {
		this.resultMap = resultMap;
	}
	public ChannelCashBackManagerService getChannelCashBackService() {
		return channelCashBackService;
	}
	public void setChannelCashBackService(
			ChannelCashBackManagerService channelCashBackService) {
		this.channelCashBackService = channelCashBackService;
	}
}

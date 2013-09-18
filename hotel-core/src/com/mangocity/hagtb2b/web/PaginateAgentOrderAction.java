package com.mangocity.hagtb2b.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mangocity.hagtb2b.condition.StaticInfoQueryCon;
import com.mangocity.hagtb2b.persistence.AgentOrder;
import com.mangocity.hagtb2b.persistence.AgentOrg;
import com.mangocity.hagtb2b.persistence.StatisticsInfo;
import com.mangocity.hagtb2b.service.IFinaceService;
import com.mangocity.hotel.base.web.webwork.PaginateAction;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.collections.FormatMap;
import com.mangocity.util.web.CookieUtils;

public class PaginateAgentOrderAction extends PaginateAction {
	
	private IFinaceService finaceService;
	
	private IOrderService orderService;
	
	private StatisticsInfo agentStatis;
	
	private List<AgentOrder> results;
	
	private String year;
	
	private String month;
	private String curMonth;
	private String currole;
	
	//代理商code
	private String  agentCode;
	
	//操作人ID
	private String operaterId ;
	
	private static final int statDate = 7;//每月6日后可以查询和统计
	/**
	 * 
	 */
	private static final long serialVersionUID = -5036010534956963996L;
	
	public String initPage(){
		//WebUtils.setAgentCode(super.request, super.getHttpResponse());
		
		operaterId = WebUtils.getOperId(request);
		currole = finaceService.getCurRole(operaterId);
		
		agentCode = WebUtils.getAgentCode(request);
		
		int intDay = DateUtil.getDayOfSysTime();
		String curmonthstr ="";
		if(intDay>=statDate){
			curmonthstr=(DateUtil.getMonthOfSysTime()-1)<10?"0"+String.valueOf(DateUtil.getMonthOfSysTime()-1):String.valueOf(DateUtil.getMonthOfSysTime()-1);
				
			curMonth=String.valueOf(DateUtil.getYearOfSysTime())+curmonthstr;	
		}else{
			curmonthstr=(DateUtil.getMonthOfSysTime()-2)<10?"0"+String.valueOf(DateUtil.getMonthOfSysTime()-2):String.valueOf(DateUtil.getMonthOfSysTime()-2);
			curMonth=String.valueOf(DateUtil.getYearOfSysTime())+curmonthstr;
		}
		
		month = String.valueOf(DateUtil.getMonthOfSysTime()-1);
		return "queryAgentOrder";
	}
	//查询
	public String queryAgentOrder()  {
		try{
			if(!super.checkLogin()){
				super.setErrorMessage("没有登录过。。。");
			}
			//WebUtils.setAgentCode(super.request, super.getHttpResponse());
			
			currole = finaceService.getCurRole(operaterId);
			//currole = "mango";//mango,agentAdmin,agent
			StaticInfoQueryCon con = new StaticInfoQueryCon();
			setQueryCon(con,super.request);//设置统计参数
			Map params = super.getParams();
			setParam(params,super.request);//设置查询参数
			List<StatisticsInfo> statLsit = finaceService.staticAgentOrderQuery(con);//根据查询条件去统计表查询
			if(null==statLsit
					||statLsit.size()<1){
				StatisticsInfo agentStatiss = new StatisticsInfo();
				//results = finaceService.getAgentOrderList(params, agentStatiss);//从Or_order表和Or_orderitem里查询并组合成统计数据
				
				results =finaceService.getAgentOrderList(params, agentStatiss,agentCode);//从or_order和or_orderitem里查询数据并放到agentStatiss对象里
				if(0==agentStatiss.getOrderNum()){
					agentStatis = null;
				}else{
					agentStatis = agentStatiss;
				}
			}else{
				agentStatis = statLsit.get(0);
				//yong.zeng
				//results = finaceService.getAgentOrderList(agentStatis.getID());//查询统计报表的订单详情
				StatisticsInfo temp = finaceService.getStatisticsInfo(agentStatis.getID());
				results = temp.getOrderItems();
			}
			
			
			int policyScope = orderService.getPolicyScope(agentCode);
			request.setAttribute("policy", policyScope);
			return "queryAgentOrder";
		}catch(Exception e){
			log.error(e.getMessage(),e);
			return "forwardToError";
		}
		
	}
	public String confirmAgentOrder(){
		try{
			if(!super.checkLogin()){
				super.setErrorMessage("没有登录过。。。");
			}
			Map params = super.getParams();
			this.setParam(params,super.request);//设置分页查询参数
			StatisticsInfo agentStatiss = new StatisticsInfo();
			String curAgentCode = CookieUtils.getCookieValue(request,"agentCode");
			finaceService.getAgentOrderList(params, agentStatiss,curAgentCode);//从or_order和or_orderitem里查询数据并放到agentStatiss对象里
			finaceService.confirmStaticAgent(agentStatiss,params);//更新到统计表中
			return "queryAgentOrder";
		}catch(Exception e){
			log.error(e.getMessage(),e);
			return "forwardToError";
		}
	}
	
	public void setQueryCon(StaticInfoQueryCon con,HttpServletRequest request){
		con.setStatYear(this.getYear());
		con.setStatMonth(month);
		//con.setAgentCode(WebUtils.getAgentCode(super.request));
		con.setAgentCode(agentCode);
		con.setOperId(operaterId);
	}
	
	public void setParam(Map<String,Object> params,HttpServletRequest request){
		String beginDate = null;
		String endDate = null;
		String beginItemDate = null;
		String endItemDate = null;
		beginDate = DateUtil.getBeginDate(DateUtil.getDate(Integer.parseInt(this.getYear()), Integer.parseInt(month)));
		endDate   = DateUtil.getEndDate(DateUtil.getDate(Integer.parseInt(this.getYear()), Integer.parseInt(month)));
		params.put("beginQueryDate", beginDate);
		params.put("endQueryDate", endDate);
		
		beginItemDate = DateUtil.formatDateToSQLString(DateUtil.newDate(Integer.parseInt(month)+1, statDate,Integer.parseInt(this.getYear())));//每月5日财务回写数据
		endItemDate = DateUtil.formatDateToSQLString(DateUtil.newDate(Integer.parseInt(month)+2, statDate,Integer.parseInt(this.getYear())));//每月5日财务回写数据
		
		params.put("beginItemDate", beginItemDate);
		params.put("endItemDate", endItemDate);
		//params.put("agentCode", WebUtils.getAgentCode(super.request));
		params.put("agentCode", agentCode);
		FormatMap newmap = new FormatMap();
		newmap.put("agentcode", params.get("agentCode"));
		List<AgentOrg> orgList = getQueryDao().queryForList("queryAgentOrgObject", newmap);
		if(null!=orgList && !orgList.isEmpty()){
			params.put("agentName", orgList.get(0).getOrg_name());	
		}else{
			params.put("agentName", "");
		}
		
		//params.put("operaterId", WebUtils.getOperId(super.request));
		params.put("operaterId", operaterId);
		params.put("year", this.getYear());
	}
	
	public IFinaceService getFinaceService() {
		return finaceService;
	}

	public void setFinaceService(IFinaceService finaceService) {
		this.finaceService = finaceService;
	}

	public StatisticsInfo getAgentStatis() {
		return agentStatis;
	}

	public void setAgentStatis(StatisticsInfo agentStatis) {
		this.agentStatis = agentStatis;
	}

	public String getYear() {
		if(!StringUtil.isValidStr(year))year=String.valueOf(DateUtil.getYearOfSysTime());
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}
	public List<AgentOrder> getResults() {
		return results;
	}
	public void setResults(List<AgentOrder> results) {
		this.results = results;
	}
	public String getAgentCode() {
		return agentCode;
	}
	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}
	public String getOperaterId() {
		return operaterId;
	}
	public String getCurrole() {
		return currole;
	}
	public void setCurrole(String currole) {
		this.currole = currole;
	}
	public void setOperaterId(String operaterId) {
		this.operaterId = operaterId;
	}
	
	public String getCurMonth() {
		return curMonth;
	}
	public void setCurMonth(String curMonth) {
		this.curMonth = curMonth;
	}

	public void setOrderService(IOrderService orderService) {
		this.orderService = orderService;
	}
}

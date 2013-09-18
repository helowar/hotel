package com.mangocity.hagtb2b.web;

import com.mangocity.hagtb2b.persistence.AgentUser;
import com.mangocity.hotel.order.web.GenericCCAction;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.dao.DAOIbatisImpl;

public class B2bStatAction extends GenericCCAction {

	
	private String userType;
	private String loginname;
	private String argarea;
	private String operaterIdSe;
	
	private static final int statDate = 7;//每月6日后可以查询和统计
	
	private DAOIbatisImpl queryDao;

	public String create() { 
		//WebUtils.setAgentCode(super.request, super.getHttpResponse());
		if(!super.checkLogin()){
			super.setErrorMessage("没有登录过。。。");
			return "forwardToError";
		}
		
		loginname = WebUtils.getOperId(request);
		AgentUser au = (AgentUser)queryDao.queryForObject("queryAgentRole",loginname);
		if(au!=null){
			String org_id = au.getOrgid();
			if("2".equals(au.getUsertype()))//芒果操作员 
			{
				userType= "mangoAdmin";
			}else if("3".equals(au.getUsertype())){//区域管理员
				userType ="areaAdmin";
/*				FormatMap agentmap = new FormatMap();
				agentmap.put("org_id", org_id);
				AgentOrg ao = (AgentOrg)queryDao.queryForObject("queryAgentOrgObject", agentmap);
*/				argarea = au.getAdminArea();
			}else if("1".equals(au.getUsertype())){
				userType ="agent";
				super.setErrorMessage("对不起,你没有权限!");
				return "forwardToError";
			}
		}
		int curMonth = DateUtil.getMonthOfSysTime()-1;
		request.setAttribute("curMonth", curMonth);
		operaterIdSe = WebUtils.getOperId(request);
		request.setAttribute("operaterId", operaterIdSe);
		
		int intDay = DateUtil.getDayOfSysTime();
		String curYearMonth = "";
		/*if(intDay>=statDate){
			curYearMonth=String.valueOf(DateUtil.getYearOfSysTime())+String.valueOf(DateUtil.getMonthOfSysTime()-1);	
		}else{
			curYearMonth=String.valueOf(DateUtil.getYearOfSysTime())+String.valueOf(DateUtil.getMonthOfSysTime()-2);
		}*/
		String curmonthstr ="";
		if(intDay>=statDate){
			curmonthstr=(DateUtil.getMonthOfSysTime()-1)<10?"0"+String.valueOf(DateUtil.getMonthOfSysTime()-1):String.valueOf(DateUtil.getMonthOfSysTime()-1);
				
			curYearMonth=String.valueOf(DateUtil.getYearOfSysTime())+curmonthstr;	
		}else{
			curmonthstr=(DateUtil.getMonthOfSysTime()-2)<10?"0"+String.valueOf(DateUtil.getMonthOfSysTime()-2):String.valueOf(DateUtil.getMonthOfSysTime()-2);
			curYearMonth=String.valueOf(DateUtil.getYearOfSysTime())+curmonthstr;
		}
		request.setAttribute("curYearMonth", curYearMonth);
		return "create";
	}

	/**
	 * 处理重复提交
	 */
	private boolean isRepeatSubmit() {
		String strutsToken = (String) getParams().get("struts.token");
		String sessionToken = (String) getFromSession("struts.token.session");
		if (StringUtil.StringEquals2(strutsToken, sessionToken)) {
			return true;
		}
		putSession("struts.token.session", strutsToken);
		return false;
	}



	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}




	public String getArgarea() {
		return argarea;
	}

	public void setArgarea(String argarea) {
		this.argarea = argarea;
	}

	public DAOIbatisImpl getQueryDao() {
		return queryDao;
	}

	public void setQueryDao(DAOIbatisImpl queryDao) {
		this.queryDao = queryDao;
	}



}

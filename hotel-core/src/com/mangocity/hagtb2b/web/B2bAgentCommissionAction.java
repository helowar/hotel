package com.mangocity.hagtb2b.web;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mangocity.hagtb2b.service.IB2bService;
import com.mangocity.hotel.base.persistence.CommisionSet;
import com.mangocity.hotel.base.web.webwork.GenericAction;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.web.CookieUtils;

public class B2bAgentCommissionAction extends GenericAction {
	
	

	private IB2bService b2bService;
	private List<CommisionSet> priceLst;
	
	private String b2BIdLst;
	private String updateType;//更新方式 add:1,edit:2
	private static int paramNum = 4 ;
	private String backURL;//上一个URL
	
	/**
	 * 创建Action
	 * @return
	 */
	public String create(){
		request.setAttribute("b2BIdLst", b2BIdLst);
		String tempUrl = request.getRequestURL().toString();
        if(request.getQueryString()!=null)    
        	tempUrl+="?"+request.getQueryString();  
		backURL = tempUrl;
		return "create";
	}
	

	/**
	 * 
	 * @return
	 */
	public String editCommission(){
		if(null==b2BIdLst)return super.forwardError("请先指定代理商");
		String tempUrl = request.getRequestURL().toString();
        if(request.getQueryString()!=null)    
        	tempUrl+="?"+request.getQueryString();  
		backURL = tempUrl;
		
		if(!b2BIdLst.contains(",")){//单个代理商
			List<CommisionSet> commSet = b2bService.getCommSetByB2B(b2BIdLst);//b2BIdLst实际上是单个ID
			if(commSet.size()<1){//未设置时,跳到设置页面
				return "create";
			}
			
			CommisionSet comm5= null,comm4= null,comm3= null,comm2= null;
			//分别得到5,4,3,2星级对应的佣金设置情况
			for(CommisionSet com:commSet){
				if(com.getHotelStar().equals("5")){//五星级
					if(null==comm5 ||com.getCommID().longValue()>comm5.getCommID().longValue()){
						comm5 = com;
					}
				}else if(com.getHotelStar().equals("4")){//四星级
					if(null==comm4||com.getCommID().longValue()>comm4.getCommID().longValue()){
						comm4 = com;
					}
				}else if(com.getHotelStar().equals("3")){//三星级
					if(null==comm3||com.getCommID().longValue()>comm3.getCommID().longValue()){
						comm3 = com;
					}
				}else if(com.getHotelStar().equals("2")){//五星级
					if(null==comm2||com.getCommID().longValue()>comm2.getCommID().longValue()){
						comm2 = com;
					}
				}
			}//end for
			
			request.setAttribute("comm5", comm5);
			request.setAttribute("comm4", comm4);
			request.setAttribute("comm3", comm3);
			request.setAttribute("comm2", comm2);
			
			}
			else if(b2BIdLst.contains(","))
			{//多个代理商
					return "create";
			}
		
		return "edit";
	}
	/**
	 * 保存佣金设置
	 * @return
	 */
	public String saveCommissionSet(){

        // 防止重复提交
        if (isRepeatSubmit()) {
            return forwardError("请不要重复提交,谢谢!");
        }
		String[] b2BArr =  null;
		List allLst = new ArrayList();
		if(b2BIdLst==null) return "";
		
		if("add".equals(updateType)){//新增
			String tempB2bLst = b2BIdLst.replaceAll(",", "','");
			tempB2bLst = "'"+tempB2bLst+"'";
			b2bService.sqlUpdate("delete htl_b2b_commision_set where b2BCd in("+tempB2bLst+")");
		}
		
		b2BArr =  b2BIdLst.split(",");
		if(b2BArr.length<1) return "";
		
		
		String opLogonID = CookieUtils.getCookieValue(request,"operaterId");
		opLogonID = opLogonID==null?"":opLogonID;
		
		
		//String opName = WebUtils.getOperId(request);//super.getUser().getName();
		//String opLogonID = "test";//super.getUser().getLogonId();
		Map params = super.getParams();
		
		
		priceLst = MyBeanUtil.getBatchObjectFromParam(params, CommisionSet.class, paramNum);
		
		for(int i=0;i<b2BArr.length;i++){

				for(CommisionSet commSet:priceLst){
					commSet.setB2BCd(b2BArr[i]);
					if("add".equals(updateType)){//新增
					//	commSet.setCreateBy(opName);//名称
						commSet.setCreateById(opLogonID);//登录ID
					}else if("edit".equals(updateType)){//修改
					//	commSet.setModifyBy(opName);//名称
						commSet.setModifyById(opLogonID);//登录ID
						commSet.setModifyDate(DateUtil.getSystemDate());//系统当前时间
					}
					allLst.add(commSet.clone());
				}//end for
		}
		
		//批量更新allLst
		try{
		b2bService.batchUpdate(allLst);
		}catch(Exception ex){
			log.error("批量更新佣金价格错误:B2bAgentCommissionAction");
			return super.forwardError("批量更新佣金价格错误");
		}
		return super.SUCCESS;
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
    
    
	public List<CommisionSet> getPriceLst() {
		return priceLst;
	}

	public void setPriceLst(List<CommisionSet> priceLst) {
		this.priceLst = priceLst;
	}

	public String getB2BIdLst() {
		return b2BIdLst;
	}
	public void setB2BIdLst(String idLst) {
		b2BIdLst = idLst;
	}

	public String getUpdateType() {
		return updateType;
	}

	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}

	public IB2bService getB2bService() {
		return b2bService;
	}

	public void setB2bService(IB2bService service) {
		b2bService = service;
	}


	public String getBackURL() {
		return backURL;
	}


	public void setBackURL(String backURL) {
		this.backURL = backURL;
	}

	

}

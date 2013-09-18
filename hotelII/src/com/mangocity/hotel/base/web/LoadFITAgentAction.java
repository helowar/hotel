package com.mangocity.hotel.base.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.mangocity.hotel.base.persistence.HtlFITAlias;
import com.mangocity.hotel.base.service.IHotelFavourableReturnService;
import com.mangocity.webnew.constant.FITAliasConstant;
import com.mangocity.webnew.util.action.GenericWebAction;


/**
 * 会员下单action
 * @author xiongxiaojun
 *
 */
public class LoadFITAgentAction extends GenericWebAction{
	private String authCode;
	private IHotelFavourableReturnService returnService;
	
    public String toLoadPage(){
    	if(authCode.equals("mangocity")){
    		return SUCCESS;
    	}
    	return "failure";
    }
    
    public String loadFitAlias(){
    	String loadResult="";
    	Properties properties = new Properties();
    	try {
    		String fitPath = request.getSession().getServletContext().getInitParameter("mangoFITPropertiesLocation");
    		
    		fitPath = null==fitPath?"/WEB-INF/hotels.properties":fitPath;
    		File file = new File(request.getSession().getServletContext().getRealPath(fitPath));
    		if(file.exists()){
    			properties.load(request.getSession().getServletContext().getResourceAsStream(fitPath));
    		}
		} catch (FileNotFoundException e) {
			log.error("not found /WEB-INF/hotels.properties "+e.getMessage(),e);
		} catch (IOException e) {
			log.error("WEB-INF/hotels IO Exception"+e.getMessage(),e);
		}
    	String code = properties.getProperty("authCode");
    	if(null == code || "".equals(code)){
    		code ="mangocity";
    	}
    	if(code.equals(authCode)){
    		List<HtlFITAlias> fitAliasList = returnService.getFITAlias("1");
        	try{
    	    	if(fitAliasList!=null && !fitAliasList.isEmpty()){
    	    		Map<String,String> aliasMap = new HashMap<String,String>(fitAliasList.size());
    	    		for(HtlFITAlias alias : fitAliasList){
    	    			aliasMap.put(alias.getAliasId(), alias.getID().toString());
    	        	}
    	    		if(!aliasMap.isEmpty()){
    	    			FITAliasConstant.fitAliasObj.clear();
    	    			FITAliasConstant.fitAliasObj.putAll(aliasMap);
    	    			log.info("::::::::::::::::::"+new Date()+"加载网站散客项目号成功！");
    	    		}
    	    	}
        	}catch(Exception e){
        		log.info("::::::::::::::::::::::::::::"+new Date()+"加载芒果网站散客项目号失败！");
        	}
        	loadResult="load success!";
    	}else{
    		loadResult="failure! the code error!";
    	}
        request.setAttribute("relult", loadResult);
    	return SUCCESS;
    }

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public IHotelFavourableReturnService getReturnService() {
		return returnService;
	}

	public void setReturnService(IHotelFavourableReturnService returnService) {
		this.returnService = returnService;
	}	

}


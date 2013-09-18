package com.mangocity.hotel.base.web;

import com.mangocity.hotel.base.persistence.MangoEmapVo;
import com.mangocity.hotel.base.service.MangoEmapService;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;

public class GMapAction extends PersistenceAction{

	private static final long serialVersionUID = 4357828703757365453L;
	private MangoEmapVo mangoEmapVo;
	private MangoEmapService mangoEmapService;
	private String googleMessage;
	private String baiduMessage;
	public String view(){
		mangoEmapVo=mangoEmapService.queryMangoEmapById(mangoEmapVo);
		return "view";
	}
	public MangoEmapVo getMangoEmapVo() {
		return mangoEmapVo;
	}
	public void setMangoEmapVo(MangoEmapVo mangoEmapVo) {
		this.mangoEmapVo = mangoEmapVo;
	}
	public MangoEmapService getMangoEmapService() {
		return mangoEmapService;
	}
	public void setMangoEmapService(MangoEmapService mangoEmapService) {
		this.mangoEmapService = mangoEmapService;
	}
	
	public String save(){
		try{
		mangoEmapService.saveMangoEmap(mangoEmapVo);
		this.googleMessage="保存成功！";
		}catch (Exception e){
			LOG.error("mgis save error:"+e);
			this.googleMessage="保存失败！";
		}
		return "view";
	}
	
	/**
	 * 保存百度地标
	 */
	public String saveBaidu(){
		try{
		mangoEmapService.saveMangoEmapBaidu(mangoEmapVo);
		this.baiduMessage="保存成功！";
		}catch (Exception e){
			LOG.error("mgis save error:"+e);
			this.baiduMessage="保存失败！";
		}
		return "view";
	}
	public String getGoogleMessage() {
		return googleMessage;
	}
	public void setGoogleMessage(String googleMessage) {
		this.googleMessage = googleMessage;
	}
	public String getBaiduMessage() {
		return baiduMessage;
	}
	public void setBaiduMessage(String baiduMessage) {
		this.baiduMessage = baiduMessage;
	}

}

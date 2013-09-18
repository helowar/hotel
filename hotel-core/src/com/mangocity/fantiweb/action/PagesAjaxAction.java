package com.mangocity.fantiweb.action;

import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.hweb.persistence.HotelPageForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebBean;
/**
 * 异步分页加载Action
 * @author chenjiajie
 *
 */
public class PagesAjaxAction extends GenericWebAction {

	private HotelManageWeb hotelManageWeb;
	
	private HotelPageForWebBean hotelPageForWebBean;
	private String source;
	
	public String execute(){
		//异步取页数的时候放直接从Session中取查询条件 同时在Session中删除条件 
		QueryHotelForWebBean queryHotelForWebBean = super.getWebBeanFromSession();
		
		if(null != queryHotelForWebBean){
			hotelPageForWebBean = hotelManageWeb.queryHotelPagesForWeb(queryHotelForWebBean);
		}
		//做不为空的判断 add by shengwei.zuo 
		if(source != null && !"".equals(source)){
			if(source.equals("mangoMap")){
				return "mangoMapPage";
			}
		}
		return SUCCESS;
	}
	
	/** getter and setter **/
	public HotelManageWeb getHotelManageWeb() {
		return hotelManageWeb;
	}

	public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
		this.hotelManageWeb = hotelManageWeb;
	}

	public HotelPageForWebBean getHotelPageForWebBean() {
		return hotelPageForWebBean;
	}

	public void setHotelPageForWebBean(HotelPageForWebBean hotelPageForWebBean) {
		this.hotelPageForWebBean = hotelPageForWebBean;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
}

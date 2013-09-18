package com.mangocity.webco.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.mangocity.framework.exception.ServiceException;
import com.mangocity.mgis.app.service.baseinfo.GisService;
import com.mangocity.mgis.domain.valueobject.MapsInfo;
import com.mangocity.webnew.util.action.GenericWebAction;
import com.opensymphony.xwork2.ActionContext;

public class HotelListMapLittleAction  extends GenericWebAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7993070490005491076L;
	private Long [] gisId;
	private String [] name;
	private String [] address;
	private String [] desc;
	private GisService gisService;
	private Map gisMaps = new HashMap();
	public String execute(){
		ActionContext ctx = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) ctx
		.get(ServletActionContext.HTTP_REQUEST);
		try {
			String path = request.getRealPath("/");
			List list=new ArrayList();
			if(null!=gisId){
				for(int i=0;i<gisId.length;i++){
					if(gisId[i]>0){
						MapsInfo info=new MapsInfo();
						info.setGisId(gisId[i]);
						info.setName(name[i]);
						info.setDescription(desc[i]);
						info.setImageUrl(path + "/images/emap/mango.gif");
						info.setImageInfo(String.valueOf(i + 1));
						list.add(info);
					}
				}
				gisMaps = gisService.kmlGenerator(list, 0, 0, path);
			}
		} catch (ServiceException e) {
			log.error("HotelListMapLittleAction ServiceException error",e);
		} catch (IOException e) {
			log.error("HotelListMapLittleAction IOException error",e);
		}
		return SUCCESS;
	}
	
	public GisService getGisService() {
		return gisService;
	}
	public void setGisService(GisService gisService) {
		this.gisService = gisService;
	}

	public String[] getAddress() {
		return address;
	}

	public void setAddress(String[] address) {
		this.address = address;
	}

	public String[] getDesc() {
		return desc;
	}

	public void setDesc(String[] desc) {
		this.desc = desc;
	}

	public Long[] getGisId() {
		return gisId;
	}

	public void setGisId(Long[] gisId) {
		this.gisId = gisId;
	}

	public String[] getName() {
		return name;
	}

	public void setName(String[] name) {
		this.name = name;
	}

	public Map getGisMaps() {
		return gisMaps;
	}

	public void setGisMaps(Map gisMaps) {
		this.gisMaps = gisMaps;
	}
	
}

package com.mangocity.hagtb2b.web;

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
import com.mangocity.util.log.MyLog;
import com.mangocity.util.web.CookieUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class HotelListMapLittleAction  extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7993070490005491076L;

	private static final MyLog log = MyLog.getLogger(HotelListMapLittleAction.class);
	private Long [] gisId;
	private String [] name;
	private String [] address;
	private String [] desc;
	private GisService gisService;
	private Map gisMaps = new HashMap();
	private String  flag_userNoLogin;//游客使用，其值为shieldComission
	private String agent_imgUrl ;
	private String flag_showCommission ="0"; ////代理用户是否显示佣金，1表示显示；
	//private String  flagShowComission ="shieldComission";//打开即跳到预订弹出图表
	public String execute(){
		ActionContext ctx = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) ctx
		.get(ServletActionContext.HTTP_REQUEST);
		//标示是否显示佣金，1显示，其他值不显示
		flag_showCommission = CookieUtils.getCookieValue(request, "showCommissionFlag");
		log.info("HotelQueryAction flag_showCommission====="+flag_showCommission);
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
			log.error(e.getMessage(),e);
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}
		if("shieldComission".equals(flag_userNoLogin)){
			return "success-ow";
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
	public String getFlag_showCommission() {
		return flag_showCommission;
	}

	public String getAgent_imgUrl() {
		return agent_imgUrl;
	}

	public String getFlag_userNoLogin() {
		return flag_userNoLogin;
	}

	public void setFlag_userNoLogin(String flag_userNoLogin) {
		this.flag_userNoLogin = flag_userNoLogin;
	}

	public void setAgent_imgUrl(String agent_imgUrl) {
		this.agent_imgUrl = agent_imgUrl;
	}

	public void setFlag_showCommission(String flag_showCommission) {
		this.flag_showCommission = flag_showCommission;
	}
	
}

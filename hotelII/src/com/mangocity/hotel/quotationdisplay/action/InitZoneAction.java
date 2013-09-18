package com.mangocity.hotel.quotationdisplay.action;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONArray;

import com.googlecode.jsonplugin.annotations.JSON;
import com.mangocity.hotel.quotationdisplay.model.QueryParam;
import com.mangocity.hotel.quotationdisplay.service.IInitZoneService;
import com.mangocity.hotel.quotationdisplay.vo.ZoneVO;
import com.opensymphony.xwork2.ActionSupport;

/***
 * 
 * @author panjianping
 * 
 */
public class InitZoneAction extends ActionSupport {
	private static final Log log = LogFactory.getLog(InitZoneAction.class);
	private IInitZoneService initZoneService;
	private String content;
	private QueryParam param = new QueryParam();
    
	/***
	 * 根据param中的cityCode返回城区
	 */
	public String execute() {
		List<ZoneVO> zoneVOs = initZoneService.getZone(param);
		if (!zoneVOs.isEmpty()) {
			JSONArray jsonArray = JSONArray.fromObject(zoneVOs);
			content = jsonArray.toString();
			//log.info(content);
			//log.info("城区返回成功");
			return SUCCESS;
		}
		return null;
	}

	@JSON(name = "content")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@JSON(serialize = false)
	public IInitZoneService getInitZoneService() {
		return initZoneService;
	}

	public void setInitZoneService(IInitZoneService initZoneService) {
		this.initZoneService = initZoneService;
	}

	@JSON(serialize = false)
	public QueryParam getParam() {
		return param;
	}

	public void setParam(QueryParam param) {
		this.param = param;
	}

}

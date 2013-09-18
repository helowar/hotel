package com.mangocity.hotel.quotationdisplay.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.web.InitServletImpl;
import com.mangocity.hotel.quotationdisplay.model.QueryParam;
import com.mangocity.hotel.quotationdisplay.service.IInitZoneService;
import com.mangocity.hotel.quotationdisplay.vo.ZoneVO;
/***
 * 
 * @author panjianping
 *
 */
public class InitZoneServiceImpl implements IInitZoneService {
   /**
    * 根据查询参数中的cityCode字段得到该城市对应的城区对象列表
    */
	public List<ZoneVO> getZone(QueryParam queryParam) {
		List<ZoneVO> zoneVOs = new ArrayList<ZoneVO>();
		Map<String,String> zones=InitServletImpl.mapCitySozeObj.get(queryParam.getCityCode());
		for(Map.Entry entry:zones.entrySet()){
			ZoneVO zoneVO = new ZoneVO();
			zoneVO.setZoneCode(entry.getKey().toString());
			zoneVO.setZoneName(entry.getValue().toString());
			zoneVOs.add(zoneVO);
		}
	
		return zoneVOs;
	}

}

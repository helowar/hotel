package com.mangocity.hotel.quotationdisplay.service;

import java.util.List;

import com.mangocity.hotel.quotationdisplay.model.QueryParam;
import com.mangocity.hotel.quotationdisplay.vo.ZoneVO;
/***
 * 
 * @author panjianping
 *
 */
public interface IInitZoneService {
	/***
	 * 根据查询参数中的cityCode字段得到该城市对应的商圈对象列表
	 * @param queryParam
	 * @return 城市所对应的商圈对象列表
	 */
	public List<ZoneVO> getZone(QueryParam queryParam);
}

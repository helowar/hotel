package com.mangocity.webnew.service;


import java.util.List;


/**
 * 网站查询接口
 * @author chenjiajie
 *
 */
public interface HotelQueryService extends java.io.Serializable {
	
	/**
	 * 按照城市中文名找城市编码
	 * @param cityChnName
	 * @return
	 */
	public String queryCityCodeByCityName(String cityChnName);
	
	/**
	 * 查询可以调用查询接口的ip列表
	 */
	public List queryHessianIpControl();
}

package com.mangocity.hotel.base.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.persistence.HtlGeographicalposition;

/**
 * 地点DAO
 * 
 * @author zuoshengwei
 * 
 */

public interface IGeograpPositionDAO {
    /**
     * 新增一个地点
     * 
     * @param htlGeographicalposition
     * @return
     */
    public Long createGeograpPosition(HtlGeographicalposition htlGeographicalposition);

    /**
     * 根据指定ID获取地点信息
     * 
     * @param id
     * @return
     */
    public HtlGeographicalposition queryGeopositionById(long id);

    /**
     * 根据类型ID获得对应的地点信息
     * 
     * @param name
     * @return
     */

    public List<HtlGeographicalposition> queryNameByAddType(long addressType, String cityName);

    /**
     * 根据城市名称查询出地点类型
     * 
     */
    public List queryAddressTypeByCityName(String cityName);

    /**
     * 修改地点信息
     * 
     * @param geographicalposition
     * @return
     */
    public int updateGeoposition(HtlGeographicalposition geographicalposition);

    /**
     * 删除地点信息
     * 
     * @param id
     * @return
     */
    public int deleteGeoposition(long id);

	public List<HtlGeographicalposition> queryPositionByType(
			long type, String cityName);

	public List queryBusinessForCityName(String cityName);

	public List queryGeograpPosition(String cityName, Long gptypeId,
			String name);

	public List queryAllCity();

	public List queryPostionListByCityCode(String cityCode, Integer type);
	
	public List queryAllPosition();
	
	/**
	 * 查询所有地标信息用于创建索引
	 * @return
	 */
	public List queryPositions(Date date);

	public List queryBusinessForCityCode(String cityCode);

	public List findCityByCode(String cityCode);
	
}

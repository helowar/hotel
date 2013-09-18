package com.mangocity.hotel.base.service;

import java.util.List;

import com.mangocity.hotel.base.persistence.HtlGeographicalposition;

/**
 * 地点service
 * 
 * @author zuoshengwei
 * 
 */
public interface IGeograpPositionService {

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
     * 根据name获得对应的地点信息
     * 
     * @param name
     * @return
     */

    public List queryGeopositionByName(String name);

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

}

package com.mangocity.hotel.base.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.base.dao.IGeograpPositionDAO;
import com.mangocity.hotel.base.persistence.HtlGeographicalposition;
import com.mangocity.hotel.base.persistence.HtlGisType;
import com.mangocity.hotel.base.service.IGeograpPositionService;

/**
 * 地点service
 * 
 * @author zuoshengwei
 * 
 */
public class GeograpPositionServiceImpl implements IGeograpPositionService {

    private IGeograpPositionDAO geograpPositionDAO;

    public IGeograpPositionDAO getGeograpPositionDAO() {
        return geograpPositionDAO;
    }

    public void setGeograpPositionDAO(IGeograpPositionDAO geograpPositionDAO) {
        this.geograpPositionDAO = geograpPositionDAO;
    }

    /**
     * 新增一个地点
     * 
     * @param htlGeographicalposition
     * @return
     */
    public Long createGeograpPosition(HtlGeographicalposition htlGeographicalposition) {
        geograpPositionDAO.createGeograpPosition(htlGeographicalposition);
        return htlGeographicalposition.getID();
    }

    /**
     * 根据指定ID获取地点信息
     * 
     * @param id
     * @return
     */

    public HtlGeographicalposition queryGeopositionById(long id) {
        return geograpPositionDAO.queryGeopositionById(id);
    }

    /**
     * 修改地点信息
     * 
     * @param geographicalposition
     * @return
     */
    public int updateGeoposition(HtlGeographicalposition geographicalposition) {

        return geograpPositionDAO.updateGeoposition(geographicalposition);
    }

    /**
     * 删除地点信息
     * 
     * @param id
     * @return
     */
    public int deleteGeoposition(long id) {
        return geograpPositionDAO.deleteGeoposition(id);
    }

    /**
     * 根据name获得对应的地点信息
     * add by shengwei.zuo 
     * @param name
     * @return
     */
    public List queryGeopositionByName(String name) {

        List lstGisType = new ArrayList();

        List lstGType = geograpPositionDAO.queryAddressTypeByCityName(name);

        if (null == lstGType || lstGType.isEmpty()) {

            return null;
        }

        for (int i = 0; i < lstGType.size(); i++) {

        	Integer gisTypeId = null;
            
            gisTypeId = ((BigDecimal)lstGType.get(i)).intValue();
            
            List<HtlGeographicalposition> lstName = geograpPositionDAO.queryNameByAddType(gisTypeId, name);

            HtlGisType htlGisType = new HtlGisType();

            htlGisType.setAddressTypeId(gisTypeId);
            htlGisType.setCityName(name);
            htlGisType.setLstAddress(lstName);

            lstGisType.add(htlGisType);

        }

        return lstGisType;
    }

}

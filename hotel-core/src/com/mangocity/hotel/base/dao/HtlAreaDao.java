package com.mangocity.hotel.base.dao;

import com.mangocity.hotel.base.persistence.HtlArea;
import com.mangocity.util.dao.GenericDAO;

public interface HtlAreaDao extends GenericDAO {

    /**
     * 检查区域by cityCode
     * 
     * @param
     * @return HtlArea
     */
    public HtlArea queryAreaCode(String cityCode);

}

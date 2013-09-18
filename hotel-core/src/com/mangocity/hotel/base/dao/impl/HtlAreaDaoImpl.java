package com.mangocity.hotel.base.dao.impl;

import java.util.List;

import com.mangocity.hotel.base.persistence.HtlArea;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class HtlAreaDaoImpl extends GenericDAOHibernateImpl{
    
    /**
     * 检查区域by cityCode
     * 
     * @param
     * @return HtlArea
     */
    public HtlArea queryAreaCode(String cityCode) {
        List<HtlArea> list = this.queryByNamedQuery("queryAreaCode",
                new Object[] { cityCode });
        
        if(list.isEmpty()){
            
            return null;
        }
        
        return (HtlArea) list.get(0);
    }
}

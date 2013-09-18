package com.mangocity.webnew.service.impl;



import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.base.persistence.HtlArea;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.webnew.service.HotelQueryService;

public class HotelQueryServiceImpl extends DAOHibernateImpl implements HotelQueryService{
	
    /**
     * 按照城市中文名找城市编码
     * @param cityChnName
     * @return
     */
    public String queryCityCodeByCityName(String cityChnName){
        String cityCode = "";
        String hql = " from HtlArea where cityName = ? ";
        List<HtlArea> htlAreaList = super.doquery(hql, cityChnName, false);
        if(null != htlAreaList && !htlAreaList.isEmpty()){
            HtlArea htlArea = htlAreaList.get(0);
            cityCode = htlArea.getCityCode();
        }
        return cityCode;
    }
    
    /**
	 * 查询可以调用查询接口的ip列表
	 */
	public List queryHessianIpControl(){
		
		List queryIpLis = new ArrayList();
		String hql = "from HtlHessianIpcontrol";
		queryIpLis = super.doquery(hql, false);
		return queryIpLis;
	}


}

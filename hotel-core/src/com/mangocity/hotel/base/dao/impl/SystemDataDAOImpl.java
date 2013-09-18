package com.mangocity.hotel.base.dao.impl;

import java.util.Date;
import java.util.List;

import com.mangocity.framework.base.model.Tree;
import com.mangocity.hotel.base.dao.SystemDataDAO;
import com.mangocity.hotel.base.persistence.HtlArea;
import com.mangocity.hotel.base.persistence.HtlExchange;
import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.util.dao.GenericDAOHibernateImpl;
import com.mangocity.util.getcdm.GetCDMUtil;

public class SystemDataDAOImpl extends GenericDAOHibernateImpl implements SystemDataDAO {

	public HtlArea queryAreaCode(String cityCode) 
	{
		String hql = " from HtlArea htlArea where htlArea.cityCode = ? ";
		List<HtlArea> areaList = super.query(hql, new Object[]{ cityCode });
		
		return areaList.isEmpty()?null : areaList.get(0);
	}
	
	public List<HtlExchange> getExchangeRateBasedOnCurrency(String currency)
	{
        String hql = " from HtlExchange he where he.tocurrency = ? ";

        return super.query(hql, new Object[]{ currency });
	}
	
	public OrParam querySysParamByName(String paramName) {
		String hql = " from OrParam where name = ? ";
		List<OrParam> sysParamList = super.query(hql, new Object[]{ paramName });
		
		return sysParamList.isEmpty()?null : sysParamList.get(0);
	}
	
	public void updateSysParamByName(OrParam sysParam) {
		super.update(sysParam);
	}
	
	/**
     * 获取CDM数据
     * 
     * @param path
     * @return
     */
    @SuppressWarnings("deprecation")
    public Tree qryCDMDataByPath(String path) {
        return GetCDMUtil.getParamsByPath(path, super.getCurrentSession().connection());
    }
    
    /**
     * 查询需要更新推荐级别的酒店ID
     * @return
     */
    public List<String> queryHotelIDforUpd() {
    	String hql = "select ''||hc.hotel_id from htl_comm_list hc where hc.end_date = trunc(sysdate-?)";
    	return super.queryByNativeSQL(hql, new Object[]{ 1 });
    }
}

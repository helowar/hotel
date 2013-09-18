package com.mangocity.hotel.base.dao.impl;

import java.util.Collections;
import java.util.List;

import com.mangocity.hotel.base.dao.IClauseDao;
import com.mangocity.hotel.base.persistence.HtlPopedomControl;
import com.mangocity.hotel.base.persistence.HtlPreconcertItemTemplet;
import com.mangocity.util.dao.DAOHibernateImpl;

/**
 */
public class ClauseDaoImpl extends DAOHibernateImpl implements IClauseDao {

    /*
     * 根据酒店ID获取合同列表 (non-Javadoc)
     * 
     * @see com.mangocity.hotel.base.dao.IClauseDao#getContracts(java.lang.Long)
     */
    public List getContracts(Long hotelID) {
        // TODO Auto-generated method stub
        return super.queryByNamedQuery("queryContractsByHotelID", new Object[] { hotelID });
    }

    public List<HtlPreconcertItemTemplet> queryModel(long hotelId){
    	String sql = "from HtlPreconcertItemTemplet where hotelID=? and Active = " + "1";
		List<HtlPreconcertItemTemplet> lstModel = super.query(sql, hotelId);
        return lstModel == null ? Collections.EMPTY_LIST : lstModel;
    }
    
    public List<HtlPreconcertItemTemplet> quertHotelAjaxModel(long modelId, long hotelId) {
		String sql = "from HtlPreconcertItemTemplet where ID= ? and hotelID=? and Active=" + "1";
		List<HtlPreconcertItemTemplet> lstModel = super.query(sql, new Object[] { modelId, hotelId });
		// 空指针处理下
		return (lstModel == null) ? Collections.EMPTY_LIST : lstModel;
    }
    
    //查询担保预付条款模板（最多一条数据）
    public List<HtlPreconcertItemTemplet> queryModelOnly(long modelId) {
		String sql = "from HtlPreconcertItemTemplet where ID=?";
		List<HtlPreconcertItemTemplet> listModel = super.query(sql, modelId);
		return listModel == null ? Collections.EMPTY_LIST : listModel;

	}
    
    public List<HtlPopedomControl> getPopedoms(String popedomControlType, String loginName) {
		String hsql="select a from HtlPopedomControl a where a.controlType=? and a.logName=?";
		return super.query(hsql, new Object[]{popedomControlType,loginName});
	}
}

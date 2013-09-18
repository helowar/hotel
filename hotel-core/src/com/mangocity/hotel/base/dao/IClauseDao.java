package com.mangocity.hotel.base.dao;

import java.util.List;

import com.mangocity.hotel.base.persistence.HtlPopedomControl;
import com.mangocity.hotel.base.persistence.HtlPreconcertItemTemplet;

/**
 */
public interface IClauseDao {

    /*
     * Add by Shengwei.Zuo 2009-02-01 根据酒店ID查询出该酒店下面的合同列表
     */
    public List getContracts(Long hotelID);
    
    /**
     * 查询合同模板
     * @param hotelId
     * @return
     */
    public List<HtlPreconcertItemTemplet> queryModel(long hotelId);
    
    /**
     * 查询合同模板
     * @param modelId
     * @param hotelId
     * @return
     */
    public List<HtlPreconcertItemTemplet> quertHotelAjaxModel(long modelId, long hotelId);
    
    /**
     * 查询担保预付条款模板（最多一条数据）
     * @param modelId
     * @return
     */
    public List<HtlPreconcertItemTemplet> queryModelOnly(long modelId) ;
    
    public List<HtlPopedomControl> getPopedoms(String popedomControlType, String loginName);

}

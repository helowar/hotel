package com.mangocity.hotel.base.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.persistence.HtlPreconcertItemBatch;
import com.mangocity.hotel.base.persistence.HtlPreconcertItemTemplet;

/**
 */
public interface IOnlyClauserDao {
	/**
	 * 根据酒店id查询酒店预订担保预付条款模板
	 * @param hotelId
	 * @return List<HtlPreconcertItemTemplet>
	 */
	public List<HtlPreconcertItemTemplet> queryHtlPreconcertItemTempletListByHotelId(Long hotelId);
	
	/**
	 * 插入批次表中
	 * @param htlPreconcertItemBatch
	 * @return boolean
	 */
	public boolean saveOrUpdateClause(HtlPreconcertItemBatch htlPreconcertItemBatch);
	
	/**
	 *  调用存储过程,往批次表中拿到数据再插入每天表
	 * @param hotelId
	 * @param id
	 * @param contractId
	 * @param priceTypeid
	 * @param beginDate
	 * @param engDate
	 * @param active
	 * @return boolean
	 * @throws SQLException
	 */
    public boolean saveOrUpdateClausePro(long hotelId, long id, long contractId,
        String priceTypeid, Date beginDate, Date engDate, String active) throws SQLException;
    
    /**
     * 根据某一条款查询值出来
     * @param modelId
     * @return HtlPreconcertItemTemplet
     */
    public List<HtlPreconcertItemTemplet> queryHtlPreconcertItemTempletById(Long modelId);
    
    /**
     * 查询批次表,来找到操作记录
     * @param hotelId
     * @return List<HtlPreconcertItemBatch>
     */
    public List<HtlPreconcertItemBatch> queryHtlPreconcertItemBatchListByHotelId(Long hotelId);
    
    /**
     * 根据id查询批次表
     * @param id
     * @return HtlPreconcertItemBatch
     */
    public HtlPreconcertItemBatch queryHtlPreconcertItemBatchById(Long id);
}

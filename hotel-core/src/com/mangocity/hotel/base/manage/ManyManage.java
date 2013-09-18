package com.mangocity.hotel.base.manage;

import java.sql.SQLException;
import java.util.*;

import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlPreconcertItemBatch;
import com.mangocity.hotel.base.persistence.HtlPreconcertItemTemplet;
import com.mangocity.hotel.base.persistence.HtlRoomtype;

/**
 * add by 2009-2-1 多条款业务逻辑方法
 * 
 * @author lihaibo
 * 
 */
public interface ManyManage {
	/**
	 * 查询价格类型
	 * @param hotelId
	 * @return
	 */
    public List<HtlRoomtype> queryPriceType(long hotelId);

    /**
     * 查询所有条款
     * @param hotelId
     * @return
     */
    public List<HtlPreconcertItemTemplet> queryModel(long hotelId);

    /**
     * 根据预定条款查询内容
     * @param modelid
     * @param hotelid
     * @return
     */
    public List<HtlPreconcertItemTemplet> quertHotelAjaxModel(long modelid, long hotelid);

    /**
     * 调用存储过程
     * @param hotelid
     * @param id
     * @param contractId
     * @param priceTypeid
     * @param beginDate
     * @param engDate
     * @param active
     * @return
     * @throws SQLException
     */
    public boolean saveOrupdatePro(long hotelId, long id, long contractId, long priceTypeId,
        Date beginDate, Date engDate, String active) throws SQLException;

    /**
     * 插入批次表
     * @param htlPreconcertItemBatch
     * @return
     */
    public boolean saveOrupdateAll(HtlPreconcertItemBatch htlPreconcertItemBatch);

    /**
     * 根据合同id查询出合同开始时间和结束时间
     * @param ID
     * @return
     */
    public List<HtlContract> queryHtlContract(long ID);

    /**
     * 查询日志的方法最多显示5条
     * @param modelId
     * @return
     */
    public List<HtlPreconcertItemBatch> queryHtlBatch(long hotelId);

    /**
     * 查询条款模板
     * @param modelId
     * @return
     */
    public List<HtlPreconcertItemTemplet> queryModelOnly(long modelId);

}

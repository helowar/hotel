package com.mangocity.hotel.base.manage;

import java.sql.SQLException;
import java.util.*;

import com.mangocity.hotel.base.persistence.HtlPreconcertItemBatch;

/**
 * 2009-2-5 hotel V2.6 该接口用于定义设置单条款绑定的各种方法
 * 
 * @author lihaibo
 * 
 */
public interface OnlyClauserManage {
    // 查询酒店预定条款模板方法 add by haibo.li 2009-2-6 Hotel V2.6
    public List queryHotelModel(long hotelid);

    // 单条款查询条款模板方法 add by haibo.li 2009-2-12 Hotel V2.6
    public List quertHotelModelAll(long modelid, long hotelid);

    // 单条款插入批次表数据库
    public boolean saveOrUpdateClause(HtlPreconcertItemBatch htlPreconcertItemBatch);

    // 调用存储过程方法
    public boolean saveOrUpdateClausePro(long hotelid, long modelid, long contractId,
        String priceTypeid, Date beginDate, Date engDate, String active) throws SQLException;

    public List queryHtlContract(long contractid);

    // 查询日志的方法最多显示5条
    public List queryHtlBatch(long hotelId);

    // 根据ID查询出批次表的相关记录
    public HtlPreconcertItemBatch findHtlPreconcertItemBatch(Long id);

}

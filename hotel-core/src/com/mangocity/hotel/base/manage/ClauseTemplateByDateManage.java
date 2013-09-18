package com.mangocity.hotel.base.manage;

import java.sql.SQLException;
import java.util.*;

import com.mangocity.hotel.base.persistence.HtlPreconcertItem;
import com.mangocity.hotel.base.persistence.HtlRoomtype;

/**
 */
public interface ClauseTemplateByDateManage {

    /*
     * 根据日期查询出酒店预订担保预付条款的信息 add by shengwei.zuo 2009-02-18
     */
    public List findHtlPreconItemInfo(Long hotelId, String validDate);

    /*
     * 根据价格类型和日期查询出酒店预订担保预付条款的信息
     */
    public HtlPreconcertItem findHtlPreconItemInfo(Long hotelId,
        Long priceTypeId, String validDate);

    /*
     * 根据输入的起止日期，查看该酒店对应的日期列表 add by shengwei.zuo 2009-02-18
     */
    public List<HtlPreconcertItem> findDateLis(Long hotelId, String beginDate, String endDate);

    /*
     * 根据输入的起止日期，查看该酒店下的房型列表 add by shengwei.zuo 2009-05-22
     */

    public List<HtlRoomtype> findRoomTypeLis(Long hotelId, String validDate);

    /*
     * 
     * 修改每天的酒店预定条款相关信息 add by shengwei.zuo 2009-02-20
     */
    public long savePreconcertItem(HtlPreconcertItem htlPreconcertItem);

    /**
     * 保存时调用存储过程更新价格表
     * 
     * @param id
     * @param hotelid
     * @param date
     * @param priceid
     * @return
     * @throws SQLException 
     */
    public boolean proUpdateDate(long id, long hotelid, Date date, long priceid) 
    throws SQLException;

    /**
     * 根据批次表ID查询操作日志明细
     * 
     * @param id
     * @return
     */
    public List queryRecord(long id);

    /**
     * 根据酒店ID查询合同
     * 
     * @param hotelId
     * @return
     */
    public List queryContract(long hotelId);

}

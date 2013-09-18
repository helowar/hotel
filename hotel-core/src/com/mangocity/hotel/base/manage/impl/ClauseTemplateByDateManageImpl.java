package com.mangocity.hotel.base.manage.impl;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.*;

import com.mangocity.hotel.base.manage.ClauseTemplateByDateManage;
import com.mangocity.hotel.base.persistence.HtlPreconcertItem;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.log.MyLog;

/**
 */
public class ClauseTemplateByDateManageImpl extends DAOHibernateImpl implements
    ClauseTemplateByDateManage {

	private static final MyLog log = MyLog.getLogger(ClauseTemplateByDateManageImpl.class);
    /*
     * 根据日期查询出酒店预订担保预付条款的信息 add by shengwei.zuo 2009-02-18
     */
    public List findHtlPreconItemInfo(Long hotelId, String validDate) {

        String hsql = "FROM  HtlPreconcertItem  WHERE  hotel_id=?  "
            + "and  VALID_DATE = to_date(?,'YYYY-mm-dd') and ACTIVE = '1' ";

        Object[] obj = new Object[] { hotelId, validDate };

        List htlPI = super.query(hsql, obj);

        return htlPI;

    }

    /*
     * 根据输入的起止日期，查看该酒店对应的日期列表 add by shengwei.zuo 2009-02-18
     */
    public List<HtlPreconcertItem> findDateLis(Long hotelId, String beginDate, String endDate) {

        List queryDateList = new ArrayList();

        // String hsql = "FROM HtlPreconcertItem WHERE hotel_id=?
        // and VALID_DATE >= to_date(?,'YYYY-mm-dd') \n"+
        // " and VALID_DATE <= to_date(?,'YYYY-mm-dd')  order by VALID_DATE";

        String sql = "select distinct validDate from HtlPreconcertItem t where hotelId = ?"
            + "and validDate >= to_date(?, 'YYYY-mm-dd') "
            + "and validDate <= to_date(?,'YYYY-mm-dd') and t.active='1' "
            + "and t.priceTypeId is not null order by validDate";

        Object[] obj = new Object[] { hotelId, beginDate, endDate };

        queryDateList = super.query(sql, obj);

        return queryDateList;

    }

    /**
     * 根据批次表ID查找日期明细
     */

    public List queryRecord(long id) {
        List RecordList = new ArrayList();
        String sql = "from HtlModifRecord where recordId = ?";
        RecordList = super.query(sql, id);
        return RecordList;
    }

    /*
     * 
     * 修改每天的酒店预定条款相关信息 add by shengwei.zuo 2009-02-20
     */

    public long savePreconcertItem(HtlPreconcertItem htlPreconcertItem) {
        // TODO Auto-generated method stub
        try {
            super.merge(htlPreconcertItem);
        } catch (Exception e) {
        	log.error(e.getMessage(),e);
        }

        return 0;
    }

    /*
     * 根据价格类型和日期查询出酒店预订担保预付条款的信息
     */
    public HtlPreconcertItem findHtlPreconItemInfo(Long hotelId, 
        Long priceTypeId, String validDate) {

        String hsql = " from HtlPreconcertItem  where  hotelId = ? and priceTypeId = ? "
            + " and  validDate = to_date(?,'yyyy-MM-dd') and active = '1' ";

        Object[] obj = new Object[] { hotelId, priceTypeId, validDate };

        HtlPreconcertItem lisHtlPreconcertItem = (HtlPreconcertItem) super.find(hsql, obj);

        return lisHtlPreconcertItem;

    }

    /**
     * 更新每天表中数据时调用存储过程更新价格表中数据
     * @throws SQLException 
     */
    public boolean proUpdateDate(long id, long hotelid, Date date, long priceid) 
    throws SQLException {
        CallableStatement cstmt = null;
        try {
            String procedureName = "{call sp_hotel_trrms_Date_adjust(?,?,?,?)} ";
            cstmt = super.getCurrentSession().connection().prepareCall(
                procedureName);
            java.sql.Date bDate = new java.sql.Date(date.getTime());
            cstmt.setLong(1, id);
            cstmt.setLong(2, hotelid);
            cstmt.setDate(3, bDate);
            cstmt.setLong(4, priceid);
            cstmt.executeUpdate();
            cstmt.close();
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            cstmt.close();
            return false;
        }finally{
            cstmt.close();
        }

    }

    /**
     * 根据酒店ID查询合同
     */

    public List queryContract(long hotelId) {
        List list = new ArrayList();
        String sql = "from HtlContract where hotelId = ?";
        list = super.query(sql, hotelId);
        return list;
    }

    /*
     * 根据输入的起止日期，查看该酒店下的房型列表 add by shengwei.zuo 2009-05-22
     */

    public List<HtlRoomtype> findRoomTypeLis(Long hotelId, String validDate) {
        List queryRoomTypeList = new ArrayList();

        String sql = "select  m  from HtlPreconcertItem t,HtlRoomtype m,HtlPriceType "
            + " p   where t.hotelId = ?"
            + "and t.validDate = to_date(?,'YYYY-mm-dd') and t.active='1' "
            + "and  t.priceTypeId=p.ID  and p.roomType = m.ID " + " order by t.validDate";

        Object[] obj = new Object[] { hotelId, validDate };

        queryRoomTypeList = super.query(sql, obj);

        return queryRoomTypeList;

    }

}

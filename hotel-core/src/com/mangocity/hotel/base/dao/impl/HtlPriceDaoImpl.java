package com.mangocity.hotel.base.dao.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.mangocity.hotel.base.dao.HtlPriceDao;
import com.mangocity.hotel.base.persistence.HtlHdlAddscope;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.util.DateUtil;
import com.mangocity.util.dao.GenericDAOHibernateImpl;
import com.mangocity.util.log.MyLog;

/**
*@author chenhuizhong e-mail:huizong.chen@mangocity.com
*@version 1.0
*@since 1.0
*/
public class HtlPriceDaoImpl extends GenericDAOHibernateImpl implements HtlPriceDao
{

    private static final MyLog log = MyLog.getLogger(HtlPriceDaoImpl.class);
    
    public HtlPrice findPriceBizKey(long hotelId, long roomTypeId,
            long childRoomTypeId, String quotaType, Date ableSaleDate, String payMethod) {

        List<HtlPrice> lstPrice = this.queryByNamedQuery("findPriceBizKey",
                new Object[] { hotelId, roomTypeId, childRoomTypeId, quotaType,
                        payMethod, ableSaleDate });

        return lstPrice.isEmpty() ? null : lstPrice.get(0);
    }
    
    public void saveOrUpdatePrice(List<HtlPrice> lstPrice) {

        this.saveOrUpdateAll(lstPrice);
    }
    
    @SuppressWarnings({ "deprecation", "static-access" })
    public String saveOrUpdatePrice(long hotelId, long contractId,
            String quotaType, String currency, String week) {
        
        String procedureName = "{call SP_HOTEL_BATCHPRICE(?, ?, ?, ?, ?, ?)} ";
        Map<Integer, Object> inParamsIdxAndValue = new HashMap<Integer, Object>();
        Map<Integer, Integer> outParamsIdxAndType = new HashMap<Integer, Integer>();
        inParamsIdxAndValue.put(1, hotelId);
        inParamsIdxAndValue.put(2, contractId);
        inParamsIdxAndValue.put(3, quotaType);
        inParamsIdxAndValue.put(4, currency);
        inParamsIdxAndValue.put(5, week);
        outParamsIdxAndType.put(6, java.sql.Types.VARCHAR);
        try{
            this.execProcedure(procedureName, inParamsIdxAndValue, outParamsIdxAndType);
            
            return "1";
        }catch(DataAccessException e){
            this.log.error(e);
            
            return "0";
        }
    }

    /**
     * 加幅调用存储过程修改价格 add by zhineng.zhuang 2008-12-01
     * 
     * @return
     * @throws SQLException
     */
    @SuppressWarnings({ "deprecation", "unchecked", "static-access" })
    public String saveAddScopePrice(HtlHdlAddscope htlHdlAddscope)
            throws SQLException {
        
        long hotelId = htlHdlAddscope.getHotelId();
        long addScope = htlHdlAddscope.getAddScope();
        String beginDate = DateUtil.dateToString(htlHdlAddscope.getBeginDate());
        String endDate = DateUtil.dateToString(htlHdlAddscope.getEndDate());
        String allPriceTypeId = htlHdlAddscope.getAllPriceTypeId();
        String procedureName = "{call SP_HOTEL_ADDSCOPEPRICE(?,?,?,?,?,?)}";
        Map<Integer, Object> inParamsIdxAndValue = new HashMap<Integer, Object>();
        Map<Integer, Integer> outParamsIdxAndType = new HashMap<Integer, Integer>();
        inParamsIdxAndValue.put(1, hotelId);
        inParamsIdxAndValue.put(2, addScope);
        inParamsIdxAndValue.put(3, beginDate);
        inParamsIdxAndValue.put(4, endDate);
        inParamsIdxAndValue.put(5, allPriceTypeId);
        outParamsIdxAndType.put(6, java.sql.Types.VARCHAR);
        try{
            List<String> objList = (List<String>)this.execProcedure(procedureName, inParamsIdxAndValue, outParamsIdxAndType);

            return objList.get(0).toString();
        }catch(DataAccessException e){
            this.log.error(e);
            
            return "error";
        }
    }
    
    @SuppressWarnings("deprecation")
    public void saveOrUpdatePrice() {

        String procedureName = "{call PKGMTNPRICE.mtnPrice()} ";
        this.execProcedure( procedureName, null );
    }
}

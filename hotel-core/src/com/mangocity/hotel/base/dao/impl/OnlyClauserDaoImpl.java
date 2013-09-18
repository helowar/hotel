package com.mangocity.hotel.base.dao.impl;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.dao.IOnlyClauserDao;
import com.mangocity.hotel.base.manage.impl.OnlyClauserManageImpl;
import com.mangocity.hotel.base.persistence.HtlPreconcertItemBatch;
import com.mangocity.hotel.base.persistence.HtlPreconcertItemTemplet;
import com.mangocity.hotel.base.persistence.HtlPrepayItemTemplateOne;
import com.mangocity.util.dao.GenericDAOHibernateImpl;
import com.mangocity.util.log.MyLog;

/**
 */
public class OnlyClauserDaoImpl<T> extends GenericDAOHibernateImpl implements IOnlyClauserDao{
	
	private static final MyLog log = MyLog.getLogger(OnlyClauserDaoImpl.class);
	
	public List<HtlPreconcertItemTemplet> queryHtlPreconcertItemTempletListByHotelId(Long hotelId){
		String hql = "from HtlPreconcertItemTemplet where hotelID=? and Active=1";
		return super.query(hql, new Object[]{hotelId}, 0, 0, false);
	}
	
	
	public boolean saveOrUpdateClause(HtlPreconcertItemBatch htlPreconcertItemBatch){
		try{
			super.save(htlPreconcertItemBatch);
		    return true;
		 }catch (Exception e) {
		    log.debug(e.getMessage(),e);
		    return false;
		 }
	}
	
    public boolean saveOrUpdateClausePro(long hotelId, long id, long contractId,
        String priceTypeid, Date beginDate, Date engDate, String active) throws SQLException {
    	boolean isSucess = false;
    	CallableStatement cstmt = null;
        try {
            String procedureName = "{call sp_hotel_Terms_scheduled(?,?,?,?,?,?,?)} ";
            cstmt = super.getSession().connection().prepareCall(
                procedureName);
            java.sql.Date bDate = new java.sql.Date(beginDate.getTime());
            java.sql.Date eDate = new java.sql.Date(engDate.getTime());
            cstmt.setLong(1, hotelId);
            cstmt.setLong(2, id);
            cstmt.setLong(3, contractId);
            cstmt.setString(4, priceTypeid);
            cstmt.setDate(5, bDate);
            cstmt.setDate(6, eDate);
            cstmt.setString(7, active);
            // cstmt.registerOutParameter(7,oracle.jdbc.driver.OracleTypes.VARCHAR) ;
            cstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            log.error(" 存储过程出问题了!",e);
            return false;
        }finally{
        	cstmt.close();
        }
        
    }
    
    public List<HtlPreconcertItemTemplet> queryHtlPreconcertItemTempletById(Long modelId){
    	 String hql = "from HtlPreconcertItemTemplet where ID= ? and Active=1";
    	 return super.query(hql, new Object[]{modelId}, 0, 0, false);
    }
    
    public List<HtlPreconcertItemBatch> queryHtlPreconcertItemBatchListByHotelId(Long hotelId){
    	 String hql = "from HtlPreconcertItemBatch where hotel_id=? and doubletofalg=1 order by ID desc";
    	 return super.query(hql, new Object[]{hotelId}, 0, 5, false);
    }
    
    public HtlPreconcertItemBatch queryHtlPreconcertItemBatchById(Long id){
    	return (HtlPreconcertItemBatch) super.get(HtlPreconcertItemBatch.class, id);
    }
    
    

}

package com.mangocity.hotel.base.service.impl;

import java.sql.CallableStatement;
import java.sql.SQLException;

import com.mangocity.hotel.base.service.IHotelMessyCodeFixService;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.log.MyLog;

/**
 * 酒店乱码修复调用接口实现类
 * @author chenjiajie
 *
 */
public class HotelMessyCodeFixService extends DAOHibernateImpl implements IHotelMessyCodeFixService {

    /**
     * 
     */
    private static final long serialVersionUID = 8601251588722545271L;
    
    private static final MyLog log = MyLog.getLogger(HotelMessyCodeFixService.class);

    /**
     * 调用乱码修复的存储过程
     * @param hotelId
     * @return
     */
    @SuppressWarnings("deprecation")
    public String callFixMessyCodeProcdure(Long hotelId) {
        CallableStatement cstmt = null;
        try {
            String procedureName = "{call p_update_hotel_chinese(?)}";
            cstmt = super.getCurrentSession().connection().prepareCall(procedureName);
            cstmt.setLong(1, hotelId);
            cstmt.execute();
        } catch (Exception e) {
            log.error("callFixMessyCodeProcdure error! the cause is:" + e);
        } finally{
            try {
                if(null!=cstmt){
                    cstmt.close();
                }
            } catch (SQLException e) {
                log.error("callFixMessyCodeProcdure finally error! the cause is:" + e);
            }
        }
        return null;
    }

}

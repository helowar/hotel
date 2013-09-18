package com.mangocity.hotel.base.dao.impl;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;

import com.mangocity.hotel.base.dao.IHotelSynDao;
import com.mangocity.util.DateUtil;
import com.mangocity.util.dao.GenericDAOHibernateImpl;
import com.mangocity.util.log.MyLog;

public class HotelSynDaoImp extends GenericDAOHibernateImpl implements IHotelSynDao {

	private static final MyLog log = MyLog.getLogger(HotelSynDaoImp.class);
	
	public void hotelSynByHotelId(String hotelId) {
		try {
        String procedureName = "{ call sp_importDataByHotelId(?,?,?,?)} ";
        CallableStatement cstmt = (CallableStatement)super.getCurrentSession().connection().prepareCall(procedureName);
        
        int count=0;
        Date curentDate = new Date();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(curentDate);
        calendar.add(Calendar.DATE, 60); 
        
		cstmt.setString(1,DateUtil.formatDateToSQLString(curentDate));
        cstmt.setString(2,DateUtil.formatDateToSQLString(calendar.getTime()));
        cstmt.setString(3, hotelId);
        cstmt.registerOutParameter(4, Types.INTEGER);
        cstmt.executeUpdate();
        count=cstmt.getInt(4);
        
        log.info("hotelSyn Count="+count);
		} catch (SQLException e) {
			e.printStackTrace();
			 log.error(" callsp_importDataByHotelId error: HotelSynDaoImp error!" ,e);
		}
    
	}

}

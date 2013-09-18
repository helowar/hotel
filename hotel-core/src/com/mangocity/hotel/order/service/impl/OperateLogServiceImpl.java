package com.mangocity.hotel.order.service.impl;

import java.util.Date;
import java.util.List;

import com.mangocity.hdl.hotel.dto.CheckReservateExRequest;
import com.mangocity.hotel.order.dao.IOperateLogDao;
import com.mangocity.hotel.order.persistence.HtlCheckReservateLog;
import com.mangocity.hotel.order.service.IOperateLogService;
import com.mangocity.util.DateUtil;

public class OperateLogServiceImpl implements IOperateLogService {
	private IOperateLogDao operateLogDao;
	
	public void makeCheckReserLogObject(CheckReservateExRequest req,Date operateBeginDate,Date operateEndDate,String operateType,Date createTime){
		HtlCheckReservateLog checkReserLog = new HtlCheckReservateLog();
		checkReserLog.setAppName("HOP");
		checkReserLog.setCheckInDate(DateUtil.getDate(req.getCheckInDate()));
		checkReserLog.setChannelType(req.getChannelType());
		checkReserLog.setCheckOutDate(DateUtil.getDate(req.getCheckInDate()));
		checkReserLog.setHotelId(req.getHotelId());
		checkReserLog.setOperateBeginTime(operateBeginDate);
		checkReserLog.setOperateEndTime(operateEndDate);
		checkReserLog.setOperateType(operateType);
		checkReserLog.setOperateUsedTime(operateEndDate.getTime() - operateBeginDate.getTime());
		checkReserLog.setPriceTypeId(req.getChildRoomTypeId());
		checkReserLog.setRoomTypeId(req.getRoomTypeId());
		checkReserLog.setCreateTime(createTime);
		saveOrUpdate(checkReserLog);
	}
	
    public void saveOrUpdateAll(List<HtlCheckReservateLog> checkLogList){
    	if(null!= checkLogList && !checkLogList.isEmpty()){
    		operateLogDao.saveOrUpdateAll(checkLogList);
    	}
    }
    
    public void saveOrUpdate(HtlCheckReservateLog checkLog){
    	if(null!= checkLog){
    		operateLogDao.saveOrUpdate(checkLog);
    	}
    }
	
	public void setOperateLogDao(IOperateLogDao operateLogDao) {
		this.operateLogDao = operateLogDao;
	}

}

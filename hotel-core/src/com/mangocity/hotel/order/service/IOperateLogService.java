package com.mangocity.hotel.order.service;

import java.util.Date;
import java.util.List;

import com.mangocity.hdl.hotel.dto.CheckReservateExRequest;
import com.mangocity.hotel.order.persistence.HtlCheckReservateLog;

public interface IOperateLogService {
	
	/**
	 * 批量保存HtlCheckReservateLog
	 * @param checkLogList
	 */
	public void saveOrUpdateAll(List<HtlCheckReservateLog> checkLogList);
	
	/**
	 * HtlCheckReservateLog对象组装
	 * @param req
	 * @param operateBeginDate
	 * @param operateEndDate
	 * @param operateType
	 * @return
	 */
	public void makeCheckReserLogObject(CheckReservateExRequest req,Date operateBeginDate,Date operateEndDate,String operateType,Date createTime);
}

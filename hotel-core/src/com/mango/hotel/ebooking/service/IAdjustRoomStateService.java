package com.mango.hotel.ebooking.service;

import java.io.Serializable;

import com.mangocity.hotel.user.UserWrapper;

/**
 * EBooking房态审核操作业务接口
 * 
 * @author chenjiajie
 * 
 */
public interface IAdjustRoomStateService extends Serializable {
	/**
	 * 
	 * @param adjustBeanIds 审核记录的IDs
	 * @param roleUser 当前用户 
	 * @param auditResult 结果，0 是否决，1是通过
	 */
   public void updateRoomState(String adjustBeanIds, UserWrapper roleUser, int auditResult);
   	/**
   	 * 查出未审核的房态记录
   	 * @param theAreaLoginerCanCheck
   	 * @return
   	 */
	public int getUnAuditRoomstate(String theAreaLoginerCanCheck);
}

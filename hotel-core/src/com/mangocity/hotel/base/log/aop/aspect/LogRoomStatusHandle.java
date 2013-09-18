package com.mangocity.hotel.base.log.aop.aspect;


import org.aspectj.lang.JoinPoint;

import com.mangocity.hotel.base.manage.assistant.RoomStateBean;
import com.mangocity.hotel.base.persistence.HtlRoomStatusProcess;
import com.mangocity.util.DateUtil;
import com.mangocity.util.dao.DAO;
import com.mangocity.util.log.MyLog;

/**
 * AOP 修改房态的操作 日志 类
 * 
 * add by shengwei.zuo 2010-3-29
 * 
 */
public class LogRoomStatusHandle {
	
	private static final MyLog log = MyLog.getLogger(LogRoomStatusHandle.class);
    
    private DAO  entityManager;

    /**
     * 修改房态的操作类，进行日志写入
     */
    public void logRoomStatus(JoinPoint jp) {
    	
    	   Object[] objLog =  jp.getArgs();
    	   
    	   String methodStr =jp.getSignature().getName();
       	
    	   log.info("=====================修改房态日志的写入=====Aop method : "+methodStr);
    	
    	   RoomStateBean roomStateBean = new RoomStateBean();
           roomStateBean = (RoomStateBean) objLog[0];
           if (null != roomStateBean) {
               HtlRoomStatusProcess rsp = new HtlRoomStatusProcess();
               rsp.setHotelId(roomStateBean.getHotelID());
               rsp.setProcessBy(roomStateBean.getUserName());
               rsp.setProcessById(roomStateBean.getUserId());
               rsp.setProcessDate(DateUtil.getSystemDate());
               rsp.setProcessRemark(roomStateBean.getProcessRemark());
               rsp.setProcessDatetime(DateUtil.getSystemDate());
               rsp.setIsRoomStatusReport(Long.valueOf(roomStateBean.getIsRoomStatusReport())
                   .longValue());
               entityManager.saveOrUpdate(rsp);
           } else {
               log.info("roomStateBean is null!");
           }
    	
    }
   
	public DAO getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(DAO entityManager) {
		this.entityManager = entityManager;
	}
	
}

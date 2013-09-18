package com.mangocity.hotel.util;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.mangocity.ep.service.EpDailyAuditService;
import com.mangocity.util.log.MyLog;

public class EpOrderAuditSchedule extends QuartzJobBean{
    
	private static final MyLog log = MyLog.getLogger(EpOrderAuditSchedule.class);
	
	private EpDailyAuditService epDailyAuditService;
	
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		
		if(epDailyAuditService.queryEpHotelId().size()>0){
			epDailyAuditService.saveAuditOrder();
			epDailyAuditService.saveAuditOrderItem();
		}
		
	}

	public EpDailyAuditService getEpDailyAuditService() {
		return epDailyAuditService;
	}

	public void setEpDailyAuditService(EpDailyAuditService epDailyAuditService) {
		this.epDailyAuditService = epDailyAuditService;
	}
	
	
}

package com.mangocity.hotel.util;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.mangocity.hotel.base.service.EpOrderService;
import com.mangocity.util.log.MyLog;

public class EpOrderManagerSchedule extends QuartzJobBean{
    
	private static final MyLog log = MyLog.getLogger(EpOrderManagerSchedule.class);
	
	private EpOrderService epOrderService;
	
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		log.info("============== save EpOrder start ===========");
		List<String> list = epOrderService.queryEpHotelId();
		epOrderService.saveEpOrderData(list);
		log.info("============== save EpOrder end ===========");		
	}

	public EpOrderService getEpOrderService() {
		return epOrderService;
	}

	public void setEpOrderService(EpOrderService epOrderService) {
		this.epOrderService = epOrderService;
	}
	
	
}

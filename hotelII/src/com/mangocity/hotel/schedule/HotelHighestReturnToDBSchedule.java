package com.mangocity.hotel.schedule;

import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.mangocity.hotel.pricelowest.service.HotelHighestReturnService;
import com.mangocity.util.log.MyLog;

public class HotelHighestReturnToDBSchedule extends QuartzJobBean {
     
	private final MyLog log = MyLog.getLogger(this.getClass());
	
	private HotelHighestReturnService hotelHighestReturnService;

	public void setHotelHighestReturnService(
			HotelHighestReturnService hotelHighestReturnService) {
		this.hotelHighestReturnService = hotelHighestReturnService;
	}

	@Override
	protected void executeInternal(JobExecutionContext arg0) {
		try{
			hotelHighestReturnService.saveOrUpdateHtlHighestReturns(HotelHighestReturnService.QUERY_SIZE);
		}catch(Exception e){ 
			log.error("HotelHighestReturnToDBSchedule error:",e);
		}
	}

}

package com.mangocity.hotel.schedule;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.mangocity.hotel.pricelowest.service.HotelLowestPriceService;
import com.mangocity.util.log.MyLog;

public class HotelLowestTaskToDBSchedule extends QuartzJobBean{

	private final MyLog log = MyLog.getLogger(this.getClass());
	//注入的service
	private  HotelLowestPriceService hotelLowestPriceService;
	@Override
	protected void executeInternal(JobExecutionContext arg0) {
		try{
			hotelLowestPriceService.initHtlLowestTask();
		}catch(Exception e){
			log.error("HotelLowestTaskToDBSchedule error",e);
		}
	}
	public void setHotelLowestPriceService(
			HotelLowestPriceService hotelLowestPriceService) {
		this.hotelLowestPriceService = hotelLowestPriceService;
	}
	
}

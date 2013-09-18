package com.mangocity.hotel.job;

import java.util.Calendar;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.mangocity.hotel.base.persistence.HtlGeographicalposition;
import com.mangocity.hotel.search.index.HotelInfoIndexer;
import com.mangocity.util.log.MyLog;

public class HtlGeographicalpositionSchedule extends QuartzJobBean{

	private HotelInfoIndexer hotelInfoIndexer;

	private MyLog log = MyLog.getLogger(HtlGeographicalposition.class);

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {

		// 系统前一天的时间
		Calendar nowCal = Calendar.getInstance();
		nowCal.add(Calendar.DAY_OF_MONTH, -1);

		log.info("前一天的时间：" + nowCal.getTime());

		try {
		//	hotelInfoIndexer.updateMgisInfoDoc(nowCal.getTime());
		} catch (Exception e) {
			throw new JobExecutionException(e);
		}
	}

	public void setHotelInfoIndexer(HotelInfoIndexer hotelInfoIndexer) {
		this.hotelInfoIndexer = hotelInfoIndexer;
	}	
}

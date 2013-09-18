package com.mangocity.hotel.schedule;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hotel.pricelowest.service.HotelHighestReturnService;
import com.mangocity.hotel.pricelowest.service.HotelLowestPriceService;
import com.mangocity.util.log.MyLog;

public class  HotelWebAllInfoSchedule extends QuartzJobBean {
	private final MyLog log = MyLog.getLogger(this.getClass());
	private  HotelLowestPriceService hotelLowestPriceService;
	private  HotelHighestReturnService hotelHighestReturnService;

	@Override
	protected void executeInternal(JobExecutionContext arg0){
		try{
		   String fileContext = hotelLowestPriceService.getHtlLowestPricesForJS();
		   writeToFile("HotelWebAllInfoNew.js",fileContext);
		   
		   
		   String highestReturnFileContext = hotelHighestReturnService.getHtlHighestReturnsForJS();
		   writeToFile("HotelWebAllInfoHighestReturn.js",highestReturnFileContext);
		}catch(Exception e){
			log.error("HotelWebAllInfoSchedule error:"+e);
		}
	}
	
	private void writeToFile(String FileName,String fileContext){
		OutputStreamWriter output=null;
        try {
            output = new OutputStreamWriter(new FileOutputStream( InitServlet.saveHWEBpath+"/"+FileName),"UTF-8");
            output.write(fileContext);
        } catch (IOException e) {
        	log.error("HotelWebAllInfoSchedule js write io error:"+e);
        }finally{
            try {
                if(null != output){
                    output.close();
                }
            } catch (IOException e) {
                log.error("HotelWebAllInfoSchedule js write close stream error:"+e);
            }
        }
	}


	public void setHotelLowestPriceService(
			HotelLowestPriceService hotelLowestPriceService) {
		this.hotelLowestPriceService = hotelLowestPriceService;
	}
	
	public void setHotelHighestReturnService(
			HotelHighestReturnService hotelHighestReturnService) {
		this.hotelHighestReturnService = hotelHighestReturnService;
	}
}

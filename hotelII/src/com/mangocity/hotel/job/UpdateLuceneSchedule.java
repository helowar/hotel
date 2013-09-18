package com.mangocity.hotel.job;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.hotel.base.service.SystemDataService;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hotel.search.index.HotelInfoIndexer;
import com.mangocity.util.hotel.constant.AutoSendSMSConstant;

public class UpdateLuceneSchedule extends QuartzJobBean implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(UpdateLuceneSchedule.class);
	
	private HotelInfoIndexer hotelInfoIndexer;
	
	private SystemDataService systemDataService;

	
	protected void executeInternal(JobExecutionContext ctx)
			throws JobExecutionException {
		
//		OrParam orParam = systemDataService.getSysParamByName("UPDATE_LUCENE");
//
//        Date newDate = new Date();
//        if (null == orParam) { // 如果从表中取出为空,则赋值
//        	orParam = new OrParam();
//        	orParam.setName(AutoSendSMSConstant.NAME);
//        	orParam.setValue(AutoSendSMSConstant.SENDWORKING);
//        	orParam.setModifyTime(newDate);
//        	systemDataService.updateSysParamByName(orParam); // 更数数据
//        } else {
//            if (10 * 60000 > (newDate.getTime() - orParam.getModifyTime().getTime())) {
//                // 如果当前日期与param的时间之差小于1分钟,则是集群重新发送,
//                // 采用不响应方式
//                return;
//            } else {
//            	orParam.setValue(AutoSendSMSConstant.SENDWORKING);
//            	orParam.setModifyTime(newDate);
//            	systemDataService.updateSysParamByName(orParam); // 更数数据
//            }
//        }
//		
//		
//		if(1 > InitServlet.businessSozeObj.size()) {
//			log.info("InitServlet尚未启动");
//			return;
//		}
//		
////		boolean bInit = hotelInfoIndexer.testInit();		
////		if(!bInit) {
////			log.info("lucene索引文件可能已被锁住");
////			return;
////		}
//
//		log.info("开始创建lucene索引文件");
//		long lBegin = System.currentTimeMillis();
//		
//		hotelInfoIndexer.createHotelInfo2Index();
//				
//		log.info("创建lucene索引文件共花 : "
//				+ (System.currentTimeMillis() - lBegin) + "毫秒");
		
	}


	public void setHotelInfoIndexer(HotelInfoIndexer hotelInfoIndexer) {
		this.hotelInfoIndexer = hotelInfoIndexer;
	}
	
}

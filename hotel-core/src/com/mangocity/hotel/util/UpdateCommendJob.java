package com.mangocity.hotel.util;

import java.io.Serializable;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.mangocity.hotel.base.service.SystemDataService;
import com.mangocity.hotel.base.util.SendLuceneMQ;
import com.mangocity.util.log.MyLog;

/**
 * 推荐级别到期后，通过定时器查出酒店ID，发MQ消息，然后同步到lucene（同步推荐级别昨天到期的酒店）
 * @author zengzhouwu
 *
 */
public class UpdateCommendJob extends QuartzJobBean implements Serializable {
	private static final MyLog log = MyLog.getLogger(UpdateCommendJob.class);
	
	private SystemDataService systemDataService;
	
	private SendLuceneMQ sendLuceneMQ;
	
    protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
    	//查询数据库，查出推荐级别结束日期为昨天的酒店ID
    	List<String> hotelIdLst = systemDataService.queryID();
    	if(null!=hotelIdLst&&hotelIdLst.size()>0) {
    		log.info("以下酒店推荐级别到期，需要更新："+hotelIdLst.toString());
    		for(String hotelId:hotelIdLst) {
    			//发送MQ消息
    			sendLuceneMQ.send("hotelinfo#"+hotelId);
        	}
    	}
    }

	public SystemDataService getSystemDataService() {
		return systemDataService;
	}

	public void setSystemDataService(SystemDataService systemDataService) {
		this.systemDataService = systemDataService;
	}

	public SendLuceneMQ getSendLuceneMQ() {
		return sendLuceneMQ;
	}

	public void setSendLuceneMQ(SendLuceneMQ sendLuceneMQ) {
		this.sendLuceneMQ = sendLuceneMQ;
	}
}

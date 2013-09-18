package com.mangocity.hotel.util;

import hk.com.cts.ctcp.hotel.service.ICtsAuditManager;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.hotel.base.service.SystemDataService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.log.MyLog;


/**
 * 中旅订单日审
 * 
 * @author chenkeming
 *
 */
public class CtsAuditTrigger extends QuartzJobBean {
	private static final MyLog log = MyLog.getLogger(CtsAuditTrigger.class);
    
    private ICtsAuditManager ctsAuditManage;
    
    private SystemDataService systemDataService;
    
    private static final String SYS_PARAM_CTS_AUDIT_JOB = "CTS_JOB_STATE";

	/** 
     * 中旅订单日审job
     * 
     * (non-Javadoc)
     * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
     */
    protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
    	
    	try {
    		long lSleep = Math.round(Math.random() * (60000)); 
    		Thread.sleep(lSleep);
    		log.info("中旅订单日审之前,sleep : " + lSleep + "毫秒");
    	} catch (Exception e) {
    		log.error(e.getMessage(),e);
    	}
    	
        OrParam orParam = systemDataService.getSysParamByName(SYS_PARAM_CTS_AUDIT_JOB);        
        Date now = new Date();        
        if (DateUtil.getDay(orParam.getModifyTime(), now) > 0) {
        	log.info("中旅订单日审开始！");
            orParam.setModifyTime(now);
            systemDataService.updateSysParamByName(orParam);
            ctsAuditManage.getCtsAuditOrder();
            log.info("中旅订单日审完成！");
        }
    }

	public void setCtsAuditManage(ICtsAuditManager ctsAuditManage) {
		this.ctsAuditManage = ctsAuditManage;
	}
	
	public void setSystemDataService(SystemDataService systemDataService) {
		this.systemDataService = systemDataService;
	}

}

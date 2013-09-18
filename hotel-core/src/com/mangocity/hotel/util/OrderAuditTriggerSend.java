package com.mangocity.hotel.util;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.hotel.base.persistence.OrWorkStates;
import com.mangocity.hotel.base.service.SystemDataService;
import com.mangocity.hotel.order.service.IAuditOrderService;
import com.mangocity.hotel.order.web.AutoSendFax;
import com.mangocity.util.DateUtil;
import com.mangocity.util.collections.FormatMap;
import com.mangocity.util.log.MyLog;


public class OrderAuditTriggerSend extends QuartzJobBean implements Serializable {
	private static final MyLog log = MyLog.getLogger(OrderAuditTriggerSend.class);
    
    private SystemDataService systemDataService;

	private IAuditOrderService auditOrderService;

    /** 
     * 自动发送传真
     * 
     * (non-Javadoc)
     * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
     */
    protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
    	
    	try {
    		long lSleep = Math.round(Math.random() * (60000)); 
    		Thread.sleep(lSleep);
    		log.info("自动日审发送日审传真之前,sleep : " + lSleep + "毫秒");
    	}
    	catch (Exception e) {
    		log.error(e.getMessage(),e);
    	}
    	
    	OrParam orParam = systemDataService.getSysParamByName("IS_SEND_FAX");
        OrWorkStates user = new OrWorkStates();
        user.setLogonId("SYSTEM");
        user.setName("系统");
        
        Date now = new Date();
        Map params = new FormatMap();
        params.put("auditDate", DateUtil.dateToString(new Date()));
        if (DateUtil.getDay(orParam.getModifyTime(), now) > 0) {
        	log.info("自动发送传真开始！");
            // orParam.setValue(IsAutomatismSendFax.SENDWORKING);
            orParam.setModifyTime(now);
            systemDataService.updateSysParamByName(orParam);
            AutoSendFax asf = new AutoSendFax(params, user, auditOrderService);
            asf.start();
            log.info("自动发送传真完成！");
        }         
    }

	public IAuditOrderService getAuditOrderService() {
		return auditOrderService;
	}

	public void setAuditOrderService(IAuditOrderService auditOrderService) {
		this.auditOrderService = auditOrderService;
	}
	
	public void setSystemDataService(SystemDataService systemDataService) {
		this.systemDataService = systemDataService;
	}

}

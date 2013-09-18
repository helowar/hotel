package com.mangocity.hotel.order.web;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.OrWorkStates;
import com.mangocity.hotel.order.persistence.DaDailyaudit;
import com.mangocity.hotel.order.persistence.DaPaperFaxItem;
import com.mangocity.hotel.order.service.IAuditOrderService;
import com.mangocity.util.StringUtil;
import com.mangocity.util.log.MyLog;

/**
 * 自动发送日审传真
 * 
 * @author chenkeming
 *
 */
public class AutoSendFax extends Thread {
	
	private static final MyLog log = MyLog.getLogger(AutoSendFax.class);

    private List channels;

    IAuditOrderService auditOrderService;

//    private HraManager hraManager;

    private OrWorkStates user;

    public AutoSendFax(Map params, OrWorkStates workuser, 
    		IAuditOrderService autoAuditOrderService) {
    	auditOrderService = autoAuditOrderService;
        channels = auditOrderService.getChannels(params);
        user = workuser;        
    }

    public void run() {        
        try {
        	if(channels == null) {
        		return;
        	}
        	int nSize = channels.size();
            for (int i = 0; i < nSize; i++) {
            	try {
                    Long ret = null;
                    Long auditId = Long.parseLong(channels.get(i).toString());                
                    DaDailyaudit dailyAudit = auditOrderService.getDaDailyAudit(auditId);
                    Long channelId = dailyAudit.getChannelid();
                    
                    // 获取该渠道的传真号码
                    Date now = new Date();
                    String faxNo = auditOrderService.getFaxNoByChannelId(channelId, now);

                	now = new Date();
                    DaPaperFaxItem dailyFaxItem = new DaPaperFaxItem();                        
                    dailyFaxItem.setChannelId(channelId);
                    dailyFaxItem.setDailyAudit(dailyAudit);
                    dailyFaxItem.setFax(faxNo);
                    dailyFaxItem.setCreatedBy(user.getLogonId());
                    dailyFaxItem.setCreateDate(now);
                    if (StringUtil.isValidStr(faxNo)) {
                        try {
                            ret = auditOrderService.sendAuditFax(dailyAudit, faxNo, user,null);                        
                            if (null != ret) {
                            	dailyAudit.setReturnid("" + ret);
                            	dailyAudit.setSendsucceed(dailyAudit.getSendsucceed() + 1);
                                dailyFaxItem.setSendState(0);
                                auditOrderService.saveOrUpdateDailyAudit(dailyAudit);                            
                                auditOrderService.noteLog(true, channelId, 
                                		dailyAudit.getAuditdate(),
                                    faxNo, user, dailyAudit.getChannelname(),null);
                            } else {
                            	dailyAudit.setSendfailure(dailyAudit.getSendfailure() + 1);
                            	dailyFaxItem.setSendState(1);
                            	auditOrderService.saveOrUpdateDailyAudit(dailyAudit);                        	
                            	auditOrderService.noteLog(false, channelId, 
                            			dailyAudit.getAuditdate(),
                                    faxNo, user, dailyAudit.getChannelname(),null);
                            }
                        } catch (Exception e) {
                        	log.error("自动发送日审传真出异常,渠道ID:" + channelId + "\n" + e);
                        	
                        	dailyFaxItem.setSendState(1);
                            // 发送日志
                        	auditOrderService.noteLog(false, channelId, 
                        			dailyAudit.getAuditdate(),
                                faxNo, user, dailyAudit.getChannelname(),null);
                        }
                    } else {
                    	dailyFaxItem.setSendState(1);
                    	auditOrderService.noteLog(false, channelId, 
                    			dailyAudit.getAuditdate(), faxNo,
                            user, dailyAudit.getChannelname(),null);
                    }
                    auditOrderService.saveOrUpdateFaxItem(dailyFaxItem);	
            	} catch (Exception e) {
            		log.error(e);
            	}
            }
        } catch (Exception e) {
        	log.error(e.getMessage(),e);
        } finally {
            /*orParam.setValue(IsAutomatismSendFax.SENDSTOP);
            hraManager.updateSendFax(orParam);*/
        }
        
    }
    
}
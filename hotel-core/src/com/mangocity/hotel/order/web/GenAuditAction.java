package com.mangocity.hotel.order.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.web.webwork.GenericAction;
import com.mangocity.hotel.order.manager.HraManager;
import com.mangocity.hotel.order.persistence.DaDailyaudit;
import com.mangocity.hotel.order.persistence.DaPaperFaxItem;
import com.mangocity.hotel.order.persistence.HtlAuditInfo;
import com.mangocity.hotel.order.service.IAuditOrderService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;

/**
 * 日审订单相关操作
 * 
 * @author chenkeming
 * 
 */
public class GenAuditAction extends GenericAction {
	
	private Date auditDate;
	
	private List<Object[]> newChannel;
	
	private List<Object[]> editChannel;
	
	private IAuditOrderService auditOrderService;
	
	private HraManager hraManager;
	
	private String channelName;	
    
    private List auditLogList;
    
    private long sendAuditId;
    
    private String selIDs;
    
    /**
     * 批量获取
     */
    private Long[] selID;
	
	/**
	 * 生成日审记录
	 * 
	 * @return
	 * @throws Exception
	 */
	public String gen() throws Exception {
				
		Long channelId = 0L; 		
		if(StringUtil.isValidStr(channelName)) {
			/*channelName = new String(request.getParameter("channelName").trim()
	                .getBytes("ISO8859_1"), "utf-8");*/
			List li = auditOrderService.getAuditInfoByChannelName(channelName);
			if(null != li && li.size() > 0) {
				channelId = ((HtlAuditInfo)li.get(0)).getID();
			} else {
				request.setAttribute("errMsg", "不存在该渠道,请填写完整渠道名称");
				return "work_send_audit";
			}
		}
		        
        newChannel = new ArrayList<Object[]>();
        editChannel = new ArrayList<Object[]>();        
		
        auditOrderService.genAuditRecords(DateUtil.getDate(auditDate, -1), channelId,  
        		newChannel, editChannel);               
        
		return "work_send_audit";
	}
	
    /**
     * 自动发送日审传真
     * 
     * @return
     */
    public String autoSendAuditFax() {    	
        // OrParam orParam = hraManager.getIsSendFax();
        /*try {
            new String(request.getParameter("hotelChannel").trim()
                .getBytes("ISO8859_1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
        	log.error(e);
        }*/
        user = super.getOnlineWorkStates();
        Map params = super.getParams();
        params.put("channelName", channelName);
            AutoSendFax asf = new AutoSendFax(params, user, auditOrderService);
            asf.start();
        return forwardMsgBox("发送传真成功！", "refreshSelf()");
    }

    /**
     * 发送日审传真日志
     * 
     * @return
     */
    public String faxLogList() {
        auditLogList = auditOrderService.getAuditLogList(auditDate);
        return "faxLogList";
    }
    
    /**
     * 发送日审酒店传真
     * 
     * @return
     */
    public String sendAuditChannel() {
    	user = super.getOnlineWorkStates();
    	int nRes = sendSingleAuditChannel(sendAuditId);
    	if(1 == nRes) {
    		return forwardMsgBox("发送传真成功！", "refreshSelf()");
    	} else if(2 == nRes) {
    		return forwardMsgBox("发送传真失败！", "refreshSelf()");
    	} else {
    		return forwardMsg("确认传真号码！");
    	}
    }
    
    /**
     * 批量发送日审酒店传真
     * 
     * @return
     */
    public String sendAllAuditChannel() {    	
    	user = super.getOnlineWorkStates();
        Map params = super.getParams();
        selIDs = (String) params.get("selIDs");
        String[] ids = selIDs.split(",");
        selID = new Long[ids.length];
        if (0 >= selID.length) {
            return forwardError("请选择要发送的酒店!");
        }
        int nSuc = 0;
        int nFail = 0;
        int nNoFax = 0;
        for (int i = 0; i < selID.length; i++) {
            selID[i] = Long.parseLong(ids[i]);
            int nRes = sendSingleAuditChannel(selID[i]);
            if(1 == nRes) {
            	nSuc ++;
            } else if(2 == nRes) {
            	nFail ++;
            } else {
            	nNoFax ++;
            }
        }
        String res = "发送结果: " + (nSuc > 0 ? ("成功渠道数:" + nSuc + ",") : "")
        	+ (nFail > 0 ? ("失败渠道数:" + nFail + ",") : "")
        	+ (nNoFax > 0 ? ("无传真渠道数:" + nSuc + ",") : "");
        return forwardMsgBox(res, "refreshSelf()");
    }
    
    /**
     * 发送单个日审渠道传真
     * 
     * @return
     */
    private int sendSingleAuditChannel(long auditId) {

        Long ret = null;
        boolean bResult = false;        
        DaDailyaudit dailyAudit = auditOrderService.getDaDailyAudit(auditId);
        //酒店备注功能
    	String remarkToHotel = (String)super.getParams().get("remarkToHotel");
        // 获取传真号码
        Date now = new Date();
        Long channelId = dailyAudit.getChannelid();
        String faxNo = auditOrderService.getFaxNoByChannelId(
        		channelId, now);        
        
        DaPaperFaxItem dailyFaxItem = new DaPaperFaxItem();
        dailyFaxItem.setChannelId(dailyAudit.getChannelid());
        dailyFaxItem.setDailyAudit(dailyAudit);
        dailyFaxItem.setFax(faxNo);
        dailyFaxItem.setCreatedBy(user.getLogonId());
        dailyFaxItem.setCreateDate(now);
        if (StringUtil.isValidStr(faxNo)) {
            // 发送传真
        	try {
        		ret = auditOrderService.sendAuditFax(dailyAudit, faxNo, user,remarkToHotel);	
        	} catch (Exception e) {
            	log.error("手工发送日审传真出异常,渠道ID:" + channelId + "\n" + e);
            	            	
                // 发送日志
            	auditOrderService.noteLog(false, channelId, 
            			dailyAudit.getAuditdate(),
                    faxNo, user, dailyAudit.getChannelname(),remarkToHotel);
            }
        } else {
        	dailyFaxItem.setSendState(1);
        	auditOrderService.saveOrUpdateFaxItem(dailyFaxItem);
            return 3;
        }

        if (null != ret) {
        	dailyAudit.setReturnid("" + ret);
        	dailyAudit.setSendsucceed(dailyAudit.getSendsucceed() + 1);
            dailyFaxItem.setSendState(0);
            auditOrderService.saveOrUpdateDailyAudit(dailyAudit);
            auditOrderService.saveOrUpdateFaxItem(dailyFaxItem);
            auditOrderService.noteLog(true, dailyAudit.getChannelid(), 
            		dailyAudit.getAuditdate(),
                faxNo, user, dailyAudit.getChannelname(),remarkToHotel);
            bResult = true;
        } else {
        	dailyAudit.setSendfailure(dailyAudit.getSendfailure() + 1);
        	dailyFaxItem.setSendState(1);
        	auditOrderService.saveOrUpdateDailyAudit(dailyAudit);
        	auditOrderService.saveOrUpdateFaxItem(dailyFaxItem);
        	auditOrderService.noteLog(false, dailyAudit.getChannelid(), 
        			dailyAudit.getAuditdate(),
                faxNo, user, dailyAudit.getChannelname(),remarkToHotel);
        }                

        if (bResult) {
            return 1;
        } else {
            return 2;
        }
    }
    
    /**
     * 发送传真次数--显示
     * 
     * @return
     */
    public String sendFaxNum() {
        List sendFaxNum = auditOrderService.getPaperFaxItem(sendAuditId);
        request.setAttribute("sendFaxNum", sendFaxNum);
        return "send_Audit_FaxTime";
    }
    /**
     * 在审核页面点发送日审传真
     * @return
     */
    public String preSendAuditFax(){
    	DaDailyaudit dailyAudit = auditOrderService.getDaDailyAudit(sendAuditId);
    	String faxNo = auditOrderService.getFaxNoByChannelId(
        		dailyAudit.getChannelid(), new Date());
    	request.setAttribute("faxPhone",faxNo);
    	//request.setAttribute("faxPhone", super.getParams().get("faxPhone"));
    	return "preSendAuditFax";
    }
    /**
     * 发送日审传真预览
     * 
     * @return
     */
    public String previewAuditFax() {        
    	DaDailyaudit dailyAudit = auditOrderService.getDaDailyAudit(sendAuditId);
    	if(null == dailyAudit) {
    		return super.forwardError("没有该日审记录");
    	}
    	String chanName = dailyAudit.getChannelname();
    	if(null != chanName && "锦江集团".equals(chanName)) {
    		request.setAttribute("forJj", 1);
    	}    	
        List itemList = auditOrderService.getItemsForAuditFaxCheckin(dailyAudit);
        if(null == itemList) {
        	itemList = new ArrayList();
        }
        int nSize = itemList.size();
        for (int i = 0; i < nSize; i++) {
            Object[] itemVal = (Object[]) itemList.get(i);            
            String itemFellowName = (String) itemVal[6];
            if (!StringUtil.isValidStr(itemFellowName)) {
            	itemVal[6] = (String) itemVal[2];
            }
        }        
        request.setAttribute("itemListIn", itemList);        
        itemList = auditOrderService.getItemsForAuditFaxCheckout(dailyAudit);
        if(null == itemList) {
        	itemList = new ArrayList();
        }
        nSize = itemList.size();
        for (int i = 0; i < nSize; i++) {
            Object[] itemVal = (Object[]) itemList.get(i);            
            String itemFellowName = (String) itemVal[6];
            if (!StringUtil.isValidStr(itemFellowName)) {
            	itemVal[6] = (String) itemVal[2];
            }
        }        
        request.setAttribute("itemListOut", itemList);        

        Date now = new Date();        
        String faxNo = auditOrderService.getFaxNoByChannelId(
        		dailyAudit.getChannelid(), now);
        if (StringUtil.isValidStr(faxNo)) {
            Date toNight = dailyAudit.getAuditdate();
            Date retTime = DateUtil.getDate(toNight, +1);
            request.setAttribute("checkoutNight", retTime);
            retTime = DateUtil.getDateByHour(retTime, 22);
            request.setAttribute("channelName", dailyAudit.getChannelname());
            request.setAttribute("toNight", toNight);            
            request.setAttribute("hotelFax", faxNo);
            request.setAttribute("retTime", retTime);
            request.setAttribute("remarkToHotel", super.getParams().get("remarkToHotel"));//加上酒店备注
            return "paperSendView";
        } else {
            return super.forwardMsgBox("非纸质日审酒店或没设置当前时刻的日审传真号码", "refreshSelf()");
        }

    }
    
    public String sendHotelFax() {
        // 记录有中台权限的人员的登陆时间
        super.markOrgMidLoginTime();        
        return "audit_send_hotel_list";
    }    
    
    /**
     * 左边菜单链接: 手工发送日审
     * 
     * @return
     */
    public String workSendAudit() {
        // 记录有中台权限的人员的登陆时间
        super.markOrgMidLoginTime();
        if(null != roleUser && !roleUser.isOrgAuditAdmin()) {
        	return forwardError("对不起，您没有日审管理员权限！");
        }
        return "work_send_audit";
    }
    
    public String setAuditInfo() {
        // 记录有中台权限的人员的登陆时间
        super.markOrgMidLoginTime();
        return "set_audit_info";
    }            
    
	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public List getNewChannel() {
		return newChannel;
	}

	public void setNewChannel(List newChannel) {
		this.newChannel = newChannel;
	}

	public List getEditChannel() {
		return editChannel;
	}

	public void setEditChannel(List editChannel) {
		this.editChannel = editChannel;
	}

	public IAuditOrderService getAuditOrderService() {
		return auditOrderService;
	}

	public void setAuditOrderService(IAuditOrderService auditOrderService) {
		this.auditOrderService = auditOrderService;
	}

	public HraManager getHraManager() {
		return hraManager;
	}

	public void setHraManager(HraManager hraManager) {
		this.hraManager = hraManager;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public List getAuditLogList() {
		return auditLogList;
	}

	public void setAuditLogList(List auditLogList) {
		this.auditLogList = auditLogList;
	}

	public long getSendAuditId() {
		return sendAuditId;
	}

	public void setSendAuditId(long sendAuditId) {
		this.sendAuditId = sendAuditId;
	}

	public String getSelIDs() {
		return selIDs;
	}

	public void setSelIDs(String selIDs) {
		this.selIDs = selIDs;
	}

}
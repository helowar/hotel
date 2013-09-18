package com.mangocity.hotel.order.service.assistant;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.mangocity.util.DateUtil;


/**
 * 酒店渠道详情
 * 用于auditDetail.jsp的显示
 * 
 * @author chenjuesu
 * 
 */
public class HtlChannelDetailInfo implements Serializable {

    /**
	 * 渠道Id
	 */
    private Long channelId;
    /**
	 * 渠道名称
	 */
    private String channelName;
    /**
	 * 审核号码
	 */
    private String auditNo;
    /**
	 * 审核方式
	 */
    private int auditType;
    /**
	 * 联系号码
	 */
    private String auditCtPhone;
    /**
	 * 回传ID
	 */
    private String returnId;
    /**
	 * 回传URL
	 */
    private String returnURL;
    /**
	 * 审核时间
	 */
    private Date auditDate;
    /**
	 * 审核注意事项
	 */
    private String auditRemark;
    /**
	 * 审核部门
	 */
    private String auditApartM;
    
    public String getChannelName() {
        return channelName;
    }
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
    public String getAuditNo() {
        return auditNo;
    }
    public void setAuditNo(String auditNo) {
        this.auditNo = auditNo;
    }
    public int getAuditType() {
        return auditType;
    }
    public void setAuditType(int auditType) {
        this.auditType = auditType;
    }
    public String getAuditCtPhone() {
        return auditCtPhone;
    }
    public void setAuditCtPhone(String auditCtPhone) {
        this.auditCtPhone = auditCtPhone;
    }
    public String getReturnId() {
        return returnId;
    }
    public void setReturnId(String returnId) {
        this.returnId = returnId;
    }
    public Date getAuditDate() {
        return DateUtil.getDate(auditDate, 1);
    }
    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }
    public String getAuditRemark() {
        return auditRemark;
    }
    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }
    public String getAuditApartM() {
        return auditApartM;
    }
    public void setAuditApartM(String auditApartM) {
        this.auditApartM = auditApartM;
    }
    public Long getChannelId() {
        return channelId;
    }
    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }
	public String getReturnURL() {
		return returnURL;
	}
	public void setReturnURL(String returnURL) {
		this.returnURL = returnURL;
	}
    
}

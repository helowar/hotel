package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.hotel.base.persistence.CEntity;

/**
 * 审核信息
 * @author chenkeming
 *
 */
public class HtlAuditInfoSetup  extends CEntity {
    
    private Long setupId;     

    /**
	 * 指定星期
	 */
    private String weeks;

    /**
	 * 开始日期
	 */
    private Date auditBeginDate;

    /**
	 * 结束日期
	 */
    private Date auditEndDate;

    /**
	 * 开始时间
	 */
    private String auditBeginTime;

    /**
	 * 结束时间
	 */
    private String auditEndTime;

    /**
	 * 审核方式
	 */
    private int auditType;

    /**
	 * 审核号码
	 */
    private String auditNo;

    /**
	 * 联系人
	 */
    private String auditCtName;

    /**
	 * 联系号码
	 */
    private String auditCtPhone;

    /**
	 * 备注
	 */
    private String auditRemark;
    
    /**
	 * 部门
	 */
    private String auditApartM;    

    private HtlAuditInfo htlAuditInfo;

    public String getAuditApartM() {
        return auditApartM;
    }

    public void setAuditApartM(String auditApartM) {
        this.auditApartM = auditApartM;
    }

    public Date getAuditBeginDate() {
        return auditBeginDate;
    }

    public void setAuditBeginDate(Date auditBeginDate) {
        this.auditBeginDate = auditBeginDate;
    }

    public String getAuditCtName() {
        return auditCtName;
    }

    public void setAuditCtName(String auditCtName) {
        this.auditCtName = auditCtName;
    }

    public String getAuditCtPhone() {
        return auditCtPhone;
    }

    public void setAuditCtPhone(String auditCtPhone) {
        this.auditCtPhone = auditCtPhone;
    }

    public String getAuditNo() {
        return auditNo;
    }

    public void setAuditNo(String auditNo) {
        this.auditNo = auditNo;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public int getAuditType() {
        return auditType;
    }

    public void setAuditType(int auditType) {
        this.auditType = auditType;
    }

    public HtlAuditInfo getHtlAuditInfo() {
        return htlAuditInfo;
    }

    public void setHtlAuditInfo(HtlAuditInfo htlAuditInfo) {
        this.htlAuditInfo = htlAuditInfo;
    }

    public String getWeeks() {
        return weeks;
    }

    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }

    public String getAuditBeginTime() {
        return auditBeginTime;
    }

    public void setAuditBeginTime(String auditBeginTime) {
        this.auditBeginTime = auditBeginTime;
    }

    public Date getAuditEndDate() {
        return auditEndDate;
    }

    public void setAuditEndDate(Date auditEndDate) {
        this.auditEndDate = auditEndDate;
    }

    public String getAuditEndTime() {
        return auditEndTime;
    }

    public void setAuditEndTime(String auditEndTime) {
        this.auditEndTime = auditEndTime;
    }

    public Long getSetupId() {
        return setupId;
    }

    public void setSetupId(Long setupId) {
        this.setupId = setupId;
    }

    


}

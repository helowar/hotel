package com.mangocity.hotel.base.persistence;

import java.util.Date;

/**
 * 酒店，合同，房态，配额等加解锁操作 实体
 * @author chenjiajie
 *
 */
public class OrLockedRecords  implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5184064890412026935L;
    
    private Long recordId;
    
    private String recordCD;
    
    /**
	 * 01：酒店锁
	 * 02：房态锁
	 * 03：合同锁
	 * 04：配额锁
	 */
    private Integer lockType;
    
    private String lockerName;
    
    private String lockerLoginName;
        
    /**
	 * 被锁时间
	 */
    private Date lockTime;
    
    /**
	 * 酒店中文名
	 */
    private String remark = "";

    /**
	 * 开始日期(查询用)
	 */
    private Date beginDate;
    
    /**
	 * 结束日期(查询用)
	 */
    private Date endDate;
    
    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getRecordCD() {
        return recordCD;
    }

    public void setRecordCD(String recordCD) {
        this.recordCD = recordCD;
    }

    /**
	 * 01：酒店锁
	 * 02：房态锁
	 * 03：合同锁
	 * 04：配额锁
	 * @return
	 */
    public Integer getLockType() {
        return lockType;
    }

    /**
	 * 
	 * @param type
	 * 01：酒店锁
	 * 02：房态锁
	 * 03：合同锁
	 * 04：配额锁
	 */
    public void setLockType(Integer lockType) {
        this.lockType = lockType;
    }

    public String getLockerName() {
        return lockerName;
    }

    public void setLockerName(String lockerName) {
        this.lockerName = lockerName;
    }

    public String getLockerLoginName() {
        return lockerLoginName;
    }

    public void setLockerLoginName(String lockerLoginName) {
        this.lockerLoginName = lockerLoginName;
    }

    public Date getLockTime() {
        return lockTime;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}

package com.mangocity.hotel.base.persistence;

import java.util.Date;
import com.mangocity.util.Entity;

/**
 * HtlQuotaJudgeException generated by MyEclipse Persistence Tools 配额调整异常
 */

public class HtlQuotaJudgeException implements Entity {

    // Fields

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
     * 配额调整异常ID
     */
    private Long ID;

    /**
     * 配额调整ID
     */
    private Long htlQuotaJudgeId;

    /**
     * 酒店配额过期ID
     */
    private Long htlQuotaCutoffdayId;

    /**
     * 调整日期
     */
    private Date judgeDate;

    /**
     * 调整数量
     */
    private Long judgeNum;

    /**
     * 原来的数量
     */
    private Long originNum;

    /**
     * 备注
     */
    private String remark;

    // Constructors

    /** default constructor */
    public HtlQuotaJudgeException() {
    }

    /** minimal constructor */
    public HtlQuotaJudgeException(Long htlQuotaJudgeExceptionId) {
        this.ID = htlQuotaJudgeExceptionId;
    }

    /** full constructor */
    public HtlQuotaJudgeException(Long htlQuotaJudgeExceptionId, Long htlQuotaJudgeId,
        Long htlQuotaCutoffdayId, Date judgeDate, Long judgeNum, Long originNum, String remark) {
        this.ID = htlQuotaJudgeExceptionId;
        this.htlQuotaJudgeId = htlQuotaJudgeId;
        this.htlQuotaCutoffdayId = htlQuotaCutoffdayId;
        this.judgeDate = judgeDate;
        this.judgeNum = judgeNum;
        this.originNum = originNum;
        this.remark = remark;
    }

    // Property accessors

    public Long getHtlQuotaJudgeId() {
        return this.htlQuotaJudgeId;
    }

    public void setHtlQuotaJudgeId(Long htlQuotaJudgeId) {
        this.htlQuotaJudgeId = htlQuotaJudgeId;
    }

    public Long getHtlQuotaCutoffdayId() {
        return this.htlQuotaCutoffdayId;
    }

    public void setHtlQuotaCutoffdayId(Long htlQuotaCutoffdayId) {
        this.htlQuotaCutoffdayId = htlQuotaCutoffdayId;
    }

    public Date getJudgeDate() {
        return this.judgeDate;
    }

    public void setJudgeDate(Date judgeDate) {
        this.judgeDate = judgeDate;
    }

    public Long getJudgeNum() {
        return this.judgeNum;
    }

    public void setJudgeNum(Long judgeNum) {
        this.judgeNum = judgeNum;
    }

    public Long getOriginNum() {
        return this.originNum;
    }

    public void setOriginNum(Long originNum) {
        this.originNum = originNum;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

}
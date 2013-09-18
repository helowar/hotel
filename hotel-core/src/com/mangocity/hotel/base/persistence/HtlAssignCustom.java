package com.mangocity.hotel.base.persistence;

import com.mangocity.util.Entity;

/**
 * HtlAssignCustom generated by MyEclipse - Hibernate Tools
 */

public class HtlAssignCustom extends CEntity implements Entity {

    // Fields

    // 分配会员id
    private Long ID;

    // 配额id
    private long quotaId;

    // 会员类型
    private int memberType;

    // 销售额度上限
    private int maxAbleQuota;

    // 独占配额
    private int privateQuota;

    // 已经销售数量
    private int saledQuota;

    // Constructors

    /** default constructor */
    public HtlAssignCustom() {
    }

    // Property accessors

    public int getMaxAbleQuota() {
        return maxAbleQuota;
    }

    public void setMaxAbleQuota(int maxAbleQuota) {
        this.maxAbleQuota = maxAbleQuota;
    }

    public int getMemberType() {
        return memberType;
    }

    public void setMemberType(int memberType) {
        this.memberType = memberType;
    }

    public int getPrivateQuota() {
        return privateQuota;
    }

    public void setPrivateQuota(int privateQuota) {
        this.privateQuota = privateQuota;
    }

    public long getQuotaId() {
        return quotaId;
    }

    public void setQuotaId(long quotaId) {
        this.quotaId = quotaId;
    }

    public int getSaledQuota() {
        return saledQuota;
    }

    public void setSaledQuota(int saledQuota) {
        this.saledQuota = saledQuota;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

}
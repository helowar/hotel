package com.mangocity.hotel.base.persistence;

import com.mangocity.util.Entity;

/**
 * 这个对象是批次释放时，对会员类型的配额使用限定，指定会员(CC,TMC,TP 等)最多可以使用多少配额，独占有多少
 * 
 * @author xiaowumi
 * 
 */
public class HtlBatchAssign extends CEntity implements Entity {

    // BatchAssign ID
    private Long ID;

    // 批次释放id
    private long freeOperateId;

    // 释放操作对象
    private HtlFreeOperate freeOperate;

    // 会员类型
    private int memberType;

    // 面付销售额度上限
    private int maxAbleQuota;

    // 面付独占配额
    private int privateQuota;

    // 预付销售额度上限
    private int preMaxAbleQuota;

    // 预付独占配额
    private int prePrivateQuota;

    public long getFreeOperateId() {
        return freeOperateId;
    }

    public void setFreeOperateId(long freeOperateId) {
        this.freeOperateId = freeOperateId;
    }

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

    public void setID(Long id) {
        ID = id;
    }

    public Long getID() {
        return ID;
    }

    public HtlFreeOperate getFreeOperate() {
        return freeOperate;
    }

    public void setFreeOperate(HtlFreeOperate freeOperate) {
        this.freeOperate = freeOperate;
    }

    public int getPreMaxAbleQuota() {
        return preMaxAbleQuota;
    }

    public void setPreMaxAbleQuota(int preMaxAbleQuota) {
        this.preMaxAbleQuota = preMaxAbleQuota;
    }

    public int getPrePrivateQuota() {
        return prePrivateQuota;
    }

    public void setPrePrivateQuota(int prePrivateQuota) {
        this.prePrivateQuota = prePrivateQuota;
    }

}

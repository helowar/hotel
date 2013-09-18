package com.mangocity.hotel.base.service.assistant;

import java.io.Serializable;

/**
 */
public class RoomOutSideQuotaPo implements Serializable {

    // 呼出配额数
    private int outSideQuota;

    // 已退呼出配额数
    private int cancelOutSideQuota;

    // 可卖配额数
    private int ableQuota;

    public int getOutSideQuota() {
        return outSideQuota;
    }

    public void setOutSideQuota(int outSideQuota) {
        this.outSideQuota = outSideQuota;
    }

    public int getCancelOutSideQuota() {
        return cancelOutSideQuota;
    }

    public void setCancelOutSideQuota(int cancelOutSideQuota) {
        this.cancelOutSideQuota = cancelOutSideQuota;
    }

    public int getAbleQuota() {
        return ableQuota;
    }

    public void setAbleQuota(int ableQuota) {
        this.ableQuota = ableQuota;
    }

}

package com.mangocity.hotel.base.dao;

import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.service.assistant.RoomOutSideQuotaPo;

/**
 */
public interface IQuotaForCCDao {
    /*
     * 更新CUTOFFDAY表 cutoffDayId,配额数量 quotaQty,已用数量 cutoffUsedQty
     */

    public int updateHtlCutoffDayQuotaForCC(String id, Map<String, Object> params);

    /*
     * 更新会员配额分配表 分配id assignCustomId 独占配额 privateQuota 已经销售数量saledQuota
     */
    public int updateHtlAssignCustomForCC(String id, Map<String, Object> params);

    /*
     * 更新配额表
     */
    public int updateHtlQuotaForCC(String id, Map<String, Object> params);

    /**
     * 查询房间表和配额表得到呼出配额，已退呼出配额，和可卖配额数
     */
    public RoomOutSideQuotaPo queryHtlRoomForCC(long roomId);

    /**
     * 修改房间表记录呼出配额
     */
    public int updateHtlRoomForCC(Map<String, Object> params);

    /**
     * 查询房间表查找呼出配额数
     */
    public int queryHtlRoomOutQuota(Map<String, Object> params);

    /**
     * 修改房间表记录已退呼出配额
     */
    public int upCancelQTHtlRoomForCC(Map<String, Object> params);

    /**
     * 查询房间表查找已退呼出配额数
     */
    public int qrCancelQTHtlRoom(Map<String, Object> params);

    /**
     * 修改房间表的临时配额数
     */
    public int updateHtlRoomTempQuota(Map<String, Object> params);

    /**
     * 修改房间表的呼出配额数
     */
    public int updateHtlRoomOutsideQuota(Map<String, Object> params);

    /**
     * 修改房间表的配额数
     */
    public int updateHtlRoomQuota(Map<String, Object> params);

    public List queryCreditAssure(String id, Map<String, Object> params);
}

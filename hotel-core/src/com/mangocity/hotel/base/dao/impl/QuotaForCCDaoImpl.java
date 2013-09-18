package com.mangocity.hotel.base.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.dao.IQuotaForCCDao;
import com.mangocity.hotel.base.service.assistant.RoomOutSideQuotaPo;
import com.mangocity.util.dao.DAOIbatisImpl;

/**
 */
public class QuotaForCCDaoImpl extends DAOIbatisImpl implements IQuotaForCCDao {

    public int updateHtlAssignCustomForCC(String id, Map<String, Object> params) {
        return super.update(id, params);
    }

    public int updateHtlCutoffDayQuotaForCC(String id, Map<String, Object> params) {
        return super.update(id, params);
    }

    public int updateHtlQuotaForCC(String id, Map<String, Object> params) {
        return super.update(id, params);
    }

    public RoomOutSideQuotaPo queryHtlRoomForCC(long roomId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("roomId", roomId);
        return (RoomOutSideQuotaPo) super.queryForObject("qryRoomOutSideQuotaPo", params);
    }

    public int queryHtlRoomOutQuota(Map<String, Object> params) {

        Integer count = (Integer) super.queryForObject("qryOutSideQuota", params);

        return count.intValue();
    }

    public int updateHtlRoomForCC(Map<String, Object> params) {

        return super.update("updateHtlRoomForCC", params);
    }

    public int qrCancelQTHtlRoom(Map<String, Object> params) {
        Integer count = (Integer) super.queryForObject("qryCancleOutSideQuota", params);

        return count.intValue();
    }

    public int upCancelQTHtlRoomForCC(Map<String, Object> params) {

        return super.update("upCancleQTHtlRoomForCC", params);
    }

    public int updateHtlRoomTempQuota(Map<String, Object> params) {
        return super.update("updateHtlRoomTempQuota", params);
    }

    public int updateHtlRoomOutsideQuota(Map<String, Object> params) {
        return super.update("updateHtlRoomOutsideQuota", params);
    }

    public int updateHtlRoomQuota(Map<String, Object> params) {

        return super.update("updateHtlRoomQuota", params);
    }

    public List queryCreditAssure(String id, Map<String, Object> params) {

        return super.queryForList(id, params);
    }
}

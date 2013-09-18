package com.mangocity.hotel.base.manage.impl;

import java.util.Date;
import java.util.List;
import com.mangocity.hotel.base.dao.HtlQuotaDao;
import com.mangocity.hotel.base.manage.QuotaPriceManageRefactor;
import com.mangocity.hotel.base.persistence.HtlAssignCustom;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlCutoffDayQuota;
import com.mangocity.hotel.base.persistence.HtlFreeOperate;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.persistence.HtlQuota;
import com.mangocity.hotel.base.persistence.HtlQuotabatch;
import com.mangocity.hotel.base.persistence.HtlRoom;
import com.mangocity.hotel.base.persistence.HtlTempQuota;
import com.mangocity.hotel.base.persistence.QuotaForCC;
import com.mangocity.hotel.base.service.assistant.QuotaQuery;

/**
 * 管理配额,价格信息
 */
public class QuotaPriceManageImplRefactor implements QuotaPriceManageRefactor {
    
    private HtlQuotaDao htlQuotaDao;
    
    public int generateEveryDayRoom(long purchaseBatchId) {
        return htlQuotaDao.generateEveryDayRoom(purchaseBatchId);
    }

    public long saleBatchFreeQuota(HtlFreeOperate batchFreeQuota) {
        return htlQuotaDao.saleBatchFreeQuota(batchFreeQuota);
    }

    public void calcCutoffDayQuota() {
    	htlQuotaDao.calcCutoffDayQuota();
    }

    public HtlQuota calcQuota(HtlQuota quota) {
        return htlQuotaDao.calcQuota(quota);
    }

    public HtlQuota freeAloneQuota(HtlQuota htlQuota) {
        List<HtlAssignCustom> htlAssignCustoms = htlQuota.getLstAssign();
        int privateQuota = 0; //独占配额
        //这里使用for each代替传统循环
        for (HtlAssignCustom htlAssignCustom:htlAssignCustoms) {
            privateQuota += htlAssignCustom.getPrivateQuota();
        }
        htlQuota.setPrivateQty(privateQuota);
        htlQuota.setAvailQty(htlQuota.getFreeQty() - htlQuota.getUsedQty()); // 未使用数量 = 已释放 - 已用
        htlQuota.setShareQty(htlQuota.getAvailQty() - privateQuota); // 共享 = 未使用 - 独占
        return htlQuota;
    }

    public int freeAloneQuotaByDays(List lstQuota) {
        return htlQuotaDao.freeAloneQuotaByDays(lstQuota);
    }

    public QuotaForCC queryQuotaCharter(QuotaQuery quotaQuery, Date queryDate, int para) {
        return htlQuotaDao.queryQuotaCharter(quotaQuery, queryDate, para);
    }

    public void updateQuotaForCC(QuotaForCC quotaForCC) {
    	htlQuotaDao.updateQuotaForCC(quotaForCC);
    }

    public HtlCutoffDayQuota findHtlCutoffDayQuota(long cutoofDayId) {
        return htlQuotaDao.findHtlCutoffDayQuota(cutoofDayId);
    }

    public HtlAssignCustom findHtlAssignCustom(long memberType, long quotaId) {
      return htlQuotaDao.findHtlAssignCustom(memberType, quotaId);
    }

    public long UpdateQuota(HtlQuota htlQuota) {
        return htlQuotaDao.update(htlQuota);
    }

    public void adjustQuota(String[] deleteQuotaIds, List<HtlQuota> lstNewQuota) {
    	//if (null != deleteQuotaIds) {            
            for(String id:deleteQuotaIds){
            	if("0".equals(id)) htlQuotaDao.deleteQuota(Long.valueOf(id));
            }
        //}
    	
       // if (null != lstNewQuota) {
            for(HtlQuota htlQuota : lstNewQuota){
            	htlQuotaDao.saveOrUpdate(htlQuota);
            }
        //}
    }

    public HtlRoom qryHtlRoomForCC(long roomTypeID, Date date) {
    	return htlQuotaDao.qryHtlRoomForCC(roomTypeID, date);
    }

    public HtlPrice qryHtlPriceForCC(long childRoomId, Date date, 
        String payMethod, String quotaType) {
    	return htlQuotaDao.qryHtlPriceForCC(childRoomId, date, payMethod, quotaType);
    }

    public HtlQuota qryHtlQuotaForCC(long roomId, String quotaType, String shareType) {
        return htlQuotaDao.qryHtlQuotaForCC(roomId, quotaType, shareType);
    }

    public HtlContract qryHtlcontractForCC(long hotelId, Date beginDate) {
        return htlQuotaDao.qryHtlcontractForCC(hotelId, beginDate);
    }

    public HtlTempQuota qryHtlTempQuotaForcc(long roomid, String bedid) {
        return htlQuotaDao.qryHtlTempQuotaForcc(roomid, bedid);
    }

    public void updateHtlTempQuotaForcc(HtlTempQuota htlTempQuota) {
    	htlQuotaDao.updateHtlTempQuotaForcc(htlTempQuota);
    }

    public HtlAssignCustom findHtlAssignCustom(long assignCustomId) {

        return htlQuotaDao.findHtlAssignCustom(assignCustomId);
    }

    public HtlQuota findHtlQuota(long quotaId) {
        return htlQuotaDao.findHtlQuota(quotaId);
    }

    public int generateEveryDayRoom(Long qbId){
    	return htlQuotaDao.generateEveryDayRoom(qbId);
    }

    public HtlQuotabatch queryHtlQuotabatch(long quotaBatchID) { 
        return htlQuotaDao.queryHtlQuotabatch(quotaBatchID);
    }
    
//这个应该通过HtlRoomtype对应的service来调用
//    public HtlRoomtype queryHtlRoomType(long roomTypeID) {
//        return (HtlRoomtype) super.find(HtlRoomtype.class, roomTypeID);
//    }


//这个应该通过合同contract对应的service来调用
//    public List queryContrCuf(long hotelId, long contractId) {
//        List queryResult = super.queryByNamedQuery("queryContrCuf", new Object[] { hotelId,
//            contractId });
//        return queryResult;
//    }

}

package com.mangocity.hotel.base.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.manage.QuotaPriceManage;
import com.mangocity.hotel.base.persistence.HtlCutoffDayQuota;
import com.mangocity.hotel.base.persistence.HtlQuota;
import com.mangocity.hotel.base.persistence.HtlRoom;
import com.mangocity.hotel.base.service.HotelRoomService;
import com.mangocity.hotel.base.util.BizRuleCheck;
import com.mangocity.hotel.base.web.webwork.GenericAction;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.bean.TempCutoffDay;

/**
 */
public class AdjustQuotaAction extends GenericAction {

    /**
     * 批次id
     */
    private long quotaBatchId;

    /**
     * 配额类型
     */
    private String quotaType;

    /**
     * 房间ID
     */
    private long roomId;

    /**
     * 共享方式
     */
    private String shareType;

    /**
     * 配额模式
     */
    private String quotaPattern;

    /**
     * 酒店ID
     */
    private long hotelId;

    /**
     * 合同ID
     */
    private long contractId;

    /**
     * 可销日期
     */
    private Date ableSaleDate;

    /**
     * cutoffDay行数
     */
    private int cutoffDayNum;

    /**
     * 配额是否可回收
     */
    private boolean takebackQuota;

    private QuotaPriceManage quotaPriceManage;
    
    private HotelRoomService hotelRoomService;

    public Date getAbleSaleDate() {
        return ableSaleDate;
    }

    public void setAbleSaleDate(Date ableSaleDate) {
        this.ableSaleDate = ableSaleDate;
    }

    public long getContractId() {
        return contractId;
    }

    public void setContractId(long contractId) {
        this.contractId = contractId;
    }

    public int getCutoffDayNum() {
        return cutoffDayNum;
    }

    public void setCutoffDayNum(int cutoffDayNum) {
        this.cutoffDayNum = cutoffDayNum;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public long getQuotaBatchId() {
        return quotaBatchId;
    }

    public void setQuotaBatchId(long quotaBatchId) {
        this.quotaBatchId = quotaBatchId;
    }

    public String getQuotaPattern() {
        return quotaPattern;
    }

    public void setQuotaPattern(String quotaPattern) {
        this.quotaPattern = quotaPattern;
    }

    public QuotaPriceManage getQuotaPriceManage() {
        return quotaPriceManage;
    }

    public void setQuotaPriceManage(QuotaPriceManage quotaPriceManage) {
        this.quotaPriceManage = quotaPriceManage;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    private HtlQuota setQuotaValue(HtlQuota qt) {
        HtlRoom room = hotelRoomService.getHtlRoomByRoomId(roomId);
        qt.setHotelId(hotelId);
        qt.setContractId(contractId);
        qt.setQuotaBatchId(quotaBatchId);
        qt.setQuotaPattern(quotaPattern);
        qt.setRoom(room);
        qt.setQuotaType(quotaType);
        qt.setAbleSaleDate(ableSaleDate);
        qt.setWeek(DateUtil.getWeekOfDate(ableSaleDate));
        qt.setTakebackQuota(takebackQuota);
        return qt;
    }

    public String saveQuota() {
        Map params = super.getParams();
        List lstCutoffDay = MyBeanUtil.getBatchObjectFromParam(params, TempCutoffDay.class, cutoffDayNum);

        List<HtlQuota> lstNewQuota = new ArrayList<HtlQuota>(1);

        // 面付配额 或共享配额
        if (StringUtil.isValidStr(shareType)) {
        	HtlQuota quota = new HtlQuota();
            quota = setQuotaValue(quota);
            if (BizRuleCheck.isQuotaShare(shareType)) {// 共享配额
                quota.setShareType(shareType);
            } else if (BizRuleCheck.isFaceToPayQuota(shareType)) {// 面付配额
            	quota.setShareType(BizRuleCheck.getFaceToPayQuotaValue());
            } else if (BizRuleCheck.isPrepayQuota(shareType)) {// 预付配额
            	quota.setShareType(BizRuleCheck.getPrepayQuotaValue());                
            }
            // 求可用配额的数量
            int ableQty = 0;
            int totalQty = 0;
            TempCutoffDay tcd = null;
            for (int i = 0; i < lstCutoffDay.size(); i++) {
                tcd = (TempCutoffDay) lstCutoffDay.get(i);
                HtlCutoffDayQuota cdq = new HtlCutoffDayQuota();
                cdq.setCutoffDay(tcd.getCutoffDay());
                cdq.setCutoffTime(tcd.getCutoffTime());
                cdq.setQuotaQty(tcd.getQuotaQty());
                totalQty += tcd.getQuotaQty();
                if (BizRuleCheck.checkCutOffDayIsAvail(cdq.getCutoffDay(), cdq.getCutoffTime(), ableSaleDate)) {
                    // 配额
                    ableQty += cdq.getQuotaQty();
                    cdq.setStatus(BizRuleCheck.getAvailStatus());
                } else {
                    cdq.setStatus(BizRuleCheck.getNotAvailStatus());
                }
                cdq.setQuota(quota);
                quota.getLstCutOffDay().add(cdq);                
            }
            quota.setTotalQty(totalQty);
            quota.setAbleQty(ableQty); // 设定配额            
            lstNewQuota.add(quota);
        }

        String[] deleteQuotaIds = (String[]) params.get("quotaId");

        quotaPriceManage.adjustQuota(deleteQuotaIds, lstNewQuota);

        return "saveQuota";
    }

    public boolean isTakebackQuota() {
        return takebackQuota;
    }

    public void setTakebackQuota(boolean takebackQuota) {
        this.takebackQuota = takebackQuota;
    }

	public void setHotelRoomService(HotelRoomService hotelRoomService) {
		this.hotelRoomService = hotelRoomService;
	}
}

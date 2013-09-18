package com.mangocity.hotel.base.manage;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.persistence.HtlAssignCustom;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlCutoffDayQuota;
import com.mangocity.hotel.base.persistence.HtlFreeOperate;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.persistence.HtlQuota;
import com.mangocity.hotel.base.persistence.HtlQuotabatch;
import com.mangocity.hotel.base.persistence.HtlRoom;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.persistence.HtlTempQuota;
import com.mangocity.hotel.base.persistence.QuotaForCC;
import com.mangocity.hotel.base.service.assistant.OperateQuotaPricePara;
import com.mangocity.hotel.base.service.assistant.QuotaQuery;

/**
 */
public interface QuotaPriceManageRefactor {
   


    /**
     * 通过采购批次id，在数据层生成每一天的房间。最好是用storeProcedure. 要检查这个批次一定要配额，cutoffday,及价格。
     * 
     * @param purchaseBatchId
     *            采风批次id
     * @return 返回一个操作结果。
     */
    public int generateEveryDayRoom(long purchaseBatchId);

    /**
     * 批次释放配额
     * 
     * @param batchFreeQuota
     * @return 批次释放配额的id
     */
    public long saleBatchFreeQuota(HtlFreeOperate batchFreeQuota);

    /**
     * 单独按天释放配额
     * 
     * @param lstQuota
     * @return
     */
    public int freeAloneQuotaByDays(List lstQuota);

   
    /**
     * 计算每一天的可用配额 是一个job,未来要用数据库的job来实现，java实现主要是理清思想
     */
    public void calcCutoffDayQuota();

    /**
     * 计算一个配额，经过cut计算的配额
     * 
     * @param quota
     * @return
     */
    public HtlQuota calcQuota(HtlQuota quota);

    /**
     * 传一个配额对象,下面带有一个lstAssignCustom，处理分配对象
     * 
     * @param htlQuota
     * @return
     */
    public HtlQuota freeAloneQuota(HtlQuota htlQuota);

    /*
     * 传入一个配额查询条件对象，返回相应的配额查询对象（包配预付时用）,para参数代表配额类型和付款方式的组合类型， 0－－包配预付 1－－普配预付 2－－普配面付 3－－普配共享
     */
    public QuotaForCC queryQuotaCharter(QuotaQuery quotaQuery, Date queryDate, int para);

    // 更新配额信息对象
    public void updateQuotaForCC(QuotaForCC quotaForCC);

    // 通过CUTOOFDAYID，获取cutoffday对象
    public HtlCutoffDayQuota findHtlCutoffDayQuota(long cutoofDayId);

    // 通过会员id，配额id，获取HtlAssignCustom对象
    public HtlAssignCustom findHtlAssignCustom(long memberType, long quotaId);

    // assignCustomId，获取HtlAssignCustom对象
    public HtlAssignCustom findHtlAssignCustom(long assignCustomId);

    // quotaId，获取HtlQuota对象
    public HtlQuota findHtlQuota(long quotaId);

    // update 配额类
    public long UpdateQuota(HtlQuota htlQuota);

    /**
     * 调整配额共享方式，要删除旧的配额，写新的配额记录进去
     * 
     * @param deleteQuotaIds
     *            要删除配额的ID
     * @param lstNewQuota
     *            要新增配额
     * @return 成功返回0 不成功返回非0值
     */
    public void adjustQuota(String[] deleteQuotaIds, List<HtlQuota> lstNewQuota);

    /**
     * 获取房间状态
     * 
     * @param roomTypeID
     *            ,date
     * @return HtlRoom
     */
    public HtlRoom qryHtlRoomForCC(long roomTypeID, Date date);

    /**
     * 获取房间价格
     * 
     * @param childRoomId
     *            ,date
     * @return HtlPrice
     */
    public HtlPrice qryHtlPriceForCC(long childRoomId, Date date,
        String payMethod, String quotaType);

    /**
     * 查询配额
     * 
     * @param 房间id
     *            room_id ,配额类型 quota_type,配额共享方式 SHARE_TYPE
     */
    public HtlQuota qryHtlQuotaForCC(long roomId, String quotaType, String shareType);

    /**
     * 查询合同
     * 
     * @param 酒店ID
     *            ，日期
     * @return htl_contract
     */
    public HtlContract qryHtlcontractForCC(long hotelId, Date beginDate);

    /**
     * 查找临时配额
     * 
     * @param 房间id
     *            床型id
     */
    public HtlTempQuota qryHtlTempQuotaForcc(long roomid, String bedid);

    /**
     * 修改临时配额
     * 
     * @param HtlTempQuota
     */
    public void updateHtlTempQuotaForcc(HtlTempQuota htlTempQuota);

    
    public int generateEveryDayRoom(Long qbId) throws SQLException;
}

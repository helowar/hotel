package com.mangocity.hotel.base.dao;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.persistence.HtlAssignCustom;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlCutoffDayQuota;
import com.mangocity.hotel.base.persistence.HtlFreeOperate;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.persistence.HtlQuota;
import com.mangocity.hotel.base.persistence.HtlQuotaNew;
import com.mangocity.hotel.base.persistence.HtlQuotabatch;
import com.mangocity.hotel.base.persistence.HtlRoom;
import com.mangocity.hotel.base.persistence.HtlTempQuota;
import com.mangocity.hotel.base.persistence.QuotaForCC;
import com.mangocity.hotel.base.service.assistant.QuotaQuery;
import com.mangocity.util.dao.GenericDAO;

/**
*@author chenhuizhong e-mail:huizong.chen@mangocity.com
*@version 1.0
*@since 1.0
*/
public interface HtlQuotaDao
{
    /**
     * 根据房态查询CC的临时配额
     * @param hotelId
     * @param roomType
     * @param saleDate
     * @param bedId
     * @return HtlQuotaNew
     */
    public List<HtlQuotaNew> queryTempQuotaByRoomState(Long hotelId, Long roomType,
            Date saleDate, String bedId);
    

    /**
     * 配额该造，根据相关查询条件，查询配额总表的相关信息 add by shengwei.zuo 2009-10-19
     * @param hotelId
     * @param roomTypeid
     * @param date
     * @param quotaType
     * @param holder
     * @param bedId
     * @return HtlQuotaNew
     */
    public List<HtlQuotaNew> queryTempQuotaNew(long hotelId, long roomTypeid, Date date,
            String quotaType, String holder, String bedId);
    
    /**
     * 通过采购批次id，在数据层生成每一天的房间。最好是用storeProcedure. 要检查这个批次一定要配额，cutoffday,及价格。
     * @param purchaseBatchId 采购批次ID
     * @return
     */
    int  generateEveryDayRoom(long purchaseBatchId);
    
    /**
     * 批次释放配额
     * 
     * @param batchFreeQuota
     * @return 批次释放配额的id
     */
    public int saleBatchFreeQuota(HtlFreeOperate batchFreeQuota);
    
    /**
     *通过cutoff Day计算，取得有效的配额数量
     */
    public void calcCutoffDayQuota();
    
    /**
     * 计算当前配额记录中一条配额记录的cutoff Day的有效配额
     * 
     * @param quota
     * @return
     */
    public HtlQuota calcQuota(HtlQuota quota);
    
    public int freeAloneQuotaByDays(List lstQuota);
    
    /**
     * 更新
     * @param htlQuota
     * @return
     */
    long update(HtlQuota htlQuota);
    
    /**
     * 删除一个配额，包括子表的记录
     */
    public int deleteQuota(Long id);
    
    void saveOrUpdate(HtlQuota htlQuota);
    
    /**
     * 批量更新Quota对象
     * 
     * @param listHtlQuota	Quota对象的集合
     */
    public void batchSaveOrUpdateQuota(List<HtlQuota> listHtlQuota);
    
    int generateEveryDayRoom(Long qbId);
    
    HtlAssignCustom findHtlAssignCustom(long memberType, long quotaId);
    
    /**
     * 获取房间状态
     * @param roomTypeID
     * @param date
     * @return
     */
    HtlRoom qryHtlRoomForCC(long roomTypeID, Date date);
    
    /**
     * 获取房间价格
     * @param childRoomId
     * @param date
     * @param payMethod
     * @param quotaType
     * @return
     */
    HtlPrice qryHtlPriceForCC(long childRoomId, Date date, 
            String payMethod, String quotaType);
    
    /**
     * 查询配额
     * @param roomId
     * @param quotaType
     * @param shareType
     * @return
     */
    HtlQuota qryHtlQuotaForCC(long roomId, String quotaType, String shareType);
    
    public void updateQuotaForCC(QuotaForCC quotaForCC);
    
    /**
     * 查询合同
     * @param hotelId
     * @param beginDate
     * @return
     */
    HtlContract qryHtlcontractForCC(long hotelId, Date beginDate);
    
    QuotaForCC queryQuotaCharter(QuotaQuery quotaQuery, Date queryDate, int para);
    
    HtlCutoffDayQuota findHtlCutoffDayQuota(long cutoofDayId);
    
    /**
     * 查找临时配额
     * @param roomid
     * @param bedid
     * @return
     */
    HtlTempQuota qryHtlTempQuotaForcc(long roomid, String bedid);
    
    /**
     * 修改临时配额
     * @param htlTempQuota
     */
    void updateHtlTempQuotaForcc(HtlTempQuota htlTempQuota);
    
    /**
     * 查找HtlAssignCustom
     * @param assignCustomId
     * @return
     */
    HtlAssignCustom findHtlAssignCustom(long assignCustomId);
    
    /**
     * 查找HtlQuota
     * @param quotaId
     * @return
     */
    HtlQuota findHtlQuota(long quotaId);
    
    HtlQuotabatch queryHtlQuotabatch(long quotaBatchID);
    
    /**
     * 通过业务主键找到一个房间
     * @param roomTypeId
     * @param hotelId
     * @param ableSaleDate
     * @return
     */
    HtlRoom findRoomByBizKey(Long roomTypeId, Long hotelId, Date ableSaleDate);
    
    List getQuotaByBizKeyWithDate(String quotaType, String shareType, long hotelId,
            long contractId, long roomTypeId, Date beginDate, Date endDate, String[] weeks);
    
    /**
     * 根据batch id和可销售日期范围查询配额，如果指定了工作日，则在查询结果中过滤不在指定工作日内的配额
     * 
     * @param batchQuotaId	batch id
     * @param beginDate		销售日期范围的起始日期
     * @param endDate		销售日期范围的截止日期
     * @param weeks			工作日，如星期一，三，五
     * @return
     */
    public List<HtlQuota> qryQuotaInWeekdaysByBatchIdSaleDate(long batchQuotaId, Date beginDate, 
    		Date endDate, String[] weeks);
    
    /**
     * 查找酒店合同内所有的CUTOFFDAY
     * 
     * @param hotelId
     * @param contractId
     * @return
     */
    public List queryContrCuf(long hotelId, long contractId);
    
    /**
     * 保存或更新HtlQuotabatch实体对象
     * 
     * @param htlQuotabatch	HtlQuotabatch对象
     * @return	HtlQuotabatch对象ID
     */
    public long saveOrUpdateQuotabatch(HtlQuotabatch htlQuotabatch);
    
    /**
     * 根据房型，配额类型查询某个酒店在某个可售时期内的配额
     * 
     * @param hotelId
     * @param roomType
     * @param quotaType
     * @param beginDate
     * @param endDate
     * @return
     */
    public List<HtlQuota> qryQuotaByRoomTypeQuotaType(long hotelId, long roomType, String quotaType,  
    		Date beginDate, Date endDate);
    
    /**
     * 根据房型，配额类型查询某个酒店在某个可售时期的某些工作日内的配额
     * 
     * @param hotelId
     * @param quotaType
     * @param roomType
     * @param beginDate
     * @param endDate
     * @param weeks
     * @return
     */
    public List<HtlQuota> qryQuotaByRoomTypeQuotaTypeWithWeeks(long hotelId, long roomType, String quotaType, 
			Date beginDate, Date endDate, String[] weeks);
    
    /**
     * 根据酒店ID查询某个房间的临时配额
     * 
     * @param hotelId
     * @param roomType
     * @param beginDate
     * @param endDate
     * @return
     */
    public List<HtlTempQuota> qryTempQuotaByHotelIdRoomId(long hotelId, long roomType, Date beginDate, Date endDate);
}
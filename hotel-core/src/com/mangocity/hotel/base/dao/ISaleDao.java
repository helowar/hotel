package com.mangocity.hotel.base.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlArea;
import com.mangocity.hotel.base.persistence.HtlFreeOperate;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.persistence.HtlQuota;
import com.mangocity.hotel.base.persistence.HtlQuotaCutoffDayNew;
import com.mangocity.hotel.base.persistence.HtlQuotaNew;
import com.mangocity.hotel.base.persistence.HtlRoom;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.bean.ContinueDatecomponent;
import com.mangocity.util.dao.DAO;

/**
 */
public interface ISaleDao extends DAO {

    /**
     * 查询配额不带星期的配额通过配额类型，共享方式，酒店id,房型id
     */
    public List lstQuotaWithoutWeek(long roomType, String quotaType, long hotelId, Date beginDate,
        Date endDate);

    /**
     * 查询配额带星期
     * 
     * @param roomType
     *            房型id
     * @param quotaType
     *            配额类型
     * @param hotelId
     *            酒店id
     * @param beginDate
     *            开始日期
     * @param endDate
     *            结束日期
     * @return
     */
    public List lstQuotaWithWeekEX(long roomType, String quotaType, long hotelId, Date beginDate,
        Date endDate, String[] weeks);

    /**
     * 通过业务主键找到一个房间
     */
    public HtlRoom findRoomByBizKey(Long roomTypeId, Long hotelId, Date ableSaleDate);

    /**
     * 通过业务主键找到一个配额对象
     */
    public HtlQuota findQuotaByBizKey(String quotaType, String shareType, Long hotelId,
        Long contractId, Date ableSaleDate, long roomTypeId);

    /**
     * 通过业务主键可销售日期 + 采购批次Id+配额共享类型 找到唯一的配额
     */
    public HtlQuota findQuotaByBatchId(Date ableSaleDate, Long batchId, String shareType);

    /**
     * 通过酒店列出所有房型
     */
    public List lstHotelRoomType(Long hotelId);

    /**
     * 通过房型id,酒店id,起止日期 找出合符条件的房间
     */
    public List lstRooms(Long hotelId, Long roomTypeId, String beginDate, String endDate);

    /**
     * 列出所有酒店
     * 
     * @param status
     * @return
     */
    public List lstAllHotels(String status);

    /**
     * 列出一个酒店所有有效配额
     */
    public List lstAllQuotas(Long hotelId);

    public HtlRoom createRoomIfNotExist(Long roomTypeId, Long hotelId, Date ableSaleDate,
        Long contractId);

    /**
     * 通过采购批次id，在数据层生成每一天的房间。最好是用storeProcedure. 要检查这个批次一定要配额，cutoffday。
     * 
     * @param purchaseBatchId
     *            采购批次id
     * @return 返回一个操作结果。
     */
    public int generateEveryDayRoom(long purchaseBatchId);

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

    /**
     * 通过酒店id,开始日期，结束日期,选择的星期，选择的房型 找出房间列表
     * 
     * @param hotelId
     *            酒店id
     * @param beginDate
     *            开始日期
     * @param endDate
     *            结束日期
     * @param week
     *            选择的星期
     * @param roomTypes
     *            选择的房型
     * @return 房间列表
     */
    public List qryRoomState(long hotelId, Date beginDate, Date endDate, String[] weeks,
        String[] roomTypes);

    /**
     * 当更新预订条款的时候，要更新每一天的担保情况
     * 
     * @param bDate
     *            开始日期
     * @param eDate
     *            结束日期
     * @param roomTypes
     *            房型数组
     * @param hotelId
     *            酒店ID
     * @return 如果操作成功，返回0，如果操作失败，返回一个非0数
     */
    public int updateRoomLastAssureTime(Date bDate, Date eDate, String[] roomTypes, long hotelId,
        String lastAssureTime);

    /**
     * 删除这个合同的配额及价格
     * 
     * @param contractId
     *            合同Id
     * @param bDate
     *            起始日期
     * @param eDate
     *            结束日期
     * @return 操作结束 1成功 0失败
     */
    public int removeQuotaAndPrice(Long contractId, Date bDate, Date eDate);

    /**
     * 检查合同的起止时间是否重复
     * 
     * @param contract
     * @return 如果重复就返回1，否则就返回0
     */
    public int checkContractDate(long pHotelId, Date beginD, Date endD);

    /**
     * 检查区域和城市信息是否存在
     * 
     * @param
     * @return 如果存在就返回1，否则就返回0
     */
    public int checkAreaExist(String areaCode, String cityCode);

    /**
     * 修改合同信息时检查合同的起止时间是否重复
     * 
     * @param contract
     * @return 如果重复就返回1，否则就返回0
     */
    public int checkEditContractDate(long pHotelId, Date beginD, Date endD, Date beginDOld,
        Date endDOld);

    /**
     * 通过房间ID查询临时配额
     * 
     * @param roomId
     * @return
     */
    public List getTempQuotaByRoomId(Long roomId);

    /**
     * 删除一个配额，包括子表的记录
     */
    public int deleteQuota(Long id);

    public int freeAloneQuotaByDays(List lstQuota);

    /**
     * 检查同一酒店房型是否重复
     * 
     * @param 酒店id
     * @return 如果重复就返回1，否则就返回0
     */
    public int checkRoomType(long hotelid, long roomTypeID, String roomName);

    public int checkContinueContractDate(long hotelId, long contractId, Date oldBeginDate,
        Date continueDate);

    public List<ContinueDatecomponent> checkContinuePrice(long hotelId, long contractId,
        List<ContinueDatecomponent> dateComponents);

    /**
     * 检查房型是否有关房历史
     * 
     * @param hotelId
     * @param childRoomTypeId
     * @param beginDate
     * @param EndDate
     * @return
     */
    public List checkCloseHistory(long hotelId, String childRoomTypeId,
        List<ContinueDatecomponent> dateComponents);

    /**
     * 查询呼出配额
     * 
     * @param roomType
     * @param hotelId
     * @param beginDate
     * @param endDate
     * @return
     */
    public List lstOutsideQuota(long roomType, long hotelId, Date beginDate, Date endDate);

    /**
     * 查询临时配额
     * 
     * @param hotelId
     * @param childRoomTypeId
     * @param beginDate
     * @param EndDate
     * @return
     */
    public List lstTempQuota(long roomType, long hotelId, Date beginDate, Date endDate);

    /**
     * 检查区域by cityCode
     * 
     * @param
     * @return HtlArea
     */
    public HtlArea queryAreaCode(String cityCode);
    
    /**
     * 查询临时配额
     * add by haibo.li 临时配额改造,改查询另外的表
     */
    public List queryTempQuota(long hotelId,long roomTypeId,Date date,String quotaType,String holder,String bedId);
    
    /**
     * 配额该造，根据相关查询条件，查询配额总表的相关信息 add by shengwei.zuo 2009-10-19
     */
    public List queryTempQuotaNew(long hotelId,long roomTypeid,Date date,String quotaType,String holder,String bedId);
    
    /**
     * 通过业务主键找到一个价格
     */
    public HtlPrice findPriceBizKey(long hotelId, long roomTypeId, long childRoomTypeId,
        String quotaType, Date ableSaleDate, String payMethod);

	public List<HtlQuotaNew> queryTempQuotaByRoomState(Long id, Long roomType, Date saleDate, String bedId);
	
	public List getHtlOpenClose(Object[] obj);
	
	/**
	 * 根据酒店Id和床型Id查出所有的配额.add by ting.li
	 * @param hotelId
	 * @param roomTypeId
	 * @param checkIdDate
	 * @param checkoutDate
	 * @return
	 */
	public List<HtlQuotaNew> queryQuotaByRoomTypeId(Long hotelId,Long roomTypeId,Date checkinDate,Date checkoutDate);
  
}

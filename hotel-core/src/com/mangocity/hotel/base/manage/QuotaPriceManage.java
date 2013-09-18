package com.mangocity.hotel.base.manage;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlAssignCustom;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlCutoffDayQuota;
import com.mangocity.hotel.base.persistence.HtlFreeOperate;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.persistence.HtlQuota;
import com.mangocity.hotel.base.persistence.HtlQuotabatch;
import com.mangocity.hotel.base.persistence.HtlRoom;
import com.mangocity.hotel.base.persistence.HtlTempQuota;
import com.mangocity.hotel.base.persistence.QuotaForCC;
import com.mangocity.hotel.base.service.assistant.QuotaQuery;
import com.mangocity.hotel.base.util.CodeName;
import com.mangocity.hotel.user.UserWrapper;

/**
 */
public interface QuotaPriceManage {

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
	 * 传入一个配额查询条件对象，返回相应的配额查询对象（包配预付时用）,para参数代表配额类型和付款方式的组合类型， 0－－包配预付 1－－普配预付
	 * 2－－普配面付 3－－普配共享
	 */
	public QuotaForCC queryQuotaCharter(QuotaQuery quotaQuery, Date queryDate,
			int para);

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
	public HtlQuota qryHtlQuotaForCC(long roomId, String quotaType,
			String shareType);

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

	/**
	 * 根据quota batch id获得quotabatch对象
	 * 
	 * @param quotaBatchID
	 * @return
	 */
	public HtlQuotabatch queryHtlQuotabatch(long quotaBatchID);

	/**
	 * 批量更新Quota对象
	 * 
	 * @param listHtlQuota
	 *            Quota对象的集合
	 */
	public void batchSaveOrUpdateQuota(List<HtlQuota> listHtlQuota);

	/**
	 * 根据batch id和可销售日期范围获得配额，如果指定了工作日，则在结果中过滤掉不在指定工作日内的配额
	 * 
	 * @param batchQuotaId
	 *            batch id
	 * @param beginDate
	 *            销售日期范围的起始日期
	 * @param endDate
	 *            销售日期范围的截止日期
	 * @param weeks
	 *            工作日，如星期一，三，五
	 * @return
	 */
	public List<HtlQuota> getQuotaInWeekdaysByBatchIdSaleDate(
			long batchQuotaId, Date beginDate, Date endDate, String[] weeks);

	/**
	 * 查找酒店合同内所有的CUTOFFDAY
	 * 
	 * @param hotelId
	 * @param contractId
	 * @return
	 */
	public List<?> getCutoffDayByHotelIdContractId(long hotelId, long contractId);

	/**
	 * 保存或更新HtlQuotabatch实体对象
	 * 
	 * @param htlQuotabatch
	 *            HtlQuotabatch对象
	 * @return HtlQuotabatch对象ID
	 */
	public long saveOrUpdateQuotabatch(HtlQuotabatch htlQuotabatch);

	/**
	 * 将配额分割为三类存在三个List集合 面付独占 预付独占 共享
	 * 
	 * @param beginDate
	 * @param endDate
	 * @param weeks
	 * @param roomType
	 * @param quotaType
	 * @param hotelId
	 * @return
	 */
	public Map<String, List<?>> buildHtlQuotaForView(Date beginDate,
			Date endDate, String weeks, long roomType, String quotaType,
			long hotelId);

	/**
	 * 根据酒店ID获得某个房间的临时配额
	 * 
	 * @param hotelId
	 * @param roomType
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<HtlTempQuota> getTempQuotaByHotelIdRoomId(long hotelId,
			long roomType, Date beginDate, Date endDate);

	/**
	 * 根据页数 获取当前页30天的日期
	 * 
	 * @return
	 */
	public List<Date> getDateForNextThirtyDays(String weeks, String[] week,
			Date beginDate, Date endDate, int pageTotal, int pageNo);

	/**
	 * 更新cuoffDay以及封装配额List
	 */
	@SuppressWarnings("unchecked")
	public Map updateCutOffDayAndBuildQuotaList(Map params, int rowCutNo,
			boolean sumOutsideQuotsOrNot, long roomType, long hotelId);

	/**
	 * 从Session获取配额集合,获取不到就查
	 * 
	 * @param map
	 * @param pageStr
	 * @param weeks
	 * @param roomType
	 * @param quotaType
	 * @param hotelId
	 * @param midbDate
	 * @param mideDate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getQuotaByMap(Map map, String pageStr, String weeks,
			long roomType, String quotaType, long hotelId, Date midbDate,
			Date mideDate);

	/**
	 * 根据房型，配额类型获得某个酒店在某个可售时期内的配额
	 * 
	 * @param hotelId
	 * @param roomType
	 * @param quotaType
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<HtlQuota> getQuotaByRoomTypeQuotaType(long hotelId,
			long roomType, String quotaType, Date beginDate, Date endDate);

	/**
	 * 查询配额集合
	 * 
	 * @param week
	 * @param roomType
	 * @param quotaType
	 * @param hotelId
	 * @param midbDate
	 * @param mideDate
	 * @return
	 */
	public List<HtlQuota> getQuotaByMap(String[] week, long roomType,
			String quotaType, long hotelId, Date
			midbDate, Date mideDate);

	/**
	 * 查询配额是否锁定
	 * 
	 * @param hotelId
	 * @param dao
	 * @param roleUser
	 * @param hotel
	 * @return
	 */
	public String queryQuotaIsLocked(long hotelId, UserWrapper roleUser,
			HtlHotel hotel);

	/**
	 * 
	 * @param quota
	 * @param lstMemberQuotaObj
	 * @param mapAssignCustom
	 * @param lstAssignCustom
	 * @return
	 */
	public List<HtlQuota> getQuotaList(HtlQuota quota, List<CodeName>

	lstMemberQuotaObj, HashMap<String, HtlAssignCustom> mapAssignCustom,
			List<Object> lstAssignCustom, String flag);

	/**
	 * 更新配额，并有需要时释放配额
	 * 
	 * @param params
	 * @param rowCutNo
	 * @param hotelId
	 * @param updataOrView
	 * @return
	 */
	public Map updateQuotaAndFreeQuota(Map params, int rowCutNo, long hotelId,
			String updataOrView);

}

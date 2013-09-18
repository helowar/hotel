package com.mangocity.hotel.base.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlQuotaCutoffDayNew;
import com.mangocity.hotel.base.persistence.HtlQuotaJudge;
import com.mangocity.hotel.base.persistence.HtlQuotaLog;
import com.mangocity.hotel.base.persistence.HtlQuotaNew;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.service.assistant.QuotaQuery;
import com.mangocity.hotel.base.service.assistant.QuotaReturn;


/**
 * 用ibatis查询配额的相关信息
 * @author zuoshengwei
 *
 */
public interface IQueryQuotaDao {
	
	/**
	 * 根据ID获得QuotaNew实体
	 * 
	 * @param quotaNewId
	 * @return
	 */
	public HtlQuotaNew getQuotaNewById(Long quotaNewId);
	
	/**
	 * 更新QuotaNew实体
	 * 
	 * @param quotaNew
	 * @return
	 */
	public void updateQuotaNew(HtlQuotaNew quotaNew);
    
    /**
     * 根据配额总表条件查询配额明细
     * 
     * @param pageSize 
     * @param pageNo 
     * @param roomTypeId 
     * @param quotaType 
     * @param endDate 
     * @param beginDate 
     * @param endDate 
     * @param quota
     * @return
     */
	public Map<String, ?> queryNewQuotaDetail(HtlQuotaJudge  quotaJudgeVO);
	
	/**
	 * 
	 * 
	 * @param hotelId
	 * @param beginDate
	 * @param endDate
	 * @param roomTypeId
	 * @param quotaHolder
	 * @param shareType
	 * @param bedId
	 * @param forewarnFlag
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Map<String, ?> queryNewQuotaByForwarn(Long hotelId, Date beginDate, Date endDate, Long roomTypeId, String quotaHolder,
			Long shareType, Long bedId, Long forewarnFlag, int pageNo, int pageSize);
	
	/**
	 * 
	 * 
	 * @param htlQuotaJudge
	 * @return
	 */
	public String saveQuotaJudge(HtlQuotaJudge htlQuotaJudge);
	
	/**
	 * 
	 * 
	 * @param quotaJudgeId
	 * @return
	 */
	public void quotaRefactor(Long quotaJudgeId);
	
	/**
	 * 
	 * 
	 * @param hotelId
	 */
	public long getQuotaNewCountByHtlId(Long hotelId);
	
	/**
	 * 保存HtlQuotaJudge实体
	 * 
	 * @param quotaJudge
	 */
	public void saveQuotaJudgeModel(HtlQuotaJudge quotaJudge);
	
	/**
	 * 根据HtlQuotaJudge中的某些字段值查询HtlQuotaNew
	 * 
	 * @param quotaJudge
	 * @return
	 */
	public List<HtlQuotaNew> getQuotaNewsByQuotaJude(HtlQuotaJudge quotaJudge);
	
	/**
	 * 批量保存或更新QuotaNew实体
	 * 
	 * @param quatoNewList
	 */
	public void batchSaveOrUpdateQuotaNew(List<HtlQuotaNew> quatoNewList);
	
	/**
	 * 获得配额已经预警的酒店总数
	 * 
	 * @return
	 */
	public long getHotelCount4QuotaForewarn();
	
	/**
	 * 
	 * 
	 * @param beginDateStr
	 * @param endDateStr
	 * @param hotelIdStr
	 * @return
	 */
	public List<Map<String, String>> getBuyQuotaByHtlIdSaleDate(String beginDateStr, String endDateStr,
			String hotelIdStr);
	
	/**
	 * 
	 * @param hotelId
	 * @param roomTypeList
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, ?> queryNewQuotaDateList(Long hotelId, List<HtlRoomtype> roomTypeList, int pageNo, int pageSize);
	
	/**
	 * 
	 * 
	 * @param quotaQuery
	 * @param cutOffDay
	 * @return
	 */
	public HtlQuotaCutoffDayNew getCutOffDayDetail(QuotaQuery quotaQuery, int cutOffDay);
	
	/**
	 * 
	 * @param quotaQuery
	 * @return
	 */
	public HtlQuotaCutoffDayNew getCutOffDayTempDetail(QuotaQuery quotaQuery);
	
	/**
	 * 
	 * 
	 * @param quotaReturn
	 * @return
	 */
	public HtlQuotaCutoffDayNew getQuotaReturnDetail(QuotaReturn quotaReturn);
	
	/**
	 * 
	 * 
	 * @param quotaReturn
	 * @return
	 */
	public HtlQuotaCutoffDayNew getQuotaReturnTempDetail(QuotaReturn quotaReturn);
	
	/**
	 * 
	 * @param quotaCutOffDayNewId
	 */
	public void updateCutOffDayNew(long quotaCutOffDayNewId);
	
	/**
	 * 
	 * @param htlQuotaLog
	 * @return
	 */
	public void saveQuotaLog(HtlQuotaLog htlQuotaLog);
	
	/**
	 * 
	 * @param quotaCutOffDayNew
	 */
	public void updateAvailableQuotaAndUsedQuota(HtlQuotaCutoffDayNew quotaCutOffDayNew);
   
}

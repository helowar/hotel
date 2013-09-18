package com.mangocity.hotel.base.manage;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.manage.assistant.QuotaBathAdjust;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlQuotaJudge;
import com.mangocity.hotel.base.persistence.HtlQuotaNew;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.user.UserWrapper;

public interface IQuotaManage {    
    
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
	 * 保存配额日历调整
	 *
	 * @param params
	 * @param user
	 */
	public void judgeQuota(List<HtlQuotaJudge> list);
	/**
	 * 根据条件查找配额总表
	 * @param hotelId
	 * @param beginDate
	 * @param endDate
	 * @param roomTypeId
	 * @param quotaHolder
	 * @param shareType
	 * @param bedId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Map<String, ?> queryNewQuotaByForwarn(Long hotelId, Date beginDate, Date endDate, Long roomTypeId, String quotaHolder, Long shareType,Long bedId,Long forewarnFlag, int pageNo, int pageSize);
	
	/**
	 * 按配额总表条件修改配额预警数
	 * @param list
	 */
	public void setQuotaForwarn(List<HtlQuotaNew> list);
	
	/**
	 * 
	 * 
	 * @param hotelId
	 * @return
	 */
	public long getQuotaNewCountByHtlId(Long hotelId);
	
	/**
	 * 批量修改配额预警数
	 * @param list
	 */
	public void batchQuotaForwrn(List<HtlQuotaJudge> list);
    
    /**
	 * 计算多少家酒店预警
	 * 
	 * @return
	 */
	public Long countHotelQuotaForwarnNum();
	
	/**
	 * 批量强制解除预警
	 * 
	 * @param quotaJudgeList
	 */
    public void batchQuotaFreeForwrn(List<HtlQuotaJudge> quotaJudgeList);

    /**
     * 
     * @param beginDateStr
     * @param endDateStr
     * @param hotelIdStr
     * @return
     */
	public List<Map<String, String>> queryBuyQuotaReport(String beginDateStr, String endDateStr, String hotelIdStr);
	
	/**
	 * 
	 * @param htlQuotaJudge
	 */
	public void sateQuotaJudge(HtlQuotaJudge htlQuotaJudge);
	
	/**
	 * 
	 * @param htlQuotaJudge
	 */
	public void saveQuotaJudgeNew(HtlQuotaJudge htlQuotaJudge);
	
	/**
	 * 
	 * @param hotelId
	 * @param hRTList
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Map<String, ?> queryNewQuotaDateList(Long hotelId, List<HtlRoomtype> hRTList, int pageNo, int pageSize);
	
	/**
	 *  * 强制解除预警的公用方法
	 * @param params
	 * @param user
	 * @param rowNum
	 */
	public void setFreeQuotaForwarn(Map params,UserWrapper user,int  rowNum);
	/**
	 *  * 显示分页的相关信息
	 * @param map
	 * @param quotaList
	 * @param cutoffDayNum
	 * @param totalNum
	 * @param totalPage
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */ 
	public String  showPagination(Map map,List quotaList,int cutoffDayNum,Long totalNum,int totalPage,int pageSize,int pageNo);
	
	 /**
	  * * 批量调整配额预警
	  * @param params
	  * @param user
	  * @param cutoffDayNum
	 */
	public void setBathQuotaForwarn(Map params,UserWrapper user,int cutoffDayNum);
	
	/**
	 * 按日历调整配额
	 * @param quotaList
	 * @param hotelId
	 * @param user
	 */
	public void  judgeQuotaByDate(List quotaList,Long hotelId,UserWrapper user );
	
	/**
	 * 批量调整配额时的保存
	 * @param lstInputCutoffDay
	 * @param hotelId
	 * @param quotaType
	 * @param shareType
	 * @param takebackQuota
	 * @param quotaHolder
	 * @param quotaPattern
	 * @param user
	 */
	public  void   bathJudgeQuota(List<QuotaBathAdjust> lstInputCutoffDay,Long hotelId,Long quotaType,Long shareType,String takebackQuota, 
			String quotaHolder,String quotaPattern,UserWrapper user);
	
	/**
	 * 根据酒店ID查询酒店合同
	 * @param hotelId
	 * @return
	 */
	public HtlContract queryContractByHotelId(long hotelId);
    
}

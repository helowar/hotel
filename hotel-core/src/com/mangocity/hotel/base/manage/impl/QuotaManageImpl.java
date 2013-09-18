package com.mangocity.hotel.base.manage.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.dao.IContractDao;
import com.mangocity.hotel.base.dao.IQueryQuotaDao;
import com.mangocity.hotel.base.manage.IQuotaManage;
import com.mangocity.hotel.base.manage.RoomControlManage;
import com.mangocity.hotel.base.manage.assistant.QuotaBathAdjust;
import com.mangocity.hotel.base.manage.assistant.QuotaJudgeCalendar;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlQuotaJudge;
import com.mangocity.hotel.base.persistence.HtlQuotaNew;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.HotelBaseConstantBean;

public class QuotaManageImpl implements IQuotaManage {
	
	private static final long QUOTA_FOREWARN = 1L;//配额预警
	
	private static final long QUOTA_FOREWARN_COMMON = 0L;//配额预警正常
	
	private static final long QUOTA_FOREWARN_STOP = 2L;//配额预警强制解除
	
	private static final String QUOTA_HANNEL_BY_HBIZ = "HBIZ";
	
	private IQueryQuotaDao qryQuotaDao;
	private IContractDao contractDao;
	
	private RoomControlManage roomControlManage;       
	
    public Map<String, ?> queryNewQuotaDetail(HtlQuotaJudge  quotaJudgeVO) {
		return qryQuotaDao.queryNewQuotaDetail(quotaJudgeVO);
	}

	public Map<String, ?> queryNewQuotaByForwarn(Long hotelId, Date beginDate, Date endDate,  Long roomTypeId, 
			String quotaHolder, Long shareType,Long bedId,Long forewarnFlag,int pageNo, int pageSize) {		
		return qryQuotaDao.queryNewQuotaByForwarn(hotelId, beginDate, endDate, roomTypeId, quotaHolder, 
				shareType, bedId, forewarnFlag, pageNo, pageSize);
	}
    
	public void judgeQuota(List<HtlQuotaJudge> quotaJudgeList) {
		for(HtlQuotaJudge quotaJudge : quotaJudgeList){
			if(quotaJudge.getOperatorDept() == null) {
				quotaJudge.setOperatorDept("");
			}
			if(quotaJudge.getOperatorId() == null) {
				quotaJudge.setOperatorId("9527");
			}
			if(quotaJudge.getBlnBack() == null) {
				quotaJudge.setBlnBack(Long.valueOf(1L));
			}
			if(quotaJudge.getJudgeWeeks() == null) {
				quotaJudge.setJudgeWeeks("");
			}
			String result = qryQuotaDao.saveQuotaJudge(quotaJudge); 
			qryQuotaDao.quotaRefactor(Long.valueOf(result));
		}
	}	

	public void setQuotaForwarn(List<HtlQuotaNew> quotaNewList) {
		Long hotelId = null;
		for(HtlQuotaNew quotaNew : quotaNewList){
			HtlQuotaNew quota = qryQuotaDao.getQuotaNewById(quotaNew.getID());
			quota.setModifyDate(new Date());
			quota.setModifyId(quotaNew.getModifyId());
			quota.setModifyName(quotaNew.getModifyName());
			quota.setForewarnQuotaNum(quotaNew.getForewarnQuotaNum());
			Long ableNum = (null == quota.getBuyQuotaAbleNum()?0L : quota.getBuyQuotaAbleNum())
						+ (null == quota.getCommonQuotaAbleNum()?0L : quota.getCommonQuotaAbleNum())
						+ (null == quota.getCasualQuotaAbleNum()?0L : quota.getCasualQuotaAbleNum());
			
			//如果预警数小于可用数则状态为正常
			if(quotaNew.getForewarnQuotaNum() < ableNum){
				quota.setForewarnFlag(Long.valueOf(QUOTA_FOREWARN_COMMON));
				hotelId = quota.getHotelId();
			}else{
				//预警数
				if(quotaNew.getForewarnFlag().longValue() == QUOTA_FOREWARN_STOP){
					quota.setForewarnFlag(quotaNew.getForewarnFlag());
					hotelId = quota.getHotelId();
				}else{
					quota.setForewarnFlag(Long.valueOf(QUOTA_FOREWARN));
				}
			}
			qryQuotaDao.updateQuotaNew(quota);
		}
		
		if(null != hotelId){
			roomControlManage.updateScheduleWarnFlag(hotelId, new Date());
		}
	}
	
	public long getQuotaNewCountByHtlId(Long hotelId) {
		return qryQuotaDao.getQuotaNewCountByHtlId(hotelId);
	}

	public void batchQuotaForwrn(List<HtlQuotaJudge> quotaJudgeList) {
		for(HtlQuotaJudge quotaJudge : quotaJudgeList){
			quotaJudge.setJudgeType("W-JUDGE");
			quotaJudge.setQuotaChannel("HBIZ");
			quotaJudge.setOperatorTime(new Date());
			qryQuotaDao.saveQuotaJudgeModel(quotaJudge);
	        
	        List<HtlQuotaNew> quotaNewList = qryQuotaDao.getQuotaNewsByQuotaJude(quotaJudge);
	        for(HtlQuotaNew quotaNew : quotaNewList) {
	        	if(!isWeekDayOfAbleSaleDateInJudgeWeeks(quotaNew.getAbleSaleDate(), quotaJudge.getJudgeWeeks())) {
	   		 		continue;
	   		 	}            

                quotaNew.setForewarnYesOrNo(Long.valueOf(1L));//设置是否需要预警                
                quotaNew.setForewarnQuotaNum(quotaJudge.getQuotaNum());//设置预警的数量
                
                
                long warnNum = (quotaJudge.getQuotaNum() != null)?quotaJudge.getQuotaNum().longValue() : 0L;
                long totalAbleData = getAvailableQuotaCount(quotaNew);
                if(warnNum >= totalAbleData){//判断预警数是否大于可用数
                	long warnFalg = (quotaNew.getForewarnFlag() != null)?quotaNew.getForewarnFlag().longValue(): 0L;
                	if(warnFalg != 2L){
                		quotaNew.setForewarnFlag(Long.valueOf(1L));
                	}
                }else{
                	quotaNew.setForewarnFlag(Long.valueOf(0L));
                }
	        }
	        
	        qryQuotaDao.batchSaveOrUpdateQuotaNew(quotaNewList);
			
			if(null != quotaJudge.getHotelId()){
				roomControlManage.updateScheduleWarnFlag(quotaJudge.getHotelId(), new Date());
			}
		}
	}

	private long getAvailableQuotaCount(HtlQuotaNew quotaNew) {
		Long totalAbleData = 0L;
		if(quotaNew.getCasualQuotaAbleNum() != null){
		    totalAbleData = totalAbleData + quotaNew.getCasualQuotaAbleNum();
		    if(quotaNew.getCasualQuotaOutofdateNum() != null){
		    	totalAbleData = totalAbleData - quotaNew.getCasualQuotaOutofdateNum();
		    }
		}
		if(quotaNew.getCommonQuotaAbleNum() != null){
		    totalAbleData = totalAbleData + quotaNew.getCommonQuotaAbleNum();
		    if(quotaNew.getCommonQuotaOutofdateNum() != null){
		    	totalAbleData = totalAbleData - quotaNew.getCommonQuotaOutofdateNum();
		    }
		}
		if(quotaNew.getBuyQuotaAbleNum() != null){
		    totalAbleData = totalAbleData + quotaNew.getBuyQuotaAbleNum();
		    if(quotaNew.getBuyQuotaOutofdateNum() != null){
		    	totalAbleData = totalAbleData - quotaNew.getBuyQuotaOutofdateNum();
		    }
		}
		
		return totalAbleData.longValue();
	}
	
	public Long countHotelQuotaForwarnNum() {
		return qryQuotaDao.getHotelCount4QuotaForewarn();
	}
	
	public void batchQuotaFreeForwrn(List<HtlQuotaJudge> quotaJudgeList){
		for(HtlQuotaJudge quotaJudge : quotaJudgeList){
			quotaJudge.setJudgeType("W-FROBID");
			quotaJudge.setQuotaChannel("HBIZ");
			quotaJudge.setOperatorTime(new Date());
			qryQuotaDao.saveQuotaJudgeModel(quotaJudge);
						
			List<HtlQuotaNew> quotaNewList = qryQuotaDao.getQuotaNewsByQuotaJude(quotaJudge);
			for(HtlQuotaNew quotaNew : quotaNewList){
	   		 	if(!isWeekDayOfAbleSaleDateInJudgeWeeks(quotaNew.getAbleSaleDate(), quotaJudge.getJudgeWeeks())) {
	   		 		continue;
	   		 	}           
                
                quotaNew.setForewarnYesOrNo(1L);//设置是否需要预警                
                quotaNew.setForewarnFlag(2L);//设置成强制解除预警
	        }
			
			qryQuotaDao.batchSaveOrUpdateQuotaNew(quotaNewList);
			
			if(null != quotaJudge.getHotelId()){
				roomControlManage.updateScheduleWarnFlag(quotaJudge.getHotelId(), new Date());
			}
		}
	}

	private boolean isWeekDayOfAbleSaleDateInJudgeWeeks(Date ableSaleDate, String judgeWeekDaysStr) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(ableSaleDate);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
		dayOfWeek = (dayOfWeek == 0)?7 : dayOfWeek;
		
		boolean isInJudgeWeekDays = true;
		if(null != judgeWeekDaysStr && !"".equals(judgeWeekDaysStr.trim())) {
			int index = judgeWeekDaysStr.indexOf(String.valueOf(dayOfWeek));
			if(index < 0){
				isInJudgeWeekDays = false;
			}
		}
		
		return isInJudgeWeekDays;
	}
	
	public void sateQuotaJudge(HtlQuotaJudge htlQuotaJudge) {
		if(htlQuotaJudge.getOperatorDept() == null) {
			htlQuotaJudge.setOperatorDept("");
		}
		if(htlQuotaJudge.getJudgeWeeks() == null) {
			htlQuotaJudge.setJudgeWeeks("");
		}
		htlQuotaJudge.setQuotaCutoffDayNewId(Long.valueOf(0L));
		
		String quotaJudgeId = qryQuotaDao.saveQuotaJudge(htlQuotaJudge);
		htlQuotaJudge.setID(Long.valueOf(quotaJudgeId));
	}
	
	/**
	 *  * 强制解除预警的公用方法
	 * @param params
	 * @param user
	 * @param rowNum
	 */
	public void setFreeQuotaForwarn(Map params,UserWrapper user,int  rowNum) {
		List<HtlQuotaJudge> list = new ArrayList<HtlQuotaJudge>();
		HtlQuotaJudge htlQuota = new HtlQuotaJudge();
		Long roomTypeId = Long.parseLong((String) params.get("roomTypeIdfree"));
		htlQuota.setRoomtypeId(roomTypeId);
		Long bedId = Long.parseLong((String) params.get("bedIdfree"));
		htlQuota.setBedId(bedId);
		String startDate = (String) params.get("cutoffBeginDatefree");
		htlQuota.setStartDate(DateUtil.getDate(startDate));
		Date curDate = DateUtil.getDate(new Date());
		htlQuota.setStartDate(htlQuota.getStartDate().getTime() <= curDate.getTime()?curDate:DateUtil.getDate(startDate));
		String cutoffEndDate = (String) params.get("cutoffEndDatefree");
		htlQuota.setEndDate(DateUtil.getDate(cutoffEndDate));
		htlQuota.setHotelId(Long.parseLong((String) params.get("hotelId")));
		htlQuota.setQuotaHolder((String) params.get("quotaHolder"));
		htlQuota.setQuotaShare((String) params.get("shareType"));
		// 星期 add by jun.guo
		htlQuota.setJudgeWeeks((String) params.get("weeksfree"));
		if (user != null && user.getId() != null) {
			htlQuota.setOperatorId(String.valueOf(user.getId()));
		}
		if (user != null && user.getName() != null) {
			htlQuota.setOperatorName(user.getName());
		}
		list.add(htlQuota);
		if (rowNum > 1) {
			list = this.setFreeQuotaForwarnList(rowNum, params, roomTypeId,bedId, startDate, curDate, cutoffEndDate,user,list);
		}
		this.batchQuotaFreeForwrn(list);
	}
	
	/**
	 * 强制解除预警的记录数大于1时的参数的封装
	 * @param params
	 * @param bedId
	 * @param startDate
	 * @param curDate
	 * @param cutoffEndDate
	 * @param user
	 * @param list
	 * @return
	 */
	public List<HtlQuotaJudge>  setFreeQuotaForwarnList(int rowNum,Map params,Long roomTypeId,Long bedId,String startDate,Date curDate,String cutoffEndDate,
			UserWrapper user,List<HtlQuotaJudge> list ) {
		for (int m = 1; m <= rowNum; m++) {
			String roomTypeStr = (String) params.get("roomTypeIdfree_" + m);
			if ((null != roomTypeStr) && (!"".equals(roomTypeStr))) {
				HtlQuotaJudge htlQuota = new HtlQuotaJudge();
				roomTypeId = Long.parseLong((String) params.get("roomTypeIdfree_" + m));
				htlQuota.setRoomtypeId(roomTypeId);
				bedId = Long.parseLong((String) params.get("bedIdfree_" + m));
				htlQuota.setBedId(bedId);
				startDate = (String) params.get("cutoffBeginDatefree_" + m);
				htlQuota.setStartDate(htlQuota.getStartDate().getTime() <= curDate.getTime()?curDate:DateUtil.getDate(startDate));
				cutoffEndDate = (String) params.get("cutoffEndDatefree_" + m);
				htlQuota.setEndDate(DateUtil.getDate(cutoffEndDate));
				htlQuota.setHotelId(Long.parseLong((String) params.get("hotelId")));
				htlQuota.setQuotaHolder((String) params.get("quotaHolder"));
				htlQuota.setQuotaShare((String) params.get("shareType"));
				// 星期 add by jun.guo
				htlQuota.setJudgeWeeks((String) params.get("weeksfree_" + m));
				if (user != null && user.getId() != null) {
					htlQuota.setOperatorId(String.valueOf(user.getId()));
				}
				if (user != null && user.getName() != null) {
					htlQuota.setOperatorName(user.getName());
				}
				list.add(htlQuota);
			}
		}
		return  list;
	}
	
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
	public String  showPagination(Map map,List quotaList,int cutoffDayNum,Long totalNum,int totalPage,int pageSize,int pageNo) {
		String  isDisablePage = "";
	
		// 如果总的页数等于当前的页数
		if (pageNo == totalPage) {
			// 如果是第一页或者为0，那么页面上"上一页"和"下一页"的按钮都要灰选
			if (pageNo == 1 || pageNo == 0) {
				isDisablePage = "3";
				// 如果是不是第一页，则是最后一页，那么页面上"下一页"的按钮要灰选
			} else {
				isDisablePage = "2";
			}
		} else {// 如果当前的页数 不等于 总的页数
			// 如果是第一页，那么页面上"上一页"的按钮要灰选
			if (pageNo == 1) {
				isDisablePage = "1";
			}
		}
		return  isDisablePage;
	}
	
	
	
	 
    /**
	  * * 批量调整配额预警
	  * @param params
	  * @param user
	  * @param cutoffDayNum
	 */
	public void setBathQuotaForwarn(Map params,UserWrapper user,int cutoffDayNum) {
		List<HtlQuotaJudge> list = new ArrayList<HtlQuotaJudge>();
		HtlQuotaJudge htlQuota = new HtlQuotaJudge();
		Long roomTypeId = Long.parseLong((String) params.get("roomTypeId"));
		htlQuota.setRoomtypeId(roomTypeId);
		Long bedId = Long.parseLong((String) params.get("bedId"));
		htlQuota.setBedId(bedId);
		String startDate = (String) params.get("cutoffBeginDate");
		htlQuota.setStartDate(DateUtil.getDate(startDate));
		Date curDate = DateUtil.getDate(new Date());
		htlQuota.setStartDate(htlQuota.getStartDate().getTime() <= curDate.getTime() ? curDate : DateUtil
				.getDate(startDate));
		String cutoffEndDate = (String) params.get("cutoffEndDate");
		htlQuota.setEndDate(DateUtil.getDate(cutoffEndDate));
		htlQuota.setHotelId(Long.parseLong((String) params.get("hotelId")));
		htlQuota.setQuotaHolder((String) params.get("quotaHolder"));
		htlQuota.setQuotaShare((String) params.get("shareType"));
		String tempNum = (String) params.get("forewarnQuotaNum");
		htlQuota.setQuotaNum(StringUtil.isValidStr(tempNum) ? Long.parseLong(tempNum) : 0L);
		// 星期 add by jun.guo
		htlQuota.setJudgeWeeks((String) params.get("weeks"));
		if (user != null && user.getId() != null) {
			htlQuota.setOperatorId(String.valueOf(user.getId()));
		}
		if (user != null && user.getName() != null) {
			htlQuota.setOperatorName(user.getName());
		}
		list.add(htlQuota);
		if (cutoffDayNum > 1) {
			list = this.setQuotaForwarnList(params,user,cutoffDayNum,roomTypeId, bedId, startDate, curDate, cutoffEndDate, tempNum, list);
		}
		this.batchQuotaForwrn(list);
	}

	/**
	 * 批量调整配额预警的记录数大于1时，设置配额调整的记录值
	 * 
	 * @param params
	 * @return
	 */
	public List<HtlQuotaJudge> setQuotaForwarnList(Map params,UserWrapper user, int cutoffDayNum,Long roomTypeId, Long bedId, String startDate,
			Date curDate,String cutoffEndDate, String tempNum, List<HtlQuotaJudge> list) {
		for (int m = 1; m <= cutoffDayNum; m++) {
			String roomTypeStr = (String) params.get("roomTypeId_" + m);
			if ((null != roomTypeStr) && (!"".equals(roomTypeStr))) {
				HtlQuotaJudge htlQuota = new HtlQuotaJudge();
				roomTypeId = Long.parseLong((String) params.get("roomTypeId_" + m));
				htlQuota.setRoomtypeId(roomTypeId);
				bedId = Long.parseLong((String) params.get("bedId_" + m));
				htlQuota.setBedId(bedId);
				startDate = (String) params.get("cutoffBeginDate_" + m);
				htlQuota.setStartDate(htlQuota.getStartDate().getTime() <= curDate.getTime() ? curDate : DateUtil
						.getDate(startDate));
				cutoffEndDate = (String) params.get("cutoffEndDate_" + m);
				htlQuota.setEndDate(DateUtil.getDate(cutoffEndDate));
				tempNum = (String) params.get("forewarnQuotaNum_" + m);
				htlQuota.setQuotaNum(StringUtil.isValidStr(tempNum) ? Long.parseLong(tempNum) : 0L);
				htlQuota.setHotelId(Long.parseLong((String) params.get("hotelId")));
				htlQuota.setQuotaHolder((String) params.get("quotaHolder"));
				htlQuota.setQuotaShare((String) params.get("shareType"));
				// 星期 add by jun.guo
				htlQuota.setJudgeWeeks((String) params.get("weeks_" + m));
				if (user != null && user.getId() != null) {
					htlQuota.setOperatorId(String.valueOf(user.getId()));
				}
				if (user != null && user.getName() != null) {
					htlQuota.setOperatorName(user.getName());
				}
				list.add(htlQuota);
			}
		}
		return list;
	}
	
	/**
	 * 按日历调整配额
	 * @param quotaList
	 * @param hotelId
	 * @param user
	 */
	public void  judgeQuotaByDate(List quotaList,Long hotelId,UserWrapper user ){
		List<HtlQuotaJudge> list = new ArrayList<HtlQuotaJudge>();
		for (Iterator i = quotaList.iterator(); i.hasNext();) {
			QuotaJudgeCalendar quota = (QuotaJudgeCalendar) i.next();
			HtlQuotaJudge quotaJudge = new HtlQuotaJudge();
			quotaJudge.setHotelId(hotelId);
			quotaJudge.setBedId(quota.getBedId());
			quotaJudge.setCutoffday(quota.getCutoffday());
			quotaJudge.setCutofftime(quota.getCutofftime());
			quotaJudge.setEndDate(quota.getAbleDate());
			Date curDate = DateUtil.getDate(new Date());
			quotaJudge.setStartDate(quota.getAbleDate().getTime() <= curDate.getTime()?curDate:quota.getAbleDate());
			if (user != null && null != user.getId()) {
				quotaJudge.setOperatorId(String.valueOf(user.getId()));
			}
			if (user != null && null != user.getName()) {
				quotaJudge.setOperatorName(user.getName());
			}
			quotaJudge.setOperatorTime(new Date());
			quotaJudge.setQuotaChannel(QUOTA_HANNEL_BY_HBIZ);
			quotaJudge.setQuotaCutoffDayNewId(quota.getQuotaDetailId());
			quotaJudge.setQuotaHolder(quota.getQuotaHolder());
			quotaJudge.setJudgeType(quota.getJudgeTypeStr());
			quotaJudge.setQuotaPattern(quota.getQuotaPattern());
			quotaJudge.setQuotaShare(quota.getQuotaShare());
			quotaJudge.setQuotaType(quota.getQuotaType());
			quotaJudge.setRoomtypeId(quota.getRoomTypeIds());
			quotaJudge.setBlnBack(quota.getBlnBack());
			// 添加星期
			quotaJudge.setJudgeWeeks("1,2,3,4,5,6,7");
			if (null != quota.getQuotaNum() && quota.getQuotaNum() > 0) {
				quotaJudge.setQuotaNum(quota.getQuotaNum());
				list.add(quotaJudge);
			} else {
				if ((!quota.getOldCutoffday().equals(quota.getCutoffday()))
						|| (!quota.getCutofftime().equals(quota.getOldCutoffTime()))) {
					quotaJudge.setQuotaNum(quota.getOldQuotaNum());
					quotaJudge.setJudgeType(HotelBaseConstantBean.JUDGE_TYPE_UTP);
					list.add(quotaJudge);
				}
			}
		}
		this.judgeQuota(list);
	}
	
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
			String quotaHolder,String quotaPattern,UserWrapper user){
		for (QuotaBathAdjust quotaBathAdjustObj : lstInputCutoffDay) {
			HtlQuotaJudge quotaJudgeObj = new HtlQuotaJudge();
			Date curDate = DateUtil.getDate(new Date());
			quotaJudgeObj.setStartDate(quotaBathAdjustObj.getCutoffBeginDate().getTime() >= curDate.getTime() ? 
					quotaBathAdjustObj.getCutoffBeginDate(): curDate);
			quotaJudgeObj.setEndDate(quotaBathAdjustObj.getCutoffEndDate());
			quotaJudgeObj.setHotelId(hotelId);
			quotaJudgeObj.setRoomtypeId(Long.parseLong(quotaBathAdjustObj.getRoomTypeId()));
			// 床型ID
			quotaJudgeObj.setBedId(quotaBathAdjustObj.getBedId());
			// 配额类型
			quotaJudgeObj.setQuotaType(quotaType.toString());
			// 配额共享模式
			quotaJudgeObj.setQuotaShare(shareType.toString());
			// 配额过期日期
			quotaJudgeObj.setCutoffday(quotaBathAdjustObj.getCutoffDay());
			// 配额过期时间
			quotaJudgeObj.setCutofftime(quotaBathAdjustObj.getCutoffTime());
			// 配额调整方式
			quotaJudgeObj.setJudgeType(quotaBathAdjustObj.getJudgeType());
			// 配额数
			quotaJudgeObj.setQuotaNum(quotaBathAdjustObj.getQuotaQty());
			// 星期 add by guojun 2009-12-15
			quotaJudgeObj.setJudgeWeeks(quotaBathAdjustObj.getWeeks());
			// 是否可退
			if (null != takebackQuota && !"".equals(takebackQuota)) {
				quotaJudgeObj.setBlnBack(Long.parseLong(takebackQuota));
			}
			// 配额所有者
			quotaJudgeObj.setQuotaHolder(quotaHolder);
			quotaJudgeObj.setOperatorTime(DateUtil.getSystemDate());
			quotaJudgeObj.setQuotaPattern(quotaPattern);
			quotaJudgeObj.setOperatorName(user.getName());
			quotaJudgeObj.setOperatorId(user.getLoginName());
			
			this.sateQuotaJudge(quotaJudgeObj);
			// 在调存储过程FUN_QUOTAREFACTOR
			this.saveQuotaJudgeNew(quotaJudgeObj);
		}
		
	}
	
	public void saveQuotaJudgeNew(HtlQuotaJudge htlQuotaJudge) {
		qryQuotaDao.quotaRefactor(htlQuotaJudge.getID());
	}
	
	public Map<String, ?> queryNewQuotaDateList(Long hotelId, List<HtlRoomtype> roomTypeList, int pageNo, int pageSize) {    	
		return qryQuotaDao.queryNewQuotaDateList(hotelId, roomTypeList, pageNo, pageSize);		
	}

	public List<Map<String, String>> queryBuyQuotaReport(String beginDateStr, String endDateStr, String hotelIdStr) {		
		return qryQuotaDao.getBuyQuotaByHtlIdSaleDate(beginDateStr, endDateStr, hotelIdStr);
	}

	public void setQryQuotaDao(IQueryQuotaDao qryQuotaDao) {
		this.qryQuotaDao = qryQuotaDao;
	}

	public void setRoomControlManage(RoomControlManage roomControlManage) {
		this.roomControlManage = roomControlManage;
	}

	public IContractDao getContractDao() {
		return contractDao;
	}

	public void setContractDao(IContractDao contractDao) {
		this.contractDao = contractDao;
	}

	public HtlContract queryContractByHotelId(long hotelId) {
		return contractDao.getContractInfoByHotelId(hotelId);
	}
}

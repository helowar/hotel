package com.mangocity.hotel.base.manage.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.dao.HtlQuotaDao;
import com.mangocity.hotel.base.manage.QuotaPriceManage;
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
import com.mangocity.hotel.base.persistence.OrLockedRecords;
import com.mangocity.hotel.base.persistence.QuotaForCC;
import com.mangocity.hotel.base.service.HotelRoomService;
import com.mangocity.hotel.base.service.ILockedRecordService;
import com.mangocity.hotel.base.service.assistant.QuotaQuery;
import com.mangocity.hotel.base.util.BizRuleCheck;
import com.mangocity.hotel.base.util.CodeName;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;

/**
 * 管理配额,价格信息
 */
public class QuotaPriceManageImpl implements QuotaPriceManage {
    
    private HtlQuotaDao htlQuotaDao;
    
    private HotelRoomService hotelRoomService;
    
    /**
	 *酒店，合同，房�?�，配额等加解锁操作接口 
	 */ 
    private ILockedRecordService lockedRecordService;
    
    public void setLockedRecordService(ILockedRecordService lockedRecordService) {
		this.lockedRecordService = lockedRecordService;
	}

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
    
    public long saveOrUpdateQuotabatch(HtlQuotabatch htlQuotabatch) { 
        return htlQuotaDao.saveOrUpdateQuotabatch(htlQuotabatch);
    }
    
    public void batchSaveOrUpdateQuota(List<HtlQuota> listHtlQuota) {
    	htlQuotaDao.batchSaveOrUpdateQuota(listHtlQuota);
    }
    
    public List<HtlQuota> getQuotaInWeekdaysByBatchIdSaleDate(long batchQuotaId, Date beginDate, 
    		Date endDate, String[] weeks) {
        return htlQuotaDao.qryQuotaInWeekdaysByBatchIdSaleDate(batchQuotaId, beginDate, endDate, weeks);
    }
    
    public List<?> getCutoffDayByHotelIdContractId(long hotelId, long contractId) {
    	return htlQuotaDao.queryContrCuf(hotelId, contractId);
    }
    
    public List<HtlQuota> getQuotaByRoomTypeQuotaType(long hotelId, long roomType, String quotaType, Date beginDate, Date endDate) {
    	return htlQuotaDao.qryQuotaByRoomTypeQuotaType(hotelId, roomType, quotaType, beginDate, endDate);
    }
    
    /** 
     * 将配额分割为三类(子方法)  面付独占 预付独占 共享
     * @param beginDate
     * @param endDate
     * @param weeks
     * @param roomType
     * @param quotaType
     * @param hotelId
     * @return
     */
	private  Map<String,Map<String,HtlQuota>> divisionHtlQuotaForView(Date beginDate,Date endDate,String weeks,long roomType,String quotaType,long hotelId){
	    	String[] week=null;
	    	if (null != weeks && 0 < weeks.length()) {
	    		week = weeks.split(",");
	    	}
    	 	List<HtlQuota> lstQuotas = null;
    	    if (BizRuleCheck.isAllWeek(week)) {
    	        lstQuotas = htlQuotaDao.qryQuotaByRoomTypeQuotaType(hotelId, roomType, quotaType, beginDate, endDate);
    	    } else {
    	        lstQuotas = htlQuotaDao.qryQuotaByRoomTypeQuotaTypeWithWeeks(hotelId, roomType, quotaType, beginDate,
    	            endDate, week);
    	    }
    	    // 面付独占
    	    Map<String,HtlQuota> mapFP = new HashMap<String,HtlQuota>(lstQuotas.size());

    	    // 预付独占
    	    Map<String,HtlQuota> mapPY = new HashMap<String,HtlQuota>(lstQuotas.size());

    	    // 共享
    	    Map<String,HtlQuota> mapSH = new HashMap<String,HtlQuota>(lstQuotas.size());
    	    
    	    Map<String,Map<String,HtlQuota>> quotaForViewMap= new HashMap<String,Map<String,HtlQuota>>(3);

    	    for (HtlQuota qa:lstQuotas ){
    	        if (BizRuleCheck.isPrepayQuota(qa.getShareType())) {
    	            mapPY.put(DateUtil.formatDateToSQLString(qa.getAbleSaleDate()), qa);
    	        } else if (BizRuleCheck.isFaceToPayQuota(qa.getShareType())) {
    	            mapFP.put(DateUtil.formatDateToSQLString(qa.getAbleSaleDate()), qa);
    	        } else if (BizRuleCheck.isQuotaShare(qa.getShareType())) {
    	            mapSH.put(DateUtil.formatDateToSQLString(qa.getAbleSaleDate()), qa);
    	        }
    	    }
    	    quotaForViewMap.put("facePay", mapPY);//面付独占
    	    quotaForViewMap.put("prePay", mapFP);//预付独占
    	    quotaForViewMap.put("sharePay", mapSH);//共享
    	    return quotaForViewMap;
    }
    
    /** 
     * 将配额分割为三类存在三个List集合   面付独占 预付独占 共享
     * @param beginDate
     * @param endDate
     * @param weeks
     * @param roomType
     * @param quotaType
     * @param hotelId
     * @return
     */
    public Map<String,List<?>> buildHtlQuotaForView(Date beginDate,Date endDate,String weeks,long roomType,String quotaType,long hotelId){
    	Map<String,Map<String,HtlQuota>> quotaForViewMap = this.divisionHtlQuotaForView(beginDate, endDate, weeks, roomType, quotaType, hotelId);
        // 面付独占
	    Map<String,HtlQuota> mapFP = quotaForViewMap.get("facePay");
	    // 预付独占
	    Map<String,HtlQuota> mapPY = quotaForViewMap.get("prePay");
	    // 共享
	    Map<String,HtlQuota> mapSH = quotaForViewMap.get("sharePay");
	    Map<String,List<?>> quotaMap = new HashMap<String,List<?>>(4);
    	  Calendar calStart = Calendar.getInstance();
          calStart.setTime(beginDate);
          Calendar calEnd = Calendar.getInstance();
          calEnd.setTime(endDate);
          // 预付
          List<HtlQuota> lstPreQuotas = new ArrayList<HtlQuota>();
          // 面付
          List<HtlQuota> lstFaceQuotas = new ArrayList<HtlQuota>();
          // 共享
          List<HtlQuota> lstShareQuotas = new ArrayList<HtlQuota>();

          List<String> lstDate = new ArrayList<String>();

          // 修改的原因是：当查询的只是一天的配额总表，即起始日期和结束日期相同的情况下，原来的代码没有显示信息。By wuyun
          if (!beginDate.after(endDate)) {
              while (true) {
                  String strEndDate = DateUtil.dateToString(DateUtil.getDate(endDate, 1));
                  lstDate.add(DateUtil.formatDateToMMDD(calStart.getTime()));
                  // 先要查找，如果没有找到要补0，只做显示用，不能更新数据库
                  if (mapFP.size()>0) {// 面付
                      lstFaceQuotas=initHtlQuota(mapFP,calStart);
                  }
                  if (mapPY.size()>0) {// 预付
                      lstPreQuotas=initHtlQuota(mapPY,calStart);
                  }
                  if (mapSH.size()>0) {// 共享
                      lstShareQuotas =initHtlQuota(mapSH,calStart);
                  }
                  calStart.add(Calendar.DATE, 1);
                  String strCurrDate = DateUtil.dateToString(calStart.getTime());
                  if (strCurrDate.equals(strEndDate)) {
                      break;
                  }
              }
          }
          quotaMap.put("lstDate", lstDate);
          quotaMap.put("lstPreQuotas", lstPreQuotas);
          quotaMap.put("lstFaceQuotas", lstFaceQuotas);
          quotaMap.put("lstShareQuotas", lstShareQuotas);
         return quotaMap;
    }
    
    /**
     * 查询配额，查不到即初始化补零
     * @param map
     * @param calStart
     * @return
     */
    private List<HtlQuota> initHtlQuota(Map<String,HtlQuota> map,Calendar calStart){
    	  List<HtlQuota> quoteList = new ArrayList<HtlQuota>(1);
    	  String starTime = DateUtil.formatDateToSQLString(calStart.getTime());
    	  HtlQuota q = map.get(starTime);
    	  if (null == q) {
             q = new HtlQuota();
             q.setAbleSaleDate(calStart.getTime());
             q.setAbleQty(0);
             q.setUsedQty(0);// 已用
             q.setAvailQty(0);// 未使用，
             q.setFreeQty(0);// 已释放数
             q.setTotalQty(0);// 总数
    	  }
    	  quoteList.add(q);
    	  return quoteList;
    }
    /**
     * 把页面中的cutoff Day 放到hashMap中，以方面查询
     * @param params
     * @param rowCutNo
     * @return
     */
    @SuppressWarnings("unchecked")
	private Map<Long,HtlCutoffDayQuota> addcutoffDayToMap(Map params,int rowCutNo){
         // 取得页面传递的cutoff Day
         List<Object> lstCutoffDay = MyBeanUtil.getBatchObjectFromParam(params, HtlCutoffDayQuota.class,
             rowCutNo);
         // 把页面中的cutoff Day 放到hashMap中，以方面查询
         HashMap<Long,HtlCutoffDayQuota> mapCutoffDay = new HashMap<Long,HtlCutoffDayQuota>(lstCutoffDay.size());
         for (int k = 0; k < lstCutoffDay.size(); k++) {
             HtlCutoffDayQuota cq = (HtlCutoffDayQuota) lstCutoffDay.get(k);
             mapCutoffDay.put(cq.getID(), cq);
         }
         return mapCutoffDay;
    }
    /**
     * 更新cuoffDay以及封装配额List
     */
    @SuppressWarnings("unchecked")
	public Map updateCutOffDayAndBuildQuotaList(Map params,int rowCutNo, boolean sumOutsideQuotsOrNot,long roomType,long hotelId){
    	 Object o = params.get("quotaId");
         String[] quotaIds = new String[1];
         if (o instanceof String) {
             quotaIds[0] = (String) o;
         } else {
             quotaIds = (String[]) params.get("quotaId");
         }
         // 把页面中的cutoff Day 放到hashMap中，以方面查询
         Map<Long,HtlCutoffDayQuota> mapCutoffDay = addcutoffDayToMap(params,rowCutNo);
         List<HtlQuota> lstQuotasUpdate = new ArrayList<HtlQuota>(quotaIds.length);

         long contractId = 0;
         for (String quotaId:quotaIds) {
             HtlQuota quota = htlQuotaDao.findHtlQuota(Long.parseLong(quotaId));
             if (null == quota) {
            	 continue;
             }
             
        	 HtlRoom htlRoom = null;        	 
        	 int outSideQuota  = 0;
        	 if(sumOutsideQuotsOrNot){
        		 htlRoom = hotelRoomService.getRoomByRoomTypeHotelIdSaleDate(roomType, hotelId, quota.getAbleSaleDate());
        		 outSideQuota = htlRoom.getOutsideQty();
        	 }
             // 不能直接更新,要多数据中读出来，已页面的数据去更新配额数量
        	 int totolQuota = 0;
        	 List<HtlCutoffDayQuota> cutoffDayList = quota.getLstCutOffDay();
             for (HtlCutoffDayQuota hcq:cutoffDayList) {
                 HtlCutoffDayQuota cq = mapCutoffDay.get(hcq.getID());
                 hcq.setQuotaQty(cq.getQuotaQty());
                 hcq.setCutoffTime(cq.getCutoffTime());
                 /***
                  * v2.8.1 guojun 2009-06-15 14:26 如果存在呼出配额,将呼出配额带入计算
                  */
                 if(sumOutsideQuotsOrNot){
                	 if (outSideQuota>0) {
                		 if (0 <= (hcq.getQuotaQty() - hcq.getCutoffUsedQty() - outSideQuota)) {
                			 hcq.setCutoffUsedQty(hcq.getCutoffUsedQty() + outSideQuota);
                			 quota.setUsedQty(outSideQuota + quota.getUsedQty());
                			 outSideQuota = 0;
                		 }
                		 if (0 > (hcq.getQuotaQty() - hcq.getCutoffUsedQty() - outSideQuota)) {
                			 hcq.setCutoffUsedQty(hcq.getQuotaQty());
                			 quota.setUsedQty((hcq.getQuotaQty() - hcq.getCutoffUsedQty()) + quota.getUsedQty());
                			 outSideQuota -= (hcq.getQuotaQty() - hcq.getCutoffUsedQty());
                		 }
                	 }
                	 totolQuota += cq.getQuotaQty();
                 }
             }
             /***
              * v2.8.1 guojun 2009-06-15 14:26 如果存在呼出配额,则更新呼出配额
              */
             if(sumOutsideQuotsOrNot){
            	 if (outSideQuota>0) {
            		 htlRoom.setOutsideQty(outSideQuota);
            		 hotelRoomService.updateHtlRoom(htlRoom);
            		 quota.setFreeQty(totolQuota);
            		 quota.setAvailQty(quota.getFreeQty() - quota.getUsedQty());
            	 }
            	 String takebackQuota = "takebackQuota_" + quota.getID();
            	 String mm = (String) params.get(takebackQuota);
            	 boolean kk = Boolean.valueOf(mm);
            	 quota.setTakebackQuota(kk);
             }
             quota = this.calcQuota(quota);
             lstQuotasUpdate.add(quota);
             contractId = quota.getContractId();
         }
         
         Map map = new HashMap(2);
         map.put("contractId", contractId);
         map.put("lstQuotasUpdate", lstQuotasUpdate);
    	return map;
    }
    
    /**
     * 从Session获取配额集合,获取不到就查
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
	public List getQuotaByMap(Map map,String pageStr,String weeks,long roomType,String quotaType,long hotelId,Date midbDate,Date mideDate){
    	List lstQuotas = null;
    	String[] week = null;
    	if (null != weeks && 0 < weeks.length()) {
                week = weeks.split(",");
         }
    	 if (null != map.get(pageStr)) {
             lstQuotas = (ArrayList) map.get(pageStr);
         } else {
             if (BizRuleCheck.isAllWeek(week)) {
            	 lstQuotas = htlQuotaDao.qryQuotaByRoomTypeQuotaType(hotelId, roomType, quotaType, midbDate, mideDate);
             } else {
                 lstQuotas = htlQuotaDao.qryQuotaByRoomTypeQuotaTypeWithWeeks(hotelId, roomType, quotaType, midbDate, mideDate, week);
             }
         }
    	 return lstQuotas;
    	 
    }
    
    /**
     * 根据页数 获取当前页30天的日期
     * 
     * @return
     */
    public List<Date> getDateForNextThirtyDays(String weeks,String[] week,Date beginDate,Date endDate,int pageTotal,int pageNo){
    	Date mideDate = null;
        Date midbDate = null;
    	List<Date> dateList = new ArrayList<Date>(2);
    	if (null != weeks && 0 < weeks.length()) {
            week = weeks.split(",");
        }
        if (BizRuleCheck.isAllWeek(week)) {
            int dayNum = DateUtil.getDay(beginDate, endDate);
            if (31 > dayNum) {
                mideDate = endDate;
                midbDate = beginDate;
                pageTotal = 1;
            } else {
                pageTotal = dayNum / 30 + (0 < dayNum % 30 ? 1 : 0);
                if (1 == pageNo) {
                    midbDate = beginDate;
                    mideDate = DateUtil.getDate(beginDate, 30 * pageNo);
                } else {
                    midbDate = DateUtil.getDate(beginDate, (30 * (pageNo - 1) + pageNo - 1));
                    mideDate = DateUtil.getDate(beginDate, 30 * pageNo + pageNo - 1);
                }
                // mideDate = DateUtil.getDate(beginDate, 30*pageNo);
            }
            // weekMid = new String[]{"1","2","3","4","5","6","7"};
        } else {
            Date[] ddd = DateUtil.getDateWithWeek(beginDate, endDate, week);
            if (31 > ddd.length) {
                mideDate = endDate;
                midbDate = beginDate;
                pageTotal = 1;
            } else {
                pageTotal = ddd.length / 30 + (0 < ddd.length % 30 ? 1 : 0);
                if (1 == pageNo) {
                    midbDate = beginDate;
                } else {
                    midbDate = ddd[30 * (pageNo - 1) + 1];
                }
                if (pageNo < pageTotal) {
                    mideDate = ddd[30 * pageNo];
                } else {
                    mideDate = ddd[ddd.length - 1];
                }
            }
        }
        dateList.add(midbDate);
        dateList.add(mideDate);
        return dateList;
    }

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
			HtlHotel hotel) {
		String lockedMSG = "";
		/** �?查酒店配额是否锁�? add by chenjiajie V2.4 2008-8-26 Begin* */
		if (0 < hotelId) {
			OrLockedRecords orLockedRecord = new OrLockedRecords();
			orLockedRecord.setRecordCD(String.valueOf(hotelId));
			orLockedRecord.setLockType(04);
			OrLockedRecords lockedRecords = lockedRecordService
					.loadLockedRecord(orLockedRecord);
			if (null != lockedRecords) { // 已锁
				String lockerName = lockedRecords.getLockerName();
				String lockerLoginName = lockedRecords.getLockerLoginName();
				if (!roleUser.getLoginName().equals(lockerLoginName)) { // 不是锁定人进�?
					lockedMSG = "此酒店释放操作已被锁定，在被解锁之前，只有锁定人才能进入(锁定人:" +

					lockerName + "[" + lockerLoginName + "])";
					// request.setAttribute("lockedMSG", lockedMSG);
					// return "lockedHint";
				}
			} else {
				if (null != roleUser) {
					orLockedRecord.setRemark(hotel.getChnName());
					orLockedRecord.setLockerName(roleUser.getName());
					orLockedRecord.setLockerLoginName(roleUser.getLoginName());
					orLockedRecord.setLockTime(DateUtil.getSystemDate());
					lockedRecordService.insertLockedRecord(orLockedRecord);
				}
			}
		}
		/** �?查酒店配额是否锁�? add by chenjiajie V2.4 2008-8-26 End* */
		return lockedMSG;
	}

	public List<HtlQuota> getQuotaList(HtlQuota quota, List<CodeName>
	lstMemberQuotaObj, HashMap<String, HtlAssignCustom> mapAssignCustom,
			List<Object> lstAssignCustom, String updateOrView) {

		List<HtlQuota> lstQuotasUpdate = new ArrayList<HtlQuota>();
		if (null != quota) {
			// 不能直接更新,要多数据中读出来，已页面的数据去更新配额数量
			for (CodeName cn : lstMemberQuotaObj) {
				boolean findFlag = false;
				// 取得数据库中的记录，如果发现就用页面的数据更�?
				List<HtlAssignCustom> list = quota.getLstAssign();
				for (HtlAssignCustom ac : list) {
					if (Integer.parseInt(cn.getCode()) == ac.getMemberType()) {
						findFlag = true;
						// 找到之后�?
						if (null != updateOrView) {
							HtlAssignCustom hac = (HtlAssignCustom) mapAssignCustom
									.get("" + ac.getQuotaId() + "_"
											+ ac.getMemberType());
							if (null != hac) {
								ac.setMaxAbleQuota(hac.getMaxAbleQuota());
								ac.setPrivateQuota(hac.getPrivateQuota());
							}
						}
						break;
					}
				}
				// 没有找到
				if (!findFlag) {
					HtlAssignCustom ac1 = new HtlAssignCustom();
					ac1.setQuotaId(quota.getID().longValue());
					ac1.setMemberType(Integer.parseInt(cn.getCode()));
					quota.getLstAssign().add(ac1);
					if (null != updateOrView) {
						for (int m = 0; m < lstAssignCustom.size(); m++) {
							// 页面传来的分配对�?
							HtlAssignCustom hac = (HtlAssignCustom) lstAssignCustom
									.get(m);
							if (hac.getQuotaId() == ac1.getQuotaId()
									&& hac.getMemberType() == ac1
											.getMemberType()) {
								ac1.setMaxAbleQuota(hac.getMaxAbleQuota());
								ac1.setPrivateQuota(hac.getPrivateQuota());
							}
						}
					} else {
						ac1.setMaxAbleQuota(-1);
						ac1.setPrivateQuota(0);
					}
				}
			}
			quota = this.freeAloneQuota(quota);
			lstQuotasUpdate.add(quota);
		}
		return lstQuotasUpdate;
	}

	@SuppressWarnings("unchecked")
	public Map updateQuotaAndFreeQuota(Map params, int rowCutNo, long hotelId,
			String updataOrView) {
		// 修复java.lang.ClassCastException: java.lang.String BUG。add by
		// shengwei.zuo hotel 2.6
		// 2009-05-25 begin
		String[] quotaIds = doObjecClassCastToStringArray(params.get("quotaId"));

		String[] freeQtys = doObjecClassCastToStringArray(params.get("freeQty"));
		// 修复java.lang.ClassCastException: java.lang.String BUG。add by
		// shengwei.zuo hotel 2.6
		// 2009-05-25 end

		List<Object> lstAssignCustom = MyBeanUtil.getBatchObjectFromParam(
				params, HtlAssignCustom.class, rowCutNo);
		// 会员类型列表

		// 把页面传来的参数放入hashmap，方便以后使�?
		HashMap<String, HtlAssignCustom> mapAssignCustom = new HashMap<String, HtlAssignCustom>

		(lstAssignCustom.size());
		for (int m = 0; m < lstAssignCustom.size(); m++) {
			HtlAssignCustom hac = (HtlAssignCustom) lstAssignCustom.get(m);
			mapAssignCustom.put("" + hac.getQuotaId() + "_"
					+ hac.getMemberType(), hac);
		}

		List<CodeName> lstMemberQuotaObj = BizRuleCheck
				.getCutMemberQuotaLevel();

		List<HtlQuota> lstQuotasUpdate = new ArrayList<HtlQuota>();
		/**
		 * 只所以加个if判断是因为：如果在查询时间段内没有任何记录�?�用户又点击了�?�确定�?�的情况下，将报
		 * 
		 * NullPointException错误 Modified by Wuyun
		 */
		if (null != quotaIds) {
			for (int i = 0; i < quotaIds.length; i++) {
				long quotaId = Long.parseLong(quotaIds[i]);
				int freeQty = StringUtil.getIntValue(freeQtys[i], 0);
				HtlQuota quota = htlQuotaDao.findHtlQuota(quotaId);
				if (null != quota) {
					quota.setFreeQty(freeQty);
					hotelId = quota.getHotelId();
				}
				lstQuotasUpdate = this.getQuotaList(quota, lstMemberQuotaObj,
						mapAssignCustom,

						lstAssignCustom, updataOrView);
			}
			if ("updateQuota".equals(updataOrView)) {
				this.freeAloneQuotaByDays(lstQuotasUpdate);
			}
		}
		Map map = new HashMap(2);
		map.put("hotelId", hotelId);
		map.put("lstQuotasUpdate", lstQuotasUpdate);
		return map;
	}

	/**
	 * 防止类型转换错误
	 * 
	 * @param obj
	 * @return
	 */
	private String[] doObjecClassCastToStringArray(Object obj) {
		String[] strArr = null;
		if (null != obj) {
			if (obj instanceof String) {
				strArr = new String[] { (String) obj };
			} else {
				strArr = (String[]) obj;
			}
		}
		return strArr;
	}

	public List<HtlTempQuota> getTempQuotaByHotelIdRoomId(long hotelId,
			long roomType, Date beginDate, Date endDate) {
		return htlQuotaDao.qryTempQuotaByHotelIdRoomId(hotelId, roomType,
				beginDate, endDate);
	}

	public void setHotelRoomService(HotelRoomService hotelRoomService) {
		this.hotelRoomService = hotelRoomService;
	}

	public void setHtlQuotaDao(HtlQuotaDao htlQuotaDao) {
		this.htlQuotaDao = htlQuotaDao;
	}
	
	/**
	 * 查询配额
	 */
	@SuppressWarnings("unchecked")
	public List<HtlQuota> getQuotaByMap(String[] week, long roomType,
			String quotaType, long hotelId, Date
			midbDate, Date mideDate) {
		List<HtlQuota> lstQuotas = null;
		if ((null == week) || (null != week && 7 == week.length)) {
			lstQuotas = htlQuotaDao.qryQuotaByRoomTypeQuotaType(hotelId,
					roomType, quotaType, midbDate, mideDate);
		} else {
			lstQuotas = htlQuotaDao.qryQuotaByRoomTypeQuotaTypeWithWeeks(
					hotelId, roomType, quotaType, midbDate, mideDate, week);
		}
		return lstQuotas;

	}
}

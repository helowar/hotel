package com.mangocity.hotel.base.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.manage.QuotaPriceManage;
import com.mangocity.hotel.base.persistence.HtlCutoffDayQuota;
import com.mangocity.hotel.base.persistence.HtlQuota;
import com.mangocity.hotel.base.persistence.HtlQuotabatch;
import com.mangocity.hotel.base.util.BizRuleCheck;
import com.mangocity.hotel.base.web.webwork.GenericAction;
import com.mangocity.util.DateUtil;
import com.mangocity.util.ValidationUtil;
import com.mangocity.util.bean.MyBeanUtil;

/**
 */
public class AdjustQuotaBatchAction extends GenericAction {
	
	private Long entityID;

    /**
     * 配额批次
     */
    private long batchQuotaId;

    /**
     * 调整开始日期
     */
    private Date beginDate;

    /**
     * 调整结束日期
     */
    private Date endDate;

    /**
     * 将要调整的开始日期到结束日期之间的星期
     */
    private String[] week;

    private long roomType;

    private String quotaType;

    private String shareType;

    private long hotelId;

    private long contractId;

    private String QUOTABYDATE = "queryQuotaByDate";

    /**
     * 要更新的行
     */
    private int rowCutNo;

    private QuotaPriceManage quotaPriceManage;

    public String queryQuotaByDate() {// parasoft-suppress NAMING.GETA "STRUTS命名没有问题"
        return QUOTABYDATE;
    }

    /**
     * 查询配额时，取得查询条件，进行查询 ，返回修改配额界面进行调整
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public String queryQuota() {
        List<HtlQuota> lstQuotas = getQuotaListByBatchIdSaleDate(batchQuotaId, beginDate, endDate, week);
        super.getParams().put("beginDate", beginDate);
        super.getParams().put("endDate", endDate);
        super.getParams().put("lstQuotas", lstQuotas);
        super.getParams().put("week", week);
        // 传一参数，表示是初始化
        super.getParams().put("ISINIT", "NO");
        return "queryQuota";
    }

	private List<HtlQuota> getQuotaListByBatchIdSaleDate(long batchId, Date beginDate, Date endDate, String[] weeks) {
		List<HtlQuota> lstQuotas = null;
        if (BizRuleCheck.isAllWeek(weeks)) {
            lstQuotas = quotaPriceManage.getQuotaInWeekdaysByBatchIdSaleDate(batchId, beginDate, endDate, null);
        } else {
            lstQuotas = quotaPriceManage.getQuotaInWeekdaysByBatchIdSaleDate(batchId, beginDate, endDate, weeks);
        }
        
		return lstQuotas;
	}

    /**
     * 要横向显示配额总数
     * 
     * @return
     */
	@SuppressWarnings("unchecked")
    public String queryQuotaTotal() {
        List<HtlQuota> lstQuotas = getQuotaListByBatchIdSaleDate(batchQuotaId, beginDate, endDate, week);
        // 面付独占
        Map<String, HtlQuota> mapFP = new HashMap<String, HtlQuota>();

        // 预付独占
        Map<String, HtlQuota> mapPY = new HashMap<String, HtlQuota>();

        // 共享
        Map<String, HtlQuota> mapSH = new HashMap<String, HtlQuota>();

        // 找到没有面付独占的配额
        boolean findMapFP = false;

        // 找到没有预付独占的配额
        boolean findMapPY = false;

        // 找到没有共享的配额
        boolean findMapSH = false;

        for (HtlQuota qa : lstQuotas) {
            if (BizRuleCheck.isPrepayQuota(qa.getShareType())) {
                mapPY.put(DateUtil.formatDateToSQLString(qa.getAbleSaleDate()), qa);
                findMapPY = true;
            } else if (BizRuleCheck.isFaceToPayQuota(qa.getShareType())) {
                mapFP.put(DateUtil.formatDateToSQLString(qa.getAbleSaleDate()), qa);
                findMapFP = true;
            } else if (BizRuleCheck.isQuotaShare(qa.getShareType())) {
                mapSH.put(DateUtil.formatDateToSQLString(qa.getAbleSaleDate()), qa);
                findMapSH = true;
            }
        }
        
        List<HtlQuota> lstPreQuotas = new ArrayList<HtlQuota>();// 预付        
        List<HtlQuota> lstFaceQuotas = new ArrayList<HtlQuota>();// 面付        
        List<HtlQuota> lstShareQuotas = new ArrayList<HtlQuota>();// 共享
        List<String> lstDate = new ArrayList<String>();

        Calendar calStart = Calendar.getInstance();
        calStart.setTime(beginDate);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(endDate);
        if (beginDate.before(endDate)) {
        	String strEndDate = DateUtil.dateToString(DateUtil.getDate(endDate, 1));
            while (true) {                
                lstDate.add(DateUtil.formatDateToMMDD(calStart.getTime()));
                // 先要查找，如果没有找到要补0，只做显示用，不能更新数据库
                if (findMapFP) {// 面付
                    checkAndAppendEmptyQuota(mapFP, lstFaceQuotas, calStart);
                }
                if (findMapPY) {// 预付
                    checkAndAppendEmptyQuota(mapPY, lstPreQuotas, calStart);
                }
                if (findMapSH) {// 共享
                    checkAndAppendEmptyQuota(mapSH, lstShareQuotas, calStart);
                }
                
                calStart.add(Calendar.DATE, 1);
                String strCurrDate = DateUtil.dateToString(calStart.getTime());
                if (strCurrDate.equals(strEndDate)) {
                    break;
                }
            }
        }
        super.getParams().put("beginDate", beginDate);
        super.getParams().put("endDate", endDate);
        super.getParams().put("lstPreQuotasSize", lstPreQuotas.size());
        super.getParams().put("lstPreQuotas", lstPreQuotas);

        super.getParams().put("lstFaceQuotasSize", lstFaceQuotas.size());
        super.getParams().put("lstFaceQuotas", lstFaceQuotas);

        super.getParams().put("lstShareQuotas", lstShareQuotas);
        super.getParams().put("lstShareQuotasSize", lstShareQuotas.size());
        super.getParams().put("lstDate", lstDate);
        super.getParams().put("week", week);
        // 传一参数，表示是初始化
        super.getParams().put("ISINIT", "OTHER");
        return "queryQuota";
    }

	private void checkAndAppendEmptyQuota(Map<String, HtlQuota> mapFP,
			List<HtlQuota> lstFaceQuotas, Calendar calStart) {
		String starTime = DateUtil.formatDateToSQLString(calStart.getTime());
		HtlQuota q = mapFP.get(starTime);
		if (null == q) {
		    q = new HtlQuota();
		    q.setAbleSaleDate(calStart.getTime());
		    q.setAbleQty(0);
		    q.setUsedQty(0);// 已用
		    q.setAvailQty(0);// 未使用，
		    q.setFreeQty(0);// 已释放数
		    q.setTotalQty(0);// 总数
		}
		lstFaceQuotas.add(q);
	}

    /**
     * 查询一天的配额进行调整，把共享的拆成独占，把独占的合并共享，事实就是重新输入一条记录，删除原来的记录
     * 
     * @return
     */
	@SuppressWarnings("unchecked")
    public String queryOneDayQuota() {
        String bDate = (String) super.getParams().get("beginDate");
        String eDate = (String) super.getParams().get("endDate");
        List<HtlQuota> lstQuotas = quotaPriceManage.getQuotaInWeekdaysByBatchIdSaleDate(batchQuotaId, 
        		DateUtil.getDate(bDate), DateUtil.getDate(eDate), null);

        super.getParams().put("lstQuotas", lstQuotas);
        return "queryOneDayQuota";
    }

    /**
     * 进入配额查询界面
     * 
     * @return
     */
	@SuppressWarnings("unchecked")
    public String intoQueryQuotaFace() {
        HtlQuotabatch quotabatch = quotaPriceManage.queryHtlQuotabatch(batchQuotaId);
        if (null != quotabatch) {
            // 传一参数，表示是初始化
            super.getParams().put("hotelId", quotabatch.getHotelId());
            super.getParams().put("roomType", quotabatch.getRoomType());
            super.getParams().put("shareType", quotabatch.getShareType());
            super.getParams().put("quotaType", quotabatch.getQuotaType());
        }
        super.getParams().put("ISINIT", "YES");
        return "intoQueryQuotaFace";
    }

    /**
     * 更新cutoffDay及配额
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public String updateQuota() {
        Map params = super.getParams();
        String[] quotaIds = (String[]) params.get("quotaId");
        if (ValidationUtil.isEmpty(quotaIds)) {
        	return "updateQuota";
        }
        // 取得页面传递的cutoff Day并放到hashMap中
        List lstCutoffDay = MyBeanUtil.getBatchObjectFromParam(params, HtlCutoffDayQuota.class, rowCutNo);
        Map<Long, HtlCutoffDayQuota> mapCutoffDay = new HashMap<Long, HtlCutoffDayQuota>(lstCutoffDay.size());
        for (int k = 0; k < lstCutoffDay.size(); k++) {
            HtlCutoffDayQuota cq = (HtlCutoffDayQuota) lstCutoffDay.get(k);
            mapCutoffDay.put(cq.getID(), cq);
        }
        
    	List<HtlQuota> lstQuotasUpdate = new ArrayList<HtlQuota>(quotaIds.length);        	
        for (String strQuotaId : quotaIds) {
            long quotaId = Long.parseLong(strQuotaId);
            String takebackQuota = (String) params.get("takebackQuota_" + quotaId);
            boolean btq = false;
            if ("false".equals(takebackQuota)) {
                btq = false;
            } else if ("true".equals(takebackQuota)) {
                btq = true;
            }
                
            HtlQuota quota = quotaPriceManage.findHtlQuota(quotaId);
            if (null != quota) {
                // 不能直接更新,要多数据中读出来，已页面的数据去更新配额数量
                for (int j = 0; j < quota.getLstCutOffDay().size(); j++) {
                    HtlCutoffDayQuota hcq = (HtlCutoffDayQuota) quota.getLstCutOffDay().get(j);
                    HtlCutoffDayQuota cq = (HtlCutoffDayQuota) mapCutoffDay.get(hcq.getID());
                    hcq.setQuotaQty(cq.getQuotaQty());
                    hcq.setCutoffDay(cq.getCutoffDay());
                }
                quota = quotaPriceManage.calcQuota(quota);
                quota.setTakebackQuota(btq);
                lstQuotasUpdate.add(quota);
                contractId = quota.getContractId();
            }
        }

        quotaPriceManage.batchSaveOrUpdateQuota(lstQuotasUpdate);            
        setEntityID(contractId);// 为了跳回原合同修改页面,设定原合同ID;
        
        return "updateQuota";
    }

    public long getBatchQuotaId() {
        return batchQuotaId;
    }

    public void setBatchQuotaId(long batchQuotaId) {
        this.batchQuotaId = batchQuotaId;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String[] getWeek() {
        return week;
    }

    public void setWeek(String[] week) {
        this.week = week;
    }

    public int getRowCutNo() {
        return rowCutNo;
    }

    public void setRowCutNo(int rowCutNo) {
        this.rowCutNo = rowCutNo;
    }

    public QuotaPriceManage getQuotaPriceManage() {
        return quotaPriceManage;
    }

    public void setQuotaPriceManage(QuotaPriceManage quotaPriceManage) {
        this.quotaPriceManage = quotaPriceManage;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    public long getRoomType() {
        return roomType;
    }

    public void setRoomType(long roomType) {
        this.roomType = roomType;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public String getQUOTABYDATE() {
        return QUOTABYDATE;
    }

    public void setQUOTABYDATE(String quotabydate) {
        QUOTABYDATE = quotabydate;
    }

    public long getContractId() {
        return contractId;
    }

    public void setContractId(long contractId) {
        this.contractId = contractId;
    }

	public Long getEntityID() {
		return entityID;
	}

	public void setEntityID(Long entityID) {
		this.entityID = entityID;
	}

}

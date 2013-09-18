package com.mangocity.hotel.base.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.manage.QuotaPriceManage;
import com.mangocity.hotel.base.persistence.HtlQuotabatch;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.service.HotelRoomService;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.base.util.BizRuleCheck;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;

/**
 */
public class AdjustQuotaByCalendarExAction extends PersistenceAction {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

    private String weeks;

    private long roomType;

    private String quotaType;

    private String shareType;

    private long hotelId;

    private String bD;

    private String eD;

    private long contractId;

    private static final String QUOTABYDATE = "queryQuotaByDate";

    private HtlRoomtype htlRoomtype;

    private String weekStr;

    /**
     * 要更新的行
     */
    private int rowCutNo;

    private String gotofrom;

    private QuotaPriceManage quotaPriceManage;
    
    private HotelRoomTypeService hotelRoomTypeService;
    
    private HotelRoomService hotelRoomService;

    private boolean saveValue;

    private boolean reviewSign;

    private int pageNo = 1;

    private int pageTotal = 0;

    private boolean oneQuery;

    private String reforward;

    // 每页显示条数
    private int pageNum;

    public String getReforward() {
        return reforward;
    }

    public void setReforward(String reforward) {
        this.reforward = reforward;
    }

    public boolean isOneQuery() {
        return oneQuery;
    }

    public void setOneQuery(boolean oneQuery) {
        this.oneQuery = oneQuery;
    }

    public String queryQuotaByDate() {// parasoft-suppress NAMING.GETA "STRUTS命名没有问题"
        return QUOTABYDATE;
    }

    /**
     * 要横向显示配额总数
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
	public String queryQuotaTotal() {
    	Map<String,List<?>> quotaMap = quotaPriceManage.buildHtlQuotaForView(beginDate, endDate, weeks, roomType, quotaType, hotelId);  
        
        Map reqMap = super.getParams();
        reqMap.put("beginDate", beginDate);
        reqMap.put("endDate", endDate);
        
        // 预付
        List<?> lstPreQuotas = quotaMap.get("lstPreQuotas");
        reqMap.put("lstPreQuotasSize", lstPreQuotas.size());
        reqMap.put("lstPreQuotas", lstPreQuotas);
        
        // 面付
        List<?> lstFaceQuotas = quotaMap.get("lstFaceQuotas");
        reqMap.put("lstFaceQuotasSize", lstFaceQuotas.size());
        reqMap.put("lstFaceQuotas", lstFaceQuotas);
        
        // 共享
        List<?> lstShareQuotas = quotaMap.get("lstShareQuotas");
        reqMap.put("lstShareQuotas", lstShareQuotas);
        reqMap.put("lstShareQuotasSize", lstShareQuotas.size());
        
        reqMap.put("lstDate", quotaMap.get("lstDate"));
        reqMap.put("week", week);
        reqMap.put("weeks", BizRuleCheck.ArrayToString(week));
        // 传一参数，表示是初始化
        reqMap.put("ISINIT", "OTHER");
        
        // 增加呼出配额显示
        List<?> lstOutSideQuota = hotelRoomService.getHtlRoomByHotelIdRoomType(roomType, hotelId, beginDate, endDate);
        reqMap.put("lstOutSideQuota", lstOutSideQuota);
        
        // 增加临时配额显示
        List<?> lstTempQuota = quotaPriceManage.getTempQuotaByHotelIdRoomId(hotelId, roomType, beginDate, endDate);
        reqMap.put("lstTempQuota", lstTempQuota);
        
        if (BizRuleCheck.isAllWeek(week)) {
            week = new String[] { "1", "2", "3", "4", "5", "6", "7" };
        }
        weekStr = BizRuleCheck.weekToString(week);
        
        // 传房型及星期到下个页面显示 add by zhineng.zhuang
        htlRoomtype = hotelRoomTypeService.getHtlRoomTypeByRoomTypeId(roomType);
        return "queryQuota";
    }
    /**
     * 要横向显示配额总数
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
	public String queryQuotaTotalView() {
    	Map<String,List<?>> quotaMap = quotaPriceManage.buildHtlQuotaForView(beginDate, endDate, weeks, roomType, quotaType, hotelId);

        super.getParams().put("beginDate", beginDate);
        super.getParams().put("endDate", endDate);
        
        // 预付
        List<?> lstPreQuotas = quotaMap.get("lstPreQuotas");
        super.getParams().put("lstPreQuotasSize", lstPreQuotas.size());
        super.getParams().put("lstPreQuotas", lstPreQuotas);

        // 面付
        List<?> lstFaceQuotas = quotaMap.get("lstFaceQuotas");
        super.getParams().put("lstFaceQuotasSize", lstFaceQuotas.size());
        super.getParams().put("lstFaceQuotas", lstFaceQuotas);

        // 共享
        List<?> lstShareQuotas = quotaMap.get("lstShareQuotas");
        super.getParams().put("lstShareQuotas", lstShareQuotas);
        super.getParams().put("lstShareQuotasSize", lstShareQuotas.size());
        
        super.getParams().put("lstDate", quotaMap.get("lstDate"));
        super.getParams().put("week", week);
        // 传一参数，表示是初始化
        super.getParams().put("ISINIT", "OTHER");
        return "queryQuotaView";
    }

    /**
     * 查询配额时，取得查询条件，进行查询 ，返回修改配额界面进行调整
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
	public String queryQuota() {
        /*
         * 每次查询30天数据，实现翻页功能
         */
        if (oneQuery) {
            for (int i = 0; 100 > i; i++) {
                String pageStrS = "No" + i;
                super.getSession().remove(pageStrS);
            }
        }
    	List<Date> dateList = quotaPriceManage.getDateForNextThirtyDays(weeks,week,beginDate,endDate,pageTotal,pageNo);
        Date mideDate = dateList.get(1);
        Date midbDate = dateList.get(0);
        String pageStr = "No" + pageNo;
        // 判断是否需要写数据库
        if (saveValue) {
            Map params = super.getParams();
            Map map = quotaPriceManage.updateCutOffDayAndBuildQuotaList(params,rowCutNo, false,0,0);
            List lstQuotasUpdate = (ArrayList)map.get("lstQuotasUpdate");
            // 保存当前修改数据入session
            if ("go".equals(gotofrom)) {
                pageStr = "No" + (pageNo - 1);
            } else if ("back".equals(gotofrom)) {
                pageStr = "No" + (pageNo + 1);
            }
            super.putSession(pageStr, lstQuotasUpdate);
        }

        pageStr = "No" + (pageNo);
        List lstQuotas = quotaPriceManage.getQuotaByMap(super.getSession(), pageStr, weeks, roomType, quotaType, hotelId, midbDate, mideDate);
        pageNum = lstQuotas.size();
        super.getParams().put("beginDate", beginDate);
        super.getParams().put("endDate", endDate);
        super.getParams().put("lstQuotas", lstQuotas);
        super.getParams().put("week", week);
        super.getParams().put("weeks", BizRuleCheck.ArrayToString(week));
        super.getParams().put("hotelId", hotelId);
        super.getParams().put("contractId", contractId);
        // 传一参数，表示是初始化
        super.getParams().put("ISINIT", "NO");
        htlRoomtype = hotelRoomTypeService.getHtlRoomTypeByRoomTypeId(roomType);
        if (BizRuleCheck.isAllWeek(week)) {
            week = new String[] { "1", "2", "3", "4", "5", "6", "7" };
        }
        weekStr = BizRuleCheck.weekToString(week);
        return "queryQuota";
    }

    /**
     * 查询一天的配额进行调整，把共享的拆成独占，把独占的合并共享，事实就是重新输入一条记录，删除原来的记录
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
	public String queryOneDayQuota() {
        super.getParams().get("beginDate");
        super.getParams().get("endDate");

        List lstQuotas = quotaPriceManage.getQuotaByRoomTypeQuotaType(hotelId, roomType, quotaType, beginDate, endDate);

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
        super.getParams().put("ISINIT", "YES");
        return "intoQueryQuotaFace";
    }

    /**
     * 进入按日历配额查询界面
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
	public String intoViewQueryQuotaFace() {
        super.getParams().put("ISINIT", "YES");
        return "intoViewQueryQuotaFace";
    }

    /**
     * 更新cutoffDay及配额
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
	public String updateQuota() {
        Map params = super.getParams();
        Map map = quotaPriceManage.updateCutOffDayAndBuildQuotaList(params,rowCutNo, false,0,0);
        List lstQuotasUpdate = (ArrayList)map.get("lstQuotasUpdate");
        long  contractId = (Long)map.get("contractId");
        super.getEntityManager().saveOrUpdateAll(lstQuotasUpdate);
        // 为了跳回原合同修改页面,设定原合同ID;
        this.setEntityID(contractId);
        return "updateQuota";
    }

    @SuppressWarnings("unchecked")
	public String review() {
        /*
         * 每次查询30天数据，实现翻页功能
         */
    	List<Date> dateList = quotaPriceManage.getDateForNextThirtyDays(weeks,week,beginDate,endDate,pageTotal,pageNo);
        Date mideDate = dateList.get(1);
        Date midbDate = dateList.get(0);
        String pageStr = "No" + pageNo;
        // 判断是否需要写数据库
        if (reviewSign) {
            Map params = super.getParams();
            Map map = quotaPriceManage.updateCutOffDayAndBuildQuotaList(params,rowCutNo, true,roomType,hotelId);
            List lstQuotasUpdate = (ArrayList)map.get("lstQuotasUpdate");
            // 保存当前修改数据入session
            if ("go".equals(gotofrom)) {
                pageStr = "No" + (pageNo - 1);
            } else if ("back".equals(gotofrom)) {
                pageStr = "No" + (pageNo + 1);
            }
            super.putSession(pageStr, lstQuotasUpdate);
        }

        if (reviewSign) {
        	pageNo = 1;
        }
        pageStr = "No" + (pageNo);
        List lstQuotas = quotaPriceManage.getQuotaByMap(super.getSession(), pageStr, weeks, roomType, quotaType, hotelId, midbDate, mideDate);
        super.getParams().put("beginDate", beginDate);
        super.getParams().put("endDate", endDate);
        super.getParams().put("lstQuotas", lstQuotas);
        super.getParams().put("week", week);
        super.getParams().put("weeks", BizRuleCheck.ArrayToString(week));
        super.getParams().put("hotelId", hotelId);
        super.getParams().put("contractId", contractId);
        // 传一参数，表示是初始化
        super.getParams().put("ISINIT", "NO");
        htlRoomtype = hotelRoomTypeService.getHtlRoomTypeByRoomTypeId(roomType);
        if (BizRuleCheck.isAllWeek(week)) {
            week = new String[] { "1", "2", "3", "4", "5", "6", "7" };
        }
        weekStr = BizRuleCheck.weekToString(week);
        return "review";
    }

    /**
     * 更新cutoffDay及配额
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
	public String updateQuotaForDay() {
        List lstQuotasUpdate = new ArrayList(pageTotal);
        for (int i = 1; i <= pageTotal; i++) {
            String pageStrS = "No" + i;
            List o = (ArrayList) super.getSession().get(pageStrS);
            if (null != o && !o.isEmpty()) {
                // if(i==1){
                lstQuotasUpdate.addAll(o);
                // }
            }
        }
        super.getEntityManager().saveOrUpdateAll(lstQuotasUpdate);
        String forward = null;
        if ("contract".equals(reforward)) {
            forward = "contract";
        }
        if ("freeQuota".equals(reforward)) {
            forward = "freeQuota";
        }
        return forward;
    }
    
    
    
    @SuppressWarnings("unchecked")
	protected Class getEntityClass() {
        return HtlQuotabatch.class;
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

    public String getBD() {
        return bD;
    }

    public void setBD(String bD) {
        this.bD = bD;
    }

    public String getED() {
        return eD;
    }

    public void setED(String eD) {
        this.eD = eD;
    }

    public long getContractId() {
        return contractId;
    }

    public void setContractId(long contractId) {
        this.contractId = contractId;
    }

    /**
     * 返回:
     * 
     * @return the weeks
     */
    public String getWeeks() {
        return this.weeks;
    }

    /**
     * 设置:
     * 
     * @param weeks
     *            the weeks to set
     */
    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }

    public HtlRoomtype getHtlRoomtype() {
        return htlRoomtype;
    }

    public void setHtlRoomtype(HtlRoomtype htlRoomtype) {
        this.htlRoomtype = htlRoomtype;
    }

    public String getWeekStr() {
        return weekStr;
    }

    public void setWeekStr(String weekStr) {
        this.weekStr = weekStr;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public boolean isSaveValue() {
        return saveValue;
    }

    public void setSaveValue(boolean saveValue) {
        this.saveValue = saveValue;
    }

    public String getGotofrom() {
        return gotofrom;
    }

    public void setGotofrom(String gotofrom) {
        this.gotofrom = gotofrom;
    }

    public boolean isReviewSign() {
        return reviewSign;
    }

    public void setReviewSign(boolean reviewSign) {
        this.reviewSign = reviewSign;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}

	public void setHotelRoomService(HotelRoomService hotelRoomService) {
		this.hotelRoomService = hotelRoomService;
	}
}

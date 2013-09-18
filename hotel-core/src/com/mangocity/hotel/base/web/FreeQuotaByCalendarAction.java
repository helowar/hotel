package com.mangocity.hotel.base.web;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.manage.QuotaPriceManage;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlQuota;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.base.service.ILockedRecordService;
import com.mangocity.hotel.base.util.BizRuleCheck;
import com.mangocity.hotel.base.util.CodeName;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.DateUtil;

/**
 * 根据日期释放配额Action
 * 2011.1.14 第一次代码重构 by xiaowei.wang
 */
public class FreeQuotaByCalendarAction extends PersistenceAction {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 配额批次
     */
    private long quotaBatchId;

    /**
     * 酒店ID
     */
    private long hotelId;

    /**
     * 接收到的已�?�星�?
     */
    private String[] week;

    /**
     * 接收到的�?始日�?
     */
    private Date beginDate;

    /**
     * 接收到的结束日期
     */
    private Date endDate;


    private int rowCutNo;

    private QuotaPriceManage quotaPriceManage;
    
    private HotelRoomTypeService hotelRoomTypeService;

    /**
     * 合同id
     */
    private long contractId;

    /**
     * 共享方式
     */
    private String shareType;

    /**
     * 配额类型
     */
    private String quotaType;

    /**
     * 房型
     */
    private long roomType;

    /**
     * 合同�?始时�?
     */
    private String bD;

    /**
     * 合同结束时间
     */
    private String eD;

    /**
     * 酒店名称
     */
    private String chnName;

    private HtlRoomtype htlRoomtype;

    private String weekStr;

    // 从查询结果传的共享方�?
    private String shareTypeForReView;

    // 从查询结果传的星�?
    private String weekForReView;

    // 酒店，合同，房�?�，配额等加解锁操作接口
    private ILockedRecordService lockedRecordService;

    /**
     * 初始化查询界�?
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
	public String initForm() {
        HtlHotel hotel = (HtlHotel) super.getEntityManager().find(HtlHotel.class, hotelId);
        String lockedMSG = quotaPriceManage.queryQuotaIsLocked(hotelId, super
                .getOnlineRoleUser(), hotel);
        if(!("".equals(lockedMSG))){
        	 request.setAttribute("lockedMSG", lockedMSG);
             return "lockedHint";
        }
        super.getParams().put("hotel", hotel);
        String contractBd = (String) super.getParams().get("bD");
        String contractEd = (String) super.getParams().get("eD");
        super.getParams().put("contractBd", contractBd);
        super.getParams().put("contractEd", contractEd);
        super.getParams().put("ISINIT", "YES");
        return "initForm";
    }

    /**
     * 查询配额，显示查询结�?
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
	public String queryQuota() {
        List<HtlQuota> lstQuotas = quotaPriceManage.getQuotaByMap(week, roomType, quotaType, hotelId, 
        		beginDate, endDate);
        List<CodeName> lstMemberQuotaObj = BizRuleCheck.getCutMemberQuotaLevel();
        if (null != lstQuotas) {
            for (HtlQuota quota: lstQuotas) {
                Date nowDateSys = DateUtil.getSystemDate();
                Date nowDate = DateUtil.getDate(nowDateSys, -1);
                Date ableSaleDateCom = quota.getAbleSaleDate();
                quota.setWhetherPass(0);
                if (ableSaleDateCom.getTime() < nowDate.getTime()) {
                    quota.setWhetherPass(1);
                }
                quotaPriceManage.getQuotaList(quota, lstMemberQuotaObj,null, null,null);
            }
        }
        super.getParams().put("hotelId", hotelId);
        super.getParams().put("contractId", contractId);
        super.getParams().put("chnName", chnName);
        super.getParams().put("bD", bD);
        super.getParams().put("eD", eD);
        String contractBd = DateUtil.dateToString(beginDate);
        String contractEd = DateUtil.dateToString(endDate);
        super.getParams().put("contractBd", contractBd);
        super.getParams().put("contractEd", contractEd);
        super.getParams().put("lstQuotas", lstQuotas);
        super.getParams().put("week", week);
        // 传一参数，表示是初始�?
        super.getParams().put("ISINIT", "NO");
        return "queryQuota";
    }

    /**
     * 更新配额，按天释放配�?
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
	public String updateQuota() {
        Map params = super.getParams();

        Map map=quotaPriceManage.updateQuotaAndFreeQuota(params, rowCutNo, hotelId, "updateQuota");
        Object hotelId_obj= map.get("hotelId");
        hotelId = (Long)hotelId_obj;
        super.getSession().remove("lstQuotasUpdate");
        // 解除配额�?
        Integer lockType = Integer.valueOf("04");
        lockedRecordService.deleteLockedRecordTwo(String.valueOf(hotelId), lockType, super
            .getOnlineRoleUser().getLoginName());
        return "updateQuota";
    }

    @SuppressWarnings("unchecked")
	public String freeQuotaByCanlenReView() {
        Map params = super.getParams();
        Map map= quotaPriceManage.updateQuotaAndFreeQuota(params, rowCutNo, hotelId, "view");
        Object hotelId_obj= map.get("hotelId");
        hotelId = (Long)hotelId_obj;
        Object lstQuotasUpdate_obj = map.get("lstQuotasUpdate");
        List<HtlQuota> lstQuotasUpdate = (List<HtlQuota>)lstQuotasUpdate_obj;
        
        htlRoomtype = hotelRoomTypeService.getHtlRoomTypeByRoomTypeId(roomType);

        super.getParams().put("shareTypeForReView", shareTypeForReView);
        super.getParams().put("lstQuotas", lstQuotasUpdate);
        super.putSession("lstQuotasUpdate", lstQuotasUpdate);

        String[] weekForReView2 = null;

        if (null != weekForReView && !("").equals(weekForReView)) {
            weekForReView2 = weekForReView.split(",");
        }

        if (BizRuleCheck.isAllWeek(weekForReView2)) {
            weekForReView2 = new String[] { "1", "2", "3", "4", "5", "6", "7" };
        }
        weekStr = BizRuleCheck.weekToString(weekForReView2);
        return "freeReView";
    }

    @SuppressWarnings("unchecked")
	public String cancBack() {
        super.getSession().remove("lstQuotasUpdate");
        super.getParams().put("ISINIT", "NO");
        return "initForm";
    }

    public long getQuotaBatchId() {
        return quotaBatchId;
    }

    public void setQuotaBatchId(long quotaBatchId) {
        this.quotaBatchId = quotaBatchId;
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

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public long getContractId() {
        return contractId;
    }

    public void setContractId(long contractId) {
        this.contractId = contractId;
    }

    public long getRoomType() {
        return roomType;
    }

    public void setRoomType(long roomType) {
        this.roomType = roomType;
    }

    public String getBD() {
        return bD;
    }

    public void setBD(String bd) {
        bD = bd;
    }

    public String getChnName() {
        return chnName;
    }

    public void setChnName(String chnName) {
        this.chnName = chnName;
    }

    public String getED() {
        return eD;
    }

    public void setED(String ed) {
        eD = ed;
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

    public String getShareTypeForReView() {
        return shareTypeForReView;
    }

    public void setShareTypeForReView(String shareTypeForReView) {
        this.shareTypeForReView = shareTypeForReView;
    }

    public String getWeekForReView() {
        return weekForReView;
    }

    public void setWeekForReView(String weekForReView) {
        this.weekForReView = weekForReView;
    }

    public ILockedRecordService getLockedRecordService() {
        return lockedRecordService;
    }

    public void setLockedRecordService(ILockedRecordService lockedRecordService) {
        this.lockedRecordService = lockedRecordService;
    }

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}
    
}

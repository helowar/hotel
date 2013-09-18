package com.mangocity.hotel.base.web;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.dao.ISaleDao;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.manage.QuotaPriceManage;
import com.mangocity.hotel.base.manage.assistant.InputCutoffDay;
import com.mangocity.hotel.base.persistence.HtlBatchCutoffDay;
import com.mangocity.hotel.base.persistence.HtlQuotabatch;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.base.service.ILockedRecordService;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.MyBeanUtil;

/**
 */
public class QuotabatchAction extends PersistenceAction {

    private long batchQuotaId;

    /**
     * 配额批次开始日期
     */
    private Date beginDate;

    /**
     * 配额批次结束日期
     */
    private Date endDate;

    /**
     * 合同开始日期
     */
    private String bD;

    /**
     * 合同结束日期
     */
    private String eD;

    private String hotelId;

    private String contractId;

    /**
     * 合同付款方法
     */
    private String shareType;

    private String[] week;

    private ISaleDao saleDao;

    private int rowCutNo;

    private long roomType;

    /**
     * cutoff行数
     */
    private int cutoffDayNum;

    private List lstInputCDay = new ArrayList();

    private List lstRoomType = new ArrayList();

    private List roomTypePriceTypeLis = new ArrayList();

    private HotelManage hotelManage;

    private String returnPage;

    private int rowCount;

    /**
	 * 
	 */
    // 酒店，合同，房态，配额等加解锁操作接口
    private ILockedRecordService lockedRecordService;

    private QuotaPriceManage quotaPriceManage;
    
    private HotelRoomTypeService hotelRoomTypeService;

    private static final long serialVersionUID = -2006434663457082302L;

    private HtlRoomtype htlRoomtype = new HtlRoomtype();

    private List contractCufDayList = new ArrayList();

    private List contractCufDayAllList = new ArrayList();

    // 合同的付款方式
    private String contractPay;

    protected Class getEntityClass() {
        return HtlQuotabatch.class;
    }
    
    /**
     * 返回酒店基本信息查询页面，并解锁
     * 
     * @return
     */
    public String backToList() {
        Map params = super.getParams();
        String hotelId = (String) params.get("hotelId");
        /** 解除酒店合同锁定 add by chenjiajie V2.4 2008-8-26 Begin* */
        lockedRecordService.deleteLockedRecord(hotelId, 03);
        /** 解除酒店合同锁定 add by chenjiajie V2.4 2008-8-26 End* */
        return "queryhotel";
    }

    public String save() throws SQLException {
        Map params = super.getParams();
        HtlQuotabatch quotabatch = (HtlQuotabatch) super.populateEntity();
        quotabatch.setContractId(Long.valueOf((String) params.get("contractId")));
        List lstInputCutoffDay = MyBeanUtil.getBatchObjectFromParam(params, InputCutoffDay.class,
            cutoffDayNum);
        Date sysDate = DateUtil.getSystemDate();
        Long.valueOf((String) params.get("contractId"));
        Map<String, HtlQuotabatch> map = new HashMap<String, HtlQuotabatch>();
        /**
         *不同房型的数据用不同的HtlQuotabatch封装
         */
        for (int i = 0; i < lstInputCutoffDay.size(); i++) {
            InputCutoffDay icd = (InputCutoffDay) lstInputCutoffDay.get(i);
            HtlQuotabatch tmpQuotabatch = new HtlQuotabatch();
            tmpQuotabatch.setID(quotabatch.getID());
            tmpQuotabatch.setBeginDate(quotabatch.getBeginDate());
            tmpQuotabatch.setEndDate(quotabatch.getEndDate());
            tmpQuotabatch.setContractId(quotabatch.getContractId());
            tmpQuotabatch.setHotelId(quotabatch.getHotelId());
            tmpQuotabatch.setQuotaType(quotabatch.getQuotaType());
            tmpQuotabatch.setShareType(quotabatch.getShareType());
            tmpQuotabatch.setTakebackQuota(quotabatch.isTakebackQuota());
            map.put(icd.getRoomTypeId(), tmpQuotabatch);
        }

        /**
         * 设置到不同的map的key中
         */
        for (int i = 0; i < lstInputCutoffDay.size(); i++) {
            InputCutoffDay icd = (InputCutoffDay) lstInputCutoffDay.get(i);
            HtlQuotabatch htlQuotabatch = (HtlQuotabatch) map.get(icd.getRoomTypeId());
            HtlBatchCutoffDay bcd = new HtlBatchCutoffDay();
            bcd.setAdjustWeek(icd.getWeeks());
            bcd.setBeginDate(icd.getCutoffBeginDate());
            bcd.setEndDate(icd.getCutoffEndDate());
            bcd.setCutoffDay(icd.getCutoffDay());
            bcd.setCutoffTime(icd.getCutoffTime());
            bcd.setQuantity(icd.getQuotaQty());
            bcd.setID(icd.getCutoffDayID());
            bcd.setQuotabatch(htlQuotabatch);
            if (null != super.getOnlineRoleUser())
                bcd.setCreateBy(super.getOnlineRoleUser().getName());
            bcd.setCreateTime(sysDate);
            htlQuotabatch.getHtlBatchCutoffDays().add(bcd);
            htlQuotabatch.setRoomType(Long.valueOf(icd.getRoomTypeId()));
            if (null != super.getOnlineRoleUser())
                htlQuotabatch.setCreateBy(super.getOnlineRoleUser().getName());
            htlQuotabatch.setCreateTime(sysDate);
            htlQuotabatch.setModifyBy(super.getOnlineRoleUser().getName());
            htlQuotabatch.setModifyTime(sysDate);
            this.setEntityID(htlQuotabatch.getContractId());
            this.setHotelId((String) params.get("hotelId"));
        }

        for (Iterator itr = map.keySet().iterator(); itr.hasNext();) {
            String key = (String) itr.next();
            HtlQuotabatch htlQuotabatch = (HtlQuotabatch) map.get(key);
            Long quotabatchid = quotaPriceManage.saveOrUpdateQuotabatch(htlQuotabatch);
            /**
             * 改用存储过程更新CUT OFF DAY配额数据
             */
            quotaPriceManage.generateEveryDayRoom(quotabatchid);
        }

        String forward = null;
        if ("create".equals(returnPage)) {
            create();
            forward = "create";
        }
        if ("contract".equals(returnPage)) {
            forward = SAVE_SUCCESS;
        }
        if ("freeQuota".equals(returnPage)) {
            forward = "freeQuota";
        }
        return forward;
    }

    public String create() {
        Map params = super.getParams();
        HtlQuotabatch quota = new HtlQuotabatch();
        quota.setContractId(Long.parseLong((String) params.get("contractId")));
        quota.setHotelId(Long.parseLong((String) params.get("hotelId")));
        quota.setBeginDate(beginDate);
        quota.setEndDate(endDate);
        quota.setTakebackQuota(true);
        super.setEntity(quota);
        this.shareType = (String) params.get("payment_Method");
        params.put("shareType", shareType);

        contractCufDayList = quotaPriceManage.getCutoffDayByHotelIdContractId(quota.getHotelId(), 
        		quota.getContractId());
        if (null != contractCufDayList) {
            String roomname = "";
            String quotoTypeName = "";
            String shareTypeName = "";
            String cutOffDayall = "";
            String lastCutOffDayall = "";
            int k = 0;

            for (int i = 0; i < contractCufDayList.size(); i++) {
                lastCutOffDayall = cutOffDayall;
                Object[] ob = (Object[]) contractCufDayList.get(i);
                if (roomname.equals(ob[3].toString()) && quotoTypeName.equals(ob[1].toString())
                    && shareTypeName.equals(ob[2].toString())) {
                    Object[] kk = (Object[]) contractCufDayAllList.get(k - 1);
                    kk[0] = kk[0] + "," + ob[0];// parasoft-suppress OPT.AAS "业务需要"
                } else {
                    roomname = ob[3].toString();
                    quotoTypeName = ob[1].toString();
                    shareTypeName = ob[2].toString();
                    lastCutOffDayall = ob[0].toString();
                    Object[] obForShow = new Object[4];
                    obForShow[0] = lastCutOffDayall;
                    obForShow[1] = quotoTypeName;
                    obForShow[2] = shareTypeName;
                    obForShow[3] = roomname;
                    contractCufDayAllList.add(obForShow);
                    k++;
                }
            }
        }
        return "create";
    }

    public String view() {
        String result = super.view();
        HtlQuotabatch quotabatch = (HtlQuotabatch) getEntity();
        Map params = super.getParams();
        params.put("contractId", quotabatch.getContractId());
        params.put("bD", bD);
        params.put("eD", eD);
        return result;
    }

    // 修改配额批次方法
    public String updateQuotaBatch() {
        Map params = super.getParams();
        HtlQuotabatch quota = new HtlQuotabatch();
        quota = quotaPriceManage.queryHtlQuotabatch(Long.parseLong((String) params
            .get("batchQuotaId")));
        htlRoomtype = hotelRoomTypeService.getHtlRoomTypeByRoomTypeId(quota.getRoomType());
        cutoffDayNum = quota.getHtlBatchCutoffDays().size();
        super.setEntity(quota);
        return "update";
    }

    public String review() {
        Map params = super.getParams();
        lstInputCDay = MyBeanUtil.getBatchObjectFromParam(params, InputCutoffDay.class,
            cutoffDayNum);
        rowCount = lstInputCDay.size();
        htlRoomtype = hotelRoomTypeService.getHtlRoomTypeByRoomTypeId(roomType);
        return "review";
    }

    public String returnback() {
        Map params = super.getParams();
        lstInputCDay = MyBeanUtil.getBatchObjectFromParam(params, InputCutoffDay.class,
            cutoffDayNum);

        roomTypePriceTypeLis = hotelManage.findRoomTypeLis(Long.valueOf(hotelId));
        rowCount = lstInputCDay.size();
        htlRoomtype = hotelRoomTypeService.getHtlRoomTypeByRoomTypeId(roomType);
        // 共享方式混乱，modify by zhineng.zhuang
        shareType = contractPay;
        HtlQuotabatch quota = new HtlQuotabatch();
        quota.setContractId(Long.parseLong((String) params.get("contractId")));
        quota.setHotelId(Long.parseLong((String) params.get("hotelId")));
        quota.setBeginDate(beginDate);
        quota.setEndDate(endDate);
        quota.setTakebackQuota(true);
        super.setEntity(quota);
        contractCufDayList = quotaPriceManage.getCutoffDayByHotelIdContractId(quota.getHotelId(), 
        		quota.getContractId());
        if (null != contractCufDayList) {
            String roomname = "";
            String quotoTypeName = "";
            String shareTypeName = "";
            String cutOffDayall = "";
            String lastCutOffDayall = "";
            int k = 0;

            for (int i = 0; i < contractCufDayList.size(); i++) {
                lastCutOffDayall = cutOffDayall;
                Object[] ob = (Object[]) contractCufDayList.get(i);
                if (roomname.equals(ob[3].toString()) && quotoTypeName.equals(ob[1].toString())
                    && shareTypeName.equals(ob[2].toString())) {
                    Object[] kk = (Object[]) contractCufDayAllList.get(k - 1);
                    kk[0] = kk[0] + "," + ob[0];// parasoft-suppress OPT.AAS "业务需要"
                } else {
                    roomname = ob[3].toString();
                    quotoTypeName = ob[1].toString();
                    shareTypeName = ob[2].toString();
                    lastCutOffDayall = ob[0].toString();
                    Object[] obForShow = new Object[4];
                    obForShow[0] = lastCutOffDayall;
                    obForShow[1] = quotoTypeName;
                    obForShow[2] = shareTypeName;
                    obForShow[3] = roomname;
                    contractCufDayAllList.add(obForShow);
                    k++;
                }
            }
        }
        return "returnback";
    }

    public QuotaPriceManage getQuotaPriceManage() {
        return quotaPriceManage;
    }

    public void setQuotaPriceManage(QuotaPriceManage quotaPriceManage) {
        this.quotaPriceManage = quotaPriceManage;
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

    public ISaleDao getSaleDao() {
        return saleDao;
    }

    public void setSaleDao(ISaleDao saleDao) {
        this.saleDao = saleDao;
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

    public String getBD() {
        return bD;
    }

    public void setBD(String bd) {
        bD = bd;
    }

    public String getED() {
        return eD;
    }

    public void setED(String ed) {
        eD = ed;
    }

    public int getCutoffDayNum() {
        return cutoffDayNum;
    }

    public void setCutoffDayNum(int cutoffDayNum) {
        this.cutoffDayNum = cutoffDayNum;
    }

    public HtlRoomtype getHtlRoomtype() {
        return htlRoomtype;
    }

    public void setHtlRoomtype(HtlRoomtype htlRoomtype) {
        this.htlRoomtype = htlRoomtype;
    }

    public List getLstInputCDay() {
        return lstInputCDay;
    }

    public void setLstInputCDay(List lstInputCDay) {
        this.lstInputCDay = lstInputCDay;
    }

    public long getRoomType() {
        return roomType;
    }

    public void setRoomType(long roomType) {
        this.roomType = roomType;
    }

    public String getReturnPage() {
        return returnPage;
    }

    public void setReturnPage(String returnPage) {
        this.returnPage = returnPage;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public List getContractCufDayList() {
        return contractCufDayList;
    }

    public void setContractCufDayList(List contractCufDayList) {
        this.contractCufDayList = contractCufDayList;
    }

    public List getContractCufDayAllList() {
        return contractCufDayAllList;
    }

    public void setContractCufDayAllList(List contractCufDayAllList) {
        this.contractCufDayAllList = contractCufDayAllList;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public List getLstRoomType() {
        return lstRoomType;
    }

    public void setLstRoomType(List lstRoomType) {
        this.lstRoomType = lstRoomType;
    }

    public List getRoomTypePriceTypeLis() {
        return roomTypePriceTypeLis;
    }

    public void setRoomTypePriceTypeLis(List roomTypePriceTypeLis) {
        this.roomTypePriceTypeLis = roomTypePriceTypeLis;
    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public String getContractPay() {
        return contractPay;
    }

    public void setContractPay(String contractPay) {
        this.contractPay = contractPay;
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

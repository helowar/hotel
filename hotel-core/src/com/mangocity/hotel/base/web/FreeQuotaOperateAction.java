package com.mangocity.hotel.base.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.manage.QuotaPriceManage;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlFreeOperate;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.OrLockedRecords;
import com.mangocity.hotel.base.service.ILockedRecordService;
import com.mangocity.hotel.base.util.BizRuleCheck;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.DateSegment;
import com.mangocity.util.bean.MyBeanUtil;

/**
 * 处理释放配额用的
 */
public class FreeQuotaOperateAction extends PersistenceAction {

    private static final long serialVersionUID = 1L;

    /**
     * 配额批次id
     */
    private Long quotaBatchId;

    /**
     * 合同id
     */
    private long contractId;

    /**
     * 酒店id
     */
    private long hotelId;

    private int freeQty;// 释放配额�?

    private int rowNum;

    private String[] week;

    private String[] roomTypeId;

    private int rowNumRoomTypes;

    private String forward;

    // 共享方式
    private String shareType;

    // 配额类型
    private String quotaType;

    // 房型
    // private long roomTypeId;

    private String returnPage;

    private QuotaPriceManage quotaPriceManage;

    private HotelManage hotelManage;

    // 酒店，合同，房�?�，配额等加解锁操作接口
    private ILockedRecordService lockedRecordService;

    // 合同�?始日期，只用来显示，
    private String bD;

    // 合同结束日期,只用来显�?
    private String eD;

    private String strRoomTypeId;

    private Integer[] intRoomTypeId;

    // 合同的付款方�?
    private String contractPay;

    public Integer[] getIntRoomTypeId() {
        return intRoomTypeId;
    }

    public void setIntRoomTypeId(Integer[] intRoomTypeId) {
        this.intRoomTypeId = intRoomTypeId;
    }

    public String getStrRoomTypeId() {
//        this.roomTypeId = roomTypeId;
        StringBuffer roomTypeBuffer = new StringBuffer();
        if (null != roomTypeId && 0 < roomTypeId.length) {
            intRoomTypeId = new Integer[roomTypeId.length];
            for (int m = 0; m < roomTypeId.length; m++) {
                // intWeek[m] = String.valueOf(week[m]);
                roomTypeBuffer.append(roomTypeId[m]);
                roomTypeBuffer.append(",");

            }

        }
        this.strRoomTypeId = roomTypeBuffer.toString();
        return strRoomTypeId;
    }

    public void setStrRoomTypeId(String strRoomTypeId) {
//        this.roomTypeId = roomTypeId;
        StringBuffer roomTypeBuffer = new StringBuffer();
        if (null != roomTypeId && 0 < roomTypeId.length) {
            intRoomTypeId = new Integer[roomTypeId.length];
            for (int m = 0; m < roomTypeId.length; m++) {
                // intWeek[m] = String.valueOf(week[m]);
                roomTypeBuffer.append(roomTypeId[m]);
                roomTypeBuffer.append(",");

            }

        }
        this.strRoomTypeId = roomTypeBuffer.toString();
    }

    // ==============================================================
    // Modified by WUYUN
    private String hotelName;

    private String strWeek;

    private Integer[] intWeek;

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getStrWeek() {
//        this.week = week;
        StringBuffer weekBuffer = new StringBuffer();
        if (null != week && 0 < week.length) {
            intWeek = new Integer[week.length];
            for (int m = 0; m < week.length; m++) {
                // intWeek[m] = String.valueOf(week[m]);
                weekBuffer.append(week[m]);
                weekBuffer.append(",");

            }

        }
        this.strWeek = weekBuffer.toString();
        return strWeek;
    }

    public void setStrWeek(String strWeek) {
//        this.week = week;
        StringBuffer weekBuffer = new StringBuffer();
        if (null != week && 0 < week.length) {
            intWeek = new Integer[week.length];
            for (int m = 0; m < week.length; m++) {
                // intWeek[m] = String.valueOf(week[m]);
                weekBuffer.append(week[m]);
                weekBuffer.append(",");

            }

        }
        this.strWeek = weekBuffer.toString();
    }

    public String[] getWeek() {
        return week;
    }

    public void setWeek(String[] week) {
        this.week = week;

        if (null != week && 0 < week.length) {
            intWeek = new Integer[week.length];
            for (int m = 0; m < week.length; m++) {
                intWeek[m] = Integer.valueOf(week[m]);

            }
        }
    }

    public Integer[] getIntWeek() {
        return intWeek;
    }

    public void setIntWeek(Integer[] intWeek) {
        this.intWeek = intWeek;
    }

    // ==============================================================
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

    /**
     * 初始化释放配额的界面
     */

    protected Class getEntityClass() {
        return HtlFreeOperate.class;
    }

    public String create() {
        super.clearSession();
        Map params = super.getParams();
        HtlContract contract = (HtlContract) super.getEntityManager().find(HtlContract.class,
            contractId);
        HtlHotel hotel = (HtlHotel) super.getEntityManager().find(HtlHotel.class, hotelId);
        this.shareType = (String) params.get("shareType");
        params.put("shareType", shareType);
        params.put("contract", contract);
        params.put("hotel", hotel);
        List lstMember = BizRuleCheck.getTempList();
        params.put("lstMember", lstMember);

        /** �?查酒店配额是否锁�? add by chenjiajie V2.4 2008-8-26 Begin* */
        if (0 < hotelId) {
            OrLockedRecords orLockedRecord = new OrLockedRecords();
            orLockedRecord.setRecordCD(String.valueOf(hotelId));
            orLockedRecord.setLockType(04);
            OrLockedRecords lockedRecords = lockedRecordService.loadLockedRecord(orLockedRecord);
            if (null != lockedRecords) { // 已锁
                String lockerName = lockedRecords.getLockerName();
                String lockerLoginName = lockedRecords.getLockerLoginName();
                if (!super.getOnlineRoleUser().getLoginName().equals(lockerLoginName)) { // 不是锁定人进�?
                    String lockedMSG = "此酒店释放操作已被锁定，在被解锁之前，只有锁定人才能进入(锁定人:" + lockerName + "["
                        + lockerLoginName + "])";
                    request.setAttribute("lockedMSG", lockedMSG);
                    return "lockedHint";
                }
            } else {
                if (null != super.getOnlineRoleUser()) {
                    orLockedRecord.setRemark(hotel.getChnName());
                    orLockedRecord.setLockerName(super.getOnlineRoleUser().getName());
                    orLockedRecord.setLockerLoginName(super.getOnlineRoleUser().getLoginName());
                    orLockedRecord.setLockTime(DateUtil.getSystemDate());
                    lockedRecordService.insertLockedRecord(orLockedRecord);
                }
            }
        }
        /** �?查酒店配额是否锁�? add by chenjiajie V2.4 2008-8-26 End* */

        return "create";
    }

    // Added by WUYUN
    public String viewFreeByWeek() {
        Map params = super.getParams();
        List lsDate = MyBeanUtil.getBatchObjectFromParam(params, DateSegment.class, rowNum);
        super.putSession("lsDate", lsDate);
        params.get("memberTypes");
        List freeList = new ArrayList();
        for (int j = 0; j < roomTypeId.length; j++) {
            for (int i = 0; i < lsDate.size(); i++) {
                DateSegment dDate = (DateSegment) lsDate.get(i);
                HtlFreeOperate freeOperate = new HtlFreeOperate();
                freeOperate.setAdjustWeek(BizRuleCheck.ArrayToString(week));
                freeOperate.setBeginDate(dDate.getStart());
                freeOperate.setEndDate(dDate.getEnd());
                freeOperate.setQuotaType(quotaType);
                freeOperate.setRoomTypeId(Long.valueOf(roomTypeId[j]));
                freeOperate.setContractId(contractId);
                freeOperate.setHotelId(hotelId);
                freeOperate.setFreeQty(freeQty);
                freeOperate.setShareType(shareType);
                if (null != super.getOnlineRoleUser())
                    freeOperate.setCreateBy(super.getOnlineRoleUser().getName());
                freeOperate.setCreateTime(DateUtil.getSystemDate());
                freeOperate.setModifyBy(super.getOnlineRoleUser().getName());
                freeOperate.setModifyTime(DateUtil.getSystemDate());
                // for (int k = 0; k < memberTypes.length; k++) {
                // String sQuota = (String) params.get("private" + memberTypes[k]);
                // String sMax = (String) params.get("max" + memberTypes[k]);
                // String sPreQuota = (String) params.get("prePrivate" + memberTypes[k]);
                // String sPreMax = (String) params.get("preMax" + memberTypes[k]);
                // int privateQuota = StringUtil.getIntValue(sQuota, 0);
                // int maxQuota = StringUtil.getIntValue(sMax, -1);
                // int prePrivateQuota = StringUtil.getIntValue(sPreQuota, 0);
                // int preMaxQuota = StringUtil.getIntValue(sPreMax, -1);
                //		
                // HtlBatchAssign assign = new HtlBatchAssign();
                // assign.setPrivateQuota(privateQuota); // 独占
                // assign.setMaxAbleQuota(maxQuota); // 可用上限
                // assign.setPreMaxAbleQuota(preMaxQuota);
                // assign.setPrePrivateQuota(prePrivateQuota);
                // assign.setMemberType(StringUtil.getIntValue(memberTypes[k],
                // 0));
                // assign.setFreeOperate(freeOperate);
                // freeOperate.getLstBatchAssign().add(assign);
                //
                // }

                // 下面这个计算�?定要有了分配之后，不然的话，就是freeQty，释放多少，共享多少，表示没有独�?
                freeOperate.setShareQty(BizRuleCheck.reCalcFreeShareQuota(freeOperate));
                freeList.add(freeOperate);
            }
        }
        this.strWeek = this.getStrWeek();
        this.strRoomTypeId = this.getStrRoomTypeId();
        HtlHotel hotel = hotelManage.findHotel(hotelId);
        hotelName = hotel.getChnName();
        super.putSession("freeList", freeList);
        return "viewFreeByWeek";
    }

    public String backToFreeQuota() {
        Map params = super.getParams();
        HtlContract contract = (HtlContract) super.getEntityManager().find(HtlContract.class,
            contractId);
        HtlHotel hotel = (HtlHotel) super.getEntityManager().find(HtlHotel.class, hotelId);
        params.put("contract", contract);
        params.put("hotel", hotel);
        shareType = contractPay;
        this.strWeek = this.getStrWeek();
        this.strRoomTypeId = this.getStrRoomTypeId();
        return "create";
    }

    public String freeByWeek() {
        List freeList = (List) super.getFromSession("freeList");
        if (null != freeList) {
            for (int i = 0; i < freeList.size(); i++) {
                HtlFreeOperate freeOperate = (HtlFreeOperate) freeList.get(i);
                super.getEntityManager().saveOrUpdate(freeOperate);
                quotaPriceManage.saleBatchFreeQuota(freeOperate);
            }
        }
        super.clearSession();
        // Modified by WUYUN
        // quotaPriceManage.saleBatchFreeQuota(freeOperate);
        // List lsDate = MyBeanUtil.getBatchObjectFromParam(params,
        // DateSegment.class, rowNum);
        // String[] memberTypes = (String[]) params.get("memberTypes");
        // for (int i = 0; i < lsDate.size(); i++) {
        // DateSegment dDate = (DateSegment) lsDate.get(i);
        // HtlFreeOperate freeOperate = new HtlFreeOperate();
        // freeOperate.setAdjustWeek(BizRuleCheck.ArrayToString(week));
        // freeOperate.setBeginDate(dDate.getStart());
        // freeOperate.setEndDate(dDate.getEnd());
        //			
        // freeOperate.setQuotaType(quotaType);
        // freeOperate.setRoomTypeId(roomTypeId);
        //			
        // freeOperate.setContractId(contractId);
        // freeOperate.setHotelId(hotelId);
        // freeOperate.setFreeQty(freeQty);
        // freeOperate.setShareType(shareType);
        // if (super.getOnlineRoleUser()!=null)
        // freeOperate.setCreateBy(super.getOnlineRoleUser().getName());
        // freeOperate.setCreateTime(DateUtil.getSystemDate());
        //			
        // for (int k = 0; k < memberTypes.length; k++) {
        // String sQuota = (String) params.get("private" + memberTypes[k]);
        // String sMax = (String) params.get("max" + memberTypes[k]);
        // String sPreQuota = (String) params.get("prePrivate" + memberTypes[k]);
        // String sPreMax = (String) params.get("preMax" + memberTypes[k]);
        // int privateQuota = StringUtil.getIntValue(sQuota, 0);
        // int maxQuota = StringUtil.getIntValue(sMax, -1);
        // int prePrivateQuota = StringUtil.getIntValue(sPreQuota, 0);
        // int preMaxQuota = StringUtil.getIntValue(sPreMax, -1);
        //		
        // HtlBatchAssign assign = new HtlBatchAssign();
        // assign.setPrivateQuota(privateQuota); // 独占
        // assign.setMaxAbleQuota(maxQuota); // 可用上限
        // assign.setPreMaxAbleQuota(preMaxQuota);
        // assign.setPrePrivateQuota(prePrivateQuota);
        // assign.setMemberType(StringUtil.getIntValue(memberTypes[k],
        // 0));
        // assign.setFreeOperate(freeOperate);
        // freeOperate.getLstBatchAssign().add(assign);
        //
        // }
        //
        // // 下面这个计算�?定要有了分配之后，不然的话，就是freeQty，释放多少，共享多少，表示没有独�?
        // freeOperate.setShareQty(BizRuleCheck
        // .reCalcFreeShareQuota(freeOperate));
        // super.getEntityManager().saveOrUpdate(freeOperate);
        // quotaPriceManage.saleBatchFreeQuota(freeOperate);
        // }
        HtlFreeOperate freeOperate = (HtlFreeOperate) super.populateEntity();
        freeOperate.setContractId(contractId);
        this.setEntityID(freeOperate.getContractId());
        this.setHotelId(hotelId);
        if ("freeByWeek".equals(returnPage)) {
            forward = "freeByWeek";
        }
        if ("contract".equals(returnPage)) {
            forward = "saveAndContract";
        }
        if ("freeQuota".equals(returnPage)) {
            forward = "freeQuota";
        }
        // 解除配额�?
        lockedRecordService.deleteLockedRecordTwo(String.valueOf(hotelId), 04, super
            .getOnlineRoleUser().getLoginName());
        return forward;
        // return "freeByWeek";
    }

    /**
     * 返回合同查询页面，并解锁
     * 
     * @return
     */
    public String backToList() {
        Map params = super.getParams();
        String hotelIdStr = (String) params.get("hotelId");
        /** 解除酒店合同锁定 add by chenjiajie V2.4 2008-8-26 Begin* */
        lockedRecordService.deleteLockedRecordTwo(hotelIdStr, 04, super.getOnlineRoleUser()
            .getLoginName());
        /** 解除酒店合同锁定 add by chenjiajie V2.4 2008-8-26 End* */
        return "updateQuota";
    }

    public Long getQuotaBatchId() {
        return quotaBatchId;
    }

    public void setQuotaBatchId(Long quotaBatchId) {
        this.quotaBatchId = quotaBatchId;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    // public String[] getWeek() {
    // return week;
    // }
    //
    // public void setWeek(String[] week) {
    // this.week = week;
    // }

    public int getRowNumRoomTypes() {
        return rowNumRoomTypes;
    }

    public void setRowNumRoomTypes(int rowNumRoomTypes) {
        this.rowNumRoomTypes = rowNumRoomTypes;
    }

    public long getContractId() {
        return contractId;
    }

    public void setContractId(long contractId) {
        this.contractId = contractId;
    }

    public int getFreeQty() {
        return freeQty;
    }

    public void setFreeQty(int freeQty) {
        this.freeQty = freeQty;
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

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    // public long getRoomTypeId() {
    // return roomTypeId;
    // }
    //
    // public void setRoomTypeId(long roomTypeId) {
    // this.roomTypeId = roomTypeId;
    // }

    public String getReturnPage() {
        return returnPage;
    }

    public void setReturnPage(String returnPage) {
        this.returnPage = returnPage;
    }

    public String getForward() {
        return forward;
    }

    public void setForward(String forward) {
        this.forward = forward;
    }

    public String[] getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(String[] roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

    public ILockedRecordService getLockedRecordService() {
        return lockedRecordService;
    }

    public void setLockedRecordService(ILockedRecordService lockedRecordService) {
        this.lockedRecordService = lockedRecordService;
    }

    public String getContractPay() {
        return contractPay;
    }

    public void setContractPay(String contractPay) {
        this.contractPay = contractPay;
    }

}

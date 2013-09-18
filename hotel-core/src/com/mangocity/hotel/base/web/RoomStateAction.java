package com.mangocity.hotel.base.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.dao.IPriceDao;
import com.mangocity.hotel.base.manage.ContractManage;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.manage.RoomStateManage;
import com.mangocity.hotel.base.manage.assistant.RoomStateBean;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlHotelExt;
import com.mangocity.hotel.base.persistence.HtlRoomStatusProcess;
import com.mangocity.hotel.base.persistence.OrLockedRecords;
import com.mangocity.hotel.base.service.ILockedRecordService;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.DateSegment;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.bean.RoomTypeStatus;

/**
 */
public class RoomStateAction extends PersistenceAction {

    private long hotelID;

    private List htlCtct;

    private List roomType;

    private List lstEexhibits = new ArrayList();

    private List lstSeasons = new ArrayList();

    private int rowNum;

    private HotelManage hotelManage;

    private RoomStateManage roomStateManage;

    private IPriceDao priceDao;

    private HtlHotel hotel;

    private Date begindate;

    private Date enddate;

    private String[] week;

    // 房态负责人
    private String roomStateManagerStr;

    private String isRoomStatusReport;

    /**
     * cutoff行数
     */
    private int cutoffDayNum;

    /*
     * 昨天到今天所有房态更改记录
     */
    private List dateRoomStatusProcess = new ArrayList();

    /**
     * 交接事项
     */
    private String processRemark;

    /**
     * 合同起始日期，只做显示用，
     */
    private String bD;

    /**
     * 合同结束日期
     */
    private String eD;

    private String addfor;

    private RoomStateBean roomStateBean = new RoomStateBean();

    // 酒店，合同，房态，配额等加解锁操作接口
    private ILockedRecordService lockedRecordService;

    // modified by wuyun
    private ContractManage contractManage;

    public ContractManage getContractManage() {
        return contractManage;
    }

    public void setContractManage(ContractManage contractManage) {
        this.contractManage = contractManage;
    }

    public RoomStateBean getRoomStateBean() {
        return roomStateBean;
    }

    public void setRoomStateBean(RoomStateBean roomStateBean) {
        this.roomStateBean = roomStateBean;
    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

    /**
     * 按时段处理
     * 
     * @return
     */
    public String processByTimes() {
        super.getOnlineRoleUser();

        dateRoomStatusProcess = hotelManage.findRoomStatusDateProcess(hotelID);
        int i = dateRoomStatusProcess.size();
        if (0 != i) {
            HtlRoomStatusProcess htlRoomStatusProcess = new HtlRoomStatusProcess();
            htlRoomStatusProcess = (HtlRoomStatusProcess) dateRoomStatusProcess.get(i - 1);
            isRoomStatusReport = htlRoomStatusProcess.getIsRoomStatusReport().toString();
            processRemark = htlRoomStatusProcess.getProcessRemark();
        }
        hotel = (HtlHotel) super.getEntityManager().find(HtlHotel.class, hotelID);
        List list = hotel.getHtelHotelExt();
        for (int j = 0; j < list.size(); j++) {
            HtlHotelExt hext = (HtlHotelExt) list.get(j);
            String roomStateManager = hext.getRoomStateManager();
            String isGreenMangoHotel = hext.getIsGreenMangoHotel();
            request.setAttribute("roomStateManager", roomStateManager);
            request.setAttribute("isGreenMangoHotel", isGreenMangoHotel);
        }
        // modify by shizhongwen 去掉属于中旅的房型(因为中旅香港房型不存在房态管理)
        roomType = hotelManage.lstRoomTypeByHotelIdRemoveHK(hotelID);
        rowNum = roomType.size();
        // 根据城市查询展会,传递城市到页面上，以供查询
        if (null != hotel.getCity() && !("".equals(hotel.getCity())))
            request.setAttribute("city", hotel.getCity());

        // 根据酒店查询大假期
        lstSeasons = hotel.getSellSeason();

        // Modified by WUYUN ，根据当前日期和酒店id去找合同
        HtlContract contract = contractManage.checkContractDateNew(hotelID, new Date());
        if (null != contract) {
            bD = DateUtil.dateToString(contract.getBeginDate());
            eD = DateUtil.dateToString(contract.getEndDate());
        }

        /** 检查酒店房态是否锁定 add by chenjiajie V2.4 2008-8-26 Begin* */
        if (0 < hotelID) {
            OrLockedRecords orLockedRecord = new OrLockedRecords();
            orLockedRecord.setRecordCD(String.valueOf(hotelID));
            orLockedRecord.setLockType(02);
            OrLockedRecords lockedRecords = lockedRecordService.loadLockedRecord(orLockedRecord);
            if (null != lockedRecords) { // 已锁
                String lockerName = lockedRecords.getLockerName();
                String lockerLoginName = lockedRecords.getLockerLoginName();
                if (!super.getOnlineRoleUser().getLoginName().equals(lockerLoginName)) { // 不是锁定人进入
                    String lockedMSG = "此酒店已被锁定，在被解锁之前，只有锁定人才能进入（锁定人：" + lockerName + "["
                        + lockerLoginName + "]）";
                    request.setAttribute("lockedMSG", lockedMSG);
                    return "lockedHint1";
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
        /** 检查酒店房态是否锁定 add by chenjiajie V2.4 2008-8-26 End* */

        return "processByTimes";
    }

    public String saveRoomStatu() {
        Map params = super.getParams();
        List ls = MyBeanUtil.getBatchObjectRowColFromParam(params, RoomTypeStatus.class, rowNum);
        List dls = MyBeanUtil.getBatchObjectFromParam(params, DateSegment.class, cutoffDayNum);
        /**
         * 将ls中roomTypeStatus不为空的取出放到templs中
         * 
         * @guojun 2008-12-19 10:12 begin
         */
        List<RoomTypeStatus> templs = new ArrayList<RoomTypeStatus>();
        for (Iterator itr = ls.iterator(); itr.hasNext();) {
            RoomTypeStatus roomTS = (RoomTypeStatus) itr.next();
            // 如果roomTypeChoose为null则继续循环
            if (null == roomTS.getRoomTypeChoose() || "".equals(roomTS.getRoomTypeChoose())) {
                continue;
                // 否则如果RoomBedId为null则继续循环
            } else if (null == roomTS.getRoomBedId() || "".equals(roomTS.getRoomBedId())) {
                continue;
                // 否则如果roomBedStatus为null则继续循环
            } else if (null == roomTS.getRoomBedStatus() || "".equals(roomTS.getRoomBedId())) {
                continue;
            }
            templs.add(roomTS);
        }
        ls = templs;
        /**
         * 将ls中roomTypeStatus不为空的取出放到templs中
         * 
         * @guojun 2008-12-19 10:12 end
         */
        // 更新房态负责人 haibo.li by 2008.12.01 begin
        hotel = (HtlHotel) super.getEntityManager().find(HtlHotel.class, hotelID);
        List lsHtlHotelExt = hotel.getHtelHotelExt();
        if (0 < lsHtlHotelExt.size()) {
            if (StringUtil.isValidStr(roomStateManagerStr)) {
                HtlHotelExt he = (HtlHotelExt) lsHtlHotelExt.get(0);
                he.setHtlHotel(hotel);
                he.setRoomStateManager(roomStateManagerStr);
                hotelManage.saveOrUpdateExt(he);
            }
        } else if (0 == lsHtlHotelExt.size()
            && (null != roomStateManagerStr && !roomStateManagerStr.trim().equals(""))) {
            if (StringUtil.isValidStr(roomStateManagerStr)) {
                HtlHotelExt he = new HtlHotelExt();
                he.setHtlHotel(hotel);
                he.setRoomStateManager(roomStateManagerStr);
                hotelManage.saveHtlHotelExt(he);
            }
        }
        // end
        for (int i = 0; i < dls.size(); i++) {
            DateSegment ds = new DateSegment();
            ds = (DateSegment) dls.get(i);
            roomStateBean.setRoomTypeStatus(ls);
            roomStateBean.setBegindate(ds.getStart());
            roomStateBean.setEnddate(ds.getEnd());
            roomStateBean.setHotelID(Long.valueOf(hotelID).longValue());
            roomStateBean.setWeek(week);
            roomStateBean.setProcessRemark(processRemark);
            roomStateBean.setIsRoomStatusReport(isRoomStatusReport);
            if (null != super.getOnlineRoleUser()) {
                roomStateBean.setUserId(super.getOnlineRoleUser().getLoginName());
                roomStateBean.setUserName(super.getOnlineRoleUser().getName());
                if (super.getOnlineRoleUser().getLoginName().equals("")
                    || super.getOnlineRoleUser().getName().equals("")) {
                    roomStateBean.setUserId(super.getBackLoginName());
                    roomStateBean.setUserName(super.getBackUserName());
                }
            } else {
                roomStateBean.setUserId(super.getBackLoginName());
                roomStateBean.setUserName(super.getBackUserName());
            }
            roomStateManage.saveRoomState(roomStateBean);
        }

        // roomStateBean.setBegindate(begindate);
        // roomStateBean.setEnddate(enddate);

        // roomStateManage.saveRoomState(roomStateBean);

        // 解除酒店房态锁定
        // 不解除锁定
        // lockedRecordService.deleteLockedRecord(String.valueOf(hotelID),02);
        return SUCCESS;
    }

    public List getHtlCtct() {
        return htlCtct;
    }

    public void setHtlCtct(List htlCtct) {
        this.htlCtct = htlCtct;
    }

    public List getRoomType() {
        return roomType;
    }

    public void setRoomType(List roomType) {
        this.roomType = roomType;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public Date getBegindate() {
        return begindate;
    }

    public void setBegindate(Date begindate) {
        this.begindate = begindate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public String[] getWeek() {
        return week;
    }

    public void setWeek(String[] week) {
        this.week = week;
    }

    public RoomStateManage getRoomStateManage() {
        return roomStateManage;
    }

    public void setRoomStateManage(RoomStateManage roomStateManage) {
        this.roomStateManage = roomStateManage;
    }

    public long getHotelID() {
        return hotelID;
    }

    public void setHotelID(long hotelID) {
        this.hotelID = hotelID;
    }

    public HtlHotel getHotel() {
        return hotel;
    }

    public void setHotel(HtlHotel hotel) {
        this.hotel = hotel;
    }

    public List getLstEexhibits() {
        return lstEexhibits;
    }

    public void setLstEexhibits(List lstEexhibits) {
        this.lstEexhibits = lstEexhibits;
    }

    public List getLstSeasons() {
        return lstSeasons;
    }

    public void setLstSeasons(List lstSeasons) {
        this.lstSeasons = lstSeasons;
    }

    public IPriceDao getPriceDao() {
        return priceDao;
    }

    public void setPriceDao(IPriceDao priceDao) {
        this.priceDao = priceDao;
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

    public String getProcessRemark() {
        return processRemark;
    }

    public void setProcessRemark(String processRemark) {
        this.processRemark = processRemark;
    }

    public List getDateRoomStatusProcess() {
        return dateRoomStatusProcess;
    }

    public void setDateRoomStatusProcess(List dateRoomStatusProcess) {
        this.dateRoomStatusProcess = dateRoomStatusProcess;
    }

    public int getCutoffDayNum() {
        return cutoffDayNum;
    }

    public void setCutoffDayNum(int cutoffDayNum) {
        this.cutoffDayNum = cutoffDayNum;
    }

    public String getAddfor() {
        return addfor;
    }

    public void setAddfor(String addfor) {
        this.addfor = addfor;
    }

    public ILockedRecordService getLockedRecordService() {
        return lockedRecordService;
    }

    public void setLockedRecordService(ILockedRecordService lockedRecordService) {
        this.lockedRecordService = lockedRecordService;
    }

    public String getIsRoomStatusReport() {
        return isRoomStatusReport;
    }

    public void setIsRoomStatusReport(String isRoomStatusReport) {
        this.isRoomStatusReport = isRoomStatusReport;
    }

    public String getRoomStateManagerStr() {
        return roomStateManagerStr;
    }

    public void setRoomStateManagerStr(String roomStateManagerStr) {
        this.roomStateManagerStr = roomStateManagerStr;
    }

}

package com.mangocity.hotel.util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import com.mangocity.hotel.base.manage.ContractManage;
import com.mangocity.hotel.base.manage.IPriceManage;
import com.mangocity.hotel.base.manage.OnlyClauserManage;
import com.mangocity.hotel.base.manage.RoomControlManage;
import com.mangocity.hotel.order.service.IAuditService;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.bean.ContinueDatecomponent;

/**
 * 这个类主要是想用来在提交页面事务之前检查业务规则，比如是否重复，在数据库中是否存在，等，用ajax来调用。
 * 
 * @author xiaowumi
 * 
 */
public class CheckBizManager implements Serializable {

    private ContractManage contractManage;

    private IPriceManage priceManage;

    private RoomControlManage roomControlManage;

    private IAuditService auditService;// parasoft-suppress SERIAL.NSFSC "JAR 包问题"

    private OnlyClauserManage onlyAjaxManage;

    /**
     * 当前登录权限用户
     */
    private UserWrapper roleUser;

    public ContractManage getContractManage() {
        return contractManage;
    }

    public void setContractManage(ContractManage contractManage) {
        this.contractManage = contractManage;
    }

    public RoomControlManage getRoomControlManage() {
        return roomControlManage;
    }

    public void setRoomControlManage(RoomControlManage roomControlManage) {
        this.roomControlManage = roomControlManage;
    }      

    public IAuditService getAuditService() {
        return auditService;
    }

    public void setAuditService(IAuditService auditService) {
        this.auditService = auditService;
    }

    public IPriceManage getPriceManage() {
        return priceManage;
    }

    public void setPriceManage(IPriceManage priceManage) {
        this.priceManage = priceManage;
    }

    public Boolean checkContractDate(long hotelid, String beginDate, String endDate) {
        int i = contractManage.checkContractDate(hotelid, beginDate, endDate);
        return !(1 == i);
    }

    public Boolean checkEditContractDate(long hotelid, String beginDate, String endDate,
        String beginDateOld, String endDateOld) {
        int i = contractManage.checkEditContractDate(hotelid, beginDate, endDate, beginDateOld,
            endDateOld);
        return !(1 == i);
    }

    public Boolean checkMainCommendDate(long commendId, long hotelid, String beginDate,
        String endDate, String bDate, String eDate) {
        int i = roomControlManage.checkMainCommendDate(commendId, hotelid, beginDate, endDate,
            bDate, eDate);
        return !(1 == i);
    }

    public Boolean checkRoomType(long hotelid, long roomTypeID, String roomName) {
        int i = contractManage.checkRoomType(hotelid, roomTypeID, roomName);
        return !(1 == i);
    }

    /**
     * 暂存 - 有已入住的情况
     * 
     * @param index
     * @param orderID
     * @param night
     * @param rState
     * @param rStateNot
     * @param orderState
     * @param rID
     * @param specialNote
     * @param roomNumber
     * @param orderAuditType
     * @param fellowNames
     * @param session
     * @return
     */
    public List checkNoteAudit(int index, String orderID, String night, String rState,
        String rStateNot, int orderState, String rID, String specialNote, String roomNumber,
        int orderAuditType, String fellowNames, HttpSession session) {
        roleUser = (UserWrapper) session.getAttribute("onlineRoleUser");
        List returtNote = new ArrayList();
        if (null == roleUser) {
            returtNote.add(-1);
            return returtNote;
        }
        auditService.auditWork(index, orderID, night, rState, rStateNot, orderState, rID,
            specialNote, roomNumber, orderAuditType, fellowNames, roleUser);
        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        returtNote.add(index);
        returtNote.add(roleUser.getName());
        returtNote.add(df.format(now));
        return returtNote;
    }

    /**
     * 确定 - 部分已入住情况
     * 
     * @param index
     * @param orderID
     * @param night
     * @param rState
     * @param rStateNot
     * @param orderState
     * @param rID
     * @param specialNote
     * @param roomNumber
     * @param hotelID
     * @param orderAuditType
     * @param fellowNames
     * @param session
     * @return
     */
    public List auditWorkNotFulfill(int index, String orderID, String night, String rState,
        String rStateNot, int orderState, String rID, String specialNote, String roomNumber,
        long hotelID, int orderAuditType, String fellowNames, HttpSession session) {
        roleUser = (UserWrapper) session.getAttribute("onlineRoleUser");
        List returtNote = new ArrayList();
        if (null == roleUser) {
            returtNote.add(-1);
            return returtNote;
        }
        auditService.auditWorkNotFulfill(index, orderID, night, rState, rStateNot, orderState, rID,
            specialNote, roomNumber, hotelID, orderAuditType, fellowNames, roleUser);
        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        returtNote.add(index);
        returtNote.add(roleUser.getName());
        returtNote.add(df.format(now));
        return returtNote;
    }

    /**
     * 确定 - 全部已入住的情况
     * 
     * @param index
     * @param orderID
     * @param night
     * @param rState
     * @param rStateNot
     * @param orderState
     * @param rID
     * @param specialNote
     * @param roomNumber
     * @param hotelID
     * @param orderAuditType
     * @param fellowNames
     * @param session
     * @return
     */
    public List checkNoteAuditFulfill(int index, String orderID, String night, String rState,
        String rStateNot, int orderState, String rID, String specialNote, String roomNumber,
        long hotelID, int orderAuditType, String fellowNames, HttpSession session) {
        roleUser = (UserWrapper) session.getAttribute("onlineRoleUser");
        List returtNote = new ArrayList();
        if (null == roleUser) {
            returtNote.add(-1);
            return returtNote;
        }
        auditService.auditWorkFulfill(index, orderID, night, rState, rStateNot, orderState, rID,
            specialNote, roomNumber, hotelID, orderAuditType, fellowNames, roleUser);
        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        returtNote.add(index);
        returtNote.add(roleUser.getName());
        returtNote.add(df.format(now));
        return returtNote;
    }

    /**
     * 暂存 - Noshow或提前退房
     * 
     * @param index
     * @param orderID
     * @param night
     * @param orderState
     * @param noshowCode
     * @param noshowReason
     * @param specialNote
     * @param orderAuditType
     * @param session
     * @return
     */
    public List auditWorkNoShow(int index, String orderID, String night, int orderState,
        String noshowCode, String noshowReason, String specialNote, int orderAuditType,
        HttpSession session) {
        roleUser = (UserWrapper) session.getAttribute("onlineRoleUser");
        List returtNote = new ArrayList();
        if (null == roleUser) {
            returtNote.add(-1);
            return returtNote;
        }
        auditService.auditWorkNoShow(index, orderID, night, orderState, noshowCode, noshowReason,
            specialNote, orderAuditType, roleUser);
        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        returtNote.add(index);
        returtNote.add(roleUser.getName());
        returtNote.add(df.format(now));
        return returtNote;
    }

    /**
     * 确定 - Noshow或提前退房
     * 
     * @param index
     * @param orderID
     * @param night
     * @param orderState
     * @param noshowCode
     * @param noshowReason
     * @param specialNote
     * @param hotelID
     * @param orderAuditType
     * @param session
     * @return
     */
    public List auditWorkNoShowFulfill(int index, String orderID, String night, int orderState,
        String noshowCode, String noshowReason, String specialNote, long hotelID,
        int orderAuditType, HttpSession session) {
        roleUser = (UserWrapper) session.getAttribute("onlineRoleUser");
        List returtNote = new ArrayList();
        if (null == roleUser) {
            returtNote.add(-1);
            return returtNote;
        }
        auditService.auditWorkNoShowFulfill(index, orderID, night, orderState, noshowCode,
            noshowReason, specialNote, hotelID, orderAuditType, roleUser);
        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        returtNote.add(index);
        returtNote.add(roleUser.getName());
        returtNote.add(df.format(now));
        return returtNote;
    }

    /**
     * 审核完成 - Noshow或提前退房
     * 
     * @param index
     * @param orderID
     * @param night
     * @param orderState
     * @param noshowCode
     * @param noshowReason
     * @param specialNote
     * @param hotelID
     * @param orderAuditType
     * @param session
     * @return
     */
    public List auditFulfill(int index, String orderID, String night, int orderState,
        String noshowCode, String noshowReason, String specialNote, long hotelID,
        int orderAuditType, HttpSession session) {
        roleUser = (UserWrapper) session.getAttribute("onlineRoleUser");
        List returtNote = new ArrayList();
        if (null == roleUser) {
            returtNote.add(-1);
            return returtNote;
        }
        auditService.auditNoShowFulfill(index, orderID, night, orderState, noshowCode,
            noshowReason, specialNote, hotelID, orderAuditType, roleUser);
        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        returtNote.add(index);
        returtNote.add(roleUser.getName());
        returtNote.add(df.format(now));
        return returtNote;
    }

    public Boolean continueContract(long hotelId, long contractId, String continueDate,
        String oldBeginDate) {
        // 如果合同不重叠,则延长合同
        if (contractManage.justContinueContract(hotelId, contractId, oldBeginDate, continueDate)) {
            return true;
        }
        return false;
    }

    public List<ContinueDatecomponent> checkContinuePrice(long hotelId, long contractId,
        List<ContinueDatecomponent> dateComponents) {
        return contractManage.checkContinuePrice(hotelId, contractId, dateComponents);
    }

    public UserWrapper getRoleUser() {
        return roleUser;
    }

    public void setRoleUser(UserWrapper roleUser) {
        this.roleUser = roleUser;
    }

    /**
     * 检查房型是否有关房历史 add by zhineng.zhuang 2008-10-30
     * 
     * @param hotelId
     * @param childRoomTypeId
     * @param beginDate
     * @param EndDate
     * @return
     */
    public List checkCloseHistory(long hotelId, String childRoomTypeId,
        List<ContinueDatecomponent> ContinueDatecomponent) {
        return contractManage.checkCloseHistory(hotelId, childRoomTypeId, ContinueDatecomponent);
    }
    
    /**
     * 检查开房是否会导致bug add by huizhong.chen 2008-9-20
     * 
     * @param hotelId
     * @param childRoomTypeId
     * @param ContinueDatecomponent
     * @return
     */
    public boolean checkCloseHistoryBySameReason(long hotelId, String childRoomTypeId, List<ContinueDatecomponent> ContinueDatecomponent)
    {
      return this.contractManage.checkCloseHistoryBySameReason(hotelId, childRoomTypeId, ContinueDatecomponent);
    }
    
    /**
     * 检查酒店价格类型以前是否存在加幅 add by zhineng.zhuang 2008-11-27
     * 
     * @param newAllPriceId
     * @param hotelId
     * @param entityID
     * @param lsContinueDatecomponent
     * @return
     */
    public List checkIsAddScope(String newAllPriceId, Long hotelId, Long entityID,
        List<ContinueDatecomponent> lsContinueDatecomponent) {
        return priceManage.checkIsAddScope(newAllPriceId, hotelId, entityID,
            lsContinueDatecomponent);
    }

    public OnlyClauserManage getOnlyAjaxManage() {
        return onlyAjaxManage;
    }

    public void setOnlyAjaxManage(OnlyClauserManage onlyAjaxManage) {
        this.onlyAjaxManage = onlyAjaxManage;
    }

}

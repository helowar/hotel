package com.mango.hotel.ebooking.action;

import com.mango.hotel.ebooking.persistence.HtlEbookingOperationlog;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;

/**
 * 操作日志查询
 * 
 * @author zuoshengwei hotel2.9 2009-07-03
 * 
 */
public class OperationLogAction extends PersistenceAction {

    // 开始日期
    private String beginDt;

    // 结束日期
    private String endDt;

    // 酒店ID
    private Long hotelId;

    // 功能模块ID
    private Long functioModuleId;

    // 操作人ID
    private String operOner;

    // 操作方式
    private String operMode;

    private HtlEbookingOperationlog htlEbookingOperationlog;

    protected Class getEntityClass() {

        return HtlEbookingOperationlog.class;

    }

    // 页面跳转
    public String allForword() {

        super.setEntity(super.populateEntity());
        this.setHtlEbookingOperationlog((HtlEbookingOperationlog) this.getEntity());

        return "viewOperationLogPage";

    }

    public HtlEbookingOperationlog getHtlEbookingOperationlog() {
        return htlEbookingOperationlog;
    }

    public void setHtlEbookingOperationlog(HtlEbookingOperationlog htlEbookingOperationlog) {
        this.htlEbookingOperationlog = htlEbookingOperationlog;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public Long getFunctioModuleId() {
        return functioModuleId;
    }

    public void setFunctioModuleId(Long functioModuleId) {
        this.functioModuleId = functioModuleId;
    }

    public String getOperOner() {
        return operOner;
    }

    public void setOperOner(String operOner) {
        this.operOner = operOner;
    }

    public String getBeginDt() {
        return beginDt;
    }

    public void setBeginDt(String beginDt) {
        this.beginDt = beginDt;
    }

    public String getEndDt() {
        return endDt;
    }

    public void setEndDt(String endDt) {
        this.endDt = endDt;
    }

    public String getOperMode() {
        return operMode;
    }

    public void setOperMode(String operMode) {
        this.operMode = operMode;
    }

}

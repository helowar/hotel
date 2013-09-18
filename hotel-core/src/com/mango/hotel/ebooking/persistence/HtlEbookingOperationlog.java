package com.mango.hotel.ebooking.persistence;

import java.util.Date;

/**
 * 操作日志实类
 * 
 * @author shengwei.zuo hotel2.9 2009-07-03
 */

public class HtlEbookingOperationlog implements java.io.Serializable {

    // Fields

    // 日志ID
    private Long operationid;

    // 操作方式
    private Integer operationmode;

    // 日志内容
    private String operationcontent;

    // 表名
    private String tablename;

    // 表ID
    private Long tableid;

    // 操作人
    private String operationer;

    // 操作时间
    private Date operationdate;

    // 功能模块ID
    private Integer functionalmoduleid;

    // 酒店ID
    private Long hotelid;

    // 操作人ID
    private String operationerid;

    // Constructors

    /** default constructor */
    public HtlEbookingOperationlog() {

    }

    public HtlEbookingOperationlog(Long operationid, Integer operationmode,
        String operationcontent, String tablename, Long tableid, String operationer,
        Date operationdate, Integer functionalmoduleid, Long hotelid, String operationerid) {
        super();
        this.operationid = operationid;
        this.operationmode = operationmode;
        this.operationcontent = operationcontent;
        this.tablename = tablename;
        this.tableid = tableid;
        this.operationer = operationer;
        this.operationdate = operationdate;
        this.functionalmoduleid = functionalmoduleid;
        this.hotelid = hotelid;
        this.operationerid = operationerid;
    }

    public Long getOperationid() {
        return operationid;
    }

    public void setOperationid(Long operationid) {
        this.operationid = operationid;
    }

    public String getOperationcontent() {
        return operationcontent;
    }

    public void setOperationcontent(String operationcontent) {
        this.operationcontent = operationcontent;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public Long getTableid() {
        return tableid;
    }

    public void setTableid(Long tableid) {
        this.tableid = tableid;
    }

    public String getOperationer() {
        return operationer;
    }

    public void setOperationer(String operationer) {
        this.operationer = operationer;
    }

    public Date getOperationdate() {
        return operationdate;
    }

    public void setOperationdate(Date operationdate) {
        this.operationdate = operationdate;
    }

    public Long getHotelid() {
        return hotelid;
    }

    public void setHotelid(Long hotelid) {
        this.hotelid = hotelid;
    }

    public String getOperationerid() {
        return operationerid;
    }

    public void setOperationerid(String operationerid) {
        this.operationerid = operationerid;
    }

    public Integer getOperationmode() {
        return operationmode;
    }

    public void setOperationmode(Integer operationmode) {
        this.operationmode = operationmode;
    }

    public Integer getFunctionalmoduleid() {
        return functionalmoduleid;
    }

    public void setFunctionalmoduleid(Integer functionalmoduleid) {
        this.functionalmoduleid = functionalmoduleid;
    }

}
package com.mangocity.hotel.base.persistence;

import java.util.Date;

/**
 * HtlPublicOperationlog entity.
 * 
 * @author shengwei.zuo hotel2.9.3 公用日志实体类 2009-08-25
 */

public class HtlPublicOperationlog implements java.io.Serializable {

    // Fields

    // 日志ID
    private Long operationid;

    // 操作方式
    private Long operationmode;

    // 操作内容
    private String operationcontent;

    // 表名
    private String tablename;

    // 表ID
    private Long tableid;

    // 操作人名称
    private String operationer;

    // 操作时间
    private Date operationdate;

    // 功能模块
    private Long functionalmoduleid;

    // 酒店ID
    private Long hotelid;

    // 操作人ID
    private String operationerid;

    // Constructors

    /** default constructor */
    public HtlPublicOperationlog() {
    }

    /** minimal constructor */
    public HtlPublicOperationlog(Long operationmode, String operationcontent, String tablename,
        String operationer, Date operationdate, Long functionalmoduleid, String operationerid) {
        this.operationmode = operationmode;
        this.operationcontent = operationcontent;
        this.tablename = tablename;
        this.operationer = operationer;
        this.operationdate = operationdate;
        this.functionalmoduleid = functionalmoduleid;
        this.operationerid = operationerid;
    }

    /** full constructor */
    public HtlPublicOperationlog(Long operationmode, String operationcontent, String tablename,
        Long tableid, String operationer, Date operationdate, Long functionalmoduleid,
        Long hotelid, String operationerid) {
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

    // Property accessors

    public Long getOperationid() {
        return this.operationid;
    }

    public void setOperationid(Long operationid) {
        this.operationid = operationid;
    }

    public Long getOperationmode() {
        return this.operationmode;
    }

    public void setOperationmode(Long operationmode) {
        this.operationmode = operationmode;
    }

    public String getOperationcontent() {
        return this.operationcontent;
    }

    public void setOperationcontent(String operationcontent) {
        this.operationcontent = operationcontent;
    }

    public String getTablename() {
        return this.tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public Long getTableid() {
        return this.tableid;
    }

    public void setTableid(Long tableid) {
        this.tableid = tableid;
    }

    public String getOperationer() {
        return this.operationer;
    }

    public void setOperationer(String operationer) {
        this.operationer = operationer;
    }

    public Date getOperationdate() {
        return this.operationdate;
    }

    public void setOperationdate(Date operationdate) {
        this.operationdate = operationdate;
    }

    public Long getFunctionalmoduleid() {
        return this.functionalmoduleid;
    }

    public void setFunctionalmoduleid(Long functionalmoduleid) {
        this.functionalmoduleid = functionalmoduleid;
    }

    public Long getHotelid() {
        return this.hotelid;
    }

    public void setHotelid(Long hotelid) {
        this.hotelid = hotelid;
    }

    public String getOperationerid() {
        return this.operationerid;
    }

    public void setOperationerid(String operationerid) {
        this.operationerid = operationerid;
    }

}
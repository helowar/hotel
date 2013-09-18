package com.mangocity.hotel.base.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 */
public class HtlBookSetup implements Entity {

    private long bookID;

    private Long ID;

    // private long hotelID;

    private String bookChnName;

    /**
     * 指定星期
     */
    private String weeks;

    /**
     * 开始日期
     */
    private Date bookBeginDate;

    /**
     * 结束日期
     */
    private Date bookEndDate;

    /**
     * 开始时间
     */
    private String bookBeginTime;

    /**
     * 结束时间
     */
    private String bookEndTime;

    /**
     * 传真号
     */
    private String bookfax;

    /**
     * 电邮
     */
    private String bookemail;

    /**
     * 联系人
     */
    private String bookctctName;

    /**
     * 默认联系方式
     */
    private String bookctctType;

    /**
     * 说明
     */
    private String bookRemark;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建人ID
     */
    private String createById;

    /**
     * 修改人
     */
    private String modifyBy;

    /**
     * 修改人ID
     */
    private String modifyById;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 是否有效
     */
    private String active;

    private HtlHotel htlHotel;

    public HtlHotel getHtlHotel() {
        return htlHotel;
    }

    public void setHtlHotel(HtlHotel htlHotel) {
        this.htlHotel = htlHotel;
    }

    public String getWeeks() {
        return weeks;
    }

    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }

    public Date getBookBeginDate() {
        return bookBeginDate;
    }

    public void setBookBeginDate(Date bookBeginDate) {
        this.bookBeginDate = bookBeginDate;
    }

    public String getBookBeginTime() {
        return bookBeginTime;
    }

    public void setBookBeginTime(String bookBeginTime) {
        this.bookBeginTime = bookBeginTime;
    }

    public Date getBookEndDate() {
        return bookEndDate;
    }

    public void setBookEndDate(Date bookEndDate) {
        this.bookEndDate = bookEndDate;
    }

    public String getBookEndTime() {
        return bookEndTime;
    }

    public void setBookEndTime(String bookEndTime) {
        this.bookEndTime = bookEndTime;
    }

    public String getBookRemark() {
        return bookRemark;
    }

    public void setBookRemark(String bookRemark) {
        this.bookRemark = bookRemark;
    }

    public String getBookChnName() {
        return bookChnName;
    }

    public void setBookChnName(String bookChnName) {
        this.bookChnName = bookChnName;
    }

    public String getBookctctName() {
        return bookctctName;
    }

    public void setBookctctName(String bookctctName) {
        this.bookctctName = bookctctName;
    }

    public String getBookctctType() {
        return bookctctType;
    }

    public void setBookctctType(String bookctctType) {
        this.bookctctType = bookctctType;
    }

    public String getBookemail() {
        return bookemail;
    }

    public void setBookemail(String bookemail) {
        this.bookemail = bookemail;
    }

    public String getBookfax() {
        return bookfax;
    }

    public void setBookfax(String bookfax) {
        this.bookfax = bookfax;
    }

    public long getBookID() {
        return bookID;
    }

    public void setBookID(long bookID) {
        this.bookID = bookID;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateById() {
        return createById;
    }

    public void setCreateById(String createById) {
        this.createById = createById;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public String getModifyById() {
        return modifyById;
    }

    public void setModifyById(String modifyById) {
        this.modifyById = modifyById;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

}

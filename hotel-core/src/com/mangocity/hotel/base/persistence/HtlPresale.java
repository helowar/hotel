package com.mangocity.hotel.base.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.util.FakeDeletedEntity;

/**
 * 芒果网促销信息主表
 * 
 * @author xiaowumi
 * 
 */
public class HtlPresale extends CEntity implements FakeDeletedEntity {

    // Fields

    // 促销信息ID
    private Long ID;

    // 促销名称
    private String presaleName;

    // 促销内容
    private String presaleContent;

    // 起始日期
    private Date beginDate;

    // 终止时期
    private Date endDate;

    // url地址
    private String url;

    /**
     * 是否在外网显示<br>
     * '0' : 不显示<br>
     * '1' : 显示
     */
    private String webShow;

    /**
     * 是否显示优惠代码 0 不显示 1 显示
     */
    private String isShowPreferentialCode;

    /**
     * 优惠代码
     * 
     */
    private String preferentialCode;

    /**
     * 假删除
     */
    private boolean deleted;

    // 与促销关联的酒店
    private List lstPresaleHotel = new ArrayList();

    // Constructors

    /** default constructor */
    public HtlPresale() {
    }

    // Property accessors
    public Long getID() {
        return this.ID;
    }

    public void setID(Long roomTypeId) {
        this.ID = roomTypeId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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

    public String getPresaleContent() {
        return presaleContent;
    }

    public void setPresaleContent(String presaleContent) {
        this.presaleContent = presaleContent;
    }

    public String getPresaleName() {
        return presaleName;
    }

    public void setPresaleName(String presaleName) {
        this.presaleName = presaleName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List getLstPresaleHotel() {
        return lstPresaleHotel;
    }

    public void setLstPresaleHotel(List lstPresaleHotel) {
        this.lstPresaleHotel = lstPresaleHotel;
    }

    public String getWebShow() {
        return webShow;
    }

    public void setWebShow(String webShow) {
        this.webShow = webShow;
    }

    public String getIsShowPreferentialCode() {
        return isShowPreferentialCode;
    }

    public void setIsShowPreferentialCode(String isShowPreferentialCode) {
        this.isShowPreferentialCode = isShowPreferentialCode;
    }

    public String getPreferentialCode() {
        return preferentialCode;
    }

    public void setPreferentialCode(String preferentialCode) {
        this.preferentialCode = preferentialCode;
    }

}

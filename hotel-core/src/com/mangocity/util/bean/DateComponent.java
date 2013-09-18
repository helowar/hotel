package com.mangocity.util.bean;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class DateComponent implements Serializable {

    private Long id;

    private Date beginDate;

    private Date endDate;

    private Date createDate;

    private Date modifyDate;

    /**
     * hotel 2.9.2 提示信息CC和网站显示需要的字段 add by shengwei.zuo 2009-08-19 begin
     */
    private Date isWeekDate;

    private String tipInfo;
    
    /**
     * hotel 2.9.3 立减优惠同个时间段，不同星期，不用覆盖。加入星期参数作为判断条件。
     */
    private String weeks;

    // add by shengwei.zuo 2009-08-19 end

    public DateComponent() {

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Date getIsWeekDate() {
        return isWeekDate;
    }

    public void setIsWeekDate(Date isWeekDate) {
        this.isWeekDate = isWeekDate;
    }

    public String getTipInfo() {
        return tipInfo;
    }

    public void setTipInfo(String tipInfo) {
        this.tipInfo = tipInfo;
    }

	public String getWeeks() {
		return weeks;
	}

	public void setWeeks(String weeks) {
		this.weeks = weeks;
	}

}

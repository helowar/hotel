package com.mangocity.hotel.order.persistence;

import java.io.Serializable;
import java.util.Date;

import com.mangocity.util.Entity;

/**
 * DaPersonalWorkload entity.
 * 个人工作量表
 * @author MyEclipse Persistence Tools
 */

public class DaPersonalWorkload implements Entity, Serializable {

    // Fields

    private Long ID;
    /**
	 * 审核(回访)人ID
	 */
    private String auditorid;
    /**
	 * 当天已完成渠道
	 */
    private Long completedChannel;
    /**
	 * 当天已完成酒店
	 */
    private Long completedhotel;
    /**
	 * 当天已完成订单
	 */
    private Long completedorder;
    /**
	 * 当天已完成房间
	 */
    private Long completedroom;
    /**
	 * 平均处理时长
	 */
    private String averagetime;
    /**
	 * 日期
	 */
    private Date auditortime;
    /**
     * 目前花费总时间,单位毫秒
     */
    private Long costTime;
    /**
     *类型,1:审核 2:回访
     */
    private Integer type;
    /**
     * noshow挽回率
     */
    private String noshowRate;
    /**
     * 回访挽回量
     */
    private Integer noshowBackCount;
    /**
     * 回访总量
     */
    private Integer noshowCountAll;
    // Constructors

    /** default constructor */
    public DaPersonalWorkload() {
    	this.completedhotel = 0L;
    	this.completedorder = 0L;
    	this.completedroom = 0L;
    	this.costTime = 0L;
    	this.completedChannel = 0L;
    	this.noshowBackCount = 0;
    	this.noshowCountAll = 0;
    }

    /** minimal constructor */
    public DaPersonalWorkload(Long personalWorkloadId) {
        this.ID = personalWorkloadId;
    }

    /** full constructor */
    public DaPersonalWorkload(Long personalWorkloadId, String auditorid,
            Long completedhotel, Long completedorder, Long completedroom,
            String averagetime, Date auditortime) {
        this.ID = personalWorkloadId;
        this.auditorid = auditorid;
        this.completedhotel = completedhotel;
        this.completedorder = completedorder;
        this.completedroom = completedroom;
        this.averagetime = averagetime;
        this.auditortime = auditortime;
    }

    // Property accessors

    public String getAuditorid() {
        return this.auditorid;
    }

    public void setAuditorid(String auditorid) {
        this.auditorid = auditorid;
    }

    public Long getCompletedhotel() {
        return this.completedhotel;
    }

    public void setCompletedhotel(Long completedhotel) {
        this.completedhotel = completedhotel;
    }

    public Long getCompletedorder() {
        return this.completedorder;
    }

    public void setCompletedorder(Long completedorder) {
        this.completedorder = completedorder;
    }

    public Long getCompletedroom() {
        return this.completedroom;
    }

    public void setCompletedroom(Long completedroom) {
        this.completedroom = completedroom;
    }

    public String getAveragetime() {
        return this.averagetime;
    }

    public void setAveragetime(String averagetime) {
        this.averagetime = averagetime;
    }

    public Date getAuditortime() {
        return this.auditortime;
    }

    public void setAuditortime(Date auditortime) {
        this.auditortime = auditortime;
    }

    public Long getID() {
        // TODO Auto-generated method stub
        return this.ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public Long getCostTime() {
        return costTime;
    }

    public void setCostTime(Long costTime) {
        this.costTime = costTime;
    }

	public Long getCompletedChannel() {
		return completedChannel;
	}

	public void setCompletedChannel(Long completedChannel) {
		this.completedChannel = completedChannel;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getNoshowRate() {
		return noshowRate;
	}

	public void setNoshowRate(String noshowRate) {
		this.noshowRate = noshowRate;
	}

	public Integer getNoshowBackCount() {
		return noshowBackCount;
	}

	public void setNoshowBackCount(Integer noshowBackCount) {
		this.noshowBackCount = noshowBackCount;
	}

	public Integer getNoshowCountAll() {
		return noshowCountAll;
	}

	public void setNoshowCountAll(Integer noshowCountAll) {
		this.noshowCountAll = noshowCountAll;
	}

}
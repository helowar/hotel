package com.mangocity.hotel.base.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 */
@SuppressWarnings("serial")
public class HtlExhibit extends CEntity implements Entity {

    /**
     * 会展信息表主键id
     */
    private Long ID;

    /**
     * 会展城市id
     */
    private String cityid;

    /**
     * 省或区域
     */
    private String state;

    /**
     * 会展类型
     */
    private String exhibitType;

    /**
     * 会展
     */
    private String name;

    /**
     * 展馆
     */
    private String saloon;

    /**
     * 会展开始时间
     */
    private Date begindate;

    /**
     * 会展结束时间
     */
    private Date enddate;

    /**
     * 会展信息
     */
    private String description;
    
    //房态会展用 
    //是否排序有效
    private Long activeForRoomstate;
    
    //会展预警时间设置。7为提前一个星期、14为半个月、30为一个月
    private Long exhibitAlert;
    
    //房控用备注与合约那边的备注不一样
    private String roomstateRemark;
    //房态会展用 end
    
    // 修改人
    private String operator;
    /**
     * 城区
     */
    private String zone;
    /**
     * 商业区
     */
    private String bizZone;

    public String getBizZone() {
		return bizZone;
	}

	public void setBizZone(String bizZone) {
		this.bizZone = bizZone;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public Date getBegindate() {
        return begindate;
    }

    public void setBegindate(Date begindate) {
        this.begindate = begindate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getExhibitType() {
        return exhibitType;
    }

    public void setExhibitType(String exhibitType) {
        this.exhibitType = exhibitType;
    }

    public String getSaloon() {
        return saloon;
    }

    public void setSaloon(String saloon) {
        this.saloon = saloon;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

	public Long getActiveForRoomstate() {
		return activeForRoomstate;
	}

	public void setActiveForRoomstate(Long activeForRoomstate) {
		this.activeForRoomstate = activeForRoomstate;
	}

	public Long getExhibitAlert() {
		return exhibitAlert;
	}

	public void setExhibitAlert(Long exhibitAlert) {
		this.exhibitAlert = exhibitAlert;
	}

	public String getRoomstateRemark() {
		return roomstateRemark;
	}

	public void setRoomstateRemark(String roomstateRemark) {
		this.roomstateRemark = roomstateRemark;
	}

}

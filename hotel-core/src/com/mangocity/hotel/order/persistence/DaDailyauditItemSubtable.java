package com.mangocity.hotel.order.persistence;

import java.io.Serializable;
import java.util.Date;

import com.mangocity.util.Entity;

/**
 * DaDailyauditItemSubtable entity.
 * 日审明细子表
 * @author MyEclipse Persistence Tools
 */

public class DaDailyauditItemSubtable implements Entity, Serializable {

    // Fields

    private Long ID;
    
    private Long dailyauditItemID;
    /**
	 * 入住人姓名
	 */
    private String checkinname;
    /**
	 * 实际入住人姓名
	 */
    private String actualcheckinname;
    /**
	 * 入住时间
	 */
    private Date checkintime;
    /**
	 * 退房时间
	 */
    private Date checkouttime;
    /**
	 * 房型ID
	 */
    private Long roomid;
    /**
	 * 房型名称
	 */
    private String roomname;
    /**
	 * 确认号
	 */
    private String affirmnumber;
    /**
	 * 审核结果
	 */
    private String auditresults;
    /**
	 * 回访结果
	 */
    private String reciprocal;
    /**
	 * 房间号
	 */
    private String roomnumber;
    /**
	 * NO SHOW原因
	 */
    private String noshow;
    /**
	 * 提前退房时间
	 */
    private Date advancecheckouttime;
    /**
	 * 提前退房时间(回访)
	 */
    private Date rtAdvancecheckouttime;
    /**
	 * 审核人ID
	 */
    private String auditorid;
    /**
	 * 审核人姓名
	 */
    private String auditorname;
    /**
	 * 审核时间
	 */
    private Date auditortime;
    /**
	 * 回访人ID
	 */
    private String returnvisitid;
    /**
	 * 回访人姓名
	 */
    private String returnvisitname;
    /**
	 * 回访时间
	 */
    private Date returnvisittime;
    /**
	 * 备注
	 */
    private String remark;
    /**
     * 是否暂存前台
     */
    private Integer isSaveFront;
    /**
     * 房间索引号
     */
    private Integer roomIndex;
    /**
     * noshow原因一级目录
     */
    private Integer noshowCode;
    /**
     * noshow其它
     */
    private String noshEdit;

    // Constructors

    public Integer getIsSaveFront() {
		return isSaveFront;
	}

	public void setIsSaveFront(Integer isSaveFront) {
		this.isSaveFront = isSaveFront;
	}

	public Integer getRoomIndex() {
		return roomIndex;
	}

	public void setRoomIndex(Integer roomIndex) {
		this.roomIndex = roomIndex;
	}

	/** default constructor */
    public DaDailyauditItemSubtable() {
    }

    /** minimal constructor */
    public DaDailyauditItemSubtable(Long dailyauditItemSubtableId) {
        this.ID = dailyauditItemSubtableId;
    }

    /** full constructor */
    public DaDailyauditItemSubtable(Long dailyauditItemSubtableId,
            DaDailyauditItem daDailyauditItem,String checkinname,
            String actualcheckinname, Date checkintime, Date checkouttime,
            Long roomid, String roomname, String affirmnumber,
            String auditresults, String reciprocal, String roomnumber,
            String noshow, Date advancecheckouttime, String auditorid,
            String auditorname, Date auditortime, String returnvisitid,
            String returnvisitname, Date returnvisittime, String remark) {
        this.ID = dailyauditItemSubtableId;
        this.checkinname = checkinname;
        this.actualcheckinname = actualcheckinname;
        this.checkintime = checkintime;
        this.checkouttime = checkouttime;
        this.roomid = roomid;
        this.roomname = roomname;
        this.affirmnumber = affirmnumber;
        this.auditresults = auditresults;
        this.reciprocal = reciprocal;
        this.roomnumber = roomnumber;
        this.noshow = noshow;
        this.advancecheckouttime = advancecheckouttime;
        this.auditorid = auditorid;
        this.auditorname = auditorname;
        this.auditortime = auditortime;
        this.returnvisitid = returnvisitid;
        this.returnvisitname = returnvisitname;
        this.returnvisittime = returnvisittime;
        this.remark = remark;
    }

    // Property accessors

    public String getCheckinname() {
        return this.checkinname;
    }

    public void setCheckinname(String checkinname) {
        this.checkinname = checkinname;
    }

    public String getActualcheckinname() {
        return this.actualcheckinname;
    }

    public void setActualcheckinname(String actualcheckinname) {
        this.actualcheckinname = actualcheckinname;
    }

    public Date getCheckintime() {
        return this.checkintime;
    }

    public void setCheckintime(Date checkintime) {
        this.checkintime = checkintime;
    }

    public Date getCheckouttime() {
        return this.checkouttime;
    }

    public void setCheckouttime(Date checkouttime) {
        this.checkouttime = checkouttime;
    }

    public Long getRoomid() {
        return this.roomid;
    }

    public void setRoomid(Long roomid) {
        this.roomid = roomid;
    }

    public String getRoomname() {
        return this.roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public String getAffirmnumber() {
        return this.affirmnumber;
    }

    public void setAffirmnumber(String affirmnumber) {
        this.affirmnumber = affirmnumber;
    }

    public String getAuditresults() {
        return this.auditresults;
    }

    public void setAuditresults(String auditresults) {
        this.auditresults = auditresults;
    }

    public String getReciprocal() {
        return this.reciprocal;
    }

    public void setReciprocal(String reciprocal) {
        this.reciprocal = reciprocal;
    }

    public String getRoomnumber() {
        return this.roomnumber;
    }

    public void setRoomnumber(String roomnumber) {
        this.roomnumber = roomnumber;
    }

    public String getNoshow() {
        return this.noshow;
    }

    public void setNoshow(String noshow) {
        this.noshow = noshow;
    }

    public Date getAdvancecheckouttime() {
        return this.advancecheckouttime;
    }

    public void setAdvancecheckouttime(Date advancecheckouttime) {
        this.advancecheckouttime = advancecheckouttime;
    }

    public String getAuditorid() {
        return this.auditorid;
    }

    public void setAuditorid(String auditorid) {
        this.auditorid = auditorid;
    }

    public String getAuditorname() {
        return this.auditorname;
    }

    public void setAuditorname(String auditorname) {
        this.auditorname = auditorname;
    }

    public Date getAuditortime() {
        return this.auditortime;
    }

    public void setAuditortime(Date auditortime) {
        this.auditortime = auditortime;
    }

    public String getReturnvisitid() {
        return this.returnvisitid;
    }

    public void setReturnvisitid(String returnvisitid) {
        this.returnvisitid = returnvisitid;
    }

    public String getReturnvisitname() {
        return this.returnvisitname;
    }

    public void setReturnvisitname(String returnvisitname) {
        this.returnvisitname = returnvisitname;
    }

    public Date getReturnvisittime() {
        return this.returnvisittime;
    }

    public void setReturnvisittime(Date returnvisittime) {
        this.returnvisittime = returnvisittime;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getID() {
        // TODO Auto-generated method stub
        return this.ID;
    }

    public void setID(Long id) {
        ID = id;
    }

	public Long getDailyauditItemID() {
		return dailyauditItemID;
	}

	public void setDailyauditItemID(Long dailyauditItemID) {
		this.dailyauditItemID = dailyauditItemID;
	}

	public Integer getNoshowCode() {
		return noshowCode;
	}

	public void setNoshowCode(Integer noshowCode) {
		this.noshowCode = noshowCode;
	}

	public String getNoshEdit() {
		return noshEdit;
	}

	public void setNoshEdit(String noshEdit) {
		this.noshEdit = noshEdit;
	}

	public Date getRtAdvancecheckouttime() {
		return rtAdvancecheckouttime;
	}

	public void setRtAdvancecheckouttime(Date rtAdvancecheckouttime) {
		this.rtAdvancecheckouttime = rtAdvancecheckouttime;
	}

}
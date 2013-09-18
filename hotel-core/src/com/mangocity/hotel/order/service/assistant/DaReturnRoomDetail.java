package com.mangocity.hotel.order.service.assistant;

import java.io.Serializable;
import java.util.Date;

public class DaReturnRoomDetail implements Serializable{
	
	/**
	 * 日审明细子表ID
	 */
    private Long ID;
    /**
	 * 实际入住人姓名
	 */
    private String actualcheckinname;
    /**
     * 房间号
     */
    private String roomnumber;
    /**
     * 确认号
     */
    private String affirmnumber;
    /**
     * 回访结果
     */
    private String returnresults;
    /**
     * 备注 
     */
    private String remark;
    /**
	 * 提前退房时间
	 */
    private Date advancecheckouttime;
    /**
     * noshow原因一级目录
     */
    private Integer noshowCode;
    /**
     * noshow其它
     */
    private String noshEdit;
    /**
     * noshow原因
     */
    private String noshow;
    
    
    /*===get   set===*/
    
	public Long getID() {
		return ID;
	}
	public void setID(Long id) {
		ID = id;
	}
	public String getActualcheckinname() {
		return actualcheckinname;
	}
	public void setActualcheckinname(String actualcheckinname) {
		this.actualcheckinname = actualcheckinname;
	}
	public String getRoomnumber() {
		return roomnumber;
	}
	public void setRoomnumber(String roomnumber) {
		this.roomnumber = roomnumber;
	}
	public String getReturnresults() {
		return returnresults;
	}
	public void setReturnresults(String returnresults) {
		this.returnresults = returnresults;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getAdvancecheckouttime() {
		return advancecheckouttime;
	}
	public void setAdvancecheckouttime(Date advancecheckouttime) {
		this.advancecheckouttime = advancecheckouttime;
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
	public String getNoshow() {
		return noshow;
	}
	public void setNoshow(String noshow) {
		this.noshow = noshow;
	}
	public String getAffirmnumber() {
		return affirmnumber;
	}
	public void setAffirmnumber(String affirmnumber) {
		this.affirmnumber = affirmnumber;
	}

}

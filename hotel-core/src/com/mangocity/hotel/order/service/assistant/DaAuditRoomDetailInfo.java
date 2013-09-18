package com.mangocity.hotel.order.service.assistant;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
/**
 * 日审房间操作结果收集Bean
 * 用于auditDetail.jsp的显示
 * @author chenjuesu
 */
public class DaAuditRoomDetailInfo implements Serializable {

    /**
	 * 日审明细子表ID
	 */
    private Long ID;
    /**
	 * 实际入住人姓名
	 */
    private String actualcheckinname;
    /**
	 * 审核结果
	 */
    private String auditresults;
    /**
	 * 回访结果
	 */
    private String reciprocal;
    /**
     * 确认号
     */
    private String affirmnumber;
    /**
     * 房间号
     */
    private String roomnumber;
    /**
	 * 提前退房时间
	 */
    private Date advancecheckouttime;
    /**
     * 数据更新类型:1:只更新入住结果 2:更新大部分信息
     */
    private int updateDataType;
    /**
     * 订单编号,用来标记此房间属于哪个订单
     */
    private String orderCd;
    /**
     * 是否暂存前台,1 是
     */
    private Integer isSaveFront;
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
    /**
     * 备注
     */
    private String remark;
    
    public Integer getIsSaveFront() {
		return isSaveFront;
	}
	public void setIsSaveFront(Integer isSaveFront) {
		this.isSaveFront = isSaveFront;
	}
	public Date getAdvancecheckouttime() {
        return advancecheckouttime;
    }
    public void setAdvancecheckouttime(Date advancecheckouttime) {
        this.advancecheckouttime = advancecheckouttime;
    }
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
    public String getAuditresults() {
        return auditresults;
    }
    public void setAuditresults(String auditresults) {
        this.auditresults = auditresults;
    }
    public String getAffirmnumber() {
        return affirmnumber;
    }
    public void setAffirmnumber(String affirmnumber) {
        this.affirmnumber = affirmnumber;
    }
    public String getRoomnumber() {
        return roomnumber;
    }
    public void setRoomnumber(String roomnumber) {
        this.roomnumber = roomnumber;
    }
	public String getOrderCd() {
		return orderCd;
	}
	public void setOrderCd(String orderCd) {
		this.orderCd = orderCd;
	}
	public int getUpdateDataType() {
		return updateDataType;
	}
	public void setUpdateDataType(int updateDataType) {
		this.updateDataType = updateDataType;
	}
	public String getNoshow() {
		return noshow;
	}
	public void setNoshow(String noshow) {
		this.noshow = noshow;
	}
	public String getReciprocal() {
		return reciprocal;
	}
	public void setReciprocal(String reciprocal) {
		this.reciprocal = reciprocal;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getNoshEdit() {
		return noshEdit;
	}
	public void setNoshEdit(String noshEdit) {
		this.noshEdit = noshEdit;
	}
	public Integer getNoshowCode() {
		return noshowCode;
	}
	public void setNoshowCode(Integer noshowCode) {
		this.noshowCode = noshowCode;
	}
}

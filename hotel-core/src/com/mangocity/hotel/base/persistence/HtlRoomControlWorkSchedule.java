package com.mangocity.hotel.base.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 * 酒店预定条款模板操作日志明细类
 * 
 * @author lihaibo add by 2009-5-2
 */
public class HtlRoomControlWorkSchedule implements java.io.Serializable  {
	
	//主键id
    private Long workSchedualId;
    
    //开始时间
    private Date beginDate;
    
    //结束时间
    private Date endDate;
    
    //登陆人id
    private String loginName;
    
    //登陆人中文名
    private String userName;
    
    //备注
    private String remark;
    
    //是否有效。0为无效、1为有效
    private String state;
    
    //创建时间
    private Date createTime;
    
    //创建人id
    private String createById;
    
    //创建人姓名
    private String createByName;
    
    //修改人id
    private String modifyById;
    
    //修改时间
    private Date modifyTime;
    
    //修改人姓名
    private String modifyByName;
    
    //房态人员要处理里面所包括城市的酒店
    private String workAreas;
    
    //房态人员负责的组。跟workAreas对应
    private String workGroup;
    
  //房态人员负责的组。跟workAreas对应
    private String workGroupZHValue;
    
    //房控人员排班班次A/B班
    private String onDutyTime;
    
    private String active;
    
    private int paixi;

	public int getPaixi() {
		return paixi;
	}
	public void setPaixi(int paixi) {
		this.paixi = paixi;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
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
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateById() {
		return createById;
	}
	public void setCreateById(String createById) {
		this.createById = createById;
	}
	public String getCreateByName() {
		return createByName;
	}
	public void setCreateByName(String createByName) {
		this.createByName = createByName;
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
	public String getModifyByName() {
		return modifyByName;
	}
	public void setModifyByName(String modifyByName) {
		this.modifyByName = modifyByName;
	}
	public String getWorkAreas() {
		return workAreas;
	}
	public void setWorkAreas(String workAreas) {
		this.workAreas = workAreas;
	}
	public String getWorkGroup() {
		return workGroup;
	}
	public void setWorkGroup(String workGroup) {
		this.workGroup = workGroup;
	}
	public String getOnDutyTime() {
		return onDutyTime;
	}
	public void setOnDutyTime(String onDutyTime) {
		this.onDutyTime = onDutyTime;
	}

	public Long getWorkSchedualId() {
		return workSchedualId;
	}
	public void setWorkSchedualId(Long workSchedualId) {
		this.workSchedualId = workSchedualId;
	}
	public String getWorkGroupZHValue() {
		return workGroupZHValue;
	}
	public void setWorkGroupZHValue(String workGroupZHValue) {
		this.workGroupZHValue = workGroupZHValue;
	}
}

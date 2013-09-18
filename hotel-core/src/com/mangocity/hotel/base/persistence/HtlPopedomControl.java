package com.mangocity.hotel.base.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.util.Entity;


/**
 * 权限实体
 * 
 * @author guzhijie
 *
 */
public class HtlPopedomControl implements Entity {

    // ID <pk>
    private Long ID;

    //登陆人账号
    private String logName;    
    
    //登陆人中文名
    private String  chnName;
    
    //创建时间
    private Date createTime;
    
    //修改时间
    private Date modifyTime;
    
    private String controlType;
    
    //房态人员要处理里面所包括城市的酒店
    private String workAreas;
    
    //房态人员负责的组。跟workAreas对应
    private String workGroup;
    
    //房态人员负责的组。跟workAreas对应
    private String workGroupZHValue;

	public String getControlType() {
		return controlType;
	}

	public void setControlType(String controlType) {
		this.controlType = controlType;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long id) {
		ID = id;
	}

	public String getLogName() {
		return logName;
	}

	public void setLogName(String logName) {
		this.logName = logName;
	}

	public String getChnName() {
		return chnName;
	}

	public void setChnName(String chnName) {
		this.chnName = chnName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getWorkGroup() {
		return workGroup;
	}

	public void setWorkGroup(String workGroup) {
		this.workGroup = workGroup;
	}

	public String getWorkAreas() {
		return workAreas;
	}

	public void setWorkAreas(String workAreas) {
		this.workAreas = workAreas;
	}

	public String getWorkGroupZHValue() {
		return workGroupZHValue;
	}

	public void setWorkGroupZHValue(String workGroupZHValue) {
		this.workGroupZHValue = workGroupZHValue;
	}

   

}

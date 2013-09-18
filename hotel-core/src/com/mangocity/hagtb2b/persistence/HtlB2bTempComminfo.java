package com.mangocity.hagtb2b.persistence;

import java.util.Date;

/**
 * B2B代理  代理模板信息 add by zhijie.gu 2010-8-5
 * 
 */

public class HtlB2bTempComminfo implements java.io.Serializable {

    // Fields

    // Id
    private Long id;
    
    // 佣金模板名称
    private String commisionTempName;

    // 备注
    private String remark;
    
    //是否有效
    private int active;

    // 创建人id
    private String createId;

    // 创建人名称
    private String createName;

    // 创建时间
    private Date createTime;

   
    // 修改人id
    private String modifyId;

    // 修改人名称
    private String modifyName;

    // 修改时间
    private Date modifyTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCommisionTempName() {
		return commisionTempName;
	}

	public void setCommisionTempName(String commisionTempName) {
		this.commisionTempName = commisionTempName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getModifyId() {
		return modifyId;
	}

	public void setModifyId(String modifyId) {
		this.modifyId = modifyId;
	}

	public String getModifyName() {
		return modifyName;
	}

	public void setModifyName(String modifyName) {
		this.modifyName = modifyName;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

}
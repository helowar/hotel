package com.mangocity.hotel.base.persistence;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.util.Entity;

/**
 * 供应商 add by xieyanhui
 */

public class HtlSupplierInfo implements Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 供应商ID    
	 */
    private Long ID;

    /**
     * 供应商名称
     */
    private String name;

    /**
     * 联系方式
     */
    private String contactMode;

    /**
     * 是否可用
     */
    private Integer active;

    /**
     * 别名
     */
    private String alias;
    
    /**
     * 供应商联系信息
     */
    private List<HtlSupplierCtct> lstCtct;
    
    /**
     * 供应商传真
     */
    private List<HtlSupplierBookSetup> lstBookSetup;

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

	public String getContactMode() {
		return contactMode;
	}

	public void setContactMode(String contactMode) {
		this.contactMode = contactMode;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public List<HtlSupplierCtct> getLstCtct() {
		if( null == lstCtct){
			lstCtct = new ArrayList<HtlSupplierCtct>();
		}
		return lstCtct;
	}

	public void setLstCtct(List<HtlSupplierCtct> lstCtct) {
		this.lstCtct = lstCtct;
	}

	public List<HtlSupplierBookSetup> getLstBookSetup() {
		if(null == lstBookSetup){
			lstBookSetup = new ArrayList<HtlSupplierBookSetup>();
		}
		return lstBookSetup;
	}

	public void setLstBookSetup(List<HtlSupplierBookSetup> lstBookSetup) {
		this.lstBookSetup = lstBookSetup;
	}
}
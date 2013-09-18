package com.mangocity.tmc.persistence.view;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * TMC附加费用信息
 * @author chenkeming
 *
 */
public class TmcFeeServe implements Serializable {
	/**
	 * 加床服务信息
	 */
	private List bedServes = new ArrayList();
	
	/**
	 * 自助早信息
	 */
	private List buffetServes= new ArrayList();
	
	/**
	 * 中早信息
	 */
	private List chineseServes = new ArrayList();
	
	/**
	 * 西早信息
	 */
	private List westernServes = new ArrayList();
	
	public List getBedServes() {
		return bedServes;
	}
	public void setBedServes(List bedServes) {
		this.bedServes = bedServes;
	}
	public List getBuffetServes() {
		return buffetServes;
	}
	public void setBuffetServes(List buffetServes) {
		this.buffetServes = buffetServes;
	}
	public List getChineseServes() {
		return chineseServes;
	}
	public void setChineseServes(List chineseServes) {
		this.chineseServes = chineseServes;
	}
	public List getWesternServes() {
		return westernServes;
	}
	public void setWesternServes(List westernServes) {
		this.westernServes = westernServes;
	}
    
    /**
     * 是否有加早费用
     * @author chenkeming Aug 5, 2009 2:30:39 PM
     * @return
     */
    public boolean isHasBreakfast() {
        return !chineseServes.isEmpty() || !westernServes.isEmpty() || !buffetServes.isEmpty();
    }
	
}

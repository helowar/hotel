package com.mangocity.webnew.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * 艺龙担保判断结果
 * @author luoguangming
 */
public class ElongAssureResult implements Serializable{
	private static final long serialVersionUID = 1705961780815157829L;
	private boolean isNeedAssure;//是否需要担保,带上房间数，到店时间判断的实际结果
	private int assureType;//根据条款判断担保类型，非实际值，1无条件，2超房，3超时，4既超时又超房
	private String conditionStr;//担保信息
	private String modifyStr;//修改规则
	private int assureMoneyType;//担保金额1首日，2全额
	private int overTimeHour;//超时时间的小时
	private int overTimeMin;//超时时间的分钟
	private int overQtyNum;//超房数
	private Date modifyBeforeDate;//修改在时间之前
	public ElongAssureResult(){
		
	}
	public ElongAssureResult(boolean isNeedAssure, int assureType, String conditionStr,
			String modifyStr, int assureMoneyType, int overTimeHour,
			int overTimeMin, int overQtyNum, Date modifyBeforeDate) {
		super();
		this.isNeedAssure = isNeedAssure;
		this.assureType=assureType;
		this.conditionStr = conditionStr;
		this.modifyStr = modifyStr;
		this.assureMoneyType = assureMoneyType;
		this.overTimeHour = overTimeHour;
		this.overTimeMin = overTimeMin;
		this.overQtyNum = overQtyNum;
		this.modifyBeforeDate = modifyBeforeDate;
	}
	public boolean isNeedAssure() {
		return isNeedAssure;
	}
	public int getAssureType() {
		return assureType;
	}
	public void setAssureType(int assureType) {
		this.assureType = assureType;
	}
	public void setNeedAssure(boolean isNeedAssure) {
		this.isNeedAssure = isNeedAssure;
	}
	public String getConditionStr() {
		return conditionStr;
	}
	public void setConditionStr(String conditionStr) {
		this.conditionStr = conditionStr;
	}
	public String getModifyStr() {
		return modifyStr;
	}
	public void setModifyStr(String modifyStr) {
		this.modifyStr = modifyStr;
	}
	public int getAssureMoneyType() {
		return assureMoneyType;
	}
	public void setAssureMoneyType(int assureMoneyType) {
		this.assureMoneyType = assureMoneyType;
	}
	public int getOverTimeHour() {
		return overTimeHour;
	}
	public void setOverTimeHour(int overTimeHour) {
		this.overTimeHour = overTimeHour;
	}
	public int getOverTimeMin() {
		return overTimeMin;
	}
	public void setOverTimeMin(int overTimeMin) {
		this.overTimeMin = overTimeMin;
	}
	public int getOverQtyNum() {
		return overQtyNum;
	}
	public void setOverQtyNum(int overQtyNum) {
		this.overQtyNum = overQtyNum;
	}
	public Date getModifyBeforeDate() {
		return modifyBeforeDate;
	}
	public void setModifyBeforeDate(Date modifyBeforeDate) {
		this.modifyBeforeDate = modifyBeforeDate;
	}
	
}

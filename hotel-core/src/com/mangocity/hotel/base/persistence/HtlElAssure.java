package com.mangocity.hotel.base.persistence;


/**
 * 艺龙担保对象，提供页面用
 */
public class HtlElAssure {
	/**
	 * 担保类型 0无需担保、1无条件担保、2超房担保、3超时担保、4既需要超时又需要超房担保
	 */
	private int assureType = 0;
	/**
	 * 担保金额
	 */
	private double assureAmount;
	
	/**
	 * 担保日期
	 */
	private String assureDateDay;
	
	/**
	 * 担保时间点（前台展示）
	 */
	private String assureDate;
	
	/**
	 * 担保时间点（后台保存）
	 */
	private String assureSaveDate;
	
	/**
	 * 担保房间数
	 */
	private int assureRoomQuantity;
	
	/**
	 * 取消修改语句
	 */
	private String cancelAndModifySentence;
	
	/**
	 * 担保金额类型 1为首日担保，2为全额担保
	 */
	private long moneyType;
	
	/**
	 * 担保条件
	 */
	private String assureCondition;
	
	/**
	 * 取消条款类型 1：不允许变更取消；2：允许变更/取消,需在XX日YY时之前通知;3：允许变更/取消,需在最早到店时间之前几小时通知 
	 * 4：允许变更/取消,需在到店日期的24点之前几小时通知
	 */
	private String cancelAssureType;
	
	public int getAssureType() {
		return assureType;
	}
	public void setAssureType(int assureType) {
		this.assureType = assureType;
	}
	public double getAssureAmount() {
		return assureAmount;
	}
	public void setAssureAmount(double assureAmount) {
		this.assureAmount = assureAmount;
	}
	public int getAssureRoomQuantity() {
		return assureRoomQuantity;
	}
	public void setAssureRoomQuantity(int assureRoomQuantity) {
		this.assureRoomQuantity = assureRoomQuantity;
	}
	
	public String getAssureDate() {
		return assureDate;
	}
	public void setAssureDate(String assureDate) {
		this.assureDate = assureDate;
	}
	public String getCancelAndModifySentence() {
		return cancelAndModifySentence;
	}
	public void setCancelAndModifySentence(String cancelAndModifySentence) {
		this.cancelAndModifySentence = cancelAndModifySentence;
	}
	@Override
	public String toString() {
		return "HtlElAssure [assureType:"+assureType + ",assureAmount:"+assureAmount+",assureDate:"+assureDate+",assureRoomQuantity:"+assureRoomQuantity+",cancelAndModifySentence:"+cancelAndModifySentence+"]";
	}
	public long getMoneyType() {
		return moneyType;
	}
	public void setMoneyType(long moneyType) {
		this.moneyType = moneyType;
	}
	public String getAssureCondition() {
		return assureCondition;
	}
	public void setAssureCondition(String assureCondition) {
		this.assureCondition = assureCondition;
	}
	public String getAssureDateDay() {
		return assureDateDay;
	}
	public void setAssureDateDay(String assureDateDay) {
		this.assureDateDay = assureDateDay;
	}
	public String getCancelAssureType() {
		return cancelAssureType;
	}
	public void setCancelAssureType(String cancelAssureType) {
		this.cancelAssureType = cancelAssureType;
	}
	public String getAssureSaveDate() {
		return assureSaveDate;
	}
	public void setAssureSaveDate(String assureSaveDate) {
		this.assureSaveDate = assureSaveDate;
	}

}

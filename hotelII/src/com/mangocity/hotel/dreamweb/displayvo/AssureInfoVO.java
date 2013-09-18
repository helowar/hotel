package com.mangocity.hotel.dreamweb.displayvo;

/**
 * 
 * TODO 用于记录担保相关的信息，担保类型，根据担保类型的进行友好提示语
 * @version   Revision History
 * <pre>
 * Author     Version       Date        Changes
 * liting    1.0           Feb 27, 2012     Created
 *
 * </pre>
 * @since 1.
 */
public class AssureInfoVO {

	/**
	 * 担保类型：0：无担保；1：无条件担保；2：超时担保;3:超房担保；4：超间夜担保；5:艺龙担保的既超时又超房量担保
	 */
	private int assureType;
	
	/**
	 * 担保提示语
	 */
	private String assureHintStr;
	
	/**
	 * 担保金额
	 */
	private double  assureMoney;
		
	/**
	 * 第一个保留时间
	 */
	private String firstRetentionTime;
	private String midFirstRetentionTime;
	private String midSecondRetentionTime;
	
	/**
	 * 第二个保留时间
	 */
	private String secondRetentionTime;
	
	/**
	 * 艺龙超房量担保的房量
	 */
	private int maxRoomNum;

	public int getAssureType() {
		return assureType;
	}

	public void setAssureType(int assureType) {
		this.assureType = assureType;
	}

	public String getAssureHintStr() {
		return assureHintStr;
	}

	public void setAssureHintStr(String assureHintStr) {
		this.assureHintStr = assureHintStr;
	}

	public double getAssureMoney() {
		return assureMoney;
	}

	public void setAssureMoney(double assureMoney) {
		this.assureMoney = assureMoney;
	}


	public String getFirstRetentionTime() {
		return firstRetentionTime;
	}

	public void setFirstRetentionTime(String firstRetentionTime) {
		this.firstRetentionTime = firstRetentionTime;
	}

	public String getSecondRetentionTime() {
		return secondRetentionTime;
	}

	public void setSecondRetentionTime(String secondRetentionTime) {
		this.secondRetentionTime = secondRetentionTime;
	}

	public int getMaxRoomNum() {
		return maxRoomNum;
	}

	public void setMaxRoomNum(int maxRoomNum) {
		this.maxRoomNum = maxRoomNum;
	}

	public String getMidFirstRetentionTime() {
		return midFirstRetentionTime;
	}

	public void setMidFirstRetentionTime(String midFirstRetentionTime) {
		this.midFirstRetentionTime = midFirstRetentionTime;
	}

	public String getMidSecondRetentionTime() {
		return midSecondRetentionTime;
	}

	public void setMidSecondRetentionTime(String midSecondRetentionTime) {
		this.midSecondRetentionTime = midSecondRetentionTime;
	}
	

	
	

}

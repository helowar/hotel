
package com.mangocity.hagtb2b.persistence;

import java.util.Date;

import com.mangocity.hotel.order.constant.OrderItemType;
import com.mangocity.util.Entity;
import com.mangocity.util.hotel.constant.QuotaType;

/** 
 *  
 *  
 *  @author yong.zeng
 */
public class AgentOrderItemStatis  {

	private static final long serialVersionUID = 4456235021667062869L;

	/**
	 * Night (短日期)
	 */
	private Date night;

	
	/**
	 * 房间号
	 */
	private String roomNo;
	
	/*
	 * 是订单的第几间房(zero based)
	 */
	private int roomIndex;
	
	/**
	 * 是订单的第几天
	 */
	private int dayIndex;

    
	    //代理 佣金
	    private  double agentComission;
	    
	    //代理佣金价
	    private  double agentComissionPrice;
	    
	    //代理佣金率
	    private  double agentComissionRate;
	    
	    //显示给在代理系统的代理 佣金 add by zhijie.gu 2010-3-17
	    private  double agentReadComission;
	    
	    //显示给在代理系统的代理佣金价
	    private  double agentReadComissionPrice;
	    
	    //显示给在代理系统的代理佣金率
	    private  double agentReadComissionRate;
	    /**
	     * 补助之前该间夜的佣金
	     */
	    private double subsidyBeforeComission;
	    


		public Date getNight() {
			return night;
		}

		public void setNight(Date night) {
			this.night = night;
		}

		public String getRoomNo() {
			return roomNo;
		}

		public void setRoomNo(String roomNo) {
			this.roomNo = roomNo;
		}

		public int getRoomIndex() {
			return roomIndex;
		}

		public void setRoomIndex(int roomIndex) {
			this.roomIndex = roomIndex;
		}

		public double getAgentComission() {
			return agentComission;
		}

		public void setAgentComission(double agentComission) {
			this.agentComission = agentComission;
		}

		public double getAgentComissionPrice() {
			return agentComissionPrice;
		}

		public void setAgentComissionPrice(double agentComissionPrice) {
			this.agentComissionPrice = agentComissionPrice;
		}

		public double getAgentComissionRate() {
			return agentComissionRate;
		}

		public void setAgentComissionRate(double agentComissionRate) {
			this.agentComissionRate = agentComissionRate;
		}

	
		public double getAgentReadComission() {
			return agentReadComission;
		}

		public void setAgentReadComission(double agentReadComission) {
			this.agentReadComission = agentReadComission;
		}

		public double getAgentReadComissionPrice() {
			return agentReadComissionPrice;
		}

		public void setAgentReadComissionPrice(double agentReadComissionPrice) {
			this.agentReadComissionPrice = agentReadComissionPrice;
		}

		public double getAgentReadComissionRate() {
			return agentReadComissionRate;
		}

		public void setAgentReadComissionRate(double agentReadComissionRate) {
			this.agentReadComissionRate = agentReadComissionRate;
		}



		public double getSubsidyBeforeComission() {
			return subsidyBeforeComission;
		}

		public void setSubsidyBeforeComission(double subsidyBeforeComission) {
			this.subsidyBeforeComission = subsidyBeforeComission;
		}

		public int getDayIndex() {
			return dayIndex;
		}

		public void setDayIndex(int dayIndex) {
			this.dayIndex = dayIndex;
		}

	

}

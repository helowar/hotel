package com.mangocity.hotel.base.persistence;

import java.io.Serializable;


/**
 * B2B 代理 佣金 的辅助类 
 * 
 * @author zuoshengwei.zuo  2010-1-13
 * 
 */
public class B2BAgentCommUtils extends CEntity implements Serializable {
    
    /**
     * B2B 代理 add by shengwei.zuo 2010-1-13 begin
     */
    
	    //实际的代理 佣金
	    private  double agentComission;
	    
	    //实际的代理佣金价
	    private  double agentComissionPrice;
	    
	    //实际的代理佣金率
	    private  double agentComissionRate;
	    
	    //显示在代理系统上给代理看的代理 佣金
	    private  double agentReadComission;
	    
	    //显示在代理系统上给代理看的代理佣金价
	    private  double agentReadComissionPrice;
	    
	    //显示在代理系统上给代理看的代理佣金率
	    private  double agentReadComissionRate;
	    
	    //佣金税率
	    private  double commTax ;
	    
	    //无用字段，从B2B代理开始，用来表示 佣金类型  2010-1-13
	    private  int  comissionType  ;
	    
	    //无用字段，从B2B代理开始，用来表示 佣金类型对应的值  2010-1-13
	    private  String comissionTypeValue;
    
    /**
     * B2B 代理 add by shengwei.zuo 2010-1-13 end
     */
    

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

	public double getCommTax() {
		return commTax;
	}

	public void setCommTax(double commTax) {
		this.commTax = commTax;
	}

	public int getComissionType() {
		return comissionType;
	}

	public void setComissionType(int comissionType) {
		this.comissionType = comissionType;
	}

	public String getComissionTypeValue() {
		return comissionTypeValue;
	}

	public void setComissionTypeValue(String comissionTypeValue) {
		this.comissionTypeValue = comissionTypeValue;
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

 

}

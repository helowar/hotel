package com.mangocity.hagtb2b.condition;

import com.mangocity.util.DateUtil;

public class StaticInfoQueryCon {
	
	private String statIds;
	
	private String statYear;
	
	private String statMonth;
	
	private String agentCode;
	
	private String agentName;
	
	private int confirmed = 0;
	
	//操作人ID；
	private String operId;

	public String getAgentCode() {
		return agentCode;
	}

	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public int getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(int confirmed) {
		this.confirmed = confirmed;
	}

	public String getStatIds() {
		return statIds;
	}

	public void setStatIds(String statIds) {
		this.statIds = statIds;
	}


	public String getStatYear() {
		if(null==this.statYear)
			statYear = DateUtil.dateToString(DateUtil.getSystemDate());
		return statYear;
	}

	public void setStatYear(String statYear) {
		this.statYear = statYear;
	}

	public String getStatMonth() {
		return statMonth;
	}

	public void setStatMonth(String statMonth) {
		this.statMonth = statMonth;
	}

	public String getOperId() {
		return operId;
	}

	public void setOperId(String operId) {
		this.operId = operId;
	}

	
}

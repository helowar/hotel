package com.mangocity.hdl.hotel.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", 
		propOrder = { 
		"result", 
		"message", 
		"ratePlanResponses" 
		}
)
@XmlRootElement(name = "QueryPriceTypeResponse")
public class QueryPriceTypeResponse {
	protected int result = 1;
	protected String message;
	@XmlElement(nillable = true)
	protected List<RatePlanResponse>  ratePlanResponses;
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<RatePlanResponse> getRatePlanResponses() {
		return ratePlanResponses;
	}
	public void setRatePlanResponses(List<RatePlanResponse> ratePlanResponses) {
		this.ratePlanResponses = ratePlanResponses;
	}
}

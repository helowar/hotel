package com.mangocity.hotel.search.log;

import com.mangocity.util.Entity;

public class HtlQueryspeedLog  implements Entity{

	private Long ID;
	
	private String city;
	private Integer days;
	private String querycondition;
	private Long lucene_times;
	private Long pricebreakfast_times;
	private Long sort_times;
	private Long commodityquery_times;
	private Long handler_times;
	private Long alltimes;
	private String webip;
	public Long getID() {
		return ID;
	}
	public void setID(Long id) {
		ID = id;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public String getQuerycondition() {
		return querycondition;
	}
	public void setQuerycondition(String querycondition) {
		this.querycondition = querycondition;
	}
	public Long getLucene_times() {
		return lucene_times;
	}
	public void setLucene_times(Long lucene_times) {
		this.lucene_times = lucene_times;
	}
	public Long getPricebreakfast_times() {
		return pricebreakfast_times;
	}
	public void setPricebreakfast_times(Long pricebreakfast_times) {
		this.pricebreakfast_times = pricebreakfast_times;
	}
	public Long getSort_times() {
		return sort_times;
	}
	public void setSort_times(Long sort_times) {
		this.sort_times = sort_times;
	}
	public Long getCommodityquery_times() {
		return commodityquery_times;
	}
	public void setCommodityquery_times(Long commodityquery_times) {
		this.commodityquery_times = commodityquery_times;
	}
	public Long getHandler_times() {
		return handler_times;
	}
	public void setHandler_times(Long handler_times) {
		this.handler_times = handler_times;
	}
	public Long getAlltimes() {
		return alltimes;
	}
	public void setAlltimes(Long alltimes) {
		this.alltimes = alltimes;
	}
	public String getWebip() {
		return webip;
	}
	public void setWebip(String webip) {
		this.webip = webip;
	}
	
	
	
}

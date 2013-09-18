package com.mangocity.hotel.flight.bean;

public class ConstantUtil {
	private String hubURL;
	private String partnerCode;
	private String secretKey;
	public String getHubURL() {
		return (hubURL==null || "".equals(hubURL)) ? "http://www.mangocity.com/Hub/QueryHotelSaleInfo" : hubURL;
	}
	public void setHubURL(String hubURL) {
		this.hubURL = hubURL;
	}
	public String getPartnerCode() {
		return partnerCode;
	}
	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	
}

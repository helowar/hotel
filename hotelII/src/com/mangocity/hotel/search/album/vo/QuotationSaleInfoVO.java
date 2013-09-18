package com.mangocity.hotel.search.album.vo;

public class QuotationSaleInfoVO {

	private double salePrice;
	
	private double retuanAmout;
	
	private String payMethod;
	
	private String currency;
	
	private boolean canBook;

	public double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

	public double getRetuanAmout() {
		return retuanAmout;
	}

	public void setRetuanAmout(double retuanAmout) {
		this.retuanAmout = retuanAmout;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public boolean isCanBook() {
		return canBook;
	}

	public void setCanBook(boolean canBook) {
		this.canBook = canBook;
	}
	
	
}

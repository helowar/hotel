package com.mangocity.hotel.quotationdisplay.vo;

import java.util.Date;
/***
 * 
 * @author panjianping
 *
 */
/***
 * 某天酒店的平均价格信息
 */
public class HotelAveragePriceVO {
	
	
	private String price; //酒店的平均价格
	private String link = "http://hotel.mangocity.com/hotel-query.shtml";//链接
	private String date;     //表示日期

   public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
   
	
	

}

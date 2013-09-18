package com.mangocity.hotel.search.model;

/**
 *提供转换json格式的对象 以便页面传递数据
 */
public class HotelPictureInfoJson {
	/**
	 * 小图地址
	 */
	private String smallSrc;
	
	/**
	 * 小图名称
	 */
	private String smallInfo;
	
	/**
	 * 大图地址
	 */
	private String bigSrc;
	
	/**
	 * 大图片名称
	 */
	private String bigInfo;

	public String getSmallSrc() {
		return smallSrc;
	}

	public void setSmallSrc(String smallSrc) {
		this.smallSrc = smallSrc;
	}

	public String getSmallInfo() {
		return smallInfo;
	}

	public void setSmallInfo(String smallInfo) {
		this.smallInfo = smallInfo;
	}

	public String getBigSrc() {
		return bigSrc;
	}

	public void setBigSrc(String bigSrc) {
		this.bigSrc = bigSrc;
	}

	public String getBigInfo() {
		return bigInfo;
	}

	public void setBigInfo(String bigInfo) {
		this.bigInfo = bigInfo;
	}
	
	
}

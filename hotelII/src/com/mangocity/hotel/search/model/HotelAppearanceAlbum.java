package com.mangocity.hotel.search.model;

/**
 *酒店外观封面图
 *
 */
public class HotelAppearanceAlbum {
	
	/**
	 * 酒店ID
	 */
	private long hotelId;
	/**
	 * 图片ID
	 */
	private long pictureId;
	
	/**
	 * 图片类型
	 */
	private int pictureType;
	
	/**
	 * 图片地址
	 */
	private String prictureUrl;



	public long getPictureId() {
		return pictureId;
	}

	public void setPictureId(long pictureId) {
		this.pictureId = pictureId;
	}

	public int getPictureType() {
		return pictureType;
	}

	public void setPictureType(int pictureType) {
		this.pictureType = pictureType;
	}

	public String getPrictureUrl() {
		return prictureUrl;
	}

	public void setPrictureUrl(String prictureUrl) {
		this.prictureUrl = prictureUrl;
	}

	public long getHotelId() {
		return hotelId;
	}

	public void setHotelId(long hotelId) {
		this.hotelId = hotelId;
	}
	
	
	
	
}

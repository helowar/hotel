package com.mangocity.hotel.dreamweb.displayvo;

public class HtlPictureSizeVO {
	/**
	 * 图片尺寸 2(500*333) 3(300*200) 4(120*80) 5(90*60)
	 */
	private int pictureType;
	
	/**
	 * 图片地址
	 */
	private String pictureUrl;

	public int getPictureType() {
		return pictureType;
	}

	public void setPictureType(int pictureType) {
		this.pictureType = pictureType;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	
	
}

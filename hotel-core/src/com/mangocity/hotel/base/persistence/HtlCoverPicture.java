package com.mangocity.hotel.base.persistence;

import java.io.Serializable;

public class HtlCoverPicture implements Serializable{

	private static final long serialVersionUID = -2436546446651892855L;
	/**
	 * 图片ID
	 */
    private long pictureId;
	/**
	 * 酒店ID
	 */
	private long hotelId;
	/**
	 * 相册名称（房型相册时取房型名称）
	 */
	private String pictureName;
	/**
	 * 封面类型
	 */
	private int classify;
	public long getPictureId() {
		return pictureId;
	}
	public void setPictureId(long pictureId) {
		this.pictureId = pictureId;
	}
	public long getHotelId() {
		return hotelId;
	}
	public void setHotelId(long hotelId) {
		this.hotelId = hotelId;
	}
	public String getPictureName() {
		return pictureName;
	}
	public void setPictureName(String pictureName) {
		this.pictureName = pictureName;
	}
	public int getClassify() {
		return classify;
	}
	public void setClassify(int classify) {
		this.classify = classify;
	}

}

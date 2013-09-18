package com.mangocity.hotel.base.persistence;

import java.io.Serializable;

public class HtlPictureUrl implements Serializable{

	private static final long serialVersionUID = -226332084317878602L;
	/**
	 * 图片id
	 */
	private long pictureId;	
	/**
	 * 图片尺寸类型（1.500*333大图 2.300*200中图 3.120*80小图 4.90*60缩略图）
	 */
	private int pictureType;
	/**
	 * 图片保存路径
	 */
	private String pictureURL;
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
	public String getPictureURL() {
		return pictureURL;
	}
	public void setPictureURL(String pictureURL) {
		this.pictureURL = pictureURL;
	}
}

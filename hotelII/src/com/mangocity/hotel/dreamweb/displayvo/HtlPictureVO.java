package com.mangocity.hotel.dreamweb.displayvo;

import java.util.List;

public class HtlPictureVO {
	private long pictureId;
	
	private String pictureName;
	
	private List<HtlPictureSizeVO> htlPictureSizeVOList;

	public long getPictureId() {
		return pictureId;
	}

	public void setPictureId(long pictureId) {
		this.pictureId = pictureId;
	}

	public String getPictureName() {
		return pictureName;
	}

	public void setPictureName(String pictureName) {
		this.pictureName = pictureName;
	}

	public List<HtlPictureSizeVO> getHtlPictureSizeVOList() {
		return htlPictureSizeVOList;
	}

	public void setHtlPictureSizeVOList(List<HtlPictureSizeVO> htlPictureSizeVOList) {
		this.htlPictureSizeVOList = htlPictureSizeVOList;
	}
	
	
}

package com.mangocity.hotel.common.vo;

import com.mangocity.hotel.search.vo.SerializableVO;

public class HotelPictureVO implements SerializableVO {

	public HotelPictureVO() {}

	protected long picId;

	/**
	 * 图片类型 1,酒店logo 2,酒店图片 3,房间图片 4,360图片
	 */
	protected int picType;
	/**
	 * 图片名称
	 */
	protected String picName;
	/**
	 * 图片地址
	 */
	protected String picURL;

	/**
	 * 图片说明
	 */

	protected String remark;

	public long getPicId() {
		return picId;
	}

	public void setPicId(long picId) {
		this.picId = picId;
	}

	public int getPicType() {
		return picType;
	}

	public void setPicType(int picType) {
		this.picType = picType;
	}

	public String getPicName() {
		return picName;
	}

	public void setPicName(String picName) {
		this.picName = picName;
	}

	public String getPicURL() {
		return picURL;
	}

	public void setPicURL(String picURL) {
		this.picURL = picURL;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}

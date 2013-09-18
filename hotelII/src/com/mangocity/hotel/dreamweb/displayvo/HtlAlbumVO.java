package com.mangocity.hotel.dreamweb.displayvo;

import java.util.List;

public class HtlAlbumVO {
	/**
	 * 相册分类
	 */
	private int albumClassify;
	
	/**
	 * 相册名称  
	 */
	private String albumName;
	
	/**
	 * 相册封面ID 
	 */
	private long albumCoverId;
	
	/**
	 * 相册封面地址120*80图片  
	 */
	private String albumCoverUrl;
	
	/**
	 * 图片信息
	 */
	private List<HtlPictureVO> htlPictureVOList;

	public int getAlbumClassify() {
		return albumClassify;
	}

	public void setAlbumClassify(int albumClassify) {
		this.albumClassify = albumClassify;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public long getAlbumCoverId() {
		return albumCoverId;
	}

	public void setAlbumCoverId(long albumCoverId) {
		this.albumCoverId = albumCoverId;
	}

	public String getAlbumCoverUrl() {
		return albumCoverUrl;
	}

	public void setAlbumCoverUrl(String albumCoverUrl) {
		this.albumCoverUrl = albumCoverUrl;
	}

	public List<HtlPictureVO> getHtlPictureVOList() {
		return htlPictureVOList;
	}

	public void setHtlPictureVOList(List<HtlPictureVO> htlPictureVOList) {
		this.htlPictureVOList = htlPictureVOList;
	}
	
	
}

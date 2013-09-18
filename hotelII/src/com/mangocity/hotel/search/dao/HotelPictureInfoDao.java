package com.mangocity.hotel.search.dao;

import java.util.List;

import com.mangocity.hotel.dreamweb.displayvo.HtlAlbumVO;
import com.mangocity.hotel.search.model.HotelAppearanceAlbum;

/**
 * 酒店对接新图片库
 *
 */
public interface HotelPictureInfoDao {
	/**
	 * 详情页图片查询
	 * @param hotelId  
	 * @return
	 */
	List<HtlAlbumVO> queryHotelPictureInfo(Long hotelId);
	
	
	/**
	 * 查询酒店外观封面图片
	 * @param hotelIds
	 * @return
	 */
	List<HotelAppearanceAlbum> queryAppearanceAlbum(String hotelIds);
}

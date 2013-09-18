package com.mangocity.hotel.base.dao;

import com.mangocity.hotel.base.persistence.HtlFavourable;
import com.mangocity.hotel.base.persistence.HtlHotel;

public interface LimitFavKeyWordDao {
	/**
	 * 查询htl_favourable表是否存在该酒店记录
	 * @param hotelId
	 * @return
	 */
	public HtlFavourable queryFromHtlFavourable(Long hotelId);
	/**
	 * 删除一条活动标签记录
	 * @param hotelId
	 */
	public int  singleDeleteHtlFavourable(Long Id);
	/**
	 * 新增一条活动标签记录
	 * @param htlFavourable
	 */
	public void insertHtlFacourable(HtlFavourable htlFavourable);
	/**
	 *批量增加活动标签
	 * @param hotelIdStr
	 * @param loginName
	 * @param batch_a
	 * @param batch_b
	 * @param batch_c
	 */
	public void batchSettingKeyWord(String hotelIdStr,String loginName,String fav_a,String fav_b,String fav_c);
	/**
	 *批量取消活动标签
	 * @param hotelIdStr
	 * @param loginName
	 * @param batch_a
	 * @param batch_b
	 * @param batch_c
	 */
	public void batchCancelKeyWord(String hotelIdStr,String loginName,String fav_a,String fav_b,String fav_c);
	/**
	 * 查询酒店基本信息
	 * @param hotelId
	 * @return
	 */
	public HtlHotel queryHotelBaseInfo(Long hotelId);
}

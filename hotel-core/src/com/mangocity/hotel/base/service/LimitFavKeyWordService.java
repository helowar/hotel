package com.mangocity.hotel.base.service;

public interface LimitFavKeyWordService {
	/**
	 * 单个设置酒店活动标签
	 * @param hotelIdStr
	 * @param loginName
	 * @param fav_a
	 * @param fav_b
	 * @param fav_c
	 * @return
	 */
    public Boolean singleSettingKeyWord(String hotelIdStr,String loginName,String fav_a,String fav_b,String fav_c);
	/**
	 * 批量设置活动标签
	 * @param hotelIdStr
	 * @param loginName
	 * @param fav_a
	 * @param fav_b
	 * @param fav_c
	 */
	public Boolean batchSettingKeyWord(String hotelIdStr,String loginName,String fav_a,String fav_b,String fav_c);
	/**
	 * 批量取消活动标签
	 * @param hotelIdStr
	 * @param loginName
	 * @param fav_a
	 * @param fav_b
	 * @param fav_c
	 */
	public Boolean batchCancelKeyWord(String hotelIdStr,String loginName,String fav_a,String fav_b,String fav_c);

	/**
	 * 设置一个酒店活动标签
	 * @param hotelId
	 * @param loginName
	 * @param fav_a
	 * @param fav_b
	 * @param fav_c
	 */
	public Boolean settingKeyWord(String hotelId,String loginName,String fav_a,String fav_b,String fav_c);
	/**
	 * 取消一个酒店标签
	 * @param hotelIdStr
	 * @param loginName
	 * @param fav_a
	 * @param fav_b
	 * @param fav_c
	 * @return
	 */
	public Boolean cancelKeyWord(String hotelIdStr,String loginName,String fav_a,String fav_b,String fav_c);
}

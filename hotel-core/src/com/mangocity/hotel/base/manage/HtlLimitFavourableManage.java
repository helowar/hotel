package com.mangocity.hotel.base.manage;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.persistence.HtlLimitFavourable;
import com.mangocity.hotel.base.persistence.HtlLimitFavourableHotel;

public interface HtlLimitFavourableManage {
	void save(HtlLimitFavourable htlLimitFavourable);
	
	/**
	 * 查询酒店在一段时间内的返限活动
	 */
	List<HtlLimitFavourableHotel> queryLimitFavourableHotel(String hotelIdList,Date beginDate,Date endDate);
	
	/**
	 * 判断是否参加限量返现活动
	 * @param hotelId
	 * @param checkIn 入住日期
	 * @param checkOut 离店日期
	 * @return
	 */
	Boolean judgeLimitFav(Long hotelId,Date checkIn,Date checkOut);
	
	/**
	 * 查询返现的比例
	 * @param hotelId
	 * @param date
	 * @return
	 */
	BigDecimal queryLimitFavRate(Long hotelId,Date date);
	
	void deleteFav(Long favId);

	HtlLimitFavourable queryLimitFav(Long favId);
	
	/**
	 * 开始结束 活动
	 * @param favId
	 */
	void favActiviyBeginOrEnd(Long favId,String status);
	/**
	 * 查询预定间夜量 总返现金额
	 * @param favId
	 * @return
	 */
	String getRoomNight_ReturnCash(Long favId);
	
	/**
	 * 判断活动是否开始
	 * @param favId
	 * @return
	 */
	Boolean judgeFavStart(Long favId);
	/**
	 * 判断已是否有其它活动开始
	 * @return
	 */
	Boolean isHaveAnotherActivityBegin(Long favId);
	
	/**
	 * 计算某价格类型现金返还金额
	 * 
	 */
	public int calculateCashLimitReturnAmount(long hotelId,long priceTypeId, Date ableDate, String currency,
			  BigDecimal salePrice,Double commission);

}

package com.mangocity.hotel.base.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlFavourableDecrease;

/**
 * 优惠立减服务接口 2009-10-15
 * @author chenjiajie
 *
 */
public interface IBenefitService {
	
	/**
	 * 查询某时间段的优惠立减信息
	 * @param childRoomTypeId
	 * @param checkInDate
	 * @param checkOutDate
	 * @return
	 */
	public List<HtlFavourableDecrease> queryBenefitByDate(String childRoomTypeId, Date checkInDate,Date checkOutDate);
	
	/**
	 * 计算某价格类型在入住时间段的优惠总金额 (参与币种换算)
	 * @param childRoomTypeId 价格类型id
	 * @param checkInDate 入住日期
	 * @param checkOutDate 离店日期
	 * @param roomQuantity 预订房间数
	 * @param currency 币种
	 * @return 计算得出的优惠总金额
	 */
	public int calculateBenefitAmount(String childRoomTypeId,Date checkInDate,Date checkOutDate,int roomQuantity,String currency);
	
	/**
	 * 计算某价格类型在入住时间段的优惠总金额 (不参与币种换算)
	 * @param childRoomTypeId 价格类型id
	 * @param checkInDate 入住日期
	 * @param checkOutDate 离店日期
	 * @param roomQuantity 预订房间数
	 * @return 计算得出的优惠总金额
	 */
	public int calculateBenefitAmount(String childRoomTypeId,Date checkInDate,Date checkOutDate,int roomQuantity);
	
	
	
	/**
	 * 批量查询某时间段的优惠立减信息，缓存到Map中 key是价格类型id
	 * @param childRoomTypeId 批量价格类型ID，用,分隔
	 * @param checkInDate
	 * @param checkOutDate
	 * @return
	 */
	public Map<String,List<HtlFavourableDecrease>> queryBatchBenefitByDate(String childRoomTypeId, Date checkInDate,Date checkOutDate);
	
	/**
	 * 计算某价格类型在入住时间段的优惠总金额 (参与币种换算)
	 * @param childRoomTypeId 价格类型id
	 * @param checkInDate 入住日期
	 * @param checkOutDate 离店日期
	 * @param roomQuantity 预订房间数
	 * @param currency 币种
	 * @param decreaseMap 缓存的优惠立减结果集
	 * @return 计算得出的优惠总金额
	 */
	public int calculateBenefitAmount(String childRoomTypeId,Date checkInDate,Date checkOutDate,int roomQuantity,String currency,Map<String,List<HtlFavourableDecrease>> decreaseMap);
}

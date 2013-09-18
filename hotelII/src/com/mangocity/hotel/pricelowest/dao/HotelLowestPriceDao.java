package com.mangocity.hotel.pricelowest.dao;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.pricelowest.persistence.HtlLowestPrice;
import com.mangocity.hotel.pricelowest.persistence.HtlLowestTask;

/**
 * @author houdiandian 2012-2-14
 *
 */
public interface HotelLowestPriceDao {

	
	/**
	 * 获取所有有效的酒店id
	 */
	public List<Object[]> getActiveHotelIds();
	
	/**
	 * 添加任务表
	 */
	public void addHtlLowestTasks(List<HtlLowestTask> lowestTaskList);
	
	/**
	 * 跟新任务表
	 */
	public void updateHtlLowestTasks(List<HtlLowestTask> lowestTaskList);
	
	/**
	 * 获得任务表
	 */	
	public List<HtlLowestTask> getHtlLowestTasks(String taskIds);
	
	/**
	 * 获得任务表
	 */
	public List<HtlLowestTask> gtHtlLowestTasksByHotelIds(String hotelIds);
	
	/**
	 * 获得任务表
	 */
	public List<HtlLowestTask> getHtlLowestTasks(int size);
	
	/**
	 * 删除task表
	 */
	public void deleteAllHtlLowestTask();
	
	//----------------------------------------
    public List<HtlLowestPrice> getHtlLowestPrices(String hotelIds);
	
	/**
	 * 添加最低价表
	 */
	public void addHtlLowestPrice(HtlLowestPrice htlLowestPrice);
	
	/**
	 * 修改最低价表
	 */
	public void updateHtlLowestPrice(HtlLowestPrice htlLowestPrice);
	
	/**
	 * 添加或修改最低价表
	 */
	public void saveOrUpdateHtlLowestPrices(List<HtlLowestPrice> lowestPriceList);
	
	/**
	 * 获得最低价表,取生成js用到的参数
	 */
	public List<Object[]> getHtlLowestPricesForJS(Date date);
	
	
	
}

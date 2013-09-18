package com.mangocity.hotel.pricelowest.dao;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.pricelowest.persistence.HtlHighestReturn;
import com.mangocity.hotel.pricelowest.persistence.HtlHighestReturnTask;
import com.mangocity.hotel.pricelowest.persistence.HtlLowestPrice;

/**
 * @author longkangfu 2012-7-3
 *
 */
public interface HotelHighestReturnDao {

	
	/**
	 * 获取所有有效的酒店id
	 */
	public List<Object[]> getActiveHotelIds();
	
	/**
	 * 添加任务表
	 */
	public void addHtlHighestReturnTasks(List<HtlHighestReturnTask> highestReturnTaskList);
	
	/**
	 * 跟新任务表
	 */
	public void updateHtlHighestReturnTasks(List<HtlHighestReturnTask> highestReturnTaskList);
	
	/**
	 * 获得任务表
	 */	
	public List<HtlHighestReturnTask> getHtlHighestReturnTasks(String taskIds);
	
	/**
	 * 获得任务表
	 */
	public List<HtlHighestReturnTask> getHtlHighestReturnTasksByHotelIds(String hotelIds);
	
	/**
	 * 获得任务表
	 */
	public List<HtlHighestReturnTask> getHtlHighestReturnTasks(int size);
	
	/**
	 * 删除task表
	 */
	public void deleteAllHtlHighestReturnTask();
	
	//----------------------------------------
    public List<HtlHighestReturn> getHtlHighestReturns(String hotelIds);
	
	/**
	 * 添加最低价表
	 */
	public void addHtlHighestReturn(HtlHighestReturn htlHighestReturn);
	
	/**
	 * 修改最低价表
	 */
	public void updateHtlHighestReturn(HtlHighestReturn htlHighestReturn);
	
	/**
	 * 添加或修改最低价表
	 */
	public void saveOrUpdateHtlHighestReturns(List<HtlHighestReturn> htlHighestReturn);
	
	/**
	 * 获得最低价表,取生成js用到的参数
	 */
	public List<Object[]> getHtlHighestReturnsForJS(Date date);
	
}

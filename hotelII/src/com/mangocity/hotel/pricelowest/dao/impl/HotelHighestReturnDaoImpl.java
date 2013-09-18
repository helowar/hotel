package com.mangocity.hotel.pricelowest.dao.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.pricelowest.dao.HotelHighestReturnDao;
import com.mangocity.hotel.pricelowest.persistence.HtlHighestReturn;
import com.mangocity.hotel.pricelowest.persistence.HtlHighestReturnTask;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

/**
 * @author longkfu 2012-7-3
 *
 */
public class HotelHighestReturnDaoImpl extends GenericDAOHibernateImpl implements HotelHighestReturnDao {
	
	//  获取所有有效的酒店id
	public List<Object[]> getActiveHotelIds(){
		String sql ="select h.city,h.hotel_id from htl_hotel h where h.active = 1 and h.city is not null order by h.city";
		List<Object[]> activeHotelsIdList = super.queryByNativeSQL(sql, null);
		return activeHotelsIdList;
	}
	
	//添加任务表
	public void addHtlHighestReturnTasks(List<HtlHighestReturnTask> highestReturnTaskList){
	   super.saveOrUpdateAll(highestReturnTaskList);
   }
	
    //跟新任务表
	public void updateHtlHighestReturnTasks(List<HtlHighestReturnTask> highestReturnTaskList){
		super.saveOrUpdateAll(highestReturnTaskList);
	}
	
	//获得任务表
	public List<HtlHighestReturnTask> getHtlHighestReturnTasks(String taskIds){
		if(null == taskIds || "".equals(taskIds) ){return Collections.EMPTY_LIST;}
		String sql="from HtlHighestReturnTask ht where ht.ID in (" + taskIds +")";
		List<HtlHighestReturnTask> highReturnTaskList = super.query(sql,null);
		return highReturnTaskList;
	}
	
	/**
	 * 获得任务表
	 */
	public List<HtlHighestReturnTask> getHtlHighestReturnTasksByHotelIds(String hotelIds){
		if(hotelIds==null || "".equals(hotelIds)){
			return Collections.EMPTY_LIST;
		}
		String sql="from HtlHighestReturnTask ht where ht.hotelId in (" + hotelIds +")";
		List<HtlHighestReturnTask> highReturnTaskList = super.query(sql,null);
		return highReturnTaskList;
	}
	
    //获得任务表
	public List<HtlHighestReturnTask> getHtlHighestReturnTasks(int size){
		String sql = "from HtlHighestReturnTask ht where ht.isFinish = 0  and ht.isTaken = 0 order by ht.cityCode";
		List<HtlHighestReturnTask> highReturnTaskList = super.query(sql, null, 0, size, false);
		return highReturnTaskList;
	}
	
	// 删除task表
	public void deleteAllHtlHighestReturnTask(){
		String sql="delete htl_highest_return_task ";
		super.execProcedure(sql, null);
	}
    
	
	//----------------------------------------
	@SuppressWarnings("unchecked")
	public List<HtlHighestReturn> getHtlHighestReturns(String hotelIds){
		 if(null==hotelIds || "".equals(hotelIds)) return Collections.EMPTY_LIST;
	     String sql = "from HtlHighestReturn hl where hl.hotelId in ("+hotelIds+")";
	     List<HtlHighestReturn> highestReturnList = super.query(sql,null);
	     return highestReturnList;
	}
	
	//
	public void addHtlHighestReturn(HtlHighestReturn htlHighestReturn){
		
	}
	
    //
	public void updateHtlHighestReturn(HtlHighestReturn htlHighestReturn){
		super.update(htlHighestReturn);
	}
	
	//添加或修改最低价表
	public void saveOrUpdateHtlHighestReturns(List<HtlHighestReturn> htlHighestReturn){
		super.saveOrUpdateAll(htlHighestReturn);
	}
	
    //获得最低价表,取生成js用到的参数
	public List<Object[]> getHtlHighestReturnsForJS(Date date){
		String sql = "select lp.hotel_id,lp.hotel_name,lp.sale_price,lp.salw_price_currency,lp.return_cash from htl_highest_return lp where lp.able_date >= ?";
		List<Object[]> lowestPriceList = super.queryByNativeSQL(sql,new Object[]{date});
		return lowestPriceList;
	}

}

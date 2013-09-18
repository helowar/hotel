package com.mangocity.hotel.pricelowest.dao.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.pricelowest.persistence.HtlLowestPrice;
import com.mangocity.hotel.pricelowest.persistence.HtlLowestTask;
import com.mangocity.hotel.pricelowest.dao.HotelLowestPriceDao;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

/**
 * @author houdiandian 2012-2-14
 *
 */
public class HotelLowestPriceDaoImpl extends GenericDAOHibernateImpl implements HotelLowestPriceDao {
	
	
	
	//  获取所有有效的酒店id
	public List<Object[]> getActiveHotelIds(){
		String sql ="select h.city,h.hotel_id from htl_hotel h where h.active = 1 and h.city is not null order by h.city";
		List<Object[]> activeHotelsIdList = super.queryByNativeSQL(sql, null);
		return activeHotelsIdList;
	}
	
	//添加任务表
	public void addHtlLowestTasks(List<HtlLowestTask> lowestTaskList){
	   super.saveOrUpdateAll(lowestTaskList);
   }
	
    //跟新任务表
	public void updateHtlLowestTasks(List<HtlLowestTask> lowestTaskList){
		super.saveOrUpdateAll(lowestTaskList);
	}
	
	//获得任务表
	public List<HtlLowestTask> getHtlLowestTasks(String taskIds){
		if(null == taskIds || "".equals(taskIds) ){return Collections.EMPTY_LIST;}
		String sql="select * from htl_lowest_task ht where ht.htllowesttaskid in (" + taskIds +")";
		List<HtlLowestTask> lowestTaskList = super.queryByNativeSQL(sql, null,HtlLowestTask.class);
		return lowestTaskList;
	}
	
	/**
	 * 获得任务表
	 */
	public List<HtlLowestTask> gtHtlLowestTasksByHotelIds(String hotelIds){
		if(hotelIds==null || "".equals(hotelIds)){
			return Collections.EMPTY_LIST;
		}
		String sql="select * from htl_lowest_task ht where ht.hotelId in (" + hotelIds +")";
		List<HtlLowestTask> lowestTaskList = super.queryByNativeSQL(sql, null,HtlLowestTask.class);
		return lowestTaskList;
	}
	
    //获得任务表
	public List<HtlLowestTask> getHtlLowestTasks(int size){
		String sql = "select * from htl_lowest_task ht where ht.isfinish = 0 and ht.istaken = 0 order by ht.cityCode";
		//TODO test用
		//sql = "select * from htl_lowest_task ht where ht.hotelid=30004202 order by ht.cityCode";
		List<HtlLowestTask> lowestTaskList = super.queryByNativeSQL(sql, 0, size , null, HtlLowestTask.class);
		return lowestTaskList;
	}
	
	// 删除task表
	public void deleteAllHtlLowestTask(){
		String sql="delete htl_lowest_task ";
		super.execProcedure(sql, null);
	}
    
	
	//----------------------------------------
	@SuppressWarnings("unchecked")
	public List<HtlLowestPrice> getHtlLowestPrices(String hotelIds){
		 if(null==hotelIds || "".equals(hotelIds)) return Collections.EMPTY_LIST;
	     String sql = "select * from htl_lowest_price hl where hl.hotelid in ("+hotelIds+")";
	     List<HtlLowestPrice> lowestPriceList = super.queryByNativeSQL(sql, null, HtlLowestPrice.class);
	     return lowestPriceList;
	}
	
	//
	public  void addHtlLowestPrice(HtlLowestPrice htlLowestPrice){
		
	}
	
    //
	public void updateHtlLowestPrice(HtlLowestPrice htlLowestPrice){
		super.update(htlLowestPrice);
	}
	
	//添加或修改最低价表
	public void saveOrUpdateHtlLowestPrices(List<HtlLowestPrice> lowestPriceList){
		super.saveOrUpdateAll(lowestPriceList);
	}
	
    //获得最低价表,取生成js用到的参数
	public List<Object[]> getHtlLowestPricesForJS(Date date){
		String sql = "select lp.hotelid,lp.hotelname,lp.lowestprice,lp.lowestpricecurrency,lp.returncash from htl_lowest_price lp where lp.abledate >= ?";
		List<Object[]> lowestPriceList = super.queryByNativeSQL(sql,new Object[]{date});
		return lowestPriceList;
	}

}

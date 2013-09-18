/**
 * 
 */
package com.mango.hotel.ebooking.dao;

import java.util.List;

import com.mango.hotel.ebooking.persistence.HtlEbookingPriceRedressal;
import com.mangocity.hotel.user.UserWrapper;

/**
 * EBooking调价审核操作DAO interface
 * @author xiongxiaojun
 *
 */
public interface PriceRedressalDao{
	
	/**
	 * 按主键查询
	 * @param id  主键ID
	 * @return
	 * @throws Exception
	 */
	public HtlEbookingPriceRedressal queryPriceRedressalById(long id);
	
	/**
	 * 更新调价审核记录
	 * @param priceRedressalBean 调价记录实体类对象
	 * @throws Exception
	 */
	public void updatePriceRedressal(HtlEbookingPriceRedressal priceRedressalBean) throws Exception;
	
	/**
	 * 根据酒店ID和调价ID找出同一时间提交的审批查询
	 * @param hotelId  酒店ID
	 * @param priceRedressalNameId 调价ID
	 * @return
	 * @throws Exception
	 */
	public List<HtlEbookingPriceRedressal> queryByHotelIdAndPriceThemeId(Long hotelId, Long priceRedressalNameId)throws Exception;
	
	/**
	 * 批量更新调价申请的状态
	 * @param priceRedressalBantchID 调价记录的ID字符串
	 * @param requisitionState 状态
	 * @param user  操作者实体类对象
	 * @throws Exception
	 */
	public void updatePriceRedressal(String priceRedressalBantchID, long requisitionState,UserWrapper user) throws Exception;
	
	/**
	 * 锁定待审核记录，以及解锁待审核记录
	 * @param srcState 原有状态
	 * @param tarState 更新状态
	 * @param hotelId  酒店ID
	 * @param priceRedressalNameId 调价ID
	 * @throws Exception
	 */
	public void updatePriceRedressal(long srcState, long tarState, Long hotelId,Long priceRedressalNameId) throws Exception;
	
	/**
	 * 查询匹配id的调价记录
	 * @param priceRedressalBantchID 调价ID字符串
	 * @return 
	 * @throws Exception
	 */
	public List<HtlEbookingPriceRedressal> queryPriceRedressal(String priceRedressalBantchID)throws Exception;
	
	/**
	 * 批量更新调价实体集合
	 * @param redressals
	 */
	public void saveOrUpdateAllRedressals(List<HtlEbookingPriceRedressal> redressals);
	
	/**
	 * 
	 * @param hql HQL查询语句
	 * @param paramValues  参数数组
	 * @param startIndex  起始索引位置
	 * @param maxResults 要查询的调价记录实体数量
	 * @param cacheable 是否从缓存中查询
	 * @return
	 */
	public List<HtlEbookingPriceRedressal> queryByHql(final String hql, final Object[] paramValues, final int startIndex, 
    		final int maxResults, final boolean cacheable);
	
	/**
	 * 查询没有审核的调价记录数
	 * @param theAreaLoginerCanCheck 酒店区域城市三字码
	 * @return
	 */
	public int getUnAuditPrice(String theAreaLoginerCanCheck);
}

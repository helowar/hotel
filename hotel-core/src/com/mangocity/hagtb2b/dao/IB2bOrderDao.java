package com.mangocity.hagtb2b.dao;

import java.io.Serializable;
import java.util.List;

import com.mangocity.hagtb2b.persistence.HtlB2bTempComminfo;
import com.mangocity.hotel.order.persistence.B2bModifyOrderInfo;
import com.mangocity.hotel.order.persistence.OrOrder;

/**
 * b2b代理商 管理订单Dao类
 * @author shizhongwen
 *
 */
public interface IB2bOrderDao {
	 public void updateOrder(B2bModifyOrderInfo b2border) ;
	 
	 
	 /**
	  * 批量更新佣金计算公式
	  * add by yong.zeng 2010.3.16
	  * @param commLst
	  */
	 public void batchUpdate(List commLst);
 
	 
	 /**
	  * 增加或更新
	  * @param obj
	  */
	 public void saveOrUpdate(Object obj);
	 
	  public Object find(Class clazz, Serializable sid);
	  
	  /**
	   * 查询列表
	   * @param hsql
	   * @return
	   */
	  public List query(String hsql);
	  
	    /**
	     * 根据类名和ID来删除对象
	     */
	    public void remove(Class clazz, Serializable id);
	    
	    
		/**
		 * 支持sql语句更新,返回更新记录数
		 */
		public int doSqlUpdate(final String sql);
	 public OrOrder loadOrder(Serializable orderID);
	 public void doSqlUpdateBatch(String hql, Object[] obj);
	 public void insertObject(Object obj);
	/**
	 * 调整佣金
	 * add by yong.zeng 2010-3-16
	 * @param commAdjust
	 *//*
	public void saveOrUpdateCommAdjust(CommisionAdjust commAdjust);
	
	*//**
	 * 
	 * 增加或更新佣金设置公式
	 * add by yong.zeng 2010-3-16
	 * @param commSet
	 *//*

	public void saveOrUpdateCommSet(CommisionSet commSet);*/
	 
	 public void saveOrUpderComTemp(HtlB2bTempComminfo htlB2bTempComminfo);
	 
	 /**
	   * 查询列表
	   * @param hsql
	   * @return
	   */
	  public Object queryObj(String hsql);
	  
	  /**
	   * 根据模板id删除对应数据
	   * @param tempId
	   * @return
	   */
	  public void deleteList(Long tempId,String loginChnName,String loginId);
	  
	  /**
	   * 根据价格类型id获取酒店名称和价格类型名称
	   * @param priceTypeStr
	   * @return
	   */
	  public List fillHotelNameAndPriceTypeName(String priceTypeIdStr);

}

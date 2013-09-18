package com.mangocity.hotel.base.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.persistence.HtlBookCaulClause;
import com.mangocity.hotel.base.persistence.HtlBookModifyField;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlFavourableDecrease;
import com.mangocity.hotel.base.persistence.HtlPopedomControl;
import com.mangocity.hotel.base.persistence.HtlPreconcertItemTemplet;
import com.mangocity.hotel.base.persistence.HtlRoomtype;

/**
 */
public interface IClauseDaoRefactor {
	public <T> T save(T model);

	public <T> void saveOrUpdate(T model);
    
    /**
     * 保存或更新多个实体对象
     * @param modelList	实体对象集合
     * @return
     */
	public <T> void saveOrUpdateAll(Collection<T> entities);
    
    /**
     * 删除
     * @param clazz	实体类类对象
     * @param id	实体ID
     * @return
     */
	public <T> void remove(Class<T> clazz, Serializable id);
	
	/**
	 * 根据酒店ID查询出该酒店下面的合同列表
	 * @param hotelID
	 * @return
	 */
    public List<HtlContract> getContracts(Long hotelID);
    
    /**
     * 根据酒店ID获取条款模板列表
     * @param hotelID
     * @return
     */
    public List<HtlPreconcertItemTemplet> getClauses(Long hotelID);
    
    /**
     * 根据酒店ID得到起预定条款计算规则信息
     * 
     * 
     */
    public List<HtlBookCaulClause> getClauseRules(Long hotelID);
    /**
     * 根据hotelID得到预订条款计算规则
     */
    public List<HtlBookCaulClause> getBookCaulClauses(Long hotelID);
    /*
     * 根据酒店ID得到起修改字段定义
     * 
     * 
     */
    public List<HtlBookModifyField> getBookModifyField(Long hotelID);
    /**
     * 
     * 批量删除实体对象
     * @param ids		实体ID数组
     * @param className	实体类类名
     * @return
     */
    public <T> void remove(String[] ids, String className);
    /**
     * 取出计算规则中最严格的计算规则
     * @param hotelID
     * @param checkInDate
     * @param checkOutDate
     * @return
     */
    public List<HtlBookCaulClause> getBookCaulByDateRange(Long hotelID, Date checkInDate,Date checkOutDate);
    
    /**
     * 取出对应酒店的所有的优惠立减条款
     * 
     */
    public List<HtlFavourableDecrease> getAllavourableclause(long hotelId);
    
    /**
     * 取出对应酒店价格类型优惠立减条款
     * 
     */
    public List<HtlFavourableDecrease> getFavourableclauseForPriceTypeId(long hotelId,long priceTypeId);
    
    /**
     * 查询优惠立减
     * @param hotelId
     * @param priceTypeId
     * @return
     */
    public List<HtlFavourableDecrease> getFavourableDecreaseOrder(long hotelId,long priceTypeId);
    /**
     * 查询小于modifyTime时的优惠立减
     * @param hotelId
     * @param priceTypeId
     * @param modifyTime
     * @return
     */
      public List<HtlFavourableDecrease> getSubFavDecrease(long hotelId,long priceTypeId,Date modifyTime);
      
      /**
       * 根据价格类型ID获取房型
       * @param priceTypeID
       * @return
       */
      public HtlRoomtype getHtlRoomTypeByPriceTypeId(long priceTypeID);
      /**
       * 根据ID获取HtlFavourableDecrease
       * @param Id
       * @return
       */
      public HtlFavourableDecrease getFavDecreaseById(long Id);
      /**
       * 取预付/面付价格数量
       * @param hotelId
       * @param priceTypeIte
       * @param beginD
       * @param endD
       * @return
       */
  	public long getEleDayPriceNum(long hotelId,long priceTypeIte,Date beginD,Date endD,String type);
	/**
	 * 得到所有权限列表
	 * @return
	 */
	public List<HtlPopedomControl> getAllPopedomList();
	
	/**
	 * 查找当前用户权限
	 * @param popedomControlType
	 * @param loginName
	 * @return
	 */
	public List<HtlPopedomControl> getPopedoms(String popedomControlType, String loginName);

	public List<HtlPopedomControl> getPopedomListByLoginName(String loginName);
}

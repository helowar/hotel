package com.mangocity.hotel.base.manage;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlBookCaulClause;
import com.mangocity.hotel.base.persistence.HtlBookModifyField;
import com.mangocity.hotel.base.persistence.HtlFavourableDecrease;
import com.mangocity.hotel.base.persistence.HtlPopedomControl;
import com.mangocity.hotel.user.UserWrapper;

/**
 */
public interface ClauseManage {

    // 根据酒店ID获得其合同列表；
    public List searchContactByHTlID(Long hotelID);

    // 根据酒店ID得到其预定条款模板列表；
    public List searchClasesByHTLID(Long hotelID);

    // 根据酒店ID得到起预定条款计算规则信息 add by shengwei.zuo 2009-02-12
    public List<HtlBookCaulClause> searchBookCaulByHTLID(Long hotelID);

    /**
     * 
     * 保存预订条款计算规则 如果有重复，则覆盖时间段，有可能拆分时间段 hotel2.9.2 modify by chenjiajie 2009-08-10
     * 
     * @param htlBookCaulClause
     * @param user
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public long createBookRules(HtlBookCaulClause htlBookCaulClause, UserWrapper user)
        throws IllegalAccessException, InvocationTargetException;

    /**
     * 根据酒店id查询修改字段定义，返回一条记录
     * 
     * @param hotelID
     * @return
     */
    public HtlBookModifyField searchBookModifyFieldByHTLID(Long hotelID);

    /**
     * 添加修改字段定义
     * 
     * @param htlBookModifyField
     */
    public void createModifyField(HtlBookModifyField htlBookModifyField);

    /**
     * 批量保存
     * 
     * @param objList
     */
    public void saveOrUpdateAll(List objList);

    /**
     * 检查有没有需要删除的记录，有则删除
     * 
     * @param oldList
     * @param newList
     */
    public void removeOldBookCaulClause(List<HtlBookCaulClause> oldList,
        List<HtlBookCaulClause> newList);

    /**
     * 按酒店id和时间段查询计算规则
     * 
     * @param hotelID
     * @param checkInDate
     * @param checkOutDate
     * @return
     */
    public List<HtlBookCaulClause> searchBookCaulByDateRange(Long hotelID, Date checkInDate,
        Date checkOutDate);

    /**
     * 取出计算规则中最严格的计算规则，如果没有计算规则，默认累加判定
     * 
     * @param list
     * @return
     */
    public String drawoutHtlBookCaulClause(List<HtlBookCaulClause> list);

    /**
     * 取出对应酒店的所有的优惠立减条款
     * add by zhijie.gu
     */
    public List queryAllavourableclause(long hotelId);
    
    /**
     * 列出一个酒店的所有房型
     * 
     * @param hotelId
     *            酒店id
     * @return roomtype 的 List;
     */
    public List lstRoomTypeByHotelId(long hotelId);
	
	/**
	 * 根据ID查询对应的立减优惠条款
	 * 
	 */
	public HtlFavourableDecrease getFavDecreaseById(long id);
	
	/**
     * 取出对应酒店价格类型优惠立减条款
     * add by zhijie.gu
     */
    public List queryFavourableclauseForPriceTypeId(long hotelId,long priceTypeId);
    
    /**
     * 新增优惠立减 add by zhijie.gu 2009-10-20
     * 
     * @param hotelId
     *            酒店id
     * @return roomtype 的 List;
     */
    public Long createFavourableDecrease(HtlFavourableDecrease htlFavourableDecrease) throws IllegalAccessException, InvocationTargetException  ;
    
    /**
	 * hotel 2.9.3
	 * 
	 * 修改一个立减优惠的信息 add by zhijie.gu 2009-10-21
	 * @param favourableDecrease
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public long modifyFavClause(HtlFavourableDecrease favourableDecrease) throws IllegalAccessException, InvocationTargetException;
	
	/**
	 * 删除立减优惠 add by zhijie.gu 2009-10-21
	 * @param favourableDecrease
	 * @return
	 */
	public long deleteFavDecreaseObj(HtlFavourableDecrease favourableDecrease);
	
	/**
	 * 获取权限表里所有数据 add by zhijie.gu 2009-10-21
	 * @return
	 */
	public List getAllPopedomList();
	
	/**
	 * 获取权限表数据
	 * @param hotelId
	 * @param priceTypeIte
	 * @param beginD
	 * @param endD
	 * @return
	 */
	public List<HtlPopedomControl> getPopedomListByLoginName(String loginName);
	
	public Map<String, ?> getContactContent(Long hotelId) ;
	 
	public Map<String, ?> getPopedoms(String popedomControlType, String loginName, Long hotelId) ;
}

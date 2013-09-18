package com.mangocity.hotel.base.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.mangocity.hotel.base.persistence.HtlFavourableReturn;
import com.mangocity.hotel.user.UserWrapper;

public interface IHotelFavourableReturnServiceRefactor {
	
	
	/**
     * 新增现金返还 add by xiaojun.xiong 2010-9-13
     * 
     * @param hotelId
     *            酒店id
     * @return roomtype 的 List;
     */
    public Long createFavourableReturn(HtlFavourableReturn htlFavourableReturn) throws IllegalAccessException, InvocationTargetException  ;
    
    
    /**
	 * 修改一个现金返还的信息 add by xiaojun.xiong 2010-9-13
	 * @param favourableReturn
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public long modifyFavReturn(HtlFavourableReturn favourableReturn) throws IllegalAccessException, InvocationTargetException;
	
	/**
	 * 批量增加现金返还信息
	 * @param htlFavReturnLis
	 * @param htlFavourableReturn
	 * @param hotelId
	 * @param onlineRoleUser
	 */
	public void batchFavourableReturn(List<HtlFavourableReturn> htlFavReturnLis, HtlFavourableReturn htlFavourableReturn, Long hotelId, UserWrapper onlineRoleUser);
	/**
	 * 根据ID修改现金返还信息
	 * @param favourableReturn
	 * @param onlineRoleUser
	 */
	public void updateFavourableReturn(HtlFavourableReturn favourableReturn, String favReturnID, UserWrapper onlineRoleUser);

	/**
	 * 根据ID删除现金返还信息
	 * @param favReturnID
	 */
	public void removeFavReturn(String favReturnID);
	/**
	 * 批量设置现金返还信息
	 * @param hotelIds
	 * @param favourableReturnBean
	 * @param onlineRoleUser
	 */
	public void batchCreateFavReturn(String hotelIds, HtlFavourableReturn favourableReturnBean, UserWrapper onlineRoleUser);
	
}

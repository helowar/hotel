/**
 * 
 */
package com.mangocity.hotel.base.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlFITAlias;
import com.mangocity.hotel.base.persistence.HtlFavourableReturn;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.hotel.order.persistence.FITCashItem;
import com.mangocity.hotel.order.persistence.FITOrderCash;

/**
 * 酒店现金返还规则service
 * @author xiongxiaojun
 *
 */
public interface IHotelFavourableReturnService {
	/**
	 * 取出对应酒店所有的现金返还条款 add by xiaojun.xiong 2010-9-13
	 * @param hotelId
	 * @return
	 */
	public List<HtlFavourableReturn> queryAllFavourableReturn(long hotelId);
	
	/**
	 * 根据ID查询对应的现金返还规则 add by xiaojun.xiong 2010-9-13
	 * @param id
	 * @return
	 */
	public HtlFavourableReturn getFavReturnById(long id);
	
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
	 * 删除现金返还 add by xiaojun.xiong 2010-9-13
	 * @param favourableReturn
	 * @return
	 */
	public long deleteFavReturnObj(HtlFavourableReturn favourableReturn); 
	
	/**
	 * 批量设置返现规则  add by xiaojun.xiong 2010-9-13
	 * @param returnList
	 * @return
	 */
	public long batchSettingReturn(List<HtlFavourableReturn> returnList);
	
	/**
	 * 取出对应酒店价格类型现金返还条款 add by xiaojun.xiong j2010-9-13
	 * @param hotelId
	 * @param priceTypeId
	 * @return
	 */
	public List<HtlFavourableReturn> queryFavourableReturnForPriceTypeId(long hotelId,long priceTypeId);
	
	
	
	/**
	 * 批量查询某时间段的现金返还信息，缓存到Map中 key是价格类型Id串 
	 * add by linpeng.fang 
	 * @param priceTypeIds 价格类型Id串
	 * @param inDate
	 * @param leaveDate
	 * @return
	 */
	public Map<String,List<HtlFavourableReturn>> queryFavourableReturnForPriceTypeIds(String priceTypeIds,Date inDate,Date leaveDate);


	public void batchFavourableReturn(List<HtlFavourableReturn> htlFavReturnLis, HtlFavourableReturn htlFavourableReturn, Long hotelId, UserWrapper onlineRoleUser)throws IllegalAccessException, InvocationTargetException;


	public void updateFavourableReturn(HtlFavourableReturn favourableReturn, String favReturnID, UserWrapper onlineRoleUser)throws IllegalAccessException, InvocationTargetException;

	public void removeFavReturn(String favReturnID);

	public void batchCreateFavReturn(String hotelIdStr, HtlFavourableReturn favourableReturnBean, UserWrapper onlineRoleUser); 


	/**
	 * 计算某价格类型现金返还金额
	 * 
	 * @param priceTypeId  价格类型
	 * @param night        入住夜晚 
	 * @param payMethod    支付方式
	 * @param currency     币种
	 * @param roomQuantity 房间总数
	 * @param price        净价
	 * @return
	 */
	public int calculateCashReturnAmount(long priceTypeId,Date night,int payMethod,String currency, int roomQuantity, BigDecimal price);
	
	
	/**
	 * 组装返现信息
	 * @param priceTypeId
	 * @param payMethod
	 * @param checkInDate
	 * @param checkOutDate
	 * @return
	 */
	public Map<String,Double> fillCashItem(long priceTypeId,String payMethod,Date checkInDate,Date checkOutDate);
	
	/**
	 * 保存对应的返现信息
	 * @param orderCash
	 * @param cashItems
	 */
	public void saveCashInformation(FITOrderCash orderCash,List<FITCashItem> cashItems);
	
	/**
	 * 清除返现信息
	 */
	public void clearCashInformation(FITOrderCash orderCash);
	
	
	/**
	 * 查询订单是否有返现
	 * @param orderCd
	 * @return
	 */
	public boolean isOrderHasCashReturn(String orderCd);
	
	/**
	 * 计算房型净价（仅适用现金返还）
	 * 
	 * @param formulaId 计算公式
	 * @param commission 佣金
	 * @param commissionRate 佣金率
	 * @param salePrice 售价
	 * @return
	 */
	public BigDecimal calculateRoomTypePrice(String formulaId,BigDecimal commission,BigDecimal commissionRate,BigDecimal salePrice);
	
	
	/**
	 * 计算返现总金额
	 * 
	 * @param priceTypeId    价格类型   
	 * @param checkInDate    入住日期
	 * @param checkOutDate   离店日期
	 * @param night          入住夜晚
	 * @param roomQuantity   房间数 
	 * @param price          净价
	 * @param payType        支付方式
	 * @param currency       币种
	 * @param cashReturns    返现规则缓存Map
	 * @return
	 */
	public int calculateCashReturnAmount(String priceTypeId, Date checkInDate, Date checkOutDate,Date night, int roomQuantity, BigDecimal price, String payMethod,String currency, Map<String, List<HtlFavourableReturn>> cashReturns);
	
	/**
	 * 得到芒果散客的项目号
	 * @param active
	 * @return
	 */
	public List<HtlFITAlias> getFITAlias(String active);

}

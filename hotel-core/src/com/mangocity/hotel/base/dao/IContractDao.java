package com.mangocity.hotel.base.dao;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.persistence.HtlAddBedPrice;
import com.mangocity.hotel.base.persistence.HtlAlerttypeInfo;
import com.mangocity.hotel.base.persistence.HtlChargeBreakfast;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlCreditAssure;
import com.mangocity.hotel.base.persistence.HtlFavourableclause;
import com.mangocity.hotel.base.persistence.HtlPresale;
import com.mangocity.hotel.base.persistence.HtlQuotabatch;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.persistence.HtlSalesPromo;
import com.mangocity.util.bean.ContractContinue;
import com.mangocity.util.bean.SingleContractContinue;

/**
 */
public interface IContractDao {

    /**
     * 获取房型
     * 
     * @param hotelID
     *            酒店ID
     * @return HtlRoomType 实体集合
     */

    public List getRoomTypes(Long hotelID);

    /**
     * 获取酒店合同促销信息
     * 
     * @param contractID
     *            合同ID
     * @return HtlSalePromos 实体集合
     */

    public List getSalePromos(Long contractID);

    /**
     * 获取酒店促销信息
     * 
     * @param
     * @return
     */

    public List getPreOrderSalePromos(Long hotelID, Date beginDate, Date endDate);

    /**
     * 获取芒果网促销信息
     * 
     * @param
     * @return
     */

    public List getPreOrderPresaleList(Long hotelID, Date beginDate, Date endDate);

    /**
     * 获取酒店合同奖惩信息
     * 
     * @param contractID
     *            合同ID
     * @return HtlSalePromos 实体集合
     */

    public List getRewards(Long contractID);

    /**
     * 获取酒店合同奖惩信息
     * 
     * @param contractID
     *            合同ID
     * @return HtlBreakfast 实体
     */

    public List getBreakfast(Long contractID);

    /**
     * 获取酒店合同奖惩信息
     * 
     * @param contractID
     *            合同ID
     * @return HtlAddBedPrice 实体
     */

    public List getAddBedPrice(Long contractID);

    /**
     * 获取酒店合同奖惩信息
     * 
     * @param contractID
     *            合同ID
     * @return HtlWelcomePrice 实体
     */

    public List getWelcomePrice(Long contractID);

    /**
     * 通过id拿到合同
     * 
     * @param contractID
     * @return
     */
    public HtlContract getContractById(Long contractID);

    /**
     * 获取合同预定信息
     * 
     * @param hotelId
     * @param creditDate
     * @return HtlAssureCredit 实体
     */

    public List getCreditAssure(Long hotelId, Date creditDate);

    /**
     * 获取价格类型信息
     * 
     * @param roomId
     * 
     * @return HtlPrice 实体
     */

    public List getPriceList(Long roomTypeId);

    /**
     * 获取调价警告信息
     * 
     * @param hotelID
     * 
     * @return HtlChangePriceWarning 实体
     */

    public List getChangePriceWarningList(Long hotelID);

    /**
     * 删除调价警告信息
     * 
     * @param obj
     */
    public void delChangePriceWarning(Object obj);

    /**
     * 根据酒店ID获取芒果网促销信息
     * 
     * @param hotelId
     * 
     * @return List
     */

    public List getPresaleList(Long hotelId);

    /**
     * 根据合同ID获取税费设定信息
     */
    public List getTaxCharges(Long contractId);

    /**
     * 查询某合同下某房型的加床信息 hotel 2.9.2 add by chenjiajie 2009-07-23
     * 
     * @param contractId
     * @param roomTypeId
     * @return
     */
    public List getAddBedPrice(long contractId, String roomTypeId, Date checkInDate,
        Date checkOutDate,String payMethod);

    /**
     * 查询某合同下某房型的免费宽带信息
     * 
     * @param contractId
     * @param roomTypeId
     * @return
     */
    public List getInternetByRoomType(long contractId, long roomTypeId, Date checkInDate,
        Date checkOutDate);

    /**
     * 获取酒店合同奖惩信息
     * 
     * @param contractID
     *            合同ID
     * @return HtlBreakfast 实体
     */
    public List getBreakfast(long contractId, Date checkInDate, Date checkOutDate);

    /**
     * 获取酒店合同奖惩信息
     * 
     * @param contractID
     *            合同ID
     * @return HtlWelcomePrice 实体
     */
    public List getWelcomePrice(long contractId, Date checkInDate, Date checkOutDate);
    
    /**
     * 获取信用卡卡担保订单
     * @param HtlCreditAssure creditAssure 信用卡担保类
     * @return 
     */
    public List getCreditAssures(HtlCreditAssure creditAssure);

    /**
     * 获取信用卡卡担保订单
     * @param contractId合同
     * @param roomType房型
     * @param modifyDate修改记录
     * @return 
     */    
    public List getSubCreditAssures(Integer contractId,String roomType,Date modifyDate);

    /**
     * 保存或修改合同信息
     */    
    public void saveOrUpdate(HtlContract contract);
    
    /**
     * 保存或修改信用卡担保
     */    
    public void saveOrUpdate(HtlCreditAssure creditAssure);
    
    /**
     * 保存或修改销售提醒
     */    
    public void saveOrUpdate(HtlSalesPromo salesPromo);
    
    /**
     * 保存或修改早餐
     */    
    public void saveOrUpdate(HtlChargeBreakfast chargeBreakfast);
    
    /**
     * 保存或修改提示信息
     */  
    public void saveHtlAlerttypeInfo(HtlAlerttypeInfo htlAlerttypeInfo);
    
    /**
     * 保存或修改优惠条款
     */  
    public void saveOrUpdate(HtlFavourableclause favourableclause);
    
    /**
     * 加床加
     */  
    public void saveAddBedPrice(HtlAddBedPrice addBedPrice);
    
    /**
     * 批量修改List
     */  
    public void saveOrUpdateAll(List list);
    
    /**
     * 根据ID删除对象
     */
    public void remove(Class clazz, Long cid);
    
    /**
     * 根据合同ID查找合同
     */
    public HtlContract findHtlContractByID(Class clazz,Long contractId);
    
    /**
     * 根据合同ID查找合同
     */
    public HtlQuotabatch findByQuotabatchId(Class clazz,Long quotabatchId);
    
    /**
     * 更新contract对象,同时刷新session里面的contract
     */
    public void merge(HtlContract contract);
    
    /**
     * 获取早餐
     * @param chargeBreakfast
     * @return
     */
    public List getBreakfastsOrder(HtlChargeBreakfast chargeBreakfast);
    
    /**
     * 获取早餐列表
     * @param contractId
     * @param payMethod
     * @param modifyDate
     * @return
     */
    public List getSubBreakfasts(Long contractId,String payMethod,Date modifyDate);
    
    /**
     * 获取加床价
     * @param addBedPrice
     * @return
     */
    public List getAddBedPricesOrder(HtlAddBedPrice addBedPrice);
    
    /**
     * 获取加床列表
     * @param contractId
     * @param roomType
     * @param modifyTime
     * @param payMethod
     * @return
     */
    public List getSubAddBedPrices(long contractId,String roomType,Date modifyTime,String payMethod);
    
    /**
     * @param contractId
     * @param roomType
     * @param modifyTime
     * @return
     */
    public List getSubAddBedPrices(long contractId,String roomType,Date modifyTime);
    
    /**
     * @param hotelId
     * @param contractId
     * @param cc
     * @param user
     * @return
     */
    public int callContinueContractStoreProcedure(long hotelId,long contractId,ContractContinue cc,Object user);
    
    /**
     * @param hotelId
     * @param contractId
     * @param cc
     * @param user
     * @return
     */
    public int callSingleContinueContractStoreProcedure(long hotelId,long contractId,SingleContractContinue cc,Object user);
    
    /**
     * @param hotelid
     * @return
     */
    public HtlContract getHtlContract(long hotelid);
    
    /**
     * @param currency
     * @param hotelId
     * @param beginDate
     * @param endDate
     */
    public void updateContract(String currency,Long hotelId,Date beginDate, Date endDate);
    
    /**
     * @param quotaPattern
     * @param contractId
     */
    public void updateHtlQuota(String quotaPattern,Long contractId);
    
    /**
     * @param obj
     * @return
     */
    public HtlContract getHtlContracts(Object[] obj);
    
    /**
     * @param htlAlerttypeInfo
     * @return
     */
    public List getAlerttypeInfoOrder(HtlAlerttypeInfo htlAlerttypeInfo);
    
    /**
     * @param contractId
     * @param priceTypeId
     * @param modifyDate
     * @return
     */
    public List getSubAlerttypeInfo(Long contractId,String priceTypeId,Date modifyDate);
    
    /**
     * @param priceTypeID
     * @return
     */
    public HtlRoomtype findHtlRoomType(long priceTypeID);
    
    /**
     * @param alerttypeInfoByIdid
     * @return
     */
    public HtlAlerttypeInfo findHtlAlerttypeInfo(long alerttypeInfoByIdid);
    
    /**
     * @param contractid
     * @return
     */
    public List getAlertInfosByConId(long contractid);
    
    /**
     * @param favourableclause
     * @return
     */
    public List getFavourableclauseOrder(HtlFavourableclause favourableclause);
    
    /**
     * @param contractId
     * @param priceTypeId
     * @param modifyDate
     * @return
     */
    public List getSubFavClause(Long contractId,Long priceTypeId, Date modifyDate);
    
    /**
     * @param htlFavourableclauseId
     * @return
     */
    public HtlFavourableclause findHtlFavourableclause(long htlFavourableclauseId);
    
    /**
     * @param contractId
     * @return
     */
    public List getHtlFavourableclauseByContractId(long contractId);
    
    /**
     * @param contractId
     * @return
     */
    public List getBreakfasts(long contractId);

	public void deleteAlertTypeInfoByIds(String[] idArray, String className);

	public List<HtlAlerttypeInfo> queryAlerttypeInfoByIds(String ids);
	 
    /**
     * 根据酒店ID，开始日期和结束日期查询酒店合同
     * 
     * @param hotelId
     * @param beginDate
     * @param endDate
     * @return
     */
    public HtlContract qryHtlContractByHtlIdValidDate(long hotelId, Date beginDate, Date endDate);    
    
	/**
	 * 
	 * refactor: 根据"lstPreOrderSalePromosRoom" sql语句查询酒店促销信息
	 * 
	 * @param params
	 * @return
	 */	
	public List<HtlSalesPromo> lstPreOrderSalePromosRoom(Object[] params);
	
	/**
	 * 
	 * refactor: 根据"lstPreOrderPresale" sql语句查询芒果促销信息
	 * 
	 * @param params
	 * @return
	 */
	public List<HtlPresale> lstPreOrderPresale(Object[] params);
	
	/**
	 * 
	 * refactor: 根据hotelId和priceTypeId查询芒果优惠信息
	 * 
	 * @param hotelId
	 * @param priceTypeId
	 * @return
	 */
	public List<HtlFavourableclause> queryFavourableclauseByHotelAndPriceType(
			Long hotelId, Long priceTypeId);
	
	/**
	 * 根据酒店ID获取合同信息
	 * @param hotelId
	 * @return
	 */
	public HtlContract getContractInfoByHotelId(long hotelId);

}

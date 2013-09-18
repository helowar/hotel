package com.mangocity.hotel.search.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlAlerttypeInfo;
import com.mangocity.hotel.base.persistence.HtlFavourableDecrease;
import com.mangocity.hotel.base.persistence.HtlFavourableReturn;
import com.mangocity.hotel.base.persistence.HtlFavourableclause;
import com.mangocity.hotel.base.persistence.HtlPriceType;
import com.mangocity.hotel.base.persistence.HtlSalesPromo;
import com.mangocity.hotel.search.model.HotelBasicInfo;
import com.mangocity.hotel.search.model.QueryCommodityCondition;

public interface HotelInfoDAO {
	
	/**
	 * 
	 * @param currentPage 当前页
	 * @param pagesize  每页记录数
	 * @return
	 */
	
	public Collection<HotelBasicInfo> queryHotelBasicInfoByPage(int currentPage,int pagesize);
	
	/**
	 * 查询酒店总数
	 * 
	 * @return
	 */
    public int queryHotelCount();
	
	/**
	 * 查询酒店基本信息
	 * 
	 * @return
	 */
    public Collection<HotelBasicInfo> queryHotelBasicInfo();
    
    /**
     * 查询酒店所属商业区
     * 
     * @param hotelId	酒店ID
     * @return	BusinessZoneModel对象列表
     */
//    public List<CommonDataModel> queryBizZoneByHotelId(long hotelId);
    
    /**
     * 查询酒店所支持的信用卡
     * 
     * @param hotelId	酒店ID
     * @return	包含信用卡代码和名称的列表
     */
//    public List<CommonDataModel> querySupportedCreditCardsByHotelId(long hotelId);
    
    /**
     * 查询酒店所属类型
     * 
     * @param hotelId	酒店ID
     * @return	包含酒店类型代码和名称的列表
     */
//    public List<CommonDataModel> queryHotelTypesByHotelId(long hotelId);
    
    /**
     * 查询酒店客房设施
     * 
     * @param hotelId	酒店ID
     * @return	包含客房设施代码和名称的列表
     */
//    public List<CommonDataModel> queryRoomFixturesByHotelId(long hotelId);
    
    /**
     * 查询酒店餐饮设施
     * 
     * @param hotelId	酒店ID
     * @return	包含餐饮设施代码和名称的列表
     */
//    public List<CommonDataModel> queryMealFixturesByHotelId(long hotelId);
    
    /**
     * 查询酒店免费服务
     * 
     * @param hotelId	酒店ID
     * @return	CommonDataModel对象列表
     */
//    public List<CommonDataModel> queryFreeServicesByHotelId(long hotelId);
    
    /**
     * 查询酒店所属主题
     * 
     * @param hotelId	酒店ID
     * @return	包含主题代码和名称的列表
     */
//    public List<CommonDataModel> queryThemesByHotelId(long hotelId);
    
    /**
	 * 查询促销信息
	 * 
	 * @return
	 */
//	public List<SalePromotionMango> querySalesPremotion();
	
	/**
	 * 
	 * 根据酒店id查该酒店的lucene基本信息
	 * 
	 * @param hotelId
	 * @return
	 */
	public Collection<HotelBasicInfo> queryHotelBasicInfo(Long hotelId);
	
	/**
	 * 根据酒店Id查该酒店的简单信息，例如酒店名字、酒店城市，酒店英文名字
	 * @param hotelId
	 * @return
	 */
	public HotelBasicInfo queryHotelSimpleInfo(Long hotelId,Date checkInDate,Date checkOutDate);
	
	
    /**
     * 根据条件(指定酒店，指定渠道，指定日期)查询酒店促销信息
     * @param queryBean
     * @return
     */
    public List<HtlSalesPromo> querySalepromos(QueryCommodityCondition queryBean);

    /**
     * 
     * 根据条件查询芒果促销信息
     * 
     * @param queryBean
     * @return
     */
    public List<Object[]> queryPreSales(QueryCommodityCondition queryBean);
    
    /**
	 * 根据条件(指定酒店，指定渠道，指定日期)查询优惠(住M送N,打折,连住包价)信息 查询条件中的入住离店日期的判断如下 startdate<=checkout
	 * and enddate>=checkin 客人离店之前优惠必须开始了，同时客人入住之后优惠还未结束的才取出来
	 * ,或者是!(startdate>checkout or enddate< checkin)
	 * 
	 * @param queryBean
	 *            hotelId,outDate,inDate.
	 * @return
	 */
    public List<HtlFavourableclause> queryFavourableClauses(QueryCommodityCondition queryBean);
    
    /**
     * 根据条件(指定酒店，指定渠道，指定日期)查询酒店返现信息
     * @param queryBean
     * @return
     */
    public List<HtlFavourableReturn> queryFavourableReturns(QueryCommodityCondition queryBean);
    
    /**
     *根据条件(指定酒店，指定渠道，指定日期)查询酒店立减信息
     * @param queryBean
     * @return
     */
    public List<HtlFavourableDecrease> queryFavourableDecrease(QueryCommodityCondition queryBean);
    
    /**
     * 根据查询条件取出酒店的提示信息
     * @param qcc
     * @return
     */
    public List<HtlAlerttypeInfo> queryArlerttypeInfoList(QueryCommodityCondition qcc);
    
    /**
     * 查询酒店的供应商信息
     * @param hotelIdLst
     * @return
     */
    public Map<String,String> querySupplierInfo(String hotelIdLst);
    
    /**
     * 
     * 根据酒店id获取所在城市code
     * 
     * @param hotelId
     * @return
     */
    public String getCityCodeByHotelId(Long hotelId);
    
    /**
     * 根据城市代码获取酒店列表
     * @param cityCode
     * @return
     */
	public List queryHotelByCityCode(String cityCode);
	
	/**
	 * 查询价格类型信息，add by ting.li
	 * @param hotelIdLst
	 * @return
	 */
	public Map<String,HtlPriceType> queryPriceTypeInfo(String hotelIdLst);
}

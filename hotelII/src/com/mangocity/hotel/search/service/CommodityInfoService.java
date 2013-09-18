package com.mangocity.hotel.search.service;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.search.model.QueryCommodityCondition;
import com.mangocity.hotel.search.model.QueryCommodityInfo;
import com.mangocity.hotel.search.service.assistant.SaleInfo;


/**
 * 商品信息Service
 * 用于酒店的数据查询,同时给商旅提供服务
 * 
 * @author zengyong
 *
 */
public interface CommodityInfoService {

	
	/**
	 * 根据排好序分好页的当前页的酒店ID列表和查询条件从db中查询商品价格条款房态等信息
	 * @param qcc
	 * @return
	 */
	public List<QueryCommodityInfo> queryCommodityInfo(QueryCommodityCondition  qcc);

	/**
	 *
	 * 设置酒店优惠(住M送N，打折,连住包价)
	 * 给满足条件的QueryCommodityInfo设置优惠,指每一天的每个床型是否优惠,如果有,优惠多少.
	 * @param commodityLst
	 */
	public List<QueryCommodityInfo> setProviderFavourableToCommodityPerday(QueryCommodityCondition  qcc,List<QueryCommodityInfo> commodityLst);
		
	/**
	 * 设置限量返现
	 */
	public void setLimitFavourableReturnToCommodityPerday(QueryCommodityCondition  qcc,List<QueryCommodityInfo> commodityLst);
	
	/**
	 * 设置返现
	 * 给当前满足条件的QueryCommodityInfo设置返现,指每天的每个床型是否返现,返多少.

	 * @param commodityLst
	 */
	public void setFavourableReturnToCommodityPerday(QueryCommodityCondition  qcc,List<QueryCommodityInfo> commodityLst);
	
	/**
	 * 设置立减
	 * 给当前满足条件的QueryCommodityInfo设置立减情况,指每天的每个床型是否立减,减多少
	 * @param commodityLst
	 */
	public void setHtlFavourableDecreaseToCommodityPerday(QueryCommodityCondition  qcc,List<QueryCommodityInfo> commodityLst);
	
	
	/**
	 * 是否显示(每天的商品),如：关房了，某些原因在外网是不需要显示价格
	 * @param queryInfo
	 * @param fromChannel
	 * @return
	 */
	public boolean showPriceForPerday(QueryCommodityInfo queryInfo, String fromChannel);
	
	/**
	 * 能否满足条款(针对每天每个商品)
	 * 1、预订时间在要求的时间范围内
	 * 2、入住天数必须大于连住小于限住
	 * @param queryInfo
	 * @return
	 */
	public void satisfyClauseForPerday(QueryCommodityInfo queryInfo,SaleInfo saleInfo,Date checkinDate,Date checkoutDate);
	
	/**
	 * 判断每天的商品能预订,如果不能预订,说明原因
	 * 关房
	 * 不满足条款
	 * 价格为0
	 * 满房
	 * @param queryInfo
	 * @param saleInfo
	 * @param checkinDate
	 * @param checkoutDate
	 * @return
	 */
	public void setCanbookPerDay(SaleInfo saleInfo,QueryCommodityCondition qcc);
	
    /**
     * 
     * 根据酒店id获取所在城市code
     * 
     * @param hotelId
     * @return
     */
    public String getCityCodeByHotelId(Long hotelId);
}

package com.mangocity.hotel.search.service;

import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlAlerttypeInfo;
import com.mangocity.hotel.base.persistence.HtlSalesPromo;
import com.mangocity.hotel.search.model.HotelBasicInfo;
import com.mangocity.hotel.search.model.HotelCurrentlySatisfyQuery;
import com.mangocity.hotel.search.model.QueryCommodityCondition;
import com.mangocity.hotel.search.model.QueryCommodityInfo;
import com.mangocity.hotel.search.model.QueryHotelCondition;
import com.mangocity.hotel.search.service.assistant.Commodity;
import com.mangocity.hotel.search.service.assistant.HotelInfo;
import com.mangocity.hotel.search.service.assistant.SortResType;


/**
 * 数据操作Service
 * 用于酒店的数据查询,同时给商旅提供服务
 * 
 * @author zengyong
 *
 */
public interface HotelInfoCombinationService {
	
	/**
	 * 
	 * 封装酒店基本信息和售卖信息, 使用handler 
	 * 
	 * @param hotelBasicInfos
	 * @param commodityInfoLst
	 * @param qcc
	 * @param hotelCount 酒店总数
	 * @param handler
	 * @author chenkeming
	 */
	public void combinationHotelInfoUseHandler(
			Map<String, HotelBasicInfo> hotelBasicInfos,
			List<QueryCommodityInfo> commodityInfoLst,
			QueryCommodityCondition qcc, int hotelCount,
			IHotelQueryHandler handler);
	

	//add by diandian.hou
 /**
 *  仅仅组装酒店基本信息  
 */
	public void fitHotelInfoWithHandler(Map<String,HotelBasicInfo> hotelBasicInfos,QueryHotelCondition queryHotelCondition,SortResType sortRes,IHotelQueryHandler handler);
	
	/**
	 * 根据HotelIDLst过滤掉多余的酒店信息
	 * hotelIdLst是需要显示的查询结果的hotelId列表
	 * hotelBaicInfos是满足酒店查询条件的所有酒店(未剔除不可订的酒店)
	 * @param hotelBasicInfos
	 * @param hotelIdLst
	 */
	public Map<String,HotelBasicInfo> filterHotel(Map<String,HotelBasicInfo> hotelBasicInfos,String hotelIdLst);
	


	/**
	 * 商品是否显示
	 * 关房时,如下原因时在外网不显示商品,在其它渠道暂时都显示
	 * 1、停业,策略性关房CC不可订,策略性关房CC可订,停止合作
	 * 2、商品的售价都为0时
	 */	
	public boolean showCommodity(Commodity comm, HotelInfo curHotel, String fromChannel);			

	
	/**
	 *不可预订的房型（价格类型）排在最后
	 *可预订房型按照本部设定推荐房型级别进行排序
	 *基础房型排在非基础房型前面
	 * @param roomLst
	 */
	public void setSortForRoomtype(List roomLst );
	
	
	/**
	 * 过滤掉不满足条件的酒店
	 * @param hotelBasicInfos
	 * @param mapDynamicHotel
	 */
	public void filterDynamicHotel(Map<String,HotelBasicInfo> hotelBasicInfos,List<HotelCurrentlySatisfyQuery> hcLst);
	
	/**
	 * 将每个房型下面的价格类型列表设置提示信息
	 * @param hspLst
	 * @param pricetypeLst
	 */
	public void setAlertToCommodity(List<HtlAlerttypeInfo> hspLst,Commodity comm,String channel);
	
	
	/**
	 * 给商品设置酒店促销情况
	 * @param hspLst
	 * @param comm
	 */
	public void setSalespromoToCommodity(List<HtlSalesPromo> hspLst,Commodity comm,String channel);
	
}

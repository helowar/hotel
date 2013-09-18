package com.mangocity.hotel.search.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.search.model.QueryCommodityCondition;
import com.mangocity.hotel.search.model.QueryCommodityInfo;
import com.mangocity.hotel.search.model.QueryDynamicCondition;
import com.mangocity.hotel.search.model.SortCondition;
import com.mangocity.hotel.search.service.assistant.SortResType;

/**
 * 酒店查询Dao接口
 * @author yong.zeng
 *
 */
public interface HotelQueryDao{

    /**
     * 查询数据库，并把数据库返回的记录放到List中
     * @param queryBean
     * @return 数据记录
     */
    public List<QueryCommodityInfo> queryHotelResultList(QueryCommodityCondition queryBean);
    
    /**
     * 根据动态条件查询商品信息
     * @param queryBean
     * @return
     */
    public Map<Long,List<QueryCommodityInfo>> queryHotelDynamicinfo(QueryDynamicCondition queryBean);
    
    /**
     * 根据动态条件查询商品信息,得到字符串
     * @param queryBean
     * @return
     */
    public String queryHotelDynamicinfoGetStr(QueryDynamicCondition queryBean);
    
    /**
     * 根据酒店Id list，支付方式，开始日期，结束日期，查询酒店id。
     * @param hotelIdList,传入酒店Idlist，例如 30151103,30159784,30000945。
     * @return 酒店Id的list
     */
    
    /**
     * 根据酒店Id list，销售渠道，支付方式，开始日期，结束日期，查询酒店id。
     * @param hotelIdList,传入酒店Idlist，例如 30151103,30159784,30000945。
     * @return 酒店Id的list
     */
   public List<Long> queryHasPrepayPriceTypeHotel(String hotelIdList,String payMethod,Date beginDate,Date endDate,String cityCode);
    
   public List<Long> queryHasPrepayPriceTypeHotelAndSaleChannel(String hotelIdList,String payMethod,Date beginDate,Date endDate,String cityCode,String saleChannel); 
   
   
    /**
	 * 酒店排序后的hotelId_list(最多15个hotelId)
	 * 
	 * @param sc
	 * @param hotelIdList
	 * @return
	 */
	public SortResType hotelSort(SortCondition sc, String hotelIdList);
    
    /**
	 * 查询宽带
	 * 
	 * @param qcc
	 * @return
	 */
//    public List<HtlInternet> queryFreeNet(QueryCommodityCondition qcc);    
    
}

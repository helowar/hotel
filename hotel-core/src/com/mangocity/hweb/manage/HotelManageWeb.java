package com.mangocity.hweb.manage;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlPresale;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.hotel.base.service.assistant.AssureInforAssistant;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrPriceDetail;
import com.mangocity.hotelweb.persistence.HotelOrderFromBean;
import com.mangocity.hweb.persistence.HotelAdditionalServe;
import com.mangocity.hweb.persistence.HotelInfoForWeb;
import com.mangocity.hweb.persistence.HotelPageForWebBean;
import com.mangocity.hweb.persistence.HwClickAmount;
import com.mangocity.hweb.persistence.HwHotelIndex;
import com.mangocity.hweb.persistence.OftenDeliveryAddress;
import com.mangocity.hweb.persistence.QueryHotelForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebResult;
import com.mangocity.hweb.persistence.QueryHotelForWebSaleItems;
import com.mangocity.hweb.persistence.QueryHotelForWebServiceIntroduction;
import com.mangocity.mgis.domain.valueobject.GisInfo;
import com.mangocity.mgis.domain.valueobject.LatLng;
import com.mangocity.webnew.persistence.HtlChannelClickLog;

/**
 */
public interface HotelManageWeb {
    /**
     * 查询酒店预定信息
     * 
     * @param List
     *            酒店查询条件对象
     * @return hotel_id;
     * 
     */
    public HotelPageForWebBean queryHotelsForWeb(QueryHotelForWebBean qureyBean);

    // public HotelPageForWebBean queryHotelForWeb(QueryHotelForWebBean qureyBean);
    /**
     * 查询预定的酒店价格列表 按周排序
     */
    public List<QueryHotelForWebSaleItems> queryPriceForWeb(long hotelId, long roomTypeId,
        long childRoomTypeId, Date beginDate, Date endDate, double minPrice, double maxPrice,
        String payMethod, String quotaType);
    
    /**
     * 查询某家酒店的信息
     */

    // public HotelInfoForWebBean queryHotelInfoBeanForWeb(long hotelId,Date beginDate,Date
    // endDate);
    public QueryHotelForWebResult queryHotelInfoBeanForWebs(QueryHotelForWebBean queryBean);

    /**
     * 查询某家酒店的服务信息
     */
    public QueryHotelForWebServiceIntroduction queryServiceIntroductionForWeb(long hotelId);

    /**
     * 根据酒店ID查询某家酒店的信息
     */
    public HotelInfoForWeb queryHotelInfoForWeb(long hotelId);

    /**
     * 重新找回酒店信息
     * 
     * @param hotel_id
     *            酒店id;
     * @return 酒店一个实体
     */
    public HtlHotel findHotel(long hotel_id);

    /**
     * 查询某会员是否有权对某酒店发表评论（条件：1年内入住该酒店，1年内对该酒店发表评论的次数不能大于2）
     * 
     * @param memberId
     *            会员ID
     * @param hotelId
     *            酒店ID
     * @return boolean
     * @author CMB
     */
    public boolean isPurviewPublish(long memberId, long hotelId);

    /**
     * 网站查询价格、预定特别提示、附加服务等信息
     */
    public HotelAdditionalServe queryWebAdditionalServeInfo(long hotelId, long roomTypeId,
        Date beginDate, Date endDate, String payMethod);

    /**
     * 网站v2.2查询酒店价格明细
     * 
     * @param hotelID
     *            酒店ID
     * @param beginDate
     *            实际开始日期
     * @param endDate
     *            实际结束日期
     * @param beginDateExt
     *            参考开始日期
     * @param endDateExt
     *            参考结束日期
     * @param roomTypeID
     *            房型
     * @param childRoomTypeID
     *            子房型
     * @param payMethod
     *            支付方式
     * @return 查询结果
     */
    public List<QueryHotelForWebSaleItems> queryPriceDetailForWeb(Long hotelID, Date beginDate,
        Date endDate, Date beginDateExt, Date endDateExt, Long roomTypeID, Long childRoomTypeID,
        String payMethod);

    /**
     * 网站查询会员常用配送地址
     */
    public List<OftenDeliveryAddress> queryOftenDeliveryAddress(Long memberID);

    /**
     * 网站V2.2查询酒店促销信息
     */
    public List<HtlPresale> queryPresalesForWeb(long hotelId, Date date);

    /**
     * 根据给定主题编号查询有该主题的城市
     * 
     * @param theme
     * @return
     */
    public List<Object[]> queryThemeCity(String theme);

    /**
     * 查询主题酒店
     * 
     * @param List
     *            酒店查询条件对象
     * @return hotel_id;
     * 
     */
    public HotelPageForWebBean queryThemeHotelsForWeb(QueryHotelForWebBean qureyBean);

    /**
     * 网站v2.8调中科接口查询香港中科酒店的价格明细
     * 
     * @param hotelID
     *            酒店ID
     * @param roomTypeID
     *            房型ID
     * @param childRoomTypeID
     *            子房型ID
     * @param beginDate
     *            入住日期
     * @param endDate
     *            离店日期
     * @return 查询结果
     */
    public List<QueryHotelForWebSaleItems> queryPriceForWebHK(long hotelId, long roomTypeId,
        long childRoomTypeId, Date beginDate, Date endDate);

    /**
     * 网站v2.8调中科接口扣配额操作
     * 
     */
    public int checkQuotaForWebHK(OrOrder order);

    /**
     * 网站v2.8调中科接口保存入住人
     */
    public int saleAddCustInfo(OrOrder order);

    /**
     * 回滚订单
     */
    public boolean rollbackForWebHK(OrOrder order);

    public boolean saleCommitFowWebHK(OrOrder order);

    /**
     * 网站V2.8取得超时限制 ADD BY WUYUN 2009-04-13
     * 
     * @return
     */
    public OrParam getHalfhoutTimeLimit();

    /**
     * 网站V2.8取得IPS国际服务费 ADD BY WUYUN 2009-04-14
     * 
     * @return
     */
    public OrParam getIpsServiceFee();

    /**
     * hotel2.6 根据订单，取出该订单的预订条款，担保条款，取消修改条款等 并组合成字符串
     * 
     * @param orOrder
     * @param bookhintSpanValue
     *            组合担保条款的字符串
     * @param cancelModifyItem
     *            组合修改取消条款的字符串
     * @param telephoneStr
     *            电话号码（组合字符串用），如40066 40066 或 80099 90999 add by chenjiajie 2009-05-25
     */
    public void getReservationHintForWeb(OrOrder orOrder, StringBuffer bookhintSpanValue,
        StringBuffer cancelModifyItem, String telephoneStr);

    /**
     * 电子地图二期 add by haibo.li
     * 
     * @param maspInfo
     *            (对象封装,里面包括gisid,酒店id，酒店名称...)
     * @param sourceType
     *            (数据来源类型)
     * @param dataType
     * @param path
     * @return
     */
    public Map queryMapskmlGenerator(List webHotelResultLis, int sourceType, int dataType,
        String path);

    /**
     * 电子地图二期,封装酒店id的公用方法 add by haibo.li
     * 
     * @param gisidLst
     * @return
     */
    public String getHotelIdLst(String gisidLst);
    
    public double calculateSuretyAmount(List<AssureInforAssistant> 
    			assureDetailLit, HotelOrderFromBean hotelOrderFromBean);
    
    public List convertToAssureInforAssistant(String assureDetailString);
    
    /**
	 * add by zhijie.gu 2009-09-15 hotel2.9.3 改变在优惠条款内的价格，
	 */
    public int changePriceForFavourableTwo(List faceItemsLis,QueryHotelForWebSaleItems queryHotelForWebSaleItems,long hotelId,long childRoomTypeId,int y, int f);
    public int changePriceForFavourableThree(List faceItemsLis,QueryHotelForWebSaleItems queryHotelForWebSaleItems,long hotelId,long childRoomTypeId,int y, int f);
    /**
	 * add by zhijie.gu 2009-09-15 hotel2.9.3 改变在优惠条款内的佣金，
	 */
    public int changeBasePriceCommission(HtlPrice htlPrice,List orPriceDetailList,OrPriceDetail orPriceDetailItems,long hotelId,long childRoomTypeId,int y, int f);
    
    /**
	 * add by zhijie.gu 2009-09-15 hotel2.9.3 获取价格表详情，
	 */
    public HtlPrice qryHtlPriceForCC(long childRoomId, Date date, 
	        String payMethod, String quotaType);
	/**
     * add by shaojun.yang 2009-11-02 hotel2.10 查找该酒店的周边酒店
     * @param latLng
     * @param hotelId
     * @param city
     * @return
     */
	public List<GisInfo> calculateMapNearHotel(LatLng latLng, Long hotelId, String city,int count,Class classes);        
     
	/**
     * 根据酒店id获取酒店名称。、
     * 
     * @param hotelId
     * @return
     */
    public String queryHotelName(long hotelId);
    
    /**
     * Normand 获得order中的OrpriceDetail 封装到 List<QueryHotelForWebSaleItems>中  
     * add by shizhongwen
     * 时间:Sep 8, 2009  5:21:36 PM
     * @param order
     * @param priceLis
     * @return
     */
    public List<QueryHotelForWebSaleItems> getSaleItemPriceList(List<QueryHotelForWebSaleItems> priceLis,OrOrder order);
    /**
     * 根据酒店所在城市Id获取所在城市的商业区。、
     * 
     * @param cityName
     * @return
     */
    public List queryBusinessForHotelInfo(String cityName);
    
    /**
     * 酒店详情页面查询附加服务（早餐和接送信息）
     * 
     * @param hotelId、beginDate、endDate
     * @return
     */
	 public String queryHotelAdditionalServeForHotelInfo(long hotelId,Date beginDate,Date endDate);
	 
	 /**
	  * 根据酒店所在城市Id获取所在城市的行政区与商业区
	  * @param cityId
	  * @return
	  */
	public Map queryBusinessForCityId(String cityId);
	
	
	/**
	 * 根据城市ID和酒店品牌ID查询出对应的酒店 add by haibo.li 网站改版
	 * @param cityCode
	 * @param parentId
	 * @return
	 */
	public List<HtlHotel> queryParentHotelInfo(String cityCode,String parentId);
	
    /**
     * 网站查配额
     * @param hotelId
     * @param roomTypeId
     * @param beginDate
     * @param endDate
     * @return
     */
    public List<Date> queryQtyForWebHK(long hotelId, long roomTypeId,
			Date beginDate, Date endDate);
    
    /**
     * 获取可能的预订日期字符串
     * @param hotelId
     * @param roomTypeId
     * @param childRoomTypeId
     * @param checkIn
     * @param checkOut
     * @return
     */
    public String getCanBookDatesForWebHK(Long hotelId, Long roomTypeId,
            Long childRoomTypeId, Date checkIn, Date checkOut);
    
    /**
     * 查询页数，页码，总记录数
     * @param queryBean
     * @return
     */
    public HotelPageForWebBean queryHotelPagesForWeb(QueryHotelForWebBean queryBean);
    
    /**
	  * 根据酒店所在城市Id和行政区或商业区的编号，查询行政区或商业区的中文名
	  * @param cityName  add by shengwei.zuo 2010-3-8
	  * @return
	  */
    public String querydisBusNaForCityNa(String cityName,String disBusCode);
    
    /**
	  * 根据品牌ID 和城市ID查询出对应的品牌名称
	  * @param cityName  add by shengwei.zuo 2010-3-9
	  * @return
	  */
	public String queryParentNaHtlInfo(String cityCode,String parentId); 
	
	/**
     * 增加酒店点击量记录
     * 
     * @param hwClickAmount
     * @return
     */
    public long addClickAmount(HwClickAmount hwClickAmount);
    
    /**
     * 根据城市查询出该城市下酒店的“今日低价”、“芒果推荐”、“最畅销”的内容
     */
    public Map queryHwHotelIndexItems(String mark);
    
    /**
     * 添加酒店点击来源渠道日志
     * @param clickLog
     */
     public void addChannelLog(HtlChannelClickLog clickLog);
     
     /**
      * 更新渠道日志的点击
      * @param logId
      */
     public void updateChannelLog(long logId);

     /**
      * 获取request的外网ip
      * @param request
      * @return
      */
     public String getIpAddr(HttpServletRequest request);
     
     /**
      * 从cookie中获取网站合作的渠道号
      * @return
      */
     public String getProjectCodeForWeb(HttpServletRequest request);
     
     /**
      * 查询网站订单统计数据
      * @return
      */
     public Long queryNetOrderStatistik();
     
     /**
      * 查询所有有效的酒店
      * @return
      */
	public List queryAllHotleActive(); 
	/**
     * 查询房型
     */
	public HtlRoomtype queryHtlRoomTypeForWeb(long hotelId, long roomTypeId);
	 /**
	  * 查询芒果网站首页特推酒店所需数据
	  * @return List
	  */
	public List<HwHotelIndex> queryTTHtlData();
}

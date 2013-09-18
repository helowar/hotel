package com.mangocity.hotel.base.manage;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mangocity.hotel.base.manage.assistant.AddPrice;
import com.mangocity.hotel.base.manage.assistant.InputPrice;
import com.mangocity.hotel.base.persistence.HtlBatchMtnPrice;
import com.mangocity.hotel.base.persistence.HtlBatchSalePrice;
import com.mangocity.hotel.base.persistence.HtlHdlAddscope;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.persistence.HtlRoom;
import com.mangocity.util.bean.ContinueDatecomponent;
import com.mangocity.util.bean.DateSegment;

/**
 */
public interface IPriceManage {

	/**
     * 价格计算，根据底价算佣金或根据佣金算底价
     * 
     * @param inputPrices
     * @param contractId
     * @return
     */
    public List<InputPrice> calculatePrice(List<InputPrice> inputPrices, Long contractId);

    /**
     * 按时间段更新批量保存或更新价格
     * 
     * @param hotelId		酒店ID
     * @param inputPrices	批量录入价格
     * @param dateSegments	批量调整的时间段
     * @param contractId	合同ID
     * @param quotaType		配额类型
     * @param week
     * @return
     */
    public void updatePrice(Long hotelId, List<InputPrice> inputPrices, List<DateSegment> dateSegments, 
    		Long contractId, String quotaType, Integer[] week);

    /**
     * 批量查询价格
     * 
     * @param hotelId		酒店ID
     * @param roomType      房型
     * @param dateSegments	日期段
     * @param week			星期        
     * @return 房间列表
     */
    public List<HtlRoom> batchQueryPrice(Long hotelId, Long[] roomType, List<DateSegment> dateSegments, 
    		Integer[] week);

    /**
     * 按天更新价格
     * 
     * @param hotelId		酒店ID
     * @param inputPrices
     * @return
     */
    public void updatePrice(Long hotelId, List<InputPrice> inputPrices);

    /**
     * 按量加幅
     * 
     * @param hotelId		酒店ID
     * @param addPrices		AddPrice 类实例的集合，主要用来传递加幅金额，房型，子房型等数据
     * @param dateSegments	时间段
     * @param quotaType		配额类型
     * @param week
     * @return 
     */
    public void batchAddPrice(Long hotelId, List<AddPrice> addPrices, List<DateSegment> dateSegments, 
    		String quotaType, Integer[] week);

    /**
     * 列出这个房间的所有子房型的价格
     * 
     * @param roomId	房间ID
     * @return
     */
    public List getRoomPrices(long roomId);

    /**
     * 查询价格信息 
     * 
     * @param hotelId			酒店ID
     * @param lstDate			要更新的日期集合
     * @param weeks				要更新的星期
     * @param priceTypeIds		要更新的价格类型ID
     * @param quotaType			配额类型
     * @param payMethodForQurey	付款方式
     * @return HtlPrice 对象集合
     */
    public List<HtlPrice> queryPriceInAddPrice(Long hotelId, List<DateSegment> lstDate, String[] weeks, 
    		String[] priceTypeIds, String quotaType, String payMethodForQurey);

    /** 
     * 根据酒店ID获得酒店的房型
     * 
     * @param hotelId	酒店ID
     * @return	
     */
    public Map<Long, String> getRoomtypeList(Long hotelId);

    /**
     * 根据酒店ID查询价格类型
     * 
     * @param hotelId	酒店ID
     * @return
     */
    public Map<Long, String> getPriceTypeList(Long hotelId);

    /**
     * 插入数据到批次销售价格
     * 
     * @param htlbatchsaleprice	批次销售价格
     * @return
     */
    public void saveBatchSalePrice(HtlBatchSalePrice batchSalePrice);
    
    /**
     * 批量插入数据到批次销售价格
     * 
     * @param htlbatchsaleprice	批次销售价格
     * @return
     */
    public void saveBatchSalePriceList(List<HtlBatchSalePrice> batchSalePrice);

    /**
     * 查询批次销售价格
     * 
     * @param contractId		合同ID
     * @param start				起始日期
     * @param end				截至日期
     * @param childRoomTypeId	房型ID
     * @param payMethod			付款方式
     * @return	批次销售价格列表
     */
    public List<HtlBatchSalePrice> queryPrice(Long contractId, Date start, Date end, Long[] childRoomTypeId, 
    		String[] payMethod);

    /**
     * 根据ID查询批次销售价格
     * 
     * @param priceId		批次销售ID
     * @return	批次销售价格
     */
    public HtlBatchSalePrice findPrice(Long priceId);
    
    /**
     * 更新批次销售价格
     * 
     * @param htlbatchsaleprice	批次销售价格
     * @return
     */
    public void updateBatchSalePrice(HtlBatchSalePrice price);

    /**
     * 更新价格
     * 
     * @param hotelId		酒店ID
     * @param contractId	合同ID
     * @param quotaType		配额类型
     * @param currency
     * @param week			
     * @return	操作是否成功的标志
     */
    public String saveOrUpdatePrice(long hotelId, long contractId, String quotaType, String currency, 
    		String week);
    
    /**
     * 批量更新价格
     * 
     * @param lstPrice	价格列表
     * @return
     */
    public void saveOrUpdatePrice(List<HtlBatchMtnPrice> lstPrice);

    /**
     * 更新价格
     * 			
     * @return	
     */
    public void saveOrUpdatePrice();

    /**
     * 查询已经录入价格类型的最早和最晚日期 
     * 
     * @param contractId	合同ID
     * @return	批次销售价格列表
     */
    public List<HtlBatchSalePrice> getAlreadyLatestAndEarliest(long contractId);

    /**
     * 保存加幅记录
     * 
     * @param htlHdlAddscope	加幅记录
     * @return	是否保存标志 
     */
    public String saveOrUpdateAddScope(HtlHdlAddscope htlHdlAddscope);

    /**
     * 获得加幅记录
     * 
     * @param hotelId	酒店ID
     * @return	加幅记录列表 
     */
    public List<HtlHdlAddscope> loadAddScope(Long hotelId);

    /**
     * 检查酒店价格类型以前是否存在加幅
     * 
     * @param newAllPriceId
     * @param hotelId		酒店ID
     * @param entityID
     * @param lsContinueDatecomponent
     * @return	价格ID列表
     */
    public List<String> checkIsAddScope(String newAllPriceId, Long hotelId, Long entityID,
        List<ContinueDatecomponent> lsContinueDatecomponent);
    
    /**
     * 根据销售日期，酒店ID，房型ID，价格ID和付款方式查询价格
     * 
     * @param ableSaleDate
     * @param hotelId
     * @param roomTypeID
     * @param priceTypeId
     * @param payMethod
     * @return
     */
    public HtlPrice getPricInfoByFor(Date ableSaleDate, long hotelId, long roomTypeID, long priceTypeId, 
    		String payMethod);
    
    
    /**
     * 获得HtlPrice对象
     * 
     * @param priceId
     * @return
     */
    public HtlPrice getHtlPrice(long htlPriceId);
    
    /**
     * 更新HtlPrice
     * 
     * @param htlPrice
     */
    public void updateHtlPrice(HtlPrice htlPrice);
    
    /**
     * 根据价格类型，付款方式和配额类型查询在指定可售日期范围内的价格
     * 
     * @param beginDate
     * @param endDate
     * @param childRoomTypeId
     * @param payMethod
     * @param quotaType
     * @return
     */
    public List<HtlPrice> getHtlPriceBySaleDateRangePriceType(Date beginDate, Date endDate, long childRoomTypeId, 
            String payMethod, String quotaType);
    
    /**
     * 根据多酒店ID、开始日期、结束日期查询出价格
     * @param hotelIdList
     * @param startDate
     * @param endDate
     * @return
     */
    public List<HtlPrice> getHtlPriceByHotelIds(String hotelIds, Date startDate, Date endDate);
    
    /**
     * 
     * @param hotelId
     * @param priceTypeIte
     * @param beginD
     * @param endD
     * @return
     */
    public long getEleDayPriceNum(long hotelId,long priceTypeIte,Date beginD,Date endD);
    
    /**
	 * 一个房型在相应时间段内的面付个数
	 * @param hotelId
	 * @param priceTypeIte
	 * @param beginD
	 * @param endD
	 * @return
	 */
	public long getEleDayPayPriceNum(long hotelId,long priceTypeIte,Date beginD,Date endD);
    
    /**
	 * 根据价格类型id，获得价格类型的名称
	 * @param priceTypeID
	 * @return
	 */
	public String getPriceTypeNameById(long priceTypeID);

}
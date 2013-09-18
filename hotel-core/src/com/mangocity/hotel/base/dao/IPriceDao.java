package com.mangocity.hotel.base.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.mangocity.hotel.base.persistence.HtlAddscopeHdr;
import com.mangocity.hotel.base.persistence.HtlBatchMtnPrice;
import com.mangocity.hotel.base.persistence.HtlBatchSalePrice;
import com.mangocity.hotel.base.persistence.HtlHdlAddscope;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.persistence.HtlPriceLOG;
import com.mangocity.hotel.base.persistence.HtlPriceType;
import com.mangocity.hotel.base.persistence.HtlRoom;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.util.bean.ContinueDatecomponent;
import com.mangocity.util.bean.DateSegment;

/**
 * 价格及税率的查询和录入
 * 
 * @author zhengxin
 * 
 */
public interface IPriceDao {
    /**
     * 批量查询价格
     * 
     * @param hotelId	酒店ID
     * @param roomType       
     * @param dateSegments	日期段
     * @param week         
     * @return 房间列表
     */
    public List<HtlRoom> batchQueryPrice(Long hotelId, Long[] roomType, List<DateSegment> dateSegments, Integer[] week);

    /**
     * 列出房间的所有子房型的价格
     * 
     * @param roomId	房间ID
     * @return 价格列表
     */
    public List getRoomPrices(long roomId);
    
    /** 
     * 根据酒店ID获得酒店的房型
     * 
     * @param hotelId	酒店ID
     * @return	房型列表
     */
    public List<HtlRoomtype> getRoomtypeList(Long hotelId);
    
    /**
     * 根据酒店ID取到价格类型
     * 
     * @param hotelId	酒店ID
     * @return	价格类型列表
     */
    public List<HtlPriceType> getPriceTypeList(Long hotelId);
    
    /**
     * 根据酒店ID取得调价信息
     * 
     * @param HotelID
     * @return 调价列表
     */
    public List getChangePriceWarnings(long HotelID);

    /**
     * 查询价格信息 hotelId :酒店id lstDate : 要更新的日期集合， weeks ：要更新的星期 例： '1，2，3' priceTypeId :
     * 要更新的价格类型id，如'12,33,22'
     * 
     * @param hotelId			酒店ID
     * @param lstDate			要更新的日期集合
     * @param weeks				要更新的星期
     * @param priceTypeIds		要更新的价格类型
     * @param quotaType			配额类型
     * @param payMethodForQurey	付款方式
     * @return	HtlPrice 对象集合
     */
    public List<HtlPrice> queryPriceInAddPrice(Long hotelId, List<DateSegment> lstDate, String[] weeks,
        String[] priceTypeIds, String quotaType, String payMethodForQurey);

    /**
     * 
     * @param contractId
     * @param start
     * @param end
     * @param childRoomTypeId
     * @param payMethod
     * @return
     */
    public List<HtlBatchSalePrice> queryPrice(Long contractId, Date start, Date end, Long[] childRoomTypeId, 
    		String[] payMethod);

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
    public List<HtlBatchSalePrice> getAlreadyLatAndEart(long contractId);

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
     * 根据房型、子房型、销售日期及支付方式获得某一条价格记录 如果子房型为空，则系统会认为该房型下没有子房型
     * 
     * @param roomTypeID
     * @param childRoomTypeID
     * @param saleDate
     * @param payMethod
     * @param quotaType
     * @return
     */
    public HtlPrice findRoomPrice(Long roomTypeID, Long childRoomTypeID, Date saleDate,
        String payMethod, String quotaType);
    
    /**
     * 
     * 
     * @param batchSalePrice
     */
    public void saveBatchSalePrice(HtlBatchSalePrice batchSalePrice);
    
    /**
     * 批量保存
     * 
     * @param batchSalePriceList
     */
    public void saveBatchSalePriceList(List<HtlBatchSalePrice> batchSalePriceList);
    
    /**
     * 批量更新或保存价格
     * 
     * @param htlPriceList
     */
    public void batchSaveOrUpdatePrice(List<HtlPrice> htlPriceList);
    
    /**
     * 
     * 
     * @param batchMtnPriceList
     */
    public void batchSaveOrUpdateMtnPrice(List<HtlBatchMtnPrice> batchMtnPriceList);
    
    /**
     * 更新或保存日志，包括日志头信息和日志详细信息
     * 
     * @param htlAddscopeHdr
     */
    public void saveOrUpdateAddscopeHdr(HtlAddscopeHdr htlAddscopeHdr);
    
    /**
     * 查询HtlPrice对象
     * 
     * @param priceId
     * @return
     */
    public HtlPrice qryHtlPrice(long priceId);
    
    /**
     * 更新HtlPrice
     * 
     * @param htlPrice
     */
    public void updateHtlPrice(HtlPrice htlPrice);
    
    /**
     * 
     * @param priceId
     * @return
     */
    public HtlBatchSalePrice loadBatchSalePrice(long priceId);
    
    /**
     * 批量更新或保存价格日志
     * 
     * @param htlPriceLogList
     */
    public void batchSaveOrUpdatePriceLong(List<HtlPriceLOG> htlPriceLogList);
    
    /**
     * 
     * @param batchSalePrice
     */
    public void updateBatchSalePrice(HtlBatchSalePrice batchSalePrice);
    
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
    public HtlPrice qryPricInfoByFor(Date ableSaleDate, long hotelId, long roomTypeID, long priceTypeId, 
    		String payMethod);
    
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
    public List<HtlPrice> qryHtlPriceBySaleDateRangePriceType(Date beginDate, Date endDate, long childRoomTypeId, 
            String payMethod, String quotaType);
    
    /**
     * 根据多酒店ID、开始日期、结束日期查询出价格
     * @param hotelIdList
     * @param startDate
     * @param endDate
     * @return
     */
    public List<HtlPrice> queryHtlPriceByHotelIds(String hotelIds, Date startDate, Date endDate);
    
    
    public long qryEleDayPriceNum(long hotelId,long priceTypeIte,Date beginD,Date endD);
    
    /**
	 * 一个房型在相应时间段内的面付个数
	 * @param hotelId
	 * @param priceTypeIte
	 * @param beginD
	 * @param endD
	 * @return
	 */
	public long qryEleDayPayPriceNum(long hotelId,long priceTypeIte,Date beginD,Date endD);
	
	/**
	 * 根据价格类型id，获得价格类型的名称
	 * @param priceTypeID
	 * @return
	 */
	public String qryPriceTypeNameById(long priceTypeID);

}

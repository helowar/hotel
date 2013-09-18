package com.mangocity.tmc.service;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hweb.persistence.HotelPageForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebSaleItems;
import com.mangocity.tmc.persistence.view.HotelInfoTmc;
import com.mangocity.tmc.persistence.view.HotelQueryConditionTmc;
import com.mangocity.tmc.service.assistant.HotelQueryCondition;
import com.mangocity.tmc.service.assistant.HotelQueryPageInfo;
import com.mangocity.tmccommon.persistence.Company;





public interface IHotelQueryService {
	/**
     * 呼叫中心查询酒店,只查询出酒店推荐的房型
     * @param hotelQueryCondition 传查询酒店的条件
     * @param pageNo 页码
     * @return 返回呼叫中心查询酒店的结果 酒店查询结果分页信息
     * 加了一个参数xuyun
     */
	public HotelQueryPageInfo queryHotel(HotelQueryCondition hotelQueryCondition,int pageNo) throws Exception;	
    
	/**
	 * 用于分页
	 * @param hotelQueryCondition
	 * @param pageNo
	 * @return
	 */
//	public HotelQueryPageInfo queryPerpageHotel(
//			HotelQueryCondition hotelQueryCondition, int pageNo)throws Exception;
	/**
	 * 对酒店按销售的最低价格进行排序
	 * @param aOrder升降序方式
	 * @param aOrderType升降序类别
	 * @return HotelQueryPageInfo对象
	 * @throws Exception
	 */
//	public HotelQueryPageInfo sortHotel(HotelQueryCondition hotelQueryCondition,String aOrder,String aOrderType)throws Exception;
    
    /**
     * CC查询协议酒店
     * 
     * @author chenkeming Sep 28, 2009 
     * @param hotelQueryCondition
     * @param member
     * @return
     */
    public List<HotelInfoTmc> queryHotelForCC(
            HotelQueryConditionTmc condTmc, MemberDTO member);
    
    /**
     * 网站查询协议酒店
     * 
     * @author chenkeming Sep 29, 2009
     * @param condWeb
     * @param companyCd
     * @param companyId
     * @return
     */
    public HotelPageForWebBean queryHotelForWeb(
            QueryHotelForWebBean condWeb, String companyCd, Long companyId);
    
    /**
     * 查询TMC酒店价格列表(前后日期)
     * add by shizhongwen
     * 时间:Oct 15, 2009  5:15:45 PM
     * @param hotelId
     * @param roomTypeId
     * @param beginDate
     * @param endDate
     * @param minPrice
     * @param maxPrice
     * @param payMethod
     * @param company
     * @return
     */
    public List<QueryHotelForWebSaleItems> queryTMCPriceForWeb(long hotelId,long roomTypeId,
         Date beginDate, Date endDate, double minPrice, double maxPrice,
        String payMethod,Company company);
    
    /**
     * 查询TMC酒店价格列表
     * add by shizhongwen
     * 时间:Oct 15, 2009  5:15:35 PM
     * @param hotelID
     * @param roomTypeId
     * @param beginDateExt
     * @param endDateExt
     * @param minPrice
     * @param maxPrice
     * @param payMethod
     * @param company
     * @return
     */
    public List<QueryHotelForWebSaleItems> queryTMCPriceDetailForWeb(Long hotelID,long roomTypeId,
         Date beginDateExt, Date endDateExt,
         double minPrice, double maxPrice, String payMethod,Company company) ;
	
}

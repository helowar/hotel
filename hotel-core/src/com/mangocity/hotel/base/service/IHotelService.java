package com.mangocity.hotel.base.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlBookSetup;
import com.mangocity.hotel.base.persistence.HtlCreditAssure;
import com.mangocity.hotel.base.persistence.HtlExhibit;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlPreconcertItem;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.service.assistant.BookRoomCondition;
import com.mangocity.hotel.base.service.assistant.HotelInfo;
import com.mangocity.hotel.base.service.assistant.HotelPriceSearchParam;
import com.mangocity.hotel.base.service.assistant.HotelQueryCondition;
import com.mangocity.hotel.base.service.assistant.QueryCreditAssureForCCBean;
import com.mangocity.hotel.base.service.assistant.UserComment;

/**
 * 
 * @author xiaowumi 对外酒店本部服务提供接口
 */

public interface IHotelService {

    /**
     * @author lixiaoyong 查修改订单的价格详情的时候
     * @param HotelPriceSearchParam
     *            hotelPriceSearchParam
     * @param subParam
     *            传入参数的map
     * @return 某个酒店的某个房型的价格详情列表
     */
    public List getHotelPriceList(HotelPriceSearchParam hotelPriceSearchParam);

    /**
     * @author lixiaoyong
     * @param queryHotelPriceList
     *            查询的ID
     * @param subParam
     *            传入参数的map
     * @return 某个酒店的某个房型的价格详情列表
     */
    public List queryHotelPriceList(String queryHotelPriceList, Map subParam);

    public List<HotelInfo> queryHotels(HotelQueryCondition hotelQueryCondition) throws SQLException;

    /**
     * 回收配额
     * 
     * @param hotelId
     * @param roomTypeId
     *            房型Id
     * @param quotaItems
     *            收回的配额项
     */
    public void recycleQuota(Long hotelId, Long roomTypeId, List quotaItems);

    /**
     * 记录呼出配额
     * 
     * @param hotelId
     * @param roomTypeId
     * @param quotaItems
     */
    public void registerQuota(Long hotelId, Long roomTypeId, List quotaItems);

    /**
     * 根据hotelId返回HotelQueryResult信息
     * 
     * @param hotelId
     * @return HotelQueryResult
     */
    public HotelInfo getHotelInfoById(long hotelId);

    /**
     * 根据入住日期，退房日期，如果用户的入住日期与退房日期中的任一天，符合酒店促销信息的规定的促销 日期段，则将返回酒店的促销信息
     * 
     * @param bc
     * @return 酒店促销信息字符串，如果返回""表示没有促销信息
     */
    public String getHotelPresale(BookRoomCondition bc);

    /**
     * 获取房型备注信息
     * 
     * @param roomTypeId
     *            房型ID
     * @return 备注信息
     */
    public String getRoomRemark(long roomTypeId);

    /**
     * 更新用户评论
     * 
     * @param uc
     * @return 成功返回0 失败返回非0数
     */
    public int saveOrUpdateUsesComment(UserComment uc);

    /**
     * CC查询担保预定条款
     * 
     * @param QueryCreditAssureForCCBean
     * @return List<HtlCreditAssure>
     */
    public List<HtlCreditAssure> queryCreditAssureForCC(QueryCreditAssureForCCBean queryBean);

    /**
     * CC查询当前发送酒店传真的号码
     * 
     * @param
     * @return String
     */
    public String getHotelFaxNo(long hotelId);

    /**
     * CC查询当前发送酒店传真的号码
     * 
     * @param
     * @return String
     */
    public String getHotelMail(long hotelId);

    /**
     * 取出预定传真列表
     * 
     * @param
     * @return List
     */
    public List getHtlBookSetupList(long hotelId, String bookctctType);

    /**
     * 根据ID查找酒店信息
     * 
     * @param hotel_id
     * @return HtlHotel对象
     */
    public HtlHotel findHotel(long hotel_id);

    /**
     * 查询预订条款，返回一个字符串
     * 
     * @return
     */
    public String qryCreditAssure(long hotelId, Date beginDate, String roomType);

    /**
     * 查询默认联系方式
     * 
     * @param
     * @return String
     */
    public String getHotelSendType(String hotelid);

    /**
     * v2.6 订单修改基本信息查询酒店
     * 
     * @author chenkeming Feb 5, 2009 4:38:03 PM
     * @param condition
     * @return
     * @throws SQLException 
     */
    public HotelInfo queryBaseInfoHotel(HotelQueryCondition condition) throws SQLException;

   

    /**
     * 酒店预定条款操作日志明细记录方法 add by haibo.li 参数(1,新的修改过后的对象，以前数据库中没有修改过的对象) 该方法用于比较
     * 预定条款按日期修改，2个对象中修改了那些属性，都于记录下来，便于查看
     */
    public void putRecord(HtlPreconcertItem newPreconcertItem, HtlPreconcertItem oldPreconcertItem,
        long id);

    /**
     * 查询指定城市某日期内的会展信息 add by juesu.chen
     * 
     * @param city
     *            城市
     * @param exhibitStartDate
     *            会展开始日期
     * @param exhibitEndDate
     *            会展结束日期
     * @return
     */

    public List<HtlExhibit> queryExhibit(String city, Date exhibitStartDate, Date exhibitEndDate);

   

    /**
     * add by shengwei.zuo hotel 2.9.2 根据酒店ID,价格类型和起始日期查询出提示信息
     */
    public String queryAlertInfoStr(long hotelid, String priceTypeId, Date begin, Date end,
        String ccOrweb);
    
    
  
    
     /**
	* add by zhijie.gu 2009-09-15 hotel2.9.3 获取价格表的佣金，
	*/
    public HtlPrice qryHtlPriceForCC(long childRoomId, Date date,String payMethod, String quotaType);
   
    public String getHotelFaxNo(long hotelId,long childRoomTypeId);
    
    public String getHotelFaxNo(List<HtlBookSetup> hbList,HtlHotel hotel,String bookctctType);
    
    public List<HtlBookSetup> getSupplierFax(long hotelId,long childRoomTypeId);
    
    public String getHotelSendType(long hotelId,Long childRoomTypeId);
}

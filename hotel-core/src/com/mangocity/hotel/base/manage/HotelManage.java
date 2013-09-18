package com.mangocity.hotel.base.manage;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.manage.assistant.HotelAddressInfo;
import com.mangocity.hotel.base.persistence.ExMapping;
import com.mangocity.hotel.base.persistence.HtlCoverPicture;
import com.mangocity.hotel.base.persistence.HtlChannelMapInfo;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlCreditAssure;
import com.mangocity.hotel.base.persistence.HtlCtct;
import com.mangocity.hotel.base.persistence.HtlCtlDsply;
import com.mangocity.hotel.base.persistence.HtlEbooking;
import com.mangocity.hotel.base.persistence.HtlEbookingFunctionMaster;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlHotelBase;
import com.mangocity.hotel.base.persistence.HtlHotelExt;
import com.mangocity.hotel.base.persistence.HtlHotelScore;
import com.mangocity.hotel.base.persistence.HtlIncreasePrice;
import com.mangocity.hotel.base.persistence.HtlPicture;
import com.mangocity.hotel.base.persistence.HtlPictureInfo;
import com.mangocity.hotel.base.persistence.HtlPictureUrl;
import com.mangocity.hotel.base.persistence.HtlPreconcertItem;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.persistence.HtlPriceType;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.persistence.HtlUsersComment;
import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.hotel.base.service.assistant.HotelQueryCondition;

/**
 */
public interface HotelManage {
    /**
     * 新增一个酒店
     * 
     * @param hotel
     *            酒店对象
     * @return hotel_id;
     */
    public Long createHotel(HtlHotel hotel);

    /**
     * 查询酒店
     * 
     * @param htlQryCond
     *            酒店查询条件对象
     * @return Hotel 酒店实体列表
     */
    public List queryHotel(HotelQueryCondition htlQryCond);

    /**
     * 重新找回酒店信息
     * 
     * @param hotel_id
     *            酒店id;
     * @return 酒店一个实体
     */
    public HtlHotel findHotel(long hotel_id);

    public List findHtlEbooking(long hotel_id);

    /**
     * 修改酒店对象
     * 
     * @param htl
     *            酒店实体对象
     * @return 修改记录的结果，1=表示修改成功，0=表示修改失败
     */
    public int modifyHotel(HtlHotel htl);

    /**
     * 新建一个房型
     * 
     * @param roomType
     *            房型实体对象
     * @return room_type_id; 房型实体的id;
     */
    public Long createRoomType(HtlRoomtype roomType);

    /**
     * 修改房型
     * 
     * @param roomType
     *            房型实体对象
     * @return 修改记录的结果，1=表示修改成功，0=表示修改失败
     */
    public int modifyRoomType(HtlRoomtype roomType);

    /**
     * 删除房型
     * 
     * @param roomTypeId
     *            房型的实体id;
     * @return 删除记录的结果，1=表示删除成功，0=表示删除失败
     */
    public int deleteRoomType(long roomTypeId);

    /**
     * 删除房型
     * 
     * @param roomTypeId
     *            房型的实体id;
     */
    public void deleteAllRoomType(long roomTypeId);

    /**
     * 删除价格类型所对应的价格
     * 
     * @param childRoomTypeId
     *            价格类型id
     */
    public void delHtlPrice(long childRoomTypeId);

    /**
     * 禁用房型
     * 
     * @param roomTypeId
     *            房型的实体id;
     * @return 禁用房型记录的结果，1=表示禁用房型成功，0=表示禁用房型失败
     */
    public int disableRoomType(long roomTypeId);

    /**
     * 启用一个房型
     * 
     * @param roomTypeId房型的实体id
     *            ;
     * @return 启用房型记录的结果，1=表示启用房型成功，0=表示启用房型失败
     */
    public int enableRoomType(long roomTypeId);

    /**
     * 禁用一个酒店
     * 
     * @param hotelId
     *            酒店实体id
     * @return 禁用酒店结果 1=表示禁用酒店成功，0=表示禁用酒店失败
     */
    public int disableHotel(long hotelId);

    /**
     * 启用一个酒店
     * 
     * @param hotelId
     *            酒店实体id
     * @return 启用酒店结果 1=表示启用酒店成功，0=表示启用酒店失败
     */
    public int enableHotel(long hotelId);

    /**
     * 列出一个酒店的所有房型(去除香港的房型) add by shizhongwen 时间:Apr 22, 2009 4:18:11 PM
     * 
     * @param hotelId
     * @return
     */
    public List lstRoomTypeByHotelIdRemoveHK(long hotelId);

    /**
     * 删除一个酒店信息，目前只删除酒店信息表中的信息
     */
    public int delHotelInfo(long hotelId);

    /**
     * 根据酒店ID查询相应的酒店联系人信息
     * 
     * @param hotelId
     * @return
     */
    public List qryHtlCtct(long hotelId);

    /**
     * 根据酒店ID查询相应的酒店销售季节信息
     * 
     * @param hotelId
     * @return
     */
    public List qryHtlSellSeason(long hotelId);

    /**
     * 保存或更新酒店联系人信息，如果实体中有酒店联系人ID则更新，否则新增
     * 
     * @param htlCtct
     * @return
     */
    public long saveOrUpdateCtct(HtlCtct htlCtct);

    /**
     * 保存或更新酒店信息，如果实体中有酒店ID则更新，否则新增
     * 
     * @param hotelId
     * @return
     */
    public long saveOrUpdateHotel(HtlHotel htlHotel);

    /**
     * 通过酒店id,开始日期，结束日期,选择的星期，选择的房型 找出房间列表
     * 
     * @param hotelId
     *            酒店id
     * @param beginDate
     *            开始日期
     * @param endDate
     *            结束日期
     * @param week
     *            选择的星期
     * @param roomTypes
     *            选择的房型
     * @return 房间列表
     */
    public List qryRoom(long hotelId, Date beginDate, Date endDate, String[] week,
        String[] roomTypes);

    /**
     * insert酒店的图片
     */
    public Long createHtlPicture(HtlPicture htlPicture);

    /**
     *根据酒店ID找酒店图片
     */
    public List findHtlPicture(long hotel_id);

    /**
     * 酒店照片更新
     */
    public Long updateHtlPicture(HtlPicture htlPicture);

    /**
     * cc查询酒店列表
     */
    public List qryLisHotelToCC(String sql, int startIndex, int maxResult, Object[] obj);

    /**
     * cc查询酒店房间列表
     */
    public List qryLisRoomTypesToCC(String sql, Object[] obj);

    /**
     * 查询房型对象列表
     * 
     * @param hotelId
     *            酒店id
     * @param roomTypes
     *            房型Id集合
     * @return
     */
    public List qryRoomTypeBedType(long hotelId, String[] roomTypes);

    /**
     * 查询信用卡担保信息
     * 
     * @param hotelId
     *            酒店id
     * @param beginDate
     *            开始日期
     * @param endDate
     *            结束日期
     * @return List 信用卡担保信息集合，当开始日期和结束日期都在信用卡担保的日期内返回一条记录，否则多条记录
     */
    public List<HtlCreditAssure> qryCreditAssureForCC(long hotelId, Date beginDate, Date endDate,
        String roomType);

    /**
     * 通过合同id拿到合同
     * 
     * @param contractId
     * @return
     */
    public HtlContract findContract(long contractId);

    /**
     * 在开关房时填入的备注信息将添加到酒店的备注信息中
     * 
     * @param hotelId
     *            酒店id
     * @param notes
     *            备注
     */
    public int updateHotelNotes(long hotelId, String notes);
    // 查询3D图片信息列表
    public List queryHotelPicList(HtlPictureInfo hotelPicInfo);

    // 查询3D图片信息
    public HtlPictureInfo queryHotelPicById(HtlPictureInfo hotelPicInfo);

    // 增加3D图片信息
    public boolean addHotelPic(HtlPictureInfo hotelPicInfo);

    // 修改3D图片信息
    public boolean modifyHotelPic(HtlPictureInfo hotelPicInfo);

    // 删除3D图片信息
    public boolean deleteHotelPic(HtlPictureInfo hotelPicInfo);

    // 查询酒店评论表
    public List<HtlUsersComment> queryHtlUsersComment(long hotelId, int startIndex, int maxResults);

    // 查询酒店全部评论
    public List<HtlUsersComment> queryHtlUsersAllComment(long hotelId);

    // 查询酒店评论表总数
    public int queryHtlUsersCommentTotal(long hotelId);

    /**
     * 统计该会员一年内的发表评论的次数
     * 
     * @param memberId
     *            会员ID
     * @param hotelId
     *            酒店ID
     * @return int
     * @author CMB
     */
    public int countCommentMember4Hotel(long memberId, long hotelId);

    /**
     * 统计该会员一年内入住该酒店的次数
     * 
     * @param memberId
     *            会员ID
     * @param hotelId
     *            酒店ID
     * @return int
     * @author CMB
     */
    public int countOrderMember4Hotel(long memberId, long hotelId);

    // 插入酒店评论
    public void addHtlUsersComment(HtlUsersComment htlUsersComment);

    public String findCommendTypeByHotelId(long hotelId);

    /**
     * 根据酒店ID查询相应设定的假期
     * 
     * @param hotelId
     * @return
     */
    public List qryHoliday(Long hotelId);

    /**
     * 查询信用卡担保信息
     * 
     * @param hotelId
     *            酒店id
     * @param beginDate
     *            开始日期
     * @param endDate
     *            结束日期
     * @return List 信用卡担保信息集合，当开始日期和结束日期都在信用卡担保的日期内返回一条记录，否则多条记录
     */
    public HtlCreditAssure qryCreditAssureForTMC(long hotelId, Date queryDate, String roomType);
    /**
     * 重新找回分数信息
     * 
     * @param score_id
     *            分数id;
     * @return 分数一个实体
     */
    public HtlHotelScore findScore(long score_id);

    /**
     * 保存分数
     * 
     * @param
     * @return
     */
    public long saveOrUpdateScore(HtlHotelScore htlScore);

    /**
     * 找前一天到现在的所有房态更改记录
     * 
     * @param hotelId
     * @return
     */
    public List findRoomStatusDateProcess(long hotelId);

    /**
     * 取汇率,返回所有兑换人民币的MAP
     * 
     * @return
     */
    public Map getExchangeRateMap();

    /**
     * 查询预订条款，返回一个字符串
     * 
     * @return
     */
    public String qryCreditAssure(long hotelId, Date beginDate, String roomType);

    /**
     * 查询某一家酒店的所有房型和价格类型组合
     * 
     * @param hotelID
     * @return list<RoomTypePriceTypeBean>
     */
    public List findRoomTypePriceTypeLis(long hotelID);

    /**
     * 修改房型的床型时，需要同步更新htl_room表中的房态信息
     */
    public void updateRoomState(long roomTypeId, String bedType);

    /**
     * 查询某一家酒店的所有房型
     * 
     * @param hotelID
     * @return list<RoomTypePriceTypeBean>
     */
    public List findRoomTypeLis(long hotelID);

    /**
     * 更新酒店基本信息表的外网是否显示基本信息字段
     * 
     * @param hotelId
     * @param webShowBase
     * @return
     */
    public int updateHotelWebShowBase(final long hotelId, final String webShowBase);

    /**
     * 查询关房记录
     * 
     */
    // 向附加表里插入一条记录.房态负责人
    public void saveHtlHotelExt(HtlHotelExt he);

    /**
     * 保存酒店ebooking设置
     * 
     * @param ek
     */
    public void saveHtlEbooking(HtlEbooking ek);
    /**
     * 查询用户权限
     * @param hotelId
     * @param loginName
     * @return
     */
    public List<HtlEbookingFunctionMaster> queryEbookingMaster(String hotelId,String loginName);

    // 查询一个酒店3D图片的数量
    public int getPictureCountByHotelId(long hotelId);

    public List queryCloseRoom(long hotelID);

    public void saveOrUpdateExt(HtlHotelExt he);

    public long merge(HtlHotel htlHotel);

    public long saveOrUpfateHtlCtct(HtlHotel htlHotel);

    public List queryHtlCtct(Long id);

    /**
     * 更新映射酒店
     * 
     * @param exMapping
     * @return
     * @author chenjiajie 2008-11-24 Version 2.5
     */
    public Long saveOrUpdateMapping(ExMapping exMapping);

    /**
     * 查询酒店映射
     * 
     * @param hotelId
     * @param mappingType
     *            映射类别
     * @return
     * @author chenjiajie 2008-11-24 Version 2.5
     */
    public List<ExMapping> findHotelMapping(long hotelId, long mappingType);

    /**
     * 查询酒店映射
     * 
     * @param hotelId
     * @param mappingType
     *            映射类别
     * @param channelType
     *            合作商类别
     * @return
     * @author chenjiajie 2008-11-26 Version 2.5
     */
    public List<ExMapping> findHotelMapping(long hotelId, long mappingType, long channelType);

    /**
     * 查询酒店映射
     * 
     * @param hotelId
     * @return
     * @author chenjiajie 2008-11-24 Version 2.5
     */
    public List<ExMapping> findHotelMapping(long hotelId);

    /**
     * 查询酒店映射
     * 
     * @param hotelId
     * @param type
     * @return
     * @author chenjiajie 2008-11-24 Version 2.5
     */
    public List<ExMapping> findHotelMapping(long hotelId, long[] type);

    /**
     * 查询某酒店的所有价格类型
     * 
     * @param hotelId
     * @return
     * @author chenjiajie 2008-11-27 Version 2.5
     */
    public List findPriceTypeLis(long hotelId);

    /**
     * 根据酒店ID取酒店扩展表信息 add by shizhongwen 时间:Feb 5, 2009 4:59:43 PM
     * 
     * @param hotelId
     * @return
     */
    public List<HtlHotelExt> findHotelExt(Long hotelId);

    /**
     * hotel2.5.0 删除房型的同时，删除合作方的房型信息 add by guojun 时间: 2009-02-20 16:14
     * 
     * @param hotelId
     */
    public void deleteAllMappingRoomType(long roomTypeId);

    /**
     * hotel2.5.0 更新集团酒店 add by guojun
     */
    public void updateAllMappingGroupCode(Long hotelId, Long groupCode, Long channel);

    /**
     * 根据酒店id，房型id，价格id，支付方式，日期查询价格列表 * add by shizhongwen 时间:Mar 8, 2009 4:52:27 PM
     * 
     * @param hotelId
     * @param roomTypeId
     * @param childRoomTypeId
     * @param payMethod
     * @param checkBeginDate
     * @param checkEndDate
     * @return
     */
    public List<HtlPrice> queryHtlPrice(long hotelId, long roomTypeId, long childRoomTypeId,
        String payMethod, String checkBeginDate, String checkEndDate);

    /**
     * add by zhineng.zhuang hotel2.6 2009-02-16 查询酒店的所有预订条款
     * 
     * @param hotelId
     * @param beginDate
     * @param endDate
     * @param roomType
     * @return
     */
    public List<HtlPreconcertItem> queryReservationForCC(long hotelId, Date beginDate,
        Date endDate, String roomType);

    /**
     * 根据酒店ID取出所有的房型价格类型用来标注显示与不显示
     * 
     * @param hotelId
     * @return
     */
    public List<HtlCtlDsply> queryHtlCtlDsply(long hotelId);

    /**
     * 根据房型ID查房型 add by shizhongwen 时间:Apr 17, 2009 4:01:34 PM
     * 
     * @param roomTypeId
     * @return
     */
    public HtlRoomtype findHtlRoomtype(long roomTypeId);

    /**
     * 根据子房型Id查子房型信息 add by shizhongwen 时间:Apr 18, 2009 4:42:48 PM
     * 
     * @param roomChildRoomId
     * @return
     */
    public HtlPriceType findHtlPriceType(Long roomChildRoomId);

    /**
     * 根据酒店ID取出所有设置的加幅记录 add by wuyun 2009-04-18 V2.8
     */
    public List<HtlIncreasePrice> queryHtlIncreasePrice();

    /**
     * 保存更新加幅记录
     */
    public boolean saveOrUpdateHtlIncreasePrice(List<HtlIncreasePrice> increaseList);

    /**
     * 得到加幅记录，写入MAP
     */
    public Map getIncreasePriceMap();

    /**
     * 根据酒店id删除此酒店与香港的所有映射 add by shizhongwen 时间:Apr 21, 2009 4:49:35 PM
     * 
     * @param hotelid
     */
    public void deleteALLHotelMapping(long hotelid) throws Exception;

    /**
     * 根据香港酒店Id找出相应的映射 add by shizhongwen 时间:Apr 22, 2009 5:32:34 PM
     * 
     * @param hkhotelId
     * @return
     */
    public List<ExMapping> findHotelMappingbyHKHotelId(String hkhotelId);

    /**
     * 此香港酒店hkhotelId 是否已映射 add by shizhongwen 时间:Apr 22, 2009 5:36:03 PM
     * 
     * @param hkhotelId
     * @return
     */
    public boolean isHKHotelMapping(String hkhotelId);

    /**
     * 调用存储过程，完成所有的显示控制房型的记录写入 add by shizhongwen 时间:May 6, 2009 2:49:53 PM
     * 
     * @param hotelId
     */
    public void InsertHtlCtlDsply(long hotelId);

    public List<HtlHotel> queryHtlHotelList(String gisids);

    /**
     * 根据酒店ID和渠道取出该酒店的加幅记录 add by chenjiajie 2009-08-03
     * 
     * @param hotelId
     * @param channelType
     * @return
     */
    public List<HtlIncreasePrice> queryHtlIncreasePrice(Long hotelId, String channelType);

    /**
     * 保存更新加幅记录 add by chenjiajie 2009-08-03
     * 
     * @param increaseList
     * @param hotelId
     * @param channelType
     * @return
     */
    public boolean saveOrUpdateHtlIncreasePrice(List<HtlIncreasePrice> increaseList, Long hotelId,
        String channelType);

    /**
     * 删除酒店的加幅记录 add by chenjiajie 2009-08-03
     * 
     * @param hotelIdStr
     */
    public void deleteHtlIncreasePriceBatch(String hotelIdStr);

    /**
     * 批量查询酒店的基本信息 add by chenjiajie 2009-08-03
     * 
     * @param hotelIds
     *            酒店id 用,分隔
     * @return
     */
    public List queryHotelInfoBatch(String hotelIds);

    /**
     * 拼装酒店地址 hotel2.9.2 add by chenjiajie 2009-08-18
     * 
     * @param hotelObj
     * @return
     */
    public String joinHotelAddress(HotelAddressInfo hotelAddressInfo);
    
    /**
     * 根据相应条件，查询对应的配额预警房态信息
     * 
     * add by shengwei.zuo  2009-10-21
     * @param hotelId
     * @param beginDate
     * @param endDate
     * @param week
     * @param roomTypes
     * @param queryCCSetRoomState 
     * @return
     */
    
    public List qryRoomForQuota(long hotelId, String [] week,List roomTypes,List quotaDateList, Object queryCCSetRoomState);
    
    /**
     * 根据输入的参数，查询改床型，是否有预警
     * @param hotelId
     * @param roomtypeId
     * @param bedId
     * @param ableSaleDate
     * @return
     */
    
    public List isShowBedType(Long hotelId,Long roomtypeId,Long bedId,Date ableSaleDate);
    /**
     * 为此酒店设置ebooking超级用户
     * @param htlHotel
     * @param name
     * @param pass
     * @param pass 
     * @param name 
     */
	public void setEbookingSupderUser(HtlHotel htlHotel, String operId, String operName, String name, String pass);
	
	/**
	 * 根据渠道查询所有的酒店或房型
	 * add by yong.zeng 2009-12-24
	 * @param type 1/2/3 酒店/房型/价格类型
	 * @param channelType 渠道编码
	 * @return
	 */
    public List getHotelMapping(long type,long channelType);
    
    /**
     * 将酒店列表和房型列表进行整合成基本酒店列表
     * add by yong.zeng 2009-12-24
     * @param hotelList
     * @param roomList
     * @return
     */
    public List<HtlHotelBase> getBaseHotelListForExMapping(List<ExMapping> hotelList,List<ExMapping> roomList);
    /**
     * 根据hotelid从房型列表获取该酒店的所有房型
     * add by yong.zeng 2009-12-24
     * @param hotelID
     * @param roomList
     * @return
     */
    public List<ExMapping> getBaseRoomListForExMapping(Long hotelID,List<ExMapping> roomList);
    
    /**
     * 整合酒店所有基本房型信息
     * @param hotelList  Mango的酒店列表
     * @param hotelList_C 渠道返回的酒店列表
     */
    public List<HtlHotelBase> mergerHotelRoomMC(List<HtlHotelBase> hotelList);
    public List getEBookingUsersByHotelId(String hotelId);
	public boolean deleteEBookingUserbyId(Long uid);
	
	  /**
     * 根据渠道，酒店ID，房型ID， 查询相应的Exampping 
     * @param channelType
     * @param hotelid
     * @param roomtypecode
     * @param type
     * @return
     */
    public List<ExMapping> getHotelRoomMapping(long channelType,long hotelid,String roomtypeid,long type) ;
    
    /**
     * 查询channel mapping 里面的酒店、房型编码信息
     * @param roomMappingType
     * @param channelType
     * @return
     */
    public List<HtlChannelMapInfo> queryHtlChannelMapInfoList(Long roomMappingType , int channelType);
    
    /**
     * 把htlChannelMapInfoList结构组装成hotelBaseLst结构，然后合并
     * @param hotelBaseLst
     * @param htlChannelMapInfoList
     * @return
     */
    public List<HtlHotelBase> addHtlChannelMapInfoListToHotelBaseLst(List<HtlHotelBase> hotelBaseLst,List<HtlChannelMapInfo> htlChannelMapInfoList);
    /**
     * 查找所有可用的酒店
     * @return
     */
	public List<HtlHotel> findActiveHotel();
	/**
	 * 根据城市代码查找可用的酒店
	 * @param cityCode
	 * @return
	 */
	public List<HtlHotel> findActiveHotelCityCode(String cityCode);
    /**
     * 查询apache无图片而artifactorty上有图的酒店
     * @return
     */
    public List<HtlHotel> queryNoPicHotels();
    /**
     * 根据酒店id查询相册封面
     * @param htlHotel
     * @return
     */
    public List<HtlCoverPicture> queryCoverPictures(HtlHotel htlHotel);
    /**
     * 根据图片id查询图片路径
     * @param htlCoverPicture
     * @return
     */
    public List<HtlPictureUrl> queryPictureUrls(HtlCoverPicture htlCoverPicture);
    /**
     * 查询所有公共会籍
     * @return
     */
    public List<OrParam> queryCommonMemberCd();
    
    /**
     * 删除艺龙酒店映射，转成自签酒店，修改htl_hotel_ext的channel
     * @param hotelId
     */
	public void elongToTradition(long hotelId);
	/**
	 * 删此酒店所有价格、房态、配额；价格类型；房型；
	 */
	public void traditionToElong(long hotelId,String hotelcodeforchannel,String hotelName);
}

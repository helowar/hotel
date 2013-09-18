package com.mangocity.hotel.base.manage;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.persistence.ContractFile;
import com.mangocity.hotel.base.persistence.HtlAddBedPrice;
import com.mangocity.hotel.base.persistence.HtlAlerttypeInfo;
import com.mangocity.hotel.base.persistence.HtlChargeBreakfast;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlCreditAssure;
import com.mangocity.hotel.base.persistence.HtlFavourableclause;
import com.mangocity.hotel.base.persistence.HtlInternet;
import com.mangocity.hotel.base.persistence.HtlPresale;
import com.mangocity.hotel.base.persistence.HtlQuotabatch;
import com.mangocity.hotel.base.persistence.HtlSalesPromo;
import com.mangocity.hotel.base.persistence.HtlWelcomePrice;
import com.mangocity.util.bean.ContinueDatecomponent;

/**
 */
public interface ContractManage {
    /**
     * 查询合同信息
     * 
     * @param contQryCond
     * @return List (多个合同列表 HtlContract)
     */
    //public List searchContract(ContractQueryCondition contQryCond);

    /**
     * 新建一个合同
     * 
     * @param contract
     *            没有id的合同对象
     * @return 返回合同id
     */
    public Long createContract(HtlContract contract);

    /**
     * 修改一个合同
     * 
     * @param contract
     *            有id的合同对象
     * @return 返回一个整数，如果为1则表示修改成功，否则表示失败
     */
    public int modifyContract(HtlContract contract);

    /**
     * 通过一个id找回合同信息
     * 
     * @param contract_id
     * @return 合同信息
     */
    public HtlContract loadContract(Long contract_id);

    /**
     * 创建一个信用卡担保信息
     * 
     * @param creditAssure
     * @return 记录的id
     */

    public Long createCreditAssure(HtlCreditAssure creditAssure) throws IllegalAccessException,
        InvocationTargetException;

    /**
     * 修改一个信用卡担保信息
     * 
     * @param creditAssure
     * @return 返回一个整数，如果为1则表示修改成功，否则表示失败
     */
    public int modifyCreditAssure(HtlCreditAssure creditAssure) throws IllegalAccessException,
        InvocationTargetException;

    /**
     * 新建一个促销信息
     * 
     * @param salesPromo
     *            =促销信息
     * @return 返回促销信息的id;
     */
    public Long createSalesPromotion(HtlSalesPromo salesPromo);

    /**
     * 修改一个促销信息
     * 
     * @param salesPromo
     * @return 返回一个整数，如果为1则表示修改成功，否则表示失败
     */
    public int modifySalesPromotion(HtlSalesPromo salesPromo);

    /**
     * 新建一个收费早餐情况，
     * 
     * @param chargeBreakfast
     * @return 返回收费早餐的id;
     */
    public Long createChargeBreakfast(HtlChargeBreakfast chargeBreakfast)
        throws IllegalAccessException, InvocationTargetException;

    /**
     * 修改一个收费早餐情况，
     * 
     * @param chargeBreakfast
     * @return 返回一个整数，如果为1则表示修改成功，否则表示失败
     */
    public int modifyChargeBreakfast(HtlChargeBreakfast chargeBreakfast)
        throws IllegalAccessException, InvocationTargetException;

    /**
     * 新建一个加床价格信息
     * 
     * @param addBedPrice
     * @return 返回加床价格信息id
     */
    public Long createAddBedPrice(HtlAddBedPrice addBedPrice) throws IllegalAccessException,
        InvocationTargetException;

    /**
     * 修改一个加床价格信息
     * 
     * @param addBedPrice
     * @return 返回一个整数，如果为1则表示修改成功，否则表示失败
     */
    public int modifyAddBedPrice(HtlAddBedPrice addBedPrice) throws IllegalAccessException,
        InvocationTargetException;

    /**
     * 找回一个采购批次
     * 
     * @param quotaBatcId
     *            采购批次id
     * @return 采购批次
     */
    public HtlQuotabatch findByQuotabatchId(long quotaBatcId);

    /**
     * 通过合同号列出当前合同的所有收费早餐情况信息。
     * 
     * @param contractId
     *            合同id
     * @return HtlChargeBreakfast 的list
     */
    public List lstChargeBreakfastByContractId(long contractId);


    /**
     * 延长合同到endDate, copy价格从某个时间段拷贝到另一个时间段
     * 
     * @param contractId
     *            合同ID
     * @param endDate
     *            新的合同终止日期
     * @param lstDates
     *            ContractContinue对象列表。记录一些时间段
     * @throws SQLException 
     */
    public void continueContract(Long contractId, long hotelId, List lstDates, boolean isPeriod,
        Object user) throws SQLException;

    /**
     * 缩短合同
     * 
     * @param cutDate
     *            缩短合同至
     */
    public void cutContractDate(Long contractId, Date cutDate);

    /**
     * 检查合同的起止时间是否重复
     * 
     * @param contract
     * @return 如果重复就返回1，否则就返回0
     */
    public int checkContractDate(HtlContract contract);

    /**
     * 检查合同的起止时间是否重复
     * 
     * @param 酒店id
     *            ,起止时间
     * @return 如果重复就返回1，否则就返回0
     */
    public int checkContractDate(long hotelid, String beginDate, String endDate);

    /**
     * 检查区域和城市信息是否存在
     * 
     * @param
     * @return 如果存在就返回1，否则就返回0
     */
    public int checkAreaExist(String areaCode, String cityCode);

    /**
     * 修改合同信息时检查合同的起止时间是否重复
     * 
     * @param 酒店id
     *            ,起止时间，修改前的起始时间
     * @return 如果重复就返回1，否则就返回0
     */
    public int checkEditContractDate(long hotelid, String beginDate, String endDate,
        String beginDateOld, String endDateOld);

    // 查询合同信息列表
    public List queryContractFileList(ContractFile contractFile);

    // 查询合同信息
    public ContractFile queryContractFileById(ContractFile contractFile);

    // 增加合同信息
    public boolean addContractFile(ContractFile contractFile);

    // 修改合同信息
    public boolean modifyContractFile(ContractFile contractFile);

    // 删除合同信息
    public boolean deleteContractFile(ContractFile contractFile);

    /**
     * 根据下订单日期查找合同
     * 
     * @param 酒店id
     *            ,起止时间
     * @return 如果重复就返回1，否则就返回0
     */
    public HtlContract checkContractDateNew(long hotelid, Date beginDate);

    /**
     * 检查同一酒店房型是否重复
     * 
     * @param 酒店id
     * @return 如果重复就返回1，否则就返回0
     */
    public int checkRoomType(long hotelid, long roomTypeID, String roomName);

    /**
     * 根据合同ID查询所有的配额明细
     */
    public int updateCurrencyPattern(Long hotelId, Long contractId, String currency,
        String quotaPattern, Date beginDate, Date endDate);

    /**
     * 仅仅延长合同
     */
    public boolean justContinueContract(long hotelId, long contractId, String oldBeginDate,
        String continueDate);

    /**
     * 检验价格是否已经存在
     */
    public List<ContinueDatecomponent> checkContinuePrice(long hotelId, long contractId,
        List<ContinueDatecomponent> dateComponents);

    /**
     * 检查房型是否有关房历史 add by zhineng.zhuang 2008-10-30
     * 
     * @param hotelId
     * @param childRoomTypeId
     * @param beginDate
     * @param EndDate
     * @return
     */
    public List checkCloseHistory(long hotelId, String childRoomTypeId,
        List<ContinueDatecomponent> dateComponents);

    /**
     * 检查开房是否会导致bug add by huizhong.chen 2008-9-20
     * 
     * @param hotelId
     * @param childRoomTypeId
     * @param ContinueDatecomponent
     * @return
     */
    public boolean checkCloseHistoryBySameReason(long hotelId, String childRoomTypeId,
        List<ContinueDatecomponent> dateComponents);
    
    /**
     * 按酒店id查询当前所在的合同实体
     * 
     * @param hotelId
     * @return
     */
    public HtlContract queryCurrentContractByHotelId(long hotelId);

    /**
     * 
     * 查询某合同的加早信息 hotel 2.9.2 add by chenjiajie 2009-07-23
     * 
     * @param contractId
     * @param checkInDate
     * @param checkOutDate
     * @return
     */
    public List<HtlChargeBreakfast> queryBreakfast(long contractId, Date checkInDate,
        Date checkOutDate);

    /**
     * 
     * 查询某合同下某房型的加床信息 hotel 2.9.2 add by chenjiajie 2009-07-23
     * 
     * @param contractId
     * @param roomType
     * @param checkInDate
     * @param checkOutDate
     * @return
     */
    public List<HtlAddBedPrice> queryAddBed(long contractId, long roomType, Date checkInDate,
        Date checkOutDate,String payMethod);

    /**
     * 
     * 查询某合同下的接送价信息 hotel 2.9.2 add by chenjiajie 2009-07-23
     * 
     * @param contractId
     * @param checkInDate
     * @param checkOutDate
     * @return
     */
    public List<HtlWelcomePrice> queryWelcomePrice(long contractId, Date checkInDate,
        Date checkOutDate);

    /**
     * 
     * 查询某合同下的房型的免费宽带信息 hotel 2.9.2 add by chenjiajie 2009-07-23
     * 
     * @param contractId
     * @param roomType
     * @param checkInDate
     * @param checkOutDate
     * @return
     */
    public List<HtlInternet> queryInternet(long contractId, long roomType, Date checkInDate,
        Date checkOutDate);

    /**
     * 新增一个提示信息 hotel 2.9.2 add by shengwei.zuo 2009-08-07
     * 
     * @param htlAlerttypeInfo
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public Long createAlerttypeInfo(HtlAlerttypeInfo htlAlerttypeInfo)
        throws IllegalAccessException, InvocationTargetException;

    /**
     * 修改一个提示信息
     * 
     * @param creditAssure
     * @return 返回一个整数，如果为1则表示修改成功，否则表示失败
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public int modifyAlerttypeInfo(HtlAlerttypeInfo htlAlerttypeInfo)
        throws IllegalAccessException, InvocationTargetException;

    /**
     * 根据提示信息ID，查询对应的提示信息内容
     * 
     * @param id
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public HtlAlerttypeInfo queryAlerttypeInfoById(long id) throws IllegalAccessException,
        InvocationTargetException;

    /**
     * 根据提示信息ID，删除对应的提示信息内容
     * 
     * @param id
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public long deleteAlerttypeInfoById(HtlAlerttypeInfo alerttypeInfo)
        throws IllegalAccessException, InvocationTargetException;

    /**
     * 根据价格类型id，获得价格类型的名称 add by shengwei.zuo 2009-08-07
     * 
     * @param priceTypeID
     * @return
     */
    public String priceTypeName(long priceTypeID);

    /**
     * 根据合同ID查询出该酒店下的提示信息 add by shengwei.zuo 2009-08-16
     * 
     */
    public List queryAlertInfoByConId(long contractId);
    
    /**
     * 根据合同ID查询出该酒店下的提示信息,是否需要封装 add by xiaoyong.li 2009-12-22
     * 
     */
    public List queryAlertInfoByConId(long contractId,boolean assemble);
    
	
	/**
	 * hotel 2.9.3
	 * 
	 * 新增一个优惠条款
	 * @param favourableclause
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public Long createFavourableclause(HtlFavourableclause favourableclause) throws IllegalAccessException, InvocationTargetException;
	
	/**
	 * hotel 2.9.3
	 * 
	 * 根据合同ID查询出该酒店下的优惠条款
	 * @param contractId
	 * @return
	 */
	public List queryAllavourableclause(long contractId);
	
	/**
	 * hotel 2.9.3
	 * 
	 * 删除优惠条款 add by shengwei.zuo 2009-08-31
	 * @param favourableclause
	 * @return
	 */
	public long deleteFavClauseObj(HtlFavourableclause favourableclause);
	
	/**
	 * hotel 2.9.3
	 * 
	 * 根据ID查询对应的优惠条款ID
	 * 
	 */
	public HtlFavourableclause getFavClauseById(long id);
	
	/**
	 * hotel 2.9.3
	 * 
	 * 修改一个优惠条款的信息 add by shengwei.zuo 2009-08-31
	 * @param favourableclause
	 * @return
	 */
	public long modifyFavClause(HtlFavourableclause favourableclause) throws IllegalAccessException, InvocationTargetException;
	
	/**
	 * 删除提示信息
	 * @param idArray
	 * @param className 
	 */
	public void deleteAlertTypeInfoByIds(String[] idArray, String className);

	public List<HtlAlerttypeInfo> queryAlerttTypeInfoByIds(String ids);
	
	/**
	 * 根据酒店ID，开始日期和结束日期获取合同
	 * 
	 * @param hotelId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public HtlContract getHtlContractByHtlIdDateRange(long hotelId, Date beginDate, Date endDate);
	
	/**
	 * 
	 * refactor: 根据"lstPreOrderSalePromosRoom" sql语句查询酒店促销信息
	 * 
	 * @param params
	 * @return
	 */
	public List<HtlSalesPromo> lstPreOrderSalePromosRoom(Object[] params);
	
	/**
	 * 
	 * refactor: 根据"lstPreOrderPresale" sql语句查询芒果促销信息
	 * 
	 * @param params
	 * @return
	 */
	public List<HtlPresale> lstPreOrderPresale(Object[] params);
	
	/**
	 * 
	 * refactor: 根据hotelId和priceTypeId查询芒果优惠信息
	 * 
	 * @param hotelId
	 * @param priceTypeId
	 * @return
	 */
	public List<HtlFavourableclause> queryFavourableclauseByHotelAndPriceType(
			Long hotelId, Long priceTypeId);
}

package com.mango.hotel.ebooking.service;

import java.util.ArrayList;
import java.util.List;

import com.mango.hotel.ebooking.persistence.HtlEbookingPriceRedressal;
import com.mango.hotel.ebooking.persistence.LeavewordAnnalBean;
import com.mangocity.hotel.user.UserWrapper;

/**
 * EBooking调价审核操作业务接口
 * 
 * @author chenjiajie
 * 
 */
public interface IPriceRedressalService {
	
	public List<HtlEbookingPriceRedressal> EMPEY_LIST = new ArrayList<HtlEbookingPriceRedressal>(0);

    /**
     * 按主键查询
     * 
     * @param id  主键ID
     * @return
     * @throws Exception
     */
    public HtlEbookingPriceRedressal queryPriceRedressalById(long id);

    /**
     * 更新调价记录
     * 
     * @param priceRedressalBean 调价记录实体类
     * @param leavewordAnnalBean
     * @param user
     * @throws Exception
     */
    public void updatePriceRedressalForSingle(HtlEbookingPriceRedressal priceRedressalBean,LeavewordAnnalBean leavewordAnnalBean, UserWrapper user) throws Exception;

    /**
     * 按酒店id和调价主题ID查询
     * 
     * @param hotelId 酒店ID
     * @param priceRedressalNameId 调价ID
     * @return
     * @throws Exception
     */
    public List<HtlEbookingPriceRedressal> queryByHotelIdAndPriceThemeId(Long hotelId,
        Long priceRedressalNameId) throws Exception;

    /**
     * 批量更新调价记录的状态后重新查询最新数据
     * 
     * @param priceRedressalBeanList 调价记录集合
     * @param hotelId 酒店ID
     * @param priceRedressalNameId 调价ID
     * @return
     * @throws Exception
     */
    public List<HtlEbookingPriceRedressal> updatePriceRedressal(
        List<HtlEbookingPriceRedressal> priceRedressalBeanList, Long hotelId,
        Long priceRedressalNameId) throws Exception;

    /**
     * 批量更新调价记录的状态后重新查询最新数据
     * 
     * @param srcState 原有状态
     * @param hotelId  酒店ID
     * @param priceRedressalNameId 调价ID
     * @return
     * @throws Exception
     */
    public List<HtlEbookingPriceRedressal> updatePriceRedressal(long srcState, Long hotelId,
        Long priceRedressalNameId) throws Exception;

    /**
     * 批量更新调价申请的状态(否决用)
     * 
     * @param priceRedressalBantchID
     * @param user
     * @param leavewordAnnalBean
     * @throws Exception
     */
    public void updatePriceRedressalForReject(String priceRedressalBantchID, UserWrapper user,
        LeavewordAnnalBean leavewordAnnalBean) throws Exception;

    /**
     * 查询没有审核的调价记录
     * @param theAreaLoginerCanCheck 酒店城市三字码
     * @return
     */
	public int getUnAuditPrice(String theAreaLoginerCanCheck);
}

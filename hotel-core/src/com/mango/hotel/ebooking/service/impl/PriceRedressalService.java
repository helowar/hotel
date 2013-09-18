package com.mango.hotel.ebooking.service.impl;

import java.util.List;

import com.mango.hotel.ebooking.constants.RequisitionState;
import com.mango.hotel.ebooking.dao.PriceRedressalDao;
import com.mango.hotel.ebooking.dao.LeavewordAnnalAndRightDao;
import com.mango.hotel.ebooking.persistence.HtlEbookingPriceRedressal;
import com.mango.hotel.ebooking.persistence.LeavewordAnnalBean;
import com.mango.hotel.ebooking.service.IPriceRedressalService;
import com.mangocity.hotel.user.UserWrapper;

/**
 * EBooking调价审核操作业务实现
 * 
 * @author chenjiajie
 * 
 */
public class PriceRedressalService implements IPriceRedressalService {

    /**
     * EBooking调价审核操作DAO
     */
    private PriceRedressalDao priceRedressalDao;

    /**
     * EBooking留言板和区域权限操作DAO
     */
    private LeavewordAnnalAndRightDao leaAndRightDao;

    /**
     * 按主键查询
     */
    public HtlEbookingPriceRedressal queryPriceRedressalById(long id) {
        return priceRedressalDao.queryPriceRedressalById(id);
    }

    /**
     * 按酒店id和调价主题ID查询
     */
    public List<HtlEbookingPriceRedressal> queryByHotelIdAndPriceThemeId(Long hotelId,
        Long priceRedressalNameId) throws Exception {
        return priceRedressalDao.queryByHotelIdAndPriceThemeId(hotelId, priceRedressalNameId);
    }

    /**
     * 更新调价记录
     */
    public void updatePriceRedressalForSingle(HtlEbookingPriceRedressal priceRedressalBean,
        LeavewordAnnalBean leavewordAnnalBean, UserWrapper user) throws Exception {
    	// 保存E-Booking价格表
        priceRedressalDao.updatePriceRedressal(priceRedressalBean);
    }


    /**
     * 批量更新调价记录的状态后重新查询最新数据
     */
    public List<HtlEbookingPriceRedressal> updatePriceRedressal(
        final List<HtlEbookingPriceRedressal> priceRedressalBeanList, final Long hotelId,
        final Long priceRedressalNameId) throws Exception {
            priceRedressalDao.saveOrUpdateAllRedressals(priceRedressalBeanList);
            return priceRedressalDao.queryByHotelIdAndPriceThemeId(hotelId,priceRedressalNameId);
    }

    /**
     * 批量更新调价申请的状态(否决用)
     */
    public void updatePriceRedressalForReject(String priceRedressalBantchID, UserWrapper user,
        LeavewordAnnalBean leavewordAnnalBean) throws Exception {
        priceRedressalDao.updatePriceRedressal(priceRedressalBantchID, RequisitionState.REJECT,
            user);
        leaAndRightDao.save(leavewordAnnalBean);
    }

    /**
     * 批量更新调价记录的状态后重新查询最新数据
     */
    public List<HtlEbookingPriceRedressal> updatePriceRedressal(long srcState, Long hotelId,
        Long priceRedressalNameId) throws Exception {
            long tarState = 0L;
            // 原待审核的，要更新为审核中
            if (srcState == RequisitionState.UNAUDITED) {
                tarState = RequisitionState.AUDITING;
                priceRedressalDao.updatePriceRedressal(srcState, tarState, hotelId,
                    priceRedressalNameId);
                // 重新查询最新的结果并返回
                return priceRedressalDao.queryByHotelIdAndPriceThemeId(hotelId,
                    priceRedressalNameId);
            }else if (srcState == RequisitionState.AUDITING) {// 原审核中，要更新为待审核
                tarState = RequisitionState.UNAUDITED;
                priceRedressalDao.updatePriceRedressal(srcState, tarState, hotelId,
                    priceRedressalNameId);
                return EMPEY_LIST;
            }
        return EMPEY_LIST;
    }
    
   /**
    * 查询没有审核的调价记录
    */
    public int getUnAuditPrice(String theAreaLoginerCanCheck) {
		return priceRedressalDao.getUnAuditPrice(theAreaLoginerCanCheck);
	}

    /**setter methods */
    public void setPriceRedressalDao(PriceRedressalDao priceRedressalDao) {
        this.priceRedressalDao = priceRedressalDao;
    }

	public void setLeaAndRightDao(LeavewordAnnalAndRightDao leaAndRightDao) {
		this.leaAndRightDao = leaAndRightDao;
	}
}

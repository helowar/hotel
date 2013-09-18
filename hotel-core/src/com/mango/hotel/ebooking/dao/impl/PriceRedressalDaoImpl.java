package com.mango.hotel.ebooking.dao.impl;

import java.util.Date;
import java.util.List;

import com.mango.hotel.ebooking.dao.PriceRedressalDao;
import com.mango.hotel.ebooking.persistence.HtlEbookingPriceRedressal;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

/**
 * EBooking调价审核操作DAO
 * 
 * @author chenjiajie
 * @param <T>
 * 
 */
public class PriceRedressalDaoImpl extends GenericDAOHibernateImpl implements PriceRedressalDao {

	/**
	 * 按主键查询
	 * @param id  主键ID
	 * @return
	 * @throws Exception
	 */
    public HtlEbookingPriceRedressal queryPriceRedressalById(long id) {
        return (HtlEbookingPriceRedressal) get(HtlEbookingPriceRedressal.class, id);
    }

    /**
	 * 更新调价审核记录
	 * @param priceRedressalBean 调价记录实体类对象
	 * @throws Exception
	 */
    public void updatePriceRedressal(HtlEbookingPriceRedressal priceRedressalBean) 
    throws Exception {
        super.saveOrUpdate(priceRedressalBean);
    }

    /**
	 * 根据酒店ID和调价ID找出同一时间提交的审批查询
	 * @param hotelId  酒店ID
	 * @param priceRedressalNameId 调价ID
	 * @return
	 * @throws Exception
	 */
    public List<HtlEbookingPriceRedressal> queryByHotelIdAndPriceThemeId(Long hotelId, Long priceRedressalNameId)
        throws Exception {
            String hql = " from HtlEbookingPriceRedressal where "
                + " hotelid = ? and "
                + " operationdate = (select operationdate from HtlEbookingPriceRedressal where priceRedressalID=?) "
                + " order by requisitionState,operationdate";//按提交时间先后排
            return super.query(hql, new Object[] { String.valueOf(hotelId), priceRedressalNameId });
    }

    /**
	 * 批量更新调价申请的状态
	 * @param priceRedressalBantchID 调价记录的ID字符串
	 * @param requisitionState 状态
	 * @param user  操作者实体类对象
	 * @throws Exception
	 */
    public void updatePriceRedressal(String priceRedressalBantchID, long requisitionState,
        UserWrapper user) throws Exception {
            String hql = " update HtlEbookingPriceRedressal ";
            hql += " set requisitionState = ? , ";
            hql += " auditingName = ?, ";
            hql += " auditingID = ?, ";
            hql += " auditingDate = ? ";
            hql += " where priceRedressalID in (" + priceRedressalBantchID + ") ";
            String userName = null != user ? user.getName() : "";
            String userLoginName = null != user ? user.getLoginName() : "";
            super.updateByQL(hql, new Object[] { requisitionState, userName, userLoginName,
                new Date() });
    }

    /**
	 * 锁定待审核记录，以及解锁待审核记录
	 * @param srcState 原有状态
	 * @param tarState 更新状态
	 * @param hotelId  酒店ID
	 * @param priceRedressalNameId 调价ID
	 * @throws Exception
	 */
    public void updatePriceRedressal(long srcState, long tarState, Long hotelId,
        Long priceRedressalNameId) throws Exception {
            String hql = " update HtlEbookingPriceRedressal "
                + " set requisitionState = ? "
                + " where hotelid = ? and "
            	+ " operationdate = (select operationdate from HtlEbookingPriceRedressal where priceRedressalID=?) and"
            	+ " requisitionState = ? ";
            super.updateByQL(hql, new Object[] { tarState, String.valueOf(hotelId),
                priceRedressalNameId, srcState });
    }

    /**
	 * 查询匹配id的调价记录
	 * @param priceRedressalBantchID 调价ID字符串
	 * @return 
	 * @throws Exception
	 */
    public List<HtlEbookingPriceRedressal> queryPriceRedressal(String priceRedressalBantchID)
        throws Exception {
            String hql = " from HtlEbookingPriceRedressal where priceRedressalID in ("
                + priceRedressalBantchID + ") ";
            return super.query(hql, null);
    }

	/**
	 * 查询没有审核的调价记录数
	 * @param theAreaLoginerCanCheck 酒店区域城市三字码
	 * @return
	 */
	public int getUnAuditPrice(String theAreaLoginerCanCheck) {
		String hql = "select count(*) from HtlEbookingPriceRedressal r, HtlHotel h "
			+ "where r.hotelid = h.ID and r.requisitionState = 0 ";
		if (!"ALL".equals(theAreaLoginerCanCheck)) {
			hql += " and h.city in (select hr.cityCode from HtlArea hr where hr.areaCode = '"
				+ theAreaLoginerCanCheck + "')";
		}
		
		List<Long> result = query(hql, null);
		
		return result.isEmpty()?0:result.get(0).intValue();
	}

	/**
	 * 
	 * @param hql HQL查询语句
	 * @param paramValues  参数数组
	 * @param startIndex  起始索引位置
	 * @param maxResults 要查询的调价记录实体数量
	 * @param cacheable 是否从缓存中查询
	 * @return
	 */
	public List<HtlEbookingPriceRedressal> queryByHql(String hql,
			Object[] paramValues, int startIndex, int maxResults,
			boolean cacheable) {
		return query(hql, paramValues, startIndex, maxResults, cacheable);
	}

	/**
	 * 批量更新调价实体集合
	 * @param redressals
	 */
	public void saveOrUpdateAllRedressals(
			List<HtlEbookingPriceRedressal> redressals) {
		super.saveOrUpdate(redressals);
	}
}

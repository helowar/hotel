package com.mangocity.hotel.base.dao.impl;

import java.sql.CallableStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.dao.IContractDao;
import com.mangocity.hotel.base.manage.impl.ContractManageImpl;
import com.mangocity.hotel.base.persistence.HtlAddBedPrice;
import com.mangocity.hotel.base.persistence.HtlAlerttypeInfo;
import com.mangocity.hotel.base.persistence.HtlChargeBreakfast;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlCreditAssure;
import com.mangocity.hotel.base.persistence.HtlFavourableclause;
import com.mangocity.hotel.base.persistence.HtlPresale;
import com.mangocity.hotel.base.persistence.HtlQuotabatch;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.persistence.HtlSalesPromo;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.ContractContinue;
import com.mangocity.util.bean.SingleContractContinue;
import com.mangocity.util.dao.GenericDAOHibernateImpl;
import com.mangocity.util.log.MyLog;

/**
 * 合同DAO
 * 
 */
public class ContractDao extends GenericDAOHibernateImpl implements IContractDao {
	
	private static final MyLog log = MyLog.getLogger(ContractManageImpl.class);
    /**
     * 根据酒店ID获取房型
     */
    public List getRoomTypes(Long hotelID) {

        return super.queryByNamedQuery("queryRoomTypes", new Object[] { hotelID });
    }

    /**
     * 根据合同ID获取促销列表
     */
    public List getSalePromos(Long contractID) {

        return super.queryByNamedQuery("querySalePromos", new Object[] { contractID });
    }

    /**
     * 根据合同ID获取奖惩列表
     */
    public List getRewards(Long contractID) {

        return super.queryByNamedQuery("queryRewards", new Object[] { contractID });
    }

    /**
     * 获取酒店合同奖惩信息
     * 
     * @param contractID
     *            合同ID
     * @return HtlBreakfast 实体集合
     */

    public List getBreakfast(Long contractID) {

        return super.queryByNamedQuery("queryBreakfasts", new Object[] { contractID });
    }

    /**
     * 获取酒店合同奖惩信息
     * 
     * @param contractID
     *            合同ID
     * @return HtlAddBedPrice 实体集合
     */

    public List getAddBedPrice(Long contractID) {

        return super.queryByNamedQuery("queryAddBedPrices", new Object[] { contractID });
    }

    /**
     * 获取酒店合同奖惩信息
     * 
     * @param contractID
     *            合同ID
     * @return HtlWelcomePrice 实体集合
     */

    public List getWelcomePrice(Long contractID) {

        return super.queryByNamedQuery("queryWelcomePrices", new Object[] { contractID });
    }

    public HtlContract getContractById(Long contractID) {
        return (HtlContract) super.load(HtlContract.class, contractID);
    }
    
    public List<HtlContract> getContractListByContractId(Long contractId) {
    	String hsql = "from HtlContract where ID =?";
        return super.find(hsql, new Object[]{contractId});
    }

    /**
     * 获取合同预定信息
     * 
     * @param hotelId
     * @param creditDate
     * @return HtlAssureCredit 实体
     */

    public List getCreditAssure(Long hotelId, Date creditDate) {

        return super.queryByNamedQuery("queryCreditAssure", new Object[] { hotelId, creditDate });

    }

    /**
     * 获取价格类型信息
     * 
     * @param roomId
     * 
     * @return HtlPrice 实体
     */

    public List getPriceList(Long roomTypeId) {

        return super.queryByNamedQuery("queryPriceType", new Object[] { roomTypeId });

    }

    /**
     * 获取调价警告信息
     * 
     * @param hotelID
     * 
     * @return HtlChangePriceWarning 实体
     */

    public List getChangePriceWarningList(Long hotelID) {

        return super.queryByNamedQuery("lstChangePriceWarnings", new Object[] { hotelID });

    }

    /**
     * 删除调价警告信息
     * 
     * @param obj
     */
    public void delChangePriceWarning(Object obj) {
        super.remove(obj);
    }

    /**
     * 根据酒店ID获取芒果网促销信息
     * 
     * @param hotelId
     * 
     * @return List
     */

    public List getPresaleList(Long hotelId) {
        return super.queryByNamedQuery("lstPresale", new Object[] { hotelId });
    }

    /**
     * 获取酒店促销信息
     * 
     * @param
     * @return
     */

    public List getPreOrderSalePromos(Long hotelID, Date beginDate, Date endDate) {
        return super.queryByNamedQuery("lstPreOrderSalePromos", new Object[] { hotelID, beginDate,
            endDate, beginDate, endDate, beginDate, endDate });
    }

    /**
     * 获取芒果网促销信息
     * 
     * @param
     * @return
     */

    public List getPreOrderPresaleList(Long hotelID, Date beginDate, Date endDate) {
        return super.queryByNamedQuery("lstPreOrderPresale", new Object[] { hotelID, beginDate,
            endDate, beginDate, endDate, beginDate, endDate });
    }

    public List getTaxCharges(Long contractId) {
        // TODO Auto-generated method stub
        return super.queryByNamedQuery("queryTaxCharges", new Object[] { contractId });
    }

    public List getAddBedPrice(long contractId, String roomTypeId, Date checkInDate,
        Date checkOutDate,String payMethod) {
        return super.queryByNamedQuery("queryAddBedPricesCheckDate", new Object[] { contractId,
            roomTypeId, checkInDate, checkInDate ,payMethod});
    }

    public List getInternetByRoomType(long contractId, long roomTypeId, Date checkInDate,
        Date checkOutDate) {
        return super.queryByNamedQuery("queryInternetCheckDate", new Object[] { contractId,
            roomTypeId, checkInDate, checkInDate });
    }

    public List getBreakfast(long contractId, Date checkInDate, Date checkOutDate) {
        return super.queryByNamedQuery("queryBreakfastsCheckDate", new Object[] { contractId,
            checkInDate, checkInDate });
    }

    public List getWelcomePrice(long contractId, Date checkInDate, Date checkOutDate) {
        return super.queryByNamedQuery("queryWelcomePricesCheckDate", new Object[] { contractId,
            checkInDate, checkInDate });
    }
    
    public List getCreditAssures(HtlCreditAssure creditAssure){
    	return super.queryByNamedQuery("queryCreditAssuresOrder", new Object[] {
				creditAssure.getContractId(), creditAssure.getRoomType() });
    }
    
    public List getSubCreditAssures(Integer contractId,String roomType,Date modifyDate){
    	return super.queryByNamedQuery("querySubCreditAssures", new Object[] {
    			contractId, roomType, modifyDate});
    }
    
    public List getBreakfastsOrder(HtlChargeBreakfast chargeBreakfast){
        return super.queryByNamedQuery("queryBreakfastsOrder",
				new Object[] { chargeBreakfast.getContractId(),
						chargeBreakfast.getPayMethod() });
    }
    
    public List getSubBreakfasts(Long contractId,String payMethod,Date modifyDate){
    	return super.queryByNamedQuery("querySubBreakfasts", new Object[] {
    			contractId, payMethod,modifyDate});
    }
    
    public List getAddBedPricesOrder(HtlAddBedPrice addBedPrice){
    	return super.queryByNamedQuery("queryAddBedPricesOrder", new Object[] {
				addBedPrice.getContractId(), addBedPrice.getRoomType(),
				addBedPrice.getPayMethod() });
    }
    
    public List getSubAddBedPrices(long contractId,String roomType,Date modifyTime,String payMethod){
    	return super.queryByNamedQuery("querySubAddBedPrices", new Object[] {
    			contractId, roomType, modifyTime, payMethod});
    }
    
    public List getSubAddBedPrices(long contractId,String roomType,Date modifyTime){
    	return super.queryByNamedQuery("querySubAddBedPrices", new Object[]{contractId,roomType,modifyTime});
    }
    
    public int callContinueContractStoreProcedure(long hotelId,long contractId,ContractContinue cc,Object user){
        try{
            UserWrapper userWrapper=(UserWrapper)user;
            String procedureName = "{?= call sp_hotel_ImpContractData(?,?,?,?,?,?,?,?,?,?,?,?)} ";
            CallableStatement cstmt = (CallableStatement)super.getCurrentSession().connection().prepareCall(procedureName);
            cstmt.setInt(1,0);
            cstmt.setLong(2,hotelId);
            cstmt.setLong(3, contractId);
            cstmt.setString(4,DateUtil.formatDateToSQLString(cc.getSrcBeginDate()));
            cstmt.setString(5,DateUtil.formatDateToSQLString(cc.getSrcEndDate()));
            cstmt.setString(6,DateUtil.formatDateToSQLString(cc.getTarBeginDate()));
            cstmt.setString(7,DateUtil.formatDateToSQLString(cc.getTarEndDate()));
            cstmt.setString(8,(userWrapper==null || userWrapper.getName()==null)?"":userWrapper.getName());
            cstmt.setString(9,(userWrapper==null || userWrapper.getLoginName()==null)?"":userWrapper.getLoginName());
            cstmt.setLong(10, cc.getRoomTypeId());
            cstmt.setLong(11, cc.getChildRoomTypeId());
            //目标房型 add by wuyun 2009-05-20
            cstmt.setLong(12, cc.getTarRoomTypeId());
            //目标价格类型 add by wuyun 2009-05-20
            cstmt.setLong(13, cc.getTarChildRoomTypeId());
            //价格复制星期 add by chenjiajie 2010-03-03 暂时不适用，逻辑不正确
            //cstmt.setString(14, cc.getWeeks());
            cstmt.executeUpdate();
            return 1;
        }catch(Exception e)
        {           
            log.error(" callContinueContractStoreProcedure error: ContractManageImpl error!" ,e);
            return 0;
        }       
    }
    
    public int callSingleContinueContractStoreProcedure(long hotelId,long contractId,SingleContractContinue cc,Object user){
        try{
            UserWrapper userWrapper=(UserWrapper)user;
            String procedureName = "{?= call SP_HOTEL_SINGLECONTINUE(?,?,?,?,?,?,?,?,?,?,?)} ";
            CallableStatement cstmt = (CallableStatement)super.getCurrentSession().connection().prepareCall(procedureName);
            cstmt.setInt(1,0);
            cstmt.setLong(2,hotelId);
            cstmt.setLong(3, contractId);
            cstmt.setString(4,DateUtil.formatDateToSQLString(cc.getSourceBeginDate()));             
            cstmt.setString(5,DateUtil.formatDateToSQLString(cc.getTargetBeginDate()));
            cstmt.setString(6,DateUtil.formatDateToSQLString(cc.getTargetEndDate()));
            cstmt.setString(7,(userWrapper==null || userWrapper.getName()==null)?"":userWrapper.getName());
            cstmt.setString(8,(userWrapper==null || userWrapper.getLoginName()==null)?"":userWrapper.getLoginName());
            cstmt.setLong(9, cc.getRoomTypeId());
            cstmt.setLong(10, cc.getChildRoomTypeId());
            //目标房型 v2.8.1 add by wuyun 2009-05-20
            cstmt.setLong(11, cc.getTargetRoomTypeId());
            //目标价格类型 v2.8.1 add by wuyun 2009-05-20
            cstmt.setLong(12, cc.getTargetChildRoomTypeId());
            cstmt.executeUpdate();
            return 1;
        }catch(Exception e)
        {           
            log.error(" callSingleContinueContractStoreProcedure error: ContractManageImpl error!" ,e);
            return 0;
        }       
    }
    
    public HtlContract getHtlContract(long hotelid){
		Object obj[] = new Object[]{hotelid};
		//String hsql = "from HtlContract c where c.hotelId =? and  ? between beginDate and endDate";
		//查询最新的一个全合返回
		String hsql = "from HtlContract c where c.hotelId =? order by endDate desc";
		List<HtlContract> contractList = super.find(hsql, obj);
		return contractList.isEmpty()?null : contractList.get(0);
    }
    
    public void updateContract(String currency,Long hotelId,Date beginDate, Date endDate){
		String hsql = "update HtlPrice set CURRENCY = ? WHERE HOTEL_ID = ? AND (ABLE_SALE_DATE between ? and ? ) ";
		Object[] obj = new Object[]{currency,hotelId,beginDate,endDate};
		super.doUpdateBatch(hsql, obj);
    }
    
    public void updateHtlQuota(String quotaPattern,Long contractId){
    	String hsql = "update HtlQuota set QUOTA_PATTERN = ? WHERE CONTRACT_ID = ? ";
    	Object[] obj = new Object[]{quotaPattern,contractId};
		super.doUpdateBatch(hsql, obj);
    }
    
    public HtlContract getHtlContracts(Object[] obj){
		String hql = " from HtlContract where hotelId = ? and beginDate <= trunc(sysdate) and endDate >= trunc(sysdate)";
		List<HtlContract> contractList = super.doquery(hql,obj, false);
		if(!contractList.isEmpty()){
			return contractList.get(0);
		}else{
			return null;
		}
    }
    
    public List getAlerttypeInfoOrder(HtlAlerttypeInfo htlAlerttypeInfo){
    	return super.queryByNamedQuery("queryAlerttypeInfoOrder", new Object[]{htlAlerttypeInfo.getContractId(),htlAlerttypeInfo.getPriceTypeId()});
    }
    
    public List getSubAlerttypeInfo(Long contractId,String priceTypeId,Date modifyDate){
    	return super.queryByNamedQuery("querySubAlerttypeInfo", new Object[]{contractId,priceTypeId,priceTypeId});
    }
    
    public HtlRoomtype findHtlRoomType(long priceTypeID){
		String hql = " select m from HtlRoomtype m, HtlPriceType p where p.roomType = m.ID  and p.ID = ? ";
		List<HtlRoomtype> roomTypeList = super.query(hql, new Object[] {priceTypeID});
		
		return roomTypeList.isEmpty()?null : roomTypeList.get(0);
    }
    
    public HtlAlerttypeInfo findHtlAlerttypeInfo(long id){
//    	String sql = " select  a  from HtlAlerttypeInfo a   where " +
//		 " a.id=? ";
//		Object[] obj = new Object[] {id};
//		HtlAlerttypeInfo alerttypeInfo=(HtlAlerttypeInfo)super.find(sql, obj);
		return super.get(HtlAlerttypeInfo.class, id);
    }
    
    public List getAlertInfosByConId(long contractid){
		String sql = " select  a  from HtlAlerttypeInfo a   where " +
		 " a.contractId=? order by a.modifyTime desc";
		Object[] obj = new Object[] {contractid};
		return (List)super.doquery(sql, obj, false);
    }
    
    public List getFavourableclauseOrder(HtlFavourableclause favourableclause){
    	return super.queryByNamedQuery("queryFavourableclauseOrder",
				new Object[] { favourableclause.getContractId(),
						favourableclause.getPriceTypeId() });
    }
    
    public List getSubFavClause(Long contractId,Long priceTypeId, Date modifyDate){
    	return super.queryByNamedQuery("querySubFavClause", new Object[]{contractId,priceTypeId,modifyDate});
    }
    
    public HtlFavourableclause findHtlFavourableclause(long htlFavourableclauseId){
//    	String sql = " select  a  from HtlFavourableclause a   where " +
//		 " a.id=? ";
//		Object[] obj = new Object[] {htlFavourableclauseId};
//		HtlFavourableclause favClause=(HtlFavourableclause)super.find(sql, obj);
		return super.get(HtlFavourableclause.class, htlFavourableclauseId);
    }
    
    public List getHtlFavourableclauseByContractId(long contractId){
		List lstFavClause = new ArrayList();
		
		String sql = " select  a  from HtlFavourableclause a   where " +
		 " a.contractId=? and a.endDate >= trunc(sysdate)  order by a.modifyTime desc";
		Object[] obj = new Object[] {contractId};
		lstFavClause=(List)super.doquery(sql, obj, false);
		return lstFavClause;
    }
	
    public void saveOrUpdate(HtlContract contract){
    	super.saveOrUpdate(contract);
    }
    
	public HtlContract findHtlContractByID(Class clazz,Long contractId){
    	return (HtlContract)super.load(clazz, contractId);
    }

	public void merge(HtlContract contract) {
		super.saveOrUpdate(contract);
	}

	public void saveOrUpdate(HtlCreditAssure creditAssure) {
		super.saveOrUpdate(creditAssure);
	}

	@SuppressWarnings("unchecked")
	public void remove(Class clazz, Long id) {
		// TODO Auto-generated method stub
		super.remove(clazz, id);
	}

	public void saveOrUpdateAll(List list) {
		super.saveOrUpdateAll(list);
	}

	public void saveOrUpdate(HtlSalesPromo salesPromo) {
		super.saveOrUpdate(salesPromo);
	}
	
	public List getBreakfasts(long contractId){
		List alist = new ArrayList();
		alist = super.queryByNamedQuery("queryBreakfasts", new Object[]{contractId});
		return alist;		
	}

	public HtlQuotabatch findByQuotabatchId(Class clazz, Long htlQuotabatchId) {
		return (HtlQuotabatch)super.load(clazz, htlQuotabatchId);
	}

	public void saveOrUpdate(HtlChargeBreakfast chargeBreakfast) {
		super.saveOrUpdate(chargeBreakfast);
	}

	public void saveHtlAlerttypeInfo(HtlAlerttypeInfo htlAlerttypeInfo) {
		// TODO Auto-generated method stub
		if(htlAlerttypeInfo.getId() != null) {
			super.update(htlAlerttypeInfo);
		}
		super.save(htlAlerttypeInfo);
		
	}

	public void saveOrUpdate(HtlFavourableclause favourableclause) {
		// TODO Auto-generated method stub
		super.saveOrUpdate(favourableclause);
	}

	public void saveAddBedPrice(HtlAddBedPrice addBedPrice) {
		// TODO Auto-generated method stub
		super.save(addBedPrice);
	}
	
	public HtlContract qryHtlContractByHtlIdValidDate(long hotelId, Date beginDate, Date endDate) {
		String hql = " from HtlContract where hotelId = ? and beginDate <= ? and endDate >= ? ";
		List<HtlContract> contractList = super.query(hql, new Object[]{Long.valueOf(hotelId), beginDate, endDate});
		
		return contractList.isEmpty()?null : contractList.get(0);
    }

	/**
	 * 
	 * refactor: 根据"lstPreOrderSalePromosRoom" sql语句查询酒店促销信息
	 * 
	 * @param params
	 * @return
	 */	
	public List<HtlSalesPromo> lstPreOrderSalePromosRoom(Object[] params) {
		return queryByNamedQuery("lstPreOrderSalePromosRoom", params);
	}
	
	/**
	 * 
	 * refactor: 根据"lstPreOrderPresale" sql语句查询芒果促销信息
	 * 
	 * @param params
	 * @return
	 */
	public List<HtlPresale> lstPreOrderPresale(Object[] params) {
		return queryByNamedQuery("lstPreOrderPresale", params);
	}
	
	/**
	 * 
	 * refactor: 根据hotelId和priceTypeId查询芒果优惠信息
	 * 
	 * @param hotelId
	 * @param priceTypeId
	 * @return
	 */
	public List<HtlFavourableclause> queryFavourableclauseByHotelAndPriceType(
			Long hotelId, Long priceTypeId) {
    	String sql = "from HtlFavourableclause where hotelId =? and priceTypeId =?";
		Object[] obj = new Object[] { hotelId, priceTypeId };
		List<HtlFavourableclause> favourableClauseList = super.query(sql, obj);
		return favourableClauseList;
	}

	public void deleteAlertTypeInfoByIds(String[] idArray,String className) {
		super.remove(idArray, className);
	}

	public List<HtlAlerttypeInfo> queryAlerttypeInfoByIds(String ids) {
		StringBuilder hql= new StringBuilder();
		hql.append(" from HtlAlerttypeInfo h where h.id in (");
		
		String[] idArray = ids.split(",");
		List<Long> paramList = new ArrayList<Long>(idArray.length);
		for (String alerttypeInfoId : idArray) {
        	hql.append("?, ");
            paramList.add(Long.valueOf(alerttypeInfoId));
        }
		hql.setLength(hql.length() - 2);
        hql.append(")");
        
		return super.query(hql.toString(), paramList.toArray());
	}	
	
	public HtlContract getContractInfoByHotelId(long hotelId) {
		String sql = " select  a  from HtlContract  a   where " +
		 " a.hotelId=?  and a.endDate >= trunc(sysdate) and a.beginDate <= trunc(sysdate)";
		List<HtlContract> contractList = super.query(sql, new Object[] {hotelId});
		if(contractList != null && contractList.size() >0) {
			return contractList.get(0);
		}
		return null;
	}
	


}


package com.mangocity.hotel.base.dao.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.dao.IClauseDaoRefactor;
import com.mangocity.hotel.base.persistence.HtlBookCaulClause;
import com.mangocity.hotel.base.persistence.HtlBookModifyField;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlFavourableDecrease;
import com.mangocity.hotel.base.persistence.HtlPopedomControl;
import com.mangocity.hotel.base.persistence.HtlPreconcertItemTemplet;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.util.DateUtil;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

/**
 * 条款dao
 * 
 */
public class ClauseDaoImplRefactor extends GenericDAOHibernateImpl implements IClauseDaoRefactor {

    
    /*
     * 根据酒店ID获取合同列表
     * 
     * 
     */
    public List<HtlContract> getContracts(Long hotelID) {
        return super.queryByNamedQuery("queryContractsByHotelID", new Object[] { hotelID });
    }

    /*
     * 根据酒店ID获取条款模板列表
     * 
     * 
     */
    public List<HtlPreconcertItemTemplet> getClauses(Long hotelID) {
        return super.queryByNamedQuery("queryClausesByHTLID", new Object[] { hotelID });
    }
    
    
    /*
     * 根据酒店ID得到起预定条款计算规则信息
     * 
     * 
     */
    public List<HtlBookCaulClause> getClauseRules(Long hotelID) {
        return super.queryByNamedQuery("queryClauseRulesByHTLID", new Object[] { hotelID });
    }
    
    
    /**
     * 根据hotelID得到预订条款计算规则
     */
    public List<HtlBookCaulClause> getBookCaulClauses(Long hotelID){
    	  /**
         * 查询(开始日期<=本对象结束日期 并且 结束日期>=本对象开始日期的记录) 或者 (开始日期=本对象开始日期 并且 结束日期=本对象结束日期的记录)
         */
        String hqlForBothSides = " from HtlBookCaulClause where hotelId = ? order by modifyTime ";
        /**
         * 查询出来有以下情况 查询出的结果1 |___________| 查询出的结果2,3,4 |_____| |___| |_______| 查询出的结果5
         * |________________| 本对象 |___________|
         */
        return super.query(hqlForBothSides, new Object[] { hotelID});
        
    }
  
    /*
     * 根据酒店ID得到起修改字段定义
     * 
     * 
     */
    public List<HtlBookModifyField> getBookModifyField(Long hotelID) {
        return super.queryByNamedQuery("queryBookModifyFieldByHTLID", new Object[] { hotelID });
    }
    
    
    /**
     * 取出计算规则中最严格的计算规则
     * @param hotelID
     * @param checkInDate
     * @param checkOutDate
     * @return
     */
    public List<HtlBookCaulClause> getBookCaulByDateRange(Long hotelID, Date checkInDate,
            Date checkOutDate) {
            StringBuffer hql = new StringBuffer(64);
            hql.append(" from HtlBookCaulClause where hotelId = ? ");
            hql.append(" and ((clauseRuleBeginDate <= trunc(?) and clauseRuleEndDate >= trunc(?))");
            hql.append(" or (clauseRuleBeginDate <= trunc(?) and clauseRuleEndDate >= trunc(?)))");
            
            // 离店日期-1
            Date checkOurDateBefore = DateUtil.getDate(checkOutDate, -1);
            Object[] params = new Object[5];
            params[0] = hotelID;
            params[1] = checkInDate;
            params[2] = checkInDate;
            params[3] = checkOurDateBefore;
            params[4] = checkOurDateBefore;
           
            return super.query(hql.toString(), params);

        }
    
    
    /**
     * 取出对应酒店的所有的优惠立减条款
     * 
     */
    public List<HtlFavourableDecrease> getAllavourableclause(long hotelId) {
		String hql = " select  a  from HtlFavourableDecrease a   where  a.hotelId=? and a.endDate >= trunc(sysdate) order by a.modifyTime desc";
		return super.query(hql, new Object[] {hotelId});
	}
    
    
    /**
     * 取出对应酒店价格类型优惠立减条款
     * 
     */
    public List<HtlFavourableDecrease> getFavourableclauseForPriceTypeId(long hotelId,long priceTypeId) {

		String hql = " select  a  from HtlFavourableDecrease a   where " +
		 " a.hotelId=?  and a. priceTypeId=? and a.endDate >= trunc(sysdate) order by a.modifyTime desc";
//		Object[] obj = new Object[] {hotelId,priceTypeId};
		return super.query(hql, new Object[] {hotelId,priceTypeId});
	//	lstFavDecrease=(List)super.query(sql, obj);
		
		//return lstFavDecrease;
	}
    
    /**
     * 查询优惠立减
     * @param hotelId
     * @param priceTypeId
     * @return
     */
    public List<HtlFavourableDecrease> getFavourableDecreaseOrder(long hotelId,long priceTypeId){
    		
    	return super.queryByNamedQuery("queryFavourableDecreaseOrder", new Object[]{hotelId,priceTypeId});
    	
    }

  /**
   * 查询小于modifyTime时的优惠立减
   * @param hotelId
   * @param priceTypeId
   * @param modifyTime
   * @return
   */
    public List<HtlFavourableDecrease> getSubFavDecrease(long hotelId,long priceTypeId,Date modifyTime){
    	return super.queryByNamedQuery("querySubFavDecrease", new Object[]{hotelId,priceTypeId,modifyTime});	
    }
    
    /**
     * 根据价格类型ID获取房型
     * @param priceTypeID
     * @return
     */
    public HtlRoomtype getHtlRoomTypeByPriceTypeId(long priceTypeID){
    	
    	String hql = " select  m  from HtlRoomtype m, HtlPriceType p   where " +
   	 				" p.roomType = m.ID  and p.ID=? ";
    	List<HtlRoomtype> results = super.query(hql, new Object[] {priceTypeID});
   	    if (results.isEmpty()) return null;
   	    return results.get(0);

    }
    /**
     * 根据ID获取HtlFavourableDecrease
     * @param Id
     * @return
     */
    public HtlFavourableDecrease getFavDecreaseById(long Id){
		String hql = " select  a  from HtlFavourableDecrease a   where a.id=? ";
    	List<HtlFavourableDecrease> resultLst = super.query(hql, new Object[] {Id});
    	if(resultLst.isEmpty())return null;
    	return resultLst.get(0);
    }
    
    /**
     * 取预付/面付价格数量
     * @param hotelId
     * @param priceTypeIte
     * @param beginD
     * @param endD
     * @return
     */
	public long getEleDayPriceNum(long hotelId,long priceTypeIte,Date beginD,Date endD,String type){
		
		
		String hql = "select count(a.ID) from HtlPrice a where a.hotelId=? and a.childRoomTypeId=?"
				+ " and a.ableSaleDate>=? and a.ableSaleDate<=? and a.payMethod=?";//将count(*)改成count(a.ID)可行吗
		List rLst = super.query(hql, new Object[] {hotelId,priceTypeIte,beginD,endD,type});
		
		//Long eleDayPriceNum=(Long)super.find(sql, obj);
		
		return Long.parseLong(rLst.get(0).toString());
		
	}
	/**
	 * 得到所有权限列表
	 * @return
	 */
	public List<HtlPopedomControl> getAllPopedomList(){

		
		String hql = " select  a  from HtlPopedomControl a";
		return super.query(hql, null);
	}
	
	public List<HtlPopedomControl> getPopedomListByLoginName(String loginName){
		String hsql = "from HtlPopedomControl a where a.logName = ?";
		
		return super.query(hsql, new Object[]{loginName});
	}

	public List<HtlPopedomControl> getPopedoms(String popedomControlType, String loginName) {
		String hsql="select a from HtlPopedomControl a where a.controlType=? and a.logName=?";
		return super.query(hsql, new Object[]{popedomControlType,loginName});
	}
}

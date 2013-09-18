package com.mangocity.hotel.base.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import com.mangocity.hotel.base.dao.IPriceDao;
import com.mangocity.hotel.base.persistence.HtlAddscopeHdr;
import com.mangocity.hotel.base.persistence.HtlBatchMtnPrice;
import com.mangocity.hotel.base.persistence.HtlBatchSalePrice;
import com.mangocity.hotel.base.persistence.HtlHdlAddscope;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.persistence.HtlPriceLOG;
import com.mangocity.hotel.base.persistence.HtlPriceType;
import com.mangocity.hotel.base.persistence.HtlRoom;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.util.DateUtil;
import com.mangocity.util.ValidationUtil;
import com.mangocity.util.bean.ContinueDatecomponent;
import com.mangocity.util.bean.DateSegment;
import com.mangocity.util.dao.GenericDAOHibernateImpl;
import com.mangocity.util.log.MyLog;

/**
 * 主要是对价格的录入、加幅、调价、查询的操作
 * 
 * @author zhengxin
 * 
 */
public class PriceDaoImpl extends GenericDAOHibernateImpl implements IPriceDao {

	private static final MyLog log = MyLog.getLogger(PriceDaoImpl.class);

	@SuppressWarnings("unchecked")
    public List<HtlRoom> batchQueryPrice(Long hotelId, Long[] roomType, List<DateSegment> dateSegments, 
    		Integer[] week) {
        List paramList = new ArrayList();
        StringBuilder hql = new StringBuilder();
        
        hql.append(" from HtlRoom room where room.hotelId = ? ");
        paramList.add(hotelId);
        
        if (!ValidationUtil.isEmpty(dateSegments)) {
        	hql.append(" and (");        	
        	for (DateSegment dateSegment : dateSegments) {
        		hql.append("(room.ableSaleDate >= ? and room.ableSaleDate <= ?) or ");
        		paramList.add(dateSegment.getStart());
        		paramList.add(dateSegment.getEnd());
            }        	
        	hql.setLength(hql.length() - 4);
        	hql.append(") ");
        	
        	if (!ValidationUtil.isEmpty(week)) {
        		hql.append(" and room.week in (");
        		for (Integer weekday : week) {
                	hql.append("?, ");
                    paramList.add(weekday);
                }
        		hql.setLength(hql.length() - 2);
                hql.append(")");
            }
        }
        
        if (!ValidationUtil.isEmpty(roomType)) {
    		hql.append(" and room.roomTypeId in (");
    		for (Long roomTypeId : roomType) {
            	hql.append("?, ");
                paramList.add(roomTypeId);
            }
    		hql.setLength(hql.length() - 2);
            hql.append(")");
        }
        log.debug("查询价格SQL：" + hql.toString());

        return super.query(hql.toString(), paramList.toArray());
    }
    public HtlPrice findRoomPrice(Long roomTypeID, Long childRoomTypeID, Date saleDate,
        String payMethod, String quotaType) {
        String hSql = null;
        Object[] params = null;
        if (null != childRoomTypeID) {
            hSql = "from HtlPrice price where  price.ableSaleDate = ? and price.room.roomTypeId = ? " 
            	+ " and price.childRoomTypeId = ? and price.payMethod = ? and price.quotaType = ? ";
            params = new Object[] { saleDate, roomTypeID, childRoomTypeID, payMethod, quotaType };
        } else {
            hSql = "from HtlPrice price where  price.ableSaleDate = ? and price.room.roomTypeId = ? " 
            	+  " and price.payMethod = ? and price.quotaType = ? ";
            params = new Object[] { saleDate, roomTypeID, payMethod, quotaType }; 
        }
        List<HtlPrice> priceList = super.query(hSql, params);
        if(priceList.isEmpty()) return null;
        
        return priceList.get(0);
    }

    public List getRoomPrices(long roomId) {
        return super.queryByNamedQuery("getRoomPrices", new Object[] { roomId });
    }

    public List<HtlRoomtype> getRoomtypeList(Long hotelId) 
    {
        return super.queryByNamedQuery("lstHotelRoomType" ,new Object[]{ hotelId });
    }
 
    public List<HtlPriceType> getPriceTypeList(Long hotelId) 
    {
        return super.queryByNamedQuery("queryPriceTypeByRoomType", new Object[] { hotelId });
    }

    public List getChangePriceWarnings(long HotelID) {        
        return super.queryByNamedQuery("getCpwData", new Object[] { HotelID, DateUtil.getDate(new Date())});
    }

    @SuppressWarnings("unchecked")
    public List<HtlPrice> queryPriceInAddPrice(Long hotelId, List<DateSegment> lstDate, String[] weeks,
        String[] priceTypeIds, String quotaType, String payMethodForQurey) {
        Criteria criteria = DetachedCriteria.forClass(HtlPrice.class).getExecutableCriteria(super.getSession());
        criteria.add(Restrictions.eq("hotelId", hotelId));
        criteria.add(Restrictions.eq("quotaType", quotaType));
        
        // 面付或者预付才会增加此条件
        if (null != payMethodForQurey && !payMethodForQurey.equals("")) {
            criteria.add(Restrictions.eq("payMethod", payMethodForQurey));
        }
        
        if (weeks != null && weeks.length > 0) {
        	List<Integer> lstWeek = new ArrayList<Integer>(weeks.length);
            for (String week: weeks) {
                lstWeek.add(Integer.valueOf(week));
            }
            criteria.add(Restrictions.in("week", lstWeek));
        }
        
        if (priceTypeIds != null && priceTypeIds.length > 0) {
            List<Long> lstPtIds = new ArrayList<Long>(priceTypeIds.length);
            for (String priceTypeId: priceTypeIds) {
                lstPtIds.add(Long.valueOf(priceTypeId));
            }
            criteria.add(Restrictions.in("childRoomTypeId", lstPtIds));
        }
        
        if (null != lstDate && !(lstDate.isEmpty())) {
        	for (DateSegment ds: lstDate) {
                if (null != ds.getEnd() && (null != ds.getStart())) {
                    criteria.add(Restrictions.between("ableSaleDate", ds.getStart(), ds.getEnd()));
                }
            }
        }
        //对查询数据按 日期,周,房型,价格类型,支付方式排序(升序)
        criteria.addOrder(Order.asc("ableSaleDate"));
        criteria.addOrder(Order.asc("week"));
        criteria.addOrder(Order.asc("roomTypeId"));
        criteria.addOrder(Order.asc("childRoomTypeId"));
        criteria.addOrder(Order.asc("payMethod"));
        
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<HtlBatchSalePrice> queryPrice(Long contractId, Date start, Date end, 
    		Long[] childRoomTypeId, String[] payMethod) 
    {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(HtlBatchSalePrice.class);
        Criteria criteria = detachedCriteria.getExecutableCriteria(super.getSession());
        criteria.add(Restrictions.eq("contractId", contractId));
        criteria.add(Restrictions.or(Restrictions.between("beginDate", start, end), Restrictions
            .between("endDate", start, end)));
        
        if (null != childRoomTypeId && 0 < childRoomTypeId.length)
        {
            criteria.add(Restrictions.in("childRoomTypeId", childRoomTypeId));
        }
        
        if (null != payMethod && 0 < payMethod.length)
        {
            criteria.add(Restrictions.in("payMethod", payMethod));
        }
        
        return criteria.list();
    }

    @SuppressWarnings("deprecation")
    public void saveOrUpdatePrice()
    {
    	super.execProcedure("{call PKGMTNPRICE.mtnPrice()}", null);
    }

    @SuppressWarnings(value={"deprecation","unchecked"})
    public String saveOrUpdatePrice(long hotelId, long contractId, String quotaType, String currency, 
    		String week)
    {
    	//输入参数的索引和参数值键值对
    	Map inParamIdxAndValue = new HashMap(5);
    	inParamIdxAndValue.put(Integer.valueOf(1), Long.valueOf(hotelId));
    	inParamIdxAndValue.put(Integer.valueOf(2), Long.valueOf(contractId));
    	inParamIdxAndValue.put(Integer.valueOf(3), quotaType);
    	inParamIdxAndValue.put(Integer.valueOf(4), currency);
    	inParamIdxAndValue.put(Integer.valueOf(5), week);
    	
    	//输出参数的索引和数据类型键值对
    	Map<Integer, Integer> outParamIdxAndType = new HashMap<Integer, Integer>(1);
    	outParamIdxAndType.put(Integer.valueOf(6), Integer.valueOf(java.sql.Types.VARCHAR));
    	
        try 
        {
        	super.execProcedure("{call SP_HOTEL_BATCHPRICE(?,?,?,?,?,?)}", 
        			inParamIdxAndValue, outParamIdxAndType);
        	
            return "1";
        } 
        catch (Exception e)
        {
        	log.error(e.getMessage(),e);
            return "0";
        }
    }

    @SuppressWarnings("unchecked")
    public List<HtlBatchSalePrice> getAlreadyLatAndEart(long contractId) {
        String hsql = "select p.childRoomTypeId, min(p.beginDate), "
            + "max(p.endDate) from HtlBatchSalePrice p where p.contractId = ?"
            + " group by p.childRoomTypeId";        
        List tepLaAndEaList = super.query(hsql, new Object[]{Long.valueOf(contractId)});
        
        List<HtlBatchSalePrice> laAndEaList = null;
        if(!tepLaAndEaList.isEmpty())
        {
        	laAndEaList = new ArrayList<HtlBatchSalePrice>(tepLaAndEaList.size());
	        for (int i = 0; i < tepLaAndEaList.size(); i++) {
	            Object[] o = (Object[]) tepLaAndEaList.get(i);
	            HtlBatchSalePrice htlBatchSalePrice = new HtlBatchSalePrice();
	            htlBatchSalePrice.setChildRoomTypeId(Long.parseLong(o[0].toString()));
	            Date beginDate = DateUtil.stringToDateTime(o[1].toString());
	            Date endDate = DateUtil.stringToDateTime(o[2].toString());
	            htlBatchSalePrice.setBeginDate(beginDate);
	            htlBatchSalePrice.setEndDate(endDate);
	            laAndEaList.add(htlBatchSalePrice);
	        }
        }
        else
        {
        	laAndEaList = new ArrayList<HtlBatchSalePrice>(0);
        }
        return laAndEaList;
    }

    @SuppressWarnings(value={"deprecation","unchecked"})
    public String saveOrUpdateAddScope(HtlHdlAddscope htlHdlAddscope) {
        String isSave = "1";
        super.saveOrUpdate(htlHdlAddscope);
        
        try
        {
        	Map inParamIdxAndValue = new HashMap(5);
        	inParamIdxAndValue.put(Integer.valueOf(1), Long.valueOf(htlHdlAddscope.getHotelId()));
        	inParamIdxAndValue.put(Integer.valueOf(2), Long.valueOf(htlHdlAddscope.getAddScope()));
        	inParamIdxAndValue.put(Integer.valueOf(3), DateUtil.dateToString(htlHdlAddscope.getBeginDate()));
        	inParamIdxAndValue.put(Integer.valueOf(4), DateUtil.dateToString(htlHdlAddscope.getEndDate()));
        	inParamIdxAndValue.put(Integer.valueOf(5), htlHdlAddscope.getAllPriceTypeId());
        	
        	Map<Integer, Integer> outParamIdxAndType = new HashMap<Integer, Integer>(1);
        	outParamIdxAndType.put(Integer.valueOf(6), Integer.valueOf(java.sql.Types.VARCHAR));
        	
	        Map<Integer, ?> resultMap = super.execProcedure("{call SP_HOTEL_ADDSCOPEPRICE(?,?,?,?,?,?)}", 
	        		inParamIdxAndValue, outParamIdxAndType);
	        if ("error".equals(resultMap.get(Integer.valueOf(6)).toString())) 
	        {
	            isSave = "0";
	        }
        } 
        catch (Exception e) 
        {
        	log.error(e.getMessage(),e);
            isSave = "error";
        }
        
        return isSave;
    }

    public List<HtlHdlAddscope> loadAddScope(Long hotelId) {
        String hsql = "from HtlHdlAddscope hda where hda.hotelId=?";
        
        return super.query(hsql, new Object[] { hotelId });
    }

    public List<String> checkIsAddScope(String newAllPriceId, Long hotelId, Long entityID,
        List<ContinueDatecomponent> lsContinueDatecomponent) {
        String[] allPriceId = newAllPriceId.split(",");
        List<String> lsReturnPriceId = new ArrayList<String>(allPriceId.length);
        StringBuilder hsql = new StringBuilder();
        hsql.append(" from HtlHdlAddscope hda where hda.hotelId = ? ");
        hsql.append(" and hda.allPriceTypeId like ? and hda.ID != ? and ");
        hsql.append(" ( ( ? between beginDate and endDate) or ( ? ");
        hsql.append(" between beginDate and endDate ) or ((beginDate between ? ");
        hsql.append(" and ? ) and ( endDate between ? and ?)))");
        
        for (ContinueDatecomponent continueDatecomponent: lsContinueDatecomponent) {
            Date beginDate = DateUtil.getDate(continueDatecomponent.getBeginDate());
            Date endDate = DateUtil.getDate(continueDatecomponent.getEndDate());
            
            for (String priceIdObj: allPriceId) {                
                List<HtlHdlAddscope> lsAddScope = super.query(hsql.toString(), 
                		new Object[] { hotelId, "%" + priceIdObj + "%", entityID, beginDate, endDate, 
                		beginDate, endDate, beginDate, endDate });
                if (!lsAddScope.isEmpty()) {
                    lsReturnPriceId.add(priceIdObj);
                }
            }
        }
        
        return lsReturnPriceId;
    }
    
    public HtlPrice qryPricInfoByFor(Date ableSaleDate, long hotelId, long roomTypeID, long priceTypeId, 
    		String payMethod) {
		String hql = "from HtlPrice where ableSaleDate = ? and  hotelId = ? and  roomTypeId = ? " 
			+ "and childRoomTypeId = ? and payMethod =? ";
		List<HtlPrice> htlPriceList = super.query(hql, new Object[]{ableSaleDate, hotelId, roomTypeID, 
				priceTypeId, payMethod});
		return htlPriceList.isEmpty()?null : htlPriceList.get(0);
    }
    
    public void saveBatchSalePrice(HtlBatchSalePrice batchSalePrice) {
    	super.save(batchSalePrice);
    }
    
    public void saveBatchSalePriceList(List<HtlBatchSalePrice> batchSalePriceList) {
    	super.saveOrUpdateAll(batchSalePriceList);
    }
    
    public void batchSaveOrUpdatePrice(List<HtlPrice> htlPriceList) {
    	super.saveOrUpdateAll(htlPriceList);
    }
    
    public void batchSaveOrUpdatePriceLong(List<HtlPriceLOG> htlPriceLogList) {
    	super.saveOrUpdateAll(htlPriceLogList);
    }
    
    public void batchSaveOrUpdateMtnPrice(List<HtlBatchMtnPrice> batchMtnPriceList) {
    	super.saveOrUpdateAll(batchMtnPriceList);
    }
    
    public void saveOrUpdateAddscopeHdr(HtlAddscopeHdr htlAddscopeHdr) {
    	super.saveOrUpdate(htlAddscopeHdr);
    }
    
    public HtlPrice qryHtlPrice(long priceId) {
    	return super.get(HtlPrice.class, Long.valueOf(priceId));
    }
    
    public void updateHtlPrice(HtlPrice htlPrice) {
    	super.update(htlPrice);
    }
    
    public HtlBatchSalePrice loadBatchSalePrice(long priceId) {
    	return super.load(HtlBatchSalePrice.class, Long.valueOf(priceId));
    }
    
    public void updateBatchSalePrice(HtlBatchSalePrice batchSalePrice) {
        super.update(batchSalePrice);
    }
    
    public List<HtlPrice> qryHtlPriceBySaleDateRangePriceType(Date beginDate, Date endDate, long childRoomTypeId, 
           String payMethod, String quotaType) {
        String hql = " from HtlPrice where ableSaleDate >= ? and ableSaleDate < ? "
        		+ " and childRoomTypeId = ? AND payMethod = ? and quotaType = ? order by ableSaleDate ";
        return super.query(hql, new Object[]{beginDate, endDate, Long.valueOf(childRoomTypeId), payMethod, quotaType});
    }
    
	public List<HtlPrice> queryHtlPriceByHotelIds(String hotelIds, Date startDate, Date endDate) {
		
		String hql = "select * from hwtemp_htl_price a where a.hotel_id in (" + hotelIds + ") and a.able_sale_date >= ? and a.able_sale_date < ?";
		List<HtlPrice> htlPriceList = super.queryByNativeSQL(hql, new Object[]{startDate,endDate},HtlPrice.class);
		return htlPriceList;
		
	}
    
    public long qryEleDayPriceNum(long hotelId,long priceTypeIte,Date beginD,Date endD){		
		String hql = " select count(*) from HtlPrice a where a.hotelId = ? and a.childRoomTypeId = ? "
				+ " and a.ableSaleDate >= ? and a.ableSaleDate <= ? and a.payMethod='pre_pay'";		
		List<?> eleDayPriceNum = super.query(hql, new Object[] {hotelId, priceTypeIte, beginD, endD});		
		
		return eleDayPriceNum.isEmpty()?0L : Long.parseLong(eleDayPriceNum.get(0).toString());		
	}
    
    public long qryEleDayPayPriceNum(long hotelId,long priceTypeIte,Date beginD,Date endD){
		
		String hql = "select count(*) from HtlPrice a where a.hotelId = ? and a.childRoomTypeId = ? "
				+ " and a.ableSaleDate >= ? and a.ableSaleDate <= ? and a.payMethod = 'pay'";
		List<?> eleDayPriceNum = super.query(hql, new Object[] {hotelId, priceTypeIte, beginD, endD});		
		
		return eleDayPriceNum.isEmpty()?0L : Long.parseLong(eleDayPriceNum.get(0).toString());
	}
	
	public String qryPriceTypeNameById(long priceTypeID) {		
		String hql = " select m from HtlRoomtype m, HtlPriceType p where p.roomType = m.ID  and p.ID=? ";
		List<HtlRoomtype> roomTypeList = super.query(hql, new Object[] {priceTypeID});
		if(roomTypeList.isEmpty()){
			return "";
		}
		
		HtlRoomtype roomtype = roomTypeList.get(0);
		List  lstPriceType = roomtype.getLstPriceType();
		String priceTypeStr="";
		int  j=0;
		for(int i =0;i<lstPriceType.size();i++){
			
			HtlPriceType  priceTypeObj = (HtlPriceType)lstPriceType.get(i);
			
			if(priceTypeObj.getID()==priceTypeID)
			{
				priceTypeStr+=(j>0?",":"")+roomtype.getRoomName()+"("+priceTypeObj.getPriceType()+")";
				j++;
			}
		}
		
		return priceTypeStr;
	}
}

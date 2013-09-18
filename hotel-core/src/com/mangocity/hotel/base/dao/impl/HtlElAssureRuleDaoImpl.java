package com.mangocity.hotel.base.dao.impl;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.dao.HtlElAssureRuleDao;
import com.mangocity.hotel.base.persistence.HtlElAssureRule;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

/**
 * 艺龙担保条款规则表DAO
 * @author huanglingfeng
 */
public class HtlElAssureRuleDaoImpl  extends GenericDAOHibernateImpl implements HtlElAssureRuleDao {
	
	/**
	 * 查询艺龙担保条款
	 * @param elHotelId 艺龙酒店ID
	 * @param rateplanId 艺龙价格类型ID
	 * @param checkinDate 订单首晚日期
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HtlElAssureRule> queryElAssureList(String elHotelId,
			String rateplanId, Date checkinDate) {
		String hql="from HtlElAssureRule h where h.elhotelid=? and h.rateplanid=? and h.startdate<=? and h.enddate>=? and h.status=1";
		//String hql="from HtlElAssureRule h where h.elhotelid="+elHotelId+" and h.rateplanid="+rateplanId+" and h.startdate<=? and h.enddate>=? and h.status=1";
		Object[] objs={elHotelId,rateplanId,checkinDate,checkinDate};
		return getHibernateTemplate().find(hql, objs);
	}
	
	
	/**
	 * 根据checkindate 和checkoutdate日期范围，查包含的艺龙条款，包括在店条款
	 * key=datetype-VouchMoneyType (在店/入住-首日金额/全额) value=rule
	 * 入住日期担保的条款只能有一个,在店日期判断的可能有多个
	 * @param hotelId
	 * @param priceTypeId
	 * @param checkInDate
	 * @param checkOutDate
	 * @return
	 */
	public List<HtlElAssureRule> queryElAssureMap(long hotelId, long priceTypeId,
			Date checkInDate, Date checkOutDate){
		String hql = "from HtlElAssureRule r where r.status=1 and "
	    +"r.startdate <= ? and r.enddate >= ? "
		+"and exists(select m.exMappingId from ExMapping m where m.channeltype=9 and m.type=3 "
		+"and m.isActive=1 and m.hotelcodeforchannel=r.elhotelid and m.rateplancode=r.rateplanid "
		+"and m.hotelid=? and m.priceTypeId = ?) order by r.startdate desc ";
		List<HtlElAssureRule> lst = getHibernateTemplate().find(hql, new Object[]{checkOutDate,checkInDate,new Long(hotelId),priceTypeId+""});
		return lst;
	}
}

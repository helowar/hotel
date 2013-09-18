package com.mangocity.hagtb2b.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.mangocity.hagtb2b.dao.HtlB2bDao;
import com.mangocity.hagtb2b.persistence.HtlB2bIncrease;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.dao.GenericDAOHibernateImpl;
public class HtlB2bDaoImpl extends GenericDAOHibernateImpl implements HtlB2bDao{
	
	public List<Object[]> queryMinPrice(Set<String> minPriceParams,Date date) {
		StringBuilder sql = new StringBuilder("select hpi.hotel_id,hpi.price_type_id  from htl_preconcert_item hpi,htl_prepay_everyday hpe");
		sql.append(" where hpe.everyday_reservation_clause_id = hpi.id ");
		sql.append(" and  hpi.active = 1");
		sql.append(" and  hpe.payment_type = 1");
		sql.append(" and hpi.valid_date = ?");
		sql.append(" and hpi.hotel_id in (");
		for(String minPriceParam : minPriceParams){
			sql.append(minPriceParam).append(",");
		}
		//将最后一个, 替换为)
		System.out.println(sql.toString());
		sql.setCharAt(sql.length()-1, ')');
		System.out.println(sql.toString());
		return super.queryByNativeSQL(sql.toString(), new Object[]{date}, null);
	}

	public List<HtlB2bIncrease> queryIncreasePrice(List<Long> hotelIdParams,
			List<Long> priceTypeIdParams, Date inDate, Date outDate) {
		StringBuilder sql =new StringBuilder(" select p.hotel_id,p.room_type_id,p.child_room_type_id,p.base_price,(p.sale_price - p.base_price) as COMMISSION,p.able_sale_date, ");
		sql.append(" i.INCREASE_RATE,p.currency ");
		sql.append(" from hwtemp_htl_price p, b2b_increase i ");
		sql.append(" where p.hotel_id = i.hotel_id ");
		sql.append(" and i.flag =1 ");
		sql.append(" and p.pay_method = 'pre_pay' ");
		sql.append(" and p.able_sale_date >=trunc(?) ");
		sql.append(" and p.able_sale_date <trunc(?) ");//离店日期 应该只有小于不能等于
		sql.append(" and p.child_room_type_id in ( ");
		for(Long priceTypeIdParam:priceTypeIdParams){
			sql.append(priceTypeIdParam).append(",");
		}
		sql.setCharAt(sql.length()-1, ')');
		sql.append(" and p.hotel_id in ( ");
		for(Long hotelIdParam : hotelIdParams){
			sql.append(hotelIdParam).append(",");
		}
		sql.setCharAt(sql.length()-1, ')');
		//这里封装对象
		List<Object[]> objects= super.queryByNativeSQL(sql.toString(), new Object[]{inDate,outDate}, null);
		List<HtlB2bIncrease> htlB2bIncreases = new ArrayList<HtlB2bIncrease>();
		
		for(Object[] obj:objects){
			HtlB2bIncrease htlB2bIncrease = new HtlB2bIncrease();
			htlB2bIncrease.setHotelId(((BigDecimal)obj[0]).longValue()); //BigDeimal类型 需要转换
			htlB2bIncrease.setRoomTypeId(((BigDecimal)obj[1]).longValue()); 
			htlB2bIncrease.setPriceTypeId(((BigDecimal)obj[2]).longValue()); 
			htlB2bIncrease.setBasePrice(((BigDecimal)obj[3]).doubleValue()); 
			htlB2bIncrease.setCommission(((BigDecimal)obj[4]).doubleValue()); 
			htlB2bIncrease.setAbleSaleDate((Date)(obj[5])); 
			double increaseRate = Double.valueOf(String.valueOf(obj[6]));
			if(increaseRate > 1) {//设置加幅的是固定金额
				htlB2bIncrease.setIncreasePrice(htlB2bIncrease.getBasePrice()+increaseRate/CurrencyBean.getRateToRMB(String.valueOf(obj[7])));
				htlB2bIncrease.setCurrency(String.valueOf(obj[7]));
			}else {//设置加幅的是比例
				htlB2bIncrease.setIncreasePrice(htlB2bIncrease.getBasePrice() + htlB2bIncrease.getCommission() * increaseRate);
				htlB2bIncrease.setCurrency(String.valueOf(obj[7]));
			}
			htlB2bIncreases.add(htlB2bIncrease);
		}
		
		return htlB2bIncreases;
	}
	
	public List<HtlB2bIncrease> queryIncreasePrice(List<String> hotelIds, Date inDate) {
		if(hotelIds != null && hotelIds.size() >0) {
			StringBuilder sql =new StringBuilder(" select p.hotel_id,p.room_type_id,p.child_room_type_id,p.base_price,(p.sale_price - p.base_price) as COMMISSION,p.able_sale_date, ");
			sql.append(" i.INCREASE_RATE,p.currency ");
			sql.append(" from hwtemp_htl_price p, b2b_increase i ");
			sql.append(" where p.hotel_id = i.hotel_id ");
			sql.append(" and i.flag =1 ");
			sql.append(" and p.pay_method = 'pre_pay' ");
			sql.append(" and p.able_sale_date = trunc(?) ");
			sql.append(" and p.hotel_id in ( ");
			for(String hotelId : hotelIds){
				sql.append(hotelId).append(",");
			}
			sql.setCharAt(sql.length()-1, ')');
			
			//这里封装对象
			List<Object[]> objects= super.queryByNativeSQL(sql.toString(), new Object[]{inDate});
			
			List<HtlB2bIncrease> htlB2bIncreases = new ArrayList<HtlB2bIncrease>();
			
			for(Object[] obj:objects){
				HtlB2bIncrease htlB2bIncrease = new HtlB2bIncrease();
				htlB2bIncrease.setHotelId(((BigDecimal)obj[0]).longValue()); //BigDeimal类型 需要转换
				htlB2bIncrease.setRoomTypeId(((BigDecimal)obj[1]).longValue()); 
				htlB2bIncrease.setPriceTypeId(((BigDecimal)obj[2]).longValue()); 
				htlB2bIncrease.setBasePrice(((BigDecimal)obj[3]).doubleValue()); 
				htlB2bIncrease.setCommission(((BigDecimal)obj[4]).doubleValue()); 
				htlB2bIncrease.setAbleSaleDate((Date)(obj[5])); 
				double increaseRate = Double.valueOf(String.valueOf(obj[6]));
				if(increaseRate > 1) {//设置加幅的是固定金额
					htlB2bIncrease.setIncreasePrice(CurrencyBean.getRateToRMB(String.valueOf(obj[7]))*htlB2bIncrease.getBasePrice()+increaseRate);
					htlB2bIncrease.setCurrency(CurrencyBean.RMB);
				}else {//设置加幅的是比例
					htlB2bIncrease.setIncreasePrice(htlB2bIncrease.getBasePrice() + htlB2bIncrease.getCommission() * increaseRate);
					htlB2bIncrease.setCurrency(String.valueOf(obj[7]));
				}
				htlB2bIncreases.add(htlB2bIncrease);
			}
			return htlB2bIncreases;
		}
		return null;
	}
	
	
}

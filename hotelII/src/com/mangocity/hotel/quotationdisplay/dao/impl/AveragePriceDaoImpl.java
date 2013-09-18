package com.mangocity.hotel.quotationdisplay.dao.impl;



import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.mangocity.hotel.base.web.InitServletImpl;
import com.mangocity.hotel.quotationdisplay.dao.IAveragePriceDao;
import com.mangocity.hotel.quotationdisplay.model.QueryParam;
import com.mangocity.util.StringUtil;
/***
 * 
 * @author panjianping
 *
 */
public class AveragePriceDaoImpl extends HibernateDaoSupport implements IAveragePriceDao {
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 通过queryParam查询参数对象，根据条件查询价格统计表中的信息
	 * （需要用到价格统计表中的最低价信息min_price字段）
	 */
	public List queryHotelOnCondition(QueryParam queryParam) {
		//确定价格统计表
		String priceSortTableName=getPriceSortTableName(queryParam.getCityCode());
		if(priceSortTableName==null)return null;
		Date date = queryParam.getDate();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),0,0);
		String d =calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH);
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("select * from "+priceSortTableName+" hp ");
		if(null!=queryParam.getDate()){
			stringBuffer.append(" where hp.suable_date = "+"to_date('"+d+"','yyyy-mm-dd')");
	}
		stringBuffer.append(" and hp.hotel_id in (select hotel_id from htl_hotel ");
		if(null!=queryParam.getCityCode()&&""!=queryParam.getCityCode()){//增加city限制条件
		   stringBuffer.append(" where city= '"+queryParam.getCityCode()+"'");	
			}
		if(null!=queryParam.getZone()&&""!=queryParam.getZone()&&!"bxdq".equals(queryParam.getZone().trim())){//增加商圈限制条件
			   stringBuffer.append(" and zone= '"+queryParam.getZone()+"'");
			}
		if(queryParam.getLevel()){  //查询中高档酒店
			stringBuffer.append(" and hotel_star in ('19' ,'29','39' ,'49' ,'59','64')");
		}else{                             //查询经济型酒店
			stringBuffer.append(" and hotel_star in('69' ,'79' ,'66')");
		}
		stringBuffer.append(")");
		List hotelSummaryList=jdbcTemplate.queryForList(stringBuffer.toString());
        return hotelSummaryList;

		
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	/**
	 * 确定价格统计表
	 */
	@SuppressWarnings("unchecked")
	private String getPriceSortTableName(String cityCode){
		if(!StringUtil.isValidStr(cityCode))return null;
		String area=cityCode;
		//如果是八大执门城市则直接查询对应名称的统计表
		if(InitServletImpl.cityAreaObj.get(cityCode)!=null)
			return "htl_price_sort_"+area;
		String sql="select areacode from htl_area ha where ha.citycode='"+cityCode+"'";
		Map map=jdbcTemplate.queryForMap(sql);
		if(map!=null&&map.size()!=0){
			area=map.get("areacode").toString();
			//如果为华东或者华北则到查询对应区域名称的统计表,否则查询other表
			if("HDQ".equals(area)||"HBQ".equals(area)){
				return "htl_price_sort_"+area;
			}else{
				return "htl_price_sort_other";
			}
		}
		return null;
	}

}

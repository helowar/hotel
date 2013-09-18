package com.mangocity.webnew.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.webnew.dao.MangoSalesDao;
import com.mangocity.webnew.util.MangoSalesUtil;

/**
 */
public class MangoSaleDaoImpl extends DAOHibernateImpl implements MangoSalesDao {

	private JdbcTemplate jdbcTemplate;

	/*
	 * 根据酒店ID获取促销酒店信息 (non-Javadoc)
	 * 
	 * 
	 */
	public List getSalesHotel(Long hotelID) {
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf
				.append(" select v.hotel_id,v.chn_name,v.city,v.gisid,v.longitude,v.latitude  from  htl_hotel v ");
		sqlBuf.append(" where v.hotel_id = ? ");
		List lstHl = jdbcTemplate.queryForList(sqlBuf.toString(),
				new Object[] { hotelID });
		if (lstHl == null) {
			return MangoSalesUtil.EMPTY_LIST;
		}
		return lstHl;
	}

	public List getRoomType(Long hotelid, Long roomTypeID, Long priceTypeId) {
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf
				.append(" select m.room_type_id,m.room_name,p.price_type_id,p.price_type  from  htl_hotel h,htl_roomtype m, htl_price_type p ");
		sqlBuf
				.append(" where  h.hotel_id = m.hotel_id  and  m.room_type_id = p.room_type_id ");
		sqlBuf
				.append(" and  h.hotel_id = ?  and  m.room_type_id = ? and  p.price_type_id = ? ");
		List lstHl = jdbcTemplate.queryForList(sqlBuf.toString(), new Object[] {
				hotelid, roomTypeID, priceTypeId });
		if (lstHl == null) {
			return MangoSalesUtil.EMPTY_LIST;
		}
		return lstHl;
	}

	public List getSalePrice(Long hotelid, Long roomTypeID, Long priceTypeId,
			Date currDate) {
		StringBuffer sqlBuf = new StringBuffer();                                                                                 
		
		sqlBuf.append("SELECT  p.sale_price,                                                                                                            ");
		sqlBuf.append("        case when sum((case when (NVL(q.BUY_QUOTA_ABLE_NUM,0)+NVL(q.COMMON_QUOTA_ABLE_NUM,0)+NVL(q.CASUAL_QUOTA_ABLE_NUM,0)) > 0 ");
		sqlBuf.append("        then 1                                                                                                                   ");
		sqlBuf.append("        else 0                                                                                                                   ");
		sqlBuf.append("        end)) >0 then 1 else 0 end as FLAGSTR                                                                                    ");        
		sqlBuf.append("  FROM hwtemp_htl_room       r,                                                                                                  ");
		sqlBuf.append("       hwtemp_htl_price      p,                                                                                                  ");
		sqlBuf.append("       htl_quota_new  q,                                                                                                         ");
		sqlBuf.append("       htl_hotel      h,                                                                                                         ");
		sqlBuf.append("       htl_price_type t,                                                                                                         ");
		sqlBuf.append("       htl_roomtype   y                                                                                                          ");
		sqlBuf.append(" WHERE r.room_id = p.room_id                                                                                                     ");
		sqlBuf.append("   AND y.ROOM_TYPE_ID = q.ROOMTYPE_ID                                                                                            ");
		sqlBuf.append("   AND ((p.pay_method = 'pay' AND                                                                                                ");
		sqlBuf.append("       (q.QUOTA_SHARE_TYPE = '1' OR q.QUOTA_SHARE_TYPE = '3')) OR                                                                ");
		sqlBuf.append("       (p.pay_method = 'pre_pay' AND                                                                                             ");
		sqlBuf.append("       (q.QUOTA_SHARE_TYPE = '2' OR q.QUOTA_SHARE_TYPE = '3')))                                                                  ");
		sqlBuf.append("   AND h.hotel_id = r.hotel_id                                                                                                   ");
		sqlBuf.append("   AND h.hotel_id = y.hotel_id                                                                                                   ");
		sqlBuf.append("   AND p.child_room_type_id = t.price_type_id                                                                                    ");
		sqlBuf.append("   and q.hotel_id = h.hotel_id                                                                                                   ");
		sqlBuf.append("   and p.hotel_id = h.hotel_id                                                                                                   ");
		sqlBuf.append("   AND p.room_type_id = t.room_type_id                                                                                           ");
		sqlBuf.append("   AND r.room_type_id = y.room_type_id                                                                                           ");
		sqlBuf.append("   and t.room_type_id = y.room_type_id                                                                                           ");
		sqlBuf.append("   and q.able_sale_date = r.able_sale_date                                                                                       ");
		sqlBuf.append("   and h.ACTIVE = '1'                                                                                                            ");
		sqlBuf.append("   and q.bed_id !=0                                                                                                              ");
		sqlBuf.append("   and h.hotel_id = ?                                                                                                     ");
		sqlBuf.append("   and y.room_type_id =  ?                                                                                                   ");
		sqlBuf.append("   and t.price_type_id = ?                                                                                                   ");
		sqlBuf.append("   and q.able_sale_date = ?                                                                                        ");
		sqlBuf.append("   group by p.sale_price                                                                                                                        ");
		
		List lstHl = jdbcTemplate.queryForList(sqlBuf.toString(),new Object[]{hotelid,roomTypeID,priceTypeId,currDate});
		if(lstHl == null){
			return MangoSalesUtil.EMPTY_LIST;
		}
		return lstHl;
	}
	
	public List getRuJiaHotel(Long hotelid) {
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("select count(distinct h.HOTEL_ID) as hotel_count   ");
		sqlBuf.append("  from V_HTL_HOTEL h, EX_MAPPING m                 ");
		sqlBuf.append(" where  h.group_name = '如家酒店连锁管理有限公司'  ");
		sqlBuf.append("   and h.cooperate_channel = 2                     ");
		sqlBuf.append("   and h.HOTEL_ID = m.hotelid                      ");
		sqlBuf.append("   and m.isactive = 1                              ");
		sqlBuf.append("   and m.channeltype = 2                           ");
		sqlBuf.append("   and m.roomtypecodeforchannel is not null        ");
		sqlBuf.append("   and h.HOTEL_ID =  ?                              ");
		List lstHl = jdbcTemplate.queryForList(sqlBuf.toString(), new Object[] {hotelid });
		if (lstHl == null) {
			return MangoSalesUtil.EMPTY_LIST;
		}
		return lstHl;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}

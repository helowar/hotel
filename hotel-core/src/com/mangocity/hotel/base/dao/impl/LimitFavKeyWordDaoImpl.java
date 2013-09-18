package com.mangocity.hotel.base.dao.impl;

import java.io.Serializable;
import java.util.List;

import com.mangocity.hotel.base.dao.LimitFavKeyWordDao;
import com.mangocity.hotel.base.persistence.HtlFavourable;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.util.dao.DAOHibernateImpl;

public class LimitFavKeyWordDaoImpl extends DAOHibernateImpl implements LimitFavKeyWordDao{
	/**
	 * 根据hotelId查询htl_favourable表相应记录
	 * @param hotelId
	 * @return
	 */
	public HtlFavourable queryFromHtlFavourable(Long hotelId){
		String hql = "from HtlFavourable  f where f.flag = 1 and f.hotelId = ?";
		List<HtlFavourable>  favList = super.doquery(hql, new Object[]{hotelId}, false);
		if(favList.size()>0){return favList.get(0);}else{
			return null;
		}
		
	}
	/**
	 * 删除一条活动标签记录
	 * @param hotelId
	 */
	public int  singleDeleteHtlFavourable(Long Id){
		String updateSql = "update htl_favourable f set f.flag = 0 where f.id = "+Id;
		return super.doSqlUpdate(updateSql);
	}
	/**
	 * 新增一条活动标签记录
	 * @param htlFavourable
	 */
	public void insertHtlFacourable(HtlFavourable htlFavourable){
		Serializable id =  super.save(htlFavourable);
	}
	/**
	 * 
	 */
	public void singleSettingKeyWord(String hotelId,String loginName,int fav_a,int fav_b,int fav_c){
		String insertSql = "insert into htl_favourable f (id, city_name, hotel_name, hotel_id,fav_a,fav_b,fav_c, update_time, update_by, city_code, flag)"+
       "select seq_htl_favourable.nextval, b.title, h.chn_name, h.hotel_id,"+fav_a+","+fav_b+","+fav_c+",sysdate,"+loginName+",h.city,1"+
      " from htl_hotel h, cmd.t_cdm_basedata b where b.levels = 5 and b.name = h.city and h.hotel_id = "+hotelId;
		super.doSqlUpdate(insertSql);
	}
	/**
	 *批量增加活动标签
	 * @param hotelIdStr
	 * @param loginName
	 * @param batch_a
	 * @param batch_b
	 * @param batch_c
	 */
	public void batchSettingKeyWord(String hotelIdStr,String loginName,String fav_a,String fav_b,String fav_c){
		String sql = "update htl_favourable f set f.update_by = '" +loginName+"',f.update_time = sysdate";
		if(null != fav_a && "1".equals(fav_a)){sql += " ,f.fav_a = 1 "; }
		if(null != fav_b && "1".equals(fav_b)){sql += ",f.fav_b = 1 "; }
		if(null != fav_c && "1".equals(fav_c)){sql += " ,f.fav_c = 1 "; }
		sql += " where f.hotel_id in ("+hotelIdStr+")";
		super.doSqlUpdate(sql);
	}
	
	/**
	 *批量取消活动标签
	 * @param hotelIdStr
	 * @param loginName
	 * @param batch_a
	 * @param batch_b
	 * @param batch_c
	 */
	public void batchCancelKeyWord(String hotelIdStr,String loginName,String fav_a,String fav_b,String fav_c){
		String sql = "update htl_favourable f set f.update_by = '" +loginName+"',f.update_time = sysdate";
		if(null != fav_a && "1".equals(fav_a)){sql += " ,f.fav_a = 0 "; }
		if(null != fav_b && "1".equals(fav_b)){sql += ",f.fav_b = 0 "; }
		if(null != fav_c && "1".equals(fav_c)){sql += " ,f.fav_c = 0 "; }
		sql += " where f.hotel_id in ("+hotelIdStr+")";
		super.doSqlUpdate(sql);
	}
	/**
	 * 查询酒店基本信息
	 * @param hotelId
	 * @return
	 */
	public HtlHotel queryHotelBaseInfo(Long hotelId){
		String hql = "from HtlHotel  h where h.ID = ?";
		List<HtlHotel>  hotelList = super.doquery(hql, new Object[]{hotelId}, false);
		return hotelList.get(0);	
	}
}

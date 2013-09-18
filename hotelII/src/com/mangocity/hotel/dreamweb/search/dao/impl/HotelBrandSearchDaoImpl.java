package com.mangocity.hotel.dreamweb.search.dao.impl;

import java.util.List;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.dreamweb.search.dao.HotelBrandSearchDao;
import com.mangocity.hotel.search.model.HotelBrandBean;
import com.mangocity.util.dao.GenericDAOHibernateImpl;
 
public class HotelBrandSearchDaoImpl extends GenericDAOHibernateImpl implements HotelBrandSearchDao{

	public String queryHotelBrandName(String brandCode) {
		String sql = "select brand.nameplate_name from htl_nameplate brand where brand.nameplate_id = ? ";
		List<String> brandCodeList = super.queryByNativeSQL(sql,
				new Object[] { brandCode });
		if (brandCodeList.size() == 0) {
			return null;
		} else {
			return brandCodeList.get(0);
		}
	}
	
	public String queryBrandIntroduce(String brandCode) {
		String sql = "select brand.brand_introduce from htl_contactinfo brand where brand.nameplate_id = ? ";
		List<String> brandCodeList = super.queryByNativeSQL(sql,
				new Object[] { brandCode });
		if (brandCodeList.size() == 0) {
			return null;
		} else {
			return brandCodeList.get(0);
		}
	}
	//按酒店数的多少来查询酒店的其他品牌信息，不包括该品牌
    public List<Object[]> queryOtherHotelBrands(String brandCode){
    	String sql = "select hc.nameplate_id,hc.nameplate_name, count(h.hotel_Id) from htl_hotel h,htl_nameplate hc where hc.nameplate_id = h.nameplate_name and "
                     +" hc.nameplate_id <> ? group by hc.nameplate_id ,hc.nameplate_name order by count(h.hotel_Id) desc ";
    	List<Object[]> brandCodeList = super.queryByNativeSQL(sql,0,6,new Object[] { brandCode },null);
    	return brandCodeList;
    }
	
	
	// 按酒店数的多少来查询酒店的其他品牌信息，不包括该品牌
	public List<Object[]> queryOtherHotelBrands(String brandCode,String cityCode){
		String sql = "select hc.nameplate_id,hc.nameplate_name, count(h.hotel_Id) from htl_hotel h,htl_nameplate hc where hc.nameplate_id = h.nameplate_name and "
            +" hc.nameplate_id <> ? and h.city = ? group by hc.nameplate_id ,hc.nameplate_name order by count(h.hotel_Id) desc ";
		List<Object[]> brandCodeList = super.queryByNativeSQL(sql,0,6,new Object[] { brandCode,cityCode },null);
		return brandCodeList;
	}
}

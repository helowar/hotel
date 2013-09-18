package com.mangocity.hotel.dreamweb.search.dao.impl;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.persistence.ExMapping;
import com.mangocity.hotel.dreamweb.search.dao.HotelBookDao;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class HotelBookDaoImpl extends GenericDAOHibernateImpl implements HotelBookDao {
	
	/**
	 * 根据priceTypeId得到HotelId
	 */
	public Long getHotelId(Long priceTypeId){
		String sql="select hr.hotel_id from htl_roomtype hr where hr.room_type_id in (select hp.room_type_id  from htl_price_type hp where hp.price_type_id = ? ) ";
		
		List<Object> result=super.queryByNativeSQL(sql, 0, 1, new Object[]{priceTypeId}, null);
		if(result==null || result.size() <=0){
			return 0L;
		}
		else{
		return Long.valueOf(result.get(0).toString());
		}
	}
	
	/**
	 * 根据priceTypeId得到roomTypeId
	 */
	public Long getRoomTypeId(Long priceTypeId){
		String sql ="select hp.room_type_id  from htl_price_type hp where hp.price_type_id = ?  ";
		List<Object> result=super.queryByNativeSQL(sql, 0, 1, new Object[]{priceTypeId}, null);
		if(result==null || result.size() <=0){
			return 0L;
		}
		else{
		return Long.valueOf(result.get(0).toString());
		}
	}
	
	/**
	 * 查询渠道号 对应ex_mapping表
	 */
	public ExMapping getExMapping(Long priceTypeId){
		String sql ="select g.* from ex_mapping g where "
			           +" g.privetypeid = ? "
					   +"  and exists (select 1 from ex_mapping em where em.hotelid = g.hotelid and em.type = 1 and em.isactive =1 and em.channeltype=g.channeltype)";
					   
		List<ExMapping> channelTypeList = super.queryByNativeSQL(sql,  0,1,new Object[]{priceTypeId},ExMapping.class);
		if(null == channelTypeList || channelTypeList.size() == 0 ){
			return null;
		}else{
			return channelTypeList.get(0);
		}
	}
	
	/**
	 * 查询直连酒店是否激活
	 */
	public boolean getIsActive(Long priceTypeId,int roomChannel){
		String sql = "select g.* from ex_mapping g where "
				+ " g.privetypeid = ? "
				+ " and g.channeltype = ? "
				+ " and exists (select 1 from ex_mapping em where em.roomtypeid = g.roomtypeid and em.type = 2 and em.isactive =1 and em.channeltype=g.channeltype)"
				+ " and g.isactive = 1 and g.type = 3 ";

		List<ExMapping> channelTypeList = super.queryByNativeSQL(sql, 0, 1,
				new Object[] { priceTypeId ,roomChannel}, ExMapping.class);
		if (null == channelTypeList || channelTypeList.size() == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	
	/**
	 * 获取商品信息
	 *  
	 * 获取以下信息：
	 * 
	 * 	roomTypeId=32904,      房型Id
    roomTypeName=海景预付, 房型名称	
	childRoomTypeId=34053,  价格类型Id
	childRoomTypeName=标准, 价格类型名称			
	
	breakfastType=3,     早餐类型
	breakfastNum=0,      早餐数
	bedTypeStr=2,        床型

	currency=HKD,        币种
	canbook=null,是否可预订

	payToPrepay=false,   面转预
	
	priceNum=200.0,        售卖价格（总价）
	returnAmount=25,        返现金额

	 minRoomNumCts=1,        中旅的最少入住间数
	 avlprice=200            均价 
	 * 
	 */
	private static String QUERY_COM_SQL_1 = "select rt.room_type_id,"
			+ "rt.room_name,"
			+ "pt.price_type_id,"
			+ "pt.price_type,"
			+ "p.able_sale_date,"
			+ "p.inc_breakfast_type,"
			+ "p.inc_breakfast_number,"
			+ "0 as avail_qty,"
			+ "r.room_state,"
			+ "p.currency,"
			+ "p.pay_to_prepay,"
			+ "p.formulaid,"
			+ "p.sale_price,"
			+ "p.commission_rate,"
			+ "p.commission,"
			+ "p.hotel_id, "			
			+ "p.CLOSE_FLAG, "
			
			+ "p.LATEST_BOOKABLE_DATE, "
			+ "p.LATEST_BOKABLE_TIME, "
			+ "p.MUST_LAST_DATE, "
			+ "p.MUST_FIRST_DATE, "
			+ "p.CONTINUE_DAY, "
			+ "p.MUST_IN_DATE, "
			+ "p.MIN_RESTRICT_NIGHTS, "
			+ "p.MAX_RESTRICT_NIGHTS, "
			+ "p.CONTINUE_DATES_RELATION, "
			+ "p.FIRST_BOOKABLE_DATE, "
			+ "p.FIRST_BOOKABLE_TIME "						  					
			
			+ "from htl_price p, htl_room r, htl_roomtype rt, htl_price_type pt "
			+ "where p.child_room_type_id = ? " + "and p.pay_method = ? "
			+ "and p.able_sale_date >= ? " + "and p.able_sale_date < ? "
			+ "and rt.room_type_id = p.room_type_id "
			+ "and pt.price_type_id = p.child_room_type_id "
			+ "and r.room_type_id = p.room_type_id "
			+ "and r.able_sale_date >= ? " + "and r.able_sale_date < ? "
			+ "and r.able_sale_date = p.able_sale_date "			
			+ "and p.sale_Price > 0 and p.sale_Price < 99999 " + "order by p.able_sale_date ";
	private static String QUERY_COM_SQL_2 = "select rt.room_type_id,"
			+ "rt.room_name,"
			+ "pt.price_type_id,"
			+ "pt.price_type,"
			+ "p.able_sale_date,"
			+ "p.inc_breakfast_type,"
			+ "p.inc_breakfast_number,"
			+ "0 as avail_qty,"
			+ "r.room_state,"
			+ "p.currency,"
			+ "p.pay_to_prepay,"
			+ "p.formulaid,"
			+ "p.sale_price,"
			+ "p.commission_rate,"
			+ "p.commission,"
			+ "p.hotel_id, "
			+ "p.CLOSE_FLAG, "
			
			+ "p.LATEST_BOOKABLE_DATE, "
			+ "p.LATEST_BOKABLE_TIME, "
			+ "p.MUST_LAST_DATE, "
			+ "p.MUST_FIRST_DATE, "
			+ "p.CONTINUE_DAY, "
			+ "p.MUST_IN_DATE, "
			+ "p.MIN_RESTRICT_NIGHTS, "
			+ "p.MAX_RESTRICT_NIGHTS, "
			+ "p.CONTINUE_DATES_RELATION, "
			+ "p.FIRST_BOOKABLE_DATE, "
			+ "p.FIRST_BOOKABLE_TIME, "
			
			+ "nvl(r.inc_qty_type,999), "
			+ "p.base_price "
			+ "from htl_price p, htl_room r, htl_roomtype rt, htl_price_type pt "
			+ "where p.child_room_type_id = ? " + "and p.pay_method = ? "
			+ "and p.able_sale_date >= ? " + "and p.able_sale_date < ? "
			+ "and rt.room_type_id = p.room_type_id "
			+ "and pt.price_type_id = p.child_room_type_id "
			+ "and r.room_type_id = p.room_type_id "
			+ "and r.able_sale_date >= ? " + "and r.able_sale_date < ? "
			+ "and r.able_sale_date = p.able_sale_date "
			+ "and p.sale_Price > 0 and p.sale_Price < 99999 " + "order by p.able_sale_date ";	
    public List<Object[]> queryCommidity(Long priceTypeId, String payMehtod,
			Date inDate, Date outDate, boolean forCts) {

    	if (!forCts) {
			return super.queryByNativeSQL(QUERY_COM_SQL_1, new Object[] {
					priceTypeId, payMehtod, inDate, outDate, inDate, outDate });
		} else {
			return super.queryByNativeSQL(QUERY_COM_SQL_2, new Object[] {
					priceTypeId, payMehtod, inDate, outDate, inDate, outDate});
		}

	}
	
}

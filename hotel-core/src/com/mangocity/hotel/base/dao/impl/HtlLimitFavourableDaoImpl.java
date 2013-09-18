package com.mangocity.hotel.base.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.dao.HtlLimitFavourableDao;
import com.mangocity.hotel.base.persistence.HtlLimitFavourable;
import com.mangocity.hotel.base.persistence.HtlLimitFavourableHotel;
import com.mangocity.util.DateUtil;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class HtlLimitFavourableDaoImpl extends GenericDAOHibernateImpl implements HtlLimitFavourableDao {

	public void saveHtlLimitFavourable(HtlLimitFavourable htlLimitFavourable) {
		super.saveOrUpdate(htlLimitFavourable);
	}
	
	public HtlLimitFavourable queryByFavId(Long favId){
		return (HtlLimitFavourable)super.get(HtlLimitFavourable.class, favId);
	}
	
	/**
	 * 查询酒店在一段时间内的返限活动
	 */
	public List<HtlLimitFavourableHotel> queryLimitFavourableHotel(String hotelIdList,Date beginDate,Date endDate){
		List<HtlLimitFavourableHotel> listHtlLimitFavourableHotel=new ArrayList<HtlLimitFavourableHotel>();
		
		String nativeSql = "select hfh.hotelid," +
				                  "hlf.favid," +
				                  "hlf.favname," +
				                  "hlf.begindate," +
				                  "hlf.enddate," +
				                  "hlf.begintime," +
				                  "hlf.endtime," +
				                  "hlf.checkin," +
				                  "hlf.checkout," +
				                  "hlf.rule," +
				                  "hlf.toplimit," +
				                  "to_char(hlf.actualbegindate, 'yyyy-mm-dd hh24:mi:ss') as  actualbegindate," +
				                  "to_char(hlf.actualenddate, 'yyyy-mm-dd hh24:mi:ss') as  actualenddate," +
				                  "hlf.favtype " +
				                  "from  htl_limit_favourable hlf ,htl_favourable_hotel hfh " +
				                  "where hlf.favid = hfh.favid " +
				                  "and hlf.flag = 1 " +
				                  "and hlf.favisStart=1 " +
				                  "and hfh.htlflag = 1 " +
				                  "and hfh.hotelid in (" + hotelIdList+ ") " +
				                  "and ( hlf.checkin <= ? and hlf.checkout >= ?  or " +
				                  "hlf.checkin <= ? and hlf.checkout >= ? or " +
				                  "hlf.checkin >= ? and hlf.checkout <= ?)";
		Object[] paramValues = new Object[] { beginDate,beginDate,endDate,endDate,beginDate,endDate};
		List<Object[]> list = super.queryByNativeSQL(nativeSql,paramValues);
		if(list==null || list.size()==0) return listHtlLimitFavourableHotel;
		
		for(int i=0;i<list.size();i++){
			Object[] obj=list.get(i);
			HtlLimitFavourableHotel htlLimitFavourableHotel=new HtlLimitFavourableHotel();
			htlLimitFavourableHotel.setHotelId(Long.valueOf(obj[0] + ""));
			htlLimitFavourableHotel.setFavId(Long.valueOf(obj[1] + ""));
			htlLimitFavourableHotel.setFavName(obj[2] + "");
			htlLimitFavourableHotel.setBeginDate((Date) obj[3]);
			htlLimitFavourableHotel.setEndDate((Date) obj[4]);
			htlLimitFavourableHotel.setBeginTime(obj[5] + "");
			htlLimitFavourableHotel.setEndTime(obj[6] + "");
			htlLimitFavourableHotel.setCheckIn((Date) obj[7]);
			htlLimitFavourableHotel.setCheckOut((Date) obj[8]);
			htlLimitFavourableHotel.setRule(Double.valueOf(obj[9] + ""));
			htlLimitFavourableHotel.setTopLimit(Integer.valueOf(obj[10] + ""));
			
			if(null!=obj[11]){
				//活动已经开始
				htlLimitFavourableHotel.setActualBeginDate(DateUtil.stringToDateMain(obj[11]+"", "yyyy-MM-dd HH:mm:ss"));
			}
			if(null!=obj[12]){
				htlLimitFavourableHotel.setActualEndDate(DateUtil.stringToDateMain(obj[12]+"", "yyyy-MM-dd HH:mm:ss"));
			}
			htlLimitFavourableHotel.setFavIsStart(1); //查询条件中已经明确活动已经开始
			htlLimitFavourableHotel.setFlag(1);
			htlLimitFavourableHotel.setFavType(Integer.valueOf(obj[13] + ""));
			
			//判断当前日期是否在活动日期内
			Date favBeginDate=DateUtil.getDateByHour(htlLimitFavourableHotel.getBeginDate(), Integer.valueOf(htlLimitFavourableHotel.getBeginTime()));
			Date favEndDate=DateUtil.getDateByHour(htlLimitFavourableHotel.getEndDate(), Integer.valueOf(htlLimitFavourableHotel.getEndTime()));
			Date currDate=DateUtil.getSystemDate();
			
			//如果当前日期在活动日期内，则此酒店有活动
			if(DateUtil.compare(favBeginDate, currDate) >= 0 && DateUtil.compare(favEndDate, currDate) < 0){
				listHtlLimitFavourableHotel.add(htlLimitFavourableHotel);
			}
		}
		
		return listHtlLimitFavourableHotel;
	}

	private HtlLimitFavourable queryByHotelId(Long hotelId) {
		String nativeSql = "select hlf.favid,hlf.favname,hlf.begindate,hlf.enddate,hlf.begintime,hlf.endtime,hlf.checkin,hlf.checkout,hlf.rule,to_char(hlf.actualbegindate, 'yyyy-mm-dd hh24:mi:ss') as  actualbegindate,to_char(hlf.actualenddate, 'yyyy-mm-dd hh24:mi:ss') as  actualenddate from  htl_limit_favourable hlf ,htl_favourable_hotel hfh where hlf.favid = hfh.favid and hlf.flag = 1 and hlf.favisStart=1 and hfh.htlflag = 1 and hfh.hotelid = ?";
		Object[] paramValues = new Object[] { hotelId };
		List<Object[]> list = super.queryByNativeSQL(nativeSql,0,0, paramValues, null);
		if (list.size() == 0)
			return null;

		HtlLimitFavourable htlLimitFavourable = new HtlLimitFavourable();
		Object[] obj = list.get(0);
		htlLimitFavourable.setFavId(Long.valueOf(obj[0] + ""));
		htlLimitFavourable.setFavName(obj[1] + "");
		htlLimitFavourable.setBeginDate((Date) obj[2]);
		htlLimitFavourable.setEndDate((Date) obj[3]);
		htlLimitFavourable.setBeginTime(obj[4] + "");
		htlLimitFavourable.setEndTime(obj[5] + "");
		htlLimitFavourable.setCheckIn((Date) obj[6]);
		htlLimitFavourable.setCheckOut((Date) obj[7]);
		htlLimitFavourable.setRule(Double.valueOf(obj[8] + ""));
		//DateUtil.stringToDateMain(obj[9]+"", "yyyy-MM-dd HH:mm:ss");
		htlLimitFavourable.setActualBeginDate(DateUtil.stringToDateMain(obj[9]+"", "yyyy-MM-dd HH:mm:ss")); //活动已经开始 实际开始时间一定不会为空
		if(null!=obj[10]){
			htlLimitFavourable.setActualEndDate(DateUtil.stringToDateMain(obj[10]+"", "yyyy-MM-dd HH:mm:ss"));
		}else{
			htlLimitFavourable.setActualEndDate((Date)obj[10]);
		}
		htlLimitFavourable.setFavIsStart(1); //查询条件中已经明确活动已经开始
		return htlLimitFavourable;
	}

	public Boolean judgeLimitFav(Long hotelId, Date checkIn, Date checkOut) {
		boolean flag = false;
		// step1:查询该酒店所属的 活动对象
		HtlLimitFavourable htlLimitFavourable = queryByHotelId(hotelId);

		if (null == htlLimitFavourable)
			return flag;

		// step2:根据step1的对象判断当前时间是否在活动的开始时间、结束时间之中(注意应当使用活动的实际开始时间，活动的实际结束时间来判断)
		Date currDate = DateUtil.getSystemDate();
//		Date in = jointDate(htlLimitFavourable.getBeginDate(), htlLimitFavourable.getBeginTime()); //这里是用设置的时间点来计算的
//		Date out = jointDate(htlLimitFavourable.getEndDate(), htlLimitFavourable.getEndTime());
		Date actualBeginDate = htlLimitFavourable.getActualBeginDate();
		Date actualEndDate = htlLimitFavourable.getActualEndDate();
		if (currDate.before(actualBeginDate) || (null!=actualEndDate&&currDate.after(actualEndDate)))
			return flag; // 当前时间 不在活动时间之内
		
		
		boolean a = htlLimitFavourable.getCheckIn().after(checkOut); // in 在checkOut之后为true
		boolean b = htlLimitFavourable.getCheckIn().equals(checkOut);
		boolean c = htlLimitFavourable.getCheckOut().before(checkIn); // out在checkIn之前为true
		if(!((a||b)||(c))){
			flag = true;
		}
		return flag;
	}

	public BigDecimal queryLimitFavRate(Long hotelId, Date date) {
		double rate = 0;
		// step1:查询该酒店所属的 活动对象
		HtlLimitFavourable htlLimitFavourable = queryByHotelId(hotelId);
		if (null == htlLimitFavourable)
			return new BigDecimal(rate);
		if (DateUtil.between(date, htlLimitFavourable.getCheckIn(), htlLimitFavourable.getCheckOut())) { // 判断日期是否在活动入住日期和活动离店日期之间(包含了入住和离店日期)
				rate = htlLimitFavourable.getRule();
		}
		return  new BigDecimal(rate);
	}



	public void deleteFav(Long favId) {
		
//		String sql = "{call prcDeleteFavourble(?)}";
//		Object[] paramValues = new Object[] { favId };
//		super.execProcedure(sql, paramValues);
		
		HtlLimitFavourable htlLimitFavourable = queryByFavId(favId);
		if(1 != htlLimitFavourable.getFavIsStart()){ //活动开始中不能进行软删除
			String hql = "update HtlLimitFavourable hlf set hlf.flag = 0 where hlf.favId = ?";
			Object[] paramValues = new Object[] { favId };
			//只能够软删除活动，而不能直接将其删除掉
			super.updateByQL(hql, paramValues);
		}
	}

	public HtlLimitFavourable queryLimitFav(Long favId) {
		return (HtlLimitFavourable)super.get(HtlLimitFavourable.class, favId);
	}
	/**
	 * 更新限量返现活动状态
	 * @param favId
	 * @param status
	 */
	public void updateFavActiviyToBeginOrEnd(Long favId,String status){
		HtlLimitFavourable htlLimitFavourable =  queryByFavId(favId);
		if("1".equals(status)){
			htlLimitFavourable.setActualBeginDate(DateUtil.getSystemDate());
			htlLimitFavourable.setFavIsStart(1);
		}else if("2".equals(status)){
			htlLimitFavourable.setActualEndDate(DateUtil.getSystemDate());
			htlLimitFavourable.setFavIsStart(2);
		}
		saveHtlLimitFavourable(htlLimitFavourable);
		//下面的方式 存储的时间 没有时分秒
//		String updateSql = "update HtlLimitFavourable hlf set hlf.favIsStart =? ";
//		
//		if("1".equals(status)){
//			updateSql +=" , hlf.actualBeginDate = ? ";
//		}else if("2".equals(status)){
//			updateSql +=" , hlf.actualEndDate = ? ";
//		}
//		updateSql +=" where hlf.favId = ? ";
//		
//		super.updateByQL(updateSql,new Object[]{Integer.parseInt(status),new java.sql.Timestamp(DateUtil.getSystemDate().getTime()),favId});
	}
	/**
	 * 统计间夜量和返现限量总金额
	 * @param favId
	 * @return
	 */
	public String sumRoomNightAndReturnCash(Long favId){
		String nativeSql=" select sum((v.checkoutdate - v.checkindate) * v.roomquantity) amountofroomnights,"
        +"sum(v.returncash) favourable_returncash"
        +" from (select lf.checkin,"
        +" lf.checkout,"
        +"  lf.actualbegindate,"
        +" lf.actualenddate,"
        +" lf.favisstart,"
        +" o.checkindate,"
        +" o.checkoutdate,"
        +" o.createdate,"
        +" o.ordercd,"
        +" o.confirmtotal,"
        +" o.roomquantity,"
        +" foc.ordercd,"
        +" foc.returncash"
        +" from htl_limit_favourable lf,"
        +"        Htl_favourable_hotel fh,"
        +"        fit_order_cash       foc,"
        +"        or_order             o    "
        +" where lf.favisstart >= 1 "
        +"    and lf.favid = fh.favid "
        +"and lf.favid = ? "
        +"    and fh.hotelid = o.hotelid  "    
        +"    and foc.ordercd = o.ordercd "
        +"    and o.confirmtotal>0 "
        +"     and o.createdate between lf.actualbegindate and nvl(lf.actualenddate,sysdate) "
        +"     and o.checkindate between lf.checkin and lf.checkout "
        +"  ) v";
		Object[] paramValues = new Object[]{favId};
		List<Object[]> list = super.queryByNativeSQL(nativeSql,0,0, paramValues, null);
		if(list.size() ==0){
			return 0+"_"+0;
		}else{
			Object[] obj_RoomNight= list.get(0);
			String num ="";
			if(null != obj_RoomNight[0] ){
				num =obj_RoomNight[0]+"";
			}else{
				num="0";
			}
			if(null!= obj_RoomNight[1]){
				num +="_"+obj_RoomNight[1];
			}else{
				num+="_0";
			}
			return num;
		}
	}
	
	public Boolean isHaveAnotherActivityBegin(Long favId){
		Boolean flag = false;
		String hql ="from HtlLimitFavourable hlf where hlf.favIsStart =1 and hlf.favId=?";
		
		List<HtlLimitFavourable> hlfList = super.query(hql, new Object[]{favId},0,0,false);
		if(hlfList.size()>0){
			flag = true;
		}
		return flag;
	}
	
}

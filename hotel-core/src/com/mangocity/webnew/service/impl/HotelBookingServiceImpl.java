package com.mangocity.webnew.service.impl;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mangocity.hdl.hotel.dto.CheckHotelReservateExResponse;
import com.mangocity.hdl.hotel.dto.CheckPriceTypeResponse;
import com.mangocity.hdl.hotel.dto.CheckReservateExResponse;
import com.mangocity.hdl.hotel.dto.CheckRoomTypeResponse;
import com.mangocity.hdl.hotel.dto.MGExReservItem;
import com.mangocity.hotel.base.dao.ISaleDao;
import com.mangocity.hotel.base.persistence.HtlTaxCharge;
import com.mangocity.hotel.base.persistence.OrReservation;
import com.mangocity.hotel.base.service.assistant.ReservationAssist;
import com.mangocity.hotel.order.dao.impl.OrOrderDao;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.hweb.persistence.QueryHotelForWebSaleItems;
import com.mangocity.util.DateUtil;
import com.mangocity.util.PWDFunction;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.log.MyLog;
import com.mangocity.webnew.persistence.HotelOrderFromBean;
import com.mangocity.webnew.persistence.HtlCalendarColsPriceBean;
import com.mangocity.webnew.persistence.HtlCalendarFav;
import com.mangocity.webnew.persistence.HtlCalendarHelperBean;
import com.mangocity.webnew.persistence.HtlCalendarRowsBean;
import com.mangocity.webnew.persistence.HtlCalendarStyle;
import com.mangocity.webnew.service.HotelBookingService;
import com.mangocity.webnew.util.HotelGetHdlPrice;

/**
 * 下单的service实现类
 * @author zuoshengwei
 *
 */

public class HotelBookingServiceImpl extends DAOHibernateImpl implements HotelBookingService  {
	private static final long serialVersionUID = -8291821534458259942L;
	private static final MyLog log = MyLog.getLogger(HotelBookingServiceImpl.class);
	private HotelManageWeb hotelManageWeb;
	
	
	private ISaleDao saleDao;
	
    /**
     * v2.6 add by wuyun 2009-06-18 16:00 预定条款取消修改记录条数
     */
    private int detailNum;

    /**
     * v2.6 add by wuyun 2009-06-12 21:00 预定条款明细记录条数List<OrGuaranteeItem> guarantees
     */
    private int guaranteeNum;
    
    /**
     * v2.6 add by lixiaoyong 2009-06-15 21:00 预定条款取消修改记录条数
     */
    private int assureNum;
    
    /**
     * 币种符号
     */
    private String idCurStr;
    
    //担保条款、担保取消条款、预付取消条款
    private OrReservation reservation;
    
    // 酒店预订条款取出
    private ReservationAssist reservationAssist;
    
    //订单service
    protected IOrderService orderService;
    
    
    private HotelGetHdlPrice hotelGetHdlPrice;
    
    private OrOrderDao orOrderDao;
	
	/**
	 * 连住优惠，改变相应的售价
	 * @param hotelOrderFromBean
	 * @param priceList
	 * @return
	 * add by shengwei.zuo 2009-11-04
	 */
	public List changeFavPrice(HotelOrderFromBean hotelOrderFromBean,List priceLis) {
		
		// 连住优惠查询价格列表
	    List favPriceLst = new ArrayList<QueryHotelForWebSaleItems>();
		
        //连住优惠改变优惠价格显示 begin
        int dayN = DateUtil.getDay(hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate());
        for(int kk=0;kk<dayN;kk++){
        	Date d = DateUtil.getDate(hotelOrderFromBean.getCheckinDate(), kk);
        	for(int k =0;k<priceLis.size();k++){
        		QueryHotelForWebSaleItems queryHotelForWebSaleItem = (QueryHotelForWebSaleItems)priceLis.get(k);
        		if(d.equals(queryHotelForWebSaleItem.getFellowDate())){
        			favPriceLst.add(queryHotelForWebSaleItem);
        			
        		}
        	}
        }
        
        int f = 0;
    	//连住优惠改变相应售价
    	for(int j = 0 ;j<favPriceLst.size();j++){
    		QueryHotelForWebSaleItems queryHotelForWebSaleItems = (QueryHotelForWebSaleItems)favPriceLst.get(j);
            f = hotelManageWeb.changePriceForFavourableTwo(favPriceLst, queryHotelForWebSaleItems, hotelOrderFromBean.getHotelId(), hotelOrderFromBean.getChildRoomTypeId(),j,f);
        }
        for(int n=0;n<favPriceLst.size();n++){
    		QueryHotelForWebSaleItems FavQueryHotelForWebSaleItems = (QueryHotelForWebSaleItems)favPriceLst.get(n);
    		for(int nn=0;nn<priceLis.size();nn++){
    			QueryHotelForWebSaleItems queryHotelForWebSaleItems = (QueryHotelForWebSaleItems)priceLis.get(nn);
    			if(FavQueryHotelForWebSaleItems.getFellowDate().equals(queryHotelForWebSaleItems.getFellowDate())){
    				priceLis.set(nn,FavQueryHotelForWebSaleItems);
    			}
    		}
    		
    	}
      //连住优惠改变优惠价格显示 end
		
		return priceLis;		
	}
    
    /**
     * 组装URL参数
     * 
     * @param params
     * @return
     */
    public String getUrlPropertyByBean(HotelOrderFromBean hotelOrderFromBean,Map  params,double rate) {
        StringBuffer str = new StringBuffer();
        String priceNum = String.valueOf(hotelOrderFromBean.getPriceNum());
        // 加密方法,对金额采取了加密的方法 add by haibo.li 生产bug
        String priceFun = PWDFunction.encode(priceNum);

        str.append("priceNum=").append(priceFun);

        String checkinDate = DateUtil.dateToString(hotelOrderFromBean.getCheckinDate());
        str.append("&checkinDate=").append(checkinDate);
        String checkoutDate = DateUtil.dateToString(hotelOrderFromBean.getCheckoutDate());
        str.append("&checkoutDate=").append(checkoutDate);

        String hotelId = String.valueOf(hotelOrderFromBean.getHotelId());
        str.append("&hotelId=").append(hotelId);

        String hotelName = hotelOrderFromBean.getHotelName();
        params.put("hotelName", hotelName);

        String childRoomTypeId = String.valueOf(hotelOrderFromBean.getChildRoomTypeId());
        str.append("&childRoomTypeId=").append(childRoomTypeId);

        String childRoomTypeName = hotelOrderFromBean.getChildRoomTypeName();
        params.put("childRoomTypeName", childRoomTypeName);

        String roomTypeId = String.valueOf(hotelOrderFromBean.getRoomTypeId());
        str.append("&roomTypeId=").append(roomTypeId);

        String roomTypeName = hotelOrderFromBean.getRoomTypeName();
        params.put("roomTypeName", roomTypeName);
        
        //所选择的床型；add by shengwei.zuo 2009-10-27
        String bedTypeName = String.valueOf(hotelOrderFromBean.getBedType());
        params.put("bedType", bedTypeName);

        String payMethod = hotelOrderFromBean.getPayMethod();
        str.append("&payMethod=").append(payMethod);
        String cityId = hotelOrderFromBean.getCityId();
        str.append("&cityId=").append(cityId);
        String roomQuantity = (null == hotelOrderFromBean.getRoomQuantity() || hotelOrderFromBean
            .getRoomQuantity().equalsIgnoreCase("null")) ? "1" : hotelOrderFromBean
            .getRoomQuantity();
        str.append("&roomQuantity=").append(roomQuantity);

        String quotaType = hotelOrderFromBean.getQuotaType();
        str.append("&quotaType=").append(quotaType);

        String minPrice = String.valueOf(hotelOrderFromBean.getMinPrice());
        str.append("&minPrice=").append(minPrice);

        String maxPrice = String.valueOf(hotelOrderFromBean.getMaxPrice());
        str.append("&maxPrice=").append(maxPrice);

        str.append("&currency=").append(hotelOrderFromBean.getCurrency());

        str.append("&breakfastType=").append(hotelOrderFromBean.getBreakfastType());

        str.append("&breakfastNum=").append(hotelOrderFromBean.getBreakfastNum());

        str.append("&rate=").append(rate);

        return str.toString();
    }
    
	/**
	 * 订单页面，日历控件显示每一天的价格
	 * @param inDate  入住日期
	 * @param outDate 离店日期
	 * @param year    年份
	 * @param month   月份
	 * @param day     入住天数
	 * @return
	 * add by shengwei.zuo 2009-11-04
	 */
	public HtlCalendarHelperBean getBookCalendarExtender() {
		
		String days[],nextDays[];
		
		days=new String[42]; 
		nextDays =new String[42]; 
		for(int i=0;i<42;i++) 
		{ 
		days[i]=""; 
		nextDays[i]=""; 
		} 
		
		GregorianCalendar currentDay = new GregorianCalendar(); 
		int month=currentDay.get(Calendar.MONTH); 
		int year= currentDay.get(Calendar.YEAR); 
		int day = currentDay.get(Calendar.DAY_OF_MONTH); 

		//String stoday = String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day);

		Calendar thisMonth=Calendar.getInstance(); 
		thisMonth.set(Calendar.MONTH, month ); 
		thisMonth.set(Calendar.YEAR, year ); 
		thisMonth.setFirstDayOfWeek(Calendar.SUNDAY); 
		thisMonth.set(Calendar.DAY_OF_MONTH,1); 


		int firstIndex=thisMonth.get(Calendar.DAY_OF_WEEK)-1; 
		int maxIndex=thisMonth.getActualMaximum(Calendar.DAY_OF_MONTH); 
		for(int i=0;i<maxIndex;i++) 
		{ 
		days[firstIndex+i]=String.valueOf(i+1); 
		} 

		thisMonth.add(Calendar.MONTH, 1);
		int nextFirstIndex=thisMonth.get(Calendar.DAY_OF_WEEK)-1; 
		int nextMaxIndex=thisMonth.getActualMaximum(Calendar.DAY_OF_MONTH); 
		for(int i=0;i<nextMaxIndex;i++) 
		{ 
		nextDays[nextFirstIndex+i]=String.valueOf(i+1); 
		} 
		int nextMonth=thisMonth.get(Calendar.MONTH); 
		int nextYear= thisMonth.get(Calendar.YEAR); 
		
		HtlCalendarHelperBean   caHelperBean = new  HtlCalendarHelperBean();
		
		//日历控件中，第一个年，月，日期的数组
		caHelperBean.setCadays(days);
		caHelperBean.setCaMonth(month+1);
		caHelperBean.setCaYear(year);
		
		//日历控件中，第二个年，月，日期的数组
		caHelperBean.setCaNextDays(nextDays);
		caHelperBean.setCaNextMonth(nextMonth+1);
		caHelperBean.setCaNextYear(nextYear);
		
		return caHelperBean;
		
	} 
    
	/**
	 * 组装css 和价格
	 * @param inDate
	 * @param outDate
	 * @param nextYear
	 * @param nextMonth
	 * @param nextDays
	 * @return
	 * add by shengwei.zuo 2009-11-06
	 */
	public  List  getCalendarPrice(String calendarStyle, HtlCalendarHelperBean calendarHelperEnty){
		
		String tdclass = "huise";
		
		List <HtlCalendarRowsBean> calendarRowsLst =  new ArrayList <HtlCalendarRowsBean>();
		
		List <HtlCalendarColsPriceBean> calendarColsPriceLst =  new ArrayList<HtlCalendarColsPriceBean>();
		
	    int  nextYear = 2008;
	    int  nextMonth = 8;
	    String [] nextDays = null;
	    
	    if(calendarStyle.equals(HtlCalendarStyle.Calendar_LEFT)){
	    	
	    	nextYear  = calendarHelperEnty.getCaYear();
	    	nextMonth =	calendarHelperEnty.getCaMonth();
	    	nextDays  = calendarHelperEnty.getCadays();	
	    	
	    }else if(calendarStyle.equals(HtlCalendarStyle.Calendar_RIGHT)){
	    	
	    	nextYear  = calendarHelperEnty.getCaNextYear();
	    	nextMonth =	calendarHelperEnty.getCaNextMonth();
	    	nextDays  = calendarHelperEnty.getCaNextDays();	
	    	
	    }
	    
	    //有连住优惠的List 
	    List<HtlCalendarFav>  lstCalendarFav  = calendarHelperEnty.getLstFavChgPri();
		
		for(int j=0;j<6;j++){ 
			
			for(int i=j*7;i<(j+1)*7;i++){ 
			 
			  HtlCalendarColsPriceBean 	calendarColsPriceLstObj = new  HtlCalendarColsPriceBean();	 
					
			  tdclass = getTdclass(calendarHelperEnty.getInDateStr(),calendarHelperEnty.getOutDateStr(),nextYear,nextMonth,nextDays[i]);
			  
			  if(tdclass!="huise"&&!"huise".equals(tdclass)){
				  
				  String currDateStr = nextYear+"-"+nextMonth+"-"+nextDays[i];
				  
				  Date currDate = DateUtil.getDate(currDateStr);
				  
				  // 增加日期 add by xuyiwen 2010-12-10 begin
				  calendarColsPriceLstObj.setCurrDate(currDate);
				  
				  List favCalendarLst = getCalendarList(calendarHelperEnty.getHotelId(),calendarHelperEnty.getRoomTypeId(),
						  calendarHelperEnty.getChildRoomTypeId(),currDate,calendarHelperEnty.getPayMethod());
				  
				  for (Iterator iterator = favCalendarLst.iterator(); iterator.hasNext();) {
					  	
					    List myFavCalendarList = (List) iterator.next();
					  
	        	 		//售价
					    calendarColsPriceLstObj.setDayPrice((Double)myFavCalendarList.get(0));
	        	 		
	        	 		//含早数量
					    calendarColsPriceLstObj.setDayBreakfastNumber((String)myFavCalendarList.get(2));
	        	 		
	        	 		//是否有免费宽带
					    calendarColsPriceLstObj.setDayFreeNet((String)myFavCalendarList.get(3));
					  
				  }
				  
				  for(int  k=0;k<lstCalendarFav.size();k++){
					  
					  //有连住优惠的日期和售价list add by shengwei.zuo
					  HtlCalendarFav  favChgPriceObj  = new HtlCalendarFav();
					  favChgPriceObj = lstCalendarFav.get(k);
					  
					  Date favPriceDate = favChgPriceObj.getFavDate();
					  int aDate= DateUtil.compare(favPriceDate,currDate);
					  //指定日期有连住优惠
					  if(aDate==0){
						  calendarColsPriceLstObj.setDayPrice(Math.floor(favChgPriceObj.getFavPrice()));
						  break;
						  
					  }
				  }
			  }
			
			  String edayPriceStr = getChangePriceStyleOne(calendarColsPriceLstObj.getDayPrice());
			  calendarColsPriceLstObj.setDayPriceStr(edayPriceStr);
			  
			  calendarColsPriceLstObj.setDaysCss(tdclass);
			  calendarColsPriceLstObj.setDaysId(i);
			  calendarColsPriceLstObj.setDayIndex(nextDays[i]);
			  
			  calendarColsPriceLst.add(calendarColsPriceLstObj);
			
			}
			
			HtlCalendarRowsBean calendarRowsObj = new  HtlCalendarRowsBean();
			calendarRowsObj.setRowNumber(j);
			calendarRowsObj.setLstColsPrice(calendarColsPriceLst);
			
			calendarRowsLst.add(calendarRowsObj);
			
		}
		
		
		return calendarRowsLst;
	}
	
	
    /**
     * 获得通过连住优惠变价后的list 
     * @param queryFromBean
     * @return
     * add by shengwei.zuo 2009-11-13
     */
	public List getFavChgPrice(List queryFromBean) {
		
		 List<HtlCalendarFav>  lstFavChgPrice =  new ArrayList<HtlCalendarFav>();
		  for(int m=0;m<queryFromBean.size();m++){
	        	
	        	QueryHotelForWebSaleItems  forWebSaleItems = (QueryHotelForWebSaleItems) queryFromBean.get(m);
	        	
	        	if(forWebSaleItems.isChgSalePri()){
	        		
	        		HtlCalendarFav  calendarFavObj  = new  HtlCalendarFav();
	        		
	        		calendarFavObj.setFavDate(forWebSaleItems.getFellowDate());
	        		calendarFavObj.setFavPrice(forWebSaleItems.getSalePrice());
	        		
	        		lstFavChgPrice.add(calendarFavObj);
	        	}
	        	
	        }
		
		return lstFavChgPrice;
	}
	
	  
	/**
	 * 订单页面，日历控件table中 td的创建
	 * @param inDate  入住日期
	 * @param outDate 离店日期
	 * @param year    年份
	 * @param month   月份
	 * @param day     入住天数
	 * @return
	 * add by shengwei.zuo 2009-11-04
	 */
	public String getTdclass(String inDate,String outDate,int year,int month,String day) { 
		
		String tdclass="huise";
		
		if(day.equals(""))return tdclass;

		GregorianCalendar currentDay = new GregorianCalendar(); 
		int m=currentDay.get(Calendar.MONTH); 
		int y= currentDay.get(Calendar.YEAR); 
		int d = currentDay.get(Calendar.DAY_OF_MONTH); 

		String stoday = String.valueOf(y)+"-"+String.valueOf(m+1)+"-"+String.valueOf(d);

		String sdisday = String.valueOf(year)+"-"+String.valueOf(month)+"-"+day;

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date today = null;
		Date disday = null;
		Date date1 = null;
		Date date2 = null;
		try {    
		today = format.parse(stoday);
		disday = format.parse(sdisday);
		date1 = format.parse(inDate);
		date2 = format.parse(outDate);
		} catch (ParseException e) {
			log.error(e);
		} 
		if(today.equals(disday)){
		   if(today.equals(date1))return "huangse";
		   else return "lvse";
		}
		if(disday.before(today))return "huise";

		if(disday.before(date1)){
		 tdclass="lvse";
		}else if(disday.after(date2)||disday.equals(date2)){
		 tdclass="lvse";
		}else{
		 tdclass="huangse";
		}
		
		return tdclass;
		
	}
	
	
	public List  getCalendarList(long hotelId, long roomTypeId, long childRoomTypeId,
	       Date ableSaleDate, String payMethod){
		
	 String hsql = " select new list ( p.salePrice,                                                   \n"+   
					"       p.currency,                                                     \n"+
					"       p.incBreakfastNumber,                                         \n"+
					"       (case                                                           \n"+
					"         when exists (select roomTypeId                              \n"+
					"                 from HtlInternet                                     \n"+
					"                where roomTypeId = ?                           \n"+
					"                  and p.ableSaleDate between internetBeginDate and internetEndDate \n"+
					"                  and htlContract is not null) then                    \n"+
					"          '1'                                                          \n"+
					"         else                                                          \n"+
					"          (case                                                        \n"+
					"         when exists (select ID                              \n"+
					"                 from HtlRoomtype                                     \n"+
					"                where ID = ?                           \n"+
					"                  and roomEquipment like '%21%') then                 \n"+
					"          '2'                                                          \n"+
					"         else                                                          \n"+
					"          '3'                                                          \n"+
					"       end) end) )                                                       \n"+
					"  FROM  HtlPrice p                                         \n"+
					"  where                                    \n"+
					"   p.ableSaleDate = ?          \n"+
					"   and p.hotelId = ?                                       \n"+
					"   and p.childRoomTypeId = ?                                \n"+
					"   and p.payMethod = ?  ";
		
        Object[] obj = new Object[] {roomTypeId, roomTypeId,ableSaleDate,hotelId, childRoomTypeId,payMethod };
        
        return super.query(hsql, obj);
		
		
	}
	
    
	public HotelManageWeb getHotelManageWeb() {
		return hotelManageWeb;
	}


	public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
		this.hotelManageWeb = hotelManageWeb;
	}


	public int getDetailNum() {
		return detailNum;
	}


	public void setDetailNum(int detailNum) {
		this.detailNum = detailNum;
	}


	public OrReservation getReservation() {
		return reservation;
	}


	public void setReservation(OrReservation reservation) {
		this.reservation = reservation;
	}


	public int getGuaranteeNum() {
		return guaranteeNum;
	}


	public void setGuaranteeNum(int guaranteeNum) {
		this.guaranteeNum = guaranteeNum;
	}


	public int getAssureNum() {
		return assureNum;
	}


	public void setAssureNum(int assureNum) {
		this.assureNum = assureNum;
	}


	public String getIdCurStr() {
		return idCurStr;
	}


	public void setIdCurStr(String idCurStr) {
		this.idCurStr = idCurStr;
	}


	public ReservationAssist getReservationAssist() {
		return reservationAssist;
	}


	public void setReservationAssist(ReservationAssist reservationAssist) {
		this.reservationAssist = reservationAssist;
	}


	public IOrderService getOrderService() {
		return orderService;
	}


	public void setOrderService(IOrderService orderService) {
		this.orderService = orderService;
	}

	public ISaleDao getSaleDao() {
		return saleDao;
	}

	public void setSaleDao(ISaleDao saleDao) {
		this.saleDao = saleDao;
	}
	
	 /**
     * 根据合同ID，入住日期，离店日期查询出 房费税费设定的记录  add by shengwei.zuo 2009-12-1
     */
	public HtlTaxCharge getHaveTaxCharge(Long contractId, Date beginDate, Date endDate) {
		
		   String sql = " select  a  from HtlTaxCharge a   where "
	            + " a.contractId = ? and a.roomIncTax = 1 "
	            + "and a.taxBeginDate <=? and a.taxEndDate >= ? ";

	        Object[] obj = new Object[] { contractId, beginDate, endDate };

	        HtlTaxCharge taxCharge = (HtlTaxCharge) super.find(sql, obj);
	        
	        return  taxCharge;
	        
	}
	
	/**
     * 根据订单ID,更新支付相关表中的流水号
     * @param orderId
    */
	public int updatePayment(final long orderId,final String seriNo) {
		
		 HibernateCallback cb = new HibernateCallback() {
	            public Object doInHibernate(Session session) throws HibernateException, SQLException {
	                String Hql = " update OrPayment c set c.prepayBillNo = ? \n"+
	                    		 " where c.order = ?";
	                Query query = session.createQuery(Hql);
	                query.setString(0, seriNo);
	                query.setLong(1, orderId);
	                query.executeUpdate();
	                return 0;
	            }
	        };

	        Integer ret = (Integer) getHibernateTemplate().execute(cb);
	        if (null == ret) {
	            return -1;
	        } else {
	            return ret.intValue();
	        }

		
	}
	
	 /**
     * 根据订单ID,更新支付相关表中的流水号,繁体网站
     * @param orderId
     */
	public boolean updatePaymentForBig5(long orderId, String seriNo, long paymentId) {
		 StringBuffer sqlStrBuf = new StringBuffer();
		 sqlStrBuf.append("update OrPayment p     ");
		 sqlStrBuf.append("   set p.prepayBillNo = ? ");
		 sqlStrBuf.append(" where p.ID =  ?    ");
		 sqlStrBuf.append("   and p.order = ? ");
		 int count = orOrderDao.updateByQL(sqlStrBuf.toString(), 
				 new Object[]{seriNo, Long.valueOf(paymentId), Long.valueOf(orderId)});
		 
		 return count == 0?false : true;
		
	}
	
	
    /**
     * 价格取整或者特殊价格的转换 add by shengwei.zuo 2009-12-22
     */
    public String getChangePriceStyleOne(double price) {
        try {
            if (0 == Double.compare(Double.parseDouble("999999"), price) || 0 == Double.compare(Double.parseDouble("999999.0"), price) ||
            	 0 == Double.compare(Double.parseDouble("2"), price) || 0 == Double.compare(Double.parseDouble("2.0"), price)) {
                return "***";
            }else if(0 == Double.compare(Double.parseDouble("99999.0"), price) || 0 == Double.compare(Double.parseDouble("99999"), price)){
            	return "免费";
            }else if(0 == Double.compare(Double.parseDouble("888888"), price)||0 == Double.compare(Double.parseDouble("888888.0"), price)){
            	return "×";
            }else if (0 != Double.compare(price, 0.0)) {
            	
	              //逢一进十 add by shengwei.zuo 2009-12-22
	  			  Double  everyDaPrice = Math.ceil(price);
	  			  //价格取整
	  			  Double  everyFoolrPrice = Math.floor(price);
	  			  
	  			  String dayPriceStr =  String.valueOf(price);
	  			  
	  			  if(everyDaPrice.intValue()>0){
	  				  if(everyDaPrice.intValue()> everyFoolrPrice.intValue()){
	  					   dayPriceStr = String.valueOf(price);
	  				  }else{
	  					   dayPriceStr = String.valueOf(everyFoolrPrice.intValue());
	  				  }
	  			  }
	  			  
	              return dayPriceStr;
            } else if (0 == Double.compare(price, 0.0)) {
                return "";
            }
            return "";
        } catch (Exception e) {
        	log.error(e);
            return "";
        }

    }
    
    /**
     * 刷畅联的价格 add by shengwei.zuo 2009-12-22
     */
    public String  reReshReservateExResponse(long hotelId,long roomTypeId,String childRoomTypeId,int roomChannel,Date inDate,Date outDate)throws Exception{
  
    	
    	
    	String  hdlPriceStr="";
    	
    	//这里还要加判断房型是否激活
	  	CheckReservateExResponse res = hotelGetHdlPrice.getPrice(hotelId, roomTypeId, childRoomTypeId, roomChannel,
			     			inDate,outDate, 1);
	  	  if(res!=null){
	  		if(res.getResult()>0){	
	  				int k = res.getReservItems().size();
	  				//预定天数
	  				int days = DateUtil.getDay(inDate,outDate);
	  				if(k>=days){
	  					float hdlPirceFlo = 0f;
		          		for(int j=0;j<k;j++){
		          			MGExReservItem rItem = res.getReservItems().get(j);
		          			hdlPirceFlo += rItem.getSalePrice();
		          		}
		          		hdlPriceStr = String.valueOf(hdlPirceFlo);
	  				}else{
	  					hdlPriceStr = "hdlRefreshPriceFail"; 
	  					 log.info(" k < days  "+ res.getReason());
	  				}

	         }else if(res.getResult()<1){//没有刷到价格
	        	 
	        	 hdlPriceStr = "hdlRefreshPriceFail"; 
	        	 log.info(" res.getResult()<1  "+ res.getReason());
	         } 
	  	  }else{
	  		hdlPriceStr = "hdlRefreshPriceFail"; 
	  		log.info("CheckReservateExResponse res is null " ) ;
	  		
	  	  }
	  	
	    StringBuilder bookInfo = new StringBuilder();
		bookInfo.append("hotelId:");
		bookInfo.append(String.valueOf(hotelId));
		bookInfo.append(" roomTypeId:");
		bookInfo.append(String.valueOf(roomTypeId));
		bookInfo.append(" childRoomTypeId:");
		bookInfo.append(childRoomTypeId);
		bookInfo.append("roomChannel : ");
		bookInfo.append(roomChannel);
		bookInfo.append("  hdlPriceStr :");
		bookInfo.append(hdlPriceStr);		
		log.info("reReshReservateExResponse "+bookInfo.toString());
		
		
	  	  
	  	return hdlPriceStr;
    	
    }
    
    /**
     * 刷中旅的价格
     */
    public List<QueryHotelForWebSaleItems>  refreshHotelReservateExResponse(long hotelId,long roomTypeId,Long childRoomTypeId,int channelType,Date inDate,Date outDate)throws Exception{
    	
    	CheckHotelReservateExResponse res=hotelGetHdlPrice.checkHotelReservate(channelType,hotelId,roomTypeId,
			childRoomTypeId,inDate,outDate,1);
    	
    	// 需要对返回对象进行重新组装以便适合现有网站代码的处理
        List<QueryHotelForWebSaleItems> itemList = new ArrayList<QueryHotelForWebSaleItems>();
        
    	if(res!=null){
	  		if(res.getResult().getValue()>=0){
	  			
	  			for(CheckRoomTypeResponse roomTypeRes:res.getRoomTypes()){
	  				//如果房型无房，则不可订，直接返回
	  				if(roomTypeRes.getResult()==null||roomTypeRes.getResult().getValue()<0)return null;
	  			}
	  			
	  			for(CheckPriceTypeResponse priceTypeRes:res.getPriceTypeResponse()){
	  			    //如果酒店无价格的时候不可定，直接返回
	  				if(priceTypeRes.getResult()==null||priceTypeRes.getResult().getValue()<0)return null;
	  				
	  				String rRoomTypeId=priceTypeRes.getRoomTypeId();
	  				String rChildRoomTypeId=priceTypeRes.getChildRoomTypeId();
	  				
	  				if(roomTypeId==Long.parseLong(rRoomTypeId)&&childRoomTypeId==Long.parseLong(rChildRoomTypeId)){
	  					
	  					int k = priceTypeRes.getReservItems().size();
		  				//预定天数
		  				int days = DateUtil.getDay(inDate,outDate);
		  				if(k>=days){
			          		for(int j=0;j<k;j++){
			          			MGExReservItem rItem = priceTypeRes.getReservItems().get(j);
			          			
			          			QueryHotelForWebSaleItems item = new QueryHotelForWebSaleItems();
			                    item.setFellowDate(inDate);
			                    item.setPriceId(Long.parseLong(rChildRoomTypeId));
			                    item.setWeekDay(rItem.getDayIndex());
			                    // //销售价
			                    item.setSalePrice(rItem.getSalePrice());
			                    // //低价
			                    item.setBasePrice(rItem.getBasePrice());
			                    // 测试网上银行交易
			                    // item.setSalePrice(1d);
			                    // 低价
			                    // item.setBasePrice(1d);
			                    itemList.add(item);
			          		}
		  				}
	  				}
	  			}
	  		}
    	}
    	return itemList;
    }

	public HotelGetHdlPrice getHotelGetHdlPrice() {
		return hotelGetHdlPrice;
	}

	public void setHotelGetHdlPrice(HotelGetHdlPrice hotelGetHdlPrice) {
		this.hotelGetHdlPrice = hotelGetHdlPrice;
	}

	public void setOrOrderDao(OrOrderDao orOrderDao) {
		this.orOrderDao = orOrderDao;
	}

}

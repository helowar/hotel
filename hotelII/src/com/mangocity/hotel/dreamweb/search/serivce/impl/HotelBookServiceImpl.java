package com.mangocity.hotel.dreamweb.search.serivce.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hdl.constant.ChannelType;
import com.mangocity.hotel.base.dao.HtlRoomtypeDao;
import com.mangocity.hotel.base.manage.ContractManage;
import com.mangocity.hotel.base.manage.HtlLimitFavourableManage;
import com.mangocity.hotel.base.persistence.ExMapping;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.persistence.HtlTaxCharge;
import com.mangocity.hotel.base.persistence.OrReservation;
import com.mangocity.hotel.base.service.IHotelFavourableReturnService;
import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.hotel.base.service.assistant.ReservationAssist;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hotel.dreamweb.search.dao.HotelBookDao;
import com.mangocity.hotel.dreamweb.search.service.HotelBookService;
import com.mangocity.hotel.order.constant.LocalFlag;
import com.mangocity.hotel.search.model.QueryCommodityInfo;
import com.mangocity.hotel.search.service.assistant.SaleInfo;
import com.mangocity.hotel.search.util.PriceUtil;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.bean.MustDate;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.webnew.persistence.HotelOrderFromBean;
import com.mangocity.webnew.service.HotelBookingService;

public class HotelBookServiceImpl implements HotelBookService {

	//注入service
	private HotelManageWeb hotelManageWeb;
	private HotelBookDao hotelBookDao;
	private HtlRoomtypeDao roomtypeDao;
	private ContractManage contractManage;
	private IHotelService hotelService;
	//旧的booking服务
	private HotelBookingService hotelBookingService;
	
    private static Date DATE_1970_01_01 = DateUtil.stringToDateMain("1970-01-01", "yyyy-MM-dd");
    private static Date DATE_2099_01_01 = DateUtil.stringToDateMain("2099-01-01", "yyyy-MM-dd");
	
	/**
     * 现金返还服务接口
     */
    private IHotelFavourableReturnService returnService;
    
    //计算限量返现
	private HtlLimitFavourableManage limitFavourableManage;
	

	/**
	 * 根据priceTypeId得到hotelId
	 */
	public Long getHotelId(Long priceTypeId){
		return hotelBookDao.getHotelId(priceTypeId);
	}
	
	/**
	 * 根据priceTypeId得到roomTypeId
	 */
	public Long getRoomTypeId(Long priceTypeId){
		return hotelBookDao.getRoomTypeId(priceTypeId);
	}
	
	
	/**
	 * 根据priceTypeId查询酒店基本信息
	 */
	public HtlHotel queryHtlHotelInfo(Long priceTypeId){
 	    Long hotelId = getHotelId(priceTypeId);
        return hotelManageWeb.findHotel(hotelId);			
	}
	
	/**
	 * 根据priceTypeId查询房型的基本信息
	 */
	public HtlRoomtype queryHtlRoomtypeInfo(Long priceTypeId){
		Long roomTypeId =  getRoomTypeId(priceTypeId);
		return roomtypeDao.qryHtlRoomTypeByRoomTypeId(roomTypeId);
	}
	
	/**
	 * 
	 * 能否满足条款(针对每天每个商品) 1、预订时间在要求的时间范围内 2、入住天数必须大于连住小于限住
	 * 
	 * @param queryInfo
	 * @param checkinDate
	 * @param checkoutDate
	 * @return
	 */
	public boolean satisfyClauseForPerday(QueryCommodityInfo queryInfo, Date checkinDate,Date checkoutDate){
		String sDate="",eDate="";
		String stime="",etime="";		
		
		sDate = DateUtil.dateToString(queryInfo.getBookstartdate());//开始日期
		eDate = DateUtil.dateToString(queryInfo.getBookenddate());//结束日期
		stime = queryInfo.getMorningtime();//开始时间
		etime = queryInfo.getEveningtime();//结束时间
		
		if(null==sDate||"".equals(sDate)) {
			sDate="1900-01-01";
		}
		if(null==eDate||"".equals(eDate)) {
			eDate="2099-12-31";
		}
		
		if(null==stime||"".equals(stime))stime="00:00";
		if(null==etime||"".equals(etime))etime="23:59";
		
		Date startDate = DateUtil.stringToDateMinute(sDate+" "+stime);//开始日期时间
		Date endDate = DateUtil.stringToDateMinute(eDate+" "+etime);//结束日期时间
			
		Date curDate = DateUtil.getSystemDate();//当前日期									
		
		
		boolean flag = true;
		/**
		 * 如果当前日期在要求的时间区间内
		 * 并且满足连住和限住条件就return true
		 */
		StringBuilder notSatisfyStr = new StringBuilder("");
		if (curDate.before(startDate) || curDate.after(endDate)) { // 不在时间区间内
			flag = false;
			notSatisfyStr.append(" 必须在");
			if (DATE_1970_01_01.after(startDate)) {
				notSatisfyStr.append(DateUtil.datetimeToString(endDate))
						.append("之前预订。");
			} else {
				notSatisfyStr.append(DateUtil.datetimeToString(startDate));
				if (DATE_2099_01_01.before(endDate)) {
					notSatisfyStr.append("之后预订。");
				} else {
					notSatisfyStr.append("和").append(
							DateUtil.datetimeToString(endDate)).append("之间预订。");
				}
			}
		}
		
		if(flag) {
			long bookDays = DateUtil.getDay(checkinDate, checkoutDate);		
			long continueDays = null == queryInfo.getContinueDay() ? 0 : queryInfo
					.getContinueDay().longValue();// 连住
			if (bookDays < continueDays) {
				notSatisfyStr.append(" 必须连住").append(continueDays)
						.append("晚(含").append(continueDays).append("晚)");
				flag = false;
			}
			
			if(flag) {
				Long restrictInDays = queryInfo.getRestrictIn();// 限住
				if (null != restrictInDays && 0 < restrictInDays.longValue()) {
					if (bookDays != restrictInDays.longValue()) {
						notSatisfyStr.append("  限住" + restrictInDays + "晚");
						flag = false;
					}
				}
				
				if(flag) {
					// 必住日期判断
					String mustIn = queryInfo.getMustIn();
					if (StringUtil.isValidStr(mustIn)) {
						if (!checkMustInDate(queryInfo, notSatisfyStr, checkinDate,
								checkoutDate, 0 >= notSatisfyStr.length())) {
							flag = false;
						}
					}						
				}
			}	
		}				
		
		queryInfo.setCantbookReason(notSatisfyStr.toString());
		
		return flag;
	}
	
    /**
     * 
     * 对必住日期进行逻辑判断的方法
     * 
     * @param queryInfo
     * @param bookHintNoMeet
     * @param checkInDate
     * @param checkOutDate
     * @param bFirst
     * @return
     */
    private boolean checkMustInDate(QueryCommodityInfo queryInfo,
        StringBuilder bookHintNoMeet, Date checkInDate, Date checkOutDate, boolean bFirst) {

        List<MustDate> mustInDates = new ArrayList<MustDate>(2);
        int type = MustDate.getMustIndatesAndType(mustInDates, queryInfo.getMustIn());
        boolean isCanLive = false;
        StringBuilder noMeet = new StringBuilder();
        String mustDatesRelation = queryInfo.getContinueDatesRelation();
        if (!StringUtil.isValidStr(mustDatesRelation) || mustDatesRelation.equals("or")) {// 里边为 或者
                                                                                          // 逻辑判断
            // 得到必住日期集合
            if (type == MustDate.DATE_TYPE) {// 必住日期逻辑
                for (MustDate date : mustInDates) {
                    // //如果入住日期包括任意一个必住日期即可入住
                    if (DateUtil.isBetween(date.getContinueDate(), checkInDate, checkOutDate)) {
                        isCanLive = true;
                        break;
                    }
                    noMeet.append(DateUtil.dateToString(date.getContinueDate())).append(",");
                }
                if (!isCanLive) {
                    if (bFirst) {
                        bookHintNoMeet.append("入住此房型，住店日期需至少包含");
                    } else {
                        bookHintNoMeet.append("并且住店日期需至少包含");
                    }
                    noMeet.deleteCharAt(noMeet.length() - 1);
                    bookHintNoMeet.append(noMeet.toString());
                    if (bFirst) {
                        bookHintNoMeet.append("中任意一天方可接受预订。");
                    } else {
                        bookHintNoMeet.append("中任意一天。");
                    }
                }
            } else if (type == MustDate.WEEK_TYPE) {// 必住星期逻辑
                for (MustDate date : mustInDates) {
                    if (DateUtil.isBetween(checkInDate, date.getContinueDate(), date
                        .getContinueEndDate())) {
                        String[] checkInWeeks = date.getWeeks().split(",");
                        Date[] checkInDates = DateUtil.getDateWithWeek(checkInDate,
                        		checkOutDate, checkInWeeks);
                        if (0 < checkInDates.length) {// 说明入住日期内已经至少包含有一个必住星期
                            isCanLive = true;
                        } else {
                            noMeet.append("从")
                                .append(DateUtil.dateToString(date.getContinueDate())).append("至")
                                .append(DateUtil.dateToString(date.getContinueEndDate())).append(
                                    "期间的星期").append(date.getWeeks());
                        }
                        break;
                    }
                }
                // 如果不能入住,则显示提示信息
                if (!isCanLive) {
                    if (bFirst) {
                        bookHintNoMeet.append("入住此房型，住店日期需至少包含");
                    } else {
                        bookHintNoMeet.append("并且住店日期需至少包含");
                    }
                    bookHintNoMeet.append(noMeet.toString());
                    if (bFirst) {
                        bookHintNoMeet.append("中任意一天方可接受预订。");
                    } else {
                        bookHintNoMeet.append("中任意一天。");
                    }
                }
            }
        } else {// 里边为 并且 逻辑判断
            if (type == MustDate.DATE_TYPE) {// 必住日期逻辑
                if (!DateUtil.isBetween(queryInfo.getContinuumInStart(), checkInDate, checkOutDate)
                    || !DateUtil.isBetween(queryInfo.getContinuumInEnd(), checkInDate, checkOutDate)) {
                    // 不能入住
                    if (bFirst) {
                        bookHintNoMeet.append("入住此房型，住店日期需包含");
                    } else {
                        bookHintNoMeet.append("并且住店日期需包含");
                    }
                    for (MustDate date : mustInDates)
                        noMeet.append(DateUtil.dateToString(date.getContinueDate())).append(",");
                    noMeet.deleteCharAt(noMeet.length() - 1);
                    bookHintNoMeet.append(noMeet.toString());
                    if (bFirst) {
                        bookHintNoMeet.append("方可接受预订。");
                    } else {
                        bookHintNoMeet.append("。");
                    }
                } else
                    isCanLive = true;// 可以入住
            } else if (type == MustDate.WEEK_TYPE) {// 必住星期逻辑
                for (MustDate date : mustInDates) {
                    if (DateUtil.isBetween(checkInDate, date.getContinueDate(), date
                        .getContinueEndDate())) {
                        String[] checkInWeeks = date.getWeeks().split(",");
                        Date[] checkInDates = DateUtil.getDateWithWeek(checkInDate,
                        		checkOutDate, checkInWeeks);
                        if (checkInDates.length >= checkInWeeks.length) {// 说明入住日期内已经至少包含有一个整体的必住星期
                            isCanLive = true;
                        } else {
                            noMeet.append("从")
                                .append(DateUtil.dateToString(date.getContinueDate())).append("至")
                                .append(DateUtil.dateToString(date.getContinueEndDate())).append(
                                    "期间的逢星期").append(date.getWeeks());
                        }
                        break;
                    }
                }
                // 如果不能入住,则显示提示信息
                if (!isCanLive) {
                    if (bFirst) {
                        bookHintNoMeet.append("入住此房型，住店日期需包含");
                    } else {
                        bookHintNoMeet.append("并且住店日期需包含");
                    }
                    bookHintNoMeet.append(noMeet.toString());
                    if (bFirst) {
                        bookHintNoMeet.append("方可接受预订。");
                    } else {
                        bookHintNoMeet.append("。");
                    }
                }
            }
        }
        return isCanLive;
    }
	
	/**
	 * 获取商品信息<br>
	 *  
	 目前返回以下信息：<br>
	  
		roomTypeId=32904,      房型Id<br>
	    roomTypeName=海景预付, 房型名称<br>	
		childRoomTypeId=34053,  价格类型Id<br>
		childRoomTypeName=标准, 价格类型名称<br>				
		breakfastType=3,     早餐类型<br>
		breakfastNum=0,      早餐数<br>
		bedTypeStr=2,        可预订的床型(如"1,2")<br>
		currency=HKD,        币种(如"RMB")<br>
		canbook=null,		 是否可预订 ("1"为可预订)<br>
		payToPrepay=false,   面转预(请参看QueryCommodityInfo类的该成员说明)<br>		
		returnAmount=25,        返现金额<br>  
	 */
    public QueryCommodityInfo queryCommidity(Long priceTypeId, String payMethod,
			Date inDate, Date outDate, boolean forCts) {    	
    	List<Object[]> liRes = hotelBookDao.queryCommidity(priceTypeId,
				payMethod, inDate, outDate, forCts);
		if (null != liRes && !liRes.isEmpty()) {
			
			boolean bCanBook = true;
			String bed = "";
			
			int totalDaysHavePrice = 0;
			int totalDays = DateUtil.getDay(inDate, outDate);
			Map<Integer, String> dateMap = new HashMap<Integer, String>(totalDays);
			
			QueryCommodityInfo comm = new QueryCommodityInfo();
			boolean bFirst = true;
			boolean beds[] = {true, true, true};
			int cashReturnAmount = 0;
			int nPayMethod = PayMethod.PAY.equals(payMethod) ? 1 : 2;
			
			for(Object[] obj : liRes) {
				
				if(bFirst) {
					comm.setRoomtypeId(Long.valueOf(obj[0].toString()));
					comm.setRoomtypeName(obj[1].toString());
					comm.setCommodityId(priceTypeId);
					comm.setCommodityName(obj[3].toString());
					comm.setBreakfasttype(obj[5]==null?0:Long.valueOf(obj[5].toString()));
					comm.setBreakfastnumber(obj[6]==null?0:Long.valueOf(obj[6].toString()));
					comm.setCurrency(obj[9].toString());
					comm.setPaymethod(payMethod);
					comm.setPaytoprepay(obj[10]==null? "" :obj[10].toString());
					comm.setHotelId(Long.valueOf(obj[15].toString()));		
					
					if(null != obj[26]) {
						comm.setBookstartdate((Date)obj[26]);	
					}
					if(null != obj[17]) {
						comm.setBookenddate((Date)obj[17]);	
					}
					if(null != obj[27]) {
						comm.setMorningtime(obj[27].toString());	
					}
					if(null != obj[18]) {
						comm.setEveningtime(obj[18].toString());	
					}
					if(null != obj[19]) {
						comm.setContinuumInEnd((Date)obj[19]);	
					}
					if(null != obj[20]) {
						comm.setContinuumInStart((Date)obj[20]);	
					}
					if(null != obj[21]) {
						comm.setContinueDay(Long.valueOf(obj[21].toString()));	
					}
					if(null != obj[22]) {
						comm.setMustIn(obj[22].toString());	
					}
					if(null != obj[24]) {
						comm.setRestrictIn(Long.valueOf(obj[24].toString()));	
					}
					if(null != obj[25]) {
						comm.setContinueDatesRelation(obj[25].toString());	
					}
					
					bFirst = false;
				}				
				
				Date ableDate = (Date)obj[4];
				int dayIndex = DateUtil.getDay(inDate, ableDate);
				if(!dateMap.containsKey(dayIndex)) {
					dateMap.put(dayIndex, "1");
					totalDaysHavePrice ++;
					
					comm.setCloseflag(null != obj[16] ? obj[16].toString()
									: "");
					if (bCanBook && "G".equalsIgnoreCase(comm.getCloseflag())) {
						bCanBook = false;
						comm.setCantbookReason("该房型已关房");
					}
					
					Double salePrice = Double.valueOf(obj[12].toString());
					comm.setSaleprice(salePrice);
					if(bCanBook && (0.1 > salePrice || 99998 < salePrice)) {
						bCanBook = false;
						comm.setCantbookReason("该房型暂无价格");
					}
					
					// 计算返现金额
					comm.setAbledate((Date)obj[4]);
					comm.setFormula(obj[11].toString());
					comm.setCommissionRate(Double.valueOf(obj[13].toString()));
					comm.setCommission(Double.valueOf(obj[14].toString()));										
					BigDecimal cPrice = returnService.calculateRoomTypePrice(
							comm.getFormula(), new BigDecimal(comm
									.getCommission()), new BigDecimal(comm
									.getCommissionRate()), new BigDecimal(comm
									.getSaleprice()));
					
					//如果是中旅，俑金等于售价-底价   add by longkangfu
					double commission = comm.getCommission();
					if(forCts){
						commission = comm.getSaleprice()-Double.valueOf(obj[29].toString());
					}
					
					//计算限量返现
					int cashLimitReturnAmount=limitFavourableManage.calculateCashLimitReturnAmount(comm.getHotelId(), priceTypeId, 
							        ableDate, comm.getCurrency(), new BigDecimal(comm.getSaleprice()), commission);
					
					//如果没有限量返现，再计算普通返现，如果有，则不计算普通返现
					if(cashLimitReturnAmount==-1){
						cashReturnAmount += returnService
						.calculateCashReturnAmount(priceTypeId, comm
								.getAbledate(), nPayMethod, comm
								.getCurrency(), 1, cPrice);
					}else{
						cashReturnAmount += cashLimitReturnAmount;
					}
				}
				
				this.handleRoomState(obj[8].toString(), beds);
			}
			
			bFirst = true;
			for(int i=0; i<beds.length; i++) {
				if(beds[i]) {
					if(bFirst) {
						bed += (i + 1);	
						bFirst = false;
					} else {
						bed += "," + (i + 1);
					}
				}
			}
			
			if(bCanBook && 0 >= bed.length()) {
				bCanBook = false;
				comm.setCantbookReason("该房型满房，暂无法预订");
			}
			if(bCanBook && totalDaysHavePrice < totalDays) {
				bCanBook = false;
				comm.setCantbookReason("该房型暂无价格，无法预订");
			}
			
			if(bCanBook && !satisfyClauseForPerday(comm, inDate, outDate)) {
				bCanBook = false;
			}
			
			comm.setBedtype(bed);
			comm.setHasbook(bCanBook ? "1" : "0");
			
			
			comm.setReturnCash(Double.valueOf(cashReturnAmount));
			comm.setHasReturnCash(0 < cashReturnAmount);
			
			return comm;
		} else {
			return null;
		}    	
	}
    
    /**
     * 
     * 处理房态数据
     * 
     * @param roomState
     * @param beds
     */
    private void handleRoomState(String roomState, boolean[] beds) {
		boolean[] curBeds = { false, false, false };
		for (String statePair : roomState.split("/")) {
			String[] pair = statePair.split(":");
			if (!"4".equals(pair[1])) {
				curBeds[Integer.parseInt(pair[0]) - 1] = true;
			}
		}
		for(int i=0; i<beds.length; i++) {
			if(beds[i]) {
				beds[i] = curBeds[i];
			}
		}
	}
		
	/**
	 * hotelOrderFromBean添加酒店信息,必须有childRoomTypeId
	 */
	public void addHotelInfoToOrderBean(HotelOrderFromBean hotelOrderFromBean){
		if( null == hotelOrderFromBean ){
			return ;
		}
		HtlHotel htlHotel = queryHtlHotelInfo(hotelOrderFromBean.getChildRoomTypeId());
		if(htlHotel!=null){
		hotelOrderFromBean.setHotelId(htlHotel.getID());
		hotelOrderFromBean.setHotelName(htlHotel.getChnName());
		hotelOrderFromBean.setCityCode(htlHotel.getCity());
		hotelOrderFromBean.setCityName(InitServlet.cityObj.get(htlHotel.getCity()));
		}
	}
	
	/**
	 * hotelOrderFromBean添加房型信息，必须有childRoomTypeId
	 */
	public void addRoomInfoToOrderBean(HotelOrderFromBean hotelOrderFromBean){
		if( null == hotelOrderFromBean ){
			return ;
		}
		HtlRoomtype htlRoomtype = queryHtlRoomtypeInfo(hotelOrderFromBean.getChildRoomTypeId());
		if(htlRoomtype!=null){
		hotelOrderFromBean.setRoomTypeId(htlRoomtype.getID());
		hotelOrderFromBean.setRoomTypeName(htlRoomtype.getRoomName());
		hotelOrderFromBean.setBedTypeStr(htlRoomtype.getBedType());
		hotelOrderFromBean.setRoomChannel(getCooperateChannel(hotelOrderFromBean.getChildRoomTypeId()).intValue());
		}
	}
	
	/**
	 * hotelOrderFromBean添加价格类型信息，必须有childRoomTypeId,payMethod,Indate,oudate
	 */
	public void addPriceInfoToOrderBean(HotelOrderFromBean hotelOrderFromBean){
		if( null == hotelOrderFromBean ){
			return ;
		}		
		//得到渠道
		int  roomChannel = hotelOrderFromBean.getRoomChannel();
		boolean forCts = roomChannel == 8 ? true : false ;  
		QueryCommodityInfo commodityInfo = queryCommidity(hotelOrderFromBean.getChildRoomTypeId(),hotelOrderFromBean.getPayMethod(),
				hotelOrderFromBean.getCheckinDate(),hotelOrderFromBean.getCheckoutDate(),forCts);
		
//		roomTypeId=32904,      房型Id<br>
//	    roomTypeName=海景预付, 房型名称<br>	
//		childRoomTypeId=34053,  价格类型Id<br>
//		childRoomTypeName=标准, 价格类型名称<br>				
//		breakfastType=3,     早餐类型<br>
//		breakfastNum=0,      早餐数<br>
//		bedTypeStr=2,        可预订的床型(如"1,2")<br>
//		currency=HKD,        币种(如"RMB")<br>
//		canbook=null,		 是否可预订 ("1"为可预订)<br>
//		payToPrepay=false,   面转预(请参看QueryCommodityInfo类的该成员说明)<br>		
//		returnAmount=25,        返现金额<br>  
		if (commodityInfo != null) {
			hotelOrderFromBean.setChildRoomTypeName(commodityInfo.getCommodityName());
			hotelOrderFromBean.setBedTypeStr(commodityInfo.getBedtype());
			hotelOrderFromBean.setBreakfastNum(commodityInfo.getBreakfastnumber().intValue());
			hotelOrderFromBean.setCurrency(commodityInfo.getCurrency());
			hotelOrderFromBean.setCurrencyStr(PriceUtil.currencyMap.get(commodityInfo.getCurrency()));
			if (CurrencyBean.RMB.equals(commodityInfo.getCurrency())) {
				hotelOrderFromBean.setCurrencyStr("&yen;");
			}
			hotelOrderFromBean.setPayToPrepay("1".equals(commodityInfo.getPaytoprepay()) ? true : false);// 1为必须
			hotelOrderFromBean.setReturnAmount(commodityInfo.getReturnCash());
			// TODO this is a bad method ,you can overwrite HotelOrderFromBean
			hotelOrderFromBean.setFlag_canbook("1".equals(commodityInfo.getHasbook()) ? true : false);
			hotelOrderFromBean.setCanntBookReason(commodityInfo.getCantbookReason());
		}
	}
	
	/**
	 * 政府税
	 */
	private void addRoomIncTaxStr(HotelOrderFromBean hotelOrderFromBean){
			HtlContract contractEntity = contractManage.queryCurrentContractByHotelId(hotelOrderFromBean.getHotelId());
			 if (contractEntity != null) {
				 HtlTaxCharge taxChargeObj = hotelBookingService.getHaveTaxCharge(contractEntity.getID(), hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate());
				 if(taxChargeObj!=null && taxChargeObj.getRoomTaxName()!=null && !"".equals(taxChargeObj.getRoomTaxName())){
				     hotelOrderFromBean.setRoomIncTaxStr("(注：此价格不含"+taxChargeObj.getRoomTaxName()+")");
				 }
		     }	 
	}
	
	 /**
     * 酒店提示信息 
     */
	private void addHotelAlertInfo (HotelOrderFromBean hotelOrderFromBean){
	    String cauleInfo = hotelService.queryAlertInfoStr(hotelOrderFromBean.getHotelId(), String
	         .valueOf(hotelOrderFromBean.getChildRoomTypeId()), hotelOrderFromBean.getCheckinDate(),
	          hotelOrderFromBean.getCheckoutDate(), LocalFlag.HWEB);
	    cauleInfo = cauleInfo.trim();	
	    if (null != cauleInfo && !cauleInfo.equals("")) {
	        cauleInfo = cauleInfo.replace("\r\n", "");
	        hotelOrderFromBean.setTipInfo(cauleInfo);
	    }
	}
	
	/**
	 * 必须有childRoomTypeId,payMethod,Indate,oudate
	 */
	public void addsomeInfoToOrderBean(HotelOrderFromBean hotelOrderFromBean){
		if( null == hotelOrderFromBean ){
			return ;
		}
		addHotelInfoToOrderBean(hotelOrderFromBean);
		addRoomInfoToOrderBean(hotelOrderFromBean);
		addPriceInfoToOrderBean(hotelOrderFromBean);
		//政府税
		addRoomIncTaxStr(hotelOrderFromBean);
		//酒店提示信息
		addHotelAlertInfo(hotelOrderFromBean);
	}

	
	/**
	 * 查询ExMapping，主要提供一个渠道号
	 */
	public ExMapping queryExMapping(Long priceTypeId){
		return hotelBookDao.getExMapping(priceTypeId);
	}
	
	/**
	 * 获取渠道号
	 */
	public Long getCooperateChannel(Long priceTypeId){
		ExMapping em = queryExMapping(priceTypeId);
		if(em == null || em.getChanneltype() == null){
			return 0L;
		}
		if(em.getChanneltype()==ChannelType.CHANNEL_CTS){
	    	HtlRoomtype roomtype = queryHtlRoomtypeInfo(priceTypeId);
	    	String isHKRoomtype = roomtype==null ? "0" : roomtype.getIshkroomtype();
	    	if("0".equals(isHKRoomtype)){
	    		return 0L;
	    	}
		}
    	return em.getChanneltype();
		
	}
	
	/**
	 * 担保条款
	 */
	public String getBookhintAssure(OrReservation orReservation,String payMethod,boolean payToPrepay){		
		if(null == orReservation ){
			return "";
		}
		if(PayMethod.PRE_PAY.equals(payMethod) || payToPrepay ){
			return "";
		}
		String bookhintAssureStr = "";
		boolean overTimeAssure = false;
		boolean overRoomsAssure = false;
		//无条件担保
		if(orReservation.isUnCondition()){
			bookhintAssureStr =  "该时段内入住该房型，需按酒店要求提供信用卡担保。";
		}else {
			if(orReservation.isOverTimeAssure()){
				String lastArriveTime = orReservation.getLateSuretyTime();
				bookhintAssureStr = "入住时间超过酒店规定最晚时间 "+lastArriveTime+",";
				overTimeAssure = true;
			}if(orReservation.isRoomsAssure()){
		    	int maxRoomNum = orReservation.getRooms();
		    	if(overTimeAssure){
		    	    bookhintAssureStr = bookhintAssureStr+"或"+"预订该房型"+maxRoomNum+"间以上,";
		    	}else{
		    		bookhintAssureStr = "预订该房型"+maxRoomNum+"间以上,";//this is bad hint ,each assurehint can been written to a method
		    	}		    	
		    	overRoomsAssure = true;
		    }if(orReservation.isNightsAssure()){
		    	int maxNightNum = orReservation.getNights();
		    	if(overTimeAssure || overRoomsAssure){
		    		bookhintAssureStr += "或"+"预订该房型超过"+maxNightNum+"间夜以上,";
		    	}else{
		    		bookhintAssureStr = "预订该房型超过"+maxNightNum+"间夜以上,";
		    	}
		    }
		    if(!"".equals(bookhintAssureStr)){
		          bookhintAssureStr += "需按酒店要求提供信用卡担保。";
		    }
		}    
		return bookhintAssureStr;
	}
	
	/**
	 * 修改条款
	 */
	public String getBookhintCancelAndModify(OrReservation orReservation,ReservationAssist reservationAssist,String payMethod,boolean payToPrepay){
	    if(null == orReservation  || null == reservationAssist )	{
	    	return "";
	    }
	    String bookhintCancelAndModifyStr = "";
	    if(PayMethod.PRE_PAY.equals(payMethod) || payToPrepay){
	    	bookhintCancelAndModifyStr = "该房型一旦预订确认成功将不接受免费取消或修改，如需取消或修改将按酒店规定扣取您的全部预付金额。";
	    }else{
	    	if("1".equals(reservationAssist.getCancmodiType())){
	    		bookhintCancelAndModifyStr = "该房型一旦预订并确认成功将不接受免费取消，如需取消将按酒店规定比例扣取您的担保金额。";
	    	}
	    	else if("2".equals(reservationAssist.getCancmodiType())){
	    		bookhintCancelAndModifyStr = "该房型一旦预订并确认成功将不接受免费修改，如需修改将按酒店规定比例扣取您的担保金额。";
	    	}else if("3".equals(reservationAssist.getCancmodiType())){
	    		bookhintCancelAndModifyStr = "该房型一旦预订并确认成功将不接受免费取消或修改，如需取消或修改将按酒店规定比例扣取您的担保金额。";
	    	}else if("4".equals(reservationAssist.getCancmodiType())){
	    		 String earliestDate = DateUtil.dateToString(reservationAssist.getEarliestNoPayDate());
	             String earliestTime = reservationAssist.getEarliestNoPayTime();
	             Date formatEarliestDate = DateUtil.stringToDatetime(earliestDate + " " + earliestTime);
	             int isBeforeCanMod = Float.valueOf(((float) (formatEarliestDate.getTime() - Calendar.getInstance().getTime()
	                        .getTime()) / 86400000 + 1)).intValue();
	             if(isBeforeCanMod > 0){
	            	 bookhintCancelAndModifyStr ="需取消或修改本次预订，请您务必于 "+earliestDate+" "+earliestTime+" 前致电40066 40066提出变更，否则将按酒店规定比例扣取您的担保金额。";
	             }else {
	            	 bookhintCancelAndModifyStr = "该房型一旦预订并确认成功将不接受免费取消或修改，如需取消或修改将按酒店规定比例扣取您的担保金额。";
	             }
	             
	    	}	    
	    }
	    return bookhintCancelAndModifyStr;
	}
	
	
	//get set method
	
	public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
		this.hotelManageWeb = hotelManageWeb;
	}

	public void setHotelBookDao(HotelBookDao hotelBookDao) {
		this.hotelBookDao = hotelBookDao;
	}

	public void setRoomtypeDao(HtlRoomtypeDao roomtypeDao) {
		this.roomtypeDao = roomtypeDao;
	}

	public void setContractManage(ContractManage contractManage) {
		this.contractManage = contractManage;
	}

	public void setHotelService(IHotelService hotelService) {
		this.hotelService = hotelService;
	}

	public void setHotelBookingService(HotelBookingService hotelBookingService) {
		this.hotelBookingService = hotelBookingService;
	}

	public void setReturnService(IHotelFavourableReturnService returnService) {
		this.returnService = returnService;
	}

	public void setLimitFavourableManage(
			HtlLimitFavourableManage limitFavourableManage) {
		this.limitFavourableManage = limitFavourableManage;
	}
	 
	
}

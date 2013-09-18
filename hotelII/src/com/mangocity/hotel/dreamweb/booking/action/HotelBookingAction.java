package com.mangocity.hotel.dreamweb.booking.action;

import hk.com.cts.ctcp.hotel.service.HKManage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import com.mangocity.hotel.ext.member.dto.PointDTO;
import com.mangocity.hdl.constant.ChannelType;
import com.mangocity.hotel.base.manage.HtlLimitFavourableManage;
import com.mangocity.hotel.base.persistence.ExMapping;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlPresale;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.persistence.OrReservation;
import com.mangocity.hotel.base.service.IHotelFavourableReturnService;
import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.hotel.base.service.assistant.ReservationAssist;
import com.mangocity.hotel.base.util.WebStrUtil;
import com.mangocity.hotel.dreamweb.search.dao.HotelBookDao;
import com.mangocity.hotel.dreamweb.search.service.HotelBookService;
import com.mangocity.hotel.dreamweb.search.service.HotelInfoService;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrPriceDetail;
import com.mangocity.hotel.search.constant.PayMethod;
import com.mangocity.hotel.search.handler.impl.HotelQueryHandler;
import com.mangocity.hotel.search.model.HotelBasicInfo;
import com.mangocity.hotel.search.util.PriceUtil;
import com.mangocity.hotel.search.vo.HotelResultForWebVO;
import com.mangocity.hotel.search.vo.SaleItemVO;
import com.mangocity.hweb.persistence.HotelAdditionalServe;
import com.mangocity.hweb.persistence.OftenDeliveryAddress;
import com.mangocity.hweb.persistence.QueryHotelForWebSaleItems;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.log.MyLog;
import com.mangocity.util.web.CookieUtils;
import com.mangocity.webnew.persistence.HotelOrderFromBean;
import com.mangocity.webnew.service.HotelBookingService;
import com.mangocity.webnew.util.action.GenericWebAction;

public class HotelBookingAction extends GenericWebAction{
	
	//进来的参数
	private String priceTypeId;
	private String payMethod;
	private String inDate;
	private String outDate;
	//private HotelOrderFromBean hotelOrderFromBean;	
	//输出的参数
    private HotelAdditionalServe additionalServe = new HotelAdditionalServe();	 // 酒店附加服务
    private List<HtlPresale> presaleList = new ArrayList<HtlPresale>();    // 获取酒店促销信息
    private List<QueryHotelForWebSaleItems> priceLis = new ArrayList<QueryHotelForWebSaleItems>();    // 获取酒店促销信息
    private ReservationAssist reservationAssist;      // 酒店预订条款取出
    private String  bookhintAssureStr ;
    private String bookhintCancelAndModifyStr ;
    private List<SaleItemVO> saleItemVOList ; //价格详情展示
    private HotelResultForWebVO hotelVO = new HotelResultForWebVO();// 酒店展示用的酒店星级展示
	private HotelBasicInfo hotelBasicInfo;// 提供酒店的信息 add by diandian.hou 2011-5-25
    // 会员
    private List<OftenDeliveryAddress> oftenAddressList = new ArrayList<OftenDeliveryAddress>(); // 会员常用配送地址列表
    
	
	//注入的service
	private HotelBookService hotelBookService;// 新的bookService
	private HotelBookingService hotelBookingService;  //旧的bookingSerivce
	private IHotelService hotelService;
	private HotelInfoService hotelInfoService;
    private IHotelFavourableReturnService returnService; //现金返还服务接口 add by linpeng.fang 2009-09-29 
    private HtlLimitFavourableManage limitFavourableManage;//芒果限量返现活动促销service add by xiaojun.xiong 2011-3-4
    private HKManage hkManage;
    private HotelBookDao hotelBookDao;
   
	
    //支付宝 用的projectcode 与父类 的projectCode区别开 add by diandian.hou 2011-11-30
    private String projectcode;
    //错误信息
    private static final String ERROR_MESSAGE = "该段时间内没有相关预订信息，请返回重新预订！";
    private static final MyLog log = MyLog.getLogger(HotelBookingAction.class);
	public String execute() {
		switchPayMethod();
		initOrderBean();
		try{
		    hotelBookService.addsomeInfoToOrderBean(hotelOrderFromBean);
		}catch(Exception e){
			log.error("HotelBookingAction addsomeInfo error", e);
			super.setErrorCode("H04");
			return super.forwardError(ERROR_MESSAGE);
		}
		//判断是否可订
		if(!hotelOrderFromBean.isFlag_canbook()){
			super.setErrorCode("H04");
			String errorMessage = ERROR_MESSAGE;
			String canntBookReason = hotelOrderFromBean.getCanntBookReason();
			if(StringUtil.isValidStr(canntBookReason)){
				errorMessage = canntBookReason;
			}
			return super.forwardError(errorMessage);
		}
		
		//畅联的
		 if(hotelOrderFromBean.getRoomChannel()!=0 && hotelOrderFromBean.getRoomChannel()!= ChannelType.CHANNEL_CTS){	        	
	        	String hdlPriceStr ="";
				try {
					boolean isActive = hotelBookDao.getIsActive(Long.valueOf(hotelOrderFromBean.getChildRoomTypeId()), hotelOrderFromBean.getRoomChannel());
					if( !isActive ){
						return super.forwardError(ERROR_MESSAGE);
					}
					hdlPriceStr = hotelBookingService.reReshReservateExResponse(hotelOrderFromBean.getHotelId(),
					        hotelOrderFromBean.getRoomTypeId(), String.valueOf(hotelOrderFromBean.getChildRoomTypeId()),
					        hotelOrderFromBean.getRoomChannel(),hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate());
				} catch (Exception e) {
					log.error("===========HotelBookingAction==========CHINAONLINE=========call HDL interfaces exception: ",e);
					super.setErrorCode("H04");
					setErrorMessage(ERROR_MESSAGE);	            	
	                return "forwardToError";
				}	        	
	        	log.info("HotelBookingAction==========CHINAONLINE===========hdlPriceStr----" + hdlPriceStr);        	
	        	if( null!=hdlPriceStr && !"".equals(hdlPriceStr) && !hdlPriceStr.equals("hdlRefreshPriceFail")){
	        		double hdlTotal = Double.parseDouble(hdlPriceStr);
	        		log.info("HotelBookingAction==========CHINAONLINE===========hdlTotal----" + hdlTotal);
	        		// 需要比较原来的价格是否相等，如果原币种价格相等，则表示价格没有发生变化；如果不等，则需要打上已变化标记
		                    //hotelOrderFromBean.setPriceChange(true);
		                    hotelOrderFromBean.setPriceNum(hdlTotal);
	        	}else{
	        		super.setErrorCode("H01");
	        		log.info("HotelBookingAction==========CHINAONLINE==========该段时间内没有相关预订信息，请返回重新预订");
	        		setErrorMessage(ERROR_MESSAGE);	            	
	                return "forwardToError";
	        	}
	        }
		 //中旅的
		     /**
	         * 如果是香港中科酒店，需要重新调接口取价格 ADD BY WUYUN 2009-03-23
	         */
	        Date checkIn = hotelOrderFromBean.getCheckinDate();
	        Date checkOut = hotelOrderFromBean.getCheckoutDate();
	        int days = DateUtil.getDay(checkIn, checkOut);
	        if (hotelOrderFromBean.getPayMethod().equals("pre_pay")&& ChannelType.CHANNEL_CTS == hotelOrderFromBean.getRoomChannel()) {
	        	
	        	//调HDL接口
	        	try{
		        	boolean isActive = hotelBookDao.getIsActive(Long.valueOf(hotelOrderFromBean.getChildRoomTypeId()), hotelOrderFromBean.getRoomChannel());
					if( !isActive ){
						return super.forwardError(ERROR_MESSAGE);
					}
					
					priceLis = hotelBookingService.refreshHotelReservateExResponse(hotelOrderFromBean.getHotelId(), hotelOrderFromBean.getRoomTypeId(), 
							hotelOrderFromBean.getChildRoomTypeId(), hotelOrderFromBean.getRoomChannel(), hotelOrderFromBean.getCheckinDate(), 
							hotelOrderFromBean.getCheckoutDate());
	        	}catch(Exception e){
	        		log.error("===========HotelBookingAction==========CTS=========call HDL interfaces exception: ",e);
					super.setErrorCode("H04");
					setErrorMessage(ERROR_MESSAGE);
	                return "forwardToError";
	        	}
	        	if (null != priceLis && days <= priceLis.size()){
	        		double reTotal = 0.0;
	        		StringBuffer saleBuffer = new StringBuffer();
	                StringBuffer baseBuffer = new StringBuffer();
	                for (int k = 0; k < priceLis.size(); k++) {
	                    QueryHotelForWebSaleItems it = (QueryHotelForWebSaleItems) priceLis.get(k);
	                    saleBuffer.append(it.getSalePrice() + "#");
	                    baseBuffer.append(it.getBasePrice() + "#");
	                    reTotal += it.getSalePrice();
	                    log.info("HotelBookingAction==========CTS===========priceLis[k].salePrice:" + it.getSalePrice());
	                    log.info("HotelBookingAction==========CTS===========priceLis[k].basePrice:" + it.getBasePrice());
	                }
	                // 需要比较原币种(即港币)价格是否相等，如果原币种价格相等，则表示价格没有发生变化；如果不等，则需要打上已变化标记
	                    //hotelOrderFromBean.setPriceChange(true);
	                //把刷到价格同步到数据库中，该方法应放到service层中
	                hotelOrderFromBean.setPriceNum(reTotal);
	        		log.info("HotelBookingAction==========CTS===========hdlTotal----" + reTotal);
	        	}else{
	        		super.setErrorCode("H01");
	        		log.info("HotelBookingAction==========CTS==========该段时间内没有相关预订信息，请返回重新预订");
	        		setErrorMessage(ERROR_MESSAGE);
	                return "forwardToError";
	        	}
	        }
	    try{
		//酒店星级展示用的
	        hotelBasicInfo = hotelInfoService.queryHotelInfoByHotelId(String.valueOf(hotelOrderFromBean.getHotelId()));
	        hotelVO.setHotelStar(HotelQueryHandler.HotelStarConvert.getForWebVOByKey(hotelBasicInfo.getHotelStar()));
	        
		 //酒店的附加服务
        additionalServe = hotelManageWeb.queryWebAdditionalServeInfo(hotelOrderFromBean
                .getHotelId(), hotelOrderFromBean.getRoomTypeId(), hotelOrderFromBean.getCheckinDate(),
                hotelOrderFromBean.getCheckoutDate(), hotelOrderFromBean.getPayMethod());
        //判断是否为空
        if (null != additionalServe && 0 == additionalServe.getBedServes().size()
                && 0 == additionalServe.getBuffetServes().size()) {
             additionalServe = null;
        }
        // 查询预定的价格列表
        priceLis = hotelManageWeb.queryPriceForWeb(hotelOrderFromBean.getHotelId(),
            hotelOrderFromBean.getRoomTypeId(), hotelOrderFromBean.getChildRoomTypeId(),
            hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate(),
            hotelOrderFromBean.getMinPrice(), hotelOrderFromBean.getMaxPrice(), hotelOrderFromBean
                .getPayMethod(), hotelOrderFromBean.getQuotaType());        
        //连住优惠，更改销售价
        priceLis = hotelBookingService.changeFavPrice(hotelOrderFromBean, priceLis);
        // 获取酒店促销信息
        presaleList = hotelManageWeb.queryPresalesForWeb(hotelOrderFromBean.getHotelId(),
            hotelOrderFromBean.getCheckinDate());
	    }catch(Exception e){
	    	log.error("hotelBookingAction query price error:",e);
	    }
		      
        //member信息start
        
        try {
            // 获取会员信息
            member = getMemberInfoForWeb(true);
        } catch (Exception e) {
            log.error("getMemberInfoForWeb is Error" + e);
        }
        // 封装会员的Point类
        try {
            PointDTO pw = null;
            if (null != member) {
                pw = member.getPoint();
            }
            //获得积分余额
            hotelOrderFromBean.setAbleUlmPoint(null != pw ? pw.getBalanceValue() : "0");
        } catch (Exception e) {
        	 //获得积分余额
            hotelOrderFromBean.setAbleUlmPoint("0");
            log.error("member is null or member.getPoint is error!" + e);
        }
        // 查询会员常用配送地址信息
        if (null != member) {
            oftenAddressList = hotelManageWeb.queryOftenDeliveryAddress(member.getId());
        }
        // 获取会员常入住人和常联系人
        if (null != member && member.isMango()) { // 114会员应该没有常入住人和常联系人
            memberInterfaceService.getFellowAndLinkmanByMemberCd(member);
        }
        
        //member信息end
        
        //订单
        try{
        order = new OrOrder();
        order.setCheckinDate(hotelOrderFromBean.getCheckinDate());
        order.setCheckoutDate(hotelOrderFromBean.getCheckoutDate());
        order.setRoomTypeId(hotelOrderFromBean.getRoomTypeId());
        order.setRoomQuantity(null == hotelOrderFromBean.getRoomQuantity()
            || hotelOrderFromBean.getRoomQuantity().equalsIgnoreCase("null") ? 1 : Integer
            .parseInt(hotelOrderFromBean.getRoomQuantity()));
        order.setChildRoomTypeId(hotelOrderFromBean.getChildRoomTypeId());
        order.setHotelId(hotelOrderFromBean.getHotelId());
        // v2.6 必须面转预，订单的支付方式需要由面付改成预付 ADD BY WUYUN 2009-06-04
        order.setPayMethod(hotelOrderFromBean.hasPayToPrepay() ? "pre_pay" : hotelOrderFromBean
            .getPayMethod());
        order.setPayToPrepay(hotelOrderFromBean.hasPayToPrepay());
        if (null != member) {
            order.setMemberId(member.getId());
            order.setMemberCd(member.getMembercd());
        }

        // add by zhineng.zhuang hotel2.6 包装预订条款,修改了order.PriceList ,reservation,reservationAssist的值
        getReserva(hotelOrderFromBean, order);
        
        //this method is getting prices and setPriceNum() 
        double priceNum = 0.0;
        for(int i = 0 ; i< order.getPriceList().size();i++){
        	OrPriceDetail items = order.getPriceList().get(i);
        	priceNum += items.getSalePrice();
        }
        hotelOrderFromBean.setPriceNum(priceNum);
        String payMethodTemp = hotelOrderFromBean.isPayToPrepay() ? "pre_pay" : hotelOrderFromBean.getPayMethod();
        try{
        saleItemVOList = PriceUtil.getSaleItemVOList(order.getPriceList(), payMethodTemp, hotelOrderFromBean.getCurrency());
        }catch(Exception e){
        	log.error("query every price error",e);
        }
        //get bookhintAssureStr must after the method getReserva(),because reservation
        bookhintAssureStr = hotelBookService.getBookhintAssure(reservation, hotelOrderFromBean.getPayMethod(), hotelOrderFromBean.isPayToPrepay());
        bookhintCancelAndModifyStr = hotelBookService.getBookhintCancelAndModify(reservation, reservationAssist, 
        		hotelOrderFromBean.getPayMethod(), hotelOrderFromBean.isPayToPrepay());
           
        /** hotel2.9.3 代金券需要传递酒店相关基本信息 add by chenjiajie 2009-09-15 begin **/
        HtlHotel htlHotel = hotelService.findHotel(hotelOrderFromBean.getHotelId());
        request.setAttribute("htlHotel", htlHotel);
        /** hotel2.9.3 代金券需要传递酒店相关基本信息 add by chenjiajie 2009-09-15 end **/
		
        refreshCashReturnAmount(hotelOrderFromBean,priceLis);
        }catch(Exception e){
        	log.error("hotelBookingAction order error", e);
        }
        
        //by ting.li，保存订单填写数据空间
       // saveOrderRecord();
        
        if(PayMethod.PRE_PAY.equals(hotelOrderFromBean.getPayMethod()) || hotelOrderFromBean.isPayToPrepay() == true ){
		      return "pre_pay";
        }
        return SUCCESS;//只是面付
	}
	
	
	private void switchPayMethod(){
		if(PayMethod.PRE_PAY.equals(payMethod) || PayMethod.PAY.equals(payMethod)){
			return;
		}
	   if("提前支付".equals(payMethod)){
		   payMethod = PayMethod.PRE_PAY;
	   }else{
		   payMethod = PayMethod.PAY;
	   }
	}

	
	private void initOrderBean(){
		this.hotelOrderFromBean = new HotelOrderFromBean();
		hotelOrderFromBean.setChildRoomTypeId(Long.valueOf(priceTypeId));
		hotelOrderFromBean.setPayMethod(payMethod);
		//TODO 支付宝没有时间 add by diandian.hou 2011-11-30
		if(inDate==null){
			inDate = DateUtil.dateToString(DateUtil.getDate(DateUtil.getSystemDate(), 1));
			outDate = DateUtil.dateToString(DateUtil.getDate(DateUtil.getSystemDate(), 2));
		}
		//添加支付宝的cookie add by diandian.hou 2011-11-30
		if(projectcode==null){
			projectcode = CookieUtils.getCookieValue(request,"projectcode");
		}
		hotelOrderFromBean.setCheckinDate(DateUtil.stringToDateMain(inDate,"yyyy-MM-dd"));
		hotelOrderFromBean.setCheckoutDate(DateUtil.stringToDateMain(outDate,"yyyy-MM-dd"));
        int days = DateUtil.getDay(hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate());
		hotelOrderFromBean.setDays(days);
		hotelOrderFromBean.setRoomQuantity(String.valueOf(1));
		hotelOrderFromBean.setReturnAmount(0.0D);
	}
	
	
	public void refreshCashReturnAmount(HotelOrderFromBean hotelOrderFromBean,List<QueryHotelForWebSaleItems> priceList){	
		if(null == member) {
			hotelOrderFromBean.setReturnAmount(0.0D);//返现设为0
			return;
		}
		//判断会员是否为芒果散客,用于现金返还判断 
		if(null != member){
	        	hotelOrderFromBean.setLoginBeforeBooking(true);
	        	if(!member.isFitFlag()){
	        		hotelOrderFromBean.setReturnAmount(0d);
	        		log.info("================会员:"+member.getMembercd()+"的项目号:"+member.getAliasid()+"不再散客项目号之列，返现清零！");
	        		return;
	        	}
	      }else{
	        	hotelOrderFromBean.setReturnAmount(0d);
	        	log.info("================会员为空，返现清零！");
	        	return;
	      }
		if(hotelOrderFromBean.getReturnAmount() <= 0 || null == priceList || priceList.isEmpty()) return;
		double  cashReturnAmount = 0;
		int payMethod = PayMethod.PAY.equals(hotelOrderFromBean.getPayMethod()) ? 1 : 2;
		if(hotelOrderFromBean.isPayToPrepay()){
			payMethod =1;
		}
	    int days = DateUtil.getDay(hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate());
		for(int i=0; i<days; i++){
    		Date night = DateUtil.getDate(hotelOrderFromBean.getCheckinDate(), i); 
    		for(Object obj : priceList){
	        		QueryHotelForWebSaleItems item = (QueryHotelForWebSaleItems)obj;
	        		BigDecimal price = new BigDecimal(item.getSalePrice());
	        		if(price.intValue() >= 99999 || "1".equals(item.getCloseShowPrice())) continue;
	        		if(DateUtil.getDay(item.getFellowDate(),night) == 0){    	        			
	        			BigDecimal cPrice = new BigDecimal("0");
	        			
	        			if (hotelOrderFromBean.getPayMethod().equals("pre_pay")
	        		            && ChannelType.CHANNEL_CTS == hotelOrderFromBean.getRoomChannel()){
	        				//如果是香港中科酒店，只有简单公式， 净价等于售价
	        				cPrice = price;
	        			}
	        			else{
	        			    cPrice = returnService.calculateRoomTypePrice(item.getFormulaId(), new BigDecimal(item.getCommission()), new BigDecimal(item.getCommissionRate()), price);
	        			}
	        			log.info("返现规则计算净价:"+cPrice.doubleValue()+"价格公式:"+item.getFormulaId()+"佣金："+item.getCommission()+"佣金率:"+item.getCommissionRate()+"售价："+item.getSalePrice());
	        			int returnAmount=0;
	        		    returnAmount = returnService.calculateCashReturnAmount(hotelOrderFromBean.getChildRoomTypeId(),item.getFellowDate(), payMethod, hotelOrderFromBean.getCurrency(), 1, cPrice);
	        			cashReturnAmount += returnAmount;
	        			log.info("单步计算的返现金额:"+returnAmount);
	        	}
	        }
    	}
		if(cashReturnAmount!= hotelOrderFromBean.getReturnAmount()){
			log.info(member.getMembercd()+":"+DateUtil.datetimeToString(new Date())+"刷取到最新的返现规则，返现金额由原来的："+hotelOrderFromBean.getReturnAmount()+"变更为："+cashReturnAmount);
			hotelOrderFromBean.setReturnAmount(cashReturnAmount);	
		}
	}
	
	 /**
     * 取出预订条款并给订单，给计算担保金额 add by zhineng.zhuang hotel2.6
     */
    public void getReserva(HotelOrderFromBean hotelOrderFromBean, OrOrder order) {
        // add by zhineng.zhuang hotel2.6 2009-03-01 begin
        // 查询每天的价格，给计算预付担保金额用。
        List<QueryHotelForWebSaleItems> queryPriceList = new ArrayList<QueryHotelForWebSaleItems>();
        queryPriceList = hotelManageWeb.queryPriceDetailForWeb(hotelOrderFromBean.getHotelId(),
            hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate(),
            hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate(),
            hotelOrderFromBean.getRoomTypeId(), hotelOrderFromBean.getChildRoomTypeId(),
            hotelOrderFromBean.getPayMethod());
        int priceSize = queryPriceList.size();
        //连住优惠对应改变售价 add by guzhijie begin 2009-09-13
        int f =0;
        for(int y=0;y<priceSize;y++){
        	QueryHotelForWebSaleItems queryHotelForWebSaleItems = queryPriceList.get(y);
        	f=hotelManageWeb.changePriceForFavourableThree(queryPriceList, queryHotelForWebSaleItems, order.getHotelId(), order.getChildRoomTypeId(), y, f);
        	
        }
        //连住优惠价格改变 add by guzhijie end 2009-09-13
        
        detailNum = priceSize;
        QueryHotelForWebSaleItems queryHotelForWebSaleItems = new QueryHotelForWebSaleItems();

        // add by shengwei.zuo 2009-05-05 hotel V2.6 修复担保金额计算错误BUG begin
        // 首日金额
        double priceFirstDay = 0.0;
        // 全额金额
        double priceAllDay = 0.0;

        // 填充订单价格详情
        List<String> dateStrList = DateUtil.getDateStrList(order.getCheckinDate(), order
            .getCheckoutDate(), false);
        for (int p = 0; p < priceSize; p++) {
            queryHotelForWebSaleItems = queryPriceList.get(p);
            OrPriceDetail orPriceDetail = new OrPriceDetail();
            orPriceDetail.setNight(queryHotelForWebSaleItems.getFellowDate());
            orPriceDetail.setDayIndex(p);
            orPriceDetail.setSalePrice(queryHotelForWebSaleItems.getSalePrice());
            // 设置房态字符串
            orPriceDetail.setRoomState(WebStrUtil.showRoomType(queryHotelForWebSaleItems
                .getRoomStatus(), queryHotelForWebSaleItems.getAvailQty()));
            // 设置配额数量
            orPriceDetail.setQuantity(queryHotelForWebSaleItems.getAvailQty());
            // 设置当天日期字符串
            orPriceDetail.setDateStr(dateStrList.get(p));
            // 设置早餐信息字符串
            orPriceDetail.setBreakfastStr(WebStrUtil.getResourceValue("breakfast_typeForCC",
                queryHotelForWebSaleItems.getBreakfastType())
                + ":"
                + WebStrUtil.getResourceValue("breakfast_num", String
                    .valueOf(queryHotelForWebSaleItems.getBreakfastNum())));
            order.getPriceList().add(orPriceDetail);
            // 获取首日房价
            if (0 == p) {
                priceFirstDay = queryHotelForWebSaleItems.getSalePrice();
            }

            priceAllDay += queryHotelForWebSaleItems.getSalePrice();

        }

        // 设置订单的总金额，汇率，以及下单页面所用到的币种字符串和币种符号
        reservation = order.getReservation();
        if (null == reservation) {
            reservation = new OrReservation();
            order.setReservation(reservation);
        }

        Map<String, String> rateMap = CurrencyBean.rateMap;
        if (null != rateMap) {
            String rateStr = rateMap.get(hotelOrderFromBean.getCurrency());
            if (null == rateStr) {
                rateStr = "1.0";
            }
            request.setAttribute("rateCurrency", rateStr);
            rate = Double.valueOf(rateStr.trim()).doubleValue();
            order.setRateId(rate);
            reservation.setFirstPrice(priceFirstDay * rate);
            order.setSum(priceAllDay);
            order.setSumRmb(priceAllDay * rate);
        } else {
            reservation.setFirstPrice(priceFirstDay);
            order.setSum(priceAllDay);
            order.setSumRmb(priceAllDay);
        }

        // add by shengwei.zuo 2009-05-05 hotel V2.6 修复担保金额计算错误BUG end;

        // add by zhineng.zhuang hotel2.6 2009-03-01 end
        reservationAssist = orderService.loadBookClauseForWeb(order);
        //TODO 下面的代码有用吗?好像没用
        hotelOrderFromBean.setOrignalSuretyPrice(order.getSuretyPrice());
        hotelOrderFromBean.setSuretyPriceRMB(Math.ceil(order.getSuretyPrice() * order.getRateId()));
        if (null != reservationAssist) {
            hotelOrderFromBean.setNeedAssure(reservationAssist.isNeedCredit());
        }

        if (null != reservation.getGuarantees()) {
            guaranteeNum = reservation.getGuarantees().size();
        }

        if (null != reservation.getAssureList()) {
            assureNum = reservation.getAssureList().size();
        }

    }
    
    
    /**
	 * create by ting.li
	 * 保存OrderRecord
	 *//*
	private void saveOrderRecord() {

		try {
			HttpSession httpSession = request.getSession(false);
			if (httpSession == null) {
				return;
			}
			OrderRecord orderRecord = (OrderRecord) httpSession.getAttribute("orderRecord");
			// 判断是否修改日期的
			if (orderRecord != null && orderRecord.getPriceTypeId().equals(hotelOrderFromBean.getChildRoomTypeId())) {

				orderRecordService.updateOrderRecord(orderRecord, request, hotelOrderFromBean);
			} else {
				orderRecord = new OrderRecord();
				// 事先设置不能从hotelOrderFromBean得到的值
				orderRecord.setMemberId(member != null ? member.getId() : 0L);
				if (member != null) {
					orderRecord.setLinkMan(member.getName());
					orderRecord.setLinkMobile(member.getLinkmobile());
					orderRecord.setLinkEmail(member.getEmail());
				}
				orderRecord.setCustomerIp(hotelManageWeb.getIpAddr(request));
				projectCode = CookieUtils.getCookieValue(request, "projectcode");
				orderRecord.setProjectCode(projectCode != null ? projectCode : null);
				orderRecord.setReserveTime(order.getReservation().getLateSuretyTime());
				orderRecord.setCreateDate(new Date());
				orderRecordService.addOrderRecord(orderRecord, request, hotelOrderFromBean);

			}
		} catch (Exception e) {
			log.error("HotelBookingAction order Record:" + e);
		}

	}
*/
	
	/**
	 * create by ting.li
	 * 将OrderRecord保存到Session中去，传递到下一个页面
	
	private void putOrderRecordToSession(OrderRecord orderRecord) {
		HttpSession httpSession = request.getSession(true);
		httpSession.removeAttribute("orderRecord");
		httpSession.setAttribute("orderRecord", orderRecord);
	}
 */
	//get set
    
	
	public String getPriceTypeId() {
		return priceTypeId;
	}


	public void setPriceTypeId(String priceTypeId) {
		this.priceTypeId = priceTypeId;
	}


	public String getPayMethod() {
		return payMethod;
	}


	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}


	public String getInDate() {
		return inDate;
	}


	public void setInDate(String inDate) {
		this.inDate = inDate;
	}


	public String getOutDate() {
		return outDate;
	}


	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}


	public void setHotelBookService(HotelBookService hotelBookService) {
		this.hotelBookService = hotelBookService;
	}


	public HotelAdditionalServe getAdditionalServe() {
		return additionalServe;
	}


	public List<HtlPresale> getPresaleList() {
		return presaleList;
	}


	public List<QueryHotelForWebSaleItems> getPriceLis() {
		return priceLis;
	}


	public ReservationAssist getReservationAssist() {
		return reservationAssist;
	}
	
	public void setHotelService(IHotelService hotelService) {
		this.hotelService = hotelService;
	}


	public void setReturnService(IHotelFavourableReturnService returnService) {
		this.returnService = returnService;
	}


	public void setLimitFavourableManage(
			HtlLimitFavourableManage limitFavourableManage) {
		this.limitFavourableManage = limitFavourableManage;
	}


	public List<OftenDeliveryAddress> getOftenAddressList() {
		return oftenAddressList;
	}


	public void setHotelBookingService(HotelBookingService hotelBookingService) {
		this.hotelBookingService = hotelBookingService;
	}


	public String getBookhintAssureStr() {
		return bookhintAssureStr;
	}


	public String getBookhintCancelAndModifyStr() {
		return bookhintCancelAndModifyStr;
	}


	public List<SaleItemVO> getSaleItemVOList() {
		return saleItemVOList;
	}

	public HotelResultForWebVO getHotelVO() {
		return hotelVO;
	}


	public HotelBasicInfo getHotelBasicInfo() {
		return hotelBasicInfo;
	}


	public void setHotelInfoService(HotelInfoService hotelInfoService) {
		this.hotelInfoService = hotelInfoService;
	}


	public void setHkManage(HKManage hkManage) {
		this.hkManage = hkManage;
	}


	public void setHotelBookDao(HotelBookDao hotelBookDao) {
		this.hotelBookDao = hotelBookDao;
	}

	public String getProjectcode() {
		return projectcode;
	}

	public void setProjectcode(String projectcode) {
		this.projectcode = projectcode;
	}

}
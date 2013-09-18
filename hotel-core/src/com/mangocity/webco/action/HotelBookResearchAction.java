package com.mangocity.webco.action;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.ext.member.dto.PointDTO;
import com.mangocity.hdl.constant.ChannelType;
import com.mangocity.hotel.base.manage.ContractManage;
import com.mangocity.hotel.base.manage.IQuotaManage;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlPresale;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.persistence.HtlTaxCharge;
import com.mangocity.hotel.base.persistence.OrReservation;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.base.service.IBenefitService;
import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.hotel.base.service.assistant.ReservationAssist;
import com.mangocity.hotel.base.util.WebStrUtil;
import com.mangocity.hotel.order.constant.LocalFlag;
import com.mangocity.hotel.order.persistence.OrFellowInfo;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrPriceDetail;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.hweb.persistence.AdditionalServeItem;
import com.mangocity.hweb.persistence.HotelAdditionalServe;
import com.mangocity.hweb.persistence.HotelPageForWebBean;
import com.mangocity.hweb.persistence.OftenDeliveryAddress;
import com.mangocity.hweb.persistence.QueryHotelForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebResult;
import com.mangocity.hweb.persistence.QueryHotelForWebRoomType;
import com.mangocity.hweb.persistence.QueryHotelForWebSaleItems;
import com.mangocity.util.ConfigParaBean;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.webnew.persistence.HotelOrderFromBean;
import com.mangocity.webnew.persistence.HtlCalendarHelperBean;
import com.mangocity.webnew.persistence.HtlCalendarStyle;
import com.mangocity.webnew.service.HotelBookingService;
import com.mangocity.webnew.util.action.GenericWebAction;


/**
 * 订单页面，修改住店日期，重新查询action
 * @author zuoshengwei
 *
 */
public class HotelBookResearchAction extends GenericWebAction {
	
    // 查询条件最大金额
    private String maxPrice;
    
    // 查询条件酒店星级
    private String hotelStar;
    
    // 汇率
    private double rate;
    
    // 是否有提示信息;add by shengwei.zuo hotel2.9.2 2009-08-19
    private int isHasTip;
    
    // 会员url
    private String url;
    
    // 第一条价格信息的日期
    private Date firstDate;

    // 最后一条价格信息的日期
    private Date lastDate;
    
    // 常入住人数
    private int oftenFellowCount;
    
    //允许取消修改的时间是否在今天之前
    private int isBeforeCanMod;
    
    
    /**
     * 房型信息  add by shengwei.zuo 2009-11-07  begin
     */
    //房型面积
    private String  roomAcreage;
    
    //房型所在的楼层
    private String  roomFloors;
    
    //房间设施 
    private String roomEquipments;
    /**
     * 房型信息  add by shengwei.zuo 2009-11-07 end;
     */
	
    // 查询条件封装类
    private HotelOrderFromBean hotelOrderFromBean;
    
//  原始查询条件封闭类
    private QueryHotelForWebBean queryHotelForWebBean;
    //本类是系统启动时读取了jdbc.properties中系统值的类
    private ConfigParaBean configParaBean;
    
    private HotelManageWeb hotelManageWeb;
    
    private IQuotaManage quotaManage;
    
    private ContractManage contractManage;
    
    private IHotelService hotelService;
    
    private HotelRoomTypeService hotelRoomTypeService;
    
    //下单的service类 
    private HotelBookingService   hotelBookingService;
    
    // 酒店预订条款取出
    private ReservationAssist reservationAssist;
    
    // 担保条款、担保取消条款、预付取消条款
    private OrReservation reservation;
    
    /**
     * 币种符号
     */
    private String idCurStr;
    
    // 酒店附加服务
    private HotelAdditionalServe additionalServe = new HotelAdditionalServe();
    
    // 获取酒店促销信息
    private List<HtlPresale> presaleList = new ArrayList<HtlPresale>();
    
    // 查询价格列表
    private List priceLis = new ArrayList<QueryHotelForWebSaleItems>();
    
    //订单核对页面显示价格信息
    private List priceTemplist = new ArrayList();
    
    //日历控件在前台的显示
    private List calendarExtenderLst = new ArrayList();
    
  //日历控件在前台的显示
    private List calendarExtenderLstOne = new ArrayList();
    
    /**
     * 订单页面 日历 的年份，和月份  add by shengwei.zuo 2009-11-06 begin
     */
    
    private  Date currYearMonth;
    
    private  Date nextYearMonth;
    
    /**
     * 订单页面 日历 的年份，和月份  add by shengwei.zuo 2009-11-06  end
     */
    
	
    // 会员常用配送地址列表
    private List<OftenDeliveryAddress> oftenAddressList = new ArrayList<OftenDeliveryAddress>();
    
    /**
     * 用于保存香港中科获取的新价格，以便后续生成订单明细 ADD BY WUYUN 2009-04-21
     */
    private String hkPrices;

    /**
     * 用于保存香港中科获取的底价，以便后续生成订单明细
     */
    private String hkBasePrices;
    

    // 修改后的入住日期
    private Date begindate;

    // 修改后的离店日期
    private Date enddate;
    
    /**
     * v2.6 预订按钮显示 1表示显示，0表示灰显 仅在重新搜索时用到 add by wuyun 2009-06-11 15:30
     */
    private String bookButtonenAble;

    /**
     * v2.6 预订提示：在某日期之前预订，且要预订此房型，必须连住4晚，且入住日期必须包括某天。 不能预订的原因 仅在重新搜索时用到 add by wuyun 2009-06-11
     * 15:30
     */
    private String bookHintNotMeet;
    
    /**
     * 点击“下一步“的时候是否提示不可预订
     */
    private String falseButton;
    
    /**
     * 查询预付立减金额
     */
    private IBenefitService benefitService;
    
	/**
	 * 登陆会员，点击预订进入订单填写页面 add by shengwei.zuo 2009-11-04
	 */
	public String execute() {
		
		 Map params = super.getParams();
	        if (null == hotelOrderFromBean) {
	            hotelOrderFromBean = new HotelOrderFromBean();
	        }
	        MyBeanUtil.copyProperties(hotelOrderFromBean, params);
	        List fellowList = MyBeanUtil.getBatchObjectFromParam(params, OrFellowInfo.class, fellowNum);
	        hotelOrderFromBean.setFellowList(fellowList);
	        hotelOrderFromBean.setCheckinDate(begindate);
	        hotelOrderFromBean.setCheckoutDate(enddate);

	        // begin add by wuyun 2009-06-11 14:53 重新搜索需要重新取预订条款等信息
	        QueryHotelForWebBean queryHotelForWebBean = new QueryHotelForWebBean();
	        queryHotelForWebBean.setInDate(DateUtil.getDate(begindate));
	        queryHotelForWebBean.setOutDate(DateUtil.getDate(enddate));
	        queryHotelForWebBean.setCityId(hotelOrderFromBean.getCityId());
	        queryHotelForWebBean.setCityName(hotelOrderFromBean.getCityName());
	        queryHotelForWebBean.setHotelId(hotelOrderFromBean.getHotelId());
	        queryHotelForWebBean.setHotelName(hotelOrderFromBean.getHotelName());
	        queryHotelForWebBean.setPageIndex(1);
	        queryHotelForWebBean.setMinPrice("");
	        queryHotelForWebBean.setMaxPrice("");
	        queryHotelForWebBean.setPriceStr("");
	        queryHotelForWebBean.setFromChannel("web");
	        queryHotelForWebBean.setPriceOrder(0);
	        HotelPageForWebBean hotelPageForWebBean = hotelManageWeb
	            .queryHotelsForWeb(queryHotelForWebBean);
	        List webHotelResultLis = hotelPageForWebBean.getList();
	        
	        if(webHotelResultLis==null||webHotelResultLis.isEmpty()){
	        	
	        	return super.forwardError("该酒店在该段时间内没有相关预订信息。");
	        	
	        }

	        QueryHotelForWebResult result = (QueryHotelForWebResult) webHotelResultLis.get(0);
	        List<QueryHotelForWebRoomType> roomTypes = result.getRoomTypes();
	        QueryHotelForWebRoomType currentRoomType = new QueryHotelForWebRoomType();
	       
	       
	      //根据面转预的标记，判断判断是否转预付 add by shengwei.zuo 2010-1-7
//			if(hotelOrderFromBean.getPrepayToPay()!=null&&!"".equals(hotelOrderFromBean.getPrepayToPay())){
//				//如果是必须面转预，就得转为预付 add by shengwei.zuo 2010-1-7
//				if("1".equals(hotelOrderFromBean.getPrepayToPay().trim())){
//					hotelOrderFromBean.setPayToPrepay(true);
//					//hotelOrderFromBean.setPayMethod(PayMethod.PRE_PAY);
//				}
//			}
	        
	        if (hotelOrderFromBean.getPayMethod().equals("pay")) {
	            bookButtonenAble = currentRoomType.getBookButtonenAble();
	            bookHintNotMeet = currentRoomType.getBookHintNotMeet();
	        } else {
	            bookButtonenAble = currentRoomType.getBookButtonenAbleForPrepay();
	            bookHintNotMeet = currentRoomType.getBookHintNotMeetForPrepay();
	        }
	        //currentRoomType.isFalseButton()如果为false表示不可预订，为true则可以继续预订
	        if (!currentRoomType.isFalseButton()){
	        	falseButton = "0";
	        }
	        // end add by wuyun 2009-06-11 14:53
	        
	        //当没有优惠立减的时候，才可以这样设置,add by shengwei.zuo 2009-12-8
            if(hotelOrderFromBean.getIsReduction()==1&&hotelOrderFromBean.getPayMethod().equals("pay")
            		&&hotelOrderFromBean.getPayMethod().equals(hotelOrderFromBean.getAddPayMethod())
            		&&hotelOrderFromBean.getOrderPayType()==2){
            	hotelOrderFromBean.setPayToPrepay(true);
            }else{
            	//add by wuyun 2009-06-05 重新搜索时新的预订日期内有可能不再需要面转预	
            	hotelOrderFromBean.setPayToPrepay(false);
            }
            
	        needOvertimeAssure = false;
	        int days = DateUtil.getDay(hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean
	            .getCheckoutDate());
	        hotelOrderFromBean.setDays(days);
	        
	    
	      //房型信息  add by shengwei.zuo 
			HtlRoomtype roomTypeInfo = hotelRoomTypeService.getHtlRoomTypeByIdAndHtlId(hotelOrderFromBean.getHotelId(), 
				hotelOrderFromBean.getRoomTypeId());
			if(roomTypeInfo != null) {
				//房间有多少平米
				hotelOrderFromBean.setRoomAcreage(roomTypeInfo.getAcreage());
				//房间所属楼层
				hotelOrderFromBean.setRoomFloors(roomTypeInfo.getRoomFloor());
				//房间设施
				hotelOrderFromBean.setRoomEquipments(roomTypeInfo.getRoomEquipment());
			}
        
        try {
            // 获取会员信息
            member = getMemberInfoForWeb(true);
            
            // 交行全卡商旅等渠道预订必须要先登录会员
            if(null == member) {
            	return super.forwardError("请先登录会员");
            }
        } catch (Exception e) {
            log.error("getMemberInfoForWeb is Error" + e);
        }
      
        // 封装会员的Point类
        try {
            PointDTO pw = null;
            /*if (null != member) {
                pw = member.getPoint();
            }*/
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
		  
        double returnAmount = 0;
        if(null != member){
        	hotelOrderFromBean.setLoginBeforeBooking(true);
        	/*if(member.isFitFlag()){
    	        for (QueryHotelForWebRoomType roomType : roomTypes) {
    	            if (roomType.getRoomTypeId().equals("" + hotelOrderFromBean.getRoomTypeId())
    	                && roomType.getChildRoomTypeId().equals(
    	                    "" + hotelOrderFromBean.getChildRoomTypeId())) {
    	                currentRoomType = roomType;
    	                //累加返现金额
    	                if(PayMethod.PAY.equals(hotelOrderFromBean.getPayMethod())){
    	                	returnAmount += currentRoomType.getPayCashReturnAmount();
    	    	        }else{
    	    	        	returnAmount += currentRoomType.getCashReturnAmount();
    	    	        }
    	                break;
    	            }
    	        }
        	}*/
        }else{
        	log.info("================会员为空，返现清零！");
        }
        
        
        hotelOrderFromBean.setReturnAmount(returnAmount);
        /**
         * 如果是畅联酒店，需要重新调接口刷价格 add by shengwei.zuo 2009-12-22
         * 畅联为2  格林豪泰为5
         */
        if(hotelOrderFromBean.isChannelToWith()){
        	
        	String hdlPriceStr ="";
			try {
				hdlPriceStr = hotelBookingService.reReshReservateExResponse(hotelOrderFromBean.getHotelId(),
				        hotelOrderFromBean.getRoomTypeId(), String.valueOf(hotelOrderFromBean.getChildRoomTypeId()),
				        hotelOrderFromBean.getRoomChannel(),hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate());
			} catch (Exception e) {
				log.error("===========HotelBookResearchAction==========CHINAONLINE=========call HDL interfaces exception: ",e);
				setErrorMessage("该段时间内没有相关预订信息，请返回重新预订！");	            	
                return "forwardToError";
			}
        	
        	if( null!=hdlPriceStr && !"".equals(hdlPriceStr) && !hdlPriceStr.equals("hdlRefreshPriceFail")){
        		double hdlTotal = Double.parseDouble(hdlPriceStr);
        		log.info("HotelBookResearchAction =======CHINAONLINE=====hdlTotal-----"+hdlTotal);
        		// 需要比较原来的价格是否相等，如果原币种价格相等，则表示价格没有发生变化；如果不等，则需要打上已变化标记
        		if (0 != Double.compare(hotelOrderFromBean.getPriceNum().doubleValue(), hdlTotal)) {
	                    hotelOrderFromBean.setPriceChange(true);
	                    hotelOrderFromBean.setPriceNum(hdlTotal);
	            }
        	}else{
        		setErrorMessage("该段时间内没有相关预订信息，请返回重新预订！");	            	
                return "forwardToError";
        	}
        }
		  
	        /**
	         * 酒店提示信息 add by shengwei.zuo 2009-08-18 hotel2.9.2 begin
//	         */
	        String cauleInfo = hotelService.queryAlertInfoStr(hotelOrderFromBean.getHotelId(), String
	            .valueOf(hotelOrderFromBean.getChildRoomTypeId()), hotelOrderFromBean.getCheckinDate(),
	            hotelOrderFromBean.getCheckoutDate(), LocalFlag.HWEB);

	        cauleInfo = cauleInfo.trim();

	        if (null != cauleInfo && !cauleInfo.equals("")) {

	            isHasTip = 1;

	            cauleInfo = cauleInfo.replace("\r\n", "");

	            hotelOrderFromBean.setTipInfo(cauleInfo);
	        }

	        // 提示信息 add by shengwei.zuo 2009-08-18 hotel2.9.2 end
		  
	        //酒店的附加服务
	        additionalServe = hotelManageWeb.queryWebAdditionalServeInfo(hotelOrderFromBean
	                .getHotelId(), hotelOrderFromBean.getRoomTypeId(), hotelOrderFromBean.getCheckinDate(),
	                hotelOrderFromBean.getCheckoutDate(), hotelOrderFromBean.getPayMethod());
	        //判断是否为空
	        if (null != additionalServe && 0 == additionalServe.getBedServes().size()
	                && 0 == additionalServe.getBuffetServes().size()) {
	             additionalServe = null;
	        }
		  
	      //需另缴政府税的判断 add by shengwei.zuo 2009-12-1
			if(hotelOrderFromBean.getPayMethod() .equals(PayMethod.PRE_PAY) ||
			  (hotelOrderFromBean.getIsReduction()==1 && !hotelOrderFromBean.getAddPayMethod().equals(hotelOrderFromBean.getPayMethod())) ){
				
				 HtlContract contractEntity = contractManage.queryCurrentContractByHotelId(hotelOrderFromBean.getHotelId());
				 if (contractEntity != null) {
					 HtlTaxCharge taxChargeObj = hotelBookingService.getHaveTaxCharge(contractEntity.getID(), hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate());
					 if(taxChargeObj!=null && taxChargeObj.getRoomTaxName()!=null && !"".equals(taxChargeObj.getRoomTaxName())){
					     hotelOrderFromBean.setRoomIncTaxStr("(注：此价格不含"+taxChargeObj.getRoomTaxName()+")");
					 }
				 }
				
			}
	        
	        // 查询预定的价格列表
	        priceLis = hotelManageWeb.queryPriceForWeb(hotelOrderFromBean.getHotelId(),
	            hotelOrderFromBean.getRoomTypeId(), hotelOrderFromBean.getChildRoomTypeId(),
	            hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate(),
	            hotelOrderFromBean.getMinPrice(), hotelOrderFromBean.getMaxPrice(), hotelOrderFromBean
	                .getPayMethod(), hotelOrderFromBean.getQuotaType());
	        
	        if(priceLis==null||priceLis.isEmpty()){
	        	
	        	return super.forwardError("该房型在该段时间内没有设定价格，无法预订！");
	        	
	        }
	        
	        
	        if (null!=priceLis&&!priceLis.isEmpty()) {
	            firstDate = ((QueryHotelForWebSaleItems) priceLis.get(0)).getFellowDate();
	            lastDate = ((QueryHotelForWebSaleItems) priceLis.get(priceLis.size() - 1))
	                .getFellowDate();
	        }
	        
	        //连住优惠，更改销售价
	        priceLis = hotelBookingService.changeFavPrice(hotelOrderFromBean, priceLis);
	        
			//返回日历控件中的年份，月份，日期数组  add by shengwei.zuo 2009-11-04 
			HtlCalendarHelperBean calendarHelperObj  = new  HtlCalendarHelperBean();
			calendarHelperObj = hotelBookingService.getBookCalendarExtender();
			
			 //有连住优惠变价后的日期和售价list
	        List  changePireList = hotelBookingService.getFavChgPrice(priceLis);
			
			//入住日期和离店日期分专为字符串
			String  inDateStr  = DateUtil.dateToString(hotelOrderFromBean.getCheckinDate());
			String  outDateStr = DateUtil.dateToString(hotelOrderFromBean.getCheckoutDate());
			
			calendarHelperObj.setInDateStr(inDateStr);
			calendarHelperObj.setOutDateStr(outDateStr);
			calendarHelperObj.setHotelId(hotelOrderFromBean.getHotelId());
			calendarHelperObj.setRoomTypeId(hotelOrderFromBean.getRoomTypeId());
			calendarHelperObj.setChildRoomTypeId(hotelOrderFromBean.getChildRoomTypeId());
			calendarHelperObj.setQuotaType(hotelOrderFromBean.getQuotaType());
			calendarHelperObj.setPayMethod(hotelOrderFromBean.getPayMethod());
			calendarHelperObj.setLstFavChgPri(changePireList);
			
			//左边日历的list  add by shengwei.zuo 2009-11-06 
			calendarExtenderLstOne = hotelBookingService.getCalendarPrice(HtlCalendarStyle.Calendar_LEFT,calendarHelperObj);
			//右边日历的list  add by shengwei.zuo 2009-11-06 
			calendarExtenderLst = hotelBookingService.getCalendarPrice(HtlCalendarStyle.Calendar_RIGHT,calendarHelperObj);
			
			
			//左边的年份和月份。add by shengwei.zuo 2009-11-06 
			String currYearMonthStr = calendarHelperObj.getCaYear()+"-"+calendarHelperObj.getCaMonth()+"-01";
			hotelOrderFromBean.setCurrYearMonth(DateUtil.getDate(currYearMonthStr));
			//右边的年份和月份。add by shengwei.zuo 2009-11-06 
			String nextYearMonthStr = calendarHelperObj.getCaNextYear()+"-"+calendarHelperObj.getCaNextMonth()+"-01";
			hotelOrderFromBean.setNextYearMonth(DateUtil.getDate(nextYearMonthStr));
	        
	        double reTotal = 0.0;
	        if (null != priceLis) {
	            for (int k = 0; k < priceLis.size(); k++) {
	                QueryHotelForWebSaleItems it = (QueryHotelForWebSaleItems) priceLis.get(k);
	                if (0 <= it.getFellowDate().compareTo(hotelOrderFromBean.getCheckinDate())) {
	                    if (0 > it.getFellowDate().compareTo(hotelOrderFromBean.getCheckoutDate())) {
	                    	if(it.getSalePrice() <99998){
	                    		reTotal += it.getSalePrice();
	                    	}
	                        
	                    }
	                }
	                
	                //当没有优惠立减的时候，才可以这样设置,add by shengwei.zuo 2009-12-8
	                if(hotelOrderFromBean.getIsReduction()==1&&hotelOrderFromBean.getPayMethod().equals("pay")
	                		&&hotelOrderFromBean.getPayMethod().equals(hotelOrderFromBean.getAddPayMethod())
	                		&&hotelOrderFromBean.getOrderPayType()==1){
	                	hotelOrderFromBean.setPayToPrepay(false);
	                }else{
	                	// add by wuyun 2009-06-05 重新搜索时新的预订日期内有可能不再需要面转预
		                if ("pay".equals(hotelOrderFromBean.getPayMethod()) && 1 == it.getPayToPrepay()) {
		                    hotelOrderFromBean.setPayToPrepay(true);
		                }
	                }
	               
	            }
	            hotelOrderFromBean.setPriceNum(reTotal);
	            firstDate = ((QueryHotelForWebSaleItems) priceLis.get(0)).getFellowDate();
	            lastDate = ((QueryHotelForWebSaleItems) priceLis.get(priceLis.size() - 1))
	                .getFellowDate();
	        }

	        
	        //订单
	        order = new OrOrder();
	        order.setCheckinDate(hotelOrderFromBean.getCheckinDate());
	        order.setCheckoutDate(hotelOrderFromBean.getCheckoutDate());
	        order.setRoomTypeId(hotelOrderFromBean.getRoomTypeId());
	        order.setRoomQuantity(Integer.parseInt(hotelOrderFromBean.getRoomQuantity()));
	        order.setChildRoomTypeId(hotelOrderFromBean.getChildRoomTypeId());
	        order.setHotelId(hotelOrderFromBean.getHotelId());
	        // v2.6 必须面转预，订单的支付方式需要由面付改成预付 ADD BY WUYUN 2009-06-04
	        order.setPayMethod(hotelOrderFromBean.hasPayToPrepay() ? PayMethod.PRE_PAY : hotelOrderFromBean
	            .getPayMethod());
	        order.setPayToPrepay(hotelOrderFromBean.hasPayToPrepay());
	        
	        if (null != member) {
	            order.setMemberId(member.getId());
	            order.setMemberCd(member.getMembercd());
	        }

	        // add by zhineng.zhuang hotel2.6 包装预订条款
	        getReserva(hotelOrderFromBean, order);

	        // 获取会员常入住人和常联系人
	        if (null != member && member.isMango()) { // 114会员应该没有常入住人和常联系人
	            memberInterfaceService.getFellowAndLinkmanByMemberCd(member);
	            oftenFellowCount = member.getFellowList().size();
	        }
	        
	        // 获取酒店促销信息
	        presaleList = hotelManageWeb.queryPresalesForWeb(hotelOrderFromBean.getHotelId(),
	            hotelOrderFromBean.getCheckinDate());
	        
	        String[] priceStr = null;
	        /**
	         * 如果是香港中科酒店，需要重新调接口取价格 ADD BY WUYUN 2009-03-23
	         */
	        Date checkIn = hotelOrderFromBean.getCheckinDate();
	        Date checkOut = hotelOrderFromBean.getCheckoutDate();
	        if (hotelOrderFromBean.getPayMethod().equals("pre_pay")
	            && ChannelType.CHANNEL_CTS == hotelOrderFromBean.getRoomChannel() && hotelOrderFromBean.isFlagCtsHK()) {
	            priceLis = hotelManageWeb.queryPriceForWebHK(hotelOrderFromBean.getHotelId(),
	                hotelOrderFromBean.getRoomTypeId(), hotelOrderFromBean.getChildRoomTypeId(),
	                checkIn, DateUtil.getDate(checkOut, -1));	            
	            List<Date> liDate = hotelManageWeb.queryQtyForWebHK(hotelOrderFromBean.getHotelId(), 
	            		hotelOrderFromBean.getRoomTypeId(), checkIn, checkOut);
	            double reTotalHK = 0.0;
	            log.info("HotelBookResearchAction==============CTS===========hotelOrderFromBean.getPriceNum()-----"+hotelOrderFromBean.getPriceNum());
	            if (null != priceLis && days <= priceLis.size() && days <= liDate.size()) {
	                StringBuffer saleBuffer = new StringBuffer();
	                StringBuffer baseBuffer = new StringBuffer();
	                for (int k = 0; k < priceLis.size(); k++) {
	                    QueryHotelForWebSaleItems it = (QueryHotelForWebSaleItems) priceLis.get(k);
	                    saleBuffer.append(it.getSalePrice() + "#");
	                    baseBuffer.append(it.getBasePrice() + "#");
	                    reTotalHK += it.getSalePrice();
	                    log.info("HotelBookResearchAction==============CTS===========priceLis[k].salePrice:" + it.getSalePrice());
	                    log.info("HotelBookResearchAction==============CTS===========priceLis[k].basePrice:" + it.getBasePrice());
	                }
	                hkPrices = saleBuffer.toString();
	                hkBasePrices = baseBuffer.toString();
	                // 需要比较原币种(即港币)价格是否相等，如果原币种价格相等，则表示价格没有发生变化；如果不等，则需要打上已变化标记
	                if (0 != Double.compare(hotelOrderFromBean.getPriceNum().doubleValue(), reTotalHK)) {
	                    hotelOrderFromBean.setPriceChange(true);
	                    hotelOrderFromBean.setPriceNum(reTotalHK);
	                }
	       
//	                priceTemplist = FormartHwebUtil.setPriceTemplistUtil(priceLis, hotelOrderFromBean
//	                    .getCheckinDate(), hotelOrderFromBean.getCheckoutDate());
//	                StringBuffer sb = new StringBuffer();
//	                if (null != priceTemplist) {
//	                    for (int i = 0; i < priceTemplist.size(); i++) {
//	                        sb.append("" + ((AdditionalServeItem) priceTemplist.get(i)).getValidDate()
//	                            + "#" + ((AdditionalServeItem) priceTemplist.get(i)).getAmount() + "@");
//	                    }
//	                }
//	                //做不为空的判断，此处引起网站多处BUG，add by shengwei.zuo 2009-10-20
//	                if(sb!=null&&sb.length()>0){
//	                	hotelOrderFromBean.setDatePriceStr(sb.toString().substring(0, sb.lastIndexOf("@")));
//	                }
	                
	            } else {
	            	// 如果获取价格失败，则获取相关信息 add by chenkeming
	            	boolean chkPrice[] = new boolean[days];
	            	boolean chkQty[] = new boolean[days];
	            	boolean chkCanBook[] = new boolean[days];
	            	for(int i=0; i<days; i++) {
	            		chkCanBook[i] = false;
	            		chkPrice[i] = false;
	            		chkQty[i] = false;
	            	}
	            	for(Object item : priceLis) {
	            		QueryHotelForWebSaleItems saleItem = (QueryHotelForWebSaleItems)item;
	            		int i = DateUtil.getDay(checkIn, saleItem.getFellowDate());
	            		if(0 <= i && i < days) {
	            			chkPrice[i] = true;	
	            		}	            		
	            	}
	            	for(Date date : liDate) {
	            		int i = DateUtil.getDay(checkIn, date);
	            		if(0 <= i && i < days) {
	            			chkQty[i] = true;
	            			if(chkPrice[i]) {
	            				chkCanBook[i] = true;
	            			}
	            		}	            		
	            	}
	            	StringBuffer sbCanBook = new StringBuffer("");
	            	boolean bFirst = true;
	            	for(int i=0; i<days; i++) {
	            		if(chkCanBook[i]) {
	            			if(!bFirst) {
	            				sbCanBook.append(",");
	            			} else {
	            				bFirst = false;
	            			}	            				
	            			sbCanBook.append(DateUtil.toStringByFormat(DateUtil.getDate(checkIn, i), "MM-dd"));	
	            		}
	            	}
	            	String strMayBook = hotelManageWeb.getCanBookDatesForWebHK(
						hotelOrderFromBean.getHotelId(), hotelOrderFromBean
								.getRoomTypeId(), hotelOrderFromBean
								.getChildRoomTypeId(), checkIn, checkOut);
	            	StringBuffer sbMsg = new StringBuffer();
	            	sbMsg.append("对不起，有的日期无房或价格在调整，暂时不能预订。");
	            	if(0 < sbCanBook.length()) {
	            		sbMsg.append("但以下日期可以预订：" + sbCanBook.toString() + "。");
	            		if(StringUtil.isValidStr(strMayBook)) {
	            			sbMsg.append("此外，附近以下日期也可能有房，可以尝试预订：" + strMayBook + "。");
	            		}
	            	} else {
	            		if(StringUtil.isValidStr(strMayBook)) {
	            			sbMsg.append("附近以下日期可能有房，可以尝试预订：" + strMayBook + "。");
	            		}
	            	}
	            	setErrorMessage(sbMsg.toString());	            	
	                // setErrorMessage("对不起，请返回重新预订!");
	                return "forwardToError";
	            }
	        } else {
	        	
	        	//做不为空的判断，此处引起网站多处BUG，add by shengwei.zuo 2009-10-20
	        	if(hotelOrderFromBean.getDatePriceStr()!=null){
	        		
	                if (1 < hotelOrderFromBean.getDatePriceStr().indexOf("@")) {
	                    priceStr = hotelOrderFromBean.getDatePriceStr().split("@");
	                } else {
	                    priceStr = new String[] { hotelOrderFromBean.getDatePriceStr() };
	                }
	        		
	        	}
	        	
	        	if(priceStr!=null){
	            	
	                for (int i = 0; i < priceStr.length; i++) {
	                    AdditionalServeItem item = new AdditionalServeItem();
	                    String[] items = priceStr[i].split("#");
	                    item.setValidDate(items[0]);
	                    item.setAmount(Double.parseDouble(items[1]));
	                    priceTemplist.add(item);
	                }
	        		
	        	}
	        	
	        	//做不为空的判断，此处引起网站多处BUG，add by shengwei.zuo 2009-10-20

	        }

	        
	        // add by lixiaoyong v2.6 日历控件所需要的两个日期 2009-05-18 begin
	        String beginDateFu = "";
	        String endDateFu = "";

	        Calendar calendar = Calendar.getInstance();
	        beginDateFu = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
	            + calendar.get(Calendar.DAY_OF_MONTH);
	        // modify by shizhongwen 2009-07-08 如果为预付付款时限
	        if (null != this.reservation.getAdvancePayTime()) {
	            calendar.setTime(this.reservation.getAdvancePayTime());
	        } else if (null != order.getReservation()
	            && null != order.getReservation().getAdvancePayTime()) {
	            calendar.setTime(order.getReservation().getAdvancePayTime());
	        } else {
	            calendar.setTime(order.getCheckinDate());
	        }
	        endDateFu = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
	            + calendar.get(Calendar.DAY_OF_MONTH);
	        request.setAttribute("beginDateFu", beginDateFu);
	        request.setAttribute("endDateFu", endDateFu);
	        // add by lixiaoyong v2.6 日历控件所需要的两个日期 2009-05-18 end
        
	        //如果是预付立减则刷新预付立减的价格 add by huizhong.chen
	        /*if(1 == this.hotelOrderFromBean.getIsReduction()){
	            Date checkInDate = this.hotelOrderFromBean.getCheckinDate();
                Date checkOutDate = this.hotelOrderFromBean.getCheckoutDate();
	            String childRoomTypeId = new Long(this.hotelOrderFromBean.getChildRoomTypeId()).toString();
	            int roomQuantity = Integer.parseInt(this.hotelOrderFromBean.getRoomQuantity());
	            String currency = this.hotelOrderFromBean.getCurrency();
                int benefitAmount =  this.benefitService.calculateBenefitAmount(childRoomTypeId, checkInDate, checkOutDate, 1,currency);
	            this.hotelOrderFromBean.setBenefitAmount(benefitAmount);
	        }*/
	        
            return "bookingOrder";
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
            String rateStr = rateMap.get(order.getPaymentCurrency());
            if (null == rateStr) {
                rateStr = "1.0";
            }
            request.setAttribute("rateCurrency", rateStr);
            double rate = Double.valueOf(rateStr.trim()).doubleValue();
            order.setRateId(rate);
            reservation.setFirstPrice(priceFirstDay * rate);
            order.setSum(priceAllDay);
            order.setSumRmb(priceAllDay * rate);
            idCurStr = CurrencyBean.idCurMap.get(order.getPaymentCurrency());
        } else {
            reservation.setFirstPrice(priceFirstDay);
            order.setSum(priceAllDay);
            order.setSumRmb(priceAllDay);
        }

        // add by shengwei.zuo 2009-05-05 hotel V2.6 修复担保金额计算错误BUG end;

        // add by zhineng.zhuang hotel2.6 2009-03-01 end
        reservationAssist = orderService.loadBookClauseForWeb(order);
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



	public HotelOrderFromBean getHotelOrderFromBean() {
		return hotelOrderFromBean;
	}

	public void setHotelOrderFromBean(HotelOrderFromBean hotelOrderFromBean) {
		this.hotelOrderFromBean = hotelOrderFromBean;
	}

	public String getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}

	public String getHotelStar() {
		return hotelStar;
	}

	public void setHotelStar(String hotelStar) {
		this.hotelStar = hotelStar;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}


	public ConfigParaBean getConfigParaBean() {
		return configParaBean;
	}


	public void setConfigParaBean(ConfigParaBean configParaBean) {
		this.configParaBean = configParaBean;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public int getIsHasTip() {
		return isHasTip;
	}


	public void setIsHasTip(int isHasTip) {
		this.isHasTip = isHasTip;
	}


	public HotelManageWeb getHotelManageWeb() {
		return hotelManageWeb;
	}


	public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
		this.hotelManageWeb = hotelManageWeb;
	}


	public List getPriceLis() {
		return priceLis;
	}


	public void setPriceLis(List priceLis) {
		this.priceLis = priceLis;
	}


	public Date getFirstDate() {
		return firstDate;
	}


	public void setFirstDate(Date firstDate) {
		this.firstDate = firstDate;
	}


	public Date getLastDate() {
		return lastDate;
	}


	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}




	public int getOftenFellowCount() {
		return oftenFellowCount;
	}




	public void setOftenFellowCount(int oftenFellowCount) {
		this.oftenFellowCount = oftenFellowCount;
	}




	public int getIsBeforeCanMod() {
		return isBeforeCanMod;
	}




	public void setIsBeforeCanMod(int isBeforeCanMod) {
		this.isBeforeCanMod = isBeforeCanMod;
	}




	public HotelBookingService getHotelBookingService() {
		return hotelBookingService;
	}




	public void setHotelBookingService(HotelBookingService hotelBookingService) {
		this.hotelBookingService = hotelBookingService;
	}

	public ReservationAssist getReservationAssist() {
		return reservationAssist;
	}

	public void setReservationAssist(ReservationAssist reservationAssist) {
		this.reservationAssist = reservationAssist;
	}

	public HotelAdditionalServe getAdditionalServe() {
		return additionalServe;
	}




	public void setAdditionalServe(HotelAdditionalServe additionalServe) {
		this.additionalServe = additionalServe;
	}




	public List<HtlPresale> getPresaleList() {
		return presaleList;
	}




	public void setPresaleList(List<HtlPresale> presaleList) {
		this.presaleList = presaleList;
	}




	public List getPriceTemplist() {
		return priceTemplist;
	}




	public void setPriceTemplist(List priceTemplist) {
		this.priceTemplist = priceTemplist;
	}




	public List getCalendarExtenderLst() {
		return calendarExtenderLst;
	}




	public void setCalendarExtenderLst(List calendarExtenderLst) {
		this.calendarExtenderLst = calendarExtenderLst;
	}




	public List getCalendarExtenderLstOne() {
		return calendarExtenderLstOne;
	}




	public void setCalendarExtenderLstOne(List calendarExtenderLstOne) {
		this.calendarExtenderLstOne = calendarExtenderLstOne;
	}



	public Date getCurrYearMonth() {
		return currYearMonth;
	}




	public void setCurrYearMonth(Date currYearMonth) {
		this.currYearMonth = currYearMonth;
	}




	public Date getNextYearMonth() {
		return nextYearMonth;
	}




	public void setNextYearMonth(Date nextYearMonth) {
		this.nextYearMonth = nextYearMonth;
	}




	public String getRoomAcreage() {
		return roomAcreage;
	}




	public void setRoomAcreage(String roomAcreage) {
		this.roomAcreage = roomAcreage;
	}




	public String getRoomFloors() {
		return roomFloors;
	}




	public void setRoomFloors(String roomFloors) {
		this.roomFloors = roomFloors;
	}




	public String getRoomEquipments() {
		return roomEquipments;
	}




	public void setRoomEquipments(String roomEquipments) {
		this.roomEquipments = roomEquipments;
	}




	public OrReservation getReservation() {
		return reservation;
	}




	public void setReservation(OrReservation reservation) {
		this.reservation = reservation;
	}

	public String getIdCurStr() {
		return idCurStr;
	}

	public void setIdCurStr(String idCurStr) {
		this.idCurStr = idCurStr;
	}

	public List<OftenDeliveryAddress> getOftenAddressList() {
		return oftenAddressList;
	}

	public void setOftenAddressList(List<OftenDeliveryAddress> oftenAddressList) {
		this.oftenAddressList = oftenAddressList;
	}

	public String getHkPrices() {
		return hkPrices;
	}

	public void setHkPrices(String hkPrices) {
		this.hkPrices = hkPrices;
	}

	public String getHkBasePrices() {
		return hkBasePrices;
	}

	public void setHkBasePrices(String hkBasePrices) {
		this.hkBasePrices = hkBasePrices;
	}

	public Date getBegindate() {
		return begindate;
	}

	public void setBegindate(Date begindate) {
		this.begindate = begindate;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public String getBookButtonenAble() {
		return bookButtonenAble;
	}

	public void setBookButtonenAble(String bookButtonenAble) {
		this.bookButtonenAble = bookButtonenAble;
	}

	public String getBookHintNotMeet() {
		return bookHintNotMeet;
	}

	public void setBookHintNotMeet(String bookHintNotMeet) {
		this.bookHintNotMeet = bookHintNotMeet;
	}

	public IQuotaManage getQuotaManage() {
		return quotaManage;
	}

	public void setQuotaManage(IQuotaManage quotaManage) {
		this.quotaManage = quotaManage;
	}
	
	public QueryHotelForWebBean getQueryHotelForWebBean() {
		return queryHotelForWebBean;
	}

	public void setQueryHotelForWebBean(QueryHotelForWebBean queryHotelForWebBean) {
		this.queryHotelForWebBean = queryHotelForWebBean;
	}

	public String getFalseButton() {
		return falseButton;
	}

	public void setFalseButton(String falseButton) {
		this.falseButton = falseButton;
	}

	public void setContractManage(ContractManage contractManage) {
		this.contractManage = contractManage;
	}

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}
	
	public void setBenefitService(IBenefitService benefitService) {
        this.benefitService = benefitService;
    }

}

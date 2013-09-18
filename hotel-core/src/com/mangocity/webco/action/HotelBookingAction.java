package com.mangocity.webco.action;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import com.mangocity.hotel.ext.member.dto.PointDTO;
import com.mangocity.hdl.constant.ChannelType;
import com.mangocity.hotel.base.manage.ContractManage;
import com.mangocity.hotel.base.manage.HtlLimitFavourableManage;
import com.mangocity.hotel.base.manage.IQuotaManage;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlPresale;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.persistence.HtlTaxCharge;
import com.mangocity.hotel.base.persistence.OrReservation;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.base.service.IHotelFavourableReturnService;
import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.hotel.base.service.assistant.ReservationAssist;
import com.mangocity.hotel.base.util.WebStrUtil;
import com.mangocity.hotel.order.constant.LocalFlag;
import com.mangocity.hotel.order.persistence.OrFellowInfo;
import com.mangocity.hotel.order.persistence.OrFulfillment;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrPriceDetail;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.hweb.persistence.AdditionalServeItem;
import com.mangocity.hweb.persistence.HotelAdditionalServe;
import com.mangocity.hweb.persistence.OftenDeliveryAddress;
import com.mangocity.hweb.persistence.QueryHotelForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebSaleItems;
import com.mangocity.util.ConfigParaBean;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.webnew.persistence.HotelOrderFromBean;
import com.mangocity.webnew.persistence.HtlCalendarFav;
import com.mangocity.webnew.persistence.HtlCalendarHelperBean;
import com.mangocity.webnew.persistence.HtlCalendarStyle;
import com.mangocity.webnew.service.HotelBookingService;
import com.mangocity.webnew.util.action.GenericWebAction;


/**
 * 会员下单action
 * @author zuoshengwei
 *
 */
public class HotelBookingAction extends GenericWebAction {
	
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
    
    //下单的service类 
    private HotelBookingService   hotelBookingService;
    
    private IHotelService hotelService;
    
    private HotelRoomTypeService hotelRoomTypeService;
    
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
    
    private int fellowNum;
    
    private List fellowList;
    
    
    private String saleDepartmentPay;
    
    private String bizCode;
	private String bizName;
	private String bizFlag;

    
    
	public String getSaleDepartmentPay() {
		return saleDepartmentPay;
	}

	public void setSaleDepartmentPay(String saleDepartmentPay) {
		this.saleDepartmentPay = saleDepartmentPay;
	}

	public List getFellowList() {
		return fellowList;
	}

	public void setFellowList(List fellowList) {
		this.fellowList = fellowList;
	}

	public int getFellowNum() {
		return fellowNum;
	}

	public void setFellowNum(int fellowNum) {
		this.fellowNum = fellowNum;
	}

	
	/**
     * 现金返还服务接口 add by linpeng.fang 2009-09-29
     */
    private IHotelFavourableReturnService returnService;
    
    /**
     * 芒果限量返现活动促销service add by xiaojun.xiong 2011-3-4
     */
    private HtlLimitFavourableManage limitFavourableManage;
	
	/**
	 * 登陆会员，点击预订进入订单填写页面 add by shengwei.zuo 2009-11-04
	 */
	public String execute() {
		
		Map params = super.getParams();
		
		if (null == hotelOrderFromBean) {
			hotelOrderFromBean = new HotelOrderFromBean();
	    }
		//获取页面上的表单元素的属性值
		MyBeanUtil.copyProperties(hotelOrderFromBean, params);
		
		//入住日期和 离店日期
//		hotelOrderFromBean.setCheckinDate(DateUtil.getDate(hotelOrderFromBean.getCheckinDate())); 
//		hotelOrderFromBean.setCheckoutDate(DateUtil.getDate(hotelOrderFromBean.getCheckoutDate()));
		
	    /**在床型中去除满房状态的,并转化成BedType add by juesu.chen 2009-11-26 begin **/
	    String bedTypeNameStr = (String)params.get("bedTypeNameStr");
	    if(StringUtil.isValidStr(bedTypeNameStr)){
	    	String bedTypeStr = bedTypeNameStr.replace("/", ",").replace("床","").replace("大", "1").replace("双", "2").replace("单", "3");
	    	hotelOrderFromBean.setBedTypeStr(bedTypeStr);
	    }
	    //add by haibo.li 传入金融bug
//	    if(hotelOrderFromBean.getAvlprice()==0){
//	    	if(params.get("avlprice")!=null){
//	    		hotelOrderFromBean.setAvlprice(new Double(params.get("avlprice").toString()));
//	    	}
//        }
	    /**在床型中去除满房状态的,并转化成BedType add by juesu.chen 2009-11-26 end **/
		//获取入住的天数
		int days = DateUtil.getDay(hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate());
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
		
		//修改入住人bug add by haibo.li  网站改版2.10bug
		fellowList = MyBeanUtil.getBatchObjectFromParam(params, OrFellowInfo.class, fellowNum);
		hotelOrderFromBean.setFellowList(fellowList);
		
		//最大金额
		if (StringUtil.isValidStr(maxPrice)) {
			hotelOrderFromBean.setMaxPrice(Double.valueOf(maxPrice));
	    } else if (0 == Double.compare(hotelOrderFromBean.getMaxPrice(), 0.0)) {
	    	hotelOrderFromBean.setMaxPrice(999997.0d);
	    }
		
		//根据面转预的标记，判断判断是否转预付 add by shengwei.zuo 2010-1-7
		if(hotelOrderFromBean.getPrepayToPay()!=null&&!"".equals(hotelOrderFromBean.getPrepayToPay())){
			//如果是必须面转预，就得转为预付 add by shengwei.zuo 2010-1-7
			if("1".equals(hotelOrderFromBean.getPrepayToPay().trim())){
				hotelOrderFromBean.setPayToPrepay(true);
				hotelOrderFromBean.setPrepayToPay("1");
				//hotelOrderFromBean.setPayMethod(PayMethod.PRE_PAY);
			}
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
	    
	    //酒店星级
//        if (null != hotelStar) {
//            hotelOrderFromBean.setHotelStar(Float.parseFloat(hotelStar));
//        }
		
        //酒店汇率
        try {
            String rateStr = (String) params.get("rateStr");
            rate = Double.parseDouble(rateStr);
            putSession("rateStr", rateStr);
        } catch (Exception ex) {
            rate = 1;
            log.error("HotelBookingAction execute get rate error,the cause is " + ex);
        }
        
        //根据Cookie中的值，显示上一次预订用户的联系人信息 add by shengwei.zuo 2009-11-30
        //setLinkManInfo();
        
        //把hotelOrderFromBean 塞到 session 里面
        putSession("hotelOrderFromBean", hotelOrderFromBean);
        
        try {
            // 获取会员信息
        	member = getMemberInfoForWeb(true);
            
            // 交行全卡商旅等渠道，必须要登录会员
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
        
        //判断会员是否为芒果散客,用于现金返还判断
        hotelOrderFromBean.setReturnAmount(0d);
        
        /**
         * 如果是畅联酒店，需要重新调接口刷价格 add by shengwei.zuo 2009-12-22
         */
        if(hotelOrderFromBean.isChannelToWith()){
        	
        	String hdlPriceStr ="";
			try {
				hdlPriceStr = hotelBookingService.reReshReservateExResponse(hotelOrderFromBean.getHotelId(),
				        hotelOrderFromBean.getRoomTypeId(), String.valueOf(hotelOrderFromBean.getChildRoomTypeId()),
				        hotelOrderFromBean.getRoomChannel(),hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate());
			} catch (Exception e) {
				log.error("===========HotelBookingAction==========CHINAONLINE=========call HDL interfaces exception: ",e);
				setErrorMessage("该段时间内没有相关预订信息，请返回重新预订！");	            	
                return "forwardToError";
			}
        	
        	log.info("HotelBookingAction==========CHINAONLINE===========hdlPriceStr----" + hdlPriceStr);
        	
        	if( null!=hdlPriceStr && !"".equals(hdlPriceStr) && !hdlPriceStr.equals("hdlRefreshPriceFail")){
        		double hdlTotal = Double.parseDouble(hdlPriceStr);
        		log.info("HotelBookingAction==========CHINAONLINE===========hdlTotal----" + hdlTotal);
        		// 需要比较原来的价格是否相等，如果原币种价格相等，则表示价格没有发生变化；如果不等，则需要打上已变化标记
        		if (0 != Double.compare(hotelOrderFromBean.getPriceNum().doubleValue(), hdlTotal)) {
	                    hotelOrderFromBean.setPriceChange(true);
	                    hotelOrderFromBean.setPriceNum(hdlTotal);
	            }
        	}else{
        		log.info("HotelBookingAction==========CHINAONLINE==========该段时间内没有相关预订信息，请返回重新预订");
        		setErrorMessage("该段时间内没有相关预订信息，请返回重新预订！");	            	
                return "forwardToError";
        	}
        }
        
		  
	        /**
	         * 酒店提示信息 add by shengwei.zuo 2009-08-18 hotel2.9.2 begin
	         */
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
	        String maxPersons = (String)params.get("maxPersons");
        	if(null != maxPersons && !"".equals(maxPersons)){
        		maxPersons = maxPersons.trim();
        		hotelOrderFromBean.setMaxPersons(Integer.parseInt(maxPersons));
        	}
        	if(null == hotelOrderFromBean.getRoomQuantity()){
        		hotelOrderFromBean.setRoomQuantity("1");
        	}
	      
	        // 查询预定的价格列表
	        priceLis = hotelManageWeb.queryPriceForWeb(hotelOrderFromBean.getHotelId(),
	            hotelOrderFromBean.getRoomTypeId(), hotelOrderFromBean.getChildRoomTypeId(),
	            hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate(),
	            hotelOrderFromBean.getMinPrice(), hotelOrderFromBean.getMaxPrice(), hotelOrderFromBean
	                .getPayMethod(), hotelOrderFromBean.getQuotaType());
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
			List<HtlCalendarFav> changePireList = hotelBookingService.getFavChgPrice(priceLis);
			
			
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
	        
	        //订单
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
	            double reTotal = 0.0;
	            log.info("HotelBookingAction==========CTS===========hotelOrderFromBean.getPriceNum()-----"+hotelOrderFromBean.getPriceNum());
	            if (null != priceLis && days <= priceLis.size() && days <= liDate.size()) {
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
	                hkPrices = saleBuffer.toString();
	                hkBasePrices = baseBuffer.toString();
	                // 需要比较原币种(即港币)价格是否相等，如果原币种价格相等，则表示价格没有发生变化；如果不等，则需要打上已变化标记
	                if (0 != Double.compare(hotelOrderFromBean.getPriceNum().doubleValue(), reTotal)) {
	                    hotelOrderFromBean.setPriceChange(true);
	                    hotelOrderFromBean.setPriceNum(reTotal);
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
	            	StringBuilder sbMsg = new StringBuilder();
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
	        
	        
	        // 允许取消修改的时间跟系统时间比较 add by guzhijie 2009-08-13
	        if (null != reservationAssist && null != hotelOrderFromBean.getPayMethod()
	            && "pay".equals(hotelOrderFromBean.getPayMethod())
	            && null != reservationAssist.getCancmodiType()
	            && !("1").equals(reservationAssist.getCancmodiType())
	            && !("2").equals(reservationAssist.getCancmodiType())
	            && !("3").equals(reservationAssist.getCancmodiType())) {
	            String earliestDate = DateUtil.dateToString(reservationAssist.getEarliestNoPayDate());
	            String earliestTime = reservationAssist.getEarliestNoPayTime();
	            Date formatEarliestDate = DateUtil.stringToDatetime(earliestDate + " " + earliestTime);
	            isBeforeCanMod = Float
	                .valueOf(
	                    ((float) (formatEarliestDate.getTime() - Calendar.getInstance().getTime()
	                        .getTime()) / 86400000 + 1)).intValue();
	        }
		
	        OrFulfillment orFulfillment = new OrFulfillment();
			MyBeanUtil.copyProperties(orFulfillment, params);
			request.setAttribute("orFulfillment", orFulfillment);
		  	
	        /** hotel2.9.3 代金券需要传递酒店相关基本信息 add by chenjiajie 2009-09-15 begin **/
	        HtlHotel htlHotel = hotelService.findHotel(hotelOrderFromBean.getHotelId());
	        request.setAttribute("htlHotel", htlHotel);
	        /** hotel2.9.3 代金券需要传递酒店相关基本信息 add by chenjiajie 2009-09-15 end **/
	        
	        /** 刷新最新的返现金额 **/
	        /*long start = System.currentTimeMillis();
	        refreshCashReturnAmount(hotelOrderFromBean,priceLis);
	        long end = System.currentTimeMillis();
	        log.info("时间："+(end-start));*/
	        
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
    
    //根据Cookie中的值，显示上一次预订用户的联系人信息 add by shengwei.zuo 2009-11-30
    public void setLinkManInfo(){
    	
        //取出cookie里面的值 add by shengwei.zuo 
		Cookie[] cookies = request.getCookies();
		if(cookies!=null)
		{
		    for (int i = 0; i < cookies.length; i++) 
		    {
		       Cookie c = cookies[i];     
		       if(c.getName().equalsIgnoreCase("linkManCookie"))
		       {	
		    	    if(c.getValue()!=null&&!"".equals(c.getValue())){
		    	    	 hotelOrderFromBean.setLinkMan(c.getValue());
		    	    }
		        }
		        else if(c.getName().equalsIgnoreCase("confirmtypeCookie"))
		        {
		        	
		        	if(c.getValue()!=null&&!"".equals(c.getValue())){
		    	    	 hotelOrderFromBean.setConfirmtype(c.getValue());
		    	    }
		        	
		        } else if(c.getName().equalsIgnoreCase("mobileCookie"))
		        {
		        	if(c.getValue()!=null&&!"".equals(c.getValue())){
		    	    	 hotelOrderFromBean.setMobile(c.getValue());
		    	    }
		        	
		        }   else if(c.getName().equalsIgnoreCase("fixedDistrictNumCookie"))
		        {
		        	if(c.getValue()!=null&&!"".equals(c.getValue())){
		    	    	 hotelOrderFromBean.setFixedDistrictNum(c.getValue());
		    	    }
		        	
		        }   else if(c.getName().equalsIgnoreCase("fixedPhoneCookie"))
		        {
		        	if(c.getValue()!=null&&!"".equals(c.getValue())){
		    	    	 hotelOrderFromBean.setFixedPhone(c.getValue());
		    	    }
		        }   else if(c.getName().equalsIgnoreCase("fixedExtensionCookie"))
		        {
		        	if(c.getValue()!=null&&!"".equals(c.getValue())){
		    	    	 hotelOrderFromBean.setFixedExtension(c.getValue());
		    	    }
		        }   else if(c.getName().equalsIgnoreCase("emailCookie"))
		        {
		        	if(c.getValue()!=null&&!"".equals(c.getValue())){
		    	    	 hotelOrderFromBean.setEmail(c.getValue());
		    	    }
		        }   else if(c.getName().equalsIgnoreCase("faxDistrictNumCookie"))
		        {
		        	if(c.getValue()!=null&&!"".equals(c.getValue())){
		    	    	 hotelOrderFromBean.setFaxDistrictNum(c.getValue());
		    	    }
		        }   else if(c.getName().equalsIgnoreCase("faxPhoneCookie"))
		        {
		        	if(c.getValue()!=null&&!"".equals(c.getValue())){
		    	    	 hotelOrderFromBean.setFaxPhone(c.getValue());
		    	    }
		        }else if(c.getName().equalsIgnoreCase("faxExtensionCookie"))
		        {
		        	if(c.getValue()!=null&&!"".equals(c.getValue())){
		    	    	 hotelOrderFromBean.setFaxExtension(c.getValue());
		    	    }
		        }          
		    } 
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

	public QueryHotelForWebBean getQueryHotelForWebBean() {
		return queryHotelForWebBean;
	}

	public void setQueryHotelForWebBean(QueryHotelForWebBean queryHotelForWebBean) {
		this.queryHotelForWebBean = queryHotelForWebBean;
	}
	
	public IQuotaManage getQuotaManage() {
		return quotaManage;
	}

	public void setQuotaManage(IQuotaManage quotaManage) {
		this.quotaManage = quotaManage;
	}

	public String getBizCode() {
		return bizCode;
	}

	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}

	public String getBizName() {
		return bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}

	public String getBizFlag() {
		return bizFlag;
	}

	public void setBizFlag(String bizFlag) {
		this.bizFlag = bizFlag;
	}

	public void setHotelService(IHotelService hotelService) {
		this.hotelService = hotelService;
	}

	public void setContractManage(ContractManage contractManage) {
		this.contractManage = contractManage;
	}

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}
	
	public IHotelFavourableReturnService getReturnService() {
		return returnService;
	}

	public void setReturnService(IHotelFavourableReturnService returnService) {
		this.returnService = returnService;
	}
	
	public HtlLimitFavourableManage getLimitFavourableManage() {
		return limitFavourableManage;
	}

	public void setLimitFavourableManage(
			HtlLimitFavourableManage limitFavourableManage) {
		this.limitFavourableManage = limitFavourableManage;
	}
}

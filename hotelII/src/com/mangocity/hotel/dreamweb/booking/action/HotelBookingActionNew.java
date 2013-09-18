package com.mangocity.hotel.dreamweb.booking.action;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;

import com.mangocity.hdl.constant.ChannelType;
import com.mangocity.hotel.base.manage.HtlLimitFavourableManage;
import com.mangocity.hotel.base.persistence.HtlElAssure;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlHotelExt;
import com.mangocity.hotel.base.persistence.HtlPresale;
import com.mangocity.hotel.base.persistence.OrReservation;
import com.mangocity.hotel.base.resource.ResourceManager;
import com.mangocity.hotel.base.service.ChannelCashBackManagerService;
import com.mangocity.hotel.base.service.IHotelFavourableReturnService;
import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.hotel.base.service.assistant.ReservationAssist;
import com.mangocity.hotel.base.util.WebStrUtil;
import com.mangocity.hotel.dreamweb.datacheck.service.BookingDataCheckService;
import com.mangocity.hotel.dreamweb.displayvo.AssureInfoVO;
import com.mangocity.hotel.dreamweb.displayvo.BreakfastAndPriceItemVO;
import com.mangocity.hotel.dreamweb.displayvo.handler.impl.DispayVoHandlerImpl;
import com.mangocity.hotel.dreamweb.orderrecord.service.BookOrderRecordService;
import com.mangocity.hotel.dreamweb.priceUtil.HotelRereshPrice;
import com.mangocity.hotel.dreamweb.search.dao.HotelBookDao;
import com.mangocity.hotel.dreamweb.search.service.HotelBookService;
import com.mangocity.hotel.dreamweb.search.service.HotelInfoService;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.ext.member.dto.PointDTO;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrPreSale;
import com.mangocity.hotel.order.persistence.OrPriceDetail;
import com.mangocity.hotel.order.service.HotelElOrderService;
import com.mangocity.hotel.order.service.IHotelReservationInfoService;
import com.mangocity.hotel.orderrecord.model.OrderRecord;
import com.mangocity.hotel.search.constant.PayMethod;
import com.mangocity.hotel.search.handler.impl.HotelQueryHandler;
import com.mangocity.hotel.search.model.HotelBasicInfo;
import com.mangocity.hotel.search.util.PriceUtil;
import com.mangocity.hotel.search.vo.HotelResultForWebVO;
import com.mangocity.hotel.search.vo.SaleItemVO;
import com.mangocity.hweb.persistence.HotelAdditionalServe;
import com.mangocity.hweb.persistence.OftenDeliveryAddress;
import com.mangocity.hweb.persistence.QueryHotelForWebSaleItems;
import com.mangocity.proxy.vo.MembershipVO;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.log.MyLog;
import com.mangocity.util.web.CookieUtils;
import com.mangocity.webnew.persistence.ElongAssureResult;
import com.mangocity.webnew.persistence.HotelOrderFromBean;
import com.mangocity.webnew.service.CheckElongAssureService;
import com.mangocity.webnew.service.HotelBookingService;
import com.mangocity.webnew.util.action.GenericWebAction;
import com.opensymphony.xwork2.ActionContext;
public class HotelBookingActionNew extends GenericWebAction {

	//进来的参数
	private String priceTypeId;
	private String payMethod;
	private String inDate;
	private String outDate;
	private String source;

	//输出的参数
	private HotelAdditionalServe additionalServe; // 酒店附加服务
	private List<HtlPresale> presaleList; // 获取酒店促销信息
	private List<QueryHotelForWebSaleItems> priceLis ; // 获取酒店促销信息
	private ReservationAssist reservationAssist; // 酒店预订条款取出
	//private String  bookhintAssureStr ;
	private String bookhintCancelAndModifyStr;
	private List<SaleItemVO> saleItemVOList; //价格详情展示
	private HotelResultForWebVO hotelVO = new HotelResultForWebVO() ;// 酒店展示用的酒店星级展示
	private HotelBasicInfo hotelBasicInfo;// 提供酒店的信息 add by diandian.hou 2011-5-25
	private List<String> freeServiceToShow; //免费服务
	private List<String> roomEquipToShow; //客房设施
	private List<String> mealAndFixtureToShow; //休闲设施
	private List<BreakfastAndPriceItemVO> breakfastAndPriceItemVOList;//价格和早餐显示
	private int days;
	private String weekOfInDate; //获取入住日期是星期几
	private String weekOfOutDate;
	private Map<String,Integer>  bedTypeMinQuatoMap;

	private AssureInfoVO assureInfoVO;//酒店担保信息提示展示
	private HotelElOrderService hotelElOrderService;
	
	private List<OrPreSale> roomPresaleList;//酒店房型促销信息，小礼包
	private int fagChangeDate = 0;//标记是否修改日期
	// 会员
	private List<OftenDeliveryAddress> oftenAddressList = new ArrayList<OftenDeliveryAddress>(); // 会员常用配送地址列表

	//注入的service
	private HotelBookService hotelBookService;// 新的bookService
	private HotelBookingService hotelBookingService; //旧的bookingSerivce
	private IHotelService hotelService;
	private HotelInfoService hotelInfoService;
	private IHotelFavourableReturnService returnService; //现金返还服务接口 add by linpeng.fang 2009-09-29 
	//计算限量返现
	private HtlLimitFavourableManage limitFavourableManage;
	private HotelBookDao hotelBookDao;
	private ResourceManager resourceManager;	
	private IHotelReservationInfoService hotelReservationInfoService;
	private HotelRereshPrice hotelRereshPrice;
	private CheckElongAssureService checkElongAssureService;
	private BookingDataCheckService bookingDataCheckService;
	private BookOrderRecordService bookOrderRecordService;
    /**
     * 返利查询service
     */
	public ChannelCashBackManagerService channelCashBackService;
	
	//记录去其它渠道过来，而且在酒店详情页点击预订按钮的
	private long logId;
	
	

	//add by diandian.hou 2011-11-30
	private String projectcode;
	//错误信息
	private static final String ERROR_MESSAGE = "很抱歉，该段时间内没有相关预订信息！请致电芒果网客服电话40066-40066或者重新选择别的酒店！";
	private static final MyLog log = MyLog.getLogger(HotelBookingActionNew.class);
	
	private Date afterCheckInDate;//入住日期的后一天，有用担保的提示展示
	
	private double roomprice; //去哪儿渠道传递价格
	
	private String roomType; //去哪儿渠道传递房型
	//判断是否登录的标识
	private boolean whetherLogin=false;
	
	//渠道返现控制比例 add by hushunqiang
	private double cashbackratevalue; 
	String mbrId;
	String telephone;
	String email;
	public String result;
	public String execute() {
		
		/*
		try {
		
			
		if(logId!=0 && (projectcode!=null && !"".equals(projectcode))){
			hotelManageWeb.updateChannelLog(logId);
		}
		}catch(Exception e){
			log.error("updateChannelLog has a wrong", e);
		}
		*/
	   			
		double priceSum=0;		
		if(validateInputParam()){
			super.setErrorCode("HB15");
			return super.forwardError(ERROR_MESSAGE);
		}
		switchPayMethod();
	
		if(!StringUtil.isValidStr(priceTypeId)){
			super.setErrorCode("HB00");
			return super.forwardError(ERROR_MESSAGE);
		}
		
		try {
			initOrderBean();		
			days = DateUtil.getDay(hotelOrderFromBean.getCheckinDate(),  hotelOrderFromBean.getCheckoutDate());			
			afterCheckInDate=DateUtil.stringToDateMain(DateUtil.addStringDateALL(inDate, 1),"yyyy-MM-dd");
			hotelBookService.addsomeInfoToOrderBean(hotelOrderFromBean);
			//过滤hotel active不为1的 add by diandian.hou
			HtlHotel htlHotel_temp = hotelManageWeb.findHotel(hotelOrderFromBean.getHotelId());
			if(!"1".equals(htlHotel_temp.getActive())){
				return super.forwardError(ERROR_MESSAGE);
			}//过滤hotel 停止售卖
			for(HtlHotelExt hotelExt : htlHotel_temp.getHtelHotelExt()){
				if("1".equals(hotelExt.getIsStopSell())){
					if(DateUtil.between(new Date(), hotelExt.getStopSellBeginDate(), hotelExt.getStopSellEndDate())){
						return super.forwardError(ERROR_MESSAGE);
					}
				}
			}
		} catch (Exception e) {
			log.error("init hotelOrderFromBean has a wrong", e);
			super.setErrorCode("HB01");
			return super.forwardError(ERROR_MESSAGE);
		}
		
		addLogInfo(hotelOrderFromBean,"hotelBookingActionNew ");
		
		saveOrderRecord();//保存预订流程相关的信息，add by ting.li
		
		
		try {
			
	   //过滤掉满房的床型
		String bedTypeStr=bookingDataCheckService.filterFullBedType(hotelOrderFromBean);
		hotelOrderFromBean.setBedTypeStr(bedTypeStr);
			
		//校验是否满房，房态不可控，配额为0
		if(hotelOrderFromBean.getBedTypeStr()==null||"".equals(hotelOrderFromBean.getBedTypeStr())){
			String errerInfo=getBookHotelInfo(hotelOrderFromBean);
			log.error(errerInfo);
			super.setErrorCode("HB02");
			return super.forwardError(ERROR_MESSAGE);
		}
		
		
		bedTypeMinQuatoMap=bookingDataCheckService.getBedTypeMinQuato(hotelOrderFromBean);			
		
		} catch (Exception e) {
			log.error("HotelBookingAction isRoomFull error", e);
		}
		//判断是否可订
		if (!hotelOrderFromBean.isFlag_canbook()) {
			super.setErrorCode("HB03");
			String errorMessage = ERROR_MESSAGE;
			String canntBookReason = hotelOrderFromBean.getCanntBookReason();
			if (StringUtil.isValidStr(canntBookReason)) {
				errorMessage = canntBookReason;
			}
			return super.forwardError(errorMessage);
		}

		//畅联的
		if (hotelOrderFromBean.getRoomChannel() != 0 && hotelOrderFromBean.getRoomChannel() != ChannelType.CHANNEL_CTS) {
			String hdlPriceStr = "";
			try {
				boolean isActive = hotelBookDao.getIsActive(Long.valueOf(hotelOrderFromBean.getChildRoomTypeId()), hotelOrderFromBean.getRoomChannel());
				if (!isActive) {
					super.setErrorCode("HB13");
					return super.forwardError(ERROR_MESSAGE);
				}
				hdlPriceStr = hotelBookingService.reReshReservateExResponse(hotelOrderFromBean.getHotelId(), hotelOrderFromBean.getRoomTypeId(), String
						.valueOf(hotelOrderFromBean.getChildRoomTypeId()), hotelOrderFromBean.getRoomChannel(), hotelOrderFromBean.getCheckinDate(),
						hotelOrderFromBean.getCheckoutDate());
			} catch (Exception e) {
				String errerInfo=getBookHotelInfo(hotelOrderFromBean);
				log.error("===========HotelBookingAction==========CHINAONLINE=========call HDL interfaces exception: "+errerInfo, e);
				super.setErrorCode("HB04");
				setErrorMessage(ERROR_MESSAGE);
				return "forwardToError";
			}
			log.info("HotelBookingAction==========CHINAONLINE===========hdlPriceStr----" + hdlPriceStr);
			if (null != hdlPriceStr && !"".equals(hdlPriceStr) && !hdlPriceStr.equals("hdlRefreshPriceFail")) {
				priceSum = Double.parseDouble(hdlPriceStr);
				log.info("HotelBookingAction==========CHINAONLINE===========hdlTotal----" + priceSum);				
				//hotelOrderFromBean.setPriceNum(hdlTotal);
			} else { 
				String errerInfo=getBookHotelInfo(hotelOrderFromBean);
				log.error(errerInfo);
				super.setErrorCode("HB05");				
				return super.forwardError(ERROR_MESSAGE);
			}
		}
		//中旅的
		/**
		 * 如果是香港中科酒店，需要重新调接口取价格 ADD BY WUYUN 2009-03-23
		 */
	
		if (hotelOrderFromBean.getPayMethod().equals("pre_pay") && ChannelType.CHANNEL_CTS == hotelOrderFromBean.getRoomChannel()) {

			//调HDL接口
			try {
				boolean isActive = hotelBookDao.getIsActive(Long.valueOf(hotelOrderFromBean.getChildRoomTypeId()), hotelOrderFromBean.getRoomChannel());
				if (!isActive) {
					String errerInfo=getBookHotelInfo(hotelOrderFromBean);
					log.error(errerInfo);
					super.setErrorCode("HB14");
					return super.forwardError(ERROR_MESSAGE);
				}

				priceLis = hotelBookingService.refreshHotelReservateExResponse(hotelOrderFromBean.getHotelId(), hotelOrderFromBean.getRoomTypeId(),
						hotelOrderFromBean.getChildRoomTypeId(), hotelOrderFromBean.getRoomChannel(), hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean
								.getCheckoutDate());
			} catch (Exception e) {
				String errerInfo=getBookHotelInfo(hotelOrderFromBean);
				log.error("===========HotelBookingAction==========CTS=========call HDL interfaces exception: "+errerInfo, e);
				super.setErrorCode("HB06");
				return super.forwardError(ERROR_MESSAGE);
			}
			if (null != priceLis && days <= priceLis.size()) {
				double reTotal = 0.0;
				StringBuffer saleBuffer = new StringBuffer();
				StringBuffer baseBuffer = new StringBuffer();
				for (int k = 0; k < priceLis.size(); k++) {
					QueryHotelForWebSaleItems it = (QueryHotelForWebSaleItems) priceLis.get(k);
					saleBuffer.append(it.getSalePrice() + "#");
					baseBuffer.append(it.getBasePrice() + "#");
					reTotal += it.getSalePrice();
				}			
				priceSum=reTotal;
				log.info("HotelBookingAction==========CTS===========hdlTotal----" + reTotal);
			} else {
				String errerInfo=getBookHotelInfo(hotelOrderFromBean);
				log.error(errerInfo);
				super.setErrorCode("HB07");			
				return super.forwardError(ERROR_MESSAGE);
			}
		}
		
		
		List<QueryHotelForWebSaleItems> queryPriceList=null;
		
		try {
			//查询每天的价格，计算担保金额等等
			queryPriceList = hotelManageWeb.queryPriceDetailForWeb(hotelOrderFromBean.getHotelId(), hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate(), 
					hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate(), hotelOrderFromBean.getRoomTypeId(), hotelOrderFromBean.getChildRoomTypeId(), hotelOrderFromBean.getPayMethod());
			
			//如果查不到某一天的价格，就返回错误页面			
			if(queryPriceList==null || queryPriceList.size()<days){
				String errerInfo=getBookHotelInfo(hotelOrderFromBean);
				log.error(errerInfo);
				super.setErrorCode("HB08");
				return super.forwardError(ERROR_MESSAGE);
			}
		
					
			// 查询预定的价格列表
		
			priceLis = hotelManageWeb.queryPriceForWeb(hotelOrderFromBean.getHotelId(), hotelOrderFromBean.getRoomTypeId(), hotelOrderFromBean
					.getChildRoomTypeId(), hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate(), hotelOrderFromBean.getMinPrice(),
					hotelOrderFromBean.getMaxPrice(), hotelOrderFromBean.getPayMethod(), hotelOrderFromBean.getQuotaType());
					
			if (priceLis == null || priceLis.size() == 0) {
				super.setErrorCode("HB09");
				String errerInfo=getBookHotelInfo(hotelOrderFromBean);
				log.error(errerInfo);
				setErrorMessage(ERROR_MESSAGE);
				return "forwardToError";
			}
								
			//连住优惠，更改销售价
			priceLis = hotelBookingService.changeFavPrice(hotelOrderFromBean, priceLis);
			
			// 获取酒店促销信息
			//presaleList = hotelManageWeb.queryPresalesForWeb(hotelOrderFromBean.getHotelId(), hotelOrderFromBean.getCheckinDate());
						
		} catch (Exception e) {
			log.error("hotelBookingAction query price error:", e);
			
		}

		//查询酒店相关的信息
		try {
			// 酒店星级展示用的
			hotelBasicInfo = hotelInfoService.queryHotelInfoByHotelId(String.valueOf(hotelOrderFromBean.getHotelId()));
			HtlHotel htlHotel = hotelService.findHotel(hotelOrderFromBean.getHotelId());// 获取酒店免费服务、酒店服务设施等其他备注项的内容
			setSomeValueToHotelVO(hotelVO, hotelBasicInfo, htlHotel);
			
			//EL酒店级别提示信息 add by lgm 2013年5月15日 
			if(hotelOrderFromBean.getRoomChannel() == ChannelType.CHANNEL_ELONG){
				String hotelAlertMsg = htlHotel.getAlertMessage();
				request.setAttribute("hotelAlertMsg", hotelAlertMsg);
			}
			// 酒店的附加服务
			additionalServe = hotelManageWeb.queryWebAdditionalServeInfo(hotelOrderFromBean.getHotelId(), hotelOrderFromBean.getRoomTypeId(),
					hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate(), hotelOrderFromBean.getPayMethod());
			// 判断是否为空
			if (null != additionalServe && 0 == additionalServe.getBedServes().size() && 0 == additionalServe.getBuffetServes().size()) {
				additionalServe = null;
			}
		} catch (Exception e) {
			log.error("hotelBookingAction query information of hotel +:" + String.valueOf(hotelOrderFromBean.getHotelId()) + " has a wrong ", e);
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
				whetherLogin=true;
				pw = member.getPoint();
			}
			//获得积分余额
			hotelOrderFromBean.setAbleUlmPoint(null != pw ? pw.getBalanceValue() : "0");
		} catch (Exception e) {
			//获得积分余额
			hotelOrderFromBean.setAbleUlmPoint("0");
			log.error("member is null or member.getPoint is error!" ,e);
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
		double firstNightSalePrice = 0.0;
		//订单
		try {
			order = new OrOrder();
			order.setCheckinDate(hotelOrderFromBean.getCheckinDate());
			order.setCheckoutDate(hotelOrderFromBean.getCheckoutDate());
			order.setRoomTypeId(hotelOrderFromBean.getRoomTypeId());
			order.setRoomQuantity(null == hotelOrderFromBean.getRoomQuantity() || hotelOrderFromBean.getRoomQuantity().equalsIgnoreCase("null") ? 1 : Integer
					.parseInt(hotelOrderFromBean.getRoomQuantity()));
			order.setChildRoomTypeId(hotelOrderFromBean.getChildRoomTypeId());
			order.setHotelId(hotelOrderFromBean.getHotelId());
			// v2.6 必须面转预，订单的支付方式需要由面付改成预付 ADD BY WUYUN 2009-06-04
			order.setPayMethod(hotelOrderFromBean.hasPayToPrepay() ? "pre_pay" : hotelOrderFromBean.getPayMethod());
			order.setPayToPrepay(hotelOrderFromBean.hasPayToPrepay());
			if (null != member) {
				order.setMemberId(member.getId());
				order.setMemberCd(member.getMembercd());
			}

			// add by zhineng.zhuang hotel2.6 包装预订条款,修改了order.PriceList ,reservation,reservationAssist的值
			try {
				
			getReserva(queryPriceList,hotelOrderFromBean, order);
								
			} catch (Exception e) {
				super.setErrorCode("HB10");
				
				log.error(getBookHotelInfo(hotelOrderFromBean),e);
				return super.forwardError(ERROR_MESSAGE);
			}

			//this method is getting prices and setPriceNum() 
			double priceNum = 0.0;
			
			for (int i = 0; i < order.getPriceList().size(); i++) {
				OrPriceDetail items = order.getPriceList().get(i);
				if(0 == i){
					firstNightSalePrice = items.getSalePrice();
				}
				priceNum += items.getSalePrice();
			}
			
			priceSum=priceNum;
			String payMethodTemp = hotelOrderFromBean.isPayToPrepay() ? "pre_pay" : hotelOrderFromBean.getPayMethod();
			try {
				saleItemVOList = PriceUtil.getSaleItemVOList(order.getPriceList(), payMethodTemp, hotelOrderFromBean.getCurrency());
			} catch (Exception e) {
				log.error("query every price error", e);
			}
			//get bookhintAssureStr must after the method getReserva(),because reservation
			//bookhintAssureStr = hotelBookService.getBookhintAssure(reservation, hotelOrderFromBean.getPayMethod(), hotelOrderFromBean.isPayToPrepay());		
			
			//填充担保相关的信息(添加艺龙担保信息----add by wangjian)
			if(hotelOrderFromBean.getRoomChannel() == ChannelType.CHANNEL_ELONG){
				ElongAssureResult assureResult = checkElongAssureService.checkIsAssure(hotelOrderFromBean.getHotelId(), hotelOrderFromBean.getChildRoomTypeId()
						, hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate(), 1, "00:00");
				
				if(assureResult.getConditionStr()!=null && !"".equals(assureResult.getConditionStr())){
					HtlElAssure htlElAssure = new HtlElAssure();
					htlElAssure.setAssureRoomQuantity(assureResult.getOverQtyNum());
					String overTimeHour = assureResult.getOverTimeHour()==0 ? "18":assureResult.getOverTimeHour()+"";
					overTimeHour = overTimeHour.length()<2 ? "0"+overTimeHour:overTimeHour;
					String overTimeMin = assureResult.getOverTimeMin()==0 ? "00":assureResult.getOverTimeMin()+"";
					overTimeMin = overTimeMin.length()<2 ? "0"+overTimeMin:overTimeMin;
					htlElAssure.setAssureDate(overTimeHour+":"+overTimeMin);
					if(assureResult.getAssureMoneyType()==1){
						htlElAssure.setAssureAmount(firstNightSalePrice);
					}else if(assureResult.getAssureMoneyType()==2){
						htlElAssure.setAssureAmount(priceSum);
					}
					htlElAssure.setAssureType(assureResult.getAssureType());
					setElAssureToAssureInfoVO(htlElAssure);
					bookhintCancelAndModifyStr = assureResult.getModifyStr();
				}else{
					assureInfoVO = new AssureInfoVO();
					setNoElAssure(assureInfoVO, "18:00");
				}
			}else{
				bookhintCancelAndModifyStr = hotelBookService.getBookhintCancelAndModify(reservation, reservationAssist, hotelOrderFromBean.getPayMethod(),
						hotelOrderFromBean.isPayToPrepay());
				fillAssureInfoVO();
			}			
			
			//获取酒店房型的促销信息，小礼包
			roomPresaleList=hotelReservationInfoService.queryRoomPresale(order);
	
			/** hotel2.9.3 代金券需要传递酒店相关基本信息 add by chenjiajie 2009-09-15 begin **/			
			//request.setAttribute("htlHotel", htlHotel);
			/** hotel2.9.3 代金券需要传递酒店相关基本信息 add by chenjiajie 2009-09-15 end **/

			//查询每天的价格和早餐，提前两天和推后两天的，用于展示早两天和晚两天的价格和早餐，by ting.li
			fillBreakfastAndPriceVO(hotelOrderFromBean);
			
			//1，渠道返现控制比例 add by hushunqiang
			cashbackratevalue = channelCashBackService.getChannelCashBacktRate(projectcode);
			refreshCashReturnAmount(hotelOrderFromBean, priceLis);

		} catch (Exception e) {
			log.error("hotelBookingAction order error", e);
		}

		//by ting.li，更新订单填写数据
		updateOrderRecord(member);
		//设置界面展示的值
		setSomeValueToShow();
        hotelOrderFromBean.setPriceNum(Math.ceil(priceSum));
        hotelOrderFromBean.setFirstNightSalePrice(Math.ceil(firstNightSalePrice));

		return SUCCESS;//只是面付
	}
	
	//去哪儿来的订单，记录日志,不用了，修改为记录到l_htl_order_flow表中
	/*
	private void savaQunarChannal(){
		if( StringUtil.isValidStr(projectcode)&& projectcode.indexOf("0027")>=0){ 
			String city=null;
			HtlChannelClickLog clickLog = new HtlChannelClickLog();
			clickLog.setHotelId(hotelOrderFromBean.getHotelId());
			clickLog.setCheckInDate(DateUtil.getDate(inDate));
			clickLog.setCheckOutDate(DateUtil.getDate(outDate));
			clickLog.setClick("0");
			clickLog.setClickDate(new Date());
			clickLog.setClickTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
			clickLog.setProjectCode(projectcode);
			clickLog.setCity(city);
			clickLog.setRoomPrice(roomprice);
			clickLog.setPayMethod(payMethod);
			if(StringUtil.isValidStr(priceTypeId)) clickLog.setPriceTypeId(Long.valueOf(priceTypeId));
			try {
				clickLog.setRoomType(URLDecoder.decode(roomType==null?"":roomType, "utf8"));
				hotelManageWeb.addChannelLog(clickLog);
			}catch(Exception e){
				log.error("HotelInfoAction hotelManageWeb.addChannelLog has a wrong", e);
			}
		}
		
	}
	*/
	//处理超时担保
   private void setOverTimeAssure(AssureInfoVO assureInfoVO,String time){
	    Date nowDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String nowDateStr = dateFormat.format(nowDate);
		Calendar calendar = Calendar.getInstance();
		int timeNum=Integer.parseInt(time.split(":")[0]);
		int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
		if (nowDateStr.equals(inDate) && nowHour >= timeNum) {
			assureInfoVO.setFirstRetentionTime(null);
			assureInfoVO.setMidFirstRetentionTime(null);
			assureInfoVO.setMidSecondRetentionTime(null); 
		}
		else{
			assureInfoVO.setFirstRetentionTime(time);
			assureInfoVO.setMidFirstRetentionTime(null);
			assureInfoVO.setMidSecondRetentionTime(null); 
		}
		assureInfoVO.setSecondRetentionTime("次日凌晨1:00");
   }
   //处理非超时担保
	private void setNoElAssure(AssureInfoVO assureInfoVO, String time){
		Date nowDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String nowDateStr = dateFormat.format(nowDate);
		Calendar calendar = Calendar.getInstance();
		int timeNum=Integer.parseInt(time.split(":")[0]);
		int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
		if (nowDateStr.equals(inDate) && nowHour >= timeNum) {
			if(nowHour>=22){
			assureInfoVO.setFirstRetentionTime(null);
			assureInfoVO.setMidFirstRetentionTime(null);
			assureInfoVO.setMidSecondRetentionTime(null);
			}
			else if(nowHour>=20&&nowHour<22){
				assureInfoVO.setFirstRetentionTime(null);
				assureInfoVO.setMidFirstRetentionTime(null);
				assureInfoVO.setMidSecondRetentionTime("22:00");
				
			}
			else if(nowHour>=18&& nowHour<20){
				assureInfoVO.setFirstRetentionTime(null);
				assureInfoVO.setMidFirstRetentionTime("20:00");
				assureInfoVO.setMidSecondRetentionTime("22:00");
			}
			else if(nowHour<18){
				assureInfoVO.setFirstRetentionTime("18:00");
				assureInfoVO.setMidFirstRetentionTime("20:00");
				assureInfoVO.setMidSecondRetentionTime("22:00");
			}
		}
		else{
			assureInfoVO.setFirstRetentionTime("18:00");
			assureInfoVO.setMidFirstRetentionTime("20:00");
			assureInfoVO.setMidSecondRetentionTime("22:00");
		}
		assureInfoVO.setSecondRetentionTime("次日凌晨1:00");
	}
	
	//艺龙担保提示信息页面展示 add by wangjian 2012-7-10
	private void setElAssureToAssureInfoVO(HtlElAssure htlElAssure){
		assureInfoVO = new AssureInfoVO();
		assureInfoVO.setAssureMoney(Math.ceil(htlElAssure.getAssureAmount()*rate));
		assureInfoVO.setAssureType(htlElAssure.getAssureType());	
		
		if(StringUtil.isValidStr(htlElAssure.getAssureDate())){
			setOverTimeAssure(assureInfoVO, htlElAssure.getAssureDate());
		}else{
			setNoElAssure(assureInfoVO, "18:00");
		}
		assureInfoVO.setMaxRoomNum(htlElAssure.getAssureRoomQuantity());
	}
	
	private void setSomeValueToShow() {
		weekOfInDate = HotelQueryHandler.WeekDay.getValueByKey(DateUtil.getWeekOfDate(hotelOrderFromBean.getCheckinDate()));
		weekOfOutDate = HotelQueryHandler.WeekDay.getValueByKey(DateUtil.getWeekOfDate(hotelOrderFromBean.getCheckoutDate()));
	}

	private void switchPayMethod() {
		if (PayMethod.PRE_PAY.equals(payMethod) || PayMethod.PAY.equals(payMethod)) {
			return;
		}
		if ("提前支付".equals(payMethod)) {
			payMethod = PayMethod.PRE_PAY;
		} else {
			payMethod = PayMethod.PAY;
		}
	}

	private void initOrderBean() {
		this.hotelOrderFromBean = new HotelOrderFromBean();
		
		if(priceTypeId!=null){
			hotelOrderFromBean.setChildRoomTypeId(Long.valueOf(priceTypeId));
		}
		hotelOrderFromBean.setPayMethod(payMethod);
		
		if (inDate == null || outDate == null) { 	//支付宝没有时间 add by diandian.hou 2011-11-30
			inDate = DateUtil.dateToString(DateUtil.getDate(DateUtil.getSystemDate(), 1));
			outDate = DateUtil.dateToString(DateUtil.getDate(DateUtil.getSystemDate(), 2));
		}
		
		if (projectcode == null) { 	//添加支付宝的cookie add by diandian.hou 2011-11-30
			projectcode = CookieUtils.getCookieValue(request, "projectcode");
		}
		if(StringUtil.isValidStr(source)){
			hotelOrderFromBean.setSource(source);
		}
		hotelOrderFromBean.setCheckinDate(DateUtil.stringToDateMain(inDate, "yyyy-MM-dd"));
		hotelOrderFromBean.setCheckoutDate(DateUtil.stringToDateMain(outDate, "yyyy-MM-dd"));
		int days = DateUtil.getDay(hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate());
		hotelOrderFromBean.setDays(days);
		hotelOrderFromBean.setRoomQuantity(String.valueOf(1));
		hotelOrderFromBean.setReturnAmount(0.0D);
	}

	public void refreshCashReturnAmount(HotelOrderFromBean hotelOrderFromBean, List<QueryHotelForWebSaleItems> priceList) {
		try {
				
			if (hotelOrderFromBean.getReturnAmount() <= 0 || null == priceList || priceList.isEmpty()) {
				return;
			}
			double cashReturnAmount = 0;
			int payMethod = PayMethod.PAY.equals(hotelOrderFromBean.getPayMethod()) ? 1 : 2;
			if (hotelOrderFromBean.isPayToPrepay()) {
				payMethod = 1;
			}
			int days = DateUtil.getDay(hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate());
			for (int i = 0; i < days; i++) {
				Date night = DateUtil.getDate(hotelOrderFromBean.getCheckinDate(), i);
				for (Object obj : priceList) {
					QueryHotelForWebSaleItems item = (QueryHotelForWebSaleItems) obj;
					BigDecimal price = new BigDecimal(item.getSalePrice());
					if (price.intValue() >= 99999 || "1".equals(item.getCloseShowPrice()))
						continue;
					if (DateUtil.getDay(item.getFellowDate(), night) == 0) {
						BigDecimal cPrice = new BigDecimal("0");
						double commission=0;

						if (hotelOrderFromBean.getPayMethod().equals("pre_pay") && ChannelType.CHANNEL_CTS == hotelOrderFromBean.getRoomChannel()) {
							// 如果是香港中科酒店，只有简单公式， 净价等于售价
							cPrice = price;
							// 如果是中旅，俑金等于售价-底价
							commission=item.getSalePrice()-item.getBasePrice();
						} else {
							cPrice = returnService.calculateRoomTypePrice(item.getFormulaId(), new BigDecimal(item.getCommission()), new BigDecimal(item
									.getCommissionRate()), price);
							commission = item.getCommission();
						}
						log.info("返现规则计算净价:" + cPrice.doubleValue() + "价格公式:" + item.getFormulaId() + "佣金：" + item.getCommission() + "佣金率:"
								+ item.getCommissionRate() + "售价：" + item.getSalePrice());
						int returnAmount = 0;
						
						//计算限量返现
						returnAmount=limitFavourableManage.calculateCashLimitReturnAmount(hotelOrderFromBean.getHotelId(), hotelOrderFromBean.getChildRoomTypeId(), 
								item.getFellowDate(), hotelOrderFromBean.getCurrency(), price,commission);
						
						//如果没有限量返现，再计算普通返现，如果有，则不计算普通返现
						if(returnAmount==-1){
							returnAmount = returnService.calculateCashReturnAmount(hotelOrderFromBean.getChildRoomTypeId(), item.getFellowDate(), payMethod,
									hotelOrderFromBean.getCurrency(), 1, cPrice);
						}
						cashReturnAmount += returnAmount;
						log.info("单步计算的返现金额:" + returnAmount);
					}
				}
			}
			if (cashReturnAmount != hotelOrderFromBean.getReturnAmount()) {
				log.info(DateUtil.datetimeToString(new Date()) + "刷取到最新的返现规则，返现金额由原来的：" + hotelOrderFromBean.getReturnAmount()
						+ "变更为：" + cashReturnAmount);
				hotelOrderFromBean.setReturnAmount(cashReturnAmount);
			}
			
			//增加渠道返利控制，通过渠道设定的返现比例进行处理 add by hushunqiang
			Double lastReturnAmount = channelCashBackService.getlastCashBackAmount(cashbackratevalue, hotelOrderFromBean.getReturnAmount());
			hotelOrderFromBean.setReturnAmount(lastReturnAmount);
			
			
		} catch (Exception e) {
			log.error("HotelBookingAction refreshCashReturnAmount() has a wrong.", e);
		}
	}
	

	/**
	 * 取出预订条款并给订单，给计算担保金额 add by zhineng.zhuang hotel2.6
	 */
	public void getReserva(List<QueryHotelForWebSaleItems> queryPriceList,HotelOrderFromBean hotelOrderFromBean, OrOrder order) throws Exception {
		int priceSize = queryPriceList.size();
		
		//连住优惠对应改变售价 add by guzhijie begin 2009-09-13
		int f = 0;
		for (int y = 0; y < priceSize; y++) {
			QueryHotelForWebSaleItems queryHotelForWebSaleItems = queryPriceList.get(y);
			f = hotelManageWeb.changePriceForFavourableThree(queryPriceList, queryHotelForWebSaleItems, order.getHotelId(), order.getChildRoomTypeId(), y, f);

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
		List<String> dateStrList = DateUtil.getDateStrList(order.getCheckinDate(), order.getCheckoutDate(), false);
		for (int p = 0; p < priceSize; p++) {
			queryHotelForWebSaleItems = queryPriceList.get(p);
			OrPriceDetail orPriceDetail = new OrPriceDetail();
			orPriceDetail.setNight(queryHotelForWebSaleItems.getFellowDate());
			orPriceDetail.setDayIndex(p);
			orPriceDetail.setSalePrice(queryHotelForWebSaleItems.getSalePrice());
			// 设置房态字符串
			orPriceDetail.setRoomState(WebStrUtil.showRoomType(queryHotelForWebSaleItems.getRoomStatus(), queryHotelForWebSaleItems.getAvailQty()));
			// 设置配额数量
			orPriceDetail.setQuantity(queryHotelForWebSaleItems.getAvailQty());
			// 设置当天日期字符串
			orPriceDetail.setDateStr(dateStrList.get(p));
			// 设置早餐信息字符串
			orPriceDetail.setBreakfastStr(WebStrUtil.getResourceValue("breakfast_typeForCC", queryHotelForWebSaleItems.getBreakfastType()) + ":"
					+ WebStrUtil.getResourceValue("breakfast_num", String.valueOf(queryHotelForWebSaleItems.getBreakfastNum())));
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
	 * 
	 * TODO 填充订单填写页中价格和早餐的显示.
	 * @param hotelOrderFromBean
	 * @return
	 */
	private void fillBreakfastAndPriceVO(HotelOrderFromBean hotelOrderFromBean) {

		String datePattern = "yyyy-MM-dd";
		SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
		breakfastAndPriceItemVOList = new ArrayList<BreakfastAndPriceItemVO>();
		try {
			String startDate = dateFormat.format(hotelOrderFromBean.getCheckinDate());
			String endDate = dateFormat.format(hotelOrderFromBean.getCheckoutDate());

			List<QueryHotelForWebSaleItems> queryPriceList = hotelManageWeb.queryPriceDetailForWeb(hotelOrderFromBean.getHotelId(), DateUtil.stringToDateMain(
					startDate, datePattern), DateUtil.stringToDateMain(endDate, datePattern), DateUtil.stringToDateMain(startDate, datePattern), DateUtil
					.stringToDateMain(endDate, datePattern), hotelOrderFromBean.getRoomTypeId(), hotelOrderFromBean.getChildRoomTypeId(), hotelOrderFromBean
					.getPayMethod());
			
			
			changeEveryDayFavPrice(hotelOrderFromBean, queryPriceList);						
			for (QueryHotelForWebSaleItems queryPrice : queryPriceList) {			
				breakfastAndPriceItemVOList.add(handleBreakfastAndPriceItemVO(hotelOrderFromBean, queryPrice));

			}
			
		} catch (Exception e) {
			log.error("fill the BreakfastAndPriceVO wrong", e);
		}

	}

	private BreakfastAndPriceItemVO handleBreakfastAndPriceItemVO(HotelOrderFromBean hotelOrderFromBean, QueryHotelForWebSaleItems queryPrice) {
		DispayVoHandlerImpl dispayVoHandler=new DispayVoHandlerImpl();
		return dispayVoHandler.handleBreakfastAndPriceItemVO(hotelOrderFromBean, queryPrice);
	}

	/**
	 * 改变订单填写页的价格列表中，设置酒店优惠中，每天的价格。改变价格
	 * @param hotelOrderFromBean
	 * @param queryPriceList 查询出每天的价格，包括前两天和后两天
	 */
	private void changeEveryDayFavPrice(HotelOrderFromBean hotelOrderFromBean, List<QueryHotelForWebSaleItems> queryPriceList) {
	   int beginDateIndexOfqueryPriceList = 0;
		for (int i = 0; i < queryPriceList.size(); i++) {
			if ((queryPriceList.get(i).getFellowDate()).equals(hotelOrderFromBean.getCheckinDate())) {
				beginDateIndexOfqueryPriceList = i;
				break;
			}
		}
		int f = 0;
		for (int y = beginDateIndexOfqueryPriceList; y <beginDateIndexOfqueryPriceList+days ; y++) {
			QueryHotelForWebSaleItems queryHotelForWebSaleItems = queryPriceList.get(y);
			f = hotelManageWeb.changePriceForFavourableThree(queryPriceList, queryHotelForWebSaleItems, order.getHotelId(), order.getChildRoomTypeId(), y, f);

		}								
		
	}


	/**
	 * 填充有关担保的信息，担保类型，担保提示语，担保金额等等
	 */
	private void fillAssureInfoVO() {

		try {

			if (reservation != null) {
				assureInfoVO=handleAssureInfoVo();
				double assureMoney=assureInfoVO.getAssureMoney();
				assureMoney=Math.ceil(assureMoney*rate);
				assureInfoVO.setAssureMoney(assureMoney);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("fill the fillAssureInfoVO() wrong", e);

		}
	}

	private AssureInfoVO handleAssureInfoVo() {
		DispayVoHandlerImpl dispayVoHandler=new DispayVoHandlerImpl();
		return dispayVoHandler.handleAssureInfoVo(reservation, inDate, outDate);
		
	}
	
	
	private  void updateOrderRecord(MemberDTO member){
		try{
		HttpSession httpSession = request.getSession(true);
		OrderRecord orderRecord= (OrderRecord) httpSession.getAttribute("orderRecord");
		if(orderRecord!=null && member !=null){
			orderRecord.setMemberCd(member.getMembercd());
			orderRecord.setMemberId(member.getId());
			bookOrderRecordService.updateOrderRecord(orderRecord);
		}
		}catch(Exception e){
			log.error(" update order record has a wrong", e);
		}
	}

    /**
	 * create by ting.li
	 * 保存OrderRecord
	 */
	private void saveOrderRecord() {
	try {
			bookOrderRecordService.saveOrderRecord(request, super.getHttpResponse(), hotelOrderFromBean, member, null, 5);
		} catch (Exception e) {
			log.error("save order record error", e);
		}
	}

	//界面展示用
	private void setSomeValueToHotelVO(HotelResultForWebVO hotelVO, HotelBasicInfo hotelBasicInfo,HtlHotel htlHotel) {
		if(hotelBasicInfo!=null){
		hotelVO.setHotelStar(HotelQueryHandler.HotelStarConvert.getForWebVOByKey(hotelBasicInfo.getHotelStar()));
		hotelVO.setFreeService(resourceManager.getDescription("res_freeService", hotelBasicInfo.getFreeService()));
		hotelVO.setRoomFixtrue(resourceManager.getDescription("room_equipment", hotelBasicInfo.getRoomFixtrue()));
		hotelVO.setMealFixtrue(resourceManager.getDescription("hotel_liefallow", hotelBasicInfo.getMealAndFixture()));
		hotelVO.setChnAddress(hotelBasicInfo.getChnAddress());
		hotelVO.setCityName(hotelBasicInfo.getCityName());
		hotelVO.setDistrict(hotelBasicInfo.getZoneName());
		hotelVO.setCommendType(HotelQueryHandler.CommendTypeConvert.getCssStyleName(hotelBasicInfo.getCommendType()));
		}
		HtlHotelExt htelHotelExt=htlHotel.getHtelHotelExt().get(0);
		
		if (null != hotelVO.getFreeService()) {
			freeServiceToShow = new ArrayList<String>(Arrays.asList(hotelVO.getFreeService().split(",")));
			String freeServiceRemark=htelHotelExt.getFreeServiceRemark();
			if(freeServiceRemark != null){
				freeServiceToShow.add(freeServiceRemark);
			}
			
			
		}
		if (null != hotelVO.getRoomFixtrue()) {
			roomEquipToShow = new ArrayList<String>(Arrays.asList(hotelVO.getRoomFixtrue().split(",")));
			String roomFixtrueRemark=htelHotelExt.getRoomFixtrueRemark();
			if(roomFixtrueRemark != null){
				roomEquipToShow.add(roomFixtrueRemark);
			}
		}
		if (null != hotelVO.getMealFixtrue()) {
			mealAndFixtureToShow = Arrays.asList(hotelVO.getMealFixtrue().split(","));
		}
	}
	
	private String getBookHotelInfo(HotelOrderFromBean hotelOrderFromBean){
		StringBuilder bookInfoError=new StringBuilder();
		if(hotelOrderFromBean!=null){
			bookInfoError.append(hotelOrderFromBean.getHotelId());
			bookInfoError.append(" roomTypeId ");
			bookInfoError.append(hotelOrderFromBean.getRoomTypeId());
			bookInfoError.append(" priceTypeId ");
			bookInfoError.append(hotelOrderFromBean.getChildRoomTypeId());
			bookInfoError.append("   " );
			bookInfoError.append(inDate);
			bookInfoError.append("   " );
			bookInfoError.append(outDate);
			
		}
		return bookInfoError.toString();
	}
	
	private boolean validateInputParam(){		

		
		if(inDate==null ||(inDate!=null && !inDate.matches("^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}"))){
			return true;
		}
	

		if(outDate==null ||(outDate!=null && !inDate.matches("^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}"))){
			return true;
		}
		
		if(priceTypeId==null ||(priceTypeId!=null && priceTypeId.matches("\\W"))){
			return true;
		}
		
		return false;
	}
	
	/**
	 * 根据手机号或者Email查询会员的所有会籍信息，优先以手机号进行检索
	 * @param mobilePhone
	 * @param email
	 * @return
	 */
	public String getMemberShipList(){
		PrintWriter out =null;
		long memerId = 0;
		if (null != mbrId &&!"".equals(mbrId)){
			memerId = Long.valueOf(mbrId);
		}
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType("text/json"); 
            response.setCharacterEncoding("UTF-8"); 
            out = response.getWriter(); 
			ActionContext.getContext().getActionInvocation().getProxy().setExecuteResult(false);
			List<MembershipVO> mbrshipList = memberInterfaceService.getMemberShipList(memerId,telephone, email);
			if(null != mbrshipList && mbrshipList.size()>0){
				JSONArray jo = JSONArray.fromObject(mbrshipList);
			    result = jo.toString(); 
			}else{
				result = "{}";
			}
			out.print(result);
		} catch (Exception e) {
			log.error(e);
		}finally{
			out.flush();
			out.close();
		}		
		return null;
	}
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

	public List<OftenDeliveryAddress> getOftenAddressList() {
		return oftenAddressList;
	}

	public void setHotelBookingService(HotelBookingService hotelBookingService) {
		this.hotelBookingService = hotelBookingService;
	}
	

	public void setHotelReservationInfoService(IHotelReservationInfoService hotelReservationInfoService) {
		this.hotelReservationInfoService = hotelReservationInfoService;
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

	public void setHotelBookDao(HotelBookDao hotelBookDao) {
		this.hotelBookDao = hotelBookDao;
	}

	
	public void setHotelRereshPrice(HotelRereshPrice hotelRereshPrice) {
		this.hotelRereshPrice = hotelRereshPrice;
	}

	public String getProjectcode() {
		return projectcode; 

	}

	public void setProjectcode(String projectcode) {
		this.projectcode = projectcode;
	}

	public int getFagChangeDate() {
		return fagChangeDate;
	}

	public void setFagChangeDate(int fagChangeDate) {
		this.fagChangeDate = fagChangeDate;
	}

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	public List<String> getFreeServiceToShow() {
		return freeServiceToShow;
	}

	public List<String> getRoomEquipToShow() {
		return roomEquipToShow;
	}

	public List<String> getMealAndFixtureToShow() {
		return mealAndFixtureToShow;
	}

	public List<BreakfastAndPriceItemVO> getBreakfastAndPriceItemVOList() {
		return breakfastAndPriceItemVOList;
	}

	public void setBreakfastAndPriceItemVOList(List<BreakfastAndPriceItemVO> breakfastAndPriceItemVOList) {
		this.breakfastAndPriceItemVOList = breakfastAndPriceItemVOList;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public AssureInfoVO getAssureInfoVO() {
		return assureInfoVO;
	}

	public String getWeekOfInDate() {
		return weekOfInDate;
	}

	public void setWeekOfInDate(String weekOfInDate) {
		this.weekOfInDate = weekOfInDate;
	}

	public String getWeekOfOutDate() {
		return weekOfOutDate;
	}

	public void setWeekOfOutDate(String weekOfOutDate) {
		this.weekOfOutDate = weekOfOutDate;
	}

	
	public Date getAfterCheckInDate() {
		return afterCheckInDate;
	}

	public void setAfterCheckInDate(Date afterCheckInDate) {
		this.afterCheckInDate = afterCheckInDate;
	}

	public List<OrPreSale> getRoomPresaleList() {
		return roomPresaleList;
	}

	public void setRoomPresaleList(List<OrPreSale> roomPresaleList) {
		this.roomPresaleList = roomPresaleList;
	}
	
	public long getLogId() {
		return logId;
	}

	public void setLogId(long logId) {
		this.logId = logId;
	}

	public Map<String, Integer> getBedTypeMinQuatoMap() {
		return bedTypeMinQuatoMap;
	}

	public void setBedTypeMinQuatoMap(Map<String, Integer> bedTypeMinQuatoMap) {
		this.bedTypeMinQuatoMap = bedTypeMinQuatoMap;
	}

	public void setLimitFavourableManage(
			HtlLimitFavourableManage limitFavourableManage) {
		this.limitFavourableManage = limitFavourableManage;
	}

	public void setHotelElOrderService(HotelElOrderService hotelElOrderService) {
		this.hotelElOrderService = hotelElOrderService;
	}
	public void setBookingDataCheckService(BookingDataCheckService bookingDataCheckService) {
		this.bookingDataCheckService = bookingDataCheckService;
	}

	public void setRoomprice(double roomprice) {
		this.roomprice = roomprice;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public boolean getWhetherLogin() {
		return whetherLogin;
	}
	public CheckElongAssureService getCheckElongAssureService() {
		return checkElongAssureService;
	}
	public void setWhetherLogin(boolean whetherLogin) {
		this.whetherLogin = whetherLogin;
	}
	public void setCheckElongAssureService(
			CheckElongAssureService checkElongAssureService) {
		this.checkElongAssureService = checkElongAssureService;
	}
	public void setBookOrderRecordService(BookOrderRecordService bookOrderRecordService) {
		this.bookOrderRecordService = bookOrderRecordService;
	}

	public ChannelCashBackManagerService getChannelCashBackService() {
		return channelCashBackService;
	}

	public void setChannelCashBackService(
			ChannelCashBackManagerService channelCashBackService) {
		this.channelCashBackService = channelCashBackService;
	}

	public void setMbrId(String mbrId) {
		this.mbrId = mbrId;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
}
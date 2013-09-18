package com.mangocity.webnew.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import com.mangocity.hdl.constant.ChannelType;
import com.mangocity.hdl.constant.ChinaOnlineConstant;
import com.mangocity.hdl.hotel.dto.AddExRoomOrderRequest;
import com.mangocity.hdl.hotel.dto.AddExRoomOrderResponse;
import com.mangocity.hdl.hotel.dto.CancelExRoomOrderRequest;
import com.mangocity.hdl.hotel.dto.CancelExRoomOrderResponse;
import com.mangocity.hdl.hotel.dto.MGExOrder;
import com.mangocity.hdl.hotel.dto.MGExOrderItem;
import com.mangocity.hdl.hotel.dto.MGExResult;
import com.mangocity.hdl.service.IHDLService;
import com.mangocity.hotel.base.manage.HtlProjectCodeManage;
import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.hotel.base.resource.ResourceManager;
import com.mangocity.hotel.base.service.IBenefitService;
import com.mangocity.hotel.base.service.IHotelFavourableReturnService;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.constant.GuaranteeState;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.hotel.order.constant.PrepayType;
import com.mangocity.hotel.order.persistence.OrFulfillment;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderItem;
import com.mangocity.hotel.order.persistence.OrPayment;
import com.mangocity.hotel.order.persistence.assistant.OrderUtil;
import com.mangocity.hotel.order.service.IOrderEditService;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.hweb.persistence.QueryHotelForWebSaleItems;
import com.mangocity.jiaocha.dao.AcrossSellDao;
import com.mangocity.jiaocha.dao.HotelPKG;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.util.hotel.constant.QuotaType;
import com.mangocity.util.log.MyLog;
import com.mangocity.webnew.constant.OrderPayType;
import com.mangocity.webnew.persistence.HotelOrderFromBean;
import com.mangocity.webnew.service.HotelBookingService;
import com.mangocity.webnew.service.IHotelOrderCompleteService;
import com.mangocity.webnew.service.OrderImmedConfirmService;
import com.mangocity.webnew.util.action.GenericWebAction;

/**
 * 订单完成Action 主要功能是生成订单，订单提交中台，扣取会员信用卡费用，积分，锁定代金券等
 * @author chenjiajie
 *
 */
public class HotelCompleteAction extends GenericWebAction {
	
	private static final MyLog log = MyLog.getLogger(HotelCompleteAction.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1859286250134885159L;
	
	/**
	 * 新版网站处理订单完成Service接口 主要功能是生成订单，订单提交中台，扣取会员信用卡费用，积分，锁定代金 
	 */
	private IHotelOrderCompleteService hotelOrderCompleteService;
	
    /**
     * 修改订单相关service
     */
    private IOrderEditService orderEditService;
    
    /**
     * 现金账户接口
     */
    private IHotelFavourableReturnService returnService;

    /**
     * 订单编码
     */
    private String orderCD;

    /**
     * 订单ID
     */
    private String orderID;

    /**
     * IOrderService
     */
    private IOrderService orderService;
    
    /**
     * 是否成功占用中旅配额
     */
    private int resultHK = 0;

    /**
     * 填写历史信用卡的卡ID
     */
    private String cardID = "0";
    
    /**
     * 优惠立减服务接口
     */
    private IBenefitService benefitService;
    
    /**
     * 计算半个小时超时
     */
    private String overHalfDate;
    
    /**
     * 用于保存香港中科获取的新价格，以便后续生成订单明细 ADD BY WUYUN 2009-04-21
     */
    private String hkPrices;
    
    //酒店交叉销售 start   haochenyang
    /** 旅游产品信息 */
    private List<HotelPKG> sellHotelPKGList;
    //酒店交叉销售 end   haochenyang
    
    /**
     * 用于保存香港中科获取的底价，以便后续生成订单明细
     */
    private String hkBasePrices;
    
    //下单的service类 
    private HotelBookingService   hotelBookingService;
    
    /**
     * add by chenjiajie 2009-01-23 V2.5 资源文件类
     */
    private ResourceManager resourceManagerA;
    
    /**
     * 酒店直联 IHDLService
     * 
     * @author guojun 2008-11-26 15:30
     */
    private IHDLService hdlService;
    
    /**
     * 检测变量
     */
    private int roomPrice;
    
    /**
     * 是否变价
     */
    private boolean chgPrice ;
    
    //即时确认，发送短信，Email，传真的Service add by shengwei.zuo 2010-2-1
    private OrderImmedConfirmService orderImmedConfirmService;
    
    // 二次确定标示，不需要get or set method,add by diandian.hou 2010-10-26
	private boolean flagSecondConfirm = false;
	
	private HtlProjectCodeManage htlProjectCodeManage;
	
	//订单的间夜数量
	private int totalRoomNight;
    
	/**
	 * Action主方法
	 */
	public String execute() {

		if(super.isRepeatSubmit()){
			return super.forwardError("请不要重复提交同一订单。");
		}
		
		Map params = super.getParams();
		
		//判断预订房型是否满房
		if( this.hotelCheckOrderService.isRoomFull(params)){

		    return super.forwardError("该房型无法安排，建议客人改订其他房型或其他酒店！"); 
		}
		
        hotelOrderFromBean = new HotelOrderFromBean();
        MyBeanUtil.copyProperties(hotelOrderFromBean, params);
		
		
		//如果是中科酒店，也会去刷价格，如果没有刷到就直接返回到查询页面 add by shengwei.zuo 
		if(PayMethod.PRE_PAY.equals(hotelOrderFromBean.getPayMethod())
				&&  ChannelType.CHANNEL_CTS == hotelOrderFromBean.getRoomChannel() && hotelOrderFromBean.isFlagCtsHK()){
			
			List<QueryHotelForWebSaleItems> priceLis = new ArrayList<QueryHotelForWebSaleItems>();
			
			Date checkIn = hotelOrderFromBean.getCheckinDate();
		    Date checkOut = hotelOrderFromBean.getCheckoutDate();
		    
		    int days = DateUtil.getDay(checkIn,checkOut);
			
			 priceLis = hotelManageWeb.queryPriceForWebHK(hotelOrderFromBean.getHotelId(),
		                hotelOrderFromBean.getRoomTypeId(), hotelOrderFromBean.getChildRoomTypeId(),
		                checkIn, DateUtil.getDate(checkOut, -1));	            
		            List<Date> liDate = hotelManageWeb.queryQtyForWebHK(hotelOrderFromBean.getHotelId(), 
		            		hotelOrderFromBean.getRoomTypeId(), checkIn, checkOut);
		            double reTotal = 0.0;
		            log.info("HotelCompleteAction =======CTS=====hotelOrderFromBean.getPriceNum()-----"+hotelOrderFromBean.getPriceNum());
		            if (null != priceLis && days <= priceLis.size() && days <= liDate.size()) {
		                StringBuffer saleBuffer = new StringBuffer();
		                StringBuffer baseBuffer = new StringBuffer();
		                for (int k = 0; k < priceLis.size(); k++) {
		                    QueryHotelForWebSaleItems it = (QueryHotelForWebSaleItems) priceLis.get(k);
		                    saleBuffer.append(it.getSalePrice() + "#");
		                    baseBuffer.append(it.getBasePrice() + "#");
		                    reTotal += it.getSalePrice();
		                    log.info("HotelCompleteAction ======CTS======priceLis[k].salePrice:" + it.getSalePrice());
		                    log.info("HotelCompleteAction ======CTS======priceLis[k].basePrice:" + it.getBasePrice());
		                }
		                hkPrices = saleBuffer.toString();
		                hkBasePrices = baseBuffer.toString();
		                // 需要比较原币种(即港币)价格是否相等，如果原币种价格相等，则表示价格没有发生变化；如果不等，则需要打上已变化标记
		                if (0 != Double.compare(hotelOrderFromBean.getPriceNum().doubleValue(), reTotal)) {
		                    hotelOrderFromBean.setPriceChange(true);
		                    hotelOrderFromBean.setPriceNum(reTotal);
		                }
		                
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
		            		if(!chkCanBook[i]) {
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
		            	
		            	return super.forwardError(sbMsg.toString());
		                
		            }
		}
		
		//封装订单基本数据，还包括预订条款、客户确认方式、会员信息等 (不分面预付/面付担保单)
		fillOrderBaseInfo();

        // 更新会员的常入住人和常联系人
        if (null != member && member.isMango()) {
        	boolean  bFellow  = true;
        	if(!hotelOrderFromBean.getIsSavePerson()){
        		bFellow = false;
        	}
            memberInterfaceService.updateMemberFellowAndLinkman(member, order, bFellow, true);
        }

		/* 预付流程 */
		if((PayMethod.PRE_PAY.equals(hotelOrderFromBean.getPayMethod())|| hotelOrderFromBean.isPayToPrepay())
			   &&  ChannelType.CHANNEL_CTS != hotelOrderFromBean.getRoomChannel()){
			
			//封装订单基本数据(预付) 必须先调用fillOrderBaseInfo() 
			String result = savePrepayOrderInfo();
			
			//返回的字符串如果不为空，则证明有问题 现在有防止盗用积分的校验
			if(StringUtil.isValidStr(result)){
				return super.forwardError(result);
			}
			
			/* 模拟在线支付，正式发布需要注释begin 
			if(OrderPayType.ONLINE_PAY == hotelOrderFromBean.getOrderPayType()
			   || OrderPayType.DEBIT_CARD_PAY == hotelOrderFromBean.getOrderPayType()){
				
				return "payOnline";
				
			}
			*/
			/* 模拟在线支付，正式发布需要注释end */
			
			//如果是畅联为2  格林豪泰为5
			if(!order.isManualOrder() && hotelOrderFromBean.isChannelToWith() ) {
				
				log.info("HotelCompleteAction======chinaonline======pre_pay ----start==============");
				
				//如果orderItem不为空
				if(order.getOrderItems()!=null && !order.getOrderItems().isEmpty()){
					
					log.info("HotelCompleteAction============pre_pay chinaonline --getOrderItems is not null==============");
					
					String hdlOrderPreStr = saveHDLOrderInfo();
					 //返回的字符串如果不为空，则证明有问题 说明下单到畅联失败。
					if(StringUtil.isValidStr(hdlOrderPreStr)){
						return super.forwardError(hdlOrderPreStr);
					}
				}
			}
		}	
		/* 面付流程 */
		else if(PayMethod.PAY.equals(hotelOrderFromBean.getPayMethod())&& 
				ChannelType.CHANNEL_CTS != hotelOrderFromBean.getRoomChannel()){
			//封装订单基本数据(面付/担保单) 必须先调用fillOrderBaseInfo() 
			savePayOrderInfo();
			
			//如果是畅联为2  格林豪泰为5
			if(!order.isManualOrder() && hotelOrderFromBean.isChannelToWith() ) {
				
				log.info("HotelCompleteAction======chinaonline======pay ----start==============");
				
				//如果orderItem不为空
				if(order.getOrderItems()!=null && !order.getOrderItems().isEmpty()){
					
					log.info("HotelCompleteAction============pay  chinaonline --getOrderItems is not null==============");
					
					String hdlOrderPreStr = saveHDLOrderInfo();
					 //返回的字符串如果不为空，则证明有问题 说明下单到畅联失败。
					if(StringUtil.isValidStr(hdlOrderPreStr)){
						return super.forwardError(hdlOrderPreStr);
					}
				}
			}
		}
		/* 中旅 预付流程 */
		else if(PayMethod.PRE_PAY.equals(hotelOrderFromBean.getPayMethod())
				&&  ChannelType.CHANNEL_CTS == hotelOrderFromBean.getRoomChannel() && hotelOrderFromBean.isFlagCtsHK()){
			//封装中旅订单 
			String toForward = saveDirectOrderInfo();

			// 增加订单返现记录
			if (hotelOrderFromBean.getReturnAmount() > 0
					&& null != orderCD) {
				log.info(member.getMembercd() + ":"
						+ DateUtil.datetimeToString(new Date()) + "为中旅订单"
						+ orderCD + "增加返现记录明细,返现金额:"
						+ hotelOrderFromBean.getReturnAmount());
				hotelOrderCompleteService.fillCashInformation(order, orderCD,
						hotelOrderFromBean, member);
			}
			
			//走在线支付流程 add by shengwei.zuo 2009-11-23
			return toForward;
			
		}
		/* 畅联 流程 */
//		else if(!order.isManualOrder() && 0 < hotelOrderFromBean.getRoomChannel() 
//				&& hotelOrderFromBean.getRoomChannel() == ChannelType.CHANNEL_CHINAONLINE) {
//			//如果是预付或者是面付且需要担保，则直接下到畅联
//			if(PayMethod.PRE_PAY.equals(hotelOrderFromBean.getPayMethod())
//					|| (PayMethod.PAY.equals(hotelOrderFromBean.getPayMethod()) 
//					&& hotelOrderFromBean.isNeedAssure() && 0 != Double.compare(hotelOrderFromBean.getOrignalSuretyPrice(), 0.0))){
//				String hdlOrderPreStr = saveHDLOrderInfo();
//				 //返回的字符串如果不为空，则证明有问题 说明下单到畅联失败。
//				if(StringUtil.isValidStr(hdlOrderPreStr)){
//					return super.forwardError(hdlOrderPreStr);
//				}
//			}else{
//				//封装订单基本数据(面付) 
//				savePayOrderInfo();
//			}
//			
//		}
		
		//给合作方用的检测数据 add by chenjiajie 2010-01-21
		int nights = DateUtil.compare(order.getCheckinDate(), order.getCheckoutDate());
		roomPrice = (int)(order.getSumRmb() / order.getRoomQuantity() / nights);

        //酒店交叉销售 start   haochenyang
        try
        {
        	AcrossSellDao acrossSellDao = AcrossSellDao.getInstance();
            String city_name = InitServlet.cityObj.get(hotelOrderFromBean.getCityId());
            System.out.println("*** city_name = " + city_name + " ; id= " + hotelOrderFromBean.getCityId()
                    + " ; code = " + hotelOrderFromBean.getCityCode());
            if (city_name != null && !city_name.trim().equals(""))
            {
                sellHotelPKGList = acrossSellDao.getSellPKG(city_name);
            }
        }
        catch (Exception e)
        { 
            LOG.error(e.getMessage(), e);
        }
        if (sellHotelPKGList == null)
        {
            sellHotelPKGList = new ArrayList<HotelPKG>();
        }
        //酒店交叉销售 end   haochenyang
        
        // 增加订单返现记录
		if (hotelOrderFromBean.getReturnAmount() > 0
				&& null != orderCD) {
			log.info(member.getMembercd() + ":"
					+ DateUtil.datetimeToString(new Date()) + "为订单" + orderCD
					+ "增加返现记录明细,返现金额:" + order.getCashBackTotal());
			hotelOrderCompleteService.fillCashInformation(order, orderCD,
					hotelOrderFromBean, member);
		}
		
		 //根据三字码 填充hotelOrderFromBean中的城市名称
        String cityCode = hotelOrderFromBean.getCityCode();
        if(null!=cityCode){
        	String cityName = InitServlet.cityObj.get(cityCode);
        	if(StringUtil.isValidStr(cityName)) hotelOrderFromBean.setCityName(cityName);
        }
        
        // 在订单完成时将cookie中的projectcode、exprojectcode1和exprojectcode2取出并保存  add by xuyiwen 2011-3-17
		Cookie[] cookies = request.getCookies();
		if(cookies.length>0 &&null!=order.getOrderCD())
		htlProjectCodeManage.saveHtlProjectCode(cookies, order.getOrderCD());
		// end 
        
        addReturnLog(order, hotelOrderFromBean, member);
        
        //根据or_order计算订单的总间夜量
        totalRoomNight = DateUtil.compare(order.getCheckoutDate(), order.getCheckinDate()) * order.getRoomQuantity();
        
        return SUCCESS;
	}
	
	
	/**
	 * 封装订单基本数据(面付/担保单) 必须先调用fillOrderBaseInfo()
	 */
	private void savePayOrderInfo(){
		
        //保存订单并返回订单CD 
        orderCD = processPaymentMethod(order);
        
         //把订单的渠道
		order.setChannel(hotelOrderFromBean.getRoomChannel());

        if(null != orderId && null == order.getID()){
        	order.setID(new Long(orderId.toString()));
        }
        if(!StringUtil.isValidStr(order.getOrderCD())){
        	order.setOrderCD(orderCD);
        	//发送传真没有订单号，必须填充订单号
        	order.setOrderCDHotel(orderCD);
        }
        order.setShowBasePrice(false);
        // 如果需要超时担保或者其他非条件担保，则走面付担保流程，并扣配额
        if (hotelOrderFromBean.isNeedAssure()
            && 0 != Double.compare(hotelOrderFromBean.getOrignalSuretyPrice(), 0.0)) {
            // 去掉小数点后的金额；进行四舍五入
            order.setSuretyPrice(hotelOrderFromBean.getSuretyPriceRMB());
            order.setIsCreditAssured(true);
            // 首先生成暂存前台的订单，不扣配额，订单状态为“暂存前台”
            order.setOrderState(OrderState.NOT_SUBMIT);
            
            //当信用卡不为空的时候，要更新中台状态，并扣配额
            if(StringUtil.isValidStr(cardID)){
	            //绑定支付的信用卡
		        order.setCreditCardIdsSelect(cardID);
		        //生产bug1209，之前只有预付有该操作，信用卡担保也要有该操作 add by chenjiajie 2010-02-08
                //在非登陆的情况下需要更新会员新注册的信用卡的会员是当前会员 
		        log.info("=====isDireckbook:"+hotelOrderFromBean.isDireckbook()+"======");
		        order.setOrderState(OrderState.SUBMIT_TO_MID);
		        order.setToMidTime(DateUtil.getSystemDate());
		        /*
		         * 获取订单配额, 如果获取成功则同时填充orderItems
				 * deduct[0] 作方法调用返回值用，0为调用成功;deduct[1] 作是否扣配额用
				 * 0则什么也不做；-2为已扣配额（发生异常时返配额用）
				 */
				int[] deduct = new int[2];
				deduct = hotelOrderCompleteService.fillOrderItems(order,hotelOrderFromBean);
				//成功扣配额后，增加日志
				if(0 == deduct[0]){
					//系统自动判断是否满足即时确认条件 addby juesuchen 2009-12-25 begin
                    Map params = super.getParams();
					params.put("firstSubmit","1");
                    order.setInstantConfirm(OrderUtil.popDialogBox(order, params));
                    //系统自动判断是否满足即时确认条件 addby juesuchen 2009-12-25 end
					// 记录操作日志
		            OrHandleLog handleLog = new OrHandleLog();
		            handleLog.setModifiedTime(new Date());
		            handleLog.setContent("会员使用了信用卡担保，扣配额成功");
		            handleLog.setModifierName("网站");
		            handleLog.setOrder(order);
		            order.getLogList().add(handleLog);
				}
				
				//网站优化需求-开放简/繁体站所有非直联国内酒店网上预订即时确认功能  add by shengwei.zuo 2010-2-1  begin
				
				
				//发送传真或Email给酒店 -- 判断非直连的，只有非直连的才会发传真或Email 
				if(hotelOrderFromBean.getRoomChannel() <1){
					
					String sendHotelResult = orderImmedConfirmService.sendImmedConfirmToHotel(order, member);
					//成功发送给酒店才判断是否发送客人短信 
			    	if("success".equals(sendHotelResult)){
			    				
			    		//是否满足配额  
			    		boolean quotaOk = orderImmedConfirmService.quotaOk(order);
			    		        
			    		//如果满足配额,且信用卡担保成功 
			    		if(quotaOk && order.getSuretyState()== GuaranteeState.SUCCESS ){
			    			//则进行即时确认给客人   
			    			orderImmedConfirmService.sendImmedConfirmToCus(order, false, member);
			    		}
			    		
					 }
			    		
				}
				 
				//网站优化需求-开放简/繁体站所有非直联国内酒店网上预订即时确认功能  add by shengwei.zuo 2010-2-1  end
				
    		    order.setModifiedTime(new Date());
    		    OrderUtil.updateStayInMid(order);
            }
        } 
        // 如果不需要担保，则直接生成订单，不扣配额，订单状态为“已提交中台”
        else {
        	order.setOrderState(OrderState.SUBMIT_TO_MID);
            order.setToMidTime(DateUtil.getSystemDate());
        	
        	//如果是直联，
    		if(hotelOrderFromBean.getRoomChannel() >0 ){

    			log.info("HotelCompleteAction.savePayOrderInfo()============pay  chinaonline --kou quota start ==============");
    			
    			//把订单的渠道设为具体直联的渠道
    			order.setChannel(hotelOrderFromBean.getRoomChannel());
				int[] deduct = new int[2];
				deduct = hotelOrderCompleteService.fillOrderItems(order,hotelOrderFromBean);
				//成功扣配额后，增加日志
				if(0 == deduct[0]){
					log.info("HotelCompleteAction.savePayOrderInfo()============pay  chinaonline --kou quota success ==============");
					// 记录操作日志
		            OrHandleLog handleLog = new OrHandleLog();
		            handleLog.setModifiedTime(new Date());
		            handleLog.setContent("直连面付单，扣配额成功");
		            handleLog.setModifierName("网站");
		            handleLog.setOrder(order);
		            order.getLogList().add(handleLog);
				}
    		}
    		
    		//面付非担保单也 扣配额，并进行即时确认 add by shengwei.zuo 2010-2-1
    		if(hotelOrderFromBean.getRoomChannel() < 1){
    			
    			int[] deduct = new int[2];
				deduct = hotelOrderCompleteService.fillOrderItems(order,hotelOrderFromBean);
				//成功扣配额后，增加日志
				if(0 == deduct[0]){
					log.info("HotelCompleteAction.savePayOrderInfo()============pay order --kou quota success ==============");
					// 记录操作日志
		            OrHandleLog handleLog = new OrHandleLog();
		            handleLog.setModifiedTime(new Date());
		            handleLog.setContent("面付非担保单，扣配额成功");
		            handleLog.setModifierName("网站");
		            handleLog.setOrder(order);
		            order.getLogList().add(handleLog);
				}
    			
				//网站优化需求-开放简/繁体站所有非直联国内酒店网上预订即时确认功能  add by shengwei.zuo 2010-2-1  begin
				
				//发送传真给酒店 
		        String sendHotelResult = orderImmedConfirmService.sendImmedConfirmToHotel(order, member);
				
		        if("success".equals(sendHotelResult)){
    				//是否满足配额 
    				boolean quotaOk = orderImmedConfirmService.quotaOk(order);
    				if(quotaOk){
    					//如果满足配额，则进行即时确认 
    					orderImmedConfirmService.sendImmedConfirmToCus(order,flagSecondConfirm, member);
    				}
		        }
			    //网站优化需求-开放简/繁体站所有非直联国内酒店网上预订即时确认功能  add by shengwei.zuo 2010-2-1  end
				
    		}
    		order.setModifiedTime(new Date());
		    OrderUtil.updateStayInMid(order);
        }
        
        //同步配额明细中的总价格到订单表，防止变价，导致总金额和配额明细中的金额不相等 add by shengwei.zuo 2010-6-4
        hotelOrderCompleteService.synchroSumPriceToOrder(order, false);
        
        //变价通知 add by shengwei.zuo  2010-6-4
        if(order.getSum()!=hotelOrderFromBean.getPriceNum()*Integer.parseInt(hotelOrderFromBean.getRoomQuantity())){
        	chgPrice = true;
        }
        
        orderService.saveOrUpdate(order);
	}
	
	/**
	 * 封装订单基本数据(预付) 必须先调用fillOrderBaseInfo()
	 * 中旅/直联等在线支付方式的逻辑不在本方法中判断
	 */
	@SuppressWarnings("unchecked")
	private String savePrepayOrderInfo() {
		String result = "";
		
		//预付方式需要查询预付条款，取预付条款售价/底价支付酒店
		orderEditService.getReservationInfo(order, false);
		
		Map params = super.getParams();
		
		/* 预付立减优惠 填充订单 */
		if(0 < hotelOrderFromBean.getIsReduction()){
			fillBenefitInfo();
		}

		//保存订单并返回订单CD 
        orderCD = processPaymentMethod(order);

		/** 如果使用了积分，则调用积分接口 begin * */
		if (hotelOrderFromBean.isUseUlmPoint()) {
			try {
				//hotel2.9.3 增加了代金券支付方式，计算使用了积分的人民币需要再减去代金券的金额
				double ulmPointValue = Double.parseDouble(hotelOrderFromBean.getUlmPoint()) / 100;
				boolean isDeducted = hotelOrderCompleteService.deductUlmPoint(order, ulmPointValue, hotelOrderFromBean.isUseUlmPoint());
				if(!isDeducted){
					result = "抱歉，您所输入的积分不足已支付本次交易";
					return result;
				}
			} catch (Exception e) {
				log.error("ErrCord:75211 HotelCompleteAction.fillPrepayOrderInfo() deduct member point is error: " ,e);
			}
		}
		/** 如果使用了积分，则调用积分接口 end * */

		/** 如果使用了代金券，需要调用代金券接口 hotel2.9.3 add by chenjiajie begin* */
		if (hotelOrderFromBean.isUsedCoupon()) {
			hotelOrderCompleteService.deductUsedCoupon(params, order, member);
		}
		/** 如果使用了代金券，需要调用代金券接口 hotel2.9.3 add by chenjiajie end* */

		/* 如果使用了积分或代金券累加是全额支付 (扣配额) */
		if ((hotelOrderFromBean.isUseUlmPoint() || hotelOrderFromBean.isUsedCoupon())
				&& 0.001D > hotelOrderFromBean.getActuralAmount()) {
			log.info("==========HotelCompleteAction.fillPrepayOrderInfo() 积分或代金券累加是全额支付 code:5652 orderCD:"+orderCD+"=============");
			order.setOrderState(OrderState.SUBMIT_TO_MID);
			// //扣配额
			//if (log.isInfoEnabled())
				log.info("HotelCompleteAction.fillPrepayOrderInfo() saveOrder:request "
						+ "is prepay and save the order begin...");
			/*
			 * deduct[0] 作方法调用返回值用，0为调用成功;deduct[1] 作是否扣配额用
			 * 0则什么也不做；-2为已扣配额（发生异常时返配额用）
			 */
			int[] deduct = new int[2];
			deduct = hotelOrderCompleteService.fillOrderItems(order,hotelOrderFromBean);
			//成功扣配额后，增加日志
			if(0 == deduct[0]){
				//系统自动判断是否满足即时确认条件 addby juesuchen 2009-12-25 begin
				params.put("firstSubmit","1");
                order.setInstantConfirm(OrderUtil.popDialogBox(order, params));
                //系统自动判断是否满足即时确认条件 addby juesuchen 2009-12-25 end
				// 记录操作日志
	            OrHandleLog handleLog = new OrHandleLog();
	            handleLog.setModifiedTime(new Date());
	            handleLog.setContent("会员使用积分或代金券累加是全额支付，扣配额成功");
	            handleLog.setModifierName("网站");
	            handleLog.setOrder(order);
	            order.getLogList().add(handleLog);
			}
			
			if (0 == deduct[0]
					&& !StringUtil.isValidStr(hotelOrderFromBean.getSpecialRequest())) {
				// 扣减配额成功且无特殊要求，要发送即时确认
				//hotelOrderCompleteService.sendImediateMessage(member, order);
			}
		}
		/**
		 * 订单的支付类型 
		 * 面付方式：(酒店前台面付:1) 
		 * 预付方式：(信用卡支付:2),(营业部付款:3)
		 * 直联方式：(网上银行支付:4) add by chenjiajie 2009-11-06
		 */
		// 信用卡支付 (扣配额)
		else if (OrderPayType.CREDIT_PAY == hotelOrderFromBean.getOrderPayType()) {
			log.info("==========HotelCompleteAction.fillPrepayOrderInfo() 信用卡支付 code:5653 orderCD:"+orderCD+"=============");
	        //TODO:是否使用这个变量判断
			if (isInvoice) {
				log.info("==========HotelCompleteAction.fillPrepayOrderInfo() 信用卡支付->需要发票 code:565 orderCD:"+orderCD+"=============");
				OrFulfillment fulfill = new OrFulfillment();
				MyBeanUtil.copyProperties(fulfill, params);
				String invoiceDeliveryAddress = (String) params.get("invoiceDeliveryAddress");
				fulfill.setDeliveryAddress(invoiceDeliveryAddress);
				fulfill.setFulfillTaskType(3);// 任务类型为"配送"
				fulfill.setDeliveryType("FRP");// 配送方式为"免费挂号邮寄"
				order.setFulfill(fulfill);
			}
			order.setOrderState(OrderState.NOT_SUBMIT);
			OrPayment payment = new OrPayment();
			payment.setPayType(PrepayType.CreditCardDom);// 信用卡暂时写成国内
			payment.setMoney(hotelOrderFromBean.getActuralAmount());
			Date now = new Date();
			if (null != member)
				payment.setConfirmer(member.getName());
			payment.setConfirmTime(now);
			payment.setCreator("HWEB");
			payment.setOperator("HWEB");
			payment.setCreateTime(now);
			payment.setOperateTime(now);
			payment.setOrder(order);
			order.getPaymentList().add(payment);
            // 首先生成暂存前台的订单，不扣配额，订单状态为“暂存前台”
            order.setOrderState(OrderState.NOT_SUBMIT);
            
            //当信用卡不为空的时候，要更新中台状态，并扣配额
            if(StringUtil.isValidStr(cardID)){
				//绑定支付的信用卡
		        order.setCreditCardIdsSelect(cardID);
		        order.setOrderState(OrderState.SUBMIT_TO_MID);
		        /*
		         * 获取订单配额, 如果获取成功则同时填充orderItems
				 * deduct[0] 作方法调用返回值用，0为调用成功;deduct[1] 作是否扣配额用
				 * 0则什么也不做；-2为已扣配额（发生异常时返配额用）
				 */
				int[] deduct = new int[2];
				deduct = hotelOrderCompleteService.fillOrderItems(order,hotelOrderFromBean);
				//成功扣配额后，增加日志
				if(0 == deduct[0]){
					//系统自动判断是否满足即时确认条件 addby juesuchen 2009-12-25 begin
					params.put("firstSubmit","1");
                    order.setInstantConfirm(OrderUtil.popDialogBox(order, params));
                    //系统自动判断是否满足即时确认条件 addby juesuchen 2009-12-25 end
					// 记录操作日志
		            OrHandleLog handleLog = new OrHandleLog();
		            handleLog.setModifiedTime(new Date());
		            handleLog.setContent("会员使用信用卡支付，扣配额成功");
		            handleLog.setModifierName("网站");
		            handleLog.setOrder(order);
		            order.getLogList().add(handleLog);
				}
            }
		}
		// 营业部付款 (不扣配额)
		else if (OrderPayType.COUNTER_PAY == hotelOrderFromBean.getOrderPayType()) {
			log.info("==========HotelCompleteAction.fillPrepayOrderInfo() 现付 code:5654 orderCD:"+orderCD+"=============");
			// 设置配送信息
			OrFulfillment fulfill = new OrFulfillment();
			MyBeanUtil.copyProperties(fulfill, params);
			fulfill.setFulfillTaskType(1);// 任务类型为"营业部付款"
			fulfill.setDeliveryType("SDD");// 配送方式为"营业部自取"
			// 配送日期为空bug
			if (null == fulfill.getDeliveryDate() && null != fulfill.getFulfillPayDate()) {
				fulfill.setDeliveryDate(fulfill.getFulfillPayDate());
			}
			order.setFulfill(fulfill);
			OrPayment payment = new OrPayment();
			payment.setCurrencyType(hotelOrderFromBean.getCurrency());
			// 现付的方式
			String saleDepartmentPay = (String) params.get("saleDepartmentPay");
			if (saleDepartmentPay.equals("1")) {
				payment.setPayType(PrepayType.Cash);
			} else {
				payment.setPayType(PrepayType.POS);
			}
			// 如果是非人民币，也取人民币的价格
			payment.setMoney(hotelOrderFromBean.getActuralAmount());
			payment.setOrder(order);
			order.getPaymentList().add(payment);
			order.setOrderState(OrderState.SUBMIT_TO_MID);
			order.setToMidTime(DateUtil.getSystemDate());

			// 记录操作日志
            OrHandleLog handleLog = new OrHandleLog();
            handleLog.setModifiedTime(new Date());
            handleLog.setContent("会员使用营业部付款，未扣配额");
            handleLog.setModifierName("网站");
            handleLog.setOrder(order);
            order.getLogList().add(handleLog);
        
     
		}
		/*模拟在线支付，正式发布需要注释begin
		else{
			log.info("==========HotelCompleteAction.fillPrepayOrderInfo() 模拟在线支付 code:5653 orderCD:"+orderCD+"=============");
	        //TODO:是否使用这个变量判断
			order.setOrderState(OrderState.NOT_SUBMIT);
			
            int onlinePaytype = hotelOrderFromBean.getOnlinePaytype();
            order.setPrepayType(onlinePaytype);
			
			OrPayment payment = new OrPayment();
			payment.setPayType(onlinePaytype);
			payment.setMoney(hotelOrderFromBean.getActuralAmount());
			Date now = new Date();
			if (null != member)
			payment.setConfirmer(member.getName());
			payment.setConfirmTime(now);
			payment.setCreator("HWEB");
			payment.setOperator("HWEB");
			payment.setCreateTime(now);
			payment.setOperateTime(now);
			payment.setOrder(order);
			order.getPaymentList().add(payment);
            // 首先生成暂存前台的订单，不扣配额，订单状态为“暂存前台”
            order.setOrderState(OrderState.NOT_SUBMIT);
            int[] deduct = new int[2];
			deduct = hotelOrderCompleteService.fillOrderItems(order,hotelOrderFromBean);
			//成功扣配额后，增加日志
			if(0 == deduct[0]){
				//系统自动判断是否满足即时确认条件 addby juesuchen 2009-12-25 begin
				params.put("firstSubmit","1");
                order.setInstantConfirm(OrderUtil.popDialogBox(order, params));
                //系统自动判断是否满足即时确认条件 addby juesuchen 2009-12-25 end
				// 记录操作日志
	            OrHandleLog handleLog = new OrHandleLog();
	            handleLog.setModifiedTime(new Date());
	            handleLog.setContent("模拟在线支付支付，扣配额成功");
	            handleLog.setModifierName("网站");
	            handleLog.setOrder(order);
	            order.getLogList().add(handleLog);
			}
		}*/
        /*模拟在线支付，正式发布需要注释end*/
		
		/* 订单状态根据各种支付方式的支付状态修改成已支付 */
        if (OrderUtil.checkHasPrepayed(order)) {
        	order.setHasPrepayed(true);
        	order.setOrderState(OrderState.HAS_PAID);
        }
		
        //网站优化需求-开放简/繁体站所有非直联国内酒店网上预订即时确认功能  add by shengwei.zuo 2010-2-1  begin
        
        //发送传真给酒店 
		String sendHotelResult = orderImmedConfirmService.sendImmedConfirmToHotel(order, member);
		
		if("success".equals(sendHotelResult)){
    		//是否满足配额  
    		boolean quotaOk = orderImmedConfirmService.quotaOk(order);
    		
    		//是否支付成功
            boolean paySucc = order.isHasPrepayed();
            
            //如果满足配额,且支付成功 
    		if(quotaOk && paySucc ){
    			//则进行即时确认给客人   
    			orderImmedConfirmService.sendImmedConfirmToCus(order, false, member);
    		}            
        }
		
        //网站优化需求-开放简/繁体站所有非直联国内酒店网上预订即时确认功能  add by shengwei.zuo 2010-2-1  end

        //更新order的中台状态 
		order.setToMidTime(new Date());
        OrderUtil.updateStayInMid(order);
        
        //同步配额明细中的总价格到订单表，防止变价，导致总金额和配额明细中的金额不相等 add by shengwei.zuo 2010-6-4
        hotelOrderCompleteService.synchroSumPriceToOrder(order, false);
        
        //变价通知 add by shengwei.zuo  2010-6-4
//        if(order.getSum()!=hotelOrderFromBean.getPriceNum()*Integer.parseInt(hotelOrderFromBean.getRoomQuantity())){
//        	chgPrice = true;
//        }
        
        //当使用了积分或者代金卷
        if(hotelOrderFromBean.isUseUlmPoint() || hotelOrderFromBean.isUsedCoupon()){
        	//订单的实收金额
            double sumRmbSl = order.getSumRmb();
            
            double reCountRmb = hotelOrderCompleteService.reCountActSum(hotelOrderFromBean,sumRmbSl);
            
            hotelOrderFromBean.setActuralAmount(reCountRmb);
        }
        
		orderService.saveOrUpdate(order);
		return result;
	}

	/**
	 * 预付立减优惠 填充订单
	 */
	private void fillBenefitInfo() {
		order.setFavourableFlag(hotelOrderFromBean.getIsReduction());
		//界面传递过来的立减金额是RMB的，保存订单的是原币种的，这里重新查询一下
		//计算某价格类型在入住时间段的优惠总金额 (不参与币种换算) 
		int benefitTotalAmount = benefitService.calculateBenefitAmount(String.valueOf(order.getChildRoomTypeId())
				, order.getCheckinDate()
				, order.getCheckoutDate(), order.getRoomQuantity());
		order.setFavourableAmount(benefitTotalAmount);
	}
	
	/**
	 * 封装中旅订单
	 */
	private String saveDirectOrderInfo() {

		log.info("==========HotelCompleteAction.saveDirectOrderInfo() 封装中旅订单 ");

		// 保存订单并返回订单CD
		orderCD = processPaymentMethod(order);

		// 设置订单渠道
		order.setChannel(hotelOrderFromBean.getRoomChannel());

		/* 预付立减优惠 填充订单 */
		if (0 < hotelOrderFromBean.getIsReduction()) {
			fillBenefitInfo();
		}

		// 如果是中旅的酒店
		if (ChannelType.CHANNEL_CTS == hotelOrderFromBean.getRoomChannel()
				&& hotelOrderFromBean.isFlagCtsHK()) {

			Date nowTime = new Date();
			OrParam param = hotelManageWeb.getHalfhoutTimeLimit();
			if (null != param) {
				int timeLimit = Integer.parseInt(param.getValue());
				if ((nowTime.getTime() - DateUtil
						.stringToDateTime(overHalfDate).getTime())
						/ (1000 * 60) > timeLimit) {
					return super
							.forwardError("由于您的操作时间过长导致该页面超时，订单未能正常提交，请返回重新预订。");
				}
			}

			// 订单状态设置为“暂存前台”
			order.setOrderState(OrderState.NOT_SUBMIT);
			// 订单创建时间
			order.setCreateDate(new Date());
			// 生成订单明细
			OrderUtil.generateOrderItems(order, hkPrices, hkBasePrices);
			// 拆单
			OrderUtil.splitOrderForChannel(order, hotelOrderFromBean
					.getMinRoomNumCts());
			// 填充入住人
			OrderUtil.fillCtsFellows(order);
			// 锁定配额
			resultHK = hotelManageWeb.checkQuotaForWebHK(order);
			// 锁定配额失败
			if (0 != resultHK) {
				orderService.saveOrUpdate(order);
				// TODO:需要返回错误提醒页面
				log
						.info("HotelCompleteAction=======CTS===Quota 锁定配额失败===============resultHK----: "
								+ resultHK);
				return "HKQuotaFail";
			}
			// 填充中科订单入住人
			resultHK = hotelManageWeb.saleAddCustInfo(order);
			if (0 != resultHK) {
				orderService.saveOrUpdate(order);
				// TODO:需要返回错误提醒页面
				log
						.info("HotelCompleteAction=======CTS==Fellow 填充中科订单入住人失败===============resultHK----: "
								+ resultHK);
				return "HKQuotaFail";
			}
			int onlinePaytype = hotelOrderFromBean.getOnlinePaytype();
			order.setPrepayType(onlinePaytype);

			// 支付相关的类 add by shengwei.zuo 2009-11-27
			OrPayment payment = new OrPayment();
			payment.setPayType(onlinePaytype);
			payment.setMoney(hotelOrderFromBean.getActuralAmount());
			Date now = new Date();
			if (null != member)
				payment.setConfirmer(member.getName());
			payment.setConfirmTime(now);
			payment.setCreator("HWEB");
			payment.setOperator("HWEB");
			payment.setCreateTime(now);
			payment.setOperateTime(now);
			payment.setOrder(order);
			order.getPaymentList().add(payment);

			// 记录操作日志
			OrHandleLog handleLog = new OrHandleLog();
			handleLog.setModifiedTime(new Date());
			String payTypeStr = PrepayType.payStrMap.get(onlinePaytype);
			handleLog.setContent("客人选择在线支付方式：" + payTypeStr);
			handleLog.setModifierName("网站");
			handleLog.setOrder(order);
			order.getLogList().add(handleLog);
			
			// 中旅酒店返现
			if (hotelOrderFromBean.getReturnAmount() > 0) {
				List<OrOrderItem> items = order.getOrderItems();
				double totalCashReturnAmount = 0.0;
				String  payMethod = order.isPayToPrepay()?"pay":order.getPayMethod();
				for (OrOrderItem item : items) {
					int cashReturnAmount = returnService
							.calculateCashReturnAmount(order
									.getChildRoomTypeId(), item.getNight(),
									"pay".equals(payMethod) ? 1 : 2,
									order.getPaymentCurrency(), 1, new BigDecimal(item
											.getSalePrice()));
					item.setCashReturnAmount(cashReturnAmount);
					totalCashReturnAmount += cashReturnAmount;
				}
				order.setCashBackTotal(totalCashReturnAmount);
				// 记录操作日志
			}
		}

		orderService.saveOrUpdate(order);

		return "payOnline";

	}
	
	/**
	 * 封装 畅联 订单 add by shengwei.zuo 2009-12-23
	 */
	public  String  saveHDLOrderInfo(){
			
			Map params = super.getParams();
	        
	        String errMsg = "";
	        
            // 获取resourceDescr.xml中的资源 add by chenjiajie V2.5 2009-01-23
            Map channelMap = resourceManagerA.getDescription("select_cooperator");
            MGExResult mgresult = null;
            boolean bSuc = false;
            AddExRoomOrderResponse addExRoomOrderResponse = null;
            if (0 < hotelOrderFromBean.getRoomChannel()) {
                // 如果渠道为非传统渠道
                AddExRoomOrderRequest addExRoomOrderRequest = new AddExRoomOrderRequest();
                addExRoomOrderRequest.setChannelType(hotelOrderFromBean.getRoomChannel());
                addExRoomOrderRequest.setHotelId(order.getHotelId());
                addExRoomOrderRequest.setChainCode(null);
                /*
                 * 渠道方 hotelCode
                 */
                MGExOrder exRoomOrder = new MGExOrder();
                // 直联酒店ChinaOnline可能需要使用的首日房价 add by chenjiajie V2.5@2009-02-02
               // float colFirstDayPrice = order;
               // exRoomOrder.setFirstDayPrice(colFirstDayPrice);
                exRoomOrder.setCheckindate(DateUtil.toStringByFormat(order.getCheckinDate(),
                    "yyyy-MM-dd"));
                exRoomOrder.setCheckoutdate(DateUtil.toStringByFormat(order.getCheckoutDate(),
                    "yyyy-MM-dd"));
                exRoomOrder.setRoomquantity(order.getRoomQuantity());
                exRoomOrder.setPaymethod(order.getPayMethod());
                exRoomOrder.setCurrency(order.getPaymentCurrency());
                if (order.getPayMethod().equals(PayMethod.PRE_PAY)) {
                    exRoomOrder.setTotalamount((float) 0.0);
                } else {
                    exRoomOrder.setTotalamount(Double.valueOf(order.getSum()).floatValue());
                }
                exRoomOrder.setLocaltotalamout(Double.valueOf(order.getSumRmb()).floatValue());

                exRoomOrder.setExchangerate(Double.valueOf(order.getRateId()).floatValue());
                exRoomOrder.setArrivaltime(order.getArrivalTime());
                exRoomOrder.setLatestarrivaltime(order.getLatestArrivalTime());
                exRoomOrder.setNosmoking(null);
                log.info("HotelCompleteAction=============================saveHDLOrderInfo===order.getRemark().getHotelRemark() start");
                if(order.getRemark()!=null){
                	exRoomOrder.setHotelnotes(order.getRemark().getHotelRemark());
                }
                exRoomOrder.setSpecialrequest(order.getSpecialRequest());
                exRoomOrder.setLinkman(order.getLinkMan());
                // 子房型ID
                exRoomOrder.setPricetypecode(order.getChildRoomTypeId().intValue()); // *****
                exRoomOrder.setTitle(order.getTitle());
                exRoomOrder.setMobile(order.getMobile());
                exRoomOrder.setTelephone(order.getTelephone());
                exRoomOrder.setCustomerfax(order.getCustomerFax());
                exRoomOrder.setEmail(order.getEmail());
                exRoomOrder.setArrivaltraffic(order.getArrivalTraffic());
                exRoomOrder.setFlight(order.getFlight());
                exRoomOrder.setFellownames(order.getFellowNames());
                exRoomOrder.setAdultnum(order.getFellowList().size());
                // exRoomOrder.setPaysatus(String.valueOf(order.getPayState()));
                exRoomOrder.setOrderstate(String.valueOf(order.getOrderState()));
                exRoomOrder.setHotelid(order.getHotelId());
                exRoomOrder.setHotelname(order.getHotelName());
                exRoomOrder.setRoomtypeid(order.getRoomTypeId().intValue());
                exRoomOrder.setRoomtypecode(order.getRoomTypeId().intValue());
                exRoomOrder.setRoomtypename(order.getRoomTypeName());
                exRoomOrder.setBedtype(order.getBedType());
                exRoomOrder.setIsguarantee(String.valueOf((order.getIsCreditAssured())));
                exRoomOrder.setGuaranteetype(ChinaOnlineConstant.NO_UARANTEE); 
                // 对德比没用，但COL需要设置，这里设施未无担保类型
                exRoomOrder.setCreditcardtype("credit card type");
                exRoomOrder.setCreditcardname(null);
                exRoomOrder.setCreditcardno(null);
                exRoomOrder.setCreditcardexpires(null);
                /**
                 * 传给中间层订单orderid与ordercdhotel
                 */
                exRoomOrder.setOrderid(order.getID());
                exRoomOrder.setOrdercd(orderCD);
                exRoomOrder.setOrdercdhotel(orderCD);
                if (null == order.getCreateDate()) {
                    exRoomOrder.setCreatedate(DateUtil.toStringByFormat(new Date(),
                        "yyyy-MM-dd HH:mm:ss"));
                } else {
                    exRoomOrder.setCreatedate(DateUtil.toStringByFormat(order.getCreateDate(),
                        "yyyy-MM-dd HH:mm:ss"));
                }
                
                log.info("HotelCompleteAction=============================saveHDLOrderInfo===orderCD : "+orderCD);
                
                log.info("HotelCompleteAction=============================saveHDLOrderInfo===exRoomOrder setObject over ");
                
                List<OrOrderItem> orderItemList = order.getOrderItems();
                log.info("HotelCompleteAction=================saveHDLOrderInfo===orderItemList start ");
                for (Iterator itr = orderItemList.iterator(); itr.hasNext();) {
                	OrOrderItem orderItem = (OrOrderItem) itr.next();
                    MGExOrderItem mgExOrderItem = new MGExOrderItem();
                    mgExOrderItem.setNumber(orderItem.getRoomIndex());
                    mgExOrderItem.setCheckindate(DateUtil.toStringByFormat(orderItem.getNight(),
                        "yyyy-MM-dd"));
                    mgExOrderItem.setCheckoutdate(DateUtil.toStringByFormat(orderItem.getNight(),
                        "yyyy-MM-dd"));
                    mgExOrderItem.setGuests(order.getFellowNames());
                    if (null != orderItem.getRoomNo()) {
                        mgExOrderItem.setRoomno(Integer.valueOf(orderItem.getRoomNo()));
                    }
                    if (order.getPayMethod().equals(PayMethod.PRE_PAY)) {
                        exRoomOrder.setTotalamount(exRoomOrder.getTotalamount()
                            + Double.valueOf(orderItem.getBasePrice()).floatValue());
                    }
                    mgExOrderItem.setSum(Double.valueOf(orderItem.getBasePrice()).floatValue());
                    mgExOrderItem.setBaseprice(Double.valueOf(
                        orderItem.getBasePrice()).floatValue());
                    mgExOrderItem.setSaleprice(Double.valueOf( 
                        orderItem.getSalePrice()).floatValue());
                    mgExOrderItem.setBaserate(Double.valueOf( 
                        orderItem.getBasePrice()).floatValue());
                    mgExOrderItem.setTotalcharges(Double.valueOf(
                        orderItem.getServiceFee()).floatValue());
                    mgExOrderItem.setSpecialnote(orderItem.getSpecialNote());
                    mgExOrderItem.setCreatetime(exRoomOrder.getCreatedate());
                    orderItem.setQuotaType(QuotaType.GENERALQUOTA);
                    mgExOrderItem.setQuantity(1);
                    mgExOrderItem.setSum(Double.valueOf(order.getSum()).floatValue());
                    mgExOrderItem.setNoteresult(orderItem.getNoteResult());
                    mgExOrderItem.setOrderid(order.getID());
                    mgExOrderItem.setOrderstate(String.valueOf(order.getOrderState()));
                    mgExOrderItem.setHotelconfirm(null);
                    mgExOrderItem.setHotelconfirmid(null);
                    exRoomOrder.getExOrderItems().add(mgExOrderItem);
                    
                    //直联酒店ChinaOnline可能需要使用的首日房价 add by shengwei.zuo  2009-12-23
                    if(0==DateUtil.compare(orderItem.getNight(),hotelOrderFromBean.getCheckinDate())){
                    	 float colFirstDayPrice = Double.valueOf(orderItem.getSalePrice()).floatValue();
                    	 log.info("HotelCompleteAction===========saveHDLOrderInfo=========orderItemList========colFirstDayPrice :"+colFirstDayPrice);
                         exRoomOrder.setFirstDayPrice(colFirstDayPrice);
                    }
                }
                log.info("HotelCompleteAction=================saveHDLOrderInfo===orderItemList end ");
                addExRoomOrderRequest.setRoomOrder(exRoomOrder);
                
                if(roleUser==null){
                	roleUser  = new UserWrapper();
                }
                roleUser.setLoginName("网站用户");
            	roleUser.setName("网站用户");
                
                try {
                    addExRoomOrderResponse = hdlService.addExRoomOrder(addExRoomOrderRequest);
                } catch (Exception e) {
                	log.error("=================HotelCompleteAction.saveHDLOrderInfo()===hdlService.addExRoomOrder() exception  beging cancel order:",e);
                	
                	MGExResult  result  = addExRoomOrderResponse.getResult();
                    String message = "";
                    if(null != result && StringUtil.isValidStr(result.getMessage())){
                    	message = result.getMessage();
                    }
                    
                	String failLog = "接口异常！<font color='red'>下直联合作方"+channelMap.get(String.valueOf(hotelOrderFromBean.getRoomChannel()))+ "订单失败！"+message+"</font>";
                	order = orderService.getOrder(order.getID());
                	order.setCancelReason(88);
                    orderService.cancelOrder(order, OrderState.CANCEL, failLog,"88", roleUser);
                	return "预订失败!该段时间酒店价格发生变化，请致电芒果网客服电话40066-40066或者重新选择别的酒店！";
                }
               
                if (null != addExRoomOrderResponse) {
                    mgresult = addExRoomOrderResponse.getResult();
                    // 判断订单是否成功,1为成功,0为不成功
                    if (1 == mgresult.getValue()) {
                        bSuc = true;
                   
                        order = orderService.getOrder(order.getID());
                        if (StringUtil.isValidStr(mgresult.getMessage())) {
                            order.setOrderCdForChannel(mgresult.getMessage());
                        }
                        StringBuilder orderSuccessLog =  new  StringBuilder();
                        orderSuccessLog.append("<font color='green'>下单到直连合作方"+
                        		channelMap.get(String.valueOf(hotelOrderFromBean.getRoomChannel()))+"成功！</font>");
                        String strLog = "";
                        if (null == order.getSpecialRequest()
                            || "".equals(order.getSpecialRequest())) {
                            order.setQuotaOk(true);
                            order.setSendedHotelFax(true);
                            order.setHotelConfirm(true);
                            order.setSendedHotelFax(true);
                            order.setHotelConfirmFax(true);
                            order.setHotelConfirmTel(true);
                            order.setHotelConfirmFaxReturn(true);
                            strLog = "已满足配额,已发送酒店确认,酒店已口头确认,酒店已书面确认,已收回传";
                        } else {
                            order.setQuotaOk(true);
                            order.setSendedHotelFax(true);
                            strLog = "已满足配额,已发送酒店确认";
                        }
                         // 二次确定，取得二次确定标示，用于是否发即时确定短信 add by diandian.hou 2010-10-26 
   					     flagSecondConfirm = addExRoomOrderResponse.getFlagSecondConfirm();
   					     // 测试用的，正式用上面的，把下面一句删除
   					     //flagSecondConfirm = true;
   					     if (flagSecondConfirm) {
   						      strLog = orderService.changeOrderForSencondConfirm(order);
   					    }
   					    orderSuccessLog.append(strLog);
   					    
                        OrHandleLog handleLog = new OrHandleLog();
                        handleLog.setModifierName("网站");
                        handleLog.setBeforeState(order.getOrderState());
                        handleLog.setAfterState(order.getOrderState());
                        handleLog.setContent(orderSuccessLog.toString());
                        handleLog.setModifiedTime(new Date());
                        handleLog.setOrder(order);
                        order.getLogList().add(handleLog);
                    
                        try{
                        	orderService.saveOrUpdate(order);   
                        }catch(Exception e){
                        	  log.error("==============HotelCompleteAction.saveHDLOrderInfo()=======orderService.saveOrUpdate() update orderCdChannel and log ecception: ", e);
                        	 
                        	  StringBuilder canStr = new StringBuilder();
                        	  canStr.append("取消直连合作方").append(channelMap.get(String.valueOf(hotelOrderFromBean.getRoomChannel())))
                        	     	.append("订单");
                        	  CancelExRoomOrderRequest cancelExRoomOrderRequest = new CancelExRoomOrderRequest();
                              cancelExRoomOrderRequest.setChannelType(hotelOrderFromBean.getRoomChannel());
                              cancelExRoomOrderRequest.setHotelId(order.getHotelId());
                              cancelExRoomOrderRequest.setOrderId(order.getID());
                              cancelExRoomOrderRequest.setChainCode(null);
                              cancelExRoomOrderRequest.setCancelReason("Cancel order because of customer!");
                              cancelExRoomOrderRequest.setCancelMessage("Cancel order because of customer!");
                              try {
                            	  CancelExRoomOrderResponse  cancelExRoomOrderResponse = hdlService
                                      .cancelExRoomOrder(cancelExRoomOrderRequest);
                            	  if(null!=cancelExRoomOrderResponse){
                            		  if(1==cancelExRoomOrderResponse.getResult().getValue()){
                            			  canStr.append("成功！");
                            			  order = orderService.getOrder(order.getID());
                                    	  order.setCancelReason(88);
                                    	  orderService.cancelOrder(order, OrderState.CANCEL, "保存订单异常！","88", roleUser);
                            		  }else{
                            			  canStr.insert(0, "保存订单异常！");
                            			  canStr.append("失败！");
                            			  canStr.append("原因：").append(cancelExRoomOrderResponse.getResult().getMessage())
                            			        .append("<font color='red'>请发送取消传真或Email至酒店！</font>");
                            		  }
                            	  }
                              } catch (Exception eCancel) {
                            	  log.error("==============HotelCompleteAction.saveHDLOrderInfo()==hdlService.cancelExRoomOrder() exception=====",eCancel);
                            	  canStr.insert(0, "保存订单异常！");
                            	  canStr.append("失败！");
                            	  canStr.append("原因：合作方接口异常！<font color='red'>请发送取消传真或Email至酒店！</font>");
                              }finally{
                            	  order = orderService.getOrder(order.getID());
                            	  OrHandleLog cancelhandleLog = new OrHandleLog();
                            	  cancelhandleLog.setModifierName("网站");
                            	  cancelhandleLog.setBeforeState(order.getOrderState());
                            	  cancelhandleLog.setAfterState(order.getOrderState());
                            	  cancelhandleLog.setContent(canStr.toString());
                            	  cancelhandleLog.setModifiedTime(new Date());
                            	  cancelhandleLog.setOrder(order);
                                  order.getLogList().add(cancelhandleLog);
                                  orderService.saveOrUpdate(order);
                                  return "预订失败!该段时间酒店价格发生变化，请致电芒果网客服电话40066-40066或者重新选择别的酒店！";
                              }
                        }
                        //只有保存订单成功后才会发确认短信给客人,add by shengwei.zuo 2010-12-21
                        try {
                        	order = orderService.getOrder(order.getID());
                        	this.sendImmedConfirmToCus(order);
							//orderService.saveOrUpdate(order);
						} catch (Exception eSMS) {
							log.error("==============HotelCompleteAction.saveHDLOrderInfo()=========this.sendImmedConfirmToCus() exception : ", eSMS);
						}
                    }else{
                    	log.info("=======HotelCompleteAction.saveHDLOrderInfo()====== new order failed! reason :  "+ mgresult.getMessage());
                        // 直联订单失败的原因是合作方系统原因,
                        order.setCancelReason(88);
                        orderService.cancelOrder(order, OrderState.CANCEL, mgresult.getMessage(),"88", roleUser);
                        return "预订失败!该段时间酒店价格发生变化，请致电芒果网客服电话40066-40066或者重新选择别的酒店！";
                    }
                }else{
                	 log.info("============HotelCompleteAction.saveHDLOrderInfo() new order failed resaon: addExRoomOrderResponse is null !");
                	 order.setCancelReason(88);
                	 orderService.cancelOrder(order, OrderState.CANCEL, "下直联订单失败！","88", roleUser);
                     return "预订失败!该段时间酒店价格发生变化，请致电芒果网客服电话40066-40066或者重新选择别的酒店！";
                }
            }
		return errMsg;
	}

    
    /**
     * 保存订单并返回订单CD
     * @param order
     * @return
     */
    private String processPaymentMethod(OrOrder order) {
        String orderCD = "";
       // if (log.isInfoEnabled()){
            log.info("Begin... HotelCompleteAction.processPaymentMethod():"
                + "orderService.updateOrder(order= " + order + ") begin...");
        //}
        Serializable orderId = orderService.saveOrUpdate(order);        
        orderCD = orderService.getOrderCDByID(orderId.toString());
        //if (log.isInfoEnabled()){
            log.info("Success... HotelCompleteAction.processPaymentMethod(:"
                    + "orderService.saveOrUpdate(orderId=" + orderId
                    + ") save success and orderService.getOrderCDByID(orderCD=" + orderCD
                    + ") success");
        //}
        if(null != orderId && null == order.getID()){
        	order.setID(new Long(orderId.toString()));
        }
        if(!StringUtil.isValidStr(order.getOrderCD())){
        	order.setOrderCD(orderCD);
            //发送传真没有订单号，必须填充订单号
            order.setOrderCDHotel(orderCD);
        }
        return orderCD;
    }
    
    /**
     * 发送确认短线给客人
     * @param order
     */
    public void sendImmedConfirmToCus(OrOrder order) throws Exception{
        if (StringUtil.isValidStr(order.getOrderCdForChannel())) {
				// 如果是担保单，则得判断是否担保成功
				if (order.getIsCreditAssured() && 0 != Double.compare(order.getSuretyPrice(), 0.0)
						&& StringUtil.isValidStr(order.getCreditCardIdsSelect())) {
					if (order.getSuretyState() == GuaranteeState.SUCCESS) {
						// 则进行即时确认给客人
						orderImmedConfirmService.sendImmedConfirmToCus(order, flagSecondConfirm,member);
					}
				} else {
					// 当返回确认号才给客人进行即时确认 add by shengwei.zuo 2010-6-11
					orderImmedConfirmService.sendImmedConfirmToCus(order, flagSecondConfirm,member);
				}
		}
    }
	
	    private void addReturnLog(OrOrder order,HotelOrderFromBean fromBean,MemberDTO member){
    	if(null != member){
    		OrHandleLog CashBack = new OrHandleLog();
			CashBack.setModifiedTime(new Date());
    		if(fromBean.isLoginBeforeBooking()){
    			if(member.isFitFlag()){
    				CashBack.setContent("已登录符合返现规则会籍");
    			}else{
    				CashBack.setContent("已登录不符合返现规则会籍");
    			}
    		}else{
    			CashBack.setContent("预订订单之前未登录，订单没有返现！");
    		}
    		CashBack.setModifierName("网站");
			CashBack.setOrder(order);
			order.getLogList().add(CashBack);
    		orderService.saveOrUpdate(order);
    	}
    	
    }

	/** getter and setter * */

	public String getOrderCD() {
		return orderCD;
	}

	public void setOrderCD(String orderCD) {
		this.orderCD = orderCD;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public IOrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(IOrderService orderService) {
		this.orderService = orderService;
	}

	public String getCardID() {
		return cardID;
	}

	public void setCardID(String cardID) {
		this.cardID = cardID;
	}

	public IHotelOrderCompleteService getHotelOrderCompleteService() {
		return hotelOrderCompleteService;
	}

	public void setHotelOrderCompleteService(
			IHotelOrderCompleteService hotelOrderCompleteService) {
		this.hotelOrderCompleteService = hotelOrderCompleteService;
	}

	public int getResultHK() {
		return resultHK;
	}

	public void setResultHK(int resultHK) {
		this.resultHK = resultHK;
	}


	public IOrderEditService getOrderEditService() {
		return orderEditService;
	}


	public void setOrderEditService(IOrderEditService orderEditService) {
		this.orderEditService = orderEditService;
	}


	public IBenefitService getBenefitService() {
		return benefitService;
	}


	public void setBenefitService(IBenefitService benefitService) {
		this.benefitService = benefitService;
	}


	public String getOverHalfDate() {
		return overHalfDate;
	}


	public void setOverHalfDate(String overHalfDate) {
		this.overHalfDate = overHalfDate;
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


	public HotelBookingService getHotelBookingService() {
		return hotelBookingService;
	}


	public void setHotelBookingService(HotelBookingService hotelBookingService) {
		this.hotelBookingService = hotelBookingService;
	}


	public ResourceManager getResourceManager() {
	        return resourceManagerA;
	    }

	public void setResourceManager(ResourceManager resourceManager) {
	        this.resourceManagerA = resourceManager;
	}
	    
	public IHDLService getHdlService() {
		return hdlService;
	}


	public void setHdlService(IHDLService hdlService) {
		this.hdlService = hdlService;
	}


    public int getRoomPrice() {
        return roomPrice;
    }


    public void setRoomPrice(int roomPrice) {
        this.roomPrice = roomPrice;
    }


	public OrderImmedConfirmService getOrderImmedConfirmService() {
		return orderImmedConfirmService;
	}


	public void setOrderImmedConfirmService(
			OrderImmedConfirmService orderImmedConfirmService) {
		this.orderImmedConfirmService = orderImmedConfirmService;
	}


	public boolean isChgPrice() {
		return chgPrice;
	}


	public void setChgPrice(boolean chgPrice) {
		this.chgPrice = chgPrice;
	}


    public List<HotelPKG> getSellHotelPKGList()
    {
        return sellHotelPKGList;
    }


    public void setSellHotelPKGList(List<HotelPKG> sellHotelPKGList)
    {
        this.sellHotelPKGList = sellHotelPKGList;
    }
    
    public void setReturnService(IHotelFavourableReturnService returnService) {
		this.returnService = returnService;
	}


	public HtlProjectCodeManage getHtlProjectCodeManage() {
		return htlProjectCodeManage;
	}


	public void setHtlProjectCodeManage(HtlProjectCodeManage htlProjectCodeManage) {
		this.htlProjectCodeManage = htlProjectCodeManage;
	}


	public int getTotalRoomNight() {
		return totalRoomNight;
	}


	public void setTotalRoomNight(int totalRoomNight) {
		this.totalRoomNight = totalRoomNight;
	}

}

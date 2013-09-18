package com.mangocity.fantiweb.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import com.mangocity.hdl.constant.ChannelType;
import com.mangocity.hotel.base.manage.IPriceManage;
import com.mangocity.hotel.base.persistence.B2BAgentCommUtils;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.persistence.HtlPriceType;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.persistence.OrAssureItemEvery;
import com.mangocity.hotel.base.persistence.OrGuaranteeItem;
import com.mangocity.hotel.base.persistence.OrReservation;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.constant.ConfirmType;
import com.mangocity.hotel.order.constant.EmergencyLevel;
import com.mangocity.hotel.order.constant.OrderSource;
import com.mangocity.hotel.order.constant.OrderType;
import com.mangocity.hotel.order.persistence.OrCouponRecords;
import com.mangocity.hotel.order.persistence.OrFellowInfo;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrPriceDetail;
import com.mangocity.hotel.order.web.PopulateOrderAction;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.hweb.persistence.AdditionalServeItem;
import com.mangocity.hweb.persistence.QueryHotelForWebBean;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.util.web.CookieUtils;
import com.mangocity.webnew.persistence.HotelOrderFromBean;
import com.mangocity.webnew.service.IHotelCheckOrderService;

/**
 * 新网站改版所有Action的父类
 * 主要提供一些公用的全局变量和封装Bean的方法
 * @author chenjiajie
 *
 */
public class GenericWebAction extends PopulateOrderAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4161480094911550478L;

    /**
     * 网站业务接口
     */
	protected HotelManageWeb hotelManageWeb;
	
	/**
	 * 订单审核service接口，包括一些对会员信息获取的方法封装 
	 */
	protected IHotelCheckOrderService hotelCheckOrderService;
	
	/**
	 * 订单信息页面传递FormBean
	 */
	protected HotelOrderFromBean hotelOrderFromBean;
	
	/**
	 * 担保条款、担保取消条款、预付取消条款
	 */
    protected OrReservation reservation;
    
    /**
     * 记录担保明细的数量
     */
    protected int guaranteeNum;
    
    /**
     * 记录入住人数量
     */
    protected int fellowNum;
    
    /**
     * 记录取消/修改条款数量
     */
    protected int assureNum;
    
    /**
     * 记录价格明细数量
     */
    protected int detailNum;
    
    /**
     * 记录代金券明细数量
     */
    protected int couponNum;
    
	/**
	 * 是否需要超时担保
	 */
    protected boolean needOvertimeAssure;

    /**
     * 是否需要超房数担保
     */
    protected boolean needOverRoomAssure;

    /**
     * 是否需要超房数担保
     */
    protected boolean needOverNightsAssure;
    
    /**
     * 代理商id
     */
    protected String agentid;
    
    
    /**
     * 网络合作渠道号
     */
    protected String projectCode;
    /**
     * 公共会员号CD 
     * 测试    cd:0010200473,id:5891593 
     * 生产    cd:0001397022,id:3065103 
     * 103环境 cd:0010202701,id:610521
     */
    protected static final String COMMONMEMBERCD = "0001397022";

    /**
     * 公用的会员编号, ID
     */
    protected static final String COMMONMEMBERID = "3065103";
    
 	/**
 	 * 会员点击“继续预定”的标志，值为true
 	 */
    protected Boolean direckbook;
    
    /**
     * 用于页面显示每天入住明细
     */
    protected List<AdditionalServeItem> priceTemplist = new ArrayList<AdditionalServeItem>();
    
    /**
     * 汇率
     */
    protected double rate;
    
    /**
     * 会员CD
     */
    protected String memberCd;
    
//    /**
//     * 用于保存香港中科获取的新价格，以便后续生成订单明细
//     */
//    protected String hkPrices;
//
//    /**
//     * 用于保存香港中科获取的底价，以便后续生成订单明细
//     */
//    protected String hkBasePrices;
	
	/**
     * 标志会员类型： 1：老会员；2：新会员；3：公共会员
     */
    protected int memClass;    

    /**
     * 是否需要发票
     */
    protected boolean isInvoice;
    
    
    private String saleDepartmentPay;
    
    /**
     * 联系人字符串用,分割
     */
    protected String linkeManStr;
    
    /**
     * 每间房担保金额
     */
    protected double orignalSuretyPrice;

    /**
     * 港澳酒店人民币价格
     */
    protected double RMBPrice;
    /**
     * 从何处预定：1：大陆酒店一般查询 2：大陆酒店地图查询 3：港澳酒店一般查询 4:港澳酒店地图查询 5:酒店详情页面
     * add by juesu.chen
     */
    protected int bookFrom;
    
	/**
     * 担保提示信息
     */
    protected String bookhintSpanValue;

    /**
     * 取消修改订单提示信息
     */
    protected String cancelModifyItem;
    
    /**
     * 担保明细页面传递字符串
     */
    protected String assureDetailStr;
    
    private IPriceManage priceManage;
    
    private HotelRoomTypeService hotelRoomTypeService;
    
    

	//订单来源
    private  String sourceB2b ;
    
    //订单类型
    private  int   typeB2b;

	public String getSaleDepartmentPay() {
		return saleDepartmentPay;
	}

	public void setSaleDepartmentPay(String saleDepartmentPay) {
		this.saleDepartmentPay = saleDepartmentPay;
	}

	/**
     * 取得页面的订单的担保明细List
     * @return
     */
	@SuppressWarnings("unchecked")
	protected List getOrGuaranteeItemList(){
		Map params = super.getParams();
		List guaranteeList = MyBeanUtil.getBatchObjectFromParam(params, OrGuaranteeItem.class,
	            guaranteeNum);
		if(null != guaranteeList && !guaranteeList.isEmpty()){
			guaranteeNum = guaranteeList.size();
		}
		return guaranteeList;
	}

    /**
     * 取得页面的订单的担保(预付)取消及修改条款List
     * @return
     */
	@SuppressWarnings("unchecked")
	protected List getOrAssureItemEveryList(){
		Map params = super.getParams();
		List assureList = MyBeanUtil.getBatchObjectFromParamWithClassName(params,
	            OrAssureItemEvery.class, assureNum);
		if(null != assureList && !assureList.isEmpty()){
			assureNum = assureList.size();
		}
		return assureList;
	}

    /**
     * 取得页面的入住人List 
     * @return
     */
	@SuppressWarnings("unchecked")
	protected List getOrFellowInfoList(){
		List<OrFellowInfo> fellowList = null;
		if(StringUtil.isValidStr(linkeManStr)){
			String[] linkManArr = linkeManStr.split(",");
			if(null != linkManArr && 0 < linkManArr.length){
				fellowList = new ArrayList<OrFellowInfo>(linkManArr.length);
				for (int i = 0; i < linkManArr.length; i++) {
					OrFellowInfo orFellowInfo = new OrFellowInfo();
					  if (hotelOrderFromBean.getPayMethod().equals("pre_pay")
					            && ChannelType.CHANNEL_CTS == hotelOrderFromBean.getRoomChannel() && hotelOrderFromBean.isFlagCtsHK()) {
						  log.info("GenericWebAction============getOrFellowInfoList====CTS name@Gender : " +linkManArr[i]);
						  String [] linkManFellowGenArr = linkManArr[i].split("@");
						  orFellowInfo.setFellowName(linkManFellowGenArr[1]);
						  orFellowInfo.setFellowGender(linkManFellowGenArr[0]);
						  
					  }else{
						  orFellowInfo.setFellowName(linkManArr[i]);
					  }
					fellowList.add(orFellowInfo);
				}
			}
		}
		return fellowList;
	}

    /**
     * 取得页面的订单价格明细 List 
     * @return
     */
	@SuppressWarnings("unchecked")
	protected List getOrPriceDetailList(){
		try{
			Map params = super.getParams();
			List detailList = MyBeanUtil.getBatchObjectFromParamWithClassName(params,
		            OrPriceDetail.class, detailNum);
			if(null != detailList && !detailList.isEmpty()){
				detailNum = detailList.size();
			}
			return detailList;
		}catch(Exception e){
			log.error(e);
			return null;
		}
		
		
	}
	
	/**
     * 取得页面的代金券明细 List 
     * @return
     */
	@SuppressWarnings("unchecked")
	protected List getOrCouponRecordsList(){
		Map params = super.getParams();
		List couponRecords = MyBeanUtil.getBatchObjectFromParam(params, OrCouponRecords.class, couponNum);
		if(null != couponRecords && !couponRecords.isEmpty()){
			couponNum = couponRecords.size();
		}
		return couponRecords;
	}
	
	/**
     * 处理重复提交
     */
    protected boolean isRepeatSubmit() {
        String strutsToken = (String) getParams().get("struts.token");
        String sessionToken = (String) getFromSession("struts.token.session");
        if (StringUtil.StringEquals2(strutsToken, sessionToken)) {
            return true;
        }
        putSession("struts.token.session", strutsToken);
        return false;
    }
    
    
    /**
     * 封装订单价格，订单价格、来源等相关的数据
     * @param order
     * @param hotelOrderFromBean
     */
    protected void pupulateOrderElement(OrOrder order,HotelOrderFromBean hotelOrderFromBean) {
    	//订单总价
		order.setSum(hotelOrderFromBean.getPriceNum()
				* Integer.parseInt(hotelOrderFromBean.getRoomQuantity()));
		//订单应付金额（RMB）
		String exchange = CurrencyBean.rateMap.get(hotelOrderFromBean
				.getCurrency());
		double dbExchange = StringUtil.getStrTodouble(exchange);
		order.setRateId(dbExchange);// 汇率
		if (0 == Double.compare(0D, dbExchange)) {
			dbExchange = 1;
			order.setRateId(1);// 汇率
			log.error("ErrCode:5642832 GenericWebAction.pupulateOrderElement() " +
					"CurrencyBean.rateMap.get(hotelOrderFromBean"
					+ ".getCurrency()) result = 0, set default rate is 1");
		}
		double sumRmb = hotelOrderFromBean.getPriceNum() * Integer.parseInt(hotelOrderFromBean.getRoomQuantity()) ;
		//如果是预付，繁体网站的sumRmb和sum一样
		if(PayMethod.PRE_PAY.equals(order.getPayMethod()) 
				|| order.isPayToPrepay()
				&& 0 < hotelOrderFromBean.getIsReduction()
				&& 0 < hotelOrderFromBean.getBenefitAmount()){
			sumRmb -= hotelOrderFromBean.getBenefitAmount() * Integer.parseInt(hotelOrderFromBean.getRoomQuantity());
		}

		//填充hotelOrderFromBean的实收金额
		else if(PayMethod.PAY.equals(order.getPayMethod())){
			hotelOrderFromBean.setActuralAmount(order.getSum());
		}
		order.setSumRmb(sumRmb);
		
		order.setCreator("HHKWEB");
		order.setPaymentCurrency(hotelOrderFromBean.getCurrency());
        if(PayMethod.PAY.equals(order.getPayMethod()) && CurrencyBean.RMB.equals(order.getPaymentCurrency())){
        	order.setActualPayCurrency(hotelOrderFromBean.getCurrency());
        }
        if(PayMethod.PAY.equals(order.getPayMethod()) && CurrencyBean.MOP.equals(order.getPaymentCurrency())){
        	order.setActualPayCurrency(CurrencyBean.MOP);
        }
        if(PayMethod.PRE_PAY.equals(order.getPayMethod())){
        	order.setActualPayCurrency(CurrencyBean.HKD);
        }
		//入住人列表
		order.setFellowList(hotelOrderFromBean.getFellowList());

		//订单的要扣配额类型(包房配额2，普通配额1)
		if(hotelOrderFromBean.getQuotaType()!=null&&!"".equals(hotelOrderFromBean.getQuotaType())){
			order.setQuotaTypeOld(hotelOrderFromBean.getQuotaType());
		}else{
			//如果是B2B订单
			if(typeB2b == 4){
				order.setQuotaTypeOld("1");
			}
		}
	
		
        //是代理商订单
        if(typeB2b == 4){
        	//设置代理订单类型
            order.setType(OrderType.TYPE_B2BAGENT);
            order.setSource("NET");
            
            //代理商编码code是 会员CD   add by shengwei.zuo 2010-1-11
            String agentCodeCook = CookieUtils.getCookieValue(request,"agentCode");
            if(agentCodeCook!=null && !"".equals(agentCodeCook)){
            	order.setAgentCode(agentCodeCook);
            }
        }else{
        	//订单来源
    		order.setSource(OrderSource.FROM_WEB);
    		//订单类型 :(1-mango, 2-114,4-B2B代理) 
    		order.setType(com.mangocity.hotel.order.constant.OrderType.TYPE_MANGO);
        }

		order.setCity(hotelOrderFromBean.getCityCode());
		//设置订单中台类型 
		orderAssist.setOrderHraType(order);
		
		List<OrFellowInfo> fellowList = order.getFellowList();
		if(fellowList!=null && fellowList.size()>0){
			String fellowNames = "";
			for (OrFellowInfo fellow : fellowList) {
				fellow.setOrder(order);
				fellowNames += fellow.getFellowName() + " ";// 加起来
			}
			//订单所有入住人字符串拼接
			order.setFellowNames(fellowNames);
		}	
		HtlRoomtype htlRoomType = hotelRoomTypeService.getHtlRoomTypeByIdAndHtlId(order
				.getHotelId().longValue(), order.getRoomTypeId().longValue());
		if(htlRoomType != null) {
			//订单房型名称
			order.setRoomTypeName(htlRoomType.getRoomName());
			List priceTypes = htlRoomType.getLstPriceType();
			for (int i = 0; i < priceTypes.size(); i++) {
				HtlPriceType htlPriceType = (HtlPriceType) priceTypes.get(i);
				if (htlPriceType.getID().equals(order.getChildRoomTypeId())) {
					//订单价格类型名称
					order.setChildRoomTypeName(htlPriceType.getPriceType());
					break;
				}
			}
		}
		
		
		//操作员工号
        String operaterIdCook = CookieUtils.getCookieValue(request,"operaterId");
        String agentCode = CookieUtils.getCookieValue(request, "agentCode");//加入代理商卡号
        if(operaterIdCook !=null&&!"".equals(operaterIdCook) && agentCode!=null && !"".equals(agentCode)){
        	order.setCreator(operaterIdCook);
        	order.setCreatorName(operaterIdCook);
        	/*try{
        		order.setCreatorName(URLDecoder.decode(CookieUtils.getCookieValue(request, "loginChnName"),"UTF-8"));
        	}catch (UnsupportedEncodingException e) {
    			log.error(e.getMessage(),e);
    		}*/
        	order.setMemberCd(agentCode);
        	
         }else{
			order.setCreator("繁体网站");
			order.setCreatorName("繁体网站");
		}
		
		
	}
    
    /**
	 * 封装订单基本数据，还包括预订条款、客户确认方式、会员信息等 (不分面预付/面付担保单)
	 */
	@SuppressWarnings("unchecked")
	protected void fillOrderBaseInfo(){
		try {
		Map params = super.getParams();
        hotelOrderFromBean = new HotelOrderFromBean();
            
        /* 封装预定条款相关 begin */
        MyBeanUtil.copyProperties(hotelOrderFromBean, params);
        
        reservation = new OrReservation();
            
        MyBeanUtil.copyProperties(reservation, params);
        
        //取得页面的订单的担保明细List  
        List<OrGuaranteeItem> guaranteeList = getOrGuaranteeItemList();
        if(null != guaranteeList && !guaranteeList.isEmpty()){
        	for (OrGuaranteeItem orGuaranteeItem : guaranteeList) {
        		orGuaranteeItem.setReserv(reservation);
			}
            reservation.setGuarantees(guaranteeList);
        }

        //取得页面的订单的担保(预付)取消及修改条款List 
        List<OrAssureItemEvery> assureList = getOrAssureItemEveryList();
        if(null != assureList && !assureList.isEmpty()){
        	for (OrAssureItemEvery orAssureItemEvery : assureList) {
        		orAssureItemEvery.setReserv(reservation);
			}
            reservation.setAssureList(assureList);
        }
        /* 封装预定条款相关 end */
        
        /* 封装入住人信息 */
        List fellowList = getOrFellowInfoList();
        hotelOrderFromBean.setFellowList(fellowList);

        /* 封装订单价格明细 */
        List detailList = getOrPriceDetailList();
        
        /* 封装代金券明细 */
        List<OrCouponRecords> couponRecords = getOrCouponRecordsList();
        request.setAttribute("couponRecords", couponRecords);
        
        //b2b代理
        if(order!=null){
        	 //订单来源
        	if(order.getSource()!=null&&!"".equals(String.valueOf(order.getSource()))){
            	sourceB2b  = order.getSource();
            }
            
            //订单类型
            if(String.valueOf(order.getType())!=null&&!"".equals(String.valueOf(order.getType()))){
            	typeB2b = order.getType();
            }
        }
        
        
        
        /* 订单基本信息装配 */
        order = new OrOrder();
        
        if (null != detailList && !detailList.isEmpty()) {
            for (Iterator it = detailList.iterator(); it.hasNext();) {
                OrPriceDetail item = (OrPriceDetail) it.next();
                item.setOrder(order);
            }
        }
        order.setPriceList(detailList);
        
        /* haochenyang RMS2718 start */
        Cookie[] cookies = request.getCookies();
//        //网络合作渠道号参数更改，改为projectcode,不在使用agentid。 回滚
//        String agentid = "";//代理渠道号 
//
//
//        if (null != cookies) {
//            for (int i = 0; i < cookies.length; i++) {
//                Cookie sCookie = cookies[i];
//                String svalue = sCookie.getValue();
//                String sname = sCookie.getName();
//                if (sname.equals("agentid")) {
//                    agentid = svalue;
//                }
//            }
//        }
        //代码回滚，projectcode参数修改延迟上线
        String projectCode ="";//网络合作渠道号
        if (null != cookies) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie sCookie = cookies[i];
                String svalue = sCookie.getValue();
                String sname = sCookie.getName();
                if (sname.equals("projectcode")) {
                	projectCode = svalue;
                }
            }
        }
        order.setAgentid(projectCode); // 渠道号
        /* haochenyang RMS2718 end */
        
        /* 代金券和订单的关系 */
        if(null != couponRecords && !couponRecords.isEmpty()){
        	for (Iterator it = couponRecords.iterator(); it
					.hasNext();) {
				OrCouponRecords orCouponRecord = (OrCouponRecords) it.next();
				orCouponRecord.setOrder(order);
			}
        }
        order.setCouponRecords(couponRecords);
        
        MyBeanUtil.copyProperties(order, hotelOrderFromBean);
        
        // v2.6 必须面转预，订单的支付方式需要由面付改成预付 ADD BY WUYUN 2009-06-04
        order.setPayMethod(hotelOrderFromBean.hasPayToPrepay() ? PayMethod.PRE_PAY : hotelOrderFromBean
            .getPayMethod());
        //给hotelOrderFromBean也修改，保证数据的一致性 add by diandian.hou 2011-1-9(引起了别的bug)
        
        // 组装预订条款 hotel2.6 有必要则获取取消修改规则信息
        order.setReservation(reservation);
        order.setFellowList(fellowList);
        //封装订单价格，订单价格、来源等相关的数据 
        pupulateOrderElement(order,hotelOrderFromBean);
        String agentCode = CookieUtils.getCookieValue(request, "agentCode");//加入代理商卡号
        order.setEmergencyLevel(EmergencyLevel.VIP1);
        if(typeB2b ==4){
        	order.setMemberCd(agentCode);
        	MemberDTO mr = memberInterfaceService
	                .getMemberByCode(order.getMemberCd());
            order.setMemberId(mr.getId());
        }else{
        	 //通过点击继续预订进来的
        	if(null != direckbook && direckbook){
    			//新会员或老会员
    			if(3 != memClass && StringUtil.isValidStr(memberCd)){
    				member = super.getMemberInfo(memberCd);
    			}
    			//公共会员
    			else{
    				member = super.getMemberSimpleInfoByMemberCd(COMMONMEMBERCD, false);
    			}
    		}
    		//直接登录进来的
    		else{
    			member = super.getMemberInfoForWeb(true);
    		}
            
        	//需要获取当前会员
            if (null != member) {
            	order.setMemberName(member.getName());
                order.setMemberCd(member.getMembercd());
                order.setMemberState(member.getState());
                order.setMemberId(member.getId());
            }else{
            	if(order.getCreator()!=null && !order.getCreator().equals("")){
            		if(order.getCreator().equals("网站")){
            			order.setMemberCd(COMMONMEMBERCD);
            			order.setMemberId(Long.parseLong(COMMONMEMBERID));
            		}else{
            			order.setMemberCd(agentCode);//加入代理商卡号
            			MemberDTO mr = memberInterfaceService
            	                .getMemberByCode(order.getMemberCd());
            			order.setMemberId(mr.getId());
            		}
            	}
            }
        }
        // 客户确认方式,界面上之所以没有按照ConfirmType里的定义来定义确认方式的值，是为了方便自动选择更高优先级的确认方式，级别由高到低为短信、电子邮件、电话、传真
        if (String.valueOf(ConfirmType.FAX).equals(hotelOrderFromBean.getConfirmtype())) {// 传真确认
            order.setConfirmType(ConfirmType.FAX);
        } else if (String.valueOf(ConfirmType.EMAIL).equals(hotelOrderFromBean.getConfirmtype())) {// 电子邮件确认
            order.setConfirmType(ConfirmType.EMAIL);
        } else if (String.valueOf(ConfirmType.SMS).equals(hotelOrderFromBean.getConfirmtype())) {// 短信确认
            order.setConfirmType(ConfirmType.SMS);
        } else if (String.valueOf(ConfirmType.PHONE).equals(hotelOrderFromBean.getConfirmtype())) {// 电话确认
            order.setConfirmType(ConfirmType.PHONE);
        }
        
        // 设置订单紧急程度
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("vipLevel", "0");
        if (null != member){
            order.setMemberId(member.getId());
            order.setMemberCd(member.getMembercd());
            order.setEmergencyLevel(orderAssist.getEmergency(order, new Date(), member, map));
            //订单记录会员的联名商家项目号
            order.setMemberAliasId(member.getAliasid());
        }
        
        /* 预付的时候，设置预付的支付方法  */
        if(PayMethod.PRE_PAY.equals(order.getPayMethod())){
        	order.setPrepayType(hotelOrderFromBean.getOrderPayType());
        }

        /* 页面显示用 begin */
        String[] priceStr = null;
        if(hotelOrderFromBean.getDatePriceStr()!=null){
        	
       	 if (1 < hotelOrderFromBean.getDatePriceStr().indexOf("@")) {
                priceStr = hotelOrderFromBean.getDatePriceStr().split("@");
            } else {
                priceStr = new String[] { hotelOrderFromBean.getDatePriceStr() };
            }
       	
        }   
       
        if(null != priceStr){
           for (int i = 0; i < priceStr.length; i++) {
               AdditionalServeItem item = new AdditionalServeItem();
               String[] items = priceStr[i].split("#");
               item.setValidDate(items[0]);
               item.setAmount(Double.parseDouble(items[1]));
               priceTemplist.add(item);
           }
       	
        }
        /* 页面显示用 end */
        
        
            reservation.setReservSuretyPrice(0.0);
            if (PayMethod.PAY.equals(hotelOrderFromBean.getPayMethod())
                && !hotelOrderFromBean.hasPayToPrepay()) {
            	//assureDetailStr担保明细页面传递字符串 
                if (StringUtil.isValidStr(assureDetailStr)) {
                    List assureDetailList = hotelManageWeb.convertToAssureInforAssistant(assureDetailStr);

                    if (0 != Double.compare(hotelCheckOrderService.calculateSuretyAmount(assureDetailList,
                        hotelOrderFromBean), 0.0)) {
                        // 原币种担保金额
                        int day = DateUtil.getDay(hotelOrderFromBean.getCheckinDate(),
                            hotelOrderFromBean.getCheckoutDate());
                        if (0 != day) {
                            day *= order.getRoomQuantity();
                        }
                        needOverNightsAssure = (needOverNightsAssure && day > order
                            .getReservation().getNights()) ? true : false;
                        
                        hotelOrderFromBean.setNeedAssure(true);
                        
                        orignalSuretyPrice = Math.ceil(hotelCheckOrderService.calculateSuretyAmount(assureDetailList,
                            hotelOrderFromBean));
                        RMBPrice = Math.ceil(orignalSuretyPrice * rate);
                        // reservation.setReservSuretyPrice(RMBPrice);
                        reservation.setReservSuretyPrice(RMBPrice
                            * Integer.parseInt(hotelOrderFromBean.getRoomQuantity()));
                        
                        hotelOrderFromBean.setOrignalSuretyPrice(orignalSuretyPrice
                        		*Integer.parseInt(hotelOrderFromBean.getRoomQuantity()));
                        hotelOrderFromBean.setSuretyPriceRMB(reservation.getReservSuretyPrice());
                    }

                }
            }
        } catch (Exception e) {
        	log.error(e);
        }
        
	}
	
	/**
	 * 把查询条件放到Session中
	 * @param queryHotelForWebBean
	 */
	protected void setWebBeanFromSession(QueryHotelForWebBean queryHotelForWebBean){
		Map session = super.getSession();
		session.put("queryHotelForWebBean", queryHotelForWebBean);
	}
	
	/**
	 * 异步取页数的时候放直接从Session中取查询条件 同时在Session中删除条件
	 * @return
	 */
	protected QueryHotelForWebBean getWebBeanFromSession(){
		QueryHotelForWebBean queryHotelForWebBean = null;
		Map session = super.getSession();		
		if(null != session){
			queryHotelForWebBean = (QueryHotelForWebBean) session.get("queryHotelForWebBean");
			session.remove("queryHotelForWebBean");
		}
		return queryHotelForWebBean;
	}
    
	
	 /**
     *  根据日期，酒店ID,房型ID，子房型ID，支付方式，查询出对应的代理佣金
     *  add by shengwei.zuo  2010-1-13
     */
	
	public B2BAgentCommUtils getB2BCommInfo(Date abselDate, long hotelId,
			long roomTypeID, long priceTypeId,String payMethod) {
		
		B2BAgentCommUtils  b2BAgentCommUtils  = new  B2BAgentCommUtils();
		
		//佣金税率
		//double commTaxDo = 0d;
		
		//代理佣金率
		double agentComissionRateDo = 0d;
		
		//代理佣金 
		double agentComissionDo = 0d;
		
		//代理的佣金价 
		double agentComissionPriceDo = 0d;
		
		HtlPrice  priceInfo = priceManage.getPricInfoByFor(abselDate, hotelId,roomTypeID, priceTypeId,payMethod);
		
		if(priceInfo.getFormulaId()==null||priceInfo.getFormulaId().equals("")
			||priceInfo.getFormulaId().equals("0")){
			//代理佣金率
			agentComissionRateDo =  priceInfo.getCommission()/priceInfo.getSalePrice() - 0.015;
			//代理佣金 
			agentComissionDo = priceInfo.getSalePrice() * agentComissionRateDo; 
			//代理的佣金价 
			agentComissionPriceDo = priceInfo.getSalePrice();
			 
		}else{
			//代理佣金率
			agentComissionRateDo = priceInfo.getCommissionRate() - 0.015;
			//代理佣金 
			agentComissionDo = (priceInfo.getCommission()/priceInfo.getCommissionRate())*agentComissionRateDo;
			//代理的佣金价 
			agentComissionPriceDo = priceInfo.getCommission()/priceInfo.getCommissionRate();
		}
		
		//设置代理佣金率
		b2BAgentCommUtils.setAgentComissionRate(agentComissionRateDo);
		//设置佣金类型
		b2BAgentCommUtils.setComissionType(1);
		//设置代理佣金
		b2BAgentCommUtils.setAgentComission(agentComissionDo);
		//设置代理佣金价
		b2BAgentCommUtils.setAgentComissionPrice(agentComissionPriceDo);
       
		return b2BAgentCommUtils;
		
	}
	
	//返回代理佣金价 add by shengwei.zuo  2010-1-13
	public String getagentComissionPrice(String abselDate, long hotelId,
			long roomTypeID, long priceTypeId,String payMethod){
		Date sdate = new Date();
		if(abselDate.length()==5){
			sdate =DateUtil.getB2BDate(abselDate);
		}else{
			sdate = DateUtil.getDate(abselDate);
		}
		
		B2BAgentCommUtils   b2BAgentCommUtils = new B2BAgentCommUtils(); 
		b2BAgentCommUtils = getB2BCommInfo(sdate,  hotelId,
				 roomTypeID,  priceTypeId, payMethod);
		if(b2BAgentCommUtils!=  null){
			
			return  String.valueOf(b2BAgentCommUtils.getAgentComissionPrice());
		}else{
			
			return "无" ;
		}
	}
	
	//返回代理佣金率 add by shengwei.zuo  2010-1-13
	public String getagentComissionRate(String abselDate, long hotelId,
			long roomTypeID, long priceTypeId,String payMethod){
		Date sdate = new Date();
		if(abselDate.length()==5){
			sdate =DateUtil.getB2BDate(abselDate);
		}else{
			sdate = DateUtil.getDate(abselDate);
		}
		B2BAgentCommUtils   b2BAgentCommUtils = new B2BAgentCommUtils();
		b2BAgentCommUtils = getB2BCommInfo(sdate,  hotelId,
				 roomTypeID,  priceTypeId, payMethod);
		
		if(b2BAgentCommUtils!=  null){
			String rate = "";
			if(String.valueOf(b2BAgentCommUtils.getAgentComissionRate()).length()>3){
				rate = String.valueOf(b2BAgentCommUtils.getAgentComissionRate()).substring(0,4);
			}
			return  rate;
			
		}else{
			
			return "无" ;
		}
		
	}
	
	
//	返回代理佣金率 add by shengwei.zuo  2010-1-13
	public String getagentComissionRateDouble(String abselDate, long hotelId,
			long roomTypeID, long priceTypeId,String payMethod,String strPrice){
		
		if(StringUtil.isValidStr(strPrice)){
			strPrice = strPrice.substring(strPrice.length()-1,strPrice.length());
			String str [] = strPrice.split(",");
			String str1 = "";
			String str2 = "";
			for(int i=0;i<str.length;i++){
				str1 = str[i];
				for(int j=1;j<str.length+1;j++){
					str2 = str[j];
					if(str1.equals(str2)){
						log.info("相同");
						break;
					}else{
						log.info("不相同");
						break;
					}
				}
				
				
			}
			
			
		}
		
		
		
		
		Date sdate = new Date();
		if(abselDate.length()==5){
			sdate =DateUtil.getB2BDate(abselDate);
		}else{
			sdate = DateUtil.getDate(abselDate);
		}
		B2BAgentCommUtils   b2BAgentCommUtils = new B2BAgentCommUtils();
		b2BAgentCommUtils = getB2BCommInfo(sdate,  hotelId,
				 roomTypeID,  priceTypeId, payMethod);
		
		if(b2BAgentCommUtils!=  null){
			String rate = "";
			if(String.valueOf(b2BAgentCommUtils.getAgentComissionRate()).length()>3){
				rate = String.valueOf(b2BAgentCommUtils.getAgentComissionRate()).substring(0,4);
			}
			return  rate;
			
		}else{
			
			return "无" ;
		}
		
	}
	
	
    /** getter and setter **/

	public HotelOrderFromBean getHotelOrderFromBean() {
		return hotelOrderFromBean;
	}

	public void setHotelOrderFromBean(HotelOrderFromBean hotelOrderFromBean) {
		this.hotelOrderFromBean = hotelOrderFromBean;
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

	public int getFellowNum() {
		return fellowNum;
	}

	public void setFellowNum(int fellowNum) {
		this.fellowNum = fellowNum;
	}

	public int getAssureNum() {
		return assureNum;
	}

	public void setAssureNum(int assureNum) {
		this.assureNum = assureNum;
	}

	public int getDetailNum() {
		return detailNum;
	}

	public void setDetailNum(int detailNum) {
		this.detailNum = detailNum;
	}

	public boolean isNeedOvertimeAssure() {
		return needOvertimeAssure;
	}

	public void setNeedOvertimeAssure(boolean needOvertimeAssure) {
		this.needOvertimeAssure = needOvertimeAssure;
	}

	public boolean isNeedOverRoomAssure() {
		return needOverRoomAssure;
	}

	public void setNeedOverRoomAssure(boolean needOverRoomAssure) {
		this.needOverRoomAssure = needOverRoomAssure;
	}

	public boolean isNeedOverNightsAssure() {
		return needOverNightsAssure;
	}

	public void setNeedOverNightsAssure(boolean needOverNightsAssure) {
		this.needOverNightsAssure = needOverNightsAssure;
	}

	public String getAgentid() {
		return agentid;
	}

	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public List<AdditionalServeItem> getPriceTemplist() {
		return priceTemplist;
	}

	public void setPriceTemplist(List<AdditionalServeItem> priceTemplist) {
		this.priceTemplist = priceTemplist;
	}

	public int getCouponNum() {
		return couponNum;
	}

	public void setCouponNum(int couponNum) {
		this.couponNum = couponNum;
	}

	public HotelManageWeb getHotelManageWeb() {
		return hotelManageWeb;
	}

	public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
		this.hotelManageWeb = hotelManageWeb;
	}

//	public String getHkPrices() {
//		return hkPrices;
//	}
//
//	public void setHkPrices(String hkPrices) {
//		this.hkPrices = hkPrices;
//	}

	public int getMemClass() {
		return memClass;
	}

	public void setMemClass(int memClass) {
		this.memClass = memClass;
	}

	public Boolean getDireckbook() {
		return direckbook;
	}

	public void setDireckbook(Boolean direckbook) {
		this.direckbook = direckbook;
	}

//	public String getHkBasePrices() {
//		return hkBasePrices;
//	}
//
//	public void setHkBasePrices(String hkBasePrices) {
//		this.hkBasePrices = hkBasePrices;
//	}

	public String getMemberCd() {
		return memberCd;
	}

	public void setMemberCd(String memberCd) {
		this.memberCd = memberCd;
	}

	public boolean isInvoice() {
		return isInvoice;
	}

	public void setInvoice(boolean isInvoice) {
		this.isInvoice = isInvoice;
	}

	public static String getCOMMONMEMBERCD() {
		return COMMONMEMBERCD;
	}

	public static String getCOMMONMEMBERID() {
		return COMMONMEMBERID;
	}

	public String getLinkeManStr() {
		return linkeManStr;
	}

	public void setLinkeManStr(String linkeManStr) {
		this.linkeManStr = linkeManStr;
	}

	public double getOrignalSuretyPrice() {
		return orignalSuretyPrice;
	}

	public void setOrignalSuretyPrice(double orignalSuretyPrice) {
		this.orignalSuretyPrice = orignalSuretyPrice;
	}

	public double getRMBPrice() {
		return RMBPrice;
	}

	public void setRMBPrice(double price) {
		RMBPrice = price;
	}

	public IHotelCheckOrderService getHotelCheckOrderService() {
		return hotelCheckOrderService;
	}

	public void setHotelCheckOrderService(
			IHotelCheckOrderService hotelCheckOrderService) {
		this.hotelCheckOrderService = hotelCheckOrderService;
	}

	public int getBookFrom() {
		return bookFrom;
	}

	public void setBookFrom(int bookFrom) {
		this.bookFrom = bookFrom;
	}
	public String getBookhintSpanValue() {
		return bookhintSpanValue;
	}

	public void setBookhintSpanValue(String bookhintSpanValue) {
		this.bookhintSpanValue = bookhintSpanValue;
	}

	public String getCancelModifyItem() {
		return cancelModifyItem;
	}

	public void setCancelModifyItem(String cancelModifyItem) {
		this.cancelModifyItem = cancelModifyItem;
	}

	public String getAssureDetailStr() {
		return assureDetailStr;
	}

	public void setAssureDetailStr(String assureDetailStr) {
		this.assureDetailStr = assureDetailStr;
	}

	public String getSourceB2b() {
		return sourceB2b;
	}

	public void setSourceB2b(String sourceB2b) {
		this.sourceB2b = sourceB2b;
	}

	public int getTypeB2b() {
		return typeB2b;
	}

	public void setTypeB2b(int typeB2b) {
		this.typeB2b = typeB2b;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	
	 // add by diandian.hou
    private double rate_HKDToRMB ;
    private double rate_HKDToRMBTwoDecimal;
    private  double rate_RMBToHKD;
    private double rate_RMBToHKDTwoDecimal;
	private double rate_MOPToRMB ;
	private double rate_MOPToHKD;
	private double rate_MOPToHKDTwoDecimal;
	//把rate保留几位，decimal写成2.0（应该为int型的） add by diandian.hou
	private double rateSaveInDecimal(double rate,double decimal){
		double rateNew = Math.ceil(rate*Math.pow(10.0, decimal));
		rateNew /= Math.pow(10.0, decimal);
		return rateNew;
	}

	public double getRate_HKDToRMB() {
		rate_HKDToRMB = Double.parseDouble(CurrencyBean.rateMap.get(
				CurrencyBean.HKD).toString());
		return rate_HKDToRMB;
	}

	public double getRate_HKDToRMBTwoDecimal() {
		rate_HKDToRMB = getRate_HKDToRMB();
		rate_HKDToRMBTwoDecimal = rateSaveInDecimal(rate_HKDToRMB,2.0);
		return rate_HKDToRMBTwoDecimal;
	}

	public double getRate_RMBToHKD() {
		rate_RMBToHKD = 1 / (Double.parseDouble(CurrencyBean.rateMap.get(CurrencyBean.HKD).toString()));  
		return rate_RMBToHKD;
	}

	public double getRate_RMBToHKDTwoDecimal() {
		rate_RMBToHKD = getRate_RMBToHKD();
		rate_RMBToHKDTwoDecimal = rateSaveInDecimal(rate_RMBToHKD,2.0);
		return rate_RMBToHKDTwoDecimal;
	}

	public double getRate_MOPToRMB() {
		rate_MOPToRMB = Double.parseDouble(CurrencyBean.rateMap.get(
				CurrencyBean.MOP).toString());
		return rate_MOPToRMB;
	}

	public double getRate_MOPToHKD() {
		rate_MOPToHKD = getRate_MOPToRMB()/getRate_HKDToRMB();
		return rate_MOPToHKD;
	}

	public double getRate_MOPToHKDTwoDecimal() {
		rate_MOPToHKD = getRate_MOPToHKD();
		rate_MOPToHKDTwoDecimal = rateSaveInDecimal(rate_MOPToHKD,2.0);
		return rate_MOPToHKDTwoDecimal;
	}
	/**
	 * 添加内地酒店预付服务价格转KH$ add by diandian.hou
	 * @param price 含早，含床等附加服务的人民币价格
	 * @return price 含早，含床等附加服务的港币价格
	 */
	public double priceToInt(double price){
		if (price==0.0){
    		return 0.0;
    	}else {
    		return Math.ceil(price);
    	}
	}

	public void setPriceManage(IPriceManage priceManage) {
		this.priceManage = priceManage;
	}
	
	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}
}

package com.mangocity.webnew.util.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;

import com.mangocity.hdl.constant.ChannelType;
import com.mangocity.hotel.base.manage.IPriceManage;
import com.mangocity.hotel.base.manage.IQuotaManage;
import com.mangocity.hotel.base.persistence.B2BAgentCommUtils;
import com.mangocity.hotel.base.persistence.HtlElAssure;
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
import com.mangocity.hotel.order.constant.OrderExtInfoType;
import com.mangocity.hotel.order.constant.OrderSource;
import com.mangocity.hotel.order.constant.OrderType;
import com.mangocity.hotel.order.persistence.OrCouponRecords;
import com.mangocity.hotel.order.persistence.OrFellowInfo;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderExtInfo;
import com.mangocity.hotel.order.persistence.OrPointRecords;
import com.mangocity.hotel.order.persistence.OrPriceDetail;
import com.mangocity.hotel.order.service.HotelElOrderService;
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
import com.mangocity.webnew.persistence.ElongAssureResult;
import com.mangocity.webnew.persistence.HotelOrderFromBean;
import com.mangocity.webnew.service.CheckElongAssureService;
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
	
	private IPriceManage priceManage;
	
	/**
	 * 订单审核service接口，包括一些对会员信息获取的方法封装 
	 */
	protected IHotelCheckOrderService hotelCheckOrderService;
	
	private HotelRoomTypeService hotelRoomTypeService;
	
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
    
    //游比比传给我们的跟踪号
    protected String track_code;
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
    
    private IQuotaManage quotaManage;
    
    //订单来源
    private  String sourceB2b ;
    
    //订单类型
    private  int   typeB2b;
    
    protected HotelElOrderService hotelElOrderService;
    
    protected CheckElongAssureService checkElongAssureService; 
    
    // add by diandian.hou
    private double rate_HKDToRMB = Double.parseDouble(CurrencyBean.rateMap.get(
			CurrencyBean.HKD).toString());
    private  double rate_RMBToHKD = 1 / (Double.parseDouble(CurrencyBean.rateMap.get(
			CurrencyBean.HKD).toString()));    
	private double rateMOPToRMB = Double.parseDouble(CurrencyBean.rateMap.get(
			CurrencyBean.HKD).toString());
	private double rateMOPToHKD = rateMOPToRMB/rate_HKDToRMB;

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
						//2012.2.17 添加入住人类型 add by luoguangming 性别@姓名（hotelII） 或 性别@姓名@入住人类型（b2b）
						  if(linkManFellowGenArr.length>2){
							  orFellowInfo.setFellowManType(linkManFellowGenArr[2]);
							  orFellowInfo.setFellowNationality(linkManFellowGenArr[2]);//2012-4-16 add by luoguangming b2b保存内外宾
						  }
					  }else{
						//2012.2.17 添加入住人类型 add by luoguangming 姓名（hotelII） 或 姓名@入住人类型（b2b）
						  String [] linkManFellowGenArr = linkManArr[i].split("@");
						  orFellowInfo.setFellowName(linkManFellowGenArr[0]);
						  if(linkManFellowGenArr.length>1){
							  orFellowInfo.setFellowManType(linkManFellowGenArr[1]);
							  orFellowInfo.setFellowNationality(linkManFellowGenArr[1]);//2012-4-16 add by luoguangming b2b保存内外宾
						  }
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
    
	// 封装订单的积分记录明细 add by diandian.hou 2011-11-22
	protected List<OrPointRecords> getPointRecordsList(Long[] pointValues){
		List<OrPointRecords> pointRecordsList = new ArrayList<OrPointRecords>(1);
		for(int i = 0; i<pointValues.length;i++){
			OrPointRecords orPointRecords = new OrPointRecords();
			orPointRecords.setPointValue(pointValues[i]);
			orPointRecords.setOperateTime(new Date());
			orPointRecords.setNotes("hotelII中旅支持积分");
			pointRecordsList.add(orPointRecords);
	   }
		return pointRecordsList;
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
		double sumRmb = hotelOrderFromBean.getPriceNum() * Integer.parseInt(hotelOrderFromBean.getRoomQuantity()) * dbExchange;
		//如果是预付，优惠立减 订单应付金额= 总金额-立减总金额
		if((PayMethod.PRE_PAY.equals(order.getPayMethod()) 
				|| order.isPayToPrepay())
				&& 0 < hotelOrderFromBean.getIsReduction()
				&& 0 < hotelOrderFromBean.getBenefitAmount()){
			sumRmb -= hotelOrderFromBean.getBenefitAmount() * Integer.parseInt(hotelOrderFromBean.getRoomQuantity());
		}

		//填充hotelOrderFromBean的实收金额
		else if(PayMethod.PAY.equals(order.getPayMethod())){
			hotelOrderFromBean.setActuralAmount(order.getSum() * order.getRateId());
		}
		order.setSumRmb(sumRmb);
		
		order.setCreator("HWEB");
		order.setPaymentCurrency(hotelOrderFromBean.getCurrency());
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
		HtlRoomtype htlRoomType = hotelRoomTypeService.getHtlRoomTypeByIdAndHtlId(order.getHotelId(), 
				order.getRoomTypeId());
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
			order.setCreator("网站");
			order.setCreatorName("网站");
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
        
        //封装积分明细  add by diandian.hou 2011-11-22
        if(null != hotelOrderFromBean.getUlmPoint() && Long.parseLong(hotelOrderFromBean.getUlmPoint())>0L){
        	Long pointValue = Long.parseLong(hotelOrderFromBean.getUlmPoint());
            Long[] pointValues = new Long[]{pointValue};
            List<OrPointRecords> pointRecordsList = getPointRecordsList(pointValues);
            for (int i = 0 ;i<pointRecordsList.size();i++){
            	OrPointRecords orPointRecords = pointRecordsList.get(i);
            	orPointRecords.setOrder(order);
            }
            order.setPointRecordsList(pointRecordsList);
        }
        
        
        MyBeanUtil.copyProperties(order, hotelOrderFromBean);
        
        // v2.6 必须面转预，订单的支付方式需要由面付改成预付 ADD BY WUYUN 2009-06-04
        order.setPayMethod(hotelOrderFromBean.hasPayToPrepay() ? PayMethod.PRE_PAY : hotelOrderFromBean
            .getPayMethod());
        
        // 组装预订条款 hotel2.6 有必要则获取取消修改规则信息
        order.setReservation(reservation);
        order.setFellowList(fellowList);
        //封装订单价格，订单价格、来源等相关的数据 
        pupulateOrderElement(order,hotelOrderFromBean);
        String agentCode = CookieUtils.getCookieValue(request, "agentCode");//加入代理商卡号
        order.setEmergencyLevel(EmergencyLevel.VIP1);
        if(typeB2b ==4){
        	try{
        		order.setMemberCd(agentCode);
            	MemberDTO mr = memberInterfaceService
    	                .getMemberByCode(order.getMemberCd());
                order.setMemberId(mr.getId());
        	}catch(Exception e){
        		log.error(e);
        	}
        	
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
        }else if (String.valueOf(ConfirmType.NO).equals(hotelOrderFromBean.getConfirmtype())){//add by luoguangming 2012年6月4日11:36:57
        	order.setConfirmType(ConfirmType.NO);
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
                //取出艺龙担保条款，放进去 
                HtlElAssure htlElAssure = null;
                if(ChannelType.CHANNEL_ELONG == hotelOrderFromBean.getRoomChannel()){//判断是否需要担保
                	if(params.get("firstPrice")!=null){
                    	hotelOrderFromBean.setFirstNightSalePrice(Double.parseDouble(String.valueOf(params.get("firstPrice"))));
                    }
                	ElongAssureResult assureReslut = checkElongAssureService.checkIsAssure(hotelOrderFromBean.getHotelId(), 
                			hotelOrderFromBean.getChildRoomTypeId(), 
                			hotelOrderFromBean.getCheckinDate(), 
                			hotelOrderFromBean.getCheckoutDate(), 
                			Integer.parseInt(hotelOrderFromBean.getRoomQuantity()), 
                			hotelOrderFromBean.getLatestArrivalTime());
                	if(assureReslut.getAssureType()>0){
                		double assureMoneyTotal = 0d;
                		if(assureReslut.getAssureMoneyType()==1){
                			assureMoneyTotal = hotelOrderFromBean.getFirstNightSalePrice()*Integer.parseInt(hotelOrderFromBean.getRoomQuantity());
                		}else{
                			assureMoneyTotal = hotelOrderFromBean.getPriceNum()*Integer.parseInt(hotelOrderFromBean.getRoomQuantity());
                		}
                		htlElAssure = new HtlElAssure();
                		htlElAssure.setAssureAmount(assureMoneyTotal);
                		htlElAssure.setAssureCondition(assureReslut.getConditionStr());
                		htlElAssure.setAssureDateDay(DateUtil.dateToString(assureReslut.getModifyBeforeDate()));
                		htlElAssure.setAssureRoomQuantity(assureReslut.getOverQtyNum());
                		htlElAssure.setAssureSaveDate((assureReslut.getOverTimeHour()<10 ? "0"+assureReslut.getOverTimeHour():assureReslut.getOverTimeHour())
                				+":"
                				+(assureReslut.getOverTimeMin()<10 ? "0"+assureReslut.getOverTimeMin():assureReslut.getOverTimeMin()));
                		htlElAssure.setAssureDate(htlElAssure.getAssureSaveDate());
                		htlElAssure.setAssureType(assureReslut.getAssureType()==4 ? 5 : assureReslut.getAssureType());
                		htlElAssure.setCancelAndModifySentence(assureReslut.getModifyStr());
                		htlElAssure.setCancelAssureType("2");
                		htlElAssure.setMoneyType(assureReslut.getAssureMoneyType());
                		//保存elong担保和取消规则
                		OrOrderExtInfo ext = new OrOrderExtInfo();
                		ext.setOrder(order);
                		ext.setType(OrderExtInfoType.ELONG_ASSURE_TIP);
                		ext.setContext(assureReslut.getConditionStr());
                		OrOrderExtInfo ext2 = new OrOrderExtInfo();
                		ext2.setOrder(order);
                		ext2.setType(OrderExtInfoType.ELONG_ASSURE_MODIFY);
                		ext2.setContext(assureReslut.getModifyStr());
                		order.getOrOrderExtInfoList().add(ext);
                		order.getOrOrderExtInfoList().add(ext2);
                		
                	}else{
                		htlElAssure = null;
                	}
                	if(null != htlElAssure){
                		setElAssureRuleToReservation(reservation,htlElAssure);
                		guaranteeList = new ArrayList<OrGuaranteeItem>();
              		   	if(1 == htlElAssure.getMoneyType()){//首日担保
              			    OrGuaranteeItem orGuaranteeItem = new OrGuaranteeItem();
              	       		orGuaranteeItem.setAssureType("2");
              	       		if(htlElAssure.getAssureType()==1){
              	       			orGuaranteeItem.setAssureCondiction("无条件担保");
              	       		}else if(2 == htlElAssure.getAssureType()){
              	       			orGuaranteeItem.setOverRoomNumber(htlElAssure.getAssureRoomQuantity());
              	       			orGuaranteeItem.setLatestAssureTime(htlElAssure.getAssureDate());
              	       			orGuaranteeItem.setAssureCondiction("超房数("+htlElAssure.getAssureRoomQuantity()+")");	
              	       		}else if(3 == htlElAssure.getAssureType()){
              	       			orGuaranteeItem.setOverRoomNumber(htlElAssure.getAssureRoomQuantity());
              	       			orGuaranteeItem.setLatestAssureTime(htlElAssure.getAssureDate());
              	       			orGuaranteeItem.setAssureCondiction("超时("+htlElAssure.getAssureSaveDate()+")");
              	       		}else if(5 == htlElAssure.getAssureType()){
              	       			orGuaranteeItem.setLatestAssureTime(htlElAssure.getAssureDate());
              	       			orGuaranteeItem.setOverRoomNumber(htlElAssure.getAssureRoomQuantity());
              	       			orGuaranteeItem.setAssureCondiction("超时("+htlElAssure.getAssureSaveDate()+"),"+"超房数("+htlElAssure.getAssureRoomQuantity()+")");
              	       		}
              	       		//orGuaranteeItem.setAssureCondiction(htlElAssure.getAssureCondition());
              	       		orGuaranteeItem.setNight(order.getCheckinDate());
              	       		orGuaranteeItem.setReserv(reservation);
              	       		guaranteeList.add(orGuaranteeItem);  
              		   	}else if(2 == htlElAssure.getMoneyType()){//全额
              			   for(int i=0;i<DateUtil.getDay(order.getCheckinDate(), order.getCheckoutDate());i++){
              				   OrGuaranteeItem orGuaranteeItem = new OrGuaranteeItem();
              				   orGuaranteeItem.setAssureType("4");
              				   
	              				if(htlElAssure.getAssureType()==1){
	               	       			orGuaranteeItem.setAssureCondiction("无条件担保");
	               	       		}else if(2 == htlElAssure.getAssureType()){
	               	       			orGuaranteeItem.setOverRoomNumber(htlElAssure.getAssureRoomQuantity());
	               	       			orGuaranteeItem.setLatestAssureTime(htlElAssure.getAssureDate());
	               	       			orGuaranteeItem.setAssureCondiction("超房数("+htlElAssure.getAssureRoomQuantity()+")");	
	               	       		}else if(3 == htlElAssure.getAssureType()){
	               	       			orGuaranteeItem.setOverRoomNumber(htlElAssure.getAssureRoomQuantity());
	               	       			orGuaranteeItem.setLatestAssureTime(htlElAssure.getAssureDate());
	               	       			orGuaranteeItem.setAssureCondiction("超时("+htlElAssure.getAssureSaveDate()+")");
	               	       		}else if(5 == htlElAssure.getAssureType()){
	               	       			orGuaranteeItem.setLatestAssureTime(htlElAssure.getAssureDate());
	               	       			orGuaranteeItem.setOverRoomNumber(htlElAssure.getAssureRoomQuantity());
	               	       			orGuaranteeItem.setAssureCondiction("超时("+htlElAssure.getAssureSaveDate()+"),"+"超房数("+htlElAssure.getAssureRoomQuantity()+")");
	               	       		}
              		       		//orGuaranteeItem.setAssureCondiction(htlElAssure.getAssureCondition());
              		       		orGuaranteeItem.setNight(DateUtil.getDate(order.getCheckinDate(),i));
              		       		orGuaranteeItem.setReserv(reservation);
              		       		guaranteeList.add(orGuaranteeItem);  
              			   }
              		   	}
              		   	reservation.setGuarantees(guaranteeList);
                		List<OrAssureItemEvery> itemlist = new ArrayList<OrAssureItemEvery>();
                		OrAssureItemEvery item = new OrAssureItemEvery();
                		item.setType(htlElAssure.getCancelAssureType());
                    	item.setFirstDateOrDays(htlElAssure.getAssureDateDay());
                    	item.setFirstTime(htlElAssure.getAssureSaveDate());
                		item.setReserv(reservation);
                		itemlist.add(item);
                		reservation.setAssureList(itemlist);
                		order.setReservation(reservation);
                    	boolean judgeNeedAssure = assureReslut.isNeedAssure();
                		hotelOrderFromBean.setNeedAssure(judgeNeedAssure);
                		if(judgeNeedAssure) {
                			hotelOrderFromBean.setSuretyPriceRMB(reservation.getReservSuretyPrice());
                			hotelOrderFromBean.setOrignalSuretyPrice(reservation.getReservSuretyPrice());
                		}
                	}else{
                		hotelOrderFromBean.setNeedAssure(false);
                		reservation.setUnCondition(false);
                		reservation.setRoomsAssure(false);
                		reservation.setOverTimeAssure(false);
                		reservation.setNightsAssure(false);
                		order.setReservation(reservation);
                	}
            	}		
            }
        } catch (Exception e) {
        	log.error(e);
        }
        
	}
	//封装艺龙担保明细
	   public void setElAssureRuleToReservation(OrReservation reservation,HtlElAssure htlElAssure){
		   if(1 == htlElAssure.getAssureType()){
			   reservation.setUnCondition(true);
			   reservation.setOverTimeAssure(false);
			   reservation.setNightsAssure(false);
			   reservation.setRoomsAssure(false);
			   reservation.setNights(0);
		   }	
		   if(3 == htlElAssure.getAssureType()){
		   		reservation.setUnCondition(false);
		   		reservation.setOverTimeAssure(true);
		   		reservation.setNightsAssure(false);
				reservation.setRoomsAssure(false);	
				reservation.setNights(0);
		   		reservation.setLateSuretyTime(htlElAssure.getAssureDate());
	  		}
	  		if(2 == htlElAssure.getAssureType()){
	  			reservation.setUnCondition(false);
	  			reservation.setOverTimeAssure(false);
	  			reservation.setRoomsAssure(true);
	  			reservation.setNightsAssure(false);
	  			reservation.setNights(0);
	  			reservation.setRooms(htlElAssure.getAssureRoomQuantity());
	  		}
	  		if(5 == htlElAssure.getAssureType()){
	  			reservation.setUnCondition(false);
	  			reservation.setOverTimeAssure(true);
	  			reservation.setRoomsAssure(true);
	  			reservation.setNightsAssure(false);
	  			reservation.setNights(0);
	  			reservation.setLateSuretyTime(htlElAssure.getAssureDate());
	  			reservation.setRooms(htlElAssure.getAssureRoomQuantity());
	  		}
	  	   if(rate<=0) rate=1.0;
		   reservation.setReservSuretyPrice(Math.ceil(htlElAssure.getAssureAmount()*rate));
		   OrGuaranteeItem guarantee = new OrGuaranteeItem();
		   guarantee.setNight(hotelOrderFromBean.getCheckinDate());
		   guarantee.setLatestAssureTime(htlElAssure.getAssureSaveDate());
		   guarantee.setOverRoomNumber(htlElAssure.getAssureRoomQuantity());
		   guarantee.setAssureType(htlElAssure.getMoneyType()==1 ? "2" : "4");
		   guarantee.setAssureLetter("否");
		   String assureCondiction = reservation.isOverTimeAssure() ? "超时担保("+htlElAssure.getAssureSaveDate()+")" : "";
		   assureCondiction += reservation.isRoomsAssure() ? "超房担保("+htlElAssure.getAssureRoomQuantity()+")" : "";
		   guarantee.setAssureCondiction(assureCondiction);
		   guarantee.setReserv(reservation);
		   reservation.getGuarantees().clear();
		   reservation.getGuarantees().add(guarantee);   
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
	
	/**
	 * 添加内地酒店预付服务价格转KH$ add by diandian.hou
	 * @param price 含早，含床等附加服务的人民币价格
	 * @return price 含早，含床等附加服务的港币价格
	 */
	public double price_RMBToHK_additionalServe(double price){
		if (price==0.0){
    		return 0.0;
    	}else {
    		return Math.ceil(price);
    	}
	}
	/**
	 * 封装apacheSessionId
	 * @return
	 */
	public String getApacheSessionId(){
		String clientIp = hotelManageWeb.getIpAddr(request);
		String sessionId = CookieUtils.getCookieValue(request, "SessionID");
		String apacheSessionId= null;
		if(null != sessionId && !"".equals(sessionId)){
			apacheSessionId = clientIp+"."+sessionId;//添加sessionId
		}
	return apacheSessionId;
	}
	
	protected  void addLogInfo(HotelOrderFromBean hotelOrderFromBean,String step){
		//增加日志信息输出
		StringBuilder bookinfo=new StringBuilder();
		bookinfo.append(step+"   ");
		if(hotelOrderFromBean!=null){
			bookinfo.append(" hotelId: ");
			bookinfo.append(String.valueOf(hotelOrderFromBean.getHotelId()));
			bookinfo.append(" priceTypeId: ");
			bookinfo.append(String.valueOf(hotelOrderFromBean.getChildRoomTypeId()));
			bookinfo.append("book date:");
			bookinfo.append(DateUtil.dateToString(hotelOrderFromBean.getCheckinDate()));
			bookinfo.append(DateUtil.dateToString(hotelOrderFromBean.getCheckoutDate()));
		}
		 log.info(bookinfo.toString());
	}
	
	/**
	 * 解决参数中的sql注入问题，主要是把 单引号全部替换成双引号
	 */
	@SuppressWarnings("unchecked")
	protected void setParamsSqlReplace(Map params){
		if (params != null) {
			Set nameSets = params.keySet();
			Iterator names = nameSets.iterator();
			while (names.hasNext()) {
				String name = (String) names.next();

				if ((params.get(name) instanceof String[])) {
					String[] values = (String[]) params.get(name);
					for (int i = 0; i < values.length; i++) {
						if (values[i] != null) {
							values[i] = values[i].replaceAll("'", "\"");
						}
					}
					params.put(name, values);

				}
				if ((params.get(name) instanceof String)) {
					String value = (String) params.get(name);
					if (value != null) {
						value = value.replaceAll("'", "\"");
					}
					params.put(name, value);
				}
			}

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

	public IQuotaManage getQuotaManage() {
		return quotaManage;
	}

	public void setQuotaManage(IQuotaManage quotaManage) {
		this.quotaManage = quotaManage;
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
	

	public String getTrack_code() {
		return track_code;
	}

	public void setTrack_code(String track_code) {
		this.track_code = track_code;
	}

	public double getRate_RMBToHKD() {
		return rate_RMBToHKD;
	}

	public double getRate_HKDToRMB() {
		return rate_HKDToRMB;
	}

	public double getRateMOPToHKD() {
		return rateMOPToHKD;
	}

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}

	public HotelElOrderService getHotelElOrderService() {
		return hotelElOrderService;
	}

	public void setHotelElOrderService(HotelElOrderService hotelElOrderService) {
		this.hotelElOrderService = hotelElOrderService;
	}

	public CheckElongAssureService getCheckElongAssureService() {
		return checkElongAssureService;
	}

	public void setCheckElongAssureService(
			CheckElongAssureService checkElongAssureService) {
		this.checkElongAssureService = checkElongAssureService;
	}

	
}

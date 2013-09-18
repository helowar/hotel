package com.mangocity.hagtb2b.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hagtb2b.service.IB2bService;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.OrReservation;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.constant.OrderExtInfoType;
import com.mangocity.hotel.order.persistence.B2bModifyOrderInfo;
import com.mangocity.hotel.order.persistence.OrFellowInfo;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderExtInfo;
import com.mangocity.hotel.order.persistence.OrOrderFax;
import com.mangocity.hotel.order.persistence.OrOrderItem;
import com.mangocity.hotel.order.persistence.OrPayment;
import com.mangocity.hotel.order.persistence.OrRefund;
import com.mangocity.hotel.order.persistence.view.OrPaymentVO;
import com.mangocity.hotel.order.persistence.view.OrRefundVO;
import com.mangocity.hotel.order.web.OrderAction;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.config.ConfigUtil;
import com.mangocity.util.web.CookieUtils;
import com.mangocity.util.web.DesCryptUtil;

/**
 * 会员浏览订单
 * 
 * @author neil
 * 
 */
public class B2bOrderBrowseAction extends OrderAction {

    private static final long serialVersionUID = 8729347252487743131L;

    private static final String BILLDETAIL_URL = 
        ConfigUtil.getResourceByKey("hotelii_i_delivery.billdetail_url");

    private static final String RESDETAIL_URL = 
        ConfigUtil.getResourceByKey("hotelii_i_delivery.resdetail_url");

//    static String PREDETAIL_URL = ConfigUtil.getResourceByKey("hotelii_i_preauth.detail_url");
    
    private IB2bService b2bService;

    private String orderCD;
    
    private String mostStar;

    private String mostPriceLevel;

    private String memberId;

    private String memberCD;
    
    private String budgetNo;
    
    private String operaterId;

//    String orderStatus;

    /**
     * 担保提示信息 hotel2.6 add by chenjiajie 2009-05-25
     */
    private String bookhintSpanValue;

    /**
     * 取消修改订单提示信息 hotel2.6 add by chenjiajie 2009-05-25
     */
    private String cancelModifyItem;

    /**
     * 用于订单基本信息修改相关操作 hotel2.6 add by chenjiajie 2009-05-25
     */
    private HotelManageWeb hotelManageWeb;
    Map<String,String> reasonMap = new HashMap<String,String>();
    
    /**
     * 订单修改页面 为b2bordermodify.jsp
     * 订单修改页面所传参数 特殊要求
     */
    private String specialRequest;
    
    /**
     * 订单修改页面所传参数 联系人姓名
     */
    private String linkMan;
    
    /**
     * 订单修改页面所传参数 手机
     */
    private String mobile;
    
    /**
     * 订单修改页面所传参数 传真
     */
    private String customerFax;
    
    /**
     * 订单修改页面所传参数 电话
     */
    private String telephone;
    
    /**
     * 订单修改页面所传参数 email
     */
    private String email;
    
    /**
     * 订单修改页面所传参数 付款方式
     */
    private String payMethod;
    
    /**
     *订单修改页面所传参数  预订确认方式
     */
    private String confirmType;    
    
    /**
     * 订单的状态
     */
    private String state;

    private String showCommissionFlag;
    
    private static String SHOW_COMMISSION_FLAG="showCommissionFlag";

    public String getShowCommissionFlag() {
		return showCommissionFlag;
	}

	public void setShowCommissionFlag(String showCommissionFlag) {
		this.showCommissionFlag = showCommissionFlag;
	}

	public String member() {
        try {
            MemberDTO member = memberInterfaceService.getMemberByCode(memberCD);
            memberId = String.valueOf(member.getId());
           // if (log.isInfoEnabled())
                log.info("OrderBrowseAction member method success,memberId=" + memberId);
        } catch (Exception me) {
            log
                .error("OrderBrowseAction member method " +
                        "exception, get the member error, the " +
                        "cause is:"
                    + me);
        }
        return "member";
    }

    /**
     * 显示订单内容
     */
    public String browse() {
        order = getOrder(orderId);
        if (null == order) {
            return forwardError("order对象为空！");
        }
        boolean bFromAudit = true;

        String isFromFront = (String) getParams().get("isFromFront");
        putSession("isFromFront", isFromFront);

        if (order.isPrepayOrder()) {
            String[] names = { "Cash", "CredInt", "CredDom", "Pt", "Bank", "Voucher", "Pos" };
            OrPaymentVO[] selPayment = new OrPaymentVO[7];
            for (int i = 0; 7 > i; i++) {
                selPayment[i] = new OrPaymentVO(names[i]);
            }
            List list = order.getPaymentList();
            for (int i = 0; i < list.size(); i++) {
                OrPayment payment = (OrPayment) list.get(i);
                selPayment[payment.getPayType() - 1].setPayment(payment);
            }
            request.setAttribute("selPayment", selPayment);
        }
        if (order.isHasPrepayed()) {
            String[] names = { "Cash", "CredInt", "CredDom", "Pt", "Bank", "Voucher", "Pos" };
            OrRefundVO[] selRefund = new OrRefundVO[7];
            for (int i = 0; 7 > i; i++) {
                selRefund[i] = new OrRefundVO(names[i]);
            }
            List list = order.getRefundList();
            for (int i = 0; i < list.size(); i++) {
                OrRefund refund = (OrRefund) list.get(i);
                selRefund[refund.getRefundType() - 1].setRefund(refund);
            }
            request.setAttribute("selRefund", selRefund);
        }
        if (false == bFromAudit) {
            request.setAttribute("oftenFellowCount", null == member.getFellowList() ? 0 : member
                .getFellowList().size());
            request.setAttribute("oftenLinkmanCount", null == member.getLinkmanList() ? 0 : member
                .getLinkmanList().size());
        }
        // // 查看预授权工单信息
        // if(order.isHasCreatePreAuth()) {
        // try {
        // String preFlag = creditcardPreAuthService.getPreAuthSucceedFlag("HOTEL",
        // order.getOrderCD());
        // request.setAttribute("preFlag", preFlag);
        // } catch (RemoteException re) {
        // log.error(4e.getMessage(),4e);
        // }
        // request.setAttribute("preUrl", predetail_url);
        // }
        request.setAttribute("preUrl", "");

        // 查看配送单内容url
        if (StringUtil.isValidStr(order.getFulfillmentCD())) {
            request.setAttribute("fulUrl", BILLDETAIL_URL);
        }
        request.setAttribute("fulResUrl", RESDETAIL_URL);

        request.setAttribute("orderItemTotal", order.getOrderItems().size());

        int assureListSize = 0;
        OrReservation orReserv = order.getReservation();
        if (null != orReserv) {
            assureListSize = orReserv.getAssureList().size();
        }
        request.setAttribute("assureListSize", assureListSize);

        String creditCardIdsSelect = order.getCreditCardIdsSelect();
        if (StringUtil.isValidStr(creditCardIdsSelect)) {
            creditCardIdsSelect = creditCardIdsSelect.replaceAll("&", ",");
        }
        request.setAttribute("creditCardIdsSelect", creditCardIdsSelect);

        return "browse";
    }

    /**
     * 显示订单内容
     */
    public String detail() {
    	memberCD = CookieUtils.getCookieValue(super.request, "agentCode");
        if (null == memberCD || "".equals(memberCD)) {
            return forwardError("请登录！");
        }
    	//对orderid解密
        try {
    		cryptOrderId = new DesCryptUtil("hotel").decrypt(cryptOrderId);
    		orderId = Long.parseLong(cryptOrderId);
		} catch (java.lang.NumberFormatException e) {
			log.error("订单解密后格式转换错误！订单id="+cryptOrderId);
			return forwardError("查找此订单出错，请重试！");
		} catch (Exception e) {
			log.error("对订单解密错误！订单id="+cryptOrderId);
			return forwardError("查找此订单出错，请重试！");
		}
		order = getOrder(orderId);
		if(!memberCD.equals(order.getMemberCd())){
			return forwardError("找不到此订单！");
		}
    	
    	showCommissionFlag = CookieUtils.getCookieValue(request, SHOW_COMMISSION_FLAG);
    	order = getOrder(orderId);
    	String currency = order.getPaymentCurrency();
    	double rate = Double.parseDouble(CurrencyBean.rateMap.get(currency));
    	request.setAttribute("rate",rate);
    	/************添加排序，未入住在前，已入住在后,原来ororder.hbm.xml是按dayIndex,roomIndex排序。begin by luoguangming********/
    	List<OrOrderItem> itemlist = order.getOrderItems();
    	List<OrOrderItem> itemlist1 = new ArrayList<OrOrderItem>();//未入住的item
    	List<OrOrderItem> itemlist2 = new ArrayList<OrOrderItem>();//已入住的item
    	for(OrOrderItem item : itemlist){
    		item.setAgentReadComissionRate(StringUtil.Baoliu(item.getAgentReadComissionRate(),3));
    		if(item.getOrderState()==2){
    			itemlist1.add(item);
    		}else if(item.getOrderState()==1){
    			itemlist2.add(item);
    		}else if(item.getOrderState()==0){
    			itemlist1.add(item);//state=0，未审核，也列为未入住
    		}
    	}
    	itemlist = new ArrayList<OrOrderItem>();
    	itemlist.addAll(itemlist1);
    	itemlist.addAll(itemlist2);
    	request.setAttribute("itemlist", itemlist);
    	/************添加排序，未入住在前，已入住在后end********/
    	/*********************查找酒店确认号begin add by luoguangming******************/
    	List<OrOrderFax> faxlist = order.getFaxList();
    	String confirmNo=null;
    	for(OrOrderFax fax:faxlist){
    		if(fax.getType()==1 && StringUtil.isValidStr(fax.getConfirmNo())) confirmNo = fax.getConfirmNo();
    	}
    	if(confirmNo==null || "".equals(confirmNo) || "null".equals(confirmNo) || "NULL".equals(confirmNo)){
    		confirmNo = "无";
    	}
    	request.setAttribute("confirmNo", confirmNo);
    	/*********************查找酒店确认号end******************/
        Map param = super.getParams();
        request.setAttribute("orderStatus", param.get("orderStatus"));
        StringBuffer bookhintSpanValueObj = new StringBuffer();
        StringBuffer cancelModifyItemObj = new StringBuffer();
        // hotel2.6 填充"担保提示信息"和"取消修改订单提示信息" ，用于界面显示 add by chenjiajie 2009-05-25
        String mangoTelephone = (String) param.get("mangoTelephone");
        // 为区分HAGT和HWEB的服务电话，为空默认为40066 40066，否则用界面传入的值
        if (!StringUtil.isValidStr(mangoTelephone)) {
            mangoTelephone = "40066 40066";
        }
        hotelManageWeb.getReservationHintForWeb(order, bookhintSpanValueObj, cancelModifyItemObj,
            mangoTelephone);
        bookhintSpanValue = bookhintSpanValueObj.toString();
        cancelModifyItem = cancelModifyItemObj.toString();
        if(9==order.getChannel()){//ELONG担保提示和修改单独处理
        	bookhintSpanValue = "";
        	cancelModifyItem = "";
        	List<OrOrderExtInfo> lst = order.getOrOrderExtInfoList();
        	for(OrOrderExtInfo info:lst){
        		if(OrderExtInfoType.ELONG_ASSURE_TIP.equals(info.getType())){
        			bookhintSpanValue = info.getContext();
        		}
        		if(OrderExtInfoType.ELONG_ASSURE_MODIFY.equals(info.getType())){
        			cancelModifyItem = info.getContext();
        		}
        	}
        	//HERE EL酒店级别提示信息
        	HtlHotel hotel = hotelManage.findHotel(order.getHotelId());
        	request.setAttribute("hotelAlertMsg", hotel.getAlertMessage());
        }
        return "detail";
    }
    
    
    public String edit(){
    	memberCD = CookieUtils.getCookieValue(super.request, "agentCode");
        if (null == memberCD || "".equals(memberCD)) {
            return forwardError("请登录！");
        }
    	//对orderid解密
        try {
    		cryptOrderId = new DesCryptUtil("hotel").decrypt(cryptOrderId);
    		orderId = Long.parseLong(cryptOrderId);
		} catch (java.lang.NumberFormatException e) {
			log.error("订单解密后格式转换错误！订单id="+cryptOrderId);
			return forwardError("查找此订单出错，请重试！");
		} catch (Exception e) {
			log.error("对订单解密错误！订单id="+cryptOrderId);
			return forwardError("查找此订单出错，请重试！");
		}
		order = getOrder(orderId);
		if(!memberCD.equals(order.getMemberCd())){
			return forwardError("找不到此订单！");
		}
        Map param = super.getParams();
        request.setAttribute("orderStatus", param.get("orderStatus"));
        StringBuffer bookhintSpanValueObj = new StringBuffer();
        StringBuffer cancelModifyItemObj = new StringBuffer();
        // hotel2.6 填充"担保提示信息"和"取消修改订单提示信息" ，用于界面显示 add by chenjiajie 2009-05-25
        String mangoTelephone = (String) param.get("mangoTelephone");
        // 为区分HAGT和HWEB的服务电话，为空默认为40066 40066，否则用界面传入的值
        if (!StringUtil.isValidStr(mangoTelephone)) {
            mangoTelephone = "40066 40066";
        }
        hotelManageWeb.getReservationHintForWeb(order, bookhintSpanValueObj, cancelModifyItemObj,
            mangoTelephone);
        bookhintSpanValue = bookhintSpanValueObj.toString();
        cancelModifyItem = cancelModifyItemObj.toString();
        if(9==order.getChannel()){//ELONG担保提示和修改单独处理
        	bookhintSpanValue = "";
        	cancelModifyItem = "";
        	List<OrOrderExtInfo> lst = order.getOrOrderExtInfoList();
        	for(OrOrderExtInfo info:lst){
        		if(OrderExtInfoType.ELONG_ASSURE_TIP.equals(info.getType())){
        			bookhintSpanValue = info.getContext();
        		}
        		if(OrderExtInfoType.ELONG_ASSURE_MODIFY.equals(info.getType())){
        			cancelModifyItem = info.getContext();
        		}
        	}
        }
    	return "edit";
    }
    
    private String getReturnModifyString(String name ,String oldstr,String newStr){
    	String returnStr=null;
    	String colorstart="<font color='red'>";
    	String colorend="</font>";
    	if(name.equals("fellowName")){    	
    		if(null!=oldstr&&!"".equals(oldstr)){
    			returnStr= "入住人姓名由："+oldstr +" 更改为 "+colorstart+newStr+colorend;
    		}else{
    			returnStr="入住人姓名更改为 "+colorstart+newStr+colorend;
    		}
    	}
    	if(name.equals("fellowNationality")){
    		if(null!=oldstr&&!"".equals(oldstr)){
    			returnStr= "入住人类型由："+oldstr +" 更改为 "+colorstart+newStr+colorend;
    		}else{
    			returnStr= "入住人类型更改为 "+colorstart+newStr+colorend;
    		}
    	}
    	if(name.equals("fellowGender")){
    		if(null!=oldstr&&!"".equals(oldstr)){
    			returnStr= "入住人性别由："+oldstr +" 更改为 "+colorstart+newStr+colorend;
    		}else{
    			returnStr= "入住人性别变更改为 "+colorstart+newStr+colorend;
    		}
    	}  
    	if(name.equals("specialRequest")){
    		if(null!=oldstr&&!"".equals(oldstr)){
    			returnStr= "特殊要求由："+oldstr +" 更改为 "+colorstart+newStr+colorend;
    		}else{
    			returnStr= "特殊要求更改为 "+colorstart+newStr+colorend;
    		}
    	}
    	
    	if(name.equals("linkMan")){
    		if(null!=oldstr&&!"".equals(oldstr)){
    			returnStr= "联系人名称由："+oldstr +" 更改为 "+colorstart+newStr+colorend;
    		}else{
    			returnStr= "联系人名称更改为 "+colorstart+newStr+colorend;
    		}
    	}
    	if(name.equals("mobile")){
    		if(null!=oldstr&&!"".equals(oldstr)){
    			returnStr= "联系人手机号由："+oldstr +" 更改为 "+colorstart+newStr+colorend;
    		}else{
    			returnStr= "联系人手机号更改为 "+colorstart+newStr+colorend;
    		}
    	}
    	if(name.equals("customerFax")){
    		if(null!=oldstr&&!"".equals(oldstr)){
    			returnStr= "联系人传真由："+oldstr +" 更改为 "+colorstart+newStr+colorend;
    		}else{
    			returnStr= "联系人传真更改为 "+colorstart+newStr+colorend;
    		}
    	}
    	if(name.equals("telephone")){
    		if(null!=oldstr&&!"".equals(oldstr)){
    			returnStr= "联系人电话由："+oldstr +" 更改为 "+colorstart+newStr+colorend;
    		}else{
    			returnStr= "联系人电话更改为 "+colorstart+newStr+colorend;
    		}
    	}
    	if(name.equals("email")){
    		if(null!=oldstr&&!"".equals(oldstr)){
    			returnStr= "联系人email由："+oldstr +" 更改为 "+colorstart+newStr+colorend;
    		}else{
    			returnStr= "联系人email更改为 "+colorstart+newStr+colorend;
    		}
    	}
    	if(name.equals("payMethod")){    		
			oldstr=getValueStr(oldstr);	
			newStr=getValueStr(newStr);
			if(null!=oldstr&&!"".equals(oldstr)){
				returnStr= "付款方式由："+oldstr +" 更改为 "+colorstart+newStr+colorend;
			}else{
				returnStr= "付款方式更改为 "+colorstart+newStr+colorend;
			}
    	}
    	if(name.equals("confirmType")){
    		oldstr=getValueStr(oldstr);	
    		newStr=getValueStr(newStr); 
    		if(null!=oldstr&&!"".equals(oldstr)){
    			returnStr= "预订确认方式由："+oldstr +" 更改为 "+colorstart+newStr+colorend;
    		}else{
    			returnStr= "预订确认方式更改为 "+colorstart+newStr+colorend;
    		}
    	}    
    	return returnStr;
    }
    
    
    public String getValueStr(String str ){
    	String returnstr=null;
    	if(str.equals("pre_pay")){
    		returnstr="全额预付";
		}
		if(str.equals("pay")){
			returnstr="前台面付";
		}
		if(str.equals("4")){
			returnstr="电话确认";
		}
		if(str.equals("3")){
			returnstr="短信确认";
		}
		if(str.equals("2")){
			returnstr="电子邮件确认";
		}
		if(str.equals("1")){
			returnstr="传真确认";
		}	
		return returnstr;    	
    }
    
    protected B2bModifyOrderInfo getB2bModifyOrderInfo(OrOrder order,Map params){
    	List<B2bModifyOrderInfo> b2borderinfos=new ArrayList<B2bModifyOrderInfo>();
        B2bModifyOrderInfo b2bmodifyorderinfo=null;
        //判断保存操作的时候，订单信息是否有修改。
        boolean isModify = false;
        
        b2borderinfos= orderService.getB2bHagtOrder(order.getOrderCD());
        for(B2bModifyOrderInfo b2bmodifyorderinfotemp:b2borderinfos ){
        	if(b2bmodifyorderinfotemp.getOrderState()<2){//获得修改订单
        		b2bmodifyorderinfo=b2bmodifyorderinfotemp;
        	}
        }
        if(null==b2bmodifyorderinfo){
        	b2bmodifyorderinfo=new B2bModifyOrderInfo();
        }
    	  int fellowNum =0;         
          List<OrFellowInfo> fellowinfolist=order.getFellowList();
          fellowNum=fellowinfolist.size();
      	
      	int i=1;
      	String rfellowName=null;//入住人名称
      	String rfellowNationality=null;//内宾 1 ， 外宾 2 ，港澳台 3
      	String rfellowGender=null;// 男：M ，女：F
      	
       	String fellowName=null;//入住人名称
      	String fellowNationality=null;//内宾 1 ， 外宾 2 ，港澳台 3
      	String fellowGender=null;// 男：M ，女：F    	
      	String fellowinfoStr=null;
      	if(null!=orderId){
      		b2bmodifyorderinfo.setOrderCD(order.getOrderCD());
      	}
      	for(OrFellowInfo fellowinfo:fellowinfolist){
      		if(i==1){
      		rfellowName=((String)params.get("fellowName")).trim();
      		rfellowNationality=((String)params.get("fellowNationality")).trim();
      		if(null!=params.get("fellowGender"))
      		rfellowGender=((String)params.get("fellowGender")).trim();
      		}else{
      			rfellowName=((String)params.get("fellowName_"+i)).trim();
          		rfellowNationality=((String)params.get("fellowNationality_"+i)).trim();
          		if(null!=params.get("fellowGender_"+i))
          		rfellowGender=((String)params.get("fellowGender_"+i)).trim();
      		}
      		
      		fellowName=fellowinfo.getFellowName();
      		fellowNationality=fellowinfo.getFellowNationality();
      		fellowGender=fellowinfo.getFellowGender();
      		
      		//入住人名称变更
      		if(null!=fellowName&& !"".equals(fellowName)){
      			if(!fellowName.trim().equals(rfellowName)){
      				if(null==fellowinfoStr||"".equals(fellowinfoStr)){
      					fellowinfoStr=getReturnModifyString("fellowName",fellowName,rfellowName);
      				}else{
      					fellowinfoStr+=", "+getReturnModifyString("fellowName",fellowName,rfellowName);
      				}
      			}
      		}
      		//入住人类型变更（内宾，外宾，港奥台）
      		if(null!=fellowNationality && !"".equals(fellowNationality)){
  	    		if(!fellowNationality.trim().equals(rfellowNationality)){
  	    			if(null==fellowinfoStr||"".equals(fellowinfoStr)){
  	    				fellowinfoStr=getReturnModifyString("fellowNationality",fellowNationality,rfellowNationality);
      				}else{
      					fellowinfoStr+=", "+getReturnModifyString("fellowNationality",fellowNationality,rfellowNationality);
      				}	    			
  	    		}
      		}
      		//入住人性别变更
      		if(null!=fellowGender && !"".equals(fellowGender)){
      			if(!fellowGender.trim().equals(rfellowGender)){
      				if(null==fellowinfoStr||"".equals(fellowinfoStr)){
  	    				fellowinfoStr=getReturnModifyString("fellowGender",fellowGender,rfellowGender);
      				}else{
      					fellowinfoStr+=", "+getReturnModifyString("fellowGender",fellowGender,rfellowGender);
      				}	   
      			}
      		}
      		i++;
      	}
      	if(null!=fellowinfoStr && !"".equals(fellowinfoStr)){
      		isModify = true;
      		b2bmodifyorderinfo.setFellowInfo(fellowinfoStr);
      	}
  		
  		
  		//联系人名称变更 linkMan
  		//联系人手机号变更 mobile
  		//联系人传真变更 customerFax
  		//联系人电话变更telephone
  		//联系人email变更Email		
  		//付款方式变更payMethod
  		//预订确认方式变更(短信，电话，邮件...)confirmType
      	String specialRequesttemp=order.getSpecialRequest();
      	String linkMantemp=order.getLinkMan();
      	String mobiletemp=order.getMobile();
      	String customerFaxtemp=order.getCustomerFax();
      	String telephonetemp=order.getTelephone();
      	String emailtemp=order.getEmail();
      	String payMethodtemp=order.getPayMethod();
      	int confirmTypetemp=order.getConfirmType();
      	
      	//特殊要求变更 specialRequest
      	if(null!=specialRequest && !"".equals(specialRequest)){
      		if(!specialRequest.trim().equals(specialRequesttemp)){
      			isModify = true;
      			b2bmodifyorderinfo.setSpecialRequest(getReturnModifyString("specialRequest",specialRequesttemp,specialRequest));
      		}
      	}
      	//联系人名称变更 linkMan
      	if(null!=linkMan && !"".equals(linkMan)){
      		if(!linkMan.trim().equals(linkMantemp)){
      			isModify = true;
      			b2bmodifyorderinfo.setLinkMan(getReturnModifyString("linkMan",linkMantemp,linkMan));
      		}    		
      	}
      	//联系人手机号变更 mobile
      	if(null!=mobile && !"".equals(mobile)){
      		if(!mobile.trim().equals(mobiletemp)){
      			isModify = true;
      			b2bmodifyorderinfo.setMobile(getReturnModifyString("mobile",mobiletemp,mobile));
      		}
      	}
      	//联系人传真变更 customerFax    	
      	if(null!=customerFax && !"".equals(customerFax)){
      		if(!customerFax.trim().equals(customerFaxtemp)){
      			isModify = true;
      			b2bmodifyorderinfo.setCustomerFax(getReturnModifyString("customerFax",customerFaxtemp,customerFax));
      		}
      	}	
      	//联系人电话变更telephone
      	if(null!=telephone && !"".equals(telephone)){
      		if(!telephone.trim().equals(telephonetemp)){
      			isModify = true;
      			b2bmodifyorderinfo.setTelephone(getReturnModifyString("telephone",telephonetemp,telephone));
      		}
      	}	
      	//联系人email变更
      	if(null!=email && !"".equals(email)){
      		if(!email.trim().equals(emailtemp)){
      			isModify = true;
      			b2bmodifyorderinfo.setEmail(getReturnModifyString("email",emailtemp,email));
      		}
      	}    	
      	//付款方式变更payMethod
      	if(null!=payMethod && !"".equals(payMethod)){
      		if(!payMethod.trim().equals(payMethodtemp)){
      			isModify = true;
      			b2bmodifyorderinfo.setPayMethod(getReturnModifyString("payMethod",payMethodtemp,payMethod));
      		}
      	}
      	//预订确认方式变更(短信，电话，邮件...)confirmType
      	if(null!=confirmType && !"".equals(confirmType)){
      		if(!confirmType.trim().equals(""+confirmTypetemp)){
      			isModify = true;
      			b2bmodifyorderinfo.setConfirmType(getReturnModifyString("confirmType",""+confirmTypetemp,confirmType));
      		}
      	}
      	b2bmodifyorderinfo.setOrderState(1);
      	//如果为已出中台订单，要重新修改自动分配状态，让b2b订单参与自动分配
      	if(!order.getIsStayInMid() && isModify){
      		String operaterIdCook = CookieUtils.getCookieValue(request,"operaterId");
      		b2bService.confirmToMid(order.getID(), operaterIdCook,state);
      	}
      	return b2bmodifyorderinfo;
    }
    
    
    
    public String save(){
    	B2bModifyOrderInfo b2bmodifyorderinfo=new B2bModifyOrderInfo();
    	Map params = super.getParams();
    	String forword;   
        try {
        	order = getOrder(orderId);
        	state="modify";
        	b2bmodifyorderinfo=getB2bModifyOrderInfo(order,params);    	
        	b2bmodifyorderinfo.setCreateDate(new Date());
        	b2bService.updateOrder(b2bmodifyorderinfo);  
        	forword="savesuccess";
		} catch (Exception e) {
			log.error("保存b2b修改订单信息出错："+e);
			super.setErrorMessage("保存b2b修改订单信息出错");
			forword = "forwardToError";
		}
		return forword;
    }
    
    
    public String cancelb2border(){
    	memberCD = CookieUtils.getCookieValue(super.request, "agentCode");
        if (null == memberCD || "".equals(memberCD)) {
            return forwardError("请登录！");
        }
    	//对orderid解密
        try {
    		cryptOrderId = new DesCryptUtil("hotel").decrypt(cryptOrderId);
    		orderId = Long.parseLong(cryptOrderId);
		} catch (java.lang.NumberFormatException e) {
			log.error("订单解密后格式转换错误！订单id="+cryptOrderId);
			return forwardError("查找此订单出错，请重试！");
		} catch (Exception e) {
			log.error("对订单解密错误！订单id="+cryptOrderId);
			return forwardError("查找此订单出错，请重试！");
		}
		
    	String forword;
    	try {
    		order = getOrder(orderId);
    		if(!memberCD.equals(order.getMemberCd())){
    			return forwardError("找不到此订单！");
    		}
			B2bModifyOrderInfo b2bmodifyorderinfo = new B2bModifyOrderInfo();
			b2bmodifyorderinfo.setOrderCD(orderCD);
			b2bmodifyorderinfo.setOrderState(2);
			b2bmodifyorderinfo.setCreateDate(new Date());
			b2bService.updateOrder(b2bmodifyorderinfo);
			state="cancel";
			//如果为已出中台订单，要重新修改自动分配状态，让b2b订单参与自动分配
        	if(!order.getIsStayInMid()){
        		String operaterIdCook = CookieUtils.getCookieValue(request,"operaterId");
          		b2bService.confirmToMid(order.getID(), operaterIdCook,state);
          	}
			forword = "savesuccess";
		} catch (Exception e) {
			log.error("取消b2b订单信息出错："+e);
			super.setErrorMessage("取消b2b订单信息出错");
			forword = "forwardToError";
		}
		return forword;
    	
    }
    
    public String cancelb2borderForPrepay(){
    	memberCD = CookieUtils.getCookieValue(super.request, "agentCode");
        if (null == memberCD || "".equals(memberCD)) {
            return forwardError("请登录！");
        }
    	try {
    		cryptOrderId = new DesCryptUtil("hotel").decrypt(cryptOrderId);
    		orderId = Long.parseLong(cryptOrderId);
		} catch (java.lang.NumberFormatException e) {
			log.error("订单解密后格式转换错误！订单id="+cryptOrderId);
			return forwardError("查找此订单出错，请重试！");
		} catch (Exception e) {
			log.error("对订单解密错误！订单id="+cryptOrderId);
			return forwardError("查找此订单出错，请重试！");
		}
    	try {
    		order = getOrder(orderId);
    		if(!memberCD.equals(order.getMemberCd())){
    			return forwardError("找不到此订单！");
    		}
    		String operaterIdCook = CookieUtils.getCookieValue(request,"operaterId");
      		b2bService.cancelOrderForB2BMinPricePay(order.getID(), operaterIdCook);
		} catch (Exception e) {
			log.error("取消b2b订单信息出错："+e);
			super.setErrorMessage("取消b2b订单信息出错");
		}
		return null;
    }
    
    
    public String addBudgetNo(){
        order = orderService.getOrder(orderId);
        if(order == null){
            return forwardError("order对象为空！");
        }
        this.orderService.updateOrder(order);
        return "member";
    }

    public String getMostPriceLevel() {
        return mostPriceLevel;
    }

    public void setMostPriceLevel(String mostPriceLevel) {
        this.mostPriceLevel = mostPriceLevel;
    }

    public String getMostStar() {
        return mostStar;
    }

    public void setMostStar(String mostStar) {
        this.mostStar = mostStar;
    }

    public String getMemberCD() {
        return memberCD;
    }

    public void setMemberCD(String memberCD) {
        this.memberCD = memberCD;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
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

    public HotelManageWeb getHotelManageWeb() {
        return hotelManageWeb;
    }

    public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
        this.hotelManageWeb = hotelManageWeb;
    }

    public String getBudgetNo() {
        return budgetNo;
    }

    public void setBudgetNo(String budgetNo) {
        this.budgetNo = budgetNo;
    }

    public Map<String, String> getReasonMap() {
        return reasonMap;
    }

    public void setReasonMap(Map<String, String> reasonMap) {
        this.reasonMap = reasonMap;
    }

	public String getSpecialRequest() {
		return specialRequest;
	}

	public void setSpecialRequest(String specialRequest) {
		this.specialRequest = specialRequest;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCustomerFax() {
		return customerFax;
	}

	public void setCustomerFax(String customerFax) {
		this.customerFax = customerFax;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getConfirmType() {
		return confirmType;
	}

	public void setConfirmType(String confirmType) {
		this.confirmType = confirmType;
	}

	public IB2bService getB2bService() {
		return b2bService;
	}

	public void setB2bService(IB2bService service) {
		b2bService = service;
	}

	public String getOrderCD() {
		return orderCD;
	}

	public void setOrderCD(String orderCD) {
		this.orderCD = orderCD;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getOperaterId() {
		return operaterId;
	}

	public void setOperaterId(String operaterId) {
		this.operaterId = operaterId;
	}
    
}

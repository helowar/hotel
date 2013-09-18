package com.mangocity.webnew.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mangocity.hdl.constant.ChannelType;
import javax.servlet.http.HttpSession;
import com.mangocity.hotel.base.persistence.OrReservation;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.ext.member.util.MemberUtil;
import com.mangocity.hotel.order.persistence.OrPayment;
import com.mangocity.hotel.order.persistence.OrRefund;
import com.mangocity.hotel.order.persistence.view.OrPaymentVO;
import com.mangocity.hotel.order.persistence.view.OrRefundVO;
import com.mangocity.hotel.order.web.OrderAction;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.model.mbrship.Mbrship;
import com.mangocity.util.StringUtil;
import com.mangocity.util.config.ConfigUtil;
import com.mangocity.hotel.order.constant.OrderExtInfoType;
import com.mangocity.hotel.order.persistence.OrOrderExtInfo;

/**
 * 会员浏览订单
 * 
 * @author neil
 * 
 */
public class OrderBrowseAction extends OrderAction {

    private static final long serialVersionUID = 8729347252487743131L;

    private static final String BILLDETAIL_URL = 
        ConfigUtil.getResourceByKey("hotelii_i_delivery.billdetail_url");

    private static final String RESDETAIL_URL = 
        ConfigUtil.getResourceByKey("hotelii_i_delivery.resdetail_url");

//    static String PREDETAIL_URL = ConfigUtil.getResourceByKey("hotelii_i_preauth.detail_url");

    private String mostStar;

    private String mostPriceLevel;

    private String memberId;

    private String memberCD;
    
    private String budgetNo;
    
    private String memberCdStr;

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
    
    private String currencyStr;
    Map<String,String> reasonMap = new HashMap<String,String>();

    public String member() {
    	String mbrId = getMbrIdForWeb();
        String mbrSign = getMbrSignForWeb();
        boolean isAvalibleMember = false;
		if (StringUtil.isValidStr(memberCD)) {
			MemberDTO memerDTO = getMemberSimpleInfoByMemberCd(memberCD, false);
			if (null != memerDTO) {
					if (StringUtil.isValidStr(mbrId) && StringUtil.isValidStr(mbrSign) && String.valueOf(memerDTO.getId()).equals(mbrId)) {
						isAvalibleMember = MemberUtil.isAvalibleMemberByMbrId(mbrId,mbrSign);// 是否有效的会员
				}
			}
		}
		if(isAvalibleMember){
        try {
            MemberDTO member = memberInterfaceService.getMemberByCode(memberCD);
            if(member!=null){
            	memberId = String.valueOf(member.getId());
            	
            	 Long mbrID =  this.getMemberBaseInfoDelegate().queryMbrIdByOldMbrshipCd(memberCD);
                 List<Mbrship> mbrShipList = this.getMemberBaseInfoDelegate().mbrshipListByMbrId(mbrID);  
                 if(null != mbrShipList && mbrShipList.size()>0){
              	   memberCdStr ="'"+ mbrShipList.get(0).getOldMbrshipCd()+"'";
              	   for(int i = 1;i<mbrShipList.size();i++){
              		  memberCdStr = memberCdStr+",'" +mbrShipList.get(i).getOldMbrshipCd()+"'";
              	   }
                 }
            }
             log.info("OrderBrowseAction member method success,memberId=" + memberId);
        } catch (Exception me) {
            log.error("OrderBrowseAction member method exception, get the member error, the cause is:" ,me);
        }
        HttpSession session = request.getSession();
        session.setAttribute("memberCD", memberCD);
        session.setAttribute("memberCdStr", memberCdStr);
        return "member";
		}else{
			return forwardMsgBox("请登陆",null);
		}
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
        // 修改生产bug 判断会员是否登陆 add by haibo.li
        String memberCd = getMemberCdForWeb();

        if (null == memberCd || "".equals(memberCd)) {
            return forwardError("请重新登陆");
        } else {
        	member = getMemberSimpleInfoByMemberCd(memberCd, true);
            if (null == member) {
   
                return forwardError("请登陆");
            } else {
                order = getOrder(orderId);
                if (null == order) {
                    return forwardError("order对象为空！");
                }
               
            }
        }

        Map param = super.getParams();
        request.setAttribute("orderStatus", param.get("orderStatus"));
        //add by zengming 2012-05-02增加提示类型，用于判断是何种订单取消
        request.setAttribute("hintType",param.get("hintType"));
        StringBuffer bookhintSpanValueObj = new StringBuffer();
        StringBuffer cancelModifyItemObj = new StringBuffer();
        
        // hotel2.6 填充"担保提示信息"和"取消修改订单提示信息" ，用于界面显示 add by chenjiajie 2009-05-25
        String mangoTelephone = (String) param.get("mangoTelephone");
        // 为区分HAGT和HWEB的服务电话，为空默认为40066 40066，否则用界面传入的值
        // 如果是交行全卡商旅等渠道的订单，则要改为相应的热线号码 modify by chenkeming
        if(!order.isCooperateOrder()) {
            if (!StringUtil.isValidStr(mangoTelephone)) {
                mangoTelephone = "40066 40066";
            }	
        } else { // 交行全卡商旅等渠道的订单
        	mangoTelephone = "400-678-5167";
        }
        if(order.getChannel()==ChannelType.CHANNEL_ELONG){
        	List<OrOrderExtInfo> lst = order.getOrOrderExtInfoList();
        	for(OrOrderExtInfo info : lst){
        		if(OrderExtInfoType.ELONG_ASSURE_TIP.equals(info.getType().trim())){
        			bookhintSpanValue = info.getContext();
        		}
        		if(OrderExtInfoType.ELONG_ASSURE_MODIFY.equals(info.getType().trim())){
        			cancelModifyItem = info.getContext();
        		}
        	}
        }else{
        	hotelManageWeb.getReservationHintForWeb(order, bookhintSpanValueObj, cancelModifyItemObj,
                    mangoTelephone);
                bookhintSpanValue = bookhintSpanValueObj.toString();
                cancelModifyItem = cancelModifyItemObj.toString();
        }
       //币种转换 add by zhouna
        Map<String,String> currencyMap = new HashMap<String, String>();
    	currencyMap.put("RMB", "&yen;");
    	currencyMap.put("HKD", "HK$");
    	currencyMap.put("MOP", "MOP");
    	if("pay".equals(order.getPayMethod())){
    	currencyStr = currencyMap.get(order.getPaymentCurrency());}
    	else{
    	currencyStr =  currencyMap.get(order.getActualPayCurrency());}
        return "detail";
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

	public String getCurrencyStr() {
		return currencyStr;
	}

	public void setCurrencyStr(String currencyStr) {
		this.currencyStr = currencyStr;
	}

	public String getMemberCdStr() {
		return memberCdStr;
	}

	public void setMemberCdStr(String memberCdStr) {
		this.memberCdStr = memberCdStr;
	}
    
    
}

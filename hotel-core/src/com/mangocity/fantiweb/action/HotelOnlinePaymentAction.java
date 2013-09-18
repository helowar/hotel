package com.mangocity.fantiweb.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.OrReservation;
import com.mangocity.hotel.order.constant.PrepayType;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrPayment;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.util.ConfigParaBean;
import com.mangocity.util.DateUtil;
import com.mangocity.webnew.persistence.HotelOrderFromBean;
import com.mangocity.webnew.service.HotelBookingService;
import com.mangocity.webnew.util.HotelPayOnlieUtil;


/**
 * 在线支付action
 * @author zuoshengwei
 *
 */
public class HotelOnlinePaymentAction extends GenericWebAction {
    
    // 汇率
    private double rate;

    // 会员url
    private String url;
    
    //本类是系统启动时读取了jdbc.properties中系统值的类
    private ConfigParaBean configParaBean;
    
    private HotelManageWeb hotelManageWeb;
    
    //下单的service类 
    private HotelBookingService   hotelBookingService;
    
    /**
     * 用于保存香港中科获取的新价格，以便后续生成订单明细 ADD BY WUYUN 2009-04-21
     */
    private String hkPrices;

    /**
     * 用于保存香港中科获取的底价，以便后续生成订单明细
     */
    private String hkBasePrices;

    /**
     * 订单编码
     */
    private String orderCD;
    
    /**
     * 是否成功占用中旅配额
     */
    private int resultHK = 0;
    
    /**
     * 在线支付传递的参数 add by shengwei.zuo 2009-11-21    begin
     */
    
	private String executeURL;
    
	private String memberNum;

	//订单编号
	private String orderCd;//页面传过来
	
	//外部交易号
	private String outTradeNo;
	
	//客户ID
	private String customerId;
	
	//收款单位
	private String gatheringUnitCode;
	
	//币种
	private String currencyType;
	
	//产品类型
	private String productType;
	
	//请求时间
	private String requestDate;
	
	//支付方式
	private String payMode;
	
	//支付金额
	private String amount;//页面传过来
	
	//返回url
	private String returnURL;
	
	//备注
	private String remark;
	
	//加密方式
	private String signType;
	
	//密文
	private String sign;
    
    /**
     * 在线支付传递的参数 add by shengwei.zuo 2009-11-21    end
     */

	public String execute() {
		
		//客户ID
		customerId = HotelPayOnlieUtil.CUSTOMER_ID;
		gatheringUnitCode = HotelPayOnlieUtil.GATHERING_UNITCODE;
		currencyType = HotelPayOnlieUtil.CURRENCY_HKD;
		executeURL = HotelPayOnlieUtil.ONLINE_REQUEST_URL;
		productType = HotelPayOnlieUtil.PRODUCT_TYPE_HOTEL;
		signType = HotelPayOnlieUtil.SIGN_TYPE;
		requestDate = DateUtil.formatDateToYMDHMS1(DateUtil.getSystemDate());
		remark = "0";
		returnURL = HotelPayOnlieUtil.getRequestUrl(this.getRequest().getRequestURL().toString())+"/mpmFromPay.shtml";
		
		OrOrder order= null;
		
		//流水号 从 10 开始 
		String  seriNo = "10";
		Long paymenId = 0L;
		
		Map params = super.getParams();
		String orderID  = (String)params.get("orderID");
		order = orderService.getOrder(Long.valueOf(orderID));
		
		orderCd = order.getOrderCD();	
		
		Map<Integer,String> payMap = PrepayType.payMap;
        payMode = payMap.get(order.getPrepayType());
		
		List<OrPayment> paymentLst  = order.getPaymentList();
		if(paymentLst!=null&&!paymentLst.isEmpty()){
			for(int i = 0;i<paymentLst.size();i++){
				OrPayment paymentEntiy =  paymentLst.get(i);
				if(paymentEntiy.isWebWay()){
					String payMoney = String.valueOf(paymentEntiy.getMoney());
					amount = String.valueOf(payMoney == null ? 0:payMoney);
					//流水号
		            seriNo = paymentEntiy.getPrepayBillNo();
		            if(seriNo!=null&&!"".equals(seriNo)&&seriNo.length()==2){
		            	seriNo=String.valueOf((Integer.parseInt(seriNo)+1));   				
		            }else{
		            	seriNo="10";
		            }
		            paymenId = paymentEntiy.getID();
		           break;
				}
			}
		}
		
		//外部交易号
		outTradeNo = HotelPayOnlieUtil.CUSTOMER_ID + orderCd + seriNo;
		
		sign = HotelPayOnlieUtil.buildSign(entSignMap(), HotelPayOnlieUtil.PSD_KEY);
		
		//更新流水号到 OrPayment 中
		hotelBookingService.updatePaymentForBig5(Long.valueOf(orderID),seriNo,paymenId);
        
        return "mpmGateway";
        
	}
    
    //封装传递的参数  add by  shengwei.zuo 2009-11-21
	public Map<String, String> entSignMap() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("orderCd", orderCd);
		params.put("outTradeNo", outTradeNo);
		params.put("customerId", customerId);
		params.put("gatheringUnitCode", gatheringUnitCode);
		params.put("currencyType", currencyType);
		params.put("requestDate", requestDate);
		params.put("productType", productType);
		params.put("payMode", payMode);
		params.put("amount", amount);
		params.put("returnURL", returnURL);
		params.put("remark", remark);
		//params.put("memberCD", memberNum);
		return params;
	}

	public HotelOrderFromBean getHotelOrderFromBean() {
		return hotelOrderFromBean;
	}

	public void setHotelOrderFromBean(HotelOrderFromBean hotelOrderFromBean) {
		this.hotelOrderFromBean = hotelOrderFromBean;
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

	public HotelManageWeb getHotelManageWeb() {
		return hotelManageWeb;
	}


	public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
		this.hotelManageWeb = hotelManageWeb;
	}

	public HotelBookingService getHotelBookingService() {
		return hotelBookingService;
	}

	public void setHotelBookingService(HotelBookingService hotelBookingService) {
		this.hotelBookingService = hotelBookingService;
	}


	public List getPriceTemplist() {
		return priceTemplist;
	}




	public void setPriceTemplist(List priceTemplist) {
		this.priceTemplist = priceTemplist;
	}

	public OrReservation getReservation() {
		return reservation;
	}


	public void setReservation(OrReservation reservation) {
		this.reservation = reservation;
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

	public String getOrderCD() {
		return orderCD;
	}

	public void setOrderCD(String orderCD) {
		this.orderCD = orderCD;
	}

	public int getResultHK() {
		return resultHK;
	}

	public void setResultHK(int resultHK) {
		this.resultHK = resultHK;
	}



	public String getExecuteURL() {
		return executeURL;
	}



	public void setExecuteURL(String executeURL) {
		this.executeURL = executeURL;
	}



	public String getMemberNum() {
		return memberNum;
	}



	public void setMemberNum(String memberNum) {
		this.memberNum = memberNum;
	}



	public String getOrderCd() {
		return orderCd;
	}



	public void setOrderCd(String orderCd) {
		this.orderCd = orderCd;
	}



	public String getOutTradeNo() {
		return outTradeNo;
	}



	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}



	public String getCustomerId() {
		return customerId;
	}



	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}



	public String getGatheringUnitCode() {
		return gatheringUnitCode;
	}



	public void setGatheringUnitCode(String gatheringUnitCode) {
		this.gatheringUnitCode = gatheringUnitCode;
	}



	public String getCurrencyType() {
		return currencyType;
	}



	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}



	public String getProductType() {
		return productType;
	}



	public void setProductType(String productType) {
		this.productType = productType;
	}



	public String getRequestDate() {
		return requestDate;
	}



	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}



	public String getPayMode() {
		return payMode;
	}



	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}



	public String getAmount() {
		return amount;
	}



	public void setAmount(String amount) {
		this.amount = amount;
	}



	public String getReturnURL() {
		return returnURL;
	}



	public void setReturnURL(String returnURL) {
		this.returnURL = returnURL;
	}



	public String getRemark() {
		return remark;
	}



	public void setRemark(String remark) {
		this.remark = remark;
	}



	public String getSignType() {
		return signType;
	}



	public void setSignType(String signType) {
		this.signType = signType;
	}



	public String getSign() {
		return sign;
	}



	public void setSign(String sign) {
		this.sign = sign;
	}
}

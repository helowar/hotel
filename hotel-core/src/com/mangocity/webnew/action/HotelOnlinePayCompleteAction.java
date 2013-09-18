package com.mangocity.webnew.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.mangocity.hdl.constant.ChannelType;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.orderrecord.model.OrderRecord;
import com.mangocity.hotel.orderrecord.service.AbstractOrderRecord;
import com.mangocity.hweb.persistence.QueryHotelForWebSaleItems;
import com.mangocity.lang.StringUtils;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.util.web.CookieUtils;
import com.mangocity.webnew.persistence.HotelOrderFromBean;
import com.mangocity.webnew.service.HotelBookingService;
import com.mangocity.webnew.service.IHotelOrderCompleteService;
import com.mangocity.webnew.util.action.GenericWebAction;

/**
 * 在线支付状态查询action 
 * add by shengwei.zuo 2009-11-23
 * 
 */
public class HotelOnlinePayCompleteAction extends GenericWebAction {

    /**
     * 订单ID
     */
    private String hidOrderId;
    
    /**
     * 订单编码
     */
    private String orderCD;
     
    
    //最终的支付状态
    private String payLastFlag;
    private String hotelAddress;
    private String weekOfOutDate;
    private String weekOfInDate;
    
    
    //注入
    private IHotelOrderCompleteService hotelOrderCompleteService; // add by diandian.hou 2011-11-3 代金券
    private HotelBookingService hotelBookingService;
    public String execute(){
    	          
    	Map params = super.getParams();
		
		if (null == hotelOrderFromBean) {
			hotelOrderFromBean = new HotelOrderFromBean();
	    }
		//获取页面上的表单元素的属性值
		MyBeanUtil.copyProperties(hotelOrderFromBean, params);
		
		try {
	            // 获取会员信息
	            member = getMemberInfoForWeb(true);
	        } catch (Exception e) {
	            log.error("getMemberInfoForWeb is Error" + e);
	    }
		
		order = new OrOrder();
    	order = orderService.getOrder(Long.parseLong(hidOrderId));
    	
    	orderCD = order.getOrderCD();
    	
    	saveOrderRecord();
		
    	// 添加代金券支付 add by diandian.hou 2011-11-3
//    	if(payLastFlag!=null && payLastFlag.contains("onlinePaySuccess") && member!=null){
//    		if (hotelOrderFromBean.isUsedCoupon()) {
//    			try{
//    			    hotelOrderCompleteService.deductUsedCoupon(params, order, member); //锁定代价券
//    			    voucherInterfaceService.confirmVoucherState(order, roleUser, member);//确认并扣除代金券
//    			}catch(Exception e){
//    				log.error("HotelOnlinePayCompleteAction 代金券锁定失败" +e);
//    			}
//    		}
//    	}
    	
    	
    	return "onlinePaySuccess"; 
       
    }

    private void saveOrderRecord(){
    	try {
			HttpSession httpSession = request.getSession(true);
			final OrderRecord orderRecord;			
			OrderRecord orderRecordFromSession = (OrderRecord) httpSession.getAttribute("orderRecord");
								
			if(orderRecordFromSession==null){
				String actionId=CookieUtils.getCookieValue(request, "actionId");
				orderRecord = new OrderRecord();				
				if(!StringUtils.isEmpty(actionId)){					
					orderRecord.setActionId(Long.parseLong(actionId));
				}		
			}
			else{
				orderRecord=orderRecordFromSession;
			}
			String apacheSessionId = getApacheSessionId();
			if(null != apacheSessionId && !"".equals(apacheSessionId)){
				orderRecord.setApacheSessionId(apacheSessionId);//添加sessionId
			}
			AbstractOrderRecord orderRecoreService = new AbstractOrderRecord(orderRecord,hotelOrderFromBean) {
				public void init(){};
				public void combineOrderRecord() {
					try {
						double salePrice=0;
						List<QueryHotelForWebSaleItems> queryPriceList = hotelBookingService.refreshHotelReservateExResponse(
								hotelOrderFromBean.getHotelId(),hotelOrderFromBean.getRoomTypeId(),
								hotelOrderFromBean.getChildRoomTypeId(), hotelOrderFromBean.getRoomChannel(),
								hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate());
						int days = DateUtil.getDay(hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate());
						if (queryPriceList != null &&queryPriceList.size() >= days) {

							for (QueryHotelForWebSaleItems priceItem : queryPriceList) {
								salePrice += priceItem.getSalePrice();
							}

						}
						double rateToRMB = CurrencyBean.getRateToRMB(hotelOrderFromBean.getCurrency());
						double priceRMB = salePrice*rateToRMB;
						int roomQuantity=(hotelOrderFromBean.getRoomQuantity()==null ? 0 : Integer.parseInt(hotelOrderFromBean.getRoomQuantity()));
						orderRecord.setOneRoomPrice(salePrice);
						orderRecord.setOrderPriceSumPay(priceRMB *roomQuantity);
						orderRecord.setOrderPriceSumConctract(salePrice*roomQuantity);
						orderRecord.setCurrentStep(11);
					} catch (Exception e) {
						
						log.error("HotelOnlinePayCompleteAction refreshHotelReservateExResponse has o wrong:" + e);
					}
				}
			};
			WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(ServletActionContext.getServletContext());
			orderRecoreService.createOrderRecordTemplete(context);
			httpSession.removeAttribute("orderRecord");
			CookieUtils.setCookie(request, getHttpResponse(), "actionId", null, 0, null,null);

		} catch (Exception e) {
			log.error("HotelOnlinePayCompleteAction saveOrderRecord() has o wrong:" + e);
		}
    }
    
    
	public String getHidOrderId() {
		return hidOrderId;
	}

	public void setHidOrderId(String hidOrderId) {
		this.hidOrderId = hidOrderId;
	}



	public String getPayLastFlag() {
		return payLastFlag;
	}



	public void setPayLastFlag(String payLastFlag) {
		this.payLastFlag = payLastFlag;
	}



	public String getOrderCD() {
		return orderCD;
	}



	public void setOrderCD(String orderCD) {
		this.orderCD = orderCD;
	}



	public String getHotelAddress() {
		return hotelAddress;
	}

	public void setHotelAddress(String hotelAddress) {
		this.hotelAddress = hotelAddress;
	}

	
	public String getWeekOfOutDate() {
		return weekOfOutDate;
	}

	public void setWeekOfOutDate(String weekOfOutDate) {
		this.weekOfOutDate = weekOfOutDate;
	}

	public String getWeekOfInDate() {
		return weekOfInDate;
	}

	public void setWeekOfInDate(String weekOfInDate) {
		this.weekOfInDate = weekOfInDate;
	}

	public void setHotelOrderCompleteService(
			IHotelOrderCompleteService hotelOrderCompleteService) {
		this.hotelOrderCompleteService = hotelOrderCompleteService;
	}

	public void setHotelBookingService(HotelBookingService hotelBookingService) {
		this.hotelBookingService = hotelBookingService;
	}

	
}

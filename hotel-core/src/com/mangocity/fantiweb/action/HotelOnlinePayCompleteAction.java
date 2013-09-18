package com.mangocity.fantiweb.action;

import java.util.Map;
import org.apache.log4j.Logger;

import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.webnew.persistence.HotelOrderFromBean;

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
		
    	return "onlinePaySuccess"; 
       
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

}

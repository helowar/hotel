package com.mangocity.tmchotel.service;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.order.persistence.OrMemberConfirm;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderItem;
import com.mangocity.hotel.order.persistence.OrPayment;

/**
 * 网站优化- 网站订单的即时确认 
 * 
 * add by shengwei.zuo 2010-1-30
 * 
 */
public interface OrderImmedConfirmService  {
	
	//给用户发送即时确认信息-短信或者Email add by shengwei.zuo 2010-1-30
	public String  sendImmedConfirmToCus(OrOrder order);
	
	//给酒店发送即时确认-传真  add by shengwei.zuo 2010-1-30
	public String  sendImmedConfirmToHotel(OrOrder order);
	
    //是否满足配额  add by shengwei.zuo 2010-2-1
    public boolean  quotaOk(OrOrder order);
    
    //是否支付成功 add by shengwei.zuo 2010-2-1
    public boolean paySuccessed(OrOrder order);
    
    
    //诺曼底商旅系统发送传真
    public void sendTmcHotel(OrOrder order,String faxNum,String tohotelNotes,String faxType);
	
}

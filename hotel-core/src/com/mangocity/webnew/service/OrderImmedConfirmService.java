package com.mangocity.webnew.service;

import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.persistence.OrOrder;

/**
 * 网站优化- 网站订单的即时确认 
 * 
 * add by shengwei.zuo 2010-1-30
 * 
 */
public interface OrderImmedConfirmService  {
	
	//给用户发送即时确认信息-短信或者Email add by shengwei.zuo 2010-1-30
	// 添加一个 boolean参数 edit by diandian.hou
	/**
	 * 现只有如家的二次确定flagSencondConfirm为true（不发短信），默认为false
	 * @param order
	 * @param flagSencondConfirm
	 * @return
	 */
	public String  sendImmedConfirmToCus(OrOrder order,boolean flagSencondConfirm, MemberDTO memberDTO);
	
	//给酒店发送即时确认-传真  add by shengwei.zuo 2010-1-30
	public String  sendImmedConfirmToHotel(OrOrder order, MemberDTO memberDTO);
	
    //是否满足配额  add by shengwei.zuo 2010-2-1
    public boolean  quotaOk(OrOrder order);
    
}

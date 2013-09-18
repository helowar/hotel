package com.mangocity.hotel.orderbuildservice.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.constant.OrderSource;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.hotel.order.constant.OrderType;
import com.mangocity.hotel.order.persistence.OrFellowInfo;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.assistant.OrderAssist;
import com.mangocity.proxy.member.service.MemberInterfaceService;
import com.mangocity.webnew.persistence.HotelOrderFromBean;

public class ShadowOrderBuild extends AbstractOrderBuild{

	public void combineOrderBaseInfo(OrOrder order,HotelOrderFromBean hotelOrderFromBean,Map<Key_orderBaseMap, Object> orderBaseInfoMap) {
		super.combineOrderBaseInfo(order, hotelOrderFromBean);
		//order.setChildRoomTypeId(null);
		order.setQuotaOk(true);
		order.setSource(OrderSource.FROM_SHADOW);//订单来源
		order.setType(OrderType.TYPE_B2BAGENT);//订单类型
		order.setOrderState(OrderState.SUBMIT_TO_MID);
		order.setIsStayInMid(true);
		order.setQuotaCanReturn(false);
		order.setQuotaTypeOld("1"); //测试说应为1
		order.setRmpOrder(true);
		order.setToMidTime(new Date());
	}

	public void combineOrderClauseInfo(OrOrder order,HotelOrderFromBean hotelOrderFromBean,Map<Key_orderClauseMap, Object> orderClauseMap) {
	}

	public void combineOrderEBInfo(OrOrder order,HotelOrderFromBean hotelOrderFromBean,Map<Key_orderEBMap, Object> orderEBMap) {
	}

	public void combineOrderMemberInfo(OrOrder order,HotelOrderFromBean hotelOrderFromBean,Map<Key_orderMemberMap, Object> orderMemberMap) {
		super.combineOrderMemberInfo(order, hotelOrderFromBean);
		setOrderMemberInfo(order,orderMemberMap);
	}

	public void combineOrderMoneyInfo(OrOrder order,HotelOrderFromBean hotelOrderFromBean,Map<Key_orderMoneyMap, Object> orderMoneyMap) {
	    super.combineOrderMoneyInfo(order, hotelOrderFromBean);	
	}

	
	private void setOrderMemberInfo(OrOrder order,Map<Key_orderMemberMap,Object> orderMemberMap){
		 MemberDTO member = (MemberDTO)orderMemberMap.get(Key_orderMemberMap.Member);
		 if(member==null){
			 order.setMemberId(MemberInterfaceService.COMMONMEMBERID);
			 order.setMemberCd(MemberInterfaceService.COMMONMEMBERCD);
			 //order.setMemberVipLevel(EmergencyLevel.VIP1);
		 }else{
		     order.setMemberCd(member.getMembercd());
			 order.setMemberId(member.getId());
			 OrderAssist orderAssit = new OrderAssist(); //orderAssist 注入?
			 order.setEmergencyLevel(orderAssit.getEmergency(order, new Date(), member, new HashMap()));
			 order.setMemberAliasId(member.getAliasid());
			 order.setMemberName(member.getName());
			 order.setMemberState(member.getState());
		 }
		 order.setFellowList((List)orderMemberMap.get(Key_orderMemberMap.FollewList));
	     setMemberInfoFellowNames(order);//n个入住人
	}
	
	
	private void setMemberInfoFellowNames(OrOrder order){
		if(null == order) return;
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
	}
}

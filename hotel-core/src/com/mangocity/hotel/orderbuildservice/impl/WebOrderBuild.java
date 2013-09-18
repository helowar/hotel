package com.mangocity.hotel.orderbuildservice.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.OrReservation;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.constant.EmergencyLevel;
import com.mangocity.hotel.order.constant.OrderSource;
import com.mangocity.hotel.order.constant.OrderType;
import com.mangocity.hotel.order.persistence.OrFellowInfo;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.assistant.OrderAssist;
import com.mangocity.proxy.member.service.MemberInterfaceService;
import com.mangocity.webnew.persistence.HotelOrderFromBean;

public class WebOrderBuild extends AbstractOrderBuild {

	//订单基本信息
	public void combineOrderBaseInfo(OrOrder order, HotelOrderFromBean hotelOrderFromBean, Map<Key_orderBaseMap,Object> orderBaseInfoMap) {
		super.combineOrderBaseInfo(order, hotelOrderFromBean);
		setOrderBaseInfoBySource(order, hotelOrderFromBean);
		order.setPriceList((List)orderBaseInfoMap.get(Key_orderBaseMap.PriceList));
		order.setOrOrderExtInfoList((List)orderBaseInfoMap.get(Key_orderBaseMap.OrOrderExtInfoList));
		order.setSupplierAlias((String)orderBaseInfoMap.get(Key_orderBaseMap.SupplierAlias));
		order.setSupplierID((Long)orderBaseInfoMap.get(Key_orderBaseMap.SupplierID));
	}
	
	//订单条款信息
	public void combineOrderClauseInfo(OrOrder order, HotelOrderFromBean hotelOrderFromBean, Map<Key_orderClauseMap,Object> orderClauseMap) {
		super.combineOrderClauseInfo(order, hotelOrderFromBean);		
		order.setReservation((OrReservation)orderClauseMap.get(Key_orderClauseMap.Reservation));	
	}
    
    //电商相关的信息,可以删除的（因报表组，现不删）
	public void combineOrderEBInfo(OrOrder order, HotelOrderFromBean hotelOrderFromBean, Map<Key_orderEBMap,Object> orderEBMap) {
		order.setAgentid((String)orderEBMap.get(Key_orderEBMap.ProjectCode));
	}
    
	public void combineOrderMemberInfo(OrOrder order, HotelOrderFromBean hotelOrderFromBean, Map<Key_orderMemberMap,Object> orderMemberMap) {
		super.combineOrderMemberInfo(order, hotelOrderFromBean);
		setOrderMemberInfo(order,orderMemberMap);
		
		
	}

	public void combineOrderMoneyInfo(OrOrder order, HotelOrderFromBean hotelOrderFromBean, Map<Key_orderMoneyMap,Object> orderMoneyMap) {
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
	
	private void setOrderBaseInfoBySource(OrOrder order,HotelOrderFromBean hotelOrderFromBean){
		if(OrderSource.FROM_EMAP.equals(hotelOrderFromBean.getSource())){
			order.setSource(OrderSource.FROM_WEB);
			order.setCreator("hotelEmap");//创建人
			order.setCreatorName("hotelEmap");//创建人名称
		}else{
			order.setSource(OrderSource.FROM_WEB);//网站订单来源
			order.setCreator("hotelII");//创建人
			order.setCreatorName("hotelII");//创建人名称
		}
		order.setType(OrderType.TYPE_MANGO);//订单类型
	}
//	public static void main(String[] args){
//		Map<String,Object> a = new HashMap<String,Object>();
//		a.put("12", null);
//		MemberWrapper member = (MemberWrapper)a.get("12");
//		System.out.println(member);
//	}

	
}

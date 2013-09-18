package com.mangocity.hotel.orderbuildservice.impl;

import java.util.Date;

import com.mangocity.hotel.order.constant.ConfirmType;
import com.mangocity.hotel.order.constant.EmergencyLevel;
import com.mangocity.hotel.order.constant.OrderType;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.orderbuildservice.OrderBuild;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.webnew.persistence.HotelOrderFromBean;


public abstract class AbstractOrderBuild implements OrderBuild {
	
	//组装基本信息
	public void combineOrderBaseInfo(OrOrder order,HotelOrderFromBean hotelOrderFromBean){
		order.setCity(hotelOrderFromBean.getCityCode());//城市
		order.setHotelId(hotelOrderFromBean.getHotelId());//酒店ID
		order.setHotelName(hotelOrderFromBean.getHotelName());//酒店名称
		order.setHotelStar(hotelOrderFromBean.getHotelStar());//酒店星级
		order.setRoomTypeId(hotelOrderFromBean.getRoomTypeId());//房型ID
		order.setRoomTypeName(hotelOrderFromBean.getRoomTypeName());//房型名称
		order.setChildRoomTypeId(hotelOrderFromBean.getChildRoomTypeId());//价格类型ID
		order.setChildRoomTypeName(hotelOrderFromBean.getChildRoomTypeName());//价格类型名称
		order.setBedType(hotelOrderFromBean.getBedType());//床型
		order.setCheckinDate(hotelOrderFromBean.getCheckinDate());//入住日期
		order.setCheckoutDate(hotelOrderFromBean.getCheckoutDate());//离店日期
		order.setArrivalTime(hotelOrderFromBean.getArrivalTime());//到店日期
		order.setLatestArrivalTime(hotelOrderFromBean.getLatestArrivalTime());//最晚到店日期
		
		order.setRoomQuantity(Integer.parseInt(hotelOrderFromBean.getRoomQuantity()));//房间数
		order.setChannel(hotelOrderFromBean.getRoomChannel());//供货商渠道号
		order.setSpecialRequest(hotelOrderFromBean.getSpecialRequest());//特殊要求
		
		order.setPriceList(null);//订单价格明细
		order.setSource(null);//订单来源
		order.setType(OrderType.TYPE_MANGO);//订单类型
		order.setCreator(null);//创建人
		order.setCreateDate(new Date());
		order.setCreatorName(null);//创建人名称
		order.setSupplierAlias(null);//提供商名称
		order.setSupplierID(null);//提供商Id
	}

	//组装条款信息
	public void combineOrderClauseInfo(OrOrder order,HotelOrderFromBean hotelOrderFromBean) {
		
		
	}
    
	//组装电商信息
	public  void combineOrderEBInfo(OrOrder order,HotelOrderFromBean hotelOrderFromBean){
		
	}
    
	//组装会员信息
	public void combineOrderMemberInfo(OrOrder order,HotelOrderFromBean hotelOrderFromBean) {
		//order.setFellowList(hotelOrderFromBean.getFellowList());//入住人
		order.setLinkMan(hotelOrderFromBean.getLinkMan());//联系人
		order.setMobile(hotelOrderFromBean.getMobile());//手机
		order.setEmail(hotelOrderFromBean.getEmail());//电邮
		order.setConfirmType(ConfirmType.SMS);//确认方式
		
		order.setMemberCd(null);//会员cd（实际是会籍cd）
		order.setMemberId(null);//会员id(实际是会员id)
		order.setEmergencyLevel(EmergencyLevel.VIP1);//紧急程度
		order.setMemberAliasId(null);//联名商家
		order.setMemberName(null);//会员名称
		order.setMemberState(null);//会员状态
	}
    
	//组装和钱有关的信息
	public void combineOrderMoneyInfo(OrOrder order,HotelOrderFromBean hotelOrderFromBean) {
		double sum = hotelOrderFromBean.getPriceNum() * Integer.parseInt(hotelOrderFromBean.getRoomQuantity());
		double rateRMB = CurrencyBean.getRateToRMB(hotelOrderFromBean.getCurrency());
		double sumRMB = sum * rateRMB;
		order.setSum(sum);
		order.setRateId(rateRMB);
		order.setSumRmb(sumRMB);
        order.setPayMethod(hotelOrderFromBean.hasPayToPrepay() ? PayMethod.PRE_PAY : hotelOrderFromBean.getPayMethod());
		order.setPaymentCurrency(hotelOrderFromBean.getCurrency());
		
		if(PayMethod.PRE_PAY.equals(hotelOrderFromBean.getPayMethod())){
			order.setActualPayCurrency(CurrencyBean.RMB);
		}
		else{
			order.setActualPayCurrency(hotelOrderFromBean.getCurrency());
		}
		order.setPrepayType(hotelOrderFromBean.getOrderPayType());
	}
}

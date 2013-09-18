package com.mangocity.hotel.order.service.impl;

import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.B2BAgentCommUtils;
import com.mangocity.hotel.base.persistence.HtlFavourableDecrease;
import com.mangocity.hotel.base.service.BenefitService;
import com.mangocity.hotel.base.service.CommissionService;
import com.mangocity.hotel.order.constant.OrderType;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderItem;
import com.mangocity.hotel.order.service.IOrderBenefitService;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.util.log.MyLog;

public class OrderBenefitServiceImpl extends BenefitService implements IOrderBenefitService {

	private static final MyLog log = MyLog.getLogger(OrderBenefitServiceImpl.class);
	
	private CommissionService commissionService;
	
	private IOrderService orderService;

	/**
	 * 计算订单的优惠总金额 (优惠总金额不需要乘以汇率)
	 * @param rateMap 汇率
	 * @param order
	 */
	public void reCalculateBenefitAmount(Map<String, String> rateMap,
			OrOrder order) {
		//计算某价格类型在入住时间段一间房的优惠金额
		int benefitAmount = calculateBenefitAmount(String.valueOf(order.getChildRoomTypeId()), 
													order.getCheckinDate(), 
													order.getCheckoutDate(), 
													1);
		//支付酒店方式(1:底价,2:售价)
		String hotelPayType = "0";
		//订单保存的优惠总金额跟着合同的币种，初始化存放1间房的立减金额，在订单保存后为总数
	    order.setFavourableAmount(benefitAmount);
		//订单现有的应付金额
		double orderSumRmb = order.getSumRmb();
		if (null != rateMap) {
		    String rateStr = rateMap.get(order.getPaymentCurrency());
		    if (null == rateStr) {
		        rateStr = "1.0";
		    }
		    double rate = Double.valueOf(rateStr.trim()).doubleValue();
		    //订单应付金额减去乘以汇率后的1间房的立减金额(order的sumRmb和sum初始化默认是一间房的总价)
		    double rmbBenefitAmount = Math.floor((benefitAmount * rate));
		    order.setSumRmb(orderSumRmb - rmbBenefitAmount);
		} else {
		    //订单应付金额减去1间房的立减金额(order的sumRmb和sum初始化默认是一间房的总价)
		    order.setSumRmb(orderSumRmb - benefitAmount);
		}
	}

	/**
	 * 生成订单明细后，计算订单明细的立减金额 不需要乘以汇率
	 * @param order
	 */
	public void calculateOrderItemBenefit(OrOrder order) {
		//查询某时间段的优惠立减信息
		List<HtlFavourableDecrease> list = queryBenefitByDate(String.valueOf(order.getChildRoomTypeId()),
																order.getCheckinDate(),
																order.getCheckoutDate());
		List<OrOrderItem> orderItems = order.getOrderItems();
		
        float hstar = order.getHotelStar();
        if(hstar > 10){
        	String strHotelStar = this.getResourceManager().getDescription("res_hotelStarToNum",Math.round(order.getHotelStar()));
        	hstar = Float.parseFloat(strHotelStar);
        }
        int intStar = Math.round(hstar);
        int policyScope = orderService.getPolicyScope(order.getMemberCd());
        
		//遍历立减条件
		for (HtlFavourableDecrease htlFavourableDecrease : list) {
			String week = htlFavourableDecrease.getWeek();
			// 遍历OrderItem
			if(null != orderItems && !orderItems.isEmpty()){
				for(int y=0;y<orderItems.size();y++){
	    			OrOrderItem orderItem = orderItems.get(y);//检查日期是否在规则的开始结束日期区间
	    			boolean isBetween = DateUtil.checkDateBetween(orderItem.getNight(),htlFavourableDecrease.getBeginDate(),htlFavourableDecrease.getEndDate(),week);
	    			if(isBetween){
	    				// OrderItem的立减金额不需要乘以汇率
	    				int itemBenefitAmount = (int) htlFavourableDecrease.getDecreasePrice();
	    				orderItem.setSalePrice(orderItem.getSalePrice() - itemBenefitAmount);
	    				orderItem.setFavourableAmount(itemBenefitAmount);
	    				
	    				//如果是B2B代理过来的订单，就得计算代理佣金和佣金率 add by shengwei.zuo 2010-1-21
	    				 if(order.getType()==OrderType.TYPE_B2BAGENT){
	    						String payMo = order.getPayMethod();
	    						if(payMo.equals(PayMethod.PRE_PAY)){
	    							if(order.isPayToPrepay()){
		    							payMo = PayMethod.PAY;
		    						}
	    						}
	    						B2BAgentCommUtils B2BAgentCommUtilsInfo  =   commissionService.getB2BCommInfoForBenefit(order.getPaymentCurrency(),orderItem.getNight(), order.getHotelId(),order.getRoomTypeId(),order.getChildRoomTypeId(),
                               			payMo,order.getFavourableFlag(),orderItem.getFavourableAmount(),order.getMemberCd(),String.valueOf(intStar));
	    	    				 if(B2BAgentCommUtilsInfo!=null){
	    	    	                	log.info("benefitService  calculateOrderItemBenefit  B2BAgentCommUtilsInfo  FavourableAmount : "+
	    	    	                            " itemBenefitAmount "+ itemBenefitAmount+","+
	    	    	                			" agentComission  "+ B2BAgentCommUtilsInfo.getAgentComission() +","+
	    	    	                            " agentComissionPrice" + B2BAgentCommUtilsInfo.getAgentComissionPrice()+","+
	    	    	                            " agentComissionRate" + B2BAgentCommUtilsInfo.getAgentComissionRate() + ","+
	    	    	                            " comissionType"+ B2BAgentCommUtilsInfo.getComissionType() + ","+
	    	    	                            " comissionTypeValue" + B2BAgentCommUtilsInfo.getComissionTypeValue()+","+
	    	    	                            " CommTax" + B2BAgentCommUtilsInfo.getCommTax() );
	    	    	                	
	    	    	                	orderItem.setAgentComission(B2BAgentCommUtilsInfo.getAgentComission());
	    	    	                    orderItem.setAgentComissionPrice(B2BAgentCommUtilsInfo.getAgentComissionPrice());
	    	    	                    orderItem.setAgentComissionRate(B2BAgentCommUtilsInfo.getAgentComissionRate());
	    	    	                    orderItem.setComissionType(B2BAgentCommUtilsInfo.getComissionType());
	    	    	                    orderItem.setComissionTypeValue(B2BAgentCommUtilsInfo.getComissionTypeValue());
	    	    	                    orderItem.setCommTax(B2BAgentCommUtilsInfo.getCommTax());
	    	    	            }
	    	    				
	    				 }
	    			
	    				
	    				// 当面付立减规则中底价>0则要把OrderItem的底价更新为规则中设置的底价
	    				if(0 < htlFavourableDecrease.getBasePrice()){
	    					orderItem.setBasePrice(htlFavourableDecrease.getBasePrice());
	    				}
	    			}else{
	    			    //如果之前已经赋过立减金额值的Item不能再设置为0
	    			    if(0 >= orderItem.getFavourableAmount()){
	                        orderItem.setFavourableAmount(0);
	    			    }
	    			}
	    		}	
			}
		}
	}
	
	public void setCommissionService(CommissionService commissionService) {
		this.commissionService = commissionService;
	}
	
	public void setOrderService(IOrderService orderService) {
		this.orderService = orderService;
	}

}

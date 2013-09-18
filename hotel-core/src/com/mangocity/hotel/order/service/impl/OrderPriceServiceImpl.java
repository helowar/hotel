package com.mangocity.hotel.order.service.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mangocube.corenut.commons.exception.ErrorCode;

import com.mangocity.hotel.order.constant.BreakfastType;
import com.mangocity.hotel.order.constant.FeeType;
import com.mangocity.hotel.order.persistence.HotelorderFee;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderItem;
import com.mangocity.hotel.order.persistence.OrPayment;
import com.mangocity.hotel.order.persistence.OrPriceDetail;
import com.mangocity.hotel.order.service.IOrderPriceService;
import com.mangocity.hotel.order.service.exception.PriceInvalidException;
import com.mangocity.util.DateUtil;
import com.mangocity.util.log.MyLog;

/**
 * 
 * @author zengyong
 * 2009-9-4,上午10:57:14
 * 描述:计算和处理订单的各种价格
 */
public class OrderPriceServiceImpl implements IOrderPriceService{
	
	private static final MyLog log = MyLog.getLogger(OrderPriceServiceImpl.class);
	
	public enum OrderPriceError{
		@ErrorCode(comment="\"${1}\"价格信息异常!")
		PRICE_ERROR
	}
	
	public enum OrderRoomError{
		@ErrorCode(comment="\"${1}\"房型信息异常")
		ROOMCOUNT_ERROR
	}

    // 结算方式------底价加服务费
    public static final long FOOT_WAY1 = 2;
    
    // 结算方式------卖价返点
    public static final long FOOT_WAY2 = 1;

    public BigDecimal zeroDeciamal = new BigDecimal(0);
    /**
     * 计算销售价/底价总额,将入住期间的每天的销售价/底价相加
     * @param order
     * @param type "SALE" or "BASE"
     * @return
     */

	
    public double accountSaleBasePriceTotals(OrOrder order,String type)throws PriceInvalidException{
		
		double result = 0;
		
		List<OrPriceDetail> lst = order.getPriceList();
		if(lst!=null){//价格列表不为空
			Iterator<OrPriceDetail> iter = lst.iterator();
			if(type.equalsIgnoreCase("SALE")){
				while(iter.hasNext()){
					OrPriceDetail priceDetail = iter.next();
					 if(DateUtil.getDay(order.getCheckinDate(),priceDetail.getNight())>=0&&DateUtil.getDay(order.getCheckoutDate(),priceDetail.getNight())<0){
						 //在入住期间内
        				if ((new BigDecimal(priceDetail.getSalePrice())).compareTo(zeroDeciamal) > 0) {// 价格大于0
                            result += Math.ceil(priceDetail.getSalePrice());
                        } else {// 价格小于等0
                        	//log.info("销售价错误:" + priceDetail.getSalePrice());
                            throw new PriceInvalidException(OrderPriceError.PRICE_ERROR,new Object[]{"priceDetail.getSalePrice()"});
                        }
					 }
				}//end while
			}else if(type.equalsIgnoreCase("BASE")){
				while(iter.hasNext()){
					OrPriceDetail priceDetail = iter.next();
					if(DateUtil.getDay(order.getCheckinDate(),priceDetail.getNight())>=0&&DateUtil.getDay(order.getCheckoutDate(),priceDetail.getNight())<0){//在入住期间内
    					if((new BigDecimal(priceDetail.getBasePrice())).compareTo(zeroDeciamal)>0){//价格大于0
    						result += priceDetail.getBasePrice();
    					}else{//价格小于等0
    						//log.info("底价错误:"+priceDetail.getBasePrice());
    						throw new PriceInvalidException(OrderPriceError.PRICE_ERROR,new Object[]{"priceDetail.getBasePrice()"});
    					}
					}
				}//end while				
			}
		}
		
		return result*order.getRoomQuantity();//房间数;
	}

    
    
    
    /**
     * 取出积分对应的人民币
     * @param order
     * @return
     */
    private double getPoints(OrOrder order){
        double result = 0;
        List<OrPayment> payList  = order.getPaymentList();
        for(OrPayment payment : payList){
                if(payment.getPayType()==4){//积分支付
                    result+=payment.getMoney();
                }
        }//end for
        result = result/100;
        return result;
    }
    
    
    
    
    /**
     * 早餐费用
     * @param feeLst
     * @return
     */
    public double getBreakFastFee(List feeLst){
    	double  result = 0.0;
        List<HotelorderFee> itemLst = new ArrayList<HotelorderFee>(feeLst);
        for(HotelorderFee orderFee:itemLst){
            if(BreakfastType.CHINESE.equalsIgnoreCase(orderFee.getFeetype())||BreakfastType.BUFFET.equalsIgnoreCase(orderFee.getFeetype())||BreakfastType.WESTERN.equalsIgnoreCase(orderFee.getFeetype())){//早餐
                result += orderFee.getFeeprice()*orderFee.getFeecount();
            }
        }//end for
        return result;
    }
    
    /**
     * 加床费用
     * @param feeLst
     * @return
     */
    public double getBedFee(List feeLst){
    	double  result = 0.0;
        List<HotelorderFee> itemLst = new ArrayList<HotelorderFee>(feeLst);
        for(HotelorderFee orderFee:itemLst){
            if(FeeType.BED_FEE.equalsIgnoreCase(orderFee.getFeetype())){//加床
                result += orderFee.getFeeprice()*orderFee.getFeecount();
            }
        }//end for
        return result;
    }
    
    /**
     * internet费用
     * @param feeLst
     * @return
     */
    public double getInternetFee(List feeLst){
    	double  result = 0.0;
	    List<HotelorderFee> itemLst = new ArrayList<HotelorderFee>(feeLst);
	    for(HotelorderFee orderFee:itemLst){
	        if(FeeType.INTERNET_FEE.equalsIgnoreCase(orderFee.getFeetype())){//宽带
	            result += orderFee.getFeeprice()*orderFee.getFeecount();
	        }
	    }//end for
	    return result;
    }
    
    
    /**
     * 获取政府资金
     * @param order
     * @return
     */
    public double getGovFundFree(OrOrder order){
    	double  result = 0.0;
        List<OrOrderItem> itemLst = new ArrayList<OrOrderItem>(order.getOrderItems());
        for(OrOrderItem orderitem:itemLst){
            if(orderitem.isIncludeOtherTax()==true){//有政府基金
                result+= orderitem.getOtherTax();
            }
        }//end for
        return result;
    }
    
    /**
     * 获取服务资金
     * @param order
     * @return
     */
    public double getServiceFree(OrOrder order){
    	double  result = 0.0;
	    List<OrOrderItem> itemLst = new ArrayList<OrOrderItem>(order.getOrderItems());
	    for(OrOrderItem orderitem:itemLst){
	        if(orderitem.isIncludeService()==true){//有服务费
	            result+= orderitem.getServiceFee();
	        }
	    }//end for
	    return result;
    }
    
    public double getTaxFree(OrOrder order){
    	double  result = 0.0;
	    List<OrOrderItem> itemLst = new ArrayList<OrOrderItem>(order.getOrderItems());
	    for(OrOrderItem orderitem:itemLst){
	        if(orderitem.isHasTaxFee()==true){//有税费
	            result+= orderitem.getTaxFee();
	        }
	    }//end for
	    return result;
    }
}
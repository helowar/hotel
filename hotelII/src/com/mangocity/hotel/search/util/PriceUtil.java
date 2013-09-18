package com.mangocity.hotel.search.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.order.persistence.OrPriceDetail;
import com.mangocity.hotel.search.constant.PayMethod;
import com.mangocity.hotel.search.handler.impl.HotelQueryHandler;
import com.mangocity.hotel.search.vo.SaleItemVO;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.CurrencyBean;

public class PriceUtil implements Serializable {
   
	/**
	 * key=currency,value=currencyShow
	 */
	public static Map<String, String> currencyMap = null;
	public final static String FREEPRICE = "免费";

    static {
    	currencyMap = new HashMap<String, String>();
    	currencyMap.put("RMB", "&yen;");
    	currencyMap.put("HKD", "HKD");
    	currencyMap.put("MOP", "MOP");
    }
	
	//空的构造器
	public PriceUtil(){}
	public static final String CURRENCYRMBSYBMOL = "&yen;";
	/**
	 * 封一进十
	 */
	public static final String TYPE_CEIL ="ceil";
	/**
	 * 去掉最后一位，不进
	 */
	public static final String TYPE_FLOOR ="floor";
	/**
	 * 原币种显示
	 */
	public static List<SaleItemVO> getSaleItemVOList(List<OrPriceDetail> priceList,String currency){
		 if(priceList == null){return Collections.EMPTY_LIST;}
		 List<SaleItemVO> saleItemVOList = new ArrayList<SaleItemVO>();
		 for(int i = 0; i < priceList.size();i++){
			 OrPriceDetail orPrice = priceList.get(i);
			 SaleItemVO saleItemVO = new SaleItemVO();
			 saleItemVO.setSalePrice(CurrencyBean.numSaveInDecimal(orPrice.getSalePrice(),1));
			 saleItemVO.setWeekDay(HotelQueryHandler.WeekDay.getValueByKey(DateUtil.getWeekOfDate(orPrice.getNight())));
			 String currencyStr =  getCurrencyStr(currency);
			 saleItemVO.setSalePriceStr(currencyStr+saleItemVO.getSalePrice());
			 saleItemVOList.add(saleItemVO);
		 }
		 return saleItemVOList;
			 
	}
	
	/**
	 * 根据需求改变显示，现只有香港和澳门的预付用RMB显示,预付全部用RMB显示（面付不变）
	 */
	public static List<SaleItemVO> getSaleItemVOList(List<OrPriceDetail> priceList,String payMethod,String currency){
		 List<SaleItemVO> saleItemVOList = getSaleItemVOList(priceList,currency); 
		 changeSalItemVOList(saleItemVOList,payMethod,currency);
		 changeZeroToFree(saleItemVOList);
		 return saleItemVOList;
	}
	// double ratedoures = BigDecimal.valueOf(money).multiply(BigDecimal.valueOf(rateToHKD)).doubleValue();
	//修改saleItemVO的值,现只有香港和澳门的预付用RMB显示,预付全部用RMB显示
	private static void changeSalItemVOList(List<SaleItemVO> saleItemVOList,String payMethod,String currency){
		if(CurrencyBean.RMB.equals(currency) || PayMethod.PAY.equals(payMethod) ){return;}
		double rateToRMB = CurrencyBean.getRateToRMB(currency);
		for(int i = 0 ; i < saleItemVOList.size(); i++){
			SaleItemVO saleItemVO = saleItemVOList.get(i);
			double priceRMB =  BigDecimal.valueOf(saleItemVO.getSalePrice()).multiply(BigDecimal.valueOf(rateToRMB)).doubleValue();
			priceRMB = CurrencyBean.numSaveInDecimal(priceRMB, 1);
		    saleItemVO.setSalePriceStr(CURRENCYRMBSYBMOL+priceRMB);
		}
	}
	
	//把价格为0的变为免费
	private static void changeZeroToFree(List<SaleItemVO> saleItemVOList){
		for(int i = 0 ; i <saleItemVOList.size();i++){
			SaleItemVO saleItemVO = saleItemVOList.get(i);
			double price = saleItemVO.getSalePrice();
			if(price==0.0){
				saleItemVO.setSalePriceStr(FREEPRICE);
			}
		}
	}
	
	private static String getCurrencyStr(String currency){
		return currencyMap.get(currency);
	}
	
	 /**
     * 保留几位小数，最后一位小数是进一位的
     */   
    public static double numSaveInDecimal(double num,int decimal){
    	double numNew = Math.ceil(num*Math.pow(10.0, decimal));
		numNew /= Math.pow(10.0, decimal);
		return numNew;
    }
    /**
     * 保留几位小数
     */
    public static double numSaveInDecimal(double num,int decimal,String type){
         if(TYPE_FLOOR.equals(type)){
    	   double numNew = Math.floor(num*Math.pow(10.0, decimal));
		   numNew /= Math.pow(10.0, decimal);
		   return numNew;
    	  }
         return numSaveInDecimal(num,decimal);
    }
    
//    public static void main(String[] args){
//    	double num = 125.6789D;
//    	System.out.println(numSaveInDecimal(num,1,PriceUtil.TYPE_FLOOR));
//    }
	
}

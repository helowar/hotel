package com.mangocity.hotel.search.service.assistant;

import java.util.Comparator;





/**
 * 商品排序之一(能订的排在不能订的排面)
 * @author 
 *
 */
public class PayMethodComparator implements Comparator<PayMethodSort>{
	

	/**
	 * 商品排序
	 * 能预订的排在不能订的前面
	 * 
	 */
	public int compare(PayMethodSort c1, PayMethodSort c2){
		int p1 = 0,p2=0;
		if("PAY".equalsIgnoreCase(c1.getPayMethod())){//面付
			p1 =1;
		}else if("PRE_PAY".equalsIgnoreCase(c1.getPayMethod())){
			p1=2;
		}

		
		if("PAY".equalsIgnoreCase(c2.getPayMethod())){//面付
			p2 =1;
		}else if("PRE_PAY".equalsIgnoreCase(c2.getPayMethod())){
			p2=2;
		}
		if(p1>p2){
			return 1;
		}else if(p1<p2){
			return -1;
		}
		return 0;
	}
}

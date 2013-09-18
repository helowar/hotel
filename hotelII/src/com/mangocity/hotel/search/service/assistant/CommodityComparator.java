package com.mangocity.hotel.search.service.assistant;

import java.util.Comparator;



/**
 * 商品排序之一(能订的排在不能订的排面)
 * @author 
 *
 */
public class CommodityComparator implements Comparator<Commodity>{
	

	/**
	 * 商品排序
	 * 能预订的排在不能订的前面
	 * 按价格高低排序,最低价的价格类型排在前面
	 * 直付方式排序,面付前面,预付后面
	 */
	public int compare(Commodity c1, Commodity c2){
		int c1book = c1.isCanBookRoomType()==true?1:0;
		int c2book = c1.isCanBookRoomType()==true?1:0;
		if(c1book>c2book){
			return 1;
		}else if(c1book<c2book){
			return -1;
		}
		
		if(c1.getMinPirceRoomType()>c2.getMinPirceRoomType()){
			return 1;
		}else if(c1.getMinPirceRoomType()<c2.getMinPirceRoomType()){
			return -1;
		}
		
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

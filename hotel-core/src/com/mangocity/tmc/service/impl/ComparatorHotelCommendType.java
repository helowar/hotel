package com.mangocity.tmc.service.impl;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.mangocity.tmc.service.assistant.HotelInfo;
import com.mangocity.util.log.MyLog;

public class ComparatorHotelCommendType implements Comparator{
	private static final MyLog log = MyLog.getLogger(ComparatorHotelCommendType.class);
	/**
	 * @author gongchunshen 2008-12-15
	 * 比较酒店类型 1特，2金，3银，4黑
	 */
	public int compare(Object o1, Object o2) {
		HotelInfo hotel01=(HotelInfo)o1;
		HotelInfo hotel02=(HotelInfo)o2;		
		//如果酒店类型为空或“”，则设为5，排在倒数第二个
		if(hotel01.getCommendType() ==null || hotel01.getCommendType().equals("") ){
			hotel01.setCommendType("5");
		}
		if(hotel02.getCommendType() ==null || hotel02.getCommendType().equals("") ){
			hotel02.setCommendType("5");
		}
		
		//如果酒店类型为4黑”，则设为6，排在倒数第一
		if(hotel01.getCommendType() !=null && hotel01.getCommendType().equals("4") ){
			hotel01.setCommendType("6");
		}
		if(hotel02.getCommendType() !=null && hotel02.getCommendType().equals("4") ){
			hotel02.setCommendType("6");
		}
		
		int flag=hotel01.getCommendType().compareTo(hotel02.getCommendType());
		
		//如果酒店类型相同，则按最低价格排序
		if(flag ==0){
			if((hotel01.getLowPrice()!=null && !hotel01.getLowPrice().equals("")) && (hotel02.getLowPrice()!=null && !hotel02.getLowPrice().equals("")) ){
				return Double.valueOf(hotel01.getLowPrice()).compareTo(Double.valueOf(hotel02.getLowPrice()));	
			}else{
				return flag;
			}
		}else {		
			return flag;
		}
	}
	
	
	
	
	
	
	public static void main(String[] args) {
		
		HotelInfo h1=new HotelInfo();
		h1.setHotelChnName("酒店1");
		h1.setCommendType("2");
		HotelInfo h2=new HotelInfo();
		h2.setHotelChnName("酒店2");
		h2.setCommendType("4");
		HotelInfo h3=new HotelInfo();
		h3.setHotelChnName("酒店3");
		h3.setCommendType("1");
		HotelInfo h4=new HotelInfo();
		h4.setHotelChnName("酒店4");
		h4.setCommendType("3");
		HotelInfo h5=new HotelInfo();
		h5.setHotelChnName("酒店5");
		h5.setCommendType("");
		HotelInfo h6=new HotelInfo();
		h6.setHotelChnName("酒店6");
		h6.setCommendType("2");
		List<HotelInfo> hotellist= new ArrayList<HotelInfo>();
		hotellist.add(h1);
		hotellist.add(h2);
		hotellist.add(h3);
		hotellist.add(h4);
		hotellist.add(h5);
		hotellist.add(h6);
		ComparatorHotelCommendType comparator= new ComparatorHotelCommendType();
		Collections.sort(hotellist,comparator);
		for (HotelInfo hotel :hotellist){			
			log.info(hotel.getHotelChnName()+",类型:"+hotel.getCommendType());
		}
		

	}

}

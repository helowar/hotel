package com.mangocity.hotel.base.manage.assistant;

import java.util.Comparator;

import com.mangocity.hotel.base.persistence.HtlHotelBase;

/**
 * 
 * 
 */
public class HotelComparator implements Comparator  { 

	public int compare(Object o1, Object o2) {
		  // TODO Auto-generated method stub
		  HtlHotelBase h1 = (HtlHotelBase) o1;
		  HtlHotelBase h2 = (HtlHotelBase) o2;
		  String str1 = h1.getHotelcodeforchannel()==null?"":h1.getHotelcodeforchannel();
		  String str2 = h2.getHotelcodeforchannel()==null?"":h2.getHotelcodeforchannel();
		  
		  return str1.compareTo(str2);
		  }
		}


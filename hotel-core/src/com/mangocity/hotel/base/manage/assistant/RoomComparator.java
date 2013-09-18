package com.mangocity.hotel.base.manage.assistant;

import java.util.Comparator;

import com.mangocity.hotel.base.persistence.HtlRoomBase;

/**
 * 
 * 
 */
public class RoomComparator implements Comparator  {

	public int compare(Object o1, Object o2) {
		  // TODO Auto-generated method stub
		  HtlRoomBase h1 = (HtlRoomBase) o1;
		  HtlRoomBase h2 = (HtlRoomBase) o2;
		  String str1 = h1.getRoomtypecodeforchannel()==null?"":h1.getRoomtypecodeforchannel();
		  String str2 = h2.getRoomtypecodeforchannel()==null?"":h2.getRoomtypecodeforchannel();
		 return str1.compareTo(str2);
		  }
		}


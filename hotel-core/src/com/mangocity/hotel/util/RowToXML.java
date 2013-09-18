package com.mangocity.hotel.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class RowToXML {
	public static String createXMLFromMap(Map map) {
		String result = "";
		Set set = map.keySet();
		if(!set.isEmpty()) {
			Iterator iterator = set.iterator();
			while(iterator.hasNext()) {
				String temp = (String) iterator.next();
				result  += "<" + temp + ">" + map.get(temp) + "</" + temp + ">";
			}
		}
		return result;
		
	}
}

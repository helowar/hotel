package com.mangocity.hotel.search.constant;

import java.util.HashMap;
import java.util.Map;

public class CityBrandConstant {
	
    public  static Map<Long,String> cityBrandMap;
    static {
    	cityBrandMap = new HashMap<Long,String>();
    	addCityBrandToMap();
    }
	
	// add by the sequence of key 1,2,3,4
	private  static void addCityBrandToMap(){
	    cityBrandMap.put(1561L,"华侨城");
	    cityBrandMap.put(1583L,"如家");
		cityBrandMap.put(1584L,"格林豪泰");
		cityBrandMap.put(1585L,"洲际");
		cityBrandMap.put(1601L,"莫泰");
		cityBrandMap.put(1622L,"首旅建国");
		cityBrandMap.put(1623L,"温德姆");
		cityBrandMap.put(1624L,"世纪金源");
		cityBrandMap.put(1641L,"金陵集团");
		cityBrandMap.put(1643L,"凯宾斯基");		
		cityBrandMap.put(1661L,"万豪");
		cityBrandMap.put(1662L,"维也纳");
		cityBrandMap.put(1682L,"希尔顿");
		cityBrandMap.put(1683L,"喜达屋");
		cityBrandMap.put(1709L,"富豪");
		cityBrandMap.put(1701L,"雅高");
		cityBrandMap.put(1714L,"海逸");
		cityBrandMap.put(1715L,"汉庭");
		cityBrandMap.put(1719L,"维景");
		cityBrandMap.put(1720L,"香格里拉");
		cityBrandMap.put(1730L,"山水时尚");
		cityBrandMap.put(1732L,"最佳西方");
		cityBrandMap.put(1762L,"凯悦");	
		cityBrandMap.put(1765L,"岭南花园");
		cityBrandMap.put(1768L,"马哥孛罗");
		cityBrandMap.put(1861L,"开元");
		cityBrandMap.put(2001L, "四季");
		cityBrandMap.put(2061L, "怡莱");
		cityBrandMap.put(2281L,"玉渊潭");
		cityBrandMap.put(2361L,"文华东方");
	}

	
	public static  String getCityBrandName(Long cityBrandId){
		return cityBrandMap.get(cityBrandId);
	}
}

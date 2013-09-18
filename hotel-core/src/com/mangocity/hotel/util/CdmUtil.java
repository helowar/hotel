package com.mangocity.hotel.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.mangocity.hotel.base.web.InitServlet;



public class CdmUtil {
   
	 
	  /**
	   * @desc 通过省份，查找省份下所有的城市
	   * @param state
	   * @return String city 如：xia-西安#bji-宝鸡 组合
	   */
	  public static String getCitys(String state){
		  String citys="";
		  HashMap<String, Map<String, String>> mapCityObj = (HashMap<String, Map<String, String>>) InitServlet.mapCityObj;
		  
		  if(mapCityObj!=null && mapCityObj.get(state)!=null){
			   HashMap<String, String> cityObj = (HashMap<String, String> )mapCityObj.get(state);
			 
			   Set<String> engCityNameList = cityObj.keySet();
			   
			   if(engCityNameList!=null){
				   for(String key : engCityNameList){				   
					   citys=(citys.length()>0) ? citys+"#"+key+"-"+cityObj.get(key) : key+"-"+cityObj.get(key);				   
				   }
			   }
		  }
		  	  
		   
		  return citys;
	  }
	  
	  
	  /**
	   * @desc 通过省份，省份下的某个城市,查找中文城市
	   * @param state,city
	   * @return String city 
	   */
	  public static String getCity(String state,String city){
		  String chnCity="";
		  HashMap<String, Map<String, String>> mapCityObj = (HashMap<String, Map<String, String>>) InitServlet.mapCityObj;
		  
		  if(mapCityObj!=null && mapCityObj.get(state)!=null){
			  HashMap<String, String> cityObj = (HashMap<String, String> )mapCityObj.get(state);
			  if(cityObj!=null && cityObj.get(city)!=null){
				  chnCity=cityObj.get(city);
			  }
		  }
		  return chnCity;
	  }
	  
	  /**
	   * @desc 通过某省份下的城市，查找某省份下的城市中所有的城区
	   * @param city
	   * @return String zone 如：XIAKFQD-开发区#XIABLQD-碑林区 组合
	   */
	  public static String getZones(String city){
		  String zones="";
		  HashMap<String, Map<String, String>> mapCitySozeObj = (HashMap<String, Map<String, String>>) InitServlet.mapCitySozeObj;
		  
		  if(mapCitySozeObj!=null && mapCitySozeObj.get(city)!=null){
			   HashMap<String, String> cityZone = (HashMap<String, String> )mapCitySozeObj.get(city);
			 
			   Set<String> engCityNameList = cityZone.keySet();
			   
			   if(engCityNameList!=null){
				   for(String key : engCityNameList){				   
					   zones=(zones.length()>0) ? zones+"#"+key+"-"+cityZone.get(key) : key+"-"+cityZone.get(key);				   
				   }
			   }
		  }
		  return zones;
	  }
	  
	  /**
	   * @desc 通过某省份下的城市，省份下的某城市,城区 查找中文城区
	   * @param  city,zone
	   * @return String zone 
	   */
	  public static String getZone(String city,String zone){
		  String chnZone="";
	      HashMap<String, Map<String, String>> mapCitySozeObj = (HashMap<String, Map<String, String>>) InitServlet.mapCitySozeObj;
		  
		  if(mapCitySozeObj!=null && mapCitySozeObj.get(city)!=null){
			   HashMap<String, String> cityZone = (HashMap<String, String> )mapCitySozeObj.get(city);
			   if(cityZone!=null && cityZone.get(zone)!=null){
				   chnZone=cityZone.get(zone);
			   }
			  
		  }
		  return chnZone;
	  }
     
	  /**
	   * @desc 通过城市 查找 所有商业区
	   * @param city
	   * @return String
	   */
	  public static String getBusinesses(String city){
		  	String businesses = "";
		  	HashMap<String, Map<String, String>> mapBusinessSozeObj = (HashMap<String, Map<String, String>>) InitServlet.mapBusinessSozeObj;
			  
			  if(mapBusinessSozeObj!=null && mapBusinessSozeObj.get(city)!=null){
				   HashMap<String, String> businessesMap = (HashMap<String, String> )mapBusinessSozeObj.get(city);
				 
				   Set<String> engBusinessNameList = businessesMap.keySet();
				   
				   if(engBusinessNameList!=null){
					   for(String key : engBusinessNameList){				   
						   businesses=(businesses.length()>0) ? businesses+"#"+key+"-"+businessesMap.get(key) : key+"-"+businessesMap.get(key);				   
					   }
				   }
			  }
		  	
		  	return businesses;
	  }
	  
	  /**
	   * @desc 通过商业区 代码 查找中文 名称
	   * @param cityCode,businessCode
	   * @return String
	   */
	  public static String getBusiness(String cityCode,String businessCode){
		  String business="";
		  HashMap<String, Map<String, String>> mapBusinessSozeObj = (HashMap<String, Map<String, String>>) InitServlet.mapBusinessSozeObj;
		  
		  if(mapBusinessSozeObj!=null && mapBusinessSozeObj.get(cityCode)!=null){
			   HashMap<String, String> businessMap = (HashMap<String, String> )mapBusinessSozeObj.get(cityCode);
			   if(businessMap!=null && businessMap.get(businessCode)!=null){
				   business=businessMap.get(businessCode);
			   }
			  
		  }
		  
		  return business;
	  }
	  
}

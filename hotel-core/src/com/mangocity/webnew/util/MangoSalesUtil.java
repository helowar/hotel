package com.mangocity.webnew.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 芒果酒店促销的信息
 * @author shengwei.zuo
 * 
 */
public class  MangoSalesUtil {
	
	public static final List EMPTY_LIST = new ArrayList();
	public static final Map EMPTY_MAP = new HashMap();
    
	//促销酒店所在的城市ID
	public static final String  SALES_CITY_CODE = "SHA";
	
	//需要显示的酒店ID数组
	public  static  String [] hotelIdArray = {"30105683","30080425","30005500","30002694"};
	
	public  static  String  [] roomTypeArray = 
	{"30005500_144080_161966_2010-9-30",
	 "30105683_143441_115346_2010-9-30","30105683_150500_122966_2010-9-30",
	 "30080425_143160_115326_2010-9-30",
	 "30002694_143462_162007_2010-9-30"}; 	
    
    public  static  Map<Long,List<MangoSalesEntity>>  mango_sales = new HashMap<Long,List<MangoSalesEntity>>();
    
    //拼装房型信息
    public static  void initSalesInfo(){
    	
    	for(int i=0;i<hotelIdArray.length;i++){
    		String hotelId = hotelIdArray[i];
    		if(null!=hotelId && !"".equals(hotelId)){
    			List<MangoSalesEntity> lstMangoSales = new  ArrayList<MangoSalesEntity>();
    			for(int j=0;j<roomTypeArray.length;j++){
        			String roomTyId = roomTypeArray[j];
        			String [] roomTyArray = roomTyId.split("_");
        			if(null!=roomTyArray && roomTyArray.length>0){
        				if(hotelId==roomTyArray[0]||hotelId.equals(roomTyArray[0])){
        					MangoSalesEntity mgSalesEntity  = new MangoSalesEntity();
        					mgSalesEntity.setHotelId(Long.parseLong(hotelId));
        					mgSalesEntity.setRoomTypeId(Long.parseLong(roomTyArray[1]));
        					mgSalesEntity.setPriceTypeId(Long.parseLong(roomTyArray[2]));
        					mgSalesEntity.setSalesEndDate(roomTyArray[3]);
        					lstMangoSales.add(mgSalesEntity);
        				}
        			}
        			
        		}
    			mango_sales.put(Long.parseLong(hotelId), lstMangoSales);
    		}
    		
    	}
    }
    
    //拼装页面传递过来的信息
    public static  void initMapInfo(String hotelIdStr,String saelInfoStr){
    	hotelIdArray  = hotelIdStr.split(",");
    	roomTypeArray = saelInfoStr.split(",");
    }
    
}

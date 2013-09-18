package com.mangocity.hotel.search.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mangocity.hotel.search.dao.HotelQueryDao;
import com.mangocity.hotel.search.model.HotelBasicInfo;
import com.mangocity.hotel.search.model.QueryHotelCondition;
import com.mangocity.hotel.search.service.HotelSearchByPayMethodService;
import com.mangocity.util.StringUtil;
import com.mangocity.util.log.MyLog;

public class HotelSearchByPayMethodServiceImpl implements HotelSearchByPayMethodService {

	private HotelQueryDao hotelQueryDao;
	private static final MyLog log = MyLog.getLogger(HotelSearchByPayMethodServiceImpl.class);
	
	public List<Long> queryHotelIdByMethod(Map<String,HotelBasicInfo> hotelBasicInfos,QueryHotelCondition queryHotelCondition,String payMethod) {
		List<Long> hotelIdList=getPrepayHotelIdList(hotelBasicInfos,queryHotelCondition,payMethod);		
		return hotelIdList;
	}

	public void setPrepayHotel(Map<String,HotelBasicInfo> hotelBasicInfos,String hotelIdsStr, QueryHotelCondition queryHotelCondition,String payMethod){
		if (StringUtil.isValidStr(hotelIdsStr)) {
			List<Long> prepayHotelIdList = hotelQueryDao.queryHasPrepayPriceTypeHotel(hotelIdsStr, payMethod, queryHotelCondition.getInDate(),
					queryHotelCondition.getOutDate(), queryHotelCondition.getCityCode());

			if (prepayHotelIdList != null && prepayHotelIdList.size() > 0) {
				for (int i = 0; i < prepayHotelIdList.size(); i++) {
					Object prepayHotelId = (Object) prepayHotelIdList.get(i);
					if (hotelBasicInfos.get(prepayHotelId.toString()) != null) {
						hotelBasicInfos.get(prepayHotelId.toString()).setPrepayHotel(true);
					}
				}
			}
		}
		}
	

	public Map<String,HotelBasicInfo> showHotelByPayMethod(Map<String,HotelBasicInfo> hotelBasicInfos,List<Long> prepayHotelIdList) {
							
		return fiterHotelIds(hotelBasicInfos,prepayHotelIdList);
	}
	
	
	/**
	 * 分批处理预付酒店，Hibernate的容量是1000
	 * @param hotelBasicInfos
	 * @param queryHotelCondition
	 * @return
	 */
	private List<Long> getPrepayHotelIdList(Map<String,HotelBasicInfo> hotelBasicInfos,QueryHotelCondition queryHotelCondition,String payMethod){		
		long time1 = System.currentTimeMillis();	
		List<String> hotelIdListStr=getHotelIdListFromMap( hotelBasicInfos);		
 		int hotelcount=hotelIdListStr.size();
		List<Long> hotelIdList=new ArrayList<Long>();
		List<Long> subHotelIdList;						
			for (int i = 0; i < hotelcount / 200 + 1; i++) {
				if ( ( i + 1) * 200 > hotelcount) {
					subHotelIdList=hotelQueryDao.queryHasPrepayPriceTypeHotelAndSaleChannel(listToString(hotelIdListStr.subList(i * 200, hotelcount)), 
							payMethod, queryHotelCondition.getInDate(), queryHotelCondition.getOutDate(),queryHotelCondition.getCityCode(),"1");

				} else {
					subHotelIdList=hotelQueryDao.queryHasPrepayPriceTypeHotelAndSaleChannel(listToString(hotelIdListStr.subList(i * 200, ( i + 1) * 200)), 
							payMethod, queryHotelCondition.getInDate(), queryHotelCondition.getOutDate(),queryHotelCondition.getCityCode(),"1");
					
				}
				hotelIdList.addAll(subHotelIdList);
			}
			long time2 = System.currentTimeMillis();
			log.info("获取酒店id的时间共为：  "+ (time2-time1)+" 毫秒 ");
		return hotelIdList;
	}
	

	private List<String> getHotelIdListFromMap(Map<String,HotelBasicInfo> hotelBasicInfos){
		Set<String> hotelIdSet=hotelBasicInfos.keySet();		
		List<String> hotelIdListStr=new ArrayList<String>();
		for(String hotelId:hotelIdSet){
			hotelIdListStr.add(hotelId);
		}
		return hotelIdListStr;
	}
	
	private String listToString(List<String> list){
		
		StringBuilder hotelIdList=new StringBuilder();
		if(list!=null&&list.size()>0){		
		for(String hotelId:list){
			hotelIdList.append(hotelId);
			hotelIdList.append(",");			
		}
		}
		String hotelIdListStr=hotelIdList.toString();
		int len=hotelIdListStr.length();
		return hotelIdListStr.substring(0, len-1);
	}

	private Map<String,HotelBasicInfo> fiterHotelIds(Map<String,HotelBasicInfo> hotelBasicInfos,List<Long> hotelIdList){
		Map<String,HotelBasicInfo> hotelBasicInfoSubMap=new HashMap<String,HotelBasicInfo>();
		for(int i=0;i<hotelIdList.size();i++){
			Object prepayHotelId = (Object)hotelIdList.get(i);	
			hotelBasicInfoSubMap.put(prepayHotelId.toString(), hotelBasicInfos.get(prepayHotelId.toString()));
		}
		return hotelBasicInfoSubMap;
	
	}
	

	public void setHotelQueryDao(HotelQueryDao hotelQueryDao) {
		this.hotelQueryDao = hotelQueryDao;
	}

	
}

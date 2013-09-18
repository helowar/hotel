package com.mangocity.hotel.pricelowest.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.ejb.search.HotelSearchEJB;
import com.mangocity.hotel.pricelowest.persistence.HtlLowestPrice;
import com.mangocity.hotel.pricelowest.persistence.HtlLowestTask;
import com.mangocity.hotel.pricelowest.service.HotelLowestPriceService;
import com.mangocity.hotel.pricelowest.dao.HotelLowestPriceDao;
import com.mangocity.hotel.search.dto.HtlQueryDTO;
import com.mangocity.hotel.search.handler.CCQueryHander;
import com.mangocity.hotel.search.handler.HotelQueryHandler;
import com.mangocity.hotel.search.stub.vo.HotelInfoVO;
import com.mangocity.hotel.search.stub.vo.PreSaleVO;
import com.mangocity.hotel.search.stub.vo.QuotationVO;
import com.mangocity.hotel.search.stub.vo.RoomTypeVO;
import com.mangocity.hotel.search.util.SaleChannel;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.dao.GenericDAOHibernateImpl;
import com.mangocity.util.log.MyLog;

public class HotelLowestPriceServiceImpl extends GenericDAOHibernateImpl implements HotelLowestPriceService{
	
	//注入dao
	private HotelLowestPriceDao hotelLowestPriceDao;
    private HotelSearchEJB hotelSearchEJB ;
	
	private static final MyLog log = MyLog.getLogger(HotelLowestPriceServiceImpl.class);
	//初始化Task
	public void initHtlLowestTask(){
		hotelLowestPriceDao.deleteAllHtlLowestTask();
		List<Object[]> hotelIdsList= hotelLowestPriceDao.getActiveHotelIds();
		List<HtlLowestTask> taskList = new ArrayList<HtlLowestTask>();
		for(int i = 0; i < hotelIdsList.size(); i++){
			Object[] object = hotelIdsList.get(i);
			String cityCode = (object[0]==null)?"":object[0].toString();
			Long hotelId = Long.valueOf(object[1].toString());		
			HtlLowestTask task = new HtlLowestTask();
			task.setCityCode(cityCode);
			task.setHotelId(hotelId);
			task.setIsFinish(false);
			//task.setFinishTime(new Date());
			task.setIsTaken(false);
			taskList.add(task);
		}
		addHtlLowestTasks(taskList);
		
	}
	
	//添加或修改最低价表
	public void saveOrUpdateHtlLowestPrices(int size){
		List<HtlLowestTask> taskList = hotelLowestPriceDao.getHtlLowestTasks(size);
		setTakenToTasks(taskList);
		Map<String,List<String>> city_hotelsMap = combineTaskList(taskList);
		List<HtlLowestPrice> htlLowestPriceList = getHtlLowestPriceList(city_hotelsMap);
		saveHtlLowestPrices(htlLowestPriceList);
	}
	
	//添加或修改最低价表
	public String getHtlLowestPricesForJS(){
		String ableDate = DateUtil.dateToString(DateUtil.getDate(DateUtil.getSystemDate(), 1));
		List<Object[]> lowestPriceList = hotelLowestPriceDao.getHtlLowestPricesForJS(DateUtil.getDate(ableDate));
		StringBuffer sb = new StringBuffer();
		sb.append("var hotelInfos = new Array();");
		for(int i=0;i<lowestPriceList.size();i++){
			Object[] hotel=(Object[]) lowestPriceList.get(i);
			if(null==hotel[4]||"null".equals(hotel[4])||"".equals(hotel[4])){
				hotel[4]="0";
			}else{
				int endIndex=hotel[4].toString().indexOf(".");
				if(-1!=endIndex){
				hotel[4]=hotel[4].toString().substring(0, endIndex);
				}				
			}
			sb.append("hotelInfos[").append(i).append("]=");
			sb.append("new Array('").append(hotel[0]).append("','")
			.append(hotel[1]).append("','")
			.append(hotel[2]).append("','")
			.append(hotel[3]).append("','")
			.append(hotel[4]).append("');");
		}
		return sb.toString();
	}
	
	//设置taskList已经获取
	private void setTakenToTasks(List<HtlLowestTask> taskList){
		for(int i = 0 ; i < taskList.size(); i++){
			HtlLowestTask htlLowestTask = taskList.get(i);
			htlLowestTask.setIsTaken(true);
		}
		hotelLowestPriceDao.updateHtlLowestTasks(taskList);
		
	}
	
	//组装taskList为citycode,hotelIds对象
	private Map<String,List<String>> combineTaskList(List<HtlLowestTask> taskList){
		if(taskList.size()==0){
			return Collections.EMPTY_MAP;
		}
		Map<String,List<String>> cityHotelIdsMap = new HashMap<String,List<String>>();
		label1:for(int i = 0 ; i < taskList.size() ; i++){
			HtlLowestTask task = taskList.get(i);
			String cityCode = task.getCityCode();
			String hotelId = String.valueOf(task.getHotelId());
			for(Map.Entry<String,List<String>> cityHotelIds :cityHotelIdsMap.entrySet()){
				String cityCodeInMap = cityHotelIds.getKey();
				//如果Map已存在城市，其值添加
				if(cityCodeInMap.equals(cityCode)){
					List<String> hotelIds = cityHotelIds.getValue();
					hotelIds.add(hotelId);
					continue label1;
				}	
			}
			//没有城市的话，添加一条城市记录到Map中
			List<String> newHotelIds = new ArrayList<String>();
			newHotelIds.add(hotelId);
			cityHotelIdsMap.put(cityCode,newHotelIds);
		}
		return cityHotelIdsMap;
	}
	
	//获取对象
	private List<HtlLowestPrice> getHtlLowestPriceList(Map<String,List<String>> city_hotelIdsMap) /*throws RuntimeException*/{
		if(city_hotelIdsMap.size() == 0 ){
			return Collections.EMPTY_LIST;
		}
		List<HtlLowestPrice> lowestPriceList = new ArrayList<HtlLowestPrice>();
		for(Map.Entry<String,List<String>> cityHotelIds :city_hotelIdsMap.entrySet()){
				String cityCode = cityHotelIds.getKey();
				List<String> hotelIds = cityHotelIds.getValue();
				String hotelIdsStr = convertListToString(hotelIds);
		        HotelQueryHandler handler =  new CCQueryHander();
		    try{
		    	hotelSearchEJB.searchHotelsForPage(setQueryDTO(cityCode,hotelIds), handler);
            }catch(RuntimeException e){
            	log.error("hotelLowestservice queryhotelEJB error:"+"citycode:"+cityCode+",hotelIds:"+hotelIdsStr+",",e);
            	//throw e;
            }catch(Exception ex){
            	log.error("hotelLowestservice queryhotelEJB noRuntime error:" ,ex);
            }
            
			List<HotelInfoVO> hotelInfoList = handler.getHotelResutlList();
			System.out.println("hotelCount:"+hotelInfoList.size());
			combineLowestPriceList(lowestPriceList,hotelInfoList);
		}
		return lowestPriceList;	
	}
	
   //保存到数据库中包括task的值
	private void saveHtlLowestPrices(List<HtlLowestPrice> htlLowestPriceList){
              String hotelIdsInLowestPrice  = getHotelIdsFromList(htlLowestPriceList);
              List<HtlLowestPrice> lowestPriceList = hotelLowestPriceDao.getHtlLowestPrices(hotelIdsInLowestPrice);
              for(int i = 0 ; i < lowestPriceList.size() ; i ++){
            	  HtlLowestPrice htlLowestPriceInDB = lowestPriceList.get(i);
            	  Long hotelId = htlLowestPriceInDB.getHotelId();
            	  for(int j = 0 ; j < htlLowestPriceList.size(); j++){
            		  HtlLowestPrice htlLowestPriceToSave = htlLowestPriceList.get(j);
            		  if(hotelId.equals(htlLowestPriceToSave.getHotelId())){
            			  htlLowestPriceInDB.setAbleDate(htlLowestPriceToSave.getAbleDate());
            			  htlLowestPriceInDB.setModifyTime(new Date());
            			  htlLowestPriceInDB.setLowestPrice(htlLowestPriceToSave.getLowestPrice());
            			  htlLowestPriceInDB.setReturnCash(htlLowestPriceToSave.getReturnCash());
            			  htlLowestPriceInDB.setPresaleInfo(htlLowestPriceToSave.getPresaleInfo());
            			  htlLowestPriceList.remove(htlLowestPriceToSave);//移除需要修改的
            		  }
            	  }
              }
              hotelLowestPriceDao.saveOrUpdateHtlLowestPrices(lowestPriceList);//保存修改的
              hotelLowestPriceDao.saveOrUpdateHtlLowestPrices(htlLowestPriceList);//保存添加的
              //更新htllowestTask完成 下面的方法是否应该单独放出来
              List<HtlLowestTask> lowestTaskList = hotelLowestPriceDao.gtHtlLowestTasksByHotelIds(hotelIdsInLowestPrice);
              for(int i = 0 ; i < lowestTaskList.size(); i++){
            	  HtlLowestTask htlLowestTask = lowestTaskList.get(i);
            	  htlLowestTask.setIsFinish(true);
            	  htlLowestTask.setFinishTime(new Date());
              }
              hotelLowestPriceDao.updateHtlLowestTasks(lowestTaskList);
            
	}
	
	private String getHotelIdsFromList(List<HtlLowestPrice> htlLowestPriceList){
		StringBuffer sb_hotelIds = new StringBuffer();
		for(int i = 0 ; i <  htlLowestPriceList.size() ; i++){
			 String hotelId = String.valueOf(htlLowestPriceList.get(i).getHotelId());
			 sb_hotelIds.append(hotelId+",");
		}	
		return StringUtil.deleteLastChar(sb_hotelIds.toString(),',');
	}
	
	
   //设置查询参数	
	private HtlQueryDTO setQueryDTO(String cityCode,List<String> hotelIds){
		HtlQueryDTO queryDTO = new HtlQueryDTO();
		List<String> cityList = new ArrayList<String>();
		cityList.add(cityCode);
		queryDTO.setCityCodeList(cityList);
		String inDate = DateUtil.dateToString(DateUtil.getDate(DateUtil.getSystemDate(), 1));
		String outDate = DateUtil.dateToString(DateUtil.getDate(DateUtil.getSystemDate(), 2));
		queryDTO.setCheckInDate(DateUtil.getDate(inDate));
		queryDTO.setCheckOutDate(DateUtil.getDate(outDate));
		queryDTO.setHotelIdList(hotelIds);
		queryDTO.setPageSize(QUERY_SIZE);
		queryDTO.setFromChannel(SaleChannel.WEB);
		return queryDTO;
	}
	
	//lowestPriceList变化
	private void combineLowestPriceList(List<HtlLowestPrice> lowestPriceList,List<HotelInfoVO> hotelInfoList){
		if(hotelInfoList==null)return;
		for(int i = 0 ; i < hotelInfoList.size(); i++){
		    HtlLowestPrice htlLowestPrice = new HtlLowestPrice();
		    HotelInfoVO hotelInfo = hotelInfoList.get(i);
		    htlLowestPrice.setCityCode(hotelInfo.getCityCode());
		    htlLowestPrice.setCityName(hotelInfo.getCityName());
		    htlLowestPrice.setHotelId(Long.valueOf(hotelInfo.getHotelId()));
		    htlLowestPrice.setHotelName(hotelInfo.getChnName());
		    htlLowestPrice.setHotelStar(hotelInfo.getHotelStar());
		    htlLowestPrice.setBizZoneCode(hotelInfo.getBizZone());
		    htlLowestPrice.setBizZoneName(hotelInfo.getBizChnName());
		    htlLowestPrice.setLowestPrice(hotelInfo.getLowestPrice());
		    htlLowestPrice.setLowestPriceCurrency("RMB");
		    htlLowestPrice.setAbleDate(DateUtil.stringToDateMain(hotelInfo.getCheckInDate(),"yyyy-mm-dd"));
		    htlLowestPrice.setModifyTime(new Date());
		    htlLowestPrice.setHotelTheme("");
		    htlLowestPrice.setSupplierChannel("");
		    htlLowestPrice.setHotelLongitude(hotelInfo.getLongitude());
		    htlLowestPrice.setHotelLatitude(hotelInfo.getLatitude()); 
		    //添加礼包，添加房型名称
		    setSomeValueToHtlLowestPrice(hotelInfo.getQuotationId(),hotelInfo,htlLowestPrice);		    
		    lowestPriceList.add(htlLowestPrice);
		}
		
	}
	
	private void setSomeValueToHtlLowestPrice(String lowestPriceId,HotelInfoVO hotelInfo,HtlLowestPrice htlLowestPrice){
		if( null == lowestPriceId || null == hotelInfo || null == hotelInfo.getRoomTypeList() ){return ;}
		for(int i = 0 ; i < hotelInfo.getRoomTypeList().size() ; i++){
			RoomTypeVO roomTypeVO = hotelInfo.getRoomTypeList().get(i);
			if(null == roomTypeVO || null == roomTypeVO.getCommodityList() ){ continue;}
			for(int j = 0; j < roomTypeVO.getCommodityList().size() ; j++){
				QuotationVO quotationVO =  roomTypeVO.getCommodityList().get(j);
				if(null == quotationVO ){continue;}
				if( lowestPriceId.equals(String.valueOf(quotationVO.getPriceTypeId())) ){
					String lowestReturnCash = quotationVO.getReturnCashNum(); 				    
				    //添加信息
				    htlLowestPrice.setReturnCash(lowestReturnCash);
				    htlLowestPrice.setLowestPriceRoomName(roomTypeVO.getRoomtypeName()+"("+quotationVO.getCommodityName()+")");
				    setPresaleInfoToHtlLowestPrice(hotelInfo,htlLowestPrice);
					return ;
				}
			}
		}
	}
	
	//设置酒店促销信息
	private void setPresaleInfoToHtlLowestPrice(HotelInfoVO hotelInfo,HtlLowestPrice htlLowestPrice){
		if(null == hotelInfo.getPreSaleList())return;
		StringBuffer presale_sb  = new StringBuffer();
		for(int i = 0 ; i < hotelInfo.getPreSaleList().size(); i++){
			PreSaleVO psVO = hotelInfo.getPreSaleList().get(i);
			presale_sb.append(psVO.getPreSaleContent());
			presale_sb.append(psVO.getPreSaleBeginEnd());
		}
		htlLowestPrice.setPresaleInfo(presale_sb.toString());
	}
	
	//
	private String convertListToString(List<String> list){
		if(null == list) return null;
		StringBuffer strBf = new StringBuffer();
		for(int i = 0 ; i < list.size() ; i++){
		    strBf.append(list.get(i).toString()+",");	
		}
		return strBf.toString();
	}
	
	//添加任务表
	private void addHtlLowestTasks(List<HtlLowestTask> lowestTaskList){
		hotelLowestPriceDao.addHtlLowestTasks(lowestTaskList);
	}
	
    //添加或修改最低价表
	private void saveOrUpdateHtlLowestPrices(List<HtlLowestPrice> lowestPriceList){
		hotelLowestPriceDao.saveOrUpdateHtlLowestPrices(lowestPriceList);
	}
	
    //获得最低价表,取生成js用到的string
	public void setHotelLowestPriceDao(HotelLowestPriceDao hotelLowestPriceDao) {
		this.hotelLowestPriceDao = hotelLowestPriceDao;
	}

	public void setHotelSearchEJB(HotelSearchEJB hotelSearchEJB) {
		this.hotelSearchEJB = hotelSearchEJB;
	}

}

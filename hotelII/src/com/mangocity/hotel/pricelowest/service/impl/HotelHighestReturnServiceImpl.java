package com.mangocity.hotel.pricelowest.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.ejb.search.HotelSearchEJB;
import com.mangocity.hotel.pricelowest.dao.HotelHighestReturnDao;
import com.mangocity.hotel.pricelowest.persistence.HtlHighestReturn;
import com.mangocity.hotel.pricelowest.persistence.HtlHighestReturnTask;
import com.mangocity.hotel.pricelowest.service.HotelHighestReturnService;
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
import com.mangocity.util.log.MyLog;

public class HotelHighestReturnServiceImpl implements HotelHighestReturnService{
	
	//注入dao
	private HotelHighestReturnDao hotelHighestReturnDao;
    private HotelSearchEJB hotelSearchEJB ;
	
	private static final MyLog log = MyLog.getLogger(HotelHighestReturnServiceImpl.class);
	//初始化Task
	public void initHtlHighestReturnTask(){
		hotelHighestReturnDao.deleteAllHtlHighestReturnTask();
		List<Object[]> hotelIdsList= hotelHighestReturnDao.getActiveHotelIds();
		List<HtlHighestReturnTask> taskList = new ArrayList<HtlHighestReturnTask>();
		for(int i = 0; i < hotelIdsList.size(); i++){
			Object[] object = hotelIdsList.get(i);
			String cityCode = (object[0]==null)?"":object[0].toString();
			Long hotelId = Long.valueOf(object[1].toString());		
			HtlHighestReturnTask task = new HtlHighestReturnTask();
			task.setCityCode(cityCode);
			task.setHotelId(hotelId);
			task.setIsFinish(0);
			task.setIsTaken(0);
			taskList.add(task);
		}
		hotelHighestReturnDao.addHtlHighestReturnTasks(taskList);
	}
	
	//添加或修改最低价表
	public void saveOrUpdateHtlHighestReturns(int size){
		List<HtlHighestReturnTask> taskList = hotelHighestReturnDao.getHtlHighestReturnTasks(size);
		setTakenToTasks(taskList);
		Map<String,List<String>> city_hotelsMap = combineTaskList(taskList);
		List<HtlHighestReturn> htlHighestReturnList = getHtlHighestReturnList(city_hotelsMap);
		saveHtlHighestReturns(htlHighestReturnList);
	}
	
	//添加或修改最低价表
	public String getHtlHighestReturnsForJS(){
		String ableDate = DateUtil.dateToString(DateUtil.getDate(DateUtil.getSystemDate(), 1));
		List<Object[]> highestReturnList = hotelHighestReturnDao.getHtlHighestReturnsForJS(DateUtil.getDate(ableDate));
		StringBuffer sb = new StringBuffer();
		sb.append("var hotelInfos = new Array();");
		for(int i=0;i<highestReturnList.size();i++){
			Object[] hotel=(Object[]) highestReturnList.get(i);
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
	private void setTakenToTasks(List<HtlHighestReturnTask> taskList){
		for(int i = 0 ; i < taskList.size(); i++){
			HtlHighestReturnTask htlHighestReturnTask = taskList.get(i);
			htlHighestReturnTask.setIsTaken(1);
		}
		hotelHighestReturnDao.updateHtlHighestReturnTasks(taskList);
	}
	
	//组装taskList为citycode,hotelIds对象
	private Map<String,List<String>> combineTaskList(List<HtlHighestReturnTask> taskList){
		if(taskList.size()==0){
			return Collections.EMPTY_MAP;
		}
		Map<String,List<String>> cityHotelIdsMap = new HashMap<String,List<String>>();
		label1:for(int i = 0 ; i < taskList.size() ; i++){
			HtlHighestReturnTask task = taskList.get(i);
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
	private List<HtlHighestReturn> getHtlHighestReturnList(Map<String,List<String>> city_hotelIdsMap) /*throws RuntimeException*/{
		if(city_hotelIdsMap.size() == 0 ){
			return Collections.EMPTY_LIST;
		}
		List<HtlHighestReturn> highestReturnList = new ArrayList<HtlHighestReturn>();
		for(Map.Entry<String,List<String>> cityHotelIds :city_hotelIdsMap.entrySet()){
				String cityCode = cityHotelIds.getKey();
				List<String> hotelIds = cityHotelIds.getValue();
				String hotelIdsStr = convertListToString(hotelIds);
		        HotelQueryHandler handler =  new CCQueryHander();
		    try{
		    	hotelSearchEJB.searchHotelsForPage(setQueryDTO(cityCode,hotelIds), handler);
            }catch(RuntimeException e){
            	log.error("hotelHighestReturnService queryhotelEJB error:"+"citycode:"+cityCode+",hotelIds:"+hotelIdsStr+",",e);
            	//throw e;
            }catch(Exception ex){
            	log.error("hotelHighestReturnService queryhotelEJB noRuntime error:",ex);
            }
            
			List<HotelInfoVO> hotelInfoList = handler.getHotelResutlList();
			System.out.println("hotelCount:"+hotelInfoList.size());
			combineHighestReturnList(highestReturnList,hotelInfoList);
		}
		return highestReturnList;	
	}
	
   //保存到数据库中包括task的值
	private void saveHtlHighestReturns(List<HtlHighestReturn> newHtlHighestReturnList){
              String hotelIdsInNew  = getHotelIdsFromList(newHtlHighestReturnList);
              List<HtlHighestReturn> oldHighestReturnList = hotelHighestReturnDao.getHtlHighestReturns(hotelIdsInNew);
              for(int i = 0 ; i < oldHighestReturnList.size() ; i ++){
            	  HtlHighestReturn oldHtlHighestReturn = oldHighestReturnList.get(i);
            	  Long hotelId = oldHtlHighestReturn.getHotelId();
            	  for(int j = 0 ; j < newHtlHighestReturnList.size(); j++){
            		  HtlHighestReturn newHtlHighestReturn = newHtlHighestReturnList.get(j);
            		  if(hotelId.equals(newHtlHighestReturn.getHotelId())){
            			  oldHtlHighestReturn.setSalePrice(newHtlHighestReturn.getSalePrice());
            			  oldHtlHighestReturn.setSalePriceCurrency(newHtlHighestReturn.getSalePriceCurrency());
            			  oldHtlHighestReturn.setReturnCash(newHtlHighestReturn.getReturnCash());
            			  oldHtlHighestReturn.setAbleDate(newHtlHighestReturn.getAbleDate());
            			  oldHtlHighestReturn.setModifyTime(new Date());
            			  oldHtlHighestReturn.setHotelTheme(newHtlHighestReturn.getHotelTheme());
            			  oldHtlHighestReturn.setSupplierChannel(newHtlHighestReturn.getSupplierChannel());
            			  oldHtlHighestReturn.setHotelLongitude(newHtlHighestReturn.getHotelLongitude());
            			  oldHtlHighestReturn.setHotelLatitude(newHtlHighestReturn.getHotelLatitude());
            			  oldHtlHighestReturn.setRoomName(newHtlHighestReturn.getRoomName());
            			  oldHtlHighestReturn.setPresaleInfo(newHtlHighestReturn.getPresaleInfo());
            			  newHtlHighestReturnList.remove(newHtlHighestReturn);//移除需要修改的
            		  }
            	  }
              }
              
              hotelHighestReturnDao.saveOrUpdateHtlHighestReturns(oldHighestReturnList);//保存修改的
              hotelHighestReturnDao.saveOrUpdateHtlHighestReturns(newHtlHighestReturnList);//保存添加的
              //更新HtlHighestReturnTask完成 下面的方法是否应该单独放出来
              List<HtlHighestReturnTask> highestReturnTaskList = hotelHighestReturnDao.getHtlHighestReturnTasksByHotelIds(hotelIdsInNew);
              for(int i = 0 ; i < highestReturnTaskList.size(); i++){
            	  HtlHighestReturnTask htlHighestReturnTask = highestReturnTaskList.get(i);
            	  htlHighestReturnTask.setIsFinish(1);
            	  htlHighestReturnTask.setFinishTime(new Date());
              }
              hotelHighestReturnDao.updateHtlHighestReturnTasks(highestReturnTaskList);
	}
	
	private String getHotelIdsFromList(List<HtlHighestReturn> htlHighestReturnList){
		StringBuffer sb_hotelIds = new StringBuffer();
		for(int i = 0 ; i <  htlHighestReturnList.size() ; i++){
			 String hotelId = String.valueOf(htlHighestReturnList.get(i).getHotelId());
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
	
	//
	private void combineHighestReturnList(List<HtlHighestReturn> highestReturnList,List<HotelInfoVO> hotelInfoList){
		if(hotelInfoList==null)return;
		for(int i = 0 ; i < hotelInfoList.size(); i++){
		    HtlHighestReturn htlHighestReturn = new HtlHighestReturn();
		    HotelInfoVO hotelInfo = hotelInfoList.get(i);
		    htlHighestReturn.setCityCode(hotelInfo.getCityCode());
		    htlHighestReturn.setCityName(hotelInfo.getCityName());
		    htlHighestReturn.setHotelId(Long.valueOf(hotelInfo.getHotelId()));
		    htlHighestReturn.setHotelName(hotelInfo.getChnName());
		    htlHighestReturn.setHotelStar(hotelInfo.getHotelStar());
		    htlHighestReturn.setBizZoneCode(hotelInfo.getBizZone());
		    htlHighestReturn.setBizZoneName(hotelInfo.getBizChnName());
		    htlHighestReturn.setSalePriceCurrency("RMB");
		    htlHighestReturn.setAbleDate(DateUtil.stringToDateMain(hotelInfo.getCheckInDate(),"yyyy-mm-dd"));
		    htlHighestReturn.setModifyTime(new Date());
		    htlHighestReturn.setHotelTheme("");
		    htlHighestReturn.setSupplierChannel("");
		    htlHighestReturn.setHotelLongitude(hotelInfo.getLongitude());
		    htlHighestReturn.setHotelLatitude(hotelInfo.getLatitude()); 
		    //添加礼包，添加房型名称
		    setSomeValueToHtlHighestReturn(hotelInfo,htlHighestReturn);		    
		    highestReturnList.add(htlHighestReturn);
		}
		
	}
	
	private void setSomeValueToHtlHighestReturn(HotelInfoVO hotelInfo,HtlHighestReturn htlHighestReturn){
		if( null == hotelInfo || null == hotelInfo.getRoomTypeList() ){return ;}
		double highestReturnCash=0.0;
		//返现最高的价格类型
		RoomTypeVO highestReturnRoomTypeVO = null;
		QuotationVO highestReturnQuotation = null;
		
		//价格最低的价格类型
		RoomTypeVO lowestRoomTypeVO = null;
		QuotationVO lowestQuotation = null;
		
		for(int i = 0 ; i < hotelInfo.getRoomTypeList().size() ; i++){
			RoomTypeVO roomTypeVO = hotelInfo.getRoomTypeList().get(i);
			if(null == roomTypeVO || null == roomTypeVO.getCommodityList() ){ continue;}
			for(int j = 0; j < roomTypeVO.getCommodityList().size() ; j++){
				QuotationVO quotationVO =  roomTypeVO.getCommodityList().get(j);
				if(null == quotationVO ){continue;}
				String returnCash= quotationVO.getReturnCashNum();
				
				//如果没有返现
				if("X".equals(returnCash))continue;
				
				//如果返现金额一样，取售价低的价格类型
				if(highestReturnCash == Double.valueOf(returnCash)){
					if(highestReturnRoomTypeVO != null && highestReturnQuotation !=null
						 && Double.valueOf(highestReturnQuotation.getAvlPrice()) > Double.valueOf(quotationVO.getAvlPrice())){
						highestReturnCash = Double.valueOf(returnCash);
						highestReturnQuotation = quotationVO;
						highestReturnRoomTypeVO = roomTypeVO;
					}
				}
				
				//得到返现最高的价格类型
				if(highestReturnCash < Double.valueOf(returnCash)){
					highestReturnCash = Double.valueOf(returnCash);
					highestReturnQuotation = quotationVO;
					highestReturnRoomTypeVO = roomTypeVO;
				}
				
				//得到价格最低的价格类型
				if(StringUtil.isValidStr(hotelInfo.getQuotationId()) 
						&& hotelInfo.getQuotationId().equals(String.valueOf(quotationVO.getPriceTypeId())) ){
					lowestQuotation=quotationVO;
					lowestRoomTypeVO=roomTypeVO;
				}
			}
		}
		
		if(highestReturnRoomTypeVO != null && highestReturnQuotation != null){
			//得到返现最高的价格类型
			htlHighestReturn.setSalePrice(highestReturnQuotation.getAvlPrice());
			htlHighestReturn.setSalePriceCurrency(highestReturnQuotation.getCurrency());
			htlHighestReturn.setReturnCash(highestReturnQuotation.getReturnCashNum());
			htlHighestReturn.setRoomName(highestReturnRoomTypeVO.getRoomtypeName()+"("+highestReturnQuotation.getCommodityName()+")");
		}else if(lowestQuotation != null && lowestRoomTypeVO !=null){
			//如果返现都为0，则取最低的售价
			htlHighestReturn.setSalePrice(lowestQuotation.getAvlPrice());
			htlHighestReturn.setSalePriceCurrency(lowestQuotation.getCurrency());
			htlHighestReturn.setReturnCash(lowestQuotation.getReturnCashNum());
			htlHighestReturn.setRoomName(lowestRoomTypeVO.getRoomtypeName()+"("+lowestQuotation.getCommodityName()+")");
		}
		setPresaleInfoToHtlHighestReturn(hotelInfo,htlHighestReturn);
	}
	
	//设置酒店促销信息
	private void setPresaleInfoToHtlHighestReturn(HotelInfoVO hotelInfo,HtlHighestReturn htlHighestReturn){
		if(null == hotelInfo.getPreSaleList())return;
		StringBuffer presale_sb  = new StringBuffer();
		for(int i = 0 ; i < hotelInfo.getPreSaleList().size(); i++){
			PreSaleVO psVO = hotelInfo.getPreSaleList().get(i);
			presale_sb.append(psVO.getPreSaleContent());
			presale_sb.append(psVO.getPreSaleBeginEnd());
		}
		htlHighestReturn.setPresaleInfo(presale_sb.toString());
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

	public void setHotelHighestReturnDao(HotelHighestReturnDao hotelHighestReturnDao) {
		this.hotelHighestReturnDao = hotelHighestReturnDao;
	}

	public void setHotelSearchEJB(HotelSearchEJB hotelSearchEJB) {
		this.hotelSearchEJB = hotelSearchEJB;
	}

}

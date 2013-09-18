package com.mangocity.webnew.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlHotelSortByArea;
import com.mangocity.hotel.base.resource.ResourceManager;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hweb.persistence.DistrictHotelInfo;
import com.mangocity.hweb.persistence.HotelBookingInfoForHkSale;
import com.mangocity.hweb.persistence.HotelBookingResultInfoForHkSale;
import com.mangocity.hweb.persistence.QHotelInfo;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.bean.MustDate;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.util.hotel.constant.RoomState;
import com.mangocity.util.log.MyLog;
import com.mangocity.webnew.dao.QueryAllHkHotelDao;
import com.mangocity.webnew.service.QueryAllHkHotelService;

/**
 * 查询指定香港酒店的最低价和酒店总体是否可订
 * @author guzhijie
 *
 */
public class QueryAllHkHotelServiceImpl implements  QueryAllHkHotelService {
	
	private static final MyLog log = MyLog.getLogger(QueryAllHkHotelServiceImpl.class);
	
	private QueryAllHkHotelDao queryAllHkHotelDao;
	
    /**
     * 资源接口
     */
    private ResourceManager resourceManager;
	
	public List<HotelBookingResultInfoForHkSale> queryHkHotelInfo(String cityCode,String ableSaleDate) throws Exception{
		
		Long beginQueryDaoTime = Calendar.getInstance().getTime().getTime();
		List<HotelBookingInfoForHkSale> hotelBookingInfoForHkSaleList= queryAllHkHotelDao.queryAllHotelInfoByHotelIdAndAbleSaleDate(cityCode,ableSaleDate);
		Long endQueryDaoTime = Calendar.getInstance().getTime().getTime();
		log.info("-----根据指定酒店id查询香港酒店，从数据库查询数据花费时间 = " + (endQueryDaoTime-beginQueryDaoTime)+"ms");
		//根据酒店ID组装数据,key为酒店id，value为对应酒店id下面的List<HotelBookingInfoForHkSale>
		Map<String,List<HotelBookingInfoForHkSale>> hotelBookingInfoSplitByHotelIdMap 
																		= hotelBookingInfoSplitByHotelId(hotelBookingInfoForHkSaleList);
		Long beginMakeDistrictHotelInfo = Calendar.getInstance().getTime().getTime();
		//根据以上组装的map，遍历对应酒店下面的List<HotelBookingInfoForHkSale>，计算酒店的最低可售价格和总体可预订标志
		List<DistrictHotelInfo> districtHotelInfoLis = makeDistrictHotelInfo(hotelBookingInfoSplitByHotelIdMap,ableSaleDate);
		Long endMakeDistrictHotelInfo = Calendar.getInstance().getTime().getTime();
		log.info("-----把数据组装成酒店级数据花费时间 = " + (endMakeDistrictHotelInfo-beginMakeDistrictHotelInfo)+"ms");
		//根据商业区组装数据
		return makeDistrictHotelInfoByBusinussArea(cityCode,districtHotelInfoLis);
	}
	/**
	 * 根据酒店ID组装数据,key为酒店id，value为对应酒店id下面的List<HotelBookingInfoForHkSale>
	 * @param hotelBookingInfoForHkSaleList
	 * @return
	 */
	private Map<String,List<HotelBookingInfoForHkSale>> hotelBookingInfoSplitByHotelId(List<HotelBookingInfoForHkSale> hotelBookingInfoForHkSaleList ){
		
		//以商业区编码组装key、以酒店名称组装key
		Map<String,List<HotelBookingInfoForHkSale>> hotelBookingInfoSplitByHotelIdMap = new HashMap();
		for(HotelBookingInfoForHkSale hotelBookingInfoForHkSale : hotelBookingInfoForHkSaleList){
			hotelBookingInfoSplitByHotelIdMap.put(String.valueOf(hotelBookingInfoForHkSale.getHotelId())
													 ,new ArrayList<HotelBookingInfoForHkSale>());
		}
		//以hotelId为key组装map
		List<HotelBookingInfoForHkSale> hotelBookingInfoListSplitByHotelId = null;
		for(HotelBookingInfoForHkSale hotelBookingInfoForHkSale : hotelBookingInfoForHkSaleList){
			//如果不在外网显示预付价，把价格变为999999.0页面显示价格为***
			if("0".equals(hotelBookingInfoForHkSale.getWebPrepayShow()) && PayMethod.PRE_PAY.equals(hotelBookingInfoForHkSale.getPayMethod())){
				hotelBookingInfoForHkSale.setSalePrice(999999.0);
			}
			hotelBookingInfoListSplitByHotelId = hotelBookingInfoSplitByHotelIdMap
													.get(String.valueOf(hotelBookingInfoForHkSale.getHotelId()));
			hotelBookingInfoListSplitByHotelId.add(hotelBookingInfoForHkSale);
			hotelBookingInfoSplitByHotelIdMap.put(String.valueOf(hotelBookingInfoForHkSale.getHotelId())
												    ,hotelBookingInfoListSplitByHotelId);
		}
		return hotelBookingInfoSplitByHotelIdMap;
	}
	
	/**
	 * 根据以上组装的map，遍历对应酒店下面的List<HotelBookingInfoForHkSale>，计算酒店的最低可售价格和总体可预订标志
	 * @param hotelBookingInfoSplitByHotelIdMap
	 * @param ableSaleDate
	 * @return
	 */
	private List<DistrictHotelInfo> makeDistrictHotelInfo(Map<String,List<HotelBookingInfoForHkSale>> hotelBookingInfoSplitByHotelIdMap,String ableSaleDate){
		
		List<DistrictHotelInfo> districtHotelInfoLis = new ArrayList();
		for(Map.Entry<String,List<HotelBookingInfoForHkSale>> mapEntry : hotelBookingInfoSplitByHotelIdMap.entrySet()){
			List<HotelBookingInfoForHkSale> hotelBookingInfoList= mapEntry.getValue();
			if(hotelBookingInfoList.isEmpty())return districtHotelInfoLis;
			
			DistrictHotelInfo districtHotelInfo = new DistrictHotelInfo();
			districtHotelInfo.setHotelId(hotelBookingInfoList.get(0).getHotelId());
			districtHotelInfo.setHotelChnName(hotelBookingInfoList.get(0).getHotelChnName());
			districtHotelInfo.setCityCode(hotelBookingInfoList.get(0).getCityCode());
			if(CurrencyBean.HKD.equals(hotelBookingInfoList.get(0).getCurrency())){
				districtHotelInfo.setCurrency("HK$");
			}else if(CurrencyBean.RMB.equals(hotelBookingInfoList.get(0).getCurrency())){
				districtHotelInfo.setCurrency("CNY ");
			}else if(CurrencyBean.MOP.equals(hotelBookingInfoList.get(0).getCurrency())){
				districtHotelInfo.setCurrency("MOP$");
			}
			if(StringUtil.isValidStr(hotelBookingInfoList.get(0).getTuiJian()) ){
				districtHotelInfo.setTuijian(Integer.valueOf(hotelBookingInfoList.get(0).getTuiJian()));
			}else{
				districtHotelInfo.setTuijian(0);
			}
			if(!"".equals(hotelBookingInfoList.get(0).getBizZone())){
				districtHotelInfo.setBusinessArea(hotelBookingInfoList.get(0).getBizZone());
			}else{
				districtHotelInfo.setZone(hotelBookingInfoList.get(0).getZone());
			}
			if("".equals(hotelBookingInfoList.get(0).getHotelStar())){
				districtHotelInfo.setHotelStar(0F);
			}else{
				if(Integer.valueOf(hotelBookingInfoList.get(0).getHotelStar()) > 10){
		        	String strHotelStar = resourceManager.getDescription("res_hotelStarToNum",Math.round(Integer.valueOf(hotelBookingInfoList.get(0).getHotelStar())));
		        	districtHotelInfo.setHotelStar(Float.valueOf(strHotelStar));
		        }else{
		        	districtHotelInfo.setHotelStar(Float.valueOf(hotelBookingInfoList.get(0).getHotelStar()));
		        }
			}
			//设置对应星级的中文名
			if(districtHotelInfo.getHotelStar() > 4){
        		districtHotelInfo.setHotelStarChnName("豪华型");
        	}else if(districtHotelInfo.getHotelStar() <= 4 && districtHotelInfo.getHotelStar() > 3){
        		districtHotelInfo.setHotelStarChnName("高档型");
        	}else if(districtHotelInfo.getHotelStar() <= 3 && districtHotelInfo.getHotelStar() > 2){
        		districtHotelInfo.setHotelStarChnName("舒适型");
        	}else{
        		districtHotelInfo.setHotelStarChnName("经济型");
        	}
			//最低价格
			double minSalePrice=0.0;
			//酒店总体是否可预订标志
			boolean isBooking = false;
			//下面第一部求酒店的最低售价、第二步求酒店总体是否可预订
			for(HotelBookingInfoForHkSale hotelBookingInfo : hotelBookingInfoList){
				//第一部分
				//判断该房型是否为中旅房型、是否有映射编码并且激活，如果是中旅房型并且没有激活则无法查询出该房间价格
				if("1".equals(hotelBookingInfo.getIsHkRoomType()) 
						&& "0".equals(hotelBookingInfo.getMappingIsActive()))continue;
				//开关房标志
				boolean flag_roomOpen = !"G".equals(hotelBookingInfo.getCloseFlag());
				//如果是第一个，给最小价格赋值,hotelBookingInfo里面价格是不会为0的
				
				if(0.0 == minSalePrice && flag_roomOpen){
					minSalePrice = hotelBookingInfo.getSalePrice();
				}
				//如果上一个价格比当前价格高，把当前价格赋值为最新值
				if(minSalePrice > hotelBookingInfo.getSalePrice() && flag_roomOpen){
					minSalePrice = hotelBookingInfo.getSalePrice();
				}
				//如果为中旅房型，映射编码是激活的并且配额小于1，参与计算最低价，但是不可预订直接向下循环
				if("1".equals(hotelBookingInfo.getIsHkRoomType()) 
						&& Long.valueOf(hotelBookingInfo.getSumquota())<1)continue;
				//第二部分
				//如果售价为999999，这个房型为不可预订
				if(999999.0 == hotelBookingInfo.getSalePrice())continue;
				//如果已经判断过isBooking为true，不在执行下面语句，
				if(!isBooking && (!"G".equals(hotelBookingInfo.getCloseFlag())) 
							  && null != hotelBookingInfo.getRoomState() 
							  && !"".equals(hotelBookingInfo.getRoomState())){
					
					String[] roomStateStr = hotelBookingInfo.getRoomState().split("/");
					//是否满房 默认满房
					boolean isManFang = true;
					for(String roomStateStrItem : roomStateStr){
						roomStateStrItem = roomStateStrItem.substring(roomStateStrItem.indexOf(":")+1,roomStateStrItem.indexOf(":")+2);
						//-1为没有输入房态，4为满房,房态为不可超并且配额数为0也不可预订
						if(!(RoomState.ROOM_STATE_FULL.equals(roomStateStrItem) 
								|| (RoomState.ROOM_STATE_NOTOVERRUN.equals(roomStateStrItem) && 0 == Long.valueOf(hotelBookingInfo.getSumquota())))){
							isManFang = false;
							break;
						}
					}
					//开关房状态为开房、房型下面的某个床型房态没有满房，之后判断预订条款，如果没有预订条款，则不用判断
					if(!isManFang ){
						if(1!= hotelBookingInfo.getHasReserv()){//无预订条款无需判断
							isBooking = true;
						}else{
							isBooking = doForBookingFlag(hotelBookingInfo,ableSaleDate);
						}
						
					}
				}
			}
			districtHotelInfo.setMinSalePrice((int)Math.ceil(minSalePrice));
			districtHotelInfo.setBookingFlag(isBooking);
			districtHotelInfoLis.add(districtHotelInfo);
		}
		return districtHotelInfoLis;
	}
	
	/**
	 * 根据商业区组装数据
	 * @param districtHotelInfoLis
	 * @return
	 */
	private List<HotelBookingResultInfoForHkSale> makeDistrictHotelInfoByBusinussArea(String cityCode,List<DistrictHotelInfo> districtHotelInfoLis){
		Map<String,List<DistrictHotelInfo>>  districtHotelInfoByBizZoneMap = new HashMap();
		//以商业区三字码组装key，
		for(DistrictHotelInfo districtHotelInfo : districtHotelInfoLis){
			if(StringUtil.isValidStr(districtHotelInfo.getBusinessArea())){
				districtHotelInfoByBizZoneMap.put(districtHotelInfo.getBusinessArea(),new ArrayList<DistrictHotelInfo>() );
			}else if(StringUtil.isValidStr(districtHotelInfo.getZone())){
				//如果酒店的商业区和行政区中没有值，则把酒店数据统一放在key = otherBussinussArea中
				districtHotelInfoByBizZoneMap.put(districtHotelInfo.getZone(),new ArrayList<DistrictHotelInfo>() );
			}else{
				districtHotelInfoByBizZoneMap.put("otherBussinussArea",new ArrayList<DistrictHotelInfo>() );
			}
		}
		
		//组装对应商业区数据下面的List
		for(DistrictHotelInfo districtHotelInfo : districtHotelInfoLis){
			List<DistrictHotelInfo>  districtHotelInfoLisItemList = new ArrayList();
			String key ="";
			if(StringUtil.isValidStr(districtHotelInfo.getBusinessArea())){
				key = districtHotelInfo.getBusinessArea();
			}else if(StringUtil.isValidStr(districtHotelInfo.getZone())){
				//如果酒店的商业区中没有值，则把酒店数据统一放在key = otherBussinussArea中
				key = districtHotelInfo.getZone();
			}else{
				key = "otherBussinussArea";
			}
			districtHotelInfoLisItemList = districtHotelInfoByBizZoneMap.get(key);
			districtHotelInfoLisItemList.add(districtHotelInfo);
			districtHotelInfoByBizZoneMap.put(key, districtHotelInfoLisItemList);
		}
		
		//根据组装好的map，组装成对应list
		List<HotelBookingResultInfoForHkSale> hotelBookingResultInfoForHkSale = new ArrayList();
		Map<String,Integer> areaMap =  makeBusinessAreaMap(cityCode,districtHotelInfoByBizZoneMap);
		//获取商业区中文名
		HotelBookingResultInfoForHkSale hotelBookingResult;
		for(Map.Entry<String,Integer> areaMapEntry : areaMap.entrySet()){
			hotelBookingResult = new HotelBookingResultInfoForHkSale();
			hotelBookingResult.setBusinessArea(areaMapEntry.getKey());
			hotelBookingResult.setSort(Integer.valueOf(areaMapEntry.getValue()));
			if("HKGLDQD".equals(areaMapEntry.getKey())){
				//这是行政区其他的是商业区
				hotelBookingResult.setBusinessAreaName(InitServlet.citySozeObj.get(areaMapEntry.getKey()));
			}else{
				hotelBookingResult.setBusinessAreaName(InitServlet.businessSozeObj.get(areaMapEntry.getKey()));
			}
			if(null != districtHotelInfoByBizZoneMap.get(areaMapEntry.getKey())){
				//根据星级从高至低排序
				List<DistrictHotelInfo> districtHotelInfoList = districtHotelInfoByBizZoneMap.get(areaMapEntry.getKey());
				for(int i=districtHotelInfoList.size()-1;i>0;i--){
					for(int j=0;j<i;j++){
						DistrictHotelInfo districtHotelInfotemp = districtHotelInfoList.get(j);
						if(districtHotelInfoList.get(j).getHotelStar() < districtHotelInfoList.get(j+1).getHotelStar()){
							districtHotelInfoList.set(j, districtHotelInfoList.get(j+1));
							districtHotelInfoList.set(j+1, districtHotelInfotemp);
						}
					}
				}
				hotelBookingResult.setDistrictHotelInfo(districtHotelInfoList);
				
			}
			hotelBookingResultInfoForHkSale.add(hotelBookingResult);
		}
		HotelBookingResultInfoForHkSale hotelBookingResultInfoTemp;
		for(int i = hotelBookingResultInfoForHkSale.size()-1;i>0;i--){
			for(int j=0;j<i;j++){
				hotelBookingResultInfoTemp =  hotelBookingResultInfoForHkSale.get(j);
				if(hotelBookingResultInfoTemp.getSort()>hotelBookingResultInfoForHkSale.get(j+1).getSort()){
					hotelBookingResultInfoForHkSale.set(j, hotelBookingResultInfoForHkSale.get(j+1));
					hotelBookingResultInfoForHkSale.set(j+1, hotelBookingResultInfoTemp);
				}
			}
			
		}
		return hotelBookingResultInfoForHkSale;
	}
	
	/**
	 * 根据预订条款，判断是否可预订
	 * @param districtHotelInfoLis
	 * @return
	 */
	private boolean doForBookingFlag(HotelBookingInfoForHkSale hotelBookingInfo,String ableSaleDate){
		
		//是否可预订标志，默认可订
		//该功能只能查询一天一个间夜
		boolean isBooking = false;
		//获得当前日期
		if(0 != hotelBookingInfo.getMaxRestrictNights()){//入住间夜必须等于几个间夜的条款
			//如果入住间夜限制小于1晚
			if(hotelBookingInfo.getMaxRestrictNights() < 2){
				isBooking = true;
			}
		}else if(!isBooking && 0 != hotelBookingInfo.getContinueDay()){//入住期间必需连住几晚的条款
			if(hotelBookingInfo.getContinueDay() < 2 ){
				isBooking = true;
			}
		}else if(!isBooking && (null != hotelBookingInfo.getLatestBookableDate() || null != hotelBookingInfo.getFirstBookableDate())){//预订条款的时限限制
			isBooking = checkBookableDate(hotelBookingInfo,ableSaleDate);
		}else if(!isBooking && !"".equals(hotelBookingInfo.getMustInDate())){//必住条款
			isBooking = checkMustInDate(hotelBookingInfo,ableSaleDate);
		}else{
			isBooking =true;
		}
		
		return isBooking;
	}
	
	/**
     * 对必住日期进行逻辑判断的方法
     * 
     * @param queryHotelForWebSaleItems
     * @param bookHintNoMeet
     * @param queryBean
     */
    private boolean checkMustInDate(HotelBookingInfoForHkSale hotelBookingInfo,String ableSaleDate) {
        // TODO Auto-generated method stub
        String mustDatesRelation = hotelBookingInfo.getContinueDatesRelation();// 取得必住日期关系
        List<MustDate> mustInDates = new ArrayList<MustDate>();
        int type = MustDate.getMustIndatesAndType(mustInDates, hotelBookingInfo.getMustInDate());
        boolean isCanLive = false;
        if (!StringUtil.isValidStr(mustDatesRelation) || mustDatesRelation.equals("or")) {// 里边为 或者
            // 得到必住日期集合
            if (type == MustDate.DATE_TYPE) {// 必住日期逻辑
                for (MustDate date : mustInDates) {
                    // //如果入住日期包括任意一个必住日期即可入住
                    if (DateUtil.isBetween(date.getContinueDate(), DateUtil.stringToDate(ableSaleDate), 
                    		DateUtil.getDate(DateUtil.stringToDate(ableSaleDate),1))) {
                        isCanLive = true;
                        break;
                    }
                }
            } else if (type == MustDate.WEEK_TYPE) {// 必住星期逻辑
                for (MustDate date : mustInDates) {
                    if (DateUtil.isBetween(DateUtil.stringToDate(ableSaleDate), date.getContinueDate(), date
                        .getContinueEndDate())) {
                        String[] checkInWeeks = date.getWeeks().split(",");
                        Date[] checkInDates = DateUtil.getDateWithWeek(DateUtil.stringToDate(ableSaleDate),
                        		DateUtil.getDate(DateUtil.stringToDate(ableSaleDate),1), checkInWeeks);
                        if (0 < checkInDates.length) {// 说明入住日期内已经至少包含有一个必住星期
                            isCanLive = true;
                        }
                        break;
                    }
                }
            }
        } else {// 里边为 并且 逻辑判断
            if (type == MustDate.DATE_TYPE) {// 必住日期逻辑
                if (!(!DateUtil.isBetween(hotelBookingInfo.getMustFirstDate(), DateUtil.stringToDate(ableSaleDate)
                		, DateUtil.getDate(DateUtil.stringToDate(ableSaleDate),1))
                    || !DateUtil.isBetween(hotelBookingInfo.getMustLastDate(), DateUtil.stringToDate(ableSaleDate)
                    		, DateUtil.getDate(DateUtil.stringToDate(ableSaleDate),1)))) {
                	
                	isCanLive = true;
                }
            } else if (type == MustDate.WEEK_TYPE) {// 必住星期逻辑
                for (MustDate date : mustInDates) {
                    if (DateUtil.isBetween(DateUtil.stringToDate(ableSaleDate), date.getContinueDate(), date
                        .getContinueEndDate())) {
                        String[] checkInWeeks = date.getWeeks().split(",");
                        Date[] checkInDates = DateUtil.getDateWithWeek(DateUtil.stringToDate(ableSaleDate),
                        		DateUtil.getDate(DateUtil.stringToDate(ableSaleDate),1), checkInWeeks);
                        if (checkInDates.length >= checkInWeeks.length) {// 说明入住日期内已经至少包含有一个整体的必住星期
                            isCanLive = true;
                        }
                        break;
                    }
                }
                
            }
        }
        return isCanLive;
    }
    
    /**
     * 对最早可预订和最晚可预订方法进行逻辑判断的方法
     * @param hotelBookingInfo
     * @param ableSaleDate
     * @return
     */
    private boolean checkBookableDate(HotelBookingInfoForHkSale hotelBookingInfo,String ableSaleDate) {
    	boolean isFirstBooking = false;
    	boolean isLatestBooking = false;
    	Calendar calendar = Calendar.getInstance();
		Date today = calendar.getTime();
		//最早可预订日期不为空，判断ableSaleDate是否满足条件
		if(null == hotelBookingInfo.getFirstBookableDate()){
			isFirstBooking = true;
		}else{
			if(hotelBookingInfo.getFirstBookableDate().getTime() < DateUtil.getDate(ableSaleDate).getTime()){
				isFirstBooking = true;
			}else if(DateUtil.getDate(ableSaleDate).getTime() == hotelBookingInfo.getFirstBookableDate().getTime()){
				if(hotelBookingInfo.getFirstBookableTime().contains(":")){
					String[] splitHoursSecond = hotelBookingInfo.getFirstBookableTime().split(":");
					if(Integer.valueOf(splitHoursSecond[0])< today.getHours()){
						isFirstBooking = true;
					}else if(Integer.valueOf(splitHoursSecond[0]) == today.getHours()){
						if(Integer.valueOf(splitHoursSecond[1]) < today.getSeconds()){
							isFirstBooking = true;
						}
					}
				}
				
			}
		}
		//ableSaleDate如果已经不满足最早可预订日期，无需做是否满足最晚预订日期的逻辑判断
		if(isFirstBooking){
			if(null == hotelBookingInfo.getLatestBookableDate()){
				isLatestBooking = true;
			}else{
				if(hotelBookingInfo.getLatestBookableDate().getTime() < DateUtil.getDate(ableSaleDate).getTime()){
					isLatestBooking = true;
				}else if(DateUtil.getDate(ableSaleDate).getTime() == hotelBookingInfo.getLatestBookableDate().getTime()){
					if(hotelBookingInfo.getLatestBokableTime().contains(":")){
						String[] splitHoursSecond = hotelBookingInfo.getLatestBokableTime().split(":");
						if(Integer.valueOf(splitHoursSecond[0]) < today.getHours()){
							isLatestBooking = true;
						}else if(Integer.valueOf(splitHoursSecond[0]) == today.getHours()){
							if(Integer.valueOf(splitHoursSecond[1]) < today.getSeconds()){
								isLatestBooking = true;
							}
						}
					}
				}
			}
		
		}
		//两种情况都为true时才为true
		return isFirstBooking && isLatestBooking;
    }
    
    //根据指定商业区按顺序排序：特定业务需求，没规律可以排序，只能写死
    private  Map<String,Integer> makeBusinessAreaMap(String cityCode,Map<String,List<DistrictHotelInfo>> districtHotelInfoByBizZoneMap){
    	Map<String,Integer> areaMap= new HashMap();
    	List<HtlHotelSortByArea>  htlHotelSortByAreaList= queryAllHkHotelDao.queryHtlHotelSortByArea(cityCode);
    	for(HtlHotelSortByArea htlHotelSortByArea : htlHotelSortByAreaList){
    		if(StringUtil.isValidStr(htlHotelSortByArea.getBizZone())){
    			areaMap.put(htlHotelSortByArea.getBizZone(), htlHotelSortByArea.getSortByArea());
    		}else if(StringUtil.isValidStr(htlHotelSortByArea.getZone())){
    			areaMap.put(htlHotelSortByArea.getZone(), htlHotelSortByArea.getSortByArea());
    		}
    	}
    	return areaMap;
    }
    
	public Map<String, List<QHotelInfo>> queryQHotelInfo(String cityCode, String ableSaleDate) {
		// TODO Auto-generated method stub
		List<QHotelInfo> qHotelInfoList = queryAllHkHotelDao.queryQMangocityHotelInfo(cityCode, ableSaleDate);
		Map<String,List<QHotelInfo>> qHotelInfoMap = new HashMap();
		if(qHotelInfoList != null) {
			for(QHotelInfo qhotelInfo : qHotelInfoList) {
				String bizZone = qhotelInfo.getBizZone();
				if(bizZone != null && !"".equals(bizZone)) {
					List<QHotelInfo> qHotelInfoByBusList = qHotelInfoMap.get(bizZone);
					if(qHotelInfoByBusList == null) {
						qHotelInfoByBusList = new ArrayList<QHotelInfo>();
						qHotelInfoByBusList.add(qhotelInfo);
						qHotelInfoMap.put(bizZone, qHotelInfoByBusList);
					}else {
						qHotelInfoByBusList.add(qhotelInfo);
					}
				}
			}
		}
		return qHotelInfoMap;
	}
	
	public void setQueryAllHkHotelDao(QueryAllHkHotelDao queryAllHkHotelDao) {
		this.queryAllHkHotelDao = queryAllHkHotelDao;
	}
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	
}

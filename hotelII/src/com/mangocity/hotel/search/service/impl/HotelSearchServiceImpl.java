package com.mangocity.hotel.search.service.impl;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.mangocity.hotel.base.dao.HtlFavourableDao;
import com.mangocity.hotel.base.persistence.HtlFavourable;
import com.mangocity.hotel.base.persistence.HtlGeoDistance;
import com.mangocity.hotel.base.persistence.HtlGeographicalposition;
import com.mangocity.hotel.base.service.assistant.CalculateDistance;
import com.mangocity.hotel.order.constant.PromoteType;
import com.mangocity.hotel.search.dao.HtlQueryspeedLogDao;
import com.mangocity.hotel.search.log.HtlQueryspeedLog;
import com.mangocity.hotel.search.model.HotelBasicInfo;
import com.mangocity.hotel.search.model.HotelBasicInfoSearchParam;
import com.mangocity.hotel.search.model.QueryCommodityCondition;
import com.mangocity.hotel.search.model.QueryCommodityInfo;
import com.mangocity.hotel.search.model.QueryDynamicCondition;
import com.mangocity.hotel.search.model.QueryHotelCondition;
import com.mangocity.hotel.search.model.SortCondition;
import com.mangocity.hotel.search.service.CommodityInfoService;
import com.mangocity.hotel.search.service.HotelBasicInfoSearchService;
import com.mangocity.hotel.search.service.HotelInfoCombinationService;
import com.mangocity.hotel.search.service.HotelSearchByPayMethodService;
import com.mangocity.hotel.search.service.HotelSearchService;
import com.mangocity.hotel.search.service.HotelSearcher;
import com.mangocity.hotel.search.service.HotelSortService;
import com.mangocity.hotel.search.service.IHotelQueryHandler;
import com.mangocity.hotel.search.service.QueryConditionService;
import com.mangocity.hotel.search.service.assistant.HotelInfo;
import com.mangocity.hotel.search.service.assistant.SortResType;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.util.log.MyLog;
public class HotelSearchServiceImpl implements HotelSearchService {

	
	//注入的service
	private HotelSearcher hotelSearcher;
	private QueryConditionService queryConditionService;
	private CommodityInfoService commodityInfoService;
	private HotelInfoCombinationService combinationService;
	private HotelSortService hotelSortService;
	private HotelBasicInfoSearchService basicInfoService;
	private HtlQueryspeedLogDao htlQueryspeedLogDao;
	private HtlFavourableDao htlFavourableDao;
	private HotelSearchByPayMethodService hotelSearchByPayMethodService;
	private static final MyLog log = MyLog.getLogger(HotelSearchServiceImpl.class);
	
	/**
	 * 根据查询条件得到HotelInfo Bean 
	 */
	public void queryHotelsByHandler(QueryHotelCondition queryHotelCondition, IHotelQueryHandler handler){
		//MEMBER_VOUCHER_URL_WEB
		// 是否只查1个酒店的信息
		
		boolean bSearchOneHotel = (null != queryHotelCondition.getHotelId() && 0 < queryHotelCondition.getHotelId().trim().length());
		if(bSearchOneHotel) {
			queryHotelCondition.setCityCode(commodityInfoService
					.getCityCodeByHotelId(Long.valueOf(queryHotelCondition
							.getHotelId().trim())));
		}
		
		/*****************组装静态查询条件 start *********************************/
		try{
		long start_tranparam = System.currentTimeMillis();
		HotelBasicInfoSearchParam hotelBasicInfoSearchParam = queryConditionService.fitQueryStaticCondition(queryHotelCondition);
		long end_tranparam = System.currentTimeMillis();		
		/*****************组装静态查询条件 end *********************************/
		
		/*****************根据酒店查询条件(基本信息)过滤酒店,获取酒店信息start**********************/
		long start1 = System.currentTimeMillis();
		Map<String,HotelBasicInfo> hotelBasicInfos = hotelSearcher.searchHotelBasicInfo(hotelBasicInfoSearchParam);
        long end1 = System.currentTimeMillis();
        //酒店名称不为空时，查不到酒店，过滤掉酒店名称重查
        if(hotelBasicInfos.isEmpty()){
        	if(null != hotelBasicInfoSearchParam.getHotelName() && !"".equals(hotelBasicInfoSearchParam.getHotelName())){
        		hotelBasicInfoSearchParam.setHotelName(null);
        		hotelBasicInfos = hotelSearcher.searchHotelBasicInfo(hotelBasicInfoSearchParam);
        	}
        }
		if(hotelBasicInfos.isEmpty()){return;}
		/*****************根据查询条件(基本信息)过滤酒店,获取酒店信息end**********************/
		String hotelIdList = "";
		
		/*****************根据价格和早餐,支付方式,优惠条件等动态信息过滤酒店start**********************/
		QueryCommodityCondition qcc = queryConditionService
					.fitQueryCommodityCondition(hotelBasicInfoSearchParam,
							hotelBasicInfos);
		long start2 = 0;
		long end2 = 0;
		if (!bSearchOneHotel) {
				QueryDynamicCondition qdc = queryConditionService
						.fitQueryDynamicCondition(hotelBasicInfoSearchParam,
								hotelBasicInfos);
				start2 = System.currentTimeMillis();
				if (null != qdc) {// 动态条件不为空时
					hotelIdList = basicInfoService
							.filterDynamicinfoGetIdLst(qdc);
				} else {
					hotelIdList = queryConditionService
							.fitHotelLstFromHotelInfos(hotelBasicInfos);
				}
				end2 = System.currentTimeMillis();

				if (null == hotelIdList || 0 >= hotelIdList.length()) {
					return;
				}
				
				/*****************根据促销活动类型过滤酒店start**********************/
				hotelIdList=filterHotelPromote(queryHotelCondition,hotelIdList);
		        /*****************根据促销活动类型过滤酒店end**********************/
				
				if (null == hotelIdList || 0 >= hotelIdList.length()) {
					return;
				}
				
			} else {
				hotelIdList = queryHotelCondition.getHotelId().trim();
			}
		/*****************根据价格和早餐等动态信息过滤酒店end**********************/
		
		/*****************酒店排序start**********************/
		SortResType sortRes = null;
		long start_sort = 0;
		long end_sort = 0;
		start_sort = System.currentTimeMillis();
		SortCondition sortcon = new SortCondition();
		MyBeanUtil.copyProperties(sortcon, queryHotelCondition);
		
		//根据用户选择的地标位置设置排序条件
		List<HtlGeographicalposition> geoPosList = null;
		HtlGeographicalposition geoPos = null;
		HtlGeographicalposition geoPosParam = hotelBasicInfoSearchParam.getHtlGeographicalposition();
		if(geoPosParam != null){
			geoPosList = hotelSearcher.searchHotelGeoInfo(geoPosParam);
			//如果有多条地标位置信息则忽略，比如根据名称"前门"查询，会得到多个地标位置
			if(!geoPosList.isEmpty() && geoPosList.size() == 1){
				geoPos = geoPosList.get(0);
				sortcon.setGeoId(geoPos.getID());
			}
		}
		
		sortRes = hotelSortService.sortHotelByCondition(sortcon, hotelIdList);
		if (null == sortRes || 0 >= sortRes.getHotelCount() || "".equals(sortRes.getSortedHotelIdList())) {
			return;
		}

		if(!hotelBasicInfos.isEmpty() && geoPos != null){
			if(geoPos.getLatitude() != null && geoPos.getLongitude() != null){
				String[] hotelIdArray = sortRes.getSortedHotelIdList().split(",");
				for(String hotelId : hotelIdArray){
					HotelBasicInfo hotelBasicInfo = hotelBasicInfos.get(hotelId);
					if(hotelBasicInfo.getLatitude() != null && !hotelBasicInfo.getLatitude().equals("") 
							&& hotelBasicInfo.getLongitude() != null && !hotelBasicInfo.getLongitude().equals("")){
						setHotelGeoDistance2RefPosition(geoPos, hotelBasicInfos.get(hotelId));
					}
				}
			}
        }
		
		end_sort = System.currentTimeMillis();	
		/*****************酒店排序end**********************/
		
		//设置酒店促销类型
		setHotelPromoteType(sortRes.getSortedHotelIdList(),hotelBasicInfos);
		
		/*****************根据酒店ID列表查询商品信息start**********************/
		//Map hotelBasicInfos = new HashMap();
		qcc.setHotelIdLst(sortRes.getSortedHotelIdList());
		long start3 = System.currentTimeMillis();
	    List<QueryCommodityInfo> commodityInfoLst = commodityInfoService.queryCommodityInfo(qcc);
	    //传入渠道号，用于渠道返现控制。
	    qcc.setProjectCode(queryHotelCondition.getProjectCode());
	    //计算优惠情况
	    commodityInfoService.setProviderFavourableToCommodityPerday(qcc,commodityInfoLst);//优惠(送M送N,连住包价,打折)
	    commodityInfoService.setLimitFavourableReturnToCommodityPerday(qcc,commodityInfoLst);//返现
	    commodityInfoService.setHtlFavourableDecreaseToCommodityPerday(qcc,commodityInfoLst);//立减
	    long end3 = System.currentTimeMillis();
		/*****************根据酒店ID列表查询商品信息end**********************/
	    
		/*****************根据酒店基本信息和商品信息封装JO start**********************/
	    //根据排好序的酒店列表去过滤掉无用的酒店，然后再转换
//	    List<HotelInfo> hotelLst = combinationService.combinationHotelInfo(combinationService.filterHotel(hotelBasicInfos,qcc.getHotelIdLst()),commodityInfoLst,qcc);
	    long start4 = System.currentTimeMillis();
	    combinationService.combinationHotelInfoUseHandler(hotelBasicInfos,
					commodityInfoLst, qcc, sortRes.getHotelCount(), handler);
	    long end4 = System.currentTimeMillis();
	    
	    //性能考虑，暂不加日志  modify by chenjiajie 2011-10-01
//	    if(!bSearchOneHotel) {
//		    saveQuerySpeedLog(queryHotelCondition, start_tranparam,
//					end_tranparam, start1, end1, start2, end2, start_sort,
//					end_sort, start3, end3, start4, end4);	
//	    }

	    /*****************根据酒店基本信息和商品信息封装JO end**********************/		
		}catch(Exception e){
			log.error("酒店新查询出错", e);
		}
	    /*****************根据酒店基本信息和商品信息封装JO end**********************/
	}

	/**
	 * 保存查询速度的log数据到DB
	 * @param queryHotelCondition
	 * @param start_tranparam
	 * @param end_tranparam
	 * @param start1
	 * @param end1
	 * @param start2
	 * @param end2
	 * @param start_sort
	 * @param end_sort
	 * @param start3
	 * @param end3
	 * @param start4
	 * @param end4
	 * @throws UnknownHostException
	 */
	@SuppressWarnings("unused")
	private void saveQuerySpeedLog(QueryHotelCondition queryHotelCondition,
			long start_tranparam, long end_tranparam, long start1, long end1,
			long start2, long end2, long start_sort, long end_sort,
			long start3, long end3, long start4, long end4)
			throws UnknownHostException {
		StringBuffer sb = new StringBuffer();
		sb.append("查询条件："+"\n");
		sb.append("城市:"+queryHotelCondition.getCityCode()+"\n");
		sb.append("入住日期:"+queryHotelCondition.getInDate()+"\n");
		sb.append("离店日期:"+queryHotelCondition.getOutDate()+"\n");
		sb.append("酒店名称:"+queryHotelCondition.getHotelName()+"\n");
		sb.append("最低价:"+queryHotelCondition.getMinPrice()+"\n");
		sb.append("最高价:"+queryHotelCondition.getMaxPrice()+"\n");
		sb.append("星级:"+queryHotelCondition.getStarLeval()+"\n");
		sb.append("特殊要求:"+queryHotelCondition.getSpecialRequestString()+"\n");
		sb.append("商区:"+queryHotelCondition.getBizZone()+"\n");
		sb.append("行政区:"+queryHotelCondition.getDistrict()+"\n");
		String querycondtionString = sb.toString();
		sb.append("组装用时："+(end_tranparam - start_tranparam)+"ms.\n");
		sb.append("lucene查询用时:"+(end1-start1)+"ms.\n");
		sb.append("早餐和价格查询用时:"+(end2-start2)+"ms.\n");
		sb.append("酒店排序用时:"+(end_sort-start_sort)+"ms.\n");
		sb.append("商品查询用时:"+(end3-start3)+"ms.\n");
		sb.append("handler组装用时:"+(end4-start4)+"ms.\n");
		sb.append("总查询用时:"+(end4-start_tranparam)+"ms.\n");
		log.info(sb.toString());
		// 日志记录
		HtlQueryspeedLog htlqueryLog = new HtlQueryspeedLog();
		htlqueryLog.setDays(DateUtil.getDay(queryHotelCondition.getInDate(), queryHotelCondition.getOutDate()));
		htlqueryLog.setCity(queryHotelCondition.getCityCode());
		htlqueryLog.setQuerycondition(querycondtionString);
		htlqueryLog.setLucene_times(end1-start1);
		htlqueryLog.setPricebreakfast_times(end2-start2);
		htlqueryLog.setSort_times(end_sort-start_sort);
		htlqueryLog.setCommodityquery_times(end3-start3);
		htlqueryLog.setHandler_times(end4-start4);
		htlqueryLog.setAlltimes(end4-start_tranparam);
		htlqueryLog.setWebip(java.net.InetAddress.getLocalHost().getHostAddress());
		htlQueryspeedLogDao.save(htlqueryLog);
	}
	
	//查询酒店并组装为对应的VO add by diandian.hou
	public void queryOnlyHotelsByHandler(QueryHotelCondition queryHotelCondition,IHotelQueryHandler handler){
		try{
		HotelBasicInfoSearchParam hotelBasicInfoSearchParam = queryConditionService.fitQueryStaticCondition(queryHotelCondition);
		/*****************根据酒店查询条件(基本信息)过滤酒店,获取酒店信息start**********************/
		long start1 = System.currentTimeMillis();
		Map<String,HotelBasicInfo> hotelBasicInfos = hotelSearcher.searchHotelBasicInfo(hotelBasicInfoSearchParam);
		long end1 = System.currentTimeMillis();
        //酒店名称不为空时，查不到酒店，过滤掉酒店名称重查
		/*
        if(hotelBasicInfos.isEmpty()){
        	if(null != hotelBasicInfoSearchParam.getHotelName() && !"".equals(hotelBasicInfoSearchParam.getHotelName())){
        		hotelBasicInfoSearchParam.setHotelName(null);
        		hotelBasicInfos = hotelSearcher.searchHotelBasicInfo(hotelBasicInfoSearchParam);
        	}
        }
        */
		if(hotelBasicInfos.isEmpty()){return;}
		
		 //只显示预付酒店
		Map<String,HotelBasicInfo> prepayHotelBasicInfo;
		if(PayMethod.PRE_PAY.equals(queryHotelCondition.getPayMethod())){
			prepayHotelBasicInfo=showOnlyPrePayHotel(hotelBasicInfos,queryHotelCondition);
			hotelBasicInfos=prepayHotelBasicInfo;
		}
		
		/*****************根据查询条件(基本信息)过滤酒店,获取酒店信息end**********************/
        String hotelIdList = "";
		/*****************根据价格和早餐,支付方式,优惠条件等动态信息过滤酒店start**********************/
		QueryDynamicCondition qdc = queryConditionService.fitQueryDynamicCondition(hotelBasicInfoSearchParam,hotelBasicInfos);
		if (null != qdc) {// 动态条件不为空时
			hotelIdList = basicInfoService.filterDynamicinfoGetIdLst(qdc);
		} else {
			hotelIdList = queryConditionService.fitHotelLstFromHotelInfos(hotelBasicInfos);
		}
		
		if (null == hotelIdList || 0 >= hotelIdList.length()) {
			return;
		}
		
		/*****************根据促销活动类型过滤酒店start**********************/
		hotelIdList=filterHotelPromote(queryHotelCondition,hotelIdList);
        /*****************根据促销活动类型过滤酒店end**********************/
		
		if (null == hotelIdList || 0 >= hotelIdList.length()) {
			return;
		}
        
		/*****************酒店排序start**********************/
		SortResType sortRes = null;
		SortCondition sortcon = new SortCondition();
		MyBeanUtil.copyProperties(sortcon, queryHotelCondition);
		
		//根据用户选择的地标位置设置排序条件
		List<HtlGeographicalposition> geoPosList = null;
		HtlGeographicalposition geoPos = null;
		HtlGeographicalposition geoPosParam = hotelBasicInfoSearchParam.getHtlGeographicalposition();
		if(geoPosParam != null){
			geoPosList = hotelSearcher.searchHotelGeoInfo(geoPosParam);
			//如果有多条地标位置信息则忽略，比如根据名称"前门"查询，会得到多个地标位置
			if(!geoPosList.isEmpty() && geoPosList.size() == 1){
				geoPos = geoPosList.get(0);
				sortcon.setGeoId(geoPos.getID());
			}
			//添加多条信息的处理 add by diandian.hou 2011-09-05
			if (!geoPosList.isEmpty() && geoPosList.size() > 1) {
				String geoName = queryHotelCondition.getGeoName();
				for (int i = 0; i < geoPosList.size(); i++) {
					if (geoName.equals(geoPosList.get(i).getName())) {
						geoPos = geoPosList.get(0);
						sortcon.setGeoId(geoPos.getID());
						break;
					}
				}
			}
		}
		
		long start_sort = System.currentTimeMillis();
		sortRes = hotelSortService.sortHotelByCondition(sortcon, hotelIdList);
        long end_sort = System.currentTimeMillis();
		if (null == sortRes || 0 >= sortRes.getHotelCount() || "".equals(sortRes.getSortedHotelIdList())) {
			return;
		}
		
		if(!hotelBasicInfos.isEmpty() && geoPos != null){
			if(geoPos.getLatitude() != null && geoPos.getLongitude() != null){
				String[] hotelIdArray = sortRes.getSortedHotelIdList().split(",");
				for(String hotelId : hotelIdArray){
					HotelBasicInfo hotelBasicInfo = hotelBasicInfos.get(hotelId);
					if(hotelBasicInfo.getLatitude() != null && !hotelBasicInfo.getLatitude().equals("") 
							&& hotelBasicInfo.getLongitude() != null && !hotelBasicInfo.getLongitude().equals("")){
						setHotelGeoDistance2RefPosition(geoPos, hotelBasicInfos.get(hotelId));
					}
				}
			}
        }
		
		//设置酒店促销类型
		setHotelPromoteType(sortRes.getSortedHotelIdList(),hotelBasicInfos);
		
		combinationService.fitHotelInfoWithHandler(hotelBasicInfos,queryHotelCondition,sortRes,handler);
		}catch(Exception e){
			log.error("新查询酒店基本信息和排序 error：",e);
		}
	}
	
	//查询酒店全部信息并组装对应的VO add by diandian.hou
	public void queryHotelsByHandler(String hotelIdsStr,QueryHotelCondition queryHotelCondition,IHotelQueryHandler handler){
		HotelBasicInfoSearchParam hotelBasicInfoSearchParam = new HotelBasicInfoSearchParam();
		hotelBasicInfoSearchParam.setCityCode(queryHotelCondition.getCityCode());
		hotelBasicInfoSearchParam.setHotelIdsStr(hotelIdsStr);
		try{
		long time1=System.currentTimeMillis(); 
		Map<String,HotelBasicInfo> hotelBasicInfos = hotelSearcher.searchHotelBasicInfo(hotelBasicInfoSearchParam);
				
		if(hotelBasicInfos.isEmpty()){return;}
		
		setPrepayHotel(hotelBasicInfos,hotelIdsStr,queryHotelCondition);
		
		QueryCommodityCondition qcc = new QueryCommodityCondition();
		qcc.setFromChannel(queryHotelCondition.getFromChannel());
		qcc.setCityCode(queryHotelCondition.getCityCode());
		qcc.setHotelIdLst(hotelIdsStr);
		qcc.setInDate(queryHotelCondition.getInDate());
		qcc.setOutDate(queryHotelCondition.getOutDate());
		qcc.setPayMethod(queryHotelCondition.getPayMethod());
		
		//酒店来源渠道，用于返现控制 add by hushunqiang
		qcc.setProjectCode(queryHotelCondition.getProjectCode());
		
		List<QueryCommodityInfo> commodityInfoLst = commodityInfoService.queryCommodityInfo(qcc);
	    //计算优惠情况
	    commodityInfoService.setProviderFavourableToCommodityPerday(qcc,commodityInfoLst);//优惠(送M送N,连住包价,打折)
	    commodityInfoService.setLimitFavourableReturnToCommodityPerday(qcc,commodityInfoLst);//返现
	    commodityInfoService.setHtlFavourableDecreaseToCommodityPerday(qcc,commodityInfoLst);//立减
	    
	    combinationService.combinationHotelInfoUseHandler(hotelBasicInfos,commodityInfoLst, qcc,0, handler);
	    long time2=System.currentTimeMillis();
	    log.info("查询商品用时："+(time2-time1));
		}catch(Exception e){
			log.error("酒店新查询出错", e);
		}
		
		
	}
	
	
	private void setPrepayHotel(Map<String,HotelBasicInfo> hotelBasicInfos,String hotelIdsStr,QueryHotelCondition queryHotelCondition){
		//List<Long> prePayHotelIds=hotelSearchByPayMethodService.queryHotelIdByMethod(hotelBasicInfos, queryHotelCondition, PayMethod.PRE_PAY);
		hotelSearchByPayMethodService.setPrepayHotel(hotelBasicInfos,hotelIdsStr, queryHotelCondition, PayMethod.PRE_PAY);
	}
	private Map<String,HotelBasicInfo> showOnlyPrePayHotel(Map<String,HotelBasicInfo> hotelBasicInfos,QueryHotelCondition queryHotelCondition){
		List<Long> prePayHotelIds=hotelSearchByPayMethodService.queryHotelIdByMethod(hotelBasicInfos, queryHotelCondition, PayMethod.PRE_PAY);
		Map<String,HotelBasicInfo> prePayHotelBasicInfos=hotelSearchByPayMethodService.showHotelByPayMethod(hotelBasicInfos, prePayHotelIds);
		return prePayHotelBasicInfos;
	}

	

	/**
	 * 设置某个地标位置到酒店的距离信息
	 * @param geoPos			地标的地理信息
	 * @param hotelBasicInfo	酒店基本信息（包含有坐标信息）
	 */
	private void setHotelGeoDistance2RefPosition(HtlGeographicalposition geoPos, HotelBasicInfo hotelBasicInfo) {
		HtlGeoDistance geoDistance = new HtlGeoDistance();
		geoDistance.setCityCode(geoPos.getCityCode());
		geoDistance.setGeoId(geoPos.getID());
		geoDistance.setGeoType(geoPos.getGptypeId());
		geoDistance.setName(geoPos.getName());
		geoDistance.setHotelId(hotelBasicInfo.getHotelId());
		
		double distance = CalculateDistance.getDistance(geoPos.getLatitude().doubleValue(), 
				geoPos.getLongitude().doubleValue(), Double.valueOf(hotelBasicInfo.getLatitude()), 
				Double.valueOf(hotelBasicInfo.getLongitude()), 'K');					
		geoDistance.setDistance(Math.round(distance/10)/100.0);
		
		hotelBasicInfo.setHtlGeoDistance(geoDistance);
	}
	
	/**
	 * 设置酒店促销类型
	 */
	private void setHotelPromoteType(String hotelIdsStr,Map<String,HotelBasicInfo> hotelBasicInfos){
		if(!StringUtil.isValidStr(hotelIdsStr)) return;
		//查询酒店的促销类型
		List<HtlFavourable> htlFavourables=htlFavourableDao.queryHtlFavourableByHotelId(hotelIdsStr);
		for(HtlFavourable fav:htlFavourables){
			int promoteType=1;
			if(fav.getFavA()==1){
				promoteType=PromoteType.Five;
			}else if(fav.getFavA()!=1 && fav.getFavB()==1){
				promoteType=PromoteType.Seven;
			}else if(fav.getFavA()!=1 && fav.getFavB()!=1 && fav.getFavC()==1){
				promoteType=PromoteType.Zone;
			}
			HotelBasicInfo hotel=hotelBasicInfos.get(fav.getHotelId().toString());
			if(hotel != null){
				hotel.setPromoteType(promoteType);
			}
		}
	}
	
	/**
	 * 将没有参加活动的酒店过滤掉
	 */
	private String filterHotelPromote(QueryHotelCondition queryHotelCondition,String hotelIdList){
		if(queryHotelCondition.getPromoteType()!= PromoteType.All){
			List<HtlFavourable> htlFavourables=htlFavourableDao.queryHtlFavourableByType(queryHotelCondition.getCityCode(), queryHotelCondition.getPromoteType());
	        if(htlFavourables==null) return hotelIdList;
	        
	        List<String> favHotelIds=new ArrayList<String>();
	        for(HtlFavourable fav:htlFavourables){
	        	favHotelIds.add(fav.getHotelId().toString());
	        }
	        List<String> oldHotelIdList=Arrays.asList(hotelIdList.split(","));
	        //得到两个list的交售
	        Collection intersection=CollectionUtils.intersection(favHotelIds, oldHotelIdList);
	        if(intersection==null || intersection.size()==0)return null;
	        hotelIdList=StringUtil.deleteLastChar(StringUtil.describe(intersection.toArray()).trim(), ',');
		}
		return hotelIdList;
	}
	
	/**
	 * 根据过滤条件过滤酒店
	 * 根据是否优惠,支付方式,销量等动态信息过滤查询结果
	 * @return
	 */
	public List<HotelInfo> filterHotel(QueryHotelCondition queryHotelCondition){
		//to-do
		return null;
	}
	
	/**
	 * 根据指定的酒店ID获取酒店信息
	 * @param idLst
	 * @return
	 */
	public List<HotelInfo> queryHotelsByIdLst(String idLst) {
		return Collections.emptyList();
	}
	
	/**
	 * 根据酒店ID查询酒店的详细信息
	 */
	public HotelInfo queryDetailHotelInfoById(long hotelId) {
		return new HotelInfo();
	}
	
	/**
	 * 根据地理位置信息获取酒店列表
	 * @param mgisId 地理位置信息ID,如:世界之窗的GisId
	 * @param distance 距离,单位:公里.如:3,就代表3公里之内
	 * @return
	 */
	public List<HotelInfo> searchHotelByMgisInfo(long mgisId, int distance) {
		return Collections.emptyList();
	}
	
	/**
	 * 根据过滤条件过滤酒店(传统方式)
	 * @return
	 */
	public List<HotelInfo> queryHotelsByIO(
			QueryHotelCondition queryHotelCondition) {
		return Collections.emptyList();
	}
	

	public void setQueryConditionService(QueryConditionService queryConditionService) {
		this.queryConditionService = queryConditionService;
	}

	public void setCommodityInfoService(CommodityInfoService commodityInfoService) {
		this.commodityInfoService = commodityInfoService;
	}

	public void setCombinationService(HotelInfoCombinationService combinationService) {
		this.combinationService = combinationService;
	}

	public void setHotelSortService(HotelSortService hotelSortService) {
		this.hotelSortService = hotelSortService;
	}

	public void setBasicInfoService(HotelBasicInfoSearchService basicInfoService) {
		this.basicInfoService = basicInfoService;
	}

	public HotelSearcher getHotelSearcher() {
		return hotelSearcher;
	}

	public void setHotelSearcher(HotelSearcher hotelSearcher) {
		this.hotelSearcher = hotelSearcher;
	}

	public void setHtlQueryspeedLogDao(HtlQueryspeedLogDao htlQueryspeedLogDao) {
		this.htlQueryspeedLogDao = htlQueryspeedLogDao;
	}

	public void setHotelSearchByPayMethodService(HotelSearchByPayMethodService hotelSearchByPayMethodService) {
		this.hotelSearchByPayMethodService = hotelSearchByPayMethodService;
	}

	public void setHtlFavourableDao(HtlFavourableDao htlFavourableDao) {
		this.htlFavourableDao = htlFavourableDao;
	}
	
}

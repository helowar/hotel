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

	
	//ע���service
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
	 * ���ݲ�ѯ�����õ�HotelInfo Bean 
	 */
	public void queryHotelsByHandler(QueryHotelCondition queryHotelCondition, IHotelQueryHandler handler){
		//MEMBER_VOUCHER_URL_WEB
		// �Ƿ�ֻ��1���Ƶ����Ϣ
		
		boolean bSearchOneHotel = (null != queryHotelCondition.getHotelId() && 0 < queryHotelCondition.getHotelId().trim().length());
		if(bSearchOneHotel) {
			queryHotelCondition.setCityCode(commodityInfoService
					.getCityCodeByHotelId(Long.valueOf(queryHotelCondition
							.getHotelId().trim())));
		}
		
		/*****************��װ��̬��ѯ���� start *********************************/
		try{
		long start_tranparam = System.currentTimeMillis();
		HotelBasicInfoSearchParam hotelBasicInfoSearchParam = queryConditionService.fitQueryStaticCondition(queryHotelCondition);
		long end_tranparam = System.currentTimeMillis();		
		/*****************��װ��̬��ѯ���� end *********************************/
		
		/*****************���ݾƵ��ѯ����(������Ϣ)���˾Ƶ�,��ȡ�Ƶ���Ϣstart**********************/
		long start1 = System.currentTimeMillis();
		Map<String,HotelBasicInfo> hotelBasicInfos = hotelSearcher.searchHotelBasicInfo(hotelBasicInfoSearchParam);
        long end1 = System.currentTimeMillis();
        //�Ƶ����Ʋ�Ϊ��ʱ���鲻���Ƶ꣬���˵��Ƶ������ز�
        if(hotelBasicInfos.isEmpty()){
        	if(null != hotelBasicInfoSearchParam.getHotelName() && !"".equals(hotelBasicInfoSearchParam.getHotelName())){
        		hotelBasicInfoSearchParam.setHotelName(null);
        		hotelBasicInfos = hotelSearcher.searchHotelBasicInfo(hotelBasicInfoSearchParam);
        	}
        }
		if(hotelBasicInfos.isEmpty()){return;}
		/*****************���ݲ�ѯ����(������Ϣ)���˾Ƶ�,��ȡ�Ƶ���Ϣend**********************/
		String hotelIdList = "";
		
		/*****************���ݼ۸�����,֧����ʽ,�Ż������ȶ�̬��Ϣ���˾Ƶ�start**********************/
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
				if (null != qdc) {// ��̬������Ϊ��ʱ
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
				
				/*****************���ݴ�������͹��˾Ƶ�start**********************/
				hotelIdList=filterHotelPromote(queryHotelCondition,hotelIdList);
		        /*****************���ݴ�������͹��˾Ƶ�end**********************/
				
				if (null == hotelIdList || 0 >= hotelIdList.length()) {
					return;
				}
				
			} else {
				hotelIdList = queryHotelCondition.getHotelId().trim();
			}
		/*****************���ݼ۸����͵ȶ�̬��Ϣ���˾Ƶ�end**********************/
		
		/*****************�Ƶ�����start**********************/
		SortResType sortRes = null;
		long start_sort = 0;
		long end_sort = 0;
		start_sort = System.currentTimeMillis();
		SortCondition sortcon = new SortCondition();
		MyBeanUtil.copyProperties(sortcon, queryHotelCondition);
		
		//�����û�ѡ��ĵر�λ��������������
		List<HtlGeographicalposition> geoPosList = null;
		HtlGeographicalposition geoPos = null;
		HtlGeographicalposition geoPosParam = hotelBasicInfoSearchParam.getHtlGeographicalposition();
		if(geoPosParam != null){
			geoPosList = hotelSearcher.searchHotelGeoInfo(geoPosParam);
			//����ж����ر�λ����Ϣ����ԣ������������"ǰ��"��ѯ����õ�����ر�λ��
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
		/*****************�Ƶ�����end**********************/
		
		//���þƵ��������
		setHotelPromoteType(sortRes.getSortedHotelIdList(),hotelBasicInfos);
		
		/*****************���ݾƵ�ID�б��ѯ��Ʒ��Ϣstart**********************/
		//Map hotelBasicInfos = new HashMap();
		qcc.setHotelIdLst(sortRes.getSortedHotelIdList());
		long start3 = System.currentTimeMillis();
	    List<QueryCommodityInfo> commodityInfoLst = commodityInfoService.queryCommodityInfo(qcc);
	    //���������ţ������������ֿ��ơ�
	    qcc.setProjectCode(queryHotelCondition.getProjectCode());
	    //�����Ż����
	    commodityInfoService.setProviderFavourableToCommodityPerday(qcc,commodityInfoLst);//�Ż�(��M��N,��ס����,����)
	    commodityInfoService.setLimitFavourableReturnToCommodityPerday(qcc,commodityInfoLst);//����
	    commodityInfoService.setHtlFavourableDecreaseToCommodityPerday(qcc,commodityInfoLst);//����
	    long end3 = System.currentTimeMillis();
		/*****************���ݾƵ�ID�б��ѯ��Ʒ��Ϣend**********************/
	    
		/*****************���ݾƵ������Ϣ����Ʒ��Ϣ��װJO start**********************/
	    //�����ź���ľƵ��б�ȥ���˵����õľƵ꣬Ȼ����ת��
//	    List<HotelInfo> hotelLst = combinationService.combinationHotelInfo(combinationService.filterHotel(hotelBasicInfos,qcc.getHotelIdLst()),commodityInfoLst,qcc);
	    long start4 = System.currentTimeMillis();
	    combinationService.combinationHotelInfoUseHandler(hotelBasicInfos,
					commodityInfoLst, qcc, sortRes.getHotelCount(), handler);
	    long end4 = System.currentTimeMillis();
	    
	    //���ܿ��ǣ��ݲ�����־  modify by chenjiajie 2011-10-01
//	    if(!bSearchOneHotel) {
//		    saveQuerySpeedLog(queryHotelCondition, start_tranparam,
//					end_tranparam, start1, end1, start2, end2, start_sort,
//					end_sort, start3, end3, start4, end4);	
//	    }

	    /*****************���ݾƵ������Ϣ����Ʒ��Ϣ��װJO end**********************/		
		}catch(Exception e){
			log.error("�Ƶ��²�ѯ����", e);
		}
	    /*****************���ݾƵ������Ϣ����Ʒ��Ϣ��װJO end**********************/
	}

	/**
	 * �����ѯ�ٶȵ�log���ݵ�DB
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
		sb.append("��ѯ������"+"\n");
		sb.append("����:"+queryHotelCondition.getCityCode()+"\n");
		sb.append("��ס����:"+queryHotelCondition.getInDate()+"\n");
		sb.append("�������:"+queryHotelCondition.getOutDate()+"\n");
		sb.append("�Ƶ�����:"+queryHotelCondition.getHotelName()+"\n");
		sb.append("��ͼ�:"+queryHotelCondition.getMinPrice()+"\n");
		sb.append("��߼�:"+queryHotelCondition.getMaxPrice()+"\n");
		sb.append("�Ǽ�:"+queryHotelCondition.getStarLeval()+"\n");
		sb.append("����Ҫ��:"+queryHotelCondition.getSpecialRequestString()+"\n");
		sb.append("����:"+queryHotelCondition.getBizZone()+"\n");
		sb.append("������:"+queryHotelCondition.getDistrict()+"\n");
		String querycondtionString = sb.toString();
		sb.append("��װ��ʱ��"+(end_tranparam - start_tranparam)+"ms.\n");
		sb.append("lucene��ѯ��ʱ:"+(end1-start1)+"ms.\n");
		sb.append("��ͺͼ۸��ѯ��ʱ:"+(end2-start2)+"ms.\n");
		sb.append("�Ƶ�������ʱ:"+(end_sort-start_sort)+"ms.\n");
		sb.append("��Ʒ��ѯ��ʱ:"+(end3-start3)+"ms.\n");
		sb.append("handler��װ��ʱ:"+(end4-start4)+"ms.\n");
		sb.append("�ܲ�ѯ��ʱ:"+(end4-start_tranparam)+"ms.\n");
		log.info(sb.toString());
		// ��־��¼
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
	
	//��ѯ�Ƶ겢��װΪ��Ӧ��VO add by diandian.hou
	public void queryOnlyHotelsByHandler(QueryHotelCondition queryHotelCondition,IHotelQueryHandler handler){
		try{
		HotelBasicInfoSearchParam hotelBasicInfoSearchParam = queryConditionService.fitQueryStaticCondition(queryHotelCondition);
		/*****************���ݾƵ��ѯ����(������Ϣ)���˾Ƶ�,��ȡ�Ƶ���Ϣstart**********************/
		long start1 = System.currentTimeMillis();
		Map<String,HotelBasicInfo> hotelBasicInfos = hotelSearcher.searchHotelBasicInfo(hotelBasicInfoSearchParam);
		long end1 = System.currentTimeMillis();
        //�Ƶ����Ʋ�Ϊ��ʱ���鲻���Ƶ꣬���˵��Ƶ������ز�
		/*
        if(hotelBasicInfos.isEmpty()){
        	if(null != hotelBasicInfoSearchParam.getHotelName() && !"".equals(hotelBasicInfoSearchParam.getHotelName())){
        		hotelBasicInfoSearchParam.setHotelName(null);
        		hotelBasicInfos = hotelSearcher.searchHotelBasicInfo(hotelBasicInfoSearchParam);
        	}
        }
        */
		if(hotelBasicInfos.isEmpty()){return;}
		
		 //ֻ��ʾԤ���Ƶ�
		Map<String,HotelBasicInfo> prepayHotelBasicInfo;
		if(PayMethod.PRE_PAY.equals(queryHotelCondition.getPayMethod())){
			prepayHotelBasicInfo=showOnlyPrePayHotel(hotelBasicInfos,queryHotelCondition);
			hotelBasicInfos=prepayHotelBasicInfo;
		}
		
		/*****************���ݲ�ѯ����(������Ϣ)���˾Ƶ�,��ȡ�Ƶ���Ϣend**********************/
        String hotelIdList = "";
		/*****************���ݼ۸�����,֧����ʽ,�Ż������ȶ�̬��Ϣ���˾Ƶ�start**********************/
		QueryDynamicCondition qdc = queryConditionService.fitQueryDynamicCondition(hotelBasicInfoSearchParam,hotelBasicInfos);
		if (null != qdc) {// ��̬������Ϊ��ʱ
			hotelIdList = basicInfoService.filterDynamicinfoGetIdLst(qdc);
		} else {
			hotelIdList = queryConditionService.fitHotelLstFromHotelInfos(hotelBasicInfos);
		}
		
		if (null == hotelIdList || 0 >= hotelIdList.length()) {
			return;
		}
		
		/*****************���ݴ�������͹��˾Ƶ�start**********************/
		hotelIdList=filterHotelPromote(queryHotelCondition,hotelIdList);
        /*****************���ݴ�������͹��˾Ƶ�end**********************/
		
		if (null == hotelIdList || 0 >= hotelIdList.length()) {
			return;
		}
        
		/*****************�Ƶ�����start**********************/
		SortResType sortRes = null;
		SortCondition sortcon = new SortCondition();
		MyBeanUtil.copyProperties(sortcon, queryHotelCondition);
		
		//�����û�ѡ��ĵر�λ��������������
		List<HtlGeographicalposition> geoPosList = null;
		HtlGeographicalposition geoPos = null;
		HtlGeographicalposition geoPosParam = hotelBasicInfoSearchParam.getHtlGeographicalposition();
		if(geoPosParam != null){
			geoPosList = hotelSearcher.searchHotelGeoInfo(geoPosParam);
			//����ж����ر�λ����Ϣ����ԣ������������"ǰ��"��ѯ����õ�����ر�λ��
			if(!geoPosList.isEmpty() && geoPosList.size() == 1){
				geoPos = geoPosList.get(0);
				sortcon.setGeoId(geoPos.getID());
			}
			//��Ӷ�����Ϣ�Ĵ��� add by diandian.hou 2011-09-05
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
		
		//���þƵ��������
		setHotelPromoteType(sortRes.getSortedHotelIdList(),hotelBasicInfos);
		
		combinationService.fitHotelInfoWithHandler(hotelBasicInfos,queryHotelCondition,sortRes,handler);
		}catch(Exception e){
			log.error("�²�ѯ�Ƶ������Ϣ������ error��",e);
		}
	}
	
	//��ѯ�Ƶ�ȫ����Ϣ����װ��Ӧ��VO add by diandian.hou
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
		
		//�Ƶ���Դ���������ڷ��ֿ��� add by hushunqiang
		qcc.setProjectCode(queryHotelCondition.getProjectCode());
		
		List<QueryCommodityInfo> commodityInfoLst = commodityInfoService.queryCommodityInfo(qcc);
	    //�����Ż����
	    commodityInfoService.setProviderFavourableToCommodityPerday(qcc,commodityInfoLst);//�Ż�(��M��N,��ס����,����)
	    commodityInfoService.setLimitFavourableReturnToCommodityPerday(qcc,commodityInfoLst);//����
	    commodityInfoService.setHtlFavourableDecreaseToCommodityPerday(qcc,commodityInfoLst);//����
	    
	    combinationService.combinationHotelInfoUseHandler(hotelBasicInfos,commodityInfoLst, qcc,0, handler);
	    long time2=System.currentTimeMillis();
	    log.info("��ѯ��Ʒ��ʱ��"+(time2-time1));
		}catch(Exception e){
			log.error("�Ƶ��²�ѯ����", e);
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
	 * ����ĳ���ر�λ�õ��Ƶ�ľ�����Ϣ
	 * @param geoPos			�ر�ĵ�����Ϣ
	 * @param hotelBasicInfo	�Ƶ������Ϣ��������������Ϣ��
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
	 * ���þƵ��������
	 */
	private void setHotelPromoteType(String hotelIdsStr,Map<String,HotelBasicInfo> hotelBasicInfos){
		if(!StringUtil.isValidStr(hotelIdsStr)) return;
		//��ѯ�Ƶ�Ĵ�������
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
	 * ��û�вμӻ�ľƵ���˵�
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
	        //�õ�����list�Ľ���
	        Collection intersection=CollectionUtils.intersection(favHotelIds, oldHotelIdList);
	        if(intersection==null || intersection.size()==0)return null;
	        hotelIdList=StringUtil.deleteLastChar(StringUtil.describe(intersection.toArray()).trim(), ',');
		}
		return hotelIdList;
	}
	
	/**
	 * ���ݹ����������˾Ƶ�
	 * �����Ƿ��Ż�,֧����ʽ,�����ȶ�̬��Ϣ���˲�ѯ���
	 * @return
	 */
	public List<HotelInfo> filterHotel(QueryHotelCondition queryHotelCondition){
		//to-do
		return null;
	}
	
	/**
	 * ����ָ���ľƵ�ID��ȡ�Ƶ���Ϣ
	 * @param idLst
	 * @return
	 */
	public List<HotelInfo> queryHotelsByIdLst(String idLst) {
		return Collections.emptyList();
	}
	
	/**
	 * ���ݾƵ�ID��ѯ�Ƶ����ϸ��Ϣ
	 */
	public HotelInfo queryDetailHotelInfoById(long hotelId) {
		return new HotelInfo();
	}
	
	/**
	 * ���ݵ���λ����Ϣ��ȡ�Ƶ��б�
	 * @param mgisId ����λ����ϢID,��:����֮����GisId
	 * @param distance ����,��λ:����.��:3,�ʹ���3����֮��
	 * @return
	 */
	public List<HotelInfo> searchHotelByMgisInfo(long mgisId, int distance) {
		return Collections.emptyList();
	}
	
	/**
	 * ���ݹ����������˾Ƶ�(��ͳ��ʽ)
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

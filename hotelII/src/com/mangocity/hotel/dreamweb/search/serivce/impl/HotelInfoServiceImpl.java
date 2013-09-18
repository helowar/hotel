package com.mangocity.hotel.dreamweb.search.serivce.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mangocity.hotel.base.dao.HtlFavourableDao;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlFavourable;
import com.mangocity.hotel.base.persistence.HtlGeographicalposition;
import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.hotel.base.service.SystemDataService;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hotel.dreamweb.comment.dao.CommentStatisticsDao;
import com.mangocity.hotel.dreamweb.comment.model.CommentStatistics;
import com.mangocity.hotel.dreamweb.displayvo.HtlAlbumVO;
import com.mangocity.hotel.dreamweb.search.service.HotelInfoService;
import com.mangocity.hotel.order.constant.PromoteType;
import com.mangocity.hotel.search.dao.HotelPictureInfoDao;
import com.mangocity.hotel.search.dao.HotelQueryDao;
import com.mangocity.hotel.search.handler.impl.HotelQueryHandler;
import com.mangocity.hotel.search.model.HotelBasicInfo;
import com.mangocity.hotel.search.model.HotelBasicInfoSearchParam;
import com.mangocity.hotel.search.service.HotelSearcher;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.hweb.persistence.HwClickAmount;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.hotel.constant.PayMethod;

public class HotelInfoServiceImpl implements HotelInfoService{
	private Log log=LogFactory.getLog(HotelInfoServiceImpl.class);
	private HotelSearcher hotelSearcher;
	private HotelManage hotelManage;
	private HotelManageWeb hotelManageWeb;
	private CommentStatisticsDao commentStatisticsDao;
	private HotelQueryDao hotelQueryDao;
	private HtlFavourableDao htlFavourableDao;
	private HotelPictureInfoDao hotelPictureInfoDao;
	public void setHtlFavourableDao(HtlFavourableDao htlFavourableDao) {
		this.htlFavourableDao = htlFavourableDao;
	}


	public HotelBasicInfo queryHotelInfoByHotelId(String hotelId) {
		HotelBasicInfoSearchParam hotelBasicInfoSearchParam=new HotelBasicInfoSearchParam();
		hotelBasicInfoSearchParam.setHotelId(hotelId);
		log.info("HotelInfoServiceImpl queryHotelInfoByHotelId hotelId is: " + hotelId);
		Map<String, HotelBasicInfo> hotelInfoMap = hotelSearcher.searchHotelBasicInfo(hotelBasicInfoSearchParam);
		HotelBasicInfo hotelInfo = hotelInfoMap.get(hotelId);
		if (hotelInfo != null) {
			hotelInfo.setZoneName(InitServlet.citySozeObj.get(hotelInfo.getZone()));
		}
		return hotelInfo;
	}
	

	public HotelBasicInfo queryHotelInfo(HotelBasicInfoSearchParam hotelBasicInfoSearchParam, String ipAddress) {
		String hotelId=hotelBasicInfoSearchParam.getHotelId();
		log.info("HotelInfoServiceImpl queryHotelInfo hotelId is: "+hotelId);
		Map<String, HotelBasicInfo>  hotelInfoMap = hotelSearcher.searchHotelBasicInfo(hotelBasicInfoSearchParam);
		
		//设置酒店促销类型
		setHotelPromoteType(hotelId,hotelInfoMap);
		
		HotelBasicInfo hotelInfo=hotelInfoMap.get(hotelId);
		
		//查询是否是预付酒店
		if (hotelInfo != null) {
			Date inDate=hotelBasicInfoSearchParam.getInDate();
			Date outDate=hotelBasicInfoSearchParam.getOutDate();
			if(inDate!=null && outDate!=null){
			List<Long> prepayHotelId = hotelQueryDao.queryHasPrepayPriceTypeHotel(hotelId, PayMethod.PRE_PAY, inDate,
					outDate, hotelInfo.getCityId());
			if (prepayHotelId!=null && prepayHotelId.size() > 0) {
				hotelInfo.setPrepayHotel(true);
			}
		}
		String zonename = InitServlet.citySozeObj.get(hotelInfo.getZone());
		if(zonename==null || "null".equals(zonename)){
			zonename="";
		}
		
		String chnAddres = hotelInfo.getChnAddress();
		String cityname = hotelInfo.getCityName();
		if(chnAddres.length() >cityname.length()){
    		cityname=cityname.replace("市","");
    		if(chnAddres.length() > cityname.length()+1){
				if(chnAddres.substring(0, cityname.length()+2).indexOf(cityname)!=-1){ 
					hotelInfo.setCityShowFlag(false);
					hotelInfo.setZooeShowFlag(false);
				}else{ 
					hotelInfo.setCityShowFlag(true);
					if(chnAddres.length() > zonename.length()){
						if(chnAddres.substring(0, zonename.length()).indexOf(zonename)!=-1){ 
							hotelInfo.setZooeShowFlag(false);
						}else{
							hotelInfo.setZooeShowFlag(true);
						}
					}else{
						hotelInfo.setZooeShowFlag(true);
					}
				}
    		}else{
    			if(chnAddres.indexOf(cityname)!=-1){ 
					hotelInfo.setCityShowFlag(false);
				}else{ 
					hotelInfo.setCityShowFlag(true);
					if(chnAddres.length() > zonename.length()){
						if(chnAddres.substring(0, zonename.length()).indexOf(zonename)!=-1){ 
							hotelInfo.setZooeShowFlag(false);
						}else{
							hotelInfo.setZooeShowFlag(true);
						}
					}else{
						hotelInfo.setZooeShowFlag(true);
					}
				}
    		}
		}
		log.info("HotelInfoServiceImpl queryHotelInfo setCityShowFlag is: "+hotelInfo.isCityShowFlag()+",hotelInfo.getZooeShowFlag() is "+hotelInfo.isZooeShowFlag()+",cityname="+cityname+",zonename="+zonename+",chnAddres="+chnAddres);
		hotelInfo.setZoneName(zonename);
		log.info("HotelInfoServiceImpl queryHotelInfo ZoneName is: "+hotelInfo.getZoneName());
		hotelInfo.setHotelStar(HotelQueryHandler.HotelStarConvert.getForWebVOByKey(hotelInfo.getHotelStar()));
		log.info("HotelInfoServiceImpl queryHotelInfo hotelStar is: "+hotelInfo.getHotelStar());

		
		//begin 酒店新图片库
		List<HtlAlbumVO> htlAlbumVOList = hotelPictureInfoDao.queryHotelPictureInfo(Long.valueOf(hotelId));
		
		
		hotelInfo.setHtlAlbumVOList(htlAlbumVOList);
		//end
		
		//增加酒店点击量记录
		addHwClickAmount(Long.valueOf(hotelId),ipAddress);
		//拿出酒店所在城市的商业区和行政区数据
		Map map=hotelManageWeb.queryBusinessForCityId(hotelInfo.getCityName());
		if(null!=map&&!map.isEmpty()){
			hotelInfo.getCityBizZoneMap().putAll(map);
		}
		
		CommentStatistics comment=commentStatisticsDao.queryCommentStatistics(hotelInfo.getHotelId());
		if(null!=comment){
			hotelInfo.setComment(comment);
		}
		}
		
		return hotelInfo;
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
	private void addHwClickAmount(long hotelId, String ipAddress) {
		HwClickAmount hwClickAmount = new HwClickAmount();
		hwClickAmount.setHotelId(String.valueOf(hotelId));
		hwClickAmount.setIpAddress(ipAddress);
		hwClickAmount.setClickType("1");
		hwClickAmount.setClickDate(DateUtil.getDate(new Date()));
		hotelManageWeb.addClickAmount(hwClickAmount);

	}
	public List queryHotelInfoByMgis(String cityCode, String distanceName) {
		HtlGeographicalposition geo=new HtlGeographicalposition();
		geo.setCityCode(cityCode);
		if(null!=distanceName&&!"".equals(distanceName)){
			geo.setName(distanceName);
		}
		List list=hotelSearcher.searchHotelGeoInfo(geo);
		List<String> resultList=new ArrayList();
		for(Iterator i = list.iterator();i.hasNext();){
			HtlGeographicalposition temp=(HtlGeographicalposition) i.next();
			resultList.add(temp.getName());
		}
		return resultList;
	}
	
	public HotelManageWeb getHotelManageWeb() {
		return hotelManageWeb;
	}

	public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
		this.hotelManageWeb = hotelManageWeb;
	}


	public HotelSearcher getHotelSearcher() {
		return hotelSearcher;
	}
	public void setHotelSearcher(HotelSearcher hotelSearcher) {
		this.hotelSearcher = hotelSearcher;
	}
	public HotelManage getHotelManage() {
		return hotelManage;
	}
	public void setHotelManage(HotelManage hotelManage) {
		this.hotelManage = hotelManage;
	}


	public CommentStatisticsDao getCommentStatisticsDao() {
		return commentStatisticsDao;
	}


	public void setCommentStatisticsDao(CommentStatisticsDao commentStatisticsDao) {
		this.commentStatisticsDao = commentStatisticsDao;
	}


	public void setHotelQueryDao(HotelQueryDao hotelQueryDao) {
		this.hotelQueryDao = hotelQueryDao;
	}


	public void setHotelPictureInfoDao(HotelPictureInfoDao hotelPictureInfoDao) {
		this.hotelPictureInfoDao = hotelPictureInfoDao;
	}

	
}

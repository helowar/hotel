package com.mangocity.hotel.dreamweb.search.serivce.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
 
import com.mangocity.hotel.dreamweb.search.dao.HotelBrandSearchDao;
import com.mangocity.hotel.dreamweb.search.service.HotelBrandSearchService;
import com.mangocity.hotel.search.dao.HotelPictureInfoDao;
import com.mangocity.hotel.search.model.BrandCitiesByLetter;
import com.mangocity.hotel.search.model.HotelAppearanceAlbum;
import com.mangocity.hotel.search.model.HotelBasicInfo;
import com.mangocity.hotel.search.model.HotelBasicInfoSearchParam;
import com.mangocity.hotel.search.model.HotelBrandBean;
import com.mangocity.hotel.search.model.HotelForCityBean;
import com.mangocity.hotel.search.service.HotelSearcher;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hotel.base.web.InitServletImpl;

public class HotelBrandSearchServiceImpl implements HotelBrandSearchService{

	//注入dao 
	private HotelBrandSearchDao hotelBrandSearchDao;
	private HotelSearcher hotelSearcher;
	private HotelPictureInfoDao hotelPictureInfoDao;
	private static final Logger log =Logger.getLogger(HotelBrandSearchServiceImpl.class);
	public List<HotelForCityBean> queryHotelsByBrand(String brandCode){
		HotelBasicInfoSearchParam hotelBasicInfoSearchParam=new HotelBasicInfoSearchParam();
		hotelBasicInfoSearchParam.setHotelBrand(brandCode);
		Map<String, HotelBasicInfo>  hotelInfoMap = hotelSearcher.searchHotelBasicInfo(hotelBasicInfoSearchParam);
		List<HotelForCityBean> brandBeanList = setcityCodeList(hotelInfoMap);
		Collections.sort(brandBeanList);
		return brandBeanList;
	}
	//把citycode 放到一个List里
	private List<HotelForCityBean> setcityCodeList(Map<String,HotelBasicInfo> hotelInfoMap){
		
		setHotelPitureName(hotelInfoMap);
		
		List<HotelForCityBean> brandBeanList = new ArrayList<HotelForCityBean>();
		lable1:for(Entry<String,HotelBasicInfo> hotelInfo :hotelInfoMap.entrySet()){
			String cityCode = hotelInfo.getValue().getCityId();
            for(int i = 0 ; i<brandBeanList.size();i++){
            	HotelForCityBean hotelForCityBean= brandBeanList.get(i);
            	if(cityCode.equals(hotelForCityBean.getCityCode())){
                	hotelForCityBean.getHotelList().add(hotelInfo.getValue());
            		continue lable1;
            	}
            }
            HotelForCityBean hotelForCityBean= new HotelForCityBean();
            hotelForCityBean.setCityCode(cityCode);
            hotelForCityBean.setCityName( hotelInfo.getValue().getCityName());
            String cityFirstLetteCn =  InitServlet.localCityObj.get(cityCode) == null?null:InitServlet.localCityObj.get(cityCode).substring(0, 1);//获得拼音首字母
            hotelForCityBean.setCityFirstLetterCn(cityFirstLetteCn);
            hotelForCityBean.getHotelList().add(hotelInfo.getValue());
            brandBeanList.add(hotelForCityBean);
		}
		return brandBeanList;
	}
	
	/*
	 * add by ting.li
	 * 增加酒店的图片URL
	 */
	private void setHotelPitureName(Map<String, HotelBasicInfo> hotelInfoMap) {
		HotelBasicInfo hotelInfo = null;
		String outPictureName = null;
		HotelAppearanceAlbum piture = null;
		if (hotelInfoMap != null && hotelInfoMap.size() > 0) {
			Map<String, HotelAppearanceAlbum> hotelPitureMap = getHotelpitures(hotelInfoMap);
			for (String hotelId : hotelInfoMap.keySet()) {
				hotelInfo = hotelInfoMap.get(hotelId);
				hotelInfo.setOutPictureName(null);
				piture = hotelPitureMap.get(hotelId);
				if (piture != null) {
					outPictureName = InitServlet.PICTUREURL + piture.getPrictureUrl();
				} else {
					outPictureName = "image/small.jpg";
				}				
				hotelInfo.setOutPictureName(outPictureName);
			}
		}
	}
	
	/*
	 * add by ting.li
	 */
	private String getHotelIdsString(Map<String,HotelBasicInfo> hotelInfoMap){
		StringBuffer hotelIds=new StringBuffer();
		
		if(hotelInfoMap!=null){
			for(String hotelId:hotelInfoMap.keySet()){
				hotelIds.append(hotelId);
				hotelIds.append(",");
			}
			
		}
		String hotelIdStr=hotelIds.toString();
		if(hotelIdStr.length()>=1){
			hotelIdStr=hotelIdStr.substring(0, hotelIdStr.length()-1);
		}
		return hotelIdStr;
	}
	
	/**
	 * add by ting.li 
	 * 将hotelId拼接成字符串
	 * @param hotelids
	 * @return
	 */
	private String getHotelIdsString(List<String> hotelids){
		StringBuffer hotelIds=new StringBuffer();
		
		if(hotelids!=null){
			for(String hotelId:hotelids){
				hotelIds.append(hotelId);
				hotelIds.append(",");
			}
			
		}
		String hotelIdStr=hotelIds.toString();
		if(hotelIdStr.length()>=1){
			hotelIdStr=hotelIdStr.substring(0, hotelIdStr.length()-1);
		}
		return hotelIdStr;
	}
	
	/**
	 * add by ting.li 
	 * 查询酒店的图片信息
	 * @param hotelInfoMap
	 * @return
	 */
	private Map<String,HotelAppearanceAlbum> getHotelpitures(Map<String,HotelBasicInfo> hotelInfoMap){
		String hotelIdStr = getHotelIdsString(hotelInfoMap);
		Map<String, HotelAppearanceAlbum> hotelPitureMap = new HashMap<String, HotelAppearanceAlbum>();

		if (!"".equals(hotelIdStr)) {
			List<HotelAppearanceAlbum> hotelAppearanceAlbumList = queryPitures(hotelInfoMap);

			if (hotelAppearanceAlbumList != null && hotelAppearanceAlbumList.size() > 0) {
				for (HotelAppearanceAlbum piture : hotelAppearanceAlbumList) {
					if (piture.getPictureType() == 5) {
						hotelPitureMap.put(String.valueOf(piture.getHotelId()), piture);
					}
				}
			}
		}
		return hotelPitureMap;
	}
	
	/**
	 * add by ting.li
	 * 超过1000分开查询，因为oracle的in语句最大数量为1000
	 * @param hotelInfoMap
	 * @return
	 */
	private List<HotelAppearanceAlbum> queryPitures(Map<String,HotelBasicInfo> hotelInfoMap){
		final int maxSize=1000;
		List<HotelAppearanceAlbum> hotelAppearanceAlbumList=new ArrayList<HotelAppearanceAlbum>();
		List<HotelAppearanceAlbum> subHotelAppearanceAlbumList=new ArrayList<HotelAppearanceAlbum>();
		if(hotelInfoMap!=null && hotelInfoMap.size()>0){
			List<String> hotelIds=new ArrayList<String>(hotelInfoMap.keySet());
			int hotelIdsSize=hotelIds.size();
			if(hotelIdsSize <= maxSize){
				hotelAppearanceAlbumList = hotelPictureInfoDao.queryAppearanceAlbum(getHotelIdsString(hotelIds));
			}else{
				String subHotelIdStr;
				for (int i = 0; i < hotelIdsSize / maxSize + 1; i++) {
					if ( ( i + 1) * maxSize > hotelIdsSize) {
						if(i*maxSize == hotelIdsSize){//刚好是整倍数的情况
							break;
						}
						subHotelIdStr=getHotelIdsString(hotelIds.subList(i * maxSize, hotelIdsSize));
						subHotelAppearanceAlbumList=hotelPictureInfoDao.queryAppearanceAlbum(subHotelIdStr);
					} else {
						subHotelIdStr=getHotelIdsString(hotelIds.subList(i * maxSize, ( i + 1) * maxSize));
						subHotelAppearanceAlbumList=hotelPictureInfoDao.queryAppearanceAlbum(subHotelIdStr);
					}
					
					hotelAppearanceAlbumList.addAll(subHotelAppearanceAlbumList);
				}
				
			}
			
						
		}
		return hotelAppearanceAlbumList;
	}
	
	public List<HotelForCityBean> queryHotelsByBrand(String brandCode,String cityCode){
		HotelBasicInfoSearchParam hotelBasicInfoSearchParam=new HotelBasicInfoSearchParam();
		hotelBasicInfoSearchParam.setHotelBrand(brandCode);
		hotelBasicInfoSearchParam.setCityCode(cityCode);
		Map<String, HotelBasicInfo>  hotelInfoMap = hotelSearcher.searchHotelBasicInfo(hotelBasicInfoSearchParam);
		List<HotelForCityBean> brandBeanList = setcityCodeList(hotelInfoMap);
		return brandBeanList;		
	}
	//根据品牌id查询品牌名称
	public String queryHotelBrandName(String brandCode){
		return hotelBrandSearchDao.queryHotelBrandName(brandCode);
	}
	//根据品牌id查询品牌介绍
	public String queryBrandIntroduce(String brandCode){
		return hotelBrandSearchDao.queryBrandIntroduce(brandCode);
	}
	
	//按酒店数的多少来查询酒店的其他品牌信息，不包括该品牌
    public List<HotelBrandBean> queryOtherHotelBrands(String brandCode){
    	List<Object[]> objs = hotelBrandSearchDao.queryOtherHotelBrands(brandCode);
    	if(objs.isEmpty()){
    		return Collections.EMPTY_LIST;
    	}
    	List<HotelBrandBean> hotelBrandsList = new ArrayList<HotelBrandBean>();
    	for(int i = 0 ; i < objs.size();i++){
    		Object[] obj = objs.get(i);
    		HotelBrandBean hotelBrandBean = new HotelBrandBean();
    		hotelBrandBean.setBrandCode(obj[0].toString());
    		hotelBrandBean.setBrandName(obj[1].toString());
    		hotelBrandBean.setBrandPicture(hotelBrandBean.getBrandCode());//添加图片名称,与brandcode一致
    		hotelBrandsList.add(hotelBrandBean);
    	}
    	return hotelBrandsList;
    }
		
	// 按酒店数的多少来查询酒店的其他品牌信息，不包括该品牌
	public List<HotelBrandBean> queryOtherHotelBrands(String brandCode,String cityCode){
		List<Object[]> objs = hotelBrandSearchDao.queryOtherHotelBrands(brandCode,cityCode);
    	if(objs.isEmpty()){
    		return Collections.EMPTY_LIST;
    	}
    	List<HotelBrandBean> hotelBrandsList = new ArrayList<HotelBrandBean>();
    	for(int i = 0 ; i < objs.size();i++){
    		Object[] obj = objs.get(i);
    		HotelBrandBean hotelBrandBean = new HotelBrandBean();
    		hotelBrandBean.setBrandCode(obj[0].toString());
    		hotelBrandBean.setBrandName(obj[1].toString());
    		hotelBrandBean.setBrandPicture(hotelBrandBean.getBrandCode());//添加图片名称,与brandcode一致
    		hotelBrandsList.add(hotelBrandBean);
    	}
    	return hotelBrandsList;
	}
	
	//把查询好的List<HotelForCityBean>按字母排序
	public List<BrandCitiesByLetter> sortBrandCitiesByLetter(List<HotelForCityBean> brandHotelList){
		if(brandHotelList==null || brandHotelList.isEmpty()){
			return Collections.EMPTY_LIST;
		}
		List<BrandCitiesByLetter> brandCitiesList = new ArrayList<BrandCitiesByLetter>();
		label1:for(int i = 0 ; i< brandHotelList.size() ; i++){
			HotelForCityBean hotelForCityBean = brandHotelList.get(i);
			String cityFirstLetter = hotelForCityBean.getCityFirstLetterCn();
			for(int j = 0 ; j<brandCitiesList.size();j++){
				BrandCitiesByLetter brandCities = brandCitiesList.get(j);
				if(brandCities.getCityFirstLetter()!=null && brandCities.getCityFirstLetter().equals(cityFirstLetter)){
					brandCities.getBrandCitiesList().add(hotelForCityBean);
					continue label1;
				}
			}
			BrandCitiesByLetter brandCities = new BrandCitiesByLetter();
			brandCities.setCityFirstLetter(hotelForCityBean.getCityFirstLetterCn());
			brandCities.getBrandCitiesList().add(hotelForCityBean);
			brandCitiesList.add(brandCities);
		}
		Collections.sort(brandCitiesList);
		return brandCitiesList;
	}
	

	public void setHotelBrandSearchDao(HotelBrandSearchDao hotelBrandSearchDao) {
		this.hotelBrandSearchDao = hotelBrandSearchDao;
	}

	public void setHotelSearcher(HotelSearcher hotelSearcher) {
		this.hotelSearcher = hotelSearcher;
	}
	public void setHotelPictureInfoDao(HotelPictureInfoDao hotelPictureInfoDao) {
		this.hotelPictureInfoDao = hotelPictureInfoDao;
	}
	
	
}

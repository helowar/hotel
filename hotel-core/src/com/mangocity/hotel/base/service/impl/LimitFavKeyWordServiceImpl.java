package com.mangocity.hotel.base.service.impl;

import java.util.Date;

import com.mangocity.hotel.base.dao.LimitFavKeyWordDao;
import com.mangocity.hotel.base.persistence.HtlFavourable;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.service.LimitFavKeyWordService;
import com.mangocity.hotel.base.web.InitServlet;

public class LimitFavKeyWordServiceImpl implements LimitFavKeyWordService{
	private LimitFavKeyWordDao limitFavKeyWordDao;
	public Boolean singleSettingKeyWord(String hotelIdStr,String loginName,String fav_a,String fav_b,String fav_c){
		int fava = 0 , favb=0 , favc=0;
		if(null != fav_a && "" != fav_a && fav_a.equals("1")){ fava = 1;}
		if(null != fav_b && "" != fav_b && fav_b.equals("1")){ favb = 1;}
		if(null != fav_c && "" != fav_c && fav_c.equals("1")){ favc = 1;}
		Long hotelId = Long.valueOf(hotelIdStr);
		HtlFavourable htlFavourableold = limitFavKeyWordDao.queryFromHtlFavourable(hotelId);//查询出相应酒店记录
		HtlFavourable htlFavourablenew = new HtlFavourable();
		if(null !=htlFavourableold){
			htlFavourablenew.setCityCode(htlFavourableold.getCityCode());
			htlFavourablenew.setCityName(htlFavourableold.getCityName());
			htlFavourablenew.setHotelName(htlFavourableold.getHotelName());
		}else{
			HtlHotel hotel = limitFavKeyWordDao.queryHotelBaseInfo(hotelId);
			htlFavourablenew.setCityCode(hotel.getCity());
			htlFavourablenew.setCityName(InitServlet.cityObj.get(hotel.getCity()));
			htlFavourablenew.setHotelName(hotel.getChnName());
		}
		htlFavourablenew.setHotelId(hotelId);
		htlFavourablenew.setUpdateBy(loginName);
		htlFavourablenew.setUpdateTime(new Date());
		htlFavourablenew.setFavA(fava);
		htlFavourablenew.setFavB(favb);
		htlFavourablenew.setFavC(favc);
		htlFavourablenew.setFlag(1);
		if(null != htlFavourableold && null != htlFavourableold.getId()){
		limitFavKeyWordDao.singleDeleteHtlFavourable(htlFavourableold.getId());//将该条酒店记录置为无效
		}
		limitFavKeyWordDao.insertHtlFacourable(htlFavourablenew);//插入一条记录
		return true;
		
	}
	
	
	public Boolean batchSettingKeyWord(String hotelIdStr,String loginName,String fav_a,String fav_b,String fav_c){
	    String[] hotelIdList = hotelIdStr.split(",");
	    for(int i = 0;i<hotelIdList.length;i++){
	    	this.settingKeyWord(hotelIdList[i], loginName, fav_a, fav_b, fav_c);
	    }
	    return true;
	}
	
	public Boolean batchCancelKeyWord(String hotelIdStr,String loginName,String fav_a,String fav_b,String fav_c){
		  String[] hotelIdList = hotelIdStr.split(",");
		    for(int i = 0;i<hotelIdList.length;i++){
		    	this.cancelKeyWord(hotelIdList[i], loginName, fav_a, fav_b, fav_c);
		    }
		    return true;
	}
	public Boolean settingKeyWord(String hotelIdStr,String loginName,String fav_a,String fav_b,String fav_c){
		int fava = 0 , favb=0 , favc=0;
		if(fav_a.equals("1")){ fava = 1;}
		if(fav_b.equals("1")){ favb = 1;}
		if(fav_c.equals("1")){ favc = 1;}
		Long hotelId = Long.valueOf(hotelIdStr);
		HtlFavourable htlFavourableold = limitFavKeyWordDao.queryFromHtlFavourable(hotelId);//查询出相应酒店记录
		HtlFavourable htlFavourablenew = new HtlFavourable();
		if(null !=htlFavourableold){
			htlFavourablenew.setCityCode(htlFavourableold.getCityCode());
			htlFavourablenew.setCityName(htlFavourableold.getCityName());
			htlFavourablenew.setHotelName(htlFavourableold.getHotelName());
			htlFavourablenew.setFavA(htlFavourableold.getFavA());
			htlFavourablenew.setFavB(htlFavourableold.getFavB());
			htlFavourablenew.setFavC(htlFavourableold.getFavC());
		}else{
			HtlHotel hotel = limitFavKeyWordDao.queryHotelBaseInfo(hotelId);
			htlFavourablenew.setCityCode(hotel.getCity());
			htlFavourablenew.setCityName(InitServlet.cityObj.get(hotel.getCity()));
			htlFavourablenew.setHotelName(hotel.getChnName());
			htlFavourablenew.setFavA(0);
			htlFavourablenew.setFavB(0);
			htlFavourablenew.setFavC(0);
		}
		htlFavourablenew.setHotelId(hotelId);
		htlFavourablenew.setUpdateBy(loginName);
		htlFavourablenew.setUpdateTime(new Date());
		if(1 == fava){htlFavourablenew.setFavA(fava);}
		if(1 == favb){htlFavourablenew.setFavB(favb);}
		if(1 == favc){htlFavourablenew.setFavC(favc);}
		htlFavourablenew.setFlag(1);
		if(null != htlFavourableold && null != htlFavourableold.getId()){
		limitFavKeyWordDao.singleDeleteHtlFavourable(htlFavourableold.getId());//将该条酒店记录置为无效
		}
		limitFavKeyWordDao.insertHtlFacourable(htlFavourablenew);//插入一条记录
		return true;
	}
   
	public Boolean cancelKeyWord(String hotelIdStr,String loginName,String fav_a,String fav_b,String fav_c){
		int fava = 1 , favb=1 , favc=1;
		if(fav_a.equals("1")){ fava = 0;}
		if(fav_b.equals("1")){ favb = 0;}
		if(fav_c.equals("1")){ favc = 0;}
		Long hotelId = Long.valueOf(hotelIdStr);
		HtlFavourable htlFavourableold = limitFavKeyWordDao.queryFromHtlFavourable(hotelId);//查询出相应酒店记录
		HtlFavourable htlFavourablenew = new HtlFavourable();
		if(null !=htlFavourableold){
			htlFavourablenew.setCityCode(htlFavourableold.getCityCode());
			htlFavourablenew.setCityName(htlFavourableold.getCityName());
			htlFavourablenew.setHotelName(htlFavourableold.getHotelName());
			htlFavourablenew.setFavA(htlFavourableold.getFavA());
			htlFavourablenew.setFavB(htlFavourableold.getFavB());
			htlFavourablenew.setFavC(htlFavourableold.getFavC());
		}else{
			HtlHotel hotel = limitFavKeyWordDao.queryHotelBaseInfo(hotelId);
			htlFavourablenew.setCityCode(hotel.getCity());
			htlFavourablenew.setCityName(InitServlet.cityObj.get(hotel.getCity()));
			htlFavourablenew.setHotelName(hotel.getChnName());
			htlFavourablenew.setFavA(0);
			htlFavourablenew.setFavB(0);
			htlFavourablenew.setFavC(0);
		}
		htlFavourablenew.setHotelId(hotelId);
		htlFavourablenew.setUpdateBy(loginName);
		htlFavourablenew.setUpdateTime(new Date());
		if(0 == fava){htlFavourablenew.setFavA(fava);}
		if(0 == favb){htlFavourablenew.setFavB(favb);}
		if(0 == favc){htlFavourablenew.setFavC(favc);}
		htlFavourablenew.setFlag(1);
		if(null != htlFavourableold && null != htlFavourableold.getId()){
		limitFavKeyWordDao.singleDeleteHtlFavourable(htlFavourableold.getId());//将该条酒店记录置为无效
		}
		limitFavKeyWordDao.insertHtlFacourable(htlFavourablenew);//插入一条记录
		return true;
	}
	public LimitFavKeyWordDao getLimitFavKeyWordDao() {
		return limitFavKeyWordDao;
	}

	public void setLimitFavKeyWordDao(LimitFavKeyWordDao limitFavKeyWordDao) {
		this.limitFavKeyWordDao = limitFavKeyWordDao;
	}
	
}

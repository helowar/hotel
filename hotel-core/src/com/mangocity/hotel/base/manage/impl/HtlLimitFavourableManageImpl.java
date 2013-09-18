package com.mangocity.hotel.base.manage.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.dao.HtlLimitFavourableDao;
import com.mangocity.hotel.base.manage.HtlLimitFavourableManage;
import com.mangocity.hotel.base.persistence.HtlLimitFavourable;
import com.mangocity.hotel.base.persistence.HtlLimitFavourableHotel;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;

public class HtlLimitFavourableManageImpl implements HtlLimitFavourableManage {
	private  HtlLimitFavourableDao htlLimitFavourableDao;
	
	public void save(HtlLimitFavourable htlLimitFavourable) {
		htlLimitFavourableDao.saveHtlLimitFavourable(htlLimitFavourable);
	}
	
	/**
	 * 查询酒店在一段时间内的返限活动
	 */
	public List<HtlLimitFavourableHotel> queryLimitFavourableHotel(String hotelIdList,Date beginDate,Date endDate){
		return htlLimitFavourableDao.queryLimitFavourableHotel(hotelIdList, beginDate, endDate);
	}

	public HtlLimitFavourableDao getHtlLimitFavourableDao() {
		return htlLimitFavourableDao;
	}

	public void setHtlLimitFavourableDao(HtlLimitFavourableDao htlLimitFavourableDao) {
		this.htlLimitFavourableDao = htlLimitFavourableDao;
	}

	public Boolean judgeLimitFav(Long hotelId, Date checkIn, Date checkOut) {
		
		return htlLimitFavourableDao.judgeLimitFav(hotelId, checkIn, checkOut);
	}

	public BigDecimal queryLimitFavRate(Long hotelId, Date date) {
		
		return htlLimitFavourableDao.queryLimitFavRate(hotelId, date);
	}

	public void deleteFav(Long favId) {
		htlLimitFavourableDao.deleteFav(favId);
	}

	public HtlLimitFavourable queryLimitFav(Long favId) {
		
		return htlLimitFavourableDao.queryLimitFav(favId);
	}
	
	/**
	 * 开始活动
	 * @param favId
	 */
	public void favActiviyBeginOrEnd(Long favId,String status){
		if(null != favId ){
			htlLimitFavourableDao.updateFavActiviyToBeginOrEnd(favId, status);
		}
	}
	
	/**
	 * 查询预定间夜量 总返现金额
	 * @param favId
	 * @return
	 */
	public String getRoomNight_ReturnCash(Long favId){
		String roomNight_ReturnCash="";
		if(null != favId ){
			roomNight_ReturnCash = htlLimitFavourableDao.sumRoomNightAndReturnCash(favId);
		}
		return roomNight_ReturnCash;
	}
	
	/**
	 * 计算某价格类型现金返还金额
	 * 
	 */
	public int calculateCashLimitReturnAmount(long hotelId,long priceTypeId, Date ableDate, String currency,
			  BigDecimal salePrice,Double commission){
		List<HtlLimitFavourableHotel> listHtlLimitFavourableHotel=htlLimitFavourableDao
		               .queryLimitFavourableHotel(String.valueOf(hotelId),ableDate, ableDate);
		
		for(HtlLimitFavourableHotel limitFavourable:listHtlLimitFavourableHotel){
			
			//如果该商品所在的酒店参加了限量返现活动，并且预订时间段在活动日期内，则计算返现
			if(	DateUtil.compare(limitFavourable.getCheckIn(), ableDate) >= 0 &&
			     DateUtil.compare(limitFavourable.getCheckOut(), ableDate) <= 0){
				
				//得到汇率
				double rate = 1.0;
				if (!CurrencyBean.RMB.equals(currency)) {
					String rateStr = CurrencyBean.rateMap.get(currency);
					if (StringUtil.isValidStr(rateStr)) {
						rate = Double.parseDouble(rateStr);
					}
				}
				BigDecimal scale = new BigDecimal(limitFavourable.getRule());
				//返现规则>3时，表示比例,反之直接是返现份额
				if (3 > scale.doubleValue()) {
					double netPrice = 0d;
					
					//返现规则为售价*返现比例
					if(limitFavourable.getFavType()==1){
						netPrice = salePrice.doubleValue();
					}
					//返现规则为俑金*返现比例
					else if(limitFavourable.getFavType()==2){
						netPrice = commission;
					}
					//返现为0
					else{
						netPrice=0;
					}
					int returnCash=scale.multiply(
							new BigDecimal(netPrice)).multiply(
							new BigDecimal(rate)).intValue();
					if(returnCash > 0 && returnCash < 10){
						returnCash=10;
					}
					returnCash=returnCash/10*10;
					return returnCash;
				} else {
					int returnCash=scale.multiply(
							new BigDecimal(rate)).intValue();
					return returnCash;
				}
			}
		}
		return -1;
	}

	public Boolean judgeFavStart(Long favId) {
		boolean  flag = false; 
		HtlLimitFavourable htlLimitFavourable = htlLimitFavourableDao.queryByFavId(favId);
		if(null!=htlLimitFavourable&&1==htlLimitFavourable.getFavIsStart()) flag =true; //活动已经开始
		return flag;
	}
	
	public Boolean isHaveAnotherActivityBegin(Long favId){
		return htlLimitFavourableDao.isHaveAnotherActivityBegin(favId);
	}
}

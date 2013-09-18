package com.mangocity.hotel.search.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.constant.FavourableTypeUtil;
import com.mangocity.hotel.base.manage.HtlLimitFavourableManage;
import com.mangocity.hotel.base.persistence.HtlEveningsRent;
import com.mangocity.hotel.base.persistence.HtlFavouraParameter;
import com.mangocity.hotel.base.persistence.HtlFavourableDecrease;
import com.mangocity.hotel.base.persistence.HtlFavourableReturn;
import com.mangocity.hotel.base.persistence.HtlFavourableclause;
import com.mangocity.hotel.base.persistence.HtlLimitFavourableHotel;
import com.mangocity.hotel.base.persistence.QueryCashBackControl;
import com.mangocity.hotel.base.service.ChannelCashBackManagerService;
import com.mangocity.hotel.search.constant.CloseRoomReason;
import com.mangocity.hotel.search.constant.NotBookReason;
import com.mangocity.hotel.search.constant.OpenCloseRoom;
import com.mangocity.hotel.search.constant.PayMethod;
import com.mangocity.hotel.search.constant.SaleChannel;
import com.mangocity.hotel.search.dao.HotelInfoDAO;
import com.mangocity.hotel.search.dao.HotelQueryDao;
import com.mangocity.hotel.search.model.QueryCommodityCondition;
import com.mangocity.hotel.search.model.QueryCommodityInfo;
import com.mangocity.hotel.search.service.CommodityInfoService;
import com.mangocity.hotel.search.service.assistant.RoomStateByBedType;
import com.mangocity.hotel.search.service.assistant.SaleInfo;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.bean.MustDate;
import com.mangocity.util.bean.MyBeanUtil;


public class CommodityInfoServiceImpl implements CommodityInfoService {
	
	private HotelQueryDao hotelQueryDao;
	
	private HotelInfoDAO hotelInfoDAO;
	
	private HtlLimitFavourableManage limitFavourableManage;
    /**
     * 返利查询service add by hushunqiang
     */
	public ChannelCashBackManagerService channelCashBackService;
	
    private static Date DATE_1970_01_01 = DateUtil.stringToDateMain("1970-01-01", "yyyy-MM-dd");
    private static Date DATE_2099_01_01 = DateUtil.stringToDateMain("2099-01-01", "yyyy-MM-dd");
	
	/**
	 * 从db中查询商品动态信息
	 * @param qcc
	 * @return
	 */
	public List<QueryCommodityInfo> queryCommodityInfo(QueryCommodityCondition  qcc){
		return hotelQueryDao.queryHotelResultList(qcc);
	} 

	/**
	 * 古志杰提供,暂时未实现
	 * 设置酒店优惠(住M送N，打折,连住包价)
	 * 给满足条件的QueryCommodityInfo设置优惠,指每一天的每个床型是否优惠,如果有,优惠多少.
	 * @param hfcLst
	 * @param commodityLst
	 */
	public List<QueryCommodityInfo> setProviderFavourableToCommodityPerday(QueryCommodityCondition qcc,
			List<QueryCommodityInfo> commodityLst) {

		List<HtlFavourableclause> hfcLst = hotelInfoDAO.queryFavourableClauses(qcc);
		Map<String, List<HtlFavourableclause>> mapHfc = changeFavourableListToMap(hfcLst);
		Map<String, Map<String, List<QueryCommodityInfo>>> mapQci = changeQueryCommodityInfoListToMap(commodityLst);
		for (Map.Entry<String, Map<String, List<QueryCommodityInfo>>> queryEntrySet : mapQci.entrySet()) {
			Map<String, List<QueryCommodityInfo>> commodityInfoMap = queryEntrySet.getValue();
			for (Map.Entry<String, List<QueryCommodityInfo>> addBedqueryEntrySet : commodityInfoMap.entrySet()) {
				// 每個酒店需要重新清零
				int f = 0;
				List<QueryCommodityInfo> commodityInfoList = addBedqueryEntrySet.getValue();
				for (int y = 0; y < commodityInfoList.size(); y++) {
					QueryCommodityInfo commodityInfo = addBedqueryEntrySet.getValue().get(y);
					if(null == mapHfc.get(commodityInfoMapKey(commodityInfo))) continue;
					for (HtlFavourableclause htlFavourableclause : mapHfc.get(commodityInfoMapKey(commodityInfo))) {
						if (!commodityInfo.getAbledate().before(htlFavourableclause.getBeginDate())
								&& !commodityInfo.getAbledate().after(htlFavourableclause.getEndDate())) {
							if (FavourableTypeUtil.continueDonate.equals(htlFavourableclause.getFavourableType())) {

								List<HtlFavouraParameter> lstPackagerate = htlFavourableclause.getLstPackagerate();
								if (!lstPackagerate.isEmpty()) {

									for (HtlFavouraParameter htlFavouraParameter : lstPackagerate) {
										List<HtlEveningsRent> lstEveningsRent = htlFavouraParameter
												.getLstEveningsRent();
										if (!lstEveningsRent.isEmpty()) {
											// 连住的总天数，连住N送M：总天数=N+M
											int numDay = htlFavouraParameter.getContinueNight().intValue()
													+ htlFavouraParameter.getDonateNight().intValue();
											// 入住的天数满足连住的总天数
											if (f == numDay - 1) {
												for (HtlEveningsRent htlEveningsRent : lstEveningsRent) {
													int night = htlEveningsRent.getNight().intValue();
													QueryCommodityInfo queryCommodityInfo = (QueryCommodityInfo) commodityInfoList
															.get(y - numDay + night);
													queryCommodityInfo.setFavourableFlag(true);
													if (0 == htlEveningsRent.getSalePrice().intValue()) {
														queryCommodityInfo.setFavourableNumber(queryCommodityInfo
																.getSaleprice());
														queryCommodityInfo.setSaleprice(99999.0);
														//-----------longkangfu-------------2012-6-18
														queryCommodityInfo.setCommission(0.0);
													} else {
														queryCommodityInfo.setFavourableNumber(queryCommodityInfo
																.getSaleprice()
																- htlEveningsRent.getSalePrice());
														queryCommodityInfo.setSaleprice(htlEveningsRent.getSalePrice());
														//-----------longkangfu-------------2012-6-18
														queryCommodityInfo.setCommission(htlEveningsRent.getCommission());
													}
												}
												f = 0;
												// 判断该优惠是否循环
												if (2 == htlFavouraParameter.getCirculateType()) {
													f = 9999;
												}
											} else {
												f++;
											}
										}
									}
								}

							} else if (FavourableTypeUtil.discount.equals(htlFavourableclause.getFavourableType())) {
								List<HtlFavouraParameter> lstPackagerate = htlFavourableclause.getLstPackagerate();
								if (null != lstPackagerate) {
									commodityInfo.setFavourableFlag(true);
									for (int j = 0; j < lstPackagerate.size(); j++) {
										double oldSalePrice = commodityInfo.getSaleprice();
										BigDecimal b = new BigDecimal("" + lstPackagerate.get(j).getDiscount()
												* commodityInfo.getSaleprice() / 10);
										// 逢一进十
										if (1 == lstPackagerate.get(j).getDecimalPointType()) {
											commodityInfo.setSaleprice(Math.ceil(b
													.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()));
										}
										// 四舍五入保留1位小数
										if (2 == lstPackagerate.get(j).getDecimalPointType()) {
											commodityInfo.setSaleprice(b.setScale(1, BigDecimal.ROUND_HALF_UP)
													.doubleValue());
										}
										// 直接取整
										if (3 == lstPackagerate.get(j).getDecimalPointType()) {
											commodityInfo.setSaleprice(Math.floor(lstPackagerate.get(j).getDiscount()
													* commodityInfo.getSaleprice() / 10));
										}
										// 四舍五入取整
										if (4 == lstPackagerate.get(j).getDecimalPointType()) {
											commodityInfo.setSaleprice(b.setScale(0, BigDecimal.ROUND_HALF_UP)
													.doubleValue());
										}
										commodityInfo.setFavourableNumber(oldSalePrice - commodityInfo.getSaleprice());
										
										
		    							double newSalePrice = commodityInfo.getSaleprice();
		    							int k = 0;
		    							String formulaId=commodityInfo.getFormula();
		    							
		    							if("0".equals(formulaId)){
		    								k=0;
		    							}else if("priceA".equals(formulaId)){
		    								k=1;
		    							}else if("priceB".equals(formulaId)){
		    								k=2;
		    							}else if("priceC".equals(formulaId)){
		    								k=3;
		    							}else if("priceD".equals(formulaId)){
		    								k=4;
		    							}else if("priceE".equals(formulaId)){
		    								k=5;
		    							}else if("priceF".equals(formulaId)){
		    								k=6;
		    							}else if("priceG".equals(formulaId)){
		    								k=7;
		    							}else if("priceH".equals(formulaId)){
		    								k=8;
		    							}else if("priceI".equals(formulaId)){
		    								k=9;
		    							}else if("priceJ".equals(formulaId)){
		    								k=10;
		    							}else if("priceK".equals(formulaId)){
		    								k=11;
		    							}
		    							switch(k){
		    							case 0:
		    								commodityInfo.setCommission(commodityInfo.getCommission());
		    								break;
		    							case 1:
		    								commodityInfo.setCommission(BigDecimal.valueOf(
		    										(newSalePrice/(oldSalePrice/(commodityInfo.getCommission()/
		    												commodityInfo.getCommissionRate()+
		    												(commodityInfo.getBreakfastprice()*commodityInfo.getBreakfastnumber())))-
		    												commodityInfo.getBreakfastprice()*commodityInfo.getBreakfastnumber())*
		    												commodityInfo.getCommissionRate()).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
		    								break;
		    							case 2:
		    								commodityInfo.setCommission(BigDecimal.valueOf((newSalePrice-
		    										commodityInfo.getBreakfastprice()*commodityInfo.getBreakfastnumber())/
		    										((oldSalePrice-commodityInfo.getBreakfastprice()*commodityInfo.getBreakfastnumber())/
		    												(commodityInfo.getCommission()/commodityInfo.getCommissionRate()))*commodityInfo.getCommissionRate()).
		    												setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
		    								break;
		    							case 3:
		    								commodityInfo.setCommission(BigDecimal.valueOf(newSalePrice*commodityInfo.getCommissionRate()).
		    										setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		    								break;
		    							case 4:
		    								commodityInfo.setCommission(BigDecimal.valueOf((newSalePrice/(oldSalePrice/(commodityInfo.getCommission()/
		    										commodityInfo.getCommissionRate())))*commodityInfo.getCommissionRate()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		    								break;
		    							case 5:
		    								commodityInfo.setCommission(BigDecimal.valueOf((newSalePrice-commodityInfo.getBreakfastprice()*
		    										commodityInfo.getBreakfastnumber())*commodityInfo.getCommissionRate()).
		    										setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		    								break;
		    							case 6:
		    								commodityInfo.setCommission(commodityInfo.getCommission());
		    								break;
		    							case 7:
		    								commodityInfo.setCommission(BigDecimal.valueOf((newSalePrice-commodityInfo.getBreakfastprice()*
		    										commodityInfo.getBreakfastnumber())*commodityInfo.getCommissionRate()).
		    										setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		    								break;
		    							case 8:
		    								commodityInfo.setCommission(BigDecimal.valueOf((newSalePrice-commodityInfo.getBreakfastprice()*
		    										commodityInfo.getBreakfastnumber())/(commodityInfo.getBreakfastprice()*
		    										commodityInfo.getBreakfastnumber()/(commodityInfo.getAdvicePrice()-commodityInfo.getCommission()/commodityInfo.getCommissionRate()))*
		    	    								commodityInfo.getCommissionRate()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		    								break;
		    							case 9:
		    								commodityInfo.setCommission(BigDecimal.valueOf(newSalePrice*commodityInfo.getCommissionRate()).
		    										setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		    								break;
		    							case 10:
		    								commodityInfo.setCommission(BigDecimal.valueOf(newSalePrice*commodityInfo.getCommissionRate()).
		    										setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		    								break;
		    							case 11:
		    								commodityInfo.setCommission(BigDecimal.valueOf((newSalePrice-commodityInfo.getBreakfastprice()*
		    										commodityInfo.getBreakfastnumber())*commodityInfo.getCommissionRate()).
		    										setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
		    								break;
		    							
		    							}
									}
								}
							} else if (FavourableTypeUtil.packagerate.equals(htlFavourableclause.getFavourableType())) {
								List<HtlFavouraParameter> lstPackagerate = htlFavourableclause.getLstPackagerate();
								if (null != lstPackagerate) {
									for (HtlFavouraParameter htlFavouraParameter : lstPackagerate) {
										if (f == htlFavouraParameter.getPackagerateNight() - 1) {
											int packagerateNight = htlFavouraParameter.getPackagerateNight().intValue();
											for (int a = y - (packagerateNight - 1); a <= y; a++) {
												QueryCommodityInfo queryCommodityInfo = (QueryCommodityInfo) commodityInfoList
														.get(a);
												if (a == y - (packagerateNight - 1)) {
													queryCommodityInfo.setFavourableNumber(queryCommodityInfo
															.getSaleprice()
															- htlFavouraParameter.getPackagerateSaleprice());
													queryCommodityInfo.setSaleprice(htlFavouraParameter
															.getPackagerateSaleprice());
													//-----------longkangfu-------------2012-6-18
													queryCommodityInfo.setCommission(htlFavouraParameter.getPackagerateCommission());
												} else {
													queryCommodityInfo.setFavourableNumber(queryCommodityInfo
															.getSaleprice());
													// 如果等于y，在调用这个方法的位置再减去多加的价格。
													queryCommodityInfo.setSaleprice(99999.0);
													//-----------longkangfu-------------2012-6-18
													queryCommodityInfo.setCommission(0.0);
												}
												commodityInfo.setFavourableFlag(true);
											}
											f = 0;
										} else {
											f++;
										}
									}
								}

							}
						}
					}
				}
				commodityInfoMap.put(addBedqueryEntrySet.getKey(), commodityInfoList);
			}
			mapQci.put(queryEntrySet.getKey(), commodityInfoMap);
		}
		return changeMapQciMapToResultList(mapQci);
	}
	
	/**
	 * 设置限量返现
	 */
	public void setLimitFavourableReturnToCommodityPerday(QueryCommodityCondition  qcc,List<QueryCommodityInfo> commodityLst){
		List<HtlLimitFavourableHotel> listHtlLimitFavourableHotel=limitFavourableManage
		             .queryLimitFavourableHotel(qcc.getHotelIdLst(), qcc.getInDate(), qcc.getOutDate());
		
		for(HtlLimitFavourableHotel limitFavourable:listHtlLimitFavourableHotel){
			
			for(QueryCommodityInfo commodity : commodityLst){
				
				double salePrice = commodity.getSaleprice();
				if(0.01 > salePrice || 99998 <= salePrice) {
					continue;
				}
				//如果该商品所在的酒店参加了限量返现活动，并且预订时间段在活动日期内，则计算返现
				if(commodity.getHotelId().compareTo(limitFavourable.getHotelId())==0 &&
					DateUtil.compare(limitFavourable.getCheckIn(), commodity.getAbledate()) >= 0 &&
				     DateUtil.compare(limitFavourable.getCheckOut(), commodity.getAbledate()) <= 0){
						
					commodity.setHasReturnCash(true);
					String currency = commodity.getCurrency();
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
							netPrice = salePrice;
						}
						//返现规则为俑金*返现比例
						else if(limitFavourable.getFavType()==2){
							netPrice =commodity.getCommission();
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
						commodity.setReturnCash(Double.valueOf(String.valueOf(returnCash)));
					} else {
						int returnCash=scale.multiply(
								new BigDecimal(rate)).intValue();
						
						commodity.setReturnCash(Double.valueOf(String.valueOf(returnCash)));
					}
				}
			}
		}
		//1,增加渠道返利控制，通过渠道设定的返现比例 add by hushunqiang
		double cashbackratevalue = channelCashBackService.getChannelCashBacktRate(qcc.getProjectCode());
		
		setFavourableReturnToCommodityPerday(qcc,commodityLst);
		
		for (QueryCommodityInfo commodity : commodityLst) {
			//2,返现比例计算最终的返现金额 add by hushunqiang
			Double lastReturnAmount = channelCashBackService.getlastCashBackAmount(cashbackratevalue, commodity.getReturnCash());
			commodity.setReturnCash(lastReturnAmount);
		}
		
	}
	
	
	/**
	 * 设置返现
	 * 给当前满足条件的QueryCommodityInfo设置返现,指每天的每个床型是否返现,返多少.
	 * @param hfr
	 * @param commodityLst
	 */
	public void setFavourableReturnToCommodityPerday(QueryCommodityCondition  qcc,List<QueryCommodityInfo> commodityLst){
		
		List<HtlFavourableReturn> hfrLst = hotelInfoDAO
				.queryFavourableReturns(qcc);
		
		if (null == hfrLst || hfrLst.isEmpty() || null == commodityLst
				|| commodityLst.isEmpty()) {
			return;
		}
				
		Map<String, List<HtlFavourableReturn>> retMap = new HashMap<String, List<HtlFavourableReturn>>();
		for (HtlFavourableReturn ret : hfrLst) {
			String retKey = ret.getPriceTypeId() + "_" + ret.getPayMethod();
			List<HtlFavourableReturn> lstFavRet = retMap.get(retKey);
			if (null == lstFavRet) {
				lstFavRet = new ArrayList<HtlFavourableReturn>(1);
				retMap.put(retKey, lstFavRet);
			} 
			lstFavRet.add(ret);
		}
		
		for (QueryCommodityInfo commodity : commodityLst) {
			
			//如果该商品已经设置了限量返现，则不再算普通返现
			if(!commodity.isHasReturnCash()){
				double salePrice = commodity.getSaleprice();			
				if(0.01 > salePrice || 99998 <= salePrice) {
					continue;
				}
				
				String payMethod = commodity.getPaymethod().equals(PayMethod.PAY) ? "1"
						: "2";
				String retKey = commodity.getCommodityId() + "_" + payMethod;
				List<HtlFavourableReturn> lstFavRet = retMap.get(retKey);
				if (null != lstFavRet && !lstFavRet.isEmpty()) {
					
					Date ableDate = commodity.getAbledate();
					for (HtlFavourableReturn favRet : lstFavRet) {
						
						String weekOfDate = String.valueOf(DateUtil
								.getWeekOfDate(ableDate));
						if (0 <= ableDate.compareTo(favRet.getBeginDate())
								&& 0 >= ableDate.compareTo(favRet.getEndDate())
								&& 0 <= favRet.getWeek().indexOf(weekOfDate)) {
							
							commodity.setHasReturnCash(true);
							String currency = commodity.getCurrency();
							double rate = 1.0;
							if (!CurrencyBean.RMB.equals(currency)) {
								String rateStr = CurrencyBean.rateMap.get(currency);
								if (StringUtil.isValidStr(rateStr)) {
									rate = Double.parseDouble(rateStr);
								}
							}
							BigDecimal scale = new BigDecimal(favRet
									.getReturnScale());
							if (1 > scale.doubleValue()) {
								String formula = commodity.getFormula();
								double netPrice = 0d;
								if ("0".equals(formula) || "priceF".equals(formula)) {
									netPrice = salePrice;
								} else {
									Double commissionRate = commodity
									.getCommissionRate();
									netPrice = null == commissionRate
									|| 0.0001 > commissionRate
									.doubleValue() ? 0
											: new BigDecimal(commodity
													.getCommission()).divide(
															new BigDecimal(commissionRate),
															1, RoundingMode.UP)
															.doubleValue();
								}
								
								commodity.setReturnCash(scale.multiply(
										new BigDecimal(netPrice)).multiply(
												new BigDecimal(rate)).doubleValue());
							} else {
								commodity.setReturnCash(scale.multiply(
										new BigDecimal(rate)).doubleValue());
							}
						}
					}
				}
			}
			
		}
	}
	
	/**
	 * 设置立减
	 * 给当前满足条件的QueryCommodityInfo设置立减情况,指每天的每个床型是否立减,减多少
	 * @param hfd
	 * @param commodityLst
	 */
	public void setHtlFavourableDecreaseToCommodityPerday(QueryCommodityCondition  qcc,List<QueryCommodityInfo> commodityLst){
		List<HtlFavourableDecrease> hfdLst = hotelInfoDAO.queryFavourableDecrease(qcc);
		if(null==hfdLst || null == commodityLst || commodityLst.size()==0) return ;
		Date ableDate = commodityLst.get(0).getAbledate();
		Map<String,HtlFavourableDecrease> decMap = new HashMap<String,HtlFavourableDecrease>();
		
		for(HtlFavourableDecrease decr:hfdLst){
			if(decr.getBeginDate().after(ableDate)||decr.getEndDate().before(ableDate)) continue;
			String decrKey = decr.getHotelId()+"_"+decr.getPriceTypeId()+"_"+decr.getPayMethod();
			HtlFavourableDecrease favDec = decMap.get(decrKey);
			if(favDec == null || decr.getModifyTime().after(favDec.getModifyTime())){
				decMap.put(decrKey, decr);
			}
		}
		
		String payMethod = "";
		for(QueryCommodityInfo commodity:commodityLst){
			payMethod = commodity.getCommodityId().equals(PayMethod.PAY)?"1":"2";
			String decrKey = commodity.getHotelId()+"_"+commodity.getCommodityId()+"_"+payMethod;
			HtlFavourableDecrease favDec = decMap.get(decrKey);
			if(favDec!=null && favDec.getDecreasePrice() > 0){
				commodity.setHaspromptlyReduce(true);
				commodity.setRomptlyReduce(Double.valueOf(favDec.getDecreasePrice()));
			}
		}
	}
	
	
	
	/**
	 * 是否显示(每天的商品),如：关房了，某些原因在外网是不需要显示价格
	 * @param queryInfo
	 * @param fromChannel
	 * @return
	 */
	public boolean showPriceForPerday(QueryCommodityInfo queryInfo, String fromChannel){
		boolean result = true;
		String reason = queryInfo.getClosereason();
        if (null == reason||OpenCloseRoom.OpenRoom.equals(queryInfo.getCloseflag())) {
            return true;
        }

        if (SaleChannel.WEB.equals(fromChannel)
				|| SaleChannel.B2B.equals(fromChannel)) {
			// 以下关房原因的房型不能显示
			if (CloseRoomReason.CloseDown.equals(reason)
					|| CloseRoomReason.CloseCCCan.equals(reason)
					|| CloseRoomReason.CloseCCNo.equals(reason)
					|| CloseRoomReason.NotCollaborate.equals(reason)) {
				result = false;
			} else {
				result = true;
			}
		}

		return result;
	}
	
	/**
	 * 能否满足条款(针对每天每个商品) 1、预订时间在要求的时间范围内 2、入住天数必须大于连住小于限住
	 * 
	 * @param queryInfo
	 * @return
	 */
	public void satisfyClauseForPerday(QueryCommodityInfo queryInfo,SaleInfo saleInfo,Date checkinDate,Date checkoutDate){
		String sDate="",eDate="";
		String stime="",etime="";		
		
		sDate = DateUtil.dateToString(queryInfo.getBookstartdate());//开始日期
		eDate = DateUtil.dateToString(queryInfo.getBookenddate());//结束日期
		stime = queryInfo.getMorningtime();//开始时间
		etime = queryInfo.getEveningtime();//结束时间
		
		if(null==sDate||"".equals(sDate)) {
			sDate="1900-01-01";
		}
		if(null==eDate||"".equals(eDate)) {
			eDate="2099-12-31";
		}
		
		if(null==stime||"".equals(stime))stime="00:00";
		if(null==etime||"".equals(etime))etime="23:59";
		
		Date startDate = DateUtil.stringToDateMinute(sDate+" "+stime);//开始日期时间
		Date endDate = DateUtil.stringToDateMinute(eDate+" "+etime);//结束日期时间
			
		Date curDate = DateUtil.getSystemDate();//当前日期									
		
		
		boolean flag = true;
		/**
		 * 如果当前日期在要求的时间区间内
		 * 并且满足连住和限住条件就return true
		 */
		StringBuilder notSatisfyStr = new StringBuilder("");
		if (curDate.before(startDate) || curDate.after(endDate)) { // 不在时间区间内
			flag = false;
			notSatisfyStr.append(" 必须在");
			if (DATE_1970_01_01.after(startDate)) {
				notSatisfyStr.append(DateUtil.datetimeToString(endDate))
						.append("之前预订。");
			} else {
				notSatisfyStr.append(DateUtil.datetimeToString(startDate));
				if (DATE_2099_01_01.before(endDate)) {
					notSatisfyStr.append("之后预订。");
				} else {
					notSatisfyStr.append("和").append(
							DateUtil.datetimeToString(endDate)).append("之间预订。");
				}
			}
		}
		
		if(flag) {
			long bookDays = DateUtil.getDay(checkinDate, checkoutDate);		
			long continueDays = null == queryInfo.getContinueDay() ? 0 : queryInfo
					.getContinueDay().longValue();// 连住
			if (bookDays < continueDays) {
				notSatisfyStr.append(" 必须连住").append(continueDays)
						.append("晚(含").append(continueDays).append("晚)");
				flag = false;
			}
			
			if(flag) {
				Long restrictInDays = queryInfo.getRestrictIn();// 限住
				if (null != restrictInDays && 0 < restrictInDays.longValue()) {
					if (bookDays != restrictInDays.longValue()) {
						notSatisfyStr.append("  限住" + restrictInDays + "晚");
						flag = false;
					}
				}
				
				if(flag) {
					// 必住日期判断
					String mustIn = queryInfo.getMustIn();
					if (StringUtil.isValidStr(mustIn)) {
						if (!checkMustInDate(queryInfo, notSatisfyStr, checkinDate,
								checkoutDate, 0 >= notSatisfyStr.length())) {
							flag = false;
						}
					}						
				}
			}	
		}				
		
		saleInfo.setSatisfyClause(flag);//是否满足条款
		saleInfo.setNotsatisfyClauseOfReason(notSatisfyStr.toString());//不满足条款的原因
	}
	
    /**
     * 
     * 对必住日期进行逻辑判断的方法
     * 
     * @param queryInfo
     * @param bookHintNoMeet
     * @param checkInDate
     * @param checkOutDate
     * @param bFirst
     * @return
     */
    private boolean checkMustInDate(QueryCommodityInfo queryInfo,
        StringBuilder bookHintNoMeet, Date checkInDate, Date checkOutDate, boolean bFirst) {

        List<MustDate> mustInDates = new ArrayList<MustDate>(2);
        int type = MustDate.getMustIndatesAndType(mustInDates, queryInfo.getMustIn());
        boolean isCanLive = false;
        StringBuilder noMeet = new StringBuilder();
        String mustDatesRelation = queryInfo.getContinueDatesRelation();
        if (!StringUtil.isValidStr(mustDatesRelation) || mustDatesRelation.equals("or")) {// 里边为 或者
                                                                                          // 逻辑判断
            // 得到必住日期集合
            if (type == MustDate.DATE_TYPE) {// 必住日期逻辑
                for (MustDate date : mustInDates) {
                    // //如果入住日期包括任意一个必住日期即可入住
                    if (DateUtil.isBetween(date.getContinueDate(), checkInDate, checkOutDate)) {
                        isCanLive = true;
                        break;
                    }
                    noMeet.append(DateUtil.dateToString(date.getContinueDate())).append(",");
                }
                if (!isCanLive) {
                    if (bFirst) {
                        bookHintNoMeet.append("入住此房型，住店日期需至少包含");
                    } else {
                        bookHintNoMeet.append("并且住店日期需至少包含");
                    }
                    noMeet.deleteCharAt(noMeet.length() - 1);
                    bookHintNoMeet.append(noMeet.toString());
                    if (bFirst) {
                        bookHintNoMeet.append("中任意一天方可接受预订。");
                    } else {
                        bookHintNoMeet.append("中任意一天。");
                    }
                }
            } else if (type == MustDate.WEEK_TYPE) {// 必住星期逻辑
                for (MustDate date : mustInDates) {
                    if (DateUtil.isBetween(checkInDate, date.getContinueDate(), date
                        .getContinueEndDate())) {
                        String[] checkInWeeks = date.getWeeks().split(",");
                        Date[] checkInDates = DateUtil.getDateWithWeek(checkInDate,
                        		checkOutDate, checkInWeeks);
                        if (0 < checkInDates.length) {// 说明入住日期内已经至少包含有一个必住星期
                            isCanLive = true;
                        } else {
                            noMeet.append("从")
                                .append(DateUtil.dateToString(date.getContinueDate())).append("至")
                                .append(DateUtil.dateToString(date.getContinueEndDate())).append(
                                    "期间的星期").append(date.getWeeks());
                        }
                        break;
                    }
                }
                // 如果不能入住,则显示提示信息
                if (!isCanLive) {
                    if (bFirst) {
                        bookHintNoMeet.append("入住此房型，住店日期需至少包含");
                    } else {
                        bookHintNoMeet.append("并且住店日期需至少包含");
                    }
                    bookHintNoMeet.append(noMeet.toString());
                    if (bFirst) {
                        bookHintNoMeet.append("中任意一天方可接受预订。");
                    } else {
                        bookHintNoMeet.append("中任意一天。");
                    }
                }
            }
        } else {// 里边为 并且 逻辑判断
            if (type == MustDate.DATE_TYPE) {// 必住日期逻辑
                if (!DateUtil.isBetween(queryInfo.getContinuumInStart(), checkInDate, checkOutDate)
                    || !DateUtil.isBetween(queryInfo.getContinuumInEnd(), checkInDate, checkOutDate)) {
                    // 不能入住
                    if (bFirst) {
                        bookHintNoMeet.append("入住此房型，住店日期需包含");
                    } else {
                        bookHintNoMeet.append("并且住店日期需包含");
                    }
                    for (MustDate date : mustInDates)
                        noMeet.append(DateUtil.dateToString(date.getContinueDate())).append(",");
                    noMeet.deleteCharAt(noMeet.length() - 1);
                    bookHintNoMeet.append(noMeet.toString());
                    if (bFirst) {
                        bookHintNoMeet.append("方可接受预订。");
                    } else {
                        bookHintNoMeet.append("。");
                    }
                } else
                    isCanLive = true;// 可以入住
            } else if (type == MustDate.WEEK_TYPE) {// 必住星期逻辑
                for (MustDate date : mustInDates) {
                    if (DateUtil.isBetween(checkInDate, date.getContinueDate(), date
                        .getContinueEndDate())) {
                        String[] checkInWeeks = date.getWeeks().split(",");
                        Date[] checkInDates = DateUtil.getDateWithWeek(checkInDate,
                        		checkOutDate, checkInWeeks);
                        if (checkInDates.length >= checkInWeeks.length) {// 说明入住日期内已经至少包含有一个整体的必住星期
                            isCanLive = true;
                        } else {
                            noMeet.append("从")
                                .append(DateUtil.dateToString(date.getContinueDate())).append("至")
                                .append(DateUtil.dateToString(date.getContinueEndDate())).append(
                                    "期间的逢星期").append(date.getWeeks());
                        }
                        break;
                    }
                }
                // 如果不能入住,则显示提示信息
                if (!isCanLive) {
                    if (bFirst) {
                        bookHintNoMeet.append("入住此房型，住店日期需包含");
                    } else {
                        bookHintNoMeet.append("并且住店日期需包含");
                    }
                    bookHintNoMeet.append(noMeet.toString());
                    if (bFirst) {
                        bookHintNoMeet.append("方可接受预订。");
                    } else {
                        bookHintNoMeet.append("。");
                    }
                }
            }
        }
        return isCanLive;
    }
	
	/**
	 * 判断每天的商品能预订,如果不能预订,说明原因
	 * 关房
	 * 不满足条款
	 * 价格为0
	 * 满房
	 * @param queryInfo
	 * @param saleInfo
	 * @param checkinDate
	 * @param checkoutDate
	 * @return
	 */
	public void setCanbookPerDay(SaleInfo saleInfo,QueryCommodityCondition qcc){

		String disableBookReason = "";
		
		boolean roomState = false;
		RoomStateByBedType curRoomState = null;
		for(Map.Entry<String, RoomStateByBedType> roomStateEntry:saleInfo.getRoomstateMaps().entrySet()){
			curRoomState = roomStateEntry.getValue();
			
			/**
			 * MT 2011-7-20 新增有房可超判断
			 */
			if(curRoomState.isHasRoom()){//不满房就能预订,配额暂时不判断.只要有一种床型有房型,系统则认为该房型可以预订
				if(curRoomState.isHasoverdraft()){//可透支
					roomState = true;
				}else{//不能透支
					if(curRoomState.getQuotanumber()<qcc.getRoomAmount()){
						roomState = false;
					}else{
						roomState = true;
					}
				}
			//	roomState = true;
			}else{
				roomState = false;//不能预订
			}
			
			if(roomState){//只要有一种床型能订就OK
				break;
			}
		}//end for
		
		
		boolean flag = true;
		if(OpenCloseRoom.CloseRoom.equals(saleInfo.getCloseflag())){//关房
			flag = false;
			disableBookReason =NotBookReason.CloseRoom;
		}else if(0.1 > saleInfo.getSalePrice() || 800000 < saleInfo.getSalePrice()){//价格为0
			flag = false;
			disableBookReason =NotBookReason.NoPrice;
		}else if(roomState == false){//不满足房态和配额
			flag = false;
			disableBookReason =NotBookReason.Roomful;
		}else if(0==DateUtil.getDay(saleInfo.getAbleDate(), qcc.getInDate())){//首日
			if(!saleInfo.isSatisfyClause()){//不满足条款
				flag = false;
				disableBookReason = NotBookReason.NotSatisfyClause;
			}
		}
		saleInfo.setBookEnAble(flag);//能否预订
		
		saleInfo.setReasonOfDisableBook(disableBookReason);//不可预订原因
	}
	
	
	/**
	 * 优惠信息List转换成Map	
	 * @param htlFavourableclauseList
	 * @return
	 */
	private Map<String,List<HtlFavourableclause>> changeFavourableListToMap(List<HtlFavourableclause> htlFavourableclauseList){
    	//缓存参数传入的所有价格类型连住优惠信息
    	Map<String,List<HtlFavourableclause>> favourableMap = new HashMap<String,List<HtlFavourableclause>>();
    	if(null==htlFavourableclauseList||htlFavourableclauseList.isEmpty()) return favourableMap;
	    	//创建所有的key
	    	for (Iterator it = htlFavourableclauseList.iterator(); it.hasNext();) {
				HtlFavourableclause htlFavourableclause = (HtlFavourableclause) it.next();
				//格式: 酒店id_价格类型id
				String mapKey = htlFavourableclause.getHotelId() + "_" + htlFavourableclause.getPriceTypeId();
				favourableMap.put(mapKey, new ArrayList<HtlFavourableclause>());
			}
	    	//对每个key的值进行赋值
	    	for (Iterator it = htlFavourableclauseList.iterator(); it.hasNext();) {
				HtlFavourableclause htlFavourableclause = (HtlFavourableclause) it.next();
				//格式: 酒店id_价格类型id
				String mapKey = htlFavourableclause.getHotelId() + "_" + htlFavourableclause.getPriceTypeId();
	        	//缓存某个酒店价格类型连住优惠信息
				List<HtlFavourableclause> favourableclauseListCached = favourableMap.get(mapKey);
				favourableclauseListCached.add(htlFavourableclause);
				favourableMap.put(mapKey, favourableclauseListCached);
			}

    	return favourableMap;
    }
	
	
	/**
	 * Map<String,Map<String,List<QueryCommodityInfo>>>，第一个String是hotelid_priceid
	 * 第二个string是hotelid_priceid_bed,
	 * @param commodityLst
	 * @return
	 */
	private Map<String,Map<String,List<QueryCommodityInfo>>> changeQueryCommodityInfoListToMap(List<QueryCommodityInfo> commodityLst){
		
		Map<String,List<QueryCommodityInfo>> commodityInfoMap = new HashMap<String,List<QueryCommodityInfo>>();
		Map<String,Map<String,List<QueryCommodityInfo>>> resutlMap = new HashMap<String,Map<String,List<QueryCommodityInfo>>>();
		//组装key:key格式为hotelid_priceid
		for(QueryCommodityInfo commodityInfo : commodityLst){
			commodityInfoMap.put(commodityInfoMapKey(commodityInfo), new ArrayList<QueryCommodityInfo>());
		}
		//根据key赋值
		for(QueryCommodityInfo commodityInfo : commodityLst){
			List<QueryCommodityInfo> commodityInfoList= commodityInfoMap.get(commodityInfoMapKey(commodityInfo));
			commodityInfoList.add(commodityInfo);
			commodityInfoMap.put(commodityInfoMapKey(commodityInfo), commodityInfoList);
		}
		//把数据组装成需要返回的数据格式
		for(Map.Entry<String, List<QueryCommodityInfo>> queryentrySet: commodityInfoMap.entrySet()){
			//每个酒店都需生成一个新的对象
			Map<String,List<QueryCommodityInfo>> commodityInfoBedMap = new HashMap<String,List<QueryCommodityInfo>>();
			//组装key：key格式hotelid_priceid_bedtype
			for(QueryCommodityInfo commodityInfo : queryentrySet.getValue()){
				commodityInfoBedMap.put(addBedtypeCommodityInfoMapKey(commodityInfo), new ArrayList<QueryCommodityInfo>());
			}
			//赋值
			for(QueryCommodityInfo commodityInfo : queryentrySet.getValue()){
				List<QueryCommodityInfo> comList= commodityInfoBedMap.get(addBedtypeCommodityInfoMapKey(commodityInfo));
				comList.add(commodityInfo);
				commodityInfoBedMap.put(addBedtypeCommodityInfoMapKey(commodityInfo), comList);
			}
			resutlMap.put(queryentrySet.getKey(), commodityInfoBedMap);
		}
		
		
		return resutlMap;
	}
	
	/**
	 * key is : hotelid_priceid_bedtype_payMethod
	 * @param commodityInfo
	 * @return
	 */
	private String addBedtypeCommodityInfoMapKey(QueryCommodityInfo commodityInfo){
		return commodityInfo.getHotelId()+"_"+commodityInfo.getCommodityId()+"+"+commodityInfo.getBedtype()+"_"+commodityInfo.getPaymethod();
	}
	
	/**
	 * change map to list
	 * @param mapQci
	 * @return
	 */
	private List<QueryCommodityInfo> changeMapQciMapToResultList(Map<String, Map<String, List<QueryCommodityInfo>>> mapQci){
		//组装成需要返回的数据格式
		List<QueryCommodityInfo> newCommodityLst = new ArrayList<QueryCommodityInfo>();
		for (Map.Entry<String, Map<String,List<QueryCommodityInfo>>> newMapQci : mapQci.entrySet()) {
			Map<String,List<QueryCommodityInfo>> newCommodityInfoMap = newMapQci.getValue();
			for(Map.Entry<String, List<QueryCommodityInfo>> newComEntrySet: newCommodityInfoMap.entrySet()){
				for (QueryCommodityInfo queryCommodityInfo : newComEntrySet.getValue()) {
					newCommodityLst.add(queryCommodityInfo);
				}
			}
		}
		return newCommodityLst;
	}
	/**
	 * key is : hotelid_priceid
	 * @param commodityInfo
	 * @return
	 */
	private String commodityInfoMapKey(QueryCommodityInfo commodityInfo){
		return commodityInfo.getHotelId()+"_"+commodityInfo.getCommodityId();
	}
	
    /**
     * 
     * 根据酒店id获取所在城市code
     * 
     * @param hotelId
     * @return
     */
    public String getCityCodeByHotelId(Long hotelId) {
    	return hotelInfoDAO.getCityCodeByHotelId(hotelId);
    }
	
	class QueryCommodityInfoComparator implements Comparator<QueryCommodityInfo>{
		public int compare(QueryCommodityInfo o1, QueryCommodityInfo o2) {
			return o1.getAbledate().compareTo(o2.getAbledate());
		}
	}	
	

	public void setHotelQueryDao(HotelQueryDao hotelQueryDao) {
		this.hotelQueryDao = hotelQueryDao;
	}

	public void setHotelInfoDAO(HotelInfoDAO hotelInfoDAO) {
		this.hotelInfoDAO = hotelInfoDAO;
	}

	public void setLimitFavourableManage(
			HtlLimitFavourableManage limitFavourableManage) {
		this.limitFavourableManage = limitFavourableManage;
	}

	public ChannelCashBackManagerService getChannelCashBackService() {
		return channelCashBackService;
	}

	public void setChannelCashBackService(
			ChannelCashBackManagerService channelCashBackService) {
		this.channelCashBackService = channelCashBackService;
	}
}

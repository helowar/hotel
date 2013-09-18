package com.mangocity.hotel.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.dao.HotelQueryDao;
import com.mangocity.hotel.base.dao.IContractDao;
import com.mangocity.hotel.base.persistence.HtlEveningsRent;
import com.mangocity.hotel.base.persistence.HtlFavouraParameter;
import com.mangocity.hotel.base.persistence.HtlFavourableclause;
import com.mangocity.hotel.base.persistence.HtlSalesPromo;
import com.mangocity.hotel.base.resource.ResourceManager;
import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.hotel.order.constant.LocalFlag;
import com.mangocity.hotel.order.dao.INewOrderParamDao;
import com.mangocity.hotel.order.service.INewOrderParamService;
import com.mangocity.hotel.order.service.assistant.BaseParams;
import com.mangocity.hotel.order.service.assistant.BedTypeQuota;
import com.mangocity.hotel.order.service.assistant.BedTypeState;
import com.mangocity.hotel.order.service.assistant.EverydayParams;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;

public class NewOrderParamServiceImpl implements INewOrderParamService {
	
	
	private INewOrderParamDao dao;
	private IContractDao contractDao;
	private ResourceManager resourceManager;
	private IHotelService hotelService;
	private UserWrapper user;
	private HotelQueryDao hotelQueryDao;
	
	public BaseParams getBaseParams(String priceTypeID, String checkinDate,
			String checkoutDate, String payMethod,UserWrapper roleUser) {
		// TODO Auto-generated method stub
		
		//<c:if test="${hotel.hasPreSale==1 || roomTypeItem.hasPromo==1}">1</c:if>
		
		
		user=roleUser;
		System.out.println("dao.searchNewOrderParams(priceTypeID,checkinDate,checkoutDate);");
		List<Object[]> list = dao.searchNewOrderParams(priceTypeID,
				checkinDate, checkoutDate);
		System.out.println("list size :" + list.size());
		if (list.size() <= 0) {
			return null;
		}
		BaseParams params=this.createBookPrame(list);

		// 判断床型的房态，在所有时间段内如果床型都有房态设为不可超那么设置床型房态标志		
		List<Object[]> everydayState = dao.searchEverdayParams(priceTypeID,checkinDate, checkoutDate, payMethod);
		Map<String, BedTypeState> bedTypeStateMap=createBedTypeStateMap(everydayState);
		List<Object[]> everydayQuota = dao.searchQuotaParams(priceTypeID,checkinDate, checkoutDate, payMethod);
		Map<String, BedTypeQuota> bedTypeQuotaMap=createBedTypeQuotaMap(everydayQuota);
		
		params.setBedStateOneBool(false);
		params.setBedStateTwoBool(false);
		params.setBedStateThrBool(false);
		if (bedTypeStateMap != null && bedTypeStateMap.size() != 0) {

			for (BedTypeState state : bedTypeStateMap.values()) {
				if (state.getBedType().equals("大")&& state.getRoomState().equals("不")) {
					params.setBedStateOneBool(true);
				} else if (state.getBedType().equals("双")&& state.getRoomState().equals("不")) {
					params.setBedStateTwoBool(true);
				} else if (state.getBedType().equals("单")&& state.getRoomState().equals("不")) {
					params.setBedStateThrBool(true);
				}
			}
		}		
					
		//查询出该子房型下在该时间段的提示信息字符串
		String priceTypeclueInfo=hotelService.queryAlertInfoStr(Long.valueOf(params.getHotelId()),priceTypeID,DateUtil.getDate(checkinDate),DateUtil.getDate(checkoutDate),LocalFlag.CC);
						
		//子房型的提示信息字符串
		priceTypeclueInfo= priceTypeclueInfo.replace("\r\n", "");
		params.setTipContent(priceTypeclueInfo);			
		
		if(params.getClueInfo()!=null){
			String temp=params.getClueInfo().replace("\r\n", "");
			params.setClueInfo(temp);
		}
		
		List<EverydayParams> everydayParamsList = getEverdayParams(
				everydayState, everydayQuota, params.getHotelId(), priceTypeID,
				DateUtil.getDate(checkinDate), DateUtil.getDate(checkoutDate));

		if(null==everydayParamsList){
			return null;
		}
		
		//组装每天的配额房态信息
		int countTemp[] = new int[everydayParamsList.size()];
		int k=0;
		for (EverydayParams ep : everydayParamsList) {			

			 
				if (ep.gethRoomStatus().indexOf("不")>0) {
					countTemp[k] = ep.getQuotaNum();

				} else {
					countTemp[k] = 9999;
				}
				k++;
		}
		int quotaLeastNum = 9999;
		params.setQuotaBool(false);
		for (int quotCount : countTemp) {
			// 判断配额最小数
			if (quotCount < quotaLeastNum) {
				quotaLeastNum = quotCount;
			}
		    // 判断配额数量是否不为0，填写标志
			if (1 > quotCount) {
				params.setQuotaBool(true);
			}
		}
		params.setQuotaLeastNum(quotaLeastNum);
		params.setEverydayParams(everydayParamsList);
		//获取芒果网促销信息
		List htlPresale=contractDao.getPreOrderPresaleList(Long.valueOf(params.getHotelId()), DateUtil.getDate(checkinDate),DateUtil.getDate(checkoutDate));
		//获取芒果网促销信息
		List mangoPresale=contractDao.getPreOrderSalePromos(Long.valueOf(params.getHotelId()), DateUtil.getDate(checkinDate),DateUtil.getDate(checkoutDate));
		
		if(0!=htlPresale.size()){
			params.setIsSalesPromo("1");
		}else if(0!=mangoPresale.size()){
			Iterator <HtlSalesPromo>it = mangoPresale.iterator();			
			while(it.hasNext()){
				HtlSalesPromo htlSalesPromo = (HtlSalesPromo)it.next();
				if(htlSalesPromo.getRoomType().toString().indexOf(priceTypeID)>=0){
					params.setIsSalesPromo("1");
					break;
				}				
			}
			params.setIsSalesPromo(params.getIsSalesPromo()!=null && params.getIsSalesPromo().equals("1") ? "1" : "0");
		}else{
			params.setIsSalesPromo("0");
		}


		return params;
	}
	
	//用于封装每天的售卖信息
	public List<EverydayParams> getEverdayParams(List<Object[]> everydayParams,List<Object[]> everydayQuota,Long hotelId,String priceTypeId,Date checkInDate,Date checkOutDate) {
		// TODO Auto-generated method stub
		
		if (everydayParams.size() == 0 || everydayQuota.size() == 0) {
			return null;
		}
		List<EverydayParams> everydayParamsList=createEverdayParams(everydayParams, everydayQuota,hotelId,priceTypeId,checkInDate,checkOutDate);
		Map<String, BedTypeState> bedTypeStateMap=createBedTypeStateMap(everydayParams);
		Map<String, BedTypeQuota> bedTypeQuotaMap=createBedTypeQuotaMap(everydayQuota);
		//判断是否床型共享，如果床型共享则该床型为true
		boolean bedTypeTemp[]={true,true,true};
		//判断是否床型共享，如果床型共享
		boolean bedshare=true;

		for (BedTypeQuota  quota:bedTypeQuotaMap.values()) {			
				if(quota.getBedType().indexOf("1")<0){
					bedTypeTemp[0]=false;
				}
				if(quota.getBedType().indexOf("2")<0){
					bedTypeTemp[1]=false;
				}
				if(quota.getBedType().indexOf("3")<0){
					bedTypeTemp[2]=false;
				}
				if(!quota.getQuotaBedShare().equals("1")){
					bedshare=false;
			   }

		}
		
		// 对EverydayParamsList进行排序
		for (int i = 0; i < everydayParamsList.size(); i++) {
			for (int j = i + 1; j < everydayParamsList.size(); j++) {
				Date date1 = DateUtil.getDate(everydayParamsList.get(i)
						.getAbleSaleDate());
				Date date2 = DateUtil.getDate(everydayParamsList.get(j)
						.getAbleSaleDate());
				if (DateUtil.getDay(date1, date2) < 0) {
					EverydayParams tem = everydayParamsList.get(i);
					everydayParamsList.set(i, everydayParamsList.get(j));
					everydayParamsList.set(j, tem);
				}
			}
		}
		//判断床型共享
		if(bedshare){
			for (int k = 0; k < everydayParamsList.size(); k++) {
				EverydayParams p= everydayParamsList.get(k);
			    String RoomStats=getAbleBedTypes( p,bedTypeTemp,bedTypeStateMap);

				p.sethRoomStatus(RoomStats);
				String[] status=RoomStats.split(",");
				String quotaNumber=status[0].substring(status[0].indexOf(":")+1,status[0].length());
				p.sethQuantity(quotaNumber);
			}
		}else{
			for (int k = 0; k < everydayParamsList.size(); k++) {
				EverydayParams p= everydayParamsList.get(k);				
				String RoomStats=getAbleBedTypesIgnoreQuota( p,bedTypeTemp,bedTypeStateMap);
				p.sethRoomStatus(RoomStats);
			}
			
		}
				
		return everydayParamsList;
	}

	
	
	//组装List<EverydayParams>对象，用于封装每天的售卖信息
	public List<EverydayParams> createEverdayParams(
			List<Object[]> everydayParams, List<Object[]> everydayQuota,
			Long hotelId, String priceTypeId, Date checkInDate,
			Date checkOutDate) {
		// TODO Auto-generated method stub

		System.out.println("=======组装 everydayParams and everydayQuota========everydayParams : everydayQuota size"+ everydayParams.size() + ":" + everydayQuota.size());
		List<Object[]> list = new ArrayList<Object[]>();
		Object[] everyday = null;
		Map<String, Object[]> everydayPMap = new HashMap<String, Object[]>();
		Map<String, Object[]> everydayQMap = new HashMap<String, Object[]>();

		for (int k = 0; k < everydayParams.size(); k++) {
			Object[] ep = everydayParams.get(k);
			everydayPMap.put(ep[3].toString() + ep[4].toString(), ep);
			
		}
		for (int j = 0; j < everydayQuota.size(); j++) {
			Object[] eq = everydayQuota.get(j);
			everydayQMap.put(eq[8].toString() + eq[6].toString(), eq);
		}

		for (String key : everydayPMap.keySet()) {
			Object[] ep = everydayPMap.get(key);
			Object[] eq = everydayQMap.get(key);

			everyday = new Object[23];
			if (eq != null) {
				for (int m = 0; m < eq.length; m++) {
					everyday[m] = eq[m];
				}

				for (int n = 0; n < ep.length; ++n) {
					everyday[eq.length + n] = ep[n];
				}
				list.add(everyday);
			}

		}

		System.out.println("=======组装 everydayParams and everydayQuota 结束========"+ list.size());

		
		System.out.println("======= List<EverydayParams>赋值开始=============");
		//封装每天的售卖信息
		List<EverydayParams> EverydayParamsList = new ArrayList<EverydayParams>();
	
		EverydayParams eParams = null;
		//临时存储EverydayParams
		Map<String, EverydayParams> tempMap = new HashMap<String, EverydayParams>();
		
		for (int k = 0; k < list.size(); k++) {
			Object[] obj = list.get(k);
			StringBuffer status = new StringBuffer();
			int quotaCount = 0;
			obj[0] = obj[0] == null ? 0 : obj[0];
			obj[1] = obj[1] == null ? 0 : obj[1];
			obj[2] = obj[2] == null ? 0 : obj[2];
			int quotaAbleCount = Integer.parseInt(obj[0].toString())
					+ Integer.parseInt(obj[1].toString())
					+ Integer.parseInt(obj[2].toString());
			int quotaOCount = Integer.parseInt(obj[14].toString())
			+ Integer.parseInt(obj[15].toString())
			+ Integer.parseInt(obj[16].toString());
			
			quotaCount=(quotaAbleCount-quotaOCount<0)?0:(quotaAbleCount-quotaOCount);
			// 去掉配额为0 并且房态为不可超的床型
//			if (quotaCount == 0 && obj[19].toString().equals("3")) {
//				continue;
//			}
			if (eParams == null) {
				eParams = new EverydayParams();
				String bedType = null;
				String bedStatus = null;
				if (obj[21] != null && obj[22] != null) {
					bedType = resourceManager.getDescription("bedTypeForCC",
							obj[21].toString());
					bedStatus = resourceManager.getDescription(
							"select_roomStatusForCC", obj[22].toString());
					status.append(bedType + ":" + bedStatus + ":" + quotaCount);
					eParams.sethRoomStatus(status.toString());
				}
				eParams.setQuotaNum(quotaCount);
				if (obj[11] != null) {
					eParams.sethSalePrice(obj[11].toString());
				}
				if (obj[3] != null && obj[5] != null) {
					String breakfastType = resourceManager.getDescription(
							"breakfast_typeForCC", obj[3].toString());
					String breakfastNum = resourceManager.getDescription(
							"breakfast_num_new", obj[5].toString());
					eParams.sethBreakfast(breakfastType + ":" + breakfastNum);
				}
				
				if (obj[3] != null) {
					eParams.sethBreakfasts(obj[3].toString());
				}
				if (obj[5] != null) {
					eParams.sethBreakNum(obj[5].toString());
				}
				if (obj[9] != null) {
					eParams.sethBasePrice(obj[9].toString());
				}
				eParams.sethQuantity(new Integer(quotaCount).toString());

				if (obj[10] != null) {
					eParams.sethMarketPrice(obj[10].toString());
				}
				if (obj[8] != null) {
					eParams.setAbleSaleDate(obj[8].toString());
				}
				eParams.setMaxQty(null);
				// EverydayParamsList.add(eParams);
				tempMap.put(obj[8].toString(), eParams);
			} else if (eParams != null
					&& tempMap.get(obj[8].toString()) != null) {
				eParams = tempMap.get(obj[8].toString());
				String rs = eParams.gethRoomStatus();
				if (obj[21] != null && obj[22] != null) {
					String bedType = resourceManager.getDescription(
							"bedTypeForCC", obj[21].toString());
					String bedStatus = resourceManager.getDescription(
							"select_roomStatusForCC", obj[22].toString());
					eParams.sethRoomStatus(rs + "," + bedType + ":" + bedStatus
							+ ":" + quotaCount);
				}
				eParams.setQuotaNum(eParams.getQuotaNum()+quotaCount);
				tempMap.put(obj[8].toString(), eParams);
			} else {
				eParams = new EverydayParams();
				String bedType = null;
				String bedStatus = null;
				obj[0] = obj[0] == null ? 0 : obj[0];
				obj[1] = obj[1] == null ? 0 : obj[1];
				obj[2] = obj[2] == null ? 0 : obj[2];

				if (obj[21] != null && obj[22] != null) {
					bedType = resourceManager.getDescription("bedTypeForCC",
							obj[21].toString());
					bedStatus = resourceManager.getDescription(
							"select_roomStatusForCC", obj[22].toString());
					status.append(bedType + ":" + bedStatus + ":" + quotaCount);
					eParams.sethRoomStatus(status.toString());
				}
				if (obj[11] != null) {
					eParams.sethSalePrice(obj[11].toString());
				}
				eParams.setQuotaNum(quotaCount);
				if (obj[3] != null && obj[5] != null) {
					String breakfastType = resourceManager.getDescription(
							"breakfast_typeForCC", obj[3].toString());
					String breakfastNum = resourceManager.getDescription(
							"breakfast_num_new", obj[5].toString());
					eParams.sethBreakfast(breakfastType + ":" + breakfastNum);
				}
				if (obj[3] != null) {
					eParams.sethBreakfasts(obj[3].toString());
				}
				if (obj[5] != null) {
					eParams.sethBreakNum(obj[5].toString());
				}
				if (obj[9] != null) {
					eParams.sethBasePrice(obj[9].toString());
				}
				eParams.sethQuantity(new Integer(quotaCount).toString());

				if (obj[10] != null) {
					eParams.sethMarketPrice(obj[10].toString());
				}
				if (obj[8] != null) {
					eParams.setAbleSaleDate(obj[8].toString());
				}
				eParams.setMaxQty(null);
				// EverydayParamsList.add(eParams);
				tempMap.put(obj[8].toString(), eParams);
			}
		}
		for (EverydayParams pa : tempMap.values()) {
			pa.sethRoomStatus(pa.gethRoomStatus()+", ");
			EverydayParamsList.add(pa);
		}

		//设置优惠 修改价格 add by wupingxiang 2012-9-5
		List<HtlFavourableclause> htlFavourableclauseList = hotelQueryDao
				.queryFavourableByHotelIdAndPriceTypeId(hotelId, priceTypeId,
						checkInDate, checkOutDate);
		for (HtlFavourableclause htlFavourableclause : htlFavourableclauseList) {
			Date inDate = checkInDate
					.before(htlFavourableclause.getBeginDate()) ? htlFavourableclause
					.getBeginDate() : checkInDate;
			Date outDate = DateUtil.getDay(htlFavourableclause.getEndDate(), checkOutDate)>1 ? DateUtil.getDate(htlFavourableclause
						.getEndDate(),1) : checkOutDate;
			
			for (EverydayParams pa : EverydayParamsList) {
				if (htlFavourableclause != null) {
					if ("1".equals(htlFavourableclause.getFavourableType())) {
						continuePreferential(outDate, htlFavourableclause,
								inDate, pa);
					} else if ("2".equals(htlFavourableclause
							.getFavourableType())) {
						discountPreferential(htlFavourableclause, pa);
					} else if ("3".equals(htlFavourableclause
							.getFavourableType())) {
						packagePrice(htlFavourableclause, inDate, outDate, pa);
					}
				}
			}
		}
		System.out.println("======= List<EverydayParams>赋值结束============="+ EverydayParamsList.size());		
		return EverydayParamsList;
	}

	/**
	 * 包价优惠
	 * @param htlFavourableclause
	 * @param inDate
	 * @param outDate
	 * @param pa
	 */
	private void packagePrice(HtlFavourableclause htlFavourableclause,
			Date inDate, Date outDate, EverydayParams pa) {
		int i = 0;
		if(!DateUtil.getDate(pa.getAbleSaleDate()).before(htlFavourableclause.getBeginDate()) 
				&& !DateUtil.getDate(pa.getAbleSaleDate()).after(htlFavourableclause.getEndDate())){
			List<HtlFavouraParameter> lstPackagerate = htlFavourableclause.getLstPackagerate();
			if(null != lstPackagerate){
				for(int j = 0; j < lstPackagerate.size();j++ ){
					HtlFavouraParameter parameter = lstPackagerate.get(j);
					int night = parameter.getPackagerateNight().intValue();
					i = DateUtil.getDay(inDate,outDate)/night;
					if (i > 0){
						for (int k = 0;k < i; k++){
							if (DateUtil.getDay(inDate,DateUtil.getDate(pa.getAbleSaleDate())) == night*k){
								pa.sethSalePrice(parameter.getPackagerateSaleprice().toString());
								break;
							}else if (DateUtil.getDay(inDate,DateUtil.getDate(pa.getAbleSaleDate())) < night*(k+1)){
								pa.sethSalePrice("0");
								break;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 打折优惠
	 * @param htlFavourableclause
	 * @param pa
	 */
	private void discountPreferential(HtlFavourableclause htlFavourableclause,
			EverydayParams pa) {
		if(!DateUtil.getDate(pa.getAbleSaleDate()).before(htlFavourableclause.getBeginDate()) 
				&& !DateUtil.getDate(pa.getAbleSaleDate()).after(htlFavourableclause.getEndDate())){
			List<HtlFavouraParameter> lstPackagerate = htlFavourableclause.getLstPackagerate();
			if(null != lstPackagerate){
				for(int j = 0; j < lstPackagerate.size();j++ ){
					
					BigDecimal b = new BigDecimal(""+lstPackagerate.get(j).getDiscount()*new Double(pa.gethSalePrice())/10);
					//逢一进十
					if(1 == lstPackagerate.get(j).getDecimalPointType()){
						pa.sethSalePrice(Math.ceil(b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()) + "");
					}
					//四舍五入取一位小数
					if(2 == lstPackagerate.get(j).getDecimalPointType()){
						pa.sethSalePrice(b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue() + "") ;
					}
					//直接取整
					if(3 == lstPackagerate.get(j).getDecimalPointType()){
						pa.sethSalePrice(Math.floor(lstPackagerate.get(j).getDiscount()*new Double(pa.gethSalePrice())/10) + "");
					}
					//四舍五入取整
					if(4 == lstPackagerate.get(j).getDecimalPointType()){
						pa.sethSalePrice(b.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue() + "") ;
					}
				}
			}
		}
	}

	/**
	 * 连住M晚送N晚优惠
	 * @param htlFavourableclause
	 * @param inDate
	 * @param pa
	 * @param checkOutDate
	 */
	private void continuePreferential(Date outDate, HtlFavourableclause htlFavourableclause,
			Date inDate, EverydayParams pa) {
		int i = 0;
		if(!DateUtil.getDate(pa.getAbleSaleDate()).before(htlFavourableclause.getBeginDate()) 
				&& !DateUtil.getDate(pa.getAbleSaleDate()).after(htlFavourableclause.getEndDate())){
			List<HtlFavouraParameter> lstPackagerate = htlFavourableclause
			.getLstPackagerate();
			if (null != lstPackagerate) {
				for (int j = 0; j < lstPackagerate.size(); j++) {
					int ConNumD = (int) (lstPackagerate.get(j)
							.getContinueNight() + 0);
					int doNumD = (int) (lstPackagerate.get(j)
							.getDonateNight() + 0);
					List<HtlEveningsRent> lstEveningsRent = lstPackagerate
					.get(j).getLstEveningsRent();
					int NumDay = ConNumD + doNumD;
					i = DateUtil.getDay(inDate, outDate)/NumDay;
					//不循环计算
					if (i > 0 && 2 == lstPackagerate.get(j)
							.getCirculateType()) {
						i = 1;
					}
					if (i > 0){
						for (int k = 0 ; k < i; k++){
							for (int aa = 0; aa < lstEveningsRent
							.size(); aa++) {
								int night = (int) (lstEveningsRent.get(
										aa).getNight() + 0);
								if (DateUtil.getDay(inDate,DateUtil.getDate(pa.getAbleSaleDate())) == NumDay*k+night -1 ){
									if (0 == (int) (lstEveningsRent
											.get(aa).getSalePrice() + 0)) {
										pa.sethSalePrice("0");
									} else {
										pa.sethSalePrice(lstEveningsRent
												.get(aa).getSalePrice()	+ "");
									}
									break;
								}
							}
						}
					}
				}
			}
		}
	}
	
	/*
	 * 用于将每天床型房态信息Map<key,value> key：日期+床型    value:BedTypeState（当天的床型房态信息）
	 */
	public Map<String, BedTypeState> createBedTypeStateMap(List<Object[]> everydayParams){
		Map<String, BedTypeState> bedTypeStuList = new HashMap<String, BedTypeState>();
		for (int k = 0; k < everydayParams.size(); k++) {
			Object[] ep = everydayParams.get(k);
			BedTypeState bedTypeSta=new BedTypeState();
			bedTypeSta.setAbleSaleDate(ep[3].toString());			
			String bedStatus = resourceManager.getDescription(
					"select_roomStatusForCC", ep[5].toString());
			
			String bedType = resourceManager.getDescription("bedTypeForCC",
					ep[4].toString());
			bedTypeSta.setBedType(bedType);
			bedTypeSta.setRoomState(bedStatus);
			bedTypeStuList.put(ep[3].toString() + bedType, bedTypeSta);
		}
		return bedTypeStuList;
	}
	/*
	 * 用于将每天床型配额信息Map<key,value> key：日期+床型    value:BedTypeState（当天的床型房态信息）
	 */
	public Map<String,BedTypeQuota> createBedTypeQuotaMap(List<Object[]> everydayQuota){
		Map<String, BedTypeQuota> bedTypeQuotaMap = new HashMap<String, BedTypeQuota>();
		for (int k = 0; k < everydayQuota.size(); k++) {
			Object[] eq = everydayQuota.get(k);
			BedTypeQuota bedTypeQuo=new BedTypeQuota();
			eq[0] = eq[0] == null ? 0 : eq[0];
			eq[1] = eq[1] == null ? 0 : eq[1];
			eq[2] = eq[2] == null ? 0 : eq[2];
			eq[6] = eq[6] == null ? 0 : eq[6];
			bedTypeQuo.setBuyQuotaAbleNum(eq[0].toString());
			bedTypeQuo.setCommonQuotaAbleNum(eq[1].toString());
			bedTypeQuo.setCasulaQuotaAbleNum(eq[2].toString());
			bedTypeQuo.setIncBreakfastType(eq[3] == null ? null: eq[3].toString());
			bedTypeQuo.setIncBreakfastPrice(eq[4].toString());
			bedTypeQuo.setIncBreakfastNumber(eq[5] == null ? null: eq[5].toString());
			bedTypeQuo.setBedId(eq[6].toString());
			bedTypeQuo.setPayMethod(eq[7].toString());
			bedTypeQuo.setAbleSaleDate(eq[8].toString());
			bedTypeQuo.setBasePrice(eq[9].toString());
			bedTypeQuo.setSalesRoomPrice(eq[10].toString());
			bedTypeQuo.setSalePrice(eq[11].toString());
			bedTypeQuo.setQuotaBedShare(eq[12].toString());
			bedTypeQuo.setBedType(eq[13].toString());
			bedTypeQuo.setBuyQuotaOutOfDateNum(eq[14].toString());
			bedTypeQuo.setCommonQuotaOutOfDateNum(eq[15].toString());
			bedTypeQuo.setCasualQuotaOutOfDateNum(eq[16].toString());
			String bedType = resourceManager.getDescription("bedTypeForCC",
					bedTypeQuo.getBedId());
			bedTypeQuotaMap.put(bedTypeQuo.getAbleSaleDate()+bedType,bedTypeQuo);
		}
		return bedTypeQuotaMap;
	}
	
	/*
	 * 床型共享，如果是芒果用户则所有本部录入的房型均可定，如果是114用户本部录入的房型如果满房，或者房态为不可超且配额为0 不可定
	 */
	 public  String getAbleBedTypes(EverydayParams param,boolean[] bedTypeTemp,Map<String,BedTypeState> bedTypeStaMap) {
		 
		    String tempStatus = "";
	        String tempStatus1 = "";
	        String tempStatus2 = "";
	        String tempStatus3 = "";
	        String roomQuota=null;
	        String roomStatu=null;
	        String bedType=null;
	        int quotaNum=0;
			String RoomSta=param.gethRoomStatus();
			String[] hRoomStatus=RoomSta.split(",");
	        for (int m = 0; m < hRoomStatus.length; m++) {
	            String obj1 = hRoomStatus[m];
	            if (null != obj1) {
	                String[] roomArray0 = obj1.trim().split(",");
	                for (int n = 0; n < roomArray0.length; n++) {
	                	if(roomArray0[n].equals("")){
	                		roomQuota=null;
	                		continue;
	                	}
	                    roomQuota = roomArray0[n].substring(roomArray0[n]
	                            .indexOf(":", 3)+1,roomArray0[n].length());

	                }
	            }
	            roomQuota=roomQuota==null?"0":roomQuota;
	            quotaNum=quotaNum+Integer.valueOf(roomQuota);
	            
	        }
	        for (int m = 0; m < hRoomStatus.length; m++) {
	            String obj1 = hRoomStatus[m];
	            if (null != obj1) {
	                String[] roomArray0 = obj1.trim().split(",");
	                for (int n = 0; n < roomArray0.length; n++) {
	                	if(roomArray0[n].equals("")){
	                		continue;
	                	}	                
	                    bedType = roomArray0[n].substring(0, roomArray0[n]
	                            .indexOf(":"));
	                    String q = roomArray0[n].substring(roomArray0[n]
	                            .indexOf(":", 3)+1,roomArray0[n].length());
	                    int quota=Integer.parseInt(q);
	                    roomStatu = roomArray0[n].substring(roomArray0[n]
	                            .indexOf(":") + 1,roomArray0[n].indexOf(":") + 2);
	                    if(null!=user&&user.isMango()){		                    
		                    	if(bedType.equals("大")){
		                    	tempStatus1="大"+":"+bedTypeStaMap.get(param.getAbleSaleDate()+"大").getRoomState();	                    	
			                    }                    			                    			             
			                    if(bedType.equals("双")){
		                    	tempStatus2="双"+":"+bedTypeStaMap.get(param.getAbleSaleDate()+"双").getRoomState();	 	                    	
			                    }                   	
			                    if(bedType.equals("单")){
		                    	tempStatus3="单"+":"+bedTypeStaMap.get(param.getAbleSaleDate()+"单").getRoomState();	 	                    	
			                    } 
	                    }else{	 
	                    	 if(bedType.equals("大")){
		                		    if(!(quotaNum==0&&roomStatu.equals("不"))&&!roomStatu.equals("满")){
				                    	tempStatus1="大"+":"+roomStatu;	                    	
				                    }else{
				                    	tempStatus1="不可定";
				                    }                    	
		                     }
	                	 
			                 if(bedType.equals("双")){
		                		 if(!(quotaNum==0&&roomStatu.equals("不"))&&!roomStatu.equals("满")){
				                    	tempStatus2="双"+":"+roomStatu;	                    	
				                    }else{
				                    	tempStatus2="不可定";	                    	
				                    }                    	
		                    }
			                 if(bedType.equals("单")){
		                		 if(!(quotaNum==0&&roomStatu.equals("不"))&&!roomStatu.equals("满")){
				                    	tempStatus3="单"+":"+roomStatu;	                    	
				                    }else{
				                    	tempStatus3="不可定";                    	
				                    }                    	
		                    }
	                    }

	                }

	            }	            	            
	        }
	       	 if(bedTypeTemp[0]==true){
	       		 if(null==tempStatus1||"".equals(tempStatus1)){
	        			tempStatus1="大"+":"+bedTypeStaMap.get(param.getAbleSaleDate()+"大").getRoomState();	                    		                    	
	       		 }else if(tempStatus3.equals("不可定")){
	       			tempStatus1=""; 
	       		 }
		 	 }
	       	 if(bedTypeTemp[1]==true){
	       		 if(null==tempStatus2||"".equals(tempStatus2)){
	        			tempStatus2="双"+":"+bedTypeStaMap.get(param.getAbleSaleDate()+"双").getRoomState();	                    		                    	
	       		 }else if(tempStatus3.equals("不可定")){
	       			tempStatus2=""; 
	       		 }
		 	 }
	       	 if(bedTypeTemp[2]==true){
	       		 if(null==tempStatus3||"".equals(tempStatus3)){
	        			tempStatus3="单"+":"+bedTypeStaMap.get(param.getAbleSaleDate()+"单").getRoomState();	                    		                    	
	       		 }else if(tempStatus3.equals("不可定")){
	       			tempStatus3=""; 
	       		 }
		 	 }
            tempStatus1=tempStatus1==""?"":tempStatus1+",";
            tempStatus2=tempStatus2==""?"":tempStatus2+",";
            tempStatus3=tempStatus3==""?"":tempStatus3+",";
            tempStatus="共享:"+quotaNum+","+tempStatus1+tempStatus2+tempStatus3;
	        
	      
	        return tempStatus;        
	    }
	 /*
	  * 床型不共享，如果是芒果用户则所有本部录入的房型均可定，如果是114用户本部录入的房型如果满房，或者房态为不可超且配额为0 不可定
	  */
	 public  String getAbleBedTypesIgnoreQuota(EverydayParams param,boolean[] bedTypeTemp,Map<String,BedTypeState> bedTypeStaMap) {
		 
		    String tempStatus = "";
	        String tempStatus1 = "";
	        String tempStatus2 = "";
	        String tempStatus3 = "";
	        String roomQuota=null;
	        String roomStatu=null;
	        String bedType=null;
			String RoomSta=param.gethRoomStatus();
			String[] hRoomStatus=RoomSta.split(",");
	        for (int m = 0; m < hRoomStatus.length; m++) {
	            String obj1 = hRoomStatus[m];
	            if (null != obj1) {
	                String[] roomArray0 = obj1.trim().split(",");
	                for (int n = 0; n < roomArray0.length; n++) {
	                	if(roomArray0[n].equals("")){
	                		continue;
	                	}	
	                    roomQuota = roomArray0[n].substring(roomArray0[n].indexOf(":", 3)+1,roomArray0[n].length());
	                    bedType = roomArray0[n].substring(0, roomArray0[n].indexOf(":"));
	                    roomStatu = roomArray0[n].substring(roomArray0[n].indexOf(":") + 1,roomArray0[n].indexOf(":") + 2);
	                
	                 if(null!=user&&user.isMango()){
	                	 
			                    if(bedType.equals("大")){
		                    	tempStatus1="大"+":"+roomStatu+":"+roomQuota;	                    	
			                    }                    			                    			             
			                    if(bedType.equals("双")){
		                    	tempStatus2="双"+":"+roomStatu+":"+roomQuota;	                    	
			                    }                   	
			                    if(bedType.equals("单")){
		                    	tempStatus3="单"+":"+roomStatu+":"+roomQuota;	                    	
			                    }                    	
		                    }
	                 else{
	                	 int quotaNum=Integer.parseInt(roomQuota);	                	 
		                	
		                		 if(bedType.equals("大")){
			                		    if(!(quotaNum==0&&roomStatu.equals("不"))&&!roomStatu.equals("满")){
					                    	tempStatus1="大"+":"+roomStatu+":"+roomQuota;	                    	
					                    }else{
					                    	tempStatus1="不可定";
					                    }                    	
			                     }
		                	 
				                 if(bedType.equals("双")){
		                		 if(!(quotaNum==0&&roomStatu.equals("不"))&&!roomStatu.equals("满")){
				                    	tempStatus2="双"+":"+roomStatu+":"+roomQuota;	                    	
				                    }else{
				                    	tempStatus2="不可定";	                    	
				                    }                    	
			                    }

				                    if(bedType.equals("单")){
				                		if(!(quotaNum==0&&roomStatu.equals("不"))&&!roomStatu.equals("满")){
				                    	tempStatus3="单"+":"+roomStatu+":"+roomQuota;	                    	
				                    }else{
				                    	tempStatus3="不可定";                    	
				                    }                    	
			                    }
	                	 	}
	                 	}	    
	                }
	            }
       	 if(bedTypeTemp[0]==true){
       		 if(null==tempStatus1||"".equals(tempStatus1)){
        			tempStatus1="大"+":"+bedTypeStaMap.get(param.getAbleSaleDate()+"大").getRoomState()+":0";	                    		                    	
       		 }else if(tempStatus3.equals("不可定")){
       			tempStatus1=null; 
       		 }
	 	 }
       	 if(bedTypeTemp[1]==true){
       		 if(null==tempStatus2||"".equals(tempStatus2)){
        			tempStatus2="双"+":"+bedTypeStaMap.get(param.getAbleSaleDate()+"双").getRoomState()+":0";	                    		                    	
       		 }else if(tempStatus3.equals("不可定")){
       			tempStatus2=null; 
       		 }
	 	 }
       	 if(bedTypeTemp[2]==true){
       		 if(null==tempStatus3||"".equals(tempStatus3)){
        			tempStatus3="单"+":"+bedTypeStaMap.get(param.getAbleSaleDate()+"单").getRoomState()+":0";	                    		                    	
       		 }else if(tempStatus3.equals("不可定")){
       			tempStatus3=null; 
       		 }
	 	 }	    	        
         tempStatus1=tempStatus1==""?"":tempStatus1+",";
         tempStatus2=tempStatus2==""?"":tempStatus2+",";
         tempStatus3=tempStatus3==""?"":tempStatus3+",";
         tempStatus=tempStatus1+tempStatus2+tempStatus3;   
	        return tempStatus;        
	    }
	 
	 
	 /*
	  * 用于封装BaseParams，即要传到订单填写页面的参数
	  */
	public BaseParams createBookPrame(List<Object[]> list ){
		BaseParams params = new BaseParams();
		Object[] obj = list.get(0);
		if (obj[1] != null) {
			params.setHotelId(Long.parseLong(obj[1].toString()));
		}
		if (obj[2] != null) {
			params.setRoomTypeId(obj[2].toString());
		}
		if (obj[0] != null) {
			params.setChildRoomTypeId(obj[0].toString());
		}
		if (obj[3] != null) {
			params.setChildRoomTypeName(obj[3].toString());
		}
		if (obj[5] != null) {
			params.setQuotaType(obj[5].toString());
		}
		if (obj[6] != null) {
			params.setCanRoomNumberAlert(obj[6].toString());
		}
		if (obj[7] != null) {
			params.setHotelName(obj[7].toString());
		}
		if (obj[8] != null) {
			params.setRoomTypeName(obj[8].toString());
		}
		if (obj[9] != null) {
			params.setBalanceMethod(obj[9].toString());
		}
		if (obj[11] != null) {
			String strHotelStar = resourceManager.getDescription(
					"res_hotelStarToNum", obj[11].toString());
			if (StringUtil.isValidStr(strHotelStar)) {
				params.setHotelStar(Float.valueOf(strHotelStar).toString());
			} else {
				params.setHotelStar("1");
			}
		}
		if (obj[12] != null) {
			params.setCityId(obj[12].toString());
		}
		if (obj[14] != null) {
			params.setAcceptCustom(obj[14].toString());
		}
		if (obj[15] != null) {
			params.setClueInfo(obj[15].toString());
		}
		if (obj[16] != null) {
			params.setPaymentCurrency(obj[16].toString());
		}
		if (obj[17] != null) {
			if (obj[17].toString() == "1" || obj[17].toString().equals("1")) {
				params.setPayToHotel(true);
			} else {
				params.setPayToHotel(false);
			}
		}
		if (obj[18] != null) {
			params.setChannel(obj[18].toString());
		}
		return params;
	}
	
	public Boolean isB2Bagent(String agentCode) {
		// TODO Auto-generated method stub
		return dao.queryOrgByAgentCode(agentCode);

	}

	public INewOrderParamDao getDao() {
		return dao;
	}

	public void setDao(INewOrderParamDao dao) {
		this.dao = dao;
	}

	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	public IHotelService getHotelService() {
		return hotelService;
	}

	public void setHotelService(IHotelService hotelService) {
		this.hotelService = hotelService;
	}

	public IContractDao getContractDao() {
		return contractDao;
	}

	public void setContractDao(IContractDao contractDao) {
		this.contractDao = contractDao;
	}

	public HotelQueryDao getHotelQueryDao() {
		return hotelQueryDao;
	}

	public void setHotelQueryDao(HotelQueryDao hotelQueryDao) {
		this.hotelQueryDao = hotelQueryDao;
	}	
}

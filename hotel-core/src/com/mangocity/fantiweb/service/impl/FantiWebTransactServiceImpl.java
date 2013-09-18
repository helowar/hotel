package com.mangocity.fantiweb.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.fantiweb.service.FantiWebTransactService;
import com.mangocity.hotel.base.persistence.HtlFavourableDecrease;
import com.mangocity.hotel.base.service.IBenefitService;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderItem;
import com.mangocity.hotel.order.service.assistant.MsgAssist;
import com.mangocity.hweb.persistence.QueryHotelForWebResult;
import com.mangocity.hweb.persistence.QueryHotelForWebRoomType;
import com.mangocity.hweb.persistence.QueryHotelForWebSaleItems;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.hotel.constant.FaxEmailModel;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.webnew.persistence.HotelOrderFromBean;
import com.mangocity.webnew.persistence.HtlCalendarColsPriceBean;
import com.mangocity.webnew.persistence.HtlCalendarRowsBean;
import com.mangoctiy.communicateservice.CommunicaterService;
import com.mangoctiy.communicateservice.domain.Mail;

public class FantiWebTransactServiceImpl implements FantiWebTransactService {
	private double rate_RMBToHKD;
	private double rate_MOPToHKD;
	double serviceCharge = 0.0;// 服务费
	private IBenefitService benefitService;
	private CommunicaterService communicaterService;
	private  MsgAssist msgAssist;
	// 把多个内地酒店预付,币种为澳门币MOP用港币显示
	public void showToHK(List<QueryHotelForWebResult> webHotelResultLis,
			String payMethod) {
		if (webHotelResultLis == null) {
			return;
		}
		for (int i = 0; i < webHotelResultLis.size(); i++) {
			QueryHotelForWebResult hotelInfo = (QueryHotelForWebResult) (webHotelResultLis.get(i));
			// 人民币预付价格改
			if(CurrencyBean.RMB.equals(hotelInfo.getCurrency())){
			     showForRMBToHKByOneHotel(hotelInfo, payMethod);
			}
			//澳门币预付价格改
			if(CurrencyBean.MOP.equals(hotelInfo.getCurrency())){
				showForMOPToHKByOneHotel(hotelInfo,payMethod);
			}
			//港币价格不变，因为把界面的hotelinfo.currency改为roomtype.*currency
			if(CurrencyBean.HKD.equals(hotelInfo.getCurrency())){
				addRoomTypeCurrency(hotelInfo,payMethod);
			}
		}
	}
	/**
	 * 把一个酒店的预付价，rmb转为hk ,the second para is not used
	 */
	public void showForRMBToHKByOneHotel(QueryHotelForWebResult hotelInfo,
			String payMethod) {
		if (hotelInfo == null ) {
			return;
		}
		if(!CurrencyBean.RMB.equals(hotelInfo.getCurrency())){
			return;
		}
		List<QueryHotelForWebRoomType> roomTypes = hotelInfo.getRoomTypes();
		for (int j = 0; j < roomTypes.size(); j++) {
			QueryHotelForWebRoomType queryHotelForWebRoomType = (roomTypes
					.get(j));
			// 当付款方式只是面付（2），currencyValue不变
			if (2 == queryHotelForWebRoomType.getFk()) {
				queryHotelForWebRoomType.setCurrencyValue(hotelInfo
						.getCurrencyValue());
			}
			// 当面预付时，给面付的币种显示赋值
			if (3 == queryHotelForWebRoomType.getFk()) {
				queryHotelForWebRoomType.setPayCurrencyValue(hotelInfo
						.getCurrencyValue());
				// System.out.println("3Mianfu:"+queryHotelForWebRoomType.getPayCurrencyValue());
			}
			// 转港币的情况
			boolean flagRMBToHKD = (1 == queryHotelForWebRoomType.getFk()) 
			                    || (2 == queryHotelForWebRoomType.getFk() && 1 == queryHotelForWebRoomType.getPayToPrepay());
			if (flagRMBToHKD) {
				queryHotelForWebRoomType.setCurrencyValue("HK$");
				double itemsPrice = queryHotelForWebRoomType.getItemsPrice();
				itemsPrice = priceToHKD(itemsPrice, getRate_RMBToHKD(),
						serviceCharge);
				queryHotelForWebRoomType.setItemsPrice(itemsPrice);
				// 门市价
				double roomPrice = queryHotelForWebRoomType.getRoomPrice();
				roomPrice = priceToHKD(roomPrice, getRate_RMBToHKD(),
						serviceCharge);
				queryHotelForWebRoomType.setRoomPrice(roomPrice);
				// 当为必须面转预的话，payOneDayPrice 也改(澳门也改)(面付首日价和均价)
				double payOneDayPrice = queryHotelForWebRoomType.getPayOneDayPrice();
				payOneDayPrice = priceToHKD(payOneDayPrice,getRate_RMBToHKD(),serviceCharge);
				queryHotelForWebRoomType.setPayOneDayPrice(payOneDayPrice);
				
				double payAvlPrice = queryHotelForWebRoomType.getPayAvlPrice();
				payAvlPrice = priceToHKD(payAvlPrice,getRate_RMBToHKD(),serviceCharge);
				queryHotelForWebRoomType.setPayAvlPrice(payAvlPrice);
				
				// 首日价
				double oneDayPrice = queryHotelForWebRoomType.getOneDayPrice();
				oneDayPrice = priceToHKD(oneDayPrice, getRate_RMBToHKD(),
						serviceCharge);
				queryHotelForWebRoomType.setOneDayPrice(oneDayPrice);
				// 均价
				double avlPrice = queryHotelForWebRoomType.getAvlPrice();
				avlPrice = priceToHKD(avlPrice, getRate_RMBToHKD(),
						serviceCharge);
				queryHotelForWebRoomType.setAvlPrice(avlPrice);
				// 得到itemsList
				for (int k = 0; k < queryHotelForWebRoomType.getItemsList()
						.size(); k++) {
					QueryHotelForWebSaleItems items = queryHotelForWebRoomType
							.getItemsList().get(k);
					double salePrice = items.getSalePrice();
					salePrice = priceToHKD(salePrice, getRate_RMBToHKD(), 0.0);
					items.setSalePrice(salePrice);
				}

			}
			// 当预付和面付共存的情况下
			if (3 == queryHotelForWebRoomType.getFk()) {
				queryHotelForWebRoomType.setPrepayCurrencyValue("HK$");
				// 判断是否是预付
				if (0.0 != queryHotelForWebRoomType.getPrepayPrice()) {
					// 预付价
					double prepayPrice = queryHotelForWebRoomType
							.getPrepayPrice();
					prepayPrice = priceToHKD(prepayPrice,
							getRate_RMBToHKD(), serviceCharge);
					queryHotelForWebRoomType.setPrepayPrice(prepayPrice);
					// 首付价
					double oneDayPrice = queryHotelForWebRoomType
							.getOneDayPrice();
					oneDayPrice = priceToHKD(oneDayPrice,
							getRate_RMBToHKD(), serviceCharge);
					queryHotelForWebRoomType.setOneDayPrice(oneDayPrice);
					// 均价
					double avlPrice = queryHotelForWebRoomType.getAvlPrice();
					avlPrice = priceToHKD(avlPrice, getRate_RMBToHKD(),
							serviceCharge);
					queryHotelForWebRoomType.setAvlPrice(avlPrice);
					// System.out.println("3Mianfu:"+queryHotelForWebRoomType.getPrepayCurrencyValue());
					// 得到saleItems
					for (int n = 0; n < queryHotelForWebRoomType.getSaleItems()
							.size(); n++) {
						QueryHotelForWebSaleItems items = queryHotelForWebRoomType
								.getSaleItems().get(n);
						double salePrice = items.getSalePrice();
						salePrice = priceToHKD(salePrice, getRate_RMBToHKD(), serviceCharge);
						items.setSalePrice(salePrice);
					}

				}
			}
		}

	}
	// 把澳门币转为港币，现在是预付转,payMethod 没用
	public void showForMOPToHKByOneHotel(QueryHotelForWebResult hotelInfo,String payMethod){
		if (hotelInfo == null ) {
			return;
		}
		if(!CurrencyBean.MOP.equals(hotelInfo.getCurrency())){
			return;
		}
		List<QueryHotelForWebRoomType> roomTypes = hotelInfo.getRoomTypes();
		for (int j = 0; j < roomTypes.size(); j++) {
			QueryHotelForWebRoomType queryHotelForWebRoomType = (roomTypes
					.get(j));
			// 当付款方式只是面付（2），currencyValue不变
			if (2 == queryHotelForWebRoomType.getFk()) {
				queryHotelForWebRoomType.setCurrencyValue(hotelInfo
						.getCurrencyValue());
			}
			// 当面预付时，给面付的币种显示赋值
			if (3 == queryHotelForWebRoomType.getFk()) {
				queryHotelForWebRoomType.setPayCurrencyValue(hotelInfo
						.getCurrencyValue());
				// System.out.println("3Mianfu:"+queryHotelForWebRoomType.getPayCurrencyValue());
			}
			boolean flagMOPToHKD = (1 == queryHotelForWebRoomType.getFk()) 
            || (2 == queryHotelForWebRoomType.getFk() && 1 == queryHotelForWebRoomType.getPayToPrepay());
            if (flagMOPToHKD){
				queryHotelForWebRoomType.setCurrencyValue("HK$");
				double itemsPrice = queryHotelForWebRoomType.getItemsPrice();
				itemsPrice = priceToHKD(itemsPrice, getRate_MOPToHKD(),
						serviceCharge);
				queryHotelForWebRoomType.setItemsPrice(itemsPrice);
				// 门市价
				double roomPrice = queryHotelForWebRoomType.getRoomPrice();
				roomPrice = priceToHKD(roomPrice, getRate_MOPToHKD(),
						serviceCharge);
				queryHotelForWebRoomType.setRoomPrice(roomPrice);
				// 当为必须面转预的话，payOneDayPrice 也改(2个地方改)
				double payOneDayPrice = queryHotelForWebRoomType.getPayOneDayPrice();
				payOneDayPrice = priceToHKD(payOneDayPrice,getRate_MOPToHKD(),serviceCharge);
				queryHotelForWebRoomType.setPayOneDayPrice(payOneDayPrice);
				
				double payAvlPrice = queryHotelForWebRoomType.getPayAvlPrice();
				payAvlPrice = priceToHKD(payAvlPrice,getRate_MOPToHKD(),serviceCharge);
				queryHotelForWebRoomType.setPayAvlPrice(payAvlPrice);
				
				// 首日价
				double oneDayPrice = queryHotelForWebRoomType.getOneDayPrice();
				oneDayPrice = priceToHKD(oneDayPrice, getRate_MOPToHKD(),
						serviceCharge);
				queryHotelForWebRoomType.setOneDayPrice(oneDayPrice);
				// 均价
				double avlPrice = queryHotelForWebRoomType.getAvlPrice();
				avlPrice = priceToHKD(avlPrice, getRate_MOPToHKD(),
						serviceCharge);
				queryHotelForWebRoomType.setAvlPrice(avlPrice);
				// 得到itemsList
				for (int k = 0; k < queryHotelForWebRoomType.getItemsList()
						.size(); k++) {
					QueryHotelForWebSaleItems items = queryHotelForWebRoomType
							.getItemsList().get(k);
					double salePrice = items.getSalePrice();
					salePrice = priceToHKD(salePrice, getRate_MOPToHKD(), 0.0);
					items.setSalePrice(salePrice);
				}

			}
			// 当预付和面付共存的情况下
			if (3 == queryHotelForWebRoomType.getFk()) {
				queryHotelForWebRoomType.setPrepayCurrencyValue("HK$");
				// 判断是否是预付
				if (0.0 != queryHotelForWebRoomType.getPrepayPrice()) {
					// 预付价
					double prepayPrice = queryHotelForWebRoomType
							.getPrepayPrice();
					prepayPrice = priceToHKD(prepayPrice,
							getRate_MOPToHKD(), serviceCharge);
					queryHotelForWebRoomType.setPrepayPrice(prepayPrice);
					// 首付价
					double oneDayPrice = queryHotelForWebRoomType
							.getOneDayPrice();
					oneDayPrice = priceToHKD(oneDayPrice,
							getRate_MOPToHKD(), serviceCharge);
					queryHotelForWebRoomType.setOneDayPrice(oneDayPrice);
					// 均价
					double avlPrice = queryHotelForWebRoomType.getAvlPrice();
					avlPrice = priceToHKD(avlPrice, getRate_MOPToHKD(),
							serviceCharge);
					queryHotelForWebRoomType.setAvlPrice(avlPrice);
					// System.out.println("3Mianfu:"+queryHotelForWebRoomType.getPrepayCurrencyValue());
					// 得到saleItems
					for (int n = 0; n < queryHotelForWebRoomType.getSaleItems()
							.size(); n++) {
						QueryHotelForWebSaleItems items = queryHotelForWebRoomType
								.getSaleItems().get(n);
						double salePrice = items.getSalePrice();
						if (salePrice != 99999.0 || salePrice != 999999.0) {
							salePrice = priceToHKD(salePrice,
									getRate_MOPToHKD(), serviceCharge);
						}
						items.setSalePrice(salePrice);
					}
				}
			}
		}
	}
	// 把内地的酒店的预付金额或者澳门币预付用港币显示（在日历中显示）
	public void calendarPriceToHKD(
			List<HtlCalendarRowsBean> calendarExtenderLst,String currency) {
		// System.out.println(calendarExtenderLst);
		if (calendarExtenderLst == null) {
			return;
		}
		int num = 1;
		if (calendarExtenderLst.size() == 0) {
			num = 0;
		}
		for (int i = 0; i < num; i++) {
			HtlCalendarRowsBean calendarRows = calendarExtenderLst.get(i);
			for (int j = 0; j < calendarRows.getLstColsPrice().size(); j++) {
				List<HtlCalendarColsPriceBean> calendarColsPriceLst = calendarRows
						.getLstColsPrice();
				String dayPriceStr = calendarColsPriceLst.get(j)
						.getDayPriceStr();
				// 把人民币显示的价格转换港币显示的价格，这里在日历中放的String型的价格
				if (!"***".equals(dayPriceStr) && !"×".equals(dayPriceStr)
						&& !"免费".equals(dayPriceStr) && !"".equals(dayPriceStr)) {
					Double dayPrice = Double.parseDouble(dayPriceStr);
					//人民币的话
					if(CurrencyBean.RMB.equals(currency)){
					    dayPrice = priceToHKD(dayPrice, getRate_RMBToHKD(), 0.0);
					}
					else if(CurrencyBean.MOP.equals(currency)){
						dayPrice = priceToHKD(dayPrice,getRate_MOPToHKD(),0.0);
					}
					// 只取整数位
					int dayPriceInt = dayPrice.intValue();
					String dayPriceStr2 = String.valueOf(dayPriceInt);
					calendarColsPriceLst.get(j).setDayPriceStr(dayPriceStr2);
				}
			}
		}

	}

	/**
	 * 把order中的总价格转为HKD
	 */
	public void orderPriceNumToHKD(HotelOrderFromBean hotelOrderFromBean,
			String currency) {
		if (hotelOrderFromBean == null) {
			return;
		}
		if (currency == null) {
			return;
		}
		//预付
		boolean flag_RMBTOHKD1 =  CurrencyBean.RMB.equals(currency) && PayMethod.PRE_PAY.equals(hotelOrderFromBean.getPayMethod());
	    //面转预 
		boolean flag_TOHKD_payToPrepay = PayMethod.PAY.equals(hotelOrderFromBean.getPayMethod()) && hotelOrderFromBean.isPayToPrepay();
		boolean flag_RMBTOHKD = flag_RMBTOHKD1 || flag_TOHKD_payToPrepay;
		if (flag_RMBTOHKD && CurrencyBean.RMB.equals(currency)) {
			double priceNum = hotelOrderFromBean.getPriceNum();
			priceNum = priceToHKD(priceNum, getRate_RMBToHKD(),
					serviceCharge);
			hotelOrderFromBean.setPriceNum(priceNum);
			return;
		}
		boolean flag_MOPTOHKD1 =  CurrencyBean.MOP.equals(currency) && PayMethod.PRE_PAY.equals(hotelOrderFromBean.getPayMethod());
		boolean flag_MOPTOHKD = flag_MOPTOHKD1 || flag_TOHKD_payToPrepay;
		if(flag_MOPTOHKD && CurrencyBean.MOP.equals(currency)){
			double priceNum = hotelOrderFromBean.getPriceNum();
			priceNum = priceToHKD(priceNum, getRate_MOPToHKD(),
					serviceCharge);
			hotelOrderFromBean.setPriceNum(priceNum);
		}

	}

	

	/**
	 * 把内陆酒店预付价或者澳门酒店minprice变为港币显示
	 */
	public void minPrice_RMBToHKD(QueryHotelForWebResult hotelInfo,
			String currency) {
		if (hotelInfo == null || hotelInfo.getRoomTypes() == null) {
			return;
		}
		if (currency == null) {
			return;
		}
		if(!CurrencyBean.RMB.equals(currency)){
			return;
		}
		double minPrice = hotelInfo.getMinPrice();
		boolean flag_continueLong = hotelInfo.isContinueLong();
			for (int i = 0; i < hotelInfo.getRoomTypes().size(); i++) {
				QueryHotelForWebRoomType roomType = hotelInfo.getRoomTypes().get(i);
				// 预付的值跟最小值比较,预付与面预付中的预付
				if (roomType.getFk() == 1 || (roomType.getFk() == 3)) {
					// 看是否是连住
					if (flag_continueLong) {
						if (roomType.getAvlPrice() <= minPrice) {
							minPrice = priceToHKD(roomType.getAvlPrice(),
									getRate_RMBToHKD(), 0.0);
							hotelInfo.setFlagMinPrice_RMBToHKD(true);
							break;
						}
					} else {
						if (roomType.getOneDayPrice() <= minPrice) {
							minPrice = priceToHKD(roomType.getOneDayPrice(),
									getRate_RMBToHKD(), 0.0);
							hotelInfo.setFlagMinPrice_RMBToHKD(true);
							break;
						}
					}
				}
			}
		// 修改后赋值
		hotelInfo.setMinPrice(minPrice);
	}
	/**
	 * 把内陆酒店预付价或者澳门酒店minprice变为港币显示
	 */
	public void minPrice_MOPToHKD(QueryHotelForWebResult hotelInfo,
			String currency) {
		if (hotelInfo == null || hotelInfo.getRoomTypes() == null) {
			return;
		}
		if (currency == null) {
			return;
		}
		if(!CurrencyBean.MOP.equals(currency)){
			return;
		}
		double minPrice = hotelInfo.getMinPrice();
		boolean flag_continueLong = hotelInfo.isContinueLong();
			for (int i = 0; i < hotelInfo.getRoomTypes().size(); i++) {
				QueryHotelForWebRoomType roomType = hotelInfo.getRoomTypes().get(i);
				// 预付的值跟最小值比较,预付与面预付中的预付
				if (roomType.getFk() == 1 || (roomType.getFk() == 3)) {
					// 看是否是连住
					if (flag_continueLong) {
						if (roomType.getAvlPrice() <= minPrice) {
							minPrice = priceToHKD(roomType.getAvlPrice(),
									getRate_MOPToHKD(), 0.0);
							hotelInfo.setFlagMinPirce_MOPToHKD(true);
							break;
						}
					} else {
						if (roomType.getOneDayPrice() <= minPrice) {
							minPrice = priceToHKD(roomType.getOneDayPrice(),
									getRate_MOPToHKD(), 0.0);
							hotelInfo.setFlagMinPirce_MOPToHKD(true);
							break;
						}
					}
				}
			}
		// 修改后赋值
		hotelInfo.setMinPrice(minPrice);
	}

	public void minPriceToHKD(
			List<QueryHotelForWebResult> webHotelResultLis) {
		if (webHotelResultLis == null) {
			return;
		}
		for (int i = 0; i < webHotelResultLis.size(); i++) {
			QueryHotelForWebResult hotelInfo = webHotelResultLis.get(i);
			//如果是港币酒店不做变化
			if(null==hotelInfo || CurrencyBean.HKD.equals(hotelInfo.getCurrency())){
				continue;
			}
			if(CurrencyBean.RMB.equals(hotelInfo.getCurrency())){
			   minPrice_RMBToHKD(hotelInfo, hotelInfo.getCurrency());
			}
			if(CurrencyBean.MOP.equals(hotelInfo.getCurrency())){
				minPrice_MOPToHKD(hotelInfo,hotelInfo.getCurrency());
			}
		}
	}

	// 繁体优惠立减（预付立减）以港币显示
	public void changePriceInHKDCurrenyOfBenefit(
			List<QueryHotelForWebResult> webHotelResultLis, Date inDate,
			Date outDate) {
		if (webHotelResultLis == null) {
			return;
		}
		// 定义一个StringBuffer来放priceTypeId     可以放到一个方法里
		StringBuffer benefitPriceTypeIds = new StringBuffer();
		for (int i = 0; i < webHotelResultLis.size(); i++) {
			QueryHotelForWebResult hotelInfo = webHotelResultLis.get(i);
			List<QueryHotelForWebRoomType> roomTypes = hotelInfo.getRoomTypes();
			for (int j = 0; j < roomTypes.size(); j++) {
				QueryHotelForWebRoomType roomType = roomTypes.get(j);
				int benefitAmount = roomType.getBenefitAmount();
				if (benefitAmount == 0) {
					continue;
				}
				String childRoomTypeId = roomType.getChildRoomTypeId();
				benefitPriceTypeIds.append(childRoomTypeId + ",");
			}
		}
		String benefitPriceTypeIdsStr = benefitPriceTypeIds.toString();
		// if no benefit go back 
		if(benefitPriceTypeIdsStr==null||"".equals(benefitPriceTypeIdsStr)){
			return;
		}
		//去掉最后一个,
		benefitPriceTypeIdsStr = StringUtil.deleteLastChar(
				benefitPriceTypeIdsStr, ',');
		// 重查一次
		Map<String, List<HtlFavourableDecrease>> decreaseMap = new HashMap<String, List<HtlFavourableDecrease>>();
		if (StringUtil.isValidStr(benefitPriceTypeIdsStr)) {
			decreaseMap = benefitService.queryBatchBenefitByDate(
					benefitPriceTypeIdsStr, inDate, outDate);
		}
		// 根据不同的币种来重新放置优惠立减的价格
		for (int i = 0; i < webHotelResultLis.size(); i++) {
			QueryHotelForWebResult hotelInfo = webHotelResultLis.get(i);
			if(hotelInfo.getHasBenefit()==1){
				setbenefitPriceInHKD(hotelInfo,decreaseMap,inDate,outDate,hotelInfo.getCurrency());
			}
		}
	}

	// 繁体优惠立减（预付立减）以港币显示
	public void changePriceInHKDCurrenyOfBenefit(
			QueryHotelForWebResult hotelInfo, Date inDate, Date outDate) {
		if (hotelInfo == null ) {
			return;
		}
		// 如果没有预付立减的话，返回
		if (hotelInfo.getHasBenefit() != 1) {
			return;
		}
		List<QueryHotelForWebRoomType> roomTypes = hotelInfo.getRoomTypes();
		// 定义一个StringBuffer来放priceTypeId
		StringBuffer benefitPriceTypeIds = new StringBuffer();
		// 把有预付立减的priceTypeId全拿出来 ，用一个方法
		for (int i = 0; i < roomTypes.size(); i++) {
			QueryHotelForWebRoomType roomType = roomTypes.get(i);
			int benefitAmount = roomType.getBenefitAmount();
			if (benefitAmount == 0) {
				continue;
			}
			String childRoomTypeId = roomType.getChildRoomTypeId();
			benefitPriceTypeIds.append(childRoomTypeId + ",");
		}
		String benefitPriceTypeIdsStr = benefitPriceTypeIds.toString();
		// if no benefit go back 
		if(benefitPriceTypeIdsStr==null||"".equals(benefitPriceTypeIdsStr)){
			return;
		}
		benefitPriceTypeIdsStr = StringUtil.deleteLastChar(
				benefitPriceTypeIdsStr, ',');
		// 重查一次
		Map<String, List<HtlFavourableDecrease>> decreaseMap = new HashMap<String, List<HtlFavourableDecrease>>();
		if (StringUtil.isValidStr(benefitPriceTypeIdsStr)) {
			decreaseMap = benefitService.queryBatchBenefitByDate(
					benefitPriceTypeIdsStr, inDate, outDate);
		}
		setbenefitPriceInHKD(hotelInfo,decreaseMap,inDate,outDate,hotelInfo.getCurrency());

	}

	// 港币价=RMB*rate+serviceCharge
	private double priceToHKD(double price, double rateToHKD,double serviceCharge) {
		//五个9是免费，6个9是数据库的数据。
		if(price == 99999 || price == 999999){
			return price;
		}

		price *= rateToHKD;
		price += serviceCharge;// +服务费
		price = Math.ceil(price);
		return price;
	}

	// 给roomType中的currencyValue赋值，由于港澳酒店和内地酒店公用一个界面
	public void addRoomTypeCurrency(
			List<QueryHotelForWebResult> webHotelResultLis, String currency) {
		if (webHotelResultLis == null) {
			return;
		}
		for (int i = 0; i < webHotelResultLis.size(); i++) {
			QueryHotelForWebResult hotelInfo = webHotelResultLis.get(i);
			addRoomTypeCurrency(hotelInfo, currency);
		}
	}

	// 给roomType中的currencyValue赋值，由于港澳酒店和内地酒店公用一个界面
	public void addRoomTypeCurrency(QueryHotelForWebResult hotelInfo,
			String currency) {
		if (hotelInfo == null || (hotelInfo.getRoomTypes() == null)) {
			return;
		}
		// 如果为人民币和澳门币的话，跳出
		if (CurrencyBean.RMB.equals(currency) || CurrencyBean.MOP.equals(currency)) {
			return;
		}
		String hotelCurrencyValue = hotelInfo.getCurrencyValue();
		for (int i = 0; i < hotelInfo.getRoomTypes().size(); i++) {
			QueryHotelForWebRoomType roomType = hotelInfo.getRoomTypes().get(i);
			roomType.setCurrencyValue(hotelCurrencyValue);
			roomType.setPayCurrencyValue(hotelCurrencyValue);
			roomType.setPrepayCurrencyValue(hotelCurrencyValue);
		}
	}

	private String genDecreaseMapKey(String childRoomTypeId, Date checkInDate,
			Date checkOutDate) {
		StringBuffer mapKey = new StringBuffer();
		mapKey.append(childRoomTypeId);
		mapKey.append("_");
		mapKey.append(DateUtil.dateToStringNew(checkInDate));
		mapKey.append("_");
		mapKey.append(DateUtil.dateToStringNew(checkOutDate));
		return mapKey.toString();
	}

	// 重新放预付立减的值

	private void setbenefitPriceInHKD(QueryHotelForWebResult hotelInfo,
			Map<String, List<HtlFavourableDecrease>> decreaseMap, Date inDate,
			Date outDate, String currency) {
		List<QueryHotelForWebRoomType> roomTypes = hotelInfo.getRoomTypes();
		for (int j = 0; j < roomTypes.size(); j++) {
			QueryHotelForWebRoomType roomType = roomTypes.get(j);
			int benefitAmount = roomType.getBenefitAmount();
			if (benefitAmount == 0) {
				continue;
			}
			int benefitAmountHKD = 0;
			String priceTypeId = roomType.getChildRoomTypeId();
			String mapKey = genDecreaseMapKey(priceTypeId, inDate, outDate);
			int days = DateUtil.getDay(inDate, outDate);
			List<HtlFavourableDecrease> list = decreaseMap.get(mapKey);
			for (HtlFavourableDecrease htlFavourableDecrease : list) {
				String week = htlFavourableDecrease.getWeek();
				for (int i = 0; i < days; i++) {
					Date tempDate = DateUtil.getDate(inDate, i);
					//从入住日期到离店日期开始遍历，检查日期是否在规则的开始结束日期区间
					boolean isBetween = DateUtil.checkDateBetween(tempDate,htlFavourableDecrease.getBeginDate(),htlFavourableDecrease.getEndDate(),week);
					if(isBetween){
						//优惠金额累加
						benefitAmountHKD += (int)htlFavourableDecrease.getDecreasePrice();
					}
				}
			}
			double rate = 1.0;
			if (CurrencyBean.RMB.equals(currency)) {
				rate = 1 / (Double.parseDouble(CurrencyBean.rateMap.get(
						CurrencyBean.HKD).toString()));
			}
			if (CurrencyBean.HKD.equals(currency)) {
				rate = 1.0;
			}
			if (CurrencyBean.MOP.equals(currency)) {
				double rateHKD = Double.parseDouble(CurrencyBean.rateMap.get(
						CurrencyBean.HKD).toString());
				double rateMOP = Double.parseDouble(CurrencyBean.rateMap.get(
						CurrencyBean.HKD).toString());
				rate = rateMOP / rateHKD;
			}
			benefitAmountHKD = (int) (benefitAmountHKD * rate);
			roomType.setBenefitAmount(benefitAmountHKD);
			// 屏蔽预付立减
			roomType.setBenefitAmount(0);
			
			
		}
		// 屏蔽预付立减
		hotelInfo.setHasBenefit(0);
	}
	
	  /**
     * 同步配额明细中的总价格到订单表，防止变价，导致总金额和配额明细中的金额不相等 add by shengwei.zuo 2010-6-4
     * @param order
     * @return
     */
    public void synchroSumPriceToOrder(OrOrder order){
    	
    	//实际每天的价格的价格总和
    	double sumItemPrice = 0d;
    	
    	//每天预付立减的价格总和
    	float sumFavPrice = 0f; 
    	
    	List<OrOrderItem> orderIteLst = order.getOrderItems();
    	if(orderIteLst!=null && !orderIteLst.isEmpty()){
    		int osize = orderIteLst.size();
    		for(int i =0;i<osize;i++){
    			OrOrderItem  OrIte = new OrOrderItem();
    			OrIte = orderIteLst.get(i);
    			
    			BigDecimal  bigSumItePrice  =  new  BigDecimal(Double.toString(sumItemPrice));  
    			//每天的销售价
    			BigDecimal  bigsaPri  =  new  BigDecimal(Double.toString(OrIte.getSalePrice()));   
    			sumItemPrice  = bigSumItePrice.add(bigsaPri).doubleValue();
    			
   
    		}
    		// 没考虑预付立减的情况
    		
    		BigDecimal  sumIte =  new  BigDecimal(Double.toString(sumItemPrice));  
        	
        	BigDecimal  sumFav  =  new  BigDecimal(Float.toString(sumFavPrice));  
        	
        	//订单总金额
        	double sum =  sumIte.add(sumFav).doubleValue();
        	
        	if(PayMethod.PRE_PAY.equals(order.getPayMethod()) && CurrencyBean.RMB.equals(order.getPaymentCurrency())){
        	    sum *= getRate_RMBToHKD();
        	    sum = Math.ceil(sum);
        	}
        	if(PayMethod.PRE_PAY.equals(order.getPayMethod()) && CurrencyBean.MOP.equals(order.getPaymentCurrency())){
        		sum *=getRate_MOPToHKD();
        		sum = Math.ceil(sum);
        	}
        	order.setSum(sum);
        	order.setSumRmb(sum);
    		
    	}
    	
    }
    /**
     * 发送需担保的订单邮件给hk组
     * @return
     */
    public void sendNeedAssureOrderMailToHK(OrOrder order){
    	String toaddress = "enquiry.cs@mangocity.com.hk";
    	//String toaddress1 = "93514697@qq.com";
		//String toaddress = "diandian.hou@mangocity.com";
		//String toaddress1 = "zhijie.gu@mangocity.com";
		String[] toddresses = new String[]{toaddress};
    	Mail mail = new Mail();
		mail.setApplicationName("hotel");
		String templateNo = FaxEmailModel.ASSUREORDER_FROMHK;
		//mail.setTo(new String[] { toaddress });
		 mail.setTo(toddresses);
		String title = "信用卡擔保--訂單號為：" + order.getOrderCD();
		mail.setSubject(title);
		mail.setTemplateFileName(templateNo);
		mail.setFrom("cs@mangocity.com");
		// mail.setUserLoginId(roleUser.getLoginName());可能会得不到
		mail.setUserLoginId(order.getMemberCd());
		String xmlString = msgAssist.genMailContractor(order);
		mail.setXml(xmlString);
		long ret = communicaterService.sendEmail(mail);
		// 写日志
		OrHandleLog handleLog = new OrHandleLog();
		handleLog.setModifierName("繁体网站");
		handleLog.setModifierRole("繁体网站");
		handleLog.setBeforeState(order.getOrderState());
		handleLog.setAfterState(order.getOrderState());
		handleLog.setContent("香港信用卡担保的订单信息，发送给香港组" + (ret >= 1 ? "成功" : "失败"));
		handleLog.setModifiedTime(new Date());
		handleLog.setOrder(order);
		order.getLogList().add(handleLog);
    }
    
	private double getRate_RMBToHKD() {
		rate_RMBToHKD = 1 / (Double.parseDouble(CurrencyBean.rateMap.get(
				CurrencyBean.HKD).toString()));
		return rate_RMBToHKD;
	}
	private double getRate_MOPToHKD(){
	     double rate_HKDToRMB = Double.parseDouble(CurrencyBean.rateMap.get(
				CurrencyBean.HKD).toString());     
		 double rateMOPToRMB = Double.parseDouble(CurrencyBean.rateMap.get(
				CurrencyBean.MOP).toString());
          rate_MOPToHKD = rateMOPToRMB/rate_HKDToRMB;
          return rate_MOPToHKD;
	}
    
  
	
	//注入的方法
	public void setBenefitService(IBenefitService benefitService) {
		this.benefitService = benefitService;
	}

	public void setCommunicaterService(CommunicaterService communicaterService) {
		this.communicaterService = communicaterService;
	}

	public void setMsgAssist(MsgAssist msgAssist) {
		this.msgAssist = msgAssist;
	}

}

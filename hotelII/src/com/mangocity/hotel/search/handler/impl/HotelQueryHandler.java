package com.mangocity.hotel.search.handler.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlGeoDistance;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hotel.common.vo.CommentSummaryVO;
import com.mangocity.hotel.common.vo.HotelPromoVO;
import com.mangocity.hotel.common.vo.PreSaleVO;
import com.mangocity.hotel.dreamweb.comment.model.CommentStatistics;
import com.mangocity.hotel.search.constant.CloseRoomReason;
import com.mangocity.hotel.search.constant.OpenCloseRoom;
import com.mangocity.hotel.search.constant.PayMethod;
import com.mangocity.hotel.search.constant.RoomEquipmentConstant;
import com.mangocity.hotel.search.model.QueryHotelCondition;
import com.mangocity.hotel.search.model.TrafficInfo;
import com.mangocity.hotel.search.service.IHotelQueryHandler;
import com.mangocity.hotel.search.service.assistant.Commodity;
import com.mangocity.hotel.search.service.assistant.CommoditySummary;
import com.mangocity.hotel.search.service.assistant.HotelInfo;
import com.mangocity.hotel.search.service.assistant.HotelPromo;
import com.mangocity.hotel.search.service.assistant.HotelSummaryInfo;
import com.mangocity.hotel.search.service.assistant.PreSale;
import com.mangocity.hotel.search.service.assistant.RoomTypeInfo;
import com.mangocity.hotel.search.service.assistant.RoomTypeSummaryInfo;
import com.mangocity.hotel.search.service.assistant.SaleInfo;
import com.mangocity.hotel.search.util.PriceUtil;
import com.mangocity.hotel.search.vo.CommodityVO;
import com.mangocity.hotel.search.vo.HotelResultForWebVO;
import com.mangocity.hotel.search.vo.HotelResultVO;
import com.mangocity.hotel.search.vo.RoomTypeVO;
import com.mangocity.hotel.search.vo.SaleItemVO;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.log.MyLog;

/**
 * 
 * @author diandian.hou
 *
 */

public class HotelQueryHandler implements IHotelQueryHandler {
	
	private List<HotelResultVO> hotelResutlList = new ArrayList<HotelResultVO>(15);
	
    private int hotelCount ;
    private QueryHotelCondition queryHotelCondition;
    //临时的参数
	private HotelResultVO curHotel;
	private RoomTypeVO curRoom;
	private CommodityVO curComm;
	private SaleItemVO curSaleItem;
	final static String BEDTYPESPLITSYMBOL = "/";
	final static String RMBCURRENCYSYMBOL = "&yen;";
	private final static String PRICENOTSHOW = "***";
	private final static String PRICECLOSESHOW = "X";
	private final static String ZEROPRICE = "--";
	private final static String FAVPRICE = "免费";
	private static final MyLog log = MyLog.getLogger(HotelQueryHandler.class);
	
	public void startHandleQueryHotel(int hotelCount){
		this.hotelCount = hotelCount;
	}
	public void endHandleQueryHotel() {
		  // 对本次查询结果的酒店进行排序
		  Collections.sort(hotelResutlList);
	}
	
	public void startHandleHotel(final HotelInfo hotelInfo) {
		// TODO Auto-generated method stub
		HotelResultForWebVO hotelVO = new HotelResultForWebVO();
		hotelVO.setCityCode(hotelInfo.getCityCode());
		hotelVO.setCityCodeLower(hotelInfo.getCityCode()==null?"":hotelInfo.getCityCode().toLowerCase());//for seo
		hotelVO.setHotelId(hotelInfo.getHotelId());
		//必须加排序
		hotelVO.setSort(hotelInfo.getSort());
		String commendTypeCssStyle = CommendTypeConvert.getCssStyleName(hotelInfo.getCommendType());
		hotelVO.setCommendType(commendTypeCssStyle);
		hotelVO.setChnName(hotelInfo.getChnName());
		hotelVO.setBizZone(hotelInfo.getBizZone());
		hotelVO.setBizZoneLower(hotelInfo.getBizZone()==null?"":hotelInfo.getBizZone().toLowerCase());//for seo
		hotelVO.setBizZoneValue(hotelInfo.getBizChnName()== null ? "" :hotelInfo.getBizChnName());
		String autoIntroduce = hotelInfo.getAutoIntroduce();
		if(autoIntroduce!=null && autoIntroduce.length() > 48){
			autoIntroduce = autoIntroduce.substring(0,48)+"...";
		}
		hotelVO.setAutoIntroduce(autoIntroduce);
		hotelVO.setHotelStar(HotelStarConvert.getForWebVOByKey(hotelInfo.getHotelStar()));		
		hotelVO.setOutPictureName(hotelInfo.getOutPictureName());
		
		hotelVO.setPromoteType(hotelInfo.getPromoteType());
		//设置预付酒店,add by ting.li
		hotelVO.setPrepayHotel(hotelInfo.isPrepayHotel());
		
		//设置酒店地址，用于列表页展示，add by shunqiang.hu
		String cityname = hotelInfo.getCityName();
		String zonename = InitServlet.citySozeObj.get(hotelInfo.getZone());
		String chnAddres = hotelInfo.getChnAddress();
		String showaddres = hotelInfo.getChnAddress();
		if(cityname ==null ||"null".equals(cityname)){
			cityname ="";
		}
		if(zonename ==null ||"null".equals(zonename)){
			zonename ="";
		}

		if(chnAddres.length() >cityname.length()){
    		cityname=cityname.replace("市","");
    		if(chnAddres.length() > cityname.length()+1){
				if(chnAddres.substring(0, cityname.length()+2).indexOf(cityname)!=-1){ 
					showaddres = chnAddres;
				}else{ 
					if(chnAddres.length() > zonename.length()){
						if(chnAddres.substring(0, zonename.length()).indexOf(zonename)!=-1){ 
							showaddres = cityname+chnAddres;
						}else{
							showaddres = cityname+zonename+chnAddres;
						}
					}else{
						showaddres = cityname+zonename+chnAddres;
					}
				}
    		}else{
    			if(chnAddres.indexOf(cityname)!=-1){ 
					showaddres = chnAddres;
				}else{ 
					if(chnAddres.length() > zonename.length()){
						if(chnAddres.substring(0, zonename.length()).indexOf(zonename)!=-1){ 
							showaddres = cityname+chnAddres;
						}else{
							showaddres = cityname+zonename+chnAddres;
						}
					}else{
						showaddres = cityname+zonename+chnAddres;
					}
				}
    		}
		}

		hotelVO.setChnAddress(showaddres);
		
		setHotelRequestService(hotelVO,hotelInfo);		
		
		setHotelPresale(hotelVO, hotelInfo);
		
		CommentStatistics comment = hotelInfo.getComment();
		setHotelComment(hotelVO,comment);
		//设置地理信息
		HtlGeoDistance htlGeoDistance = hotelInfo.getHtlGeoDistance();
		setHotelGeoDistance(hotelVO,htlGeoDistance);
		//设置交通信息
		List<TrafficInfo> listTraffic =  hotelInfo.getListTraffic();
		StringBuffer trafficInfoStrBF = new StringBuffer();
		for(int i = 0 ; i<listTraffic.size();i++){
			TrafficInfo trafficInfo = listTraffic.get(i);
			trafficInfoStrBF.append("<li>距离"+trafficInfo.getAddress()+"约"+trafficInfo.getDistance()+"公里;</li>");
		}
		hotelVO.setTrafficInfo(trafficInfoStrBF.toString());
		
	
		hotelResutlList.add(hotelVO);
				
		curHotel = hotelVO;
	}
	
   //设置地理信息
	private void setHotelGeoDistance(HotelResultForWebVO hotelVO,HtlGeoDistance htlGeoDistance){
		if(htlGeoDistance==null){hotelVO.setGeoDistance("");return;}
		Long geoId = htlGeoDistance.getGeoId();
		double distance  = htlGeoDistance.getDistance();
		String geoName = htlGeoDistance.getName();
		String geoDistance = "";
		if( null!=geoId && 0!=geoId &&null!=geoName && !"".equals(geoName)){
			geoDistance = "交通信息：距离<em>"+geoName+"</em>的直线距离约<em>"+distance+"</em>公里";
		}
		hotelVO.setGeoDistance(geoDistance);
	}
	
	//设计酒店的评论信息
	private void setHotelComment(HotelResultForWebVO hotelVO,CommentStatistics comment){
		if(comment==null){return;}
		CommentSummaryVO commentVO = new CommentSummaryVO();
		commentVO.setHotelId(comment.getHotelId());
		commentVO.setCommendUp(comment.getRecommendNumber());
		commentVO.setCommendDown(comment.getUnrecommendNumber());
		commentVO.setCommentNum(comment.getCommentNumber());
		commentVO.setAverAgepoint(String.valueOf(comment.getTotalScore()));
		hotelVO.setCommentSummaryVO(commentVO);
	}

     //设置酒店的芒果促销信息
	private void setHotelPresale(HotelResultForWebVO hotelVO, HotelInfo hotelInfo) {
		for(PreSale preSale : hotelInfo.getPreSaleLst()) {
			PreSaleVO preSaleVO = new PreSaleVO();
			preSaleVO.setPreSaleName(preSale.getPreSaleName());
			preSaleVO.setPreSaleContent(preSale.getPreSaleContent());
			preSaleVO.setPreSaleURL(preSale.getPreSaleURL());
			preSaleVO.setPreSaleBeginEnd(preSale.getPreSaleBeginEnd());
			hotelVO.getPreSaleLst().add(preSaleVO);
		}
	}
	
	public void endHandleHotel(HotelSummaryInfo curHotelSumInfo) {
		  curHotel.setLowestPrice(String.valueOf(curHotelSumInfo.getLowestPrice()));
		  // 对该酒店的房型进行排序
		  Collections.sort(curHotel.getRoomTypes());
	}
	
	public void startHandleRoomType(final RoomTypeInfo roomTypeInfo) {
		RoomTypeVO roomVO = new RoomTypeVO();
		roomVO.setRoomTypeId(roomTypeInfo.getRoomTypeId());
		roomVO.setRoomtypeName(roomTypeInfo.getRoomtypeName());
		if(StringUtil.isValidStr(roomTypeInfo.getRoomAcreage())){
            roomVO.setRoomAcreage(roomTypeInfo.getRoomAcreage()+"平方米");
		}
		if(StringUtil.isValidStr(roomTypeInfo.getRoomFloor())){
			roomVO.setRoomFloor(roomTypeInfo.getRoomFloor()+"楼");
		}
		roomVO.setMaxNumOfPersons(roomTypeInfo.getMaxNumOfPersons() + "人");
		roomVO.setFlagAddBed(roomTypeInfo.isFlagAddBed() ? "部分可加床"
				: (0 < roomTypeInfo.getAddBedNum() ? "可加床" : "不可加床"));
		roomVO.setOtherExplain(roomTypeInfo.getRoomEquipment().contains(
				RoomEquipmentConstant.nosmoking) ? "可安排无烟楼层" : "无");
		curHotel.getRoomTypes().add(roomVO);	
		curRoom = roomVO;
		
	}

	public void endHandleRoomType(RoomTypeSummaryInfo curRoomTypeSumInfo) {
		
		if(curRoom.getCommodities().isEmpty()) {
			curHotel.getRoomTypes().remove(curRoom);
			return;
		}
		
		curRoom.setMinPrice(curRoomTypeSumInfo.getMinPrice());
		curRoom.setCanbook(curRoomTypeSumInfo.isCanbook());
		// 对该房型的商品进行排序
		Collections.sort(curRoom.getCommodities());

	}
	
	public void startHandleCommodity(final Commodity commodity) {
		CommodityVO commVO = new CommodityVO();
		//comm.setCommodityId(Long.parseLong(commodity.getCommodityId()));
		commVO.setPriceTypeId(commodity.getPriceTypeId());
		commVO.setHasSupplier(commodity.isHasSupplier());//是否是供应商提供的商品
		commVO.setCommodityName(commodity.getCommodityName());
		commVO.setPayMethod(commodity.getPayMethod());
		commVO.setShowMemberPrice(commodity.isShowMemberPrice());
		commVO.setPayMethodStr((PayMethod.PRE_PAY.equals(commodity.getPayMethod()) || "1".equals(commodity.getPayToPrepay())) ? "预付":"到店支付" );
		//1为必须面转预，显示为预付
		commVO.setFlagHasPromo((null==commodity.getPromoLst() || commodity.getPromoLst().size() == 0) ? false : true);
		if(!commodity.getPromoLst().isEmpty()){
			addPromoLstToCommVO(commodity,commVO);
		}
		curComm = commVO;
	}
	
	public void endHandleCommodity(CommoditySummary curComSummary) {
		
		// 设置宽带信息
		curComm.setNet(curComSummary.getBandInfo());
		
		curComm.setBedType(getCommodityBedTypeValue(curComSummary.getBedType()));
			
		boolean bPriceCloseRoom = false;
		if(PRICECLOSESHOW.equals(curComm.getAvlPrice())) { // 如果是关房只显示"X" modify by chenkeming
			bPriceCloseRoom = true;
		}
		
		int days = curComm.getSaleInfoList().size();
		if (days == 0) {
			log.error("商品下没有saleItem,商品id为" + curComm.getCommodityId());
		}
		// BigDecimal roomPriceSum = new BigDecimal(0.0);
		BigDecimal saleSum = new BigDecimal(0.0);
		BigDecimal returnSum = new BigDecimal(0.0);

		for (int i = 0; i < curComm.getSaleInfoList().size(); i++) {
			SaleItemVO saleItemVO = curComm.getSaleInfoList().get(i);
			// BigDecimal roomPrice = new
			// BigDecimal(saleItemVO.getSaleRoomPrice());
			BigDecimal salePrice = new BigDecimal(saleItemVO.getSalePrice());
			BigDecimal returnOfEveryDay = new BigDecimal(saleItemVO
					.getReturnAmount());

			// 过滤价格为0
			// 连住优惠的天数不过滤 -- modify by chenkeming
			if (saleItemVO.getSalePrice() == 0.0
					&& !FAVPRICE.equals(saleItemVO.getSalePriceStr())) {
				days--;
			} else {
				saleSum = saleSum.add(salePrice);
			}

			// roomPriceSum = roomPriceSum.add(roomPrice);
			returnSum = returnSum.add(returnOfEveryDay);
			// 获取床型和早餐类型(每天的床型和早餐类型一样，只取第一天的)
			if (i == 0) {
				String breakfastNum = BreakFastNumConvert
						.getValueForNum(saleItemVO.getBreakfastNum());
				curComm.setCurrency(saleItemVO.getCurrency());
				curComm.setHdltype(saleItemVO.getHdltype());
				curComm.setCurrencySymbol(saleItemVO.getCurrencySymbol());
				curComm.setBreakfastNum(breakfastNum);
			}

		}
		if (days == 0) {
			days = 1;
		}
		// double roomPrice =
		// PriceUtil.numSaveInDecimal(roomPriceSum.doubleValue()/days,1);
		double salePrice = PriceUtil.numSaveInDecimal(saleSum.doubleValue()
				/ days, 1);
		double avlReturnCashNum = PriceUtil.numSaveInDecimal(returnSum
				.doubleValue()
				/ days, 0, PriceUtil.TYPE_FLOOR);
		// curComm.setRoomPrice(String.valueOf(roomPrice));
		curComm.setAvlPrice(String.valueOf(salePrice));
		if (0.0 == salePrice) {
			curComm.setAvlPrice(ZEROPRICE);
		}
		curComm.setReturnCashNum(String.valueOf(returnSum));
		// 强转会不会出错，目的是去掉小数点
		int avlReturnCashNumInt = (int) avlReturnCashNum;
		curComm.setAvlReturnCashNum(String.valueOf(avlReturnCashNumInt));
		String returnCashWithCurrency = setReturnCashWithRMBCurrecny(curComm);
		curComm.setReturnCashWithCurrency(returnCashWithCurrency);
		
		
		// 商品不可预定
		curComm.setCanBook(curComSummary.isCanBook());
		curComm.setCantbookReason(curComSummary.getCantbookReason());
		curComm.setShow(curComSummary.isShow());		
		
		// 设置基类SortedCommodity的是否可预订和最低价属性，作排序用 add by chenkeming
		curComm.setCanBookRoomType(curComm.isCanBook() && curComm.isShow()); 		
		curComm.setMinPirceRoomType(curComSummary.getMinPirceRoomType());
		if (curComm.isShow()) {
			curRoom.getCommodities().add(curComm);
			
			if(bPriceCloseRoom) { // 如果是关房只显示"X" modify by chenkeming
				curComm.setAvlPrice(PRICECLOSESHOW);
				curComm.setReturnCashNum(PRICECLOSESHOW);
				curComm.setAvlReturnCashNum(PRICECLOSESHOW);
				curComm.setReturnCashWithCurrency(PRICECLOSESHOW);	
			}			
		} 
	}

	public void handleSaleInfo(List<SaleInfo> liSaleInfo) {
		//多个saleInfo组成一个saleItemVO（因为saleInfo的颗粒度为只包括一个床型，而saleItemVO是可以包括多个床型的）
		boolean bFirst = true;
		for (SaleInfo saleInfo : liSaleInfo) {			
			if (bFirst
					|| 0 != saleInfo.getAbleDate().compareTo(
							curSaleItem.getRealDate())) {
				SaleItemVO saleItemVO = new SaleItemVO();
				// saleItemVO.setSaleItemId(saleInfo.getPriceId());
				setPriceAndCurrency(saleInfo, saleItemVO);
				saleItemVO.setReturnAmount(saleInfo.getReturnAmount());
				saleItemVO.setWeekDay(WeekDay.getValueByKey(DateUtil
						.getWeekOfDate(saleInfo.getAbleDate())));
				Date curDate = saleInfo.getAbleDate();
				saleItemVO.setAbleDate(DateUtil.formatDate(curDate));
				saleItemVO.setRealDate(curDate);
				saleItemVO.setBreakfastNum(saleInfo.getBreakfastNum());
				saleItemVO.setCommission(saleInfo.getCommission());
				saleItemVO.setHdltype(saleInfo.getHdltype());// 添加直连标示
				curComm.getSaleInfoList().add(saleItemVO);
				curSaleItem = saleItemVO;
				bFirst = false;
			} 
		}
		
	}

	//附加方法
	private String setReturnCashWithRMBCurrecny(CommodityVO commVO){
		if(CurrencyBean.RMB.equals(commVO.getCurrency())){
		     return (RMBCURRENCYSYMBOL+commVO.getAvlReturnCashNum());
		}
		double rateTORMB = CurrencyBean.getRateToRMB(commVO.getCurrency());
		double cashPrice = Double.parseDouble(commVO.getAvlReturnCashNum());
		return (RMBCURRENCYSYMBOL+PriceUtil.numSaveInDecimal(cashPrice,0,PriceUtil.TYPE_FLOOR));	
	}
	
	public void showPriceForSaleInfo(SaleInfo saleInfo, SaleItemVO saleItemVO) {
		String reason = saleInfo.getClosereason();
		if (null == reason
				|| OpenCloseRoom.OpenRoom.equals(saleInfo.getCloseflag())) {
			return;
		}

		// 因变价、订单/投诉、CC关房、装修原因关房的(子)房型显示"X"
		if (CloseRoomReason.ChangPrice.equals(reason)
				|| CloseRoomReason.FitMent.equals(reason)
				|| CloseRoomReason.Complain.equals(reason)
				|| CloseRoomReason.CCClose.equals(reason)) {
			saleItemVO.setSalePriceStr(PRICECLOSESHOW);
			curComm.setAvlPrice(PRICECLOSESHOW);
		}
	}
	
	private void setPriceAndCurrency(SaleInfo saleInfo, SaleItemVO saleItemVO){
		if(saleInfo == null || saleItemVO == null){
			return ;
		}
		saleItemVO.setSaleRoomPrice(Math.ceil(saleInfo.getFullPrice()));
		saleItemVO.setSalePrice(PriceUtil.numSaveInDecimal(saleInfo
				.getSalePrice(), 1));
		// saleItemVO.setSaleRoomPriceStr(String.valueOf(saleItemVO.getSaleRoomPrice()));
		saleItemVO.setSalePriceStr(String.valueOf(saleItemVO.getSalePrice()));
		if (!saleInfo.isShowPrice()) {
			// saleItemVO.setSaleRoomPriceStr(PRICENTOSHOW);
			saleItemVO.setSalePriceStr(PRICENOTSHOW);
		} else {
			showPriceForSaleInfo(saleInfo, saleItemVO);
		}
		
		
		saleItemVO.setCurrency(saleInfo.getCurrency());
		saleItemVO.setCurrencySymbol(PriceUtil.currencyMap.get(saleInfo
				.getCurrency()));
		saleItemVO.setReturnAmount(saleInfo.getReturnAmount());
		// 预付,并且是澳门或香港转为人民币
		if (PayMethod.PRE_PAY.equals(saleInfo.getPayMethod())) {
			double rateTORMB = CurrencyBean
					.getRateToRMB(saleInfo.getCurrency());
			saleItemVO.setSalePrice(PriceUtil.numSaveInDecimal(saleInfo
					.getSalePrice()
					* rateTORMB, 1));
			saleItemVO.setSalePriceStr(String
					.valueOf(saleItemVO.getSalePrice()));
			// saleItemVO.setSalesRoomPrice(Math.ceil(saleInfo.getFullPrice()*rateTORMB));
			saleItemVO.setCurrency(CurrencyBean.RMB);
			saleItemVO.setCurrencySymbol(PriceUtil.currencyMap
					.get(CurrencyBean.RMB));
		}
		// 因为idCurMap的RMB的value不合理，要相应变化
		if (CurrencyBean.RMB.equals(saleItemVO.getCurrency())) {
			saleItemVO.setCurrencySymbol(RMBCURRENCYSYMBOL);
		}
		
		if (saleInfo.getSalePrice() == 0) {
			saleItemVO.setSalePriceStr(ZEROPRICE);
		} else if (999999 == saleInfo.getSalePrice()) {
			saleItemVO.setSalePriceStr(ZEROPRICE);
			saleItemVO.setSalePrice(0.0);
		} else if (99999 == saleInfo.getSalePrice()) { // 处理连住优惠 -- add by chenkeming
			saleItemVO.setSalePriceStr(FAVPRICE);
			saleItemVO.setSalePrice(0.0);
		}
	}
	
	// this method is for specialFix
	private void setHotelRequestService(HotelResultForWebVO hotelVO,HotelInfo hotelInfo){
		hotelVO.setForPlane(hotelInfo.isForPlane());
		hotelVO.setForFreeGym(hotelInfo.isForFreeGym());
		hotelVO.setForFreeStop(hotelInfo.isForFreeStop());
		hotelVO.setForFreePool(hotelInfo.isForFreePool());
		
		hotelVO.setFlagFreePlane(hotelInfo.isForPlane()?"on":"off");
		hotelVO.setFlagFreeGym(hotelInfo.isForFreeGym()?"on":"off");
		hotelVO.setFlagFreeStop(hotelInfo.isForFreeStop()?"on":"off");
		hotelVO.setFlagFreePool(hotelInfo.isForFreePool()?"on":"off");
		
		hotelVO.setHaveFreePlane(hotelInfo.isForPlane()?"有":"无");
		hotelVO.setHaveFreeGym(hotelInfo.isForFreeGym()?"有":"无");
		hotelVO.setHaveFreeStop(hotelInfo.isForFreeStop()?"有":"无");
		hotelVO.setHaveFreePool(hotelInfo.isForFreePool()?"有":"无");
		
	}
	
	private String getCommodityBedTypeValue(String bedType){
		if(null==bedType || "".equals(bedType)){
			return "";
		}
		String bedTypeValue = bedType.replaceAll("1", "大");
		bedTypeValue = bedTypeValue.replaceAll("2", "双");
		bedTypeValue = bedTypeValue.replaceAll("3", "单");
		bedTypeValue = bedTypeValue.replace(",", BEDTYPESPLITSYMBOL);
		return bedTypeValue;
	   // if
	}
	
	public enum WeekDay{
		Monday(1,"周一"),Tuesday(2,"周二"),Wednesday(3,"周三"),Thursday(4,"周四"),
		Friday(5,"周五"),Saturday(6,"周六"),Sunday(7,"周日"),Errorday(0,"日期数字不对");
		
		private int key;
		private String value;
		
		WeekDay(int key,String value){
			this.key = key;
			this.value = value;
		}
		
		 public static WeekDay getEnumByKey(int key) {
			for (int i = 0; i < WeekDay.values().length; i++) {
				if (WeekDay.values()[i].getKey() == key) {
					return WeekDay.values()[i];
				}
			}
			return Errorday;
		}
		public static String getValueByKey(int key){
			return getEnumByKey(key).getValue();
		}

		 
		   public String getValue() {
	            return value;
	        }
	        public int getKey() {
	            return key;
	        }
		
	}
	
    public enum HotelStarConvert{    	
    	FiveStar("19","五星","star5"),QuisaFiveStar("29","准五星","豪华型酒店"),
    	FourStar("39","四星","star4"),QuisaFourStar("49","准四星","高档型酒店"),
    	ThreeStar("59","三星","star3"),QuisaThreeStar("64","准三星","舒适型酒店"),
    	TwoStar("69","二星","star2"),QuisaTwoStar("66","准二星","经济型酒店"),
    	BelowTwoStar("79","二星以下",""),ErrorHotelStar("","error","error");
        
        private String key;
        private String value;
        private String forWebVO;           
        
        private static Map<String, String> mapForWeb = new HashMap<String, String>(10); 
        
        static {
        	mapForWeb.put("19", "star5");
        	mapForWeb.put("39", "star4");
        	mapForWeb.put("59", "star3");
        	mapForWeb.put("69", "star2");
        	mapForWeb.put("79", "");
        	
        	mapForWeb.put("29", "豪华型酒店");
        	mapForWeb.put("49", "高档型酒店");
        	mapForWeb.put("64", "舒适型酒店");
        	mapForWeb.put("66", "经济型酒店");
        	mapForWeb.put("", "error");
        }
        
        HotelStarConvert(String key, String value,String forWebVO) {
        	this.key = key;
        	this.value = value;
            this.forWebVO = forWebVO;
        }
        
        public static HotelStarConvert getEnumByKey(String key) {
        	 for(int i = 0 ; i<HotelStarConvert.values().length;i++){
        		 if(HotelStarConvert.values()[i].getKey().equals(key)){
        		       return HotelStarConvert.values()[i];
        		 }
        	 }
        	 return ErrorHotelStar;
        	}
        public static String getForWebVOByKey(String key){
        	return mapForWeb.get(key);
//        	return getEnumByKey(key).getForWebVO();
        }
        
        
        public String getValue() {
            return value;
        }
        public String getKey() {
            return key;
        }
        public String getForWebVO(){
            return forWebVO;	
        }
    }
	
    public enum BreakFastNumConvert{
    	MinusOne(-1,"1份/人"), Zero(0,"无早"),One(1,"单早"),Two(2,"双早"),Three(3,"三早"),Four(4,"四早"),Five(5,"五早"),
    	Six(6,"六早"),Seven(7,"七早"),Eight(8,"八早"),Nine(9,"九早"),MORE(100,"多于九份早");
    	private int num;
    	private String value;
    	BreakFastNumConvert(int breakFastNum,String breakFastNumValue){
    		this.num = breakFastNum;
    		this.value = breakFastNumValue;
    	}
    	public static BreakFastNumConvert getEnumByNum(int num){
    		for(int i = 0 ;i < BreakFastNumConvert.values().length;i++){
    			if(BreakFastNumConvert.values()[i].getNum()==num){
    				return BreakFastNumConvert.values()[i];
    			}
    		}
    		return MORE;
    	}
    	public static String getValueForNum(int num){
    		return getEnumByNum(num).getValue();
    	}
    	
		public int getNum() {
			return num;
		}
		public void setNum(int num) {
			this.num = num;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
    }
    
    
    //推荐级别
    public enum CommendTypeConvert{
    	SpecialCommend("1","diamondhotel"),GoldenCommend("2","goldenhotel"),SilverCommend("3","silverhotel"),
    	BlackCommend("4",""),ErrorCss("","");
    	private String commendType;
    	private String cssStyle;
    	CommendTypeConvert(String commendType,String cssStyle){
    		this.commendType = commendType;
    		this.cssStyle = cssStyle;
    	}
    	public static CommendTypeConvert getEnumByType(String commendType){
    		for(int i=0;i<CommendTypeConvert.values().length;i++){
    			if(CommendTypeConvert.values()[i].getCommendType().equals(commendType)){
    				return CommendTypeConvert.values()[i];
    			}
    		}
    		return ErrorCss;
    	}
    	
    	public static String getCssStyleName(String commendType){
    		 return getEnumByType(commendType).getCssStyle();
    	}
    	
    	
		public String getCommendType() {
			return commendType;
		}
		public void setCommendType(String commendType) {
			this.commendType = commendType;
		}
		public String getCssStyle() {
			return cssStyle;
		}
		public void setCssStyle(String cssStyle) {
			this.cssStyle = cssStyle;
		}
    }
    
    //添加商品的促销信息到vo中
    private void addPromoLstToCommVO(Commodity commodity, CommodityVO commVO){
    	List<HotelPromoVO> hpVOList = commVO.getPromoLst();
    	for(int i = 0 ;i < commodity.getPromoLst().size();i++){
    		HotelPromo  hp = commodity.getPromoLst().get(i);
    		HotelPromoVO hpVO = new HotelPromoVO();
    		hpVO.setPromoBeginDate(DateUtil.dateToString(hp.getPromoBeginDate()));
    		hpVO.setPromoEndDate(DateUtil.dateToString(hp.getPromoEndDate()));
    		hpVO.setPromoContent(hp.getPromoContent());
    		hpVO.setPromoId(hpVO.getPromoId());
    		hpVOList.add(hpVO);
    	}
    }
    
	//test 
	public static void main(String[] args){
        String dateStr = "2011-03-27";
        System.out.println(DateUtil.getWeekOfDate(DateUtil.getDate(dateStr)));
	}

	public List<HotelResultVO> getHotelResutlList() {

		return hotelResutlList;
	}

	public void setHotelResutlList(List<HotelResultVO> hotelResutlList) {
		this.hotelResutlList = hotelResutlList;
	}

	public int getHotelCount() {
		return hotelCount;
	}

	public void setHotelCount(int hotelCount) {
		this.hotelCount = hotelCount;
	}

	public QueryHotelCondition getQueryHotelCondition() {
		return queryHotelCondition;
	}

	public void setQueryHotelCondition(QueryHotelCondition queryHotelCondition) {
		this.queryHotelCondition = queryHotelCondition;
	}
	
}
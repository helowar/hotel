package com.mangocity.hotel.search.service;

import java.util.List;

import com.mangocity.hotel.search.service.assistant.Commodity;
import com.mangocity.hotel.search.service.assistant.CommoditySummary;
import com.mangocity.hotel.search.service.assistant.HotelInfo;
import com.mangocity.hotel.search.service.assistant.HotelSummaryInfo;
import com.mangocity.hotel.search.service.assistant.RoomTypeInfo;
import com.mangocity.hotel.search.service.assistant.RoomTypeSummaryInfo;
import com.mangocity.hotel.search.service.assistant.SaleInfo;



/**
 * 
 * 酒店排序用的handler, 供各个应用实现
 * 
 * <p>注:本接口的各个回调方法所带的参数对象，其已经设置的属性值在后续可能会随需求而有所改变，
 * 其所返回的属性值以该方法的声明为准
 * 
 * 
 * @author chenkeming
 * the handler must be prototype
 */
public interface IHotelQueryHandler {
	
	/**
	 * 
	 * handler开始处理酒店查询结果，此时
	 * handler可得到酒店总数值 
	 * 
	 * 
	 * @param hotelCount 本次查询条件所查到的酒店总数
	 */
	public void startHandleQueryHotel(int hotelCount);

	/**
	 * 
	 * 结束处理酒店查询结果，此时
	 * handler可作额外的操作，如对酒店进行排序等 
	 * 
	 *  
	 */
	public void endHandleQueryHotel();
	
	/**
	 * 
	 * 开始处理一个酒店信息
	 * 此时hotelInfo的以下属性已经有值:
	 * 	<li>	HotelId 酒店id
		<li>	ChnName 酒店中文名称
		<li>	EngName 英文名
		<li>	ChnAddress 中文地址
		<li>	EngAddress 英文地址
		<li>	HotelIntroduce 酒店介绍
		<li>	AutoIntroduce 生成简介
		<li>	Telephone 电话
			hotelinfo.setWebSiteURL(binfo.getWebSiteURL());// 网站
			hotelinfo.setHotelStar(binfo.getHotelStar());// 星级
			hotelinfo.setCommendType(binfo.getCommendType());// 推荐类型
			hotelinfo.setLayerHigh(binfo.getLayerHigh());// 层高
			hotelinfo.setRoomAmount(binfo.getLayerCount());// 房间总数
			hotelinfo.setPraciceDate(binfo.getPraciceDate());// 开业日期
			hotelinfo.setFitmentDate(binfo.getFitmentDate());// 装修日期
			hotelinfo.setParentHotelGroup(binfo.getParentHotelGroup());// 酒店集团
			hotelinfo.setPlateName(binfo.getPlateName());// 品牌名称
			hotelinfo.setZone(binfo.getZone());// 城区
			hotelinfo.setBizZone(binfo.getBizZone());// 商业区
			hotelinfo.setTrafficInfo(binfo.getTrafficInfo());// 交通信息
			hotelinfo.setContractCurrency(binfo.getContractCurrency());// 币种
			hotelinfo.setCreditCard(binfo.getCreditCard());// 支持信用卡
			hotelinfo.setCheckInDate(DateUtil.dateToString(qcc
					.getInDate()));// 入住日期
			hotelinfo.setCheckOutDate(DateUtil.dateToString(qcc
					.getOutDate()));// 离店日期
			hotelinfo.setLongitude(binfo.getLongitude());// 经度
			hotelinfo.setLatitude(binfo.getLatitude());// 纬度

			hotelinfo.setCooperateChannel(binfo.getCooperateChannel());// 直联渠道
			hotelinfo.setFlagCtsHK(binfo.isFlagCtsHK());// 是否中旅酒店
			hotelinfo.setNoSmokingFloor(binfo.getNoSmokingFloor());// 无烟层
			hotelinfo.setMealFixtrue(binfo.getMealAndFixture());// 餐饮设施
			hotelinfo.setRoomFixtrue(binfo.getRoomFixtrue());// 房型设施
			hotelinfo.setFreeService(binfo.getFreeService());// 免费服务
			hotelinfo.setHandicappedFixtrue(binfo
					.getHandicappedFixtrue());// 残疾人设施
			// 酒店顺序号
			hotelinfo.setSort(binfo.getSorts());//排列顺序			
	 * 
	 * @param hotelInfo
	 */
	public void startHandleHotel(final HotelInfo hotelInfo);
	
	/**
	 * 结束处理一个酒店信息, 会传给handler一些该酒店的预订统计信息, 
	 * 此时curHotelSumInfo的以下属性已经有值:
	 * 		curHotelSumInfo.setCanbook(); // 能否预订
			curHotelSumInfo.setLowestPrice(); // 最低价
	 */
	public void endHandleHotel(HotelSummaryInfo curHotelSumInfo);	
	
	/**
	 * 
	 * 开始处理一个房型信息
	 * 此时房型的以下属性已经有值:
	 * 		roominfo.setRoomtypeName(roomtype.getRoomTypeName());// 房型名称
			roominfo.setRoomTypeId(roomtype.getRoomTypeId());// 房型ID
			roominfo.setRoomtypeName(roomtype.getRoomTypeName());// 房型名称
			roominfo.setRecommend(roomtype.getRecommend());// 推荐级别
			roominfo.setRoomNumber(roomtype.getRoomNumber());// 房间数
			roominfo.setRoomAcreage(roomtype.getRoomAcreage());// 面积
			roominfo.setRoomFloor(roomtype.getRoomFloor());// 楼层
			roominfo.setMaxNumOfPersons(roomtype.getMaxNumOfPersons());// 最大入住人数
			roominfo.setRoomEquipment(roomtype.getRoomEquipment());// 房间设施
			roominfo.setBreakfastLst(roomtype.getBreakfastLst());// 加早情况
			roominfo.setFlagAddBed(roomtype.isFlagAddBed());// 能否加床
			roominfo.setAddBedNum(roomtype.getAddBedNum());// 加床数
			roominfo.setFlagCtsHK(roomtype.isFlagCtsHK());// 中旅房型
			roominfo.setBedLst(roomtype.getBedLst());// 加床价
			roominfo.setBaseRoom(roomtype.isBaseRoom());// 是否基础房型			
	 * 
	 * @param roomTypeInfo
	 */
	public void startHandleRoomType(final RoomTypeInfo roomTypeInfo);
	
	/**
	 * 结束处理一个房型信息, 会传给handler一些该房型的预订统计信息, 
	 * 此时curRoomTypeSumInfo的以下属性已经有值:
	 * 		curRoomTypeSumInfo.setCanbook(true); // 是否可订
			curRoomTypeSumInfo.setMinPrice(); // 最低价
	 */
	public void endHandleRoomType(RoomTypeSummaryInfo curRoomTypeSumInfo);
	
	/**
	 * 
	 * 开始处理一个商品信息
	 * 此时商品的以下属性已经有值:
	 * 	curcom.setCommodityId(currCommoId);// 设置商品ID
		curcom.setPriceTypeId(queryInfo.getRoomtypeId());// 价格类型ID
		curcom.setCommodityName(queryInfo.getCommodityName());
		curcom.setPayMethod(queryInfo.getPaymethod());
		curcom.setRoomtypeId(queryInfo.getRoomtypeId());
		curcom.setFreeNet(isFreeNet(queryInfo.getRoomtypeId(), freeNetLst));// 是否含宽带								
		
		comm.getPromoLst().add(hp); // 促销信息
		comm.getAlertLst().add(ainfo); // 提示信息
	 * 
	 * @param commodity
	 */
	public void startHandleCommodity(final Commodity commodity);
	
	/**
	 * 结束处理一个商品信息, 会传给handler一些该商品的预订统计信息, 
	 * 此时curComSummary的以下属性已经有值:
	 * 	curComSummary.setHdltype(); // 直连类型
	 * 	curComSummary.setShow(); // 商品是否显示
	 * 	
	 * 	// 商品能否预订
		curComSummary.setCantbookReason();
		curComSummary.setCanBook();
	 * 
	 */
	public void endHandleCommodity(CommoditySummary curComSummary);
	
	/**
	 * 
	 * 处理一个商品的所有售卖信息,
	 * 其中每个售卖信息的以下属性已经有值:
	 * 	saleInfo.setMemberType(queryInfo.getMembertype());// 会员类型,如,vip级别.未来的业务可能用到
		saleInfo.setUserType(queryInfo.getUsertype());// 用户类型,如TMC的海尔用户，平安用户,新模型有用
		saleInfo.setCommodityNo(queryInfo.getCommodityNo());// 商品编码
		saleInfo.setPriceId(queryInfo.getPriceId());// 价格ID
		saleInfo.setAbleDate(queryInfo.getAbledate());// 价格
		saleInfo.setFullPrice(queryInfo.getSalesroomprice());// 门市价
		saleInfo.setSalePrice(queryInfo.getSaleprice());// 销售价
		saleInfo.setPayMethod(queryInfo.getPaymethod());// 支付方式
		saleInfo.setHasBenifit(queryInfo.isHaspromptlyReduce());// 是否立减
		saleInfo.setBenifitAmount(queryInfo.getRomptlyReduce());// 优惠立减金额
		saleInfo.setHasReturnCash(queryInfo.isHasReturnCash());// 是否返现
		saleInfo.setReturnAmount(queryInfo.getReturnCash());// 返现数
		saleInfo.setBreakfastPrice(queryInfo.getBreakfastprice());// 早餐价
		saleInfo.setBreakfastType(Integer.parseInt(String.valueOf(queryInfo
				.getBreakfasttype())));// 含早类型
		saleInfo.setBreakfastNum(Integer.parseInt(String.valueOf(queryInfo
				.getBreakfastnumber())));// 含早数
		saleInfo.setCurrency(queryInfo.getCurrency());// 报价币种
		saleInfo.setDealerFavourableId(queryInfo.getDealerFavourableId());
		saleInfo.setDealerPromotionsaleId(queryInfo.getDealerPromotionsaleId());
		saleInfo.setProviderFavourableId(queryInfo.getDealerFavourableId());
		saleInfo.setProviderPromotionsaleId(queryInfo.getDealerFavourableId());
		saleInfo.setBookclauseId(queryInfo.getBookclauseId());// 预订条款ID
		saleInfo.setPayToprepayType(queryInfo.getPaytoprepay());// 允许,必须,不许
		saleInfo.setBedtype(queryInfo.getBedtype());// 大,双,单
		saleInfo.setHdltype(queryInfo.getHdltype());// 直联方式
		if ("1".equals(queryInfo.getNeedAssure())) {// 必须担保
			saleInfo.setNeedAssure(true);
		} else {
			saleInfo.setNeedAssure(false);
		}
		saleInfo.setCloseflag(queryInfo.getCloseflag());// 开关房
		saleInfo.setClosereason(queryInfo.getClosereason());// 关房原因

		saleInfo.setShowPrice(commodityInfoService
				.showPriceForPerday(queryInfo));// 是否显示价格,为了配合销售控制,某些时候不能显示价格.

		commodityInfoService.satisfyClauseForPerday(queryInfo, saleInfo, qcc
				.getInDate(), qcc.getOutDate());// 是否满足条款,不满足条款的原因.必须先设置此项,再判断是否能预订
		
		setRoomStateAndQuota(saleInfo.getRoomstateMaps(),queryInfo); // 设置房态和配额信息
		
		saleInfo.setBookEnAble(flag);//能否预订		
		saleInfo.setReasonOfDisableBook(disableBookReason);//不可预订原因		
	 * 
	 * @param liSaleInfo
	 */
	public void handleSaleInfo(List<SaleInfo> liSaleInfo);	

}

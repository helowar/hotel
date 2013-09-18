package com.mangocity.hotel.search.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mangocity.hotel.base.persistence.HtlAlerttypeInfo;
import com.mangocity.hotel.base.persistence.HtlPriceType;
import com.mangocity.hotel.base.persistence.HtlSalesPromo;
import com.mangocity.hotel.base.web.InitServletImpl;
import com.mangocity.hotel.dreamweb.comment.dao.CommentStatisticsDao;
import com.mangocity.hotel.dreamweb.comment.model.CommentStatistics;
import com.mangocity.hotel.search.constant.CloseRoomReason;
import com.mangocity.hotel.search.constant.NotBookReason;
import com.mangocity.hotel.search.constant.OpenCloseRoom;
import com.mangocity.hotel.search.constant.PayMethod;
import com.mangocity.hotel.search.constant.SaleChannelConstant;
import com.mangocity.hotel.search.dao.HotelInfoDAO;
import com.mangocity.hotel.search.dao.HotelPictureInfoDao;
import com.mangocity.hotel.search.model.HotelAppearanceAlbum;
import com.mangocity.hotel.search.model.HotelBasicInfo;
import com.mangocity.hotel.search.model.HotelCurrentlySatisfyQuery;
import com.mangocity.hotel.search.model.QueryCommodityCondition;
import com.mangocity.hotel.search.model.QueryCommodityInfo;
import com.mangocity.hotel.search.model.QueryHotelCondition;
import com.mangocity.hotel.search.model.RoomType;
import com.mangocity.hotel.search.service.CommodityInfoService;
import com.mangocity.hotel.search.service.HotelInfoCombinationService;
import com.mangocity.hotel.search.service.HotelSearchByPayMethodService;
import com.mangocity.hotel.search.service.IHotelQueryHandler;
import com.mangocity.hotel.search.service.assistant.AlertInfo;
import com.mangocity.hotel.search.service.assistant.Commodity;
import com.mangocity.hotel.search.service.assistant.CommoditySummary;
import com.mangocity.hotel.search.service.assistant.HotelInfo;
import com.mangocity.hotel.search.service.assistant.HotelPromo;
import com.mangocity.hotel.search.service.assistant.HotelSummaryInfo;
import com.mangocity.hotel.search.service.assistant.PreSale;
import com.mangocity.hotel.search.service.assistant.RoomStateByBedType;
import com.mangocity.hotel.search.service.assistant.RoomTypeComparator;
import com.mangocity.hotel.search.service.assistant.RoomTypeInfo;
import com.mangocity.hotel.search.service.assistant.RoomTypeSummaryInfo;
import com.mangocity.hotel.search.service.assistant.SaleInfo;
import com.mangocity.hotel.search.service.assistant.SortResType;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.hotel.constant.SaleChannel;


public class HotelInfoCombinationServiceImpl implements HotelInfoCombinationService {
	private CommodityInfoService commodityInfoService;	
	private HotelInfoDAO hotelInfoDAO;		
	private CommentStatisticsDao commentStatisticsDao;
	
	private HotelPictureInfoDao hotelPictureInfoDao;
	
	private HotelSearchByPayMethodService hotelSearchByPayMethodService;
	
	/**
	 * 根据HotelIDLst过滤掉多余的酒店信息
	 * hotelIdLst是需要显示的查询结果的hotelId列表
	 * hotelBaicInfos是满足酒店查询条件的所有酒店(未剔除不可订的酒店)
	 * @param hotelBasicInfos
	 * @param hotelIdLst
	 */
	public Map<String,HotelBasicInfo> filterHotel(Map<String,HotelBasicInfo> hotelBasicInfos,String hotelIdLst){
		if(null==hotelIdLst||null==hotelBasicInfos) new HashMap<String,HotelBasicInfo>();
		
		Map<String,HotelBasicInfo> hotelMap = new HashMap<String,HotelBasicInfo>();
		String[] hotelIdArr = hotelIdLst.split(",");
		HotelBasicInfo hinfo = null;
		for(int i=0;i<hotelIdArr.length;i++){
			hinfo = hotelBasicInfos.get(hotelIdArr[i]);
			hinfo.setSorts(i+1);//酒店排序序号
			hotelMap.put(hotelIdArr[i], hotelBasicInfos.get(hotelIdArr[i]));
		}
		
		return hotelMap;
	}
	


	/**
	 * 商品是否显示
	 * 关房时,如下原因时在外网不显示商品,在其它渠道暂时都显示
	 * 1、停业,策略性关房CC不可订,策略性关房CC可订,停止合作
	 * 2、商品的售价都为0时
	 */
	
	public boolean showCommodity(Commodity comm, HotelInfo curHotel, String fromChannel){
		boolean isshow = true;
		boolean bNoPrice = false;
		boolean bWeb = false;
		
		if (SaleChannel.WEB.equals(fromChannel)) {// 外网
			
			// 外网不显示预付
			if (PayMethod.PRE_PAY.equals(comm.getPayMethod())
					&& !curHotel.isWebPrepayShow()) {
				comm.setShow(false);
				return false;
			}
			
			bWeb = true;
		} else if (SaleChannel.TMCWEB.equals(fromChannel)
				|| SaleChannel.B2B.equals(fromChannel)) {
			bWeb = true;
		}
		
		if(bWeb) {
			for (SaleInfo curinfo : comm.getLiSaleInfo()) {
				if (OpenCloseRoom.CloseRoom.equals(curinfo.getCloseflag())) {// 关房
					if (CloseRoomReason.CloseDown.equals(curinfo
							.getClosereason())// 停业
							|| CloseRoomReason.CloseCCCan.equals(curinfo
									.getClosereason())// 策略性关房CC可预订
							|| CloseRoomReason.CloseCCNo.equals(curinfo
									.getClosereason())// 策略性关房CC不可预订
							|| CloseRoomReason.NotCollaborate.equals(curinfo
									.getClosereason())) {// 停止合作

						isshow = false;
						break;
					}
				}

				if (curinfo.getSalePrice() > 0.1) {
					bNoPrice = false;
				}
			}// end for
		}
		
		comm.setShow(isshow && !bNoPrice);
		return isshow;
	}
	
	/**
	 * 只显示需要支付方式的房型的商品，add by ting.li,必须在调用showCommodity之前
	 */
	public void showSpecifiedPayMethodCommodity(Commodity comm,String payMethod,HotelInfo curHotel){
		
			// 不包括面转预
			if (comm.getPayMethod().equals(payMethod) && curHotel.isWebPrepayShow()) {
				comm.setShow(true);

			} else {
				comm.setShow(false);
			}

	}
	
	/**
	 * 售卖信息设置房成和配额信息
	 * @param mapRooms
	 * @param queryInfo
	 */
	private void setRoomStateAndQuota(Map<String, RoomStateByBedType> mapRooms,QueryCommodityInfo queryInfo){
		
		RoomStateByBedType roomState = new RoomStateByBedType();
		roomState.setBedtype(Integer.parseInt(queryInfo.getBedtype()));//床型
		roomState.setHasRoom("1".equals(queryInfo.getHasbook())==true?true:false);//能否预订,即是否满房
		roomState.setHasoverdraft("1".equals(queryInfo.getHasoverdraft())==true?true:false);//能否透支
		roomState.setQuotanumber(queryInfo.getQuotanumber());//配额
		
		mapRooms.put(queryInfo.getBedtype(), roomState);
	}

	
	/**
	 *不可预订的房型（价格类型）排在最后
	 *可预订房型按照本部设定推荐房型级别进行排序
	 *基础房型排在非基础房型前面
	 * @param roomLst
	 */
	public void setSortForRoomtype(List roomLst ){
		Collections.sort(roomLst,new RoomTypeComparator());//对商品进行排序
	}
	
	private String getCantBookReasonByCommodity(String reason, String clauseReason){
		if(!StringUtil.isValidStr(reason))return "";
		String tmp = ","+reason+",";
		if (tmp.contains("," + NotBookReason.CloseRoom + ",")) {// 关房
			return NotBookReason.reason.get(NotBookReason.CloseRoom);
		} else if (tmp.contains("," + NotBookReason.NoPrice + ",")) {// 价格为0
			return NotBookReason.reason.get(NotBookReason.NoPrice);
		} else if (tmp.contains("," + NotBookReason.Roomful + ",")) {// 满房
			return NotBookReason.reason.get(NotBookReason.Roomful);
		} else if (tmp.contains("," + NotBookReason.NotSatisfyClause + ",")) {// 不满足条款
			return clauseReason;
		}
		return "";
	}
	
	/**
	 * 过滤掉不满足条件的酒店
	 * @param hotelBasicInfos
	 * @param mapDynamicHotel
	 */
	public void filterDynamicHotel(Map<String,HotelBasicInfo> hotelBasicInfos,List<HotelCurrentlySatisfyQuery> hcLst){
		if(hcLst==null){
			return;
		}
		for(HotelCurrentlySatisfyQuery hc:hcLst){
			if(!hc.isCanSatisfyBreakfast()||!hc.isCanSatisfyPrice()){//删除没早餐的
				hotelBasicInfos.remove(String.valueOf(hc.getHotelId()));	
			}
		}
	}
	
	/**
	 * 将每个房型下面的价格类型列表设置提示信息
	 * @param hspLst
	 * @param pricetypeLst
	 */
	public void setAlertToCommodity(List<HtlAlerttypeInfo> hspLst,Commodity comm,String channel){
		if(null==hspLst||null==comm)return;
		//将List转换为Map
		String pricetypeStr = "";
			String curFlag = "";
			for(HtlAlerttypeInfo alert:hspLst){
				pricetypeStr = (null==alert.getPriceTypeId()?"":alert.getPriceTypeId());//价格类型Id
				if(pricetypeStr.contains(comm.getPriceTypeId()+",")){
					curFlag = null==alert.getLocalFlag()?"":alert.getLocalFlag();
					if(SaleChannelConstant.WEB.equals(channel.toUpperCase())||SaleChannelConstant.TWEB.equals(channel.toUpperCase())||SaleChannelConstant.B2B.equals(channel.toUpperCase())){
						if(!curFlag.contains("2")){//外网不显示
							continue;
						}
					}else{
						if(!curFlag.contains("1")){//CC不显示
							continue;
						}
					}	
					AlertInfo ainfo = new AlertInfo();
					ainfo.setPromoContent(alert.getAlerttypeInfo());//提示信息
					comm.getAlertLst().add(ainfo);
				}
			}
		
	}
	
	
	/**
	 * 给商品设置酒店促销情况
	 * @param hspLst
	 * @param comm
	 */
	public void setSalespromoToCommodity(List<HtlSalesPromo> hspLst,Commodity comm,String channel){
		if(null==hspLst||null==comm)return;
		//将List转换为Map
		String pricetypeStr = "";
			for(HtlSalesPromo promo:hspLst){
				pricetypeStr = (null==promo.getRoomType()?"":promo.getRoomType());//写的是roomtype,实际上是pricetype
				if(pricetypeStr.contains(comm.getPriceTypeId()+",")){
					if(SaleChannelConstant.WEB.equals(channel.toUpperCase())||SaleChannelConstant.TWEB.equals(channel.toUpperCase())||SaleChannelConstant.B2B.equals(channel.toUpperCase())){
						if(promo.getWebShow().equals("0")) {//不在外网显示
							continue;
					}
					}	
					
					HotelPromo hp = new HotelPromo();
					hp.setPromoId(promo.getID());
					hp.setPromoBeginDate(promo.getBeginDate());
					hp.setPromoEndDate(promo.getEndDate());
					hp.setPromoContent(promo.getSalePromoCont());
					comm.getPromoLst().add(hp);
				}
		}
		
	}
	
	/**
	 * 
	 * 给酒店设置芒果促销情况
	 * 
	 * @param preSales
	 * @param hotelInfo
	 * @param channel
	 */
	public void setPreSalesToHotel(List<Object[]> preSales, HotelInfo hotelInfo,
			String channel) {

		if (null == preSales)
			return;		
		
		for (Object[] objects : preSales) {
			if (hotelInfo.getHotelId() == Long.parseLong(objects[0].toString())) {
				PreSale preSale = new PreSale();
				preSale.setPreSaleName(null != objects[1] ? objects[1]
						.toString() : "");
				preSale.setPreSaleContent(null != objects[2] ? objects[2]
						.toString() : "");
				preSale.setPreSaleURL(null != objects[3] ? objects[3]
						.toString() : "");
				preSale.setPreSaleBeginEnd(null != objects[4] ? objects[4]
						.toString() : "");
				hotelInfo.getPreSaleLst().add(preSale);
			}
		}
		
	}	
	
	/**
	 * 
	 * 给酒店设置评论信息
	 * 
	 * @param comments
	 * @param hotelInfo
	 */
	public void setCommentToHotel(List<CommentStatistics> comments, HotelInfo hotelInfo) {
		if(null == comments) {
			return ;
		}
		
		long hotelId = hotelInfo.getHotelId();
		for(CommentStatistics comment : comments) {
			if(hotelId == comment.getHotelId().longValue()) {
				hotelInfo.setComment(comment);
				return;
			}
		}
	}
	
	/**
	 * 
	 * 封装酒店基本信息和售卖信息, 使用handler.  
	 * 
	 * @param hotelBasicInfos
	 * @param commodityInfoLst 已经order by hotelId, roomTypeId, comodityId, payMethod, ableDate
	 * @param qcc
	 * @param hotelCount
	 * @param handler
	 * 
	 * @author chenkeming
	 */
	public void combinationHotelInfoUseHandler(
			Map<String, HotelBasicInfo> hotelBasicInfos,
			List<QueryCommodityInfo> commodityInfoLst,
			QueryCommodityCondition qcc, int hotelCount, IHotelQueryHandler handler) {
		
		// 酒店ID->酒店排序号 的map
		String[] idLst = qcc.getHotelIdLst().split(",");
		Map<String, Integer> hotelIdMap = new HashMap<String, Integer>(idLst.length);
		for(int i=0; i<idLst.length; i++) {
			hotelIdMap.put(idLst[i], i);
		}
		
		// 获取宽带信息
//		List<HtlInternet> freeNetLst = hotelQueryDao.queryFreeNet(qcc);
		
		// 获取促销信息
		List<HtlSalesPromo> salesList = hotelInfoDAO.querySalepromos(qcc);
		
		// 获取芒果促销信息
		List<Object[]> preSales = hotelInfoDAO.queryPreSales(qcc);
		
		// 获取评论信息
		
		//List<CommentSummary> comments = hotelCommentDAO.getHotelComments(qcc.getHotelIdLst());
      
		List<CommentStatistics> comments = commentStatisticsDao.queryCommentStatistics(qcc.getHotelIdLst());
		
		//获取列表页图片信息
		
		List<HotelAppearanceAlbum> hotelAppearanceAlbumList = hotelPictureInfoDao.queryAppearanceAlbum(qcc.getHotelIdLst());
		
		// 获取提示信息
		List<HtlAlerttypeInfo> hspLst = hotelInfoDAO.queryArlerttypeInfoList(qcc);
		
		// 获取供应商信息
		Map<String,HtlPriceType> priceTypeMap = hotelInfoDAO.queryPriceTypeInfo(qcc.getHotelIdLst());
		
		// 入住日期
		Date checkInDate = qcc.getInDate();
		
		// 离店日期-入住日期
		int totalDays = DateUtil.getDay(checkInDate, qcc.getOutDate());				
		
		// 当前酒店id
		long curHotelId = 0;
		
		// 当前的酒店
		HotelInfo curHotel = null;
		
		// 当前房型id
		long curRoomTypeId = 0;
		
		// 当前的房型
		RoomTypeInfo curRoom = null;
		
		// 当前商品id		
		String curCommId = "";
		
		// 当前的商品
		Commodity curComm = null;
		
		// 是否循环第一次调用
		boolean bFirst = true;
		
		// 每个酒店的房型信息临时map
		Map<Long, RoomType> tmpRtMap = Collections.emptyMap();
		
		// 当前商品的床型信息
		boolean bedFill[] = { false, false, false };
				
		// 回调的开始，并把酒店总数传过去 
		handler.startHandleQueryHotel(hotelCount);
		
		// 遍历商品信息
		for (QueryCommodityInfo queryInfo : commodityInfoLst) {			
			
			// 当前商品的酒店id
			long newHotelId = queryInfo.getHotelId();
			
			// 当前商品的房型id
			long newRoomTypeId = queryInfo.getRoomtypeId();
			
			// 当前商品的id
			String newCommId = queryInfo.getCommodityId() + "_" + queryInfo.getPaymethod();					
			
			// 如果是新的酒店
			if(curHotelId != newHotelId) {
				
				// 如果不是第一次循环进来, 则要回调结束处理上一个酒店信息的方法
				if(!bFirst) {							
					if(curComm != null){
					// 处理上一个商品的所有售卖信息
					this.finishFillSaleInfos(curComm, totalDays, bedFill, checkInDate, handler);					
					
					// 结束上一个商品信息的处理
					this.finishFillCommodity(curComm, curRoom, curHotel, qcc, handler);									
					
					// 结束上一个房型信息的处理	
					this.finishFillRoomInfo(curRoom, curHotel, handler);
					
					// 结束上一个酒店信息的处理	
					HotelSummaryInfo curHotelSumInfo = new HotelSummaryInfo();
//					curHotelSumInfo.setCanbook(curHotel.isCanbook());
					curHotelSumInfo.setLowestPrice(curHotel.getLowestPrice());
					handler.endHandleHotel(curHotelSumInfo);
					}
				} else {
					bFirst = false;
				}
				
				// 设置当前酒店id
				curHotelId = newHotelId;		
				
				// 设置当前房型id				
				curRoomTypeId = newRoomTypeId;
				
				// 设置当前商品id				
				curCommId = newCommId;
								
				// 设置新酒店的基本信息
				tmpRtMap = new HashMap<Long, RoomType>();
				curHotel = this.fillHotelInfo(hotelBasicInfos, newHotelId, qcc, tmpRtMap, preSales, comments,hotelAppearanceAlbumList);
				curHotel.setSort(hotelIdMap.get(String.valueOf(newHotelId)));//排列顺序
				
				// 开始处理新酒店
				handler.startHandleHotel(curHotel);
				
				// 设置新房型的信息 
				curRoom = this.fillRoomTypeInfo(tmpRtMap, newRoomTypeId);
				
				// 开始处理新的房型
				// add by diandian.hou
				if(curRoom.getRoomTypeId()==0){
					continue;
				}
				
				// 开始处理新的房型
				handler.startHandleRoomType(curRoom);
				
				// 设置新商品的信息
				curComm = this.fillCommodityInfo(queryInfo, newCommId,
						salesList, hspLst, qcc, curRoom,priceTypeMap);
				
				// 开始处理新的商品
				handler.startHandleCommodity(curComm);				
				
			} else { // 同一个酒店的数据
				
				// 如果是新的房型
				if(curRoomTypeId != newRoomTypeId) {					
					
					// 设置当前房型id				
					curRoomTypeId = newRoomTypeId;		
					
					// 设置当前商品id				
					curCommId = newCommId;
					if(curComm!=null){
					// 处理上一个商品的所有售卖信息
					this.finishFillSaleInfos(curComm, totalDays, bedFill, checkInDate, handler);					
										
					// 结束处理上一个商品信息
					this.finishFillCommodity(curComm, curRoom, curHotel, qcc, handler);
					
					// 结束处理上一个房型信息
					this.finishFillRoomInfo(curRoom, curHotel, handler); 				
					
					// 设置新房型的信息 
					curRoom = this.fillRoomTypeInfo(tmpRtMap, newRoomTypeId);
					
					// 开始处理新的房型
					// add by diandian.hou
					if(curRoom.getRoomTypeId()==0){
						continue;
					}
					
					handler.startHandleRoomType(curRoom);
					
					// 设置新商品的信息
					curComm = this.fillCommodityInfo(queryInfo, newCommId,
							salesList, hspLst, qcc, curRoom,priceTypeMap);
					
					// 开始处理新的商品
					handler.startHandleCommodity(curComm);	
					}
					
				} else { // 同一个房型的数据
					
					// 如果是新的商品			
					if(!curCommId.equals(newCommId)) {						
						if(curComm!=null){
						// 设置当前商品id				
						curCommId = newCommId;
						
						// 处理上一个商品的所有售卖信息
						this.finishFillSaleInfos(curComm, totalDays, bedFill, checkInDate, handler);						
						
						// 结束上一个商品信息的处理
						this.finishFillCommodity(curComm, curRoom, curHotel, qcc, handler);												
						
						// 设置新商品的信息
						curComm = this.fillCommodityInfo(queryInfo, newCommId,
								salesList, hspLst, qcc, curRoom,priceTypeMap);
						
						// 开始处理新的商品
						handler.startHandleCommodity(curComm);	
						}
					}					
					
				}
			}
			
			// 设置新的销售信息
			this.fillSaleInfo(queryInfo, curComm, qcc);
			bedFill[Integer.parseInt(queryInfo.getBedtype()) - 1] = true;
		}
		
		// 如果有信息进行了处理
		if(!bFirst) {
			if(curComm!=null){
			// 处理最后一个商品的所有售卖信息
			this.finishFillSaleInfos(curComm, totalDays, bedFill, checkInDate, handler);
			
			// 结束最后一个商品信息的处理
			this.finishFillCommodity(curComm, curRoom, curHotel, qcc, handler);								
			
			// 结束最后一个房型信息的处理
			this.finishFillRoomInfo(curRoom, curHotel, handler);
			
			// 结束最后一个酒店信息的处理
			HotelSummaryInfo curHotelSumInfo = new HotelSummaryInfo();
			curHotelSumInfo.setCanbook(curHotel.isCanbook());
			curHotelSumInfo.setLowestPrice(curHotel.getLowestPrice());			
			handler.endHandleHotel(curHotelSumInfo);	
			}
		}
		
		// 回调结束 
		handler.endHandleQueryHotel();		
		
	}
	
	
	//  仅仅组装酒店基本信息  add by diandian.hou
	public void fitHotelInfoWithHandler(Map<String,HotelBasicInfo> hotelBasicInfos,QueryHotelCondition queryHotelCondition,SortResType sortRes,IHotelQueryHandler handler){
		// 酒店ID->酒店排序号 的map
		String[] idLst = sortRes.getSortedHotelIdList().split(",");
		Map<String, Integer> hotelIdMap = new HashMap<String, Integer>(idLst.length);
		for(int i=0; i<idLst.length; i++) {
			hotelIdMap.put(idLst[i], i);
		}
		// 获取评论信息
		//List<CommentSummary> comments = hotelCommentDAO.getHotelComments(sortRes.getSortedHotelIdList());
		String sortHotelIdList=sortRes.getSortedHotelIdList();
		List<CommentStatistics> comments=commentStatisticsDao.queryCommentStatistics(sortHotelIdList);
		
		//获取列表页图片信息
				
		List<HotelAppearanceAlbum> hotelAppearanceAlbumList = hotelPictureInfoDao.queryAppearanceAlbum(sortHotelIdList);
		
		HotelInfo curHotel = null;  // 当前的酒店
		// 回调的开始，并把酒店总数传过去 
		handler.startHandleQueryHotel(sortRes.getHotelCount());
		for(Entry<String,Integer> hotelIdEntry :hotelIdMap.entrySet()){
			 String hotelId = hotelIdEntry.getKey();
			 curHotel = fitHotelInfo(hotelId,queryHotelCondition,hotelBasicInfos,comments,hotelAppearanceAlbumList);
			 curHotel.setSort(hotelIdMap.get(String.valueOf(hotelId)));//排列顺序
			// 开始处理新酒店
			 handler.startHandleHotel(curHotel);
		}
		// 回调结束 
		handler.endHandleQueryHotel();	
	}
	
	//设置当前酒店的信息 add by diandian.hou
	private HotelInfo fitHotelInfo(String hotelId,QueryHotelCondition queryHotelCondition,Map<String,HotelBasicInfo> hotelBasicInfos,List<CommentStatistics> comments,List<HotelAppearanceAlbum> hotelAppearanceAlbumList){	
		HotelBasicInfo binfo = hotelBasicInfos.get(String.valueOf(hotelId));
		HotelInfo hotelinfo = new HotelInfo(); 
		if(null != binfo) {
			setHotelInfoOnlyFromLucene(hotelinfo,binfo,queryHotelCondition.getInDate(),queryHotelCondition.getOutDate());
			setCommentToHotel(comments, hotelinfo);
			setHotelAppearanceAlbum(hotelAppearanceAlbumList,hotelinfo);
		} 
		
		
		return hotelinfo;
	}
	

	/**
	 * 设置酒店图片
	 * @param hotelAppearanceAlbumList
	 * @param hotelinfo
	 */
	private void setHotelAppearanceAlbum(List<HotelAppearanceAlbum> hotelAppearanceAlbumList, HotelInfo hotelinfo) {
		boolean outBigPictureName = false;
		boolean outPictureName = false;
		if(outBigPictureName && outPictureName) return;
		
		for(HotelAppearanceAlbum hotelAppearanceAlbum : hotelAppearanceAlbumList){
			long hotelId = hotelinfo.getHotelId();
			if(hotelId == hotelAppearanceAlbum.getHotelId() && hotelAppearanceAlbum.getPictureType() == 3){ //此处取大图 300*200
				hotelinfo.setOutBigPictureName(InitServletImpl.PICTUREURL+hotelAppearanceAlbum.getPrictureUrl());
				outBigPictureName = true;
			}else if(hotelId == hotelAppearanceAlbum.getHotelId() && hotelAppearanceAlbum.getPictureType() == 5){ //此处取小图 90*60
				hotelinfo.setOutPictureName(InitServletImpl.PICTUREURL+hotelAppearanceAlbum.getPrictureUrl());
				outPictureName = true;
			}
		}
		
		//判断OutPictureName 是否为空
		if(!StringUtil.isValidStr(hotelinfo.getOutPictureName())){
			hotelinfo.setOutPictureName("image/small.jpg");
		}
	}



	//设置从lucene过来的基本信息，给hotelInfo设值 add by diandian.hou
	private void setHotelInfoOnlyFromLucene(HotelInfo hotelinfo,HotelBasicInfo binfo,Date inDate,Date outDate){
		hotelinfo.setCityCode(binfo.getCityId());
		//add by hushunqiang
		hotelinfo.setCityName(binfo.getCityName());
		hotelinfo.setHotelId(binfo.getHotelId());// 酒店id
		hotelinfo.setChnName(binfo.getChnName());// 酒店中文名称
		hotelinfo.setEngName(binfo.getEngName());// 英文名
		hotelinfo.setChnAddress(binfo.getChnAddress());// 中文地址
		hotelinfo.setEngAddress(binfo.getEngAddress());// 英文地址
		hotelinfo.setHotelIntroduce(binfo.getHotelIntroduce());// 酒店介绍
		hotelinfo.setAutoIntroduce(binfo.getAutoIntroduce());// 生成简介
		hotelinfo.setTelephone(binfo.getTelephone());// 电话
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
		hotelinfo.setBizChnName(binfo.getBizChnName()); // 商业区中文名称
		hotelinfo.setListTraffic(binfo.getListTraffic());// 交通信息
		hotelinfo.setContractCurrency(binfo.getContractCurrency());// 币种
		hotelinfo.setCreditCard(binfo.getCreditCard());// 支持信用卡
		hotelinfo.setCheckInDate(DateUtil.dateToString(inDate));// 入住日期
		hotelinfo.setCheckOutDate(DateUtil.dateToString(outDate));// 离店日期
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
		hotelinfo.setWebPrepayShow("1".equals(binfo.getWebPrepayShow()) ? true : false); // 外网是否显示预付		
		//设置酒店展示图片
//		hotelinfo.setOutPictureName(binfo.getOutPictureName());
		//地理信息
		hotelinfo.setHtlGeoDistance(binfo.getHtlGeoDistance());
		
		hotelinfo.setPrepayHotel(binfo.isPrepayHotel());
				//促销类型,醒狮计划
		hotelinfo.setPromoteType(binfo.getPromoteType());
	}
	
	/**
	 * 
	 * 处理商品的所有售卖信息
	 * 
	 * @param curComm
	 * @param totalDays
	 * @param bedFill
	 * @param checkInDate
	 * @param handler
	 */
	private void finishFillSaleInfos(Commodity curComm, int totalDays,
			boolean[] bedFill, Date checkInDate, IHotelQueryHandler handler) {

		int total = 0;
		int beds = 0;
		StringBuilder bedAll = new StringBuilder("");
		for (int i = 0; i < bedFill.length; i++) {
			if (bedFill[i]) {
				bedAll.append((i+1)).append(",");
				beds++;
				total += totalDays;
			}
		}
		if(0 < bedAll.length()) {
			bedAll.deleteCharAt(bedAll.length() - 1);			
		}
		curComm.setBedAll(bedAll.toString());
		List<SaleInfo> liSaleInfo = curComm.getLiSaleInfo();
		int nSize = liSaleInfo.size();
		if (total > nSize) {
			int curIndex = 0;
			int index = 0;
			List<SaleInfo> liNewInfo = new ArrayList<SaleInfo>(total);
			int bed2Index[] = new int[bedFill.length];
			int index2Bed[] = new int[bedFill.length];
			for(int i=0; i<bedFill.length; i++) {
				if (bedFill[i]) {
					bed2Index[i] = index;
					index2Bed[index++] = i;
				}
			}
			index = 0;
			for (SaleInfo curInfo : liSaleInfo) {
				Date date = curInfo.getAbleDate();
				int dayIndex = DateUtil.getDay(checkInDate, date);
				index = dayIndex * beds
						+ bed2Index[Integer.parseInt(curInfo.getBedtype()) - 1];
				if (index > curIndex) {
					for (int i = curIndex; i < index; i++) {
						dayIndex = (int) (i / beds);
						SaleInfo saleInfo = new SaleInfo();
						saleInfo.setAbleDate(DateUtil.getDate(checkInDate,
								dayIndex));
						saleInfo.setBedtype(String.valueOf(index2Bed[i - dayIndex * beds]
								+ 1));
						saleInfo.setCurrency(curComm.getCurrency());
						liNewInfo.add(saleInfo);
					}
				}
				liNewInfo.add(curInfo);
				curIndex = index + 1;
			}
			if (curIndex < total) {
				for (int i = curIndex; i < total; i++) {
					int dayIndex = (int) (i / beds);
					SaleInfo saleInfo = new SaleInfo();
					saleInfo.setAbleDate(DateUtil
							.getDate(checkInDate, dayIndex));
					saleInfo
							.setBedtype(String.valueOf(index2Bed[i - dayIndex * beds] + 1));
					saleInfo.setCurrency(curComm.getCurrency());					
					liNewInfo.add(saleInfo);
				}
			}
			curComm.setLiSaleInfo(liNewInfo);
		}
		
		for (int j = 0; j < bedFill.length; j++) {
			bedFill[j] = false;
		}

		handler.handleSaleInfo(curComm.getLiSaleInfo());

	}
	
	/**
	 * 
	 * 设置新酒店的基本信息, 并设置该酒店房型信息map
	 * 
	 * @param hotelBasicInfos
	 * @param newHotelId
	 * @param qcc
	 * @param tmpRtMap
	 * @param preSales
	 * @param comments
	 * @return
	 * 
	 * @author chenkeming
	 */
	private HotelInfo fillHotelInfo(
			Map<String, HotelBasicInfo> hotelBasicInfos, long newHotelId,
			QueryCommodityCondition qcc, Map<Long, RoomType> tmpRtMap, List<Object[]> preSales,
			List<CommentStatistics> comments,List<HotelAppearanceAlbum> hotelAppearanceAlbumList) {

		HotelBasicInfo binfo = hotelBasicInfos.get(String.valueOf(newHotelId));
		HotelInfo hotelinfo = new HotelInfo(); 
		if(null != binfo) {
			setHotelInfoOnlyFromLucene(hotelinfo,binfo,qcc.getInDate(),qcc.getOutDate());
			// 设置临时房型信息map
			for (RoomType roomtype : binfo.getListRoomType()) {
				//lucene文件可能出现roomtypeId为0的情况 add by diandian.hou 2011-06-25
				if(roomtype.getRoomTypeId()!=0){
				    tmpRtMap.put(roomtype.getRoomTypeId(), roomtype);
				}
			}
			
			setPreSalesToHotel(preSales, hotelinfo, qcc.getFromChannel());
			
			setCommentToHotel(comments, hotelinfo);
			
			setHotelAppearanceAlbum(hotelAppearanceAlbumList,hotelinfo);
		} 

		return hotelinfo;
	}
	
	/**
	 * 
	 * 设置新房型信息
	 * 
	 * @param tmpRtMap
	 * @param newRoomTypeId
	 * @return
	 * 
	 * @author chenkeming
	 */
	private RoomTypeInfo fillRoomTypeInfo(Map<Long, RoomType> tmpRtMap,
			long newRoomTypeId) {
		RoomType roomtype = tmpRtMap.get(newRoomTypeId);
		RoomTypeInfo roominfo = new RoomTypeInfo();

		if (null != roomtype) {
			roominfo.setRoomtypeName(roomtype.getRoomTypeName());// 房型名称
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
			String equipment = roomtype.getRoomEquipment();
			if(null != equipment) {
				roominfo.setHasNet(0 <= equipment.indexOf("21,") || 0 <= equipment.indexOf("25,"));	
			}
		}

		return roominfo;
	}
	
	/**
	 * 
	 * 设置新商品信息
	 * 
	 * @param queryInfo
	 * @param currCommoId
	 * @param salesList
	 * @param hspLst
	 * @param qcc
	 * @param curRoom
	 * @return
	 * 
	 * @author chenkeming
	 */
	private Commodity fillCommodityInfo(QueryCommodityInfo queryInfo,
			String currCommoId, List<HtlSalesPromo> salesList, List<HtlAlerttypeInfo> hspLst, 
			QueryCommodityCondition qcc, RoomTypeInfo curRoom ,Map<String,HtlPriceType> priceTypeMap) {

		Commodity curcom = new Commodity();
		curcom.setCommodityId(currCommoId);// 设置商品ID
		curcom.setPriceTypeId(queryInfo.getCommodityId());// 价格类型ID
		curcom.setCommodityName(queryInfo.getCommodityName());
		curcom.setPayMethod(queryInfo.getPaymethod());
		curcom.setPayToPrepay(queryInfo.getPaytoprepay());
		curcom.setRoomtypeId(queryInfo.getRoomtypeId());
		
		HtlPriceType priceType=priceTypeMap.get(queryInfo.getCommodityId().toString());
		if(priceType!=null && ( priceType.getSupplierID()!=null && priceType.getSupplierID().longValue()>0 )) {
			curcom.setHasSupplier(true);
			
		}
		if(priceType!=null){
			curcom.setShowMemberPrice(priceType.getShowMemberPrice());
		}
		else{
			curcom.setShowMemberPrice(false);
		}
		// 设置商品促销信息
		setSalespromoToCommodity(salesList, curcom, qcc.getFromChannel());

		// 设置商品提示信息
		setAlertToCommodity(hspLst, curcom, qcc.getFromChannel());

		return curcom;
	}
	
	/**
	 * 
	 * 设置新的销售信息
	 * 
	 * @param queryInfo
	 * @param curComm
	 * @param qcc
	 * 
	 * @author chenkeming
	 */
	private void fillSaleInfo(QueryCommodityInfo queryInfo,
			Commodity curComm, QueryCommodityCondition qcc) {
		
		String currency = queryInfo.getCurrency();
		
		SaleInfo saleInfo = new SaleInfo();

		saleInfo.setMemberType(queryInfo.getMembertype());// 会员类型,如,vip级别.未来的业务可能用到
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
		saleInfo.setBreakfastType(Integer.parseInt(String.valueOf(queryInfo.getBreakfasttype())));// 含早类型
		saleInfo.setBreakfastNum(Integer.parseInt(String.valueOf(queryInfo
				.getBreakfastnumber())));// 含早数

		saleInfo.setCurrency(currency);// 报价币种
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
				.showPriceForPerday(queryInfo, qcc.getFromChannel()));// 是否显示价格,为了配合销售控制,某些时候不能显示价格.

		// 更新该销售信息所属商品的最低价
		double salePrice = saleInfo.getSalePrice();
		if (curComm != null) {
			curComm.setCurrency(currency);

			if (0.1 < salePrice) {
				double minPrice = curComm.getMinPirceRoomType();
				if (0.1 < minPrice) {
					if (minPrice > salePrice) {
						curComm.setMinPirceRoomType(salePrice);
					}
				} else {
					curComm.setMinPirceRoomType(salePrice);
				}
			}

			// 如果是首日，则判断预订条款
			if (0 == qcc.getInDate().compareTo(saleInfo.getAbleDate())) {
				curComm.setFreeNet(queryInfo.isFreeNet());
				commodityInfoService.satisfyClauseForPerday(queryInfo, saleInfo, qcc.getInDate(), qcc.getOutDate());// 是否满足条款,不满足条款的原因.必须先设置此项,再判断是否能预订
			}

			setRoomStateAndQuota(saleInfo.getRoomstateMaps(), queryInfo); // 设置房态和配额信息
			saleInfo.setCommission(queryInfo.getCommission());// 给芒果的佣金

			curComm.getLiSaleInfo().add(saleInfo);

		}

	}
	
	/**
	 * 
	 * 处理完一个商品的所有销售信息后, 根据该商品各天的销售信息来设置商品的某些属性, 并进行结束处理的回调
	 * 
	 * @param curComm
	 * @param curRoom
	 * @param curHotel
	 * @param qcc
	 * @param handler
	 * 
	 * @author chenkeming
	 */
	private void finishFillCommodity(Commodity curComm, RoomTypeInfo curRoom, HotelInfo curHotel, 
			QueryCommodityCondition qcc, IHotelQueryHandler handler) {		

		/**
		 * 1. 判断商品能否预订(每天的情况) 
		 * 2. 确定商品的直联方式,商品的直联方式暂时取首日的直联方式,完全可以满足当前和未来很长时间的业务需要
		 * 3. 商品能否预订 
		 * 
		 */

		boolean canbook = false;
		boolean canBookBed[] = {true, true, true};
		StringBuilder cantbookReason = new StringBuilder("");
		String notSatisfyClauseReason = "";
		for(SaleInfo curInfo : curComm.getLiSaleInfo()) {
			
			if (0 == DateUtil.getDay(curInfo.getAbleDate(), qcc.getInDate())) {// 入住首日
				curComm.setHdltype(curInfo.getHdltype());
			}
			
			int nBedType = Integer.parseInt(curInfo.getBedtype()) - 1;
			
			if(!canBookBed[nBedType]) {
				continue;
			}
			
			commodityInfoService.setCanbookPerDay(curInfo, qcc);// 每一天能否预订

			if (!curInfo.isBookEnAble()) {
				canBookBed[nBedType] = false;
				String reason = curInfo.getReasonOfDisableBook();
				cantbookReason.append(reason).append(",");
				if(NotBookReason.NotSatisfyClause.equals(reason)) {
					notSatisfyClauseReason = curInfo.getNotsatisfyClauseOfReason();
				}
			}
									
		}
		StringBuilder bedTypes = new StringBuilder("");
		String bedAll = curComm.getBedAll();
		for(int i=0; i<canBookBed.length; i++) {
			if(canBookBed[i] && 0 <= bedAll.indexOf("" + (i+1))) {
				canbook = true;
				bedTypes.append(i + 1).append(",");
			}
		}
		curComm.setCanBook(canbook);
		if(canbook) {
			bedTypes.deleteCharAt(bedTypes.length() - 1);					
			curComm.setBedtype(bedTypes.toString());	
		} else {
			curComm.setBedtype(bedAll);
			curComm.setCantbookReason(getCantBookReasonByCommodity(cantbookReason
					.toString(), notSatisfyClauseReason));	
		}
		

		/**
		 * 商品是否显示
		 */
		showCommodity(curComm, curHotel, qcc.getFromChannel());
		
		//显示指定支付方式的酒店，add by ting.li
		if(qcc.getPayMethod()!=null && !"".equals(qcc.getPayMethod())){
			showSpecifiedPayMethodCommodity(curComm,qcc.getPayMethod(),curHotel);
		}

		// 更新该商品所属房型的能否预订属性
		if (!curRoom.isCanbook() && curComm.isCanBook()) {
			curRoom.setCanbook(true);
		}

		// 更新该商品所属房型的最低价
		double minPriceCom = curComm.getMinPirceRoomType();
		if(0.1 < minPriceCom) {
			double minPrice = curRoom.getMinPrice();
			if (0.1 < minPrice) {
				if(minPrice > minPriceCom) {
					curRoom.setMinPrice(minPriceCom);
				}
			} else {
				curRoom.setMinPrice(minPriceCom);
			}
		}

		// 回调结束处理该商品信息
		CommoditySummary curComSummary = new CommoditySummary();
		curComSummary.setMinPirceRoomType(minPriceCom);
		curComSummary.setHdltype(curComm.getHdltype());
		curComSummary.setShow(curComm.isShow());
		curComSummary.setCanBook(curComm.isCanBook());
		curComSummary.setCantbookReason(curComm.getCantbookReason());
		curComSummary.setBedType(curComm.getBedtype());
		curComSummary.setBandInfo(curComm.isFreeNet() ? "免费" : (curRoom.isHasNet() ? "收费" : "无"));// 是否含宽带
		handler.endHandleCommodity(curComSummary);
	}
	
	
	/**
	 * 
	 * 处理完一个房型的信息后, 根据该房型的信息来更新其所属酒店信息的某些属性, 并进行结束处理的回调
	 * 
	 * @param curRoom
	 * @param hotelinfo
	 * @param handler
	 */
	private void finishFillRoomInfo(RoomTypeInfo curRoom, HotelInfo hotelinfo,
			IHotelQueryHandler handler) {

		// 更新该房型所属酒店的能否预订属性
		/*if (!curRoom.isCanbook()) {
			hotelinfo.setCanbook(false);
		}*/

		// 更新该房型所属酒店的最低价		
		double minPriceRoom = curRoom.getMinPrice();
		if(0.1 < minPriceRoom) {
			double minPrice = hotelinfo.getLowestPrice();
			if (0.1 < minPrice) {
				if(minPrice > minPriceRoom) {
					hotelinfo.setLowestPrice(minPriceRoom);
				}
			} else {					
				hotelinfo.setLowestPrice(minPriceRoom);
			}	
		}		

		// 回调结束该房型信息的处理
		RoomTypeSummaryInfo curRoomTypeSumInfo = new RoomTypeSummaryInfo();
		curRoomTypeSumInfo.setCanbook(curRoom.isCanbook());
		curRoomTypeSumInfo.setMinPrice(curRoom.getMinPrice());
		handler.endHandleRoomType(curRoomTypeSumInfo);
	}	

	public void setCommodityInfoService(CommodityInfoService commodityInfoService) {
		this.commodityInfoService = commodityInfoService;
	}



	public void setHotelInfoDAO(HotelInfoDAO hotelInfoDAO) {
		this.hotelInfoDAO = hotelInfoDAO;
	}



	public CommentStatisticsDao getCommentStatisticsDao() {
		return commentStatisticsDao;
	}


	public void setHotelSearchByPayMethodService(HotelSearchByPayMethodService hotelSearchByPayMethodService) {
		this.hotelSearchByPayMethodService = hotelSearchByPayMethodService;
	}



	public void setCommentStatisticsDao(CommentStatisticsDao commentStatisticsDao) {
		this.commentStatisticsDao = commentStatisticsDao;
	}



	public void setHotelPictureInfoDao(HotelPictureInfoDao hotelPictureInfoDao) {
		this.hotelPictureInfoDao = hotelPictureInfoDao;
	}
	
}

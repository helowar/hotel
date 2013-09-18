package com.mangocity.hdl.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zhx.constant.ResultConstant;
import zhx.constant.ZhxErrorMsg;
import zhx.dto.OTRequestHARQ;
import zhx.dto.OTResponseHARS;
import zhx.dto.RoomStay;
import zhx.dto.OTRequestHARQ.HotelAvailRQ;
import zhx.dto.OTRequestHARQ.HotelAvailRQ.HotelAvailCriteria;
import zhx.dto.OTRequestHARQ.HotelAvailRQ.HotelAvailCriteria.HotelSearchCriteria;
import zhx.dto.OTRequestHARQ.HotelAvailRQ.HotelAvailCriteria.StayDateRange;
import zhx.dto.OTRequestHARQ.HotelAvailRQ.HotelAvailCriteria.HotelSearchCriteria.HotelRef;
import zhx.dto.RoomStay.RoomRates;
import zhx.dto.RoomStay.RoomRates.RoomRate;
import zhx.dto.RoomStay.RoomRates.RoomRate.Rates;
import zhx.dto.RoomStay.RoomRates.RoomRate.Rates.Rate;
import zhx.service.IZhxService;
import zhx.vo.HdlRes;
import zhx.vo.SingleHotelReq;

import com.mangocity.hdl.constant.ChannelType;
import com.mangocity.hdl.constant.CheckResType;
import com.mangocity.hdl.hotel.dto.AddExRoomOrderRequest;
import com.mangocity.hdl.hotel.dto.AddExRoomOrderResponse;
import com.mangocity.hdl.hotel.dto.CancelExRoomOrderRequest;
import com.mangocity.hdl.hotel.dto.CancelExRoomOrderResponse;
import com.mangocity.hdl.hotel.dto.CheckHotelReservateExRequest;
import com.mangocity.hdl.hotel.dto.CheckHotelReservateExResponse;
import com.mangocity.hdl.hotel.dto.CheckReservateExRequest;
import com.mangocity.hdl.hotel.dto.CheckReservateExResponse;
import com.mangocity.hdl.hotel.dto.DailyAuditExRequest;
import com.mangocity.hdl.hotel.dto.DailyAuditExResponse;
import com.mangocity.hdl.hotel.dto.MGExHotelListRequest;
import com.mangocity.hdl.hotel.dto.MGExHotelListResponse;
import com.mangocity.hdl.hotel.dto.MGExHotelRoomListRequest;
import com.mangocity.hdl.hotel.dto.MGExHotelRoomListResponse;
import com.mangocity.hdl.hotel.dto.MGExReservItem;
import com.mangocity.hdl.hotel.dto.MGExResult;
import com.mangocity.hdl.hotel.dto.MangoHotelService;
import com.mangocity.hdl.hotel.dto.ModifyExRoomOrderRequest;
import com.mangocity.hdl.hotel.dto.ModifyExRoomOrderResponse;
import com.mangocity.hdl.hotel.dto.PriceCompareRequest;
import com.mangocity.hdl.hotel.dto.PriceCompareResponse;
import com.mangocity.hdl.hotel.dto.QueryHotelInfoExRequest;
import com.mangocity.hdl.hotel.dto.QueryHotelInfoExResponse;
import com.mangocity.hdl.hotel.dto.QueryPriceTypeRequest;
import com.mangocity.hdl.hotel.dto.QueryPriceTypeResponse;
import com.mangocity.hdl.hotel.dto.QueryRoomTypeInfoExRequest;
import com.mangocity.hdl.hotel.dto.QueryRoomTypeInfoExResponse;
import com.mangocity.hdl.hotel.dto.SendTestInfoRequest;
import com.mangocity.hdl.hotel.dto.SendTestInfoResponse;
import com.mangocity.hdl.service.IExMappingService;
import com.mangocity.hdl.service.IHDLService;
import com.mangocity.hdl.vo.MultiHotelReq;
import com.mangocity.hotel.base.persistence.ExMapping;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.log.MyLog;

/**
 * 调用HDL提供的WebService接口实现
 * 
 * @author chenkeming
 * 
 */
public class HDLServiceImpl implements IHDLService {

	private static final MyLog log = MyLog.getLogger(HDLServiceImpl.class);
	
	/**
	 * HDL的web service接口服务
	 */
	private MangoHotelService mangoHotelService;
	
    /**
     * 芒果和直连编码映射服务
     */
    private IExMappingService exService;

	/**
	 * 中航信提供的service - add by chenkeming
	 */
	private IZhxService zhxService;

	/**
	 * 测试接口
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public SendTestInfoResponse sendTestInfo(SendTestInfoRequest req)
			throws Exception {

		SendTestInfoResponse sendTestInfoResponse = mangoHotelService
				.sendTestInfo(req);
		return sendTestInfoResponse;
	}

	/**
	 * 新增订单
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public AddExRoomOrderResponse addExRoomOrder(AddExRoomOrderRequest req)
			throws Exception {

		AddExRoomOrderResponse addExMappingResponse = mangoHotelService
				.addExRoomOrder(req);
		return addExMappingResponse;

	}

	/**
	 * 修改订单
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public ModifyExRoomOrderResponse modifyExRoomOrder(
			ModifyExRoomOrderRequest req) throws Exception {
		// TODO Auto-generated method stub
		ModifyExRoomOrderResponse modifyExRoomOrderResponse = mangoHotelService
				.modifyExRoomOrder(req);
		return modifyExRoomOrderResponse;
	}

	/**
	 * 取消订单
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public CancelExRoomOrderResponse cancelExRoomOrder(
			CancelExRoomOrderRequest req) throws Exception {
		// TODO Auto-generated method stub
		CancelExRoomOrderResponse cancelExRoomOrderResponse = mangoHotelService
				.cancelExRoomOrder(req);
		return cancelExRoomOrderResponse;
	}

	/**
	 * 调用日审接口
	 */
	public DailyAuditExResponse dailyAudit(DailyAuditExRequest req)
			throws Exception {

		DailyAuditExResponse res = mangoHotelService.dailyAuditEx(req);
		return res;
	}

	/**
	 * 调用查询酒店基本信息接口
	 */
	public QueryHotelInfoExResponse queryHotelInfo(QueryHotelInfoExRequest req)
			throws Exception {

		QueryHotelInfoExResponse res = mangoHotelService.queryHotelInfoEx(req);
		return res;
	}

	/**
	 * 调用查询酒店房型信息接口
	 */
	public QueryRoomTypeInfoExResponse queryRoomTypeInfo(
			QueryRoomTypeInfoExRequest req) throws Exception {

		QueryRoomTypeInfoExResponse res = mangoHotelService
				.queryRoomTypeInfoEx(req);
		return res;
	}

	/**
	 * 调用试预订接口
	 * 
	 * @param req
	 * @return
	 */
	public CheckReservateExResponse checkReservate(CheckReservateExRequest req)
			throws Exception {
		
		// 如果是中航信 add by chenkeming
		if(ChannelType.CHANNEL_ZHX == req.getChannelType()) {
			SingleHotelReq shr = new SingleHotelReq();
			shr.setChannelType(ChannelType.CHANNEL_ZHX);
			shr.setCheckInDate(req.getCheckInDate());
			shr.setCheckOutDate(req.getCheckOutDate());
			shr.setChildRoomTypeId(req.getChildRoomTypeId());
			shr.setHotelId(req.getHotelId());
			shr.setRoomTypeId(req.getRoomTypeId());
			shr.setQuantity(req.getQuantity());
			CheckReservateExResponse res = new CheckReservateExResponse();
			HdlRes<OTResponseHARS> hdlRes = null;
            try {
            	hdlRes = zhxService.querySingleAvailForOrder(shr);
            } catch (Exception e) {
            	log.error(e.getMessage(), e);
                System.out.println("detail error " + e);
                res.setResult(0);
                res.setReason("合作方系统错误!!!!");
                return res;
            }
			
									
			MGExResult result = hdlRes.getResult();
			
			// 返回错误信息
	        if( ResultConstant.RESULT_SUCCESS != result.getValue() ){
	        	res.setResult(0);
	        	res.setReason(result.getMessage());
	            return res;
	        }
			
	        // 成功返回
	        OTResponseHARS resHars = hdlRes.getObj();
			OTResponseHARS.HotelAvailRS availRs = resHars.getHotelAvailRS();
			OTResponseHARS.HotelAvailRS.RoomStays roomStays = availRs
					.getRoomStays();
			List<RoomStay> lstRoomStay = roomStays.getRoomStay();
			List<MGExReservItem> reservItems = req.getReservItems();
			Date checkInDate = DateUtil.getDate(req.getCheckInDate());
			for (int i = 0; i < lstRoomStay.size(); i++) {
				RoomStay roomStay = lstRoomStay.get(i);
				RoomRates roomRates = roomStay.getRoomRates();
				List<RoomRate> lstRoomRate = roomRates.getRoomRate();
				for (int y = 0; y < lstRoomRate.size(); y++) {
					RoomRate roomRate = lstRoomRate.get(y);
					
					// 是否无法预订
					String status = roomRate.getAvailabilityStatus();
					if("noavail".equalsIgnoreCase(status)) {
			        	res.setResult(0);
			        	res.setReason("无房");
			            return res;
					}
					
					Rates rates = roomRate.getRates();
					List<Rate> liRate = rates.getRate();
					List<MGExReservItem> chgItems = new ArrayList<MGExReservItem>();
					for(Rate rate : liRate) {
						Date startDate = DateUtil.getDate(rate.getStartDate());
						int startIndex = DateUtil.getDay(startDate, checkInDate);
						Date endDate = DateUtil.getDate(rate.getEndDate());
						int days = DateUtil.getDay(startDate, endDate);
						float newPrice = Float.parseFloat(rate.getAmountPrice());
						for(int j=0; j<=days; j++) {
							int dayIndex = startIndex + j;
							MGExReservItem mgExReservItem = reservItems.get(dayIndex);
							
	                        // 如果价格不同，表明合作方价格发生了变化，需要更新芒果 数据
                            MGExReservItem newMGExReservItem = new MGExReservItem();
                            newMGExReservItem.setDayIndex(dayIndex);
                            if (newPrice != mgExReservItem.getSalePrice()) {
								newMGExReservItem.setSalePrice(newPrice);
								newMGExReservItem.setChange(1);
								chgItems.add(mgExReservItem);
							} else {
								newMGExReservItem.setSalePrice(mgExReservItem
										.getSalePrice());
								newMGExReservItem.setChange(0);
							}
							newMGExReservItem.setFirstDayPrice((float) 0);
							res.getReservItems().add(newMGExReservItem);
						}
					}
					
					// TODO: 需要更新芒果价格
					if(!chgItems.isEmpty()) {
						
					}
				}
			}
			
			res.setResult(CheckResType.PASS);
			return res;
		}
		
		
		CheckReservateExResponse res = mangoHotelService.checkReservateEx(req); 
		return res;
	}

	/**
	 * 取得所有酒店编码和名称
	 * 
	 * @param mgExHotelListRequest
	 * @return
	 */
	public MGExHotelListResponse getHotelList(
			MGExHotelListRequest mgExHotelListRequest) throws Exception {
		MGExHotelListResponse res = mangoHotelService
				.getHotelList(mgExHotelListRequest) ;
		return res;
	}

	/**
	 * 取得所有酒店房型
	 * @param mgExHotelRoomListRequest
	 * @return
	 */
	public MGExHotelRoomListResponse getHotelRoomList(
			MGExHotelRoomListRequest mgExHotelRoomListRequest) throws Exception {
		MGExHotelRoomListResponse res = mangoHotelService
				.getHotelRoomList(mgExHotelRoomListRequest);
		return res;
	}
	
	/**
	 * 查询单酒店
	 * 
	 * @param req
	 * @return
	 */
	public HdlRes querySingleHotel(SingleHotelReq req) {
		
		HdlRes res = new HdlRes();
		MGExResult result = new MGExResult();
		res.setResult(result);
		
		int channelType = req.getChannelType();
		long hotelId = req.getHotelId();
		
		// 获取合作方的酒店编码
        String hotelCode = "";
        List<Long> hotelIds = new ArrayList<Long>();
        hotelIds.add(hotelId);
        List exMappingLst = exService.getMapping(channelType,
				hotelIds);
		if (!exMappingLst.isEmpty()) {
			ExMapping em = (ExMapping) exMappingLst.get(0);
			hotelCode = em.getHotelcodeforchannel();
		} else {
			hotelCode = "SOHOTO00001";
//			result.setValue(ResultConstant.RESULT_MANGO_ERROR);
//			result.setMessage("找不到映射的直连方酒店！");
//			return res;
		}
		
		try {
			if(ChannelType.CHANNEL_ZHX == channelType) { // 中航信
				OTRequestHARQ reqZhx = new OTRequestHARQ();
				HotelAvailRQ availRq = new HotelAvailRQ();
				reqZhx.setHotelAvailRQ(availRq);
				HotelAvailCriteria availCriteria = new HotelAvailCriteria();
				availRq.setHotelAvailCriteria(availCriteria);
				HotelSearchCriteria searchCriteria = new HotelSearchCriteria();
				availCriteria.setHotelSearchCriteria(searchCriteria);
				HotelRef hotelRef = new HotelRef();
				searchCriteria.setHotelRef(hotelRef);
				hotelRef.setHotelCode(hotelCode); // 酒店编码
				StayDateRange dateRange = new StayDateRange();				
				dateRange.setCheckInDate("2010-09-28"); // 入住日期
				dateRange.setCheckOutDate("2010-09-29"); // 离店日期
//				dateRange.setCheckInDate(req.getCheckInDate()); // 入住日期
//				dateRange.setCheckOutDate(req.getCheckOutDate()); // 离店日期
				availCriteria.setStayDateRange(dateRange);
				OTResponseHARS resZhx = zhxService.querySingleAvail(reqZhx);

				OTResponseHARS.HotelAvailRS availRs = resZhx.getHotelAvailRS();
				if(null == availRs) {
					result.setValue(ResultConstant.RESULT_MANGO_ERROR);
					result.setMessage("返回信息为空！");
					return res;
				}
				
				OTResponseHARS.HotelAvailRS.Errors errors = availRs.getErrors();				
				if(null != errors) { // 如果zhx有业务错误返回 TODO:zhx的系统级错误
					result.setValue(ResultConstant.RESULT_CHANNEL_ERROR);					
					OTResponseHARS.HotelAvailRS.Errors.Error error = errors.getError();
					result.setMessage(ZhxErrorMsg.MsgMap.get(error.getErrorCode()));
					return res;
				}
				
				// 正常返回结果
				result.setValue(ResultConstant.RESULT_SUCCESS);
				res.setObj(resZhx);
				
			} else {
				result.setValue(ResultConstant.RESULT_MANGO_ERROR);
				result.setMessage("渠道号错误");
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			log.error(e.getMessage(),e);
			
			result.setValue(ResultConstant.RESULT_EXCEPTION);
			result.setMessage("系统出异常！");
		}
		
		return res;
	}
	
	/**
	 * 查询多酒店
	 * 
	 * @param req
	 * @return
	 */
	public HdlRes queryMultiHotel(MultiHotelReq req) {
		
		HdlRes res = new HdlRes();
		MGExResult result = new MGExResult();
		res.setResult(result);
		
		int channelType = req.getChannelType();
		String channelCityCode = req.getCityId();		
		
		try {
			if(ChannelType.CHANNEL_ZHX == channelType) { // 中航信

				OTRequestHARQ reqZhx = new OTRequestHARQ();
				
				HotelAvailRQ availRq = new HotelAvailRQ();
				reqZhx.setHotelAvailRQ(availRq);
				
				HotelAvailCriteria availCriteria = new HotelAvailCriteria();
				availRq.setHotelAvailCriteria(availCriteria);
				availCriteria.setRoomTypeDetailShowed(req.getRoomTypeDetailShowed());
				
				HotelSearchCriteria searchCriteria = new HotelSearchCriteria();
				availCriteria.setHotelSearchCriteria(searchCriteria);
				
				HotelRef hotelRef = new HotelRef();
				searchCriteria.setHotelRef(hotelRef);
				hotelRef.setCityCode(channelCityCode); // 暂时用合作方的城市代码
				
				StayDateRange dateRange = new StayDateRange();
				dateRange.setCheckInDate(req.getCheckInDate()); // 入住日期
				dateRange.setCheckOutDate(req.getCheckOutDate()); // 离店日期
				availCriteria.setStayDateRange(dateRange);
				
				OTResponseHARS resZhx = zhxService.querySingleAvail(reqZhx);

				OTResponseHARS.HotelAvailRS availRs = resZhx.getHotelAvailRS();
				if(null == availRs) {
					result.setValue(ResultConstant.RESULT_MANGO_ERROR);
					result.setMessage("返回信息为空！");
					return res;
				}
				
				OTResponseHARS.HotelAvailRS.Errors errors = availRs.getErrors();				
				if(null != errors) { // 如果zhx有业务错误返回 TODO:zhx的系统级错误
					result.setValue(ResultConstant.RESULT_CHANNEL_ERROR);					
					OTResponseHARS.HotelAvailRS.Errors.Error error = errors.getError();
					result.setMessage(ZhxErrorMsg.MsgMap.get(error.getErrorCode()));
					return res;
				}
				
				// 正常返回结果
				result.setValue(ResultConstant.RESULT_SUCCESS);
				res.setObj(resZhx);
				
			} else {
				result.setValue(ResultConstant.RESULT_MANGO_ERROR);
				result.setMessage("渠道号错误");
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			log.error(e.getMessage(),e);
			
			result.setValue(ResultConstant.RESULT_EXCEPTION);
			result.setMessage("系统出异常！");
		}
		
		return res;
	}
	
	/**
	 * 调用酒店级别的试预订接口
	 * 
	 * @param req
	 * @return
	 */
	public CheckHotelReservateExResponse checkHotelReservate(CheckHotelReservateExRequest checkHotelRequest)
			throws Exception {
		CheckHotelReservateExResponse checkHotelReservate = mangoHotelService.checkHotelReservateEx(checkHotelRequest); 
		return checkHotelReservate;
	}
	
	public QueryPriceTypeResponse queryElPriceType(String hotelId,
			String roomTypeId) throws Exception {
		QueryPriceTypeResponse queryPriceTypeResponse = null;
		if(StringUtil.isValidStr(hotelId) && StringUtil.isValidStr(roomTypeId) ) {
			QueryPriceTypeRequest priceTypeRequest = new QueryPriceTypeRequest();
			priceTypeRequest.setHotelId(hotelId);
			priceTypeRequest.setRoomTypeId(roomTypeId);
			queryPriceTypeResponse = mangoHotelService.queryElPriceType(priceTypeRequest);
		}else {
			queryPriceTypeResponse = new QueryPriceTypeResponse();
			queryPriceTypeResponse.setResult(0);
			queryPriceTypeResponse.setMessage("参数有误");
		}
		return queryPriceTypeResponse;
	}

	public PriceCompareResponse priceCompare(String hotelIds, Date checkInDate,
			Date checkOutDate) throws Exception {
		PriceCompareResponse compareResponse = null;
		if(StringUtil.isValidStr(hotelIds) && checkInDate != null && null != checkOutDate) {
			PriceCompareRequest priceCompareRequest = new PriceCompareRequest();
			priceCompareRequest.setHotelIds(hotelIds);
			priceCompareRequest.setCheckInDate(checkInDate);
			priceCompareRequest.setCheckOutDate(checkOutDate);
			compareResponse = mangoHotelService.priceCompare(priceCompareRequest);
		}else {
			compareResponse = new PriceCompareResponse();
			compareResponse.setResult(0);
			compareResponse.setMessage("参数有误");
		}
		return compareResponse;
	}
	

	public void setZhxService(IZhxService zhxService) {
		this.zhxService = zhxService;
	}

	public MangoHotelService getMangoHotelService() {
		return mangoHotelService;
	}

	public void setMangoHotelService(MangoHotelService mangoHotelService) {
		this.mangoHotelService = mangoHotelService;
	}

	public void setExService(IExMappingService exService) {
		this.exService = exService;
	}
}

package com.mangocity.hdl.service;

import java.util.Date;

import zhx.vo.HdlRes;
import zhx.vo.SingleHotelReq;

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
import com.mangocity.hdl.hotel.dto.ModifyExRoomOrderRequest;
import com.mangocity.hdl.hotel.dto.ModifyExRoomOrderResponse;
import com.mangocity.hdl.hotel.dto.PriceCompareResponse;
import com.mangocity.hdl.hotel.dto.QueryHotelInfoExRequest;
import com.mangocity.hdl.hotel.dto.QueryHotelInfoExResponse;
import com.mangocity.hdl.hotel.dto.QueryPriceTypeResponse;
import com.mangocity.hdl.hotel.dto.QueryRoomTypeInfoExRequest;
import com.mangocity.hdl.hotel.dto.QueryRoomTypeInfoExResponse;
import com.mangocity.hdl.hotel.dto.SendTestInfoRequest;
import com.mangocity.hdl.hotel.dto.SendTestInfoResponse;
import com.mangocity.hdl.vo.MultiHotelReq;

/**
 * 调用HDL提供的WebService接口
 * 
 * @author chenkeming
 * 
 */
public interface IHDLService {

	/**
	 * 测试接口
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public SendTestInfoResponse sendTestInfo(SendTestInfoRequest req)
			throws Exception;

	/**
	 * 新增订单
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public AddExRoomOrderResponse addExRoomOrder(AddExRoomOrderRequest req)
			throws Exception;

	/**
	 * 修改订单
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public ModifyExRoomOrderResponse modifyExRoomOrder(
			ModifyExRoomOrderRequest req) throws Exception;

	/**
	 * 取消订单
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public CancelExRoomOrderResponse cancelExRoomOrder(
			CancelExRoomOrderRequest req) throws Exception;

	/**
	 * 调用日审接口
	 */
	public DailyAuditExResponse dailyAudit(DailyAuditExRequest req)
			throws Exception;

	/**
	 * 调用查询酒店基本信息接口
	 */
	public QueryHotelInfoExResponse queryHotelInfo(QueryHotelInfoExRequest req)
			throws Exception;

	/**
	 * 调用查询酒店房型信息接口
	 */
	public QueryRoomTypeInfoExResponse queryRoomTypeInfo(
			QueryRoomTypeInfoExRequest req) throws Exception;

	/**
	 * 调用试预订接口
	 * 
	 * @param req
	 * @return
	 */
	public CheckReservateExResponse checkReservate(CheckReservateExRequest req)
			throws Exception;

	/**
	 * 取得所有酒店编码和名称
	 * @param mgExHotelListRequest
	 * @return
	 */
	public MGExHotelListResponse getHotelList (
			MGExHotelListRequest mgExHotelListRequest) throws Exception;

	/**
	 * 取得所有酒店房型
	 * @param mgExHotelRoomListRequest
	 * @return
	 */
	public MGExHotelRoomListResponse getHotelRoomList(
			MGExHotelRoomListRequest mgExHotelRoomListRequest) throws Exception;

	/**
	 * 查询单酒店
	 * 
	 * @param req
	 * @return
	 */
	public HdlRes querySingleHotel(SingleHotelReq req);
	
	/**
	 * 查询多酒店
	 * 
	 * @param req
	 * @return
	 */
	public HdlRes queryMultiHotel(MultiHotelReq req);
	
	/**
	 * 调用酒店级别的试预订接口
	 * 
	 * @param req
	 * @return
	 */
	public CheckHotelReservateExResponse checkHotelReservate(CheckHotelReservateExRequest checkHotelRequest)
			throws Exception;
	
	 /**
	  * 查询艺龙价格类型
	 * @param hotelId
	 * @param roomTypeId
	 * @return
	 */
	public QueryPriceTypeResponse queryElPriceType(String hotelId, String roomTypeId) throws Exception ;
	
	/**
	 * 价格对比
	 * @param hotelIds
	 * @param checkInDate
	 * @param checkOutDate
	 * @return
	 * @throws Exception
	 */
	public PriceCompareResponse priceCompare(String hotelIds,Date checkInDate,Date checkOutDate) throws Exception ;

}

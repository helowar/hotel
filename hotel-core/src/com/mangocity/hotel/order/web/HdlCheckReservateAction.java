package com.mangocity.hotel.order.web;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.mangocity.hdl.constant.CheckResType;
import com.mangocity.hdl.hotel.dto.CheckHotelReservateExRequest;
import com.mangocity.hdl.hotel.dto.CheckHotelReservateExResponse;
import com.mangocity.hdl.hotel.dto.CheckReservateExRequest;
import com.mangocity.hdl.hotel.dto.CheckReservateExResponse;
import com.mangocity.hdl.hotel.dto.MGExReservItem;
import com.mangocity.hdl.service.HDLWebServiceException;
import com.mangocity.hdl.service.IHDLService;
import com.mangocity.util.log.MyLog;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * 用于Ajax调用
 * 
 * @author chenkeming
 * 
 */
public class HdlCheckReservateAction extends ActionSupport implements ServletRequestAware {
	
	private static final MyLog log = MyLog.getLogger(HdlCheckReservateAction.class);
	
	 protected HttpServletRequest request;
	
	   private IHDLService hdlCheckReservate;

		/***********************************************************************************************
	     * 直联酒店的试预定方法 create guojun
	     */
	    public CheckReservateExResponse checkReservateFromQuery(String channelType, Long hotelId,
	        Long roomTypeId, String childRoomTypeId, String checkInDate, String checkOutDate,
	        String quantity, List<MGExReservItem> item, String htmlRow) {
	        CheckReservateExRequest checkReservateExRequest = new CheckReservateExRequest();
	        checkReservateExRequest.setChainCode(null);

	        // modify by shizhongwen begin 2009-02-18 不能返回为null 必须返回失败的行数,标识，失败的原因，
	        CheckReservateExResponse checkReservateExResponse = new CheckReservateExResponse();

	        // 如果如果行数为空,则返回错误码原因. modify by shizhongwen 2009-05-21
	        if (null == htmlRow || "".equals(htmlRow)) {
	            checkReservateExResponse = new CheckReservateExResponse();
	            checkReservateExResponse.setHtmlRow(htmlRow);
	            checkReservateExResponse.setResult(CheckResType.REJECT);
	            checkReservateExResponse.setReason("htmlRow 渠道类型为空");
	            return checkReservateExResponse;
	        }

	        // 如果渠道类型为空,则返回.
	        if (null == channelType || "".equals(channelType)) {
	            checkReservateExResponse = new CheckReservateExResponse();
	            checkReservateExResponse.setHtmlRow(htmlRow);
	            checkReservateExResponse.setResult(CheckResType.REJECT);
	            checkReservateExResponse.setReason("channelType 渠道类型为空");
	            return checkReservateExResponse;
	        } else {
	            checkReservateExRequest.setChannelType(Long.valueOf(channelType).intValue());
	        }
	        // 如果试预定开始时间为空,则返回.
	        if (null == checkInDate || "".equals(checkInDate)) {
	            checkReservateExResponse = new CheckReservateExResponse();
	            checkReservateExResponse.setHtmlRow(htmlRow);
	            checkReservateExResponse.setResult(CheckResType.REJECT);
	            checkReservateExResponse.setReason("checkInDate 试预定开始时间为空");
	            return checkReservateExResponse;
	        } else {
	            checkReservateExRequest.setCheckInDate(checkInDate);
	        }
	        // 如果试预定结束时间为空,则返回空.
	        if (null == checkOutDate || "".equals(checkOutDate)) {
	            checkReservateExResponse = new CheckReservateExResponse();
	            checkReservateExResponse.setHtmlRow(htmlRow);
	            checkReservateExResponse.setResult(CheckResType.REJECT);
	            checkReservateExResponse.setReason("checkOutDate 试预定结束时间为空");
	            return checkReservateExResponse;
	        } else {
	            checkReservateExRequest.setCheckOutDate(checkOutDate);
	        }
	        // modify by shizhongwen end 2009-02-18

	        checkReservateExRequest.setChildRoomTypeId(Long.valueOf(childRoomTypeId).intValue());
	        checkReservateExRequest.setHotelId(Long.valueOf(hotelId));
	        // 如果房间数量为空,则默认为1.
	        if (null == quantity || "".equals(quantity)) {
	            checkReservateExRequest.setQuantity(1);
	        } else {
	            checkReservateExRequest.setQuantity(Long.valueOf(quantity).intValue());
	        }
	        checkReservateExRequest.setRoomTypeId(roomTypeId);
	        for (Iterator itr = item.iterator(); itr.hasNext();) {
	            MGExReservItem mgRsItem = (MGExReservItem) itr.next();
	            MGExReservItem mgExReservItem = new MGExReservItem();
	            mgExReservItem.setBasePrice(mgRsItem.getBasePrice());
	            mgExReservItem.setChange(mgRsItem.getChange());
	            mgExReservItem.setDayIndex(mgRsItem.getDayIndex());
	            mgExReservItem.setFirstDayPrice(mgRsItem.getFirstDayPrice());
	            mgExReservItem.setSalePrice(mgRsItem.getSalePrice());
	            checkReservateExRequest.getReservItems().add(mgExReservItem);
	        }
	        log.info("================= hdlService.checkReservate beging.....................");
	        
	        try {
	        	checkReservateExResponse = hdlCheckReservate.checkReservate(checkReservateExRequest);
	        } catch (Exception exception) {
	            log.error("=========HdlCheckReservateAction.CheckReservateExResponse()==hdlService.checkReservate exception: ",exception);
	            throw new  HDLWebServiceException(htmlRow,exception);
//	            checkReservateExResponse = new CheckReservateExResponse();
//	            checkReservateExResponse.setHtmlRow(htmlRow);
//	            checkReservateExResponse.setResult(CheckResType.REJECT);
//	            checkReservateExResponse.setReason("调用合作方的数据出错!!!");
//	            return checkReservateExResponse;
	        }
	        log.info("================= hdlService.checkReservate end.....................");
	        
	        if (null == checkReservateExResponse) {
	            // add by shizhongwen 2009-02-18 必须返回失败的行数,标识，失败的原因，
	            checkReservateExResponse = new CheckReservateExResponse();
	            checkReservateExResponse.setHtmlRow(htmlRow);
	            checkReservateExResponse.setResult(CheckResType.REJECT);
	            checkReservateExResponse.setReason("调用合作方接口后取得的数据为空!!!");
	            return checkReservateExResponse;
	        } else {
	            if (null == htmlRow || "".equals(htmlRow)) {
	                // ajax默认刷新页面上的行是1
	                checkReservateExResponse.setHtmlRow("1");
	            } else {
	                checkReservateExResponse.setHtmlRow(htmlRow);
	            }
	        }
	        log.info(" directionLink childRoomTypeId: "
	            + checkReservateExRequest.getChildRoomTypeId() + " responseResult: "
	            + checkReservateExResponse.getResult());
	        return checkReservateExResponse;
	    }
	    
	    
	    /***********************************************************************************************
	     * 直联酒店的试预定方法 create guojun
	     */
	    public CheckHotelReservateExResponse checkHotelReservateFromQuery(Long hotelId,String channelType,String checkInDate, 
	    		String checkOutDate, String roomTypes,String childRoomTypes,String quantity) {
	    	
	    	CheckHotelReservateExRequest checkHotelRequest = new CheckHotelReservateExRequest();
	    	checkHotelRequest.setChainCode(null);
	    	
	    	CheckHotelReservateExResponse checkHotelResponse = new CheckHotelReservateExResponse();

	        // 如果如果行数为空,则返回错误码原因. modify by shizhongwen 2009-05-21
	        if (null == hotelId || "".equals(hotelId)) {
	        	checkHotelResponse.getResult().setValue(CheckResType.HOTEL_REJECT);
	        	checkHotelResponse.getResult().setMessage("hotelid 酒店id为空！");
	            return checkHotelResponse;
	        }else{
	        	checkHotelRequest.setHotelId(Long.valueOf(hotelId).intValue());
	        }

	        // 如果渠道类型为空,则返回.
	        if (null == channelType || "".equals(channelType)) {
	        	checkHotelResponse.getResult().setValue(CheckResType.HOTEL_REJECT);
	        	checkHotelResponse.getResult().setMessage("channelType 渠道类型为空！");
	            return checkHotelResponse;
	        } else {
	        	checkHotelRequest.setChannelType(Long.valueOf(channelType).intValue());
	        }
	        // 如果试预定开始时间为空,则返回.
	        if (null == checkInDate || "".equals(checkInDate)) {
	        	checkHotelResponse.getResult().setValue(CheckResType.HOTEL_REJECT);
	        	checkHotelResponse.getResult().setMessage("checkInDate 试预定开始时间为空！");
	            return checkHotelResponse;
	        } else {
	        	checkHotelRequest.setCheckInDate(checkInDate);
	        }
	        // 如果试预定结束时间为空,则返回空.
	        if (null == checkOutDate || "".equals(checkOutDate)) {
	        	checkHotelResponse.getResult().setValue(CheckResType.HOTEL_REJECT);
	        	checkHotelResponse.getResult().setMessage("checkOutDate 试预定结束时间为空！");
	            return checkHotelResponse;
	        } else {
	        	checkHotelRequest.setCheckOutDate(checkOutDate);
	        }
	        if (null == roomTypes || "".equals(roomTypes)) {
	        	checkHotelResponse.getResult().setValue(CheckResType.HOTEL_REJECT);
	        	checkHotelResponse.getResult().setMessage("roomTypes 酒店房型数据为空！");
	            return checkHotelResponse;
	        } else {
	        	checkHotelRequest.setRoomTypes(roomTypes);
	        }
	        if (null == childRoomTypes || "".equals(childRoomTypes)) {
	        	checkHotelResponse.getResult().setValue(CheckResType.HOTEL_REJECT);
	        	checkHotelResponse.getResult().setMessage("childRoomTypes 酒店价格类型数据为空！");
	            return checkHotelResponse;
	        } else {
	        	checkHotelRequest.setChildRoomTypes(childRoomTypes);
	        }
	        // 如果房间数量为空,则默认为1.
	        if (null == quantity || "".equals(quantity)) {
	        	checkHotelRequest.setQuantity(1);
	        } else {
	        	checkHotelRequest.setQuantity(Long.valueOf(quantity).intValue());
	        }
	        log.info("================= hdlService.checkHotelReservateFromQuery beging.....................");
	        
	        try {
	        	checkHotelResponse = hdlCheckReservate.checkHotelReservate(checkHotelRequest);
	        } catch (Exception exception) {
	            log.error("=========HdlCheckReservateAction.checkHotelReservateFromQuery()==hdlService.checkHotelReservateFromQuery exception: ",exception);
	            throw new  HDLWebServiceException(String.valueOf(hotelId),exception);
//	            checkReservateExResponse = new CheckReservateExResponse();
//	            checkReservateExResponse.setHtmlRow(htmlRow);
//	            checkReservateExResponse.setResult(CheckResType.REJECT);
//	            checkReservateExResponse.setReason("调用合作方的数据出错!!!");
//	            return checkReservateExResponse;
	        }
	        log.info("================= hdlService.checkReservate end.....................");
	        
	        if (null == checkHotelResponse) {
	        	checkHotelResponse.getResult().setValue(CheckResType.HOTEL_REJECT);
	        	checkHotelResponse.getResult().setMessage("调用合作方接口后取得的数据为空!!!");
	            return checkHotelResponse;
	        }
	        log.info(" directionLink hotelId: "+String.valueOf(hotelId));
	        return checkHotelResponse;
	    }
	    
	    public IHDLService getHdlCheckReservate() {
			return hdlCheckReservate;
		}

		public void setHdlCheckReservate(IHDLService hdlCheckReservate) {
			this.hdlCheckReservate = hdlCheckReservate;
		}

		public void setServletRequest(HttpServletRequest request) {
	        this.request = request;

	    }

}
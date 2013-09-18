package com.mangocity.hdl.service.impl;

import java.util.Iterator;
import java.util.List;

import com.mangocity.hdl.constant.CheckResType;
import com.mangocity.hdl.hotel.dto.CheckReservateExRequest;
import com.mangocity.hdl.hotel.dto.CheckReservateExResponse;
import com.mangocity.hdl.hotel.dto.MGExReservItem;
import com.mangocity.hdl.service.IHDLService;
import com.mangocity.util.log.MyLog;

/***************************************************************************************************
 * 
 * 直联酒店的试预定ajax刷新 create by guojun
 */
public class HdlCheckReservateManager {
	private static final MyLog log = MyLog.getLogger(HdlCheckReservateManager.class);

    private IHDLService hdlService;

    /***********************************************************************************************
     * 直联酒店的试预定方法 create guojun
     */
    public CheckReservateExResponse checkReservateFromQuery(String channelType, Long hotelId,
        Long roomTypeId, String childRoomTypeId, String checkInDate, String checkOutDate,
        String quantity, List<MGExReservItem> item, String htmlRow) {
        CheckReservateExRequest checkReservateExRequest = new CheckReservateExRequest();
        checkReservateExRequest.setChainCode(null);

        // modify by shizhongwen begin 2009-02-18 不能返回为null 必须返回失败的行数,标识，失败的原因，
        CheckReservateExResponse checkReservateExResponse;

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
        try {
            checkReservateExResponse = hdlService.checkReservate(checkReservateExRequest);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // add by shizhongwen 2009-02-18 增加日志,必须返回失败的行数,标识，失败的原因，
            log.error(e);
            checkReservateExResponse = new CheckReservateExResponse();
            checkReservateExResponse.setHtmlRow(htmlRow);
            checkReservateExResponse.setResult(CheckResType.REJECT);
            checkReservateExResponse.setReason("调用合作方的数据出错!!!");
            return checkReservateExResponse;
        }
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

    public IHDLService getHdlService() {
        return hdlService;
    }

    public void setHdlService(IHDLService hdlService) {
        this.hdlService = hdlService;
    }
}

package com.mangocity.hotel.order.web;

import hk.com.cts.ctcp.hotel.service.HKService;
import hk.com.cts.ctcp.hotel.webservice.response.HKRoomAmtResponse;
import hk.com.cts.ctcp.hotel.webservice.response.HKRoomQtyResponse;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.util.DateUtil;

/**
 * 用于中旅试预订Ajax调用
 * 
 * @author chenkeming Mar 16, 2009 5:56:03 PM
 */
public class CtsDwrAction {

    private HKService hkService;

    /**
     * 查询配额数量
     * 
     * @author chenkeming Mar 16, 2009 3:29:35 PM
     * @param hotelId
     * @param sDateFm
     * @param sDateTo
     * @param roomTypes
     * @return
     */
    public List<HKRoomQtyResponse> enqRoomQty(Long hotelId, String sDateFm, 
    		String sDateTo, String roomTypes) {
        List<HKRoomQtyResponse> li = hkService.enqRoomQty(hotelId, DateUtil.getDate(sDateFm),
            DateUtil.getDate(DateUtil.getDate(sDateTo), -1), roomTypes);
        if (null == li || li.isEmpty()) {
            HKRoomQtyResponse res = new HKRoomQtyResponse();
            res.setHotelId(hotelId);
            res.setRet(-1);
            li = new ArrayList<HKRoomQtyResponse>();
            li.add(res);
        }
        return li;
    }

    /**
     * 查询价格
     * 
     * @author chenkeming Mar 16, 2009 3:31:30 PM
     * @param hotelId
     * @param sDateFm
     * @param sDateTo
     * @return
     */
    public List<HKRoomAmtResponse> enqRoomNationAmt(Long hotelId, String sDateFm, String sDateTo, 
    		String roomTypes, String childRoomTypes) {
        List<HKRoomAmtResponse> li = hkService.enqRoomNationAmt(hotelId, DateUtil.getDate(sDateFm),
            DateUtil.getDate(DateUtil.getDate(sDateTo), -1), roomTypes, childRoomTypes);
        if (null == li || li.isEmpty()) {
            HKRoomAmtResponse res = new HKRoomAmtResponse();
            res.setHotelId(hotelId);
            res.setRet(-1);
            li = new ArrayList<HKRoomAmtResponse>();
            li.add(res);
        }
        return li;
    }

    public HKService getHkService() {
        return hkService;
    }

    public void setHkService(HKService hkService) {
        this.hkService = hkService;
    }

}
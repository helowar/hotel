package com.mangocity.hotel.order.web;

import java.util.Date;

import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.web.webwork.GenericAction;
import com.mangocity.hotel.order.persistence.HtlRoomstateCc;
import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.hotel.order.service.IHotelRoomStateCC;

/**
 */
public class HotelRoomStateCCAction extends GenericAction {
    /**
	 * 
	 */
    private static final long serialVersionUID = 4654216543241654L;

    private Long roomStateCCID;

    private HtlRoomstateCc htlRoomstateCc = new HtlRoomstateCc();

    private IHotelRoomStateCC hotelRoomStateCC;

    private String hotelChnName;

    private HtlHotel htlHotel;

    private IHotelService hotelService;

    public String queryRoomStateCclog() {
        roomStateCCID = Long.parseLong(request.getParameter("roomStateCCID"));
        htlRoomstateCc = hotelRoomStateCC.findRoomFulLog(roomStateCCID);
        htlHotel = hotelService.findHotel(htlRoomstateCc.getHotelid());
        return "log";

    }

    public String updateRoomStateCclog() {
        if (null != super.getOnlineRoleUser()) {
            htlRoomstateCc.setReviewid(super.getOnlineRoleUser().getLoginName());
            htlRoomstateCc.setReviewname(super.getOnlineRoleUser().getName());
            if (super.getOnlineRoleUser().getLoginName().equals("")
                || super.getOnlineRoleUser().getName().equals("")) {
                htlRoomstateCc.setReviewid(super.getBackLoginName());
                htlRoomstateCc.setReviewname(super.getBackUserName());
            }
        } else {
            htlRoomstateCc.setReviewid(super.getBackLoginName());
            htlRoomstateCc.setReviewname(super.getBackUserName());
        }
        htlRoomstateCc.setReviewdept("本部");
        htlRoomstateCc.setProcessdate(new Date());
        htlRoomstateCc.setReviewstate("1");
        hotelRoomStateCC.updateRoomFulLog(htlRoomstateCc);

        return "save";

    }

    public HtlRoomstateCc getHtlRoomstateCc() {
        return htlRoomstateCc;
    }

    public void setHtlRoomstateCc(HtlRoomstateCc htlRoomstateCc) {
        this.htlRoomstateCc = htlRoomstateCc;
    }

    public Long getRoomStateCCID() {
        return roomStateCCID;
    }

    public void setRoomStateCCID(Long roomStateCCID) {
        this.roomStateCCID = roomStateCCID;
    }

    public IHotelRoomStateCC getHotelRoomStateCC() {
        return hotelRoomStateCC;
    }

    public void setHotelRoomStateCC(IHotelRoomStateCC hotelRoomStateCC) {
        this.hotelRoomStateCC = hotelRoomStateCC;
    }

    public String getHotelChnName() {
        return hotelChnName;
    }

    public void setHotelChnName(String hotelChnName) {
        this.hotelChnName = hotelChnName;
    }

    public HtlHotel getHtlHotel() {
        return htlHotel;
    }

    public void setHtlHotel(HtlHotel htlHotel) {
        this.htlHotel = htlHotel;
    }

    public void setHotelService(IHotelService hotelService) {
        this.hotelService = hotelService;
    }

}

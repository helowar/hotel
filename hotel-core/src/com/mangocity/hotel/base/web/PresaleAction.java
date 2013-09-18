package com.mangocity.hotel.base.web;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.base.persistence.HtlPresale;
import com.mangocity.hotel.base.persistence.HtlPresaleHotel;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;

/**
 */
public class PresaleAction extends PersistenceAction {

    private Long presaleID;

    private Long[] hotelID;

    // private long hotelId;

    private static final String BATCH_SELECT = "batchSelect";

    // private HotelManage hotelManage;

    /**
     * 查询待促销的酒店调转名称
     */
    private static final String LISTHOTEL = "listHotel";

    /**
     * 查询已关联促销信息的酒店调转名称
     */
    private static final String DELETEPRESALEDHOTEL = "deletePresaledHotel";

    protected Class getEntityClass() {
        return HtlPresale.class;
    }

    /**
     * 查询待促销的酒店
     * 
     * @return
     */
    public String listHotel() {

        return LISTHOTEL;
    }

    /**
     * 删除酒店与促销信息的关联
     * 
     * @return
     */
    public String deletePresaledHotel() {

        return DELETEPRESALEDHOTEL;
    }

    public String batchSelect() {

        List hotels = new ArrayList();
        // 循环赋值
        log.info("presaleID=====" + presaleID);
        log.info("hotelID.length=====" + hotelID.length);
        for (int i = 0; i < hotelID.length; i++) {
            HtlPresaleHotel hotel = new HtlPresaleHotel();
            hotel.setPresaleId(presaleID);
            hotel.setHotelId(hotelID[i]);
            if (null != super.getOnlineRoleUser()) {
                hotel.setCreate_by(super.getOnlineRoleUser().getName());
                hotel.setCreate_by_id(super.getOnlineRoleUser().getLoginName());
            }
            hotels.add(hotel);
        }
        log.info("presaleID=====" + presaleID);
        super.getEntityManager().saveOrUpdateAll(hotels);

        return BATCH_SELECT;
    }

    public Long[] getHotelID() {
        return hotelID;
    }

    public void setHotelID(Long[] hotelID) {
        this.hotelID = hotelID;
    }

    public Long getPresaleID() {
        return presaleID;
    }

    public void setPresaleID(Long presaleID) {
        this.presaleID = presaleID;
    }

}

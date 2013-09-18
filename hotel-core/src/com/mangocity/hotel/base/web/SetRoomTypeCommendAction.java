package com.mangocity.hotel.base.web;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.base.util.BizRuleCheck;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.MyBeanUtil;

/**
 */
public class SetRoomTypeCommendAction extends PersistenceAction {
    private Long hotelId;

    private int roomTypeNum;

    private String BATCH_SAVE = "batchSave";

    private HotelManage hotelManage;
    
    private HotelRoomTypeService hotelRoomTypeService;

    private List lstRoomType = new ArrayList();

    protected Class getEntityClass() {
        return HtlRoomtype.class;
    }

    public String batchSave() {
        List ls = MyBeanUtil.getBatchObjectFromParam(super.getParams(), HtlRoomtype.class,
            roomTypeNum);

        for (int k = 0; k < ls.size(); k++) {
            HtlRoomtype roomtype = (HtlRoomtype) ls.get(k);

            log.info("roomtypeCommendLevel========" + roomtype.getCommendLevel());
            HtlRoomtype orignRoomtype = (HtlRoomtype) super.getEntityManager().find(
                HtlRoomtype.class, roomtype.getID());

            orignRoomtype.setActive(BizRuleCheck.getTrueString());
            if (null != super.getOnlineRoleUser())
                orignRoomtype.setCreateBy(super.getOnlineRoleUser().getName());
            orignRoomtype.setCreateTime(DateUtil.getSystemDate());

            orignRoomtype.setCommendLevel(roomtype.getCommendLevel());
            // 保存数据库
            super.getEntityManager().saveOrUpdate(orignRoomtype);

        }
        // super.getEntityManager().saveOrUpdateAll(ls);
        return BATCH_SAVE;

    }

    public String view() {
        // 根据酒店ID，获取酒店的所有房型

        hotelManage.findHotel(hotelId);
        lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId);

        return "view";
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public int getRoomTypeNum() {
        return roomTypeNum;
    }

    public void setRoomTypeNum(int roomTypeNum) {
        this.roomTypeNum = roomTypeNum;
    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

    public List getLstRoomType() {
        return lstRoomType;
    }

    public void setLstRoomType(List lstRoomType) {
        this.lstRoomType = lstRoomType;
    }

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}
}

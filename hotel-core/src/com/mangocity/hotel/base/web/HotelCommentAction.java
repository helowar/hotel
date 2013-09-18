package com.mangocity.hotel.base.web;

import java.util.Date;

import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlUsersComment;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;

/**
 */
public class HotelCommentAction extends PersistenceAction {

    private static final long serialVersionUID = 4667737557659676898L;

    private Long entityID;

    private String passradio;

    private String hotelName;

    private long hotelID;

    private HotelManage hotelManage;

    public String view() {
        super.setEntityID(entityID);
        super.setEntity(super.populateEntity());
        if (null == super.getEntity()) {
            String error = "找不到实体对象,id:" + entityID + "; 类：" + getEntityClass();
            return super.forwardError(error);
        }
        super.setEntityForm(super.populateFormBean(super.getEntity()));
        super.getEntityForm();
        hotelName = (hotelManage.findHotel(hotelID)).getChnName();
        return VIEW;
    }

    public String save() {
        super.setEntityID(entityID);
        super.setEntity(super.populateEntity());
        HtlUsersComment comment = (HtlUsersComment) super.getEntity();
        comment.setCommentStatus(passradio);
        comment.setVerifyDate(new Date());

        if (null != super.getOnlineRoleUser()) {
            comment.setModify_by(super.getOnlineRoleUser().getName());
            comment.setModify_by_id(super.getOnlineRoleUser().getLoginName());
        }
        comment.setModify_time(new Date());

        super.getEntityManager().saveOrUpdate(super.getEntity());
        return SAVE_SUCCESS;
    }

    public String getPassradio() {
        return passradio;
    }

    public void setPassradio(String passradio) {
        this.passradio = passradio;
    }

    public Long getEntityID() {
        return entityID;
    }

    public void setEntityID(Long entityID) {
        this.entityID = entityID;
    }

    protected Class getEntityClass() {
        return HtlUsersComment.class;
    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

    public long getHotelID() {
        return hotelID;
    }

    public void setHotelID(long hotelID) {
        this.hotelID = hotelID;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

}

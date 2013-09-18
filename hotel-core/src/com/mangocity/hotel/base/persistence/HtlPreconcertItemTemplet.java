package com.mangocity.hotel.base.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.util.Entity;

/**
 */
public class HtlPreconcertItemTemplet extends CEntity implements Entity {

    // 酒店预订担保预付条款模板ID

    private Long ID;

    // 酒店ID;
    private Long hotelID;

    // 预订条款模板名称
    private String reservationName;

    // 创建人工号
    private String createBy;

    // 创建人名称
    private String createByID;

    // 创建时间
    private Date createTime;

    // 修改人工号
    private String modifyBy;

    // 修改人名称
    private String modifyByID;

    // 修改时间
    private Date modifyTime;

    // 删除人工号
    private String delBy;

    // 删除人名称
    private String delByID;

    // 删除时间
    private Date delTime;

    // 有效标志
    private String Active;

    private List<HtlAssureTemplate> htlAssureTemplateZ;

    private List<HtlPrepayTemplate> htlPrepayTemplateZ;
    
    private List<HtlReservationTemplate> htlReservationTemplateZ;


    public void setHtlAssureTemplateZ(List<HtlAssureTemplate> htlAssureTemplateZ) {
        this.htlAssureTemplateZ = htlAssureTemplateZ;
    }

    public void setHtlPrepayTemplateZ(List<HtlPrepayTemplate> htlPrepayTemplateZ) {
        this.htlPrepayTemplateZ = htlPrepayTemplateZ;
    }

    public String getActive() {
        return Active;
    }

    public void setActive(String active) {
        Active = active;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateByID() {
        return createByID;
    }

    public void setCreateByID(String createByID) {
        this.createByID = createByID;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDelBy() {
        return delBy;
    }

    public void setDelBy(String delBy) {
        this.delBy = delBy;
    }

    public String getDelByID() {
        return delByID;
    }

    public void setDelByID(String delByID) {
        this.delByID = delByID;
    }

    public Date getDelTime() {
        return delTime;
    }

    public void setDelTime(Date delTime) {
        this.delTime = delTime;
    }

    public Long getHotelID() {
        return hotelID;
    }

    public void setHotelID(Long hotelID) {
        this.hotelID = hotelID;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public String getModifyByID() {
        return modifyByID;
    }

    public void setModifyByID(String modifyByID) {
        this.modifyByID = modifyByID;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getReservationName() {
        return reservationName;
    }

    public void setReservationName(String reservationName) {
        this.reservationName = reservationName;
    }

    public List<HtlAssureTemplate> getHtlAssureTemplateZ() {
        return htlAssureTemplateZ;
    }

    public List<HtlPrepayTemplate> getHtlPrepayTemplateZ() {
        return htlPrepayTemplateZ;
    }

    public List<HtlReservationTemplate> getHtlReservationTemplateZ() {
		return htlReservationTemplateZ;
	}

	public void setHtlReservationTemplateZ(
			List<HtlReservationTemplate> htlReservationTemplateZ) {
		this.htlReservationTemplateZ = htlReservationTemplateZ;
	}

	public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

}

package com.mangocity.hotel.order.persistence;

import com.mangocity.util.Entity;

/**
 * 
 * 
 * 备注信息
 * 
 * @author chenkeming 
 */
public class OrRemark implements Entity {

    private static final long serialVersionUID = -7963787959193213997L;

    /**
	 * ID <pk>
	 */
    private Long ID; 

    /**
	 * 内部备注
	 */
    private String privateRemark;
    
    /**
	 * 房型备注
	 */
    private String roomRemark;
    
    /**
	 * 联系客人注意事项
	 */
    private String contactRemark;
    
    /**
	 * 给酒店的备注
	 */
    private String hotelRemark;
    
    /**
	 * 给客人的备注
	 */
    private String memberRemark;

    public String getContactRemark() {
        return contactRemark;
    }

    public void setContactRemark(String contactRemark) {
        this.contactRemark = contactRemark;
    }

    public String getHotelRemark() {
        return hotelRemark;
    }

    public void setHotelRemark(String hotelRemark) {
        this.hotelRemark = hotelRemark;
    }

    public String getPrivateRemark() {
        return privateRemark;
    }

    public void setPrivateRemark(String privateRemark) {
        this.privateRemark = privateRemark;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long remarkId) {
        this.ID = remarkId;
    }

    public String getRoomRemark() {
        return roomRemark;
    }

    public void setRoomRemark(String roomRemark) {
        this.roomRemark = roomRemark;
    }

    public String getMemberRemark() {
        return memberRemark;
    }

    public void setMemberRemark(String memberRemark) {
        this.memberRemark = memberRemark;
    }
    
}

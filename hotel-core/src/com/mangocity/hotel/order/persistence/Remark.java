/**
 * 
 *  备注信息
 */

package com.mangocity.hotel.order.persistence;

import com.mangocity.util.Entity;

/**
 */
public class Remark implements Entity {

    // ID <pk>
    private Long ID; 

    // 内部备注
    private String privateRemark;
    
    // 房型备注
    private String roomRemark;
    
    // 联系客人注意事项
    private String contactRemark;
    
    // 给酒店的备注
    private String hotelRemark;

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
    
    
}

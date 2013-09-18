package com.mangocity.hotel.order.persistence;

import com.mangocity.util.HEntity;

/**
 * 
 * 
 * 历史订单备注信息
 * 
 * @author chenkeming 
 */
public class HRemark implements HEntity {

    private static final long serialVersionUID = -5054639344227493796L;

    /**
	 * 主键 <pk>
	 */
    private Long hisID;     

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

    public Long getHisID() {
        return hisID;
    }

    public void setHisID(Long hisID) {
        this.hisID = hisID;
    }

    public String getHotelRemark() {
        return hotelRemark;
    }

    public void setHotelRemark(String hotelRemark) {
        this.hotelRemark = hotelRemark;
    }

    public String getMemberRemark() {
        return memberRemark;
    }

    public void setMemberRemark(String memberRemark) {
        this.memberRemark = memberRemark;
    }

    public String getPrivateRemark() {
        return privateRemark;
    }

    public void setPrivateRemark(String privateRemark) {
        this.privateRemark = privateRemark;
    }

    public String getRoomRemark() {
        return roomRemark;
    }

    public void setRoomRemark(String roomRemark) {
        this.roomRemark = roomRemark;
    }
    
}

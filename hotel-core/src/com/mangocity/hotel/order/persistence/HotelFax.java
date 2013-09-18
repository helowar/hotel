/**
 *  Modified by chenkeming@2007.05.08
 *  
 *  日审酒店列表
 */
package com.mangocity.hotel.order.persistence;

import com.mangocity.util.Entity;

/**
 * 传真日审酒店列表
 * @author zhengxin
 *
 */
public class HotelFax implements Entity {

    // 列表ID
    private Long ID;

    // 酒店ID
    private Long hotelId;

    // 酒店名称
    private String hotelName;

    // 传真号
    private Long hotelFax;

    // 模版号
    private String faxNo;

    public Long getID() {
        return ID;
    }

    public void setID(Long auditHotelId) {
        this.ID = auditHotelId;
    }

    public String getFaxNo() {
        return faxNo;
    }

    public void setFaxNo(String faxNo) {
        this.faxNo = faxNo;
    }

    public Long getHotelFax() {
        return hotelFax;
    }

    public void setHotelFax(Long hotelFax) {
        this.hotelFax = hotelFax;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    
}

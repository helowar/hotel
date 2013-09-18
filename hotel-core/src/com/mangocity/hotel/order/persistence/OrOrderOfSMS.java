package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.hotel.order.constant.OrderType;
import com.mangocity.util.Entity;

/**
 */
public class OrOrderOfSMS implements Entity{
    private static final long serialVersionUID = -9013143629008145160L;
    /**
	 * 主键
	 */
    private Long ID;  // 表中 ORDERID 订单ID
    
    private Date orderDate; //订单的入住日期
    
    private Date sendDate; //短信的发送时间
    
    private String mobile; //手机号	
    
    /**
	 * 订单类型 :(1-mango, 2-114)
	 * 
	 * @see OrderType
	 */
    private int type;
    
    /**
	 * 会员所在的省份（用于114区分不同省份）
	 */
    private String memberState;
    
    private String hotelName;//酒店名称
    
    private String latestArrivalTime;//最晚到店时间

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMemberState() {
        return memberState;
    }

    public void setMemberState(String memberState) {
        this.memberState = memberState;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getLatestArrivalTime() {
        return latestArrivalTime;
    }

    public void setLatestArrivalTime(String latestArrivalTime) {
        this.latestArrivalTime = latestArrivalTime;
    }

    

}

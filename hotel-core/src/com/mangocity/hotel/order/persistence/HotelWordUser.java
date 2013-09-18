package com.mangocity.hotel.order.persistence;
/**
 * 日审
 * 
 * @author chenkeming
 *
 */
public class HotelWordUser{            
    
    private String userID;

    /**
	 * 订单操作数量
	 */
    private int orderNumbers;
    //操作员组别
    private String groups;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    public int getOrderNumbers() {
        return orderNumbers;
    }

    public void setOrderNumbers(int orderNumbers) {
        this.orderNumbers = orderNumbers;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }


    }

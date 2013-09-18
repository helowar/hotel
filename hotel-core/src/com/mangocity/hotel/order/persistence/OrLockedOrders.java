package com.mangocity.hotel.order.persistence;

import java.util.Date;


/**
 */
public class OrLockedOrders  implements java.io.Serializable {

    private static final long serialVersionUID = 1831139009435999322L;

    private Long orderId;
    
    private String orderCD;
    
    private Integer type;
    
    private String locker;
    
    /**
	 * 是否前台锁定
	 */
    private boolean frontLock;
    
    /**
	 * 被锁时间
	 */
    private Date lockTime;

    public String getLocker() {
        return locker;
    }

    public void setLocker(String locker) {
        this.locker = locker;
    }

    public String getOrderCD() {
        return orderCD;
    }

    public void setOrderCD(String orderCD) {
        this.orderCD = orderCD;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getLockTime() {
        return lockTime;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
    }

    public boolean isFrontLock() {
        return frontLock;
    }

    public void setFrontLock(boolean frontLock) {
        this.frontLock = frontLock;
    }

}

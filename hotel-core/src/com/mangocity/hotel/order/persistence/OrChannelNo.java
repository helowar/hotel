
package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 * 关联中旅订单号
 * @author chenkeming Mar 17, 2009 5:17:08 PM
 */
public class OrChannelNo implements Entity {

    private static final long serialVersionUID = -834933523716052470L;

    /**
	 * ID <pk>
	 */
    private Long ID;

    /**
	 * 订单ID <fk> 和OrOrder关联
	 */
    private OrOrder order;
    
    private String orderChannel = "";    
    
    private int quantity;
    
    private String fellows;    
    
    /**
     * 状态
     * @author chenkeming Mar 27, 2009 10:58:40 AM
     */
//    private int status = TxnStatusType.Default;
    private int status = 0;
    
    /**
     * 中旅单金额
     * @author chenkeming Apr 16, 2009 11:35:47 AM
     */
    private double orderValRmb;
    
    /**
     * 中旅单提交时间
     */
    private Date commitTime;

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public OrOrder getOrder() {
        return order;
    }

    public void setOrder(OrOrder order) {
        this.order = order;
    }

    public String getFellows() {
        return fellows;
    }

    public void setFellows(String fellows) {
        this.fellows = fellows;
    }

    public String getOrderChannel() {
        return orderChannel;
    }

    public void setOrderChannel(String orderChannel) {
        this.orderChannel = orderChannel;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getOrderValRmb() {
        return orderValRmb;
    }

    public void setOrderValRmb(double orderValRmb) {
        this.orderValRmb = orderValRmb;
    }

	public Date getCommitTime() {
		return commitTime;
	}

	public void setCommitTime(Date commitTime) {
		this.commitTime = commitTime;
	}

}

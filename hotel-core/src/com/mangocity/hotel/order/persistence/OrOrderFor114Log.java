
package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.util.Entity;


/**
 * 
 *  给号百发送信息日志类
 *  
 *  @author chenkeming
 */
public class OrOrderFor114Log implements Entity {    

    private static final long serialVersionUID = 8218704703039838997L;
    
    /**
	 * 主键
	 */
    private Long ID;

    /**
	 * 发送开始时刻
	 */
    private Date beginTime;
    
    /**
	 * 发送结束时刻
	 */
    private Date endTime;

    /**
	 * 总共要发送的订单数
	 */
    private int total;
    
    /**
	 * 发送成功的订单数
	 */
    private int sucCount;
        
    /**
	 * 发送错误次数
	 */
    private int failCount;

    /**
	 * 发送异常次数
	 */
    private int exceptCount;

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getExceptCount() {
        return exceptCount;
    }

    public void setExceptCount(int exceptCount) {
        this.exceptCount = exceptCount;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public int getSucCount() {
        return sucCount;
    }

    public void setSucCount(int sucCount) {
        this.sucCount = sucCount;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }
    
                
    
}

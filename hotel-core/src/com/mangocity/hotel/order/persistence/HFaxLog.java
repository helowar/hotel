
package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.util.HEntity;

/**
 * 
 *  历史订单传真日志
 *  
 *  @author chenkeming
 */
public class HFaxLog implements HEntity {

    private static final long serialVersionUID = -4859913490743134705L;

    /**
	 * 主键 <pk>
	 */
    private Long hisID;    

    /**
	 * 类型
	 */
    private String type;
    
    /**
	 * OrOrderFax或者日审传真表 ID<br>
	 * 和OrOrderFax的barCode关联
	 */
    private String barCode;
    
    /**
	 * url
	 */
    private String url;
    
    /**
	 * 操作员工号
	 */
    private String operator;

    /**
	 * 状态
	 */
    private int state;
    
    /**
	 * 记录时间
	 */
    private Date logTime;
    
    /**
	 * HOrderFax ID <fk> 和HOrderFax关联
	 */
    private HOrderFax orderFaxH;

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public Long getHisID() {
        return hisID;
    }

    public void setHisID(Long hisID) {
        this.hisID = hisID;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HOrderFax getOrderFaxH() {
        return orderFaxH;
    }

    public void setOrderFaxH(HOrderFax orderFaxH) {
        this.orderFaxH = orderFaxH;
    }

}


package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 * 
 *  传真日志
 *  
 *  @author chenkeming
 */
public class OrFaxLog implements Entity {

    private static final long serialVersionUID = -402813162305905745L;

    /**
	 * ID <pk>
	 */
    private Long ID;

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

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
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
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }        

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

}

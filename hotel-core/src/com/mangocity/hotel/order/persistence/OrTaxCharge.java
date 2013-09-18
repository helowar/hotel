package com.mangocity.hotel.order.persistence;

import com.mangocity.util.Entity;


/**
 * 订单绑定的房费另缴税信息
 * @author chenkeming Feb 18, 2009 3:36:26 PM
 */
public class OrTaxCharge implements Entity {
    
    private static final long serialVersionUID = -4973097920428470724L;

    /**
	 * ID <pk>
	 */
    private Long ID;
    
    /**
	 * 房费是否含税
	 * @author chenkeming Feb 18, 2009 4:52:28 PM
	 */
    private String roomIncTax;
    
    /**
	 * 税名
	 * @author chenkeming Feb 18, 2009 3:38:05 PM
	 */
    private String roomTaxName;
    
    /**
	 * 日期范围字符串
	 * @author chenkeming Feb 18, 2009 3:38:21 PM
	 */
    private String beginEnd;
    
    /**
	 * 房费税
	 */
    private Double roomTax;
    
    /**
	 * 房税单位
	 */
    private String roomTaxUnit;
    
    /**
	 * 和OrOrder关联
	 * @author chenkeming Feb 18, 2009 3:43:37 PM
	 */
    private OrOrder order;

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public String getBeginEnd() {
        return beginEnd;
    }

    public void setBeginEnd(String beginEnd) {
        this.beginEnd = beginEnd;
    }

    public OrOrder getOrder() {
        return order;
    }

    public void setOrder(OrOrder order) {
        this.order = order;
    }

    public String getRoomIncTax() {
        return roomIncTax;
    }

    public void setRoomIncTax(String roomIncTax) {
        this.roomIncTax = roomIncTax;
    }

    public Double getRoomTax() {
        return roomTax;
    }

    public void setRoomTax(Double roomTax) {
        this.roomTax = roomTax;
    }

    public String getRoomTaxName() {
        return roomTaxName;
    }

    public void setRoomTaxName(String roomTaxName) {
        this.roomTaxName = roomTaxName;
    }

    public String getRoomTaxUnit() {
        return roomTaxUnit;
    }

    public void setRoomTaxUnit(String roomTaxUnit) {
        this.roomTaxUnit = roomTaxUnit;
    }
    
    
}

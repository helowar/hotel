package com.mangocity.hotel.order.persistence;

import com.mangocity.util.HEntity;


/**
 * 历史订单绑定的房费另缴税信息
 * @author chenkeming Feb 18, 2009 3:36:26 PM
 */
public class HTaxCharge implements HEntity {

    private static final long serialVersionUID = 7431906800687208058L;

    /**
	 * ID <pk>
	 */
    private Long hisID;
    
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
	 * 和HOrder关联
	 * @author chenkeming Feb 18, 2009 3:43:37 PM
	 */
    private HOrder orderH;

    public String getBeginEnd() {
        return beginEnd;
    }

    public void setBeginEnd(String beginEnd) {
        this.beginEnd = beginEnd;
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

    public Long getHisID() {
        return hisID;
    }

    public void setHisID(Long hisID) {
        this.hisID = hisID;
    }

    public HOrder getOrderH() {
        return orderH;
    }

    public void setOrderH(HOrder orderH) {
        this.orderH = orderH;
    }
    
    
}

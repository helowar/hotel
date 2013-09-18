package com.mangocity.hotel.order.persistence;

import com.mangocity.util.Entity;

/**
 * 
 *  入住人实体类
 */
public class OrFellowInfo implements Entity {

    private static final long serialVersionUID = 8060824567034208989L;

    /**
	 * ID <pk>
	 */
    private Long ID;

    /**
	 * 订单ID <fk> 和OrOrder关联
	 */
    private OrOrder order;

    /**
	 * 入住人名称
	 */
    private String fellowName;

    /**
	 * 入住人国籍
	 */
    private String fellowNationality = "1";

    /**
	 * 入住人联系电话
	 */
    private String fellowTelNo;

    /**
	 * 入住人性别
	 */
    private String fellowGender;

    /**
	 * 入住人证件类型
	 */
    private String fellowPaperType;

    /**
	 * 入住人证件号码
	 */
    private String fellowPaperNo;        

    /**
	 * 入住人类型
	 */
    private String fellowManType;
    
    /**
	 * 是否代订
	 */
    private boolean fellowSub = false;

    public String getFellowGender() {
        return fellowGender;
    }

    public void setFellowGender(String fellowGender) {
        this.fellowGender = fellowGender;
    }

    public String getFellowManType() {
        return fellowManType;
    }

    public void setFellowManType(String fellowManType) {
        this.fellowManType = fellowManType;
    }

    public String getFellowName() {
        return fellowName;
    }

    public void setFellowName(String fellowName) {
        this.fellowName = fellowName;
    }

    public String getFellowNationality() {
        return fellowNationality;
    }

    public void setFellowNationality(String fellowNationality) {
        this.fellowNationality = fellowNationality;
    }

    public String getFellowPaperNo() {
        return fellowPaperNo;
    }

    public void setFellowPaperNo(String fellowPaperNo) {
        this.fellowPaperNo = fellowPaperNo;
    }

    public String getFellowPaperType() {
        return fellowPaperType;
    }

    public void setFellowPaperType(String fellowPaperType) {
        this.fellowPaperType = fellowPaperType;
    }

    public String getFellowTelNo() {
        return fellowTelNo;
    }

    public void setFellowTelNo(String fellowTelNo) {
        this.fellowTelNo = fellowTelNo;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long guestId) {
        this.ID = guestId;
    }

    public OrOrder getOrder() {
        return order;
    }

    public void setOrder(OrOrder order) {
        this.order = order;
    }

    public boolean isFellowSub() {
        return fellowSub;
    }

    public void setFellowSub(boolean fellowSub) {
        this.fellowSub = fellowSub;
    }


}

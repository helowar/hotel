package com.mangocity.hotel.order.persistence;

import com.mangocity.util.HEntity;

/**
 * 
 *  历史订单入住人实体类
 */
public class HFellowInfo implements HEntity {

    private static final long serialVersionUID = 8862058090873841685L;

    /**
	 * 主键 <pk>
	 */
    private Long hisID;    

    /**
	 * 历史订单ID <fk> 和HOrder关联
	 */
    private HOrder orderH;

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

    public boolean isFellowSub() {
        return fellowSub;
    }

    public void setFellowSub(boolean fellowSub) {
        this.fellowSub = fellowSub;
    }

    public String getFellowTelNo() {
        return fellowTelNo;
    }

    public void setFellowTelNo(String fellowTelNo) {
        this.fellowTelNo = fellowTelNo;
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

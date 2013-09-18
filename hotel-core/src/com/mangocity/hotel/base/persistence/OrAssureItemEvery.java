package com.mangocity.hotel.base.persistence;

import java.util.Date;

import com.mangocity.util.Entity;


/**
 * 订单的担保(预付)取消及修改条款表
 * @author chenkeming Feb 18, 2009 11:19:16 AM
 */
public class OrAssureItemEvery implements Entity {

    private static final long serialVersionUID = 6815760431709086695L;

    /**
	 * 主键
	 */
    private Long ID;

    /**
	 * 2种:<br>
	 * 1: 凡取消/修改均须扣款<br>
	 * 2: 如 需取消或修改本次预订，请您务必YYYY-MM-DD TT:TT前致电
	 * @author chenkeming Feb 27, 2009 5:35:52 PM
	 */
    private String type;

    /**
	 * 待删除
	 * @author chenkeming Feb 27, 2009 5:37:09 PM
	 */
    private String firstDateOrDays;

    /**
	 * 待删除
	 * @author chenkeming Feb 27, 2009 5:37:09 PM
	 */
    private String firstTime;

    /**
	 * 待删除
	 * @author chenkeming Feb 27, 2009 5:37:09 PM
	 */
    private String secondDateOrDays;

    /**
	 * 待删除
	 * @author chenkeming Feb 27, 2009 5:37:09 PM
	 */
    private String secondTime;

    private String scope;

    private String deductType;

    private String percentage;
        
    /**
	 * 该条款来自于哪一天的取消修改条款
	 * @author chenkeming Feb 24, 2009 3:34:14 PM
	 */
    private Date night;
    
    /**
	 * 对于type=2的情况(需取消或修改本次预订，请您务必YYYY-MM-DD TT:TT前致电),<br>
	 * 该日期指必须在什么时候前致电
	 * @author chenkeming Feb 27, 2009 5:37:49 PM
	 */
    private Date beforeDate;
    
    private OrReservation reserv;

    public String getDeductType() {
        return deductType;
    }

    public void setDeductType(String deductType) {
        this.deductType = deductType;
    }

    public String getFirstDateOrDays() {
        return firstDateOrDays;
    }

    public void setFirstDateOrDays(String firstDateOrDays) {
        this.firstDateOrDays = firstDateOrDays;
    }

    public String getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(String firstTime) {
        this.firstTime = firstTime;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getSecondDateOrDays() {
        return secondDateOrDays;
    }

    public void setSecondDateOrDays(String secondDateOrDays) {
        this.secondDateOrDays = secondDateOrDays;
    }

    public String getSecondTime() {
        return secondTime;
    }

    public void setSecondTime(String secondTime) {
        this.secondTime = secondTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public OrReservation getReserv() {
        return reserv;
    }

    public void setReserv(OrReservation reserv) {
        this.reserv = reserv;
    }

    public Date getNight() {
        return night;
    }

    public void setNight(Date night) {
        this.night = night;
    }

    public Date getBeforeDate() {
        return beforeDate;
    }

    public void setBeforeDate(Date beforeDate) {
        this.beforeDate = beforeDate;
    }
    


}
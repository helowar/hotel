package com.mangocity.hotel.base.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 */
public class HtlExchange extends CEntity implements Entity {

    /**
     * id
     */
    private Long ID;

    /**
     * 合同的汇率，本位币
     */
    private String currency;

    /**
     * 目标币，一般为人民币
     */
    private String tocurrency;

    /**
     *汇率
     */
    private String rate;

    /**
     * 操作人
     */
    private String operateuser;

    /**
     * 修改时间
     */
    private Date operatetime;

    /**
	 * 
	 */
    private String orgid;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public Date getOperatetime() {
        return operatetime;
    }

    public void setOperatetime(Date operatetime) {
        this.operatetime = operatetime;
    }

    public String getOperateuser() {
        return operateuser;
    }

    public void setOperateuser(String operateuser) {
        this.operateuser = operateuser;
    }

    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTocurrency() {
        return tocurrency;
    }

    public void setTocurrency(String tocurrency) {
        this.tocurrency = tocurrency;
    }

}

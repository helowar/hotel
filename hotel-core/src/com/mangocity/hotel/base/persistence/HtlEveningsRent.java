package com.mangocity.hotel.base.persistence;

/**
 * HtlEveningsRent entity.
 * 
 * @author shengwei.zuo 各晚房费参数实体类 2009-08-25
 */

public class HtlEveningsRent implements java.io.Serializable {

    // Fields

    // ID
    private Long id;

    // 优惠参数ID
    private Long favouraParameterId;

    // 第几晚
    private Long night;

    // 售价
    private Double salePrice;

    // 佣金
    private Double commission;

    private HtlFavouraParameter favouraParameterEntiy;

    // Constructors

    /** default constructor */
    public HtlEveningsRent() {
    }

    /** minimal constructor */
    public HtlEveningsRent(Long favouraParameterId) {
        this.favouraParameterId = favouraParameterId;
    }

    /** full constructor */
    public HtlEveningsRent(Long favouraParameterId, Long night, 
        Double salePrice, Double commission) {
        this.favouraParameterId = favouraParameterId;
        this.night = night;
        this.salePrice = salePrice;
        this.commission = commission;
    }

    // Property accessors

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFavouraParameterId() {
        return this.favouraParameterId;
    }

    public void setFavouraParameterId(Long favouraParameterId) {
        this.favouraParameterId = favouraParameterId;
    }

    public Long getNight() {
        return this.night;
    }

    public void setNight(Long night) {
        this.night = night;
    }

    public Double getSalePrice() {
        return this.salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Double getCommission() {
        return this.commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public HtlFavouraParameter getFavouraParameterEntiy() {
        return favouraParameterEntiy;
    }

    public void setFavouraParameterEntiy(HtlFavouraParameter favouraParameterEntiy) {
        this.favouraParameterEntiy = favouraParameterEntiy;
    }

}
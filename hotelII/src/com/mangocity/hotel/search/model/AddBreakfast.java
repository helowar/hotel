package com.mangocity.hotel.search.model;

import java.util.Date;

import com.mangocity.util.Entity;


public class AddBreakfast {



    // 加床价格id
    private Long ID;


    // 起始日期
    private Date beginDate;

    // 终止时期
    private Date endDate;

    // 中餐
    private String chineseFood;

    private String chineseFoodType;

    // 西餐
    private String westernFood;

    private String westernFoodType;

    // 自助餐
    private String buffetDinner;

    private String buffetDinneTyper;

    /**
     * 支付类型
     * 
     * @return haibo.li 酒店2.9
     */
    private String payMethod;

    /**
     * 备注
     * 
     * @return haibo.li 酒店2.9
     */
    private String remark;

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBuffetDinneTyper() {
        return buffetDinneTyper;
    }

    public void setBuffetDinneTyper(String buffetDinneTyper) {
        this.buffetDinneTyper = buffetDinneTyper;
    }

    public String getChineseFoodType() {
        return chineseFoodType;
    }

    public void setChineseFoodType(String chineseFoodType) {
        this.chineseFoodType = chineseFoodType;
    }

    public String getWesternFoodType() {
        return westernFoodType;
    }

    public void setWesternFoodType(String westernFoodType) {
        this.westernFoodType = westernFoodType;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

   

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public String getBuffetDinner() {
        return buffetDinner;
    }

    public void setBuffetDinner(String buffetDinner) {
        this.buffetDinner = buffetDinner;
    }

    public String getChineseFood() {
        return chineseFood;
    }

    public void setChineseFood(String chineseFood) {
        this.chineseFood = chineseFood;
    }

    public String getWesternFood() {
        return westernFood;
    }

    public void setWesternFood(String westernFood) {
        this.westernFood = westernFood;
    }

}
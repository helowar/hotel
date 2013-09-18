package com.mangocity.hotel.base.web;

import java.util.List;

import com.mangocity.hotel.base.web.webwork.GenericAction;

/**
 */
public abstract class HotelBatchAction extends GenericAction {

    /**
     * 星期--字符窜,传递给前端页面
     */
    protected String strWeek;

    /**
     * 星期--从页面获取
     */
    protected String[] week;

    /**
     * 房型--从页面获取
     */
    protected Long[] roomTypes;

    /**
     * 价格类型--从页面获取
     */
    protected Long[] childRoomId;

    protected String strChildRoomId;

    /**
     * 支付方式--从页面获取
     */
    protected String[] payMethod;

    protected String strPayMethod;

    /**
     * 币种
     */
    protected String currency;

    /**
     * 公式ID
     */
    protected String formulaId;

    /**
     * 酒店ID,
     */
    protected Long hotelId;

    /**
     * 日期行数
     */
    protected int dateRowNum;

    /**
     * 日期数据
     */
    protected List dateList;

    /**
     * 合同ID
     */
    protected Long contractId;

    private Integer[] intWeek;

    public int getDateRowNum() {
        return dateRowNum;
    }

    public void setDateRowNum(int dateRowNum) {
        this.dateRowNum = dateRowNum;
    }

    public List getDateList() {
        return dateList;
    }

    public void setDateList(List dateList) {
        this.dateList = dateList;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelID) {
        this.hotelId = hotelID;
    }

    public String getStrWeek() {
//        this.week = week;
        StringBuffer weekBuffer = new StringBuffer();
        if (null != week && 0 < week.length) {
            intWeek = new Integer[week.length];
            for (int m = 0; m < week.length; m++) {
                // intWeek[m] = String.valueOf(week[m]);
                weekBuffer.append(week[m]);
                weekBuffer.append(",");

            }

        }
        this.strWeek = weekBuffer.toString();
        return strWeek;
    }

    public void setStrWeek(String strWeek) {
//        this.week = week;
        StringBuffer weekBuffer = new StringBuffer();
        if (null != week && 0 < week.length) {
            intWeek = new Integer[week.length];
            for (int m = 0; m < week.length; m++) {
                // intWeek[m] = String.valueOf(week[m]);
                weekBuffer.append(week[m]);
                weekBuffer.append(",");

            }

        }
        this.strWeek = weekBuffer.toString();
    }

    public String[] getWeek() {
        return week;
    }

    public void setWeek(String[] week) {
        this.week = week;

        if (null != week && 0 < week.length) {
            intWeek = new Integer[week.length];
            for (int m = 0; m < week.length; m++) {
                intWeek[m] = Integer.valueOf(week[m]);

            }
        }
    }

    public Integer[] getIntWeek() {
        return intWeek;
    }

    public Long[] getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(Long[] roomTypes) {
        this.roomTypes = roomTypes;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public void setIntWeek(Integer[] intWeek) {
        this.intWeek = intWeek;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getFormulaId() {
        return formulaId;
    }

    public void setFormulaId(String formulaId) {
        this.formulaId = formulaId;
    }

    public Long[] getChildRoomId() {
        return childRoomId;
    }

    public void setChildRoomId(Long[] childRoomId) {
        this.childRoomId = childRoomId;
    }

    public String[] getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String[] payMethod) {
        this.payMethod = payMethod;
    }

    public String getStrChildRoomId() {

        StringBuffer childRoomBuffer = new StringBuffer();
        if (null != childRoomId && 0 < childRoomId.length) {

            for (int m = 0; m < childRoomId.length; m++) {

                childRoomBuffer.append(String.valueOf(childRoomId[m]));
                childRoomBuffer.append(",");

            }

        }
        this.strChildRoomId = childRoomBuffer.toString();

        return strChildRoomId;
    }

    public void setStrChildRoomId(String strChildRoomId) {

        StringBuffer childRoomBuffer = new StringBuffer();
        if (null != childRoomId && 0 < childRoomId.length) {

            for (int m = 0; m < childRoomId.length; m++) {

                childRoomBuffer.append(String.valueOf(childRoomId[m]));
                childRoomBuffer.append(",");

            }

        }
        this.strChildRoomId = childRoomBuffer.toString();

    }

    public String getStrPayMethod() {

        StringBuffer payMethodBuffer = new StringBuffer();
        if (null != payMethod && 0 < payMethod.length) {

            for (int m = 0; m < payMethod.length; m++) {

                payMethodBuffer.append(String.valueOf(payMethod[m]));
                payMethodBuffer.append(",");

            }

        }
        this.strPayMethod = payMethodBuffer.toString();

        return strPayMethod;
    }

    public void setStrPayMethod(String strPayMethod) {
        StringBuffer payMethodBuffer = new StringBuffer();
        if (null != payMethod && 0 < payMethod.length) {

            for (int m = 0; m < payMethod.length; m++) {

                payMethodBuffer.append(String.valueOf(payMethod[m]));
                payMethodBuffer.append(",");

            }

        }
        this.strPayMethod = payMethodBuffer.toString();

    }

}

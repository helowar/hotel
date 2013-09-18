package com.mangocity.hotelweb.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mangocity.util.bean.CurrencyBean;

/**
 */
public class QueryHotelForWebRoomType implements Serializable {
    /**
     * 房型ID
     */
    private String roomTypeId;

    /**
     * 子房型ID
     */
    private String childRoomTypeId;

    /**
     * 房型名称
     */
    private String roomTypeName;

    /**
     * 子房型名称
     */
    private String childRoomTypeName;

    private String quotaType;

    /**
     * 门市价
     */
    private double roomPrice;

    /**
     * 周行数
     */
    private int weekTotal;

    /**
     * 显示行的宽度
     */
    private String avgWidthStr;

    /**
     * 行显示的占用列数
     */
    private int colCount;

    /**
     * 币种
     */
    private String currency;

    /**
     * 币种显示
     */
    private String currencyStr;

    // 酒店结果中有几种付款方式
    private int fk;

    /**
     * 周天数
     */
    private List weekNum = new ArrayList();

    /**
     * 列显示占用的行数
     */
    private int rowNum;

    /**
     * 预付预定按钮显示条件，查询结果如果房态最高级别为满房，为紧张时需要用
     */
    private int yud;

    /**
     * 面付预定按钮显示条件，查询结果如果房态最高级别为满房，为紧张时需要用
     */
    private int mf;

    /**
     * 支付方式
     */
    private String payStr;

    /**
     * 预付价格
     */
    private double prepayPrice;

    /**
     * 面付价格
     */
    private double payPrice;

    /**
     * 当只有面付或者预付时，保存在这个变量中
     */
    private double itemsPrice;

    /**
     * 当只有面付或者预付时，保存支付方式
     */
    private String payMethod;

    /*
     * 宽带
     */
    private String net;

    private boolean noOrder;

    private String close_flag;

    /**
     * 床型
     */
    private String bedType;

    private int breakfastType; // 早餐类型

    private int breakfastNum; // 早餐数量

    /**
     * 预付方式价格明细
     */
    private List<QueryHotelForWebSaleItems> saleItems = new ArrayList<QueryHotelForWebSaleItems>();

    /**
     * 面付方式价格明细
     */
    private List<QueryHotelForWebSaleItems> faceItems = new ArrayList<QueryHotelForWebSaleItems>();

    /**
     * 当只有面付或者预付时，保存在这个list中
     */
    private List<QueryHotelForWebSaleItems> itemsList = new ArrayList<QueryHotelForWebSaleItems>();

    // 存放面付的每天日期和价格集合String，格式为 星期:日期（yyyy-mm-dd）:价格/星期:日期（yyyy-mm-dd）:价格/
    // 注：为了与价格明细的存放方式一致，当只有面付或者预付时，存入这个字段
    // 这样在点预订按钮时传到后面，则在订单页面中直接显示预订的每天时间和价格
    private String faceDatePriceStr;

    // 存放预付的每天日期和价格集合String，格式为 星期:日期（yyyy-mm-dd）:价格/星期:日期（yyyy-mm-dd）:价格/
    private String saleDatePriceStr;

    public String getFaceDatePriceStr() {
        return faceDatePriceStr;
    }

    public void setFaceDatePriceStr(String faceDatePriceStr) {
        this.faceDatePriceStr = faceDatePriceStr;
    }

    public String getSaleDatePriceStr() {
        return saleDatePriceStr;
    }

    public void setSaleDatePriceStr(String saleDatePriceStr) {
        this.saleDatePriceStr = saleDatePriceStr;
    }

    public String getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(String roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public String getChildRoomTypeId() {
        return childRoomTypeId;
    }

    public void setChildRoomTypeId(String childRoomTypeId) {
        this.childRoomTypeId = childRoomTypeId;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public String getChildRoomTypeName() {
        return childRoomTypeName;
    }

    public void setChildRoomTypeName(String childRoomTypeName) {
        this.childRoomTypeName = childRoomTypeName;
    }

    public double getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(double roomPrice) {
        this.roomPrice = roomPrice;
    }

    public int getWeekTotal() {
        return weekTotal;
    }

    public void setWeekTotal(int weekTotal) {
        this.weekTotal = weekTotal;
    }

    public String getAvgWidthStr() {
        return avgWidthStr;
    }

    public void setAvgWidthStr(String avgWidthStr) {
        this.avgWidthStr = avgWidthStr;
    }

    public int getColCount() {
        return colCount;
    }

    public void setColCount(int colCount) {
        this.colCount = colCount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        if (null != currency && 0 < currency.length()) {
            this.currencyStr = CurrencyBean.idCurMap.get(currency);
        }
        this.currency = currency;
    }

    public int getFk() {
        return fk;
    }

    public void setFk(int fk) {
        this.fk = fk;
    }

    public List getWeekNum() {
        return weekNum;
    }

    public void setWeekNum(List weekNum) {
        this.weekNum = weekNum;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public int getYud() {
        return yud;
    }

    public void setYud(int yud) {
        this.yud = yud;
    }

    public int getMf() {
        return mf;
    }

    public void setMf(int mf) {
        this.mf = mf;
    }

    public String getPayStr() {
        return payStr;
    }

    public void setPayStr(String payStr) {
        this.payStr = payStr;
    }

    public List<QueryHotelForWebSaleItems> getSaleItems() {
        return saleItems;
    }

    public void setSaleItems(List<QueryHotelForWebSaleItems> saleItems) {
        this.saleItems = saleItems;
    }

    public List<QueryHotelForWebSaleItems> getFaceItems() {
        return faceItems;
    }

    public void setFaceItems(List<QueryHotelForWebSaleItems> faceItems) {
        this.faceItems = faceItems;
    }

    public List<QueryHotelForWebSaleItems> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<QueryHotelForWebSaleItems> itemsList) {
        this.itemsList = itemsList;
    }

    public double getPrepayPrice() {
        return prepayPrice;
    }

    public void setPrepayPrice(double prepayPrice) {
        this.prepayPrice = prepayPrice;
    }

    public double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(double payPrice) {
        this.payPrice = payPrice;
    }

    public double getItemsPrice() {
        return itemsPrice;
    }

    public void setItemsPrice(double itemsPrice) {
        this.itemsPrice = itemsPrice;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getNet() {
        return net;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public String getBedType() {
        return bedType;
    }

    public void setBedType(String bedType) {
        this.bedType = bedType;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    public boolean isNoOrder() {
        return noOrder;
    }

    public void setNoOrder(boolean noOrder) {
        this.noOrder = noOrder;
    }

    public String getCurrencyStr() {
        return currencyStr;
    }

    public void setCurrencyStr(String currencyStr) {
        this.currencyStr = currencyStr;
    }

    public int getBreakfastNum() {
        return breakfastNum;
    }

    public void setBreakfastNum(int breakfastNum) {
        this.breakfastNum = breakfastNum;
    }

    public int getBreakfastType() {
        return breakfastType;
    }

    public void setBreakfastType(int breakfastType) {
        this.breakfastType = breakfastType;
    }

    public String getClose_flag() {
        return close_flag;
    }

    public void setClose_flag(String close_flag) {
        this.close_flag = close_flag;
    }

}

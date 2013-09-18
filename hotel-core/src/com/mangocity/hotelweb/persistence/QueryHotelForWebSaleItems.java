package com.mangocity.hotelweb.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class QueryHotelForWebSaleItems implements Serializable {

    // 子房型ID
    private long priceId;

    // 房间日期(YYYY-MM-DD)
    private Date fellowDate;

    // 房间价格
    private double salePrice;

    // 房态
    private String roomState;

    private String quotaType; // 配额类型

    private String bedType; // 床型

    private String roomEquipment; // 房间设施：宽带

    private double salesRoomPrice;// 门市价

    // 显示页面的价格－－－如果是港币则为计算后的人民币价格
    private double rmbPrice;

    private String close_flag;
    
    private double CommissionPrice;

    public double getCommissionPrice() {
		return CommissionPrice;
	}

	public void setCommissionPrice(double commissionPrice) {
		CommissionPrice = commissionPrice;
	}

	public double getRmbPrice() {
        return rmbPrice;
    }

    public void setRmbPrice(double rmbPrice) {
        this.rmbPrice = rmbPrice;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    public Date getFellowDate() {
        return fellowDate;
    }

    public void setFellowDate(Date fellowDate) {
        this.fellowDate = fellowDate;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public String getRoomState() {
        return roomState;
    }

    public void setRoomState(String roomState) {
        if (null != roomState && 0 < roomState.length()) {
            this.roomState = strRoomStatue(roomState);
        } else {
            this.roomState = roomState;
        }
    }

    protected String strRoomStatue(String roomState) {
        String testStr = new String(roomState);
        String[] str = testStr.split("/");
        int t = -1;
        for (int i = 0; i < str.length; i++) {
            String[] testStr2 = str[i].split(":");
            for (int j = 1; j < testStr2.length; j += 2) {
                try {
                    if (t < Integer.parseInt(testStr2[j])) {
                        t = Integer.parseInt(testStr2[j]);
                    }
                } catch (Exception ex) {
                    t = -1;
                }
            }
        }
        return String.valueOf(t);
    }

    public long getPriceId() {
        return priceId;
    }

    public void setPriceId(long priceId) {
        this.priceId = priceId;
    }

    public String getBedType() {
        return bedType;
    }

    public void setBedType(String bedType) {
        this.bedType = bedType;
    }

    public String getRoomEquipment() {
        return roomEquipment;
    }

    public void setRoomEquipment(String roomEquipment) {
        this.roomEquipment = roomEquipment;
    }

    public double getSalesRoomPrice() {
        return salesRoomPrice;
    }

    public void setSalesRoomPrice(double salesRoomPrice) {
        this.salesRoomPrice = salesRoomPrice;
    }

    public String getClose_flag() {
        return close_flag;
    }

    public void setClose_flag(String close_flag) {
        this.close_flag = close_flag;
    }
}

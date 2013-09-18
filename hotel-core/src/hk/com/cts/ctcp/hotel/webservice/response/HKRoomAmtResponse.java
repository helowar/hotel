package hk.com.cts.ctcp.hotel.webservice.response;

import java.io.Serializable;

/**
 */
public class HKRoomAmtResponse implements Serializable {
	
	private static final long serialVersionUID = -8635363042039748934L;

    private int ret; // 错误标识符 或目标个数

    private String message; // 错误信息

    private Long hotelId; // 芒果酒店id

    private float baseAmt; // 底价

    private float listAmt; // 销售价

    private float rmbBaseAmt;// 人民币底价

    private float rmbListAmt;// 人民币销售价

    private String date; // 当天日期

    private int dayIndex; // 日期序号

    private String childRoomTypeId; // 价格编码 价格编码（针对不同的国家提供不同的价格）

    private String childRoomTypeIdDesc; // 价格类型描述(英语)

    private String roomTypeId; // //芒果房型编码

    public float getBaseAmt() {
        return baseAmt;
    }

    public float getListAmt() {
        return listAmt;
    }

    public String getDate() {
        return date;
    }

    public String getChildRoomTypeId() {
        return childRoomTypeId;
    }

    public void setChildRoomTypeId(String childRoomTypeId) {
        this.childRoomTypeId = childRoomTypeId;
    }

    public String getChildRoomTypeIdDesc() {
        return childRoomTypeIdDesc;
    }

    public void setChildRoomTypeIdDesc(String childRoomTypeIdDesc) {
        this.childRoomTypeIdDesc = childRoomTypeIdDesc;
    }

    public String getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(String roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public int getRet() {
        return ret;
    }

    public String getMessage() {
        return message;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public float getRmbBaseAmt() {
        return rmbBaseAmt;
    }

    public void setRmbBaseAmt(float rmbnBaseAmt) {
        this.rmbBaseAmt = rmbnBaseAmt;
    }

    public float getRmbListAmt() {
        return rmbListAmt;
    }

    public void setRmbListAmt(float rmbnListAmt) {
        this.rmbListAmt = rmbnListAmt;
    }

    public int getDayIndex() {
        return dayIndex;
    }

    public void setDayIndex(int index) {
        dayIndex = index;
    }

    public void setBaseAmt(float baseAmt) {
        this.baseAmt = baseAmt;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setListAmt(float listAmt) {
        this.listAmt = listAmt;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

}

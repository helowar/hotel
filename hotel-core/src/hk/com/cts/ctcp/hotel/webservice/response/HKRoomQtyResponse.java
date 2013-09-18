package hk.com.cts.ctcp.hotel.webservice.response;

import java.io.Serializable;

/**
 */
public class HKRoomQtyResponse implements Serializable {
	
	private static final long serialVersionUID = -585849742009718697L;

    private int ret; // 错误标识符 或目标个数

    private String message; // 错误信息

    private Long hotelId;// 芒果酒店id

    private int qty; // 房间数量

    private String roomTypeId; // 芒果房型编码

    private String roomTypeIdDesc; // 房型描述(用英语)

    private String date; // 当天日期 yyyy-MM-dd

    private int dayIndex; // 日期序号

    private String sCutoff; // 此房间有效时间

    /**
     * 最大可订数
     * 
     * @author chenkeming Mar 17, 2009 6:34:33 PM
     */
    private int maxQty;

    private String sDupNameFlg;// 允许重复标识 Y:允许重复

    private int nMinNite;

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

    public String getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(String roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public String getRoomTypeIdDesc() {
        return roomTypeIdDesc;
    }

    public void setRoomTypeIdDesc(String roomTypeIdDesc) {
        this.roomTypeIdDesc = roomTypeIdDesc;
    }

    public String getDate() {
        return date;
    }

    public int getDayIndex() {
        return dayIndex;
    }

    public void setDayIndex(int index) {
        dayIndex = index;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public int getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(int maxQty) {
        this.maxQty = maxQty;
    }

    public String getSCutoff() {
        return sCutoff;
    }

    public void setSCutoff(String cutoff) {
        sCutoff = cutoff;
    }

    public String getSDupNameFlg() {
        return sDupNameFlg;
    }

    public void setSDupNameFlg(String dupNameFlg) {
        sDupNameFlg = dupNameFlg;
    }

    public int getNMinNite() {
        return nMinNite;
    }

    public void setNMinNite(int minNite) {
        nMinNite = minNite;
    }

}


package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.util.Entity;


/**
 * 
 *  号百用订单类
 *  
 *  @author chenkeming
 */
public class OrOrderFor114 implements Entity {    
    
    private static final long serialVersionUID = -5101272782544513203L;

    /**
	 * 订单ID <pk>
	 */
    private Long ID;
    
    /**
	 * 订单CD
	 */
    private String orderCD;

    /**
	 * 酒店名称
	 */
    private String hotelName;
    
    /**
	 * 房间数量
	 */
    private int roomQuantity;
        
    /**
	 * 入住日期
	 */
    private Date checkinDate;

    /**
	 * 退房时间
	 */
    private Date checkoutDate;
    
    /**
	 * 订单创建时间
	 */
    private Date createDate;
    
    /**
	 * 转换为人民币后的支付总金额
	 */
    private double sumRmb;
    
    /**
	 * 联系人
	 */
    private String linkMan;    
    
    /**
	 * 手机
	 */
    private String mobile;    
    
    /**
	 * 中英文，包括所有入住人。便于查询
	 */
    private String fellowNames;    
    
    /**
	 * 会员所在的省份（用于114区分不同省份）
	 */
    private String memberState;
    
    /**
	 * 酒店星级
	 */
    private float hotelStar;
    
    /**
	 * 城市
	 */
    private String city;        
    
    /**
	 * 发送成功标志,Y：成功,N：失败，默认为N
	 */
    private String flag;
    
    /**
	 * 最近一次发送的时间
	 */
    private Date sendTime;
    
    /**
	 * webservice返回的xml中的错误代码(<result>)
	 */
    private long errorCode;
    
    /**
	 * webservice返回的xml中的错误描述(<errorDescription>)
	 */
    private String errorDesc;    
    
    /**
	 * 订单的有效时间，供报表等用途
	 */
    private Date validTime;

    public Date getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(Date checkinDate) {
        this.checkinDate = checkinDate;
    }

    public Date getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(Date checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public long getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(long errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public String getFellowNames() {
        return fellowNames;
    }

    public void setFellowNames(String fellowNames) {
        this.fellowNames = fellowNames;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public float getHotelStar() {
        return hotelStar;
    }

    public void setHotelStar(float hotelStar) {
        this.hotelStar = hotelStar;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getMemberState() {
        return memberState;
    }

    public void setMemberState(String memberState) {
        this.memberState = memberState;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOrderCD() {
        return orderCD;
    }

    public void setOrderCD(String orderCD) {
        this.orderCD = orderCD;
    }

    public int getRoomQuantity() {
        return roomQuantity;
    }

    public void setRoomQuantity(int roomQuantity) {
        this.roomQuantity = roomQuantity;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public double getSumRmb() {
        return sumRmb;
    }

    public void setSumRmb(double sumRmb) {
        this.sumRmb = sumRmb;
    }

    public Date getValidTime() {
        return validTime;
    }

    public void setValidTime(Date validTime) {
        this.validTime = validTime;
    }
    
    
                
    
}

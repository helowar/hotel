package com.mangocity.hotel.order.service.assistant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 用于传真, 邮件模板
 * 
 * @author chenkeming
 * 
 */
public class OrderFax implements Serializable {

    private String orderCD;

    private String memberName;

    private String orderMemberName;

    private String memberNameEnglish;

    private String memberTitle;

    private String memberTitleEnglish;

    private String memberNumber;

    private String customerInfo;

    private String hotelName;

    private String hotelNameEnglish;

    private int nightRoomCount;

    private String checkInDate;

    private String checkOutDate;

    private int nightCount;

    /**
     * OrderItemFax的list
     */
    private List<OrderItemFax> orderItemList = new ArrayList<OrderItemFax>();

    private String totalPrice;

    private String arrivalTimeStart;

    private String arrivalTimeEnd;

    private String arrivalTraffic;

    private String specialRequestOverView;

    private String note;

    private String hotelAddressTelephone;

    private String hotelConfirmNo;
    
    /**
     * 供应商名称（魅影项目用到）
     * add by mapengbo
     */
    private String supplierName;

    // 条形码
    private String barCode;

    // 是否重发
    private String isAnewSend;

    // 是否为配额内用房
    private String isQuotaInner;

    // 床型
    private String bedType;

    // //传真类型
    private String FaxType;

    // 酒店确认号.
    private String confirmNo;

    /********** 以下为邮件传真用 begin ************/

    private String hotelEmail;

    private String nowDate;

    private String newTime;

    private String orderNotes;

    /********** 以下为传真用 begin ************/
    private String oldOrderCD;

    private String hotelFax;

    private String basePrice;

    private String salePrice;

    private String hotelRoomType;

    /**
     * [Linkman] 先生/小姐
     */
    private String linkman;

    /**
     * 会员登录名
     */
    private String aliasName;

    /**
     * 含早情况：[isContainMeal]
     */
    private String isContainMeal;

    /**
     * 单价
     */
    private String roomPrice;

    /**
     * 付款方式：[PayMethod]
     */
    private String payMethod;

    /**
     * 酒店电话：[HotelTelephone]
     */
    private String hotelTelephone;

    // 需求变更.原本在传真和邮件摸板上加入担保金额一项,现已经取消.haibo.li 2008-12-11
    // /**
    // *担保金额
    // */
    // private String suretyMoney;
    //	

    /**
     * 114下订单给酒店发传真需要提供会员所在城市
     */
    private String cityName;
    
    private String arrivalTimeCheckout;
    /**
     * 给酒店的备注 add by juesu.chen
     */
    private String remarkToHotel;
    
    // 当立减优惠订单的时候这两个字段才有用 add by chenjiajie 2009-10-27 begin
    /**
     * 立减金额
     */
    private String benefitAmount;
    
    /**
     * 应付金额
     */
    private String actualAmount;
    // 当立减优惠订单的时候这两个字段才有用 add by chenjiajie 2009-10-27 end
    
    public String getArrivalTimeEnd() {
        return arrivalTimeEnd;
    }

    public void setArrivalTimeEnd(String arrivalTimeEnd) {
        this.arrivalTimeEnd = arrivalTimeEnd;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getHotelTelephone() {
        return hotelTelephone;
    }

    public void setHotelTelephone(String hotelTelephone) {
        this.hotelTelephone = hotelTelephone;
    }

    public String getIsContainMeal() {
        return isContainMeal;
    }

    public void setIsContainMeal(String isContainMeal) {
        this.isContainMeal = isContainMeal;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }

    public String getArrivalTimeStart() {
        return arrivalTimeStart;
    }

    public void setArrivalTimeStart(String arrivalTimeStart) {
        this.arrivalTimeStart = arrivalTimeStart;
    }

    public String getArrivalTraffic() {
        return arrivalTraffic;
    }

    public void setArrivalTraffic(String arrivalTraffic) {
        this.arrivalTraffic = arrivalTraffic;
    }

    public String getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(String basePrice) {
        this.basePrice = basePrice;
    }

    public String getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(String customerInfo) {
        this.customerInfo = customerInfo;
    }

    public String getHotelAddressTelephone() {
        return hotelAddressTelephone;
    }

    public void setHotelAddressTelephone(String hotelAddressTelephone) {
        this.hotelAddressTelephone = hotelAddressTelephone;
    }

    public String getHotelConfirmNo() {
        return hotelConfirmNo;
    }

    public void setHotelConfirmNo(String hotelConfirmNo) {
        this.hotelConfirmNo = hotelConfirmNo;
    }

    public String getHotelFax() {
        return hotelFax;
    }

    public void setHotelFax(String hotelFax) {
        this.hotelFax = hotelFax;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelNameEnglish() {
        return hotelNameEnglish;
    }

    public void setHotelNameEnglish(String hotelNameEnglish) {
        this.hotelNameEnglish = hotelNameEnglish;
    }

    public String getHotelRoomType() {
        return hotelRoomType;
    }

    public void setHotelRoomType(String hotelRoomType) {
        this.hotelRoomType = hotelRoomType;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberNameEnglish() {
        return memberNameEnglish;
    }

    public void setMemberNameEnglish(String memberNameEnglish) {
        this.memberNameEnglish = memberNameEnglish;
    }

    public String getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(String memberNumber) {
        this.memberNumber = memberNumber;
    }

    public String getMemberTitle() {
        return memberTitle;
    }

    public void setMemberTitle(String memberTitle) {
        this.memberTitle = memberTitle;
    }

    public String getMemberTitleEnglish() {
        return memberTitleEnglish;
    }

    public void setMemberTitleEnglish(String memberTitleEnglish) {
        this.memberTitleEnglish = memberTitleEnglish;
    }

    public int getNightCount() {
        return nightCount;
    }

    public void setNightCount(int nightCount) {
        this.nightCount = nightCount;
    }

    public int getNightRoomCount() {
        return nightRoomCount;
    }

    public void setNightRoomCount(int nightRoomCount) {
        this.nightRoomCount = nightRoomCount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getOrderCD() {
        return orderCD;
    }

    public void setOrderCD(String orderCD) {
        this.orderCD = orderCD;
    }

    public String getOrderMemberName() {
        return orderMemberName;
    }

    public void setOrderMemberName(String orderMemberName) {
        this.orderMemberName = orderMemberName;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getSpecialRequestOverView() {
        return specialRequestOverView;
    }

    public void setSpecialRequestOverView(String specialRequestOverView) {
        this.specialRequestOverView = specialRequestOverView;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getHotelEmail() {
        return hotelEmail;
    }

    public void setHotelEmail(String hotelEmail) {
        this.hotelEmail = hotelEmail;
    }

    public String getNewTime() {
        return newTime;
    }

    public void setNewTime(String newTime) {
        this.newTime = newTime;
    }

    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public String getOrderNotes() {
        return orderNotes;
    }

    public void setOrderNotes(String orderNotes) {
        this.orderNotes = orderNotes;
    }

    public String getOldOrderCD() {
        return oldOrderCD;
    }

    public void setOldOrderCD(String oldOrderCD) {
        this.oldOrderCD = oldOrderCD;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getBedType() {
        return bedType;
    }

    public void setBedType(String bedType) {
        this.bedType = bedType;
    }

    public String getIsAnewSend() {
        return isAnewSend;
    }

    public void setIsAnewSend(String isAnewSend) {
        this.isAnewSend = isAnewSend;
    }

    public String getIsQuotaInner() {
        return isQuotaInner;
    }

    public void setIsQuotaInner(String isQuotaInner) {
        this.isQuotaInner = isQuotaInner;
    }

    public String getFaxType() {
        return FaxType;
    }

    public void setFaxType(String faxType) {
        this.FaxType = faxType;
    }

    public String getConfirmNo() {
        return confirmNo;
    }

    public void setConfirmNo(String confirmNo) {
        this.confirmNo = confirmNo;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

	public String getArrivalTimeCheckout() {
		return arrivalTimeCheckout;
	}

	public void setArrivalTimeCheckout(String arrivalTimeCheckout) {
		this.arrivalTimeCheckout = arrivalTimeCheckout;
	}

	public String getRemarkToHotel() {
		return remarkToHotel;
	}

	public void setRemarkToHotel(String remarkToHotel) {
		this.remarkToHotel = remarkToHotel;
	}

	public String getBenefitAmount() {
		return benefitAmount;
	}

	public void setBenefitAmount(String benefitAmount) {
		this.benefitAmount = benefitAmount;
	}

	public String getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(String actualAmount) {
		this.actualAmount = actualAmount;
	}

	public List<OrderItemFax> getOrderItemList() {
		return orderItemList;
	}

	public void setOrderItemList(List<OrderItemFax> orderItemList) {
		this.orderItemList = orderItemList;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

    // public String getSuretyMoney() {
    // return suretyMoney;
    // }
    //
    // public void setSuretyMoney(String suretyMoney) {
    // this.suretyMoney = suretyMoney;
    // }

    /********** 以上为酒店传真用 end ************/
}

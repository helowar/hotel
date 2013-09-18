package com.mangocity.hotelweb.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.Serializable;

import com.mangocity.hotel.order.persistence.OrFellowInfo;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;

/**
 */
public class HotelOrderFromBean implements Serializable {

    // "hotelId"酒店ID
    private long hotelId;

    // "roomTypeId"房型ID
    private long roomTypeId;

    // "childRoomTypeId"子房型ID
    private long childRoomTypeId;

    // "payMethod"预付面付类型
    private String payMethod;

    // "minPrice"最低价格
    private double minPrice;

    // "maxPrice"最高价格
    private double maxPrice;

    // "checkinDate"入住日期
    private Date checkinDate;

    // "checkoutDate"退房日期
    private Date checkoutDate;

    // "hotelName"酒店名称
    private String hotelName;

    // "roomTypeName"房型名称
    private String roomTypeName;

    // "childRoomTypeName"子房型名称
    private String childRoomTypeName;

    // "priceNum"定房总价格
    private Double priceNum;

    // 城市代码
    private String cityCode;

    // "cityId"城市ID
    private String cityId;

    // 城市名称
    private String cityName;

    // 入住天数
    private int days;

    // paymentGate支付网关
    private String paymentGate;

    // paymentType支付类型 全额预付 混合预付
    private String paymentType;

    // 配额类型
    private String quotaType;

    // confirmtype确认方式
    private String confirmtype;

    // ulmPoint支付积分
    private String ulmPoint;

    // 确认联系信息
    private String confirminfo;

    // 是否使用积分
    private boolean useUlmPoint;

    // 可用积分
    private String ableUlmPoint;

    private String arrivalTime;

    private String latestArrivalTime;

    private float hotelStar;

    // 积分兑换率
    private String excRate;

    // 目标支付服务器URL
    private String targetUrl;

    // 入住人信息
    private List<OrFellowInfo> fellowList = new ArrayList<OrFellowInfo>();

    // 预定房间数
    private String roomQuantity;

    // 币种
    private String currency;

    // 币种显示符号
    private String currencyStr;

    // 存放面预付的每天日期和价格集合String
    private String datePriceStr;

    // 早餐类型
    private int breakfastType;

    // 早餐数量
    private int breakfastNum;

    // 到达方式
    private String arrivalMethod;

    // 联系人姓名
    private String linkMan;

    // 联系人手机号码
    private String mobile;

    // 固定电话区号
    private String fixedDistrictNum;

    // 固定电话号码
    private String fixedPhone;

    // 固定电话分机
    private String fixedExtension;

    // 完整固定电话
    private String telephone;

    // 联系人邮箱
    private String email;

    // 传真号码区号
    private String faxDistrictNum;

    // 传真号码
    private String faxPhone;

    // 传真号码分机
    private String faxExtension;

    // 完整传真号码
    private String customerFax;

    // 特殊要求
    private String specialRequest;

    // 是否需要担保(这里标志的是非条件担保，不是超时担保)
    private boolean needAssure;

    // 实收金额
    private double acturalAmount;

    // 联系人称呼，改为性别 男女
    private String title;

    private String agentCode;

    private String lastAssureTime;// 保留时限

    private String net;// 宽带

    private String tripType;// 出行方式

    private String dateSalePriceStr;

    private String dateBasePriceStr;

    private String dateMarketPriceStr;

    // 是否是商旅会员("Y","N")
    private String isTravelbusiness = "N";

    // 是否香港中科酒店 ADD BY WUYUN 2009-03-20
    private boolean flagCtsHK;

    // 是否香港中科酒店的房型 ADD BY WUYUN 2009-03-20
    private int roomChannel;

    // 价格是否发生变化 ADD BY WUYUN 2009-03-24
    private boolean priceChange;

    // 在线支付类型 ADD BY WUYUN 2009-03-24
    private int onlinePaytype;

    // 银行类型类型 ADD BY WUYUN 2009-03-25
    private String bankId;

    // 中科酒店一单最多可订房间数 ADD BY WUYUN 2009-03-25
    private int minRoomNumCts;

    // v2.6 必须面转预标志 ADD BY WUYUN 2009-06-04
    private boolean payToPrepay;

    // 提示信息
    private String tipInfo;
	
	//房间总数量 hotel 2.9.3 add by shengwei.zuo 
	private String  canRoomNumberWeb;
	
	/**
     * 设置房型对应的床型列表，以逗号分隔;add by shengwei.zuo  2009-10-26
     */
	private String bedTypeStr;
	
	
	/**
     * 用户选择的床型;add by shengwei.zuo  2009-10-26
     */
	private int bedType;
	
    /**
     * hotel 2.9.2 房型最多入住人数 为0填写订单页面则不做限制
     */
    private int maxPersons;
    
    /**
     * 使用了代金券的金额 hotel2.9.3 add by chenjiajie 2009-09-04
     */
    private String ulmCoupon;
    
    /**
     * 是否使用了代金券 hotel2.9.3 add by chenjiajie 2009-09-04
     */
    private boolean usedCoupon;
    
    /**
     * 酒店类型(散客酒店:0,协议酒店:1) HotelTMCType
     */
    private int hotelTMCType=0;
    
    /**
     * Haier 原因代码add by shizhongwen 2009-10-20
     */
    private String reason;
    
    /**
     *  Haier 原因代码ID add by shizhongwen 2009-10-20
     */
    private String reasonid;

    public boolean hasPayToPrepay() {
        return payToPrepay;
    }
    public boolean isPayToPrepay() {
        return payToPrepay;
    }

    public void setPayToPrepay(boolean payToPrepay) {
        this.payToPrepay = payToPrepay;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public int getMinRoomNumCts() {
        return minRoomNumCts;
    }

    public void setMinRoomNumCts(int minRoomNumCts) {
        this.minRoomNumCts = minRoomNumCts;
    }

    public int getOnlinePaytype() {
        return onlinePaytype;
    }

    public void setOnlinePaytype(int onlinePaytype) {
        this.onlinePaytype = onlinePaytype;
    }

    public boolean isPriceChange() {
        return priceChange;
    }

    public void setPriceChange(boolean priceChange) {
        this.priceChange = priceChange;
    }

    public boolean isFlagCtsHK() {
        return flagCtsHK;
    }

    public void setFlagCtsHK(boolean flagCtsHK) {
        this.flagCtsHK = flagCtsHK;
    }

    public String getIsTravelbusiness() {
        return isTravelbusiness;
    }

    public void setIsTravelbusiness(String isTravelbusiness) {
        this.isTravelbusiness = isTravelbusiness;
    }

    public String getDateBasePriceStr() {
        return dateBasePriceStr;
    }

    public void setDateBasePriceStr(String dateBasePriceStr) {
        this.dateBasePriceStr = dateBasePriceStr;
    }

    public String getDateMarketPriceStr() {
        return dateMarketPriceStr;
    }

    public void setDateMarketPriceStr(String dateMarketPriceStr) {
        this.dateMarketPriceStr = dateMarketPriceStr;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public String getNet() {
        return net;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public double getActuralAmount() {
        return acturalAmount;
    }

    public void setActuralAmount(double acturalAmount) {
        this.acturalAmount = acturalAmount;
    }

    public boolean isNeedAssure() {
        return needAssure;
    }

    public void setNeedAssure(boolean needAssure) {
        this.needAssure = needAssure;
    }

    public String getCustomerFax() {
        return customerFax;
    }

    public void setCustomerFax(String customerFax) {
        this.customerFax = customerFax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFaxDistrictNum() {
        return faxDistrictNum;
    }

    public void setFaxDistrictNum(String faxDistrictNum) {
        this.faxDistrictNum = faxDistrictNum;
    }

    public String getFaxExtension() {
        return faxExtension;
    }

    public void setFaxExtension(String faxExtension) {
        this.faxExtension = faxExtension;
    }

    public String getFaxPhone() {
        return faxPhone;
    }

    public void setFaxPhone(String faxPhone) {
        this.faxPhone = faxPhone;
    }

    public String getFixedDistrictNum() {
        return fixedDistrictNum;
    }

    public void setFixedDistrictNum(String fixedDistrictNum) {
        this.fixedDistrictNum = fixedDistrictNum;
    }

    public String getFixedExtension() {
        return fixedExtension;
    }

    public void setFixedExtension(String fixedExtension) {
        this.fixedExtension = fixedExtension;
    }

    public String getFixedPhone() {
        return fixedPhone;
    }

    public void setFixedPhone(String fixedPhone) {
        this.fixedPhone = fixedPhone;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getArrivalMethod() {
        return arrivalMethod;
    }

    public void setArrivalMethod(String arrivalMethod) {
        this.arrivalMethod = arrivalMethod;
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

    public String getSpecialRequest() {
        return specialRequest;
    }

    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
    }

    public String getConfirmtype() {
        return confirmtype;
    }

    public void setConfirmtype(String confirmtype) {
        this.confirmtype = confirmtype;
    }

    public String getUlmPoint() {
        return ulmPoint;
    }

    public void setUlmPoint(String ulmPoint) {
        this.ulmPoint = ulmPoint;
    }

    public String getPaymentGate() {
        return paymentGate;
    }

    public void setPaymentGate(String paymentGate) {
        this.paymentGate = paymentGate;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public long getChildRoomTypeId() {
        return childRoomTypeId;
    }

    public void setChildRoomTypeId(long childRoomTypeId) {
        this.childRoomTypeId = childRoomTypeId;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

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

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
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

    public Double getPriceNum() {
        return priceNum;
    }

    public void setPriceNum(Double priceNum) {
        this.priceNum = priceNum;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getConfirminfo() {
        return confirminfo;
    }

    public void setConfirminfo(String confirminfo) {
        this.confirminfo = confirminfo;
    }

    public boolean isUseUlmPoint() {
        return useUlmPoint;
    }

    public void setUseUlmPoint(boolean useUlmPoint) {
        this.useUlmPoint = useUlmPoint;
    }

    public String getAbleUlmPoint() {
        return ableUlmPoint;
    }

    public void setAbleUlmPoint(String ableUlmPoint) {
        this.ableUlmPoint = ableUlmPoint;
    }

    public String getExcRate() {
        return excRate;
    }

    public void setExcRate(String excRate) {
        this.excRate = excRate;
    }

    public String getRoomQuantity() {
        return roomQuantity;
    }

    public void setRoomQuantity(String roomQuantity) {
        this.roomQuantity = roomQuantity;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public List<OrFellowInfo> getFellowList() {
        return fellowList;
    }

    public void setFellowList(List<OrFellowInfo> fellowList) {
        this.fellowList = fellowList;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getLatestArrivalTime() {
        return latestArrivalTime;
    }

    public void setLatestArrivalTime(String latestArrivalTime) {
        this.latestArrivalTime = latestArrivalTime;
    }

    public String getDatePriceStr() {
        return datePriceStr;
    }

    public void setDatePriceStr(String datePriceStr) {
        this.datePriceStr = datePriceStr;
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

    public String getCurrencyStr() {
        return currencyStr;
    }

    public void setCurrencyStr(String currencyStr) {
        this.currencyStr = currencyStr;
    }

    public float getHotelStar() {
        return hotelStar;
    }

    public void setHotelStar(float hotelStar) {
        this.hotelStar = hotelStar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getLastAssureTime() {
        return lastAssureTime;
    }

    public void setLastAssureTime(String lastAssureTime) {
        this.lastAssureTime = lastAssureTime;
    }

    public String getDateSalePriceStr() {
        return dateSalePriceStr;
    }

    public void setDateSalePriceStr(String dateSalePriceStr) {
        this.dateSalePriceStr = dateSalePriceStr;
    }

    public int getRoomChannel() {
        return roomChannel;
    }

    public void setRoomChannel(int roomChannel) {
        this.roomChannel = roomChannel;
    }

    public int getMaxPersons() {
        return maxPersons;
    }

    public void setMaxPersons(int maxPersons) {
        this.maxPersons = maxPersons;
    }

    public String getTipInfo() {
        return tipInfo;
    }

    public void setTipInfo(String tipInfo) {
        this.tipInfo = tipInfo;
    }
	public String getUlmCoupon() {
		return StringUtil.isValidStr(ulmCoupon) ? ulmCoupon : "0";
	}
	public void setUlmCoupon(String ulmCoupon) {
		this.ulmCoupon = ulmCoupon;
	}
	public boolean isUsedCoupon() {
		return usedCoupon;
	}
	public void setUsedCoupon(boolean usedCoupon) {
		this.usedCoupon = usedCoupon;
	}
	
	
	public String getCanRoomNumberWeb() {
		return canRoomNumberWeb;
	}
	public void setCanRoomNumberWeb(String canRoomNumberWeb) {
		this.canRoomNumberWeb = canRoomNumberWeb;
	}
	
	public String getBedTypeStr() {
		return bedTypeStr;
	}
	public void setBedTypeStr(String bedTypeStr) {
		this.bedTypeStr = bedTypeStr;
	}
	public int getBedType() {
		return bedType;
	}
	public void setBedType(int bedType) {
		this.bedType = bedType;
	}	
	
    public int getHotelTMCType() {
        return hotelTMCType;
    }
    public void setHotelTMCType(int hotelTMCType) {
        this.hotelTMCType = hotelTMCType;
    }
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public String getReasonid() {
        return reasonid;
    }
    public void setReasonid(String reasonid) {
        this.reasonid = reasonid;
    }

}

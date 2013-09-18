package com.mangocity.tmc.service.assistant;



/**
 * 查询酒店输入条件
 * @author bruce.yang
 * 
 */
public class HotelQueryCondition {
	/**
	 * 城市三字码
	 */
	private String cityCode;
	/**
	 * 城市中文名
	 */
	private String cityName;
	/**
	 * 城市区域
	 */
	private String zone;
	/**
	 * 商业区
	 */
	private String bizZone;
	/**
	 * 入住日期
	 */
	private String joinDate;
	/**
	 * 入住天数
	 */
	private int joinDays;
	/**
	 * 离店日期
	 */
	private String leaveDate;
	/**
	 * 房源
	 */
	private String hotelSource;
	/**
	 * 商旅公司编码
	 */
	private String companyNo;
	/**
	 * 商旅公司ID
	 */
	private Long companyID;
	/**
	 * 最低价
	 */
	private Double priceLow;
	/**
	 * 最高价
	 */
	private Double priceHigh;
	/**
	 * 最低价(实际查询用)
	 */
	private Double priceLowQry;
	/**
	 * 最高价(实际查询用)
	 */
	private Double priceHighQry;
	/**
	 * 酒店星级
	 */
	private String hotelStar;
	/**
	 * 酒店类别
	 */
	private String hotelType;
	/**
	 * 酒店设施描述
	 */
	private String hotelFixtrue;
	/**
	 * 客房设施
	 */
	private String roomEquipment;
	/**
	 * 残疾人设施
	 */
	private String handicapped;
	/**
	 * 餐饮设施
	 */
	private String meal;
	/**
	 * 酒店连锁集团
	 */
	private String hotelChain;
	/**
	 * 酒店所属集团(编码)
	 */
	private String groupCode;	
	/**
	 * 酒店中文名
	 */
	private String chnName;
	/**
	 * 酒店英文名
	 */
	private String engName;
	/**
	 * 邮编
	 */
	private String postCode;
	/**
	 * 酒店中文地址
	 */
	private String chnAddress;
	/**
	 * 酒店英文地址
	 */
	private String engAddress;
	/**
	 * 电话总机
	 */
	private String telephone;
	
	/**
	 * 入住日期ddMMMyy格式字符串
	 */
	private String joinDateEngStr;
	
	/**
	 * 离开日期ddMMMyy格式字符串
	 */
	/**
	 * 会员id
	 */
	private Long memberID;
	/**
	 * 拷贝的订单Id
	 */
	private Long copyOrderId;
	/**
	 * 取消的订单Id
	 */
	private Long cancelOrderId;
	
	private String leaveDateEngStr;
	
    /**
     * Haier VIP会员 add by shizhongwen 2009-10-17
     */
    private boolean HaierVipMember;
    
    /**
     * 每页酒店数目
     */
    private int pageSize = 10;
    
    /**
     * 是否查宽带
     * 
     * @author chenkeming 
     */
    private boolean includeNet;
    
    /**
     * 是否查装修
     * 
     * @author chenkeming 
     */
    private boolean checkFitment;
	
	public String getBizZone() {
		return bizZone;
	}
	public void setBizZone(String bizZone) {
		this.bizZone = bizZone;
	}
	public String getChnAddress() {
		return chnAddress;
	}
	public void setChnAddress(String chnAddress) {
		this.chnAddress = chnAddress;
	}
	public String getChnName() {
		return chnName;
	}
	public void setChnName(String chnName) {
		this.chnName = chnName;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public Long getCompanyID() {
		return companyID;
	}
	public void setCompanyID(Long companyID) {
		this.companyID = companyID;
	}
	public String getCompanyNo() {
		return companyNo;
	}
	public void setCompanyNo(String companyNo) {
		this.companyNo = companyNo;
	}
	public String getEngAddress() {
		return engAddress;
	}
	public void setEngAddress(String engAddress) {
		this.engAddress = engAddress;
	}
	public String getEngName() {
		return engName;
	}
	public void setEngName(String engName) {
		this.engName = engName;
	}
	public String getHotelChain() {
		return hotelChain;
	}
	public void setHotelChain(String hotelChain) {
		this.hotelChain = hotelChain;
	}
	public String getHotelFixtrue() {
		return hotelFixtrue;
	}
	public void setHotelFixtrue(String hotelFixtrue) {
		this.hotelFixtrue = hotelFixtrue;
	}
	public String getHotelSource() {
		return hotelSource;
	}
	public void setHotelSource(String hotelSource) {
		this.hotelSource = hotelSource;
	}
	public String getHotelStar() {
		return hotelStar;
	}
	public void setHotelStar(String hotelStar) {
		this.hotelStar = hotelStar;
	}
	public String getHotelType() {
		return hotelType;
	}
	public void setHotelType(String hotelType) {
		this.hotelType = hotelType;
	}
	public String getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(String joinDate) {
		this.joinDateEngStr = joinDate;
		this.joinDate = joinDate;
	}
	public int getJoinDays() {
		return joinDays;
	}
	public void setJoinDays(int joinDays) {
		this.joinDays = joinDays;
	}
	public String getLeaveDate() {
		return leaveDate;
	}
	public void setLeaveDate(String leaveDate) {
		this.leaveDateEngStr = leaveDate;
		this.leaveDate = leaveDate;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public Double getPriceHigh() {
		return priceHigh;
	}
	public void setPriceHigh(Double priceHigh) {
		this.priceHigh = priceHigh;
		this.priceHighQry = priceHigh * 1.2;
	}
	public Double getPriceLow() {
		return priceLow;
	}
	public void setPriceLow(Double priceLow) {
		this.priceLow = priceLow;
		this.priceLowQry = priceLow * 0.8;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	
	public String getJoinDateEngStr(){
		return joinDateEngStr;
	}
	public String getLeaveDateEngStr() {
		return leaveDateEngStr;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public Double getPriceHighQry() {
		return priceHighQry;
	}
	public Double getPriceLowQry() {
		return priceLowQry;
	}
	public Long getCancelOrderId() {
		return cancelOrderId;
	}
	public void setCancelOrderId(Long cancelOrderId) {
		this.cancelOrderId = cancelOrderId;
	}
	public Long getCopyOrderId() {
		return copyOrderId;
	}
	public void setCopyOrderId(Long copyOrderId) {
		this.copyOrderId = copyOrderId;
	}
	public Long getMemberID() {
		return memberID;
	}
	public void setMemberID(Long memberID) {
		this.memberID = memberID;
	}
	public String getHandicapped() {
		return handicapped;
	}
	public void setHandicapped(String handicapped) {
		this.handicapped = handicapped;
	}
	public String getMeal() {
		return meal;
	}
	public void setMeal(String meal) {
		this.meal = meal;
	}
	public String getRoomEquipment() {
		return roomEquipment;
	}
	public void setRoomEquipment(String roomEquipment) {
		this.roomEquipment = roomEquipment;
	}
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public boolean isHaierVipMember() {
        return HaierVipMember;
    }
    public void setHaierVipMember(boolean haierVipMember) {
        HaierVipMember = haierVipMember;
    }
    public boolean isIncludeNet() {
        return includeNet;
    }
    public void setIncludeNet(boolean includeNet) {
        this.includeNet = includeNet;
    }
    public boolean isCheckFitment() {
        return checkFitment;
    }
    public void setCheckFitment(boolean checkFitment) {
        this.checkFitment = checkFitment;
    }
	
	
	
}

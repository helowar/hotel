package com.mangocity.tmc.persistence.view;

import java.io.Serializable;
import java.util.Date;

import com.mangocity.util.hotel.constant.FromChannelType;

/**
 * 
 * @author xiaowumi
 * 
 * 这个类主要是为了输入查询条件
 *  
 */
public class HotelQueryConditionTmc implements Serializable {
	
	/**
     * @author chenkeming Aug 25, 2009 3:49:15 PM
     */
    private static final long serialVersionUID = -3250934080370842836L;

    private String province;
	
	private String provinceID;
	//城市id
    private String cityId;
    //城市名称
    private String cityName;
    /**
     * 城区
     */
    private String district;
    
    private String districtName;
    
    private String business;
    
    private String businessName;
    //商业区
    private String bizZone;
    
    // 搜寻范围
    private String scale;
    
    /**
     * 酒店中文地址 
     */
    private String chnAddress;   

    /**
     * 酒店星级
     */
    private String hotelStar;
    
    /**
     * 处理酒店星级 CheckBox
     */
    private String star;
    
    /**
     * 开始日期(YYYY-MM-DD) 
     */
    private Date beginDate;

    /**
     * 结束日期(YYYY-MM-DD) 
     */
    private Date endDate;
    
    /**
     * 入住天数
     */
    private int days;
    
    /**
     * 酒店中文名称 
     */
    private String hotelChnName;

    /**
     * 酒店英文名称 
     */
    private String hotelEngName;

    private String isEngHotelName ;
    /**
     * 价格下限 
     */
    private String minPrice;
    
    /**
     * 价格上限
     */
    private String maxPrice;
    
    /**
     * 酒店代码 
     */
    private String hotelCode;
    //酒店id
    private String hotelId;
    //会员类型
    private String memberType;
    //会员id
    private long memberId;		
	
    /**
     * 酒店类别
     */
	private String hotelType;
	
	/**
	 * 房型
	 */
	private String roomType;
	
	/**
	 * 特殊要求
	 */
	private String specialRequest;
	
	/**
	 * 地理名胜
	 */
	private String notes;
	
	/**
	 * 地理名胜范围
	 */
	private String notesScope;
	
	/**
	 * 只显示推荐房型
	 */
	private boolean recommendRoomType = true;
	
	
    /**
     * 每页记录数, 默认为10 
     */
    private int pageSize = 10;
    
    /**
     * 当前页, 默认为1 
     */
	private int pageNo = 1;	
	
	/**
	 * 总页数
	 */
	private int totalPage;
	
	/**
	 * 排序方式 1:芒果网推荐  2:价格  3:酒店星级
	 */
	private int sortWay = 1;
	

	
	/**
	 * 预订酒店间数,
	 */
	private String hotelCount="1";
	/**
	 * 销售渠道 01 114
	 * @return
	 */
	private String saleChannel;
    
    /**
     * 查询的渠道来源
     * @author chenkeming Mar 31, 2009 11:55:22 AM
     * @see FromChannelType
     */
    private String fromChannel = "cc";
    
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getSortWay() {
		return sortWay;
	}
	public void setSortWay(int sortWay) {
		this.sortWay = sortWay;
	}
	public String getBizZone() {
		return bizZone;
	}
	public void setBizZone(String bizZone) {
		this.bizZone = bizZone;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getHotelChnName() {
		return hotelChnName;
	}
	public void setHotelChnName(String hotelChnName) {
		this.hotelChnName = hotelChnName;
	}
	public String getHotelCode() {
		return hotelCode;
	}
	public void setHotelCode(String hotelCode) {
		this.hotelCode = hotelCode;
	}
	public String getHotelEngName() {
		return hotelEngName;
	}
	public void setHotelEngName(String hotelEngName) {
		this.hotelEngName = hotelEngName;
	}
	public String getHotelId() {
		return hotelId;
	}
	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}
	public String getHotelStar() {
		return hotelStar;
	}
	public void setHotelStar(String hotelStar) {
		this.hotelStar = hotelStar;
	}
	public long getMemberId() {
		return memberId;
	}
	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}
	public String getMemberType() {
		return memberType;
	}
	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}
	public String getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(String priceScope) {
		this.minPrice = priceScope;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getChnAddress() {
		return chnAddress;
	}
	public void setChnAddress(String chnAddress) {
		this.chnAddress = chnAddress;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public String getHotelType() {
		return hotelType;
	}
	public void setHotelType(String hotelType) {
		this.hotelType = hotelType;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getSpecialRequest() {
		return specialRequest;
	}
	public void setSpecialRequest(String specialRequest) {
		this.specialRequest = specialRequest;
	}
	public String getNotesScope() {
		return notesScope;
	}
	public void setNotesScope(String notesScope) {
		this.notesScope = notesScope;
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
	public boolean isRecommendRoomType() {
		return recommendRoomType;
	}
	public void setRecommendRoomType(boolean recommendRoomType) {
		this.recommendRoomType = recommendRoomType;
	}
	public String getMaxPrice() {
		return maxPrice;
	}
	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}
	public String getRoomType() {
		return roomType;
	}
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public String getSaleChannel() {
		return saleChannel;
	}
	public void setSaleChannel(String saleChannel) {
		this.saleChannel = saleChannel;
	}
	/**
	 * 返回: 处理酒店星级 CheckBox
	 * 
	 * @return the star
	 */
	public String getStar() {
		return this.star;
	}
	/**
	 * 设置: 处理酒店星级 CheckBox
	 * 
	 * @param star the star to set
	 */
	public void setStar(String star) {
		this.star = star;
	}
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	public String getIsEngHotelName() {
		return isEngHotelName;
	}
	public void setIsEngHotelName(String isEngHotelName) {
		this.isEngHotelName = isEngHotelName;
	}
	public String getProvinceID() {
		return provinceID;
	}
	public void setProvinceID(String provinceID) {
		this.provinceID = provinceID;
	}

	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public String getBusiness() {
		return business;
	}
	public void setBusiness(String business) {
		this.business = business;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getHotelCount() {
		return hotelCount;
	}
	public void setHotelCount(String hotelCount) {
		this.hotelCount = hotelCount;
	}
    public String getFromChannel() {
        return fromChannel;
    }
    public void setFromChannel(String fromChannel) {
        this.fromChannel = fromChannel;
    }
	
}

package com.mangocity.hotel.base.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.util.Entity;

/**
 * HtlHotel generated by MyEclipse - Hibernate Tools 记录酒店基本信息
 */

public class HtlHotel implements Entity, Serializable {

    private static final long serialVersionUID = -2511331859465337655L;

    /**
     * 酒店id
     */
    private Long ID;

    /**
     * 酒店编码
     */
    private String hotelCd;

    /**
     * 合作伙伴酒店编码
     */
    private String hotelCodeName;

    /**
     * 酒店中文名
     */
    private String chnName;

    /**
     * 酒店E文名
     */
    private String engName;

    /**
     * 所属国家
     */
    private String country;

    /**
     * 所属省份
     */
    private String state;

    /**
     * 所属城市
     */
    private String city;

    /**
     * 所属城区
     */
    private String zone;

    /**
     * 所属商业区
     */
    private String bizZone;

    /**
     * 酒店层高
     */
    private String layerHigh;

    /**
     * 房间总数
     */
    private long layerCount;

    /**
     * 酒店星级
     */
    private String hotelStar;

    /**
     * 酒店网址
     */
    private String website;

    /**
     * 酒店类型
     */
    private String hotelType;

    /**
     * 酒店中文地址
     */
    private String chnAddress;

    /**
     * 酒店E文地址
     */
    private String engAddress;

    /**
     * 开业日期
     */
    private String praciceDate;

    /**
     * 装修日期
     */
    private String fitmentDate;

    /**
     * 装修程度
     */
    private String fitmentDegree;

    /**
     * 酒店电话
     */
    private String telephone;

    /**
     * 酒店邮编
     */
    private String postCode;

    /**
     * 产品经理
     */
    private String productManager;

    /**
     * 办公时间
     */
    private String workTime;

    /**
     * 办公时间内传真号码
     */
    private String workingFax;

    /**
     * 支持语种
     */
    private String language;

    /**
     * 支持其它语种
     */
    private String otherLanguage;

    /**
     * 支持信用卡
     */
    private String creditCardInfo;

    /**
     * 其它信用卡
     */
    private String otherCredit;

    /**
     * 接受客人
     */
    private String acceptCustom;

    /**
     * 生成简介
     */
    private String autoIntroduce;

    /**
     * 酒店详细介绍
     */
    private String chnHotelIntroduce;

    /**
     * 电邮
     */
    private String email;

    /**
     * 所属酒店集团
     */
    private String parentHotelGroup;

    /**
     * 接受旅行支票？？？？？
     */
    private String travelCheck;

    private double taxPer;

    /**
     * 客人刷卡扣税
     */
    private String cardNeedTax;

    /**
     * 规定入住时间
     */
    private String checkinTime;

    /**
     * 退房时间
     */
    private String checkoutTime;

    /**
     * 客房设施
     */
    private String roomFixtrue;

    /**
     * 残疾人设施
     */
    private String handicappedFixtrue;

    /**
     * 餐饮设施
     */
    private String mealFixtrue;

    /**
     * 周边景点
     */
    private String aroundView;

    /**
     * 免费服务
     */
    private String freeService;

    /**
     * 其它联系信息
     */
    private String othersNotes;

    /**
     * 自用备注
     */
    private String selfNotes;

    /**
     * 提示信息
     */
    private String alertMessage;

    /**
     * 酒店连锁集团
     */
    private String hotelChain;

    /**
     * 可销售渠道
     */
    private String saleChannel;

    /**
     * 采购渠道
     */
    private String purchaseChannel;

    /**
     * 配额模式
     */
    private String quotaPattern;

    /**
     * 创建人ID
     */
    private String createById;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 最近修改人ID
     */
    private String modifyById;

    /**
     * 最近修改人
     */
    private String modifyBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 是否有效
     */
    private String active;

    /**
     * 是否假删除
     */
    private boolean deleted;

    private List htlPictures = new ArrayList();

    private List contracts = new ArrayList();

    /**
     * 联系人信息
     */
    private List htlCtct = new ArrayList();

    /**
     * 销售季节信息
     */
    private List sellSeason = new ArrayList();

    /**
     * 酒店状态
     */
    private String hotelStatus;

    private long queryRoomLevel;

    /**
     * 酒店国籍是否又内外宾
     */
    private String foreignInfo;

    /**
     * 酒店国籍外宾信息
     */
    private String hasForeign;

    /**
     * 调价提示信息
     */
    private String changePriceHint;

    private List htlTrafficInfo = new ArrayList();

    private List htlBookSetup = new ArrayList();

    /**
     * 无烟楼层
     */
    private String noSmokingFloor;
    
    /**
     * 是否酒店全部禁烟 0不禁烟、1禁烟 add by xuyiwen 2010-10-20
     */
    private Integer isAllNoSmoking;

    /**
     * 系统内外酒店标志
     */
    private String hotelSystemSign;

    /**
     * 合作伙伴
     */
    private String partner;

    /**
     * 是否为即时确认酒店
     */
    private String iscontract;

    /**
     * 合同文件柜号
     */
    private String hasContract;

    /**
     * 外网是否显示预付价
     */
    private String webPrepayShow;

    /**
     * 酒店基本信息是否在外网显示
     */
    private String webShowBaseInfo;

    /**
     * 酒店所属品牌名称
     */
    private String plateName;

    /**
     * 网站首页推荐
     */
    private String firstpageRecommend;

    /**
     * 酒店主题，同一酒店可以有多个主题，<br>
     * 现在主题有：品牌酒店(01)、海滨酒店(02)、温泉酒店(03)、高尔夫酒店(04)、景区酒店(05)
     */
    private String theme;

    /**
     * 电子地图gisid
     */
    private Long gisid;

    /**
     * 电子地图经度
     */
    private Double longitude;

    /**
     * 电子地图纬度
     */
    private Double latitude;
    
    //百度纬度
	private Double baiduLatitude;
	//百度经度
	private Double baiduLongitude;

    /**
     * 酒店扩展信息
     */
    private List<HtlHotelExt> htelHotelExt = new ArrayList<HtlHotelExt>();

    /**
     * 酒店ebooking设置
     * 
     * @return
     */
    private List htlEbooking = new ArrayList<HtlEbooking>();

    public HtlHotel(Long id, String chnName, String city, Double longitude,
			Double latitude) {
		ID = id;
		this.chnName = chnName;
		this.city = city;
		this.longitude = longitude;
		this.latitude = latitude;
	}


	public HtlHotel() {
	}
    
    
	public List getHtlEbooking() {
        return htlEbooking;
    }

    public void setHtlEbooking(List htlEbooking) {
         this.htlEbooking = htlEbooking;
     }

    public List<HtlHotelExt> getHtelHotelExt() {
        return htelHotelExt;
    }

    public void setHtelHotelExt(List<HtlHotelExt> htelHotelExt) {
        this.htelHotelExt = htelHotelExt;
    }

    /**
     * 酒店表扩展表
     */

    public void addHtlEbooking(HtlEbooking htlEbooking) {
        getHtlEbooking().add(htlEbooking);
        htlEbooking.setHtlHotel(this);

    }

    public String getFirstpageRecommend() {
        return firstpageRecommend;
    }

    public void setFirstpageRecommend(String firstpageRecommend) {
        this.firstpageRecommend = firstpageRecommend;
    }

    public String getAcceptCustom() {
        return acceptCustom;
    }

    public void setAcceptCustom(String acceptCustom) {
        this.acceptCustom = acceptCustom;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public String getAroundView() {
        return aroundView;
    }

    public void setAroundView(String aroundView) {
        this.aroundView = aroundView;
    }

    public String getAutoIntroduce() {
        return autoIntroduce;
    }

    public void setAutoIntroduce(String autoIntroduce) {
        this.autoIntroduce = autoIntroduce;
    }

    public String getBizZone() {
        return bizZone;
    }

    public void setBizZone(String bizZone) {
        this.bizZone = bizZone;
    }

    public String getCardNeedTax() {
        return cardNeedTax;
    }

    public void setCardNeedTax(String cardNeedTax) {
        this.cardNeedTax = cardNeedTax;
    }

    public String getCheckinTime() {
        return checkinTime;
    }

    public void setCheckinTime(String checkinTime) {
        this.checkinTime = checkinTime;
    }

    public String getCheckoutTime() {
        return checkoutTime;
    }

    public void setCheckoutTime(String checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

    public String getChnAddress() {
        return chnAddress;
    }

    public void setChnAddress(String chnAddress) {
        this.chnAddress = chnAddress;
    }

    public String getChnHotelIntroduce() {
        return chnHotelIntroduce;
    }

    public void setChnHotelIntroduce(String chnHotelIntroduce) {
        this.chnHotelIntroduce = chnHotelIntroduce;
    }

    public String getChnName() {
        return chnName;
    }

    public void setChnName(String chnName) {
        this.chnName = chnName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List getContracts() {
        return contracts;
    }

    public void setContracts(List contracts) {
        this.contracts = contracts;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateById() {
        return createById;
    }

    public void setCreateById(String createById) {
        this.createById = createById;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreditCardInfo() {
        return creditCardInfo;
    }

    public void setCreditCardInfo(String creditCardInfo) {
        this.creditCardInfo = creditCardInfo;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Integer getIsAllNoSmoking() {
		return isAllNoSmoking;
	}

	public void setIsAllNoSmoking(Integer isAllNoSmoking) {
		this.isAllNoSmoking = isAllNoSmoking;
	}

	public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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

    public String getFitmentDate() {
        return fitmentDate;
    }

    public void setFitmentDate(String fitmentDate) {
        this.fitmentDate = fitmentDate;
    }

    public String getFitmentDegree() {
        return fitmentDegree;
    }

    public void setFitmentDegree(String fitmentDegree) {
        this.fitmentDegree = fitmentDegree;
    }

    public String getHandicappedFixtrue() {
        return handicappedFixtrue;
    }

    public void setHandicappedFixtrue(String handicappedFixtrue) {
        this.handicappedFixtrue = handicappedFixtrue;
    }

    public String getHotelCd() {
        return hotelCd;
    }

    public void setHotelCd(String hotelCd) {
        this.hotelCd = hotelCd;
    }

    public String getHotelChain() {
        return hotelChain;
    }

    public void setHotelChain(String hotelChain) {
        this.hotelChain = hotelChain;
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

    public List getHtlCtct() {
        return htlCtct;
    }

    public void setHtlCtct(List htlCtct) {
        this.htlCtct = htlCtct;
    }

    public List getHtlPictures() {
        return htlPictures;
    }

    public void setHtlPictures(List htlPictures) {
        this.htlPictures = htlPictures;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public long getLayerCount() {
        return layerCount;
    }

    public void setLayerCount(long layerCount) {
        this.layerCount = layerCount;
    }

    public String getLayerHigh() {
        return layerHigh;
    }

    public void setLayerHigh(String layerHigh) {
        this.layerHigh = layerHigh;
    }

    public String getMealFixtrue() {
        return mealFixtrue;
    }

    public void setMealFixtrue(String mealFixtrue) {
        this.mealFixtrue = mealFixtrue;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public String getModifyById() {
        return modifyById;
    }

    public void setModifyById(String modifyById) {
        this.modifyById = modifyById;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getOthersNotes() {
        return othersNotes;
    }

    public void setOthersNotes(String othersNotes) {
        this.othersNotes = othersNotes;
    }

    public String getParentHotelGroup() {
        return parentHotelGroup;
    }

    public void setParentHotelGroup(String parentHotelGroup) {
        this.parentHotelGroup = parentHotelGroup;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPraciceDate() {
        return praciceDate;
    }

    public void setPraciceDate(String praciceDate) {
        this.praciceDate = praciceDate;
    }

    public String getQuotaPattern() {
        return quotaPattern;
    }

    public void setQuotaPattern(String quotaPattern) {
        this.quotaPattern = quotaPattern;
    }

    public String getRoomFixtrue() {
        return roomFixtrue;
    }

    public void setRoomFixtrue(String roomFixtrue) {
        this.roomFixtrue = roomFixtrue;
    }

    public String getSaleChannel() {
        return saleChannel;
    }

    public void setSaleChannel(String saleChannel) {
        this.saleChannel = saleChannel;
    }

    public String getSelfNotes() {
        return selfNotes;
    }

    public void setSelfNotes(String selfNotes) {
        this.selfNotes = selfNotes;
    }

    public List getSellSeason() {
        return sellSeason;
    }

    public void setSellSeason(List sellSeason) {
        this.sellSeason = sellSeason;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getTaxPer() {
        return taxPer;
    }

    public void setTaxPer(double taxPer) {
        this.taxPer = taxPer;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getTravelCheck() {
        return travelCheck;
    }

    public void setTravelCheck(String travelCheck) {
        this.travelCheck = travelCheck;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getWorkingFax() {
        return workingFax;
    }

    public void setWorkingFax(String workingFax) {
        this.workingFax = workingFax;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public List getHtlTrafficInfo() {
        return htlTrafficInfo;
    }

    public void setHtlTrafficInfo(List htlTrafficInfo) {
        this.htlTrafficInfo = htlTrafficInfo;
    }

    public List getHtlBookSetup() {
        return htlBookSetup;
    }

    public void setHtlBookSetup(List htlBookSetup) {
        this.htlBookSetup = htlBookSetup;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getChangePriceHint() {
        return changePriceHint;
    }

    public void setChangePriceHint(String changePriceHint) {
        this.changePriceHint = changePriceHint;
    }

    public String getHotelStatus() {
        return hotelStatus;
    }

    public void setHotelStatus(String hotelStatus) {
        this.hotelStatus = hotelStatus;
    }

    public String getForeignInfo() {
        return foreignInfo;
    }

    public void setForeignInfo(String foreignInfo) {
        this.foreignInfo = foreignInfo;
    }

    public String getHasForeign() {
        return hasForeign;
    }

    public void setHasForeign(String hasForeign) {
        this.hasForeign = hasForeign;
    }

    public long getQueryRoomLevel() {
        return queryRoomLevel;
    }

    public void setQueryRoomLevel(long queryRoomLevel) {
        this.queryRoomLevel = queryRoomLevel;
    }

    public String getOtherLanguage() {
        return otherLanguage;
    }

    public void setOtherLanguage(String otherLanguage) {
        this.otherLanguage = otherLanguage;
    }

    public String getOtherCredit() {
        return otherCredit;
    }

    public void setOtherCredit(String otherCredit) {
        this.otherCredit = otherCredit;
    }

    public String getHotelCodeName() {
        return hotelCodeName;
    }

    public void setHotelCodeName(String hotelCodeName) {
        this.hotelCodeName = hotelCodeName;
    }

    public String getNoSmokingFloor() {
        return noSmokingFloor;
    }

    public void setNoSmokingFloor(String noSmokingFloor) {
        this.noSmokingFloor = noSmokingFloor;
    }

    public String getHotelSystemSign() {
        return hotelSystemSign;
    }

    public void setHotelSystemSign(String hotelSystemSign) {
        this.hotelSystemSign = hotelSystemSign;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getFreeService() {
        return freeService;
    }

    public void setFreeService(String freeService) {
        this.freeService = freeService;
    }

    public String getIscontract() {
        return iscontract;
    }

    public void setIscontract(String iscontract) {
        this.iscontract = iscontract;
    }

    public String getHasContract() {
        return hasContract;
    }

    public void setHasContract(String hasContract) {
        this.hasContract = hasContract;
    }

    public String getWebPrepayShow() {
        return webPrepayShow;
    }

    public void setWebPrepayShow(String webPrepayShow) {
        this.webPrepayShow = webPrepayShow;
    }

    public String getWebShowBaseInfo() {
        return webShowBaseInfo;
    }

    public void setWebShowBaseInfo(String webShowBaseInfo) {
        this.webShowBaseInfo = webShowBaseInfo;
    }

    public String getProductManager() {
        return productManager;
    }

    public void setProductManager(String productManager) {
        this.productManager = productManager;
    }

    public String getPlateName() {
        return plateName;
    }

    public void setPlateName(String plateName) {
        this.plateName = plateName;
    }

    public String getPurchaseChannel() {
        return purchaseChannel;
    }

    public void setPurchaseChannel(String purchaseChannel) {
        this.purchaseChannel = purchaseChannel;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Long getGisid() {
        return gisid;
    }

    public void setGisid(Long gisid) {
        this.gisid = gisid;
    }


	public Double getLongitude() {
		return longitude;
	}


	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}


	public Double getLatitude() {
		return latitude;
	}


	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}


	public Double getBaiduLatitude() {
		return baiduLatitude;
	}

	public void setBaiduLatitude(Double baiduLatitude) {
		this.baiduLatitude = baiduLatitude;
	}

	public Double getBaiduLongitude() {
		return baiduLongitude;
	}
	
	public void setBaiduLongitude(Double baiduLongitude) {
		this.baiduLongitude = baiduLongitude;
	}
}
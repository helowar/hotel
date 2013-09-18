package com.mangocity.tmc.service.assistant;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 酒店查询结果类
 * @author bruce.yang 
 */
public class HotelInfo {
	
	/**
	 * 酒店ID
	 */
	private String hotelId;
	
	/**
	 * 城市
	 */
	private String city;
	
	
	/**
	 * 酒店类别(协议酒店、共有酒店、本部酒店)
	 */
	private String hotelKind;
	
	/**
	 * 酒店中文名称
	 */
	private String hotelChnName;
	
	/**
	 * 酒店英文名称
	 */
	private String hotelEngName;

	/**
	 * 酒店星级
	 */
	private String hotelStar;

	/**
	 * 酒店类型
	 */
	private String hotelType;

	/**
	 * 酒店中文地址
	 */
	private String chnAddress;

	/**
	 * 酒店英文地址
	 */
	private String engAddress;

	/**
	 * 酒店介绍
	 */
	private String hotelIntroduce;

	/**
	 * 提示信息
	 */
	private String clueInfo;
	
	/**
	 * 担保信息
	 */
	private String assureInfo;
	/**
	 * 本部房型担保信息
	 */
	private String hotelAssureInfo;
	/**
	 * 协议房型担保信息
	 */
	private String tmcAssureInfo;
	/**
	 * 本部房型促销信息
	 */
	private String saleInfo;

	/**
	 * 取消信息
	 */
	private String cancelMessage;

	/**
	 * 城区
	 */
	private String zone;
	/**
	 * 商业区
	 */
	private String bizZone;

	/**
	 * 最低价
	 */
	private String lowPrice;
	
	/**
	 * 最高价
	 */
	private String highPrice;

	/**
	 * 结算方式
	 */
	private String balanceMethod;

	/**
	 * 酒店预付款的支付时限
	 */
	private Date advancePayTime;

	/**
	 * 协议房间列表，是RoomType类的集合
	 */
	private List tmcRoomTypes = new ArrayList();
	/**
	 * 本部房间列表，是RoomType类的集合
	 */
	private List hotelRoomTypes = new ArrayList();
	/**
	 * 酒店所用币种
	 */
	private String currency;//用来显示酒店所用币种而加xy
	
	
	/**
	 * addby chunshen.gong 2008.12.12
	 *  主推类型(特推酒店1，金牌酒店2，银牌酒店3,黑4)
	 */	
	private String commendType;
	
	/**
	 * addby chunshen.gong 2008.12.12
	 * 房费需另缴税(1有，0没有)
	 */
	private int isTaxCharges;
	/**
	 * addby chunshen.gong 2008.12.12
	 *是否有预订条款 (1有，0没有)
	 */
	private int isReservation;
	/**
	 * addby chunshen.gong 2008.12.12
	 *促销信息 (1有，0没有)
	 */
	private int isSalesPromo;	
	/**
	 * addby chunshen.gong 2008.12.12
	 * 是否为即时确认酒店
	 */
	private String iscontract;
	/**
	 * addby xulei.chen 2009.05.05
	 * 酒店直联标志
	 */
	private String cooperate_channel;
    
    /**
     * 酒店LOGO照片
     */
    private String hotelLogo;
    
    /**
     * 酒店是否有接机服务. 1: 有 0: 无
     * 
     * @author chenkeming
     */
    private int forPlane;
    
    /**
     * 酒店是否有免费健身设施. 1: 有 0: 无
     * 
     * @author chenkeming 
     */
    private int forFreeGym;
    
    /**
     * 酒店是否有免费停车场. 1: 有 0: 无
     * 
     * @author chenkeming 
     */
    private int forFreeStop;
    
    /**
     * 酒店是否有免费游泳池. 1: 有 0: 无
     * 
     * @author chenkeming 
     */
    private int forFreePool;
    
    
    private String cancelModifyStr;//取消规定
	
	public String getIscontract() {
		return iscontract;
	}
	public void setIscontract(String iscontract) {
		this.iscontract = iscontract;
	}
	public Date getAdvancePayTime() {
		return advancePayTime;
	}
	public void setAdvancePayTime(Date advancePayTime) {
		this.advancePayTime = advancePayTime;
	}
	public String getBalanceMethod() {
		return balanceMethod;
	}
	public void setBalanceMethod(String balanceMethod) {
		this.balanceMethod = balanceMethod;
	}
	public String getBizZone() {
		return bizZone;
	}
	public void setBizZone(String bizZone) {
		this.bizZone = bizZone;
	}
	public String getCancelMessage() {
		return cancelMessage;
	}
	public void setCancelMessage(String cancelMessage) {
		this.cancelMessage = cancelMessage;
	}
	public String getChnAddress() {
		return chnAddress;
	}
	public void setChnAddress(String chnAddress) {
		this.chnAddress = chnAddress;
	}
	public String getClueInfo() {
		return clueInfo;
	}
	public void setClueInfo(String clueInfo) {
		this.clueInfo = clueInfo;
	}
	public String getEngAddress() {
		return engAddress;
	}
	public void setEngAddress(String engAddress) {
		this.engAddress = engAddress;
	}
	public String getHighPrice() {
		return highPrice;
	}
	public void setHighPrice(String highPrice) {
		this.highPrice = highPrice;
	}
	public String getHotelChnName() {
		return hotelChnName;
	}
	public void setHotelChnName(String hotelChnName) {
		this.hotelChnName = hotelChnName;
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
	public String getHotelIntroduce() {
		return hotelIntroduce;
	}
	public void setHotelIntroduce(String hotelIntroduce) {
		this.hotelIntroduce = hotelIntroduce;
	}
	public String getHotelKind() {
		return hotelKind;
	}
	public void setHotelKind(String hotelKind) {
		this.hotelKind = hotelKind;
	}
	public List getHotelRoomTypes() {
		return hotelRoomTypes;
	}
	public void setHotelRoomTypes(List hotelRoomTypes) {
		this.hotelRoomTypes = hotelRoomTypes;
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
	public String getLowPrice() {
		return lowPrice;
	}
	public void setLowPrice(String lowPrice) {
		this.lowPrice = lowPrice;
	}
	public List getTmcRoomTypes() {
		return tmcRoomTypes;
	}
	public void setTmcRoomTypes(List tmcRoomTypes) {
		this.tmcRoomTypes = tmcRoomTypes;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getAssureInfo() {
		return assureInfo;
	}
	public void setAssureInfo(String assureInfo) {
		this.assureInfo = assureInfo;
	}
	public String getHotelAssureInfo() {
		return hotelAssureInfo;
	}
	public void setHotelAssureInfo(String hotelAssureInfo) {
		this.hotelAssureInfo = hotelAssureInfo;
	}
	public String getSaleInfo() {
		return saleInfo;
	}
	public void setSaleInfo(String saleInfo) {
		this.saleInfo = saleInfo;
	}
	public String getTmcAssureInfo() {
		return tmcAssureInfo;
	}
	public void setTmcAssureInfo(String tmcAssureInfo) {
		this.tmcAssureInfo = tmcAssureInfo;
	}
	
	public void addTmcRoomType(Object o){
		this.tmcRoomTypes.add(o);
	}
	
	public void addHotelRoomType(Object o){
		this.hotelRoomTypes.add(o);
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getCommendType() {
		return commendType;
	}
	public void setCommendType(String commendType) {
		this.commendType = commendType;
	}
	public int getIsReservation() {
		return isReservation;
	}
	public void setIsReservation(int isReservation) {
		this.isReservation = isReservation;
	}
	public int getIsSalesPromo() {
		return isSalesPromo;
	}
	public void setIsSalesPromo(int isSalesPromo) {
		this.isSalesPromo = isSalesPromo;
	}
	public int getIsTaxCharges() {
		return isTaxCharges;
	}
	public void setIsTaxCharges(int isTaxCharges) {
		this.isTaxCharges = isTaxCharges;
	}
	public String getCooperate_channel() {
		return cooperate_channel;
	}
	public void setCooperate_channel(String cooperate_channel) {
		this.cooperate_channel = cooperate_channel;
	}
    public String getHotelLogo() {
        return hotelLogo;
    }
    public void setHotelLogo(String hotelLogo) {
        this.hotelLogo = hotelLogo;
    }
    public int getForPlane() {
        return forPlane;
    }
    public void setForPlane(int forPlane) {
        this.forPlane = forPlane;
    }
    public int getForFreeGym() {
        return forFreeGym;
    }
    public void setForFreeGym(int forFreeGym) {
        this.forFreeGym = forFreeGym;
    }
    public int getForFreeStop() {
        return forFreeStop;
    }
    public void setForFreeStop(int forFreeStop) {
        this.forFreeStop = forFreeStop;
    }
    public int getForFreePool() {
        return forFreePool;
    }
    public void setForFreePool(int forFreePool) {
        this.forFreePool = forFreePool;
    }
    public String getCancelModifyStr() {
        return cancelModifyStr;
    }
    public void setCancelModifyStr(String cancelModifyStr) {
        this.cancelModifyStr = cancelModifyStr;
    }
	
}

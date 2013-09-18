package com.mangocity.hotel.search.vo;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.common.vo.CommentSummaryVO;
import com.mangocity.hotel.common.vo.HotelPictureVO;
import com.mangocity.hotel.common.vo.PreSaleVO;
import com.mangocity.hotel.search.sort.SortedHotelInfo;


/**
 * 酒店网站查询结果<br>
 * 酒店类<br>
 * 
 * 
 * @author zengyong
 * 
 */
public class HotelResultVO extends SortedHotelInfo implements SerializableVO {
	
	
	public HotelResultVO(){}
	
	private static final long serialVersionUID = -5670439186537957813L;

    protected String cityCode;
	/**
     * 酒店id
     */
	protected long hotelId;

    /**
     * 酒店中文名称
     */
    protected String chnName;

    /**
     * 酒店英文名称
     */	
    protected String engName;

    /**
     * 酒店星级
     */
    protected String hotelStar;

    /**
     * 酒店中文地址
     */
     protected String chnAddress;
    /**
     * 酒店英文地址
     */
     protected String engAddress;
    /**
     * 酒店介绍
     */
    protected String hotelIntroduce;
    /**
     * 酒店自动生成简介信息
     */
    protected String autoIntroduce;
    /**
     * 商业区
     */
    protected String bizZone;
    
    /**
     * 商业区中文值
     */
    protected String bizZoneValue;
    
    /**
     * 城区
     */
    protected String district;

    /**
     * 酒店推荐级别
     */
    protected String commendType;

    /**
     * 酒店所在城市代码
     */
    protected String city;
    
    /**
     * 酒店所在城市名称
     */
    protected String cityName;

    /**
     * 酒店LOGO照片
     */
    protected String hotelLogo;

	 /**
	  * 酒店电话
	  */
	 protected String telephone;
	 
	 /**
	  * 酒店网站url
	  */
	 protected String webSiteURL;
  /**
	 * 酒店层高(跟数据库类型一致)
	 */
	protected String layerHigh;
    
	/**
	 * 房间总数
	 */
	protected String roomAmount;
	
	/**
	 * 所属集团
	 */
    protected String parentHotelGroup;
    
    /**
     * 所属品牌名称
     */
    protected String plateName;
         
    /**
     * 排名
     */
    protected Integer sort;
    
    /**
     * 最低价
     */
    protected String lowestPrice;
    
	/**
	 * 酒店可用信用卡
	 */
	protected String creditCard;
		
    //---------------------(特色设施)对应图片的是否显亮 start------------------------
	/**
     * 酒店是否有接机服务
     */
    protected boolean forPlane;

    /**
     * 酒店是否有免费停车场
     */
    protected boolean forFreeStop;

    /**
     * 酒店是否有免费游泳池
     */
    protected boolean forFreePool;

    /**
     * 酒店是否有免费健身设施
     */
    protected boolean forFreeGym;

    /**
     * 酒店是否有某个房型有宽带
     */
    protected boolean forNetBand;
    
    //---------------------(特色设施)对应图片的是否显亮 end------------------------

	/**
     * 开业日期
     */
	protected String praciceDate;

    /**
     * 装修日期
     */
	protected String fitmentDate;

    /**
     * 酒店交通信息
     */
    protected String trafficInfo;

    /**
     * 酒店的会员综合评分
     */
    protected String generalPoint ;
    /**
     * 增加该酒店共多少点评
     */
    protected String commonCount;
    
    /**
     * 会员评论
     */
    protected CommentSummaryVO commentSummaryVO;
    
    /**
     * 酒店经度
     */
    protected String longitude;
    
    /**
     * 酒店纬度
     */
    protected String latitude;
    
    

    /**
     * (渠道号)直连酒店
     */
    protected String cooperateChannel;

    /**
     * 无烟楼层
     */
    protected String noSmokingFloor;
    
    /**
     * 酒店餐饮休闲设施
     */
    protected String mealFixtrue;
    
    /**
     * 酒店客房设施
     */
    protected String roomFixtrue;
    
    /**
     * 酒店免费服务
     */
    protected String freeService;
    
    /**
     * 酒店残疾人设施
     */
    protected String handicappedFixtrue;
   
    /**
     * 图片集
     */
    protected List<HotelPictureVO> picLst = new ArrayList<HotelPictureVO>();
    
    /**
     * 展示的图片
     */
    protected String outPictureName;
    
    /**
	 * 芒果促销列表
	 */
	protected List<PreSaleVO> preSaleLst;
 
    /**
     * 房型列表，是RoomType类的集合
     */
    protected List<RoomTypeVO> roomTypes = new ArrayList<RoomTypeVO>();
    
    //地理信息
    protected String geoDistance;
        
    //促销类型，醒狮计划所增加
	protected int promoteType;

	public int getPromoteType() {
		return promoteType;
	}

	public void setPromoteType(int promoteType) {
		this.promoteType = promoteType;
	}
    
    /**
     * 是否是预付酒店
     * add by ting.li
     */
    protected boolean prepayHotel;

	public long getHotelId() {
		return hotelId;
	}

	public void setHotelId(long hotelId) {
		this.hotelId = hotelId;
	}

	public String getChnName() {
		return chnName;
	}

	public void setChnName(String chnName) {
		this.chnName = chnName;
	}

	public String getEngName() {
		return engName;
	}

	public void setEngName(String engName) {
		this.engName = engName;
	}

	public String getHotelStar() {
		return hotelStar;
	}

	public void setHotelStar(String hotelStar) {
		this.hotelStar = hotelStar;
	}

	public String getChnAddress() {
		return chnAddress;
	}

	public void setChnAddress(String chnAddress) {
		this.chnAddress = chnAddress;
	}

	public String getEngAddress() {
		return engAddress;
	}

	public void setEngAddress(String engAddress) {
		this.engAddress = engAddress;
	}

	public String getHotelIntroduce() {
		return hotelIntroduce;
	}

	public void setHotelIntroduce(String hotelIntroduce) {
		this.hotelIntroduce = hotelIntroduce;
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

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getCommendType() {
		return commendType;
	}

	public void setCommendType(String commendType) {
		this.commendType = commendType;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getHotelLogo() {
		return hotelLogo;
	}

	public void setHotelLogo(String hotelLogo) {
		this.hotelLogo = hotelLogo;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getWebSiteURL() {
		return webSiteURL;
	}

	public void setWebSiteURL(String webSiteURL) {
		this.webSiteURL = webSiteURL;
	}

	public String getLayerHigh() {
		return layerHigh;
	}

	public void setLayerHigh(String layerHigh) {
		this.layerHigh = layerHigh;
	}

	public String getRoomAmount() {
		return roomAmount;
	}

	public void setRoomAmount(String roomAmount) {
		this.roomAmount = roomAmount;
	}

	public String getParentHotelGroup() {
		return parentHotelGroup;
	}

	public void setParentHotelGroup(String parentHotelGroup) {
		this.parentHotelGroup = parentHotelGroup;
	}

	public String getPlateName() {
		return plateName;
	}

	public void setPlateName(String plateName) {
		this.plateName = plateName;
	}

	public String getLowestPrice() {
		return lowestPrice;
	}

	public void setLowestPrice(String lowestPrice) {
		this.lowestPrice = lowestPrice;
	}

	public String getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}

	public boolean isForPlane() {
		return forPlane;
	}

	public void setForPlane(boolean forPlane) {
		this.forPlane = forPlane;
	}

	public boolean isForFreeStop() {
		return forFreeStop;
	}

	public void setForFreeStop(boolean forFreeStop) {
		this.forFreeStop = forFreeStop;
	}

	public boolean isForFreePool() {
		return forFreePool;
	}

	public void setForFreePool(boolean forFreePool) {
		this.forFreePool = forFreePool;
	}

	public boolean isForFreeGym() {
		return forFreeGym;
	}

	public void setForFreeGym(boolean forFreeGym) {
		this.forFreeGym = forFreeGym;
	}

	public boolean isForNetBand() {
		return forNetBand;
	}

	public void setForNetBand(boolean forNetBand) {
		this.forNetBand = forNetBand;
	}

	public String getPraciceDate() {
		return praciceDate;
	}

	public void setPraciceDate(String praciceDate) {
		this.praciceDate = praciceDate;
	}

	public String getFitmentDate() {
		return fitmentDate;
	}

	public void setFitmentDate(String fitmentDate) {
		this.fitmentDate = fitmentDate;
	}

	public String getTrafficInfo() {
		return trafficInfo;
	}

	public void setTrafficInfo(String trafficInfo) {
		this.trafficInfo = trafficInfo;
	}

	public String getGeneralPoint() {
		return generalPoint;
	}

	public void setGeneralPoint(String generalPoint) {
		this.generalPoint = generalPoint;
	}

	public String getCommonCount() {
		return commonCount;
	}

	public void setCommonCount(String commonCount) {
		this.commonCount = commonCount;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getCooperateChannel() {
		return cooperateChannel;
	}

	public void setCooperateChannel(String cooperateChannel) {
		this.cooperateChannel = cooperateChannel;
	}

	public String getNoSmokingFloor() {
		return noSmokingFloor;
	}

	public void setNoSmokingFloor(String noSmokingFloor) {
		this.noSmokingFloor = noSmokingFloor;
	}

	public String getMealFixtrue() {
		return mealFixtrue;
	}

	public void setMealFixtrue(String mealFixtrue) {
		this.mealFixtrue = mealFixtrue;
	}

	public String getRoomFixtrue() {
		return roomFixtrue;
	}

	public void setRoomFixtrue(String roomFixtrue) {
		this.roomFixtrue = roomFixtrue;
	}

	public String getFreeService() {
		return freeService;
	}

	public void setFreeService(String freeService) {
		this.freeService = freeService;
	}

	public String getHandicappedFixtrue() {
		return handicappedFixtrue;
	}

	public void setHandicappedFixtrue(String handicappedFixtrue) {
		this.handicappedFixtrue = handicappedFixtrue;
	}

	public List<RoomTypeVO> getRoomTypes() {
		return roomTypes;
	}

	public void setRoomTypes(List<RoomTypeVO> roomTypes) {
		this.roomTypes = roomTypes;
	}

	public String getBizZoneValue() {
		return bizZoneValue;
	}

	public void setBizZoneValue(String bizZoneValue) {
		this.bizZoneValue = bizZoneValue;
	}

	public String getOutPictureName() {
		return outPictureName;
	}

	public void setOutPictureName(String outPictureName) {
		this.outPictureName = outPictureName;
	}

	public List<PreSaleVO> getPreSaleLst() {
		if(null == preSaleLst) {
			preSaleLst = new ArrayList<PreSaleVO>(2);
		}
		return preSaleLst;
	}

	public void setPreSaleLst(List<PreSaleVO> preSaleLst) {
		this.preSaleLst = preSaleLst;
	}

	public CommentSummaryVO getCommentSummaryVO() {
		return commentSummaryVO == null ? new CommentSummaryVO() : commentSummaryVO;
	}

	public void setCommentSummaryVO(CommentSummaryVO commentSummaryVO) {
		this.commentSummaryVO = commentSummaryVO;
	}

	public String getGeoDistance() {
		return geoDistance;
	}

	public void setGeoDistance(String geoDistance) {
		this.geoDistance = geoDistance;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public boolean isPrepayHotel() {
		return prepayHotel;
	}

	public void setPrepayHotel(boolean prepayHotel) {
		this.prepayHotel = prepayHotel;
	}


}

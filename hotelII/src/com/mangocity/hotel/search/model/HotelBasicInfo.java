package com.mangocity.hotel.search.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.mangocity.hotel.base.persistence.HtlGeoDistance;
import com.mangocity.hotel.base.persistence.HtlPicture;
import com.mangocity.hotel.dreamweb.comment.model.CommentStatistics;
import com.mangocity.hotel.dreamweb.displayvo.HtlAlbumVO;

public class HotelBasicInfo {
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
	 * 生成简介
	 */
	protected String autoIntroduce;
	/**
	 * 酒店电话
	 */
	protected String telephone;
	/**
	 * 酒店网站
	 */
	protected String webSiteURL;
	/**
	 * 酒店星级
	 */
	protected String hotelStar;
	/**
	 * 主推类型(htl_comm_list中的commendType的值)
	 */
	protected String commendType;
	/**
	 * 酒店层高
	 */
	protected String layerHigh;
	/**
	 * 房间总数
	 */
	protected short layerCount;
	/**
	 * 开业日期
	 */
	protected String praciceDate;
	/**
	 * 装修日期
	 */
	protected String fitmentDate;
	
	/**
	 * 装修程度
	 */
	private String fitmentDegree;
	
	/**
	 * 所属集团
	 */
    protected String parentHotelGroup;
    
    /**
     * 所属品牌名称
     */
    protected String plateName;
	/**
	 * 城市三字码
	 */
	protected String cityId;
	/**
	 * 城市名称
	 */
	protected String cityName;
	/**
	 * 所属城区
	 */
	protected String zone;
	/**
	 *  所属城区的中文名称
	 */
	protected String zoneName;
	/**
	 * 所属商业区
	 */
	protected String bizZone;
	
	/**
	 * 所属商业区的中文名称
	 */
	protected String bizChnName;

	/**
	 * 酒店合同币种
	 */
	protected String contractCurrency;
	/**
	 * 酒店可用信用卡
	 */
	protected String creditCard;
	
	/**
	 *  酒店类型
	 */
	private String hotelType;
	
	/**
	 * 首页推荐
	 */
	private String firstPageRecommend;
	
	/**
	 * 主题
	 */
	private String theme;
	
	private int gisId;
	
    /**
     * 酒店所在地图经度
     */
    protected String longitude;
    
    /**
     * 酒店所在地图纬度
     */
    protected  String latitude;
    
    private boolean isAllNoSmoking = false;
    
    /**
     * 酒店直联信息，0表示本部，其他值表示是直联酒店
     */
    protected String cooperateChannel;

    /**
     * 中旅酒店标识
     */
    protected boolean flagCtsHK = false;

    /**
     * 无烟楼层
     */
    protected String noSmokingFloor;
    
	/**
	 * 免费服务
	 */
	protected String freeService;
	
	private String webPrepayShow;
	
	private String webShowBaseInfo;
	
	/**
	 * 餐饮休闲设施
	 */
	protected String mealAndFixture;
	
	/**
	 * 周围景点
	 */
	private String aroundView;
	
	private String alertMessage;
	
	/**
	 * 客房设施
	 */
	protected String roomFixtrue;
	/**
	 * 酒店残疾人设施
	 */
	protected String handicappedFixtrue;
	
	/**
	 * 排名顺序
	 */
	protected int sorts;
	/**
	 * 大礼包
	 */
	protected List<SalePromotionMango> listPromotionMango;
	
	/**
	 * 房型
	 */
	protected List<RoomType> listRoomType;
	
	/**
	 * 酒店地理信息
	 */
	private HtlGeoDistance htlGeoDistance;
	
	/**
	 * 酒店展示的图片名称 
	 */
    protected String outPictureName;
    
    /**
     * 酒店规定入住时间
     */
    protected String checkInTime;
    
    /**
     * 酒店规定退房时间
     */
    protected String checkOutTime;
        
    
    /**
     * 酒店交通信息
     */
    protected String trafficInfo;
    /**
     * 图片集（详情页展示用，不从luncene 带出来）
     */
    private List<HtlPicture> hotelPictureList=new ArrayList();
    
    /**
     * 详情页新图片库 
     */
    private List<HtlAlbumVO> htlAlbumVOList = new ArrayList<HtlAlbumVO>();
    /**
     * 360图片（详情页展示用，不从luncene 带出来）
     */
    private List hotel360PictureList=new ArrayList();
    /**
     * 商业区与行政区信息（详情页展示用，不从luncene带出来）
     */
    private Map cityBizZoneMap=new HashMap();
    /**
     * 酒店评论信息（展示用，不从luncene带出来）
     */
   // private CommentSummary comment;
    private CommentStatistics comment;
    //标记是否是预付酒店
    private boolean prepayHotel;
    
    protected List<TrafficInfo> listTraffic = Collections.emptyList();
    
    //促销类型，醒狮计划所增加
	protected int promoteType;
	
	/**
	 * 城市名称是否展示
	 */
	protected boolean cityShowFlag;
	
	/**
	 * 城区名称是否展示
	 */
	protected boolean zooeShowFlag;

	public int getPromoteType() {
		return promoteType;
	}

	public void setPromoteType(int promoteType) {
		this.promoteType = promoteType;
	}

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

	public String getHotelStar() {
		return hotelStar;
	}

	public void setHotelStar(String hotelStar) {
		this.hotelStar = hotelStar;
	}

	public String getCommendType() {
		return commendType;
	}

	public void setCommendType(String commendType) {
		this.commendType = commendType;
	}

	public String getLayerHigh() {
		return layerHigh;
	}

	public void setLayerHigh(String layerHigh) {
		this.layerHigh = layerHigh;
	}

	public short getLayerCount() {
		return layerCount;
	}

	public void setLayerCount(short layerCount) {
		this.layerCount = layerCount;
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

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getBizZone() {
		return bizZone;
	}

	public void setBizZone(String bizZone) {
		this.bizZone = bizZone;
	}

	public String getContractCurrency() {
		return contractCurrency;
	}

	public void setContractCurrency(String contractCurrency) {
		this.contractCurrency = contractCurrency;
	}

	public String getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}

	public String getFreeService() {
		return freeService;
	}

	public void setFreeService(String freeService) {
		this.freeService = freeService;
	}

	public String getMealAndFixture() {
		return mealAndFixture;
	}

	public void setMealAndFixture(String mealAndFixture) {
		this.mealAndFixture = mealAndFixture;
	}

	public String getRoomFixtrue() {
		return roomFixtrue;
	}

	public void setRoomFixtrue(String roomFixtrue) {
		this.roomFixtrue = roomFixtrue;
	}

	public String getHandicappedFixtrue() {
		return handicappedFixtrue;
	}

	public void setHandicappedFixtrue(String handicappedFixtrue) {
		this.handicappedFixtrue = handicappedFixtrue;
	}


	public List<RoomType> getListRoomType() {
		return listRoomType;
	}

	public void setListRoomType(List<RoomType> listRoomType) {
		this.listRoomType = listRoomType;
	}

	public List<SalePromotionMango> getListPromotionMango() {
		return listPromotionMango;
	}

	public void setListPromotionMango(List<SalePromotionMango> listPromotionMango) {
		this.listPromotionMango = listPromotionMango;
	}

	public String getAutoIntroduce() {
		return autoIntroduce;
	}

	public void setAutoIntroduce(String autoIntroduce) {
		this.autoIntroduce = autoIntroduce;
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

	public boolean isFlagCtsHK() {
		return flagCtsHK;
	}

	public void setFlagCtsHK(boolean flagCtsHK) {
		this.flagCtsHK = flagCtsHK;
	}

	public String getNoSmokingFloor() {
		return noSmokingFloor;
	}

	public void setNoSmokingFloor(String noSmokingFloor) {
		this.noSmokingFloor = noSmokingFloor;
	}

	public int getSorts() {
		return sorts;
	}

	public void setSorts(int sorts) {
		this.sorts = sorts;
	}

	public String getFitmentDegree() {
		return fitmentDegree;
	}

	public void setFitmentDegree(String fitmentDegree) {
		this.fitmentDegree = fitmentDegree;
	}

	public String getHotelType() {
		return hotelType;
	}

	public void setHotelType(String hotelType) {
		this.hotelType = hotelType;
	}

	public String getAroundView() {
		return aroundView;
	}

	public void setAroundView(String aroundView) {
		this.aroundView = aroundView;
	}

	public String getAlertMessage() {
		return alertMessage;
	}

	public void setAlertMessage(String alertMessage) {
		this.alertMessage = alertMessage;
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

	public String getFirstPageRecommend() {
		return firstPageRecommend;
	}

	public void setFirstPageRecommend(String firstPageRecommend) {
		this.firstPageRecommend = firstPageRecommend;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public int getGisId() {
		return gisId;
	}

	public void setGisId(int gisId) {
		this.gisId = gisId;
	}

	public boolean isAllNoSmoking() {
		return isAllNoSmoking;
	}

	public void setAllNoSmoking(boolean isAllNoSmoking) {
		this.isAllNoSmoking = isAllNoSmoking;
	}

	public HtlGeoDistance getHtlGeoDistance() {
		return htlGeoDistance;
	}

	public void setHtlGeoDistance(HtlGeoDistance htlGeoDistance) {
		this.htlGeoDistance = htlGeoDistance;
	}

	public String getOutPictureName() {
		return outPictureName;
	}

	public void setOutPictureName(String outPictureName) {
		this.outPictureName = outPictureName;
	}

	public String getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(String checkInTime) {
		this.checkInTime = checkInTime;
	}

	public String getCheckOutTime() {
		return checkOutTime;
	}

	public void setCheckOutTime(String checkOutTime) {
		this.checkOutTime = checkOutTime;
	}

	public String getTrafficInfo() {
		return trafficInfo;
	}

	public void setTrafficInfo(String trafficInfo) {
		this.trafficInfo = trafficInfo;
	}

	public List<TrafficInfo> getListTraffic() {
		return listTraffic;
	}

	public void setListTraffic(List<TrafficInfo> listTraffic) {
		this.listTraffic = listTraffic;
	}	
	
	public List<HtlPicture> getHotelPictureList() {
		return hotelPictureList;
	}

	public void setHotelPictureList(List<HtlPicture> hotelPictureList) {
		this.hotelPictureList = hotelPictureList;
	}

	public List getHotel360PictureList() {
		return hotel360PictureList;
	}

	public void setHotel360PictureList(List hotel360PictureList) {
		this.hotel360PictureList = hotel360PictureList;
	}
	

	public Map getCityBizZoneMap() {
		return cityBizZoneMap;
	}

	public void setCityBizZoneMap(Map cityBizZoneMap) {
		this.cityBizZoneMap = cityBizZoneMap;
	}

	public void handleTrafficInfoStr(String trafficInfo) {
		if(null == trafficInfo || 0 == trafficInfo.length()) {
			return;
		}
		String[] trafficDetails = trafficInfo.split("#");
		List<TrafficInfo> listTraffic = new ArrayList<TrafficInfo>(2);
		this.setListTraffic(listTraffic);
		for(String trafficDetail : trafficDetails) {
			String[] fields = trafficDetail.split("!");
			if(2 > fields.length) {
				continue;
			}
			TrafficInfo info = new TrafficInfo();
			info.setAddress(fields[0]);
			info.setDistance(fields[1]);
			listTraffic.add(info);
		}
	}

	/**
	 * 
	 * 是否有接机
	 * 
	 * 免费服务各项在数据库中用数字表示，其中13表示有接机
	 * 餐饮设施各项在数据库中用数字表示，其中43表示有接机
	 * 
	 * @return
	 */
	public boolean isForPlane() {
		return (null != freeService && 0 <= freeService.indexOf("13,"))
				|| (null != mealAndFixture && 0 <= mealAndFixture.indexOf("43,"));
	}
	
	/**
	 * 
	 * 是否有免费接机
	 * 
	 * 免费服务各项在数据库中用数字表示，其中13表示有免费接机
	 * 
	 * @return
	 */
	public boolean isForFreePlane() {
		return (null != freeService && 0 <= freeService.indexOf("13,"));
	}
	
	/**
	 * 
	 * 是否有免费停车场
	 * 
	 * 免费服务各项在数据库中用数字表示，其中4表示有免费停车场
	 * 
	 * @return
	 */
	public boolean isForFreeStop() {
		return (null != freeService && (0 < freeService.indexOf(",4,") || 0 == freeService.indexOf("4,")));
	}
	
	/**
	 * 
	 * 是否有免费游泳池
	 * 
	 * 免费服务各项在数据库中用数字表示，其中2表示有免费游泳池
	 * 
	 * @return
	 */
	public boolean isForFreePool() {
		return (null != freeService && (0 < freeService.indexOf(",2,") || 0 == freeService.indexOf("2,")));
	}
	
	/**
	 * 
	 * 是否有免费健身设施
	 * 
	 * 免费服务各项在数据库中用数字表示，其中1表示有免费健身设施
	 * 
	 * @return
	 */
	public boolean isForFreeGym() {
		return (null != freeService && (0 < freeService.indexOf(",1,") || 0 == freeService.indexOf("1,")));
	}
	
	/**
	 * 
	 * 是否有宽带服务
	 * 
	 * @return	 
	 */
	public boolean isForNetBand() {
		if(null != mealAndFixture && 0 <= mealAndFixture.indexOf("21,")) {
			return true;
		}
		
		for(RoomType roomType : this.getListRoomType()) {
			String equipment = roomType.getRoomEquipment();
			if(null != equipment && 0 <= equipment.indexOf("21,")) {
				return true;
			}
		}
		
		return false;
	}

	public String getBizChnName() {
		return bizChnName;
	}

	public void setBizChnName(String bizChnName) {
		this.bizChnName = bizChnName;
	}
/*
	public CommentSummary getComment() {
		return comment;
	}

	public void setComment(CommentSummary comment) {
		this.comment = comment;
	}
*/
	
	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public CommentStatistics getComment() {
		return comment;
	}

	public void setComment(CommentStatistics comment) {
		this.comment = comment;
	}

	public boolean isPrepayHotel() {
		return prepayHotel;
	}

	public void setPrepayHotel(boolean prepayHotel) {
		this.prepayHotel = prepayHotel;
	}

	public List<HtlAlbumVO> getHtlAlbumVOList() {
		return htlAlbumVOList;
	}

	public void setHtlAlbumVOList(List<HtlAlbumVO> htlAlbumVOList) {
		this.htlAlbumVOList = htlAlbumVOList;
	}

	public boolean isCityShowFlag() {
		return cityShowFlag;
	}

	public void setCityShowFlag(boolean cityShowFlag) {
		this.cityShowFlag = cityShowFlag;
	}

	public boolean isZooeShowFlag() {
		return zooeShowFlag;
	}

	public void setZooeShowFlag(boolean zooeShowFlag) {
		this.zooeShowFlag = zooeShowFlag;
	}
	
	

}

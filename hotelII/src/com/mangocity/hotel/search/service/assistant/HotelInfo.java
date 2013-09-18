package com.mangocity.hotel.search.service.assistant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlGeoDistance;
import com.mangocity.hotel.dreamweb.comment.model.CommentStatistics;
import com.mangocity.hotel.search.model.TrafficInfo;

public class HotelInfo {
	/**
	 * 酒店Id
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
     * 酒店生成简介
     */
    private String autoIntroduce;
	
	 /**
	  * 酒店电话
	  */
	 protected String telephone;
	 
	 /**
	  * 酒店网站url
	  */
	 protected String webSiteURL;
	
	/**
	 * 酒店星级
	 */
	protected String hotelStar;
	

	
	/**
	 * 主推类型(htl_comm_list)
	 */
    protected String commendType;
	
    /**
	 * 酒店层高(跟数据库类型一致)
	 */
	protected String layerHigh;
    
	/**
	 * 房间总数
	 */
	protected long roomAmount;
	
	/**
     * 开业日期
     */
	protected String praciceDate;

    /**
     * 装修日期
     */
	protected String fitmentDate;
	
	/**
	 * 所属集团
	 */
    protected String parentHotelGroup;
    
	/**
	 * 城市三字码
	 */
	protected String cityCode;
	/**
	 * 城市名称
	 */
	protected String cityName;
    
    /**
     * 所属品牌名称
     */
    protected String plateName;
	
    /**
	 * 所属城区
	 */
	 protected String zone;
	 
	 /**
	  * 所属商业区
	  */
	 protected String bizZone;
	 
	 /**
	 * 所属商业区中文名称
	 */
	protected String bizChnName;
	
	 /**
	  * 交通信息
	  */
	protected String trafficInfo;
	
	/**
	 * 酒店合同币种
	 */
    private String contractCurrency;
 /*   
     * 兑换汇率
     
    private String rateValue;*/

    /**
     * 排名
     */
    private int sort;
    
    /**
     * 最低价
     */
    protected double lowestPrice;
    
	/**
	 * 酒店可用信用卡
	 */
	protected String creditCard;
	
	/**
	 * 入住日期
	 */
	protected String checkInDate;
	
	/**
	 * 退房日期
	 */
	protected String checkOutDate;
	
	/**
	 * 能否预订
	 */
	protected boolean canbook = true;
  
    /***************地图相关信息 start *****************/
    
    /**
     * 酒店所在地图经度
     */
    protected String longitude;
    
    /**
     * 酒店所在地图纬度
     */
    protected  String latitude;
    

    
    /***************地图相关信息 end   *****************/
    /**
     * 酒店直联信息，0表示本部，其他值表示是直联酒店
     */
    protected String cooperateChannel;

    /**
     * 中旅酒店标识
     */
    protected boolean flagCtsHK = false;

    /**
     * 是否停止销售, true:停止, false:不停止
     */
    //protected boolean flagStopSell = false;
    
     
    /**
     * 停止销售原因
     * 
     */
    protected String stopSellNote;
        
     
    /**
     * 无烟楼层
     */
    protected String noSmokingFloor;
    
    /**
     * 酒店餐饮休闲设施
     */
    private String mealFixtrue;
    
    /**
     * 酒店客房设施
     */
    private String roomFixtrue;
    
    /**
     * 酒店免费服务
     */
    private String freeService;
    
    /**
     * 酒店残疾人设施
     */
    private String handicappedFixtrue;
	
	
	private String praciceAndFitmentStr;    
	
	/**
	 * 外网是否显示预付价
	 */
	private boolean webPrepayShow;
    
    protected List<HotelPicture> picLst;
    //酒店展示的图片名称小图
    protected String outPictureName;
    
    /**
     * 大图
     */
    private String outBigPictureName;
   
    /**
	 * 芒果促销列表
	 */
	protected List<PreSale> preSaleLst;
	
    /**
     * 房型列表，是RoomType类的集合
     * Long 房型ID
     * 
     */
	protected Map<Long,RoomTypeInfo> mapRoomTypeInfo;
	
    /**
     * 酒店交通信息
     */
    protected List<TrafficInfo> listTraffic = Collections.emptyList();
    
    /**
     * 酒店评论信息（展示用，不从luncene带出来）
     */
    private CommentStatistics comment;
    
    //地理信息
    protected HtlGeoDistance htlGeoDistance;
    
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
     */
    protected boolean prepayHotel;
    
    
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
				|| (null != mealFixtrue && 0 <= mealFixtrue.indexOf("43,"));
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

	

	public String getTrafficInfo() {
		return trafficInfo;
	}

	public void setTrafficInfo(String trafficInfo) {
		this.trafficInfo = trafficInfo;
	}

	public String getContractCurrency() {
		return contractCurrency;
	}

	public void setContractCurrency(String contractCurrency) {
		this.contractCurrency = contractCurrency;
	}

	public double getLowestPrice() {
		return lowestPrice;
	}

	public void setLowestPrice(double lowestPrice) {
		this.lowestPrice = lowestPrice;
	}

	public String getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
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


	public List<PreSale> getPreSaleLst() {
		if(null == preSaleLst) {
			preSaleLst = new ArrayList<PreSale>(2);
		}
		return preSaleLst;
	}

	public void setPreSaleLst(List<PreSale> preSaleLst) {
		this.preSaleLst = preSaleLst;
	}



	public long getRoomAmount() {
		return roomAmount;
	}

	public void setRoomAmount(long roomAmount) {
		this.roomAmount = roomAmount;
	}

	

	public String getAutoIntroduce() {
		return autoIntroduce;
	}

	public void setAutoIntroduce(String autoIntroduce) {
		this.autoIntroduce = autoIntroduce;
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

	public List<HotelPicture> getPicLst() {
		if(picLst == null) {
			picLst = new ArrayList<HotelPicture>();
		}
		return picLst;
	}

	public void setPicLst(List<HotelPicture> picLst) {
		this.picLst = picLst;
	}

	


	public Map<Long, RoomTypeInfo> getMapRoomTypeInfo() {
		if (null == mapRoomTypeInfo) {
			mapRoomTypeInfo = new HashMap<Long, RoomTypeInfo>();
		}
		return mapRoomTypeInfo;
	}

	public void setMapRoomTypeInfo(Map<Long, RoomTypeInfo> mapRoomTypeInfo) {
		this.mapRoomTypeInfo = mapRoomTypeInfo;
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

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getStopSellNote() {
		return stopSellNote;
	}

	public void setStopSellNote(String stopSellNote) {
		this.stopSellNote = stopSellNote;
	}

	public boolean isCanbook() {
		return canbook;
	}

	public void setCanbook(boolean canbook) {
		this.canbook = canbook;
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

	public String getOutPictureName() {
		return outPictureName;
	}

	public void setOutPictureName(String outPictureName) {
		this.outPictureName = outPictureName;
	}

	public List<TrafficInfo> getListTraffic() {
		return listTraffic;
	}

	public void setListTraffic(List<TrafficInfo> listTraffic) {
		this.listTraffic = listTraffic;
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

	public String getBizChnName() {
		return bizChnName;
	}

	public void setBizChnName(String bizChnName) {
		this.bizChnName = bizChnName;
	}

	public boolean isWebPrepayShow() {
		return webPrepayShow;
	}

	public void setWebPrepayShow(boolean webPrepayShow) {
		this.webPrepayShow = webPrepayShow;
	}


	
	public HtlGeoDistance getHtlGeoDistance() {
		return htlGeoDistance;
	}

	public void setHtlGeoDistance(HtlGeoDistance htlGeoDistance) {
		this.htlGeoDistance = htlGeoDistance;
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

	public String getOutBigPictureName() {
		return outBigPictureName;
	}

	public void setOutBigPictureName(String outBigPictureName) {
		this.outBigPictureName = outBigPictureName;
	}

	
}

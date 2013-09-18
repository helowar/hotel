package com.mangocity.hotel.search.service.assistant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.search.model.AddBed;
import com.mangocity.hotel.search.model.AddBreakfast;

public class RoomTypeInfo {

	/**
	 * 房型Id
	 */
	private long roomTypeId;
	
	/**
	 * 房型名称
	 */
	private String roomtypeName;
	
    /**
     * 推荐级别
     */
    private String recommend;
	
    /**
	 * 房间数量
	 */
	private String roomNumber;
	/**
	 * 房间面积(单位为平方米)
	 */
	private String roomAcreage;
	
	/**
	 * 房型所在楼层
	 */
	private String roomFloor;
	
	/**
	 * 最多入住人数
	 */
	private int maxNumOfPersons;

	/**
	 * 房间设施（包括了宽带，加床）
	 */
	private String roomEquipment ="";

	/**
	 * 是否含宽带
	 */
	private boolean hasNet;
	
	/**
	 * 是否有加床
	 */
	private boolean flagAddBed;
	
	/**
	 * 加床数量
	 */
	private int addBedNum;
	
	/**
	 * 是否是中旅房型
	 */
	private boolean flagCtsHK;

	/**
	 *直联方式 
	 */
	private String hdltype;
	

	/**
	 * 是否基础房型
	 */
	
	private boolean baseRoom;
	
	/**
	 * 能否预订
	 */
	private boolean canbook;
	
	/**
	 * 不能预订原因
	 */
	private String cantBookReason;
	
	/**
	 * 最低价
	 */
	private double minPrice;
	
	/**
	 * 排名
	 */
	private int sortorder;
	/**
	 * 加床情况
	 */
	private List<AddBed> bedLst;
	
	/**
	 * 加早情况
	 */
	private List<AddBreakfast> breakfastLst;
	
	/**
	 * 价格类型，未来对于商品
	 * Long 商品ID,价格类型ID+"_"+支付方式
	 */
	private Map<String,Commodity> mapCommodityInfo;
	

	public long getRoomTypeId() {
		return roomTypeId;
	}

	public void setRoomTypeId(long roomTypeId) {
		this.roomTypeId = roomTypeId;
	}




	public String getRoomAcreage() {
		return roomAcreage;
	}

	public void setRoomAcreage(String roomAcreage) {
		this.roomAcreage = roomAcreage;
	}

	public String getRoomFloor() {
		return roomFloor;
	}

	public void setRoomFloor(String roomFloor) {
		this.roomFloor = roomFloor;
	}

	public int getMaxNumOfPersons() {
		return maxNumOfPersons;
	}

	public void setMaxNumOfPersons(int maxNumOfPersons) {
		this.maxNumOfPersons = maxNumOfPersons;
	}

	public String getRoomEquipment() {
		return roomEquipment;
	}

	public void setRoomEquipment(String roomEquipment) {
		this.roomEquipment = roomEquipment;
	}



	public boolean isFlagAddBed() {
		return flagAddBed;
	}

	public void setFlagAddBed(boolean flagAddBed) {
		this.flagAddBed = flagAddBed;
	}

	public int getAddBedNum() {
		return addBedNum;
	}

	public void setAddBedNum(int addBedNum) {
		this.addBedNum = addBedNum;
	}

	public boolean isFlagCtsHK() {
		return flagCtsHK;
	}

	public void setFlagCtsHK(boolean flagCtsHK) {
		this.flagCtsHK = flagCtsHK;
	}

	
	public String getRoomtypeName() {
		return roomtypeName;
	}

	public void setRoomtypeName(String roomtypeName) {
		this.roomtypeName = roomtypeName;
	}

	public String getRecommend() {
		return recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}




	public String getHdltype() {
		return hdltype;
	}

	public void setHdltype(String hdltype) {
		this.hdltype = hdltype;
	}

	public Map<String, Commodity> getMapCommodityInfo() {
		if (null == mapCommodityInfo) {
			mapCommodityInfo = new HashMap<String, Commodity>();
		}
		return mapCommodityInfo;
	}

	public void setMapCommodityInfo(Map<String, Commodity> mapCommodityInfo) {
		this.mapCommodityInfo = mapCommodityInfo;
	}

	public boolean isBaseRoom() {
		return baseRoom;
	}

	public void setBaseRoom(boolean baseRoom) {
		this.baseRoom = baseRoom;
	}

	public boolean isCanbook() {
		return canbook;
	}

	public void setCanbook(boolean canbook) {
		this.canbook = canbook;
	}


	public String getCantBookReason() {
		return cantBookReason;
	}

	public void setCantBookReason(String cantBookReason) {
		this.cantBookReason = cantBookReason;
	}

	public double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(double minPrice) {
		this.minPrice = minPrice;
	}

	public List<AddBed> getBedLst() {
		if(null == bedLst) {
			bedLst = new ArrayList<AddBed>();
		}
		return bedLst;
	}

	public void setBedLst(List<AddBed> bedLst) {
		this.bedLst = bedLst;
	}

	public List<AddBreakfast> getBreakfastLst() {
		if (null == breakfastLst) {
			breakfastLst = new ArrayList<AddBreakfast>();
		}
		return breakfastLst;
	}

	public void setBreakfastLst(List<AddBreakfast> breakfastLst) {
		this.breakfastLst = breakfastLst;
	}

	public int getSortorder() {
		return sortorder;
	}

	public void setSortorder(int sortorder) {
		this.sortorder = sortorder;
	}

	public boolean isHasNet() {
		return hasNet;
	}

	public void setHasNet(boolean hasNet) {
		this.hasNet = hasNet;
	}


}

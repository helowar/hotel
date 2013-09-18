package com.mangocity.hotel.search.vo;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.common.vo.AddBedVO;
import com.mangocity.hotel.common.vo.AddBreakfastVO;
import com.mangocity.hotel.search.sort.SortedRoomTypeInfo;






/**
 * 酒店网站v2.2查询结果<br>
 * 房型类<br>
 * 
 * @author zengyong
 * 
 */
public class RoomTypeVO extends SortedRoomTypeInfo implements SerializableVO{
	
	public RoomTypeVO(){}
	
	private static final long serialVersionUID = 1443786632749256672L;

	/**
	 * 房型Id
	 */
	private long roomTypeId;
	
	/**
	 * 房型名称
	 */
	private String roomtypeName;
		
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
	private String maxNumOfPersons;

	/**
	 * 房间设施（包括了宽带，加床）
	 */
	private String roomEquipment;
	
	/**
	 * 是否有加床（有，无）
	 */
	private String flagAddBed;
	
	/**
	 * 加床数量
	 */
	private String addBedNum;
	
	/**
	 *直联方式 
	 */
	private String hdltype;	
	
	/**
	 * 排名
	 */
	private int sortorder;
	/**
	 * 加床情况
	 */
	private List<AddBedVO> bedLst = new ArrayList<AddBedVO>();
	
	/**
	 * 加早情况
	 */
	private List<AddBreakfastVO> breakfastLst = new ArrayList<AddBreakfastVO>();
	
	
	 /**
     * 价格类型VO add by diandian.hou
     */
    private List<CommodityVO> commodities = new ArrayList<CommodityVO>();
	/**
	 * 其他说明
	 */
    private String otherExplain;

	public long getRoomTypeId() {
		return roomTypeId;
	}

	public void setRoomTypeId(long roomTypeId) {
		this.roomTypeId = roomTypeId;
	}

	public String getRoomtypeName() {
		return roomtypeName;
	}

	public void setRoomtypeName(String roomtypeName) {
		this.roomtypeName = roomtypeName;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public String getRoomAcreage() {
		return roomAcreage == null? "" : roomAcreage;
	}

	public void setRoomAcreage(String roomAcreage) {
		this.roomAcreage = roomAcreage;
	}

	public String getRoomFloor() {
		return roomFloor == null ? "" : roomFloor;
	}

	public void setRoomFloor(String roomFloor) {
		this.roomFloor = roomFloor;
	}

	public String getMaxNumOfPersons() {
		return maxNumOfPersons;
	}

	public void setMaxNumOfPersons(String maxNumOfPersons) {
		this.maxNumOfPersons = maxNumOfPersons;
	}

	public String getRoomEquipment() {
		return roomEquipment;
	}

	public void setRoomEquipment(String roomEquipment) {
		this.roomEquipment = roomEquipment;
	}

	public String getFlagAddBed() {
		return flagAddBed;
	}

	public void setFlagAddBed(String flagAddBed) {
		this.flagAddBed = flagAddBed;
	}

	public String getAddBedNum() {
		return addBedNum;
	}

	public void setAddBedNum(String addBedNum) {
		this.addBedNum = addBedNum;
	}

	public String getHdltype() {
		return hdltype;
	}

	public void setHdltype(String hdltype) {
		this.hdltype = hdltype;
	}

	public int getSortorder() {
		return sortorder;
	}

	public void setSortorder(int sortorder) {
		this.sortorder = sortorder;
	}

	public String getOtherExplain() {
		return otherExplain;
	}

	public void setOtherExplain(String otherExplain) {
		this.otherExplain = otherExplain;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public List<CommodityVO> getCommodities() {
		return commodities;
	}

	public void setCommodities(List<CommodityVO> commodities) {
		this.commodities = commodities;
	}
    
    /**
     * 价格类型VO add by diandian.hou
     */


 

}

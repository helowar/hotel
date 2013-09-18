package com.mangocity.hotel.base.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.util.FakeDeletedEntity;

/**
 * HtlRoomtype generated by MyEclipse - Hibernate Tools
 */

public class HtlRoomtype implements FakeDeletedEntity {

    // Fields

    private Long ID;

    /**
     * 酒店id
     */
    private Long hotelID;

    /**
     * 房型名称
     */
    private String roomName;

    /**
     * 房型英文名
     */
    private String roomEngName;

    /**
     * 房型归类 1 :基础房型 2：非基础
     */
    private String classify;

    /**
     * 其它设施
     */
    private String otherEquipment;

    private String roomTypeNo; // ---新增属性,房型编码

    private Integer maxPersons; // ---新增属性,最大入住人数

    private Integer roomNumber; // --新增属性,房间数量

    private Integer commendLevel; // --新增属性,推荐级别 如1,2,3等

    /**
     * 面积
     */
    private String acreage;

    /**
     * 销售渠道
     */
    private String channel;

    /**
     * 床型
     */
    private String bedType;

    private String createBy;

    private String createById;

    private String modifyBy;

    private String modifyById;

    private Date createTime;

    private Date modifyTime;

    private String active;

    /**
     * 是否合给房型
     */
    private String isAgrement;

    /**
     * 房间设施
     */
    private String roomEquipment;
    /**
     * 房间设施“其它”选项备注
     */
    private String roomEquipmentRemark;

    /**
     * 备注
     */
    private String remark;

    /**
     * 加床数量
     */
    private Integer addBedQty;
    
    private Integer isAddBed; //是否可加床( "2" 部分可加 ；"1"可加； "0"不可加) add by xuyiwen 2010-10-20

    private List lstPriceType = new ArrayList();

    /**
     * 假删除
     */
    private boolean deleted;

    /**
     * 所属楼层
     */
    private String roomFloor;

    // Constructors

    /**
     * add by shizhongwen 2009-04-17 hotel v2.8 是否港中旅房型 0:否,1:是
     */
    private String ishkroomtype;

    /**
     * 配额床型共享
     */
    private Long quotaBedShare;
    
    public Long getQuotaBedShare() {
		return quotaBedShare;
	}

	public void setQuotaBedShare(Long quotaBedShare) {
		this.quotaBedShare = quotaBedShare;
	}

	/** default constructor */
    public HtlRoomtype() {
    }

    // Property accessors

    public Long getID() {
        return this.ID;
    }

    public void setID(Long roomTypeId) {
        this.ID = roomTypeId;
    }

    public String getRoomName() {
        return this.roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomEngName() {
        return this.roomEngName;
    }

    public void setRoomEngName(String roomEngName) {
        this.roomEngName = roomEngName;
    }

    public String getAcreage() {
        return this.acreage;
    }

    public void setAcreage(String acreage) {
        this.acreage = acreage;
    }

    public String getBedType() {
        return this.bedType;
    }

    public void setBedType(String bedType) {
        this.bedType = bedType;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getModifyBy() {
        return this.modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getActive() {
        return this.active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getCommendLevel() {
        return commendLevel;
    }

    public void setCommendLevel(Integer commendLevel) {
        this.commendLevel = commendLevel;
    }

    public Integer getMaxPersons() {
        return maxPersons;
    }

    public void setMaxPersons(Integer maxPersons) {
        this.maxPersons = maxPersons;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomTypeNo() {
        return roomTypeNo;
    }

    public void setRoomTypeNo(String roomTypeNo) {
        this.roomTypeNo = roomTypeNo;
    }

    public Long getHotelID() {
        return hotelID;
    }

    public void setHotelID(Long hotelID) {
        this.hotelID = hotelID;
    }

    public String getRoomEquipment() {
        return roomEquipment;
    }

    public void setRoomEquipment(String roomEquipment) {
        this.roomEquipment = roomEquipment;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getAddBedQty() {
        return addBedQty;
    }

    public void setAddBedQty(Integer addBedQty) {
        this.addBedQty = addBedQty;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getOtherEquipment() {
        return otherEquipment;
    }

    public void setOtherEquipment(String otherEquipment) {
        this.otherEquipment = otherEquipment;
    }

    public List getLstPriceType() {
        return lstPriceType;
    }

    public void setLstPriceType(List lstPriceType) {
        this.lstPriceType = lstPriceType;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getIsAgrement() {
        return isAgrement;
    }

    public void setIsAgrement(String isAgrement) {
        this.isAgrement = isAgrement;
    }

    public String getRoomFloor() {
    	if(roomFloor!=null) return roomFloor.replaceAll("#", " ").replaceAll("!", " ");
        return roomFloor;
    }

    public void setRoomFloor(String roomFloor) {
        if(roomFloor!=null) this.roomFloor = roomFloor.replaceAll("#", " ").replaceAll("!", " ");//过滤掉# !等特殊字符
        else this.roomFloor = roomFloor;
    }

    public String getCreateById() {
        return createById;
    }

    public void setCreateById(String createById) {
        this.createById = createById;
    }

    public String getModifyById() {
        return modifyById;
    }

    public void setModifyById(String modifyById) {
        this.modifyById = modifyById;
    }

    public String getIshkroomtype() {
        return ishkroomtype;
    }

    public void setIshkroomtype(String ishkroomtype) {
        this.ishkroomtype = ishkroomtype;
    }

	public String getRoomEquipmentRemark() {
		return roomEquipmentRemark;
	}

	public void setRoomEquipmentRemark(String roomEquipmentRemark) {
		this.roomEquipmentRemark = roomEquipmentRemark;
	}

	public Integer getIsAddBed() {
		return isAddBed;
	}

	public void setIsAddBed(Integer isAddBed) {
		this.isAddBed = isAddBed;
	}



}
package com.mangocity.hotel.base.persistence;

import java.util.Date;

/**
 * 地理位置/名胜 实体类
 * 
 * @author shengwei.zuo
 */

public class HtlGeographicalposition implements java.io.Serializable,Comparable<HtlGeographicalposition> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Fields
    // 地点ID
    private Long ID;

    // 地点
    private String name;

    // 详细地点
    private String address;

    // 省份名称
    private String provinceName;

    // 城市名称
    private String cityName;

    // 电子地图
    private Long gisId;

    // 经度
    private Double longitude;

    // 纬度
    private Double latitude;
    
    //百度纬度
	private Double baiduLatitude;
	//百度经度
	private Double baiduLongitude;

    // 类型ID
    private Long gptypeId;

    // 操作人名称
    private String operationer;

    // 操作人ID
    private String operationerId;

    // 操作日期
    private Date operationDate;
    
    private String cityCode;
    
    private Long seqNo;
    
    //是否生效
    private String isActive;

    
    private Integer sortNum;
    
    // Constructors
    public static final int MAX_SORT_NUM = 999;
    

	public HtlGeographicalposition(Long id, String name) {
		ID = id;
		this.name = name;
	}

	public HtlGeographicalposition(Long id, String name, String address,
			String provinceName, String cityName, Long gisId, Double longitude,
			Double latitude, Long gptypeId, String operationer,
			String operationerId, Date operationDate, String cityCode) {
		ID = id;
		this.name = name;
		this.address = address;
		this.provinceName = provinceName;
		this.cityName = cityName;
		this.gisId = gisId;
		this.longitude = longitude;
		this.latitude = latitude;
		this.gptypeId = gptypeId;
		this.operationer = operationer;
		this.operationerId = operationerId;
		this.operationDate = operationDate;
		this.cityCode = cityCode;
	}

	public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Long getGisId() {
        return gisId;
    }

    public void setGisId(Long gisId) {
        this.gisId = gisId;
    }

    public Long getGptypeId() {
        return gptypeId;
    }

    public void setGptypeId(Long gptypeId) {
        this.gptypeId = gptypeId;
    }

    public String getOperationer() {
        return operationer;
    }

    public void setOperationer(String operationer) {
        this.operationer = operationer;
    }

    public String getOperationerId() {
        return operationerId;
    }

    public void setOperationerId(String operationerId) {
        this.operationerId = operationerId;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    /** default constructor */
    public HtlGeographicalposition() {
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
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

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public Long getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Long seqNo) {
		this.seqNo = seqNo;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public int compareTo(HtlGeographicalposition geo) {
		 int firstSortNum = geo.getSortNum() == null ? MAX_SORT_NUM : geo.getSortNum();
		 int secondSortNum = this.sortNum == null ? MAX_SORT_NUM : this.sortNum;
		// TODO Auto-generated method stub
		return secondSortNum - firstSortNum;
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
package com.mangocity.hweb.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class HotelInfoForWebBean implements Serializable {

    // 酒店名称
    private String chnName;

    // 酒店id
    private long hotelId;

    // 酒店星级
    private String hotelStar;

    // 酒店简介
    private String hotelIntroduce;

    // 酒店地址
    private String chnAddress;

    // 酒店电话
    private String telephone;

    // 酒店LOGO图片
    private String pictureName;

    // 酒店加床价
    private double addPricebed;

    // 酒店可用信用卡类型
    private String creditCard;

    // 酒店规定入住时间
    private String checkInTime;

    // 规定退房时间
    private String checkOutTime;

    // 酒店付费说明
    // 酒店星级数目
    private int starNum;

    // 酒店星级类型
    private int starBody;

    // 酒店价格列表
    private List<QueryHotelForWebRoomType> priceType = new ArrayList<QueryHotelForWebRoomType>();

    // 主推类型
    private String commendType;
    
    //酒店房间总数
    private String layerCount;

    /**
     * 省份代码 hotelV2.9.2 add by chenjiajie 2009-07-22
     */
    private String state;

    /**
     * 省份代码 hotelV2.9.2 add by chenjiajie 2009-07-22
     */
    private String city;

    /**
     * 省份代码 hotelV2.9.2 add by chenjiajie 2009-07-22
     */
    private String zone;

    /**
     * 省份代码 hotelV2.9.2 add by chenjiajie 2009-07-22
     */
    private String bizZone;
    
    //酒店餐饮休闲设施
    private String mealFixtrue;
    
    //酒店客房设施
    private String roomFixtrue;
    
    //酒店免费服务
    private String freeService;
    
    //酒店残疾人设施
    private String handicappedFixtrue;
    
    //电子地图用begin
    private String longitude;
    
    //电子地图用
    private String latitude;
    
    private String freeServiceRemark;
    
    private String roomFixtrueRemark;
  //电子地图用end
    //酒店网址 TMC-v2.0 add by shengwei.zuo  2010-3-11
    private String website;
    
    // 酒店英文名称 TMC-v2.0 add by shengwei.zuo  2010-3-11  
    private String  engName;
    
    /**
     * 开业日期  TMC-v2.0 add by shengwei.zuo  2010-3-11  
     */
    private String praciceDate;

    /**
     * 装修日期  TMC-v2.0 add by shengwei.zuo  2010-3-11  
     */
    private String fitmentDate;

    /**
     * 装修程度 TMC-v2.0 add by shengwei.zuo  2010-3-11  
     */
    private String fitmentDegree;


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

	public String getFitmentDegree() {
		return fitmentDegree;
	}

	public void setFitmentDegree(String fitmentDegree) {
		this.fitmentDegree = fitmentDegree;
	}

    public String getFreeServiceRemark() {
		return freeServiceRemark;
	}

	public void setFreeServiceRemark(String freeServiceRemark) {
		this.freeServiceRemark = freeServiceRemark;
	}

	public String getRoomFixtrueRemark() {
		return roomFixtrueRemark;
	}

	public void setRoomFixtrueRemark(String roomFixtrueRemark) {
		this.roomFixtrueRemark = roomFixtrueRemark;
	}

	public String getChnName() {
        return chnName;
    }

    public void setChnName(String chnName) {
        this.chnName = chnName;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelStar() {
        return hotelStar;
    }

    public void setHotelStar(String hotelStar) {
        this.hotelStar = hotelStar;
        switch (Integer.parseInt(hotelStar)) {
        case 19:
            setStarNum(5);
            setStarBody(1);
            break;
        case 29:
            setStarNum(5);
            setStarBody(2);
            break;
        case 39:
            setStarNum(4);
            setStarBody(1);
            break;
        case 49:
            setStarNum(4);
            setStarBody(2);
            break;
        case 59:
            setStarNum(3);
            setStarBody(1);
            break;
        // 加个准3星，modify by zhineng.zhuang
        case 64:
            setStarNum(3);
            setStarBody(2);
            break;
        case 69:
            setStarNum(2);
            setStarBody(1);
            break;
        case 79:
            setStarNum(2);
            setStarBody(2);
            break;
        case 66:
            setStarNum(2);
            setStarBody(2);
            break;
        default:
            break;
        }
    }

    public String getHotelIntroduce() {
        return hotelIntroduce;
    }

    public void setHotelIntroduce(String hotelIntroduce) {
        this.hotelIntroduce = hotelIntroduce;
    }

    public String getChnAddress() {
        return chnAddress;
    }

    public void setChnAddress(String chnAddress) {
        this.chnAddress = chnAddress;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public double getAddPricebed() {
        return addPricebed;
    }

    public void setAddPricebed(double addPricebed) {
        this.addPricebed = addPricebed;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
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

    public List<QueryHotelForWebRoomType> getPriceType() {
        return priceType;
    }

    public void setPriceType(List<QueryHotelForWebRoomType> priceType) {
        this.priceType = priceType;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getCommendType() {
        return commendType;
    }

    public void setCommendType(String commendType) {
        this.commendType = commendType;
    }

    public int getStarNum() {
        return starNum;
    }

    public void setStarNum(int starNum) {
        this.starNum = starNum;
    }

    public int getStarBody() {
        return starBody;
    }

    public void setStarBody(int starBody) {
        this.starBody = starBody;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

	public String getLayerCount() {
		return layerCount;
	}

	public void setLayerCount(String layerCount) {
		this.layerCount = layerCount;
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

	public String getHandicappedFixtrue() {
		return handicappedFixtrue;
	}

	public void setHandicappedFixtrue(String handicappedFixtrue) {
		this.handicappedFixtrue = handicappedFixtrue;
	}

	public String getFreeService() {
		return freeService;
	}

	public void setFreeService(String freeService) {
		this.freeService = freeService;
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

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getEngName() {
		return engName;
	}

	public void setEngName(String engName) {
		this.engName = engName;
	}
}

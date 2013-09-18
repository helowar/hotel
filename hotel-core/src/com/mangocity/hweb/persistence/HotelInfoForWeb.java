package com.mangocity.hweb.persistence;

import java.io.Serializable;

/**
 */
public class HotelInfoForWeb implements Serializable {
	
	private static final long serialVersionUID = -958839234119381459L;

    private long hotelId;

    private String hotelName;

    private String chnAddress;

    private String hotelIntroduce;

    private String telephone;

    private String starType;

    private String hotelStar;

    private int starNum;

    private int starBody;

    private String commendType;

    private String logoPictureName;

    private String hallPictureName;

    private String outPictureName;

    private String roomPictureName;

    private String checkOutTime;

    private String checkInTime;

    private String creditCard;
    
    private String city;
    
    //电子地图用begin
    private String longitude;
    
    private String latitude;
    
    private String freeServiceRemark;
    
    private String roomFixtrueRemark;
    //电子地图用end
    
    //酒店餐饮休闲设施
    private String mealFixtrue;
    
    //酒店客房设施
    private String roomFixtrue;
    
    //酒店免费服务
    private String freeService;
    
    //酒店残疾人设施
    private String handicappedFixtrue;
    
  //房间总数量 hotel 2.9.3 add by shengwei.zuo 
	private String  roomCount;
	
	/**
     *  酒店网址 TMC-v2.0 add by shengwei.zuo  2010-3-11
    */
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
    
    
    //开业时间，装修时间的字符串   TMC-v2.0 add by shengwei.zuo  2010-3-11  
    private String  praciceAndFitmentStr;
    
    //诺曼底,加入酒店网站显示 add by haibo.li
	private String hotelNet; 

    public String getHallPictureName() {
        return hallPictureName;
    }

    public void setHallPictureName(String hallPictureName) {
        this.hallPictureName = hallPictureName;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getRoomPictureName() {
        return roomPictureName;
    }

    public void setRoomPictureName(String roomPictureName) {
        this.roomPictureName = roomPictureName;
    }

    public String getOutPictureName() {
        return outPictureName;
    }

    public void setOutPictureName(String outPictureName) {
        this.outPictureName = outPictureName;
    }

    public int getStarBody() {
        return starBody;
    }

    private void setStarBody(int starBody) {
        this.starBody = starBody;
    }

    public int getStarNum() {
        return starNum;
    }

    private void setStarNum(int starNum) {
        this.starNum = starNum;
    }

    public String getStarType() {
        return starType;
    }

    public void setStarType(String starType) {
        this.starType = starType;
        switch (Integer.parseInt(starType)) {
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

    public String getCommendType() {
        return commendType;
    }

    public void setCommendType(String commendType) {
        this.commendType = commendType;
    }

    public String getChnAddress() {
        return chnAddress;
    }

    public void setChnAddress(String chnAddress) {
        this.chnAddress = chnAddress;
    }

    public String getHotelIntroduce() {
        return hotelIntroduce;
    }

    public void setHotelIntroduce(String hotelIntroduce) {
        this.hotelIntroduce = hotelIntroduce;
    }

    public String getHotelStar() {
        return hotelStar;
    }

    public void setHotelStar(String hotelStar) {
        this.hotelStar = hotelStar;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public String getLogoPictureName() {
        return logoPictureName;
    }

    public void setLogoPictureName(String logoPictureName) {
        this.logoPictureName = logoPictureName;
    }

	public String getRoomCount() {
		return roomCount;
	}

	public void setRoomCount(String roomCount) {
		this.roomCount = roomCount;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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

	public String getPraciceAndFitmentStr() {
		return praciceAndFitmentStr;
	}

	public void setPraciceAndFitmentStr(String praciceAndFitmentStr) {
		this.praciceAndFitmentStr = praciceAndFitmentStr;
	}
	
	public String getHotelNet() {
		return hotelNet;
	}

	public void setHotelNet(String hotelNet) {
		this.hotelNet = hotelNet;
	}
}

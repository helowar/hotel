package com.mangocity.hweb.persistence;

import java.io.Serializable;
import java.util.Date;

import com.mangocity.util.hotel.constant.FromChannelType;


/**
 * 
 * 酒店网站查询条件类
 * 
 * TODO: 如果为了集群session，有可能需要序列化
 * 
 * @author chenkeming
 * 
 */
public class QueryHotelForWebBean implements Serializable {
	
	private static final long serialVersionUID = -2715466393125109053L;

    /**
     * 城市ID
     */
    private String cityId;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 酒店ID
     */
    private long hotelId;

    /**
     * 酒店名称
     */
    private String hotelName;
    /**
     * 用于查询条件的酒店名称
     */
    private String queryHotelName;

    /**
     * 行政区代码
     */
    private String district;

    /**
     * 商业区代码
     */
    private String bizZone;

    /**
     * 入住日期
     */
    private Date inDate;

    /**
     * 离店日期
     */
    private Date outDate;

    /**
     * 价格范围
     */
    private String priceStr;

    /**
     * 最低价
     */
    private String minPrice;

    /**
     * 最高价
     */
    private String maxPrice;

    /**
     * 酒店星级
     */
    private String hotelStar;

    /**
     * 酒店星级 -- 挂牌星级
     */
    private String formalStarLevel;

    /**
     * 酒店星级 -- 未评星级(准星级)
     */
    private String informalStarLevel;

    /**
     * 特殊要求，目前网站查询有:宽带3，最近开业/装修4， 中间用","分割
     */
    private String specialRequest;

    /**
     * 酒店主题
     */
    private String theme;

    /**
     * 酒店排列顺序方式:<br>
     * 芒果推荐1 <br>
     * 价格2 <br>
     * 星级3 <br>
     * 缺省是按芒果推荐方式
     */
    private int qrymethod = 1;

    /**
     * 房型排列顺序<br>
     * 1: 严格按价格排序 0: 正常排序
     */
    private int priceOrder = 0;

    /**
     * 页码
     */
    private int pageNo = 1;

    /**
     * 多少条记录/页，默认15个酒店/页
     */
    private int pageSize = 15;

    /**
     * 当前页，默认第1页
     */
    private int pageIndex = 1;

    /**
     * 总页数
     */
    private int totalIndex;

    /**
     * 查询的渠道来源
     * 
     * @author chenkeming Mar 31, 2009 11:55:22 AM
     * @see FromChannelType
     */
    private String fromChannel = "web";

    /**
     * 酒店类型 hotel2.9.2 add by chenjiajie 2009-07-23 商务酒店:01
     * ;商务酒店:02;酒店式公寓:03;酒店式公寓:04;经济型酒店:05;主题酒店:06
     */
    private String hotelType;

    /**
     * 地图酒店ID封装 add by haibo.li 电子地图二期
     * 
     * @return
     */
    private String hotelIdLst;
    
    /**
     * Haier 出行方式  因公出行为N, 因私出行为I add by shizhongwen 2009-09-29
     */
    private String traveltype;
    
    /**
     * Haier VIP会员 add by shizhongwen 2009-10-17
     */
    private boolean HaierVipMember;
    
    /**
     * 是否tmc会员查酒店
     * @author chenkeming Nov 3, 2009 11:41:45 AM
     */
    private boolean forTmc = false;
    
    /**
     * 酒店详情页面，判断是否把日期默认为当天间夜。（当在酒店详情页面，选择行政区、商业区、我预览过的酒店是入住时间为当天间夜）
     * 
     * @return
     */
    private String dateIsNotUse;
    
    /**
     * 网站改版优化 是否忽略count总数,1:忽略;0:不忽略,默认是0;页面总数通过异步获取 add by chenjiajie 2009-12-16
     */
    private int ignorePageCount = 0;
    
    /**
     * 网站改版优化 是否忽略查询酒店List,1:忽略;0:不忽略,默认是0;页面总数通过异步获取 add by chenjiajie 2009-12-16
     */
    private int ignoreQueryList = 0;
    
    
    private String priceStrValue;
    
	    /**
     * 网站JSON优化 0:不是JSON解析;1:是JSON解析
     */
    private int jsonTag = 0;
    /**
     * b2b账号
     */
    private String b2bCd;
    
    private double maxLat;
    private double maxLng;
    private double minLat;
    private double minLng;
    private double distance;
    

    public String getB2bCd() {
		return b2bCd;
	}

	public void setB2bCd(String cd) {
		b2bCd = cd;
	}
	
	   /**
     * 诺曼底查询渠道,这里是为了诺曼底查询不显示按钮灰选的房型
     * 所以必须标记为这个查询为诺曼底过去的
     * add by haibo.li 
     */
    private boolean conTmcQueryFalg = false;
    
    /**
     * 诺曼底 查询酒店区分是查询全部酒店还是单个酒店
     * 查询多个酒店传入:ALL,单个酒店可以不传
     * add by haibo.li
     */
    private String queryType;
    
    /**
     * 是否交行等合作商渠道查询
     */
    private boolean forCooperate = false;    

    public boolean isConTmcQueryFalg() {
		return conTmcQueryFalg;
	}

	public void setConTmcQueryFalg(boolean conTmcQueryFalg) {
		this.conTmcQueryFalg = conTmcQueryFalg;
	}

    public String getPriceStrValue() {
		return priceStrValue;
	}

	public void setPriceStrValue(String priceStrValue) {
		this.priceStrValue = priceStrValue;
	}

	public String getHotelIdLst() {
        return hotelIdLst;
    }

    public void setHotelIdLst(String hotelIdLst) {
        this.hotelIdLst = hotelIdLst;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public Date getInDate() {
        return inDate;
    }

    public void setInDate(Date inDate) {
        this.inDate = inDate;
    }

    public Date getOutDate() {
        return outDate;
    }

    public void setOutDate(Date outDate) {
        this.outDate = outDate;
    }

    public String getPriceStr() {
        return priceStr;
    }

    public void setPriceStr(String priceStr) {
        this.priceStr = priceStr;
    }

    public String getHotelStar() {
        return hotelStar;
    }

    public void setHotelStar(String hotelStar) {
        this.hotelStar = hotelStar;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getTotalIndex() {
        return totalIndex;
    }

    public void setTotalIndex(int totalIndex) {
        this.totalIndex = totalIndex;
    }

    public int getQrymethod() {
        return qrymethod;
    }

    public void setQrymethod(int qrymethod) {
        this.qrymethod = qrymethod;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
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

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public String getSpecialRequest() {
        return specialRequest;
    }

    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
    }

    public String getFormalStarLevel() {
        return formalStarLevel;
    }

    public void setFormalStarLevel(String formalStarLevel) {
        this.formalStarLevel = formalStarLevel;
    }

    public String getInformalStarLevel() {
        return informalStarLevel;
    }

    public void setInformalStarLevel(String informalStarLevel) {
        this.informalStarLevel = informalStarLevel;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public int getPriceOrder() {
        return priceOrder;
    }

    public void setPriceOrder(int priceOrder) {
        this.priceOrder = priceOrder;
    }

    public String getFromChannel() {
        return fromChannel;
    }

    public void setFromChannel(String fromChannel) {
        this.fromChannel = fromChannel;
    }

    public String getHotelType() {
        return hotelType;
    }

    public void setHotelType(String hotelType) {
        this.hotelType = hotelType;
    }
	
		public String getDateIsNotUse() {
		return dateIsNotUse;
	}

	public void setDateIsNotUse(String dateIsNotUse) {
		this.dateIsNotUse = dateIsNotUse;
	}

    public String getTraveltype() {
        return traveltype;
    }

    public void setTraveltype(String traveltype) {
        this.traveltype = traveltype;
    }

    public boolean isHaierVipMember() {
        return HaierVipMember;
    }

    public void setHaierVipMember(boolean haierVipMember) {
        HaierVipMember = haierVipMember;
    }

    public boolean isForTmc() {
        return forTmc;
    }

    public void setForTmc(boolean forTmc) {
        this.forTmc = forTmc;
    }

	public String getQueryHotelName() {
		return queryHotelName;
	}

	public void setQueryHotelName(String queryHotelName) {
		this.queryHotelName = queryHotelName;
	}

	public int getIgnorePageCount() {
		return ignorePageCount;
	}

	public void setIgnorePageCount(int ignorePageCount) {
		this.ignorePageCount = ignorePageCount;
	}

	public int getIgnoreQueryList() {
		return ignoreQueryList;
	}

	public void setIgnoreQueryList(int ignoreQueryList) {
		this.ignoreQueryList = ignoreQueryList;
	}
	
//	@Override
//	public String toString() {
//		JSONObject json = JSONObject.fromObject(this, JsonUtils.configJson("yyyy-MM-dd"));
//		return json.toString();//.replace("\"", "'")
//	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public int getJsonTag() {
		return jsonTag;
	}

	public void setJsonTag(int jsonTag) {
		this.jsonTag = jsonTag;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getMaxLat() {
		return maxLat;
	}

	public void setMaxLat(double maxLat) {
		this.maxLat = maxLat;
	}

	public double getMaxLng() {
		return maxLng;
	}

	public void setMaxLng(double maxLng) {
		this.maxLng = maxLng;
	}

	public double getMinLat() {
		return minLat;
	}

	public void setMinLat(double minLat) {
		this.minLat = minLat;
	}

	public double getMinLng() {
		return minLng;
	}

	public void setMinLng(double minLng) {
		this.minLng = minLng;
	}

	public boolean isForCooperate() {
		return forCooperate;
	}

	public void setForCooperate(boolean forCooperate) {
		this.forCooperate = forCooperate;
	}

	

}

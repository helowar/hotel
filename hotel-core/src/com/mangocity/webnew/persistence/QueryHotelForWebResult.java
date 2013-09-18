package com.mangocity.webnew.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 酒店网站v2.2查询结果<br>
 * 酒店类<br>
 * 
 * 注: 该类除了多了房型列表成员，实际上和存储过程返回的结果类是一致的，在页面展现中 使用该类来展现酒店相关的信息，该类中的房型相关信息，在页面展现中没有用到
 * 
 * @author chenkeming
 * 
 */
public class QueryHotelForWebResult implements Serializable {
	
	private static final long serialVersionUID = -5670439186537957813L;

    /**
     * 酒店id
     */
    private long hotelId;

    /**
     * 酒店中文名称
     */
    private String hotelChnName;

    /**
     * 酒店英文名称
     */
    private String hotelEngName;

    /**
     * 酒店星级
     */
    private String hotelStar;

    /**
     * 酒店类型
     */
    // private String hotelType;
    /**
     * 酒店中文地址
     */
     private String chnAddress;
    /**
     * 酒店英文地址
     */
     private String engAddress;
    /**
     * 酒店介绍
     */
    private String hotelIntroduce;

    /**
     * 提示信息
     */
    // private String clueInfo;
    /**
     * 取消信息
     */
    // private String cancelMessage;
    /**
     * 酒店图片类型
     */
    // private String pictureType;
    /**
     * 酒店图片名
     */
    // private String pictureName;
    /**
     * 商业区
     */
    private String bizZone;

    /**
     * 城区
     */
    private String district;

    /**
     * 酒店推荐级别
     */
    private String commendType;

    /**
     * 酒店所在城市
     */
    private String city;

    /**
     * 酒店自动生成简介信息
     */
    private String autoIntroduce;

    /**
     * 酒店图片
     */
    // private String hotelPicture;
    /**
     * 酒店星级数目
     */
    private int starNum;

    /**
     * 酒店星级类型 1代表实心mango 2代表准星级酒店空心mango
     */
    private int starBody;

    /**
     * 酒店大厅照片名
     */
    private String hallPictureName;

    /**
     * 酒店外观照片名
     */
    private String outPictureName;

    /**
     * 酒店房间照片名
     */
    private String roomPictureName;

    /**
     * 酒店LOGO照片
     */
    private String hotelLogo;

    /**
     * 酒店3D照片数量
     */
    private int sandNum;

    /**
     * 酒店币种
     */
    private String currency;

    /**
     * 酒店汇率
     */
    private String rateStr;

    /**
     * 酒店是否有接机服务. 1: 有 0: 无
     */
    private int forPlane;

    /**
     * 酒店是否有免费停车场. 1: 有 0: 无
     */
    private int forFreeStop;

    /**
     * 酒店是否有免费游泳池. 1: 有 0: 无
     */
    private int forFreePool;

    /**
     * 酒店是否有免费健身设施. 1: 有 0: 无
     */
    private int forFreeGym;

    /**
     * 酒店是否有某个房型有宽带. 1: 有 0: 无
     */
    private int forNetBand;

    /**
     * 开业年份
     */
    private int praciceYear = 0;

    /**
     * 装修年份 -- 目前为包括装修程度的字符串
     */
    private String fitmentYear;

    /**
     * 酒店交通信息
     */
    private String trafficInfo;

    /**
     * 酒店的会员综合评分
     */
    private double generalPoint = 0.0;

    /**
     * ADD BY WUYUN 2009-03-19 是否香港中科酒店
     */
    private boolean flagCtsHK;

    /**
     * ADD BY WUYUN 2009-03-19 是否直连酒店
     */
    private String cooperateChannel;

    /**
     * ADD BY WUYUN 2009-03-20 房型是否是香港中科
     */
    private int roomChannel;

    /**
     * v2.8 中旅酒店最大可订房间数
     * 
     * @author chenkeming Apr 1, 2009 9:05:22 AM
     */
    private int roomNumCts;

    /** *************** 芒果网大礼包相关信息 begin ************** */

    /**
     * 酒店是否有芒果网大礼包优惠信息 1: 有 0: 无
     */
    private int hasPreSale;

    /**
     * 芒果网优惠信息名称
     */
    private String preSaleName;

    /**
     * 芒果网优惠信息内容
     */
    private String preSaleContent;

    /**
     * 芒果网优惠起止日期字符串
     */
    private String preSaleBeginEnd;

    /**
     * 芒果网优惠URL
     */
    private String preSaleURL;

    /** *************** 芒果网大礼包相关信息 end ************** */

    /** ******************************** 显示控制参数信息 begin ***************************************** */

    /**
     * 周行数
     */
    private int weekTotal;

    /**
     * 显示行的宽度
     */
    // private String avgWidthStr;
    /**
     * 行显示的占用列数
     */
    private int colCount;

    /**
     * 周天数
     */
    private List weekNum = new ArrayList();

    /**
     * 日期数
     */
    private List<String> dateNum = new ArrayList<String>();

    /**
     * 该酒店有多少个房型, 目前, 如果fx>3则需要"显示全部房型"按钮
     */
    private int fx;

    /** ******************************** 显示控制参数信息 end ***************************************** */

    /** ******************************** 房型相关信息 begin ******************************************* */

    /**
     * 房态
     */
    private String room_state;

    /**
     * 子房型ID
     */
    private String childRoomTypeId;

    /**
     * 房型ID
     */
    private long room_type_id;

    /**
     * 房型名称
     */
    private String roomName;

    /**
     * 配额类型
     */
    private String quotaType;

    /**
     * 支付方式
     */
    private String payMethod;

    /**
     * 房间间夜日期
     */
    private String able_sale_date;

    /**
     * 销售价
     */
    private Double salesPrice;

    /**
     * 门市价
     */
    private Double salesRoomPrice;

    /**
     * 价格类型
     */
    private String priceType;

    /**
     * 早餐类型
     */
    private int breakfastType;

    /**
     * 早餐数量
     */
    private int breakfastNum;

    /**
     * 开关房标志
     */
    private String close_flag;

    /**
     * 关房原因
     */
    private String reason;

    /**
     * 价格开始个数
     */
    private int priceId;

    /**
     * 
     */
    private int endpriceId;

    /**
     * 存储过程返回的根据预订条款该房型能否预订<br>
     * 1: 能<br>
     * 0: 不能<br>
     */
    private int canBook;

    /**
     * 如果根据预订条款该房型不能预订，则保存原因信息
     */
    private String cantBookReason;

    /**
     * 房型面积(单位为平方米)
     */
    private String acreage;

    /**
     * 房型所在楼层
     */
    private String roomFloor;

    /**
     * 配额数量 -- 用于检查房态不可超且配额为0的情况
     */
    private int availQty = 0;

    /**
     * 房型宽带信息
     */
    private String roomNet;

    /**
     * 子房型是否有优惠信息
     */
    private int hasPromo;

    /**
     * 子房型优惠信息内容
     */
    private String promoContent;

    /**
     * 子房型优惠起始日期字符串
     */
    private String promoBeginEnd;

    /**
     * ***********************hotel2.6加了8个参数@author
     * zhuangzhineng@begin********************************
     */

    /**
     * hotel2.6 是否需要无条件担保, 是:1(true), 否:0(false)
     * 
     * @author chenkeming Feb 6, 2009 8:58:29 AM
     */
    private boolean needAssure;

    /**
     * hotel2.6 面付转预付, 必须:1, 允许:2, 不许:3
     * 
     * @author chenkeming Feb 6, 2009 8:59:16 AM
     */
    private int payToPrepay;

    /**
     * 最晚可预订日期 hotel2.6 add by zhineng.zhuang 2009-02-16
     */
    private Date latestBookableDate;

    /**
     * 最晚可预订时间点 hotel2.6 add by zhineng.zhuang 2009-02-16
     */
    private String latestBokableTime;
    
    /**hotel 2.9.3  
     * 新增时间段的预订规则 add by shengwei.zuo 2009-09-06 begin  
     */
    
    //最早可预订日期 
    private Date  firstBookableDate;
    
    //最早可预订时间
    private String firstBookableTime;

    /**hotel 2.9.3  
     * 新增时间段的预订规则 add by shengwei.zuo 2009-09-06 end  
     */

    /**
     * 必住最后日期 hotel2.6 add by zhineng.zhuang 2009-02-16
     */
    private Date mustLastDate;

    /**
     * 必住最早日期 hotel2.6 add by zhineng.zhuang 2009-02-16
     */
    private Date mustFirstDate;

    /**
     * 连住天数 hotel2.6 add by zhineng.zhuang 2009-02-16
     */
    private long continueDay;

    /**
     * 最少限住天数 hotel2.9.2 add by xiaoyong.li 2009-07-28
     */
    private long minRestrictNights;

    /**
     * 最大限住天数 hotel2.9.2 add by xiaoyong.li 2009-07-28
     */
    private long maxRestrictNights;

    /**
     * 必住日期 hotel2.6 add by zhineng.zhuang 2009-02-16
     */
    private String mustInDate;

    /*
     * 必须连住日期的关系,或者 or 并且 V2.9.2 addby chenjuesu
     */
    private String mustInDatesRelation;

    /**
     * ***********************hotel2.6加了8个参数@author
     * zhuangzhineng@end********************************
     */

    /** hotel2.9.2加了5个参数@author chenjiajie 2009-07-22 begin * */

    /**
     * 房间最多入住人数
     */
    private int maxPersons;

    /**
     * 加床数量
     */
    private int addBedQty;

    /**
     * 房间设施 结果是用,分隔
     */
    private String roomEquipment;

    /**
     * 酒店是否有接机服务. 1: 有 0: 无
     */
    private boolean freeForPlane;

    /**
     * 房间其实设施 结果是用中文字符串
     */
    private String roomOtherEquipment;

    /** hotel2.9.2加了5个参数@author chenjiajie 2009-07-22 end * */

    /** ******************************** 房型相关信息 end ******************************************* */

    /**
     * 房型列表，是RoomType类的集合
     */
    private List roomTypes = new ArrayList();

    /**
     * 地图标志
     */
    private long gisid;

    /**
     * 最低价 电子地图二期 add by haibo.li
     */
    private double minPrice;

    /**
     * 多天标志 电子地图二期 add by haibo.li
     * 
     * @return
     */
    private boolean continueLong;
    
    /**
     * 商业区值
     */
    private String bizValue;
    
    /**
     * 最终币种
     */
    private String currencyValue;
    
    
  
  //房间数量 add by shengwei.zuo hotel2.9.3 2009-09-09
    private Integer room_qty;
    
    //酒店详情添加属性。add by zhijie.gu 2009-11-03 begin 
    //酒店电话
    private String telephone;
    
    //酒店星级类型
    private String starType;
    
    //酒店标志图片名称
    private String logoPictureName;
    
    //酒店可用信用卡
    private String creditCard;
    
    //酒店所在商业区中文名
    private String districtName;
    
    //酒店总房间数
    private String roomCount;
    
    //组装开业年份、装修年份字符串
    private String praciceAndFitmentStr;
    
    //酒店餐饮休闲设施
    private String mealFixtrue;
    
    //酒店客房设施
    private String roomFixtrue;
    
    //酒店免费服务
    private String freeService;
    
    //酒店残疾人设施
    private String handicappedFixtrue;
    
    //用来存放cookie中存放的酒店名称和酒店Id，存放的是数组
    private List hotelNameAndIdStr;
    
    //酒店所在城市的行政区
    private List businessList;
    
    //酒店所在城市的商业区
    private List districtList;
    
    //酒店详情页面附加信息
    private String serviceStr;
    //酒店详情添加属性。add by zhijie.gu 2009-11-03 end

    public String getRoomCount() {
		return roomCount;
	}

	public void setRoomCount(String roomCount) {
		this.roomCount = roomCount;
	}

	public boolean isContinueLong() {
        return continueLong;
    }

    public void setContinueLong(boolean continueLong) {
        this.continueLong = continueLong;
    }

    public int getWeekTotal() {
        return weekTotal;
    }

    public void setWeekTotal(int weekTotal) {
        this.weekTotal = weekTotal;
    }

    public String getFitmentYear() {
        return fitmentYear;
    }

    public void setFitmentYear(String fitmentYear) {
        this.fitmentYear = fitmentYear;
    }

    public String getRoomNet() {
        return roomNet;
    }

    public void setRoomNet(String roomNet) {
        this.roomNet = roomNet;
    }

    public int getAvailQty() {
        return availQty;
    }

    public void setAvailQty(int availQty) {
        this.availQty = availQty;
    }

    public String getAcreage() {
        return acreage;
    }

    public void setAcreage(String acreage) {
        this.acreage = acreage;
    }

    public String getRoomFloor() {
        return roomFloor;
    }

    public void setRoomFloor(String roomFloor) {
        this.roomFloor = roomFloor;
    }

    public int getCanBook() {
        return canBook;
    }

    public void setCanBook(int canBook) {
        this.canBook = canBook;
    }

    public int getColCount() {
        return colCount;
    }

    public void setColCount(int colCount) {
        this.colCount = colCount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List getWeekNum() {
        return weekNum;
    }

    public void setWeekNum(List weekNum) {
        this.weekNum = weekNum;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelChnName() {
        return hotelChnName;
    }

    public void setHotelChnName(String hotelChnName) {
        this.hotelChnName = hotelChnName;
    }

    public String getHotelEngName() {
        return hotelEngName;
    }

    public void setHotelEngName(String hotelEngName) {
        this.hotelEngName = hotelEngName;
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

    public String getBizZone() {
        return bizZone;
    }

    public void setBizZone(String bizZone) {
        this.bizZone = bizZone;
    }

    public List getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(List roomTypes) {
        this.roomTypes = roomTypes;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAutoIntroduce() {
        return autoIntroduce;
    }

    public void setAutoIntroduce(String autoIntroduce) {
        this.autoIntroduce = autoIntroduce;
    }

    public int getFx() {
        return fx;
    }

    public void setFx(int fx) {
        this.fx = fx;
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

    public String getHallPictureName() {
        return hallPictureName;
    }

    public void setHallPictureName(String hallPictureName) {
        this.hallPictureName = hallPictureName;
    }

    public String getOutPictureName() {
        return outPictureName;
    }

    public void setOutPictureName(String outPictureName) {
        this.outPictureName = outPictureName;
    }

    public String getRoomPictureName() {
        return roomPictureName;
    }

    public void setRoomPictureName(String roomPictureName) {
        this.roomPictureName = roomPictureName;
    }

    public int getSandNum() {
        return sandNum;
    }

    public void setSandNum(int sandNum) {
        this.sandNum = sandNum;
    }

    public String getRateStr() {
        return rateStr;
    }

    public void setRateStr(String rateStr) {
        this.rateStr = rateStr;
    }

    public int getPriceId() {
        return priceId;
    }

    public void setPriceId(int priceId) {
        this.priceId = priceId;
    }

    public int getEndpriceId() {
        return endpriceId;
    }

    public void setEndpriceId(int endpriceId) {
        this.endpriceId = endpriceId;
    }

    public String getChildRoomTypeId() {
        return childRoomTypeId;
    }

    public void setChildRoomTypeId(String childRoomTypeId) {
        this.childRoomTypeId = childRoomTypeId;
    }

    public long getRoom_type_id() {
        return room_type_id;
    }

    public void setRoom_type_id(long room_type_id) {
        this.room_type_id = room_type_id;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    public String getRoom_state() {
        return room_state;
    }

    public void setRoom_state(String room_state) {
        this.room_state = room_state;
    }

    public Double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(Double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getAble_sale_date() {
        return able_sale_date;
    }

    public void setAble_sale_date(String able_sale_date) {
        this.able_sale_date = able_sale_date;
    }

    public Double getSalesRoomPrice() {
        return salesRoomPrice;
    }

    public void setSalesRoomPrice(Double salesRoomPrice) {
        this.salesRoomPrice = salesRoomPrice;
    }

    public String getHotelLogo() {
        return hotelLogo;
    }

    public void setHotelLogo(String hotelLogo) {
        this.hotelLogo = hotelLogo;
    }

    public int getBreakfastNum() {
        return breakfastNum;
    }

    public void setBreakfastNum(int breakfastNum) {
        this.breakfastNum = breakfastNum;
    }

    public int getBreakfastType() {
        return breakfastType;
    }

    public void setBreakfastType(int breakfastType) {
        this.breakfastType = breakfastType;
    }

    public String getClose_flag() {
        return close_flag;
    }

    public void setClose_flag(String close_flag) {
        this.close_flag = close_flag;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCantBookReason() {
        return cantBookReason;
    }

    public void setCantBookReason(String cantBookReason) {
        this.cantBookReason = cantBookReason;
    }

    public int getForFreeGym() {
        return forFreeGym;
    }

    public void setForFreeGym(int forFreeGym) {
        this.forFreeGym = forFreeGym;
    }

    public int getForFreePool() {
        return forFreePool;
    }

    public void setForFreePool(int forFreePool) {
        this.forFreePool = forFreePool;
    }

    public int getForFreeStop() {
        return forFreeStop;
    }

    public void setForFreeStop(int forFreeStop) {
        this.forFreeStop = forFreeStop;
    }

    public int getForNetBand() {
        return forNetBand;
    }

    public void setForNetBand(int forNetBand) {
        this.forNetBand = forNetBand;
    }

    public int getForPlane() {
        return forPlane;
    }

    public void setForPlane(int forPlane) {
        this.forPlane = forPlane;
    }

    public int getHasPreSale() {
        return hasPreSale;
    }

    public void setHasPreSale(int hasPreSale) {
        this.hasPreSale = hasPreSale;
    }

    public String getPreSaleBeginEnd() {
        return preSaleBeginEnd;
    }

    public void setPreSaleBeginEnd(String preSaleBeginEnd) {
        this.preSaleBeginEnd = preSaleBeginEnd;
    }

    public String getPreSaleContent() {
        return preSaleContent;
    }

    public void setPreSaleContent(String preSaleContent) {
        this.preSaleContent = preSaleContent;
    }

    public String getPreSaleName() {
        return preSaleName;
    }

    public void setPreSaleName(String preSaleName) {
        this.preSaleName = preSaleName;
    }

    public String getPreSaleURL() {
        return preSaleURL;
    }

    public void setPreSaleURL(String preSaleURL) {
        this.preSaleURL = preSaleURL;
    }

    public int getPraciceYear() {
        return praciceYear;
    }

    public void setPraciceYear(int praciceYear) {
        this.praciceYear = praciceYear;
    }

    public String getTrafficInfo() {
        return trafficInfo;
    }

    public void setTrafficInfo(String trafficInfo) {
        this.trafficInfo = trafficInfo;
    }

    public double getGeneralPoint() {
        return generalPoint;
    }

    public void setGeneralPoint(double generalPoint) {
        this.generalPoint = generalPoint;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getHasPromo() {
        return hasPromo;
    }

    public void setHasPromo(int hasPromo) {
        this.hasPromo = hasPromo;
    }

    public String getPromoBeginEnd() {
        return promoBeginEnd;
    }

    public void setPromoBeginEnd(String promoBeginEnd) {
        this.promoBeginEnd = promoBeginEnd;
    }

    public String getPromoContent() {
        return promoContent;
    }

    public void setPromoContent(String promoContent) {
        this.promoContent = promoContent;
    }

    public long getContinueDay() {
        return continueDay;
    }

    public void setContinueDay(long continueDay) {
        this.continueDay = continueDay;
    }

    public String getLatestBokableTime() {
        return latestBokableTime;
    }

    public void setLatestBokableTime(String latestBokableTime) {
        this.latestBokableTime = latestBokableTime;
    }

    public Date getLatestBookableDate() {
        return latestBookableDate;
    }

    public void setLatestBookableDate(Date latestBookableDate) {
        this.latestBookableDate = latestBookableDate;
    }

    public Date getMustFirstDate() {
        return mustFirstDate;
    }

    public void setMustFirstDate(Date mustFirstDate) {
        this.mustFirstDate = mustFirstDate;
    }

    public String getMustInDate() {
        return mustInDate;
    }

    public void setMustInDate(String mustInDate) {
        this.mustInDate = mustInDate;
    }

    public Date getMustLastDate() {
        return mustLastDate;
    }

    public void setMustLastDate(Date mustLastDate) {
        this.mustLastDate = mustLastDate;
    }

    public boolean isNeedAssure() {
        return needAssure;
    }

    public void setNeedAssure(boolean needAssure) {
        this.needAssure = needAssure;
    }

    public int getPayToPrepay() {
        return payToPrepay;
    }

    public void setPayToPrepay(int payToPrepay) {
        this.payToPrepay = payToPrepay;
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

    public int getRoomChannel() {
        return roomChannel;
    }

    public void setRoomChannel(int roomChannel) {
        this.roomChannel = roomChannel;
    }

    public int getRoomNumCts() {
        return roomNumCts;
    }

    public void setRoomNumCts(int roomNumCts) {
        this.roomNumCts = roomNumCts;
    }

    public int getMaxPersons() {
        return maxPersons;
    }

    public void setMaxPersons(int maxPersons) {
        this.maxPersons = maxPersons;
    }

    public int getAddBedQty() {
        return addBedQty;
    }

    public void setAddBedQty(int addBedQty) {
        this.addBedQty = addBedQty;
    }

    public String getRoomEquipment() {
        return roomEquipment;
    }

    public void setRoomEquipment(String roomEquipment) {
        this.roomEquipment = roomEquipment;
    }

    public boolean isFreeForPlane() {
        return freeForPlane;
    }

    public void setFreeForPlane(boolean freeForPlane) {
        this.freeForPlane = freeForPlane;
    }

    public String getRoomOtherEquipment() {
        return roomOtherEquipment;
    }

    public void setRoomOtherEquipment(String roomOtherEquipment) {
        this.roomOtherEquipment = roomOtherEquipment;
    }

    public long getGisid() {
        return gisid;
    }

    public void setGisid(long gisid) {
        this.gisid = gisid;
    }

    public List<String> getDateNum() {
        return dateNum;
    }

    public void setDateNum(List<String> dateNum) {
        this.dateNum = dateNum;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public String getMustInDatesRelation() {
        return mustInDatesRelation;
    }

    public void setMustInDatesRelation(String mustInDatesRelation) {
        this.mustInDatesRelation = mustInDatesRelation;
    }

    public long getMinRestrictNights() {
        return minRestrictNights;
    }

    public void setMinRestrictNights(long minRestrictNights) {
        this.minRestrictNights = minRestrictNights;
    }

    public long getMaxRestrictNights() {
        return maxRestrictNights;
    }

    public void setMaxRestrictNights(long maxRestrictNights) {
        this.maxRestrictNights = maxRestrictNights;
    }

	public Date getFirstBookableDate() {
		return firstBookableDate;
	}

	public void setFirstBookableDate(Date firstBookableDate) {
		this.firstBookableDate = firstBookableDate;
	}

	public String getFirstBookableTime() {
		return firstBookableTime;
	}

	public void setFirstBookableTime(String firstBookableTime) {
		this.firstBookableTime = firstBookableTime;
	}

	public Integer getRoom_qty() {
		return room_qty;
	}

	public void setRoom_qty(Integer room_qty) {
		this.room_qty = room_qty;
	}
	
	public String getBizValue() {
		return bizValue;
	}

	public void setBizValue(String bizValue) {
		this.bizValue = bizValue;
	}

	public String getCurrencyValue() {
		return currencyValue;
	}

	public void setCurrencyValue(String currencyValue) {
		this.currencyValue = currencyValue;
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

	public String getStarType() {
		return starType;
	}

	public void setStarType(String starType) {
		this.starType = starType;
	}

	public String getLogoPictureName() {
		return logoPictureName;
	}

	public void setLogoPictureName(String logoPictureName) {
		this.logoPictureName = logoPictureName;
	}

	public String getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}

	public String getEngAddress() {
		return engAddress;
	}

	public void setEngAddress(String engAddress) {
		this.engAddress = engAddress;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getPraciceAndFitmentStr() {
		return praciceAndFitmentStr;
	}

	public void setPraciceAndFitmentStr(String praciceAndFitmentStr) {
		this.praciceAndFitmentStr = praciceAndFitmentStr;
	}

	public List getBusinessList() {
		return businessList;
	}

	public void setBusinessList(List businessList) {
		this.businessList = businessList;
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

	public List getHotelNameAndIdStr() {
		return hotelNameAndIdStr;
	}

	public void setHotelNameAndIdStr(List hotelNameAndIdStr) {
		this.hotelNameAndIdStr = hotelNameAndIdStr;
	}

	public List getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List districtList) {
		this.districtList = districtList;
	}

	public String getServiceStr() {
		return serviceStr;
	}

	public void setServiceStr(String serviceStr) {
		this.serviceStr = serviceStr;
	}

	
}

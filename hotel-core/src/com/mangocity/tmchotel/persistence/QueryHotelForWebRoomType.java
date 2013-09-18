package com.mangocity.tmchotel.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.base.util.WebStrUtil;

/**
 * 酒店网站v2.2查询结果<br>
 * 房型类<br>
 * 
 * @author chenkeming
 * 
 */
public class QueryHotelForWebRoomType implements Serializable {
	
	private static final long serialVersionUID = 1443786632749256672L;

    /**
     * 房型ID
     */
    private String roomTypeId;

    /**
     * 子房型ID
     */
    private String childRoomTypeId;

    /**
     * 房型名称
     */
    private String roomTypeName;

    /**
     * 子房型名称
     */
    private String childRoomTypeName;

    /**
     * 配额类型
     */
    private String quotaType;

    /**
     * 门市价
     */
    private double roomPrice;

    /**
     * 币种
     */
    private String currency;

    /**
     * 币种显示
     */
    // private String currencyStr;
    /**
     * 酒店结果中有几种付款方式, fx = <br>
     * 1 : 只有预付<br>
     * 2 : 只有面付<br>
     * 3 : 两种都有
     */
    private int fk;

    /**
     * 支付方式中文名称字符串
     */
    // private String payStr;
    /**
     * 预付价格
     */
    private double prepayPrice;

    /**
     * 面付价格
     */
    private double payPrice;

    /**
     * 当只有面付或者预付时，保存在这个变量中
     */
    private double itemsPrice;

    /**
     * 当只有面付或者预付时，保存支付方式
     */
    private String payMethod;

    /**
     * 宽带
     */
    private String net;

    /**
     * true : 所有间夜均无价格信息，因此不可预订
     */
    private boolean noOrder;

    /**
     * 关房标志
     */
    private String close_flag;

	/**
	 * 房间数量 add shengwei.zuo hotel2.9.3 2009-09-09
	 */
	private String canRoomNumber;
    
	/**
	 * 床型
     * 
     * <br>
     * 规则： <br>
     * a) 酒店本部系统勾选内容对应网站显示内容为：[大床]对应网站显示[大床]，[双床]对应网站显示[双床]，
     * [单床]对应网站显示[单床]，复选时，如[大床][双床]则对应网站显示[大/双床]。 <br>
     * b) 当某房型的床型同时存在两种或以上床型时，需判断本部后台系统中对应床型的房型是否满房。满房，则
     * 不显示该床型。例如，标准房有大床、双床两种床型，当系统中大床标准房已满房，则该房型的床型只显 示双床。（指定首日作为判断逻辑)
     * 
     */
    private String bedType;
    
    
    /**
     * 设置房型对应的床型列表，以逗号分隔;add by shengwei.zuo  2009-10-26
     */
    private String bedTypeStr;

    /**
     * 早餐类型
     */
    private int breakfastType;

    /**
     * 早餐数量<br>
     * 因为早餐跟支付方式有关,如果只有1种支付方式,则只用本变量,<br>
     * 如果有2种支付方式,则本变量用来保存预付方式的早餐数量
     */
    private int breakfastNum;

    /**
     * 如果有2种支付方式,则本变量用来保存面付方式的早餐数量
     */
    private int breakfastNum1 = 0;

    /**
     * 预付方式价格明细
     */
    private List<QueryHotelForWebSaleItems> saleItems = new ArrayList<QueryHotelForWebSaleItems>();

    /**
     * 面付方式价格明细
     */
    private List<QueryHotelForWebSaleItems> faceItems = new ArrayList<QueryHotelForWebSaleItems>();

    /**
     * 当只有面付或者预付时，保存在这个list中
     */
    private List<QueryHotelForWebSaleItems> itemsList = new ArrayList<QueryHotelForWebSaleItems>();

    /**
     * 存放面付的每天日期和价格集合String，格式为 星期:日期（yyyy-mm-dd）:价格/星期:日期（yyyy-mm-dd）:价格/
     * 注：为了与价格明细的存放方式一致，当只有面付或者预付时，存入这个字段 这样在点预订按钮时传到后面，则在订单页面中直接显示预订的每天时间和价格
     */
    // private String faceDatePriceStr;
    /**
     * 存放预付的每天日期和价格集合String，格式为 星期:日期（yyyy-mm-dd）:价格/星期:日期（yyyy-mm-dd）:价格/
     */
    // private String saleDatePriceStr;
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
     * 预付预定按钮显示条件，yud=4表示因为满房或不可超且配额为0, 因此不可预订
     */
    private int yud = -1;

    /**
     * 面付预定按钮显示条件，mf=4表示因为满房或不可超且配额为0, 因此不可预订
     */
    private int mf = -1;

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
    // private int colCount;
    /**
     * 周天数
     */
    // private List weekNum = new ArrayList();
    /**
     * 列显示占用的行数
     */
    // private int rowNum;
    /**
     * 是否香港中科酒店的房型 ADD BY WUYUN 2009-03-20
     */
    private int roomChannel;

    /**
     * 香港中科酒店预订日期内最小可订房间数 ADD BY WUYUN 2009-03-25
     */
    private int minRoomNumCts;

    /**
     * 均价 add by haibo.li 电子地图二期
     */
    private double avlPrice;

    /**
     * 
     * @return
     */
    private double oneDayPrice;

    /**
     * 面付首日价
     */
    private double payOneDayPrice;

    /**
     * 面付均价
     */
    private double payAvlPrice;
    
 

	public double getOneDayPrice() {
        return oneDayPrice;
    }

    public void setOneDayPrice(double oneDayPrice) {
        this.oneDayPrice = oneDayPrice;
    }

    public int getMinRoomNumCts() {
        return minRoomNumCts;
    }

    public void setMinRoomNumCts(int minRoomNumCts) {
        this.minRoomNumCts = minRoomNumCts;
    }

    /**
     * hotel2.6 预订按钮显示 1表示显示，0表示灰显 add by zhineng.zhuang 2009-02-26
     */
    private String bookButtonenAble;

    /**
     * hotel2.6 预订按钮显示 1表示显示，0表示灰显 add by wuyun 2009-06-16
     */
    private String bookButtonenAbleForPrepay;

    /**
     * 预订提示：在某日期之前预订，且要预订此房型，必须连住4晚，且入住日期必须包括某天。 hotel2.6 add by zhineng.zhuang 2009-02-26
     */
    private String bookHintNotMeet;

    /**
     * 预付专用 预订提示：在某日期之前预订，且要预订此房型，必须连住4晚，且入住日期必须包括某天。 add by wuyun v2.6 2009-06-16
     */
    private String bookHintNotMeetForPrepay;

    /**
     * hotel2.6 面转预标记, 必须:1, 允许:2, 不许:3
     * 
     * @author shengwei.zuo 2009-03-30
     */
    private int payToPrepay;

    /** hotel2.9.2加了4个参数@author chenjiajie 2009-07-22 begin * */

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
     * 房间其实设施 结果是用中文字符串
     */
    private String roomOtherEquipment;

    /** hotel2.9.2加了4个参数@author chenjiajie 2009-07-22 end * */

    public int getHasPromo() {
        return hasPromo;
    }

    public void setHasPromo(int hasPromo) {
        this.hasPromo = hasPromo;
    }

    public int getBreakfastNum1() {
        return breakfastNum1;
    }

    public void setBreakfastNum1(int breakfastNum1) {
        this.breakfastNum1 = breakfastNum1;
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

    public String getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(String roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public String getChildRoomTypeId() {
        return childRoomTypeId;
    }

    public void setChildRoomTypeId(String childRoomTypeId) {
        this.childRoomTypeId = childRoomTypeId;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public String getChildRoomTypeName() {
        return childRoomTypeName;
    }

    public void setChildRoomTypeName(String childRoomTypeName) {
        this.childRoomTypeName = childRoomTypeName;
    }

    public double getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(double roomPrice) {
        this.roomPrice = roomPrice;
    }

    public int getWeekTotal() {
        return weekTotal;
    }

    public void setWeekTotal(int weekTotal) {
        this.weekTotal = weekTotal;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getFk() {
        return fk;
    }

    public void setFk(int fk) {
        this.fk = fk;
    }

    public int getYud() {
        return yud;
    }

    public void setYud(int yud) {
        this.yud = yud;
    }

    public int getMf() {
        return mf;
    }

    public void setMf(int mf) {
        this.mf = mf;
    }

    public List<QueryHotelForWebSaleItems> getSaleItems() {
        return saleItems;
    }

    public void setSaleItems(List<QueryHotelForWebSaleItems> saleItems) {
        this.saleItems = saleItems;
    }

    public List<QueryHotelForWebSaleItems> getFaceItems() {
        return faceItems;
    }

    public void setFaceItems(List<QueryHotelForWebSaleItems> faceItems) {
        this.faceItems = faceItems;
    }

    public List<QueryHotelForWebSaleItems> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<QueryHotelForWebSaleItems> itemsList) {
        this.itemsList = itemsList;
    }

    public double getPrepayPrice() {
        return prepayPrice;
    }

    public void setPrepayPrice(double prepayPrice) {
        this.prepayPrice = prepayPrice;
    }

    public double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(double payPrice) {
        this.payPrice = payPrice;
    }

    public double getItemsPrice() {
        return itemsPrice;
    }

    public void setItemsPrice(double itemsPrice) {
        this.itemsPrice = itemsPrice;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getNet() {
        return net;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public String getBedType() {
        return bedType;
    }

    public void setBedType(String roomState, int qty) {
        if (null != roomState && 0 < roomState.length()) {
            this.bedType = WebStrUtil.getWebRoomStatue(roomState, qty);
        } else {
            this.bedType = "";
        }
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    public boolean isNoOrder() {
        return noOrder;
    }

    public void setNoOrder(boolean noOrder) {
        this.noOrder = noOrder;
    }

    public int getBreakfastNum() {
        return breakfastNum;
    }

    public void setBreakfastNum(int breakfastNum) {
        this.breakfastNum = breakfastNum;
    }

    public String getClose_flag() {
        return close_flag;
    }

    public void setClose_flag(String close_flag) {
        this.close_flag = close_flag;
    }

    public int getCanBook() {
        return canBook;
    }

    public void setCanBook(int canBook) {
        this.canBook = canBook;
    }

    public String getCantBookReason() {
        return cantBookReason;
    }

    public void setCantBookReason(String cantBookReason) {
        this.cantBookReason = cantBookReason;
    }

    /**
     * 根据breakfastNum来构造早餐信息字符串
     * 
     * @return
     */
    public String getBreakfastStr() {
        return WebStrUtil.getWebBreakfastStr(this.breakfastNum);
    }

    /**
     * 根据breakfastNum1来构造早餐信息字符串
     * 
     * @return
     */
    public String getBreakfast1Str() {
        return WebStrUtil.getWebBreakfastStr(this.breakfastNum1);
    }

    public int getBreakfastType() {
        return breakfastType;
    }

    public void setBreakfastType(int breakfastType) {
        this.breakfastType = breakfastType;
    }

    public String getBookButtonenAble() {
        return bookButtonenAble;
    }

    public void setBookButtonenAble(String bookButtonenAble) {
        this.bookButtonenAble = bookButtonenAble;
    }

    public String getBookHintNotMeet() {
        return bookHintNotMeet;
    }

    public void setBookHintNotMeet(String bookHintNotMeet) {
        this.bookHintNotMeet = bookHintNotMeet;
    }

    public void setBedType(String bedType) {
        this.bedType = bedType;
    }

    public int getPayToPrepay() {
        return payToPrepay;
    }

    public void setPayToPrepay(int payToPrepay) {
        this.payToPrepay = payToPrepay;
    }

    public int getRoomChannel() {
        return roomChannel;
    }

    public void setRoomChannel(int roomChannel) {
        this.roomChannel = roomChannel;
    }

    public String getBookHintNotMeetForPrepay() {
        return bookHintNotMeetForPrepay;
    }

    public void setBookHintNotMeetForPrepay(String bookHintNotMeetForPrepay) {
        this.bookHintNotMeetForPrepay = bookHintNotMeetForPrepay;
    }

    public String getBookButtonenAbleForPrepay() {
        return bookButtonenAbleForPrepay;
    }

    public void setBookButtonenAbleForPrepay(String bookButtonenAbleForPrepay) {
        this.bookButtonenAbleForPrepay = bookButtonenAbleForPrepay;
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

    public String getRoomOtherEquipment() {
        return roomOtherEquipment;
    }

    public void setRoomOtherEquipment(String roomOtherEquipment) {
        this.roomOtherEquipment = roomOtherEquipment;
    }

    public double getAvlPrice() {
        return avlPrice;
    }

    public void setAvlPrice(double avlPrice) {
        this.avlPrice = avlPrice;
    }

    public double getPayAvlPrice() {
        return payAvlPrice;
    }

    public void setPayAvlPrice(double payAvlPrice) {
        this.payAvlPrice = payAvlPrice;
    }

    public double getPayOneDayPrice() {
        return payOneDayPrice;
    }

    public void setPayOneDayPrice(double payOneDayPrice) {
        this.payOneDayPrice = payOneDayPrice;
    }
	public String getCanRoomNumber() {
		return canRoomNumber;
	}
	public void setCanRoomNumber(String canRoomNumber) {
		this.canRoomNumber = canRoomNumber;
	}

	public String getBedTypeStr() {
		return bedTypeStr;
	}
	
	 /**
     * 根据房态字符串设置网站房型对应的床型列表
     * add by shengwei.zuo 2009-10-26
     * @param bedTypeStr
     * @return
     */
	public void setBedTypeStr(String bedTypeStr) {
		
		 if (null != bedTypeStr && 0 < bedTypeStr.length()) {
	            this.bedTypeStr = WebStrUtil.getWebRoomBedType(bedTypeStr);
	        } else {
	            this.bedTypeStr = "";
	        }
	}
	
}

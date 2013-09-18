package com.mangocity.hotel.search.vo;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.common.vo.AlertInfoVO;
import com.mangocity.hotel.common.vo.HotelPromoVO;
import com.mangocity.hotel.search.sort.SortedCommodity;




/**
 * 
 * 酒店网站v2.2查询结果<br>
 * 商品类<br>
 * 
 * @author zengyong
 * 
 */
public class CommodityVO extends SortedCommodity implements SerializableVO {
	
    public CommodityVO(){}
	private static final long serialVersionUID = 4884058131631491381L;
    
	/**
	 * 商品ID
	 * priceTypeId+"
	 */
	protected long commodityId;
	
    /**
	 * 价格类型ID
	 */
	
	protected long priceTypeId;
	
	/**
	 * 是否有供应商(为了控制第三方价格是否显示)
	 */
	private boolean hasSupplier;
	
	/**
	 * 房型ID
	 */
	protected long roomtypeId;
	/**
	 * 商品名称
	 */
	protected String commodityName;
	
	/**
	 * 直联方式
	 */
	protected String hdltype;

	/**
	 * 是否显示
	 */
	protected boolean show = true;
	
	/**
	 * 能否预订
	 */
	protected boolean canBook = true;
	
	/**
	 * 不能预订原因
	 */
	protected String cantbookReason;
	
	/**
	 * 最大可预订房间数
	 */
	protected String maxRooms;
	
	/**
	 * 能预订房间数量(首日配额)
	 */
	protected String canRoomNumber;

	/**
	 * 商品排名
	 */
	protected int orders;

	/**
	 *是否有促销信息 
	 */
	protected boolean flagHasPromo;
	
	/**
	 * 促销信息
	 */
	protected List<HotelPromoVO> promoLst = new ArrayList<HotelPromoVO>();
	
	/**
	 * 提示信息
	 */
	protected List<AlertInfoVO> alertLst = new ArrayList<AlertInfoVO>();
	
	/**
	 * 价格类型，未来对于商品
	 */
	protected List<SaleItemVO> SaleInfoList = new ArrayList<SaleItemVO>(5);

	 /**
      * 组合商品数量
      */
	protected long commodityCount = 0L;
  
     /**
      * 早餐类型
      */
	protected String breakfastType;

     /**
      * 币种
      */
	protected String currency;
	
	//币种符号 
	protected String currencySymbol;
	
     /**
      * 是否无条件担保
      */
	protected String needAssure;
     /**
      * 是否满房
      */
	protected boolean isRoomFull;
       
     /**
      * 优惠数(连住,打折,住M送N)
      */
	protected boolean hasFavourable;
     /**
      * 是否返现
      */
	protected boolean hasReturnCash;
       
       /**
        * 是否立减
        */
	protected boolean haspromptlyReduce;
       
	  /**
     * 是否可以面转预 （可以，必须，不需）
     */
    private String payToprepayType;
	// --------button处理---------------------------------
	/**
	 * button的显示值
	 */
	private String buttonValue;
	/**
	 * button是否亮
	 */
	private boolean buttonShow;
	/**
	 * buttonalert提示信息
	 */
	private String buttonAlert;
	
	//-----------------显示start--------------------------
	 /**
     * 门市价
     */
	protected String roomPrice;

    /**
     * 均价
     */
	protected String avlPrice;

    /**
     * 首日价
     */
	protected String oneDayPrice;
    
	/**
	 * 总返现金额
	 */
	protected String returnCashNum;
	
	/**
	 * 日均返现
	 */
	protected String avlReturnCashNum;
	
	/**
	 * 返现币种和value
	 */
	protected String returnCashWithCurrency;
	
    /**
     * 宽带
     */
	protected String net;
	
	 /**
     * 早餐数量<br>
     * 因为早餐跟支付方式有关,如果只有1种支付方式,则只用本变量,<br>
     * 如果有2种支付方式,则本变量用来保存预付方式的早餐数量
     */
	protected String breakfastNum;
	
	/**
	 * 支付方式
	 */
	protected String payMethod;
	
	protected String payMethodStr;
	
	/**
	 * 是否展示会员价
	 */
	protected boolean showMemberPrice;
	
	//-----------------显示end--------------------------
    
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
     * 周行数
     */
    private int weekTotal;

	public long getCommodityId() {
		return commodityId;
	}

	public void setCommodityId(long commodityId) {
		this.commodityId = commodityId;
	}

	public long getPriceTypeId() {
		return priceTypeId;
	}

	public void setPriceTypeId(long priceTypeId) {
		this.priceTypeId = priceTypeId;
	}

	public long getRoomtypeId() {
		return roomtypeId;
	}

	public void setRoomtypeId(long roomtypeId) {
		this.roomtypeId = roomtypeId;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public String getHdltype() {
		return hdltype;
	}

	public void setHdltype(String hdltype) {
		this.hdltype = hdltype;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public boolean isCanBook() {
		return canBook;
	}

	public void setCanBook(boolean canBook) {
		this.canBook = canBook;
	}

	public String getCantbookReason() {
		return cantbookReason;
	}

	public void setCantbookReason(String cantbookReason) {
		this.cantbookReason = cantbookReason;
	}

	public String getMaxRooms() {
		return maxRooms;
	}

	public void setMaxRooms(String maxRooms) {
		this.maxRooms = maxRooms;
	}

	public String getCanRoomNumber() {
		return canRoomNumber;
	}

	public void setCanRoomNumber(String canRoomNumber) {
		this.canRoomNumber = canRoomNumber;
	}

	public int getOrders() {
		return orders;
	}

	public void setOrders(int orders) {
		this.orders = orders;
	}

	public boolean isFlagHasPromo() {
		return flagHasPromo;
	}

	public void setFlagHasPromo(boolean flagHasPromo) {
		this.flagHasPromo = flagHasPromo;
	}


	public long getCommodityCount() {
		return commodityCount;
	}

	public void setCommodityCount(long commodityCount) {
		this.commodityCount = commodityCount;
	}

	public String getBreakfastType() {
		return breakfastType;
	}

	public void setBreakfastType(String breakfastType) {
		this.breakfastType = breakfastType;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getNeedAssure() {
		return needAssure;
	}

	public void setNeedAssure(String needAssure) {
		this.needAssure = needAssure;
	}

	public boolean isRoomFull() {
		return isRoomFull;
	}

	public void setRoomFull(boolean isRoomFull) {
		this.isRoomFull = isRoomFull;
	}

	public boolean isHasFavourable() {
		return hasFavourable;
	}

	public void setHasFavourable(boolean hasFavourable) {
		this.hasFavourable = hasFavourable;
	}

	public boolean isHasReturnCash() {
		return hasReturnCash;
	}

	public void setHasReturnCash(boolean hasReturnCash) {
		this.hasReturnCash = hasReturnCash;
	}

	public boolean isHaspromptlyReduce() {
		return haspromptlyReduce;
	}

	public void setHaspromptlyReduce(boolean haspromptlyReduce) {
		this.haspromptlyReduce = haspromptlyReduce;
	}

	public String getPayToprepayType() {
		return payToprepayType;
	}

	public void setPayToprepayType(String payToprepayType) {
		this.payToprepayType = payToprepayType;
	}

	public String getButtonValue() {
		return buttonValue;
	}

	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}

	public boolean isButtonShow() {
		return buttonShow;
	}

	public void setButtonShow(boolean buttonShow) {
		this.buttonShow = buttonShow;
	}

	public String getButtonAlert() {
		return buttonAlert;
	}

	public void setButtonAlert(String buttonAlert) {
		this.buttonAlert = buttonAlert;
	}

	public String getRoomPrice() {
		return roomPrice;
	}

	public void setRoomPrice(String roomPrice) {
		this.roomPrice = roomPrice;
	}

	public String getAvlPrice() {
		return avlPrice;
	}

	public void setAvlPrice(String avlPrice) {
		this.avlPrice = avlPrice;
	}

	public String getOneDayPrice() {
		return oneDayPrice;
	}

	public void setOneDayPrice(String oneDayPrice) {
		this.oneDayPrice = oneDayPrice;
	}

	public String getReturnCashNum() {
		return returnCashNum;
	}

	public void setReturnCashNum(String returnCashNum) {
		this.returnCashNum = returnCashNum;
	}

	public String getNet() {
		return net;
	}

	public void setNet(String net) {
		this.net = net;
	}

	public String getBreakfastNum() {
		return breakfastNum;
	}

	public void setBreakfastNum(String breakfastNum) {
		this.breakfastNum = breakfastNum;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getBedType() {
		return bedType;
	}

	public void setBedType(String bedType) {
		this.bedType = bedType;
	}

	public int getWeekTotal() {
		return weekTotal;
	}

	public void setWeekTotal(int weekTotal) {
		this.weekTotal = weekTotal;
	}

	public List<HotelPromoVO> getPromoLst() {
		return promoLst;
	}

	public void setPromoLst(List<HotelPromoVO> promoLst) {
		this.promoLst = promoLst;
	}

	public List<AlertInfoVO> getAlertLst() {
		return alertLst;
	}

	public void setAlertLst(List<AlertInfoVO> alertLst) {
		this.alertLst = alertLst;
	}

	public List<SaleItemVO> getSaleInfoList() {
		return SaleInfoList;
	}

	public void setSaleInfoList(List<SaleItemVO> saleInfoList) {
		SaleInfoList = saleInfoList;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public String getReturnCashWithCurrency() {
		return returnCashWithCurrency;
	}

	public void setReturnCashWithCurrency(String returnCashWithCurrency) {
		this.returnCashWithCurrency = returnCashWithCurrency;
	}

	public String getAvlReturnCashNum() {
		return avlReturnCashNum;
	}

	public void setAvlReturnCashNum(String avlReturnCashNum) {
		this.avlReturnCashNum = avlReturnCashNum;
	}

	public String getPayMethodStr() {
		return payMethodStr;
	}

	public void setPayMethodStr(String payMethodStr) {
		this.payMethodStr = payMethodStr;
	}

	public boolean isHasSupplier() {
		return hasSupplier;
	}

	public void setHasSupplier(boolean hasSupplier) {
		this.hasSupplier = hasSupplier;
	}

	public boolean isShowMemberPrice() {
		return showMemberPrice;
	}

	public void setShowMemberPrice(boolean showMemberPrice) {
		this.showMemberPrice = showMemberPrice;
	}

  

	
   
	
}

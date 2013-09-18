package com.mangocity.hotel.search.service.assistant;

import java.util.ArrayList;
import java.util.List;

public class Commodity {
	
	
	
	/**
	 * 商品ID(价格类型和支付方式确定一种商品)
	 * priceTypeId+"_"+payMethod
	 */
	private String commodityId;
	/**
	 * 价格类型ID
	 */
	
	private long priceTypeId;
	
	/**
	 * 是否有供应商(为了控制第三方价格是否显示)
	 */
	private boolean hasSupplier;
	
	/**
	 * 房型ID
	 */
	private long roomtypeId;
	/**
	 * 商品名称
	 */
	private String commodityName;
	
	
	/**
	 * 支付方式
	 */
	private String payMethod;
	
	/**
	 * 面转预
	 */
	private String payToPrepay;
	
	
	/**
	 * 直联方式
	 */
	private String hdltype;

	/**
	 * 是否显示
	 */
	private boolean show;
	
	/**
	 * 能否预订
	 */
	private boolean canBook;
	
	
	/**
	 * 不能预订原因
	 */
	private String cantbookReason;
	
	/**
	 * 最大可预订房间数
	 */
	private int maxRooms;
	

	/**
	 * 配额模式,在店或进店
	 */
	private String quotaPattern;
	
	/**
	 * 商品排名
	 */
	private int orders;

	/**
	 * 免费宽带
	 */
	private boolean freeNet;
	/**
	 * 促销信息
	 */
	List<HotelPromo> promoLst;
	
	/**
	 * 提示信息
	 */
	List<AlertInfo> alertLst;
	
	/**
	 * 该商品的销售信息列表
	 */
	private List<SaleInfo> liSaleInfo;

	
	/**
	 * 该价格类型是否能预订,用于排序
	 */
	private boolean canBookRoomType;
	
	/**
	 * 该价格类型的最低价,用于排序
	 */
	private double minPirceRoomType;
	
	
	
	
	
	
	 /**
      * 组合商品数量
      */
     private Long commodityCount = 0L;
     /**
      * 床型列表
		1,2,3分别代表大床\双床\单床.如果某商品有大床和双床,那么就赋值为1,2
      */
     private String bedtype = "";
     
     /**
     * 所拥有的所有床型
     */
    private String bedAll = "";
          
     /**
      * 早餐数量
      */
     private Long breakfastnumber = 0L;
     /**
      * 币种
      */
     private String currency = "";
     /**
      * 是否无条件担保
      */
     private String needAssure = "";
     /**
      * 是否满房
      */
     private boolean isRoomFull = false;
     
     
     /**
      * 优惠数(连住,打折,住M送N)
      */
     private boolean hasFavourable;
     /**
      * 是否返现
      */
       private boolean hasReturnCash;
       
       /**
        * 是否立减
        */
       private boolean haspromptlyReduce;    
       
       /**
        * 是否展示会员价
        */
       private boolean showMemberPrice;
       
       
	public String getCommodityId() {
		return commodityId;
	}

	public void setCommodityId(String commodityId) {
		this.commodityId = commodityId;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public List<HotelPromo> getPromoLst() {
		if(null == promoLst) {
			promoLst = new ArrayList<HotelPromo>();
		}
		return promoLst;
	}

	public void setPromoLst(List<HotelPromo> promoLst) {
		this.promoLst = promoLst;
	}

	public List<AlertInfo> getAlertLst() {
		if(null == alertLst) {
			alertLst = new ArrayList<AlertInfo>();
		}
		return alertLst;
	}

	public void setAlertLst(List<AlertInfo> alertLst) {
		this.alertLst = alertLst;
	}

	public long getPriceTypeId() {
		return priceTypeId;
	}

	public void setPriceTypeId(long priceTypeId) {
		this.priceTypeId = priceTypeId;
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

	public String getQuotaPattern() {
		return quotaPattern;
	}

	public void setQuotaPattern(String quotaPattern) {
		this.quotaPattern = quotaPattern;
	}

	public boolean isCanBook() {
		return canBook;
	}

	public void setCanBook(boolean canBook) {
		this.canBook = canBook;
	}



	public boolean isCanBookRoomType() {
		return canBookRoomType;
	}

	public void setCanBookRoomType(boolean canBookRoomType) {
		this.canBookRoomType = canBookRoomType;
	}

	public double getMinPirceRoomType() {
		return minPirceRoomType;
	}

	public void setMinPirceRoomType(double minPirceRoomType) {
		this.minPirceRoomType = minPirceRoomType;
	}

	public long getRoomtypeId() {
		return roomtypeId;
	}

	public void setRoomtypeId(long roomtypeId) {
		this.roomtypeId = roomtypeId;
	}

	public int getOrders() {
		return orders;
	}

	public void setOrders(int orders) {
		this.orders = orders;
	}

	public String getCantbookReason() {
		return cantbookReason;
	}

	public void setCantbookReason(String cantbookReason) {
		this.cantbookReason = cantbookReason;
	}

	public Long getCommodityCount() {
		return commodityCount;
	}

	public void setCommodityCount(Long commodityCount) {
		this.commodityCount = commodityCount;
	}

	public String getBedtype() {
		return bedtype;
	}

	public void setBedtype(String bedtype) {
		this.bedtype = bedtype;
	}

	public Long getBreakfastnumber() {
		return breakfastnumber;
	}

	public void setBreakfastnumber(Long breakfastnumber) {
		this.breakfastnumber = breakfastnumber;
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

	public int getMaxRooms() {
		return maxRooms;
	}

	public void setMaxRooms(int maxRooms) {
		this.maxRooms = maxRooms;
	}

	public boolean isFreeNet() {
		return freeNet;
	}

	public void setFreeNet(boolean freeNet) {
		this.freeNet = freeNet;
	}

	public List<SaleInfo> getLiSaleInfo() {
		if(null == liSaleInfo) {
			liSaleInfo = new ArrayList<SaleInfo>();
		}
		return liSaleInfo;
	}

	public void setLiSaleInfo(List<SaleInfo> liSaleInfo) {
		this.liSaleInfo = liSaleInfo;
	}

	public String getBedAll() {
		return bedAll;
	}

	public void setBedAll(String bedAll) {
		this.bedAll = bedAll;
	}

	public String getPayToPrepay() {
		return payToPrepay;
	}

	public void setPayToPrepay(String payToPrepay) {
		this.payToPrepay = payToPrepay;
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

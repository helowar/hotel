package com.mangocity.webnew.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 酒店网站v2.2查询结果<br>
 * 房型类<br>
 * 
 * @author chenkeming
 * 
 */
public class QueryHotelForSalesRoomType implements Serializable {
	
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
     * 预付方式价格明细
     */
    private List<QueryHotelForSalesSaleItems> saleItems = new ArrayList<QueryHotelForSalesSaleItems>();

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

	public String getQuotaType() {
		return quotaType;
	}

	public void setQuotaType(String quotaType) {
		this.quotaType = quotaType;
	}

	public double getRoomPrice() {
		return roomPrice;
	}

	public void setRoomPrice(double roomPrice) {
		this.roomPrice = roomPrice;
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

	public List<QueryHotelForSalesSaleItems> getSaleItems() {
		return saleItems;
	}

	public void setSaleItems(List<QueryHotelForSalesSaleItems> saleItems) {
		this.saleItems = saleItems;
	}

  }

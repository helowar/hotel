package com.mangocity.hotel.base.persistence;

import java.util.Date;

public class HtlOnSale {
	
	//上架销售id
	private long onsaleId;
	
	//商品id
	private long commodityId;
	
	//销售渠道id
	private long saleChannelId;
	
	//售卖开始日期
	private Date saleStartDate;
	
	//售卖结束日期
	private Date saleEndDate;
	
	//售卖状态
	private int saleState;
	
	//入住开始日期
	private Date checkStartDate;
	
	//入住结束日期
	private Date checkEndDate;
	
	//创建日期
    private Date createDate;
    
    //创建人
    private String creator;
    
    //修改日期
    private Date modifyDate;
    
    //修改人
    private String modifier;
    
    //加幅渠道商品关系ID
    private int chCdInId;

	public long getOnsaleId() {
		return onsaleId;
	}

	public void setOnsaleId(long onsaleId) {
		this.onsaleId = onsaleId;
	}

	public long getCommodityId() {
		return commodityId;
	}

	public void setCommodityId(long commodityId) {
		this.commodityId = commodityId;
	}

	public long getSaleChannelId() {
		return saleChannelId;
	}

	public void setSaleChannelId(long saleChannelId) {
		this.saleChannelId = saleChannelId;
	}

	public Date getSaleStartDate() {
		return saleStartDate;
	}

	public void setSaleStartDate(Date saleStartDate) {
		this.saleStartDate = saleStartDate;
	}

	public Date getSaleEndDate() {
		return saleEndDate;
	}

	public void setSaleEndDate(Date saleEndDate) {
		this.saleEndDate = saleEndDate;
	}

	public int getSaleState() {
		return saleState;
	}

	public void setSaleState(int saleState) {
		this.saleState = saleState;
	}

	public Date getCheckStartDate() {
		return checkStartDate;
	}

	public void setCheckStartDate(Date checkStartDate) {
		this.checkStartDate = checkStartDate;
	}

	public Date getCheckEndDate() {
		return checkEndDate;
	}

	public void setCheckEndDate(Date checkEndDate) {
		this.checkEndDate = checkEndDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public int getChCdInId() {
		return chCdInId;
	}

	public void setChCdInId(int chCdInId) {
		this.chCdInId = chCdInId;
	}
}

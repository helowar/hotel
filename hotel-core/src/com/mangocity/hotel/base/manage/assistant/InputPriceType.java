package com.mangocity.hotel.base.manage.assistant;

import java.io.Serializable;

/**
 */
public class InputPriceType implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Long keyId;

    private String priceTypeName;
    
    /**
	 * 页面多渠道预定的值
	 */
    private String preMmultiChannel;
    
    /**
	 * 选中的多渠道预定复选框的值数组
	 */
    private String []multiChannel;
    
    /**
	 * 价格类型中供应商ID
	 */
    private String supplierID;
    
    private String showMemberPrice;

    public Long getKeyId() {
        return keyId;
    }

    public void setKeyId(Long keyId) {
        this.keyId = keyId;
    }

    public String getPriceTypeName() {
        return priceTypeName;
    }

    public void setPriceTypeName(String priceTypeName) {
        this.priceTypeName = priceTypeName;
    }

	public String[] getMultiChannel() {
		return multiChannel;
	}

	public void setMultiChannel(String[] multiChannel) {
		this.multiChannel = multiChannel;
	}

	public String getPreMmultiChannel() {
		return preMmultiChannel;
	}

	public void setPreMmultiChannel(String preMmultiChannel) {
		this.preMmultiChannel = preMmultiChannel;
	}

	public String getSupplierID() {
		return supplierID;
	}

	public void setSupplierID(String supplierID) {
		this.supplierID = supplierID;
	}

	public String getShowMemberPrice() {
		return showMemberPrice;
	}

	public void setShowMemberPrice(String showMemberPrice) {
		this.showMemberPrice = showMemberPrice;
	}
	
}

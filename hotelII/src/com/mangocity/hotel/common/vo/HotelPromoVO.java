package com.mangocity.hotel.common.vo;

import java.util.Date;

import com.mangocity.hotel.search.vo.SerializableVO;
public class HotelPromoVO implements SerializableVO {

	public HotelPromoVO(){}
	
	protected long promoId;
    /**
     * 芒果网优惠信息内容
     */
    protected String promoContent;

    /**
     * 芒果网优惠起止日期字符串
     */
    protected String promoBeginDate;
    
    protected String promoEndDate;

	public long getPromoId() {
		return promoId;
	}

	public void setPromoId(long promoId) {
		this.promoId = promoId;
	}

	public String getPromoContent() {
		return promoContent;
	}

	public void setPromoContent(String promoContent) {
		this.promoContent = promoContent;
	}

	public String getPromoBeginDate() {
		return promoBeginDate;
	}

	public void setPromoBeginDate(String promoBeginDate) {
		this.promoBeginDate = promoBeginDate;
	}

	public String getPromoEndDate() {
		return promoEndDate;
	}

	public void setPromoEndDate(String promoEndDate) {
		this.promoEndDate = promoEndDate;
	}
	
}

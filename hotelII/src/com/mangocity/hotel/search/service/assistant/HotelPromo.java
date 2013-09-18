package com.mangocity.hotel.search.service.assistant;

import java.util.Date;


public class HotelPromo {
	

	protected long promoId;
    /**
     * 芒果网优惠信息内容
     */
    protected String promoContent;

    /**
     * 芒果网优惠起止日期字符串
     */
    protected Date promoBeginDate;
    
    protected Date promoEndDate;

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

	public Date getPromoBeginDate() {
		return promoBeginDate;
	}

	public void setPromoBeginDate(Date promoBeginDate) {
		this.promoBeginDate = promoBeginDate;
	}

	public Date getPromoEndDate() {
		return promoEndDate;
	}

	public void setPromoEndDate(Date promoEndDate) {
		this.promoEndDate = promoEndDate;
	}

  
}

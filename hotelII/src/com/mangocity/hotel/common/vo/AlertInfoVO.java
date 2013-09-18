package com.mangocity.hotel.common.vo;

import com.mangocity.hotel.search.vo.SerializableVO;

public class AlertInfoVO implements SerializableVO {

	public AlertInfoVO(){} 
	/**
     * 提示信息内容
     */
    protected String promoContent;

	
    public String getPromoContent() {
		return promoContent;
	}

	public void setPromoContent(String promoContent) {
		this.promoContent = promoContent;
	}
	 
}

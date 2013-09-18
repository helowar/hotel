package com.mangocity.hotel.order.persistence;

import java.io.Serializable;

/**
 * 
 * 从页面获取的用户信用卡类
 * 
 * @author chenkeming
 */
public class MemberCreditCard implements Serializable {

	private static final long serialVersionUID = 4863368734320905654L;

	/**
     * 信用卡ID
     */
    private Long cardId;

    /**
     * 是否选中
     */
    private boolean isCardActive;

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public boolean isCardActive() {
        return isCardActive;
    }

    public void setCardActive(boolean isCardActive) {
        this.isCardActive = isCardActive;
    }

}

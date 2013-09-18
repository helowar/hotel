package com.mangocity.hotel.order.persistence;

import com.mangocity.util.Entity;

/**
 *    
 *  订单信用卡资料表
 *  
 *  @author chenkeming
 */
public class OrCreditCard implements Entity {

    private static final long serialVersionUID = 4116901530281284564L;

    /**
	 * ID <pk>
	 */
    private Long ID;

    /**
	 * 订单ID <fk> 和OrOrder关联
	 */
    private OrOrder order;
    
    /**
	 * 会员信用卡ID
	 */
    private Long cardId;
    
    /**
	 * 卡种
	 * @author chenkeming Mar 10, 2009 6:39:13 PM
	 */
    private String cardType;
    
    /**
	 * 卡号末四位
	 * @author chenkeming Mar 10, 2009 6:39:20 PM
	 */
    private String cardNo;

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public OrOrder getOrder() {
        return order;
    }

    public void setOrder(OrOrder order) {
        this.order = order;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

}

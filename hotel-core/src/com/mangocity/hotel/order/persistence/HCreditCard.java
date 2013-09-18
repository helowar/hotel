package com.mangocity.hotel.order.persistence;

import com.mangocity.util.HEntity;

/**
 *    
 *  历史订单信用卡资料表
 *  
 *  @author chenkeming
 */
public class HCreditCard implements HEntity {

    private static final long serialVersionUID = -7590458425592754189L;

    /**
	 * 主键 <pk>
	 */
    private Long hisID;

    /**
	 * 历史订单ID <fk> 和HOrder关联
	 */
    private HOrder orderH;
    
    /**
	 * 会员信用卡ID
	 */
    private Long cardId;

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public Long getHisID() {
        return hisID;
    }

    public void setHisID(Long hisID) {
        this.hisID = hisID;
    }

    public HOrder getOrderH() {
        return orderH;
    }

    public void setOrderH(HOrder orderH) {
        this.orderH = orderH;
    }

}

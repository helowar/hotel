
package com.mangocity.hotel.order.persistence;

import com.mangocity.util.Entity;

/** 
 *  会员订单统计信息
 *  
 *  @author chenkeming
 */
public class OrOrderStatistics implements Entity {

    private static final long serialVersionUID = 6840361060017305579L;

    /**
	 * ID <pk>
	 */
    private Long ID;
    
    /**
	 * 会员ID
	 */
    private Long memberId;    

    /**
	 * 平均星级 
	 */
    private double avgStar;

    /**
	 * 平均价格 
	 */
    private double avgPrice;
    
    /**
	 * 订单数
	 */
    private int totalOrder;
    
    /**
	 * noshow订单数
	 */
    private int noshow;
    
    /**
	 * noshow率
	 */
    private double noshowRate;
    
    private String memberCd;
    

    public String getMemberCd() {
		return memberCd;
	}

	public void setMemberCd(String memberCd) {
		this.memberCd = memberCd;
	}

	public double getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(double avgPrice) {
        this.avgPrice = avgPrice;
    }

    public int getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(int totalOrder) {
        this.totalOrder = totalOrder;
    }

    public double getAvgStar() {
        return avgStar;
    }

    public void setAvgStar(double avtStar) {
        this.avgStar = avtStar;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public int getNoshow() {
        return noshow;
    }

    public void setNoshow(int noshow) {
        this.noshow = noshow;
    }

    public double getNoshowRate() {
        return noshowRate;
    }

    public void setNoshowRate(double noshowRate) {
        this.noshowRate = noshowRate;
    }


}

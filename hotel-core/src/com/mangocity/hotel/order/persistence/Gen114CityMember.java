
package com.mangocity.hotel.order.persistence;

import com.mangocity.util.Entity;


/**
 * 
 *  表示城市的114会员
 *  
 *  @author chenkeming
 */
public class Gen114CityMember implements Entity {
    
    private static final long serialVersionUID = -239919322419755346L;

    /**
	 * ID <pk>
	 */
    private Long ID;
    
    /**
	 * 城市114会员编号
	 */
    private String memberCD;
    
    /**
	 * 114会员中文名
	 */
    private String name;

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public String getMemberCD() {
        return memberCD;
    }

    public void setMemberCD(String memberCD) {
        this.memberCD = memberCD;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}

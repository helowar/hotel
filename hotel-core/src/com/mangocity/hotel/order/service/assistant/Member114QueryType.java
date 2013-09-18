package com.mangocity.hotel.order.service.assistant;

import java.io.Serializable;

/**
 * 
 * 用于获取114用户所在省份的114会员列表
 * 
 * @author chenkeming
 */
public class Member114QueryType implements Serializable {
    private String membercd;

    private String name;

    public String getMembercd() {
        return membercd;
    }

    public void setMembercd(String membercd) {
        this.membercd = membercd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package com.mangocity.hotel.order.persistence;

/**
 */
public class MemberOfFollow {

    private Integer id;

    private Long memberid;

    private String followname;

    public String getFollowname() {
        return followname;
    }

    public void setFollowname(String followname) {
        this.followname = followname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getMemberid() {
        return memberid;
    }

    public void setMemberid(Long memberid) {
        this.memberid = memberid;
    }
}

package com.mangocity.hotel.order.persistence;

/**
 */
public class MemberOfLinkman {
    private Integer id;

    private Long memberid;

    private String linkmanname;

    private String linkmanappellation;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLinkmanappellation() {
        return linkmanappellation;
    }

    public void setLinkmanappellation(String linkmanappellation) {
        this.linkmanappellation = linkmanappellation;
    }

    public String getLinkmanname() {
        return linkmanname;
    }

    public void setLinkmanname(String linkmanname) {
        this.linkmanname = linkmanname;
    }

    public Long getMemberid() {
        return memberid;
    }

    public void setMemberid(Long memberid) {
        this.memberid = memberid;
    }

}

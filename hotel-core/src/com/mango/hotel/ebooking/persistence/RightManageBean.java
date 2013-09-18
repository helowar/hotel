/**
 * @author chenjuesu 功能说明：实现了HTL_EBOOKING_RIGHT_MANAGE 的实体类 参数说明：无 创建时间：2009-6-16
 */
package com.mango.hotel.ebooking.persistence;

import java.io.Serializable;

/**
 * @author yangshaojun
 */
public class RightManageBean implements Serializable {
    private static final long serialVersionUID = 885529215448113946L;

    /**
     * 权限ID
     */
    private long rightId;

    /**
     * 人员ID(登陆号)
     */
    private String memberId;

    /**
     * 人员名字
     */
    private String memberName;

    /**
     * 此人员可查看的区域
     */
    private String area;

    public long getRightId() {
        return rightId;
    }

    public void setRightId(long rightId) {
        this.rightId = rightId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

}

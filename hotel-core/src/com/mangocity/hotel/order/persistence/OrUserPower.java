package com.mangocity.hotel.order.persistence;

import com.mangocity.util.Entity;

/**
 * @author zhuangzhineng
 *
 */
public class OrUserPower implements Entity {

    /**
	 * ID <pk>
	 */
    private Long ID;
    
    /**
	 * 中文名
	 */
    private String userCnName;
    
    /**
	 * 用户ID
	 */
    private String userId;
    
    /**
	 * 角色
	 */
    private String role;
    
    /**
	 * 权限
	 */
    private String powerId;
    
    

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPowerId() {
        return powerId;
    }

    public void setPowerId(String powerId) {
        this.powerId = powerId;
    }

    public String getUserCnName() {
        return userCnName;
    }

    public void setUserCnName(String userCnName) {
        this.userCnName = userCnName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }
    
}

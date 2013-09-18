package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 * 
 * 员工工作状态设置log表
 * 
 * @author chenkeming
 */
public class OrWorkStatesLog implements Entity {

    private static final long serialVersionUID = 4996990862367653677L;

    /**
	 * 联系表ID <pk> 
	 */
    private Long ID;

    /**
	 * 登陆ID（工号）
	 */
    private String logonId;

    /**
	 * 操作时间
	 */
    private Date operateTime;
    
    /**
	 * 操作工号
	 */
    private Long operateMan;
    
    /**
	 * 姓名
	 */
    private String name;
    
    /**
	 * 重新选择的部门
	 */
    private int type;
    
    /**
	 * 重新选择的组
	 */
    private String group;
    
    /**
	 * 修改后的状态
	 */
    private int state;    
    
    /**
	 * 修改/删除
	 */
    private int operateType;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }


    public String getLogonId() {
        return logonId;
    }

    public void setLogonId(String logonId) {
        this.logonId = logonId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOperateMan() {
        return operateMan;
    }

    public void setOperateMan(Long operateMan) {
        this.operateMan = operateMan;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
            
}

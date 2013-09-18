package com.mangocity.hotel.order.persistence;

import com.mangocity.hotel.base.persistence.OrWorkStates;
import com.mangocity.util.Entity;

/**
 * 
 * 当前open的操作人员列表，员工工作状态设置表按组别进行拆分后形成
 * 
 * @author chenkeming
 */
public class OrWorkStatesByGroup implements Entity {

    private static final long serialVersionUID = -5537149174981572411L;

    /**
	 * 联系表ID <pk> 
	 */
    private Long ID;

    /**
	 * 登陆ID（工号）
	 */
    private String logonId;

    /**
	 * HRA/日审
	 */
    private int type;
    
    /**
	 * 姓名
	 */
    private String name;
    
    /**
	 * 组别：芒果散客，114，预付，担保，香港组，凝难　（这里只有一个）
	 */
    private int groups;
    
    /**
	 * 工作状态：open/close
	 */
    private int state;
    
    /**
	 * 和OrWorkStates关联
	 */
    private OrWorkStates worker;

    public int getGroups() {
        return groups;
    }

    public void setGroups(int groups) {
        this.groups = groups;
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

    public OrWorkStates getWorker() {
        return worker;
    }

    public void setWorker(OrWorkStates worker) {
        this.worker = worker;
    }
        
}

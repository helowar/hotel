package com.mangocity.hotel.base.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.constant.WorkType;
import com.mangocity.util.Entity;

/**
 * 
 * 员工工作状态设置表
 * 
 * @author chenkeming
 */
public class OrWorkStates implements Entity {

    private static final long serialVersionUID = -7400356141355664811L;

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
	 * 
	 * @see WorkType
	 */
    private int type;
    
    /**
	 * 姓名
	 */
    private String name;
    
    /**
	 * 组别：芒果散客，114，预付，担保，香港组，凝难　（可多选）
	 * 
	 * @see WorkGroup, HraOrderType
	 */
    private String groups;
    
    /**
	 * <br>工作状态：
	 * <br>1:open
	 * <br>0:close
	 */
    private int state;
    
    /**
	 * 创建时间
	 */
    private Date createTime;
    
    /**
	 * 当前获取的订单总数
	 */
    private int total;
    
    /**
	 * 等待回传数
	 */
    private int wait;
    
    /**
	 * 正操作数
	 */
    private int working;
    
    /**
	 * 紧急程度排行前三位的订单数
	 */
    private int emergency;
    
    /**
	 * 分配系数
	 */
    private double assignCoef;
    
    /**
	 * 最后修改人ID
	 */
    private Long modifier;
    
    /**
	 * 最后修改时间
	 */
    private Date modifyTime;
    
    /**
	 * 和OrWorkStatesByGroup关联
	 */
    private List items = new ArrayList();
    
    /**
	 * 上次登陆时间 add by chenjiajie 2008-10-20
	 */
    private Date lastLoginTime;
    /**
     * 记录RSC房控的酒店数量 add by shaljun.yang 
     */
    private Integer rscCount;
    /**
     * 如果是中台员工的话，这里存的是他的技能
     */
    private List<WorkStatesSkill> wskills;

    public List<WorkStatesSkill> getWskills() {
		return wskills;
	}


	public void setWskills(List<WorkStatesSkill> wskills) {
		this.wskills = wskills;
	}


	public OrWorkStates() {
	}
    
    
	public OrWorkStates(Long id, String logonId, int type, String name, String groups, int state, Date createTime, int total, int wait, int working, int emergency, double assignCoef, Long modifier, Date modifyTime, Date lastLoginTime) {
		ID = id;
		this.logonId = logonId;
		this.type = type;
		this.name = name;
		this.groups = groups;
		this.state = state;
		this.createTime = createTime;
		this.total = total;
		this.wait = wait;
		this.working = working;
		this.emergency = emergency;
		this.assignCoef = assignCoef;
		this.modifier = modifier;
		this.modifyTime = modifyTime;
		this.lastLoginTime = lastLoginTime;
	}

	public double getAssignCoef() {
        return assignCoef;
    }

    public void setAssignCoef(double assignCoef) {
        this.assignCoef = assignCoef;
    }

    public int getEmergency() {
        return emergency;
    }

    public void setEmergency(int emergency) {
        this.emergency = emergency;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getWait() {
        return wait;
    }

    public void setWait(int wait) {
        this.wait = wait;
    }

    public int getWorking() {
        return working;
    }

    public void setWorking(int working) {
        this.working = working;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getModifier() {
        return modifier;
    }

    public void setModifier(Long modifier) {
        this.modifier = modifier;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }


	public Integer getRscCount() {
		return rscCount;
	}


	public void setRscCount(Integer rscCount) {
		this.rscCount = rscCount;
	}


    
}

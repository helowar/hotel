package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.hotel.order.constant.MoneyTargetType;
import com.mangocity.hotel.order.constant.MoneyType;
import com.mangocity.util.Entity;

/**
 * 订单金额列表 
 * @author chenkeming Mar 5, 2009 4:28:15 PM
 */
public class OrOrderMoney implements Entity {

    private static final long serialVersionUID = 8874113009070797632L;

    /**
	 * ID <pk>
	 */
    private Long ID;

    /**
	 * 订单ID <fk> 和OrOrder关联
	 */
    private OrOrder order;
    
    /**
	 * 创建时间
	 * @author chenkeming Mar 5, 2009 4:29:13 PM
	 */
    private Date createTime;
    
    /**
	 * 历史订单序号,zero based
	 */
    private int hisNo;
    
    /**
	 * 哪一种金额类型. 1:担保, 2:预付, 3:修改, 4:取消
	 * @author chenkeming Mar 6, 2009 9:37:27 AM
	 * @see MoneyType
	 */
    private int moneyType;
    
    /**
	 * 交易方. 0:会员, 1:酒店. 默认为0
	 * @author chenkeming Mar 5, 2009 4:29:53 PM
	 * @see MoneyTargetType
	 */
    private static int target = MoneyTargetType.CUSTOMER;
    
    /**
	 * 金额,按人民币算
	 * @author chenkeming Mar 5, 2009 4:30:14 PM
	 */
    private double money;
    
    /**
	 * 收/付. 0:应收, 1:应付. 默认为0
	 * @author chenkeming Mar 5, 2009 4:31:17 PM
	 */
    private int direction;
    
    /**
	 * 原因
	 * @author chenkeming Mar 5, 2009 4:31:38 PM
	 */
    private String reason;
    
    /**
	 * 预付时的支付类型. 具体参考PrePayType类
	 * @author chenkeming Mar 5, 2009 4:32:37 PM
	 */
    private int payType;
    
    /**
	 * 卡号/会员编号
	 * @author chenkeming Mar 5, 2009 4:32:55 PM
	 */
    private String payIdent;
    
    /**
	 * 是否有效. 0:无效, 1:有效. 默认为0
	 * @author chenkeming Mar 5, 2009 4:33:19 PM
	 */
    private boolean valid = true;
    
    /**
	 * 是否支付成功. 0:否, 1:成功. 默认为0
	 * @author chenkeming Mar 5, 2009 4:33:51 PM
	 */
    private boolean success = false;
    
    /**
	 * 是否手工修改(适用于担保金额和修改/取消金额的情况). 0:非手工, 1:手工. 默认为0
	 * @author chenkeming Mar 5, 2009 4:34:04 PM
	 */
    private boolean manual = false;
    
    /**
	 * 否已经创建预授权工单,适用于担保情况. 0:否,1:是. 默认为0
	 * @author chenkeming Mar 5, 2009 4:34:21 PM
	 */
    private boolean hasPreAuth = false;
    
    /**
	 * 创建人(工号)
	 * @author chenkeming Mar 5, 2009 4:35:07 PM
	 */
    private String creator;
    
    /**
	 * 最后修改时间
	 * @author chenkeming Mar 5, 2009 4:35:17 PM
	 */
    private Date modifyTime;
    
    /**
	 * 最后修改人(工号)
	 * @author chenkeming Mar 5, 2009 4:35:27 PM
	 */
    private String modifier;
    
    /**
	 * 取消标志
	 * @author chenkeming Mar 6, 2009 11:32:30 AM
	 */
    private boolean cancel = false;

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public OrOrder getOrder() {
        return order;
    }

    public void setOrder(OrOrder order) {
        this.order = order;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean isHasPreAuth() {
        return hasPreAuth;
    }

    public void setHasPreAuth(boolean hasPreAuth) {
        this.hasPreAuth = hasPreAuth;
    }

    public boolean isManual() {
        return manual;
    }

    public void setManual(boolean manual) {
        this.manual = manual;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getPayIdent() {
        return payIdent;
    }

    public void setPayIdent(String payIdent) {
        this.payIdent = payIdent;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        OrOrderMoney.target = target;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public int getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(int moneyType) {
        this.moneyType = moneyType;
    }

    public int getHisNo() {
        return hisNo;
    }

    public void setHisNo(int hisNo) {
        this.hisNo = hisNo;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }


}

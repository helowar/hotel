package com.mangocity.hotel.order.constant;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * 中台优化调用存储过程封装的参数
 * 
 * @author chenjuesu
 * 
 */
public class MidOptPrcParameter {

	/**
	 * 操作类型
	 */
	private int optType;

	/**
	 * 将要操作的订单号
	 */
	private String orderIds;

	/**
	 * 当前用户或要分配的用户
	 */
	private String loginName;

	/**
	 * 当前时间
	 */
	private Date nowTime;

	/**
	 * 重新分配的时间
	 */
	private Date reAssignTime;

	/**
	 * 组别
	 */
	private int groupType;

	/**
	 * 释放时间
	 */
	private int relaxTime;

	/**
	 * 交接原因
	 */
	private int transReason;

	/**
	 * 让此订单在中台执行分配
	 */
	public static final int OPT_ORDERINTOMID = 1;

	/**
	 * 分配订单时：（自动分配的，所以是组内间的）
	 */
	public static final int OPT_AUTOASSIGNORDER = 2;

	/**
	 * 释放订单，交接订单时：（不包括明后九点处理）
	 */
	public static final int OPT_RELAXORTRANSFOR = 3;

	/**
	 * 让此订单退出中台的分配
	 */
	public static final int OPT_ORDEROUTTOMID = 4;

	/**
	 * 还有个明后九点处理的，不算释放，也不算完成，只是把它放到池子里就行了，当然要清空一些状态
	 */
	public static final int OPT_DOITWITHTOMORROW = 5;

	/**
	 * 订单实时监控统计,主管点击进入时，先统计出来，然后直接取表数据即可
	 */
	public static final int OPT_ORDERNOWSTATUAS = 6;
	
	/**
	 * 主管分配订单，分到个人手里
	 */
	public static final int OPT_ADMINASSINGORDER = 4;
	/**
	 * 打开工作状态时，新增一条当天工作记录
	 */
	public static final int OPT_ADDNEWWORKINGRATE = 6;

	public MidOptPrcParameter(){
		nowTime = new Date();
	}
	public MidOptPrcParameter(int optType) {
		this();
		this.optType = optType;
	}

	public int getGroupType() {
		return groupType;
	}

	public void setGroupType(int groupType) {
		this.groupType = groupType;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public Timestamp getNowTime() {
		return new Timestamp(nowTime.getTime());
	}

	public void setNowTime(Date nowTime) {
		this.nowTime = nowTime;
	}

	public int getOptType() {
		return optType;
	}

	public void setOptType(int optType) {
		this.optType = optType;
	}

	public String getOrderIds() {
		return orderIds;
	}

	public void setOrderIds(String orderIds) {
		this.orderIds = orderIds;
	}

	public Timestamp getReAssignTime() {
		if (relaxTime > 0) {
			Calendar now = Calendar.getInstance();
			now.add(Calendar.MINUTE, relaxTime);
			reAssignTime = now.getTime();
		}
		if (null != reAssignTime)
			return new Timestamp(reAssignTime.getTime());
		return new Timestamp(new Date().getTime());
	}

	public void setReAssignTime(Date reAssignTime) {
		this.reAssignTime = reAssignTime;
	}

	public int getRelaxTime() {
		return relaxTime;
	}

	public void setRelaxTime(int relaxTime) {
		this.relaxTime = relaxTime;
	}

	public int getTransReason() {
		return transReason;
	}

	public void setTransReason(int transReason) {
		this.transReason = transReason;
	}

}

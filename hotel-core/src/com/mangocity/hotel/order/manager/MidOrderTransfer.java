package com.mangocity.hotel.order.manager;

import java.util.Calendar;

import org.springframework.jdbc.core.JdbcTemplate;

import com.mangocity.hotel.base.persistence.OrWorkStates;
import com.mangocity.hotel.base.resource.ResourceManager;
import com.mangocity.hotel.order.constant.MidOptPrcParameter;
import com.mangocity.hotel.order.dao.impl.OrOrderDao;
import com.mangocity.hotel.user.UserWrapper;

/**
 * 中台订单流转优化
 * 
 * @author chenjuesu
 * 
 */
public class MidOrderTransfer {

	private OrOrderDao orOrderDao;
	
	 private JdbcTemplate jdbcTemplate;

	/**
     * 资源接口
     */
    private ResourceManager resourceManager;

	/**
	 * 订单流转 chenjuesu editAt 2010-3-29 上午09:28:32
	 * 
	 * @param user
	 *            当前用户
	 * @param orderId
	 *            要流转的订单号
	 * @param toGroup
	 *            要流向的组别
	 * @param transReason
	 *            流转原因
	 */
	public void orderTransTo(UserWrapper user, Long orderId, int toGroup,
			int transReason) {
		relaxOrTransfor(user, orderId, toGroup, transReason, 0);
		handOrderLog(user,Long.valueOf(orderId),"执行订单流转到 " +
				resourceManager.getDescription("res_hraOrderGroup_All", toGroup));
	}


	private void relaxOrTransfor(UserWrapper user, Long orderId, int toGroup,
			int reason, int relaxTime) {
		MidOptPrcParameter mp = new MidOptPrcParameter(
				MidOptPrcParameter.OPT_RELAXORTRANSFOR);
		mp.setGroupType(toGroup);
		mp.setOrderIds(orderId.toString());
		mp.setTransReason(reason);
		mp.setLoginName(user.getLoginName());
		mp.setRelaxTime(relaxTime);
		orOrderDao.execMidOptPrcOrFnc(1, mp);
	}

	/**
	 * 订单释放 chenjuesu editAt 2010-3-29 上午09:28:32
	 * 
	 * @param user
	 *            当前用户
	 * @param orderId
	 *            要释放的订单号
	 * @param relaxTime
	 *            释放时长
	 * @param relaxReason
	 *            释放原因
	 */
	public void orderRelaxTo(UserWrapper user, Long orderId, int relaxTime,
			int relaxReason) {
		relaxOrTransfor(user, orderId, 0, relaxReason, relaxTime);
		handOrderLog(user,Long.valueOf(orderId),"执行订单释放，时长为 "+relaxTime+" 分钟");
	}

	/**
	 * 订单交接(实际为跟单组的明后九点处理) chenjuesu editAt 2010-3-29 上午09:28:32
	 * 
	 * @param user
	 *            当前用户
	 * @param orderId
	 *            要交接的订单号
	 * @param reason
	 *            交接原因
	 */
	public void orderToDoTomorrow(UserWrapper user, Long orderId, int reason) {
		MidOptPrcParameter mp = new MidOptPrcParameter(
				MidOptPrcParameter.OPT_DOITWITHTOMORROW);
		mp.setLoginName(user.getLoginName());
		mp.setOrderIds(orderId.toString());
		mp.setTransReason(reason);
		Calendar reAssign = Calendar.getInstance();
		int hour = reAssign.get(Calendar.HOUR_OF_DAY);
		if(hour >= 9){
			reAssign.add(Calendar.DATE, 1);
		}
		reAssign.set(Calendar.HOUR_OF_DAY,9);
		reAssign.set(Calendar.MINUTE,0);
		reAssign.set(Calendar.SECOND,0);
		mp.setReAssignTime(reAssign.getTime());
		orOrderDao.execMidOptPrcOrFnc(1, mp);
		handOrderLog(user,Long.valueOf(orderId),"执行交接班订单");
	}

	/**
	 * 主管分配订单 chenjuesu editAt 2010-3-29 上午09:28:32
	 * 
	 * @param user
	 *            当前用户
	 * @param orderIds
	 *            要分配的订单号,用逗号隔开 如：1001,1002,1003
	 * @param assignTo
	 *            被分配的工员
	 */
	public void adminAssignOrder(UserWrapper user, String orderIds,
			OrWorkStates worker) {
		MidOptPrcParameter mp = new MidOptPrcParameter(
				MidOptPrcParameter.OPT_ADMINASSINGORDER);
		mp.setLoginName(worker.getLogonId());
		mp.setOrderIds(orderIds);
		orOrderDao.execMidOptPrcOrFnc(2, mp);
		
		//写日志
		String[] orIds = orderIds.split(",");
		String content = "主管手工分配订单给"+worker.getName()+"("+worker.getLogonId()+")";
		for(String orderId : orIds){
			handOrderLog(user,Long.valueOf(orderId),content);
		}
	}
	/**
	 * 日志调用，在一些操作后面调用
	 * chenjuesu editAt 2010-3-29 下午02:46:17
	 * @param roleUser
	 * @param orderId
	 * @param content
	 */
	private void handOrderLog(UserWrapper roleUser, Long orderId,
			String content) {
		String sql = "insert into OR_HANDLELOG (logid,orderid,"
				+ "modifiername,modifierrole,content,modifiedtime,"
				+ "beforestate,afterstate,statechange) values("
				+ "seq_order_handlelog.nextval," + orderId + ",'"
				+ roleUser.getName() + "','" + roleUser.getLoginName() + "','"
				+ content +  "'," + "sysdate,3,3,0" + ")";
		
		jdbcTemplate.update(sql);
	}
	
	/**
	 * 自动分配订单
	 * chenjuesu editAt 2010-3-29 下午05:08:12
	 * @param user
	 */
	public void autoAssignOrder(OrWorkStates user){
		if(null == user || 0 == user.getState())
			return;
		MidOptPrcParameter mp = new MidOptPrcParameter(
				MidOptPrcParameter.OPT_AUTOASSIGNORDER);
		mp.setLoginName(user.getLogonId());
		orOrderDao.execMidOptPrcOrFnc(1, mp);
	}
	/**
	 * 让此订单在中台执行分配或退出中台
	 * chenjuesu editAt 2010-3-29 下午05:12:40
	 * @param orderId
	 */
	public void orderIntoOrOutoMid(UserWrapper user,Long orderId,boolean into){
		MidOptPrcParameter mp = new MidOptPrcParameter();
		if(into)
			mp.setOptType(MidOptPrcParameter.OPT_ORDERINTOMID);
		else
			mp.setOptType(MidOptPrcParameter.OPT_ORDEROUTTOMID);
		mp.setLoginName(user.getLoginName());
		mp.setOrderIds(orderId.toString());
		orOrderDao.execMidOptPrcOrFnc(1, mp);
	}
	/**
	 * 打开工作状态时，新增一条当天工作记录
	 * @param workStates
	 */
	public void createMyWorkingRateToday(OrWorkStates workStates) {
		// TODO Auto-generated method stub
		MidOptPrcParameter mp = new MidOptPrcParameter(MidOptPrcParameter.OPT_ADDNEWWORKINGRATE);
		mp.setLoginName(workStates.getLogonId());
		orOrderDao.execMidOptPrcOrFnc(2, mp);
		
	}
	public int autoAllotOrderBy(OrWorkStates user){
		if(null == user || 0 == user.getState())
			return 0;
		return orOrderDao.autoAllotOrderBy(user.getLogonId());
	}
	
	public OrOrderDao getOrOrderDao() {
		return orOrderDao;
	}

	public void setOrOrderDao(OrOrderDao orOrderDao) {
		this.orOrderDao = orOrderDao;
	}


	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}


	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}


	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}

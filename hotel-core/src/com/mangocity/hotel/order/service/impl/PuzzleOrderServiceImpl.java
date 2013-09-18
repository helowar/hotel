package com.mangocity.hotel.order.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.mangocity.hotel.base.manage.OrWorkStatesManager;
import com.mangocity.hotel.base.persistence.OrWorkStates;
import com.mangocity.hotel.base.resource.Property;
import com.mangocity.hotel.order.constant.EmergencyLevel;
import com.mangocity.hotel.order.constant.MidOptPrcParameter;
import com.mangocity.hotel.order.dao.impl.OrOrderDao;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.service.PuzzleOrderService;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.log.MyLog;

/**
 * 疑难单service
 * 
 * @author:shizhongwen 创建日期:May 11, 2009,3:58:05 PM 描述：
 */
public class PuzzleOrderServiceImpl implements PuzzleOrderService {
	private static final MyLog log = MyLog.getLogger(PuzzleOrderServiceImpl.class);

    protected OrOrderDao orOrderDao;
    
    protected OrWorkStatesManager workStatesManager;

    /**
     * 当前登录权限用户
     */
    protected UserWrapper roleUser;

    private String hopLonginId;

    /**
     * 获得疑难单列表 add by shizhongwen 时间:May 11, 2009 3:59:25 PM
     * 
     * @return
     */
    public List getPuzzLeOrderList(HttpSession session) {
    	
        List puzzleOrderList = new ArrayList();

        roleUser = (UserWrapper) session.getAttribute("onlineRoleUser");
        if (null == roleUser) {
            log.error("roleUser 为空");
            return puzzleOrderList;
        }
        autoAssignPuzzleOrder(roleUser);//自动分配疑难订单
        
        // UserWrapper 中的email 已换成HOP 的登陆账号(注:email @之前的部分)
        String email = roleUser.getEmail();
        if (null == email || "".equals(email)) {
            log.error("roleUser的属性email为空");
            return null;
        }
        if (0 < email.indexOf('@')) {
            hopLonginId = email.substring(0, email.indexOf('@'));
        } else {
            hopLonginId = email;
        }
        List li = findPuzzLeOrderList(hopLonginId);
        if (null != li && !li.isEmpty()) {
            OrOrder order = (OrOrder) li.get(0);
            String str = "提示时间:" + DateUtil.datetimeToString(new Date()) + "\n\n疑难单尚未处理-"
                + order.getOrderCD() + " 酒店:" + order.getHotelName() + ",房型:"
                + order.getRoomTypeName() + "," + DateUtil.dateToString(order.getCheckinDate())
                + "至" + DateUtil.dateToString(order.getCheckoutDate()) + ","
                + order.getRoomQuantity() + "间房.";
            if (order.getEmergencyLevel() <= EmergencyLevel.VIP4) {
                str += "订单紧急!";
            }
            str += "确认现在要打开处理吗?";
            Property prop = new Property(String.valueOf(order.getID()), str);
            puzzleOrderList.add(prop);
        }
        return puzzleOrderList;
    }
    /**
     * 自动分配疑难订单
     * @param user
     */
    public void autoAssignPuzzleOrder(UserWrapper roleUser){
    	OrWorkStates user = workStatesManager.returnWorkStatesBylogonId(roleUser.getLoginName());
    	if(null == user || 0 == user.getState())
			return;
		MidOptPrcParameter mp = new MidOptPrcParameter(
				MidOptPrcParameter.OPT_AUTOASSIGNORDER);
		mp.setLoginName(user.getLogonId());
		orOrderDao.execMidOptPrcOrFnc(1, mp);
    }
    /**
     * 根据中台操作人员账号获得疑难单列表 add by shizhongwen 时间:May 11, 2009 6:36:22 PM
     * 
     * @param assignTo
     * @return
     */
    public List findPuzzLeOrderList(String hopLonginId) {
        String hsql = " from OrOrder where assignTo=? and isStayInMid=1";
        Object[] values = new Object[1];
        values[0] = hopLonginId;
        List li = orOrderDao.query(hsql, values);
        List liRes = new ArrayList();
        if (!li.isEmpty()) {
            for (int i = 0; i < li.size(); i++) {
                OrOrder order = (OrOrder) li.get(i);
                boolean bHas = false;
                if (hopLonginId.equals(order.getModifier())) {
                    bHas = true;
                } else {
                    hsql = "select orderId from or_locked_orders where orderId = ? and locker = ? ";
                    List liLock = orOrderDao.queryByNativeSQL(hsql, new Object[]{order.getID(), hopLonginId});
                    if (null != liLock && !liLock.isEmpty()) {
                        bHas = true;
                    }
                }
                if (!bHas) {
                    liRes.add(order);
                }
            }
        }
        return liRes;
    }

    /**
     * 中台管理员对跟单员的订单量的预警
     * 
     * @author chenkeming Jul 2, 2009 1:39:02 PM
     * @return
     */
    public List getOrderAlert() {
        String hsql = " from OrWorkStates where state=1 and type=1 " +
                "and groups like '%6%' and total>7";
        List li = orOrderDao.query(hsql, null);
        List liRes = new ArrayList();
        if (null != li && !li.isEmpty()) {
            String value = "以下工号的订单量超过了7:";
            for (int i = 0; i < li.size(); i++) {
                OrWorkStates worker = (OrWorkStates) li.get(i);
                value += worker.getName() + "(" + worker.getLogonId() + ")-" + worker.getTotal()
                    + " ";
            }
            liRes.add(new Property("", value));
        }
        return liRes;
    }

    public OrOrderDao getOrOrderDao() {
        return orOrderDao;
    }

    public void setOrOrderDao(OrOrderDao orderDao) {
        this.orOrderDao = orderDao;
    }
	public OrWorkStatesManager getWorkStatesManager() {
		return workStatesManager;
	}
	public void setWorkStatesManager(OrWorkStatesManager workStatesManager) {
		this.workStatesManager = workStatesManager;
	}

}

package com.mangocity.hotel.base.util;

import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.mangocity.hotel.base.constant.WorkType;
import com.mangocity.hotel.base.dao.OrWorkStatesDao;
import com.mangocity.hotel.base.manage.OrWorkStatesManager;
import com.mangocity.hotel.base.persistence.OrWorkStates;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.web.WebUtil;

/**
 * 工作状态工具类
 * 
 * @author chenjiajie
 * 
 */
public class WorkStateUtil {

    /**
     * 记录中台人员最后登陆时间
     * 
     * @param userWrapper
     *            add by chenjiajie
     */
    public static void markOrgMidLoginTime(ServletContext servletContext, UserWrapper userWrapper) {
        OrWorkStatesDao orWorkStatesDao = (OrWorkStatesDao) WebUtil.getBean(servletContext,
            "orWorkStatesDao");
        String loginId = userWrapper.getLoginName();
        String hqlStr = "from OrWorkStates s where s.logonId = ?";
        List workStatesList = orWorkStatesDao.doquery(hqlStr, loginId, false);
        if (0 < workStatesList.size()) {
            OrWorkStates workState = (OrWorkStates) workStatesList.get(0);
            workState.setLastLoginTime(new Date());
            orWorkStatesDao.saveOrUpdate(workState);
        }
    }
    /**
     * 标记房控人员的相关权限
     * addby juesu.chen 2009-12-24
     * @param servletContext
     * @param userWrapper
     */
    public static void markRSCRights(HttpServletRequest request,ServletContext servletContext, UserWrapper userWrapper){
    	if(null != userWrapper && !userWrapper.isRSCUser()){
    		OrWorkStatesManager workStatesManager = (OrWorkStatesManager) WebUtil.getBean(servletContext,
        								"workStatesManager");
	    	workStatesManager.markRSCRights(userWrapper);
	    	HttpSession session = request.getSession();
    		session.setAttribute("onlineRoleUser", userWrapper);
    	}
    }
    
    /**
     * 检查是否房控用户
     * 
     * @author chenkeming Jun 8, 2009 4:20:01 PM
     * @param servletContext
     * @param loginId
     * @return
     */
    public static boolean checkRSCUser(ServletContext servletContext, String loginId) {
        OrWorkStatesDao orWorkStatesDao = (OrWorkStatesDao) WebUtil.getBean(servletContext,
            "orWorkStatesDao");
        return checkRSCUserUseBean(orWorkStatesDao, loginId);
    }

    public static boolean checkRSCUserUseBean(OrWorkStatesDao dao, String loginId) {
        String hqlStr = "from OrWorkStates s where s.logonId = ?";
        List workStatesList = dao.doquery(hqlStr, loginId, false);
        if (0 < workStatesList.size()) {
            OrWorkStates workState = (OrWorkStates) workStatesList.get(0);
            if (workState.getType() == WorkType.RSC) {
                return true;
            }
        }
        return false;
    }
}

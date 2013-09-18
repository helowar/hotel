package com.mango.hotel.ebooking.action;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import com.mango.hotel.ebooking.persistence.LeavewordAnnalBean;
import com.mango.hotel.ebooking.persistence.RightManageBean;
import com.mango.hotel.ebooking.service.ILeavewordAnnalAndRightService;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.hotel.user.UserWrapper;

/**
 * 使用DWR方式异步获取同主题留言
 * 
 * @author chenjuesu
 * 
 */
public class LeavewordAnnalAndRightDWR extends PersistenceAction {
    /**
     * 业务操作类
     */
    private ILeavewordAnnalAndRightService leaAndRightService;

    /**
     * 使用DWR方式异步获取数据
     * 
     * @param topic
     * @return
     */
    public List<LeavewordAnnalBean> queryTopicMsg(String topic) {
        if (!isLogin())
            return null;
        return leaAndRightService.queryTopicMsg(topic);
    }
    /**
     * 标记此留言已读,并且记录处理人
     * @param msgId
     * @return
     */
    public boolean setMsgHasRead(long msgId){
    	if (!isLogin())
            return false;
    	leaAndRightService.setMsgHasReadOrLock(msgId,1,roleUser.getLoginName(),roleUser.getName());
        return true;
    }
    /**
     * 标记此留言未锁
     * @param msgId
     * @return
     */
    public boolean setMsgUnLock(long msgId){
    	if (!isLogin())
            return false;
    	leaAndRightService.setMsgHasReadOrLock(msgId,2,null,null);
        return true;
    }
    /**
     * 使用DWR方式异步删除数据
     * 
     * @param msgId
     * 
     */
    public boolean deleteMsgById(long msgId) {
        if (!isLogin())
            return false;
        leaAndRightService.delLeavewordAnnalById(msgId);
        return true;
    }

    /**
     * 增加一个酒店查看权限人员
     * 
     * @param rmb
     * @return
     */
    public String addRightMember(RightManageBean rmb) {
        if (!isLogin())
            return "增加失败,您未登陆系统";
        // 增加时,防止有重复名,先查看是否存在,如果增加的用户名地区权限不为'NONE',则说明已存在
        String thisRmbArea = leaAndRightService.getHotelAreaByLoginName(rmb.getMemberId());
        if (!"NONE".equals(thisRmbArea)) {
            return "增加失败,此用户名已存在!";
        }
        leaAndRightService.addRightMember(rmb);
        return "增加成功!";
    }

    /**
     * 删除一个酒店查看权限人员
     * 
     * @param rmb
     * @return
     */
    public boolean delRightMemberById(long rmbId) {
        if (!isLogin())
            return false;
        leaAndRightService.delRightMemberById(rmbId);
        return true;
    }

    private boolean isLogin() {
        WebContext wct = WebContextFactory.get();
        HttpSession session = wct.getSession();
        roleUser = (UserWrapper) session.getAttribute("onlineRoleUser");
        return (null == roleUser) ? false : true;
    }

    public ILeavewordAnnalAndRightService getLeaAndRightService() {
        return leaAndRightService;
    }

    public void setLeaAndRightService(ILeavewordAnnalAndRightService leaAndRightService) {
        this.leaAndRightService = leaAndRightService;
    }

}

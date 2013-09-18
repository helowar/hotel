package com.mango.hotel.ebooking.action;

import java.util.Date;
import java.util.Map;

import com.mango.hotel.ebooking.persistence.LeavewordAnnalBean;
import com.mango.hotel.ebooking.service.ILeavewordAnnalAndRightService;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;

/**
 * 留言板Action
 * 
 * @author chenjuesu
 * 
 */
public class LeavewordAnnalAction extends PersistenceAction {
    /**
     * 转向
     */
    private String forwardTo;

    protected static final String QUERYHOTELFORMSG = "queryEbookingHotelForLeaveWord";

    private ILeavewordAnnalAndRightService leaAndRightService;

    public String getForwardTo() {
        return forwardTo;
    }

    public void setForwardTo(String forwardTo) {
        this.forwardTo = forwardTo;
    }

    /**
     * 保存或回复留言
     * 
     * @return
     */
    public String saveLeaveWord() {
        roleUser = getOnlineRoleUser();
        if (null == roleUser)
            return forwardError("您未登陆,不能留言");
        create();// 创建实体,并把相关参数设置好
        LeavewordAnnalBean leavewordAnnal = (LeavewordAnnalBean) getEntity();
        String addressee = (String)super.getParams().get("addressee");
        String[] addrees = addressee.split(",");
        leavewordAnnal.setAddressee(addrees[0]);
        //如果是回复的话，则标记刚才的为已阅读
        if(1 == leavewordAnnal.getRevert()){
        	leaAndRightService.setMsgHasReadOrLock(leavewordAnnal.getLeaveWordID(),1,
        			roleUser.getLoginName(),roleUser.getName());
        	leavewordAnnal.setLeaveWordID(0);
        }
        leavewordAnnal.setAddresser(LeavewordAnnalBean.MANGOCITY);// 设置发件人
        leavewordAnnal.setOperationdate(new Date());// 设置发送日期
        leavewordAnnal.setOperationer(roleUser.getName());// 设置操作人名称
        leavewordAnnal.setOperationerID(roleUser.getLoginName());// 设置操作人ID
        leaAndRightService.saveLeavewordAnnal(leavewordAnnal);
        //为其它酒店设置收件人
        for (int i = 1; i < addrees.length; i++) {
			String addr = addrees[i];
			LeavewordAnnalBean lwan = (LeavewordAnnalBean)leavewordAnnal.clone();
			if(null != lwan){
				lwan.setLeaveWordID(0);
				lwan.setAddressee(addr);
				leaAndRightService.saveLeavewordAnnal(lwan);
			}
		}
        request.setAttribute("msg", "您给酒店留言成功!");
        return PersistenceAction.SAVE_SUCCESS;
    }

    /**
     * 根据forwardTo转向页面
     * 
     * @return
     */
    public String forwardTo() {
        // 如果是转向查询酒店页面
        if (LeavewordAnnalAction.QUERYHOTELFORMSG.equals(forwardTo)) {
            // 设置登陆人能查看的区域以作筛选留言用
            roleUser = getOnlineRoleUser();
            if (null == roleUser)
                return forwardError("您未登陆,不能新建留言");
            request.setAttribute("theAreaLoginerCanCheck", leaAndRightService
                .getHotelAreaByLoginName(roleUser.getLoginName()));
        }
        return forwardTo;
    }

    /**
     * 进入留言板的入口
     * 
     * @return
     */
    public String forward() {
        roleUser = getOnlineRoleUser();
        if (null == roleUser)
            return forwardError("您未登陆,不能查看留言");
        // 设置登陆人能查看的区域以作筛选留言用
        String theAreaLoginerCanCheck = leaAndRightService.getHotelAreaByLoginName(roleUser.getLoginName());
        request.setAttribute("theAreaLoginerCanCheck", theAreaLoginerCanCheck);
        request.setAttribute("loginName", roleUser.getLoginName());
        request.setAttribute("isFromMenu", super.getParams().get("isFromMenu"));
        return PersistenceAction.LIST_ALL;
    }
    /**
     * 取得未读留言数
     * @return
     */
    public String getUnReadMsgCount(){
    	 // 设置登陆人能查看的区域以作筛选留言用
    	roleUser = getOnlineRoleUser();
        String theAreaLoginerCanCheck = leaAndRightService.getHotelAreaByLoginName(roleUser.getLoginName());
    	int unReadMsg = leaAndRightService.getUnReadMsgCount(theAreaLoginerCanCheck);
        request.setAttribute("unReadMsg",unReadMsg);
        return "toUnReadMsgCount";
    }
    /**
     * 准备酒店数据转向新建留言页面
     * 
     * @return
     */
    public String createPre() {
        roleUser = getOnlineRoleUser();
        if (null == roleUser)
            return forwardError("您未登陆,不能新建留言");
        Map params = super.getParams();
        String hotelId = (String) params.get("hotelId");
        String hotelName = (String) params.get("hotelName");
        request.setAttribute("hotelId", hotelId);
        request.setAttribute("hotelName", hotelName);
        return PersistenceAction.CREATE;
    }
    /**
     * 准备酒店数据转向新建留言页面,批量留言
     * 
     * @return
     */
    public String createPreBatch() {
        roleUser = getOnlineRoleUser();
        if (null == roleUser)
            return forwardError("您未登陆,不能新建留言");
        Map params = super.getParams();
        String hotelId = (String) params.get("hotelIds");
        String hotelName = leaAndRightService.getHotelNamesByIds(hotelId);
        request.setAttribute("hotelId", hotelId);
        request.setAttribute("hotelName", hotelName);
        return PersistenceAction.CREATE;
    }
    /**
     * 点击查看留言时根据留言的ID取得实体
     * 
     * @return
     */
    public String getLeaveWordById() {
        String leaveWordID = (String) super.getParams().get("leaveWordID");
        LeavewordAnnalBean leavewordAnnal = leaAndRightService.getLeaveWordById(Long
            .parseLong(leaveWordID));
        //判断是否已锁,如果是已锁，则不许查看
        if(1 == leavewordAnnal.getHasLock()){
        	String msg = "此留言已被 "+leavewordAnnal.getReferer()+" 锁住查看，请稍后再看！";
        	request.setAttribute("hasLockMsg", msg);
        	return PersistenceAction.VIEW;
        }
        // 如果所查看的留言中,如不是自已的发,则记下查阅人,但如果是已经查阅的话，则不在记下查阅人
        roleUser = getOnlineRoleUser();
        String operationerID = leavewordAnnal.getOperationerID();
        if (!roleUser.getLoginName().equals(operationerID)) {
            leavewordAnnal.setReferer(roleUser.getName());
            leavewordAnnal.setReferdate(new Date());
        }
        //标记已锁
        leavewordAnnal.setHasLock(1);
        leaAndRightService.updateLeavewordAnnal(leavewordAnnal);
        // 如果不是自己发的留言才有回回复功能
        if (roleUser.getLoginName().equals(operationerID))
            request.setAttribute("isCanReply", false);
        else
            request.setAttribute("isCanReply", true);
        request.setAttribute("leavewordAnnal", leavewordAnnal);
        return PersistenceAction.VIEW;
    }
    
    /**
     * 设置实体类型
     * 
     * @return
     */
    protected Class getEntityClass() {
        return LeavewordAnnalBean.class;
    }

    public ILeavewordAnnalAndRightService getLeaAndRightService() {
        return leaAndRightService;
    }

    public void setLeaAndRightService(ILeavewordAnnalAndRightService leaAndRightService) {
        this.leaAndRightService = leaAndRightService;
    }

}

package com.mango.hotel.ebooking.action;

import java.util.Map;

import com.mango.hotel.ebooking.service.IAdjustRoomStateService;
import com.mango.hotel.ebooking.service.ILeavewordAnnalAndRightService;
import com.mangocity.hotel.base.web.webwork.PaginateAction;
import com.mangocity.util.StringUtil;

/**
 * EBooking房态审批Action
 * 
 * @author chenjuesu
 * 
 */
public class AdjustRoomStateAction extends PaginateAction {
    /**
     * 审核状态：0：未审核，2：已审核
     */
    private int auditState;
    
    /**
     * 审核结果：0：否决，1：通过
     */
    private int auditResult;

    /**
     * 页面传入参数
     */
    private Long hotelId;

    /**
     * EBooking留言板和酒店区域查看权限业务接口
     */
    private ILeavewordAnnalAndRightService leaAndRightService;
    
    private IAdjustRoomStateService adjustRoomStateService;

    /**
     * 进入查询页面
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public String forwardToQuery() {
        Map session = super.getSession();
        String userArea = (String) session.get("userArea");
        if(!StringUtil.isValidStr(userArea)){
        	roleUser = super.getCurrentUser();
            // 查询当前用户所在的区域，把其所属区域放入Session中
            if (null != roleUser) {
                userArea = leaAndRightService.getHotelAreaByLoginName(roleUser.getLoginName());
                session.put("userArea", userArea);
            }
        }
        
        return "queryRoomStatHotels";
    }
    @Override
    public String execute() {
    	setUserArea();
        return super.execute();
    }
    
    private void setUserArea(){
    	 Map params = super.getParams();
         Map session = super.getSession();
         String userArea = (String) params.get("userArea");
         // 如果页面有传入userArea参数，则不需要从Session取
         if (StringUtil.isValidStr(userArea)) {
             params.put("userArea", userArea);
         } else {
             userArea = (String) session.get("userArea");
             // 先从Session去当前用户的区域编码
             if (StringUtil.isValidStr(userArea)) {
                 // 返回的区域不是ALL则增加查询条件，否则不放到参数中，即可以查询所有区域
                 if (!userArea.equals("ALL")) {
                     params.put("userArea", userArea);
                 }
             }
             // 如果Session不存在，再从数据库查询
             else {
                 roleUser = super.getCurrentUser();
                 // 查询当前用户所在的区域，当前用户不为空则取用户所在区域，否则查不出结果
                 if (null != roleUser) {
                     userArea = leaAndRightService.getHotelAreaByLoginName(roleUser.getLoginName());
                     if (StringUtil.isValidStr(userArea)) {
                         // 返回的区域不是ALL则增加查询条件，否则不放到参数中，即可以查询所有区域
                         if (!userArea.equals("ALL")) {
                             params.put("userArea", userArea);
                         }
                     }
                     // 取不到区域编码则查不出结果
                     else {
                         params.put("userArea", "NONE");
                     }
                 } else {
                     params.put("userArea", "NONE");
                 }
             }
         }
    }
    public String roomStatHotelsDetail(){
    	return "roomStatHotelsDetail";
    }
    /**
     * 处理房态审核
     * @return
     */
    public String doRoomStateExc(){
    	Map params = super.getParams();
    	 roleUser = super.getCurrentUser();
    	 if(null == roleUser){
    		 return forwardMsg("您未登陆，不能审核！");
    	 }
    	String adjustBeanIds = (String)params.get("adjustBeanIds");
    	if(StringUtil.isValidStr(adjustBeanIds)){
    		adjustRoomStateService.updateRoomState(adjustBeanIds,roleUser,auditResult);
    		request.setAttribute("msg", "操作成功！");
    	}
    	return "doRoomStateExc";
    }
    /** getter and setter * */


    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }


    public int getAuditState() {
        return auditState;
    }

    public void setAuditState(int auditState) {
        this.auditState = auditState;
    }

    public ILeavewordAnnalAndRightService getLeaAndRightService() {
        return leaAndRightService;
    }

    public void setLeaAndRightService(ILeavewordAnnalAndRightService leaAndRightService) {
        this.leaAndRightService = leaAndRightService;
    }
	public int getAuditResult() {
		return auditResult;
	}
	public void setAuditResult(int auditResult) {
		this.auditResult = auditResult;
	}
	public IAdjustRoomStateService getAdjustRoomStateService() {
		return adjustRoomStateService;
	}
	public void setAdjustRoomStateService(
			IAdjustRoomStateService adjustRoomStateService) {
		this.adjustRoomStateService = adjustRoomStateService;
	}

}

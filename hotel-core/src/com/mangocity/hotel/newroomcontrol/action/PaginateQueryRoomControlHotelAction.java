package com.mangocity.hotel.newroomcontrol.action;

import java.util.Map;

import com.mangocity.hotel.base.web.webwork.PaginateAction;
import com.mangocity.hotel.newroomcontrol.service.NewRoomControlService;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.StringUtil;

/**
 * 房控酒店列表查询Action
 * @author chenjiajie
 *
 */
public class PaginateQueryRoomControlHotelAction extends PaginateAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9102503301687335902L;
	
	/**
	 * 房控改版Service
	 */
	private NewRoomControlService newRoomControlService;
	
    @SuppressWarnings("unchecked")
	@Override
    public String execute() {

        Map params = super.getParams();
        UserWrapper user = super.getCurrentUser();
        if(null != user){
        	params.put("loginName", user.getLoginName());
        }
        /*获取酒店列表排序字符串 即order by 后面的字符串 */
        String sortingStr = newRoomControlService.getRoomControlSortingStr();
		if(StringUtil.isValidStr(sortingStr)){
			params.put("sortingStr", sortingStr);
			log.info("sortingStr:"+sortingStr);
		}
        return super.execute();
    }

    /*getter and setter*/
	public NewRoomControlService getNewRoomControlService() {
		return newRoomControlService;
	}

	public void setNewRoomControlService(NewRoomControlService newRoomControlService) {
		this.newRoomControlService = newRoomControlService;
	}
}

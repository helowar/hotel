package com.mangocity.hotel.base.service;

import com.mangocity.hotel.base.service.assistant.OperationParam;

public interface IOperationSetService {
	
	 /** 
     * 获取当前中台订单分配模式
     * 
     * @return
     */
    public OperationParam getAssignMode() ;
	
    public void updateAssignMode(OperationParam sendFax) ;

    public String queryUserOrgInfo(String userLoginId);
    
    /**
     * 根据id查询用户名
     */
    public String[] queryLoginNameAndUserName(String userId);
}

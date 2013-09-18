package com.mangocity.hotel.order.service;

import java.util.List;

import com.mangocity.hotel.order.service.assistant.BaseParams;
import com.mangocity.hotel.order.service.assistant.EverydayParams;
import com.mangocity.hotel.user.UserWrapper;

public interface INewOrderParamService {
    /**
     * 
     * 获取酒店的基本参数
     * @param priceTypeID
     * @param checkinDate
     * @param checkoutDate
     */
    public BaseParams getBaseParams (String priceTypeID,String checkinDate,String checkoutDate,String payMethod,UserWrapper roleUser);
    
    public Boolean isB2Bagent(String agentCode);
       
}

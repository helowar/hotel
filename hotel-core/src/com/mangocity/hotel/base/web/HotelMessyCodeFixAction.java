package com.mangocity.hotel.base.web;

import java.util.Map;

import com.mangocity.hotel.base.service.IHotelMessyCodeFixService;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.StringUtil;

/**
 * 酒店乱码修复Action
 * @author chenjiajie
 *
 */
public class HotelMessyCodeFixAction extends PersistenceAction {
    
    /**
     * 
     */
    private static final long serialVersionUID = -1967364717296181275L;
    /**
     * 酒店乱码修复调用接口 
     */
    private IHotelMessyCodeFixService hotelMessyCodeFixService;
    
    public String fixMessyCode(){
        Map params = super.getParams();
        
        String hotelIdStr = (String) params.get("hotelId");
        
        log.info("订单乱码修复："+hotelIdStr);
        
        if(StringUtil.isValidStr(hotelIdStr)){
            //调用乱码修复的存储过程 
            hotelMessyCodeFixService.callFixMessyCodeProcdure(new Long(hotelIdStr));
        }
        return SUCCESS;
    }

    /** getter and setter **/
    public IHotelMessyCodeFixService getHotelMessyCodeFixService() {
        return hotelMessyCodeFixService;
    }

    public void setHotelMessyCodeFixService(
            IHotelMessyCodeFixService hotelMessyCodeFixService) {
        this.hotelMessyCodeFixService = hotelMessyCodeFixService;
    }
}

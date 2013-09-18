package com.mangocity.hotel.base.service;

import java.io.Serializable;

/**
 * 酒店乱码修复调用接口
 * @author chenjiajie
 *
 */
public interface IHotelMessyCodeFixService extends Serializable {
    
    /**
     * 调用乱码修复的存储过程
     * @param hotelId
     * @return
     */
    public String callFixMessyCodeProcdure(Long hotelId);
}

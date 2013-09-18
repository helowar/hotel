package com.mangocity.outservices.hotelquery;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.service.assistant.HotelInfo;
import com.mangocity.hotel.base.service.assistant.HotelQueryCondition;
import com.mangocity.hweb.persistence.HotelInfoForWeb;
import com.mangocity.hweb.persistence.HotelPageForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebBean;

/**
 * 酒店对外查询接口基础接口
 * @author chenjiajie
 *
 */
public interface IHotelQueryOutService extends Serializable {
    
    /* ===============================参数key定义begin=============================== */
    /**
     * 查询接口调用方，参数列表中Map的key，Map的value存放下面CHANNEL_*的参数
     */
    public static final String KEY_QUERY_CHANNEL = "queryChannel";    
    /* ===============================参数key定义end=============================== */
    
    /* ===============================参数value定义begin=============================== */
    /**
     * TMC渠道
     */
    public static final String CHANNEL_TMC = "tmc";
    
    /**
     * B2B渠道
     */
    public static final String CHANNEL_B2B = "b2b";
    
    /**
     * 诺曼底渠道
     */
    public static final String CHANNEL_NORMANDY = "normandy";
    
    /**
     * 114/南航渠道
     */
    public static final String CHANNEL_114 = "114";

    /**
     * 中航信渠道
     */
    public static final String CHANNEL_ZHX = "zhx";
    
   /**
    * 繁体网站渠道 add by diandian.hou 2010-9-14
    */
    public static final String CHANNEL_FANTI = "fanti";
    /* ===============================参数value定义end=============================== */

    
    /**
     * 测试方法,对qureyBean的cityId操作并返回
     * @param qureyBean
     * @return
     */
    public String testHessian(QueryHotelForWebBean qureyBean);
}

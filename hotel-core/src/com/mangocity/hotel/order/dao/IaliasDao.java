package com.mangocity.hotel.order.dao;

import java.util.List;

/**
 * 根据通过会员的联名商家CODE查询中文名
 * 
 * @author:shizhongwen 创建日期:Feb 19, 2009,5:49:10 PM 描述：
 */
public interface IaliasDao {
    public List queryNamebyCode(String sql);
}

package com.mangocity.hotel.order.service;

import java.util.List;

import javax.servlet.http.HttpSession;

/**
 * 疑难单接口
 * 
 * @author:shizhongwen 创建日期:May 11, 2009,3:53:17 PM 描述：
 */
public interface PuzzleOrderService {

    /**
     * 根据session获得疑难单列表 add by shizhongwen 时间:May 11, 2009 3:56:25 PM
     * 
     * @return
     */
    public List getPuzzLeOrderList(HttpSession session);

    /**
     * 根据中台操作人员账号获得疑难单列表 add by shizhongwen 时间:May 11, 2009 6:29:22 PM
     * 
     * @param assignTo
     * @return
     */
    public List findPuzzLeOrderList(String assignTo);

    /**
     * 中台管理员对跟单员的订单量的预警
     * 
     * @author chenkeming Jul 2, 2009 1:39:02 PM
     * @return
     */
    public List getOrderAlert();

}

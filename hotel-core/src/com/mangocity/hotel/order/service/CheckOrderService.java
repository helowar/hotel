package com.mangocity.hotel.order.service;

import java.util.Date;
import java.util.Map;

import com.mangocity.hdl.hotel.dto.AddExRoomOrderRequest;
import com.mangocity.hotel.order.persistence.OrOrder;

/**
 * CheckOrder 业务逻辑处理
 * 
 * @author:shizhongwen 创建日期:Feb 6, 2009,7:53:05 PM 描述：
 */
public interface CheckOrderService {

    /**
     * 功能:将order和 params的数据封装到 AddExRoomOrderRequest 中 add by shizhongwen 时间:Feb 5, 2009 11:08:14 AM
     * 
     * @param addExRoomOrderRequest
     * @param order
     *            订单
     * @param params
     *            页面传过来的参数集
     * @param sum
     *            AddExRoomOrderRequest
     * @return
     */
    public AddExRoomOrderRequest encapsulationData(AddExRoomOrderRequest addExRoomOrderRequest,
        OrOrder order, Map params);

    /**
     * 封装订单信息(将datemap_sale,datemap_base,sum中的数据封装到OrOrder) add by shizhongwen 时间:Feb 4, 2009
     * 6:15:02 PM
     * 
     * @param order
     * @param datemap_sale
     * @param datemap_base
     * @return
     */
    public OrOrder getNewOrOrder(OrOrder order, Map datemap_sale, Map datemap_base, String sum);

    /**
     * 取得两日期之间的天数,并将该天与销售价格绑定 add by shizhongwen 时间:Feb 3, 2009 10:47:07 AM
     * 
     * @param checkInDate
     *            起始日期
     * @param checkOutDate
     *            终止日期
     * @param params
     *            页面所传过来的参数(Map)
     * @param pricetype
     *            价格类型:(SalePrice,BasePrice)
     * @return
     * @throws NumberFormatException
     *             如 map中存放 key '2009-02-3' value='480' 时间与当天的价格金额一一对应
     */
    public Map dateChange(Date checkInDate, Date checkOutDate, Map params, String pricetype);

}

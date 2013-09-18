package com.mangocity.proxy.delivery.service;

import com.mangocity.delivery.domain.DeliveryInfo;
import com.mangocity.delivery.domain.Gathering;

/**
 * 配送接口。
 * 
 * @author oliverkang
 */
public interface DeliveryInterfaceService {
    /**
     * 创建配送单。
     * 
     * @param type
     *            任务类型。
     * @param deliveryInfo
     *            配送信息。
     * @param gathering
     *            第三方收款信息。
     * @return 配送单编号。
     */
    String createDeliveryBill(int type, DeliveryInfo deliveryInfo, Gathering gathering);
}

package com.mangocity.hotel.order.web;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import com.mangocity.delivery.charge.ChargeService;
import com.mangocity.hotel.base.web.webwork.GenericAction;

/**
 * 配送相关操作Action
 * 
 * @author chenkeming
 * 
 */
public class FulfillAction extends GenericAction {

    private static final long serialVersionUID = 8087266095058168584L;

    /**
     * 配送费用接口
     */
    private ChargeService deliveryChargeService;

    // /**
    // * 资源接口
    // */
    // private ResourceManager resourceManager;

    /**
     * 查看配送费用
     * 
     * @return
     */
    public String showFee() {
        Map params = getParams();
        String deliveryCityID = (String) params.get("deliveryCityID");
        String deliveryZoneID = (String) params.get("deliveryZoneID");
        deliveryCityID = null == deliveryCityID ? "" : deliveryCityID;
        deliveryZoneID = null == deliveryZoneID ? "" : deliveryZoneID;
        try {
            // List<DsCharge> dsCharges = deliveryChargeService.getDsCharges(deliveryCityID,
            // deliveryZoneID);
            List dsCharges = deliveryChargeService.getDsCharges(deliveryCityID, deliveryZoneID);
            request.setAttribute("dsCharges", dsCharges);
        } catch (RemoteException re) {
        	log.error(re.getMessage(),re);
            return forwardError("获取配送费用失败!");
        }

        return "fee";
    }

    // /**
    // * 查看配送单位
    // * @return
    // */
    // public String showUnit() {
    // Map params = getParams();
    // String deliveryCityID = (String)params.get("deliveryCityID");
    // String deliveryZoneID = (String)params.get("deliveryZoneID");
    // try {
    // List<DsCharge> dsCharges = deliveryChargeService.getDsCharges(deliveryCityID,
    // deliveryZoneID);
    // request.setAttribute("dsCharges", dsCharges);
    // } catch(RemoteException re) {
    // return forwardError("获取配送费用失败!");
    // }
    //		
    // return "fee";
    // }

    // String deliveryCityID;
    // String deliveryDate;
    // String interval;
    //	
    // /**
    // * 查看配送资源
    // * @return
    // */
    // public String showRes() {
    // setDeliveryCityID(resourceManager.getDescription("queryBaseData", deliveryCityID));
    //		
    // return "res";
    // }

    public ChargeService getDeliveryChargeService() {
        return deliveryChargeService;
    }

    public void setDeliveryChargeService(ChargeService deliveryChargeService) {
        this.deliveryChargeService = deliveryChargeService;
    }

}

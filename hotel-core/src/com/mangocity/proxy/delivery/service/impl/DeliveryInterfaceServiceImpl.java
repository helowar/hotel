package com.mangocity.proxy.delivery.service.impl;

import java.io.Serializable;
import java.rmi.RemoteException;

import com.mangocity.delivery.bill.DSBillService;
import com.mangocity.delivery.domain.DeliveryInfo;
import com.mangocity.delivery.domain.Gathering;
import com.mangocity.delivery.exception.BaseException;
import com.mangocity.proxy.delivery.service.DeliveryInterfaceException;
import com.mangocity.proxy.delivery.service.DeliveryInterfaceService;
import com.mangocity.util.log.MyLog;

/**
 */
public class DeliveryInterfaceServiceImpl implements DeliveryInterfaceService, Serializable {
	private static final MyLog log = MyLog.getLogger(DeliveryInterfaceServiceImpl.class);

    private DSBillService dSBillService;

    public DSBillService getDSBillService() {
        return dSBillService;
    }

    public void setDSBillService(DSBillService billService) {
        dSBillService = billService;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.mangocity.proxy.delivery.service.DeliveryInterfaceService#createDeliveryBill(int,
     * com.mangocity.delivery.domain.DeliveryInfo, com.mangocity.delivery.domain.Gathering)
     */
    public String createDeliveryBill(int type, DeliveryInfo deliveryInfo, Gathering gathering) {
        log.debug("createDeliveryBill() starts!");

        try {
            return this.dSBillService.createDeliveryBill(type, deliveryInfo, gathering);
        } catch (RemoteException e) {
        	log.error(e.getMessage(), e);
            throw new DeliveryInterfaceException(e.getMessage(), e);
        } catch (BaseException e) {
        	log.error(e.getMessage(), e);
            throw new DeliveryInterfaceException(e.getMessage(), e);
        }
    }
}

package com.mangocity.hotel.sendmessage.service.impl;

import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.hotel.sendmessage.service.AbstractSendMessageSystemDataService;

public class SendHkMacPmsgSystemDataServiceImpl extends AbstractSendMessageSystemDataService {

	public OrParam getSendPMsgSystemData() {
		return systemDataService.getSysParamByName("IS_SEND_HKMAC_PROMOTION_SMS");
	}

	
}

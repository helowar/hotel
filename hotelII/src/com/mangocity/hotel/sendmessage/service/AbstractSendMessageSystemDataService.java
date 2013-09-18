package com.mangocity.hotel.sendmessage.service;

import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.hotel.base.service.SystemDataService;

public abstract class AbstractSendMessageSystemDataService {

	protected SystemDataService systemDataService;

	public abstract OrParam getSendPMsgSystemData();

	public void updateSendPMsgSystemData(OrParam orParam) {

		systemDataService.updateSysParamByName(orParam);
	}

	public void setSystemDataService(SystemDataService systemDataService) {
		this.systemDataService = systemDataService;
	}
}

package com.mangocity.hotel.base.service;

import java.util.List;

import com.mangocity.hotel.base.service.assistant.QuotaQuery;
import com.mangocity.hotel.base.service.assistant.QuotaReturn;

public interface IQuotaControlService {

	/**
	 * 扣配额
	 * 
	 * @param quotaQuery
	 * @return
	 */
	public List<QuotaReturn> deductQuota(QuotaQuery quotaQuery);

	/**
	 * 退配额
	 * 
	 * @param quotaReturnList
	 * @return
	 */
	public Boolean returnQuota(List<QuotaReturn> quotaReturnList);

}

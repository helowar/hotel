package com.mangocity.hotel.base.service;

import java.util.List;

import com.mangocity.hotel.base.service.assistant.QuotaQueryPo;
import com.mangocity.hotel.base.service.assistant.QuotaReturnPo;

/**
 */
public interface IQuotaForCCService {

    /*
     * 扣配额
     */
    public List<QuotaReturnPo> deductQuota(QuotaQueryPo quotaQueryPo);

    /*
     * 退配额
     */
    public List<QuotaReturnPo> returnQuota(QuotaQueryPo quotaQueryPo);

}

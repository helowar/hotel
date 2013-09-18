package com.mangocity.hotel.base.manage;

import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlChangePrice;
import com.mangocity.hotel.base.persistence.HtlChangePriceLog;

/**
 */
public interface IChangePriceManage {

    /**
     * 新建变价工单
     * 
     * @param changePrice
     * @return 变价工单的ID
     */
    public Long createChangePrice(HtlChangePrice changePrice);

    /**
     * 检查这个酒店的变价工单当天是否存在
     * 
     * @param hotelId
     * @return 如果存在就返回true 否则返回false;
     */
    public boolean checkChangePriceExist(long hotelId);

    /**
     * 新建或修改变价工单更产生日志
     * 
     * @param changePrice
     * @param changePriceLog
     * @return 变价工单ID
     */
    public Long createOrUpdateChangePriceWithLog(HtlChangePrice changePrice,
        HtlChangePriceLog changePriceLog);

    /**
     * 更新为变价中
     * 
     * @param changePrice
     * @param changePriceLog
     * @return
     */
    public Long updateChangeing(HtlChangePrice changePrice, HtlChangePriceLog changePriceLog);

    /**
     * 更新为已核查
     * 
     * @param changePrice
     * @param changePriceLog
     * @return
     */
    public Long updateCheck(HtlChangePrice changePrice, HtlChangePriceLog changePriceLog);

    /**
     * 更新为再次跟进
     * 
     * @param changePrice
     * @param changePriceLog
     * @return
     */
    public Long updateAgainChange(HtlChangePrice changePrice, HtlChangePriceLog changePriceLog);

    /**
     * 更新为QA已审核
     * 
     * @param changePrice
     * @param changePriceLog
     * @return
     */
    public Long updateQAcheck(HtlChangePrice changePrice, HtlChangePriceLog changePriceLog);

    /**
     * 更新为再次核查
     * 
     * @param changePrice
     * @param changePriceLog
     * @return
     */
    public Long updateAgainCheck(HtlChangePrice changePrice, HtlChangePriceLog changePriceLog);

    /**
     * 更新为已调整
     * 
     * @param changePrice
     * @param changePriceLog
     * @return
     */
    public Long updateAdjusted(HtlChangePrice changePrice, HtlChangePriceLog changePriceLog);

    /**
     * 通过ID拿到变价工单
     * 
     * @param id
     * @return
     */
    public HtlChangePrice getChangePriceById(Long id);

    /**
     * 得到变价工单历史记录
     * 
     * @param taskCode
     *            工单CD
     * @return
     */
    public List getChangePriceLog(String taskCode);

    public List queryPriceHistory(Map params);
}

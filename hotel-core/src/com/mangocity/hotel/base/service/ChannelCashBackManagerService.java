package com.mangocity.hotel.base.service;
/**
 * 
 * 渠道返利管理类
 * 
 * @author hushunqiang
 * @since v1.0
 */

public interface ChannelCashBackManagerService {

    /**
     * 根据传入的查询条件，查询渠道返现比例
     * 
     * @param projectcode
     *            渠道编码
     * @param prefixreturnAmount
     *            原返利金额
     * @return Double
     * 			  通过渠道设定的返现比例处理后的返利金额
     * @author hushunqiang
     * @date 2013-04-20 15:15:00
     */
    public double getChannelCashBacktRate(String projectcode);
    
    /**
     * 通过渠道返现比例计算最终返现值
     * 
     * @param projectcode
     *            渠道编码
     * @param prefixreturnAmount
     *            原返利金额
     * @return Double
     * 			  通过渠道设定的返现比例处理后的返利金额
     * @author hushunqiang
     * @date 2013-04-20 15:15:00
     */
	public Double getlastCashBackAmount(double cashbackratevalue,Double prefixreturnAmount);
}

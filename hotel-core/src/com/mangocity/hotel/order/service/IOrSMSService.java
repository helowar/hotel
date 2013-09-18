package com.mangocity.hotel.order.service;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrSMSRecv;

public interface IOrSMSService {
	/**
	 * 短信取消定时器work
	 * 放在定时器中，在有关联关系一对多查询时，session有问题，会出现懒加载异常
	 * 放到service层来避免此问题
	 */
	public void work();
	/**
     * 根据order,项目编号aliasId 判断是否要发送验证码,如果要发送验证码同时会save到OrOrderExtInfo
     * @param order
     * @param aliasId
     * @return 0不发验证码，1验证码生成失败，其他返回5位随机码-表示成功生成验证码并保存
     */
    public String getSendStr(Long ID, String aliasId);
	/**
	 * 获取所有接收到的短信
	 * @return
	 */
	List<OrSMSRecv> queryAll();
	/**
	 * 根据手机号和日期获取所有接收短信
	 * @return
	 */
	List<OrSMSRecv> querySMSByMobileAndDate(String mobile,String date,String orderid);
	
	/**
	 * 根据日期获取未处理的短信
	 * @param date
	 * @return
	 */
	List<OrSMSRecv> querySMSByDate(Date date);
	/**
	 * 批量更新
	 * @param orSMSRecv
	 */
	void batchUpdate(List<OrSMSRecv> lst);
	/**
	 * 批量处理短信取消订单
	 * @param lstRecv
	 * @param lstInfo
	 */
	void process(List<OrSMSRecv> lstRecv,List<OrOrder> orderlist);
	
	/**
	 * 根据日期查询已发送短信验证码的订单信息
	 * @param date
	 */
	List<OrOrder> querySMSOrder(Date date);
	/**
	 * 获取序列的下一个值
	 * @param seqName
	 * @return
	 */
	public long getOrParamSeqNextVal(String seqName);
}

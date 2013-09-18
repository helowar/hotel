package com.mangocity.hotel.order.dao;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrSMSRecv;

public interface IOrSMSRecvDao {
	/**
	 * 获取所有接收到的短信
	 * @return
	 */
	List<OrSMSRecv> queryAll();
	/**
	 * 根据日期获取未处理的短信
	 * @param date
	 * @return
	 */
	List<OrSMSRecv> querySMSByDate(Date date);
	/**
	 * 更新orSMSRecv
	 * @param orSMSRecv
	 */
	void update(OrSMSRecv orSMSRecv);
	/**
	 * 批量更新orSMSRecv
	 * @param orSMSRecv
	 */
	void batchUpdate(List<OrSMSRecv> orSMSRecv);
	/**
	 * 根据手机号和日期获取所有接收短信
	 * @return
	 */
	List<OrSMSRecv> querySMSByMobileAndDate(String mobile, String date,String orderid);
	/**
	 * 根据日期查询已发送短信验证码的订单信息
	 * @param date
	 */
	List<OrOrder> querySMSOrder(Date date);
	/**
	 * 插入表extinfo随机码
	 */
	public void addCheckcodeToExtInfoBySql(String checkcode,long orderid);
	/**
	 * 根据orderid删除发送失败后存储到表or_order_sms和or_orderextinfo中的记录
	 * @param orderid
	 */
	public void delOrderSMS(long orderid);
	/**
	 * 获取序列的下一个值
	 * @param seqName
	 * @return
	 */
	long getOrParamSeqNextVal(String seqName);
}

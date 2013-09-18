package com.mangocity.hotel.sendmessage.service;


/**
 * 调用发送信息接口，进行信息发送，同时把发送记录保存到数据库中
 * @author liting
 *
 */
public interface SendPromotionMessageService {

	/**
	 * 发送的促销信息
	 * 
	 */
	public void sendPromotionMessage() throws Exception;
	
	/**
	 * 获取发送的序列，在集群要用到，用于判断是否有集群在执行发送
	 */
	public Long getJobSendPromotionSeqNextVal();
}

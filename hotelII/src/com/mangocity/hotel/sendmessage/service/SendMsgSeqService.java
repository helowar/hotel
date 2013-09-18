package com.mangocity.hotel.sendmessage.service;

/**
 * 发送短信相关的序列的获取
 * @author liting
 *
 */
public interface SendMsgSeqService {
	/**
	 * 根据序列的名字来获取序列
	 * @param seqName
	 * @return
	 */
	public Long getNextSeq(String seqName);
}

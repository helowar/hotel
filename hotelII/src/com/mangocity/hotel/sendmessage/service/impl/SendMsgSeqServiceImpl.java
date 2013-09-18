package com.mangocity.hotel.sendmessage.service.impl;

import com.mangocity.hotel.sendmessage.dao.SendMsgSeqDao;
import com.mangocity.hotel.sendmessage.service.SendMsgSeqService;

public class SendMsgSeqServiceImpl implements SendMsgSeqService {

	private SendMsgSeqDao sendMsgSeqDao;
	public Long getNextSeq(String seqName) {
		// TODO Auto-generated method stub
		return sendMsgSeqDao.getNextSeq(seqName);
	}
	public void setSendMsgSeqDao(SendMsgSeqDao sendMsgSeqDao) {
		this.sendMsgSeqDao = sendMsgSeqDao;
	}

}

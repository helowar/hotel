package com.mangocity.hotel.sendmessage.dao.impl;

import java.math.BigDecimal;
import java.util.List;

import com.mangocity.hotel.sendmessage.dao.SendMsgSeqDao;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class SendMsgSeqDaoImpl  extends GenericDAOHibernateImpl implements SendMsgSeqDao {

	public Long getNextSeq(String seqName) {
		StringBuilder sql=new StringBuilder();
		sql.append("select ");
		sql.append(seqName+".nextval");
		sql.append(" from dual");
		List<BigDecimal> list=super.queryByNativeSQL(sql.toString(), null, null);
		Long nextVal=list.get(0).longValue();
		return nextVal;
	}

}

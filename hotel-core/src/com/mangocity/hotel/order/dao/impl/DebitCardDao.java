package com.mangocity.hotel.order.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.order.dao.IDebitCardDao;
import com.mangocity.hotel.order.persistence.DebitCardHistoryInfo;
import com.mangocity.util.DateUtil;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.log.MyLog;


/**
 */
public class DebitCardDao extends DAOHibernateImpl implements IDebitCardDao, Serializable{
	private static final MyLog log = MyLog.getLogger(DebitCardDao.class);
	/*
	 * 根据会员cd获取对应的借记卡（银联手机支付）的历史使用信息
	 */
	public List getDebitCardHistotyLis(String memberCd){
		List debitCardlis = new ArrayList();
		try{
			String sql = "select d.mobilenumber,d.bankname,d.cardholdername,d.createdate," +
					"d.bankcardnofour,d.accountinprovince,d.accountincity from t_debitcard d where d.membercd='"+memberCd+"'";
			List lis  = this.doquerySQL(sql, false);
			for(int i = 0;i<lis.size();i++){
				DebitCardHistoryInfo debitCardInfo = new DebitCardHistoryInfo();
				Object[] obj = (Object[])lis.get(i);
				if(null != obj[0]){
					debitCardInfo.setMobileNumber(obj[0].toString());
				}
				if(null != obj[1]){
					debitCardInfo.setBankName(obj[1].toString());
				}
				if(null != obj[2]){
					debitCardInfo.setCardHolderName(obj[2].toString());
				}
				if(null != obj[3]){
					debitCardInfo.setCreateDate(DateUtil.stringToDate(obj[3].toString()));
				}
				if(null != obj[4]){
					debitCardInfo.setBankCardNoFour(obj[4].toString());
				}
				if(null != obj[5]){
					debitCardInfo.setAccountinProvince(obj[5].toString());
				}
				if(null != obj[6]){
					debitCardInfo.setAccountinCity(obj[6].toString());
				}
				debitCardlis.add(debitCardInfo);
			}
		}
		catch (Exception e1) {
            log.error("" + e1.getMessage());
        }
		return debitCardlis;
		
	}

}

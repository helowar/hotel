package com.mangocity.hotel.order.dao.impl;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.order.dao.IOrSMSRecvDao;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrSMSRecv;
import com.mangocity.util.DateUtil;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.log.MyLog;

public class OrSMSRecvDaoImpl extends DAOHibernateImpl implements IOrSMSRecvDao {
	private static final MyLog log = MyLog.getLogger(OrSMSRecvDaoImpl.class);
	/**
	 * 获取所有接收到的短信
	 * @return
	 */
	public List<OrSMSRecv> queryAll() {
		return super.getHibernateTemplate().loadAll(OrSMSRecv.class);
	}
	/**
	 * 根据手机号和日期获取所有接收短信
	 * @return
	 */
	public List<OrSMSRecv> querySMSByMobileAndDate(String mobile, String date,String orderid){
		Date begin = DateUtil.stringToDateMain(date+" 00:00:00", "yyyy-MM-dd HH:mm:SS");
		Date end = DateUtil.stringToDateMain(date+" 23:59:59", "yyyy-MM-dd HH:mm:SS");
		String hql="from OrSMSRecv s where s.fromno=? and s.recvtime >=? and s.recvtime<=? and (s.orderid is null or s.orderid=?) order by s.recvtime desc";
		List<OrSMSRecv> lst = super.getHibernateTemplate().find(hql,new Object[]{mobile,begin,end,orderid});
		return lst;
	}
	/**
	 * 根据日期获取未处理的短信 status=0
	 * @param date
	 * @return
	 */
	public List<OrSMSRecv> querySMSByDate(Date date) {
		String hql="from OrSMSRecv o where o.recvtime>=? and o.recvtime<=? and o.status=0";
		Date begin = DateUtil.stringToDateMain(DateUtil.dateToString(date)+" 00:00:00", "yyyy-MM-dd HH:mm:SS");
		Date end = DateUtil.stringToDateMain(DateUtil.dateToString(date)+" 23:59:59", "yyyy-MM-dd HH:mm:SS");
		return super.getHibernateTemplate().find(hql,new Object[]{begin,end});
	}
	/**
	 * 更新 OrSMSRecv
	 * @param orSMSRecv
	 */
	public void update(OrSMSRecv orSMSRecv) {
		super.getHibernateTemplate().update(orSMSRecv);
	}; 

	/**
	 * 批量save/update orSMSRecv
	 * @param orSMSRecv
	 */
	public void batchUpdate(List<OrSMSRecv> lst){
		super.getHibernateTemplate().saveOrUpdateAll(lst);
	}
	
	/**
	 * 根据日期查询已发送短信验证码的订单信息,订单状态非已撤单
	 * @param date
	 */
	public List<OrOrder> querySMSOrder(Date date){
		List<OrOrder> orderlist = null;
		Date dayPre = DateUtil.getPreviousDate(date);
		Date dayAfter = DateUtil.getNextDate(date);
		//hql
		String hqlStr="select distinct o from OrOrder o where o.checkinDate>? and o.checkinDate<? and o.orderState!=14 "
			+"and exists(select 1 from OrOrderExtInfo f where f.order.ID = o.ID and f.type='03')";
		orderlist = super.getHibernateTemplate().find(hqlStr, new Object[]{dayPre,dayAfter});
		return orderlist;
	}
	/**
	 * 插入表extinfo随机码
	 */
	public void addCheckcodeToExtInfoBySql(String checkcode, long orderid) {
		String sql = "insert into or_orderextinfo(id,orderid,type,context) values(seq_order_extinfo.nextval,"+orderid+",'03','"+checkcode+"') ";
		super.getSession().createSQLQuery(sql).executeUpdate();
	}
	/**
	 * 根据orderid删除发送失败后存储到表or_order_sms和or_orderextinfo中的记录
	 * @param orderid
	 */
	public void delOrderSMS(long orderid){
		String sql1 = "delete from or_order_sms m where m.orderid="+orderid;
		String sql2 = "delete from or_orderextinfo f where f.orderid="+orderid+" and f.type='03' ";
		log.info("清空or_order_sms对应的数据，orderid="+orderid);
		super.getSession().createSQLQuery(sql1).executeUpdate();
		
		log.info("清空or_orderextinfo对应的数据，orderid="+orderid);
		super.getSession().createSQLQuery(sql2).executeUpdate();
	}
	/**
	 * 获取序列的下一个值
	 * @param seqName
	 * @return
	 */
	public long getOrParamSeqNextVal(final String seqName){
		return super.getSequenceNextVal(seqName);
	}
}

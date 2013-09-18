package com.mangocity.hotel.order.dao.impl;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.mangocity.hotel.base.persistence.CommisionAdjust;
import com.mangocity.hotel.base.persistence.HtlBookModifyField;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.persistence.HtlTaxCharge;
import com.mangocity.hotel.order.constant.MidOptPrcParameter;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.hotel.order.dao.IOrOrderDAO;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderOfSMS;
import com.mangocity.hotel.order.persistence.Order;
import com.mangocity.hotel.order.persistence.Orderstations;
import com.mangocity.hotel.order.persistence.VOrOrder;
import com.mangocity.hotel.order.service.assistant.MemberAliasConstants;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.dao.GenericDAOHibernateImpl;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.util.log.MyLog;

/**
 * OrOrder Dao类
 * 
 * @author yong.zeng
 * 
 */
public class OrOrderDao extends GenericDAOHibernateImpl implements IOrOrderDAO {
	private static final MyLog log = MyLog.getLogger(OrOrderDao.class);

	/**
	 * 保存(新增)order
	 * @param order
	 * @return
	 */
    public Serializable insertOrder(OrOrder order) {
        return save(order);
    }
    /**
     * 根据ID获取OrOrder
     * @param orderID
     * @return
     */
    public OrOrder getOrder(Serializable orderID) {
        return load(OrOrder.class, orderID);
    }
    /**
     * 更新订单
     * @param order
     */
    public void updateOrder(OrOrder order) {
        super.saveOrUpdate(order);
    }
    /**
     * 根据ID获取OrOrder(直接操作数据库)
     * @param orderID
     * @return
     */
    public OrOrder findOrOrder(Serializable orderID) {
        return super.get(OrOrder.class, orderID);
    }
    
    /**
     * 根据orderCD获取订单对象
     * 
     * @param orderCD
     * @return
     */
    public OrOrder getCustomOrderByCD(String orderCD) {
    	OrOrder order = null ;
        String hql = "from OrOrder where orderCD=?";
        Object[] params = new Object[1];
        params[0] = orderCD;
        List<OrOrder> orderLst = query(hql, params);
        if(!orderLst.isEmpty()){
        	order = orderLst.get(0);
            Hibernate.initialize(order.getPaymentList());
            Hibernate.initialize(order.getFaxList());
            Hibernate.initialize(order.getFulfill());
        }
        return order;
    }

    /**
     * 根据ID获取OrOrder对象
     * 
     * @param orderID
     * @return
     */
    public OrOrder getCustomOrderByID(Long orderID) {
    	OrOrder order = null ;
        String hql = "from OrOrder where ID=?";
        Object[] params = new Object[1];
        params[0] = orderID;
        List<OrOrder> orderLst = query(hql, params);
        if(!orderLst.isEmpty()){
        	order = orderLst.get(0);
            Hibernate.initialize(order.getOrderItems());
            Hibernate.initialize(order.getFellowList());
            Hibernate.initialize(order.getRemark());
            Hibernate.initialize(order.getFaxList());
        }
        return order;
    }

    /**
     * 加入日志到DB
     * @param sql
     */
    public void insertlog(String sql) {
    	super.queryByNativeSQL(sql, 0, 0, null,null);
    }

   
    /**
     * @param contractId
     * @return 根据酒店ID,起始日期获取税费设定信息
     */
    public List getTaxCharges(Long contractId, Date beginDate, Date endDate) {
        // TODO Auto-generated method stub
        return super.queryByNamedQuery("queryTaxCharges1", new Object[] { contractId, beginDate,
            endDate });
    }
    
    
    /**
     * 根据日期查询当天适合条件的订单
     * 
     * @param triggerdate
     * @return
     */
    public List<OrOrderOfSMS> getOrderforTrigger(String triggerdate) {
        //changed by luoguangming 2012年3月31日16:40:03 qc-4185
        String sql2 = "select o.orderId,o.mobile,o.type,o.memberstate,o.hotelname,o.latestarrivaltime  from or_order o where o.illusive=0  and o.emergencyLevel>0 "
        	+"and o.orderstate>=3 and o.orderstate!=14 "
        	+"and o.checkinDate = to_date('"+triggerdate+"','yyyy-MM-dd') "
        	+"and o.hotelconfirm=1 and o.customerconfirm=1 and o.confirmtype=3 "
        	+"and not exists(select 1 from t_member m where m.membercd=o.membercd and  m.aliasid in('"
        	+MemberAliasConstants.BJWT+"','"
        	+MemberAliasConstants.GDLT+"','"
        	+MemberAliasConstants.NHZY+"','"
        	+MemberAliasConstants.DL+"')) ";
        
        List list = super.queryByNativeSQL(sql2, 0, 0, null,null);
        List<OrOrderOfSMS> smsLst = getOrOrderOfSMS(list);
        return smsLst;

    }



    /**
     *根据HotelId获取合作方
     * 
     * @param hotelId
     * @return
     */
    public List<String> getChannelByHotelId(String hotelId) {
        String sql = "select h.cooperate_channel from  htl_hotel_ext h" + " where h.hotel_id="
            + hotelId;

        List list = super.queryByNativeSQL(sql, 0, 0, null,null);
        if (null == list || 0 == list.size()) {
            return null;
        }
        List<String> results = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            String channel = (String) list.get(i);
            results.add(channel);
        }
        return results;
    }
    
    
    public boolean validateSid(String sid) {
    	boolean flag=true;
    	String sql = "select o.id from or_orderextinfo o where o.context='"+sid+"'";
    	List list = super.queryByNativeSQL(sql, 0, 0, null,null);
    	if (null == list || 0 == list.size()) {
    		flag=false;
        }
		return flag;
	}
    
    /**
     * 查询预订条款修改字段定义
     * 
     * @param hotelId
     * @return
     */
    public List<HtlBookModifyField> queryHtlBookModifyField(Long hotelId) {
        return null;
    }

    /**
     * 查询酒店的所有预订条款计算规则
     * 
     * @param hotelId
     * @return
     */
    public List<HtlBookModifyField> queryHtlBookCaulClause(Long hotelId) {
        return null;
    }

    /**
     * 查询酒店某时段的预订条款计算规则
     * 
     * @param hotelId
     * @return
     */
    public List<HtlBookModifyField> queryHtlBookCaulClause(Long hotelId, Date checkInDate,
        Date checkOutDate) {
        return null;
    }
    
    /**
     * 需要移到pricedao
     * @param abselDate
     * @param hotelId
     * @param roomTypeID
     * @param priceTypeId
     * @param payMethod
     * @return
     */
    
	public HtlPrice getPricInfoByFor(Date abselDate, long hotelId,
			long roomTypeID, long priceTypeId,String payMethod){
		String hql = "from HtlPrice where ableSaleDate = ? and  hotelId = ? and  roomTypeId = ? and childRoomTypeId = ? \n"+
                     " and payMethod =? ";
		HtlPrice priceObj =(HtlPrice)super.query(hql, new Object[]{abselDate, hotelId,roomTypeID, priceTypeId,payMethod});
		
		return priceObj;
		
	}
	
	/**
	 * 需要移到pricedao
	 * @param contractId
	 * @param currDate
	 * @return
	 */
	public HtlTaxCharge getCommTaxInfo(long contractId,Date currDate){
		String hql = " from HtlTaxCharge taxcharge where taxcharge.contractId =? and  taxcharge.taxBeginDate <= ?  \n"+
					 " and taxcharge.taxEndDate>= ? and taxcharge.active=1 ";
		HtlTaxCharge taxChargeObj =(HtlTaxCharge)super.query(hql, new Object[]{contractId,currDate,currDate});
		return taxChargeObj;
	}
	
	@SuppressWarnings(value={"deprecation","unchecked"})
    public void getWorkingRateAndOrderStations()
	{
    	MidOptPrcParameter mp = new MidOptPrcParameter(MidOptPrcParameter.OPT_ORDERNOWSTATUAS);
    	
    	execMidOptPrcOrFnc(1,mp);
    }
	
	 /**
     * 调用中台优化的存储过程或函数
     * @param execType 执行类型，1：存储过程，2：函数
     * @param mp 参数
     */
	@SuppressWarnings("deprecation")
    public void execMidOptPrcOrFnc(int execType,MidOptPrcParameter mp){
			String procedureName =null;
			Object[] inParams = null;
			if(1 == execType){
				inParams = new Object[]{Integer.valueOf(mp.getOptType()), mp.getOrderIds(), mp.getLoginName(), 
						mp.getNowTime(), mp.getReAssignTime(), Integer.valueOf(mp.getRelaxTime()), 
						Integer.valueOf(mp.getGroupType()), Integer.valueOf(mp.getTransReason())};
				procedureName = "{ call prc_htlmidoptimization(?,?,?,?,?,?,?,?)}";
			}else{
				inParams = new Object[]{"", Integer.valueOf(mp.getOptType()), mp.getOrderIds(), 
					mp.getNowTime(), mp.getReAssignTime(), mp.getLoginName(), 
					Integer.valueOf(mp.getGroupType()), Integer.valueOf(mp.getRelaxTime())};
		    			    	
				procedureName = "{ ? = call fnc_htlorderassign(?,?,?,?,?,?,?)}";
			}
	
			super.execProcedure(procedureName, inParams);	
    }
    /**
     * proc调用
     * @param loginName
     * @return
     */
    @SuppressWarnings(value={"deprecation","unchecked"})
    public int autoAllotOrderBy(String loginName)
    {
    	//输入参数的索引和参数值键值对
    	Map inParamIdxAndValue = new HashMap(2);
    	inParamIdxAndValue.put(Integer.valueOf(2), loginName);
    	inParamIdxAndValue.put(Integer.valueOf(3), new Timestamp(new Date().getTime()));
    	
    	//输出参数的索引和数据类型键值对
    	Map<Integer, Integer> outParamIdxAndType = new HashMap<Integer, Integer>(1);
    	outParamIdxAndType.put(Integer.valueOf(1), Integer.valueOf(java.sql.Types.INTEGER));
    	
    	int result = 0;
        try 
        {
        	Map<Integer, ?> resultMap = super.execProcedure("{ ? = call fnc_autoorderassign(?,?)}", 
        			inParamIdxAndValue, outParamIdxAndType);
        	
            result = Integer.valueOf(resultMap.get(Integer.valueOf(6)).toString()).intValue();
        } 
        catch (Exception e)
        {
        	log.error(e.getMessage(),e);
        }
        
    	return result;
    }
    
    /**
     * 查询工作量
     * @param loginName
     * @return
     */
	public List getMyWorkingRateByLoginName(String loginName) {
		// TODO Auto-generated method stub
		String hsql = "from Myworkingrate where operatordate=trunc(sysdate) and grouptype < 8 and operatorid = ? order by operatorid";
		return super.query(hsql, new Object[]{loginName});
	}
	
	/**
	 * 得到工作量和工作状态
	 * @param params
	 * @return
	 */
	public List getWorkingRateAndOrderStationsByAjax(Map<String, String> params) {
		// TODO Auto-generated method stub
		String groups = params.get("groups");
		String date = params.get("theDate");
		String hql = null;
		hql = "select os.grouptype,os.orderprocessmount,os.averageprocesstime,os.waitingordermount,os.longestwatingtime,os.averagewaitingtime,os.relaxordermount,os.averagerelaxtime,"
			+ "(select count(ws.logonId) from WorkStatesSkill ws,OrWorkStates ow where ws.logonId = ow.logonId and ws.groupId=os.grouptype and ow.state=1 and trunc(sysdate)=?) as total from Orderstations os where os.operatordate = ?";
		if (StringUtil.isValidStr(groups))
			hql += " and os.grouptype in (" + groups + ")";
		hql += " and os.grouptype < 8 order by os.grouptype";
		Date theDate = DateUtil.getDate(date);
		List result = super.query(hql, new Object[]{theDate,theDate});
		return getOrderStations(result);
	}
	/**
	 * 封装成OrderStations对象
	 * @param result
	 * @return
	 */
	private List getOrderStations(List<Object[]> result){
		List list = new ArrayList();
		for(Object[] objs : result){
			Orderstations os = new Orderstations();
			os.setGrouptype((Integer)objs[0]);
			os.setOrderprocessmount((Integer)objs[1]);
			os.setAverageprocesstime((Integer)objs[2]);
			os.setWaitingordermount((Integer)objs[3]);
			os.setLongestwatingtime((Integer)objs[4]);
			os.setAveragewaitingtime((Integer)objs[5]);
			os.setRelaxordermount((Integer)objs[6]);
			os.setAveragerelaxtime((Integer)objs[7]);
			os.setTotal(((Long)objs[8]).intValue());
			list.add(os);
		}
		return list;
	}
	
	
	/**
	 * 数组转换
	 * @param list
	 * @return
	 */
    private List<OrOrderOfSMS> getOrOrderOfSMS(List list) {
        List<OrOrderOfSMS> smsLst = new ArrayList<OrOrderOfSMS>();
        	if(null!=list){
        		Iterator iter = list.iterator();
        		while(iter.hasNext()){
        			OrOrderOfSMS orsms = new OrOrderOfSMS();
        			Object[] objArr = (Object[])iter.next();
                    orsms.setID(null==objArr[0]?null:Long.parseLong(objArr[0].toString()));
                    orsms.setMobile(null==objArr[1]?"":objArr[1].toString());
                    orsms.setType(null==objArr[2]?0:Integer.parseInt(objArr[2].toString()));
                    orsms.setMemberState(null==objArr[3]?"":objArr[3].toString());
                    orsms.setHotelName(null==objArr[4]?"":objArr[4].toString());
                    orsms.setLatestArrivalTime(null==objArr[5]?"":objArr[5].toString());
                    smsLst.add(orsms);
        			
        		}
        	}

        return smsLst;

    }
    
    /**
     * 根据订单ID获取订单CD
     * 
     * @param orderId	订单ID
     * @return	订单CD
     */
    public String getOrderCDByID(Long orderId) 
    {
    	String hql = "select order.orderCD from OrOrder order where order.ID = ?";
        List<String> resultList = super.query(hql, new Object[]{ orderId });
        
        return resultList.isEmpty()?null: resultList.get(0);
    }
    
    /**
     *
     * 根据hotelid,date主键获取orderList
     *
     * @param hotelId
     * @param checkNight
     * @return
     */
    public List<VOrOrder> getViewOrder(Long hotelId, Date checkNight) {
    	StringBuffer hsql = new StringBuffer("from VOrOrder a where a.hotelId = ? ");
    	hsql.append("and a.payMethod = '"+PayMethod.PAY+"' and a.orderState != "+OrderState.CANCEL);
		hsql.append("and a.ID in (select b.orderID from VOrOrderItem");
		hsql.append(" b where b.hotelId = ? ");
		hsql.append("and b.night = ? and b.show != 1 and  a.orderType = b.auditType ) ");
		hsql.append("and a.orderType in (select b.auditType from VOrOrderItem b where b.hotelId = ? ");
		hsql.append("and b.night = ? and b.show != 1 and  a.orderType = b.auditType ) " + "order by a.ID");
		
        Object[] obj = new Object[] { hotelId, hotelId, checkNight, hotelId, checkNight };

        return query(hsql.toString(), obj, 0, 0, false);
    }
    /**
     * 查询可供日审的订单
     */
    public List<Order> getOrderList(Date checkinDate){
    	return queryByNamedQuery("hQueryOrderForAudit", new Object[] {checkinDate, checkinDate });
    }
    
    public void updateB2bModifyOrderWithOrderState(long orderId, int orderState) {
    	String hql = "update B2bModifyOrderInfo bmo set bmo.orderState = ? where ID = ?";
    	updateByQL(hql, new Object[]{Integer.valueOf(orderState), Long.valueOf(orderId)});
    }
    
    /**
     * 查询日审订单
     * @param date
     * @return
     */
    public List<OrOrder> findSimilarOrder(Date date) {
        return queryByNamedQuery("hQueryAudit_Order", new Object[] { date, date });
    }
    
    
    /**
     * 日审传真获取酒店信息
     * 
     */
    public Object[] getHotelInfoForAuditFax(Long auditId) {
        String hsql = " select a.hotelName, a.checkNight, b.fax  from OrDailyAudit a, OrPaperContact b   where  a.ID = ? and a.hotelId = b.hotelId";

        List itemList = query(hsql, new Object[] { auditId });
        if (itemList.isEmpty()) return null;
        
        return (Object[]) itemList.get(0);
}

/**
     * 获取日审传真信息
     * 
     */
    public List getOrderItemsForAuditFax(Long auditId) {
        String hsql = " select b.orderCD, b.roomTypeName, b.fellowNames, 1 " +
                "as quantity ,b.confirmNo, b.checkinDate, b.checkoutDate, " +
                "a.roomNo, a.hotelId, a.night, a.orderID, a.roomIndex, " +
                "a.fellowName, b.createDate"
            + " from VOrOrderItem a, VOrOrder b, OrDailyAudit c "
            + " where "
            + "	c.ID = ? "
            + "	and a.hotelId = c.hotelId "
            + "	and a.hotelId = b.hotelId "
            + "	and b.hotelId = c.hotelId "
            + "	and c.checkNight = a.night "
            + " 	and a.orderID = b.ID "
            + "   and b.orderState != 14 "
            + "	and a.auditType = b.orderType "
            + "   and a.show != 1 "
            + "   and b.payMethod = 'pay' " + " order by b.ID,a.ID";

       return query(hsql, new Object[] { auditId });
    }
    
    
    public List findSimilarOrder1(Date date) {

        String hsql = "select DISTINCT (a.hotelId), a.hotelName "
            + "from VOrOrder a where  a.orderState != 5 " + "and a.orderState != 13 "
            + "and a.orderState != 14 " + "and a.payMethod = 'pay' "
            + "and a.ID in (select b.orderID from VOrOrderItem b where  " + "b.night = ?)"
            + "order by a.hotelId ";
        return query(hsql, new Object[] { date });
    }
    
    /**
	 * 根据代理商code得到享受政策范围
	 * add by yong.zeng
	 * @param agentcode
	 * @return
	 */
	public int getPolicyScope(String agentcode){
		int result  = 0;
		String sql = "select c.policyscope from b2b_organizaioninfo c where c.agentcode= ? and c.STATUS=1";
		List tempLst  = super.queryByNativeSQL(sql,  new Object[]{ agentcode });
		if(!tempLst.isEmpty()){
			result = Integer.parseInt(tempLst.get(0).toString());
		}
		return result;
	}
	
	public List<CommisionAdjust> getCommisionAdjustByHotelIds(String hotelIds, String agentId, Date startDate, Date endDate) {

		String hql = "from CommisionAdjust c where c.b2BCd = ? and c.hotelId in (" + hotelIds + ") and not exists (" +
				" from CommisionAdjust d where d.adjustID = c.adjustID and ( d.endDate < ? or d.startDate > ?))";
		return super.query(hql, new Object[]{agentId,startDate,endDate});

	}
	
    /**
	 * 根据多个订单号查询订单 
	 * @param orderIds 订单ID字符串
	 * @return
	 */
	public List<OrOrder> getOrOrderList(String orderIds) {
		String hql=" from OrOrder o where o.ID in ("+orderIds+")";
		return super.query(hql, new Object[]{});
	}
	/**
	 * 查询hotelIds下的每个价格类型的佣金调整
	 * 一个价格类型有多个调整，按创建日期排列取最后一个
	 * @param hotelIds
	 * @return
	 */
	public List<CommisionAdjust> getCommisionAdjustByHotelIds(String hotelIds) {
		List<CommisionAdjust> adjustList = new ArrayList<CommisionAdjust>();
		String sql = "select t.adjustid,t.paytype,t.hotel_id,t.child_room_type_id,t.room_type_id,t.valuetype,t.comm_value,t.create_by,to_char(t.create_date,'yyyy-MM-dd HH24:Mi:SS') as createTime from "
						+"(select row_number() over(partition by c.hotel_id,c.room_type_id,c.child_room_type_id,c.paytype order by c.create_date desc) rn,c.* "
                          +"from htl_b2b_adjust_by_hotel c "
                         +"where c.b2bcd='0' and c.hotel_id in("+hotelIds+")) t where rn=1 ";
		List lst = super.queryByNativeSQL(sql, new Object[]{});
		if(lst==null || lst.size()==0) return adjustList;
		for(int i=0;i<lst.size();i++){
			Object[] obj = (Object[]) lst.get(i);
			CommisionAdjust adjust = new CommisionAdjust();
			adjust.setB2BCd("0");
			adjust.setAdjustID(Long.parseLong(String.valueOf(obj[0])));
			adjust.setPayType(String.valueOf(obj[1]));
			adjust.setHotelId(Long.parseLong(String.valueOf(obj[2])));
			adjust.setChildRoomId(Long.parseLong(String.valueOf(obj[3])));
			adjust.setRoomTypeId(Long.parseLong(String.valueOf(obj[4])));
			adjust.setValueType(Long.parseLong(String.valueOf(obj[5])));
			adjust.setComm_value(Double.parseDouble(String.valueOf(obj[6])));
			adjust.setCreateBy(String.valueOf(obj[7]==null ? "":obj[7]));
			adjust.setCreateDate(DateUtil.stringToDateTime(String.valueOf(obj[8])));
			adjustList.add(adjust);
		}
		return adjustList;
	}
	/**
	 * 查询连住包价
	 * @param hotelId
	 * @param priceTypeId
	 * @param currDate
	 * @return
	 */
	public List getFavourable(String hotelIds,Date begin,Date end){
		int nightNum = DateUtil.compare(begin, end);
		String hql = "from HtlFavourableclause t where t.hotelId in ("+hotelIds+") and t.favourableType=? and t.beginDate<=? and t.endDate>=? "
			+" and exists(select 1 from HtlFavouraParameter s where s.favourableclauseEntiy.id=t.id and s.packagerateNight<="+nightNum+")";
		List lst = super.query(hql, new Object[]{"3",begin,end});
		return lst;
	}
	
	/**
	 * 据ID查询订单短信
	 * @param orderID
	 * @return
	 */
	 public OrOrderOfSMS getOrOrderOfSMS(Serializable id) {
	     return get(OrOrderOfSMS.class, id);
	 }
}

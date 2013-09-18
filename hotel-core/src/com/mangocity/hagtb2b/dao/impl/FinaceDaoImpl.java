package com.mangocity.hagtb2b.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mangocity.hagtb2b.condition.StaticInfoQueryCon;
import com.mangocity.hagtb2b.dao.IFinaceDao;
import com.mangocity.hagtb2b.persistence.AgentOrder;
import com.mangocity.hagtb2b.persistence.CommPolicySecond;
import com.mangocity.hagtb2b.persistence.StatisticsInfo;
import com.mangocity.hotel.order.persistence.OrOrderItem;
import com.mangocity.util.DateUtil;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.log.MyLog;

/**
 * 财务实现类
 * @author lixiaoyong
 *
 */
public class FinaceDaoImpl extends DAOHibernateImpl implements IFinaceDao {
	private static final MyLog log = MyLog.getLogger(FinaceDaoImpl.class);
	public void confirmStaticAgent(StatisticsInfo statiInfo)
			throws Exception {
		super.save(statiInfo);
	}

	@SuppressWarnings("unchecked")
	public List<StatisticsInfo> staticAgentOrderQuery(StaticInfoQueryCon con) throws Exception {
		List<StatisticsInfo>  staInfoList = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select STATIID,AGENT_CODE,OPER_ID,STATIS_YEAR,STATIS_MONTH,ORDER_NUM,NIGHTS_NUM,SUM_ACOUNT,ACT_NIGHTS_NUM,ACT_SUM_ACOUNT,COMMISION,CONFIRMED,AGENT_NAME,BACK_COMM,FACTCOMM,COMMRATE from T_STATISTICS_AGENT_ORDER o where 1=1");
		if(con.getStatIds()!=null){
			sql.append(" and o.ORDERID in (").append(con.getStatIds()).append(")");
		}else{
			sql.append(" and o.STATIS_YEAR = '").append(con.getStatYear()).append("'");
			sql.append(" and o.STATIS_MONTH in (").append(con.getStatMonth()).append(")");
			//代理商code
			if(con.getAgentCode()!=null && !"".equals(con.getAgentCode())){
				sql.append(" and o.AGENT_CODE = '").append(con.getAgentCode()).append("'");
			}
			//操作人ID
/*			if(con.getOperId()!=null && !"".equals(con.getOperId())){
				sql.append(" and upper(o.oper_id) like upper('%").append(con.getOperId()).append("%')");
			}
*/			if(0!=con.getConfirmed())sql.append(" and o.CONFIRMED = ").append(con.getConfirmed());
		}
		log.info(sql.toString());
		staInfoList = this.assmebleStatis(super.doquerySQL(sql.toString(), false));
		return staInfoList;
	}
	
	private List<StatisticsInfo> assmebleStatis(List list){
		List<StatisticsInfo>  staInfoList = new ArrayList<StatisticsInfo>();
		StatisticsInfo statsInf = null;
		for(int i=0;i<list.size();i++){
			statsInf = new StatisticsInfo();
			Object[] objArray = (Object[])list.get(i);
			statsInf.setID(Long.parseLong(objArray[0].toString()));
			statsInf.setAgentCode(objArray[1].toString());
			if(null!=objArray[2])statsInf.setOperId(objArray[2].toString());
			statsInf.setStatYear(objArray[3].toString());
			statsInf.setStatMonth(Integer.parseInt(objArray[4].toString()));
			statsInf.setOrderNum(Integer.parseInt(objArray[5].toString()));
			statsInf.setNightsNum(Integer.parseInt(objArray[6].toString()));
			statsInf.setSumAcount(Double.parseDouble(objArray[7].toString()));
			statsInf.setActNightsNum(Integer.parseInt(objArray[8].toString()));
			statsInf.setActSumAcount(Double.parseDouble(objArray[9].toString()));
			statsInf.setCommsion(Double.parseDouble(objArray[10].toString()));
			statsInf.setConfirmed(Integer.parseInt(objArray[11].toString()));
			statsInf.setAgentName(objArray[12].toString());
			statsInf.setBackCommission(Double.parseDouble(objArray[13].toString()));
			statsInf.setFactcomm(Double.parseDouble(objArray[14].toString()));
			statsInf.setCommrate(Double.parseDouble(objArray[15].toString()));
			staInfoList.add(statsInf);
		}
		return staInfoList;
	}

	public List<OrOrderItem> getOrderitem(Long orderId) {
		List<OrOrderItem> retList = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select it.saleprice,it.settlement,it.auditstate,it.orderstate,it.agent_comission from or_orderitem it where 1=1");
		sql.append(" and  it.orderid =").append(orderId);
		log.info(sql.toString());
		retList = this.assmebleItem(super.doquerySQL(sql.toString(), false));
		return retList;
	}
	
	/**
	 * 得到orderitem的总间夜数 add by yong.zeng
	 * @param map
	 * @return
	 */
	public long getOrderitemCount(Map maps) {
		int result = 0;
		
		String orderIdLst = maps.get("orderIdLst")==null?"":maps.get("orderIdLst").toString();
		
		String beginQueryDate = maps.get("beginQueryDate")==null?"":maps.get("beginQueryDate").toString();
		String endQueryDate = maps.get("endQueryDate")==null?"":maps.get("endQueryDate").toString();
		
		String sql = "select count(*) countRow from or_orderitem it where it.saleprice>0 and it.saleprice<99999 ";
		/** 解决订单数量超过1000 in 的BUG add by xiaowei.wang 2011.7.11  begin*/
		int listConunt = Integer.parseInt(maps.get("listSize")==null?"0":maps.get("listSize").toString());
	 	if(!orderIdLst.equals("")){
			if(listConunt>1000){
				sql+= " and it.orderid in(1) ";
				String orderIDListSub = "";
				String[] idArr = orderIdLst.split(",");
				sql+= strConvertForORA01795Helper(idArr,0,orderIDListSub);
			}else{
				sql+= " and it.orderid in("+orderIdLst+") ";
			}
	}
	/** 解决订单数量超过1000 in 的BUG add by xiaowei.wang 2011.7.11  end*/
		if(!beginQueryDate.equals("")){
			sql+=" and it.night>=to_date('"+beginQueryDate+"', 'YYYY-MM-DD')";
		}
		if(!endQueryDate.equals("")){
			sql+=" and it.night<=to_date('"+endQueryDate+"', 'YYYY-MM-DD')";
		}
		result = super.totalNum(sql);
		return result;
	}

	/**
	 * 将idArr 每1000项的拆分开来  add by xiaowei.wang 2011.7.11
	 * @param idArr
	 * @param index
	 * @param orderIDListSub
	 * @return
	 */
	private String strConvertForORA01795Helper(String[] idArr,int index,String orderIDListSub){
		String[] idListSub = new String[1000];
		String idsStr = "";
		if(idArr.length>1000+index*1000){
			System.arraycopy(idArr,index*1000, idListSub, 0, 1000);
			index++;
			for(String id:idListSub){
				idsStr+=id+",";
			}
			if(idsStr.endsWith(","))idsStr = idsStr.substring(0,idsStr.length()-1);
			orderIDListSub += "or it.orderid in ("+idsStr+") ";
			strConvertForORA01795Helper(idArr,index,orderIDListSub);//递归调用
		}else{
			for(int i = index*1000 ;i<idArr.length;i++){
				idsStr +=idArr[i]+",";
			}
			if(idsStr.endsWith(","))idsStr = idsStr.substring(0,idsStr.length()-1);
			orderIDListSub += "or it.orderid in ("+idsStr+") ";
		}
		return orderIDListSub;
	}
	
	private List<OrOrderItem> assmebleItem(List list){
		List<OrOrderItem> retList = new  ArrayList<OrOrderItem>();
		OrOrderItem item = null;
		for(int i=0;i<list.size();i++){
			Object[] objArray = (Object[])list.get(i);
			item = new OrOrderItem();
			if(null!=objArray[0])item.setSalePrice(Double.parseDouble(objArray[0].toString()));
			if(null!=objArray[1])item.setSettlement("0".equals(objArray[1].toString())?true:false);
			if(null!=objArray[2])item.setAuditState(Integer.parseInt(objArray[2].toString()));
			if(null!=objArray[3])item.setOrderState(Integer.parseInt(objArray[3].toString()));
			if(null!=objArray[4])item.setAgentComission(Double.parseDouble(objArray[4].toString()));
			retList.add(item);
		}
		
		return retList;
	}

	public List<AgentOrder> getAgentItem(Long orderId) throws Exception {
		List<AgentOrder> retAList = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select  t.orderid,t.hotel_name,t.room_name,t.child_room_name,t.bed_type,t.room_num,t.checkindate,t.checkoutdate,t.sum_rmb,t.commision,t.ORDERCD  from T_AGENT_ORDER t where t.STATIID=").append(orderId);
		log.info(sql.toString());
		retAList = this.assmbleAgentOrder(super.doquerySQL(sql.toString(), false));
		return retAList;
	}
	public List<AgentOrder> assmbleAgentOrder(List list){
		List<AgentOrder> retList = new  ArrayList<AgentOrder>();
		AgentOrder item = null;
		for(int i=0;i<list.size();i++){
			Object[] objArray = (Object[])list.get(i);
			item = new AgentOrder();
			if(null!=objArray[0])item.setOrderId(Long.parseLong(objArray[0].toString()));
			if(null!=objArray[1])item.setHotelName(objArray[1].toString());
			if(null!=objArray[2])item.setRoomName(objArray[2].toString());
			if(null!=objArray[3])item.setChildRoomName(objArray[3].toString());
			if(null!=objArray[4])item.setBedType(Integer.parseInt(objArray[4].toString()));
			if(null!=objArray[5])item.setRoomNum(Integer.parseInt(objArray[5].toString()));
			if(null!=objArray[6])item.setCheckInDate(DateUtil.getDate(objArray[6].toString()));
			if(null!=objArray[7])item.setCheckOutDate(DateUtil.getDate(objArray[7].toString()));
			if(null!=objArray[8])item.setSumRmb(Double.parseDouble(objArray[8].toString()));
			if(null!=objArray[9])item.setCommision(Double.parseDouble(objArray[9].toString()));
			if(null!=objArray[10])item.setOrderCd(objArray[10].toString());
			retList.add(item);
		}
		return retList;
	}
	
	
	   /**
     * 根据代理商code获取CommPolicySecond对象
     * add by yong.zeng
     * @param agentCode
     * @return
     */
    public CommPolicySecond getSecondPolicy(String agentCode){
    	CommPolicySecond result = null;
    	String hsql = "select c from CommPolicySecond c where c.agentCode='"+agentCode+"' and c.active=1 order by c.ID desc";
    	List<CommPolicySecond> cpLst = super.query(hsql);
    	if(null!=cpLst && !cpLst.isEmpty()){
    		result = cpLst.get(0);
    	}
    	return result;
    }
}

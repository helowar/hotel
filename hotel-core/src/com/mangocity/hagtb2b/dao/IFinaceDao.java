package com.mangocity.hagtb2b.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.mangocity.hagtb2b.condition.StaticInfoQueryCon;
import com.mangocity.hagtb2b.persistence.AgentOrder;
import com.mangocity.hagtb2b.persistence.CommPolicySecond;
import com.mangocity.hagtb2b.persistence.StatisticsInfo;
import com.mangocity.hotel.order.persistence.OrOrderItem;

/**
 * 财务dao层
 * @author lixiaoyong
 *
 */
public interface IFinaceDao {
	/**
	 * 修改统计状态
	 * @return
	 */
	public void confirmStaticAgent(StatisticsInfo statiInfo) throws Exception;;
	/**
	 * 查询统计，可以分单月和多月查询
	 * @return
	 */
	public List<StatisticsInfo> staticAgentOrderQuery(StaticInfoQueryCon con) throws Exception;
	
	public List<OrOrderItem> getOrderitem(Long orderId) throws Exception;
	
	public List<AgentOrder> getAgentItem(Long orderId) throws Exception;
	
	/**
	 * 得到orderitem的总间夜数 add by yong.zeng
	 * @param map
	 * @return
	 */
	public long getOrderitemCount(Map map);
	
	   /**
     * 批量保存
     * 
     * @param entities
     */
    public void saveOrUpdateAll(Collection entities);
    
    /**
     * 根据代理商code获取CommPolicySecond对象
     * add by yong.zeng
     * @param agentCode
     * @return
     */
    public CommPolicySecond getSecondPolicy(String agentCode);
    
    public Object load(Class clazz, Serializable sid);
}

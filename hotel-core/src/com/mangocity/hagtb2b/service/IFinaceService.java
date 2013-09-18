package com.mangocity.hagtb2b.service;

import java.util.List;
import java.util.Map;

import com.mangocity.hagtb2b.condition.StaticInfoQueryCon;
import com.mangocity.hagtb2b.persistence.AgentOrder;
import com.mangocity.hagtb2b.persistence.AgentOrg;
import com.mangocity.hagtb2b.persistence.StatisticsInfo;

public interface IFinaceService {
	
	/**
	 * 更新统计状态
	 * @return
	 * @throws Exception
	 */
	public void confirmStaticAgent(StatisticsInfo statInfo,Map params) throws Exception;
	/**
	 * 查询统计，可以分单月和多月查询
	 * @return
	 */
	public List<StatisticsInfo> staticAgentOrderQuery(StaticInfoQueryCon con) throws Exception;
	
	
	public List<AgentOrder> getAgentOrderList(long orderId) throws Exception;
	public List<AgentOrder> getAgentOrderList(Map params,StatisticsInfo agentStatis) throws Exception;
	/**
	 * 当满足阶梯返佣时，要用新的政策佣金替代原来的佣金，并更新到database
	 * add by yong.zeng
	 * @param params
	 * @param agentStatis
	 * @param curAgentCode
	 * @return
	 * @throws Exception
	 */
	public List<AgentOrder> getAgentOrderList(Map params,StatisticsInfo agentStatis,String curAgentCode) throws Exception;
	
	
	/**
	 * add yong.zeng
	 * @param id
	 * @return
	 */
	public StatisticsInfo getStatisticsInfo(Long id);
	
	/**
	 * 根据当前loginname得到其会角色
	 * @param loginname
	 * @return
	 */
	public String getCurRole(String loginname);
	
	/**
	 * 根据maps得到AgentOrg
	 * @param maps
	 * @return
	 */
	public AgentOrg queryAgentOrg(Map maps);
}

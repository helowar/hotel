package com.mangocity.hotel.search.dao;

import java.util.List;

import com.mangocity.hotel.search.po.ClusterConfigPO;

/**
 * BDB集群环境配置参数DAO接口
 * 
 * @author MT  2011-9-20
 * @version v1.0
 *
 */
public interface ClusterConfigDAO {
	
	/**
	 * 获取BDB集群配置
	 * 
	 * @return 
	 */
	List<ClusterConfigPO> getJEClusterConfig();

}

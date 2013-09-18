package com.mangocity.hotel.search.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mangocity.hotel.search.dao.ClusterConfigDAO;
import com.mangocity.hotel.search.po.ClusterConfigPO;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

/**
 * 集群配置参数DAO实现类
 * 
 * @author MT 
 * @version v1.0
 */
public class ClusterConfigDAOImpl extends GenericDAOHibernateImpl implements ClusterConfigDAO {

	@SuppressWarnings("unchecked")
	public List<ClusterConfigPO> getJEClusterConfig() {
		StringBuilder sb = new StringBuilder();
    	sb.append(" SELECT REP_ID,APP_NAME,GROUP_NAME,NODE_NAME,NODE_HOST,HELPER_HOSTS,REP_DIR ");
    	sb.append(" FROM BDB_CLUSTER_CONFIG order by REP_ID");	
    	List<Object[]> resultList = super.queryByNativeSQL(sb.toString(), null);  	
		if (resultList.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
    	
		List<ClusterConfigPO> configs = new ArrayList<ClusterConfigPO>();
		ClusterConfigPO configPO=null;
		for(Object[] item : resultList){
			configPO = new ClusterConfigPO();
			configPO.setRepId(item[0].toString());
			configPO.setAppName(item[1].toString());
			configPO.setGroupName(item[2].toString());
			configPO.setNodeName(item[3].toString());
			configPO.setNodeHost(item[4].toString());
			configPO.setHelperHosts(item[5].toString());
			configPO.setRepDir(item[6].toString());
			configs.add(configPO);
		}		
		return configs;
	}

}

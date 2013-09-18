package com.mangocity.hotel.search.searchengine.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.mangocity.hotel.search.dao.ClusterConfigDAO;
import com.mangocity.hotel.search.po.ClusterConfigPO;
import com.mangocity.hotel.search.searchengine.service.RepEnvInfo;
import com.mangocity.hotel.search.searchengine.service.RepEnvInfoFactory;
import com.sleepycat.je.Durability;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.EnvironmentLockedException;
import com.sleepycat.je.rep.ReplicationConfig;
import com.sleepycat.je.rep.StateChangeEvent;
import com.sleepycat.je.rep.StateChangeListener;
import com.sleepycat.je.rep.TimeConsistencyPolicy;

/**
 * This factory for RepEnvInfo object
 * 
 * @author MT 2011.9.20
 * @version v1.0
 * @see RepEnvInfoFactory
 * @see InitializingBean
 * @see DisposableBean
 * 
 */
public class DefaultRepEnvInfoFactory implements RepEnvInfoFactory,InitializingBean,DisposableBean{
	
	private final Log log = LogFactory.getLog(DefaultRepEnvInfoFactory.class);
	
    /* The current master as maintained by the Listener. */
    private volatile String currentmasterName = null;
    
	private RepEnvInfo repEnvInfo;
	
    private String nodeName = null;
	
	private ClusterConfigDAO clusterConfigDAO;

	public void setClusterConfigDAO(ClusterConfigDAO clusterConfigDAO) {
		this.clusterConfigDAO = clusterConfigDAO;
	}
	
	public RepEnvInfo getRepEnvInfo() {
		return repEnvInfo;			
	}

	public void afterPropertiesSet() throws Exception {
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setTransactional(true);
		envConfig.setDurability(new Durability(
				Durability.SyncPolicy.WRITE_NO_SYNC,
				Durability.SyncPolicy.WRITE_NO_SYNC,
				Durability.ReplicaAckPolicy.SIMPLE_MAJORITY));
		envConfig.setAllowCreate(true);
		envConfig.setTransactional(true);
		
		List<ClusterConfigPO> configs = getClusterConfig();	
		
		for(ClusterConfigPO configPO:configs){
			nodeName = configPO.getNodeName();			
			repEnvInfo = new RepEnvInfo(getEnvHome(configPO), setupReplicationConfig(configPO), envConfig);	
			try{
				repEnvInfo.openEnv();
				break;
			}catch(EnvironmentLockedException e){
				log.error(e);
				repEnvInfo.shutdown();
				continue;
			}
		}	
		repEnvInfo.setStateChangeListener(new Listener());
	}
	
	public void destroy() throws Exception {
		log.info("-------repEnvInfo.shutdown()-------");
		repEnvInfo.shutdown();
	}
	
	/**
	 * get bdb cluster config params. 
	 * 
	 * @return 
	 */
	private List<ClusterConfigPO> getClusterConfig(){		
		List<ClusterConfigPO> nodes =new ArrayList<ClusterConfigPO>();
		List<ClusterConfigPO>  configs = clusterConfigDAO.getJEClusterConfig();
		if(configs != null){
			String classPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();			
			for(ClusterConfigPO po:configs){
				if(classPath.indexOf(po.getAppName())>=0){
					nodes.add(po);
				}
			}
		}	
		if(nodes.isEmpty()){
			/**
			 * Used for local development
			 */
			ClusterConfigPO clusterConfigPO = new ClusterConfigPO();
			clusterConfigPO.setGroupName("HotelRepGroup");
			clusterConfigPO.setNodeName("rep1");
			clusterConfigPO.setNodeHost("127.0.0.1:5001");
			clusterConfigPO.setRepDir("D:/");
			clusterConfigPO.setHelperHosts("127.0.0.1:5001");
			nodes.add(clusterConfigPO);			
			clusterConfigPO = new ClusterConfigPO();
			clusterConfigPO.setGroupName("HotelRepGroup");
			clusterConfigPO.setNodeName("rep2");
			clusterConfigPO.setNodeHost("127.0.0.1:5002");
			clusterConfigPO.setRepDir("D:/");
			clusterConfigPO.setHelperHosts("127.0.0.1:5001");
			nodes.add(clusterConfigPO);
		}
		return nodes;
	}	
	
	/**
	 * get the bdb Environment directory file. if the file is not exists and created it. 
	 * @param clusterConfigPO
	 * @return
	 */
	private File getEnvHome(ClusterConfigPO clusterConfigPO){
		File envHome = new File(clusterConfigPO.getRepDir(),clusterConfigPO.getNodeName());	
		if(!envHome.exists()){
			envHome.mkdirs();
		}
		return envHome;
	}
	
	
	/**
	 * setup the ReplicationConfig by ClusterConfigPO
	 * 
	 * @param clusterConfigPO
	 * @return
	 */
	private ReplicationConfig setupReplicationConfig(ClusterConfigPO clusterConfigPO){
		/*
		 * Set envHome and generate a ReplicationConfig. Note that
		 * ReplicationConfig and EnvironmentConfig values could all be
		 * specified in the je.properties file, as is shown in the
		 * properties file included in the example.
		 */
		if(clusterConfigPO==null) return null;
		ReplicationConfig repConfig = new ReplicationConfig();
		repConfig.setGroupName(clusterConfigPO.getGroupName());
		repConfig.setNodeName(clusterConfigPO.getNodeName());
		repConfig.setNodeHostPort(clusterConfigPO.getNodeHost());
		repConfig.setHelperHosts(clusterConfigPO.getHelperHosts());

		/* Set consistency policy for replica. */
		TimeConsistencyPolicy consistencyPolicy = new TimeConsistencyPolicy(
				1, TimeUnit.SECONDS, /* 1 sec of lag */
				3, TimeUnit.SECONDS /* Wait up to 3 sec */);
		repConfig.setConsistencyPolicy(consistencyPolicy);

		/* Wait up to two seconds for commit acknowledgments. */
		repConfig.setReplicaAckTimeout(2, TimeUnit.SECONDS);
		
		return repConfig;
	}
	
	
	 /**
     * The Listener used to react to StateChangeEvents. It maintains the name
     * of the current master.
     */
    private class Listener implements StateChangeListener {

        /**
         * The protocol method used to service StateChangeEvent notifications.
         */
        public void stateChange(StateChangeEvent stateChangeEvent)
            throws RuntimeException {

            switch (stateChangeEvent.getState()) {
                case MASTER:
                	log.info("current node:"+ nodeName+" is master");
                	break;
                case REPLICA:
                    currentmasterName = stateChangeEvent.getMasterNodeName();
                    log.error("Master: " + currentmasterName  + " at " + new Date());
                    break;
                default:
                	log.error("Unknown master. " +   " Node state: " + stateChangeEvent.getState());
                    break;
            }
        }
    }
}

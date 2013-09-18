package com.mangocity.hotel.search.searchengine.service;

import java.io.File;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.je.JEDirectory;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.Transaction;
import com.sleepycat.je.rep.RepInternal;
import com.sleepycat.je.rep.ReplicatedEnvironment;
import com.sleepycat.je.rep.ReplicationConfig;
import com.sleepycat.je.rep.StateChangeListener;
import com.sleepycat.je.rep.UnknownMasterException;
import com.sleepycat.je.rep.impl.RepImpl;
import com.sleepycat.je.rep.impl.node.RepNode;

/**
 * 集群环境参数信息对象
 * 
 * @author MT 2011.9.20
 * @version v1.0
 */
public class RepEnvInfo {
	
    /* The maximum number of times to retry handle creation. */
    private static int REP_HANDLE_RETRY_MAX = 5;
	
    private final File envHome;
    private final ReplicationConfig repConfig;
    private final EnvironmentConfig envConfig;

    private ReplicatedEnvironment repEnv = null;
	private Database index, blocks;

    public RepEnvInfo(File envHome,
                      ReplicationConfig repConfig,
                      EnvironmentConfig envConfig) {
        super();
        this.envHome = envHome;
        this.repConfig = repConfig;
        this.envConfig = envConfig;
    }

	public synchronized ReplicatedEnvironment openEnv() {
		if (repEnv != null) {
			throw new IllegalStateException("rep env already exists");
		} else {
			DatabaseException exception = null;
			/*
			 * In this example we retry REP_HANDLE_RETRY_MAX times, but a
			 * production HA application may retry indefinitely.
			 */
			for (int i = 0; i < REP_HANDLE_RETRY_MAX; i++) {
				try {					
					repEnv = new ReplicatedEnvironment(envHome, repConfig, envConfig);		       
					return repEnv;
				} catch (UnknownMasterException unknownMaster) {
					exception = unknownMaster;
					try {
						Thread.sleep(5*10);
					} catch (InterruptedException e) {
						
					}
					continue;
				}
			}
			/* Failed despite retries. */
			if (exception != null) {
				throw exception;
			}
			/* Don't expect to get here. */
			throw new IllegalStateException("Failed despite retries");
		}
    }
	
	public synchronized void openDB() {
		Transaction txn = null;
		try {
			txn = repEnv.beginTransaction(null, null);
			DatabaseConfig dbConfig = new DatabaseConfig();
			dbConfig.setTransactional(true);
			dbConfig.setAllowCreate(true);

			index = repEnv.openDatabase(null, "__index__", dbConfig);
			blocks = repEnv.openDatabase(null, "__blocks__", dbConfig);

		} catch (DatabaseException e) {
			if (txn != null) {
				txn.abort();
				txn = null;
			}
			index = null;
			blocks = null;
			throw e;
		} finally {
			if (txn != null)
				txn.commit();
			txn = null;
		}
	}
	
	public synchronized Directory getDirectory(Transaction txn){		
		if(!getRepImpl().isValid()){
			shutdown();
			openEnv();
		}		
		if(index==null || blocks==null){
			closeDB();				
			openDB();
		}
		return new JEDirectory(txn,index,blocks);
	}

	public ReplicatedEnvironment getEnv() {
        return repEnv;
    }

    public RepImpl getRepImpl() {
        return RepInternal.getRepImpl(repEnv);
    }

    public RepNode getRepNode() {
        return getRepImpl().getRepNode();
    }

    public ReplicationConfig getRepConfig() {
        return repConfig;
    }

    public File getEnvHome() {
        return envHome;
    }

    public EnvironmentConfig getEnvConfig() {
        return envConfig;
    }
    
    public void setStateChangeListener(StateChangeListener listener) {
        if(repEnv!=null)
        	repEnv.setStateChangeListener(listener);
    }
    
    public synchronized void closeDB(){
		if (index != null){
			index.close();
			index = null;
		}
		if (blocks != null){
			blocks.close();
			blocks = null;
		}		
    }
    

    public synchronized void shutdown()  {
    	closeDB();
    	if(repEnv!=null){
    		repEnv.cleanLog();
    		repEnv.evictMemory();
            repEnv.close();
            repEnv = null;
    	}
    }

    /**
     * Convenience method that guards against a NPE when iterating over
     * a set of RepEnvInfo, looking for the master.
     */
    public boolean isMaster() {
        return (repEnv != null) && repEnv.getState().isMaster();
    }

    /**
     * Convenience method that guards against a NPE when iterating over
     * a set of RepEnvInfo, looking for a replica
     */
    public boolean isReplica() {
        return (repEnv != null) && repEnv.getState().isReplica();
    }

    /**
     * Simulate a crash of the environment, don't do a graceful close.
     */
    public void abnormalCloseEnv() {
        try {
            /* Although we want an abnormal close, we do want to flush. */
            RepInternal.getRepImpl(repEnv).getLogManager().flushNoSync();
            RepInternal.getRepImpl(repEnv).abnormalClose();
        } catch (DatabaseException ignore) {

            /*
             * The close will face problems like unclosed txns, ignore.
             * We're trying to simulate a crash.
             */
        } finally {
            repEnv = null;
        }
    }

    public String toString() {
        if (repEnv == null) {
            return envHome.toString();
        } else {
            return repEnv.getNodeName();
        }
    }
}
package com.mangocity.hotel.search.searchengine.service.impl;

import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.mangocity.hotel.search.searchengine.service.RepEnvInfo;
import com.sleepycat.je.Transaction;


/**
 * Utility class to begin and commit/abort a transaction and handle exceptions
 * according to this application's policies.  The doTransactionWork method is
 * abstract and must be implemented by callers.  The transaction is run and
 * doTransactionWork is called by the run() method of this class.  
 */
public abstract class RunTransaction {
	
    /* The maximum number of times to retry the transaction. */
    private static final int TRANSACTION_RETRY_MAX = 10;
    
    /* Amount of time to wait after a lock conflict. */
    private static final int LOCK_RETRY_SEC = 1;

    private RepEnvInfo repEnvInfo;

    /**
     * Creates the runner.
     */
    RunTransaction(RepEnvInfo repEnvInfo) {  
    	this.repEnvInfo = repEnvInfo;
    }    

    /**
     * Runs a transaction, calls the doTransactionWork method
     */
    @SuppressWarnings({ "deprecation" })
	public synchronized void run(boolean create) throws InterruptedException, IOException {
		boolean success = false;
		Transaction txn = null;
		Directory store = null;
		IndexWriter writer = null;

		LockObtainFailedException exception = null;
		long sleepMillis = 0;
		for (int i = 0; i < TRANSACTION_RETRY_MAX; i++) {
			/* Sleep before retrying. */
			if (sleepMillis != 0) {
				Thread.sleep(sleepMillis);
				sleepMillis = 0;
			}
			try {
				txn = repEnvInfo.getEnv().beginTransaction(null, null);
				store = repEnvInfo.getDirectory(txn);
				writer = new IndexWriter(store, new IKAnalyzer(), create, IndexWriter.MaxFieldLength.UNLIMITED);
				doTransactionWork(writer); /* CALL APP-SPECIFIC CODE */
				success = true;
			} catch (CorruptIndexException e) {
				throw e;
			} catch (LockObtainFailedException e) {
				exception = e;
				if(!repEnvInfo.getRepImpl().isValid())
					repEnvInfo.shutdown();
				sleepMillis = LOCK_RETRY_SEC * 1000;					
				continue;
			} catch (IOException e) {
				throw e;
			} finally {
				try {
					if (writer != null)
						writer.close();
				} catch (CorruptIndexException e) {
					throw e;
				} catch (IOException e) {
					throw e;
				} finally {
					writer = null;
					try {
						if(store!=null)
						   store.close();
					} catch (IOException e) {
						throw e;
					} finally {
						store = null;
						if (!success) {
							if (txn != null) {
								txn.abort();
							}
						} else {
							txn.commit();
							return;
						}
					}
				}
			}
			if(!success){
				throw exception;
			}
		}
	}

    /**
     * Must be implemented to perform operations using the given Transaction.
     */
    public abstract void doTransactionWork(IndexWriter writer) throws CorruptIndexException, IOException;
}
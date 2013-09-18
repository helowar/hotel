package com.mangocity.hotel.search.searchengine.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LogByteSizeMergePolicy;
import org.apache.lucene.index.LogMergePolicy;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.wltea.analyzer.lucene.IKAnalyzer;


import com.mangocity.hotel.search.dao.ClusterConfigDAO;
import com.mangocity.hotel.search.po.ClusterConfigPO;
import com.mangocity.hotel.search.searchengine.service.IndexFactory;

class DefaultIndexFactory implements IndexFactory, InitializingBean,
		DisposableBean {

	private final Log log = LogFactory.getLog(DefaultIndexFactory.class);

	private Directory dir = null;

	private ClusterConfigDAO clusterConfigDAO;

	private IndexWriter indexWriter = null;

	private double ramBufferSize = 128;

	private long writeLockTimeout = 1500;

	private IndexReader indexReader;

	private IndexSearcher currentIndexSearcher;

	@SuppressWarnings("deprecation")
	public IndexSearcher getIndexSearcher() {
		return currentIndexSearcher;
	}

	@SuppressWarnings("deprecation")
	public IndexWriter getIndexWriter() {
		try {
			if (this.indexWriter == null) {
				synchronized (this) {
					if (this.indexWriter == null) {
						IndexWriterConfig config = new IndexWriterConfig(
								Version.LUCENE_33, new IKAnalyzer());
						config.setRAMBufferSizeMB(this.ramBufferSize);// set to 128m
						config.setWriteLockTimeout(this.writeLockTimeout);
						config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
						// reducing the mergeFactor to a lower value. so that
						// the search performance will be raised
						LogMergePolicy mergePolicy = new LogByteSizeMergePolicy();
						mergePolicy.setMergeFactor(5);// default value is 10
						config.setMergePolicy(mergePolicy);
						this.indexWriter = new IndexWriter(dir, config);
						this.indexReader = IndexReader.open(this.indexWriter,true);
						if (currentIndexSearcher == null) {
							this.currentIndexSearcher = new IndexSearcher(this.indexReader);
						}
						new Thread(new WactherThread()).start();
					}
				}
			}
		} catch (Exception e) {
			log.error("getIndexWriter", e);
		}
		return this.indexWriter;
	}

	private List<ClusterConfigPO> getClusterConfig() {
		List<ClusterConfigPO> nodes = new ArrayList<ClusterConfigPO>();
		List<ClusterConfigPO> configs = clusterConfigDAO.getJEClusterConfig();
		if (configs != null) {
			String classPath = Thread.currentThread().getContextClassLoader()
					.getResource("").getPath();
			for (ClusterConfigPO po : configs) {
				if (classPath.indexOf(po.getAppName()) >= 0) {
					nodes.add(po);
				}
			}
		}
		if (nodes.isEmpty()) {
			/**
			 * Used for local development
			 */
			ClusterConfigPO clusterConfigPO = new ClusterConfigPO();
			clusterConfigPO.setGroupName("HotelRepGroup");
			clusterConfigPO.setNodeName("Index2");
			clusterConfigPO.setNodeHost("127.0.0.1:5002");
			clusterConfigPO.setRepDir("F:/REP1");
			clusterConfigPO.setHelperHosts("127.0.0.1:5002");
			nodes.add(clusterConfigPO);
		}
		return nodes;
	}
	
	public void afterPropertiesSet() throws Exception {
		ClusterConfigPO clusterConfigPO = getClusterConfig().get(0);
		
		String nodeName =new Integer((int)(Math.random()*100000)).toString();
		File envHome = new File(clusterConfigPO.getRepDir(), nodeName);
		if (!envHome.exists()) {
			envHome.mkdirs();
		}
		this.dir = FSDirectory.open(envHome);
		getIndexWriter();
		
	}

	private class WactherThread implements Runnable {
		public void run() {
			
			while(true){
				
				try {
					Thread.sleep(30000);// wait for 30 seconds
				} catch (InterruptedException e) {
					log.error("Wather failed to wait");
				}

				log.info("After 30 second about to swap the reader.");
				try {
					if (!indexReader.isCurrent()) {
						log.info("The reader is changed then reopen it.");
						IndexReader newindexReader = indexReader.reopen(indexWriter, true);
						indexReader.close();// close the old one
						log.info("The old reader is closed.");
						indexReader = newindexReader;// reassign the value to the old reader
						currentIndexSearcher = new IndexSearcher(indexReader);
						log.info("The newer searcher is created.");
					}
				} catch (CorruptIndexException e) {
					log.error("Retrieve indexSearcher failed!", e);
				} catch (IOException e) {
					log.error("Retrieve indexSearcher failed!", e);				
				}
			}
			
		}
	}

	public void destroy() throws Exception {

		try {
			if (this.indexWriter != null) {
				this.indexWriter.commit();
				this.indexWriter.close();
			}
		} catch (Exception e) {
			log.error(e);
		} finally {
			this.indexWriter = null;
		}
	}

	public void setClusterConfigDAO(ClusterConfigDAO clusterConfigDAO) {
		this.clusterConfigDAO = clusterConfigDAO;
	}

	public Directory getDirectory() {

		return this.dir;
	}

	public double getRamBufferSize() {
		return ramBufferSize;
	}

	public void setRamBufferSize(double ramBufferSize) {
		this.ramBufferSize = ramBufferSize;
	}

	public long getWriteLockTimeout() {
		return writeLockTimeout;
	}

	public void setWriteLockTimeout(long writeLockTimeout) {
		this.writeLockTimeout = writeLockTimeout;
	}

}

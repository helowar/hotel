package com.mangocity.hotel.search.searchengine.service.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.mangocity.hotel.search.searchengine.service.IndexFactory;
import com.mangocity.hotel.search.searchengine.service.IndexWriterService;

/**
 * 索引维护服务实现
 * 
 * @author MT 2011.9.20
 * @version v1.0
 * @see IndexWriterService
 * @see InitializingBean
 *
 */
public class IndexWriterServiceImpl  implements
		IndexWriterService, InitializingBean, DisposableBean {

	private final Log log = LogFactory.getLog(IndexWriterServiceImpl.class);

	private IndexFactory indexFactory = null;



	public boolean checkIndexFileExist() {
		boolean isExist = false;
		try {
			if (IndexReader.indexExists(indexFactory.getDirectory()))
				isExist = true;
		} catch (Exception e) {
			log.error("checkIndexFileExist", e);
		}
		return isExist;
	}

	public void addDocuments(final List<Document> docs) {
		IndexWriter writer = null;

		try {
			writer = indexFactory.getIndexWriter();
			writer.addDocuments(docs);
			//writer.commit();
		//	writer.optimize();
		} catch (Exception e) {
			log.error("addDocuments", e);
		} 

	}

	public void updDocument(final String fieldName, final String fieldValue,
			final Document doc) {
		IndexWriter writer = null;

		try {
			writer = indexFactory.getIndexWriter();
			writer.updateDocument(new Term(fieldName, fieldValue), doc);
			log.info("update document success");

		} catch (Exception e) {
			log.error("addDocuments", e);
		} 
	}

	public void delDocuments(final String fieldName, final String fieldValue) {
		IndexWriter writer = null;

		try {
			writer = indexFactory.getIndexWriter();
			Term term = new Term(fieldName, fieldValue);
			writer.deleteDocuments(new TermQuery(term));
		} catch (Exception e) {
			log.error("delDocuments", e);
		} 
	}

	public void clearAllDocuments() {
		IndexWriter writer = null;

		try {
			writer = indexFactory.getIndexWriter();
			writer.deleteAll();
			writer.optimize();
		} catch (Exception e) {
			log.error("clearAllDocuments", e);
		} 
	}

	public void afterPropertiesSet() throws Exception {
	}

	public void destroy() throws Exception {
	}

	public void setIndexFactory(IndexFactory indexFactory) {
		this.indexFactory = indexFactory;
	}

	public void commit() {
		IndexWriter writer = null;

		try {
			writer = indexFactory.getIndexWriter();
			writer.commit();
			writer.optimize();
		} catch (Exception e) {
			log.error("commit", e);
		} 
	}



}
			
	
	


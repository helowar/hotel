package com.mangocity.hotel.search.searchengine.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.mangocity.hotel.search.searchengine.service.IndexFactory;
import com.mangocity.hotel.search.searchengine.service.IndexSearcherService;
import com.mangocity.hotel.search.searchengine.service.ResultProcessor;

/**
 * 索引搜索服务实现
 * 
 * @author MT 2011.9.20
 * @version v1.0
 * @see IndexSearcherService
 * @see InitializingBean
 * @see DisposableBean
 *
 */
public class IndexSearcherServiceImpl  implements IndexSearcherService,InitializingBean,DisposableBean {

	private final Log log = LogFactory.getLog(IndexSearcherServiceImpl.class);

	private static final int MAX_SCORE = 10000;

	private IndexFactory indexFactory;
	
	@SuppressWarnings("unchecked")
	public <T> List<T> search(Query query, ResultProcessor<T> resultHandler) {
		
		List<T> resultList =Collections.EMPTY_LIST;
		try {		
			resultList = excuteSearch(query,resultHandler);
		} catch (Exception e) {
			try {
				resultList = excuteSearch(query,resultHandler);
			} catch (Exception ex) {
				log.error("Can not execute lucene query ", e);
			}
		}
		return resultList;
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> List<T> excuteSearch(Query query,ResultProcessor<T> resultHandler) throws Exception {
		IndexSearcher searcher = indexFactory.getIndexSearcher();
		TopDocs topDocs = searcher.search(query, MAX_SCORE);
		if (topDocs.totalHits <= 0) {
			return Collections.EMPTY_LIST;
		}

		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		List<T> resultList = new ArrayList<T>(topDocs.totalHits);
		for (int i = 0; i < topDocs.totalHits; i++) {
			Document targetDoc = searcher.doc(scoreDocs[i].doc);
			resultList.add(resultHandler.processResult(targetDoc));
		}
		return resultList;
	}
	
	
	public void afterPropertiesSet() throws Exception {
		
	}	

	public void destroy() throws Exception {
		
	}

	public IndexFactory getIndexFactory() {
		return indexFactory;
	}

	public void setIndexFactory(IndexFactory indexFactory) {
		this.indexFactory = indexFactory;
	}


}

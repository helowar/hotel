package com.mangocity.hotel.search.searchengine.service;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;

/**
 * 索引创建接口
 * @author tuzihui
 *
 */
public interface IndexFactory {
	
	/**
	 * 获取 IndexSearcher
	 * @return IndexSearcher
	 */
	
	public IndexSearcher getIndexSearcher();
	
	/**
	 * 获取 IndexSearcher
	 * @return IndexSearcher
	 */
	
	public IndexWriter getIndexWriter();
	
	
	/**
	 * 获取 Directory
	 * @return Directory
	 */
	public Directory getDirectory();

}

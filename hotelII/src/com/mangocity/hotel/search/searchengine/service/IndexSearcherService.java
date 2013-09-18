package com.mangocity.hotel.search.searchengine.service;

import java.util.List;

import org.apache.lucene.search.Query;

/**
 * 索引查询服务
 * 
 * @author MT 2011-9-20
 * @version v1.0
 */
public interface IndexSearcherService  {
	
	/**
	 * 酒店信息搜索入口
	 * 
	 * @param <T>
	 * @param Query
	 * @param resultHandler
	 * @return
	 */
	public <T> List<T> search(Query query, ResultProcessor<T> resultHandler);
	

}

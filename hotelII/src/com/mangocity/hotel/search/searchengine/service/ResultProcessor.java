package com.mangocity.hotel.search.searchengine.service;

import org.apache.lucene.document.Document;

public interface ResultProcessor<T> {
	
	/**
	 * 将搜索出的Document对象转换成系统业务对象
	 * 
	 * @param doc	Document对象
	 * @return
	 */
	public T processResult(Document doc);

}

package com.mangocity.hotel.search.searchengine.service;

import java.util.List;

import org.apache.lucene.document.Document;

/**
 * 索引维护服务
 * 
 * @author MT 2011-9-20
 * @version v1.0
 * 
 */
public interface IndexWriterService  {
	
	/**
	 * 检查索引库是否初始化（存在）如果索引库已经初始化，返回true；反之，返回false
	 * @return  
	 */
	public boolean checkIndexFileExist();
	
	/**
	 * 批量新增索引
	 * @param docs
	 * @param create (true:初始化索引库；false：新增索引数据)
	 */
	void addDocuments(final List<Document> docs);
	
	/**
	 * 修改索引数据
	 * @param fieldName
	 * @param fieldValue
	 * @param doc
	 */
	public void updDocument(final String fieldName, final String fieldValue, final Document doc);
	
	/**
	 * 删除索引数据
	 * @param fieldName
	 * @param fieldValue
	 */
	public void delDocuments(final String fieldName, final String fieldValue);
	
	/**
	 * 清理所有索引数据
	 */
	public void clearAllDocuments();
	
	public void commit();
}

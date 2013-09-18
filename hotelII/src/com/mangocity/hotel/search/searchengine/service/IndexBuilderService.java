package com.mangocity.hotel.search.searchengine.service;

import java.util.List;

import org.apache.lucene.document.Document;

public interface IndexBuilderService {
	
	/**
	 * 检查索引库是否初始化（存在）如果索引库已经初始化，返回true；反之，返回false
	 * @return  
	 */
	public boolean checkIndexWriterExist();
	
	/**
	 * 清理所有索引数据 
	 */
	public void clearAllDocuments();
	
	/**
	 * 添加多个Document到索引文件中
	 * 
	 * @param documentList	Document对象集合
	 */
	public void addDocuments(List<Document> documentList, boolean create);

	/**
	 * 根据Field名称和Field值删除所有包含该字段的Document
	 * 
	 * @param fieldName
	 * @param fieldValue
	 */
	public void deleteDocuments(String fieldName, String fieldValue);

	/**
	 * 首先根据Field名称和Field值删除所有包含该字段的Document， 然后添加指定的Document到索引中
	 * 注意：如果想更新单个Document，必须确保包含Field名称和Field值的Document是唯一的
	 * 
	 * @param fieldName
	 * @param fieldValue
	 * @param doc
	 */
	public void updateDocument(String fieldName, String fieldValue, Document doc);
	

}

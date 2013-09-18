package com.mangocity.hotel.search.searchengine.service.impl;

import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;

import com.mangocity.hotel.search.searchengine.service.IndexBuilderService;
import com.mangocity.hotel.search.searchengine.service.IndexWriterService;

public class IndexBuilderServiceImpl implements IndexBuilderService {
	
	private IndexWriterService indexWriterService;
	
	public void clearAllDocuments(){
		indexWriterService.clearAllDocuments();
	}
	
	public void addDocuments(List<Document> docs,boolean create) {
	//	indexWriterService.addDocuments(docs, create);
	}	
	
	public void deleteDocuments(String fieldName, String fieldValue) {
		Term term = new Term(fieldName, String.valueOf(fieldValue));
	//	indexWriterService.delDocuments(new TermQuery(term));
	}
	
	public void updateDocument(String fieldName, String fieldValue, Document doc) {	
	//	indexWriterService.updDocument(new Term(fieldName, fieldValue), doc);	
	}

    
    public void setIndexWriterService(IndexWriterService indexWriterService) {
	//	this.indexWriterService = indexWriterService;
	}

	public boolean checkIndexWriterExist() {
		return false;
	//	return indexWriterService.checkIndexWriterExist();
	}
}

package com.mangocity.hotel.search.exception;

public class LuceneCreateQueryException extends RuntimeException{
	 /**
	    * Construct a <code>LuceneIndexMRException</code> with the specified detail message.
	    *
	    * @param msg the detail message
	    */
	   public LuceneCreateQueryException(String msg) {
	       super(msg);
	   }

	   /**
	    * Construct a <code>LuceneIndexMRException</code> with the specified detail message
	    * and nested exception.
	    *
	    * @param msg   the detail message
	    * @param cause the nested exception
	    */
	   public LuceneCreateQueryException(String msg, Throwable cause) {
	       super(msg, cause);
	   }
}

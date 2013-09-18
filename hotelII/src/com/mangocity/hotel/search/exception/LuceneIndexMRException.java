package com.mangocity.hotel.search.exception;

public class LuceneIndexMRException extends RuntimeException{

	 /**
    * Construct a <code>LuceneIndexMRException</code> with the specified detail message.
    *
    * @param msg the detail message
    */
   public LuceneIndexMRException(String msg) {
       super(msg);
   }

   /**
    * Construct a <code>LuceneIndexMRException</code> with the specified detail message
    * and nested exception.
    *
    * @param msg   the detail message
    * @param cause the nested exception
    */
   public LuceneIndexMRException(String msg, Throwable cause) {
       super(msg, cause);
   }

}


package com.mangocity.hotel.search.exception;

public class LuceneSearchException extends RuntimeException{

	 /**
   * Construct a <code>LuceneSerachException</code> with the specified detail message.
   *
   * @param msg the detail message
   */
  public LuceneSearchException(String msg) {
      super(msg);
  }

  /**
   * Construct a <code>LuceneSerachException</code> with the specified detail message
   * and nested exception.
   *
   * @param msg   the detail message
   * @param cause the nested exception
   */
  public LuceneSearchException(String msg, Throwable cause) {
      super(msg, cause);
  }
}

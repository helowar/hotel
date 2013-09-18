package com.mangocity.hotel.exception;

import org.mangocube.corenut.commons.exception.CheckedException;


@SuppressWarnings("unchecked")
public class BusinessException extends CheckedException 
{
	private static final long serialVersionUID = 7885131430047864901L;

	public BusinessException (Enum anEnum)
	{
	    super(anEnum);
	}

	public BusinessException (Enum anEnum, Object[] params)
	{
	    super(anEnum, params);
	}
	
	public BusinessException (Enum anEnum, Throwable throwable)
	{
	    super(anEnum, throwable);
	}

	public BusinessException (Enum anEnum, Throwable throwable, Object[] params)
	{
	    super(anEnum, throwable, params);
	}
}

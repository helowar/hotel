package com.mangocity.hotel.order.service.exception;

import org.mangocube.corenut.commons.exception.CheckedException;


@SuppressWarnings("unchecked")
public class PriceInvalidException extends CheckedException{

	private static final long serialVersionUID = 7885131430047864901L;

	
	public PriceInvalidException(Enum anEnum)
	{
	    super(anEnum);
	}

	public PriceInvalidException(Enum anEnum, Object[] params)
	{
	    super(anEnum, params);
	}
	
	public PriceInvalidException(Enum anEnum, Throwable throwable)
	{
	    super(anEnum, throwable);
	}

	public PriceInvalidException(Enum anEnum, Throwable throwable, Object[] params)
	{
	    super(anEnum, throwable, params);
	}
}

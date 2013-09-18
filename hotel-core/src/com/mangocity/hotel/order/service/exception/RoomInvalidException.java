package com.mangocity.hotel.order.service.exception;

import org.mangocube.corenut.commons.exception.CheckedException;


@SuppressWarnings("unchecked")
public class RoomInvalidException  extends CheckedException{

	private static final long serialVersionUID = 7885131430047864901L;

	public RoomInvalidException(Enum anEnum)
	{
	    super(anEnum);
	}

	public RoomInvalidException(Enum anEnum, Object[] params)
	{
	    super(anEnum, params);
	}
	
	public RoomInvalidException(Enum anEnum, Throwable throwable)
	{
	    super(anEnum, throwable);
	}

	public RoomInvalidException(Enum anEnum, Throwable throwable, Object[] params)
	{
	    super(anEnum, throwable, params);
	}
}


package com.mangocity.hotel.exception;

import org.mangocube.corenut.commons.exception.UncheckedException;


@SuppressWarnings("unchecked")
public class SysException extends UncheckedException 
{
	private static final long serialVersionUID = 1047930035110363102L;

	public SysException (Enum anEnum)
	{
	    super(anEnum);
	}

	public SysException (Enum anEnum, Object[] params)
	{
	    super(anEnum, params);
	}
	
	public SysException (Enum anEnum, Throwable throwable)
	{
	    super(anEnum, throwable);
	}

	public SysException (Enum anEnum, Throwable throwable, Object[] params)
	{
	    super(anEnum, throwable, params);
	}
	
}

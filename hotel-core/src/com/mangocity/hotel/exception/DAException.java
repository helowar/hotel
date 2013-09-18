package com.mangocity.hotel.exception;

@SuppressWarnings("unchecked")
public class DAException extends SysException 
{
	private static final long serialVersionUID = 5459803999818527382L;

	public DAException (Enum anEnum)
	{
	    super(anEnum);
	}

	public DAException (Enum anEnum, Object[] params)
	{
	    super(anEnum, params);
	}
	
	public DAException (Enum anEnum, Throwable throwable)
	{
	    super(anEnum, throwable);
	}

	public DAException (Enum anEnum, Throwable throwable, Object[] params)
	{
	    super(anEnum, throwable, params);
	}
	
}

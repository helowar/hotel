package com.mangocity.hotel.exception;

@SuppressWarnings("unchecked")
public class NetworkAccessException extends SysException 
{
	private static final long serialVersionUID = -6924176095600327128L;

	public NetworkAccessException (Enum anEnum)
	{
	    super(anEnum);
	}

	public NetworkAccessException (Enum anEnum, Object... strings)
	{
	    super(anEnum, strings);
	}
	
	public NetworkAccessException (Enum anEnum, Throwable throwable)
	{
	    super(anEnum, throwable);
	}

	public NetworkAccessException (Enum anEnum, Throwable throwable, Object... strings)
	{
	    super(anEnum, throwable, strings);
	}
}

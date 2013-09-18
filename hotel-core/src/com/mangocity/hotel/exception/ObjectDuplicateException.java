package com.mangocity.hotel.exception;

@SuppressWarnings("unchecked")
public class ObjectDuplicateException extends BusinessException 
{
	private static final long serialVersionUID = -8938908502650217768L;

	public ObjectDuplicateException (Enum anEnum)
	{
	    super(anEnum);
	}

	public ObjectDuplicateException (Enum anEnum, Object[] params)
	{
	    super(anEnum, params);
	}
	
	public ObjectDuplicateException (Enum anEnum, Throwable throwable)
	{
	    super(anEnum, throwable);
	}

	public ObjectDuplicateException (Enum anEnum, Throwable throwable, Object[] params)
	{
	    super(anEnum, throwable, params);
	}
}

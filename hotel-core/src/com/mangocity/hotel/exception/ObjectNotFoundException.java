package com.mangocity.hotel.exception;

@SuppressWarnings("unchecked")
public class ObjectNotFoundException extends BusinessException 
{
	private static final long serialVersionUID = 1782387555204616663L;

	public ObjectNotFoundException (Enum anEnum)
	{
	    super(anEnum);
	}

	public ObjectNotFoundException (Enum anEnum, Object[] params)
	{
	    super(anEnum, params);
	}
	
	public ObjectNotFoundException (Enum anEnum, Throwable throwable)
	{
	    super(anEnum, throwable);
	}

	public ObjectNotFoundException (Enum anEnum, Throwable throwable, Object[] params)
	{
	    super(anEnum, throwable, params);
	}
}

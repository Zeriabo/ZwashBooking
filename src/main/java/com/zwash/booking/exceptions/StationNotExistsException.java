package com.zwash.booking.exceptions;

import java.io.Serial;

public class StationNotExistsException extends Exception {
    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -3267837584889499032L;
    public StationNotExistsException()
    {

    }
    public StationNotExistsException(String message)
    {

      super(message);
    }
	public StationNotExistsException(Long id)
	{
		super(" station id "+id +" does not exists in the System");
	}
}

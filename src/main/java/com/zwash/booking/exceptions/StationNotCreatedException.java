package com.zwash.booking.exceptions;

import java.io.Serial;

public class StationNotCreatedException extends Exception {
    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -3267837584889499032L;
    public StationNotCreatedException()
    {

    }
    public StationNotCreatedException(String message)
    {

      super(message);
    }
	public StationNotCreatedException(Long id)
	{
		super(" station has not beeen created");
	}
}

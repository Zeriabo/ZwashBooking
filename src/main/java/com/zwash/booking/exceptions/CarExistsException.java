package com.zwash.booking.exceptions;

import java.io.Serial;

public class CarExistsException extends Exception {
    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -3267837584889499032L;
    public CarExistsException()
    {

    }
	public CarExistsException(String car)
	{
		super(car+ " is already in the System");
	}
}

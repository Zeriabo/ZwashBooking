package com.zwash.booking.exceptions;

import java.io.Serial;


public class IncorrectTokenException extends Exception {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -3267837584889499032L;

	public IncorrectTokenException(String errorMessage)
	{
		super(errorMessage);
	}
}


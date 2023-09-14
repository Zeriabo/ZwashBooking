package com.zwash.booking.exceptions;

public class UserIsNotFoundException extends Exception {
	/**
	 *
	 */
	private static final long serialVersionUID = -3267837584889499032L;
    public UserIsNotFoundException()
    {

    }
	public UserIsNotFoundException(String username)
	{
		super(username+ "is not found in the System");
	}
}

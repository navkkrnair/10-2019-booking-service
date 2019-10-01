package com.ams.booking.exception;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class BookingError
{
	private final String errorMessage;
	private List<String> errors = new ArrayList<String>();

	public void addBookingValidationError(String error)
	{
		this.errors.add(error);
	}

}

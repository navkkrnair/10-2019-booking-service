package com.ams.booking.exception;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingErrorBuilder
{
	private static final BookingErrorBuilder builder = new BookingErrorBuilder();
	private String                           errorMessage;

	public static BookingErrorBuilder create()
	{
		return builder;
	}

	public BookingErrorBuilder withError(String errorMessage)
	{
		this.errorMessage = errorMessage;
		return this;
	}

	public BookingError build()
	{
		return new BookingError(this.errorMessage);
	}

	public static BookingError fromBindingErrors(Errors errors)
	{
		BookingError error = new BookingError("Validation failed on "
				+ StringUtils.capitalize(errors.getObjectName()) + ". "
				+ errors.getErrorCount()
				+ " error(s)");
		errors.getFieldErrors()
			.forEach(fe -> error
				.addBookingValidationError(fe.getField() + " " + fe.getDefaultMessage()));
		return error;
	}
}

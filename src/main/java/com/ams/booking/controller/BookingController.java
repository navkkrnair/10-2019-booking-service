package com.ams.booking.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ams.booking.entity.BookingRecord;
import com.ams.booking.exception.BookingError;
import com.ams.booking.exception.BookingErrorBuilder;
import com.ams.booking.exception.BookingException;
import com.ams.booking.service.BookingService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/booking")
public class BookingController
{
	private static final Logger logger = LoggerFactory.getLogger(BookingController.class);
	private BookingService      service;

	@PostMapping
	public ResponseEntity<?> book(@Valid @RequestBody BookingRecord bookingRecord, Errors errors)
	{
		if (errors.hasErrors())
		{
			return ResponseEntity.badRequest()
				.body(BookingErrorBuilder.fromBindingErrors(errors));
		}
		logger.info(">> Booking in progress...");
		try
		{
			BookingRecord record = this.service.book(bookingRecord);
			return ResponseEntity.ok(record);
		}
		catch (BookingException e)
		{
			logger.info(">> Exception occured {}", e.getMessage());
			return ResponseEntity.badRequest()
				.body(BookingErrorBuilder.create()
					.withError(e.getMessage())
					.build());
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<BookingRecord> getBooking(@PathVariable String id)
	{
		logger.info(">> Finding BookingRecord with id {}", id);
		Optional<BookingRecord> br = this.service.getBooking(id);
		return br.isPresent() ? ResponseEntity.ok(br.get())
				: ResponseEntity.notFound()
					.build();
	}

	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public BookingError handleException(Exception e)
	{
		logger.info(">> Exception caught {}", e.getClass().getTypeName());
		return BookingErrorBuilder.create()
			.withError(e.getMessage())
			.build();

	}

}

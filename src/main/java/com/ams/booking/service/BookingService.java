package com.ams.booking.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Service;

import com.ams.booking.entity.BookingRecord;
import com.ams.booking.entity.Inventory;
import com.ams.booking.entity.Passenger;
import com.ams.booking.exception.BookingException;
import com.ams.booking.repository.BookingRepository;
import com.ams.booking.repository.InventoryRepository;
import com.ams.booking.vo.Fare;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import lombok.RequiredArgsConstructor;

@Service
@EnableFeignClients
@RequiredArgsConstructor
public class BookingService
{
	private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

	private final BookingRepository   bookingRepository;
	private final InventoryRepository inventoryRepository;
	private final Sender              sender;
	private final FareServiceProxy    proxy;

	@HystrixCommand(fallbackMethod = "bookingFallBack")
	public BookingRecord book(BookingRecord record) throws BookingException
	{
		logger.info(">> Calling FareService");
		Fare fare1 = proxy.getFare(record.getFlightNumber(), record.getFlightDate())
			.getBody();
		logger.info(">> Fare received through Feign: {}", fare1);

		logger.info(">> Checking inventory associated with {} and {}", record
			.getFlightNumber(), record.getFlightDate());
		//check inventory
		Inventory inventory = inventoryRepository
			.findByFlightNumberAndFlightDate(record.getFlightNumber(), record.getFlightDate());

		if (!inventory.isAvailable(record.getPassengers()
			.size()))
		{
			throw new BookingException("No more seats avaialble");
		}

		logger.info(">> Successfully checked inventory: " + inventory);

		logger.info(">> Calling inventory to update it");
		//update inventory
		inventory.setAvailable(inventory.getAvailable() - record.getPassengers()
			.size());
		inventoryRepository.saveAndFlush(inventory);
		logger.info(">> Sucessfully updated inventory");

		//Setting status, setting passengers to bookingrecord and save booking
		record.setStatus(BookingStatus.BOOKING_CONFIRMED);
		Set<Passenger> passengers = record.getPassengers();
		passengers.forEach(passenger -> passenger.setBookingRecord(record));
		record.setBookingDate(LocalDate.now());
		BookingRecord response = bookingRepository.save(record);
		logger.info(">> Successfully saved booking");

		//send a message to search to update inventory
		logger.info(">> Sending booking event");

		Map<String, Object> bookingDetails = new HashMap<String, Object>();
		bookingDetails.put("FLIGHT_NUMBER", response.getFlightNumber());
		bookingDetails.put("FLIGHT_DATE", response.getFlightDate());
		bookingDetails.put("NEW_INVENTORY", inventory.getBookableInventory());
		sender.send(bookingDetails);
		logger.info(">> Booking event successfully delivered ");
		return response;
	}

	public BookingRecord bookingFallBack(BookingRecord record)
	{
		logger.info(">> Booking could not be processed. Executing fallback..");
		record.setId("fallback");
		record.setStatus(BookingStatus.BOOKING_NOT_CONFIRMED);
		return record;
	}

	public Optional<BookingRecord> getBooking(String id)
	{
		return bookingRepository.findById(id);

	}

	public void updateStatus(String status, String bookingId)
	{
		BookingRecord record = bookingRepository.findById(bookingId)
			.get();
		record.setStatus(status);
	}

}

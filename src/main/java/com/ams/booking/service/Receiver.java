package com.ams.booking.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class Receiver
{
	private static final Logger logger = LoggerFactory.getLogger(Receiver.class);

	private BookingService bookingService;

	@RabbitListener(queues = "CheckInQ")
	public void processMessage(String bookingId)
	{
		logger.info("BookingId {} received through meessage event", bookingId);
		bookingService.updateStatus(BookingStatus.CHECKED_IN, bookingId);
		logger.info(">>  Booking status updated to {}", BookingStatus.CHECKED_IN);
	}

}
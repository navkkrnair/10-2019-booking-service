package com.ams.booking;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.ams.booking.configuration.BookingProperties;
import com.ams.booking.entity.BookingRecord;
import com.ams.booking.entity.Inventory;
import com.ams.booking.entity.Passenger;
import com.ams.booking.repository.InventoryRepository;
import com.ams.booking.service.BookingService;
import com.ams.booking.service.BookingStatus;

import brave.sampler.Sampler;

@SpringBootApplication
@EnableConfigurationProperties(BookingProperties.class)
@EnableDiscoveryClient
@EnableCircuitBreaker
public class BookingServiceApplication
{
	private static final Logger logger = LoggerFactory.getLogger(BookingServiceApplication.class);

	@LoadBalanced
	@Bean
	RestTemplate restTemplate()
	{
		return new RestTemplate();
	}

	@Bean
	public Sampler defaultSampler()
	{
		return Sampler.ALWAYS_SAMPLE;
	}

	public static void main(String[] args)
	{
		SpringApplication.run(BookingServiceApplication.class, args);
	}

	@Autowired
	@Bean
	CommandLineRunner init(BookingService bookingService, InventoryRepository inventoryRepository)
	{
		return args ->
		{
			Inventory[] invs =
			{ new Inventory("BF100", LocalDate.of(2019, 01, 22), 100),
					new Inventory("BF101", LocalDate.of(2019, 01, 22), 100),
					new Inventory("BF102", LocalDate.of(2019, 01, 22), 100),
					new Inventory("BF103", LocalDate.of(2019, 01, 22), 100),
					new Inventory("BF104", LocalDate.of(2019, 01, 22), 100),
					new Inventory("BF105", LocalDate.of(2019, 01, 22), 100),
					new Inventory("BF106", LocalDate.of(2019, 01, 22), 100) };
			Arrays.asList(invs)
				.forEach(inventory -> inventoryRepository.save(inventory));
			LocalDate     date    = LocalDate.of(2019, 01, 22);
			BookingRecord booking = new BookingRecord("BF101", "NYC", "SFO", date, date, "101", BookingStatus.BOOKING_CONFIRMED);

			Set<Passenger> passengers = new HashSet<Passenger>();
			passengers.add(new Passenger("Krishna", "Kumar", "Male", booking));
			passengers.add(new Passenger("Deepa", "S", "Female", booking));
			passengers.add(new Passenger("Madhav", "K", "Male", booking));
			passengers.add(new Passenger("Mahadev", "K", "Female", booking));

			booking.setPassengers(passengers);
			BookingRecord record = bookingService.book(booking);
			if (record.getId()
				.equals("fallback"))
			{
				logger.info(">> Booking could not proceed");
				return;
			}
			logger.info(">> Booking successfully saved..." + record);

			logger.info(">> Looking to load booking record...");
			logger.info(">> Result: " + bookingService.getBooking(record.getId())
				.get());
		};
	}

}

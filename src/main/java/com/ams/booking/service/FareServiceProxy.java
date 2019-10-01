package com.ams.booking.service;

import java.time.LocalDate;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ams.booking.vo.Fare;

@FeignClient(name = "fare-service")
public interface FareServiceProxy
{
	@GetMapping("/fare")
	public ResponseEntity<Fare> getFare(@RequestParam(value = "flightNumber") String flightNumber, @RequestParam(value = "flightDate") @DateTimeFormat(iso = ISO.DATE) LocalDate flightDate);

}

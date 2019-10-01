package com.ams.booking.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "ams.booking")
@Data
public class BookingProperties
{
	private String fareUrl = "http://localhost:8080/fare";

}

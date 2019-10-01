package com.ams.booking.service;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class Sender
{

	private RabbitMessagingTemplate template;

	@Bean
	Queue searchQ()
	{
		return new Queue("SearchQ", false);
	}

	@Bean
	Queue checkInQ()
	{
		return new Queue("CheckInQ", false);
	}

	public void send(Object message)
	{
		template.convertAndSend("SearchQ", message);
	}
}
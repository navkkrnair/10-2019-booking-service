package com.ams.booking.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@RequiredArgsConstructor
@Data
@Entity
public class Inventory
{

	@Id
	@GeneratedValue(generator = "inventory-uuid")
	@GenericGenerator(name = "inventory-uuid", strategy = "uuid2")
	private String id;

	@NonNull
	@NotNull
	private String flightNumber;

	@NotNull
	@NonNull
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate flightDate;

	@NotNull
	@NonNull
	private int available;

	public boolean isAvailable(int count)
	{
		return ((available - count) > 5);
	}

	public int getBookableInventory()
	{
		return available - 5;
	}
}

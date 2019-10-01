package com.ams.booking.entity;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@RequiredArgsConstructor
@Data
@Entity
public class BookingRecord
{

	@Id
	@GeneratedValue(generator = "booking-uuid")
	@GenericGenerator(name = "booking-uuid", strategy = "uuid2")
	private String id;

	@NonNull
	@NotNull
	private String flightNumber;

	@NonNull
	@NotNull
	private String origin;

	@NonNull
	@NotNull
	private String destination;

	@NonNull
	@NotNull
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate flightDate;

	@NonNull
	@NotNull
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate bookingDate;

	@NonNull
	@NotNull
	private String amount;

	@NonNull
	@NotNull
	private String status;

	@NotNull
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@JsonProperty(access = Access.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "bookingRecord")
	Set<Passenger> passengers;
}

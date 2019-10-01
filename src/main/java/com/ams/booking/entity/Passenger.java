package com.ams.booking.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

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
public class Passenger
{

	@Id
	@GeneratedValue(generator = "passenger-uuid")
	@GenericGenerator(name = "passenger-uuid", strategy = "uuid2")
	private String id;

	@NonNull
	@NotNull
	private String firstName;

	@NonNull
	@NotNull
	private String lastName;

	@NonNull
	@NotNull
	private String gender;

	@NonNull
	@NotNull
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@JsonProperty(access = Access.WRITE_ONLY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BOOKING_ID")
	private BookingRecord bookingRecord;

}

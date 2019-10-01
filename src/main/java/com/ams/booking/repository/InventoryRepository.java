package com.ams.booking.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ams.booking.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, String>
{
	Inventory findByFlightNumberAndFlightDate(String flightNumber, LocalDate flightDate);
}

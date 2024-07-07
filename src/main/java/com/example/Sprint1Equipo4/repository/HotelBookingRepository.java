package com.example.Sprint1Equipo4.repository;

import com.example.Sprint1Equipo4.model.HotelBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelBookingRepository extends JpaRepository<HotelBooking, Long> {
}

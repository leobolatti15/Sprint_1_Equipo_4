package com.example.Sprint1Equipo4.repository;

import com.example.Sprint1Equipo4.model.HotelBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface HotelBookingRepository extends JpaRepository<HotelBooking, Long> {
    boolean existsByDateFromAndDateToAndDestinationAndHotelCode(
            LocalDate dateFrom, LocalDate dateTo, String destination, String hotelCode
    );
}

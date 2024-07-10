package com.example.Sprint1Equipo4.repository;

import com.example.Sprint1Equipo4.model.FlightReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface FlightReservationRepository extends JpaRepository<FlightReservation, Long> {
    boolean existsByDateFromAndDateToAndOriginAndDestination(
            LocalDate dateFrom, LocalDate dateTo, String origin, String destination);
}

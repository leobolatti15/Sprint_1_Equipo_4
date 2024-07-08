package com.example.Sprint1Equipo4.repository;

import com.example.Sprint1Equipo4.model.FlightReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightReservationRepository extends JpaRepository<FlightReservation, Long> {
}

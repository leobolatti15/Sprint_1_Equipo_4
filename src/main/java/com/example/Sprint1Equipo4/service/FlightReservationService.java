package com.example.Sprint1Equipo4.service;

import com.example.Sprint1Equipo4.dto.request.BoockingDto;
import com.example.Sprint1Equipo4.dto.request.FlightReqDto;
import com.example.Sprint1Equipo4.dto.request.FlightReservationDto;
import com.example.Sprint1Equipo4.dto.request.ReservationDtoRequest;
import com.example.Sprint1Equipo4.dto.response.StatusDTO;

import java.util.List;

public interface FlightReservationService {
    List<FlightReservationDto> listAllFlightReservations();

    StatusDTO updateFlightReservation(FlightReqDto reservation, Long id);

    StatusDTO deleteFlightReservation(Long id);
}

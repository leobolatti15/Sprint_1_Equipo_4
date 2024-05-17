package com.example.Sprint1Equipo4.service;

import com.example.Sprint1Equipo4.dto.request.FlightReqDto;
import com.example.Sprint1Equipo4.dto.response.FlightDTO;
import com.example.Sprint1Equipo4.dto.response.FlightResDto;
import com.example.Sprint1Equipo4.model.Flight;

import java.time.LocalDate;
import java.util.List;

    public interface FlightService {
        List<FlightDTO> listFlights();
        List<FlightDTO> flightsAvailable(LocalDate dateFrom, LocalDate dateTo, String origin, String destination);
        FlightResDto reserve(FlightReqDto flight);
        Flight create(FlightDTO flight);
        Flight upDate(FlightDTO flight);

    }


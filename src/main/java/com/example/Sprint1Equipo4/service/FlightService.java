package com.example.Sprint1Equipo4.service;

import com.example.Sprint1Equipo4.dto.request.FlightReqDto;
import com.example.Sprint1Equipo4.dto.response.FlightDTO;
import com.example.Sprint1Equipo4.dto.response.FlightResDto;

import java.util.List;

    public interface FlightService {
        List<FlightDTO> listFlights();
        FlightResDto reserve(FlightReqDto flight);
    }


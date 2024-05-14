package com.example.Sprint1Equipo4.service;


import com.example.Sprint1Equipo4.dto.response.FlightDTO;
import com.example.Sprint1Equipo4.dto.response.HotelDTO;
import com.example.Sprint1Equipo4.model.Flight;
import com.example.Sprint1Equipo4.model.Hotel;
import com.example.Sprint1Equipo4.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class FlightServiceImpl implements FlightService{

   @Autowired
   FlightRepository flightRepository;

   @Override
   public List<FlightDTO> listFlights() {
      List<Flight> listFlight = flightRepository.findAll();

      return listFlight.stream()
              .map(a -> new FlightDTO(
                      a.getFlightNumber(),
                      a.getOrigin(),
                      a.getDestination(),
                      a.getSeatType(),
                      a.getPricePerPerson(),
                      a.getDateFrom(),
                      a.getDateTo()))
              .toList();
   }
}

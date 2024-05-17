package com.example.Sprint1Equipo4.service;


import com.example.Sprint1Equipo4.dto.request.FlightReqDto;
import com.example.Sprint1Equipo4.dto.request.FlightReservationDto;
import com.example.Sprint1Equipo4.dto.response.FlightDTO;
import com.example.Sprint1Equipo4.dto.response.FlightResDto;
import com.example.Sprint1Equipo4.dto.response.ResponseFlightDTO;
import com.example.Sprint1Equipo4.dto.response.StatusDTO;
import com.example.Sprint1Equipo4.exception.FlightNotFoundException;
import com.example.Sprint1Equipo4.model.Flight;
import com.example.Sprint1Equipo4.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightServiceImpl implements FlightService {

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

   @Override
   public FlightDTO findByFlightName(String name){
      Flight flight = flightRepository.findByName(name);
      return new FlightDTO(
            flight.getFlightNumber(),
            flight.getOrigin(),
            flight.getDestination(),
            flight.getSeatType(),
              flight.getPricePerPerson(),
              flight.getDateFrom(),
              flight.getDateTo());
   }

   @Override
   public ResponseFlightDTO deleteFlight(String name){
      flightRepository.delete(name);
      return new ResponseFlightDTO("El vuelo fue borrado correctamente");
   }

   @Override
   public List<FlightDTO> flightsAvailable(LocalDate dateFrom, LocalDate dateTo, String origin, String destination) {
      List<Flight> listFlight = flightRepository.findAll();
      List<Flight> availableFlights = listFlight.stream()
            .filter(flight -> flight.getDestination().equals(destination))
            .filter(flight -> flight.getOrigin().equals(origin))
            .filter(flight -> !flight.getDateFrom().isAfter(dateTo))
            .filter(flight -> !flight.getDateTo().isBefore(dateFrom))
            .collect(Collectors.toList());


      return availableFlights.stream()
            .map(flight -> new FlightDTO(
                  flight.getFlightNumber(),
                  flight.getOrigin(),
                  flight.getDestination(),
                  flight.getSeatType(),
                  flight.getPricePerPerson(),
                  flight.getDateFrom(),
                  flight.getDateTo()))
            .collect(Collectors.toList());
   }


   private Flight getFlight(List<Flight> flights, FlightReservationDto fr) {
      return flights.stream()
            .filter(flight -> flight.getOrigin().equalsIgnoreCase(fr.getOrigin()) &&
                  flight.getDestination().equalsIgnoreCase(fr.getDestination()) &&
                  flight.getDateFrom().equals(fr.getDateFrom()) &&
                  flight.getDateTo().equals((fr.getDateTo())))
            .findFirst()
            .orElseThrow(FlightNotFoundException::new);
   }

   private Integer getTotalPrice(Flight flight, FlightReservationDto fr) {
      int price = flight.getPricePerPerson();
      if (fr.getPeople().isEmpty()) {

         return 0;
      }
      return price * fr.getPeople().size();
   }

   private double calculateInterest(double totalPrice, int dues) {
      if (dues > 1) {
         return totalPrice * 1.3;
      }
      return 1;
   }

   @Override
   public FlightResDto reserve(FlightReqDto fDto) {
      List<Flight> flightList = flightRepository.findAll();

      Flight flight = getFlight(flightList, fDto.getFlightReservationDto());

      int amount = getTotalPrice(flight, fDto.getFlightReservationDto());
      double interest = calculateInterest(amount, fDto.getFlightReservationDto()
            .getPaymentMethodsDto().getDues());
      double total = amount + interest;

      FlightResDto resDto = new FlightResDto();
      resDto.setUserName(fDto.getFlightReservationDto().getPeople().getFirst().getEmail());
      resDto.setAmount(amount);
      resDto.setInterest(interest);
      resDto.setTotal(total);
      resDto.setFlightReservationDto(fDto.getFlightReservationDto());
      resDto.setStatusDTO(new StatusDTO(201, "El proceso termino satisfactoriamente"));


      return resDto;
   }
}
package com.example.Sprint1Equipo4.service;

import com.example.Sprint1Equipo4.dto.request.FlightReqDto;
import com.example.Sprint1Equipo4.dto.request.FlightReservationDto;
import com.example.Sprint1Equipo4.dto.request.PaymentMethodsDto;
import com.example.Sprint1Equipo4.dto.response.FlightDTO;
import com.example.Sprint1Equipo4.dto.response.FlightResDto;
import com.example.Sprint1Equipo4.dto.response.ResponseFlightDTO;
import com.example.Sprint1Equipo4.dto.response.StatusDTO;
import com.example.Sprint1Equipo4.exception.*;
import com.example.Sprint1Equipo4.model.Flight;
import com.example.Sprint1Equipo4.model.FlightReservation;
import com.example.Sprint1Equipo4.model.PaymentMethod;
import com.example.Sprint1Equipo4.repository.FlightRepository;
import com.example.Sprint1Equipo4.repository.FlightReservationRepository;
import com.example.Sprint1Equipo4.repository.PaymentMethodRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightServiceImpl implements FlightService {

   @Autowired
   FlightRepository flightRepository;

   @Autowired
   FlightReservationRepository flightReservationRepository;

   @Autowired
   PaymentMethodRepository paymentMethodRepository;

   @Autowired
   private final ModelMapper modelMapper;


   public FlightServiceImpl(ModelMapper modelMapper,FlightRepository flightRepository) {
      this.modelMapper = modelMapper;
      this.flightRepository = flightRepository;
   }

   @Override
   public List<FlightDTO> listFlights() {
      List<Flight> listFlight = flightRepository.findAll();
      return listFlight.stream()
            .map(flight -> modelMapper.map(flight, FlightDTO.class))
            .collect(Collectors.toList());
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
            .map(flight -> modelMapper.map(flight, FlightDTO.class))
            .collect(Collectors.toList());
   }

   public DateOutOfRangeException validateDateRangeFlight(LocalDate dateFrom, LocalDate dateTo, String destination) {
      if (dateFrom.isAfter(dateTo)) {
         throw new DateOutOfRangeException();
      }

      List<Flight> flights = flightRepository.findAll();
      boolean isInRange = flights.stream()
            .filter(flight -> flight.getDestination().equals(destination))
            .anyMatch(flight ->
                  (dateFrom.isAfter(flight.getDateFrom()) || dateFrom.equals(flight.getDateFrom())) &&
                        (dateTo.isBefore(flight.getDateTo()) || dateTo.equals(flight.getDateTo()))
            );

      if (!isInRange) {
         throw new DateOutOfRangeException();
      }

      return null;
   }

   public Flight getFlight(List<Flight> flights, FlightReservationDto fr) {
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
      return fr.getPeople().isEmpty() ? 0 : price * fr.getPeople().size();
   }

   private double calculateInterest(double totalPrice, int dues, String type) {
      switch (type) {
         case "DEBIT":
            if (dues == 1) {
               return totalPrice;
            } else if(dues > 1){
               throw new InvalidDuesForDebit();
            }
         case "CREDIT":
            return calculateCreditInterest(totalPrice, dues);
         default:
            throw new IllegalArgumentException("Invalid payment type");
      }
   }

   private double calculateCreditInterest(double totalPrice, int dues) {
      if (dues >= 1 && dues <= 3) {
         return totalPrice * 1.05;
      } else if (dues > 3 && dues <= 6) {
         return totalPrice * 1.1;
      } else if (dues > 6 && dues <= 12) {
         return totalPrice * 1.15;
      } else {
         throw new InvalidDuesForCredit();
      }
   }

   @Override
   public FlightResDto reserve(FlightReqDto fDto) {
      List<Flight> flightList = flightRepository.findAll();

      Flight flight = getFlight(flightList, fDto.getFlightReservationDto());

      FlightReservationDto flightReservationDto = fDto.getFlightReservationDto();

      boolean exists = flightReservationRepository.existByDateFromAndDateToAndOriginAndDestination(
              flightReservationDto.getDateFrom(),
              flightReservationDto.getDateTo(),
              flightReservationDto.getOrigin(),
              flightReservationDto.getDestination()
      );
      if (exists) {
         throw new DuplicateBookingException();
      }

      int amount = getTotalPrice(flight, fDto.getFlightReservationDto());
      double interest = calculateInterest(amount, fDto.getFlightReservationDto()
            .getPaymentMethodsDto().getDues(), fDto.getFlightReservationDto().getPaymentMethodsDto().getType());
      double total = amount + interest;

      FlightResDto resDto = new FlightResDto();
      resDto.setUserName(fDto.getFlightReservationDto().getPeople().getFirst().getEmail());
      resDto.setAmount(amount);
      resDto.setInterest(interest);
      resDto.setTotal(total);
      resDto.setFlightReservationDto(fDto.getFlightReservationDto());
      resDto.setStatusDTO(new StatusDTO(201, "El proceso termino satisfactoriamente"));

      FlightReservation flightReservation = new FlightReservation();
      flightReservation.setUserName(fDto.getUserName());
      flightReservation.setDateFrom(flightReservationDto.getDateFrom());
      flightReservation.setDateTo(flightReservationDto.getDateTo());
      flightReservation.setOrigin(flightReservationDto.getOrigin());
      flightReservation.setDestination(flightReservationDto.getDestination());
      flightReservation.setFligthNumber(flightReservationDto.getFlightNumber());
      flightReservation.setSeatType(flightReservationDto.getSeatType());
      flightReservation.setSeats(flightReservationDto.getSeats());


      PaymentMethodsDto paymentDto = flightReservationDto.getPaymentMethodsDto();
      PaymentMethod paymentMethod = new PaymentMethod();
      paymentMethod.setType(paymentDto.getType());
      paymentMethod.setNumber(paymentDto.getNumberCard());
      paymentMethod.setDues(paymentDto.getDues());

      PaymentMethod existingPaymentMethod = paymentMethodRepository.findByTypeAndNumber(paymentMethod.getType(), paymentMethod.getNumber());
      if (existingPaymentMethod == null) {
         paymentMethodRepository.save(paymentMethod);
      } else {
         paymentMethod = existingPaymentMethod;
      }

      flightReservation.setPaymentMethod(paymentMethod);

      flightReservation.setUserName(fDto.getUserName());

      flightReservationRepository.save(flightReservation);

      return resDto;
   }

   @Override
   public FlightDTO findByFlightName(String name) {
      Flight flight = flightRepository.findByName(name);
      return modelMapper.map(flight, FlightDTO.class);
   }

   @Override
   public ResponseFlightDTO deleteFlight(String name) {
      flightRepository.delete(name);
      return new ResponseFlightDTO("El vuelo fue borrado correctamente");
   }

   @Override
   public Flight create(FlightDTO flight) {
      int flag = 0;
      Flight newFlight = new Flight();
      newFlight.setFlightNumber(flight.getFlightNumber());
      newFlight.setOrigin(flight.getOrigin());
      newFlight.setDestination(flight.getDestination());
      newFlight.setSeatType(flight.getSeatType());
      newFlight.setPricePerPerson(flight.getPricePerPerson());
      newFlight.setDateFrom(flight.getDepartureDate());
      newFlight.setDateTo(flight.getReturnDate());
      flightRepository.save(newFlight);
      return newFlight;
   }

   @Override
   public Flight upDate(FlightDTO flight) {
      Flight flightChek = create(flight);
      return flightChek;
   }
}

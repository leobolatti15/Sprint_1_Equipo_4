package com.example.Sprint1Equipo4.service;

import com.example.Sprint1Equipo4.dto.request.BoockingDto;
import com.example.Sprint1Equipo4.dto.request.ReservationDtoRequest;
import com.example.Sprint1Equipo4.dto.response.HotelDTO;
import com.example.Sprint1Equipo4.dto.response.ReservationDto;
import com.example.Sprint1Equipo4.dto.response.StatusDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HotelService {
   List<HotelDTO> listHotels();

   HotelDTO searchByCode(String hotelCode);

   Boolean existsHotel(String hotelCode);

   HotelDTO updateHotel(HotelDTO hotelDTO);

   List<HotelDTO> findAvailableHotels(LocalDate dateFrom, LocalDate dateTo, String destination);

   ReservationDto bookHotel(ReservationDtoRequest reservationDtoRequest);

   HotelDTO saveHotel(HotelDTO hotelDTO);

   StatusDTO deleteHotel(String hotelCode);

   Optional numOfPeople(BoockingDto bookingDto);

   void validateDateRange(LocalDate dateFrom, LocalDate dateTo, String destination);
}



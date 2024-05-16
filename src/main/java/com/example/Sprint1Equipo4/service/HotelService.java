package com.example.Sprint1Equipo4.service;

import com.example.Sprint1Equipo4.dto.request.ReservationDtoRequest;
import com.example.Sprint1Equipo4.dto.response.HotelDTO;


import java.time.LocalDate;

import com.example.Sprint1Equipo4.dto.response.ReservationDto;


import java.util.List;

public interface HotelService {
   List<HotelDTO> listHotels();


   List<HotelDTO> findAvailableHotels(LocalDate dateFrom, LocalDate dateTo, String destination);

   ReservationDto bookHotel(ReservationDtoRequest reservationDtoRequest);

}



package com.example.Sprint1Equipo4.service;

import com.example.Sprint1Equipo4.dto.request.ReservationDtoRequest;
import com.example.Sprint1Equipo4.dto.response.HotelDTO;
import com.example.Sprint1Equipo4.dto.response.ReservationDto;

import java.util.List;

public interface HotelService {
   List<HotelDTO> listHotels();
   ReservationDto bookHotel(ReservationDtoRequest reservationDtoRequest);
}

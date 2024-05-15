package com.example.Sprint1Equipo4.service;

import com.example.Sprint1Equipo4.dto.response.HotelDTO;

import java.time.LocalDate;
import java.util.List;

public interface HotelService {
   List<HotelDTO> listHotels();

   List<HotelDTO> findAvailableHotels(LocalDate dateFrom, LocalDate dateTo, String destination);
}



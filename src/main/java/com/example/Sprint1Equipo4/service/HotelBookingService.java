package com.example.Sprint1Equipo4.service;

import com.example.Sprint1Equipo4.dto.request.BoockingDto;
import com.example.Sprint1Equipo4.dto.request.ReservationDtoRequest;
import com.example.Sprint1Equipo4.dto.response.StatusDTO;

import java.util.List;

public interface HotelBookingService {

    List<BoockingDto> listAllHotelBookings();

    StatusDTO updateHotelBooking(ReservationDtoRequest booking, Long id);

    StatusDTO deleteHotelBooking(Long id);

}

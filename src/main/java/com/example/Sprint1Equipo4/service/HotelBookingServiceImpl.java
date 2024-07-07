package com.example.Sprint1Equipo4.service;

import com.example.Sprint1Equipo4.dto.request.BoockingDto;
import com.example.Sprint1Equipo4.dto.request.ReservationDtoRequest;
import com.example.Sprint1Equipo4.dto.response.StatusDTO;
import com.example.Sprint1Equipo4.repository.HotelBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class HotelBookingServiceImpl implements HotelBookingService{

    @Autowired
    HotelBookingRepository bookingRepository;

    @Override
    public List<BoockingDto> listAllHotelBookings() {
        return List.of();
    }

    @Override
    public StatusDTO updateHotelBooking(ReservationDtoRequest booking, Long id) {
        return null;
    }

    @Override
    public StatusDTO deleteHotelBooking(Long id) {
        return null;
    }
}

package com.example.Sprint1Equipo4.service;

import com.example.Sprint1Equipo4.dto.response.HotelDTO;
import com.example.Sprint1Equipo4.model.Hotel;
import com.example.Sprint1Equipo4.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {

   @Autowired
   HotelRepository hotelRepository;

   @Override
   public List<HotelDTO> listHotels() {
      List<Hotel> listHotel = hotelRepository.findAll();
      return listHotel.stream()
            .map(a -> new HotelDTO(
                  a.getName(),
                  a.getDestination(),
                  a.getRoomType(),
                  a.getPricePerNight(),
                  a.getDateFrom(),
                  a.getDateTo(),
                  a.getReserved()))
                  .toList();
   }
}

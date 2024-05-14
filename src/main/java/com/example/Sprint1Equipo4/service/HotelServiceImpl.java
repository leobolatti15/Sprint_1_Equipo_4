package com.example.Sprint1Equipo4.service;

import com.example.Sprint1Equipo4.dto.response.HotelDTO;
import com.example.Sprint1Equipo4.model.Hotel;
import com.example.Sprint1Equipo4.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelServiceImpl implements HotelService {

   @Autowired
   HotelRepository hotelRepository;

   @Override
   public List<HotelDTO> listHotels() {
      List<Hotel> listHotel = hotelRepository.findAll();
      return listHotel.stream()
            .map(a -> new HotelDTO(
                  a.getHotelCode(),
                  a.getName(),
                  a.getDestination(),
                  a.getRoomType(),
                  a.getPricePerNight(),
                  a.getDateFrom(),
                  a.getDateTo(),
                  a.getReserved()))
                  .toList();
   }

   @Override
   public List<HotelDTO> findAvailableHotels(LocalDate dateFrom, LocalDate dateTo, String destination) {
      List<Hotel> allHotels = hotelRepository.findAll();
      List<Hotel> availableHotels = allHotels.stream()
              .filter(hotel -> hotel.getDestination().equals(destination))
              .filter(hotel -> !hotel.getReserved())
              .filter(hotel -> dateFrom.isBefore(hotel.getDateTo()) && dateTo.isAfter(hotel.getDateFrom()))

              .toList();


      List<HotelDTO> availableHotelsDTO = new ArrayList<>();
      for (Hotel hotel : availableHotels) {
         availableHotelsDTO.add(new HotelDTO(hotel.getHotelCode(),
                 hotel.getName(),
                 hotel.getDestination(),
                 hotel.getRoomType(),
                 hotel.getPricePerNight(),
                 hotel.getDateFrom(),
                 hotel.getDateTo(),
                 hotel.getReserved()));
      }

      return availableHotelsDTO;
   }
   }


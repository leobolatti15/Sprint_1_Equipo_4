package com.example.Sprint1Equipo4.repository;

import com.example.Sprint1Equipo4.model.Hotel;
import java.util.List;

public interface HotelRepository {
   List<Hotel> findAll();

   Hotel findByCode(String hotelCode);

   boolean existsByCode(String hotelCode);

   Hotel update(Hotel hotel);
}

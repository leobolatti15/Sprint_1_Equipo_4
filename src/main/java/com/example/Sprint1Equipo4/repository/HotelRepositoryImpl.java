package com.example.Sprint1Equipo4.repository;

import com.example.Sprint1Equipo4.model.Hotel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Repository
public class HotelRepositoryImpl implements HotelRepository {

   private List<Hotel> hotels;

   public HotelRepositoryImpl() {
      this.hotels = loadData();
   }

   @Override
   public List<Hotel> findAll() {
      return hotels;
   }

   @Override
   public Hotel findByCode(String hotelCode) {
      return hotels
            .stream()
            .filter(hotel -> hotel.getHotelCode().equals(hotelCode))
            .findFirst()
            .orElse(null);
   }

   @Override
   public boolean existsByCode(String hotelCode) {
      Hotel hotel = this.findByCode(hotelCode);
      return hotel != null;
   }

   @Override
   public Hotel update(Hotel hotel) {
      Hotel alumnoEncontrado = findByCode(hotel.getHotelCode());
      hotel.setHotelCode(alumnoEncontrado.getHotelCode());
      hotels.remove(alumnoEncontrado);
      hotels.add(hotel);
      return hotel;
   }

   private List<Hotel> loadData() {
      List<Hotel> loadedData = null;
      File file;

      ObjectMapper objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
            .registerModule(new ParameterNamesModule()) //para mantener el orden de los atributos
            .registerModule(new JavaTimeModule());

      TypeReference<List<Hotel>> typeRef = new TypeReference<>() {
      };

      try {
         file = ResourceUtils.getFile("classpath:hotel.json");
         loadedData = objectMapper.readValue(file, typeRef);
      } catch (FileNotFoundException e) {
         e.printStackTrace();
         System.out.println("Failed while initializing DB, check your resources files");
      } catch (IOException e) {
         e.printStackTrace();
         System.out.println("Failed while initializing DB, check your JSON formatting.");
      }
      return loadedData;
   }
}

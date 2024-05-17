package com.example.Sprint1Equipo4.controller;

import com.example.Sprint1Equipo4.dto.request.ReservationDtoRequest;
import com.example.Sprint1Equipo4.dto.response.HotelDTO;
import com.example.Sprint1Equipo4.dto.response.ReservationDto;
import com.example.Sprint1Equipo4.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")

public class HotelController {
   @Autowired
   private HotelService hotelService;

   @GetMapping("/hotels")

   public ResponseEntity<List<HotelDTO>> listHotels(
           @RequestParam (value="date_from", required = false)  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateFrom,
           @RequestParam (value="date_to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateTo,
           @RequestParam (value="destination", required = false) String destination) {



      if (dateFrom == null || dateTo == null || destination == null) {
         List<HotelDTO> allHotels = hotelService.listHotels();
         System.out.println("Advertencia: Uno o más parámetros están ausentes. Por favor ingresar fechas y destino.");
         return new ResponseEntity<>(allHotels, HttpStatus.OK);

      }

      hotelService.validateDateRange(dateFrom, dateTo, destination);

      List<HotelDTO> availableHotels = hotelService.findAvailableHotels(dateFrom, dateTo, destination);
      return new ResponseEntity<>(availableHotels, HttpStatus.OK);
   }

   @PostMapping("/booking")
   public ResponseEntity<ReservationDto> bookHotel(@RequestBody ReservationDtoRequest reservationDtoRequest) {
      return new ResponseEntity<>(hotelService.bookHotel(reservationDtoRequest), HttpStatus.CREATED);
   }

   @GetMapping("/hotel/{hotelCode}")
   public ResponseEntity<HotelDTO> searchByCode(@PathVariable String hotelCode) {
      return new ResponseEntity<>(hotelService.searchByCode(hotelCode), HttpStatus.OK);
   }

   @PutMapping("/hotel/{hotelCode}")
   public ResponseEntity<HotelDTO> updateHotel(@RequestBody HotelDTO hotelDTO, @PathVariable String hotelCode) {
      return new ResponseEntity<>(hotelService.updateHotel(hotelDTO), HttpStatus.OK);
   }
}


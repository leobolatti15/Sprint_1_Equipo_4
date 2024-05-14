package com.example.Sprint1Equipo4.controller;

import com.example.Sprint1Equipo4.dto.request.ReservationDtoRequest;
import com.example.Sprint1Equipo4.dto.response.HotelDTO;
import com.example.Sprint1Equipo4.dto.response.ReservationDto;
import com.example.Sprint1Equipo4.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")

public class HotelController {
   @Autowired
   private HotelService hotelService;

   @GetMapping("/hotels")
   public ResponseEntity<List<HotelDTO>> listHotels() {
      return new ResponseEntity<>(hotelService.listHotels(), HttpStatus.OK);
   }

   @PostMapping("/booking")
   public ResponseEntity<ReservationDto> bookHotel(@RequestBody ReservationDtoRequest reservationDtoRequest) {
      return new ResponseEntity<>(hotelService.bookHotel(reservationDtoRequest), HttpStatus.CREATED);
   }
}

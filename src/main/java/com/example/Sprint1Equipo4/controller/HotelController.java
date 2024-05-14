package com.example.Sprint1Equipo4.controller;

import com.example.Sprint1Equipo4.dto.response.HotelDTO;
import com.example.Sprint1Equipo4.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static com.sun.beans.introspect.PropertyInfo.Name.required;

@RestController
@RequestMapping("/api/v1")

public class HotelController {
   @Autowired
   private HotelService hotelService;

   @GetMapping("/hotels")
   public ResponseEntity<List<HotelDTO>> listHotels(
           @RequestParam (value="date_from")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateFrom,
           @RequestParam (value="date_to") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateTo,
           @RequestParam (value="destination") String destination) {
      List<HotelDTO> availableHotels = hotelService.findAvailableHotels(dateFrom, dateTo, destination);

      return new ResponseEntity<>(availableHotels, HttpStatus.OK);
   }

}

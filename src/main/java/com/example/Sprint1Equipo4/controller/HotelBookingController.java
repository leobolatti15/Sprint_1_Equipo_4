package com.example.Sprint1Equipo4.controller;

import com.example.Sprint1Equipo4.dto.request.BoockingDto;
import com.example.Sprint1Equipo4.dto.request.ReservationDtoRequest;
import com.example.Sprint1Equipo4.dto.response.StatusDTO;
import com.example.Sprint1Equipo4.service.HotelBookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1")
public class HotelBookingController {
    @Autowired
    HotelBookingService hotelBookingService;

    @GetMapping("/hotel-bookings") //Listado de todas las  reservas de hotel.
    public ResponseEntity<List<BoockingDto>> listHotelBookings(){
        return new ResponseEntity<>(hotelBookingService.listAllHotelBookings(), HttpStatus.OK);
    }

    @PutMapping("/edit") // Actualizar una reserva buscando por id
    public ResponseEntity<StatusDTO> editBooking(@RequestBody @Valid ReservationDtoRequest booking, @RequestParam Long id){
        StatusDTO status = hotelBookingService.updateHotelBooking(booking, id);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<StatusDTO> deleteBooking(@RequestParam Long id){
        StatusDTO status = hotelBookingService.deleteHotelBooking(id);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}

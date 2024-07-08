package com.example.Sprint1Equipo4.controller;

import com.example.Sprint1Equipo4.dto.request.BoockingDto;
import com.example.Sprint1Equipo4.dto.request.FlightReqDto;
import com.example.Sprint1Equipo4.dto.request.FlightReservationDto;
import com.example.Sprint1Equipo4.dto.request.ReservationDtoRequest;
import com.example.Sprint1Equipo4.dto.response.StatusDTO;
import com.example.Sprint1Equipo4.service.FlightReservationService;
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
public class FlightReservationController {
    @Autowired
    FlightReservationService flightReservationService;

    @GetMapping("/flight-reservations") //Listado de todas las  reservas de vuelo.
    public ResponseEntity<List<FlightReservationDto>> listReservations(){
        return new ResponseEntity<>(flightReservationService.listAllFlightReservations(), HttpStatus.OK);
    }

    @PutMapping("/flight-reservation/edit") // Actualizar una reserva buscando por id
    public ResponseEntity<StatusDTO> editReservation(@RequestBody @Valid FlightReqDto reservation, @RequestParam Long id){
        StatusDTO status = flightReservationService.updateFlightReservation(reservation, id);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @DeleteMapping("/flight-reservation/delete") // Borrar una reserva buscando por id
    public ResponseEntity<StatusDTO> deleteReservation(@RequestParam Long id){
        StatusDTO status = flightReservationService.deleteFlightReservation(id);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

}

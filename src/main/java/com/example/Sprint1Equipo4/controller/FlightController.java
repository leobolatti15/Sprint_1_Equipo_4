package com.example.Sprint1Equipo4.controller;

import com.example.Sprint1Equipo4.dto.response.FlightDTO;
import com.example.Sprint1Equipo4.dto.response.HotelDTO;
import com.example.Sprint1Equipo4.service.FlightService;
import com.example.Sprint1Equipo4.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")

public class FlightController {
    @Autowired
    private FlightService flightService;

    @GetMapping("/flights")
    public ResponseEntity<List<FlightDTO>> listFlights() {
        return new ResponseEntity<>(flightService.listFlights(), HttpStatus.OK);
    }

    @PostMapping("/flight-reservation")
    public ResponseEntity<FlightResDto> reserve( @RequestBody FlightReqDto flightReqDto){
        return new ResponseEntity<>(flightService.reserve(flightReqDto),HttpStatus.CREATED);
    }

}



package com.example.Sprint1Equipo4.repository;
import com.example.Sprint1Equipo4.model.Flight;
import java.util.List;

public interface FlightRepository {
    List<Flight> findAll();
}

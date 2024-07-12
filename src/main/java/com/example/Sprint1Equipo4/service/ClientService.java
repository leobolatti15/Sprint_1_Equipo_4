package com.example.Sprint1Equipo4.service;

import com.example.Sprint1Equipo4.dto.response.ClientDTO;
import com.example.Sprint1Equipo4.model.Client;

import java.util.List;

public interface ClientService {
   List<ClientDTO> getHotelsTop3ClientsByBookingQuantity(int year);
   void updateBookingQuantity(String username);

//   List<ClientDTO> getFlightsTop3ClientsByBookingQuantity(int year);
}

package com.example.Sprint1Equipo4.service;

import com.example.Sprint1Equipo4.dto.response.ClientDTO;
import com.example.Sprint1Equipo4.model.Client;
import com.example.Sprint1Equipo4.model.HotelBooking;
import com.example.Sprint1Equipo4.repository.ClientRepository;
import com.example.Sprint1Equipo4.repository.HotelBookingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

   @Autowired
   private ClientRepository clientRepository;

   @Autowired
   private HotelBookingRepository hotelBookingRepository;

   @Autowired
   private ModelMapper modelMapper;

   public List<ClientDTO> getHotelsTop3ClientsByBookingQuantity(int year) {
      List<Client> clients = clientRepository.findBookingQuantityByYear(year);

      // Obtener solo los primeros 3 clientes (Top 3)
      List<Client> top3Clients = clients.stream()
            .limit(3)
            .toList();

      // Mapear los clientes a ClientDTO y configurar topNumber, year y totalAmount
      return top3Clients.stream()
            .map(client -> {
               ClientDTO clientDTO = modelMapper.map(client, ClientDTO.class);
               clientDTO.setTopNumber(getTopNumberForClient(client));
               clientDTO.setYear(LocalDate.now().getYear());
               clientDTO.setTotalAmount(calculateTotalAmount(client.getUserName()));
               return clientDTO;
            })
            .toList();
   }

   public void updateBookingQuantity(String username) {
      Client client = clientRepository.findByUserName(username);
      client.setBookingQuantity(client.getBookingQuantity() + 1);
      clientRepository.save(client);
   }

   private double calculateTotalAmount(String userName) {
      // Obtener el cliente por su userName
      Client client = clientRepository.findByUserName(userName);
      double totalAmount = 0.0;

      // Obtener las reservas del cliente
      List<HotelBooking> bookings = client.getHotelBooking();

      // Iterar sobre cada reserva y sumar el total de cada una
      for (HotelBooking booking : bookings) {
         totalAmount += booking.getTotalPrice();
      }
      return totalAmount;
   }

   private int getTopNumberForClient(Client client) {
      // Obtener la lista de clientes ordenada por cantidad de reservas para el año actual
      int currentYear = LocalDate.now().getYear();
      List<Client> clientsSorted = clientRepository.findBookingQuantityByYear(currentYear);

      // Encontrar la posición del cliente actual en la lista ordenada
      int index = clientsSorted.indexOf(client);

      // Asignar topNumber (1 para el primero, 2 para el segundo, etc.)
      return index + 1;
   }

//   public List<ClientDTO> getFlightsTop3ClientsByBookingQuantity(int year) {
//      //Pageable topThree = PageRequest.of(0, 3);
//      List<Client> clients = clientRepository.findBookingQuantityByYear(year);
//      return clients.stream()
//            .map(client -> modelMapper.map(client, ClientDTO.class))
//            .collect(Collectors.toList());
//   }

}

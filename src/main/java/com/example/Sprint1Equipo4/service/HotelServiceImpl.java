package com.example.Sprint1Equipo4.service;

import com.example.Sprint1Equipo4.dto.request.BoockingDto;
import com.example.Sprint1Equipo4.dto.request.PaymentMethodsDto;
import com.example.Sprint1Equipo4.dto.request.ReservationDtoRequest;
import com.example.Sprint1Equipo4.dto.response.HotelDTO;
import com.example.Sprint1Equipo4.dto.response.ReservationDto;
import com.example.Sprint1Equipo4.dto.response.StatusDTO;
import com.example.Sprint1Equipo4.model.Hotel;
import com.example.Sprint1Equipo4.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {

   @Autowired
   HotelRepository hotelRepository;

   @Override
   public List<HotelDTO> listHotels() {
      List<Hotel> listHotel = hotelRepository.findAll();
      return listHotel.stream()
            .map(a -> new HotelDTO(
                  a.getName(),
                  a.getDestination(),
                  a.getRoomType(),
                  a.getPricePerNight(),
                  a.getDateFrom(),
                  a.getDateTo(),
                  a.getReserved()))
                  .toList();
   }

   //PARA CREAR UNA RESERVA EN HOTEL
   private Hotel selectHotel(List<Hotel> availableHotels, BoockingDto bookingDto) {
      Hotel hotelEncontrado = null;
      for (Hotel hotel : availableHotels) {
         if (hotel.getDestination().equalsIgnoreCase(bookingDto.getDestination()) &&
               isDateRangeAvailable(hotel, bookingDto.getDateFrom(), bookingDto.getDateTo())) {
            hotelEncontrado = hotel;
         }
      }
      return hotelEncontrado;
   }

   private boolean isDateRangeAvailable(Hotel hotel, LocalDate dateFrom, LocalDate dateTo) {
      return !hotel.getReserved() && hotel.getDateFrom().equals(dateFrom) && hotel.getDateTo().equals(dateTo);
   }

   private double calculateTotalPrice(Hotel hotel, LocalDate dateFrom, LocalDate dateTo) {
      long nights = dateFrom.until(dateTo).getDays();
      return hotel.getPricePerNight() * nights;
   }

   private double calculateInterest(double totalPrice, int dues) {
      if (dues == 0) {
         return 0;
      }
      return totalPrice * 0.055;
   }

   public ReservationDto bookHotel(ReservationDtoRequest reservationDtoRequest) {
      List<Hotel> availableHotels = hotelRepository.findAll(); //ver caso si reservado es true
      Hotel selectedHotel = selectHotel(availableHotels, reservationDtoRequest.getBooking());

      if (selectedHotel == null) {
         return null; //excepcion si el hotel no cumple los requisitos
      }

      double totalPrice = calculateTotalPrice(selectedHotel, reservationDtoRequest.getBooking().getDateFrom(), reservationDtoRequest.getBooking().getDateTo());
      double interest = calculateInterest(totalPrice, reservationDtoRequest.getBooking().getPayment().getDues());
      double total = totalPrice + interest;

      ReservationDto reservationDto = new ReservationDto();
      reservationDto.setUserName(reservationDtoRequest.getUserName());
      reservationDto.setAmount(String.format("%.2f", totalPrice));
      reservationDto.setInterest(String.format("%.2f", interest));
      reservationDto.setTotal(String.format("%.2f", total));
      reservationDto.setBooking(reservationDtoRequest.getBooking());
      reservationDto.setStatus(new StatusDTO(201, "La reserva terminó satisfactoriamente"));
      System.out.println(reservationDto);
      return reservationDto;
   }
}

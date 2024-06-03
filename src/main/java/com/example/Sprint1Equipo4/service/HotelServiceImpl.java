package com.example.Sprint1Equipo4.service;

import com.example.Sprint1Equipo4.dto.request.BoockingDto;
import com.example.Sprint1Equipo4.dto.request.ReservationDtoRequest;
import com.example.Sprint1Equipo4.dto.response.HotelDTO;
import com.example.Sprint1Equipo4.dto.response.ReservationDto;
import com.example.Sprint1Equipo4.dto.response.StatusDTO;
import com.example.Sprint1Equipo4.exception.DateOutOfRangeException;
import com.example.Sprint1Equipo4.exception.HotelNotFoundException;
import com.example.Sprint1Equipo4.model.Hotel;
import com.example.Sprint1Equipo4.repository.HotelRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelServiceImpl implements HotelService {

   @Autowired
   HotelRepository hotelRepository;

   @Autowired
   private final ModelMapper modelMapper;

   public HotelServiceImpl(ModelMapper modelMapper) {
      this.modelMapper = modelMapper;
   }

   @Override
   public List<HotelDTO> listHotels() {
      List<Hotel> listHotel = hotelRepository.findAll();
      return listHotel.stream()
            .map(hotel -> modelMapper.map(hotel, HotelDTO.class))
            .collect(Collectors.toList());
   }

   @Override
   public HotelDTO searchByCode(String hotelCode) {
      Hotel hotel = hotelRepository.findByCode(hotelCode);
      if (!hotelRepository.existsByCode(hotelCode)) {
         throw new HotelNotFoundException();
      } else {
         return modelMapper.map(hotel, HotelDTO.class);
      }
   }

   @Override
   public Boolean existsHotel(String hotelCode) {
      return hotelRepository.existsByCode(hotelCode);
   }

   @Override
   public HotelDTO saveHotel(HotelDTO hotelDTO) {
      Hotel hotel = new Hotel();
      hotel.setHotelCode(hotelDTO.getHotelCode());
      hotel.setName(hotelDTO.getName());
      hotel.setDestination(hotelDTO.getDestination());
      hotel.setRoomType(hotelDTO.getRoomType());
      hotel.setPricePerNight(hotelDTO.getPricePerNight());
      hotel.setDateFrom(hotelDTO.getDateFrom());
      hotel.setDateTo(hotelDTO.getDateTo());
      hotel.setReserved(hotelDTO.getReserved());
      hotelRepository.save(hotel);
      return hotelDTO;
   }

   @Override
   public HotelDTO updateHotel(HotelDTO hotelDTO) {
      Hotel hotel = new Hotel();
      hotel.setHotelCode(hotelDTO.getHotelCode());
      hotel.setName(hotelDTO.getName());
      hotel.setDestination(hotelDTO.getDestination());
      hotel.setRoomType(hotelDTO.getRoomType());
      hotel.setPricePerNight(hotelDTO.getPricePerNight());
      hotel.setDateFrom(hotelDTO.getDateFrom());
      hotel.setDateTo(hotelDTO.getDateTo());

      hotelRepository.update(hotel);
      return hotelDTO;
   }

   @Override
   public List<HotelDTO> findAvailableHotels(LocalDate dateFrom, LocalDate dateTo, String destination) {
      List<Hotel> allHotels = hotelRepository.findAll();
      List<Hotel> availableHotels = allHotels.stream()
            .filter(hotel -> !hotel.getReserved())
            .filter(hotel -> dateFrom.isBefore(hotel.getDateTo()) && dateTo.isAfter(hotel.getDateFrom()))
            .filter(hotel -> hotel.getDestination().equals(destination))
            .toList();

      List<HotelDTO> availableHotelsDTO = new ArrayList<>();
      for (Hotel hotel : availableHotels) {
         availableHotelsDTO.add(modelMapper.map(hotel, HotelDTO.class));
      }
      return availableHotelsDTO;
   }

   @Override
   public StatusDTO deleteHotel(String hotelCode) {
      hotelRepository.delete(hotelCode);
      return new StatusDTO(200, "El hotel se eliminó exitosamente");
   }

   public void validateDateRange(LocalDate dateFrom, LocalDate dateTo, String destination) {
      if (dateFrom.isAfter(dateTo)) {
         throw new DateOutOfRangeException();
      }

      List<Hotel> hotels = hotelRepository.findAll();
      boolean isInRange = hotels.stream()
            .filter(hotel -> hotel.getDestination().equals(destination))
            .anyMatch(hotel ->
                  (dateFrom.isAfter(hotel.getDateFrom()) || dateFrom.equals(hotel.getDateFrom())) &&
                        (dateTo.isBefore(hotel.getDateTo()) || dateTo.equals(hotel.getDateTo()))
            );

      if (!isInRange) {
         throw new DateOutOfRangeException();
      }
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

   @Override
   public ReservationDto bookHotel(ReservationDtoRequest reservationDtoRequest) {
      List<Hotel> allHotels = hotelRepository.findAll();
      List<Hotel> availableHotels = allHotels.stream()
            .filter(hotel -> !hotel.getReserved())
            .collect(Collectors.toList());

      Hotel selectedHotel = selectHotel(availableHotels, reservationDtoRequest.getBooking());

      if (selectedHotel == null) {
         throw new HotelNotFoundException();
      }

      double totalPrice = calculateTotalPrice(selectedHotel, reservationDtoRequest.getBooking().getDateFrom(), reservationDtoRequest.getBooking().getDateTo());
      double interest = calculateInterest(totalPrice, reservationDtoRequest.getBooking().getPayment().getDues());
      double total = totalPrice + interest;

      ReservationDto reservationDto = new ReservationDto();
      reservationDto.setUserName(reservationDtoRequest.getUserName());
      reservationDto.setAmount(String.valueOf(totalPrice));
      reservationDto.setInterest(String.valueOf(interest));
      reservationDto.setTotal(String.valueOf(total));
      reservationDto.setBooking(reservationDtoRequest.getBooking());
      reservationDto.setStatus(new StatusDTO(201, "La reserva se realizó satisfactoriamente"));

      return reservationDto;
   }

   private double calculateTotalPrice(Hotel hotel, LocalDate dateFrom, LocalDate dateTo) {
      long nights = dateFrom.until(dateTo).getDays();
      return hotel.getPricePerNight() * nights;
   }

   private double calculateInterest(double totalPrice, int dues) {
      if (dues == 1) {
         return totalPrice * 1;
      } else if (dues > 1 && dues <= 3) {
         return totalPrice * 1.05;
      } else if (dues > 3 && dues <= 6) {
         return totalPrice * 1.1;
      } else
         return totalPrice * 1.15;
   }
}


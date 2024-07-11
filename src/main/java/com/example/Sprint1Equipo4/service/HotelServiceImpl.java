package com.example.Sprint1Equipo4.service;

import com.example.Sprint1Equipo4.dto.request.BoockingDto;
import com.example.Sprint1Equipo4.dto.request.PaymentMethodsDto;
import com.example.Sprint1Equipo4.dto.request.ReservationDtoRequest;
import com.example.Sprint1Equipo4.dto.response.HotelDTO;
import com.example.Sprint1Equipo4.dto.response.ReservationDto;
import com.example.Sprint1Equipo4.dto.response.StatusDTO;
import com.example.Sprint1Equipo4.exception.*;
import com.example.Sprint1Equipo4.model.Hotel;
import com.example.Sprint1Equipo4.model.HotelBooking;
import com.example.Sprint1Equipo4.model.PaymentMethod;
import com.example.Sprint1Equipo4.repository.HotelBookingRepository;
import com.example.Sprint1Equipo4.repository.HotelRepository;
import com.example.Sprint1Equipo4.repository.PaymentMethodRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HotelServiceImpl implements HotelService {

   @Autowired
   private HotelRepository hotelRepository;

   @Autowired
   private PaymentMethodRepository paymentMethodRepository;

   @Autowired
   private HotelBookingRepository hotelBookingRepository;

   @Autowired
   private final ModelMapper modelMapper;

   public HotelServiceImpl(HotelRepository hotelRepository,ModelMapper modelMapper) {
      this.modelMapper = modelMapper;
      this.hotelRepository = hotelRepository;
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
      Optional<Hotel> hotel = hotelRepository.findByHotelCode(hotelCode);
      if (!hotelRepository.existsByHotelCode(hotelCode)) {
         throw new HotelNotFoundException();
      } else {
         return modelMapper.map(hotel, HotelDTO.class);
      }
   }

   @Override
   public Boolean existsHotel(String hotelCode) {
      return hotelRepository.existsByHotelCode(hotelCode);
   }

   @Override
   public HotelDTO saveHotel(HotelDTO hotelDTO) {
      Hotel hotel = new Hotel();
      modelMapper.map(hotelDTO,hotel);
      hotelRepository.save(hotel);
      return hotelDTO;
   }

   @Override
   public HotelDTO updateHotel(HotelDTO hotelDTO) {
      Hotel guardado = hotelRepository.findByHotelCode(hotelDTO.getHotelCode()).orElse(null);
      Hotel hotel = modelMapper.map(hotelDTO,Hotel.class);
       hotel.setId(guardado.getId());
      hotelRepository.save(hotel);
      return modelMapper.map(hotel,HotelDTO.class);
   }

   @Override
   public List<HotelDTO> findAvailableHotels(LocalDate dateFrom, LocalDate dateTo, String destination) {
      List<Hotel> allHotels = hotelRepository.findAll();
      List<Hotel> availableHotels = allHotels.stream()
            .filter(hotel -> !hotel.getReserved())
            .filter(hotel -> (hotel.getDateFrom().isAfter(dateFrom) || hotel.getDateTo().isBefore(dateTo))
                    || (hotel.getDateFrom().equals(dateFrom) && hotel.getDateTo().equals(dateTo)))
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
      Hotel hotel = hotelRepository.findByHotelCode(hotelCode).orElse(null);
      Long id = (Long) hotel.getId();
      hotelRepository.deleteById(id);
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
   public StatusDTO bookHotel(ReservationDtoRequest reservationDtoRequest) {
      BoockingDto bookingDto = reservationDtoRequest.getBooking();
      numOfPeople(bookingDto);

      boolean exists = hotelBookingRepository.existsByDateFromAndDateToAndDestinationAndHotelCode(
              bookingDto.getDateFrom(),
              bookingDto.getDateTo(),
              bookingDto.getDestination(),
              bookingDto.getHotelCode()
      );
      if (exists) {
         throw new DuplicateBookingException();
      }

      List<Hotel> allHotels = hotelRepository.findAll();
      List<Hotel> availableHotels = allHotels.stream()
            .filter(hotel -> !hotel.getReserved())
            .collect(Collectors.toList());

      Hotel selectedHotel = selectHotel(availableHotels, reservationDtoRequest.getBooking());

      if (selectedHotel == null) {
         throw new HotelNotFoundException();
      }

      double totalPrice = calculateTotalPrice(selectedHotel, reservationDtoRequest.getBooking().getDateFrom(), reservationDtoRequest.getBooking().getDateTo());
      double interest = calculateInterest(totalPrice, reservationDtoRequest.getBooking().getPayment().getDues(), reservationDtoRequest.getBooking().getPayment().getType());
      double total = totalPrice + interest;

      ReservationDto reservationDto = new ReservationDto();
      reservationDto.setUserName(reservationDtoRequest.getUserName());
      reservationDto.setAmount(String.valueOf(totalPrice));
      reservationDto.setInterest(String.valueOf(interest));
      reservationDto.setTotal(String.valueOf(total));
      reservationDto.setBooking(reservationDtoRequest.getBooking());
      reservationDto.setStatus(new StatusDTO(201, "La reserva se realizó satisfactoriamente"));



      HotelBooking hotelBooking = new HotelBooking();
      hotelBooking.setUserName(reservationDtoRequest.getUserName());
      hotelBooking.setHotelCode(bookingDto.getHotelCode());
      hotelBooking.setDateTo(bookingDto.getDateTo());
      hotelBooking.setDateFrom(bookingDto.getDateFrom());
      hotelBooking.setDestination(bookingDto.getDestination());
      hotelBooking.setRoomType(bookingDto.getRoomType());
      hotelBooking.setReservedDate(LocalDate.now());
      hotelBooking.setPeopleAmount(bookingDto.getPeopleAmount());
      hotelBooking.setTotalPrice(Double.valueOf(reservationDto.getTotal()));

      // Mapear el DTO a la entidad PaymentMethod
      PaymentMethodsDto paymentDto = bookingDto.getPayment();
      PaymentMethod paymentMethod = new PaymentMethod();
      paymentMethod.setType(paymentDto.getType());
      paymentMethod.setNumber(paymentDto.getNumberCard());
      paymentMethod.setDues(paymentDto.getDues());

      PaymentMethod existingPaymentMethod = paymentMethodRepository.findByTypeAndNumber(paymentMethod.getType(), paymentMethod.getNumber());
      if (existingPaymentMethod == null) {
         paymentMethodRepository.save(paymentMethod);
      } else {
         paymentMethod = existingPaymentMethod;
      }

      hotelBooking.setPaymentMethod(paymentMethod);

      hotelBooking.setUserName(reservationDtoRequest.getUserName());
      selectedHotel.setReserved(true);
      hotelRepository.save(selectedHotel);

      hotelBookingRepository.save(hotelBooking);

      return new StatusDTO(200, "Reserva de hotel dada de alta correctamente");
   }

   private double calculateTotalPrice(Hotel hotel, LocalDate dateFrom, LocalDate dateTo) {
      long nights = dateFrom.until(dateTo).getDays();
      return hotel.getPricePerNight() * nights;
   }

   private double calculateInterest(double totalPrice, int dues, String type) {
      switch (type) {
         case "DEBIT":
            if (dues == 1) {
               return totalPrice;
            } else if(dues > 1){
               throw new InvalidDuesForDebit();
            }
         case "CREDIT":
            return calculateCreditInterest(totalPrice, dues);
         default:
            throw new IllegalArgumentException("Invalid payment type");
      }
   }

   private double calculateCreditInterest(double totalPrice, int dues) {
      if (dues >= 1 && dues <= 3) {
         return totalPrice * 1.05;
      } else if (dues > 3 && dues <= 6) {
         return totalPrice * 1.1;
      } else if (dues > 6 && dues <= 12) {
         return totalPrice * 1.15;
      } else {
         throw new InvalidDuesForCredit();
      }
   }

   @Override
   public Optional<?> numOfPeople(BoockingDto bookingDto) {
      // Validate people size and room type combination
      if (bookingDto.getPeopleAmount() == 1 && bookingDto.getRoomType().equals("SINGLE")) {
         return Optional.of(bookingDto.getPeople().size());
      } else if (bookingDto.getPeopleAmount() == 2 && bookingDto.getRoomType().equals("DOUBLE")) {
         return Optional.of(bookingDto.getPeople().size());
      } else if (bookingDto.getPeopleAmount() == 3 && bookingDto.getRoomType().equals("TRIPLE")) {
         return Optional.of(bookingDto.getPeopleAmount());
      } else {
         // Throw a more specific exception indicating the mismatch
         throw new InvalidBoockingException();
      }
   }
}


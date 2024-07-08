package com.example.Sprint1Equipo4.service;

import com.example.Sprint1Equipo4.dto.PeopleDto;
import com.example.Sprint1Equipo4.dto.request.BoockingDto;
import com.example.Sprint1Equipo4.dto.request.ReservationDtoRequest;
import com.example.Sprint1Equipo4.dto.response.StatusDTO;
import com.example.Sprint1Equipo4.exception.InvalidRequestException;
import com.example.Sprint1Equipo4.exception.ResourceNotFoundException;
import com.example.Sprint1Equipo4.model.HotelBooking;
import com.example.Sprint1Equipo4.model.PaymentMethod;
import com.example.Sprint1Equipo4.model.People;
import com.example.Sprint1Equipo4.repository.HotelBookingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HotelBookingServiceImpl implements HotelBookingService{

    @Autowired
    HotelBookingRepository bookingRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<BoockingDto> listAllHotelBookings() {
        return bookingRepository.findAll().stream()
                .map(hotelBooking -> modelMapper.map(hotelBooking, BoockingDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public StatusDTO updateHotelBooking(ReservationDtoRequest booking, Long id) {
        //Busco reserva por Id
       HotelBooking existingHotelBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException());

        //Valido booking
        if(booking.getBooking() ==null){
            throw new InvalidRequestException();
        }

        existingHotelBooking.setUserName(booking.getUserName());
        BoockingDto bookingDto = booking.getBooking();

        existingHotelBooking.setDateFrom(bookingDto.getDateFrom());
        existingHotelBooking.setDateTo(bookingDto.getDateTo());
        existingHotelBooking.setDestination(bookingDto.getDestination());
        existingHotelBooking.setHotelCode(bookingDto.getHotelCode());
        existingHotelBooking.setPeopleAmount(bookingDto.getPeopleAmount());
        existingHotelBooking.setRoomType(bookingDto.getRoomType());
        existingHotelBooking.setPeople(bookingDto.getPeople().stream()
                .map(peopleDto -> modelMapper.map(peopleDto, People.class))
                .collect(Collectors.toList()));
        existingHotelBooking.setPaymentMethod(modelMapper.map(bookingDto.getPayment(), PaymentMethod.class));

        // Guarda reserva actualizada
        bookingRepository.save(existingHotelBooking);

        return new StatusDTO();

    }

    @Override
    public StatusDTO deleteHotelBooking( Long id) {
        //Busco reserva por id
        HotelBooking existingHotelBooking = bookingRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException());

        //Elimino reserva
        bookingRepository.delete(existingHotelBooking);

        return new StatusDTO();
    }
}

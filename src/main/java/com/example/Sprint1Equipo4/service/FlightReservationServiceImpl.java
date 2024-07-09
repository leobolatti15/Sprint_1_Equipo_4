package com.example.Sprint1Equipo4.service;

import com.example.Sprint1Equipo4.dto.request.FlightReqDto;
import com.example.Sprint1Equipo4.dto.request.FlightReservationDto;
import com.example.Sprint1Equipo4.dto.response.StatusDTO;
import com.example.Sprint1Equipo4.exception.InvalidRequestException;
import com.example.Sprint1Equipo4.exception.ResourceNotFoundException;
import com.example.Sprint1Equipo4.model.FlightReservation;
import com.example.Sprint1Equipo4.model.PaymentMethod;
import com.example.Sprint1Equipo4.repository.FlightReservationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightReservationServiceImpl implements FlightReservationService{

    @Autowired
    FlightReservationRepository flightReservationRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<FlightReservationDto> listAllFlightReservations() {
        return flightReservationRepository.findAll().stream()
                .map(flightReservation -> modelMapper.map(flightReservation, FlightReservationDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public StatusDTO updateFlightReservation(FlightReqDto reservation, Long id) {
       FlightReservation existingFlightReservation = flightReservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException());

        //Valido booking
        if(reservation.getClass() ==null){
            throw new InvalidRequestException();
        }

        //SETEO DATOS

        existingFlightReservation.setUserName(reservation.getUserName());
        FlightReservationDto flightReservationDto = reservation.getFlightReservationDto();

        existingFlightReservation.setGoingDate(flightReservationDto.getDateFrom());
        existingFlightReservation.setReturnDate(flightReservationDto.getDateTo());
        existingFlightReservation.setDestination(flightReservationDto.getDestination());
        existingFlightReservation.setOrigin(flightReservationDto.getOrigin());
        existingFlightReservation.setFligthNumber(flightReservationDto.getFlightNumber());
        existingFlightReservation.setSeatType(flightReservationDto.getSeatType());
        existingFlightReservation.setPaymentMethod(modelMapper.map(flightReservationDto.getPaymentMethodsDto(), PaymentMethod.class));


/*
        existingFlightReservation.setGoingDate(flightReservationDto.getDateFrom());
        existingFlightReservation.setReturnDate(flightReservationDto.getDateTo());
        existingFlightReservation.setDestination(flightReservationDto.getDestination());
        existingFlightReservation.setOrigin(flightReservationDto.getOrigin());
        existingFlightReservation.setFligthNumber(flightReservationDto.getFlightNumber());
        existingFlightReservation.setSeatType(flightReservationDto.getSeatType());
        existingFlightReservation.set(flightReservationDto.getPeople().stream()
                .map(peopleDto -> modelMapper.map(peopleDto, People.class))
                .collect(Collectors.toList()));
        existingFlightReservation.setPaymentMethod(modelMapper.map(flightReservationDto.getPaymentMethodsDto(), PaymentMethod.class));
*/
        // Guarda reserva actualizada
        flightReservationRepository.save(existingFlightReservation);

        return new StatusDTO();

    }

    @Override
    public StatusDTO deleteFlightReservation(Long id) {
        //Busco reserva por id
        FlightReservation existingFlightReservation = flightReservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException());

        //Elimino reserva
        flightReservationRepository.delete(existingFlightReservation);

        return new StatusDTO();
    }
}

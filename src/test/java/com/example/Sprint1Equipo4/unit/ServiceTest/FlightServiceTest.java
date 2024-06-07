package com.example.Sprint1Equipo4.unit.ServiceTest;

import com.example.Sprint1Equipo4.dto.PeopleDto;
import com.example.Sprint1Equipo4.dto.request.FlightReqDto;
import com.example.Sprint1Equipo4.dto.request.FlightReservationDto;
import com.example.Sprint1Equipo4.dto.request.PaymentMethodsDto;
import com.example.Sprint1Equipo4.dto.response.FlightDTO;
import com.example.Sprint1Equipo4.dto.response.FlightResDto;
import com.example.Sprint1Equipo4.dto.response.StatusDTO;
import com.example.Sprint1Equipo4.model.Flight;
import com.example.Sprint1Equipo4.repository.FlightRepositoryImpl;
import com.example.Sprint1Equipo4.service.FlightService;
import com.example.Sprint1Equipo4.service.FlightServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FlightServiceTest {

    @Mock
    FlightRepositoryImpl flightRepository;
    @InjectMocks
    FlightServiceImpl flightService;

    @Mock
    ModelMapper modelMapper;


    private static final Flight vuelo1 = new Flight("PIBA-1420","Puerto Iguazu","Bogota",
            "Business",43200, LocalDate.of(2025,02,10),
            LocalDate.of(2025,02,20));

    private static final Flight vuelo2 = new Flight("PIBA-1421","Puerto Iguazu","Bogota",
            "Economy",25735, LocalDate.of(2025,02,10),
            LocalDate.of(2025,02,21));

    private static final FlightDTO vueloDto1 = new FlightDTO("PIBA-1420","Puerto Iguazu","Bogota",
            "Business",43200, LocalDate.of(2025,02,10),
            LocalDate.of(2025,02,20));

    private static final FlightDTO vueloDto2 = new FlightDTO("PIBA-1421","Puerto Iguazu","Bogota",
            "Economy",43200, LocalDate.of(2025,02,10),
            LocalDate.of(2025,02,21));

    private static final PeopleDto people1 = new PeopleDto("12345678","pepe","peras",
            "1990-05-25","pepe@peras.com");

    private static final PeopleDto people2 = new PeopleDto("12345678","pepa","peras",
            "1990-07-09","pepa@peras.com");

    private static final PaymentMethodsDto pay1 = new PaymentMethodsDto("CREDIT","1234-5678-9123-4567",3);

    private static final FlightReservationDto reservation = new FlightReservationDto(LocalDate.of(2025, 02,
            10),LocalDate.of(2025, 02, 20),"Puerto Iguazu","Bogota",
            "PIBA-1420",2,"Business", List.of(people1,people2),pay1);

    private static final FlightReqDto req = new FlightReqDto("pepe@peras.com",reservation);

    private static final FlightResDto res = new FlightResDto("pepe@peras.com",86400,95040.0,
            181440.0,reservation,new StatusDTO(201,"El proceso termino satisfactoriamente"));

    @Test
    public void testReserveOk(){
        // Crear objetos necesarios para el test
        PeopleDto people1 = new PeopleDto("12345678","pepe","peras",
                "1990-05-25","pepe@peras.com");
        PeopleDto people2 = new PeopleDto("12345678","pepa","peras",
                "1990-07-09","pepa@peras.com");
        PaymentMethodsDto pay1 = new PaymentMethodsDto("CREDIT","1234-5678-9123-4567",3);

        List<Flight> flightList = new ArrayList<>();
        Flight vuelo1 = new Flight("PIBA-1420","Puerto Iguazu","Bogota",
                "Business",43200, LocalDate.of(2025,02,10),
                LocalDate.of(2025,02,20));
        flightList.add(vuelo1);

        // Mockear el repositorio de vuelos
        when(flightRepository.findAll()).thenReturn(flightList);


        // Crear el objeto de reserva
        FlightReservationDto reservation = new FlightReservationDto(LocalDate.of(2025, 02,
                10),LocalDate.of(2025, 02, 20),"Puerto Iguazu","Bogota",
                "PIBA-1420",2,"Business", List.of(people1,people2),pay1);
        FlightReqDto req = new FlightReqDto("pepe@peras.com",reservation);

        FlightResDto reservaObtenida = flightService.reserve(req);

        assertEquals("El proceso termino satisfactoriamente", reservaObtenida.getStatusDTO().getMessage());
    }

}

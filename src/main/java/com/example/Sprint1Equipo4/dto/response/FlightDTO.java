package com.example.Sprint1Equipo4.dto.response;

import com.example.Sprint1Equipo4.model.Flight;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class FlightDTO {
    @JsonProperty("flight_number")
    private String flightNumber;
    private String origin;
    private String destination;
    @JsonProperty("seat_type")
    private String seatType;
    @JsonProperty("price_per_person")
    private Integer pricePerPerson;
    @JsonProperty("date_from")
    @JsonFormat(pattern ="dd/MM/yyyy")
    private LocalDate departureDate;
    @JsonProperty("date_to")
    @JsonFormat(pattern ="dd/MM/yyyy")
    private LocalDate returnDate;

}

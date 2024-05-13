package com.example.Sprint1Equipo4.dto.request;

import com.example.Sprint1Equipo4.dto.PeopleDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor @NoArgsConstructor
public class BoockingDto {

    @JsonProperty("date_from")
    private String dateFrom;
    @JsonProperty("date_to")
    private String dateTo;
    private String destination;
    @JsonProperty("hotel_code")
    private String hotelCode;
    @JsonProperty("people_amount")
    private Integer peopleAmount;
    @JsonProperty("room_type")
    private String roomType;
    private List<PeopleDto>people;
    @JsonProperty("payment_methods")
    private PaymentMethodsDto payment;

}
